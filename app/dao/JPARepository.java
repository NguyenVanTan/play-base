package dao;

import controllers.NoticeStatus;
import models.CNotice;
import models.CUserNotice;
import models.CUserNoticePK;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.*;
import javax.persistence.*;

import models.SUser;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPARepository implements Repository {

    private JPAApi jpaApi;
    private DatabaseExecutionContext executionContext;

    public static final int INSERT_TYPE = 1;
    public static final int UPDATE_TYPE = 2;
    public static final int DELETE_TYPE = 3;

    @Inject
    public JPARepository(JPAApi api, DatabaseExecutionContext executionContext) {
        this.jpaApi = api;
        this.executionContext = executionContext;
    }

    public CompletionStage<List<CNotice>> getNoticeReceived(Integer userId){
        return supplyAsync(() -> wrap(em -> fetchNoticeReceived(em, userId)), executionContext);
    }

    private List<CNotice> fetchNoticeReceived(EntityManager em, Integer userId) {
        String planTextQuery = String.format("select * from c_notices where notice_id in (select notice_id from c_user_notice where user_id = %d)", userId);
        return em.createNativeQuery(planTextQuery, CNotice.class)
                .getResultList();
    }

    public CompletionStage<CNotice> getNoticeById(Integer noticeId){
        return supplyAsync(() -> wrap(em -> fetchNoticeById(em, noticeId)), executionContext);
    };

    private CNotice fetchNoticeById(EntityManager em, Integer noticeId) {
        return em.createNamedQuery("CNotice.findNoticeById", CNotice.class)
                .setParameter(0, noticeId)
                .getSingleResult();
    }

    public CompletionStage<List<CNotice>> getCreatedNotices(Integer userId, Integer noticeType){
        return supplyAsync(() -> wrap(em -> fetchCreatedNoticeByUser(em, userId, noticeType)), executionContext);

    }

    private List<CNotice> fetchCreatedNoticeByUser(EntityManager em, Integer userId, Integer noticeType) {
        List<CNotice> notices = null;
        if (NoticeStatus.ALL.getStatusId() == noticeType ||
                NoticeStatus.UNDEFINED.getStatusId() == noticeType) {
            notices = em.createNamedQuery("CNotice.findAllCreatedNotice", CNotice.class)
                    .setParameter("createdById", userId)
                    .getResultList();
        } else {
            notices = em.createNamedQuery("CNotice.findCreatedNoticeByStatus", CNotice.class)
                    .setParameter(0, userId)
                    .setParameter(1, noticeType)
                    .getResultList();
        }

        if (notices != null) {
            notices.forEach(e -> {
                String sqlQuery = String.format("SELECT GROUP_CONCAT(user_id SEPARATOR ',') from play_core.c_user_notice where notice_id = %d group by notice_id", e.getNoticeId());
                String receiver = em.createNativeQuery(sqlQuery)
                        .getSingleResult().toString();
                e.setReceiver(receiver);
            });
        }

        return notices;
    }

    @Override
    public CompletionStage<List<SUser>> getAllUser(){
        return supplyAsync(() -> wrap(em -> fetchAllUser(em)), executionContext);
    }

    private List<SUser> fetchAllUser(EntityManager em){
        return em.createQuery("select p from SUser p", SUser.class)
                .getResultList();
    }

    @Override
    public CompletionStage<SUser> getUserByEmail(String email) {
        return supplyAsync(() -> wrap(em -> fetchUserByEmail(em, email)), executionContext);
    }

    private SUser fetchUserByEmail(EntityManager em, String email){
        return em.createQuery("select p from SUser p where p.email = ?", SUser.class)
                .setParameter(0, email)
                .getSingleResult();
    }

    @Override
    public CompletionStage<SUser> add(SUser user) {
        return supplyAsync(() -> wrap(em -> insert(em, user)), executionContext);
    }

    @Override
    public CompletionStage<SUser> update(SUser user) {
        return supplyAsync(() -> wrap(em -> update(em, user)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private SUser insert(EntityManager em, SUser user) {
        em.persist(user);
        return user;
    }

    private SUser update(EntityManager em, SUser user) {
        return em.merge(user);
    }

    public CompletionStage<String> saveNotice(CNotice notice, List<Integer> receiverIds, int type){
       return supplyAsync(() -> wrap(em -> saveNotice(em, notice, receiverIds, type)), executionContext);
    }

    private String saveNotice(EntityManager em, CNotice notice, List<Integer> receiverIds, int type) {
        switch (type) {
            case INSERT_TYPE:
                em.persist(notice);
                insertNoticeUser(em, notice.getNoticeId(), receiverIds);
                break;
            case UPDATE_TYPE:
                em.merge(notice);
                deleteNoticeUser(em, notice.getNoticeId());
                insertNoticeUser(em, notice.getNoticeId(), receiverIds);
                break;
            case DELETE_TYPE:
                deleteNoticeUser(em, notice.getNoticeId());
                deleteNotice(em, notice.getNoticeId());
                break;
        }
        return "OK";
    }

    private void insertNoticeUser(EntityManager em, int noticeId, List<Integer> receiverIds){
        Logger.of("application").debug("Insert data into CUserNotice table for notice_id " + noticeId);

        receiverIds.stream().forEach(e -> {
            CUserNoticePK id = new CUserNoticePK();
            id.setUserId(e);
            id.setNoticeId(noticeId);

            CUserNotice userNotice = new CUserNotice();
            userNotice.setId(id);
            userNotice.setStatus(NoticeStatus.UNREAD.getStatusId());

            em.persist(userNotice);
        });
    }

    private void deleteNoticeUser(EntityManager em, int noticeId){
        String deleteQuery = String.format("delete from c_user_notice where notice_id = %d", noticeId);
        Logger.of("application").debug(deleteQuery);
        em.createNativeQuery(deleteQuery).executeUpdate();
    }

    private void deleteNotice(EntityManager em, int noticeId){
        String deleteQuery = String.format("delete from c_notices where notice_id = %d", noticeId);
        Logger.of("application").debug(deleteQuery);
        em.createNativeQuery(deleteQuery).executeUpdate();
    }

}
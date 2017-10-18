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

    @Inject
    public JPARepository(JPAApi api, DatabaseExecutionContext executionContext) {
        this.jpaApi = api;
        this.executionContext = executionContext;
    }

    public CompletionStage<List<CNotice>> getCreatedNotices(Integer userId, Integer noticeType){
        return supplyAsync(() -> wrap(em -> fetchCreatedNoticeByUser(em, userId, noticeType)), executionContext);
    }

    public CompletionStage<List<CNotice>> getReceivedNotices(Integer userId){
        return null;
    }

    private List<CNotice> fetchCreatedNoticeByUser(EntityManager em, Integer userId, Integer noticeType){
        if(noticeType == -1){//ALL
            return em.createQuery("select p from CNotice p where p.createdBy = ? order by p.creationTime DESC", CNotice.class)
                    .setParameter(0, userId)
                    .getResultList();
        }

       return em.createQuery("select p from CNotice p where p.createdBy = ? and p.status = ? order by p.creationTime DESC", CNotice.class)
               .setParameter(0, userId)
               .setParameter(1, noticeType)
               .getResultList();
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

    public CompletionStage<String> saveNotice(CNotice notice, List<Integer> receiverIds){
       return supplyAsync(() -> wrap(em -> saveNotice(em, notice, receiverIds)), executionContext);
    }

    private String saveNotice(EntityManager em, CNotice notice, List<Integer> receiverIds){
        em.persist(notice);
        insertNoticeUser(em, notice.getNoticeId(), receiverIds);
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
        Logger.of("application").debug("Delete data into CUserNotice table for notice_id " + noticeId);
        em.createQuery("delete from CUserNotice where noticeId = ?")
                .setParameter(0, noticeId)
                .executeUpdate();
    }

}
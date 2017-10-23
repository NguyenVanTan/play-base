package dao;

import models.SRole;
import models.SUserRole;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class RoleJPARepository implements RoleRepository {
    private JPAApi jpaApi;
    private DatabaseExecutionContext executionContext;

    @Inject
    public RoleJPARepository(JPAApi api, DatabaseExecutionContext executionContext) {
        this.jpaApi = api;
        this.executionContext = executionContext;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    @Override
    public CompletionStage<SRole> add(SRole role) {
        return supplyAsync(() -> wrap(em -> insert(em, role)), executionContext);
    }

    @Override
    public CompletionStage<SRole> update(SRole role) {
        return supplyAsync(() -> wrap(em -> update(em, role)), executionContext);
    }

    @Override
    public CompletionStage<List<SRole>> getAllRole() {
        return supplyAsync(() -> wrap(em -> fetchAllRole(em)), executionContext);
    }

    @Override
    public CompletionStage<SRole> getRoleById(int roleId) {
        return supplyAsync(() -> wrap(em -> fetchRoleById(em, roleId)), executionContext);
    }

    @Override
    public CompletionStage<Integer> deleteRoleByIds(String roleIds) {
        return supplyAsync(() -> wrap(em -> deleteRoleByIds(em, roleIds)), executionContext);
    }

    @Override
    public CompletionStage<Integer> deleteUserRoleByUserId(int userId) {
        return supplyAsync(() -> wrap(em -> deleteUserRoleByUserId(em, userId)), executionContext);
    }

    @Override
    public CompletionStage<SUserRole> add(SUserRole userRole) {
        return supplyAsync(() -> wrap(em -> insert(em, userRole)), executionContext);
    }

    @Override
    public CompletionStage<SUserRole> getUserRoleByUserId(int userId) {
        return supplyAsync(() -> wrap(em -> getUserRoleByUserId(em, userId)), executionContext);
    }

    private SRole insert(EntityManager em, SRole role) {
        em.persist(role);
        return role;
    }

    private SRole update(EntityManager em, SRole role) {
        em.merge(role);
        return role;
    }

    private List<SRole> fetchAllRole(EntityManager em){
        return em.createQuery("select p from SRole p", SRole.class)
                .getResultList();
    }

    private SRole fetchRoleById(EntityManager em, int roleId){
        return em.createQuery("select p from SRole p where p.roleId = ?", SRole.class)
                .setParameter(0, roleId)
                .getSingleResult();
    }

    private int deleteRoleByIds(EntityManager em, String roleIds){
        return em.createNativeQuery(String.format("delete from s_roles where role_id in (%s)", roleIds))
                .executeUpdate();
    }

    private int deleteUserRoleByUserId(EntityManager em, int userId){
        return em.createNativeQuery("delete from s_user_roles where user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    private SUserRole insert(EntityManager em, SUserRole userRole) {
        em.persist(userRole);
        return userRole;
    }

    private SUserRole getUserRoleByUserId(EntityManager em, int userId){
        return em.createQuery("select p from SUserRole p where p.id.userId = ?", SUserRole.class)
                .setParameter(0, userId)
                .getSingleResult();
    }
}

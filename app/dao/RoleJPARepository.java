package dao;

import models.SRole;
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
        return em.createNativeQuery("delete from s_roles where role_id in " + roleIds)
                .executeUpdate();
    }
}

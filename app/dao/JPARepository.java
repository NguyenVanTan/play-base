package dao;

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
        em.merge(user);
        return user;
    }

}
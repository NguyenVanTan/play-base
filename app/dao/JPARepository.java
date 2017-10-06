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
    public CompletionStage<SUser> add(SUser user) {
        return supplyAsync(() -> wrap(em -> insert(em, user)), executionContext);
    }

    @Override
    public CompletionStage<Stream<SUser>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private SUser insert(EntityManager em, SUser user) {
        em.persist(user);
        return user;
    }

    private Stream<SUser> list(EntityManager em) {
        List<SUser> persons = em.createQuery("select p from User p", SUser.class).getResultList();
        return persons.stream();
    }
}
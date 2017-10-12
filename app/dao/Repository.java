package dao;

import com.google.inject.ImplementedBy;

import models.SUser;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPARepository.class)
public interface Repository {

    CompletionStage<SUser> add(SUser user);

    CompletionStage<List<SUser>> getAllUser();

    CompletionStage<SUser> getUserByEmail(String email);

}

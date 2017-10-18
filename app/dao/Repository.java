package dao;

import com.google.inject.ImplementedBy;
import models.CNotice;
import models.SUser;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPARepository.class)
public interface Repository {

    CompletionStage<SUser> add(SUser user);

    CompletionStage<SUser> update(SUser user);

    CompletionStage<List<SUser>> getAllUser();

    CompletionStage<SUser> getUserByEmail(String email);

    CompletionStage<List<CNotice>> getCreatedNotices(Integer userId, Integer noticeType);

    CompletionStage<List<CNotice>> getReceivedNotices(Integer userId);

    CompletionStage<String> saveNotice(CNotice notice, List<Integer> receiverIds);

    CompletionStage<Integer> deleteUserByIds(String userIds);

}

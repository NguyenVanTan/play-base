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

    CompletionStage<SUser> addUser(SUser user);

    CompletionStage<SUser> updateUser(SUser user);

    CompletionStage<List<SUser>> getAllUser();

    CompletionStage<SUser> getUserByEmail(String email);

    CompletionStage<List<CNotice>> getCreatedNotices(Integer userId, Integer noticeType);

    CompletionStage<String> saveNotice(CNotice notice, List<Integer> receiverIds, int type);

    CompletionStage<CNotice> getNoticeById(Integer noticeId);

    CompletionStage<List<CNotice>> getNoticeReceived(Integer userId);

    CompletionStage<Integer> deleteUserByIds(String userIds);

}

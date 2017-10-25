import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import javax.inject.Inject;

import akka.stream.Materializer;
import dao.Repository;
import dao.RoleRepository;
import models.SUser;
import play.Logger;
import play.mvc.*;
import scala.collection.JavaConversions;

public class LoggingFilter extends Filter {
    private final Repository repository;
    private final RoleRepository roleRepository;

    @Inject
    public LoggingFilter(Materializer mat, RoleRepository roleRepository, Repository repository) {
        super(mat);
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public CompletionStage<Result> apply(
            Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
            Http.RequestHeader requestHeader) {
        long startTime = System.currentTimeMillis();
        return nextFilter.apply(requestHeader).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;

            Logger.info("{} {} took {}ms and returned {}",
                    requestHeader.method(), requestHeader.uri(), requestTime, result.status());
            Http.Session session = new Http.Session(JavaConversions.mapAsJavaMap(requestHeader._underlyingHeader().session().data()));


            String role = "";
            try {
                String email = session.get("email");
                if(email != null && email.length() > 0){
                    SUser user = repository.getUserByEmail(email).toCompletableFuture().get();
                    int roleId = roleRepository.getUserRoleByUserId(user.getId()).toCompletableFuture().get().getId().getRoleId();
                    role = roleRepository.getRoleById(roleId).toCompletableFuture().get().getRoleName();
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }

            return result.withHeader("XXXRequest-Time", session.get("email") + " XXX " + requestTime)
                    .withHeader("XXXRole", role)
                    .withHeader("XXXQueryString", requestHeader.uri());
        });
    }
}
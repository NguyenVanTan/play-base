package controllers;

import dao.Repository;
import models.Login;
import models.SUser;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Int;

import javax.inject.Inject;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class AppController extends Controller {

    private final Repository repository;
    private final FormFactory formFactory;

    @Inject
    public AppController(Repository repository, FormFactory formFactory){
        this.repository = repository;
        this.formFactory = formFactory;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result dashboard() {
        return ok(views.html.dashboard.render(session().get("userType"), session().get("userName")));
    }

    public Result profile() {
        try {
            SUser user = repository.getUserByEmail(session("email")).toCompletableFuture().get();
            return ok(views.html.profile.render(user, formFactory.form(SUser.class)));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("User not found");
        }
    }

    public Result profile_save() {
        return ok("OK");
        //return ok(views.html.profile.render(session().get("userType"), session().get("userName")));
    }

    public Result management_user() {
        try {
            List<SUser> userList = repository.getAllUser().toCompletableFuture().get();
            return ok(views.html.users.render(userList, session().get("userType"), session().get("userName")));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("User list not found");
        }
    }

    public Result remove_user(Int userId) {
        return ok("OK");
    }

}

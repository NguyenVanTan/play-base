package controllers;

import dao.Repository;
import models.Login;
import models.SUser;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Int;

import javax.inject.Inject;
import java.util.Calendar;
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
            Form<SUser> profileForm = formFactory.form(SUser.class);
            return ok(views.html.profile.render(user, profileForm.fill(user)));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("User not found");
        }
    }

    @RequireCSRFCheck
    public Result profile_save() {
        SUser currentUser = null;
        try {
            currentUser = repository.getUserByEmail(session("email")).toCompletableFuture().get();
        } catch (Exception e) {
            return notFound("User not found");
        }

        DynamicForm requestData = formFactory.form().bindFromRequest();
        String confirmPassword = requestData.get("confirmPassword");

        Form<SUser> boundForm = formFactory.form(SUser.class, SUser.Update.class).bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(views.html.profile.render(currentUser, boundForm));
        }

        SUser user = boundForm.get();

        if (!user.getPassword().equals(confirmPassword)) {
            flash("error", "Please correct the confirm password");
            return badRequest(views.html.profile.render(currentUser, boundForm));
        }

        currentUser.setName(user.getName());
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            currentUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        currentUser.setMobile(user.getMobile());
        currentUser.setGender(user.getGender());

        repository.update(currentUser);
        flash("success", String.format("Successfully update user %s", user.getEmail()));
        Form<SUser> profileForm = formFactory.form(SUser.class);
        return ok(views.html.profile.render(currentUser, profileForm.fill(currentUser)));
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

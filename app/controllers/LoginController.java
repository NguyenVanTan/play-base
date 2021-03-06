package controllers;

import dao.Repository;
import models.Login;
import models.SUser;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class LoginController extends Controller {

    private final Repository repository;
    private Form<Login> loginForm;
    private FormFactory formFactory;
    private Form<SUser> registerForm;

    @Inject
    public LoginController(Repository repository, FormFactory formFactory) {
        this.repository = repository;
        this.loginForm = formFactory.form(Login.class);

        this.formFactory = formFactory;
        this.registerForm = formFactory.form(SUser.class, SUser.Register.class);
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result login() {
        return ok(views.html.login.render(loginForm));
    }

    public Result logout() {
        String userName = session().get("userName");
        session().clear();
        return ok(views.html.logout.render(userName));
    }

    @RequireCSRFCheck
    public Result authenticate() {
        Form<Login> requestForm = loginForm.bindFromRequest();
        if(requestForm.hasErrors()){
            return badRequest(views.html.login.render(requestForm));
        }

        String email = requestForm.get().getEmail();
        String password = requestForm.get().getPassword();

        Logger.of("application").info("EMAIL: " + email);
        Logger.of("application").info("PASS: " + password);

        SUser user = null;
        try {
            user = repository.getUserByEmail(email).toCompletableFuture().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user == null) {
            flash("error", "Email not found");
            return ok(views.html.login.render(requestForm));
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            flash("error", "Password is invalid");
            return ok(views.html.login.render(requestForm));
        }

        session().clear();
        session("email", email);
        session("userId", String.valueOf(user.getId()));
        session("userType", String.valueOf(user.getType()));
        session("userName", user.getName());

        return redirect(routes.AppController.dashboard());
    }

    public Result newRegister() {
        return ok(views.html.register.render(registerForm));
    }

    @RequireCSRFCheck
    public Result register() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String confirmPassword = requestData.get("confirmPassword");

        Form<SUser> boundForm = registerForm.bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(views.html.register.render(boundForm));
        }

        SUser user = boundForm.get();

        if (!user.getPassword().equals(confirmPassword)) {
            flash("error", "Please correct the confirm password");
            return badRequest(views.html.register.render(boundForm));
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setCreationDate(Calendar.getInstance().getTime());
        user.setCreationBy("Manual Registration");
        user.setType(2);

        repository.addUser(user);
        flash("success", String.format("Successfully register user %s", user.getEmail()));
        return redirect(routes.LoginController.login());
    }

}

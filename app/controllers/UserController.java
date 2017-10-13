package controllers;

import dao.Repository;
import models.SUser;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Calendar;

public class UserController extends Controller {
    private FormFactory formFactory;
    private Form<SUser> registerForm;
    private final Repository repository;

    @Inject
    public UserController(FormFactory formFactory, Repository repository) {
        this.repository = repository;
        this.formFactory = formFactory;
        registerForm = formFactory.form(SUser.class);
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

        repository.add(user);
        flash("success", String.format("Successfully register user %s", user.getEmail()));
        return redirect(routes.LoginController.login());
    }
}

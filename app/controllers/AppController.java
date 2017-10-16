package controllers;

import dao.Repository;
import dao.RoleRepository;
import models.Login;
import models.SRole;
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
import java.util.concurrent.ExecutionException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class AppController extends Controller {

    private final Repository repository;
    private final RoleRepository roleRepository;
    private final FormFactory formFactory;

    @Inject
    public AppController(Repository repository, RoleRepository roleRepository, FormFactory formFactory){
        this.repository = repository;
        this.roleRepository = roleRepository;
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

    public Result newRole() {
        try {
            SUser user = repository.getUserByEmail(session("email")).toCompletableFuture().get();
            Form<SRole> newRoleForm = formFactory.form(SRole.class);
            return ok(views.html.roleDetail.render(user, newRoleForm));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("User not found");
        }
    }

    public Result roleDetail(int roleId) {
        SUser currentUser = null;
        try {
            currentUser = repository.getUserByEmail(session("email")).toCompletableFuture().get();
        } catch (Exception e) {
            return notFound("User not found");
        }

        SRole role = null;
        try {
            role = roleRepository.getRoleById(roleId).toCompletableFuture().get();
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("Role not found");
        }
        Form<SRole> detailRoleForm = formFactory.form(SRole.class);

        return ok(views.html.roleDetail.render(currentUser, detailRoleForm.fill(role)));
    }

    public Result saveRole() {
        SUser currentUser = null;
        try {
            currentUser = repository.getUserByEmail(session("email")).toCompletableFuture().get();
        } catch (Exception e) {
            return notFound("User not found");
        }

        Form<SRole> boundForm = formFactory.form(SRole.class).bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(views.html.roleDetail.render(currentUser, boundForm));
        }

        SRole role = boundForm.get();

        SRole checkRole = null;
        try {
            checkRole = roleRepository.getRoleById(role.getRoleId()).toCompletableFuture().get();
        } catch (Exception e) {
            checkRole = null;
        }

        if (checkRole != null) {
            checkRole.setRoleName(role.getRoleName());
            checkRole.setRoleDesc(role.getRoleDesc());
            roleRepository.update(role);
            flash("success", String.format("Successfully update role %s", role.getRoleName()));
            return ok(views.html.roleDetail.render(currentUser, boundForm));
        } else {
            roleRepository.add(role);
            flash("success", String.format("Successfully add role %s", role.getRoleName()));
            return redirect(routes.AppController.management_role());
        }
    }

    public Result management_role() {
        try {
            List<SRole> roleList = roleRepository.getAllRole().toCompletableFuture().get();
            return ok(views.html.roleList.render(roleList, session().get("userType"), session().get("userName")));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("Role list not found");
        }
    }
}
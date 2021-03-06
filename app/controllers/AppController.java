package controllers;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;
import dao.JPARepository;
import dao.Repository;
import dao.RoleRepository;
import models.*;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.MailerService;

import javax.inject.Inject;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class AppController extends Controller {

    private final Repository repository;
    private final RoleRepository roleRepository;
    private final FormFactory formFactory;
    private final MailerService mailerService;

    @Inject
    public AppController(Repository repository, RoleRepository roleRepository, FormFactory formFactory, MailerService mailerService){
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.formFactory = formFactory;
        this.mailerService = mailerService;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result dashboard() {
        try {
            DynamicForm requestData = formFactory.form().bindFromRequest();

            String filterType = requestData.get("filter");
            Integer noticeType = Optional.of(NoticeStatus.valueOfName(filterType)).map(e -> e.getStatusId()).orElse(NoticeStatus.ALL.getStatusId());

            Integer userId = Integer.parseInt(session("userId"));
            List<CNotice> noticeList = repository.getCreatedNotices(userId, noticeType).toCompletableFuture().get();

            Logger.of("application").info("Dashboard {" +
                    "noticeType: " + noticeType +
                    ", filterType: " + filterType +
                    ", noticeSize: " + noticeList.size() + "}");

            List<SUser> userList = repository.getAllUser()
                    .toCompletableFuture()
                    .get()
                    .stream()
                    .filter(e -> e.getId() != userId)
                    .collect(Collectors.toList());

            return ok(views.html.dashboard.render(filterType, noticeList, userList, formFactory.form(CNotice.class)));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("An unexpected error");
        }
    }

    public Result dashboard_save() {
        CNotice notice = null;
        DynamicForm requestData = formFactory.form().bindFromRequest();
        int saveType = Integer.parseInt(requestData.get("saveType"));
        Logger.of("application").info("Perform dashboard_save action with type: " + saveType);
        try {
            if(saveType == JPARepository.INSERT_TYPE){
                notice = new CNotice();
                notice.setNoticeMessage(requestData.get("noticeMessage"));
                notice.setStatus(Integer.parseInt(requestData.get("noticeStatus")));
                notice.setReceiver(requestData.get("receiverIds"));
                notice.setCreatedBy(Integer.parseInt(session("userId")));
                notice.setCreationTime(new Date(System.currentTimeMillis()));
                if (notice.getNoticeMessage().isEmpty() || notice.getReceiver().isEmpty()) {
                    flash("error", "Please enter notice content and receiver !");
                    return redirect(routes.AppController.dashboard());
                }
            } else if(saveType == JPARepository.UPDATE_TYPE){
                notice = repository.getNoticeById(Integer.parseInt(requestData.get("noticeId"))).toCompletableFuture().get();
                notice.setNoticeMessage(requestData.get("noticeMessage"));
                notice.setStatus(Integer.parseInt(requestData.get("noticeStatus")));
                notice.setReceiver(requestData.get("receiverIds"));
                notice.setUpdateTime(new Date(System.currentTimeMillis()));
                if (notice.getNoticeMessage().isEmpty() || notice.getReceiver().isEmpty()) {
                    flash("error", "Please enter notice content and receiver !");
                    return redirect(routes.AppController.dashboard());
                }
            }  else if(saveType == JPARepository.DELETE_TYPE){
                notice = new CNotice();
                notice.setNoticeId(Integer.parseInt(requestData.get("noticeId")));
            }

            Logger.of("application").info(notice.toString());
            List<Integer> receiverIdList = splitToList(notice.getReceiver(), ",");
            repository.saveNotice(notice, receiverIdList, saveType).toCompletableFuture().get();
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("An unexpected error");
        }

        return redirect(routes.AppController.dashboard());
    }

    private List<Integer> splitToList(String content, String separator) {
        if(content == null){
            return Lists.newArrayList();
        }
        return Arrays.asList(content.split(separator))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
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

        repository.updateUser(currentUser);
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

    public Result userDetail(String email) {
        try {
            SUser user = repository.getUserByEmail(email).toCompletableFuture().get();

            List<SRole> listRole = roleRepository.getAllRole().toCompletableFuture().get();

            String selectedRole = "";
            try {
                SUserRole userRole = roleRepository.getUserRoleByUserId(user.getId()).toCompletableFuture().get();
                selectedRole = userRole == null ? "" : String.valueOf(userRole.getId().getRoleId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Form<SUser> profileForm = formFactory.form(SUser.class);
            return ok(views.html.userDetail.render(user, profileForm.fill(user), listRole, selectedRole));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("User not found");
        }
    }

    @RequireCSRFCheck
    public Result user_save() {
        SUser currentUser = null;
        List<SRole> listRole;
        try {
            currentUser = repository.getUserByEmail(session("email")).toCompletableFuture().get();
            listRole = roleRepository.getAllRole().toCompletableFuture().get();
        } catch (Exception e) {
            return notFound("User not found");
        }

        DynamicForm requestData = formFactory.form().bindFromRequest();
        String confirmPassword = requestData.get("confirmPassword");

        Form<SUser> boundForm = formFactory.form(SUser.class, SUser.Update.class).bindFromRequest();
        if (boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(views.html.userDetail.render(currentUser, boundForm, listRole, null));
        }

        SUser user = boundForm.get();

        if (!user.getPassword().equals(confirmPassword)) {
            flash("error", "Please correct the confirm password");
            return badRequest(views.html.userDetail.render(currentUser, boundForm, listRole, null));
        }

        SUser updateUser = null;

        try {
            updateUser = repository.getUserByEmail(user.getEmail()).toCompletableFuture().get();
        } catch (Exception e) {
            return notFound("User not found");
        }

        updateUser.setName(user.getName());
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            updateUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }
        updateUser.setMobile(user.getMobile());
        updateUser.setGender(user.getGender());

        repository.updateUser(updateUser);

        String role = requestData.get("role");
        if (role != null && !"".equals(role)) {
            try {
                roleRepository.deleteUserRoleByUserId(updateUser.getId()).toCompletableFuture().get();
                SUserRole userRole = new SUserRole();
                SUserRolePK pk = new SUserRolePK();
                pk.setUserId(updateUser.getId());
                pk.setRoleId(Integer.valueOf(role));
                userRole.setId(pk);
                roleRepository.add(userRole);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            roleRepository.deleteUserRoleByUserId(updateUser.getId());
        }

        flash("success", String.format("Successfully update user %s", updateUser.getEmail()));

        Form<SUser> profileForm = formFactory.form(SUser.class);
        return ok(views.html.userDetail.render(currentUser, profileForm.fill(updateUser), listRole, role));
    }

    public Result deleteUsers() {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String[] checkedVal = map.get("checked");

        if (checkedVal == null) {
            flash("error", "Please check user for delete!");
            return redirect(routes.AppController.management_user());
        }

        try {
            repository.deleteUserByIds(String.join(",", checkedVal)).toCompletableFuture().get();
            flash("success", "Successful delete!");
        } catch (Exception e) {
            flash("error", "Cannot delete!");
        }

        return redirect(routes.AppController.management_user());
    }

    public Result inbox(){
        try {
            List<CNotice> noticeList = repository.getNoticeReceived(Integer.parseInt(session("userId"))).toCompletableFuture().get();
            return ok(views.html.inbox.render(noticeList));
        } catch (Exception e) {
            e.printStackTrace();
            return notFound("An unexpected error");
        }
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
            try {
                roleRepository.update(role).toCompletableFuture().get();
            } catch (Exception e) {
                flash("error", String.format("Error update role %s", role.getRoleName()));
            }
            flash("success", String.format("Successfully update role %s", role.getRoleName()));
            return ok(views.html.roleDetail.render(currentUser, boundForm));
        } else {
            try {
                roleRepository.add(role).toCompletableFuture().get();
            } catch (Exception e) {
                flash("error", String.format("Error add role %s", role.getRoleName()));
            }
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

    public Result deleteRoles() {
        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String[] checkedVal = map.get("checked");

        if (checkedVal == null) {
            flash("error", "Please check role for delete!");
            return redirect(routes.AppController.management_role());
        }

        try {
            roleRepository.deleteRoleByIds( String.join(",", checkedVal)).toCompletableFuture().get();
            flash("success", "Successful delete!");
        } catch (Exception e) {
            flash("error", "Cannot delete!");
        }

        return redirect(routes.AppController.management_role());
    }

    public Result initUploadListRole() {
        SUser currentUser = null;
        try {
            currentUser = repository.getUserByEmail(session("email")).toCompletableFuture().get();
        } catch (Exception e) {
            return notFound("User not found");
        }
        return ok(views.html.uploadListRole.render(currentUser));
    }

    @RequireCSRFCheck
    public Result uploadListRole() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> uploadFile = body.getFile("uploadFile");
        if (uploadFile != null) {
            String fileName = uploadFile.getFilename();
            String contentType = uploadFile.getContentType();
            File file = uploadFile.getFile();
            Logger.of("application").info("Uploaded file:" + fileName + " with content type: " + contentType) ;

            if (!fileName.endsWith(".csv")) {
                flash("error", "Please choose .csv file");
                return redirect(routes.AppController.initUploadListRole());
            }

            try {
                CSVReader csvReader = new CSVReader(new FileReader(file));
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Logger.of("application").info("Role [name= " + line[0] + ", desc= " + line[1] + "]");
                    SRole role = new SRole();
                    role.setRoleName(line[0]);
                    role.setRoleDesc(line[1]);
                    roleRepository.add(role);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mailerService.sendEmail(fileName, file);

            flash("success", "Successfully upload list role");
            return redirect(routes.AppController.initUploadListRole());
        } else {
            flash("error", "Missing file");
            return redirect(routes.AppController.initUploadListRole());
        }
    }
}
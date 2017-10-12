package controllers;

import dao.Repository;
import models.SUser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Security.Authenticated(Secured.class)
public class AppController extends Controller {

    private final Repository repository;

    @Inject
    public AppController(Repository repository) {
        this.repository = repository;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result management() {
        List<SUser> userList = new ArrayList<>();
        try {
            userList = repository.getAllUser().toCompletableFuture().get();
        } catch(Exception e){
            e.printStackTrace();
        }
        return ok(views.html.userlist.render(userList));
    }

    public Result userlist(){
        List<SUser> userList = new ArrayList<>();
        try {
           userList = repository.getAllUser().toCompletableFuture().get();
        } catch(Exception e){
            e.printStackTrace();
        }
        return ok(views.html.userlist.render(userList));
    }

    public Result removeUser(Integer userId){
        return ok("OK");
    }

}

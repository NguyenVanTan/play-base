package controllers;

import dao.Repository;
import models.SUser;
import play.mvc.*;
import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	private final Repository repository;
	
	@Inject
    public HomeController(Repository repository) {
        this.repository = repository;
    }
	
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result login() {
    	SUser user = new SUser();
    	user.setEmail("vuta0512@gmail.com");
    	user.setPassword("123456");
    	user.setName("vuta0512");
    	user.setMobile("0936544434");
    	repository.add(user);
        return ok(views.html.login.render());
    }
    
    public Result register() {
        return ok(views.html.register.render());
    }

}

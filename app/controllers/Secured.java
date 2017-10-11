package controllers;

import com.mysql.jdbc.StringUtils;
import play.Logger;
import play.Play;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Date;

public class Secured extends Security.Authenticator {

    private final String REQUEST_BEGIN_TIME = "beginTime";

    @Override
    public String getUsername(Context ctx) {
        String email = ctx.session().get("email");
//        Logger.of("application").debug("EMAIL: " + email);
        // see if user is logged in
        if (email == null || email.isEmpty()) {
            return null;
        }

        // see if the session is expired
        String previousTick = ctx.session().get(REQUEST_BEGIN_TIME);
        if (!StringUtils.isNullOrEmpty(previousTick)) {
            long previousT = Long.valueOf(previousTick);
            long currentT = new Date().getTime();
            long timeout = Long.valueOf(Play.application().configuration().getString("sessionTimeout")) * 60 * 1000;
            if ((currentT - previousT) > timeout) {
                // session expired
                ctx.session().clear();
                return null;
            }
        }

        // update time in session
        ctx.session().put(REQUEST_BEGIN_TIME, Long.toString(new Date().getTime()));

        return email;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.LoginController.login());
    }
}

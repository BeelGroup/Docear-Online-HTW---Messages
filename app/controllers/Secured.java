package controllers;

import play.Logger;
import play.mvc.*;
import play.mvc.Http.*;

public class Secured extends Security.Authenticator {
    public static final String SESSION_KEY_USERNAME = "username";

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get(SESSION_KEY_USERNAME);
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.flash().put("error", "You need to authenticate.");
        return redirect(routes.ControllerFactory.user.loginForm());
    }
}
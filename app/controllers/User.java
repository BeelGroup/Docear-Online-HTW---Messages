package controllers;

import models.frontend.Credentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.backend.user.UserService;

@Component
public class User extends Controller {

    private static final Form<Credentials> credentialsForm = form(Credentials.class);

	@Autowired
    private UserService userService;
	
	public Result login() {
        final Form<Credentials> filledForm = credentialsForm.bindFromRequest();
        Result result;

        if (filledForm.hasErrors()) {
            result = badRequest(views.html.user.loginForm.render(filledForm));
        } else {
            final Credentials credentials = filledForm.get();
            final String accessToken = userService.authenticate(credentials.getUsername(), credentials.getPassword());
            final boolean authenticationSuccessful = accessToken != null;
            if (authenticationSuccessful) {
                setAuthenticatedSession(credentials, accessToken);
                result = redirect(routes.Application.index());
            } else {
                filledForm.reject("The credentials doesn't match any user.");
                Logger.debug(credentials.getUsername() + " is unauthorized");
                result = unauthorized(views.html.user.loginForm.render(filledForm));
            }
        }
        return result;
    }

    private void setAuthenticatedSession(Credentials credentials, String accessToken) {
        final String sessionId = Session.createSession(credentials.getUsername(), accessToken);
        session(Application.getSessionCookieName(), sessionId);
        session("username", credentials.getUsername());
    }

    public Result loginForm() {
        return ok(views.html.user.loginForm.render(credentialsForm));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.Application.index());
    }

    public Result profile() {
        return redirect(routes.ControllerFactory.mindMap.mapListFromDB());
    }
	
	/**
	 * 
	 * @return User or null if non is logged-in
	 */
    public static models.backend.User getCurrentUser() {
        final String authToken = session(Application.getSessionCookieName());
        return Session.getUserForSessionId(authToken);
    }
	
}

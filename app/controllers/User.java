package controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.backend.user.UserService;

@Component
public class User extends Controller {

	@Autowired
    private UserService userService;
	
	public Result login() {
		Map<String,String[]> postMap = request().body().asFormUrlEncoded();

		String[] userNameArray = postMap.get("username");
		String[] pwArray = postMap.get("password");

		if(userNameArray == null || userNameArray[0].isEmpty() || pwArray == null || pwArray[0].isEmpty())
			return badRequest("please define 'username' and 'password'");


		final String username  = userNameArray[0];
		final String password = pwArray[0];
		Logger.debug(username +";"+password);

		String accessToken = userService.authenticate(username, password);


		if(accessToken != null) { //succesful authentication
			String sessionId = Session.createSession(username, accessToken);
			response().setCookie(Application.getSessionCookieName(), sessionId);
			return redirect(routes.Application.index());
		} else {
			return unauthorized("Authentication failed");
		}

		
	}
	
}

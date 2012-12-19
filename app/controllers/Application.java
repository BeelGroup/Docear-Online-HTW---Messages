package controllers;

import java.util.Map;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;
import backend.User;

public class Application extends Controller {





	/** displays current mind map drawing */
	public static Result index() {
		return ok(views.html.index.render());
	}

	/** for evolving mvc structure on client side */
	public static Result mvc() {
		return ok(views.html.mvc.render());
	}

	public static Result smallSolutions() {
		return ok(views.html.smallSolutions.render("Solutions"));
	}

	public static Result login() {

		Map<String,String[]> postMap = request().body().asFormUrlEncoded();

		String[] userNameArray = postMap.get("username");
		String[] pwArray = postMap.get("password");

		if(userNameArray == null || userNameArray[0].isEmpty() || pwArray == null || pwArray[0].isEmpty())
			return badRequest("please define 'username' and 'password'");


		final String username  = userNameArray[0];
		final String password = pwArray[0];
		Logger.debug(username +";"+password);

		//		MultipartEntity me =  new MultipartEntity();
		//		me.addPart("password", new StringBody(password));
		//		
		//		final Map<String,String> map = new HashMap<String, String>();
		//		map.put("password", password);
		//		
		//		BufferedOutputStream out = new BufferedOutputStream(new ByteOutputStream());
		//		me.writeTo(out);

		Response response = WS.url("https://api.docear.org/authenticate/"+username)
				.post("password="+password).get();


		if(response.getStatus() == 200) { //succesful authentication
			String accessToken = response.getHeader("accessToken");
			String sessionId = Session.createSession(username, accessToken);
			response().setCookie(getSessionCookieName(), sessionId);
			return redirect(routes.Application.index());
		} else {
			return unauthorized("Authentication failed");
		}
	}

	private static String getSessionCookieName() {
		return Play.application().configuration().getString("backend.sessionIdName");
	}
	
	public static User getCurrentUser() {
		String name = getSessionCookieName();
		String sessionId = request().cookies().get(getSessionCookieName()).value();
		
			
		Logger.debug(getSessionCookieName());
		Logger.debug(sessionId);
		return Session.getUserForSessionId(sessionId);
	}
}
package controllers;

import java.util.Map;

import play.Logger;
import play.Play;
import play.mvc.Http;
import util.backend.WS;
import util.backend.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;
import models.backend.User;

public class Application extends Controller {





	/** displays current mind map drawing */
	public static Result index() {
		return ok(views.html.index.render());
	}

    /** displays a feature list and help site */
	public static Result help() {
		return ok(views.html.help.render());
	}

	/** for evolving mvc structure on client side */
	public static Result mvc() {
		return ok(views.html.mvc.render());
	}

	public static Result smallSolutions() {
		return ok(views.html.smallSolutions.render("Solutions"));
	}

	public static String getSessionCookieName() {
		return Play.application().configuration().getString("backend.sessionIdName");
	}

    public static User getCurrentUser() {
        Http.Cookie cookie = request().cookies().get(getSessionCookieName());
        if(cookie != null) {
            String sessionId = cookie.value();
            return Session.getUserForSessionId(sessionId);
        } else {
            return null;
        }
    }
}
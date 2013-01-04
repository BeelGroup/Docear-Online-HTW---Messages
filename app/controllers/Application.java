package controllers;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

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
}
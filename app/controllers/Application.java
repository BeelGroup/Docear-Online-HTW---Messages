package controllers;

import play.mvc.Controller;
import play.mvc.Result;

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
}
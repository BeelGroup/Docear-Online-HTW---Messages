package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.smallSolutions;

public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render("Your new application is ready."));
  }

  public static Result smallSolutions() {
    return ok(smallSolutions.render("Solutions"));
  }

}
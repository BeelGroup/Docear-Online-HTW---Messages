package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class QUnit extends Controller {

    public static Result testAll() {
        return ok(views.html.qunit.test.render());
    }
}

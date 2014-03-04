package controllers;

import models.Target;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.targets.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class TargetEdit extends Controller {
  
    /**
     * Display the target.
     */
    public static Result index() {
        return ok(
        );
    }
}


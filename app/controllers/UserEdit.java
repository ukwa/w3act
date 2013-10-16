package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;

import views.html.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class UserEdit extends Controller {
  
    /**
     * Display the targets.
     */
    public static Result index() {
        return ok(
//            useredit.render(
//                    User.findByUrl("")
////                    "UserEdit", User.find.byId(request().username()), models.Target.findInvolving(), User.findAll()
//            )
        );
    }

    /**
     * Display the user edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("user url: " + url);
		User user = User.findByUrl(url);
		Logger.info("user name: " + user.name + ", url: " + url);
        return ok(
                useredit.render(
                        User.findByUrl(url)
                )
//                useredit.render(
//                    url, user.name
//                )
            );
    }
    
    public static Result sites(String url) {
        return ok(
                usersites.render(
                        User.findByUrl(url)
                )
            );
    }
    
    public static Result bookmarks(String url) {
        return ok(
                userbookmarks.render(
                        User.findByUrl(url)
                )
            );
    }
    
    /**
     * Add a target.
     */
    public static Result add() {
        Target newTarget = Target.create(
            "New target", 
            "url"
        );
        return null;
    }
    
    /**
     * Rename a target.
     */
    public static Result rename(Long target) {
        return ok(
            Target.rename(
                target, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a target.
     */
    public static Result delete(Long target) {
        Target.find.ref(target).delete();
        return ok();
    }
    
}


package controllers;

import models.User;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import views.html.userbookmarks;
import views.html.users.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class UserEdit extends AbstractController {
  
    /**
     * Display the users.
     */
    public static Result index() {
        return ok(
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
                        User.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result sites(String url) {
        return ok(
                usersites.render(
                        User.findByUrl(url), User.find.byId(request().username())
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
}


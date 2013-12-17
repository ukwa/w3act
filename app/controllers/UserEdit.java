package controllers;

import models.Target;
import models.User;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.curators;
import views.html.lookup;
import views.html.targetedit;
import views.html.userbookmarks;
import views.html.useredit;
import views.html.usersites;
import views.html.userview;

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
     * Display the user view panel for this URL.
     */
    public static Result view(String url) {
		Logger.info("view user url: " + url);
		User user = User.findByUrl(url);
		Logger.info("user name: " + user.name + ", url: " + url);
        return ok(
                userview.render(
                        User.findByUrl(url), User.find.byId(request().username())
                )
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
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("UserEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.UserEdit.addEntry(name));
        	} else {
        		Logger.info("User name is empty. Please write name in search window.");
                res = ok(
                        curators.render(
                            "Curators", User.find.byId(request().username()), models.User.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
                    curators.render(
                        "Curators", User.find.byId(request().username()), models.User.filterByName(name), name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * Add new user entry.
     * @param user
     * @return
     */
    public static Result addEntry(String name) {
    	User user = new User();
    	user.name = name;
        user.uid = Target.createId();
        user.url = Const.ACT_URL + user.uid;
		Logger.info("add entry with url: " + user.url + ", and name: " + user.name);
        return ok(
                useredit.render(
                      user, User.find.byId(request().username())
                )
            );
    }
        
}


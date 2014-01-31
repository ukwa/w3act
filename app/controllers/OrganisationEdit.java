package controllers;

import models.Organisation;
import models.User;
import play.mvc.Result;
import play.mvc.Security;
import views.html.organisations.*;

/**
 * Manage targets.
 */
@Security.Authenticated(Secured.class)
public class OrganisationEdit extends AbstractController {
  
    /**
     * Display the organisation.
     */
    public static Result index() {
        return ok(
        );
    }

    public static Result sites(String url) {
        return ok(
                organisationsites.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * Administer users
     * @param url
     * @return
     */
    public static Result admin(String url) {
        return ok(
                organisationadmin.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
//    public static Result filter() {
//    	Result res = null;
//    	Logger.info("OrganisationEdit.filter()");
//        String addentry = getFormParam(Const.ADDENTRY);
//        String search = getFormParam(Const.SEARCH);
//        String name = getFormParam(Const.NAME);
//        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
//        if (addentry != null) {
//        	if (name != null && name.length() > 0) {
//        		res = redirect(routes.Organisations.create(name));
//        	} else {
//        		Logger.info("Organisation name is empty. Please write name in search window.");
//        		flash("message", "Please enter a name in the Search Text Field");
//        		return redirect(routes.Organisations.index());
//        	}
//        } else {
//            res = ok(
//            		organisations.render(
//                        "Organisations", User.find.byId(request().username()), models.Organisation.filterByName(name), name
//                    )
//                );
//        }
//        return res;
//    }	   
}


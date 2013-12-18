package controllers;

import models.Organisation;
import models.Target;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.*;

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

    /**
     * Display the organisation edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("organisation url: " + url);
		Organisation organisation = Organisation.findByUrl(url);
		Logger.info("organisation title: " + organisation.title + ", url: " + url);
        return ok(
                organisationedit.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                organisationview.render(
                        Organisation.findByUrl(url), User.find.byId(request().username())
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
    	Logger.info("OrganisationEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.OrganisationEdit.addEntry(name));
        	} else {
        		Logger.info("Organisation name is empty. Please write name in search window.");
                res = ok(
                        organisations.render(
                            "Organisations", User.find.byId(request().username()), models.Organisation.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
            		organisations.render(
                        "Organisations", User.find.byId(request().username()), models.Organisation.filterByName(name), name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * Add new organisation entry.
     * @param organisation title
     * @return
     */
    public static Result addEntry(String title) {
    	Organisation organisation = new Organisation();
    	organisation.title = title;
        organisation.nid = Target.createId();
        organisation.url = Const.ACT_URL + organisation.nid;
		Logger.info("add entry with url: " + organisation.url + ", and title: " + organisation.title);
        return ok(
                organisationedit.render(
                      organisation, User.find.byId(request().username())
                )
            );
    }
        
    
}


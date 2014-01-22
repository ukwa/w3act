package controllers;

import play.*;
import play.mvc.*;
import static play.data.Form.*;

import java.util.*;

import com.avaje.ebean.Ebean;

import models.*;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.organisations.*;;

/**
 * Manage organisations.
 */
@Security.Authenticated(Secured.class)
public class Organisations extends AbstractController {
  
    /**
     * Display the organisations.
     */
    public static Result index() {
        return ok(
            organisations.render(
                "Organisations", User.find.byId(request().username()), models.Organisation.findInvolving(), ""
            )
        );
    }

    // -- Organisations

    /**
     * Add a organisation.
     */
    public static Result add() {
        Organisation newOrganisation = Organisation.create(
            "New organisation"
        );
        return ok();
    }
    
    /**
     * Rename a organisation.
     */
    public static Result rename(Long organisation) {
        return ok(
            Organisation.rename(
                organisation, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a organisation.
     */
    public static Result delete(Long organisation) {
        Organisation.find.ref(organisation).delete();
        return ok();
    }

    /**
     * This method saves new object or changes on given Organisation in the same object
     * completed by revision comment. The "version" field in the Organisation object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save organisation nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION) + 
        			", abbreviation: " + getFormParam(Const.FIELD_ABBREVIATION));
        	Organisation organisation = null;
            boolean isExisting = true;
            try {
                try {
                	organisation = Organisation.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	organisation = new Organisation();
                	organisation.nid = Long.valueOf(getFormParam(Const.NID));
                	organisation.url = getFormParam(Const.URL);
                }
                if (organisation == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	organisation = new Organisation();
                	organisation.nid = Long.valueOf(getFormParam(Const.NID));
                	organisation.url = getFormParam(Const.URL);
                }
                
                organisation.title = getFormParam(Const.TITLE);
        	    if (getFormParam(Const.FIELD_ABBREVIATION) != null) {
        	    	organisation.field_abbreviation = getFormParam(Const.FIELD_ABBREVIATION);
        	    }
        	    if (getFormParam(Const.SUMMARY) != null) {
        	    	organisation.summary = getFormParam(Const.SUMMARY);
        	    }
        	    if (organisation.revision == null) {
        	    	organisation.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	organisation.revision = organisation.revision.concat(", " + getFormParam(Const.REVISION));
                }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(organisation);
    	        Logger.info("save organisation: " + organisation.toString());
        	} else {
           		Logger.info("update organisation: " + organisation.toString());
               	Ebean.update(organisation);
        	}
	        res = redirect(routes.OrganisationEdit.view(organisation.url));
        } 
        if (delete != null) {
        	Organisation organisation = Organisation.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(organisation);
	        res = redirect(routes.Organisations.index()); 
        }
        return res;
    }
	
    /**
     * This method checks if given user belongs to given organisation. This objects 
     * are linked by organisation URL.
     * @param user
     * @param organisation
     * @return
     */
    private static boolean isOrganisationLink(User user, Organisation organisation) {
    	boolean res = false;
		if (user.field_affiliation != null && organisation.url != null 
				&& user.field_affiliation.equals(organisation.url)) {
			res = true;
		} 
		return res;
	}
    
    /**
     * This method adds link to passed organisation in given User object if 
     * link does not already exists.
     * @param user
     * @param organisation
     */
    private static void addLink(User user, Organisation organisation) {
//		Logger.info("flag true add link: " + user.name);
    	if (!isOrganisationLink(user, organisation)) {
    		user.field_affiliation = organisation.url;
        	Ebean.update(user);
    	}
	}
    
    /**
     * This method removes link to passed organisation from given User object if 
     * link exists.
     * @param user
     * @param organisation
     */
    private static void removeLink(User user, Organisation organisation) {
//		Logger.info("flag false remove link: " + user.name);
    	if (isOrganisationLink(user, organisation)) {
    		Logger.info("remove link: " + user.name);
    		user.field_affiliation = "";
        	Ebean.update(user);
    	}
	}
    
    /**
     * This method implements administration for users associated with particular organisation.
     * @return
     */
    public static Result admin() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        if (save != null) {
        	Organisation organisation = null;
            boolean isExisting = true;
            try {
               	organisation = Organisation.findByUrl(getFormParam(Const.URL));
               	
		        List<User> userList = User.findAll();
		        Iterator<User> userItr = userList.iterator();
		        while (userItr.hasNext()) {
		        	User user = userItr.next();
	                if (getFormParam(user.name) != null) {
//                		Logger.info("getFormParam(user.name): " + getFormParam(user.name) + " " + user.name);
		                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(user.name));
		                if (userFlag) {
		                	addLink(user, organisation); 
		                } else {
		                	removeLink(user, organisation); 
		                }
	                } else {
	                	removeLink(user, organisation); 	                	
	                }
		        }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
	        res = redirect(routes.OrganisationEdit.admin(organisation.url));
        } else {
        	res = ok();
        }
        return res;
    }
	    
}


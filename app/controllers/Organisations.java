package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import com.avaje.ebean.Ebean;

import models.*;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.*;

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
	
    
}


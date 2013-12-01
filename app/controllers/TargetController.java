package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import views.html.*;
import uk.bl.api.*;

import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuilder;
import java.io.FileNotFoundException;

import com.avaje.ebean.Ebean;

import play.libs.Json;
import uk.bl.Const;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class TargetController extends AbstractController {
  
    /**
     * This method saves changes on given target in a new target object
     * completed by revision comment. The "version" field in the Target object
     * contains the timestamp of the change and the last version is marked by
     * flag "active". Remaining Target objects with the same URL are not active.
     * @return
     */
    public static Result saveTarget() {
    	Result res = null;
        String save = getFormParam("save");
        String delete = getFormParam("delete");
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save udated target nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", subject: " + getFormParam(Const.SUBJECT) +
        			", organisation: " + getFormParam(Const.ORGANISATION) +
        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
            Target target = new Target();
        	Target newTarget = new Target();
            boolean isExisting = true;
            try {
        	    target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
            } catch (Exception e) {
            	Logger.info("is not existing exception");
            	isExisting = false;
            }
            if (target == null) {
            	target = new Target();
            	Logger.info("is not existing");
            	isExisting = false;
            }
            newTarget.nid = Target.createId();
            newTarget.url = target.url;
            newTarget.author = target.author;
            if (target.author == null) {
            	newTarget.author = getFormParam(Const.USER);
            }
            newTarget.field_collection_categories = target.field_collection_categories;
            newTarget.field_nominating_organisation = target.field_nominating_organisation;
            Logger.info("new nid: " + newTarget.nid);
            newTarget.title = getFormParam(Const.TITLE);
            newTarget.field_url = getFormParam(Const.FIELD_URL);
            if (getFormParam(Const.KEYSITE) == null) {
            	newTarget.field_key_site = Const.FALSE;
            } else {
            	newTarget.field_key_site = Const.TRUE;
            }
            newTarget.field_description = getFormParam(Const.DESCRIPTION);
            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
            	newTarget.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
            } 
            if(getFormParam(Const.SUBJECT).equals("10")) {
            	newTarget.field_subject = "Arts &amp; Humanities";
            }
            if(getFormParam(Const.SUBJECT).equals("11")) {
            	newTarget.field_subject = "Business, Economy &amp; Industry";
            }
            if(getFormParam(Const.SUBJECT).equals("12")) {
            	newTarget.field_subject = "Education &amp; Research";
            }
            if (getFormParam(Const.ORGANISATION) != null) {
            	if (getFormParam(Const.ORGANISATION) != null && 
            			!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
//            		Logger.info("organisation: " + getFormParam(Const.ORGANISATION));
            		newTarget.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
            	} else {
            		newTarget.field_nominating_organisation = Const.NONE;
            	}
            }
            newTarget.summary = getFormParam(Const.SUMMARY);
            newTarget.revision = getFormParam(Const.REVISION);
            newTarget.active = true;
        	if (!isExisting) {
//        		if (newTarget.url == null) {
//        		newTarget.url = "none.com/url";
        		newTarget.url = Const.ACT_URL + newTarget.nid;
        		newTarget.edit_url = Const.WCT_URL + newTarget.nid;
        		if (newTarget.value == null) {
        			newTarget.value = "";
        		}
        		if (newTarget.summary == null) {
        			newTarget.summary = "";
        		}
        		if (newTarget.format == null) {
        			newTarget.format = "";
        		}
        		if (newTarget.field_scope == null) {
        			newTarget.field_scope = "root";
        		}
        		if (newTarget.field_depth == null) {
        			newTarget.field_depth = "capped";
        		}
        		if (newTarget.type == null) {
        			newTarget.type = Const.URL;
        		}
        		if (newTarget.field_collection_categories == null || newTarget.field_collection_categories.length() == 0) {
        			newTarget.field_collection_categories = "";
        		}
        	} else {
                target.active = false;
        		Logger.info("update target: " + target.nid);
            	Ebean.update(target);
        	}
        	Ebean.save(newTarget);
	        Logger.info("save target: " + newTarget.toString());
	        res = redirect(routes.TargetEdit.edit(newTarget.url));
        } 
        if (delete != null) {
        	Target target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
        	Ebean.delete(target);
	        res = redirect(routes.Targets.index()); 
        }
        return res;
    }
	
}


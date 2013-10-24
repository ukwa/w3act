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
  
	private static Target previewObj;
	
    public static Result addTarget() {
    	Result res;
        Target target = new Target();
        target.title = getFormParam(Const.TITLE);
        target.summary = getFormParam(Const.SUMMARY);
        UUID id = UUID.randomUUID();
        Logger.info("id: " + id.toString());
        target.nid = id.getMostSignificantBits();
        String save = getFormParam("save");
        String preview = getFormParam("preview");
//        Logger.info("save: " + save + ", preview: " + preview);
        if (save != null) {
	        target.save();
	        Logger.info("add article: " + target.toString());
	        res = redirect(routes.Article.index());
        } else {
        	previewObj = target;
	        res = redirect(routes.Article.preview());
        }
        return res;
    }
	
    public static Result saveTarget() {
    	Result res = null;
        String save = getFormParam("save");
        String preview = getFormParam("preview");
        String delete = getFormParam("delete");
//        Logger.info("save: " + save + ", preview: " + preview);
        if (save != null) {
        	Logger.info("save udated target nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", subject: " + getFormParam(Const.SUBJECT) +
        			", organisation: " + getFormParam(Const.ORGANISATION) +
        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
            Target target = new Target();
            boolean isExisting = true;
            try {
        	    target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
            } catch (Exception e) {
            	Logger.info("is not existing");
            	isExisting = false;
         		target.nid = Long.valueOf(getFormParam(Const.NID));
            }
            target.title = getFormParam(Const.TITLE);
            target.field_url = getFormParam(Const.FIELD_URL);
            if (getFormParam(Const.KEYSITE) == null) {
            	target.field_key_site = Const.FALSE;
            } else {
            	target.field_key_site = Const.TRUE;
            }
            target.field_description = getFormParam(Const.DESCRIPTION);
            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
            	target.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
            } 
            if(getFormParam(Const.SUBJECT).equals("10")) {
            	target.field_subject = "Arts &amp; Humanities";
            }
            if(getFormParam(Const.SUBJECT).equals("11")) {
            	target.field_subject = "Business, Economy &amp; Industry";
            }
            if(getFormParam(Const.SUBJECT).equals("12")) {
            	target.field_subject = "Education &amp; Research";
            }
        	target.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
        	if (!isExisting) {
        		target.url = "none.com/url";
        		Ebean.save(target);
        	} else {
        		Ebean.update(target);
        	}
	        Logger.info("save target: " + target.toString());
	        res = redirect(routes.TargetEdit.edit(target.url));
        } 
        if (preview != null) {
//        	previewObj = target;
	        res = redirect(routes.TargetEdit.edit(getFormParam(Const.URL))); // TODO preview
        }
        if (delete != null) {
        	Target target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
        	Ebean.delete(target);
	        res = redirect(routes.Targets.index()); 
        }
        return res;
    }
	
}


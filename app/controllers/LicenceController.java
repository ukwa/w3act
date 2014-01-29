package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.text.SimpleDateFormat;
import java.util.*;

import models.*;
import views.html.*;
import views.html.licence.*;
import uk.bl.api.*;
import uk.bl.scope.Scope;

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
 * Support for adding owner licence.
 */
public class LicenceController extends AbstractController {
  
    /**
     * Display the licence form.
     */
    public static Result index() {
		return ok(
            ukwalicence.render()
        );
    }
    
    public static String getCurrentDate() {
    	return Utils.getCurrentDate();
    }
    
    /**
     * This method submits owner settings for UKWA licence.
     * @return
     */
    public static Result submit() {
    	Result res = null;
        Logger.info("Licence controller submit()");
        String submit = getFormParam(Const.SUBMIT);
        Logger.info("submit: " + submit);
        if (submit != null) {
        	Logger.info("save UKWA licence - title: " + getFormParam(Const.TITLE));
//            Target target = new Target();
//        	Target newTarget = new Target();
//            boolean isExisting = true;
//            try {
//        	    target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
//            } catch (Exception e) {
//            	Logger.info("is not existing exception");
//            	isExisting = false;
//            }
//            if (target == null) {
//            	target = new Target();
//            	Logger.info("is not existing");
//            	isExisting = false;
//            }
//            newTarget.nid = Target.createId();
//            newTarget.url = target.url;
//            newTarget.author = target.author;
//            if (target.author == null) {
//            	newTarget.author = getFormParam(Const.USER);
//            }
//            newTarget.field_collection_categories = target.field_collection_categories;
//            newTarget.field_nominating_organisation = target.field_nominating_organisation;
////            Logger.info("new nid: " + newTarget.nid);
//            newTarget.title = getFormParam(Const.TITLE);
//            newTarget.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL));
//            newTarget.field_key_site = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
//            newTarget.field_description = getFormParam(Const.DESCRIPTION);
//            if (getFormParam(Const.STATUS) != null) {
////        		Logger.info("status: " + getFormParam(Const.STATUS) + ".");
//            	newTarget.status = Long.valueOf(getFormParam(Const.STATUS));
//            } 
//            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
//            	newTarget.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
//            } 
//            newTarget.field_subject = getFormParam(Const.SUBJECT);
//            if (getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS) != null) {
//        		Logger.info("status: " + getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS) + ".");
//            	if (!getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS).toLowerCase().contains(Const.NONE)) {
//            		newTarget.field_suggested_collections = DCollection.findByTitle(getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS)).url;
//            	} else {
//            		newTarget.field_suggested_collections = Const.NONE;
//            	}
//            }
//            if (getFormParam(Const.ORGANISATION) != null) {
//            	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
////            		Logger.info("organisation: " + getFormParam(Const.ORGANISATION));
//            		newTarget.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
//            	} else {
//            		newTarget.field_nominating_organisation = Const.NONE;
//            	}
//            }
////    		Logger.info("author: " + getFormParam(Const.AUTHOR) + ", user: " + User.findByName(getFormParam(Const.AUTHOR)).url);
//            if (getFormParam(Const.AUTHOR) != null) {
//           		newTarget.author = User.findByName(getFormParam(Const.AUTHOR)).url;
//            }
//            newTarget.summary = getFormParam(Const.SUMMARY);
//            newTarget.revision = getFormParam(Const.REVISION);
//            newTarget.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
//            newTarget.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
//            newTarget.field_license = getFormParam(Const.FIELD_LICENSE);
//            newTarget.field_uk_hosting = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
//            newTarget.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
//            newTarget.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
//            newTarget.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
//            newTarget.field_notes = getFormParam(Const.FIELD_NOTES);
//            newTarget.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
//            newTarget.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
//            newTarget.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
////            Logger.info("ignore robots: " + getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
//            newTarget.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
//            newTarget.field_crawl_start_date = getFormParam(Const.FIELD_CRAWL_START_DATE);
//            newTarget.field_crawl_end_date = getFormParam(Const.FIELD_CRAWL_END_DATE);
//            newTarget.white_list = getFormParam(Const.WHITE_LIST);
//            newTarget.black_list = getFormParam(Const.BLACK_LIST);
//            newTarget.field_depth = getFormParam(Const.FIELD_DEPTH);
//            newTarget.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
//            newTarget.field_scope = getFormParam(Const.FIELD_SCOPE);
//            newTarget.keywords = getFormParam(Const.KEYWORDS);
//            newTarget.synonyms = getFormParam(Const.SYNONYMS);
//            newTarget.active = true;
//        	if (!isExisting) {
//        		newTarget.url = Const.ACT_URL + newTarget.nid;
//        		newTarget.edit_url = Const.WCT_URL + newTarget.nid;
//        	} else {
//                target.active = false;
//        		Logger.info("update target: " + target.nid);
//            	Ebean.update(target);
//        	}
//        	Ebean.save(newTarget);
//	        Logger.info("save target: " + newTarget.toString());
	        res = redirect(routes.LicenceController.result());
        } 
        return res;
    }	   
	
    /**
     * Display the result of th licence form submission.
     */
    public static Result result() {
		return ok(
            ukwalicenceresult.render()
        );
    }
    
    
}


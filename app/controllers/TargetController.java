package controllers;

import play.*;
import play.mvc.*;

import models.*;
import uk.bl.api.*;
import uk.bl.scope.Scope;

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
        Logger.info("save: " + save);
        Logger.info("delete: " + delete);
        if (save != null) {
        	Logger.info("save updated target nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", subject: " + getFormParams(Const.SUBJECT) +
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
//            Logger.info("new nid: " + newTarget.nid);
            newTarget.title = getFormParam(Const.TITLE);
            newTarget.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL));
            newTarget.field_key_site = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
            newTarget.field_description = getFormParam(Const.DESCRIPTION);
            if (getFormParam(Const.STATUS) != null) {
//        		Logger.info("status: " + getFormParam(Const.STATUS) + ".");
            	newTarget.status = Long.valueOf(getFormParam(Const.STATUS));
            } 
            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
            	newTarget.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
            } 
            if (getFormParam(Const.SUBJECT) != null) {
            	String[] subjects = getFormParams(Const.SUBJECT);
//            	Logger.info("subjects: " + subjects[0] + subjects[1]);
            	String resSubject = "";
            	for (String subject: subjects)
                {
//                	Logger.info("add subjects: " + subjects + ", " + Taxonomy.findByName(subject).url);
           			resSubject = resSubject + Taxonomy.findByName(subject).url + Const.LIST_DELIMITER;
                }
            	newTarget.field_subject = resSubject;
            }
            if (getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS) != null) {
        		Logger.info("status: " + getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS) + ".");
            	if (!getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS).toLowerCase().contains(Const.NONE)) {
            		newTarget.field_suggested_collections = DCollection.findByTitle(getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS)).url;
            	} else {
            		newTarget.field_suggested_collections = Const.NONE;
            	}
            }
            if (getFormParam(Const.ORGANISATION) != null) {
            	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
//            		Logger.info("organisation: " + getFormParam(Const.ORGANISATION));
            		newTarget.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
            	} else {
            		newTarget.field_nominating_organisation = Const.NONE;
            	}
            }
//    		Logger.info("author: " + getFormParam(Const.AUTHOR) + ", user: " + User.findByName(getFormParam(Const.AUTHOR)).url);
            if (getFormParam(Const.AUTHOR) != null) {
           		newTarget.author = User.findByName(getFormParam(Const.AUTHOR)).url;
            }
            newTarget.summary = getFormParam(Const.SUMMARY);
            newTarget.revision = getFormParam(Const.REVISION);
            newTarget.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
            newTarget.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
            newTarget.field_license = getFormParam(Const.FIELD_LICENSE);
            newTarget.field_uk_hosting = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
            newTarget.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
            newTarget.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
            newTarget.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
            newTarget.field_notes = getFormParam(Const.FIELD_NOTES);
            newTarget.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
            newTarget.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
            newTarget.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
//            Logger.info("ignore robots: " + getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
            newTarget.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
            newTarget.field_crawl_start_date = getFormParam(Const.FIELD_CRAWL_START_DATE);
            newTarget.field_crawl_end_date = getFormParam(Const.FIELD_CRAWL_END_DATE);
            newTarget.white_list = getFormParam(Const.WHITE_LIST);
            newTarget.black_list = getFormParam(Const.BLACK_LIST);
            newTarget.field_depth = getFormParam(Const.FIELD_DEPTH);
            newTarget.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
            newTarget.field_scope = getFormParam(Const.FIELD_SCOPE);
            newTarget.keywords = getFormParam(Const.KEYWORDS);
            newTarget.synonyms = getFormParam(Const.SYNONYMS);
            newTarget.active = true;
        	if (!isExisting) {
        		newTarget.url = Const.ACT_URL + newTarget.nid;
        		newTarget.edit_url = Const.WCT_URL + newTarget.nid;
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
        	Long id = Long.valueOf(getFormParam(Const.NID));
        	Logger.info("deleting: " + id);
        	Target target = Target.findById(id);
        	Ebean.delete(target);
	        res = redirect(routes.Targets.index()); 
        }
        return res;
    }
	
    /**
     * This method is checking scope for given URL and returns result in JSON format.
     * @param url
     * @return JSON result
     */
    public static Result isInScope(String url) {
//    	Logger.info("isInScope controller: " + url);
    	boolean res = Target.isInScope(url, null);
//    	Logger.info("isInScope res: " + res);
    	return ok(Json.toJson(res));
    }
}


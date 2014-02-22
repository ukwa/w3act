package controllers;

import play.*;
import play.mvc.*;
import models.*;
import uk.bl.api.*;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

import com.avaje.ebean.Ebean;

import play.libs.Json;
import uk.bl.Const;
import views.html.instances.instanceedit;
import views.html.instances.instanceview;

/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class InstanceController extends AbstractController {
  
    /**
     * Display the instance.
     */
    public static Result index() {
        return ok(
        );
    }

    /**
     * Display the instance edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("InstanceController.edit() url: " + url);
		Instance instance = Instance.findByUrl(url);
		Logger.info("InstanceController.edit() instance name: " + instance.title + ", url: " + url + ", username: " + request().username());
        return ok(
                instanceedit.render(
                        Instance.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                instanceview.render(
                        Instance.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method shows selected revision of a Instance by given ID.
     * @param nid
     * @return
     */
    public static Result viewrevision(Long nid) {
        return ok(
                instanceview.render(
                        Instance.findById(nid), User.find.byId(request().username())
                )
            );
    }
    	
    /**
     * This method saves changes on given instance in a new instance object
     * completed by revision comment. The "version" field in the Instance object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result saveInstance() {
    	Result res = null;
        String save = getFormParam("save");
        String delete = getFormParam("delete");
        Logger.info("save: " + save);
        Logger.info("delete: " + delete);
        if (save != null) {
        	Logger.info("save updated instance nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", subject: " + getFormParams(Const.SUBJECT) +
        			", organisation: " + getFormParam(Const.ORGANISATION) +
        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
            Instance instance = new Instance();
        	Instance newInstance = new Instance();
            boolean isExisting = true;
            try {
        	    instance = Instance.findById(Long.valueOf(getFormParam(Const.NID)));
            } catch (Exception e) {
            	Logger.info("is not existing exception");
            	isExisting = false;
            }
            if (instance == null) {
            	instance = new Instance();
            	Logger.info("is not existing");
            	isExisting = false;
            }
            newInstance.nid = Utils.createId();
            newInstance.url = instance.url;
            newInstance.author = instance.author;
            if (instance.author == null) {
            	newInstance.author = getFormParam(Const.USER);
            }
            newInstance.field_collection_categories = instance.field_collection_categories;
            newInstance.field_nominating_organisation = instance.field_nominating_organisation;
//            Logger.info("new nid: " + newInstance.nid);
            newInstance.title = getFormParam(Const.TITLE);
            newInstance.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL));
            newInstance.field_key_site = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
            newInstance.field_description = getFormParam(Const.DESCRIPTION);
            if (getFormParam(Const.STATUS) != null) {
            	newInstance.status = Long.valueOf(getFormParam(Const.STATUS));
            } 
            if (getFormParam(Const.LANGUAGE) != null) {
            	newInstance.language = getFormParam(Const.LANGUAGE);
            } 
            if (getFormParam(Const.SELECTION_TYPE) != null) {
            	newInstance.selection_type = getFormParam(Const.SELECTION_TYPE);
            } 
            if (getFormParam(Const.SELECTOR_NOTES) != null) {
            	newInstance.selector_notes = getFormParam(Const.SELECTOR_NOTES);
            } 
            if (getFormParam(Const.ARCHIVIST_NOTES) != null) {
            	newInstance.archivist_notes = getFormParam(Const.ARCHIVIST_NOTES);
            } 
            if (getFormParam(Const.LEGACY_SITE_ID) != null && getFormParam(Const.LEGACY_SITE_ID).length() > 0) {
        		Logger.info("legacy site id: " + getFormParam(Const.LEGACY_SITE_ID) + ".");
            	newInstance.legacy_site_id = Long.valueOf(getFormParam(Const.LEGACY_SITE_ID));
            }

    		Logger.info("authors: " + getFormParam(Const.AUTHORS) + ".");
            if (getFormParam(Const.AUTHORS) != null) {
            	if (!getFormParam(Const.AUTHORS).toLowerCase().contains(Const.NONE)) {
	            	String[] contactPersons = getFormParams(Const.AUTHORS);
	            	Logger.info("param: " + contactPersons.length + contactPersons.toString());
	            	String resContactPersons = "";
	            	for (String contactPerson : contactPersons)
	                {
                		Logger.info("contactPerson: " + contactPerson);
                		if (contactPerson != null && contactPerson.length() > 0) {
	                		resContactPersons = resContactPersons + ContactPerson.findByName(contactPerson).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	instance.authors = resContactPersons;
            	} else {
            		instance.authors = Const.NONE;
            	}
            }            
            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
            	newInstance.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
            } 
            if (getFormParam(Const.SUBJECT) != null) {
            	if (!getFormParam(Const.SUBJECT).toLowerCase().contains(Const.NONE)) {
	            	String[] subjects = getFormParams(Const.SUBJECT);
	            	String resSubject = "";
	            	for (String subject: subjects)
	                {
	            		if (subject != null && subject.length() > 0) {
	                		Logger.info("add subject: " + subject);
	            			resSubject = resSubject + Taxonomy.findByFullName(subject).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newInstance.field_subject = resSubject;
            	} else {
            		newInstance.field_subject = Const.NONE;
            	}
            }
            if (getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS) != null) {
            	if (!getFormParam(Const.FIELD_SUGGESTED_COLLECTIONS).toLowerCase().contains(Const.NONE)) {
	            	String[] elems = getFormParams(Const.FIELD_SUGGESTED_COLLECTIONS);
	            	String resElem = "";
	            	for (String elem: elems)
	                {
	            		if (elem != null && elem.length() > 0) {
	            			resElem = resElem + DCollection.findByTitle(elem).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newInstance.field_suggested_collections = resElem;
            	} else {
            		newInstance.field_suggested_collections = Const.NONE;
            	}
            }
            if (getFormParam(Const.ORGANISATION) != null) {
            	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
            		newInstance.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
            	} else {
            		newInstance.field_nominating_organisation = Const.NONE;
            	}
            }
            if (getFormParam(Const.ORIGINATING_ORGANISATION) != null) {
            	if (!getFormParam(Const.ORIGINATING_ORGANISATION).toLowerCase().contains(Const.NONE)) {
            		newInstance.originating_organisation = Organisation.findByTitle(getFormParam(Const.ORIGINATING_ORGANISATION)).url;
            	} else {
            		newInstance.originating_organisation = Const.NONE;
            	}
            }
            if (getFormParam(Const.AUTHOR) != null) {
           		newInstance.author = User.findByName(getFormParam(Const.AUTHOR)).url;
            }
            if (getFormParam(Const.TAGS) != null) {
            	if (!getFormParam(Const.TAGS).toLowerCase().contains(Const.NONE)) {
	            	String[] tags = getFormParams(Const.TAGS);
	            	String resTags = "";
	            	for (String tag: tags)
	                {
	            		if (tag != null && tag.length() > 0) {
	                		Logger.info("add tag: " + tag);
	            			resTags = resTags + Tag.findByName(tag).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newInstance.tags = resTags;
            	} else {
            		newInstance.tags = Const.NONE;
            	}
            }
            if (getFormParam(Const.FLAGS) != null) {
            	if (!getFormParam(Const.FLAGS).toLowerCase().contains(Const.NONE)) {
	            	String[] flags = getFormParams(Const.FLAGS);
	            	String resFlags = "";
	            	for (String flag: flags)
	                {
	            		if (flag != null && flag.length() > 0) {
	                		Logger.info("add flag: " + flag);
	            			resFlags = resFlags + Flag.findByName(flag).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newInstance.flags = resFlags;
            	} else {
            		newInstance.flags = Const.NONE;
            	}
            }
            newInstance.justification = getFormParam(Const.JUSTIFICATION);
            newInstance.summary = getFormParam(Const.SUMMARY);
            newInstance.revision = getFormParam(Const.REVISION);
            newInstance.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
            newInstance.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
            newInstance.field_license = getFormParam(Const.FIELD_LICENSE);
            newInstance.field_uk_hosting = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
            newInstance.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
            newInstance.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
            newInstance.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
            newInstance.field_notes = getFormParam(Const.FIELD_NOTES);
            newInstance.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
            newInstance.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
            newInstance.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
            newInstance.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
            newInstance.field_crawl_start_date = getFormParam(Const.FIELD_CRAWL_START_DATE);
            newInstance.date_of_publication = getFormParam(Const.DATE_OF_PUBLICATION);
            newInstance.field_crawl_end_date = getFormParam(Const.FIELD_CRAWL_END_DATE);
            newInstance.white_list = getFormParam(Const.WHITE_LIST);
            newInstance.black_list = getFormParam(Const.BLACK_LIST);
            newInstance.field_depth = getFormParam(Const.FIELD_DEPTH);
            newInstance.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
            newInstance.field_scope = getFormParam(Const.FIELD_SCOPE);
            newInstance.keywords = getFormParam(Const.KEYWORDS);
            Logger.info("instance keywords: " + getFormParam(Const.KEYWORDS));
            newInstance.synonyms = getFormParam(Const.SYNONYMS);
        	if (!isExisting) {
        		newInstance.url = Const.ACT_URL + newInstance.nid;
        		newInstance.edit_url = Const.WCT_URL + newInstance.nid;
        	} else {
        		Logger.info("update instance: " + instance.nid + ", obj: " + instance.toString());
                instance.keywords = getFormParam(Const.KEYWORDS);
            	Ebean.update(instance);
    	        res = redirect(routes.InstanceController.view(instance.url));
    	        return res;
        	}
        	Ebean.save(newInstance);
	        Logger.info("save instance: " + newInstance.toString());
	        res = redirect(routes.InstanceController.view(newInstance.url));
        } 
        if (delete != null) {
        	Long id = Long.valueOf(getFormParam(Const.NID));
        	Logger.info("deleting instance: " + id);
        	Instance instance = Instance.findById(id);
        	Ebean.delete(instance);
	        res = redirect(routes.Instances.index()); 
        }
        return res;
    }
	
    /**
     * This method is checking scope for given URL and returns result in JSON format.
     * @param url
     * @return JSON result
     * @throws WhoisException 
     */
    public static Result isInScope(String url) throws WhoisException {
//    	Logger.info("isInScope controller: " + url);
    	boolean res = Instance.isInScope(url, null);
//    	Logger.info("isInScope res: " + res);
    	return ok(Json.toJson(res));
    }
}


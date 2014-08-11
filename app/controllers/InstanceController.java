package controllers;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import play.*;
import play.mvc.*;
import models.*;
import uk.bl.api.*;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import play.data.Form;
import play.libs.Json;
import uk.bl.Const;
import views.html.instances.edit;
import views.html.instances.view;

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
		Form<Instance> instanceForm = Form.form(Instance.class);
		instanceForm = instanceForm.fill(instance);
        return ok(edit.render(instanceForm, User.findByEmail(request().username())));
    }
    
    public static Result view(String url) {
        return ok(
                view.render(
                        Instance.findByUrl(url), User.findByEmail(request().username())
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
                view.render(
                        Instance.findById(nid), User.findByEmail(request().username())
                )
            );
    }
    	
	/**
	 * This method prepares Instance form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
	    Instance newInstance = new Instance();
        try {
    	    Instance instance = Instance.findById(Long.valueOf(getFormParam(Const.NID)));
        	if (getFormParam(Const.FIELD_WCT_ID) != null && !getFormParam(Const.FIELD_WCT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
            	newInstance.field_wct_id = instance.field_wct_id;
            }
        	if (getFormParam(Const.FIELD_SPT_ID) != null && !getFormParam(Const.FIELD_SPT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
        		newInstance.field_spt_id = instance.field_spt_id;
        	}
        	if (getFormParam(Const.LEGACY_SITE_ID) != null && !getFormParam(Const.LEGACY_SITE_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
        		newInstance.legacy_site_id = instance.legacy_site_id;
            }
        } catch (Exception e) {
        	Logger.info("The instance for given NID is not yet existing in database");
        } 	
	    newInstance.nid = Long.valueOf(getFormParam(Const.NID));
        if (newInstance.author == null) {
        	newInstance.author = getFormParam(Const.USER);
        }
        newInstance.url = Const.ACT_URL + newInstance.nid;
        newInstance.title = getFormParam(Const.TITLE);
        newInstance.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL_NODE));
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
        if (getFormParam(Const.LEGACY_SITE_ID) != null 
        		&& getFormParam(Const.LEGACY_SITE_ID).length() > 0
        		&& Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
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
            	newInstance.authors = resContactPersons;
        	} else {
        		newInstance.authors = Const.NONE;
        	}
        }            
        if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
        	newInstance.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
        } 
        if (getFormParam(Const.FIELD_SUBJECT) != null) {
        	newInstance.field_subject = Utils.removeDuplicatesFromList(getFormParam(Const.FIELD_SUBJECT));
    		Logger.debug("newInstance.field_subject: " + newInstance.field_subject);
    		if (newInstance.field_subject == null) {
    			newInstance.field_subject = Const.NONE;
    		}
        } else {
        	newInstance.field_subject = Const.NONE;
        }            
        if (getFormParam(Const.TREE_KEYS) != null) {
        	newInstance.field_collection_categories = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
    		Logger.debug("newInstance.field_collection_categories: " + newInstance.field_collection_categories);
        }
        if (getFormParam(Const.ORGANISATION) != null) {
        	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
        		newInstance.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
        		newInstance.updateOrganisation();
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
        if (getFormParam(Const.FIELD_WCT_ID) != null  && getFormParam(Const.FIELD_WCT_ID).length() > 0) {
        	newInstance.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
        }
        if (getFormParam(Const.FIELD_SPT_ID) != null && getFormParam(Const.FIELD_SPT_ID).length() > 0) {
        	newInstance.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
        }
        newInstance.field_license = getFormParam(Const.FIELD_LICENSE);
//        newInstance.field_uk_hosting = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
        newInstance.field_uk_hosting = Target.isInScopeIp(newInstance.field_url, newInstance.url);
    	Logger.debug("field_uk_hosting: " + newInstance.field_uk_hosting);
        newInstance.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
        newInstance.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
        newInstance.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
        newInstance.field_notes = getFormParam(Const.FIELD_NOTES);
        newInstance.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
        newInstance.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
        newInstance.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
        newInstance.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));           
        if (getFormParam(Const.FIELD_CRAWL_START_DATE) != null) {
        	String startDateHumanView = getFormParam(Const.FIELD_CRAWL_START_DATE);
        	String startDateUnix = Utils.getUnixDateStringFromDate(startDateHumanView);
        	Logger.info("startDateHumanView: " + startDateHumanView + ", startDateUnix: " + startDateUnix);
        	newInstance.field_crawl_start_date = startDateUnix;
        }
        if (getFormParam(Const.FIELD_CRAWL_END_DATE) != null) {
        	String endDateHumanView = getFormParam(Const.FIELD_CRAWL_END_DATE);
        	String endDateUnix = Utils.getUnixDateStringFromDate(endDateHumanView);
        	Logger.info("endDateHumanView: " + endDateHumanView + ", endDateUnix: " + endDateUnix);
        	newInstance.field_crawl_end_date = endDateUnix;
        }
        newInstance.date_of_publication = getFormParam(Const.DATE_OF_PUBLICATION);
        newInstance.white_list = getFormParam(Const.WHITE_LIST);
        newInstance.black_list = getFormParam(Const.BLACK_LIST);
        if (getFormParam(Const.FIELD_DEPTH) != null) {
        	newInstance.field_depth = Targets.getDepthNameFromGuiName(getFormParam(Const.FIELD_DEPTH));
        }
        if (getFormParam(Const.FIELD_SCOPE) != null) {
        	newInstance.field_scope = Targets.getScopeNameFromGuiName(getFormParam(Const.FIELD_SCOPE));
        }
        newInstance.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
        newInstance.keywords = getFormParam(Const.KEYWORDS);
//        Logger.info("instance keywords: " + getFormParam(Const.KEYWORDS));
        newInstance.synonyms = getFormParam(Const.SYNONYMS);
        if (getFormParam(Const.QA_STATUS) != null) {
        	if (!getFormParam(Const.QA_STATUS).toLowerCase().contains(Const.NONE)) {
        		Logger.info("Instance qa status: " + getFormParam(Const.QA_STATUS));
        		newInstance.qa_status = getFormParam(Const.QA_STATUS);
        	} else {
        		newInstance.qa_status = Const.NONE;
        	}
        }
        if (getFormParam(Const.QA_ISSUE_CATEGORY) != null) {
        	if (!getFormParam(Const.QA_ISSUE_CATEGORY).toLowerCase().contains(Const.NONE)) {
        		Logger.info("Instance qa issue category: " + getFormParam(Const.QA_ISSUE_CATEGORY));
            	String[] issueCategories = getFormParams(Const.QA_ISSUE_CATEGORY);
            	String resIssueCategories = "";
            	for (String issueCategory: issueCategories)
                {
            		if (issueCategory != null && issueCategory.length() > 0) {
                		Logger.info("add issueCategory: " + issueCategory);
            			resIssueCategories = resIssueCategories + issueCategory + Const.LIST_DELIMITER;
            		}
                }
            	newInstance.qa_issue_category = resIssueCategories;
        	} else {
        		newInstance.qa_issue_category = Const.NONE;
        	}
        }
        newInstance.qa_notes = getFormParam(Const.QA_NOTES);
        newInstance.quality_notes = getFormParam(Const.QUALITY_NOTES);

        Form<Instance> instanceFormNew = Form.form(Instance.class);
		instanceFormNew = instanceFormNew.fill(newInstance);
		Logger.debug("info() goto edit form");
      	return ok(
	              edit.render(instanceFormNew, User.findByEmail(request().username()))
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
        	Logger.info("input data for saving instance nid: " + getFormParam(Const.NID) + 
        			", url: " + getFormParam(Const.URL) + 
        			", field url: " + getFormParam(Const.FIELD_URL_NODE) + 
        			", title: " + getFormParam(Const.TITLE) + 
        			", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", subject: " + getFormParams(Const.FIELD_SUBJECT) +
        			", organisation: " + getFormParam(Const.ORGANISATION) +
        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
        	Logger.info("treeKeys: " + getFormParam(Const.TREE_KEYS));
        	Form<Instance> instanceForm = Form.form(Instance.class).bindFromRequest();
            if(instanceForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : instanceForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + instanceForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star. There are required fields in more than one tab. " + 
	  					" Missing fields are: " + missingFields);
	  			return info();
            }
        	
        	if (getFormParam(Const.FIELD_WCT_ID) != null && !getFormParam(Const.FIELD_WCT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
                Logger.info("Only numeric values are valid identifiers. Please check field 'WCT ID'.");
	  			flash("message", "Only numeric values are valid identifiers. Please check field 'WCT ID'.");
	  			return info();
        	}    	
        	if (getFormParam(Const.FIELD_SPT_ID) != null && !getFormParam(Const.FIELD_SPT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
                Logger.info("Only numeric values are valid identifiers. Please check field 'SPT ID'.");
	  			flash("message", "Only numeric values are valid identifiers. Please check field 'SPT ID'.");
	  			return info();
        	}    	
        	if (getFormParam(Const.LEGACY_SITE_ID) != null && !getFormParam(Const.LEGACY_SITE_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
                Logger.info("Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
	  			flash("message", "Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
	  			return info();
        	}    	

        	Instance newInstance = new Instance();
            boolean isExisting = true;
            try {
        	    newInstance = Instance.findById(Long.valueOf(getFormParam(Const.NID)));
            } catch (Exception e) {
            	Logger.info("is not existing exception");
            	isExisting = false;
            	newInstance = new Instance();
            	newInstance.nid = Long.valueOf(getFormParam(Const.NID));
//            	newInstance.url = getFormParam(Const.URL);            	
        		newInstance.url = Const.ACT_URL + newInstance.nid;
        		newInstance.edit_url = Const.WCT_URL + newInstance.nid;            	
            }
            if (newInstance == null) {
            	newInstance = new Instance();
            	newInstance.nid = Long.valueOf(getFormParam(Const.NID));
//            	newInstance.url = getFormParam(Const.URL);      
        		newInstance.url = Const.ACT_URL + newInstance.nid;
        		newInstance.edit_url = Const.WCT_URL + newInstance.nid;            	
            	Logger.info("is not existing");
            	isExisting = false;
            }
//        	if (StringUtils.isBlank(getFormParam(Const.TITLE)) 
//        			|| StringUtils.isBlank(getFormParam(Const.FIELD_URL))
//        			|| StringUtils.isBlank(getFormParam(Const.FIELD_SUBJECT))
//        			|| StringUtils.isBlank(getFormParam(Const.AUTHOR))
//        			|| StringUtils.isBlank(getFormParam(Const.SELECTION_TYPE))) {
//            	Logger.info("Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
//        		return badRequest("Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
//        	}    	
            if (newInstance.author == null) {
            	newInstance.author = getFormParam(Const.USER);
            }
//            Logger.info("new nid: " + newInstance.nid);
            newInstance.title = getFormParam(Const.TITLE);
            newInstance.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL_NODE));
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
	            	newInstance.authors = resContactPersons;
            	} else {
            		newInstance.authors = Const.NONE;
            	}
            }            
            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
            	newInstance.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
            } 
            if (getFormParam(Const.FIELD_SUBJECT) != null) {
            	newInstance.field_subject = Utils.removeDuplicatesFromList(getFormParam(Const.FIELD_SUBJECT));
        		Logger.debug("newInstance.field_subject: " + newInstance.field_subject);
            } else {
            	newInstance.field_subject = Const.NONE;
            }            
            if (getFormParam(Const.TREE_KEYS) != null) {
            	newInstance.field_collection_categories = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
	    		Logger.debug("newInstance.field_collection_categories: " + newInstance.field_collection_categories);
            }
            if (getFormParam(Const.ORGANISATION) != null) {
            	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
            		newInstance.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
            		newInstance.updateOrganisation();
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
            if (getFormParam(Const.FIELD_WCT_ID) != null  && getFormParam(Const.FIELD_WCT_ID).length() > 0) {
            	newInstance.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
            }
            if (getFormParam(Const.FIELD_SPT_ID) != null && getFormParam(Const.FIELD_SPT_ID).length() > 0) {
            	newInstance.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
            }
            newInstance.field_license = getFormParam(Const.FIELD_LICENSE);
//            newInstance.field_uk_hosting = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
            newInstance.field_uk_hosting = Target.isInScopeIp(newInstance.field_url, newInstance.url);
        	Logger.debug("field_uk_hosting: " + newInstance.field_uk_hosting);
            newInstance.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
            newInstance.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
            Logger.debug("field_uk_postal_address: " + newInstance.field_uk_postal_address + 
            		", field_uk_postal_address_url: " + newInstance.field_uk_postal_address_url);
            if (newInstance.field_uk_postal_address 
            		&& (newInstance.field_uk_postal_address_url == null || newInstance.field_uk_postal_address_url.length() == 0)) {
            	Logger.info("If UK Postal Address field has value 'Yes', the Postal Address URL is required.");
	  			flash("message", "If UK Postal Address field has value 'Yes', the Postal Address URL is required.");
	  			return info();
            }
            newInstance.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
            newInstance.field_notes = getFormParam(Const.FIELD_NOTES);
            if (newInstance.field_via_correspondence 
            		&& (newInstance.value == null || newInstance.value.length() == 0)) {
            	Logger.info("If Via Correspondence field has value 'Yes', the Notes field is required.");
	  			flash("message", "If Via Correspondence field has value 'Yes', the Notes field is required.");
	  			return info();
            }
            newInstance.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
            newInstance.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
            if (newInstance.field_professional_judgement 
            		&& (newInstance.field_professional_judgement_exp == null || newInstance.field_professional_judgement_exp.length() == 0)) {
            	Logger.info("If Professional Judgement field has value 'Yes', the Professional Judgment Explanation field is required.");
	  			flash("message", "If Professional Judgement field has value 'Yes', the Professional Judgment Explanation field is required.");
	  			return info();
            }
            newInstance.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
            newInstance.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));           
            if (getFormParam(Const.FIELD_CRAWL_START_DATE) != null) {
            	String startDateHumanView = getFormParam(Const.FIELD_CRAWL_START_DATE);
            	String startDateUnix = Utils.getUnixDateStringFromDate(startDateHumanView);
            	Logger.info("startDateHumanView: " + startDateHumanView + ", startDateUnix: " + startDateUnix);
            	newInstance.field_crawl_start_date = startDateUnix;
            }
            if (getFormParam(Const.FIELD_CRAWL_END_DATE) != null) {
            	String endDateHumanView = getFormParam(Const.FIELD_CRAWL_END_DATE);
            	String endDateUnix = Utils.getUnixDateStringFromDate(endDateHumanView);
            	Logger.info("endDateHumanView: " + endDateHumanView + ", endDateUnix: " + endDateUnix);
            	newInstance.field_crawl_end_date = endDateUnix;
            }
            newInstance.date_of_publication = getFormParam(Const.DATE_OF_PUBLICATION);
            newInstance.white_list = getFormParam(Const.WHITE_LIST);
            newInstance.black_list = getFormParam(Const.BLACK_LIST);
            if (getFormParam(Const.FIELD_DEPTH) != null) {
            	newInstance.field_depth = Targets.getDepthNameFromGuiName(getFormParam(Const.FIELD_DEPTH));
            }
            if (getFormParam(Const.FIELD_SCOPE) != null) {
            	newInstance.field_scope = Targets.getScopeNameFromGuiName(getFormParam(Const.FIELD_SCOPE));
            }
            newInstance.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
            newInstance.keywords = getFormParam(Const.KEYWORDS);
//            Logger.info("instance keywords: " + getFormParam(Const.KEYWORDS));
            newInstance.synonyms = getFormParam(Const.SYNONYMS);
            if (getFormParam(Const.QA_STATUS) != null) {
            	if (!getFormParam(Const.QA_STATUS).toLowerCase().contains(Const.NONE)) {
            		Logger.info("Instance qa status: " + getFormParam(Const.QA_STATUS));
            		newInstance.qa_status = getFormParam(Const.QA_STATUS);
            	} else {
            		newInstance.qa_status = Const.NONE;
            	}
            }
            if (getFormParam(Const.QA_ISSUE_CATEGORY) != null) {
            	if (!getFormParam(Const.QA_ISSUE_CATEGORY).toLowerCase().contains(Const.NONE)) {
            		Logger.info("Instance qa issue category: " + getFormParam(Const.QA_ISSUE_CATEGORY));
	            	String[] issueCategories = getFormParams(Const.QA_ISSUE_CATEGORY);
	            	String resIssueCategories = "";
	            	for (String issueCategory: issueCategories)
	                {
	            		if (issueCategory != null && issueCategory.length() > 0) {
	                		Logger.info("add issueCategory: " + issueCategory);
	            			resIssueCategories = resIssueCategories + issueCategory + Const.LIST_DELIMITER;
	            		}
	                }
	            	newInstance.qa_issue_category = resIssueCategories;
            	} else {
            		newInstance.qa_issue_category = Const.NONE;
            	}
            }
            newInstance.qa_notes = getFormParam(Const.QA_NOTES);
            newInstance.quality_notes = getFormParam(Const.QUALITY_NOTES);

            long unixTime = System.currentTimeMillis() / 1000L;
            String changedTime = String.valueOf(unixTime);
            Logger.info("changed time: " + changedTime);
            newInstance.changed = changedTime;

        	if (!isExisting) {
            	Ebean.save(newInstance);
    	        Logger.info("save instance: " + newInstance.nid);
        	} else {
        		Logger.info("update instance: " + newInstance.nid);
            	Ebean.update(newInstance);
        	}
	        return redirect(routes.InstanceController.edit(newInstance.url));
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
    
    /**
     * This method calculates collection children - objects that have parents.
     * @param url The identifier for parent 
     * @return child collection in JSON form
     */
    public static String getChildren(String url, String targetUrl) {
    	String res = "";
        final StringBuffer sb = new StringBuffer();
    	sb.append(", \"children\":");
    	List<DCollection> childSuggestedCollections = DCollection.getChildLevelCollections(url);
    	if (childSuggestedCollections.size() > 0) {
	    	sb.append(getTreeElements(childSuggestedCollections, targetUrl, false));
	    	res = sb.toString();
//	    	Logger.info("getChildren() res: " + res);
    	}
    	return res;
    }
    
    /**
     * Mark collections that are stored in target object as selected
     * @param collectionUrl The collection identifier
     * @param targetUrl This is an identifier for current instance object
     * @return
     */
    public static String checkSelection(String collectionUrl, String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Instance instance = Instance.findByUrl(targetUrl);
    		if (instance.field_collection_categories != null && 
    				instance.field_collection_categories.contains(collectionUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current instance object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getTreeElements(List<DCollection> collectionList, String targetUrl, boolean parent) { 
    	String res = "";
    	if (collectionList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	    	Iterator<DCollection> itr = collectionList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		DCollection collection = itr.next();
//    			Logger.debug("add collection: " + collection.title + ", with url: " + collection.url +
//    					", parent:" + collection.parent + ", parent size: " + collection.parent.length());
	    		if ((parent && collection.parent.length() == 0) || !parent) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.title + "\"," + checkSelection(collection.url, targetUrl) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, targetUrl) + "}");
	    		}
	    	}
//	    	Logger.info("collectionList level size: " + collectionList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.info("getTreeElements() res: " + res);
    	}
    	return res;
    }
    
    /**
     * This method computes a tree of suggested collections in JSON format. 
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollections(String targetUrl) {
//    	Logger.info("getSuggestedCollections()");
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<DCollection> suggestedCollections = DCollection.getFirstLevelCollections();
    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
//    	Logger.info("suggestedCollections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getSuggestedCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }
            
    /**
     * This method calculates subject children - objects that have parents.
     * @param url The identifier for parent 
     * @param targetUrl This is an identifier for current target object
     * @return child subject in JSON form
     */
    public static String getSubjectChildren(String url, String targetUrl) {
//    	Logger.info("getSubjectChildren() target URL: " + targetUrl);
    	String res = "";
        final StringBuffer sb = new StringBuffer();
    	sb.append(", \"children\":");
//    	List<Taxonomy> childSubject = Taxonomy.findListByType(Const.SUBSUBJECT);
    	Taxonomy subject = Taxonomy.findByUrl(url);
    	List<Taxonomy> childSubject = Taxonomy.findSubSubjectsList(subject.name);
    	if (childSubject.size() > 0) {
	    	sb.append(getSubjectTreeElements(childSubject, targetUrl, false));
	    	res = sb.toString();
//	    	Logger.info("getSubjectChildren() res: " + res);
    	}
    	return res;
    }
    
    /**
     * Mark subjects that are stored in target object as selected
     * @param subjectUrl The subject identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
    public static String checkSubjectSelection(String subjectUrl, String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Instance target = Instance.findByUrl(targetUrl);
    		if (target.field_subject != null && 
    				target.field_subject.contains(subjectUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
     * Check if none value is selected
     * @param subjectUrl The subject identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
    public static String checkNone(String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Instance target = Instance.findByUrl(targetUrl);
    		if (target.field_subject != null 
    				&& (target.field_subject.toLowerCase().contains(Const.NONE.toLowerCase()))) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
   	 * This method calculates first order subjects.
     * @param subjectList The list of all subjects
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getSubjectTreeElements(List<Taxonomy> subjectList, String targetUrl, boolean parent) { 
//    	Logger.info("getSubjectTreeElements() target URL: " + targetUrl);
    	String res = "";
    	if (subjectList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	        if (parent) {
	        	sb.append("{\"title\": \"" + "None" + "\"," + checkNone(targetUrl) + 
	        			" \"key\": \"" + "None" + "\"" + "}, ");
	        }
	    	Iterator<Taxonomy> itr = subjectList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		Taxonomy subject = itr.next();
//    			Logger.debug("add subject: " + subject.name + ", with url: " + subject.url +
//    					", parent:" + subject.parent + ", parent size: " + subject.parent.length());
	    		if ((parent && subject.parent.length() == 0) || !parent) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + subject.name + "\"," + checkSubjectSelection(subject.url, targetUrl) + 
							" \"key\": \"" + subject.url + "\"" + 
							getSubjectChildren(subject.url, targetUrl) + "}");
	    		}
	    	}
//	    	Logger.info("subjectList level size: " + subjectList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.info("getSubjectTreeElements() res: " + res);
    	}
    	return res;
    }
        
    /**
     * This method computes a tree of subjects in JSON format. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSubjectTree(String targetUrl) {
    	Logger.info("getSubjectTree() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
//    	Logger.info("getSubjectTree() parentSubjects: " + parentSubjects.size());
    	sb.append(getSubjectTreeElements(parentSubjects, targetUrl, true));
//    	Logger.info("subjects main level size: " + parentSubjects.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getSubjectTree() json: " + jsonData.toString());
        return ok(jsonData);
    }        
    
}


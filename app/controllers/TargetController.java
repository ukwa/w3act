package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import models.CrawlPermission;
import models.DCollection;
import models.Flag;
import models.FlashMessage;
import models.Organisation;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.User;
import models.WatchedTarget;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.exception.WhoisException;
import uk.bl.scope.Scope;
import views.html.licence.ukwalicenceresult;
import views.html.infomessage;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import views.html.targets.edit;


/**
 * Describe W3ACT project.
 */
@Security.Authenticated(Secured.class)
public class TargetController extends AbstractController {
	
	private static String collectionsJsonData;
	private static String subjectsJsonData;
	
	/**
	 * This method prepares Target form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
	    Target targetObj = new Target();
        try {
    	    Target target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
        	if (getFormParam(Const.FIELD_WCT_ID) != null && !getFormParam(Const.FIELD_WCT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
            	targetObj.field_wct_id = target.field_wct_id;
            }
        	if (getFormParam(Const.FIELD_SPT_ID) != null && !getFormParam(Const.FIELD_SPT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
            	targetObj.field_spt_id = target.field_spt_id;
        	}
        	if (getFormParam(Const.LEGACY_SITE_ID) != null && !getFormParam(Const.LEGACY_SITE_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
            	targetObj.legacy_site_id = target.legacy_site_id;
            }
        } catch (Exception e) {
        	Logger.info("The target for given NID is not yet existing in database");
        } 	

	    targetObj.field_url = getFormParam(Const.FIELD_URL_NODE);
	    targetObj.nid = Long.valueOf(getFormParam(Const.NID));
	    targetObj.url = Const.ACT_URL + targetObj.nid;
        targetObj.author = getFormParam(Const.USER);
        targetObj.title = getFormParam(Const.TITLE);
        targetObj.field_key_site = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
        targetObj.field_description = getFormParam(Const.DESCRIPTION);
        if (getFormParam(Const.FLAG_NOTES) != null) {
        	targetObj.flag_notes = getFormParam(Const.FLAG_NOTES);
        } 
        if (getFormParam(Const.FIELD_QA_STATUS) != null) {
        	targetObj.field_qa_status = Taxonomy.findByNameExt(getFormParam(Const.FIELD_QA_STATUS)).url;
        } 
//        if (getFormParam(Const.STATUS) != null) {
//        	targetObj.status = Long.valueOf(getFormParam(Const.STATUS));
//        } 
        if (getFormParam(Const.QA_STATUS) != null) {
        	targetObj.qa_status = getFormParam(Const.QA_STATUS);
        } else {
        	targetObj.qa_status = Const.NONE_VALUE;
        }
        if (getFormParam(Const.LANGUAGE) != null) {
        	targetObj.language = getFormParam(Const.LANGUAGE);
        } 
        if (getFormParam(Const.SELECTION_TYPE) != null) {
        	targetObj.selection_type = getFormParam(Const.SELECTION_TYPE);
        } 
        if (getFormParam(Const.SELECTOR_NOTES) != null) {
        	targetObj.selector_notes = getFormParam(Const.SELECTOR_NOTES);
        } 
        if (getFormParam(Const.ARCHIVIST_NOTES) != null) {
        	targetObj.archivist_notes = getFormParam(Const.ARCHIVIST_NOTES);
        } 
        if (getFormParam(Const.LEGACY_SITE_ID) != null 
        		&& getFormParam(Const.LEGACY_SITE_ID).length() > 0
        		&& Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
        	targetObj.legacy_site_id = Long.valueOf(getFormParam(Const.LEGACY_SITE_ID));
        }
        if (getFormParam(Const.AUTHORS) != null) {
        	targetObj.authors = getFormParam(Const.AUTHORS);
        } 
        if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
        	targetObj.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
        } 
        if (getFormParam(Const.FIELD_SUBJECT) != null) {
        	String subjectListStr = Utils.removeDuplicatesFromList(getFormParam(Const.FIELD_SUBJECT));
        	if (subjectListStr != null && subjectListStr.length() > 0
        			&& subjectListStr.toLowerCase().contains(Const.NONE)
        			&& subjectListStr.contains(Const.COMMA)) {
        	    targetObj.field_subject = Const.NONE;
        	} else {
        		targetObj.field_subject = subjectListStr;
        	}
//        	targetObj.updateSubject();
    		Logger.debug("targetObj.field_subject: " + targetObj.field_subject);
        } else {
        	targetObj.field_subject = Const.NONE;
        }
        if (getFormParam(Const.TREE_KEYS) != null) {
    		targetObj.field_collection_categories = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
        	targetObj.collection_to_target = DCollection.convertUrlsToObjects(targetObj.field_collection_categories);
//    		targetObj.updateCollection();
        }
        if (getFormParam(Const.ORGANISATION) != null) {
        	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
        		targetObj.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
        		targetObj.updateOrganisation();
        	} else {
        		targetObj.field_nominating_organisation = Const.NONE;
        	}
        }
        if (getFormParam(Const.ORIGINATING_ORGANISATION) != null) {
       		targetObj.originating_organisation = getFormParam(Const.ORIGINATING_ORGANISATION);
        }
        if (getFormParam(Const.AUTHOR) != null) {
       		targetObj.author = User.findByName(getFormParam(Const.AUTHOR)).url;
        }
        if (getFormParam(Const.TAGS) != null) {
        	if (!getFormParam(Const.TAGS).toLowerCase().contains(Const.NONE)) {
            	String[] tags = getFormParams(Const.TAGS);
            	String resTags = "";
            	for (String tag: tags)
                {
            		if (tag != null && tag.length() > 0) {
            			resTags = resTags + Tag.findByName(tag).url + Const.LIST_DELIMITER;
            		}
                }
            	targetObj.tags = resTags;
            	targetObj.tag_to_target = Tag.convertUrlsToObjects(targetObj.tags);
        	} else {
        		targetObj.tags = Const.NONE;
        	}
        }
        String flagStr = "";
        List<Flag> flagList = Flag.findAll();
        Iterator<Flag> flagItr = flagList.iterator();
        while (flagItr.hasNext()) {
        	Flag flag = flagItr.next();
//        	Logger.info("flag: " + flag + ", param: " + getFormParam(flag.name));
            if (getFormParam(flag.name) != null) {
                boolean flagFlag = Utils.getNormalizeBooleanString(getFormParam(flag.name));
//            	Logger.info("flagFlag: " + flagFlag);
                if (flagFlag) {
                	if (flagStr.length() == 0) {
                		flagStr = flag.name;
                	} else {
                		flagStr = flagStr + ", " + flag.name;
                	}
                }
            }
        }
        if (flagStr.length() == 0) {
        	targetObj.flags = Const.NONE;
        } else {
        	targetObj.flags = flagStr;
        	targetObj.flag_to_target = Flag.convertUrlsToObjects(targetObj.flags);
        }
        Logger.info("flagStr: "+ flagStr + ", targetObj.flags: " + targetObj.flags);
        targetObj.justification = getFormParam(Const.JUSTIFICATION);
        targetObj.summary = getFormParam(Const.SUMMARY);
        targetObj.revision = getFormParam(Const.REVISION);
        if (getFormParam(Const.FIELD_WCT_ID) != null 
        		&& getFormParam(Const.FIELD_WCT_ID).length() > 0
        		&& Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
        	targetObj.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
        }
        if (getFormParam(Const.FIELD_SPT_ID) != null 
        		&& getFormParam(Const.FIELD_SPT_ID).length() > 0
        		&& Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
        	targetObj.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
        }
        if (getFormParam(Const.FIELD_LICENSE) != null) {
        	if (!getFormParam(Const.FIELD_LICENSE).toLowerCase().contains(Const.NONE)) {
            	String[] licenses = getFormParams(Const.FIELD_LICENSE);
            	String resLicenses = "";
            	for (String curLicense: licenses)
                {
            		if (curLicense != null && curLicense.length() > 0) {
            			resLicenses = resLicenses + Taxonomy.findByFullNameExt(curLicense, Const.LICENCE).url + Const.LIST_DELIMITER;
            		}
                }
            	targetObj.field_license = resLicenses;
            	targetObj.license_to_target = Taxonomy.convertUrlsToObjects(targetObj.field_license);
//        		targetObj.updateLicense();
        	} else {
        		targetObj.field_license = Const.NONE;
        	}
        }
        targetObj.field_uk_hosting = Target.checkUkHosting(targetObj.field_url);
        targetObj.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
        targetObj.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
        targetObj.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
        targetObj.value = getFormParam(Const.FIELD_NOTES);
        targetObj.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
        targetObj.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
        targetObj.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
        targetObj.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
        if (getFormParam(Const.FIELD_CRAWL_START_DATE) != null) {
        	String startDateHumanView = getFormParam(Const.FIELD_CRAWL_START_DATE);
        	String startDateUnix = Utils.getUnixDateStringFromDate(startDateHumanView);
        	targetObj.field_crawl_start_date = startDateUnix;
        }
        targetObj.date_of_publication = getFormParam(Const.DATE_OF_PUBLICATION);
        targetObj.field_crawl_end_date = getFormParam(Const.FIELD_CRAWL_END_DATE);
        if (getFormParam(Const.FIELD_CRAWL_END_DATE) != null) {
        	String endDateHumanView = getFormParam(Const.FIELD_CRAWL_END_DATE);
        	String endDateUnix = Utils.getUnixDateStringFromDate(endDateHumanView);
        	targetObj.field_crawl_end_date = endDateUnix;
        }
        targetObj.white_list = getFormParam(Const.WHITE_LIST);
        targetObj.black_list = getFormParam(Const.BLACK_LIST);
        if (getFormParam(Const.FIELD_DEPTH) != null) {
        	targetObj.field_depth = Targets.getDepthNameFromGuiName(getFormParam(Const.FIELD_DEPTH));
        }
        targetObj.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
        if (getFormParam(Const.FIELD_SCOPE) != null) {
        	targetObj.field_scope = Targets.getScopeNameFromGuiName(getFormParam(Const.FIELD_SCOPE));
        }
        targetObj.keywords = getFormParam(Const.KEYWORDS);
        targetObj.synonyms = getFormParam(Const.SYNONYMS);
        if (getFormParam(Const.TAB_STATUS) != null) {
        	targetObj.tabstatus = getFormParam(Const.TAB_STATUS);
        }
	    targetObj.active = true;
	    if (getFormParam("watched") != null) {
	    	targetObj.watchedTarget = new WatchedTarget(targetObj, getFormParam("watchedTarget.documentUrlScheme"));
	    }
		Form<Target> targetFormNew = Form.form(Target.class);
		targetFormNew = targetFormNew.fill(targetObj);
      	return ok(
	              edit.render(targetFormNew, User.findByEmail(request().username()))
	            );
    }

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
        String request = getFormParam(Const.REQUEST);
        String archive = getFormParam(Const.ARCHIVE);
        boolean watched = getFormParam("watched") != null;
        Logger.info("save: " + save);
        Logger.info("delete: " + delete);
        if (save != null) {
        	Logger.info("input data for the target saving nid: " + getFormParam(Const.NID) + 
        			", url: " + getFormParam(Const.URL) +
        			", field_subject: " + getFormParam(Const.FIELD_SUBJECT) + 
        			", field_url: " + getFormParam(Const.FIELD_URL_NODE) + 
        			", title: " + getFormParam(Const.TITLE) + ", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", qa status: " + getFormParam(Const.QA_STATUS) +
        			", subject: " + getFormParams(Const.SUBJECT) +
        			", organisation: " + getFormParam(Const.ORGANISATION) +
        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
        	Logger.info("treeKeys: " + getFormParam(Const.TREE_KEYS));
        	Logger.info("current tab: " + getFormParam(Const.TAB_STATUS));
        	
        	Form<Target> targetForm = Form.form(Target.class).bindFromRequest();
            if(targetForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : targetForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	if (missingFields != null) {
	            	Logger.info("form errors size: " + targetForm.errors().size() + ", " + missingFields);
		  			flash("message", "Please fill out all the required fields, marked with a red star. There are required fields in more than one tab. " + 
		  					"Missing fields are: " + missingFields);
		  			return info();
            	}
            }
        	        	
        	DynamicForm requestData = Form.form().bindFromRequest();
        	String title = requestData.get(Const.TITLE);
        	Logger.info("form title: " + title);
            Target target = new Target();
        	Target newTarget = new Target();
            boolean isExisting = true;
            try {
        	    target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
            } catch (Exception e) {
            	Logger.info("is not existing exception");
            	isExisting = false;
            } 	
        	
//            Logger.info("wct id: " + getFormParam(Const.FIELD_WCT_ID) +
//            		", isNumeric: " + Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID)));
            if (getFormParam(Const.FIELD_WCT_ID) != null 
            		&& (getFormParam(Const.FIELD_WCT_ID).equals("")
            		|| !Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID)))) {
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

            if (target == null) {
            	target = new Target();
            	Logger.info("is not existing");
            	isExisting = false;
            }
            newTarget.nid = Target.createId();
            newTarget.url = target.url;
            /**
             * Copy association fields
             */
            newTarget.organisation_to_target = target.organisation_to_target;
            
            newTarget.author = target.author;
            if (target.author == null) {
            	newTarget.author = getFormParam(Const.USER);
            }
            newTarget.field_nominating_organisation = target.field_nominating_organisation;
    		newTarget.updateOrganisation();
            newTarget.field_collection_categories = target.field_collection_categories;
            newTarget.title = getFormParam(Const.TITLE);
            newTarget.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL_NODE));
            newTarget.field_key_site = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
            newTarget.field_description = getFormParam(Const.DESCRIPTION);
            if (getFormParam(Const.FLAG_NOTES) != null) {
            	newTarget.flag_notes = getFormParam(Const.FLAG_NOTES);
            } 
            if (getFormParam(Const.FIELD_QA_STATUS) != null) {
            	newTarget.field_qa_status = Taxonomy.findByNameExt(getFormParam(Const.FIELD_QA_STATUS)).url;
            } 
            if (getFormParam(Const.QA_STATUS) != null) {
            	Logger.debug("###   QA_STATUS");
            	newTarget.qa_status = getFormParam(Const.QA_STATUS);
            	CrawlPermissions.updateAllByTargetStatusChange(newTarget.field_url, newTarget.qa_status);
            } else {
            	newTarget.qa_status = Const.NONE_VALUE;
            } 
    		Logger.info("QA status: " + newTarget.qa_status + ", getFormParam(Const.QA_STATUS): " + getFormParam(Const.QA_STATUS));
            if (getFormParam(Const.LANGUAGE) != null) {
//        		Logger.info("language: " + getFormParam(Const.LANGUAGE) + ".");
            	newTarget.language = getFormParam(Const.LANGUAGE);
            } 
            if (getFormParam(Const.SELECTION_TYPE) != null) {
            	newTarget.selection_type = getFormParam(Const.SELECTION_TYPE);
            } 
            if (getFormParam(Const.SELECTOR_NOTES) != null) {
            	newTarget.selector_notes = getFormParam(Const.SELECTOR_NOTES);
            } 
            if (getFormParam(Const.ARCHIVIST_NOTES) != null) {
            	newTarget.archivist_notes = getFormParam(Const.ARCHIVIST_NOTES);
            } 
            if (getFormParam(Const.LEGACY_SITE_ID) != null 
            		&& getFormParam(Const.LEGACY_SITE_ID).length() > 0
            		&& Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
        		Logger.info("legacy site id: " + getFormParam(Const.LEGACY_SITE_ID) + ".");
            	newTarget.legacy_site_id = Long.valueOf(getFormParam(Const.LEGACY_SITE_ID));
            }

    		Logger.info("authors: " + getFormParam(Const.AUTHORS) + ".");
            if (getFormParam(Const.AUTHORS) != null) {
            	newTarget.authors = getFormParam(Const.AUTHORS);
            } 
            if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
            	newTarget.field_live_site_status = getFormParam(Const.LIVE_SITE_STATUS);
            } 
            if (getFormParam(Const.FIELD_SUBJECT) != null) {
            	String subjectListStr = Utils.removeDuplicatesFromList(getFormParam(Const.FIELD_SUBJECT));
            	if (subjectListStr != null && subjectListStr.length() > 0
            			&& subjectListStr.toLowerCase().contains(Const.NONE)
            			&& subjectListStr.contains(Const.COMMA)) {
            		if (subjectListStr.contains(Const.NONE_VALUE + Const.COMMA + " ")) {
            			subjectListStr = subjectListStr.replace(Const.NONE_VALUE + Const.COMMA + " ", "");
            		}
            		if (subjectListStr.contains(Const.COMMA + " " + Const.NONE_VALUE)) {
            			subjectListStr = subjectListStr.replace(Const.COMMA + " " + Const.NONE_VALUE, "");
            		}
            		Logger.info("after removing 'None' value is it was combined with another subject");      		
            	}
            	newTarget.field_subject = subjectListStr;
            	newTarget.subject_to_target = Taxonomy.convertUrlsToObjects(newTarget.field_subject);
        		Logger.debug("newTarget.field_subject: " + newTarget.field_subject);
            } else {
            	newTarget.field_subject = Const.NONE;
            }            
            if (getFormParam(Const.TREE_KEYS) != null) {
	    		newTarget.field_collection_categories = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
	        	newTarget.collection_to_target = DCollection.convertUrlsToObjects(newTarget.field_collection_categories);
	    		Logger.debug("newTarget.field_collection_categories: " + newTarget.field_collection_categories);
            }
            if (getFormParam(Const.ORGANISATION) != null) {
            	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
            		Logger.info("nominating organisation: " + getFormParam(Const.ORGANISATION));
            		newTarget.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
            		newTarget.updateOrganisation();
            	} else {
            		newTarget.field_nominating_organisation = Const.NONE;
            	}
            }
            if (getFormParam(Const.ORIGINATING_ORGANISATION) != null) {
           		newTarget.originating_organisation = getFormParam(Const.ORIGINATING_ORGANISATION);
            }
//    		Logger.info("author: " + getFormParam(Const.AUTHOR) + ", user: " + User.findByName(getFormParam(Const.AUTHOR)).url);
            if (getFormParam(Const.AUTHOR) != null) {
           		newTarget.author = User.findByName(getFormParam(Const.AUTHOR)).url;
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
	            	newTarget.tags = resTags;
                	newTarget.tag_to_target = Tag.convertUrlsToObjects(newTarget.tags);
            	} else {
            		newTarget.tags = Const.NONE;
            	}
            }
            String flagStr = "";
            List<Flag> flagList = Flag.findAll();
            Iterator<Flag> flagItr = flagList.iterator();
            while (flagItr.hasNext()) {
            	Flag flag = flagItr.next();
//            	Logger.info("flag: " + flag + ", param: " + getFormParam(flag.name));
                if (getFormParam(flag.name) != null) {
                    boolean flagFlag = Utils.getNormalizeBooleanString(getFormParam(flag.name));
//                	Logger.info("flagFlag: " + flagFlag);
                    if (flagFlag) {
                    	if (flagStr.length() == 0) {
                    		flagStr = flag.name;
                    	} else {
                    		flagStr = flagStr + ", " + flag.name;
                    	}
                    }
                }
            }
            if (flagStr.length() == 0) {
            	newTarget.flags = Const.NONE;
            } else {
            	newTarget.flags = flagStr;
            	newTarget.flag_to_target = Flag.convertUrlsToObjects(newTarget.flags);
            }
            Logger.info("flagStr: "+ flagStr + ", newTarget.flags: " + newTarget.flags);
            newTarget.justification = getFormParam(Const.JUSTIFICATION);
            newTarget.summary = getFormParam(Const.SUMMARY);
            newTarget.revision = getFormParam(Const.REVISION);
            if (getFormParam(Const.FIELD_WCT_ID) != null 
            		&& getFormParam(Const.FIELD_WCT_ID).length() > 0
            		&& Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
            	newTarget.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
            }
            if (getFormParam(Const.FIELD_SPT_ID) != null 
            		&& getFormParam(Const.FIELD_SPT_ID).length() > 0
            		&& Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
            	newTarget.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
            }
            if (getFormParam(Const.FIELD_LICENSE) != null) {
            	if (!getFormParam(Const.FIELD_LICENSE).toLowerCase().contains(Const.NONE)) {
	            	String[] licenses = getFormParams(Const.FIELD_LICENSE);
	            	String resLicenses = "";
	            	for (String curLicense: licenses)
	                {
	            		if (curLicense != null && curLicense.length() > 0) {
	                		Logger.info("add curLicense: " + curLicense);
	                		if (curLicense.equals(Const.OPEN_UKWA_LICENSE) 
	                				&& getFormParam(Const.QA_STATUS) != null 
	                				&& !getFormParam(Const.QA_STATUS).equals(Const.CrawlPermissionStatus.GRANTED.name())) {
	                        	Logger.info("Saving is not allowed if License='Open UKWA License (2014-)' and Open UKWA License Requests status is anything other than 'Granted'.");
	            	  			flash("message", "Saving is not allowed if License='Open UKWA License (2014-)' and Open UKWA License Requests status is anything other than 'Granted'.");
	            	  			return info();
	                		}
	            			resLicenses = resLicenses + Taxonomy.findByFullNameExt(curLicense, Const.LICENCE).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newTarget.field_license = resLicenses;
                	newTarget.license_to_target = Taxonomy.convertUrlsToObjects(newTarget.field_license);
            	} else {
            		newTarget.field_license = Const.NONE;
            	}
            }
            newTarget.field_uk_hosting = Target.checkUkHosting(newTarget.field_url);
        	Logger.debug("field_uk_hosting: " + newTarget.field_uk_hosting);
            newTarget.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
            newTarget.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
            Logger.debug("newTarget.field_uk_postal_address: " + newTarget.field_uk_postal_address);
            if (newTarget.field_uk_postal_address 
            		&& (newTarget.field_uk_postal_address_url == null || newTarget.field_uk_postal_address_url.length() == 0)) {
            	Logger.info("If UK Postal Address field has value 'Yes', the Postal Address URL is required.");
	  			flash("message", "If UK Postal Address field has value 'Yes', the Postal Address URL is required.");
	  			return info();
            }
            newTarget.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
            newTarget.value = getFormParam(Const.FIELD_NOTES);
            if (newTarget.field_via_correspondence 
            		&& (newTarget.value == null || newTarget.value.length() == 0)) {
            	Logger.info("If Via Correspondence field has value 'Yes', the Notes field is required.");
	  			flash("message", "If Via Correspondence field has value 'Yes', the Notes field is required.");
	  			return info();
            }
            newTarget.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
            newTarget.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
            Logger.debug("newTarget.field_professional_judgement: " + newTarget.field_professional_judgement);
            if (newTarget.field_professional_judgement 
            		&& (newTarget.field_professional_judgement_exp == null || newTarget.field_professional_judgement_exp.length() == 0)) {
            	Logger.info("If Professional Judgement field has value 'Yes', the Professional Judgment Explanation field is required.");
	  			flash("message", "If Professional Judgement field has value 'Yes', the Professional Judgment Explanation field is required.");
	  			return info();
            }
            newTarget.field_no_ld_criteria_met = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
//            Logger.info("ignore robots: " + getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
            newTarget.field_ignore_robots_txt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));
            if (getFormParam(Const.FIELD_CRAWL_START_DATE) != null) {
            	String startDateHumanView = getFormParam(Const.FIELD_CRAWL_START_DATE);
            	String startDateUnix = Utils.getUnixDateStringFromDate(startDateHumanView);
            	Logger.info("startDateHumanView: " + startDateHumanView + ", startDateUnix: " + startDateUnix);
            	newTarget.field_crawl_start_date = startDateUnix;
            }
            newTarget.date_of_publication = getFormParam(Const.DATE_OF_PUBLICATION);
            newTarget.field_crawl_end_date = getFormParam(Const.FIELD_CRAWL_END_DATE);
            if (getFormParam(Const.FIELD_CRAWL_END_DATE) != null) {
            	String endDateHumanView = getFormParam(Const.FIELD_CRAWL_END_DATE);
            	String endDateUnix = Utils.getUnixDateStringFromDate(endDateHumanView);
            	Logger.info("endDateHumanView: " + endDateHumanView + ", endDateUnix: " + endDateUnix);
            	newTarget.field_crawl_end_date = endDateUnix;
            }
            newTarget.white_list = getFormParam(Const.WHITE_LIST);
            newTarget.black_list = getFormParam(Const.BLACK_LIST);
            if (getFormParam(Const.FIELD_DEPTH) != null) {
            	newTarget.field_depth = Targets.getDepthNameFromGuiName(getFormParam(Const.FIELD_DEPTH));
            }
            newTarget.field_crawl_frequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
            if (getFormParam(Const.FIELD_SCOPE) != null) {
            	newTarget.field_scope = Targets.getScopeNameFromGuiName(getFormParam(Const.FIELD_SCOPE));
            }
            newTarget.keywords = getFormParam(Const.KEYWORDS);
            newTarget.synonyms = getFormParam(Const.SYNONYMS);
            newTarget.active = true;
            long unixTime = System.currentTimeMillis() / 1000L;
            String changedTime = String.valueOf(unixTime);
            Logger.info("changed time: " + changedTime);
            
            ExpressionList<Target> expressionList = Target.find.where()
            		.eq(Const.FIELD_URL_NODE, newTarget.field_url)
            		.eq(Const.ACTIVE, true);
            		if (isExisting)
            		expressionList = expressionList.ne("id", target.nid);
            boolean urlExists = expressionList.findRowCount() > 0;
            if (watched && urlExists) {
            	new FlashMessage(FlashMessage.Type.ERROR, "Can't create a watched target with an URL that is not unique.").send();
            	return info();
            }
            
        	if (!isExisting) {
        		newTarget.url = Const.ACT_URL + newTarget.nid;
        		newTarget.edit_url = Const.WCT_URL + newTarget.nid;
        	} else {
                target.active = false;
            	if (target.field_url != null) {
                	Logger.info("current target field_url: " + target.field_url);
            		target.domain = Scope.getDomainFromUrl(target.field_url);
            	}
            	target.changed = changedTime;
        		Logger.info("update target: " + target.nid + ", obj: " + target.toString());
                boolean newScope = Target.isInScopeIp(target.field_url, target.url);
                // TODO: save new entry or update current
            	Scope.updateLookupEntry(target, newScope);
                /**
                 * Reset association fields
                 */
                target.organisation_to_target = null;
                target.collection_to_target = null;
                target.subject_to_target = null;
                Utils.removeAssociationFromDb(Const.SUBJECT_TARGET, Const.ID + "_" + Const.TARGET, target.nid);
                Utils.removeAssociationFromDb(Const.COLLECTION_TARGET, Const.ID + "_" + Const.TARGET, target.nid);
                Utils.removeAssociationFromDb(Const.LICENSE_TARGET, Const.ID + "_" + Const.TARGET, target.nid);
                Utils.removeAssociationFromDb(Const.FLAG_TARGET, Const.ID + "_" + Const.TARGET, target.nid);
                Utils.removeAssociationFromDb(Const.TAG_TARGET, Const.ID + "_" + Const.TARGET, target.nid);
                target.flag_to_target = null;
                target.tag_to_target = null;
                Logger.info("+++ subject_to_target object before target nid: " + target.nid + ", update: " + target.subject_to_target);
            	Ebean.update(target);
            	if (!watched && target.watchedTarget != null) {
            		WatchedTarget watchedTarget = WatchedTarget.find.byId(target.watchedTarget.id);
            		Documents.deleteHtmlFiles(watchedTarget);
            		Ebean.delete(watchedTarget);
            	}
        	}
        	if (newTarget.field_url != null) {
            	Logger.info("current target field_url: " + newTarget.field_url);
        		newTarget.domain = Scope.getDomainFromUrl(newTarget.field_url);
        	}
        	newTarget.changed = changedTime;
        	if (newTarget.created == null || newTarget.created.length() == 0) {
        		if (target != null && target.created != null && target.created.length() > 0) {
        			Logger.info("The creation time remains the same like in original revision of the target: " + target.created);
        			newTarget.created = target.created;
        		} else {
        			newTarget.created = changedTime;
        		}
        	}
            boolean newScope = Target.isInScopeIp(newTarget.field_url, newTarget.url);
            // TODO: save new entry or update current
            Scope.updateLookupEntry(newTarget, newScope);
        	
        	/**
        	 * NPLD scope values
        	 */
        	newTarget.isInScopeUkRegistrationValue   = Target.isInScopeUkRegistration(newTarget.field_url);
        	newTarget.isInScopeDomainValue           = Target.isInScopeDomain(newTarget.field_url, newTarget.url);
        	newTarget.isUkHostingValue               = Target.checkUkHosting(newTarget.field_url);
        	newTarget.isInScopeIpValue               = newScope;
        	newTarget.isInScopeIpWithoutLicenseValue = Target.isInScopeIpWithoutLicense(newTarget.field_url, newTarget.url);
        	
        	List<Taxonomy> subjects = newTarget.subject_to_target;
        	Iterator<Taxonomy> itrSubjects = subjects.iterator();
        	while (itrSubjects.hasNext()) {
        		Logger.info("+++ subject_to_target before target save: " + itrSubjects.next().toString());
        	}
        	Ebean.save(newTarget);
        	if (watched) {
        		if (target.watchedTarget != null) {
        			target.watchedTarget.target = newTarget;
        			target.watchedTarget.documentUrlScheme = getFormParam("watchedTarget.documentUrlScheme");
        			Ebean.update(target.watchedTarget);
        		}
        		else {
        			Ebean.save(new WatchedTarget(newTarget, getFormParam("watchedTarget.documentUrlScheme")));
        		}
        	}
        	try {
	            /**
	             * Create or update association between CrawlPermission and Target
	             */
	            CrawlPermission crawlPermission = CrawlPermission.findByTarget(newTarget.field_url);
	            crawlPermission.updateTarget();
	            Ebean.update(crawlPermission);
        	} catch (Exception e) {
        		Logger.info("No crawl permission to update for URL: " + newTarget.field_url);
        	}
	        Logger.info("Your changes have been saved: " + newTarget.toString());
	        FlashMessage.updateSuccess.send();
	        res = redirect(routes.Targets.view(newTarget.url) + getFormParam(Const.TAB_STATUS));
        } // end of save
        else if (delete != null) {
        	Long id = Long.valueOf(getFormParam(Const.NID));
        	Logger.info("deleting: " + id);
        	Target target = Target.findById(id);
        	if (target.watchedTarget != null) Documents.deleteHtmlFiles(target.watchedTarget);
        	Ebean.delete(target);
	        res = redirect(routes.Targets.index()); 
        }
        else if (request != null) {
            Logger.debug("request permission for title: " + getFormParam(Const.TITLE) + 
            		" and target: " + getFormParam(Const.FIELD_URL_NODE));
        	if (getFormParam(Const.TITLE) != null && getFormParam(Const.FIELD_URL_NODE) != null) {
                String name = getFormParam(Const.TITLE);
                String target = Scope.normalizeUrl(getFormParam(Const.FIELD_URL_NODE));
    	        res = redirect(routes.CrawlPermissions.licenceRequestForTarget(name, target)); 
        	}
        }
        else if (archive != null) {
            Logger.debug("archive target title: " + getFormParam(Const.TITLE) + 
            		" with URL: " + getFormParam(Const.FIELD_URL_NODE));
        	if (getFormParam(Const.FIELD_URL_NODE) != null) {
                String target = Scope.normalizeUrl(getFormParam(Const.FIELD_URL_NODE));
    	        res = redirect(routes.TargetController.archiveTarget(target)); 
        	}
        }
        return res;
    }
	
    /**
     * This method pushes a message onto a RabbitMQ queue for given target
     * using global settings from project configuration file.
     * @param target The field URL of the target
     * @return
     */
    public static Result archiveTarget(String url) {    	
    	Logger.debug("archiveTarget() " + url);
    	if (url != null && url.length() > 0) {
    		try {
		    	String queueHost = Play.application().configuration().getString(Const.QUEUE_HOST);
		    	String queuePort = Play.application().configuration().getString(Const.QUEUE_PORT);
		    	String queueName = Play.application().configuration().getString(Const.QUEUE_NAME);
		    	String routingKey = Play.application().configuration().getString(Const.ROUTING_KEY);
		    	String exchangeName = Play.application().configuration().getString(Const.EXCHANGE_NAME);
	
		    	Logger.debug("archiveTarget() queue host: " + queueHost);
		      	Logger.debug("archiveTarget() queue port: " + queuePort);
		      	Logger.debug("archiveTarget() queue name: " + queueName);
		      	Logger.debug("archiveTarget() routing key: " + routingKey);
		      	Logger.debug("archiveTarget() exchange name: " + exchangeName);
		      	
		      	Target target = Target.findByUrl(url);
		      	JsonNode jsonData = Json.toJson(target);
		      	String message = jsonData.toString();
		      	Logger.debug("Crawl Now message: " + message);
		      	
		      	ConnectionFactory factory = new ConnectionFactory();
		    	if (queueHost != null) {
		    		factory.setHost(queueHost);
		    	}
		    	if (queuePort != null) {
		    		factory.setPort(Integer.parseInt(queuePort));
		    	}
		    	Connection connection = factory.newConnection();
		    	Channel channel = connection.createChannel();
	
		    	channel.exchangeDeclare(exchangeName, "direct", true);
		    	channel.queueDeclare(queueName, true, false, false, null);
		    	channel.queueBind(queueName, exchangeName, routingKey);
		    	channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
		    	Logger.debug(" ### sent target '" + message + "' to queue");    	    
		    	channel.close();
		    	connection.close();
	    	
	    	} catch (IOException e) {
	    		String msg = "There was a problem queuing this crawl instruction. Please refer to the system administrator.";
	    		User currentUser = User.findByEmail(request().username());
    	    	StringWriter sw = new StringWriter();
    	    	e.printStackTrace(new PrintWriter(sw));
    	    	String msgFullTrace = sw.toString();
	    		Logger.error(msgFullTrace);
	    	    if (currentUser.hasRole("sys_admin")) {
	    	    	msg = msgFullTrace;
	    	    }
	            return ok(infomessage.render(msg));
	    	} catch (Exception e) {
	    		String msg = "There was a problem queuing this crawl instruction. Please refer to the system administrator.";
	    		User currentUser = User.findByEmail(request().username());
    	    	StringWriter sw = new StringWriter();
    	    	e.printStackTrace(new PrintWriter(sw));
    	    	String msgFullTrace = sw.toString();
	    		Logger.error(msgFullTrace);
	    	    if (currentUser.hasRole("sys_admin")) {
	    	    	msg = msgFullTrace;
	    	    }
	            return ok(infomessage.render(msg));
	    	}    	      
    	} else {
    		Logger.debug("There was a problem sending the message. Target field for archiving is empty");
            return ok(infomessage.render("There was a problem sending the message. Target field for archiving is empty"));
    	}
		return ok(
	            ukwalicenceresult.render()
	        );
    }
          
    /**
     * This method is checking scope for given URL and returns result in JSON format.
     * @param url
     * @return JSON result
     * @throws WhoisException 
     */
    public static Result isInScope(String url) throws WhoisException {
//    	Logger.info("isInScope controller: " + url);
    	boolean res = Target.isInScope(url, null);
//    	Logger.info("isInScope res: " + res);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method calculates collection children - objects that have parents.
     * @param url The identifier for parent 
     * @param targetUrl This is an identifier for current target object
     * @return child collection in JSON form
     */
    public static String getChildren(String url, String targetUrl) {
//    	Logger.info("getChildren() target URL: " + targetUrl);
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
     * @param targetUrl This is an identifier for current target object
     * @return
     */
    public static String checkSelection(String collectionUrl, String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		Target target = Target.findByUrl(targetUrl);
    		if (target.field_collection_categories != null && 
    				target.field_collection_categories.contains(collectionUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
     * Mark preselected collections as selected in filter
     * @param collectionUrl The collection identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
    public static String checkSelectionFilter(String collectionUrl, String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		if (collectionUrl != null && targetUrl.equals(collectionUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getTreeElements(List<DCollection> collectionList, String targetUrl, boolean parent) { 
//    	Logger.info("getTreeElements() target URL: " + targetUrl);
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
	    		if ((parent && collection.parent.length() == 0) || !parent || collection.parent.equals(Const.NONE_VALUE)) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.title + "\"," + checkSelection(collection.url, targetUrl) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, targetUrl) + 
							"}");
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
   	 * This method calculates first order collections for filtering.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getTreeElementsFilter(List<DCollection> collectionList, String targetUrl, boolean parent) { 
//    	Logger.info("getTreeElements() target URL: " + targetUrl);
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
	    		if ((parent && collection.parent.length() == 0) || !parent || collection.parent.equals(Const.NONE_VALUE)) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.title + "\"," + checkSelectionFilter(collection.url, targetUrl) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, targetUrl) + 
							"}");
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
     * This method computes a tree of collections in JSON format. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollections(String targetUrl) {
//    	Logger.info("getSuggestedCollections() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<DCollection> suggestedCollections = DCollection.getFirstLevelCollections();
    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
//    	Logger.info("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }
    
    public static String getCollectionsJsonData(String targetUrl) {
    	if (collectionsJsonData != null) return collectionsJsonData;
        final StringBuffer sb = new StringBuffer();
    	List<DCollection> suggestedCollections = DCollection.getFirstLevelCollections();
    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
    	collectionsJsonData = sb.toString();
        return collectionsJsonData;
    }
    
    /**
     * This method computes a tree of collections in JSON format for filtering. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollectionsFilter(String targetUrl) {
//    	Logger.info("getSuggestedCollectionsFilter() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<DCollection> suggestedCollections = DCollection.getFirstLevelCollections();
    	sb.append(getTreeElementsFilter(suggestedCollections, targetUrl, true));
//    	Logger.info("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getCollections() json: " + jsonData.toString());
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
//    		if (subjectUrl != null && targetUrl.equals(subjectUrl)) {
    		Target target = Target.findByUrl(targetUrl);
    		if (target.field_subject != null && 
    				target.field_subject.contains(subjectUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
     * Mark preselected subjects as selected in filter
     * @param subjectUrl The subject identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
    public static String checkSubjectSelectionFilter(String subjectUrl, String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		if (subjectUrl != null && targetUrl.equals(subjectUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
     * Check if none value is selected in filter
     * @param subjectUrl The subject identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
    public static String checkNoneFilter(String targetUrl) {
    	String res = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		if (targetUrl.toLowerCase().contains(Const.NONE.toLowerCase())) {
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
    		Target target = Target.findByUrl(targetUrl);
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
							getSubjectChildren(subject.url, targetUrl) + 
							"}");
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
   	 * This method calculates first order subjects for targets filtering.
     * @param subjectList The list of all subjects
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getSubjectTreeElementsFilter(List<Taxonomy> subjectList, String targetUrl, boolean parent) { 
//    	Logger.info("getSubjectTreeElements() target URL: " + targetUrl);
    	String res = "";
    	if (subjectList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	        if (parent) {
	        	sb.append("{\"title\": \"" + "None" + "\"," + checkNoneFilter(targetUrl) + 
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
					sb.append("{\"title\": \"" + subject.name + "\"," + checkSubjectSelectionFilter(subject.url, targetUrl) + 
							" \"key\": \"" + subject.url + "\"" + 
							getSubjectChildren(subject.url, targetUrl) + 
							"}");
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
    	Logger.info("subjects main level size: " + parentSubjects.size());
//    	Logger.info("sb.toString(): " + sb.toString());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getSubjectTree() json: " + jsonData.toString());
        return ok(jsonData);
    }
    
    public static String getSubjectsJsonData(String targetUrl) {
    	if (subjectsJsonData != null) return subjectsJsonData;
        final StringBuffer sb = new StringBuffer();
    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
    	sb.append(getSubjectTreeElements(parentSubjects, targetUrl, true));
    	subjectsJsonData = sb.toString();
    	return subjectsJsonData;
    }  
    
    /**
     * This method computes a tree of subjects in JSON format. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSubjectTreeFilter(String targetUrl) {
    	Logger.info("getSubjectTree() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
//    	Logger.info("getSubjectTree() parentSubjects: " + parentSubjects.size());
    	sb.append(getSubjectTreeElementsFilter(parentSubjects, targetUrl, true));
    	Logger.info("subjects main level size: " + parentSubjects.size());
//    	Logger.info("sb.toString(): " + sb.toString());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getSubjectTree() json: " + jsonData.toString());
        return ok(jsonData);
    }        
    
}


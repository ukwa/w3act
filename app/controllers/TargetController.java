package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import models.DCollection;
import models.Flag;
import models.Organisation;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
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
import views.html.targets.edit;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

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
        String request = getFormParam(Const.REQUEST);
        Logger.info("save: " + save);
        Logger.info("delete: " + delete);
        if (save != null) {
        	Logger.info("save updated target nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", keysite: " + getFormParam(Const.KEYSITE) +
        			", description: " + getFormParam(Const.DESCRIPTION) + 
        			", status: " + getFormParam(Const.STATUS) +
        			", qa status: " + getFormParam(Const.QA_STATUS) +
        			", subject: " + getFormParams(Const.SUBJECT) +
        			", organisation: " + getFormParam(Const.ORGANISATION) +
        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
        	Logger.info("treeKeys: " + getFormParam(Const.TREE_KEYS));

        	DynamicForm requestData = Form.form().bindFromRequest();
        	String title = requestData.get(Const.TITLE);
        	Logger.info("form title: " + title);
        	
            Form<Target> targetForm = Form.form(Target.class).bindFromRequest();
//            targetForm.get().field_url
            /*if(targetForm.hasErrors()) {
            	Logger.info("form errors size: " + targetForm.errors().size() + ", " + targetForm.errors().toString());
//            	return badRequest(
//                        edit.render(targetForm, User.find.byId(request().username()))
//                      );
//        		return badRequest("Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
        		return badRequest("Please fill out all the required fields, marked with a red star. There are required fields in more than one tab. " +
        				"\n\nMissing fields are:\n" + targetForm.errors().toString());
//    			flash("message", "Please fill out ll the required fields, marked with a red star. There are required fields in more than one tab. " + 
//    							 targetForm.errors().toString());
//            	return ok(
//		              edit.render(targetForm, User.find.byId(request().username()))
//		            );
    	    } else {*/
//            	Target targetFormObject = targetForm.get();
//            	Logger.info("targetFormObject: " + targetFormObject);
            Target target = new Target();
        	Target newTarget = new Target();
            boolean isExisting = true;
            try {
        	    target = Target.findById(Long.valueOf(getFormParam(Const.NID)));
            } catch (Exception e) {
            	Logger.info("is not existing exception");
            	isExisting = false;
            }
        	if (StringUtils.isBlank(getFormParam(Const.TITLE)) 
        			|| StringUtils.isBlank(getFormParam(Const.FIELD_URL))
//        			|| StringUtils.isBlank(getFormParam(Const.SUBJECT))
        			|| (StringUtils.isBlank(getFormParam(Const.SUBSUBJECT)) && !User.find.byId(request().username()).hasRole(Const.USER))
        			|| (StringUtils.isBlank(getFormParam(Const.AUTHOR)) && !User.find.byId(request().username()).hasRole(Const.USER))
        			|| StringUtils.isBlank(getFormParam(Const.SELECTION_TYPE))) {
            	Logger.info("title: " + getFormParam(Const.TITLE) + ", field URL: " + getFormParam(Const.FIELD_URL) +
            			", subject: " + getFormParam(Const.SUBSUBJECT) + ", selector: " + getFormParam(Const.AUTHOR) +
            			", selection type: " + getFormParam(Const.SELECTION_TYPE));
            	Logger.info("Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
        		return badRequest("Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
//    			Logger.info("One of the required fields is empty. Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
//    			flash("message", "Please fill out all the required fields, marked with a red star. There are required fields in more than one tab.");
//    			if (isExisting) {
//    				if (target != null && target.url != null) {
//    	    			Logger.info("target.url: " + target.url);
//    					return redirect(routes.Targets.edit(target.url));
//    				} else {
//        				return redirect(routes.Targets.create(getFormParam(Const.FIELD_URL)));
//        			}
//    			} else {
//    				return redirect(routes.Targets.create(getFormParam(Const.FIELD_URL)));
//    			}
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
//            newTarget.field_collection_categories = target.field_collection_categories;
            newTarget.field_nominating_organisation = target.field_nominating_organisation;
//            Logger.info("new nid: " + newTarget.nid);
            newTarget.title = getFormParam(Const.TITLE);
            newTarget.field_url = Scope.normalizeUrl(getFormParam(Const.FIELD_URL));
            newTarget.field_key_site = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
            newTarget.field_description = getFormParam(Const.DESCRIPTION);
            if (getFormParam(Const.FLAG_NOTES) != null) {
            	newTarget.flag_notes = getFormParam(Const.FLAG_NOTES);
            } 
            if (getFormParam(Const.STATUS) != null) {
//        		Logger.info("status: " + getFormParam(Const.STATUS) + ".");
            	newTarget.status = Long.valueOf(getFormParam(Const.STATUS));
//        		Logger.info("status: " + newTarget.status + ".");
            } 
            if (getFormParam(Const.QA_STATUS) != null) {
            	Logger.debug("###   QA_STATUS");
            	newTarget.qa_status = getFormParam(Const.QA_STATUS);
            	CrawlPermissions.updateAllByTargetStatusChange(newTarget.field_url, newTarget.qa_status);
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
            if (getFormParam(Const.LEGACY_SITE_ID) != null && getFormParam(Const.LEGACY_SITE_ID).length() > 0) {
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
//            if (getFormParam(Const.SUBJECT) != null) {
//            	if (!getFormParam(Const.SUBJECT).toLowerCase().contains(Const.NONE)) {
//	            	String[] subjects = getFormParams(Const.SUBJECT);
//	//            	Logger.info("subjects: " + subjects[0] + subjects[1]);
//	            	String resSubject = "";
//	            	for (String subject: subjects)
//	                {
//	            		if (subject != null && subject.length() > 0) {
//	                		Logger.info("add subject: " + subject);
////	                		Logger.info("add subject: " + subject + ", " + Taxonomy.findByName(subject).url);
//	            			resSubject = resSubject + Taxonomy.findByFullNameExt(subject, Const.SUBJECT).url + Const.LIST_DELIMITER;
//	            		}
//	                }
//	            	newTarget.field_subject = resSubject;
//            	} else {
//            		newTarget.field_subject = Const.NONE;
//            	}
//            }
            if (getFormParam(Const.SUBSUBJECT) != null) {
            	if (!getFormParam(Const.SUBSUBJECT).toLowerCase().contains(Const.NONE)) {
	            	String[] subjects = getFormParams(Const.SUBSUBJECT);
	            	String resSubject = "";
	            	for (String subject: subjects)
	                {
	            		if (subject != null && subject.length() > 0) {
	                		Logger.info("add subsubject: " + subject);
	            			resSubject = resSubject + Taxonomy.findByFullNameExt(subject, Const.SUBSUBJECT).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newTarget.field_subsubject = resSubject;
            	} else {
            		newTarget.field_subsubject = Const.NONE;
            	}
            }
            if (getFormParam(Const.TREE_KEYS) != null) {
	    		newTarget.field_collection_categories = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
	    		Logger.debug("newTarget.field_collection_categories: " + newTarget.field_collection_categories);
            }
            if (getFormParam(Const.ORGANISATION) != null) {
            	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
            		Logger.info("nominating organisation: " + getFormParam(Const.ORGANISATION));
            		newTarget.field_nominating_organisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
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
            	} else {
            		newTarget.tags = Const.NONE;
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
	                		String origFlag = Flags.getNameFromGuiName(flag);
	                		Logger.info("original flag name: " + origFlag);
	            			resFlags = resFlags + Flag.findByName(origFlag).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newTarget.flags = resFlags;
            	} else {
            		newTarget.flags = Const.NONE;
            	}
            }
            newTarget.justification = getFormParam(Const.JUSTIFICATION);
            newTarget.summary = getFormParam(Const.SUMMARY);
            newTarget.revision = getFormParam(Const.REVISION);
            newTarget.field_wct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
            newTarget.field_spt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
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
	                    		return badRequest("Saving is not allowed if License='Open UKWA License (2014-)' and Open UKWA License Requests status is anything other than 'Granted'.");	                			
	                		}
	            			resLicenses = resLicenses + Taxonomy.findByFullNameExt(curLicense, Const.LICENCE).url + Const.LIST_DELIMITER;
	            		}
	                }
	            	newTarget.field_license = resLicenses;
            	} else {
            		newTarget.field_license = Const.NONE;
            	}
            }
//            if (getFormParam(Const.FIELD_LICENSE) != null) {
//            	Logger.info("license: " + getFormParam(Const.FIELD_LICENSE));
//            	String licenseUrl = Taxonomy.findByFullNameExt(getFormParam(Const.FIELD_LICENSE), Const.LICENCE).url;
//            	newTarget.field_license = licenseUrl;
//            }
            newTarget.field_uk_hosting = Target.checkUkHosting(newTarget.field_url);
        	Logger.debug("field_uk_hosting: " + newTarget.field_uk_hosting);
//            Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
            newTarget.field_uk_postal_address = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
            newTarget.field_uk_postal_address_url = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
            Logger.debug("newTarget.field_uk_postal_address: " + newTarget.field_uk_postal_address);
            if (newTarget.field_uk_postal_address 
            		&& (newTarget.field_uk_postal_address_url == null || newTarget.field_uk_postal_address_url.length() == 0)) {
            	Logger.info("If UK Postal Address field has value 'Yes', the Postal Address URL is required.");
        		return badRequest("If UK Postal Address field has value 'Yes', the Postal Address URL is required.");
            }
            newTarget.field_via_correspondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
            newTarget.value = getFormParam(Const.FIELD_NOTES);
            if (newTarget.field_via_correspondence 
            		&& (newTarget.value == null || newTarget.value.length() == 0)) {
            	Logger.info("If Via Correspondence field has value 'Yes', the Notes field is required.");
        		return badRequest("If Via Correspondence field has value 'Yes', the Notes field is required.");
            }
            newTarget.field_professional_judgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
            newTarget.field_professional_judgement_exp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
            Logger.debug("newTarget.field_professional_judgement: " + newTarget.field_professional_judgement);
            if (newTarget.field_professional_judgement 
            		&& (newTarget.field_professional_judgement_exp == null || newTarget.field_professional_judgement_exp.length() == 0)) {
            	Logger.info("If Professional Judgement field has value 'Yes', the Professional Judgment Explanation field is required.");
        		return badRequest("If Professional Judgement field has value 'Yes', the Professional Judgment Explanation field is required.");
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
            	Scope.updateLookupEntry(target, newScope);
            	Ebean.update(target);
        	}
        	if (newTarget.field_url != null) {
            	Logger.info("current target field_url: " + newTarget.field_url);
        		newTarget.domain = Scope.getDomainFromUrl(newTarget.field_url);
        	}
        	newTarget.changed = changedTime;
        	if (newTarget.created == null || newTarget.created.length() == 0) {
        		newTarget.created = changedTime;
        	}
            boolean newScope = Target.isInScopeIp(newTarget.field_url, newTarget.url);
        	Scope.updateLookupEntry(newTarget, newScope);
        	
        	Ebean.save(newTarget);
	        Logger.info("save target: " + newTarget.toString());
	        res = redirect(routes.Targets.edit(newTarget.url));
        } 
        //} // end of save
        if (delete != null) {
        	Long id = Long.valueOf(getFormParam(Const.NID));
        	Logger.info("deleting: " + id);
        	Target target = Target.findById(id);
        	Ebean.delete(target);
	        res = redirect(routes.Targets.index()); 
        }
        if (request != null) {
            Logger.debug("request permission for title: " + getFormParam(Const.TITLE) + 
            		" and target: " + getFormParam(Const.FIELD_URL));
        	if (getFormParam(Const.TITLE) != null && getFormParam(Const.FIELD_URL) != null) {
                String name = getFormParam(Const.TITLE);
                String target = Scope.normalizeUrl(getFormParam(Const.FIELD_URL));
    	        res = redirect(routes.CrawlPermissions.licenceRequestForTarget(name, target)); 
        	}
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
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current target object
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
     * This method computes a tree of collections in JSON format. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollections(String targetUrl) {
//    	Logger.info("getCollections()");
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<DCollection> suggestedCollections = DCollection.getFirstLevelCollections();
    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
//    	Logger.info("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }        
    
}


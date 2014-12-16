package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import models.Collection;
import models.ContactPerson;
import models.Instance;
import models.Organisation;
import models.QaIssue;
import models.Tag;
import models.Taxonomy;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.Const.QAIssueCategory;
import uk.bl.api.Utils;
import views.html.instances.edit;
import views.html.instances.list;
import views.html.instances.listByTarget;
import views.html.instances.view;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * Manage instances.
 */
@Security.Authenticated(SecuredController.class)
public class InstanceController extends AbstractController {
  
    /**
     * Display the instances.
     */
    public static Result index() {
        return GO_HOME;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String url) {
        JsonNode jsonData = null;
        if (url != null) {
	        List<Instance> instances = Instance.filterUrl(url);
	        jsonData = Json.toJson(instances);
        }
        return ok(jsonData);
    }
    
    /**
     * Display the paginated list of instances.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on instance urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {

    	User user = User.findByEmail(request().username());
    	Page<Instance> pages = Instance.page(pageNo, 10, sortBy, order, filter);
    	
        return ok(
        	list.render(
        			"Lookup", 
        			user, 
        			filter, 
        			pages,
        			sortBy, 
        			order)
        	);
    }
	
    /**
     * Display the paginated list of instances.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on instance urls
     * @param targetUrl Filter by target url
     */
    public static Result listByTarget(int pageNo, String sortBy, String order, String filter, String targetUrl) {
    	Logger.info("Instances.listByTarget()");
        return ok(
        	listByTarget.render(
        			"Lookup", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Instance.pageByTarget(pageNo, 10, sortBy, order, filter, targetUrl), 
        			sortBy, 
        			order,
        			targetUrl)
        	);
    }
	
    /**
     * This method exports selected instances to CSV file.
     * @param list of Instance objects
     * @return
     */
    public static void export(List<Instance> instanceList) {
    	Logger.info("export() instanceList size: " + instanceList.size());

        StringWriter sw = new StringWriter();
		Field[] fields = Instance.class.getFields();
		for (Field f : fields) {
//			Logger.info("Instance fields: " + f.getName());
    		sw.append(f.getName());
	 	    sw.append(Const.CSV_SEPARATOR);
		}
 	    sw.append(Const.CSV_LINE_END);
 	    
 	    String csv = "";
 	    if (instanceList != null && instanceList.size() > 0) {
 	    	Iterator<Instance> itr = instanceList.iterator();
 	    	while (itr.hasNext()) {
 	    		Instance instance = itr.next();
 	    		csv = csv + ", " + instance.toString();
 	    	}
 	    }
        if (csv != null) {
	        String content = csv.replace(", " + Const.TARGET_DEF,  "").replace("[", "").replace("]", "").substring(Const.TARGET_DEF.length());
	        sw.append(content);
//        Logger.info("content: " + content);
        }

    	Utils.generateCsvFile(Const.EXPORT_INSTANCE_FILE, sw.toString());
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url");

    	if (StringUtils.isBlank(query)) {
			Logger.info("Instance name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.InstanceController.list(0, "title", "asc", "")
	        );
    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		//return redirect(routes.Instances.create(query));
				Logger.info("addEntry()");
				Instance instance = new Instance();
				instance.title = query;
				instance.id = Utils.createId();
				instance.url = Const.ACT_URL + instance.id;
				instance.revision = Const.INITIAL_REVISION;
				Logger.info("add instance with url: " + instance.url + " and name: " + instance.title);
    			Form<Instance> instanceForm = Form.form(Instance.class);
    			instanceForm = instanceForm.fill(instance);
    			User user = User.findByEmail(request().username());
    			List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
	  			QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
	  	        return ok(edit.render(instanceForm, user, qaIssues, qaIssueCategories));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.InstanceController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * Add new entry.
     * @param instance
     * @return
     */
    public static Result create(String title) {
        Logger.info("addEntry()");
    	Instance instance = new Instance();
    	instance.title = title;
        instance.id = Utils.createId();
        instance.url = Const.ACT_URL + instance.id;
        instance.revision = Const.INITIAL_REVISION;
		Logger.info("add instance with url: " + instance.url + " and name: " + instance.title);
		Form<Instance> instanceForm = Form.form(Instance.class);
		instanceForm = instanceForm.fill(instance);
		User user = User.findByEmail(request().username());
		JsonNode collectionData = getCollectionsData();
		JsonNode subjectData = getSubjectsData();
		List<User> authors = User.findAll();
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
        return ok(edit.render(instanceForm, user, qaIssues, qaIssueCategories));
    }
    
    /**
     * This method shows the list of instances associated to a target in QA table.
     * @param url The target URL
     * @return instances view
     */
    public static Result showByTarget(String url) {    	
    	if (url != null && url.length() > 0) {
			Logger.info("Show instances filtered by target.");
	        return redirect(
	        		routes.InstanceController.listByTarget(0, "title", "asc", "", url)
	        );
    	}    	
        return redirect(
        		routes.InstanceController.list(0, "title", "asc", "")
        );
    }
        
	/**
	 * This method filters instances by given license.
	 * @return license list
	 */
//	public static List<Taxonomy> getLicense() {
//		List<Taxonomy> res = new ArrayList<Taxonomy>();
//		List<String> subjects = new ArrayList<String>();
//		List<Instance> allInstances = Instance.find.all();
//		Iterator<Instance> itr = allInstances.iterator();
//		while (itr.hasNext()) {
//			Instance instance = itr.next();
//			if (instance.fieldLicense != null && instance.fieldLicense.length() > 0 && !subjects.contains(instance.fieldLicense)) {
//		        ExpressionList<Instance> ll = Instance.find.where().contains("field_license", instance.fieldLicense);
//		        if (ll.findRowCount() > 0) {
//		        	Taxonomy taxonomy = Taxonomy.findByUrl(instance.fieldLicense);
//		        	Logger.info("instance.field_license: " + instance.fieldLicense + ".");
////		        	Logger.info("taxonomy url: " + taxonomy.url);
////		        	Logger.info("license: " + taxonomy.title);
//		        	res.add(taxonomy);
//		        	subjects.add(instance.fieldLicense);
//		        }
//			}
//		}
//		Logger.info("getLicense res: " + res);
//    	return res;
//	}
	
	/**
	 * This method filters instances by crawl frequency.
	 * @return crawl frequency list
	 */
//	public static List<Instance> getCrawlFrequency() {
//		List<Instance> res = new ArrayList<Instance>();
//		List<String> subjects = new ArrayList<String>();
//		List<Instance> allInstances = Instance.find.all();
//		Iterator<Instance> itr = allInstances.iterator();
//		while (itr.hasNext()) {
//			Instance instance = itr.next();
//			if (instance.fieldCrawlFrequency != null && instance.fieldCrawlFrequency.length() > 0 && !subjects.contains(instance.fieldCrawlFrequency)) {
//		        ExpressionList<Instance> ll = Instance.find.where().contains("field_crawl_frequency", instance.fieldCrawlFrequency);
//		        if (ll.findRowCount() > 0) {
//		        	res.add(instance);
//		        	subjects.add(instance.fieldCrawlFrequency);
//		        }
//			}
//		}
//    	return res;
//	}
	
	/**
	 * This method filters instances by depth.
	 * @return depth list
	 */
//	public static List<Instance> getDepth() {
//		List<Instance> res = new ArrayList<Instance>();
//		List<String> subjects = new ArrayList<String>();
//		List<Instance> allInstances = Instance.find.all();
//		Iterator<Instance> itr = allInstances.iterator();
//		while (itr.hasNext()) {
//			Instance instance = itr.next();
//			if (instance.fieldDepth != null && instance.fieldDepth.length() > 0 && !subjects.contains(instance.fieldDepth)) {
//		        ExpressionList<Instance> ll = Instance.find.where().contains("field_depth", instance.fieldDepth);
//		        if (ll.findRowCount() > 0) {
//		        	res.add(instance);
//		        	subjects.add(instance.fieldDepth);
//		        }
//			}
//		}
//    	return res;
//	}
	
	/**
	 * This method filters instances by collection categories.
	 * @return collection categories list
	 */
//	public static List<Taxonomy> getCollectionCategories() {
//		List<Instance> res = new ArrayList<Instance>();
//		List<String> subjects = new ArrayList<String>();
//		List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();
//		List<Instance> allInstances = Instance.find.all();
//		Iterator<Instance> itr = allInstances.iterator();
//		while (itr.hasNext()) {
//			Instance instance = itr.next();
//			if (instance.fieldCollectionCategories != null && instance.fieldCollectionCategories.length() > 0 && !subjects.contains(instance.fieldCollectionCategories)) {
//		        ExpressionList<Instance> ll = Instance.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, instance.fieldCollectionCategories);
//		        if (ll.findRowCount() > 0) {
//		        	res.add(instance);
//		        	subjects.add(instance.fieldCollectionCategories);
//		        	Taxonomy taxonomy = Taxonomy.findByUrl(instance.fieldCollectionCategories);
//		        	taxonomies.add(taxonomy);
//		        }
//			}
//		}
//    	return taxonomies;
//	}
	
    public static Result GO_HOME = redirect(
            routes.InstanceController.list(0, "title", "asc", "")
        );

    /**
     * Display the instance edit panel for this URL.
     */
    public static Result edit(Long id) {
		Instance instance = Instance.findById(id);
		Form<Instance> instanceForm = Form.form(Instance.class);
		instanceForm = instanceForm.fill(instance);
		User user = User.findByEmail(request().username());
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
        return ok(edit.render(instanceForm, user, qaIssues, qaIssueCategories));
    }
    
    public static Result view(Long id) {
		Instance instance = Instance.findById(id);
		User user = User.findByEmail(request().username());
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
		return ok(view.render(instance, user, qaIssues, qaIssueCategories));	
    }
    
    public static Result viewAct(String url) {
    	Instance instance = Instance.findByUrl(url);
    	User user = User.findByEmail(request().username());
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
		return ok(view.render(instance, user, qaIssues, qaIssueCategories));	
	}

    public static Result viewWct(String url) {
    	Instance instance = Instance.findByWct(url);
    	User user = User.findByEmail(request().username());
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
		return ok(view.render(instance, user, qaIssues, qaIssueCategories));	
	}
    
    /**
     * This method shows selected revision of a Instance by given ID.
     * @param nid
     * @return
     */
    public static Result viewrevision(Long id) {
		Instance instance = Instance.findById(id);
    	User user = User.findByEmail(request().username());
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
		return ok(view.render(instance, user, qaIssues, qaIssueCategories));
    }
    	
	/**
	 * This method prepares Instance form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
	    Instance newInstance = new Instance();
        try {
    	    Instance instance = Instance.findById(Long.valueOf(getFormParam(Const.ID)));
        	if (getFormParam(Const.FIELD_WCT_ID) != null && !getFormParam(Const.FIELD_WCT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
//            	newInstance.fieldWct_id = instance.fieldWct_id;
            }
        	if (getFormParam(Const.FIELD_SPT_ID) != null && !getFormParam(Const.FIELD_SPT_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
//        		newInstance.fieldSpt_id = instance.fieldSpt_id;
        	}
        	if (getFormParam(Const.LEGACY_SITE_ID) != null && !getFormParam(Const.LEGACY_SITE_ID).equals("")
        			&& !Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
//        		newInstance.legacySite_id = instance.legacySite_id;
            }
        } catch (Exception e) {
        	Logger.info("The instance for given NID is not yet existing in database");
        } 	
	    newInstance.id = Long.valueOf(getFormParam(Const.ID));
        if (newInstance.authorUser == null) {
        	newInstance.authorUser.url = getFormParam(Const.USER);
        }
        newInstance.url = Const.ACT_URL + newInstance.id;
        newInstance.title = getFormParam(Const.TITLE);
//        newInstance.fieldUrl = Scope.normalizeUrl(getFormParam(Const.FIELD_URL_NODE));
//        newInstance.fieldKeySite = Utils.getNormalizeBooleanString(getFormParam(Const.KEYSITE));
//        newInstance.fieldDescription = getFormParam(Const.DESCRIPTION);
        if (getFormParam(Const.STATUS) != null) {
        	newInstance.status = Long.valueOf(getFormParam(Const.STATUS));
        } 
        if (getFormParam(Const.LANGUAGE) != null) {
        	newInstance.language = getFormParam(Const.LANGUAGE);
        } 
        if (getFormParam(Const.SELECTION_TYPE) != null) {
//        	newInstance.selectionType = getFormParam(Const.SELECTION_TYPE);
        } 
        if (getFormParam(Const.SELECTOR_NOTES) != null) {
//        	newInstance.selectorNotes = getFormParam(Const.SELECTOR_NOTES);
        } 
        if (getFormParam(Const.ARCHIVIST_NOTES) != null) {
//        	newInstance.archivistNotes = getFormParam(Const.ARCHIVIST_NOTES);
        } 
        if (getFormParam(Const.LEGACY_SITE_ID) != null 
        		&& getFormParam(Const.LEGACY_SITE_ID).length() > 0
        		&& Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
//        	newInstance.legacySite_id = Long.valueOf(getFormParam(Const.LEGACY_SITE_ID));
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
            	newInstance.authorUser.url = resContactPersons;
        	} else {
        		newInstance.authorUser.url = Const.NONE;
        	}
        }            
        if (getFormParam(Const.LIVE_SITE_STATUS) != null) {
//        	newInstance.fieldLiveSiteStatus = getFormParam(Const.LIVE_SITE_STATUS);
        } 
        if (getFormParam(Const.FIELD_SUBJECT) != null) {
//        	newInstance.fieldSubject = Utils.removeDuplicatesFromList(getFormParam(Const.FIELD_SUBJECT));
//    		Logger.debug("newInstance.field_subject: " + newInstance.fieldSubject);
//    		if (newInstance.fieldSubject == null) {
//    			newInstance.fieldSubject = Const.NONE;
//    		}
        } else {
//        	newInstance.fieldSubject = Const.NONE;
        }            
//		newInstance.updateSubject();
        if (getFormParam(Const.TREE_KEYS) != null) {
//        	newInstance.fieldCollectionCategories = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
//        	newInstance.collectionToInstance = Collection.convertUrlsToObjects(newInstance.fieldCollectionCategories);
//        	newInstance.updateCollection();
//    		Logger.debug("newInstance.field_collection_categories: " + newInstance.fieldCollectionCategories);
        }
        if (getFormParam(Const.ORGANISATION) != null) {
        	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
//        		newInstance.fieldNominatingOrganisation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
//        		newInstance.updateOrganisation();
        	} else {
//        		newInstance.fieldNominatingOrganisation = Const.NONE;
        	}
        }
        if (getFormParam(Const.ORIGINATING_ORGANISATION) != null) {
        	if (!getFormParam(Const.ORIGINATING_ORGANISATION).toLowerCase().contains(Const.NONE)) {
        		newInstance.target.organisation.url = Organisation.findByTitle(getFormParam(Const.ORIGINATING_ORGANISATION)).url;
        	} else {
        		newInstance.target.organisation.url = Const.NONE;
        	}
        }
        if (getFormParam(Const.AUTHOR) != null) {
       		newInstance.authorUser.url = User.findByName(getFormParam(Const.AUTHOR)).url;
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
//            	newInstance.tags = resTags;
//            	newInstance.tagToInstance = Tag.convertUrlsToObjects(newInstance.tags);
        	} else {
//        		newInstance.tags = Const.NONE;
        	}
        }
//        if (getFormParam(Const.FLAGS) != null) {
//        	if (!getFormParam(Const.FLAGS).toLowerCase().contains(Const.NONE)) {
//            	String[] flags = getFormParams(Const.FLAGS);
//            	String resFlags = "";
//            	for (String flag: flags)
//                {
//            		if (flag != null && flag.length() > 0) {
//                		Logger.info("add flag: " + flag);
//            			resFlags = resFlags + Flag.findByName(flag).url + Const.LIST_DELIMITER;
//            		}
//                }
//            	newInstance.flags = resFlags;
////            	newInstance.flagToInstance = Flag.convertUrlsToObjects(newInstance.flags);
//        	} else {
//        		newInstance.flags = Const.NONE;
//        	}
//        }
//        newInstance.justification = getFormParam(Const.JUSTIFICATION);
//        newInstance.summary = getFormParam(Const.SUMMARY);
//        newInstance.revision = getFormParam(Const.REVISION);
//        if (getFormParam(Const.FIELD_WCT_ID) != null  && getFormParam(Const.FIELD_WCT_ID).length() > 0) {
//        	newInstance.fieldWct_id = Long.valueOf(getFormParam(Const.FIELD_WCT_ID));
//        }
//        if (getFormParam(Const.FIELD_SPT_ID) != null && getFormParam(Const.FIELD_SPT_ID).length() > 0) {
//        	newInstance.fieldSpt_id = Long.valueOf(getFormParam(Const.FIELD_SPT_ID));
//        }
//        newInstance.fieldLicense = getFormParam(Const.FIELD_LICENSE);
////        newInstance.field_uk_hosting = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_HOSTING));
//        newInstance.fieldUkHosting = Target.isInScopeIp(newInstance.target.fieldUrl(), newInstance.url);
//    	Logger.debug("field_uk_hosting: " + newInstance.fieldUkHosting);
//        newInstance.fieldUkPostalAddress = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_UK_POSTAL_ADDRESS));
//        newInstance.fieldUkPostalAddressUrl = getFormParam(Const.FIELD_UK_POSTAL_ADDRESS_URL);
//        newInstance.fieldViaCorrespondence = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_VIA_CORRESPONDENCE));
//        newInstance.fieldNotes = getFormParam(Const.FIELD_NOTES);
//        newInstance.fieldProfessionalJudgement = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT));
//        newInstance.fieldProfessionalJudgementExp = getFormParam(Const.FIELD_PROFESSIONAL_JUDGEMENT_EXP);
//        newInstance.fieldNoLdCriteriaMet = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_NO_LD_CRITERIA_MET));
//        newInstance.fieldIgnoreRobotsTxt = Utils.getNormalizeBooleanString(getFormParam(Const.FIELD_IGNORE_ROBOTS_TXT));           
//        if (getFormParam(Const.FIELD_CRAWL_START_DATE) != null) {
//        	String startDateHumanView = getFormParam(Const.FIELD_CRAWL_START_DATE);
//        	String startDateUnix = Utils.getUnixDateStringFromDate(startDateHumanView);
//        	Logger.info("startDateHumanView: " + startDateHumanView + ", startDateUnix: " + startDateUnix);
//        	// TODO: UNIX DATE
////        	newInstance.fieldCrawlStartDate = startDateUnix;
//        }
//        if (getFormParam(Const.FIELD_CRAWL_END_DATE) != null) {
//        	String endDateHumanView = getFormParam(Const.FIELD_CRAWL_END_DATE);
//        	String endDateUnix = Utils.getUnixDateStringFromDate(endDateHumanView);
//        	Logger.info("endDateHumanView: " + endDateHumanView + ", endDateUnix: " + endDateUnix);
//        	// TODO: UNIX DATE
////        	newInstance.fieldCrawlEndDate = endDateUnix;
//        }
//        newInstance.dateOfPublication = getFormParam(Const.DATE_OF_PUBLICATION);
//        newInstance.whiteList = getFormParam(Const.WHITE_LIST);
//        newInstance.blackList = getFormParam(Const.BLACK_LIST);
//        if (getFormParam(Const.FIELD_DEPTH) != null) {
////        	newInstance.fieldDepth = TargetController.getDepthNameFromGuiName(getFormParam(Const.FIELD_DEPTH));
//        }
//        if (getFormParam(Const.FIELD_SCOPE) != null) {
////        	newInstance.fieldScope = TargetController.getScopeNameFromGuiName(getFormParam(Const.FIELD_SCOPE));
//        }
//        newInstance.fieldCrawlFrequency = getFormParam(Const.FIELD_CRAWL_FREQUENCY);
//        newInstance.keywords = getFormParam(Const.KEYWORDS);
////        Logger.info("instance keywords: " + getFormParam(Const.KEYWORDS));
//        newInstance.synonyms = getFormParam(Const.SYNONYMS);
//        if (getFormParam(Const.FIELD_QA_STATUS) != null) {
//        	// TODO: KL
////        	newInstance.fieldQaStatus = Taxonomy.findByNameExt(getFormParam(Const.FIELD_QA_STATUS)).url;
//        }             
//        if (getFormParam(Const.QA_STATUS) != null) {
//        	if (!getFormParam(Const.QA_STATUS).toLowerCase().contains(Const.NONE)) {
//        		Logger.info("Instance qa status: " + getFormParam(Const.QA_STATUS));
//        		newInstance.qaIssue.name = getFormParam(Const.QA_STATUS);
//        	} else {
//        		newInstance.qaIssue.name = Const.NONE;
//        	}
//        }
//        if (getFormParam(Const.QA_ISSUE_CATEGORY) != null) {
//        	if (!getFormParam(Const.QA_ISSUE_CATEGORY).toLowerCase().contains(Const.NONE)) {
//        		Logger.info("Instance qa issue category: " + getFormParam(Const.QA_ISSUE_CATEGORY));
//            	String[] issueCategories = getFormParams(Const.QA_ISSUE_CATEGORY);
//            	String resIssueCategories = "";
//            	for (String issueCategory: issueCategories)
//                {
//            		if (issueCategory != null && issueCategory.length() > 0) {
//                		Logger.info("add issueCategory: " + issueCategory);
//            			resIssueCategories = resIssueCategories + issueCategory + Const.LIST_DELIMITER;
//            		}
//                }
//            	newInstance.qaIssueCategory = resIssueCategories;
//        	} else {
//        		newInstance.qaIssueCategory = Const.NONE;
//        	}
//        }
//        newInstance.qaNotes = getFormParam(Const.QA_NOTES);
//        newInstance.technicalNotes = getFormParam(Const.QUALITY_NOTES);

        Form<Instance> instanceFormNew = Form.form(Instance.class);
		instanceFormNew = instanceFormNew.fill(newInstance);
		Logger.debug("info() goto edit form");
		
		User user = User.findByEmail(request().username());
		JsonNode collectionData = getCollectionsData();
		JsonNode subjectData = getSubjectsData();
		List<User> authors = User.findAll();
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		QAIssueCategory[] qaIssueCategories = Const.QAIssueCategory.values();
        return ok(edit.render(instanceFormNew, user, qaIssues, qaIssueCategories));
	}
    
    /**
     * This method saves changes on given instance in a new instance object
     * completed by revision comment. The "version" field in the Instance object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
        String save = getFormParam("save");
        String delete = getFormParam("delete");
        Logger.info("delete: " + delete);
        
        
        
//        if (save != null) {
//        	Logger.info("input data for saving instance nid: " + getFormParam(Const.ID) + 
//        			", url: " + getFormParam(Const.URL) + 
//        			", field url: " + getFormParam(Const.FIELD_URL_NODE) + 
//        			", title: " + getFormParam(Const.TITLE) + 
//        			", keysite: " + getFormParam(Const.KEYSITE) +
//        			", description: " + getFormParam(Const.DESCRIPTION) + 
//        			", status: " + getFormParam(Const.STATUS) +
//        			", subject: " + getFormParams(Const.FIELD_SUBJECT) +
//        			", organisation: " + getFormParam(Const.ORGANISATION) +
//        			", live site status: " + getFormParam(Const.LIVE_SITE_STATUS));
//        	Logger.info("treeKeys: " + getFormParam(Const.TREE_KEYS));
        	Form<Instance> instanceForm = Form.form(Instance.class).bindFromRequest();
            if(instanceForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : instanceForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
//            	    key = Utils.showMissingField(key);
//            	    if (missingFields.length() == 0) {
//            	    	missingFields = key;
//            	    } else {
//            	    	missingFields = missingFields + Const.COMMA + " " + key;
//            	    }
            	}
//            	Logger.info("form errors size: " + instanceForm.errors().size() + ", " + missingFields);
//	  			flash("message", "Please fill out all the required fields, marked with a red star. There are required fields in more than one tab. " + 
//	  					" Missing fields are: " + missingFields);
//	  			return info();
            }
        	
//        	if (getFormParam(Const.FIELD_WCT_ID) != null && !getFormParam(Const.FIELD_WCT_ID).equals("")
//        			&& !Utils.isNumeric(getFormParam(Const.FIELD_WCT_ID))) {
//                Logger.info("Only numeric values are valid identifiers. Please check field 'WCT ID'.");
//	  			flash("message", "Only numeric values are valid identifiers. Please check field 'WCT ID'.");
//	  			return info();
//        	}    	
//        	if (getFormParam(Const.FIELD_SPT_ID) != null && !getFormParam(Const.FIELD_SPT_ID).equals("")
//        			&& !Utils.isNumeric(getFormParam(Const.FIELD_SPT_ID))) {
//                Logger.info("Only numeric values are valid identifiers. Please check field 'SPT ID'.");
//	  			flash("message", "Only numeric values are valid identifiers. Please check field 'SPT ID'.");
//	  			return info();
//        	}    	
//        	if (getFormParam(Const.LEGACY_SITE_ID) != null && !getFormParam(Const.LEGACY_SITE_ID).equals("")
//        			&& !Utils.isNumeric(getFormParam(Const.LEGACY_SITE_ID))) {
//                Logger.info("Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
//	  			flash("message", "Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
//	  			return info();
//        	}    	

            Long id = Long.valueOf(instanceForm.field("id").value());
    	    Instance instanceFromDB = Instance.findById(id);
    	    Instance instanceFromForm  = instanceForm.get();

            DynamicForm requestData = Form.form().bindFromRequest();
            
            String qaIssueId = requestData.get("qaIssueId");
            Logger.info("instanceFromForm: " + instanceFromForm);
            if (StringUtils.isNotEmpty(qaIssueId)) {
            	Long qaId = Long.valueOf(qaIssueId);
            	QaIssue qaIssue = QaIssue.findById(qaId);
            	instanceFromDB.qaIssue = qaIssue;
            }
            
//        qaIssueCategory
            instanceFromDB.qaIssueCategory = instanceFromForm.qaIssueCategory;
            
            instanceFromDB.qaNotes = instanceFromForm.qaNotes;
            
            instanceFromDB.notes = instanceFromForm.notes;
            
            instanceFromDB.revision = instanceFromForm.revision;

            instanceFromDB.title = instanceFromForm.title;

            Logger.info("instanceFromDB: " + instanceFromDB);

            instanceFromDB.save();
            
	        return redirect(routes.InstanceController.view(instanceFromDB.id));

//        } 
//        if (delete != null) {
//        	Logger.info("deleting instance: " + id);
//        	Instance instance = Instance.findById(id);
//        	Ebean.delete(instance);
//	        res = redirect(routes.InstanceController.index()); 
//        }
    }
	
    /**
     * This method is checking scope for given URL and returns result in JSON format.
     * @param url
     * @return JSON result
     * @throws WhoisException 
     */
//    public static Result isInScope(String url) throws WhoisException {
////    	Logger.info("isInScope controller: " + url);
//    	boolean res = Instance.isInScope(url, null);
////    	Logger.info("isInScope res: " + res);
//    	return ok(Json.toJson(res));
//    }
    
    /**
     * This method calculates collection children - objects that have parents.
     * @param url The identifier for parent 
     * @return child collection in JSON form
     */
//    public static String getChildren(String url, String targetUrl) {
//    	String res = "";
//        final StringBuffer sb = new StringBuffer();
//    	sb.append(", \"children\":");
//    	List<Collection> childSuggestedCollections = Collection.getChildLevelCollections(url);
//    	if (childSuggestedCollections.size() > 0) {
//	    	sb.append(getTreeElements(childSuggestedCollections, targetUrl, false));
//	    	res = sb.toString();
////	    	Logger.info("getChildren() res: " + res);
//    	}
//    	return res;
//    }
    
    /**
     * Mark collections that are stored in target object as selected
     * @param collectionUrl The collection identifier
     * @param targetUrl This is an identifier for current instance object
     * @return
     */
//    public static String checkSelection(String collectionUrl, String targetUrl) {
//    	String res = "";
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		Instance instance = Instance.findByUrl(targetUrl);
//    		if (instance.fieldCollectionCategories != null && 
//    				instance.fieldCollectionCategories.contains(collectionUrl)) {
//    			res = "\"select\": true ,";
//    		}
//    	}
//    	return res;
//    }
    
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current instance object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
//    public static String getTreeElements(List<Collection> collectionList, String targetUrl, boolean parent) { 
//    	String res = "";
//    	if (collectionList.size() > 0) {
//	        final StringBuffer sb = new StringBuffer();
//	        sb.append("[");
//	    	Iterator<Collection> itr = collectionList.iterator();
//	    	boolean firstTime = true;
//	    	while (itr.hasNext()) {
//	    		Collection collection = itr.next();
////    			Logger.debug("add collection: " + collection.title + ", with url: " + collection.url +
////    					", parent:" + collection.parent + ", parent size: " + collection.parent.length());
//	    		if ((parent && collection.parent == null) || !parent || collection.parent == null) {
//		    		if (firstTime) {
//		    			firstTime = false;
//		    		} else {
//		    			sb.append(", ");
//		    		}
////	    			Logger.debug("added");
//					sb.append("{\"title\": \"" + collection.name + "\"," + checkSelection(collection.url, targetUrl) + 
//							" \"key\": \"" + collection.url + "\"" + 
//							getChildren(collection.url, targetUrl) + "}");
//	    		}
//	    	}
////	    	Logger.info("collectionList level size: " + collectionList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.info("getTreeElements() res: " + res);
//    	}
//    	return res;
//    }
    
    /**
     * This method computes a tree of suggested collections in JSON format. 
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollections(String targetUrl) {
//    	Logger.info("getSuggestedCollections()");
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Collection> suggestedCollections = Collection.getFirstLevelCollections();
//    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
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
//    public static String getSubjectChildren(String url, String targetUrl) {
////    	Logger.info("getSubjectChildren() target URL: " + targetUrl);
//    	String res = "";
//        final StringBuffer sb = new StringBuffer();
//    	sb.append(", \"children\":");
////    	List<Taxonomy> childSubject = Taxonomy.findListByType(Const.SUBSUBJECT);
//    	Taxonomy subject = Taxonomy.findByUrl(url);
//    	List<Taxonomy> childSubject = Taxonomy.findSubSubjectsList(subject.name);
//    	if (childSubject.size() > 0) {
//	    	sb.append(getSubjectTreeElements2(childSubject, targetUrl, false));
//	    	res = sb.toString();
////	    	Logger.info("getSubjectChildren() res: " + res);
//    	}
//    	return res;
//    }
    
    /**
     * Mark subjects that are stored in target object as selected
     * @param subjectUrl The subject identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
//    public static String checkSubjectSelection(String subjectUrl, String targetUrl) {
//    	String res = "";
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		Instance target = Instance.findByUrl(targetUrl);
//    		if (target.fieldSubject != null && 
//    				target.fieldSubject.contains(subjectUrl)) {
//    			res = "\"select\": true ,";
//    		}
//    	}
//    	return res;
//    }
    
    /**
     * Check if none value is selected
     * @param subjectUrl The subject identifier
     * @param targetUrl This is an identifier for current target object
     * @return
     */
//    public static String checkNone(String targetUrl) {
//    	String res = "";
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		Instance target = Instance.findByUrl(targetUrl);
//    		if (target.fieldSubject != null 
//    				&& (target.fieldSubject.toLowerCase().contains(Const.NONE.toLowerCase()))) {
//    			res = "\"select\": true ,";
//    		}
//    	}
//    	return res;
//    }
    
    /**
   	 * This method calculates first order subjects.
     * @param subjectList The list of all subjects
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
//    public static String getSubjectTreeElements2(List<Taxonomy> subjectList, String targetUrl, boolean parent) { 
////    	Logger.info("getSubjectTreeElements() target URL: " + targetUrl);
//    	String res = "";
//    	if (subjectList.size() > 0) {
//	        final StringBuffer sb = new StringBuffer();
//	        sb.append("[");
//	        if (parent) {
//	        	sb.append("{\"title\": \"" + "None" + "\"," + checkNone(targetUrl) + 
//	        			" \"key\": \"" + "None" + "\"" + "}, ");
//	        }
//	    	Iterator<Taxonomy> itr = subjectList.iterator();
//	    	boolean firstTime = true;
//	    	while (itr.hasNext()) {
//	    		Taxonomy subject = itr.next();
////    			Logger.debug("add subject: " + subject.name + ", with url: " + subject.url +
////    					", parent:" + subject.parent + ", parent size: " + subject.parent.length());
//	    		if ((parent && subject.parent == null) || !parent) {
//		    		if (firstTime) {
//		    			firstTime = false;
//		    		} else {
//		    			sb.append(", ");
//		    		}
////	    			Logger.debug("added");
//					sb.append("{\"title\": \"" + subject.name + "\"," + checkSubjectSelection(subject.url, targetUrl) + 
//							" \"key\": \"" + subject.url + "\"" + 
//							getSubjectChildren(subject.url, targetUrl) + "}");
//	    		}
//	    	}
////	    	Logger.info("subjectList level size: " + subjectList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.info("getSubjectTreeElements() res: " + res);
//    	}
//    	return res;
//    }
        
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
//    	sb.append(getSubjectTreeElements2(parentSubjects, targetUrl, true));
//    	Logger.info("subjects main level size: " + parentSubjects.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getSubjectTree() json: " + jsonData.toString());
        return ok(jsonData);
    }  
}


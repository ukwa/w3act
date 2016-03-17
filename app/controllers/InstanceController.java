package controllers;

import static play.data.Form.form;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import models.Collection;
import models.FieldUrl;
import models.Instance;
import models.QaIssue;
import models.Target;
import models.Taxonomy;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.Const.QAIssueCategory;
import uk.bl.api.Utils;
import uk.bl.api.models.Results;
import uk.bl.api.models.Wayback;
import uk.bl.exception.ActException;
import uk.bl.export.WaybackExport;
import views.html.instances.edit;
import views.html.instances.list;
import views.html.instances.listByTarget;
import views.html.instances.view;
import views.html.instances.newForm;
import views.html.instances.results;

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
    public static Result listByTarget(int pageNo, String sortBy, String order, String filter, Long targetId) {
    	Logger.debug("Instances.listByTarget()");
    	Page<Instance> pages = Instance.pageByTarget(pageNo, 10, sortBy, order, filter, targetId);
    	Logger.debug("pages :" + pages);
        return ok(
        	listByTarget.render(
        			"Lookup", 
        			User.findByEmail(request().username()), 
        			filter, 
        			pages, 
        			sortBy, 
        			order,
        			targetId)
        	);
    }
	
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	
    	DynamicForm request = form().bindFromRequest();
    	String action = request.get("action");
    	String query = request.get("url");

    	Logger.debug("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Instance name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.InstanceController.list(0, "title", "asc", "")
	        );
    	}    	

    	int pageNo = Integer.parseInt(request.get(Const.PAGE_NO));
    	String sort = request.get(Const.SORT_BY);
    	String order = request.get(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("add")) {
    	    	return redirect(routes.InstanceController.newForm(query));				
    		} 
    		else if (action.equals("search")) {
    			Logger.debug("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.InstanceController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * This method shows the list of instances associated to a target in QA table.
     * @param url The target URL
     * @return instances view
     */
    public static Result showByTarget(Long targetId) {    	
    	if (targetId != null) {
			Logger.debug("Show instances filtered by target.");
	        return redirect(
	        		routes.InstanceController.listByTarget(0, "createdAt", "desc", "", targetId)
	        );
    	}    	
        return redirect(
        		routes.InstanceController.list(0, "createdAt", "desc", "")
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
//		        	Logger.debug("instance.field_license: " + instance.fieldLicense + ".");
////		        	Logger.debug("taxonomy url: " + taxonomy.url);
////		        	Logger.debug("license: " + taxonomy.title);
//		        	res.add(taxonomy);
//		        	subjects.add(instance.fieldLicense);
//		        }
//			}
//		}
//		Logger.debug("getLicense res: " + res);
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
            routes.InstanceController.list(0, "createdAt", "desc", "")
        );
    
    public static Result newForm(String title) {
		User user = User.findByEmail(request().username());
    	Instance instance = new Instance();
		instance.revision = Const.INITIAL_REVISION;

		Form<Instance> instanceForm = Form.form(Instance.class);
		instanceForm = instanceForm.fill(instance);    	
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> qaIssueCategories = QAIssueCategory.options();
		Map<String,String> authors = User.options();
		return ok(newForm.render(instanceForm, user, qaIssues, qaIssueCategories, authors,null));
    }
    
    public static Result newWithTarget(Long targetId, String title) throws ActException {
    	User user = User.findByEmail(request().username());
    	Target target = Target.findById(targetId);
    	Instance instance = new Instance();
    	instance.target = target;
    	instance.revision = Const.INITIAL_REVISION;
    	Form<Instance> instanceForm = Form.form(Instance.class);
    	instanceForm = instanceForm.fill(instance);
    	Map<String,String> qaIssues = QaIssue.options();
    	Map<String,String> qaIssueCategories = QAIssueCategory.options();
    	Map<String,String> authors = User.options();
    	return ok(newForm.render(instanceForm, user, qaIssues, qaIssueCategories, authors, targetId));
    }
    
    public static Result importFromWayback(Long targetId, String view) throws ActException {

    	String webArchiveUrl = WaybackController.getWaybackEndpoint();
    	String webArchivePath = WaybackController.getWaybackQueryEndpoint();
    	
    	String wayBackUrl = webArchiveUrl + webArchivePath;
    	
    	Logger.debug("webArchiveUrl: " + wayBackUrl);
    	Target target = Target.findById(targetId);
    	
    	for (FieldUrl fieldUrl : target.fieldUrls) {
//    		http://www.bl.uk/bibliographic/ukmarc.html";
    		String urlValue = wayBackUrl + fieldUrl.url;
    		
        	Logger.debug("urlValue: " + urlValue);

			Wayback wayback = WaybackExport.INSTANCE.export(urlValue);
			
			Results results = wayback.getResults();
			if (results != null && results.getResults() != null) {
				try {
					for (uk.bl.api.models.Result result : results.getResults()) {
						// check instance first
						String captureDatetitle = result.getCapturedate().toString();
						
						Instance instance = Instance.findbyTitleAndTargetId(captureDatetitle, target.id);
						if (instance == null) {
							instance = new Instance();
							instance.title = captureDatetitle;
							instance.createdAt = Utils.INSTANCE.getDateFromLongValue(result.getCapturedate());
							Logger.debug("instance.createdAt: " + instance.createdAt);
							instance.format = result.getMimetype();
							instance.revision = "initial revision";
							instance.fieldDate = Utils.INSTANCE.getDateFromLongValue(result.getCapturedate());
							instance.target = target;
							instance.save();
						}
					}
				flash("message", "Import from Wayback Complete");
				} catch (ParseException e) {
					throw new ActException("No Instances Found");
				}
			} else {
				flash("message", "No Instances Found");
			}
    	}

		if (StringUtils.isNotBlank(view)) {
			if (view.equals("view")) {
				return redirect(routes.TargetController.view(targetId));
			} else if (view.equals("edit")) {
				return redirect(routes.TargetController.edit(targetId));
			}
		}
    	return redirect(routes.TargetController.index());
		
//        flash("message", "Target " + filledForm.get().title + " has been updated");
//    	return redirect(routes.TargetController.view(filledForm.get().id));

		
		
//    	Instance instance = new Instance();
//    	instance.target = target;
//		instance.revision = Const.INITIAL_REVISION;
//
//		Form<Instance> instanceForm = Form.form(Instance.class);
//		instanceForm = instanceForm.fill(instance);    	
//		Map<String,String> qaIssues = QaIssue.options();
//		Map<String,String> qaIssueCategories = QAIssueCategory.options();
//		Map<String,String> authors = User.options();
//		return ok(newForm.render(instanceForm, user, qaIssues, qaIssueCategories, authors, targetId));
    }
    
    public static Result edit(Long id) {
		Instance instance = Instance.findById(id);
		if (instance == null) return notFound("THere is no Instance with ID " +id);
		
		Long targetId = instance.target.id;
		Logger.debug("title: " + instance.title);
		Logger.debug("authorUser: " + instance.authorUser);
		Form<Instance> instanceForm = Form.form(Instance.class);
		instanceForm = instanceForm.fill(instance);
		User user = User.findByEmail(request().username());
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> qaIssueCategories = QAIssueCategory.options();
		Map<String,String> authors = User.options();
        return ok(edit.render(instanceForm, user, id, qaIssues, qaIssueCategories, authors, targetId));
    }
    
    public static Result view(Long id) {
		Instance instance = Instance.findById(id);
		if (instance == null) return notFound("There is no Instance with ID " +id);
		
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

    public static Result results(Long targetId) {
    	Target target = Target.findById(targetId);
    	User user = User.findByEmail(request().username());
		return ok(results.render(target, user));	
    }

    public static Result byTargetAsJson(Long targetId) {
    	Target target = Target.findById(targetId);
        JsonNode jsonData = Json.toJson(target.instances);
        return ok(jsonData);
    }

    public static Result viewInstance(Long targetId, Long instanceId) {
		Instance instance = Instance.findByTargetAndInstance(targetId, instanceId);
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
    	
	public static Result info(Form<Instance> form, Long id) {
		User user = User.findByEmail(request().username());
		Instance instance = Instance.findById(id);
		Long targetId = instance.target.id;
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> qaIssueCategories = QAIssueCategory.options();
		Map<String,String> authors = User.options();

        return badRequest(edit.render(form, user, id, qaIssues, qaIssueCategories, authors, targetId));
	}

	public static Result newInfo(Form<Instance> form, Long targetId) {
		User user = User.findByEmail(request().username());
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> qaIssueCategories = QAIssueCategory.options();
		Map<String,String> authors = User.options();
		return badRequest(newForm.render(form, user, qaIssues, qaIssueCategories, authors, targetId));
	}
	
	@BodyParser.Of(value = BodyParser.FormUrlEncoded.class, maxLength = 1024 * 1024)
	public static Result update(Long id) {
		Logger.debug("update");
		RequestBody body = request().body();
		if(body.isMaxSizeExceeded()) {
			return badRequest("Too much data!");
		} else {
	    	DynamicForm requestData = form().bindFromRequest();
			Logger.debug("requestData: " + requestData);
	        Form<Instance> filledForm = form(Instance.class).bindFromRequest();
	    	Logger.debug("hasGlobalErrors: " + filledForm.hasGlobalErrors());
	    	Logger.debug("errors: " + filledForm.errors());
	
	    	String action = requestData.get("action");
	
	    	Logger.debug("action: " + action);
	    	
	        if (StringUtils.isNotEmpty(action)) {
	        	if (action.equals("save")) {
	                if (filledForm.hasErrors()) {
	                	Logger.debug("hasErrors: " + filledForm.errors());
	                    return info(filledForm, id);
	                }		  
	                //Hidden flag
	        		if (filledForm.get().hidden == null) {
	        			filledForm.get().hidden = Boolean.FALSE;
	        		}
			        filledForm.get().update(id);
			        flash("message", "Instance " + filledForm.get().title + " has been updated");
			        return redirect(routes.InstanceController.view(filledForm.get().id));
	        	} else if (action.equals("delete")) {
	            	Instance instance = Instance.findById(id);
			        flash("message", "Instance " + filledForm.get().title + " has been deleted");
	            	instance.delete();
	        		return redirect(routes.InstanceController.index()); 
	        	}
	        }
		}
        return null;
	}
	
    /**
     * This method saves changes on given instance in a new instance object
     * completed by revision comment. The "version" field in the Instance object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
        		String target = requestData.get("targetId");
        		Long targetId = null;
        		if (StringUtils.isNotBlank(target)) {
        			targetId = Long.valueOf(target);
        		}
		        Form<Instance> filledForm = form(Instance.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm, targetId);
		        }
		        //Hidden flag
				if (filledForm.get().hidden == null) {
					filledForm.get().hidden = Boolean.FALSE;
				}
		        filledForm.get().save();
		        flash("message", "Instance " + filledForm.get().title + " has been created");
		        return redirect(routes.InstanceController.view(filledForm.get().id));
        	}
        }
        return null;
    }
	
    /**
     * This method is checking scope for given URL and returns result in JSON format.
     * @param url
     * @return JSON result
     * @throws WhoisException 
     */
//    public static Result isInScope(String url) throws WhoisException {
////    	Logger.debug("isInScope controller: " + url);
//    	boolean res = Instance.isInScope(url, null);
////    	Logger.debug("isInScope res: " + res);
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
////	    	Logger.debug("getChildren() res: " + res);
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
////	    	Logger.debug("collectionList level size: " + collectionList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.debug("getTreeElements() res: " + res);
//    	}
//    	return res;
//    }
    
    /**
     * This method computes a tree of suggested collections in JSON format. 
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollections(String targetUrl) {
//    	Logger.debug("getSuggestedCollections()");
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Collection> suggestedCollections = Collection.getFirstLevelCollections();
//    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
//    	Logger.debug("suggestedCollections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.debug("getSuggestedCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }
            
    /**
     * This method calculates subject children - objects that have parents.
     * @param url The identifier for parent 
     * @param targetUrl This is an identifier for current target object
     * @return child subject in JSON form
     */
//    public static String getSubjectChildren(String url, String targetUrl) {
////    	Logger.debug("getSubjectChildren() target URL: " + targetUrl);
//    	String res = "";
//        final StringBuffer sb = new StringBuffer();
//    	sb.append(", \"children\":");
////    	List<Taxonomy> childSubject = Taxonomy.findListByType(Const.SUBSUBJECT);
//    	Taxonomy subject = Taxonomy.findByUrl(url);
//    	List<Taxonomy> childSubject = Taxonomy.findSubSubjectsList(subject.name);
//    	if (childSubject.size() > 0) {
//	    	sb.append(getSubjectTreeElements2(childSubject, targetUrl, false));
//	    	res = sb.toString();
////	    	Logger.debug("getSubjectChildren() res: " + res);
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
////    	Logger.debug("getSubjectTreeElements() target URL: " + targetUrl);
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
////	    	Logger.debug("subjectList level size: " + subjectList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.debug("getSubjectTreeElements() res: " + res);
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
    	Logger.debug("getSubjectTree() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
//    	Logger.debug("getSubjectTree() parentSubjects: " + parentSubjects.size());
//    	sb.append(getSubjectTreeElements2(parentSubjects, targetUrl, true));
//    	Logger.debug("subjects main level size: " + parentSubjects.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.debug("getSubjectTree() json: " + jsonData.toString());
        return ok(jsonData);
    }  
}


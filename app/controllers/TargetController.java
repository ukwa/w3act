package controllers;

import static play.data.Form.form;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import models.Collection;
import models.FieldUrl;
import models.Flag;
import models.License;
import models.Organisation;
import models.QaIssue;
import models.Subject;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.Const.CrawlFrequency;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;
import uk.bl.exception.WhoisException;
import views.html.collections.sites;
import views.html.licence.ukwalicenceresult;
import views.html.infomessage;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import views.html.targets.blank;
import views.html.targets.newForm;
import views.html.targets.edit;
import views.html.targets.list;
import views.html.targets.lookup;
import views.html.targets.view;
import views.html.users.usersites;


/**
 * Describe W3ACT project.
 */
@Security.Authenticated(SecuredController.class)
public class TargetController extends AbstractController {
  
    final static Form<Target> targetForm = new Form<Target>(Target.class);

    /**
     * Display the targets.
     */
    public static Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result lookup(int pageNo, String sortBy, String order, String filter) {
    	Logger.debug("TargetController.lookup()");
    	
    	Page<Target> pages = Target.find.fetch("fieldUrls").where()
			.eq(Const.ACTIVE, true)
			.add(Expr.or(Expr.icontains("fieldUrls.url", filter), Expr.icontains("t0.title", filter)))
			.orderBy("t0.title" + " " + order)
			.findPagingList(10)
			.setFetchAhead(false).getPage(pageNo);
    	
    	Logger.debug("Total: " + pages.getTotalRowCount());
    	
        return ok(
        	lookup.render(
        			"Lookup", 
        			User.findByEmail(request().username()), 
        			filter, 
        			pages, 
        			sortBy, 
        			order)
        	);
    }
    
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     * @param curatorId Author of the target
     * @param organisation The author's organisation
     * @param subject Target subject
     * @param crawlFrequency The crawl frequency
     * @param depth The crawl depth
     * @param collection The associated collection
     * @param license The license name
     * @param pageSize The number of Target entries on the page
     * @param flag The flag assigned by user
     */
    public static Result list(int pageNo, String sortBy, String order, String filter, Long curatorId, Long organisationId, String subject, 
    		String crawlFrequencyName, String depthName, String collection, Long licenseId, int pageSize, Long flagId) {
    	
    	Logger.debug("Pre Targets.list(): " + pageNo + " - " + filter + " - " + curatorId + " - " + organisationId + " - " + subject + " - " + crawlFrequencyName + " - " + depthName + " - " + collection + " - " + licenseId + " - " + pageSize + " - " + flagId);

    	Page<Target> pageTargets = Target.pageTargets(pageNo, pageSize, sortBy, order, filter, curatorId, organisationId, subject, crawlFrequencyName, depthName, collection, licenseId, flagId);
    	
    	
		User user = User.findByEmail(request().username());
    	List<License> licenses = License.findAllLicenses();
    	
    	List<Long> subjectIds = new ArrayList<Long>();
        String[] subjects = subject.split(", ");
        for (String sId : subjects) {
        	if (StringUtils.isNotEmpty(sId)) {
	        	Long subjectId = Long.valueOf(sId);
	        	subjectIds.add(subjectId);
        	}
        }
        JsonNode subjectData = getSubjectsDataByIds(subjectIds);
    		
    	List<Long> collectionIds = new ArrayList<Long>();
        String[] collections = collection.split(", ");
        for (String cId : collections) {
        	if (StringUtils.isNotEmpty(cId)) {
	        	Long collectionId = Long.valueOf(cId);
	        	collectionIds.add(collectionId);
        	}
        }
        JsonNode collectionData = getCollectionsDataByIds(collectionIds);

		List<User> users = User.findAllSorted();
		List<Organisation> organisations = Organisation.findAllSorted();
		CrawlFrequency[] crawlFrequencies = Const.CrawlFrequency.values();
		List<Flag> flags = Flag.findAllFlags();

		Logger.debug("getTotalRowCount: " + pageTargets.getTotalRowCount());
		
        return ok(list.render(
			"Targets", 
			user, 
			filter,
			pageTargets,
			sortBy, 
			order, 
	    	curatorId, 
	    	organisationId, 
	    	subject, 
	    	crawlFrequencyName, 
	    	depthName, 
	    	collection, 
	    	licenseId, 
	    	pageSize,
	    	flagId,
	    	licenses, 
	    	collectionData, 
	    	subjectData,
	    	users,
	    	organisations,
	    	crawlFrequencies, flags)
		);
    }
    
    public static Result view(Long id) {
    	Target target = Target.findById(id);
    	User user = User.findByEmail(request().username());
        return ok(view.render(target, user));
    }
    
    public static Result viewAct(String url) {
    	Target target = Target.findByUrl(url);
    	User user = User.findByEmail(request().username());
        return ok(view.render(target, user));
    }

    public static Result viewWct(String url) {
    	Target target = Target.findByWct(url);
    	User user = User.findByEmail(request().username());
        return ok(view.render(target, user));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String url) {
        JsonNode jsonData = null;
        if (url != null) {
	        List<Target> targets = Target.filterActiveUrl(url);
	        jsonData = Json.toJson(targets);
        }
        return ok(jsonData);
    }

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result searchTargets() {
        DynamicForm requestData = Form.form().bindFromRequest();
        if (requestData.get("pageSize") == null || (requestData.get("pageSize") != null && !Utils.INSTANCE.isNumeric(requestData.get("pageSize")))) {
  			flash("message", "You may only enter a numeric page size.");
	        return GO_HOME;
    	}    	
    	
    	String action = requestData.get("action");
    	String filter = requestData.get("filter");

//    	if (StringUtils.isBlank(query)) {
//			Logger.debug("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return GO_HOME;
//    	}    	

    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");
    	int pageSize = Integer.parseInt(requestData.get("pageSize"));
    	Long curatorId = Long.parseLong(requestData.get("curator"));
    	Long organisationId = Long.parseLong(requestData.get("organisation"));
    	Long licenseId = Long.parseLong(requestData.get("license"));
    	String depthName = requestData.get("depth");
    	String crawlFrequencyName = requestData.get("crawlFrequency");
    	Long flagId = Long.parseLong(requestData.get("flag"));
    	
        String subjectSelect = requestData.get("subjectSelect").replace("\"", "");
        String collectionSelect = requestData.get("collectionSelect").replace("\"", "");
    	
    	Logger.debug(filter + " " + pageNo + " " + sort + " " + order + " " + pageSize + " " + curatorId + " " + curatorId + " " + licenseId + " " + depthName + " " + crawlFrequencyName + " " + flagId + " " + collectionSelect + " " + subjectSelect);
    	
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("add")) {
    			return redirect(
    	        		routes.TargetController.newForm(filter)
    			        );
    		} 
    		else if (action.equals("clear")) {
    			return GO_HOME;
    		} 
    		else if (action.equals("export")) {
//    			List<Target> exportTargets = new ArrayList<Target>();
//    	    	Page<Target> page = Target.pageTargets(0, pageSize, sort, order, query, curator, organisation, 
//    					subject, crawlFrequency, depth, collection, license, flag); 
//    			int rowCount = page.getTotalRowCount();
//    	    	Page<Target> pageAll = Target.pageTargets(0, rowCount, sort, order, query, curator, organisation, 
//    					subject, crawlFrequency, depth, collection, license, flag); 
//    			exportTargets.addAll(pageAll.getList());
//				Logger.debug("export size: " + exportTargets.size());
//    			export(exportTargets);
//    	    	return redirect(routes.TargetController.list(pageNo, sort, order, query, curator, organisation, 
//    	    			subject, crawlFrequency, depth, collection, license, pageSize, flag));
    		} 
    		else if (action.equals("search") || action.equals("apply")) {
    			if (StringUtils.isBlank(filter)) {
    	  			flash("message", "Please enter in the search field.");
    			}
    	        return redirect(routes.TargetController.list(pageNo, sort, order, filter, curatorId, organisationId, subjectSelect, crawlFrequencyName, depthName, collectionSelect, licenseId, pageSize, flagId));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    	return ok("");
    }
    
    /**
     * This method exports selected targets to CSV file.
     * @param list of Target objects
     * @return
     */
    public static void export(List<Target> targetList) {
    	Logger.debug("export() targetList size: " + targetList.size());

        StringWriter sw = new StringWriter();
        for (int i = 0; i < Const.targetExportMap.size(); i++) {
        {
            for (Map.Entry<String, Integer> entry : Const.targetExportMap.entrySet())
//        	Logger.debug("export key: " + entry.getKey());
            	if (entry.getValue() == i) {
	            	sw.append(entry.getKey());
		 	    	sw.append(Const.CSV_SEPARATOR);
            	}
            }
        }

        sw.append(Const.CSV_LINE_END);
 	    
 	    if (targetList != null && targetList.size() > 0) {
 	    	Iterator<Target> itr = targetList.iterator();
 	    	while (itr.hasNext()) {
 	    		Target target = itr.next();
// 	        	Logger.debug("export target: " + target); 	    		
 	            for (int i = 0; i < Const.targetExportMap.size(); i++) {
		 	        for (Map.Entry<String, Integer> entry : Const.targetExportMap.entrySet()) {
			 	   		Field[] fields = Target.class.getFields();
			 			for (Field field : fields) {
		 	               if (entry.getValue() == i) {
//		 	                   Logger.debug("field.name: " + field.getName() + ", entry.getkey: " + entry.getKey());
		 	                   if (field.getName().equals(entry.getKey())) {
		 	                	   try {
										Object value = field.get(target);
//				 	                    Logger.debug("value: " + value);
				 	                    if (field.getName().equals(Const.AUTHOR)) {
				 	                    	if (value != null) {
				 	                    		value = User.findByUrl((String) value).name;
				 	                    	}
				 	                    }
				 	                    // TODO: CREATED_AT
				 	                    if (field.getName().equals(Const.CREATED_AT)) {
				 	                    	if (value != null) {
				 	                    		value = Utils.INSTANCE.showTimestampInTable((String) value);
				 	                    	}
				 	                    }
										if (field.getType().equals(String.class)) {
								    		sw.append((String) value);
									 	    sw.append(Const.CSV_SEPARATOR);
										}
										if (field.getType().equals(Long.class)) {
								    		sw.append(String.valueOf(((Long) value)));
									 	    sw.append(Const.CSV_SEPARATOR);
										}
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									}
		 	                   }
		 	               }
			 			}
		 	        }
	            }
	 	 	    sw.append(Const.CSV_LINE_END);
 	    	}
 	    }

    	Utils.INSTANCE.generateCsvFile(Const.EXPORT_FILE, sw.toString());
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	
    	String action = form.get("action");
    	String query = form.get("filter");
    	int pageNo = Integer.parseInt(form.get("p"));
    	String sort = form.get("s");
    	String order = form.get("o");

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("add")) {
    			return redirect(
	        		routes.TargetController.newForm(query)
			        );
    		} 
    		else if (action.equals("search")) {
    			Logger.debug("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.TargetController.lookup(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     * @param collection_url Collection where targets search occurs
     */
    public static Result collectionTargets(int pageNo, String sortBy, String order, String filter, 
    		Long collectionId) {
    	Logger.debug("Targets.collectionTargets()");
    	Collection collection = Collection.findById(collectionId);
    	Logger.debug("Collection: " + collection);
    	Page<Target> pages = Target.pageCollectionTargets(pageNo, 10, sortBy, order, filter, collection.id);
        return ok(
        		sites.render(
        			collection,  
        			User.findByEmail(request().username()), 
        			filter, 
        			pages, 
        			sortBy, 
        			order) 
        	);
    }
	    

    /**
    * Display the paginated list of targets.
    *
    * @param page Current page number (starts from 0)
    * @param sortBy Column to be sorted
    * @param order Sort order (either asc or desc)
    * @param filter Filter applied on target urls
    * @param subject_url Subject where targets search occurs
    */
    public static Result subjectTargets(int pageNo, String sortBy, String order, String filter,
	    Long subjectId) {
	    Logger.debug("Targets.subjectTargets()");
	    return ok(
		    views.html.subjects.sites.render(
		    Taxonomy.findById(subjectId),
		    User.findByEmail(request().username()),
		    filter,
		    Target.pageSubjectTargets(pageNo, 10, sortBy, order, filter, subjectId),
		    sortBy,
		    order)
	    );
    }
    
    /**
     * Display the paginated list of targets for given organisation.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     * @param collection_url Collection where targets search occurs
     */
    public static Result organisationTargets(int pageNo, String sortBy, String order, String filter, 
    		Long organisationId) {
    	Logger.debug("Targets.organisationTargets()");
    	
    	User user = User.findByEmail(request().username());
    	
		Organisation organisation = Organisation.findById(organisationId);  
    	
		Page<Target> pageTargets = Target.pageOrganisationTargets(pageNo, 10, sortBy, order, filter, organisation.id);
		
        return ok(
        		views.html.organisations.sites.render(
        			organisation,
        			user, 
        			filter, 
        			pageTargets, 
        			sortBy, 
        			order) 
        	);
    }
	    
    /**
     * This method enables searching for given URL and particular collection.
     * @return
     */
    public static Result searchTargetsByCollection() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url");

//    	if (StringUtils.isBlank(query)) {
//			Logger.debug("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return redirect(routes.CollectionController.list(0, "title", "asc", ""));
//    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String collection_url = form.get(Const.COLLECTION_URL);

    	Collection collection = Collection.findByUrl(collection_url);
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.debug("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.TargetController.collectionTargets(pageNo, sort, order, query, collection.id));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * This method enables searching for given URL and particular subject.
     * @return
     */
    public static Result searchTargetsBySubject() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url");

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String subject_url = form.get("subject_url");
    	Long subjectId = Long.valueOf(subject_url);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.debug("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.TargetController.subjectTargets(pageNo, sort, order, query, subjectId));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * This method enables searching for given URL and particular organisation.
     * @return
     */
    public static Result searchTargetsByOrganisation() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	String action = form.get(Const.ACTION);
    	String query = form.get(Const.URL);

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String organisation_id = form.get("organisation_id");
    	Long organisationId = Long.valueOf(organisation_id);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.debug("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.TargetController.organisationTargets(pageNo, sort, order, query, organisationId));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     * @param user_url User for whom targets search occurs
     * @param subject Taxonomy of type subject
     * @param collection Taxonomy of type collection
     */
    public static Result userTargets(int pageNo, String sortBy, String order, String filter, 
    		Long userId, Long subjectId, Long collectionId) {
    	User curator = User.findById(userId);
    	User user = User.findByEmail(request().username());
    	
    	Page<Target> pageTargets = Target.pageUserTargets(pageNo, 10, sortBy, order, filter, userId, subjectId, collectionId);
    	
    	List<Subject> subjects = Subject.findAllSubjects();
    	List<Collection> collections = Collection.findAllCollections();
    	
    	Logger.debug("Targets.collectionTargets() " + userId + ", " + subjectId + ", " + collectionId);
    	
        return ok(
        		usersites.render(
        			curator,  
        			user, 
        			filter, 
        			pageTargets, 
        			sortBy, 
        			order,
        			subjectId,
        			collectionId,
        			subjects,
        			collections)
        	);
    }	        
        
    /**
     * This method enables searching for given URL and particular user.
     * @return
     */
    public static Result searchTargetsByUser() {
    	
    	DynamicForm requestData = DynamicForm.form().bindFromRequest();
    	String action = requestData.get("action");
    	String query = requestData.get("url");

    	String user_id = requestData.get("userId");
    	Long userId = Long.valueOf(user_id);
    	
    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");

    	String subject = requestData.get("subjectId");
    	Long subjectId = null;
    	if (StringUtils.isNotBlank(subject)) {
    		subjectId = Long.valueOf(subject);
    	}
    	
    	String collection = requestData.get("collectionId");
    	Long collectionId = null;
    	if (StringUtils.isNotBlank(collection)) {
    		collectionId = Long.valueOf(collection);
    	}
    	
//    	if (StringUtils.isBlank(query)) {
//			Logger.debug("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return redirect(routes.Targets.userTargets(pageNo, sort, order, query, user_url, subject, collection));
//    	}    	
    	
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("add")) {
    			String title = requestData.get("filter");
    	    	return redirect(routes.TargetController.newForm(title));
    		}
    		if (action.equals("search")) {
    			Logger.debug("searching " + pageNo + " " + sort + " " + order + " " + userId + ", " + subjectId + ", " + collectionId);
    	    	return redirect(routes.TargetController.userTargets(pageNo, sort, order, query, userId, subjectId, collectionId));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }
        
    public static Result GO_HOME = redirect(routes.TargetController.list(0, "title", "asc", "", 0, 0, "", "", "", "", 0, 10, 0));
       
    public static Result newForm(String title) {
    	User user = User.findByEmail(request().username());
		Form<Target> filledForm = Form.form(Target.class);
		Target target = new Target();
		if (StringUtils.isNotBlank(title)) {
			try {
			  new URL(title);
			  target.formUrl = title;
			} catch (MalformedURLException e) {
				target.title = title;
			}
		}
        target.revision = Const.INITIAL_REVISION;
        target.active = Boolean.TRUE;
        
    	target.authorUser = user;
    	target.organisation = user.organisation;
    	
		Logger.debug("add target with url: " + target.url);
		Logger.debug("target title: " + target.title);
		
		filledForm = filledForm.fill(target);

		JsonNode collectionData = getCollectionsData();
		JsonNode subjectData = getSubjectsData();
		
		Map<String,String> authors = User.options();
		List<Tag> tags = Tag.findAllTags();
		List<Flag> flags= Flag.findAllFlags();
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> languages = Const.TargetLanguage.options();
		Map<String,String> selectionTypes = Const.SelectionType.options();
		Map<String,String> scopeTypes = Const.ScopeType.options();
		Map<String,String> depthTypes = Const.DepthType.options();
		List<License> licenses = License.findAllLicenses();
		Map<String,String> licenseStatuses = License.LicenseStatus.options();
		Map<String,String> crawlFrequencies = Const.CrawlFrequency.options();
		Map<String,String> siteStatuses = Const.SiteStatus.options();
		Map<String,String> organisations = Organisation.options();
	    return ok(newForm.render(filledForm, user, collectionData, subjectData, authors, tags, flags, qaIssues, languages, selectionTypes, scopeTypes, depthTypes, licenses, licenseStatuses, crawlFrequencies, siteStatuses, organisations, null));
    }
    
    /**
     * Display the target edit panel for this URL.
     * @param url The target identifier URL
     */
    public static Result edit(Long id) {
		Logger.debug("Targets.edit() id: " + id);
		Target target = Target.findById(id);
		target.formUrl = target.fieldUrl();
		target.subjectSelect = target.subjectIdsAsString();
		target.collectionSelect = target.collectionIdsAsString();
		Form<Target> filledForm = targetForm.fill(target);
		User user = User.findByEmail(request().username());
		JsonNode collectionData = getCollectionsData(target.collections);
		JsonNode subjectData = getSubjectsData(target.subjects);

		Map<String,String> authors = User.options();
		List<Tag> tags = Tag.findAllTags();
		List<Tag> targetTags = target.tags;
		List<Flag> flags= Flag.findAllFlags();
		List<Flag> targetFlags = target.flags;
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> languages = Const.TargetLanguage.options();
		Map<String,String> selectionTypes = Const.SelectionType.options();
		Map<String,String> scopeTypes = Const.ScopeType.options();
		Map<String,String> depthTypes = Const.DepthType.options();
		List<License> licenses = License.findAllLicenses();
		List<License> targetLicenses = target.licenses;
		Map<String,String> licenseStatuses = License.LicenseStatus.options();
		Map<String,String> crawlFrequencies = Const.CrawlFrequency.options();
		Map<String,String> siteStatuses = Const.SiteStatus.options();
		Map<String,String> organisations = Organisation.options();
        return ok(edit.render(filledForm, user, id, collectionData, subjectData, authors, tags, flags, qaIssues, languages, selectionTypes, scopeTypes, depthTypes, licenses, licenseStatuses, crawlFrequencies, siteStatuses, organisations, null, targetTags, targetFlags, targetLicenses));
    }
    
    public static Result delete(Long id) {
    	Target target = Target.findById(id);
    	target.delete();
    	return redirect(routes.TargetController.index());
    }
    
    /**
     * This method shows selected revision of a Target by given ID.
     * @param id
     * @return
     */
    public static Result viewrevision(Long id) {
    	Target target = Target.findById(id);
    	User user = User.findByEmail(request().username());
        return ok(view.render(target, user));
    }
    
    /**
     * This method provides data exports for each possible crawl-frequency. 
     * For each frequency this contains a list of Targets and associated 
     * crawl metadata.
     * @param frequency The crawl frequency e.g. 'daily'
     * @return list of Target objects
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result exportByFrequencyJson(String frequency) {
        JsonNode jsonData = null;
        if (frequency != null) {
	        List<Target> targets = new ArrayList<Target>();
        	targets = Target.exportByFrequency(frequency);
	        jsonData = Json.toJson(targets);
        }
        return ok(jsonData);
    }
    
    /**
     * This method provides data exports for each possible crawl-frequency that are in legal deposit. 
     * For each frequency this contains a list of Targets and associated 
     * crawl metadata.
     * @param frequency The crawl frequency e.g. 'daily'
     * @return list of Target objects
     * @throws ActException 
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result exportLdFrequencyJson(String frequency) throws ActException {
        JsonNode jsonData = null;
        if (frequency != null) {
	        List<Target> targets = new ArrayList<Target>();
        	try {
				targets = Target.exportLdFrequency(frequency);
			} catch (MalformedURLException | WhoisException
					| URISyntaxException e) {
				throw new ActException(e);
			}
	        jsonData = Json.toJson(targets);
        }
        return ok(jsonData);
    }

    /**
     * Example form with validation
     * @return blank form for data entry
     */
    public static Result blank() {
        Logger.debug("blank()");
        return ok(blank.render(targetForm, User.findByEmail(request().username())));
    }
    
    public static Result saveBlank() {
    	Form<Target> filledForm = targetForm.bindFromRequest();
	    if(filledForm.hasErrors()) {
	    	Logger.debug("hasErrors: " + filledForm.hasErrors());
        	for (String key : filledForm.errors().keySet()) {
        		Logger.debug("" + key);
        	}
	        return badRequest(blank.render(filledForm, User.findByEmail(request().username())));
	    } else {
	        flash("success", "You've saved");
	    	Logger.debug("saved");
	        return ok(blank.render(filledForm, User.findByEmail(request().username())));
	    }
    }
    
	/**
	 * This method indicates to the user in a target record if data has been entered 
	 * by other users relating to NPLD status in another target record at a higher 
	 * level in the domain. 
	 * This is to avoid duplication of effort: users should not need to spend time 
	 * (outside ACT) doing the necessary research to populate the 'UK Postal Address', 
	 * 'Via Correspondence', and 'Professional Judgment' fields for abc.co.uk/directory 
	 * if those fields are already populated in a target record for abc.co.uk 
	 * @param fieldUrl The target URL
	 * @return result as a flag. Result is true if:
	 * 		   (i) one or more of the three fields named above is not null in any other 
	 *             target record at a higher level within the same domain AND 
	 *         (ii) where both 'UK hosting' and 'UK top-level domain' = No.
	 */
//    public static boolean indicateNpldStatus(String fieldUrl) {
//    	boolean res = false;
//    	if (Target.getNpldStatusList(fieldUrl).size() > 0) {
//    		res = true;
//    	}
//    	Logger.debug("indicateNpldStatus() res: " + res);
//    	return res;
//    }
    
	/**
	 * This method should give a list of the Target Titles and URLs for the 
	 * first three examples, in descending order of date of creation of the record. 
	 * @param fieldUrl The target URL
	 * @return result as a string
	 */
    public static String showNpldStatusList(String fieldUrl) {
    	String res = "";
//        try {
//            StringBuilder sb = new StringBuilder();
//        	List<Target> targets = Target.getNpldStatusList(fieldUrl);
//        	Iterator<Target> itr = targets.iterator();
//        	while (itr.hasNext()) {
//        		Target target = itr.next();
//        		sb.append(target.title + " " + target.fieldUrl());
//                sb.append(System.getProperty("line.separator"));
//        	}
//            res = sb.toString();
//    	} catch (Exception e) {
//            Logger.error("showNpldStatusList() " + e.getMessage());
//        }
    	return res;
    }
    
	/**
	 * This method prepares Target form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info(Form<Target> form, Long id) {
//		Long id = Long.valueOf(requestData.get("id"));
//		Target target = Target.findById(id); 
//
//		Form<Target> targetFormNew = targetForm.fill(target);
		
		User user = User.findByEmail(request().username());
		JsonNode collectionData = getCollectionsData();
		JsonNode subjectData = getSubjectsData();

		Target target = Target.findById(id);
		Map<String,String> authors = User.options();
		List<Tag> tags = Tag.findAllTags();
		List<Tag> targetTags = target.tags;
		List<Flag> flags= Flag.findAllFlags();
		List<Flag> targetFlags = target.flags;
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> languages = Const.TargetLanguage.options();
		Map<String,String> selectionTypes = Const.SelectionType.options();
		Map<String,String> scopeTypes = Const.ScopeType.options();
		Map<String,String> depthTypes = Const.DepthType.options();
		List<License> licenses = License.findAllLicenses();
		List<License> targetLicenses = target.licenses;
		Map<String,String> licenseStatuses = License.LicenseStatus.options();
		Map<String,String> crawlFrequencies = Const.CrawlFrequency.options();
		Map<String,String> siteStatuses = Const.SiteStatus.options();
		Map<String,String> organisations = Organisation.options();

		DynamicForm requestData = Form.form().bindFromRequest();
        String tabStatus = requestData.get("tabstatus");
        return badRequest(edit.render(form, user, id, collectionData, subjectData, authors, tags, flags, qaIssues, languages, selectionTypes, scopeTypes, depthTypes, licenses, licenseStatuses, crawlFrequencies, siteStatuses, organisations, tabStatus, targetTags, targetFlags, targetLicenses));
    }

	public static Result newInfo(Form<Target> form) {
//      DynamicForm requestData = Form.form().bindFromRequest();
//		Long id = Long.valueOf(requestData.get("id"));
//		Target target = Target.findById(id); 
//
//		Form<Target> targetFormNew = targetForm.fill(target);
		
		User user = User.findByEmail(request().username());
		JsonNode collectionData = getCollectionsData();
		JsonNode subjectData = getSubjectsData();
		Map<String,String> authors = User.options();
		List<Tag> tags = Tag.findAllTags();
		List<Flag> flags= Flag.findAllFlags();
		Map<String,String> qaIssues = QaIssue.options();
		Map<String,String> languages = Const.TargetLanguage.options();
		Map<String,String> selectionTypes = Const.SelectionType.options();
		Map<String,String> scopeTypes = Const.ScopeType.options();
		Map<String,String> depthTypes = Const.DepthType.options();
		List<License> licenses = License.findAllLicenses();
		Map<String,String> licenseStatuses = License.LicenseStatus.options();
		Map<String,String> crawlFrequencies = Const.CrawlFrequency.options();
		Map<String,String> siteStatuses = Const.SiteStatus.options();
		Map<String,String> organisations = Organisation.options();
		DynamicForm requestData = Form.form().bindFromRequest();
        String tabStatus = requestData.get("tabstatus");
		return badRequest(newForm.render(form, user, collectionData, subjectData, authors, tags, flags, qaIssues, languages, selectionTypes, scopeTypes, depthTypes, licenses, licenseStatuses, crawlFrequencies, siteStatuses, organisations, tabStatus));
	}
	
    public static Result update(Long id) throws ActException {
    	DynamicForm requestData = form().bindFromRequest();
	    Map<String, String[]> formParams = request().body().asFormUrlEncoded();
        Form<Target> filledForm = form(Target.class).bindFromRequest();

        String action = requestData.get("action");

        if (StringUtils.isNotEmpty(action)) {
	    	if (action.equals("request")) {
    	        return redirect(routes.CrawlPermissionController.licenceRequestForTarget(id)); 
	    	} else if (action.equals("save")) {
		    	Logger.debug("hasGlobalErrors: " + filledForm.hasGlobalErrors());

		        if (filledForm.hasErrors()) {
		        	Logger.debug("hasErrors: " + filledForm.errors());
		            return info(filledForm, id);
		        }
		        
		        String wctId = requestData.get("wctId");
		        
		        if (StringUtils.isNotBlank(wctId) && !Utils.INSTANCE.isNumeric(wctId)) {
		        	flash("message", "Only numeric values are valid identifiers. Please check field 'WCT ID'.");
		            return info(filledForm, id);
			  	}    	
		
		        String sptId = requestData.get("sptId");
		
			  	if (StringUtils.isNotBlank(sptId) && !Utils.INSTANCE.isNumeric(sptId)) {
			          Logger.debug("Only numeric values are valid identifiers. Please check field 'SPT ID'.");
						flash("message", "Only numeric values are valid identifiers. Please check field 'SPT ID'.");
			            return info(filledForm, id);
			  	}    	
			
			  	String legacySiteId = requestData.get("legacySiteId");
			  	if (StringUtils.isNotBlank(legacySiteId) && !Utils.INSTANCE.isNumeric(legacySiteId)) {
			          Logger.debug("Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
						flash("message", "Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
			            return info(filledForm, id);
			  	}    	
		
			  	String subjectString = requestData.get("subjectSelect");
			  	if (StringUtils.isBlank(subjectString)) {
					flash("message", "Please choose a subject(s).");
		            return info(filledForm, id);
			  	}
			  	
		        String fieldUrl = requestData.get("formUrl");
		        
		        if (StringUtils.isNotEmpty(fieldUrl)) {
		            String[] urls = fieldUrl.split(",");
		            List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
		            
		            for (String url : urls) {
		            	FieldUrl fu = FieldUrl.findByUrl(url.trim());
		            	if (fu == null) {
		                    URL uri;
							try {
				            	Logger.debug("url: " + url.trim());
								uri = new URI(url.trim()).normalize().toURL();
			        			String extFormUrl = uri.toExternalForm();
				            	fu = new FieldUrl(extFormUrl.trim());
				            	Logger.debug("extFormUrl: " + extFormUrl);
							} catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
		//						flash("message", e.getMessage());
		//						e.printStackTrace();
					            ValidationError ve = new ValidationError("formUrl", "The URL entered is not valid. Please check and correct it, and click Save again");
					            filledForm.reject(ve);
					            return newInfo(filledForm);
					        }
		            	}
		            	fieldUrls.add(fu);
		            }
		            filledForm.get().fieldUrls = fieldUrls;
		            Logger.debug("fieldUrls: " + fieldUrls);
		        }        
		        
		        String crawlStartDate = requestData.get("crawlStartDateText");
		    	if (StringUtils.isNotEmpty(crawlStartDate)) {
					DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					try {
						Date date = formatter.parse(crawlStartDate);
						filledForm.get().crawlStartDate = date;
					} catch (ParseException e) {
						e.printStackTrace();
			            return info(filledForm, id);
					}
		    	}
		    	
		        String crawlFrequency = filledForm.get().crawlFrequency;
	        	Logger.debug("crawl frequency: " + crawlFrequency);
	        	Logger.debug("crawlStartDate: " + crawlStartDate);
		        if (crawlFrequency.equals(CrawlFrequency.DOMAINCRAWL.name())) {
		        	if (StringUtils.isEmpty(crawlStartDate)) {
			            ValidationError ve = new ValidationError("crawlStartDateText", "Start Date is required when 'Domain Crawl Only' is selected");
			            filledForm.reject(ve);
			            return info(filledForm, id);
		        	}
		        }
		        
		        String crawlEndDate = requestData.get("endStartDateText");
		    	if (StringUtils.isNotEmpty(crawlEndDate)) {
					DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					try {
						Date date = formatter.parse(crawlEndDate);
						filledForm.get().crawlEndDate = date;
					} catch (ParseException e) {
						e.printStackTrace();
			            return info(filledForm, id);
					}
		    	}
		        
		//        String qaIssueId = requestData.get("qaIssueId");
		//        if (StringUtils.isNotEmpty(qaIssueId)) {
		//        	Long qaId = Long.valueOf(qaIssueId);
		//        	QaIssue qaIssue = QaIssue.findById(qaId);
		//        	filledForm.get().qaIssue = qaIssue;
		//        }
		        	            
		        List<Tag> newTags = new ArrayList<Tag>();
		        String[] tagValues = formParams.get("tagsList");
		
		        if (tagValues != null) {
		            for(String tagValue: tagValues) {
		            	Long tagId = Long.valueOf(tagValue);
		            	Tag tag = Tag.findById(tagId);
		            	newTags.add(tag);
		            }
		            filledForm.get().tags = newTags;
		        }
		        
		        List<Flag> newFlags = new ArrayList<Flag>();
		        String[] flagValues = formParams.get("flagsList");
		
		        if (flagValues != null) {
		            for(String flagValue: flagValues) {
		            	Long flagId = Long.valueOf(flagValue);
		            	Flag flag =  Flag.findById(flagId);
		            	newFlags.add(flag);
		            }
		            filledForm.get().flags = newFlags;
		        }
		        
		        List<Subject> newSubjects = new ArrayList<Subject>();
		        String subjectSelect = requestData.get("subjectSelect").replace("\"", "");
		        Logger.debug("subjectSelect: " + subjectSelect);
		        if (StringUtils.isNotEmpty(subjectSelect)) {
		            String[] subjects = subjectSelect.split(", ");
		            for (String sId : subjects) {
		//            	sId = sId.replace("\"", "");
		            	Long subjectId = Long.valueOf(sId);
		            	Subject subject = Subject.findById(subjectId);
		            	newSubjects.add(subject);
		            }
		            filledForm.get().subjects = newSubjects;
		        }
		        
		        List<Collection> newCollections = new ArrayList<Collection>();
		        String collectionSelect = requestData.get("collectionSelect").replace("\"", "");
		        Logger.debug("collectionSelect: " + collectionSelect);
		        if (StringUtils.isNotEmpty(collectionSelect)) {
		            String[] collections = collectionSelect.split(", ");
		            for (String cId : collections) {
		//            	sId = sId.replace("\"", "");
		            	Long collectionId = Long.valueOf(cId);
		            	Collection collection = Collection.findById(collectionId);
		            	newCollections.add(collection);
		            }
		            filledForm.get().collections = newCollections;
		        }
		        
		//        String organisationId = requestData.get("organisationId");
		//        if (StringUtils.isNotEmpty(organisationId) || !organisationId.equals("")) {
		//        	Long oId = Long.valueOf(organisationId);
		//        	Organisation organisation = Organisation.findById(oId);
		//        	filledForm.get().organisation = organisation;
		//        }
		//        
		//        String authorId = requestData.get("authorId");
		//        if (StringUtils.isNotEmpty(authorId)) {
		//        	Long aId = Long.valueOf(authorId);
		//        	User author = User.findById(aId);
		//        	filledForm.get().authorUser = author;
		//        }
		
		        String dateOfPublication = requestData.get("dateOfPublicationText");
		    	if (StringUtils.isNotEmpty(dateOfPublication)) {
					DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					try {
						Date date = formatter.parse(dateOfPublication);
						filledForm.get().dateOfPublication = date;
					} catch (ParseException e) {
						e.printStackTrace();
			            return info(filledForm, id);
					}
		    	}
		        
		        List<License> newLicenses = new ArrayList<License>();
		        String[] licenseValues = formParams.get("licensesList");
		
		        if (licenseValues != null) {
		            for(String licenseValue: licenseValues) {
		            	Long licenseId = Long.valueOf(licenseValue);
		            	License license =  License.findById(licenseId);
		            	newLicenses.add(license);
		            }
		            filledForm.get().licenses = newLicenses;
		        }
		    	
		        try {
			        filledForm.get().isUkHosting = filledForm.get().isUkHosting();
					filledForm.get().isTopLevelDomain = filledForm.get().isTopLevelDomain();
			        filledForm.get().isUkRegistration = filledForm.get().isUkRegistration();
					Logger.debug("isUkHosting: " + filledForm.get().isUkHosting);
					Logger.debug("isTopLevelDomain: " + filledForm.get().isTopLevelDomain);
					Logger.debug("isUkRegistration: " + filledForm.get().isUkRegistration);
				} catch (MalformedURLException | WhoisException | URISyntaxException e) {
					throw new ActException(e);
				}

				filledForm.get().update(id);
		        flash("message", "Target " + filledForm.get().title + " has been updated");
		    	return redirect(routes.TargetController.view(filledForm.get().id));
	        } else if (action.equals("archive")) {
		        return redirect(routes.TargetController.archive(id)); 
	        } else if (action.equals("delete")) {
		        return redirect(routes.TargetController.delete(id)); 
	        }
        }
    	return null;
    }
    
	/**
     * This method saves changes on given target in a new target object
     * completed by revision comment. The "version" field in the Target object
     * contains the timestamp of the change and the last version is marked by
     * flag "active". Remaining Target objects with the same URL are not active.
     * @return
	 * @throws ActException 
     */
    public static Result save() throws ActException {

    	DynamicForm requestData = form().bindFromRequest();
	    Map<String, String[]> formParams = request().body().asFormUrlEncoded();

        Form<Target> filledForm = form(Target.class).bindFromRequest();
        if(filledForm.hasErrors()) {
    		Logger.debug("errors: " + filledForm.errors());
            return newInfo(filledForm);
        }

        String wctId = requestData.get("wctId");
        
        if (StringUtils.isNotBlank(wctId) && !Utils.INSTANCE.isNumeric(wctId)) {
        	flash("message", "Only numeric values are valid identifiers. Please check field 'WCT ID'.");
            return newInfo(filledForm);
	  	}    	

        String sptId = requestData.get("sptId");

	  	if (StringUtils.isNotBlank(sptId) && !Utils.INSTANCE.isNumeric(sptId)) {
	          Logger.debug("Only numeric values are valid identifiers. Please check field 'SPT ID'.");
				flash("message", "Only numeric values are valid identifiers. Please check field 'SPT ID'.");
	            return newInfo(filledForm);
	  	}    	
	
	  	String legacySiteId = requestData.get("legacySiteId");
	  	if (StringUtils.isNotBlank(legacySiteId) && !Utils.INSTANCE.isNumeric(legacySiteId)) {
	          Logger.debug("Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
				flash("message", "Only numeric values are valid identifiers. Please check field 'LEGACY SITE ID'.");
	            return newInfo(filledForm);
	  	}    	

	  	String subjectString = requestData.get("subjectSelect");
	  	if (StringUtils.isBlank(subjectString)) {
			flash("message", "Please choose a subject(s).");
            return newInfo(filledForm);
	  	}

//			  	String selectionType = requestData.get("selectionType");
//			  	if (StringUtils.isEmpty(selectionType)) {
//					flash("message", "Please choose a selection.");
//		            return newInfo(filledForm);
//			  	}
	  	
        String fieldUrl = requestData.get("formUrl");
        
        if (StringUtils.isNotEmpty(fieldUrl)) {
            String[] urls = fieldUrl.split(",");
            List<FieldUrl> fieldUrls = new ArrayList<FieldUrl>();
            
            for (String url : urls) {
            	FieldUrl fu = FieldUrl.findByUrl(url.trim());
            	if (fu == null) {
                    URL uri;
					try {
		            	Logger.debug("url: " + url.trim());
						uri = new URI(url.trim()).normalize().toURL();
	        			String extFormUrl = uri.toExternalForm();
		            	fu = new FieldUrl(extFormUrl.trim());
		            	Logger.debug("extFormUrl: " + extFormUrl);
					} catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
//								flash("message", e.getMessage());
//								e.printStackTrace();
			            ValidationError ve = new ValidationError("formUrl", "The URL entered is not valid. Please check and correct it, and click Save again");
			            filledForm.reject(ve);
			            return newInfo(filledForm);
			        }
            	}
            	fieldUrls.add(fu);
            }
            filledForm.get().fieldUrls = fieldUrls;
            Logger.debug("fieldUrls: " + fieldUrls);
        }		        
        
//	            String qaIssueId = requestData.get("qaIssueId");
//	            if (StringUtils.isNotEmpty(qaIssueId)) {
//	            	Long qaId = Long.valueOf(qaIssueId);
//	            	QaIssue qaIssue = QaIssue.findById(qaId);
//	            	filledForm.get().qaIssue = qaIssue;
//	            }

        String crawlStartDate = requestData.get("crawlStartDateText");
    	if (StringUtils.isNotEmpty(crawlStartDate)) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date date = formatter.parse(crawlStartDate);
				filledForm.get().crawlStartDate = date;
			} catch (ParseException e) {
				e.printStackTrace();
	            return newInfo(filledForm);
			}
    	}
    	
        String crawlFrequency = filledForm.get().crawlFrequency;
        if (crawlFrequency.equals(CrawlFrequency.DOMAINCRAWL.name())) {
        	if (StringUtils.isEmpty(crawlStartDate)) {
	            ValidationError ve = new ValidationError("crawlStartDateText", "Start Date is required when 'Domain Crawl Only' is selected");
	            filledForm.reject(ve);
	            return newInfo(filledForm);
        	}
        }
        
        String crawlEndDate = requestData.get("endStartDateText");
    	if (StringUtils.isNotEmpty(crawlEndDate)) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date date = formatter.parse(crawlEndDate);
				filledForm.get().crawlEndDate = date;
			} catch (ParseException e) {
				e.printStackTrace();
	            return newInfo(filledForm);
			}
    	}        
        
        List<Tag> newTags = new ArrayList<Tag>();
        String[] tagValues = formParams.get("tagsList");

        if (tagValues != null) {
            for(String tagValue: tagValues) {
            	Long tagId = Long.valueOf(tagValue);
            	Tag tag = Tag.findById(tagId);
            	newTags.add(tag);
            }
            filledForm.get().tags = newTags;
        }
        
        List<Flag> newFlags = new ArrayList<Flag>();
        String[] flagValues = formParams.get("flagsList");

        if (flagValues != null) {
            for(String flagValue: flagValues) {
            	Long flagId = Long.valueOf(flagValue);
            	Flag flag =  Flag.findById(flagId);
            	newFlags.add(flag);
            }
            filledForm.get().flags = newFlags;
        }
        
        List<Subject> newSubjects = new ArrayList<Subject>();
        String subjectSelect = requestData.get("subjectSelect").replace("\"", "");
        Logger.debug("subjectSelect: " + subjectSelect);
        if (StringUtils.isNotEmpty(subjectSelect)) {
            String[] subjects = subjectSelect.split(", ");
            for (String sId : subjects) {
//            	sId = sId.replace("\"", "");
            	Long subjectId = Long.valueOf(sId);
            	Subject subject = Subject.findById(subjectId);
            	newSubjects.add(subject);
            }
            filledForm.get().subjects = newSubjects;
        }
        
        List<Collection> newCollections = new ArrayList<Collection>();
        String collectionSelect = requestData.get("collectionSelect").replace("\"", "");
        Logger.debug("collectionSelect: " + collectionSelect);
        if (StringUtils.isNotEmpty(collectionSelect)) {
            String[] collections = collectionSelect.split(", ");
            for (String cId : collections) {
//            	sId = sId.replace("\"", "");
            	Long collectionId = Long.valueOf(cId);
            	Collection collection = Collection.findById(collectionId);
            	newCollections.add(collection);
            }
            filledForm.get().collections = newCollections;
        }
        
//	            String organisationId = requestData.get("organisationId");
//	            if (StringUtils.isNotEmpty(organisationId) || !organisationId.equals("-1")) {
//	            	Long oId = Long.valueOf(organisationId);
//	            	Organisation organisation = Organisation.findById(oId);
//	            	filledForm.get().organisation = organisation;
//	            }
//	            
//	            String authorId = requestData.get("authorId");
//	            if (StringUtils.isNotEmpty(authorId)) {
//	            	Long aId = Long.valueOf(authorId);
//	            	User author = User.findById(aId);
//	            	filledForm.get().authorUser = author;
//	            }
//	
        
        String dateOfPublication = requestData.get("dateOfPublicationText");
    	if (StringUtils.isNotEmpty(dateOfPublication)) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date date = formatter.parse(dateOfPublication);
				filledForm.get().dateOfPublication = date;
			} catch (ParseException e) {
				e.printStackTrace();
	            return newInfo(filledForm);
			}
    	}
        
        List<License> newLicenses = new ArrayList<License>();
        String[] licenseValues = formParams.get("licensesList");

        if (licenseValues != null) {
            for(String licenseValue : licenseValues) {
            	if (StringUtils.isNotBlank(licenseValue)) {
	            	Long licenseId = Long.valueOf(licenseValue);
	            	License license =  License.findById(licenseId);
	            	if (license != null) {
	            		newLicenses.add(license);
	            	}
            	}
            }
            filledForm.get().licenses = newLicenses;
        }
        
    	filledForm.get().url = "act-" + Utils.INSTANCE.createId();
    	filledForm.get().active = Boolean.TRUE;
    	
    	Logger.debug("active: " + filledForm.get().active);
    	
        try {
	        filledForm.get().isUkHosting = filledForm.get().isUkHosting();
			filledForm.get().isTopLevelDomain = filledForm.get().isTopLevelDomain();
	        filledForm.get().isUkRegistration = filledForm.get().isUkRegistration();
			Logger.debug("isUkHosting: " + filledForm.get().isUkHosting);
			Logger.debug("isTopLevelDomain: " + filledForm.get().isTopLevelDomain);
			Logger.debug("isUkRegistration: " + filledForm.get().isUkRegistration);
		} catch (MalformedURLException | WhoisException | URISyntaxException e) {
			throw new ActException(e);
		}
        
    	filledForm.get().save();
        flash("success", "Target " + filledForm.get().title + " has been created");
    	return redirect(routes.TargetController.view(filledForm.get().id));
    }
	
    /**
     * This method pushes a message onto a RabbitMQ queue for given target
     * using global settings from project configuration file.
     * @param target The field URL of the target
     * @return
     */
    public static Result archive(Long id) {

    	Target target = Target.findById(id);
    	Logger.debug("archiveTarget() " + target);
    	if (target != null) {
    		try {
//    	        String target = Scope.INSTANCE.normalizeUrl(url);
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
		    	String message = target.fieldUrl();
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
//    public static Result isInScope(String url) throws WhoisException {
////    	Logger.debug("isInScope controller: " + url);
//    	boolean res = Target.isInScope(url, null);
////    	Logger.debug("isInScope res: " + res);
//    	return ok(Json.toJson(res));
//    }
    
    /**
     * This method calculates collection children - objects that have parents.
     * @param url The identifier for parent 
     * @param targetUrl This is an identifier for current target object
     * @return child collection in JSON form
     */
    public static String getChildren(String url, String targetUrl) {
//    	Logger.debug("getChildren() target URL: " + targetUrl);
    	String res = "";
        final StringBuffer sb = new StringBuffer();
    	sb.append(", \"children\":");
    	List<Collection> childSuggestedCollections = Collection.getChildLevelCollections(url);
    	if (childSuggestedCollections.size() > 0) {
	    	sb.append(getTreeElements(childSuggestedCollections, targetUrl, false));
	    	res = sb.toString();
//	    	Logger.debug("getChildren() res: " + res);
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
    		if (target.collections != null && 
    				target.collections.contains(collectionUrl)) {
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
   	 * This method calculates first order collections for filtering.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getTreeElementsFilter(List<Collection> collectionList, String targetUrl, boolean parent) { 
//    	Logger.debug("getTreeElements() target URL: " + targetUrl);
    	String res = "";
    	if (collectionList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	    	Iterator<Collection> itr = collectionList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		Collection collection = itr.next();
//    			Logger.debug("add collection: " + collection.name + ", with url: " + collection.url +
//    					", parent:" + collection.parent + ", parent size: " + collection.parent.length());
	    		if ((parent && collection.parent == null) || !parent || collection.parent == null) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.name + "\"," + checkSelectionFilter(collection.url, targetUrl) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, targetUrl) + 
							"}");
	    		}
	    	}
//	    	Logger.debug("collectionList level size: " + collectionList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.debug("getTreeElements() res: " + res);
    	}
    	return res;
    }
    
//    /**
//   	 * This method calculates first order collections.
//     * @param collectionList The list of all collections
//     * @param filter This is an identifier for current selected object
//     * @param parent This parameter is used to differentiate between root and children nodes
//     * @return collection object in JSON form
//     */
//    protected static List<ObjectNode> getCollectionTreeElements(List<Collection> collectionList, String filter, boolean parent) {
//		List<ObjectNode> result = new ArrayList<ObjectNode>();
//		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
//
//    	for (Collection collection : collectionList) {
//			ObjectNode child = nodeFactory.objectNode();
//			child.put("title", collection.name);
//			if (StringUtils.isNotEmpty(collection.url) && collection.url.equalsIgnoreCase(filter)) {
//	    		child.put("select", true);
//	    	}
//			child.put("key", "\"" + collection.url + "\"");
//	    	List<Collection> children = Collection.findChildrenByParentId(collection.id);
//	    	Logger.debug("collection: " + collection.name + " - " + collection.children.size());
////    	    	Logger.debug("children: " + children.size());
//	    	if (children.size() > 0) {
//	    		child.put("children", Json.toJson(getCollectionTreeElements(children, filter, false)));
//	    	}
//			result.add(child);
//    	}
////        	Logger.debug("getTreeElements() res: " + result);
//    	return result;
//    }
    
//    [{"title":"100 Best Sites (95)","url":"/actdev/collections/act-170","key":"\"act-170\""},
//     {"title":"19th Century English Literature (0)","url":"/actdev/collections/act-153","key":"\"act-153\""},
//     {"title":"Commonwealth Games Glasgow 2014 (625)","url":"/actdev/collections/act-252","key":"\"act-252\"","children":[
//        {"title":"Competitors (350)","url":"/actdev/collections/act-266","key":"\"act-266\""},{"title":"Cultural Programme (44)","url":"/actdev/collections/act-295","key":"\"act-295\""},{"title":"Organisational bodies/venues (57)","url":"/actdev/collections/act-267","key":"\"act-267\""},{"title":"Press & Media Comment (41)","url":"/actdev/collections/act-269","key":"\"act-269\""},{"title":"Sponsors (15)","url":"/actdev/collections/act-270","key":"\"act-270\""},{"title":"Sports (405)","url":"/actdev/collections/act-268","key":"\"act-268\"","children":[{"title":"Aquatics (74)","url":"/actdev/collections/act-287","key":"\"act-287\""},{"title":"Athletics (107)","url":"/actdev/collections/act-286","key":"\"act-286\""},{"title":"Badminton (18)","url":"/actdev/collections/act-285","key":"\"act-285\""},{"title":"Boxing (26)","url":"/actdev/collections/act-284","key":"\"act-284\""},{"title":"Cycling (34)","url":"/actdev/collections/act-283","key":"\"act-283\""},{"title":"Gymnastics (29)","url":"/actdev/collections/act-282","key":"\"act-282\""},{"title":"Hockey (17)","url":"/actdev/collections/act-281","key":"\"act-281\""},{"title":"Judo (9)","url":"/actdev/collections/act-280","key":"\"act-280\""},{"title":"Lawn bowls (11)","url":"/actdev/collections/act-279","key":"\"act-279\""},{"title":"Netball (6)","url":"/actdev/collections/act-278","key":"\"act-278\""},{"title":"Rugby Sevens (16)","url":"/actdev/collections/act-277","key":"\"act-277\""},{"title":"Shooting (14)","url":"/actdev/collections/act-276","key":"\"act-276\""},{"title":"Squash (16)","url":"/actdev/collections/act-275","key":"\"act-275\""},{"title":"Table tennis (6)","url":"/actdev/collections/act-274","key":"\"act-274\""},{"title":"Triathlon (7)","url":"/actdev/collections/act-273","key":"\"act-273\""},{"title":"Weightlifting (7)","url":"/actdev/collections/act-272","key":"\"act-272\""},{"title":"Wrestling (7)","url":"/actdev/collections/act-271","key":"\"act-271\""}]}]},{"title":"Conservative Party Website deletions - Press articles November 2013 (10)","url":"/actdev/collections/act-175","key":"\"act-175\""},{"title":"Ebola (192)","url":"/actdev/collections/act-296","key":"\"act-296\""},{"title":"European Parliament Elections 2014 (1743)","url":"/actdev/collections/act-250","key":"\"act-250\"","children":[{"title":"Academia & think tanks (15)","url":"/actdev/collections/act-257","key":"\"act-257\""},{"title":"Blogs (215)","url":"/actdev/collections/act-263","key":"\"act-263\""},{"title":"Candidates (520)","url":"/actdev/collections/act-262","key":"\"act-262\""},{"title":"EU Institutions (1)","url":"/actdev/collections/act-260","key":"\"act-260\""},{"title":"Interest groups (63)","url":"/actdev/collections/act-254","key":"\"act-254\""},{"title":"Opinion Polls (7)","url":"/actdev/collections/act-261","key":"\"act-261\""},{"title":"Political Parties: National (99)","url":"/actdev/collections/act-256","key":"\"act-256\""},{"title":"Political Parties: Regional & Local (65)","url":"/actdev/collections/act-258","key":"\"act-258\""},{"title":"Press & Media Comment (744)","url":"/actdev/collections/act-255","key":"\"act-255\""},{"title":"Regulation and Guidance (13)","url":"/actdev/collections/act-253","key":"\"act-253\""},{"title":"Social Media (1)","url":"/actdev/collections/act-259","key":"\"act-259\""}]},{"title":"Evolving role of libraries in the UK (0)","url":"/actdev/collections/act-154","key":"\"act-154\""},{"title":"First World War Centenary, 2014-18 (208)","url":"/actdev/collections/act-174","key":"\"act-174\"","children":[{"title":"Heritage Lottery Fund (66)","url":"/actdev/collections/act-265","key":"\"act-265\""}]},

 //    	 {"title":"Health and Social Care Act 2012 - NHS Reforms (752)","url":"/actdev/collections/act-24","key":"\"act-24\"","children":[
    
//    	     {"title":"NHS (720)","url":"/actdev/collections/act-25","key":"\"act-25\"","children":[
//    	         {"title":"Acute Trusts (161)","url":"/actdev/collections/act-27","key":"\"act-27\""},{"title":"Ambulance Trusts (12)","url":"/actdev/collections/act-136","key":"\"act-136\""},{"title":"Campaigning and Advocacy Groups (18)","url":"/actdev/collections/act-148","key":"\"act-148\""},{"title":"Cancer Networks (28)","url":"/actdev/collections/act-137","key":"\"act-137\""},{"title":"Care Trust (29)","url":"/actdev/collections/act-139","key":"\"act-139\""},{"title":"Clinical Commissioning Groups (191)","url":"/actdev/collections/act-28","key":"\"act-28\""},{"title":"Gateways (11)","url":"/actdev/collections/act-141","key":"\"act-141\""},{"title":"Health and Wellbeing Boards (108)","url":"/actdev/collections/act-29","key":"\"act-29\""},{"title":"Healthwatch (127)","url":"/actdev/collections/act-144","key":"\"act-144\""},{"title":"Legislation (17)","url":"/actdev/collections/act-143","key":"\"act-143\""},{"title":"Local Authorities (142)","url":"/actdev/collections/act-166","key":"\"act-166\""},{"title":"Local Involvement Networks (LINks) (159)","url":"/actdev/collections/act-30","key":"\"act-30\""},{"title":"Mental Health Trusts (50)","url":"/actdev/collections/act-138","key":"\"act-138\""},{"title":"NHS programmes (3)","url":"/actdev/collections/act-142","key":"\"act-142\""},{"title":"Press Comment (248)","url":"/actdev/collections/act-150","key":"\"act-150\""},{"title":"Primary Care Trusts (15)","url":"/actdev/collections/act-26","key":"\"act-26\""},{"title":"Private and voluntary sector providers (8)","url":"/actdev/collections/act-169","key":"\"act-169\""},{"title":"Professional Bodies Trade Union (49)","url":"/actdev/collections/act-147","key":"\"act-147\""},{"title":"Public Health Agencies (1)","url":"/actdev/collections/act-134","key":"\"act-134\""},{"title":"Public Health England (138)","url":"/actdev/collections/act-149","key":"\"act-149\""},{"title":"Regulators & Central Government (1)","url":"/actdev/collections/act-135","key":"\"act-135\""},{"title":"Social Media (Facebook, Twitter etc) (42)","url":"/actdev/collections/act-167","key":"\"act-167\""},{"title":"Special Health Authorities (13)","url":"/actdev/collections/act-140","key":"\"act-140\""},{"title":"Specialised Commissioning Group (12)","url":"/actdev/collections/act-145","key":"\"act-145\""},{"title":"Strategic Health Authorities (20)","url":"/actdev/collections/act-82","key":"\"act-82\"","children":[{"title":"London SHA Cluster (1)","url":"/actdev/collections/act-83","key":"\"act-83\"","children":[{"title":"London SHA (1)","url":"/actdev/collections/act-87","key":"\"act-87\"","children":[{"title":"London (1)","url":"/actdev/collections/act-31","key":"\"act-31\""},{"title":"North Central London (6)","url":"/actdev/collections/act-33","key":"\"act-33\""},{"title":"North East London and City (9)","url":"/actdev/collections/act-32","key":"\"act-32\""},{"title":"North West London (8)","url":"/actdev/collections/act-34","key":"\"act-34\""},{"title":"South East London (6)","url":"/actdev/collections/act-36","key":"\"act-36\""},{"title":"South West London (6)","url":"/actdev/collections/act-35","key":"\"act-35\""}]}]},{"title":"Midlands and East SHA Cluster (4)","url":"/actdev/collections/act-85","key":"\"act-85\"","children":[{"title":"East Midlands (1)","url":"/actdev/collections/act-91","key":"\"act-91\"","children":[{"title":"Derby City and Derbyshire (2)","url":"/actdev/collections/act-53","key":"\"act-53\""},{"title":"East Midlands (2)","url":"/actdev/collections/act-52","key":"\"act-52\""},{"title":"Leicestershire County & Rutland and Leicestershire City (2)","url":"/actdev/collections/act-54","key":"\"act-54\""},{"title":"Lincolnshire (1)","url":"/actdev/collections/act-55","key":"\"act-55\""},{"title":"Milton Keynes and Northamptonshire (2)","url":"/actdev/collections/act-56","key":"\"act-56\""},{"title":"Nottinghamshhire County and Nottingham City (2)","url":"/actdev/collections/act-57","key":"\"act-57\""}]},{"title":"East of England (1)","url":"/actdev/collections/act-92","key":"\"act-92\"","children":[{"title":"Bedfordshire and Luton (1)","url":"/actdev/collections/act-59","key":"\"act-59\""},{"title":"Cambridgeshire and Peterborough (3)","url":"/actdev/collections/act-62","key":"\"act-62\""},{"title":"Hertfordshire (1)","url":"/actdev/collections/act-58","key":"\"act-58\""},{"title":"Norfolk and Waveney (2)","url":"/actdev/collections/act-63","key":"\"act-63\""},{"title":"North, Mid and East Essex (1)","url":"/actdev/collections/act-60","key":"\"act-60\""},{"title":"South Essex (2)","url":"/actdev/collections/act-61","key":"\"act-61\""},{"title":"Suffolk (1)","url":"/actdev/collections/act-64","key":"\"act-64\""}]},{"title":"West Midlands (1)","url":"/actdev/collections/act-93","key":"\"act-93\"","children":[{"title":"Arden (2)","url":"/actdev/collections/act-65","key":"\"act-65\""},{"title":"Birmingham and Solihull (5)","url":"/actdev/collections/act-66","key":"\"act-66\""},{"title":"Black Country (4)","url":"/actdev/collections/act-67","key":"\"act-67\""},{"title":"Staffordshire (3)","url":"/actdev/collections/act-69","key":"\"act-69\""},{"title":"West Mercia (5)","url":"/actdev/collections/act-70","key":"\"act-70\""},{"title":"West Midlands (1)","url":"/actdev/collections/act-68","key":"\"act-68\""}]}]},{"title":"North of England SHA Cluster (7)","url":"/actdev/collections/act-84","key":"\"act-84\"","children":[{"title":"North East (3)","url":"/actdev/collections/act-88","key":"\"act-88\"","children":[{"title":"County Durham and Darlington (3)","url":"/actdev/collections/act-37","key":"\"act-37\""},{"title":"North of Tyne (5)","url":"/actdev/collections/act-38","key":"\"act-38\""},{"title":"South of Tyne (4)","url":"/actdev/collections/act-39","key":"\"act-39\""},{"title":"Tees (5)","url":"/actdev/collections/act-40","key":"\"act-40\""}]},{"title":"North West (1)","url":"/actdev/collections/act-89","key":"\"act-89\"","children":[{"title":"Cheshire, Warrington, Wirral (4)","url":"/actdev/collections/act-41","key":"\"act-41\""},{"title":"Cumbria (1)","url":"/actdev/collections/act-42","key":"\"act-42\""},{"title":"Greater Manchester (9)","url":"/actdev/collections/act-43","key":"\"act-43\""},{"title":"Lancashire (5)","url":"/actdev/collections/act-44","key":"\"act-44\""},{"title":"Merseyside (3)","url":"/actdev/collections/act-45","key":"\"act-45\""}]},{"title":"Yorkshire and the Humber (3)","url":"/actdev/collections/act-90","key":"\"act-90\"","children":[{"title":"Bradford (2)","url":"/actdev/collections/act-49","key":"\"act-49\""},{"title":"Calderdale, Kirklees and Wakefield (3)","url":"/actdev/collections/act-46","key":"\"act-46\""},{"title":"Humber (5)","url":"/actdev/collections/act-47","key":"\"act-47\""},{"title":"Leeds (1)","url":"/actdev/collections/act-50","key":"\"act-50\""},{"title":"North Yorkshire and York (1)","url":"/actdev/collections/act-51","key":"\"act-51\""},{"title":"South Yorkshire and Bassetlaw (5)","url":"/actdev/collections/act-48","key":"\"act-48\""}]}]},{"title":"South of England SHA Cluster (1)","url":"/actdev/collections/act-86","key":"\"act-86\"","children":[{"title":"South Central (0)","url":"/actdev/collections/act-94","key":"\"act-94\"","children":[{"title":"Berkshire (6)","url":"/actdev/collections/act-71","key":"\"act-71\""},{"title":"Buckinghamshire and Oxfordshire (2)","url":"/actdev/collections/act-73","key":"\"act-73\""},{"title":"Southampton, Hampshire, Isle of Wight & Portsmouth (5)","url":"/actdev/collections/act-72","key":"\"act-72\""}]},{"title":"South East Coast (1)","url":"/actdev/collections/act-95","key":"\"act-95\"","children":[{"title":"Kent and Medway (4)","url":"/actdev/collections/act-74","key":"\"act-74\""},{"title":"Surrey (1)","url":"/actdev/collections/act-75","key":"\"act-75\""},{"title":"Sussex (5)","url":"/actdev/collections/act-76","key":"\"act-76\""}]},{"title":"South West (0)","url":"/actdev/collections/act-96","key":"\"act-96\"","children":[{"title":"Bath, North East Somerset and Wiltshire (2)","url":"/actdev/collections/act-77","key":"\"act-77\""},{"title":"Bournemouth, Poole and Dorset (2)","url":"/actdev/collections/act-78","key":"\"act-78\""},{"title":"Bristol, North Somerset and South Gloucestershire (4)","url":"/actdev/collections/act-79","key":"\"act-79\""},{"title":"Devon, Plymouth and Torbay (3)","url":"/actdev/collections/act-80","key":"\"act-80\""},{"title":"Gloucestershire and Swindon (3)","url":"/actdev/collections/act-81","key":"\"act-81\""}]}]}]},{"title":"Think Tanks (30)","url":"/actdev/collections/act-146","key":"\"act-146\""}]}]},{"title":"History of Libraries Collection (0)","url":"/actdev/collections/act-155","key":"\"act-155\""},{"title":"History of the Book (0)","url":"/actdev/collections/act-156","key":"\"act-156\""},{"title":"Legal Aid Reform (0)","url":"/actdev/collections/act-157","key":"\"act-157\""},{"title":"Margaret Thatcher (77)","url":"/actdev/collections/act-151","key":"\"act-151\""},{"title":"Nelson Mandela (174)","url":"/actdev/collections/act-178","key":"\"act-178\""},{"title":"News Sites (576)","url":"/actdev/collections/act-173","key":"\"act-173\"","children":[{"title":"Hyperlocal (515)","url":"/actdev/collections/act-297","key":"\"act-297\""}]},{"title":"Oral History in the UK (2)","url":"/actdev/collections/act-158","key":"\"act-158\""},{"title":"Political Action and Communication (5)","url":"/actdev/collections/act-159","key":"\"act-159\""},{"title":"Religion, politics and law since 2005 (9)","url":"/actdev/collections/act-160","key":"\"act-160\""},{"title":"Religion/Theology (237)","url":"/actdev/collections/act-172","key":"\"act-172\""},{"title":"Scottish Independence Referendum (1144)","url":"/actdev/collections/act-171","key":"\"act-171\"","children":[{"title":"Charities, Churches and Third Sector (16)","url":"/actdev/collections/act-293","key":"\"act-293\""},{"title":"Commercial Publishers (1)","url":"/actdev/collections/act-294","key":"\"act-294\""},{"title":"Government (UK and Scottish) (83)","url":"/actdev/collections/act-291","key":"\"act-291\""},{"title":"National Campaigning Groups (80)","url":"/actdev/collections/act-288","key":"\"act-288\""},{"title":"Political Parties and Trade Unions (249)","url":"/actdev/collections/act-289","key":"\"act-289\""},{"title":"Press, Media & Comment (263)","url":"/actdev/collections/act-292","key":"\"act-292\""},{"title":"Think Tanks and Research Institutes (47)","url":"/actdev/collections/act-290","key":"\"act-290\""}]},{"title":"Slavery and Abolition in the Caribbean (0)","url":"/actdev/collections/act-161","key":"\"act-161\""},{"title":"Tour de France (Yorkshire) 2014 (60)","url":"/actdev/collections/act-264","key":"\"act-264\""},{"title":"UK relations with the Low Countries (0)","url":"/actdev/collections/act-162","key":"\"act-162\""},{"title":"UK response to Philippines disaster 2013 (501)","url":"/actdev/collections/act-176","key":"\"act-176\""},{"title":"Video Games (0)","url":"/actdev/collections/act-163","key":"\"act-163\""},{"title":"Winter Olympics Sochi 2014 (128)","url":"/actdev/collections/act-177","key":"\"act-177\""}]    

    
    
    //    [{"title":"Health and Social Care Act 2012 - NHS Reforms","select":true,"key":"act-24","children":[
    
    
    
//       {"title":"NHS","key":"act-25","children":[
//          {"title":"Campaigning and Advocacy Groups","key":"act-148"},{"title":"Acute Trusts","key":"act-27"},{"title":"Clinical Commissioning Groups","key":"act-28"},{"title":"Strategic Health Authorities","key":"act-82","children":[
//              {"title":"London SHA Cluster","key":"act-83","children":[{"title":"London SHA","key":"act-87","children":[
//                   {"title":"North West London","key":"act-34"},{"title":"London","key":"act-31"},{"title":"North East London and City","key":"act-32"},{"title":"North Central London","key":"act-33"},{"title":"South West London","key":"act-35"},{"title":"South East London","key":"act-36"}]}]},{"title":"Midlands and East SHA Cluster","key":"act-85","children":[{"title":"East Midlands","key":"act-91","children":[{"title":"Nottinghamshhire County and Nottingham City","key":"act-57"},{"title":"Derby City and Derbyshire","key":"act-53"},{"title":"Leicestershire County & Rutland and Leicestershire City","key":"act-54"},{"title":"Lincolnshire","key":"act-55"},{"title":"Milton Keynes and Northamptonshire","key":"act-56"}]},{"title":"West Midlands","key":"act-93","children":[{"title":"Black Country","key":"act-67"},{"title":"Arden","key":"act-65"},{"title":"Birmingham and Solihull","key":"act-66"},{"title":"Staffordshire","key":"act-69"},{"title":"West Mercia","key":"act-70"}]},{"title":"East of England","key":"act-92","children":[{"title":"Hertfordshire","key":"act-58"},{"title":"Bedfordshire and Luton","key":"act-59"},{"title":"North,  Mid and East Essex","key":"act-60"},{"title":"South Essex","key":"act-61"},{"title":"Cambridgeshire and Peterborough","key":"act-62"},{"title":"Norfolk and Waveney","key":"act-63"},{"title":"Suffolk","key":"act-64"}]}]},{"title":"North of England SHA Cluster","key":"act-84","children":[{"title":"North West","key":"act-89","children":[{"title":"Greater Manchester","key":"act-43"},{"title":"Cheshire,  Warrington,  Wirral","key":"act-41"},{"title":"Cumbria","key":"act-42"},{"title":"Lancashire","key":"act-44"},{"title":"Merseyside","key":"act-45"}]},{"title":"North East","key":"act-88","children":[{"title":"County Durham and Darlington","key":"act-37"},{"title":"North of Tyne","key":"act-38"},{"title":"South of Tyne","key":"act-39"},{"title":"Tees","key":"act-40"}]},{"title":"Yorkshire and the Humber","key":"act-90","children":[{"title":"Calderdale,  Kirklees and Wakefield","key":"act-46"},{"title":"Humber","key":"act-47"},{"title":"South Yorkshire and Bassetlaw","key":"act-48"},{"title":"Bradford","key":"act-49"},{"title":"Leeds","key":"act-50"},{"title":"North Yorkshire and York","key":"act-51"}]}]},{"title":"South of England SHA Cluster","key":"act-86","children":[{"title":"South Central","key":"act-94","children":[{"title":"Berkshire","key":"act-71"},{"title":"Southampton,  Hampshire,  Isle of Wight & Portsmouth","key":"act-72"},{"title":"Buckinghamshire and Oxfordshire","key":"act-73"}]},{"title":"South East Coast","key":"act-95","children":[{"title":"Kent and Medway","key":"act-74"},{"title":"Surrey","key":"act-75"},{"title":"Sussex","key":"act-76"}]},{"title":"South West","key":"act-96","children":[{"title":"Bath,  North East Somerset and Wiltshire","key":"act-77"},{"title":"Bournemouth,  Poole and Dorset","key":"act-78"},{"title":"Bristol,  North Somerset and South Gloucestershire","key":"act-79"},{"title":"Devon,  Plymouth and Torbay","key":"act-80"},{"title":"Gloucestershire and Swindon","key":"act-81"}]}]}]},{"title":"Health and Wellbeing Boards","key":"act-29"},{"title":"Local Involvement Networks (LINks)","key":"act-30"},{"title":"Care Trust","key":"act-139"},{"title":"Public Health England","key":"act-149"},{"title":"Special Health Authorities","key":"act-140"},{"title":"Public Health Agencies","key":"act-134"},{"title":"Ambulance Trusts","key":"act-136"},{"title":"Mental Health Trusts","key":"act-138"},{"title":"Gateways","key":"act-141"},{"title":"NHS programmes","key":"act-142"},{"title":"Professional Bodies Trade Union","key":"act-147"},{"title":"Legislation","key":"act-143"},{"title":"Healthwatch","key":"act-144"},{"title":"Specialised Commissioning Group","key":"act-145"},{"title":"Think Tanks","key":"act-146"},{"title":"Press Comment","key":"act-150"},{"title":"Cancer Networks","key":"act-137"},{"title":"Regulators & Central Government","key":"act-135"},{"title":"Local Authorities","key":"act-166"},{"title":"Social Media (Facebook,  Twitter etc)","key":"act-167"},{"title":"Primary Care Trusts","key":"act-26"},{"title":"Private and voluntary sector providers","key":"act-169"}]}]},{"title":"Scottish Independence Referendum","key":"act-171","children":[{"title":"Press,  Media & Comment","key":"act-292"},{"title":"Political Parties and Trade Unions","key":"act-289"},{"title":"Think Tanks and Research Institutes","key":"act-290"},{"title":"National Campaigning Groups","key":"act-288"},{"title":"Government (UK and Scottish)","key":"act-291"},{"title":"Charities,  Churches and Third Sector","key":"act-293"}]},{"title":"Science,  Technology & Medicine","key":"act-99","children":[{"title":"Physics","key":"act-108"},{"title":"Astronomy","key":"act-107"}]},{"title":"Religion,  politics and law since 2005","key":"act-160"},{"title":"News Sites","key":"act-173"},{"title":"100 Best Sites","key":"act-170"},{"title":"Margaret Thatcher","key":"act-151"},{"title":"First World War Centenary,  2014-18","key":"act-174","children":[{"title":"Heritage Lottery Fund","key":"act-265"}]},{"title":"Religion/Theology","key":"act-172"},{"title":"European Parliament Elections 2014","key":"act-250","children":[{"title":"Political Parties: National","key":"act-256"},{"title":"Academia & think tanks","key":"act-257"},{"title":"Interest groups","key":"act-254"},{"title":"Regulation and Guidance","key":"act-253"},{"title":"Blogs","key":"act-263"},{"title":"Political Parties: Regional & Local","key":"act-258"},{"title":"Social Media","key":"act-259"},{"title":"EU Institutions","key":"act-260"},{"title":"Opinion Polls","key":"act-261"},{"title":"Candidates","key":"act-262"}]},{"title":"Nelson Mandela","key":"act-178"},{"title":"UK response to Philippines disaster 2013","key":"act-176"},{"title":"Commonwealth Games Glasgow 2014","key":"act-252","children":[{"title":"Organisational bodies/venues","key":"act-267"},{"title":"Sports","key":"act-268","children":[{"title":"Rugby Sevens","key":"act-277"},{"title":"Badminton","key":"act-285"},{"title":"Cycling","key":"act-283"},{"title":"Wrestling","key":"act-271"},{"title":"Hockey","key":"act-281"},{"title":"Boxing","key":"act-284"},{"title":"Aquatics","key":"act-287"},{"title":"Athletics","key":"act-286"},{"title":"Gymnastics","key":"act-282"},{"title":"Judo","key":"act-280"},{"title":"Lawn bowls","key":"act-279"},{"title":"Netball","key":"act-278"},{"title":"Shooting","key":"act-276"},{"title":"Squash","key":"act-275"},{"title":"Table tennis","key":"act-274"},{"title":"Triathlon","key":"act-273"},{"title":"Weightlifting","key":"act-272"}]},{"title":"Press & Media Comment","key":"act-269"},{"title":"Sponsors","key":"act-270"},{"title":"Competitors","key":"act-266"},{"title":"Cultural Programme","key":"act-295"}]},{"title":"Winter Olympics Sochi 2014","key":"act-177"},{"title":"Oral History in the UK","key":"act-158"},{"title":"Conservative Party Website deletions - Press articles November 2013","key":"act-175"},{"title":"Political Action and Communication","key":"act-159"},{"title":"Tour de France (Yorkshire) 2014","key":"act-264"}]

    
    /**
     * This method computes a tree of collections in JSON format. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollections(String targetUrl) {
//    	Logger.debug("getSuggestedCollections() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Collection> suggestedCollections = Collection.getFirstLevelCollections();
    	sb.append(getTreeElements(suggestedCollections, targetUrl, true));
//    	Logger.debug("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.debug("getCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }        
    
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param targetUrl This is an identifier for current target object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getTreeElements(List<Collection> collectionList, String targetUrl, boolean parent) { 
//    	Logger.debug("getTreeElements() target URL: " + targetUrl);
    	String res = "";
    	if (collectionList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	    	Iterator<Collection> itr = collectionList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		Collection collection = itr.next();
//    			Logger.debug("add collection: " + collection.name + ", with url: " + collection.url +
//    					", parent:" + collection.parent + ", parent size: " + collection.parent.length());
	    		if ((parent && collection.parent == null) || !parent || collection.parent == null) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.name + "\"," + checkSelection(collection.url, targetUrl) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, targetUrl) + 
							"}");
	    		}
	    	}
//	    	Logger.debug("collectionList level size: " + collectionList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.debug("getTreeElements() res: " + res);
    	}
    	return res;
    }
    /**
     * This method computes a tree of collections in JSON format for filtering. 
     * @param targetUrl This is an identifier for current target object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getSuggestedCollectionsFilter(String targetUrl) {
//    	Logger.debug("getSuggestedCollectionsFilter() target URL: " + targetUrl);
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Collection> suggestedCollections = Collection.getFirstLevelCollections();
    	sb.append(getTreeElementsFilter(suggestedCollections, targetUrl, true));
//    	Logger.debug("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.debug("getCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }        
    
//    /**
//     * This method calculates subject children - objects that have parents.
//     * @param url The identifier for parent 
//     * @param targetUrl This is an identifier for current target object
//     * @return child subject in JSON form
//     */
//    public static String getSubjectChildren(String url, String targetUrl) {
////    	Logger.debug("getSubjectChildren() target URL: " + targetUrl);
//    	String res = "";
//        final StringBuffer sb = new StringBuffer();
//    	sb.append(", \"children\":");
////    	List<Taxonomy> childSubject = Taxonomy.findListByType(Const.SUBSUBJECT);
//    	Taxonomy subject = Taxonomy.findByUrl(url);
//    	List<Taxonomy> childSubject = Taxonomy.findSubSubjectsList(subject.name);
//    	if (childSubject.size() > 0) {
//	    	sb.append(getSubjectTreeElements(childSubject, targetUrl, false));
//	    	res = sb.toString();
////	    	Logger.debug("getSubjectChildren() res: " + res);
//    	}
//    	return res;
//    }
    
//    /**
//     * Mark subjects that are stored in target object as selected
//     * @param subjectUrl The subject identifier
//     * @param targetUrl This is an identifier for current target object
//     * @return
//     */
//    public static String checkSubjectSelection(String subjectUrl, String targetUrl) {
////    	String res = "";
////    	if (targetUrl != null && targetUrl.length() > 0) {
//////    		if (subjectUrl != null && targetUrl.equals(subjectUrl)) {
////    		Target target = Target.findByUrl(targetUrl);
////    		if (target.fieldSubject != null && 
////    				target.fieldSubject.contains(subjectUrl)) {
////    			res = "\"select\": true ,";
////    		}
////    	}
////    	return res;
//		throw new NotImplementedError();
//    }
//    
//    /**
//     * Mark preselected subjects as selected in filter
//     * @param subjectUrl The subject identifier
//     * @param targetUrl This is an identifier for current target object
//     * @return
//     */
//    public static String checkSubjectSelectionFilter(String subjectUrl, String targetUrl) {
//    	String res = "";
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		if (subjectUrl != null && targetUrl.equals(subjectUrl)) {
//    			res = "\"select\": true ,";
//    		}
//    	}
//    	return res;
//    }
//    
//    /**
//     * Check if none value is selected in filter
//     * @param subjectUrl The subject identifier
//     * @param targetUrl This is an identifier for current target object
//     * @return
//     */
//    public static String checkNoneFilter(String targetUrl) {
//    	String res = "";
//    	if (targetUrl != null && targetUrl.length() > 0) {
//    		if (targetUrl.toLowerCase().contains(Const.NONE.toLowerCase())) {
//    			res = "\"select\": true ,";
//    		}
//    	}
//    	return res;
//    }
//    
//    /**
//     * Check if none value is selected
//     * @param subjectUrl The subject identifier
//     * @param targetUrl This is an identifier for current target object
//     * @return
//     */
//    public static String checkNone(String targetUrl) {
////    	String res = "";
////    	if (targetUrl != null && targetUrl.length() > 0) {
////    		Target target = Target.findByUrl(targetUrl);
////    		if (target.fieldSubject != null 
////    				&& (target.fieldSubject.toLowerCase().contains(Const.NONE.toLowerCase()))) {
////    			res = "\"select\": true ,";
////    		}
////    	}
////    	return res;
//		throw new NotImplementedError();
//    }
    
//    /**
//   	 * This method calculates first order subjects.
//     * @param subjectList The list of all subjects
//     * @param targetUrl This is an identifier for current target object
//     * @param parent This parameter is used to differentiate between root and children nodes
//     * @return collection object in JSON form
//     */
//    public static String getSubjectTreeElements(List<Taxonomy> subjectList, String targetUrl, boolean parent) { 
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
//							getSubjectChildren(subject.url, targetUrl) + 
//							"}");
//	    		}
//	    	}
////	    	Logger.debug("subjectList level size: " + subjectList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.debug("getSubjectTreeElements() res: " + res);
//    	}
//    	return res;
//    }
        
//    /**
//   	 * This method calculates first order subjects for targets filtering.
//     * @param subjectList The list of all subjects
//     * @param targetUrl This is an identifier for current target object
//     * @param parent This parameter is used to differentiate between root and children nodes
//     * @return collection object in JSON form
//     */
//    public static String getSubjectTreeElementsFilter(List<Taxonomy> subjectList, String targetUrl, boolean parent) { 
////    	Logger.debug("getSubjectTreeElements() target URL: " + targetUrl);
//    	String res = "";
//    	if (subjectList.size() > 0) {
//	        final StringBuffer sb = new StringBuffer();
//	        sb.append("[");
//	        if (parent) {
//	        	sb.append("{\"title\": \"" + "None" + "\"," + checkNoneFilter(targetUrl) + 
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
//					sb.append("{\"title\": \"" + subject.name + "\"," + checkSubjectSelectionFilter(subject.url, targetUrl) + 
//							" \"key\": \"" + subject.url + "\"" + 
//							getSubjectChildren(subject.url, targetUrl) + 
//							"}");
//	    		}
//	    	}
////	    	Logger.debug("subjectList level size: " + subjectList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.debug("getSubjectTreeElements() res: " + res);
//    	}
//    	return res;
//    }
        
//    /**
//     * This method computes a tree of subjects in JSON format. 
//     * @param targetUrl This is an identifier for current target object
//     * @return tree structure
//     */
//    @BodyParser.Of(BodyParser.Json.class)
//    public static Result getSubjectTree(String targetUrl) {
//    	Logger.debug("getSubjectTree() target URL: " + targetUrl);
//        JsonNode jsonData = null;
//        final StringBuffer sb = new StringBuffer();
//    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
////    	Logger.debug("getSubjectTree() parentSubjects: " + parentSubjects.size());
//    	sb.append(getSubjectTreeElements(parentSubjects, targetUrl, true));
//    	Logger.debug("subjects main level size: " + parentSubjects.size());
////    	Logger.debug("sb.toString(): " + sb.toString());
//        jsonData = Json.toJson(Json.parse(sb.toString()));
////    	Logger.debug("getSubjectTree() json: " + jsonData.toString());
//        return ok(jsonData);
//    }        
    
//    /**
//     * This method computes a tree of subjects in JSON format. 
//     * @param targetUrl This is an identifier for current target object
//     * @return tree structure
//     */
//    @BodyParser.Of(BodyParser.Json.class)
//    public static Result getSubjectTreeFilter(String targetUrl) {
//    	Logger.debug("getSubjectTree() target URL: " + targetUrl);
//        JsonNode jsonData = null;
//        final StringBuffer sb = new StringBuffer();
//    	List<Taxonomy> parentSubjects = Taxonomy.findListByTypeSorted(Const.SUBJECT);
////    	Logger.debug("getSubjectTree() parentSubjects: " + parentSubjects.size());
//    	sb.append(getSubjectTreeElementsFilter(parentSubjects, targetUrl, true));
//    	Logger.debug("subjects main level size: " + parentSubjects.size());
////    	Logger.debug("sb.toString(): " + sb.toString());
//        jsonData = Json.toJson(Json.parse(sb.toString()));
////    	Logger.debug("getSubjectTree() json: " + jsonData.toString());
//        return ok(jsonData);
//    }
    
//	/**
//	 * This method filters targets by given URLs.
//	 * @return duplicate count
//	 */
//	public static List<Target> getSubjects() {
//		List<Target> res = new ArrayList<Target>();
////		List<String> subjects = new ArrayList<String>();
//		List<Target> allTargets = Target.find.all();
//		Iterator<Target> itr = allTargets.iterator();
////		while (itr.hasNext()) {
////			Target target = itr.next();
////			if (target.fieldSubject != null && target.fieldSubject.length() > 0 && !res.contains(target)) {
//////				if (target.field_subject != null && target.field_subject.length() > 0 && !subjects.contains(target.field_subject)) {
////		        ExpressionList<Target> ll = Target.find.where().contains("field_subject", target.fieldSubject);
////		        if (ll.findRowCount() > 0) {
//////		        	subjects.add(target.field_subject);
////		        	res.add(target);
////		        }
////			}
////		}
////   	return res;
//		throw new NotImplementedError();
//	}
	
//	/**
//	 * This method filters targets by given scope.
//	 * @return scope list
//	 */
//	public static List<Target> getScope() {
//		List<Target> res = new ArrayList<Target>();
//		List<String> subjects = new ArrayList<String>();
//		List<Target> allTargets = Target.find.all();
//		Iterator<Target> itr = allTargets.iterator();
//		while (itr.hasNext()) {
//			Target target = itr.next();
//			if (target.scope != null && target.scope.length() > 0 && !subjects.contains(target.scope)) {
//		        ExpressionList<Target> ll = Target.find.where().contains("field_scope", target.scope);
//		        if (ll.findRowCount() > 0) {
//		        	res.add(target);
//		        	subjects.add(target.scope);
//		        }
//			}
//		}
//   	return res;
//	}
	
//	/**
//	 * This method retrieves targets from database for given taxonomy URL.
//	 * @param url
//	 * @return
//	 */
//	public static List<Target> getTargetsForTaxonomy(String url) {
//		List<Target> res = new ArrayList<Target>();
////		Logger.debug("url: " + url);
//		if (url != null) {
//	        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, url);
//	        res = ll.findList();
//		}
////		Logger.debug("res size: " + res.size());
//		return res;
//	}
	
//	/**
//	 * This method filters targets by given license.
//	 * @return license list
//	 */
//	public static List<Taxonomy> getLicense() {
//		List<Taxonomy> licenses = new ArrayList<Taxonomy>();
//		List<String> subjects = new ArrayList<String>();
//		
//		List<Target> allTargets = Target.find.all();
//		
//		Iterator<Target> itr = allTargets.iterator();
//		while (itr.hasNext()) {
//			Target target = itr.next();
//			if (target.fieldLicense != null) {
//				String curLicense = target.fieldLicense.replace(Const.LIST_DELIMITER, "");
//				if (curLicense.length() > 0 && !subjects.contains(curLicense)) {
//			        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_LICENSE_NODE, curLicense);
//			        if (ll.findRowCount() > 0) {
//			        	Taxonomy taxonomy = Taxonomy.findByUrl(curLicense);
//			        	Logger.debug("curLicense: " + curLicense + ".");
//	//		        	Logger.debug("taxonomy url: " + taxonomy.url);
//	//		        	Logger.debug("license: " + taxonomy.name);
//			        	licenses.add(taxonomy);
//			        	subjects.add(curLicense);
//			        }
//				}
//			}
//		}
////		Logger.debug("getLicense res: " + res);
//   	return licenses;
////		throw new NotImplementedError();
//	}
	
//	/**
//	 * This method filters targets by crawl frequency.
//	 * @return crawl frequency list
//	 */
	public static List<Target> getCrawlFrequency() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.crawlFrequency != null && target.crawlFrequency.length() > 0 && !subjects.contains(target.crawlFrequency)) {
		        ExpressionList<Target> ll = Target.find.where().contains("crawlFrequency", target.crawlFrequency);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.crawlFrequency);
		        }
			}
		}
   	return res;
	}
	
//	/**
//	 * This method filters targets by depth.
//	 * @return depth list
//	 */
//	public static List<Target> getDepth() {
//		List<Target> res = new ArrayList<Target>();
//		List<String> subjects = new ArrayList<String>();
//		List<Target> allTargets = Target.find.all();
//		Iterator<Target> itr = allTargets.iterator();
//		while (itr.hasNext()) {
//			Target target = itr.next();
//			if (target.depth != null && target.depth.length() > 0 && !subjects.contains(target.depth)) {
//		        ExpressionList<Target> ll = Target.find.where().contains("depth", target.depth);
//		        if (ll.findRowCount() > 0) {
//		        	res.add(target);
//		        	subjects.add(target.depth);
//		        }
//			}
//		}
//   	return res;
//	}
	
//	/**
//	 * This method filters targets by collection categories.
//	 * @return collection categories list
//	 */
//	public static List<Taxonomy> getCollectionCategories() {
//		List<Target> res = new ArrayList<Target>();
//		List<String> subjects = new ArrayList<String>();
//		List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();
//		List<Target> allTargets = Target.find.all();
//		Iterator<Target> itr = allTargets.iterator();
//		while (itr.hasNext()) {
//			Target target = itr.next();
//			if (target.collections != null && !subjects.contains(target.fieldCollectionCategories)) {
//		        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, target.fieldCollectionCategories);
//		        if (ll.findRowCount() > 0) {
//		        	res.add(target);
//		        	subjects.add(target.fieldCollectionCategories);
//		        	Taxonomy taxonomy = Taxonomy.findByUrl(target.fieldCollectionCategories);
//		        	taxonomies.add(taxonomy);
//		        }
//			}
//		}
//   	return taxonomies;
//		throw new NotImplementedError();
//	}    
    
}


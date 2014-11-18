package controllers;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.Collection;
import models.Flag;
import models.Organisation;
import models.Target;
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
import uk.bl.api.Utils;
import views.html.collections.sites;
import views.html.targets.blank;
import views.html.targets.edit;
import views.html.targets.list;
import views.html.targets.targets;
import views.html.targets.view;
import views.html.users.usersites;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage targets.
 */
/**
 * @author kli
 *
 */
/**
 * @author kli
 *
 */
@Security.Authenticated(Secured.class)
public class Targets extends AbstractController {
  
    final static play.data.Form<Target> targetForm = play.data.Form.form(Target.class);

    /**
     * Display the targets.
     */
    public static Result index() {
        return GO_TARGETS_HOME;
    }

	/**
	 * This method filters targets by given URLs.
	 * @return duplicate count
	 */
	public static List<Target> getSubjects() {
		List<Target> res = new ArrayList<Target>();
//		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.fieldSubject != null && target.fieldSubject.length() > 0 && !res.contains(target)) {
//				if (target.field_subject != null && target.field_subject.length() > 0 && !subjects.contains(target.field_subject)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_subject", target.fieldSubject);
		        if (ll.findRowCount() > 0) {
//		        	subjects.add(target.field_subject);
		        	res.add(target);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by given scope.
	 * @return scope list
	 */
	public static List<Target> getScope() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_scope != null && target.field_scope.length() > 0 && !subjects.contains(target.field_scope)) {
		        ExpressionList<Target> ll = Target.find.where().contains("field_scope", target.field_scope);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_scope);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method retrieves targets from database for given taxonomy URL.
	 * @param url
	 * @return
	 */
	public static List<Target> getTargetsForTaxonomy(String url) {
		List<Target> res = new ArrayList<Target>();
//		Logger.info("url: " + url);
		if (url != null) {
	        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, url);
	        res = ll.findList();
		}
//		Logger.info("res size: " + res.size());
		return res;
	}
	
	/**
	 * This method filters targets by given license.
	 * @return license list
	 */
	public static List<Taxonomy> getLicense() {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.fieldLicense != null) {
				String curLicense = target.fieldLicense.replace(Const.LIST_DELIMITER, "");
				if (curLicense.length() > 0 && !subjects.contains(curLicense)) {
			        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_LICENSE_NODE, curLicense);
			        if (ll.findRowCount() > 0) {
			        	Taxonomy taxonomy = Taxonomy.findByUrl(curLicense);
			        	Logger.info("curLicense: " + curLicense + ".");
	//		        	Logger.info("taxonomy url: " + taxonomy.url);
	//		        	Logger.info("license: " + taxonomy.name);
			        	res.add(taxonomy);
			        	subjects.add(curLicense);
			        }
				}
			}
		}
//		Logger.info("getLicense res: " + res);
    	return res;
	}
	
	/**
	 * This method returns all possible licenses.
	 * @return license list
	 */
	public static List<Taxonomy> getLicenses() {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
		res = Taxonomy.findByType(Const.LICENCE);
    	return res;
	}
	
	/**
	 * This method filters targets by crawl frequency.
	 * @return crawl frequency list
	 */
	public static List<Target> getCrawlFrequency() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_crawl_frequency != null && target.field_crawl_frequency.length() > 0 && !subjects.contains(target.field_crawl_frequency)) {
		        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_CRAWL_FREQUENCY, target.field_crawl_frequency);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_crawl_frequency);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by depth.
	 * @return depth list
	 */
	public static List<Target> getDepth() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.field_depth != null && target.field_depth.length() > 0 && !subjects.contains(target.field_depth)) {
		        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_DEPTH, target.field_depth);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.field_depth);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters targets by collection categories.
	 * @return collection categories list
	 */
	public static List<Taxonomy> getCollectionCategories() {
		List<Target> res = new ArrayList<Target>();
		List<String> subjects = new ArrayList<String>();
		List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();
		List<Target> allTargets = Target.find.all();
		Iterator<Target> itr = allTargets.iterator();
		while (itr.hasNext()) {
			Target target = itr.next();
			if (target.fieldCollectionCategories != null && target.fieldCollectionCategories.length() > 0 && !subjects.contains(target.fieldCollectionCategories)) {
		        ExpressionList<Target> ll = Target.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, target.fieldCollectionCategories);
		        if (ll.findRowCount() > 0) {
		        	res.add(target);
		        	subjects.add(target.fieldCollectionCategories);
		        	Taxonomy taxonomy = Taxonomy.findByUrl(target.fieldCollectionCategories);
		        	taxonomies.add(taxonomy);
		        }
			}
		}
    	return taxonomies;
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
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Targets.list()");
        return ok(
        	list.render(
        			"Lookup", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Target.page(pageNo, 10, sortBy, order, filter), 
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
     * @param curator Author of the target
     * @param organisation The author's organisation
     * @param subject Target subject
     * @param crawlFrequency The crawl frequency
     * @param depth The crawl depth
     * @param collection The associated collection
     * @param license The license name
     * @param pageSize The number of Target entries on the page
     * @param flag The flag assigned by user
     */
    public static Result targets(int pageNo, String sortBy, String order, String filter, String curator,
    		String organisation, String subject, String crawlFrequency, String depth, String collection, 
    		String license, int pageSize, String flag) {
    	Logger.info("Pre Targets.targets() subject: " + subject);
    	
    	Page<Target> pageTargets = Target.pageTargets(pageNo, pageSize, sortBy, order, filter, curator, organisation, 
				subject, crawlFrequency, depth, collection, license, flag);
    	
    	Logger.info("Post Targets.targets() subject: " + subject);
    	
        return ok(
        	targets.render(
        			"Targets", 
        			User.findByEmail(request().username()), 
        			filter,
        			pageTargets,
        			sortBy, 
        			order, 
        	    	curator, 
        	    	organisation, 
        	    	subject, 
        	    	crawlFrequency, 
        	    	depth, 
        	    	collection, 
        	    	license, 
        	    	pageSize,
        	    	flag)
        		);
    }
	
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result searchTargets() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	Logger.debug("page size: " + getFormParam(Const.PAGE_SIZE));
        if (form.get(Const.PAGE_SIZE) == null 
        		|| (form.get(Const.PAGE_SIZE) != null 
        		   && !Utils.isNumeric(form.get(Const.PAGE_SIZE)))) {
            Logger.info("You may only enter a numeric page size.");
  			flash("message", "You may only enter a numeric page size.");
	        return GO_TARGETS_HOME;
    	}    	
    	
    	String action = form.get("action");
    	String query = form.get("url");

//    	if (StringUtils.isBlank(query)) {
//			Logger.info("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return GO_TARGETS_HOME;
//    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	int pageSize = Integer.parseInt(form.get(Const.PAGE_SIZE));
    	String curator_name = form.get(Const.AUTHOR);
    	String curator = "";
    	if (curator_name != null && !curator_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			curator = User.findByName(curator_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find curator for name: " + curator_name + ". " + e);
    		}
    	} 
    	String organisation_name = form.get(Const.FIELD_NOMINATING_ORGANISATION);
    	String organisation = "";
    	if (organisation_name != null && !organisation_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			organisation = Organisation.findByTitle(organisation_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find organisation for title: " + organisation_name + ". " + e);
    		}
    	} 
    	
//		String subject_name = Utils.removeDuplicatesFromList(form.get(Const.TREE_KEYS));
//    	String subject = "";
//		if (subject_name != null) {
//			if (!subject_name.toLowerCase().equals(Const.NONE)) {
//				subject = Taxonomy.findByUrlExt(subject_name).url;
//			} else {
//				subject = "";
//			}
//		}
		
    	String subject = Const.EMPTY;
        if (form.get(Const.SUBJECT) != null) {
        	String subjectListStr = Utils.removeDuplicatesFromList(form.get(Const.SUBJECT));
        	if (subjectListStr != null && subjectListStr.length() > 0
        			&& subjectListStr.toLowerCase().contains(Const.NONE)
        			&& subjectListStr.contains(Const.COMMA)) {
        		if (subjectListStr.contains(Const.NONE_VALUE + Const.COMMA + " ")) {
        			subjectListStr = subjectListStr.replace(Const.NONE_VALUE + Const.COMMA + " ", "");
        		}
        		if (subjectListStr.contains(Const.COMMA + " " + Const.NONE_VALUE)) {
        			subjectListStr = subjectListStr.replace(Const.COMMA + " " + Const.NONE_VALUE, "");
        		}     		
        	}
        	subject = subjectListStr;
        	subject = Utils.cutFirstSelection(subject);
        	if (subject.length() == 0) {
        		subject = Const.EMPTY;
        	}
            if (subject.equals(Const.NONE_VALUE)) {
            	subject = Const.NONE;
            }
        }            	
		Logger.debug("subject: " + subject);
		
//    	String subject_name = form.get(Const.FIELD_SUBJECT);
//    	String subject = "";
//    	if (subject_name != null && !subject_name.toLowerCase().equals(Const.NONE)) {
//    		try {
//    			Logger.info("find subject for title: " + subject_name + ". " + subject_name.length());
//           		subject = Taxonomy.findByNameConf(subject_name).url;
//    		} catch (Exception e) {
//    			Logger.info("Can't find subject for name: " + subject_name + ". " + e);
//    		}
//    	} 
    	String collection = Const.NONE;
        if (form.get(Const.TREE_KEYS) != null) {
    		collection = Utils.removeDuplicatesFromList(form.get(Const.TREE_KEYS));
    		collection = Utils.cutFirstSelection(collection);
        }
		Logger.debug("collection: " + collection);
//    	String collection_name = form.get(Const.FIELD_SUGGESTED_COLLECTIONS);
//    	String collection = "";
//    	if (collection_name != null && !collection_name.toLowerCase().equals(Const.NONE)) {
//    		try {
//    			collection = DCollection.findByTitleExt(collection_name).url;
//    		} catch (Exception e) {
//    			Logger.info("Can't find collection for title: " + collection_name + ". " + e);
//    		}
//    	} 
    	String license_name = form.get(Const.FIELD_LICENSE_NODE);
    	String license = "";
    	if (license_name != null && !license_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			license = Taxonomy.findByName(license_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find license for name: " + license_name + ". " + e);
    		}
    	} 
    	Logger.debug("license: " + license);
    	String depth = form.get(Const.FIELD_DEPTH);
    	String crawlFrequency = form.get(Const.FIELD_CRAWL_FREQUENCY);
    	String inputFlag = form.get(Const.FLAGS);
    	String flag = "";
    	if (inputFlag != null && !inputFlag.toLowerCase().equals(Const.NONE)) {
	    	String origFlag = Flags.getNameFromGuiName(inputFlag);
	    	flag = Flag.findByName(origFlag).url;
    	}
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    			return redirect(
    	        		routes.Targets.list(0, Const.TITLE, Const.ASC, query)
    			        );
    		} 
    		else if (Const.CLEAR.equals(action)) {
    			return GO_TARGETS_HOME;
    		} 
    		else if (Const.EXPORT.equals(action)) {
    			List<Target> exportTargets = new ArrayList<Target>();
    	    	Page<Target> page = Target.pageTargets(0, pageSize, sort, order, query, curator, organisation, 
    					subject, crawlFrequency, depth, collection, license, flag); 
    			int rowCount = page.getTotalRowCount();
    	    	Page<Target> pageAll = Target.pageTargets(0, rowCount, sort, order, query, curator, organisation, 
    					subject, crawlFrequency, depth, collection, license, flag); 
    			exportTargets.addAll(pageAll.getList());
				Logger.info("export size: " + exportTargets.size());
    			export(exportTargets);
    	    	return redirect(routes.Targets.targets(pageNo, sort, order, query, curator, organisation, 
    	    			subject, crawlFrequency, depth, collection, license, pageSize, flag));
    		} 
    		else if (Const.SEARCH.equals(action) || Const.APPLY.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Targets.targets(pageNo, sort, order, query, curator, organisation, 
    	    			subject, crawlFrequency, depth, collection, license, pageSize, flag));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * This method exports selected targets to CSV file.
     * @param list of Target objects
     * @return
     */
    public static void export(List<Target> targetList) {
    	Logger.info("export() targetList size: " + targetList.size());

        StringWriter sw = new StringWriter();
        for (int i = 0; i < Const.targetExportMap.size(); i++) {
        {
            for (Map.Entry<String, Integer> entry : Const.targetExportMap.entrySet())
//        	Logger.info("export key: " + entry.getKey());
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
// 	        	Logger.info("export target: " + target); 	    		
 	            for (int i = 0; i < Const.targetExportMap.size(); i++) {
		 	        for (Map.Entry<String, Integer> entry : Const.targetExportMap.entrySet()) {
			 	   		Field[] fields = Target.class.getFields();
			 			for (Field field : fields) {
		 	               if (entry.getValue() == i) {
//		 	                   Logger.info("field.name: " + field.getName() + ", entry.getkey: " + entry.getKey());
		 	                   if (field.getName().equals(entry.getKey())) {
		 	                	   try {
										Object value = field.get(target);
//				 	                    Logger.info("value: " + value);
				 	                    if (field.getName().equals(Const.AUTHOR)) {
				 	                    	if (value != null) {
				 	                    		value = User.findByUrl((String) value).name;
				 	                    	}
				 	                    }
				 	                    // TODO: CREATED_AT
				 	                    if (field.getName().equals(Const.CREATED_AT)) {
				 	                    	if (value != null) {
				 	                    		value = Utils.showTimestampInTable((String) value);
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

    	Utils.generateCsvFile(Const.EXPORT_FILE, sw.toString());
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url"); 	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    	        Logger.info("create()");
    	    	Target target = new Target();
    	    	target.fieldUrl = query;
    	        target.revision = Const.INITIAL_REVISION;
    	        target.active = true;
    	        if (User.findByEmail(request().username()).hasRole(Const.USER)) {
    	        	target.authorUser.url = User.findByEmail(request().username()).url;
    	        	target.fieldSubSubject = Const.NONE;
    	        	target.fieldSubject = Const.NONE;
    	        }
//	        	target.qa_status = Const.NONE_VALUE;
    			Logger.info("add target with url: " + target.url);
    			Logger.info("target title: " + target.title);
    			Form<Target> targetForm = Form.form(Target.class);
    			targetForm = targetForm.fill(target);
    	        return ok(edit.render(targetForm, User.findByEmail(request().username())));    			
    		} 
    		else if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Targets.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    /**
     * Add new entry.
     * @param target
     * @return
     */
    public static Result create(String title) {
        Logger.info("create()");
    	Target target = new Target();
    	target.fieldUrl = title;
    	// TODO: createId
//        target.id = Target.createId();
//        target.url = Const.ACT_URL + target.id;
        target.revision = Const.INITIAL_REVISION;
        target.active = true;
		Logger.info("add entry with target url: " + target.url);
		Logger.info("target name: " + target.title);
		Form<Target> targetForm = Form.form(Target.class);
		targetForm = targetForm.fill(target);
        return ok(edit.render(targetForm, User.findByEmail(request().username())));
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
    		String collection_url) {
    	Logger.info("Targets.collectionTargets()");
    	
        return ok(
        		sites.render(
        			Collection.findByUrl(collection_url),  
        			User.findByEmail(request().username()), 
        			filter, 
        			Target.pageCollectionTargets(pageNo, 10, sortBy, order, filter, collection_url), 
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
    		String subject_url) {
    	Logger.info("Targets.subjectTargets()");
    	
        return ok(
        		views.html.subjects.sites.render(
        			Taxonomy.findByUrl(subject_url),  
        			User.findByEmail(request().username()), 
        			filter, 
        			Target.pageSubjectTargets(pageNo, 10, sortBy, order, filter, subject_url), 
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
    		String organisation_url) {
    	Logger.info("Targets.organisationTargets()");
    	
        return ok(
        		views.html.organisations.sites.render(
        			Organisation.findByUrl(organisation_url),  
        			User.findByEmail(request().username()), 
        			filter, 
        			Target.pageOrganisationTargets(pageNo, 10, sortBy, order, filter, organisation_url), 
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
//			Logger.info("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return redirect(routes.Collections.list(0, "title", "asc", ""));
//    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String collection_url = form.get(Const.COLLECTION_URL);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Targets.collectionTargets(pageNo, sort, order, query, collection_url));
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
    	String subject_url = form.get(Const.SUBJECT_URL);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Targets.subjectTargets(pageNo, sort, order, query, subject_url));
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
    	String organisation_url = form.get(Const.ORGANISATION_URL);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Targets.organisationTargets(pageNo, sort, order, query, organisation_url));
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
    		String user_url, String subject, String collection) {
    	Logger.info("Targets.collectionTargets()");
    	
        return ok(
        		usersites.render(
        			User.findByUrl(user_url),  
        			User.findByEmail(request().username()), 
        			filter, 
        			Target.pageUserTargets(pageNo, 10, sortBy, order, filter, user_url, subject, collection), 
        			sortBy, 
        			order,
        			subject,
        			collection)
        	);
    }	        
        
    /**
     * This method enables searching for given URL and particular user.
     * @return
     */
    public static Result searchTargetsByUser() {
    	
    	DynamicForm form = DynamicForm.form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url");

    	String user_url = form.get(Const.USER_URL);
    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);

    	String subject_name = form.get(Const.FIELD_SUBJECT);
    	String subject = "";
    	if (subject_name != null && !subject_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			Logger.info("find subject for title: " + subject_name + ". " + subject_name.length());
           		subject = Taxonomy.findByName(subject_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find subject for name: " + subject_name + ". " + e);
    		}
    	} 
    	String collection_name = form.get(Const.FIELD_SUGGESTED_COLLECTIONS);
    	String collection = "";
    	if (collection_name != null && !collection_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			collection = Collection.findByTitle(collection_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find collection for title: " + collection_name + ". " + e);
    		}
    	} 
    	
//    	if (StringUtils.isBlank(query)) {
//			Logger.info("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return redirect(routes.Targets.userTargets(pageNo, sort, order, query, user_url, subject, collection));
//    	}    	
    	
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Targets.userTargets(pageNo, sort, order, query, user_url, subject, collection));
		    } else {
		    	return badRequest("This action is not allowed");
		    }
    	}
    }
        
    /**
     * Display the targets.
     */
    public static Result lookup() {
    	Logger.info("Targets.lookup()");
        return GO_HOME;
    }

    public static Result GO_HOME = redirect(
            routes.Targets.list(0, Const.TITLE, Const.ASC, "")
        );
    
    public static Result GO_TARGETS_HOME = redirect(
            routes.Targets.targets(0, Const.TITLE, Const.ASC, "", "", "", Const.EMPTY, "", "", Const.NONE, "", Const.PAGINATION_OFFSET, "")
        );
       
    /**
     * Display the target edit panel for this URL.
     * @param url The target identifier URL
     */
    public static Result edit(String url) {
		Logger.info("Targets.edit() url: " + url);
		Target target = Target.findByUrl(url);
		if (target.fieldSubject == null || target.fieldSubject.length() == 0) {
			Logger.info("Targets.edit() set subject value to 'None' for imported targets.");
			target.fieldSubject = Const.NONE;
			Ebean.update(target);
		}		
		Logger.info("Targets.edit() target name: " + target.title + ", url: " + url + ", username: " + request().username());
		Form<Target> targetForm = Form.form(Target.class);
		targetForm = targetForm.fill(Target.findByUrl(url));
        return ok(edit.render(targetForm, User.findByEmail(request().username())));
    }
    
    /**
     * @param url The target identifier URL
     * @return
     */
    public static Result view(String url) {
        return ok(
                view.render(
                        Target.findByUrl(url), User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * This method shows selected revision of a Target by given ID.
     * @param nid
     * @return
     */
    public static Result viewrevision(Long nid) {
        return ok(
                view.render(
                        Target.findById(nid), User.findByEmail(request().username())
                )
            );
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
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result exportLdFrequencyJson(String frequency) {
        JsonNode jsonData = null;
        if (frequency != null) {
	        List<Target> targets = new ArrayList<Target>();
        	targets = Target.exportLdFrequency(frequency);
	        jsonData = Json.toJson(targets);
        }
        return ok(jsonData);
    }

    /**
     * Example form with validation
     * @return blank form for data entry
     */
    public static Result blank() {
        Logger.info("blank()");
        return ok(blank.render(targetForm, User.findByEmail(request().username())));
    }
    
    public static Result saveBlank() {
    	play.data.Form<Target> filledForm = targetForm.bindFromRequest();
	    if(filledForm.hasErrors()) {
	        return badRequest(blank.render(filledForm, User.findByEmail(request().username())));
	    } else {
	        flash("success", "You've saved");
	        return ok(blank.render(filledForm, User.findByEmail(request().username())));
	    }
    }
    
    /**
     * This method updates QA status for target if it is changed for e.g. 
     * associated crawl permission.
     * @param fieldUrl The target crawl URL
     * @param qaStatus The current QA status
     */
    public static void updateQaStatus(String fieldUrl, String qaStatus) {
        if (fieldUrl != null) {
        	Target targetObj = Target.findByFieldUrl(fieldUrl);
        	if (targetObj != null && targetObj.url != null) {
	        	targetObj.qaStatus = qaStatus;
	        	Logger.debug("update Qa Status for target object: " + qaStatus);
    	        Ebean.update(targetObj);
        	}
        }
    }
    
    /**
     * This method maps scope value to present predefined Scope values in GUI.
     * @param name The predefined scope value.
     * @return scope name that should be presented in GUI
     */
    public static String getScopeGuiName(String name) {
    	String res = name;
    	if (name != null && name.length() > 0) {
    		String guiName = Const.guiScopeMap.get(name);
    		if (guiName != null && guiName.length() > 0) {
    			res = guiName;
    		}
    	}
    	return res;
    }
    
    /**
     * This method calculates scope value from the GUI scope name.
     * @param name The GUI scope value.
     * @return original scope name 
     */
    public static String getScopeNameFromGuiName(String name) {
    	String res = name;
    	if (name != null && name.length() > 0) {
			for (Map.Entry<String, String> entry : Const.guiScopeMap.entrySet()) {
				if (entry.getValue().equals(name)) {
					res = entry.getKey();
					break;
				}
			}
    	}
    	return res;
    }
            
    /**
     * This method maps depth value to present predefined Scope values in GUI.
     * @param name The predefined depth value.
     * @return depth name that should be presented in GUI
     */
    public static String getDepthGuiName(String name) {
    	String res = name;
    	if (name != null && name.length() > 0) {
    		String guiName = Const.guiDepthMap.get(name);
    		if (guiName != null && guiName.length() > 0) {
    			res = guiName;
    		}
    	}
    	return res;
    }
    
    /**
     * This method calculates depth value from the GUI depth name.
     * @param name The GUI depth value.
     * @return original depth name 
     */
    public static String getDepthNameFromGuiName(String name) {
    	String res = name;
    	if (name != null && name.length() > 0) {
			for (Map.Entry<String, String> entry : Const.guiDepthMap.entrySet()) {
				if (entry.getValue().equals(name)) {
					res = entry.getKey();
					break;
				}
			}
    	}
    	return res;
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
    public static boolean indicateNpldStatus(String fieldUrl) {
    	boolean res = false;
    	if (Target.getNpldStatusList(fieldUrl).size() > 0) {
    		res = true;
    	}
    	Logger.info("indicateNpldStatus() res: " + res);
    	return res;
    }
    
	/**
	 * This method should give a list of the Target Titles and URLs for the 
	 * first three examples, in descending order of date of creation of the record. 
	 * @param fieldUrl The target URL
	 * @return result as a string
	 */
    public static String showNpldStatusList(String fieldUrl) {
    	String res = "";
        try {
            StringBuilder sb = new StringBuilder();
        	List<Target> targets = Target.getNpldStatusList(fieldUrl);
        	Iterator<Target> itr = targets.iterator();
        	while (itr.hasNext()) {
        		Target target = itr.next();
        		sb.append(target.title + " " + target.fieldUrl);
                sb.append(System.getProperty("line.separator"));
        	}
            res = sb.toString();
    	} catch (Exception e) {
            Logger.error("showNpldStatusList() " + e.getMessage());
        }
    	return res;
    }    
}


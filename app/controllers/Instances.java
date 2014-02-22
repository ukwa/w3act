package controllers;

import static play.data.Form.form;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.DCollection;
import models.Organisation;
import models.Instance;
import models.Taxonomy;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.instances.list;
import views.html.instances.instanceedit;
import views.html.collections.collectionsites;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

import views.html.users.*;

/**
 * Manage instances.
 */
@Security.Authenticated(Secured.class)
public class Instances extends AbstractController {
  
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
    	Logger.info("Instances.list()");
        return ok(
        	list.render(
        			"Lookup", 
        			User.find.byId(request().username()), 
        			filter, 
        			Instance.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
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
	        		routes.Instances.list(0, "title", "asc", "")
	        );
    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		return redirect(routes.Instances.create(query));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.Instances.list(pageNo, sort, order, query));
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
        instance.nid = Utils.createId();
        instance.url = Const.ACT_URL + instance.nid;
        instance.revision = Const.INITIAL_REVISION;
		Logger.info("add entry with instance url: " + instance.url);
		Logger.info("instance name: " + instance.title);
        return ok(
                instanceedit.render(
                      instance, User.find.byId(request().username())
                )
            );
    }

	/**
	 * This method filters instances by given license.
	 * @return license list
	 */
	public static List<Taxonomy> getLicense() {
		List<Taxonomy> res = new ArrayList<Taxonomy>();
		List<String> subjects = new ArrayList<String>();
		List<Instance> allInstances = Instance.find.all();
		Iterator<Instance> itr = allInstances.iterator();
		while (itr.hasNext()) {
			Instance instance = itr.next();
			if (instance.field_license != null && instance.field_license.length() > 0 && !subjects.contains(instance.field_license)) {
		        ExpressionList<Instance> ll = Instance.find.where().contains("field_license", instance.field_license);
		        if (ll.findRowCount() > 0) {
		        	Taxonomy taxonomy = Taxonomy.findByUrl(instance.field_license);
		        	Logger.info("instance.field_license: " + instance.field_license + ".");
//		        	Logger.info("taxonomy url: " + taxonomy.url);
//		        	Logger.info("license: " + taxonomy.name);
		        	res.add(taxonomy);
		        	subjects.add(instance.field_license);
		        }
			}
		}
		Logger.info("getLicense res: " + res);
    	return res;
	}
	
	/**
	 * This method filters instances by crawl frequency.
	 * @return crawl frequency list
	 */
	public static List<Instance> getCrawlFrequency() {
		List<Instance> res = new ArrayList<Instance>();
		List<String> subjects = new ArrayList<String>();
		List<Instance> allInstances = Instance.find.all();
		Iterator<Instance> itr = allInstances.iterator();
		while (itr.hasNext()) {
			Instance instance = itr.next();
			if (instance.field_crawl_frequency != null && instance.field_crawl_frequency.length() > 0 && !subjects.contains(instance.field_crawl_frequency)) {
		        ExpressionList<Instance> ll = Instance.find.where().contains("field_crawl_frequency", instance.field_crawl_frequency);
		        if (ll.findRowCount() > 0) {
		        	res.add(instance);
		        	subjects.add(instance.field_crawl_frequency);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters instances by depth.
	 * @return depth list
	 */
	public static List<Instance> getDepth() {
		List<Instance> res = new ArrayList<Instance>();
		List<String> subjects = new ArrayList<String>();
		List<Instance> allInstances = Instance.find.all();
		Iterator<Instance> itr = allInstances.iterator();
		while (itr.hasNext()) {
			Instance instance = itr.next();
			if (instance.field_depth != null && instance.field_depth.length() > 0 && !subjects.contains(instance.field_depth)) {
		        ExpressionList<Instance> ll = Instance.find.where().contains("field_depth", instance.field_depth);
		        if (ll.findRowCount() > 0) {
		        	res.add(instance);
		        	subjects.add(instance.field_depth);
		        }
			}
		}
    	return res;
	}
	
	/**
	 * This method filters instances by collection categories.
	 * @return collection categories list
	 */
	public static List<Taxonomy> getCollectionCategories() {
		List<Instance> res = new ArrayList<Instance>();
		List<String> subjects = new ArrayList<String>();
		List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();
		List<Instance> allInstances = Instance.find.all();
		Iterator<Instance> itr = allInstances.iterator();
		while (itr.hasNext()) {
			Instance instance = itr.next();
			if (instance.field_collection_categories != null && instance.field_collection_categories.length() > 0 && !subjects.contains(instance.field_collection_categories)) {
		        ExpressionList<Instance> ll = Instance.find.where().contains(Const.FIELD_COLLECTION_CATEGORIES, instance.field_collection_categories);
		        if (ll.findRowCount() > 0) {
		        	res.add(instance);
		        	subjects.add(instance.field_collection_categories);
		        	Taxonomy taxonomy = Taxonomy.findByUrl(instance.field_collection_categories);
		        	taxonomies.add(taxonomy);
		        }
			}
		}
    	return taxonomies;
	}
	
    public static Result GO_HOME = redirect(
            routes.Instances.list(0, "title", "asc", "")
        );
    
}


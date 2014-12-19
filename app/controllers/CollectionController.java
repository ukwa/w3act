package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.Collection;
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
import views.html.collections.edit;
import views.html.collections.list;
import views.html.collections.view;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;


@Security.Authenticated(SecuredController.class)
public class CollectionController extends AbstractController {

	/**
	 * Display the Collections.
	 */
	public static Result index() {
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.CollectionController.list(0, "title", "asc", ""));

	/**
	 * Display the paginated list of collections.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filter
	 *            Filter applied on target urls
	 */
	public static Result list(int pageNo, String sortBy, String order, String filter) {
		
		JsonNode node = getCollectionsDataByFilter(filter);
		
		Page<Collection> pages = Collection.pager(pageNo, 10, sortBy, order, filter);
		
		return ok(list.render("Collections", User.findByEmail(request().username()), filter, pages, sortBy, order, node));
	}
	
	/**
	 * This method enables searching for given URL and redirection in order to
	 * add new entry if required.
	 * 
	 * @return
	 */
	public static Result search() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");
    	String query = requestData.get(Const.URL);

    	if (StringUtils.isBlank(query)) {
			Logger.info("Collection name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(routes.CollectionController.list(0, "title", "asc", ""));
    	}
    	
    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");


    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("addentry")) {
    	    	Collection collection = new Collection();
    	    	collection.name = query;
    			List<Collection> thisCollection = new ArrayList<Collection>();
    			thisCollection.add(collection);
    			JsonNode node = getCollectionsData(thisCollection);
    			Form<Collection> collectionForm = Form.form(Collection.class);
    			collectionForm = collectionForm.fill(collection);
    	        return ok(edit.render(collectionForm, User.findByEmail(request().username()), node));    			
    		} 
    		else if (action.equals("search")) {
    	    	return redirect(routes.CollectionController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
	}

	@BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
    	JsonNode jsonData = null;
        if (name != null) {
	        List<Collection> dCollections = Collection.filterByCollectionName(name);
	        jsonData = Json.toJson(dCollections);
        }
        return ok(jsonData);
    }
	
	public static Result view(Long id) {
		User user = User.findByEmail(request().username());
		Collection collection = Collection.findById(id);
		Logger.info("" + id+ " " + collection.parent);
        return ok(view.render(collection, user));
	}
	
    public static Result viewAct(String url) {
		User user = User.findByEmail(request().username());
		Collection collection = Collection.findByUrl(url);
        return ok(view.render(collection, user));
    }
    
    /**
     * Display the collection edit panel for this URL.
     */
    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		Collection collection = Collection.findById(id);
		List<Collection> thisCollection = new ArrayList<Collection>();
		thisCollection.add((Collection)collection.parent);
		JsonNode node = getCollectionsData(thisCollection);
		Form<Collection> collectionForm = Form.form(Collection.class);
		collectionForm = collectionForm.fill(collection);
		Logger.info("id: " + collectionForm.get().id);
        return ok(edit.render(collectionForm, user, node));
    }

    public static Result info(Form<Collection> form) {
    	Logger.info("info");
    	User user = User.findByEmail(request().username());
		List<Collection> thisCollection = new ArrayList<Collection>();
		Collection collection = form.get();
		thisCollection.add(collection);
		JsonNode node = getCollectionsData(thisCollection);
		return ok(edit.render(form, user, node));
    }
    
    /**
     * This method saves new object or changes on given Collection in the same object
     * completed by revision comment. The "version" field in the Collection object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

        if (StringUtils.isNotEmpty(action)) {
        	Form<Collection> collectionForm = Form.form(Collection.class).bindFromRequest();
        	Long id = collectionForm.get().id;

        	if (action.equals("save")) {
	            if(collectionForm.hasErrors()) {
	            	String missingFields = "";
	            	for (String key : collectionForm.errors().keySet()) {
	            	    Logger.debug("key: " +  key);
	            	    key = Utils.showMissingField(key);
	            	    if (missingFields.length() == 0) {
	            	    	missingFields = key;
	            	    } else {
	            	    	missingFields = missingFields + Const.COMMA + " " + key;
	            	    }
	            	}
	            	Logger.info("form errors size: " + collectionForm.errors().size() + ", " + missingFields);
		  			flash("message", "Please fill out all the required fields, marked with a red star." + 
		  					" Missing fields are: " + missingFields);
		  			return info(collectionForm);
	            }
	        	
	            Collection collectionFromDB = Collection.findById(id);
	            Collection collectionFromForm = collectionForm.get();
	            
	            collectionFromDB.name = collectionFromForm.name;
	            collectionFromDB.publish = collectionFromForm.publish;
	            collectionFromDB.description = collectionFromForm.description;
	            collectionFromDB.revision = collectionFromForm.revision;
	            
	            String collectionSelect = requestData.get("collectionSelect").replace("\"", "");
	            Logger.info("collectionSelect: " + collectionSelect);
	            if (StringUtils.isNotEmpty(collectionSelect)) {
	                String[] collections = collectionSelect.split(", ");
	                if (collections.length == 1) {
	                	Long collectionId = Long.valueOf(collections[0]);
	                	if (collectionId == id) {
	                		Logger.info("same id");
	        	  			flash("message", "It is not possible to assign a node to itself as a parent. Please select one parent.");
	        	  			return info(collectionForm);
	                	} else {
			            	Collection collection = Collection.findById(collectionId);
		                	collectionFromDB.parent = collection;
		                	Logger.info("looking good");
	                	}
	                }
	                else if (collections.length > 1) {
	                	Logger.info("Please select only one parent.");
	    	  			flash("message", "Please select only one parent.");
	    	  			return info(collectionForm);
	                }
	            }
	            collectionFromDB.save();
	            
		        res = redirect(routes.CollectionController.view(collectionFromDB.id));
        	} else if (action.equals("delete")) {
	        	Collection collection = Collection.findById(id);
            	/**
            	 * Check whether children exist
            	 */
                if (Collection.hasChildren(collection.url)) {
                	Logger.info("This collection has children nodes. Please re-assign children to other nodes first.");
    	  			flash("message", "This collection has children nodes. Please re-assign children to other nodes first.");
    	  			return info(collectionForm);
                } 
	        	collection.delete();
    	        res = redirect(routes.CollectionController.index()); 
        	}
        } 
        return res;
    }
	    
    public static Result sites(Long id) {
        return redirect(routes.TargetController.collectionTargets(0, "title", "asc", "", id));
    }    
}

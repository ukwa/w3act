package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.DCollection;
import models.Target;
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

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Security.Authenticated(Secured.class)
public class Collections extends AbstractController {

	/**
	 * Display the dcollections.
	 */
	public static Result index() {
		Logger.info("DCollections.index()");
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.Collections.list(0, "title",
			"asc", ""));

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
	public static Result list(int pageNo, String sortBy, String order,
			String filter) {
		JsonNode node = getCollectionsData(filter);
//		Logger.info("LookUp.list() " + node);
		
		return ok(list.render("Collections",
				User.find.byId(request().username()), filter,
				DCollection.page(pageNo, 10, sortBy, order, filter), sortBy,
				order, node));
	}
	
	/**
	 * This method enables searching for given URL and redirection in order to
	 * add new entry if required.
	 * 
	 * @return
	 */
	public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.URL);

		Logger.info("action: " + action);
    	Logger.info("collections search() query: " + query);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Collection name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.Collections.list(0, "title", "asc", "")
	        );
    	}
    	
    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);


    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
//        		return redirect(routes.Collections.create(query));
    	        Logger.info("create collection()");
    	    	DCollection collection = new DCollection();
    	    	collection.title = query;
    	        collection.nid = Target.createId();
    	        collection.url = Const.ACT_URL + collection.nid;
    			Logger.info("add collection with url: " + collection.url + ", and title: " + collection.title);
    			Form<DCollection> collectionForm = Form.form(DCollection.class);
    			collectionForm = collectionForm.fill(collection);
    	        return ok(edit.render(collectionForm, User.find.byId(request().username())));    			

    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Collections.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
	}

	@BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
    	JsonNode jsonData = null;
        if (name != null) {
	        List<DCollection> dCollections = DCollection.filterByName(name);
	        jsonData = Json.toJson(dCollections);
        }
        return ok(jsonData);
    }
	    
	  
    public static Result view(String url) {
        return ok(
                view.render(
                        DCollection.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * Add new collection entry.
     * @param collection title
     * @return
     */
    public static Result create(String title) {
        Logger.info("create collection()");
    	DCollection collection = new DCollection();
    	collection.title = title;
        collection.nid = Target.createId();
        collection.url = Const.ACT_URL + collection.nid;
		Logger.info("add collection with url: " + collection.url + ", and title: " + collection.title);
		Form<DCollection> collectionForm = Form.form(DCollection.class);
		collectionForm = collectionForm.fill(collection);
        return ok(edit.render(collectionForm, User.find.byId(request().username())));
    }
    
    /**
     * Display the collection edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("collection url: " + url);
		DCollection collection = DCollection.findByUrl(url);
		Logger.info("collection title: " + collection.title + ", url: " + url);
		Form<DCollection> collectionForm = Form.form(DCollection.class);
		collectionForm = collectionForm.fill(collection);
        return ok(edit.render(collectionForm, User.find.byId(request().username())));
    }

	/**
	 * This method prepares Target form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	DCollection collection = new DCollection();
    	collection.nid = Long.valueOf(getFormParam(Const.NID));
    	collection.url = getFormParam(Const.URL);
    	collection.title = getFormParam(Const.TITLE);
    	collection.publish = Utils.getNormalizeBooleanString(getFormParam(Const.PUBLISH));
	    if (getFormParam(Const.SUMMARY) != null) {
	    	collection.summary = getFormParam(Const.SUMMARY);
	    }
	    if (collection.revision == null) {
	    	collection.revision = "";
	    }
	    if (getFormParam(Const.REVISION) != null) {
	    	String comma = "";
	    	if (StringUtils.isNotBlank(collection.revision)) {
	    		comma = Const.COMMA + " ";
	    	}
	    	collection.revision = collection.revision.concat(comma + getFormParam(Const.REVISION));
	    }
		Form<DCollection> collectionFormNew = Form.form(DCollection.class);
		collectionFormNew = collectionFormNew.fill(collection);
		Logger.info("info() goto edit");
      	return ok(
	              edit.render(collectionFormNew, User.find.byId(request().username()))
	            );
    }
    
    /**
     * This method saves new object or changes on given Collection in the same object
     * completed by revision comment. The "version" field in the Collection object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("input data for saving collection nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION));
        	
        	Form<DCollection> collectionForm = Form.form(DCollection.class).bindFromRequest();
            if(collectionForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : collectionForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + collectionForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out ll the required fields, marked with a red star." + 
	  					"Missing fields are " + missingFields);
	  			return info();
            }
        	
        	DCollection collection = null;
            boolean isExisting = true;
            try {
                try {
                	collection = DCollection.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	collection = new DCollection();
                	collection.nid = Long.valueOf(getFormParam(Const.NID));
                	collection.url = getFormParam(Const.URL);
                }
                if (collection == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	collection = new DCollection();
                	collection.nid = Long.valueOf(getFormParam(Const.NID));
                	collection.url = getFormParam(Const.URL);
                }
                
                collection.title = getFormParam(Const.TITLE);
                collection.publish = Utils.getNormalizeBooleanString(getFormParam(Const.PUBLISH));
        	    if (getFormParam(Const.SUMMARY) != null) {
        	    	collection.summary = getFormParam(Const.SUMMARY);
        	    }
        	    if (collection.revision == null) {
        	    	collection.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	String comma = "";
                	if (StringUtils.isNotBlank(collection.revision)) {
                		comma = Const.COMMA + " ";
                	}
                	collection.revision = collection.revision.concat(comma + getFormParam(Const.REVISION));
                }
            } catch (Exception e) {
            	Logger.info("Collection not exists exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(collection);
    	        Logger.info("save collection: " + collection.toString());
        	} else {
           		Logger.info("update collection: " + collection.toString());
               	Ebean.update(collection);
        	}
	        res = redirect(routes.Collections.edit(collection.url));
        } 
        if (delete != null) {
        	String url = getFormParam(Const.URL);
        	Logger.info("deleting: " + url);
        	DCollection collection = DCollection.findByUrl(url);
        	Ebean.delete(collection);
	        res = redirect(routes.Collections.index()); 
        }
        return res;
    }
	    
    public static Result sites(String url) {
        return redirect(routes.Targets.collectionTargets(0, "title", "asc", "", url));
    }    

    /**
     * This method computes a tree of collections in JSON format. 
     * @param collectionUrl This is an identifier for current selected object
     * @return tree structure
     */

    private static JsonNode getCollectionsData(String url) {
    	List<DCollection> collections = DCollection.getFirstLevelCollections();
    	List<ObjectNode> result = getCollectionTreeElements(collections, url, true);
    	Logger.info("collections main level size: " + collections.size());
    	JsonNode jsonData = Json.toJson(result);
        return jsonData;
    }
    
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param collectionUrl This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static List<ObjectNode> getCollectionTreeElements(List<DCollection> collectionList, String collectionUrl, boolean parent) { 
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

		if (collectionList.size() > 0) {
	    	Iterator<DCollection> itr = collectionList.iterator();
	    	while (itr.hasNext()) {
	    		DCollection collection = itr.next();
	    		
	    		if (collectionUrl.isEmpty() || (StringUtils.isNotEmpty(collectionUrl) && StringUtils.containsIgnoreCase(collection.title, collectionUrl))) {	    		
		    		if ((parent && collection.parent.length() == 0) || !parent) {
						ObjectNode child = nodeFactory.objectNode();
						child.put("title", collection.title + " (" + Target.findAllforCollection(collection.url).size() + ")");
						child.put("url", String.valueOf(routes.Collections.view(collection.url)));
				    	if (StringUtils.isNotEmpty(collection.url) && collection.url.equalsIgnoreCase(collectionUrl)) {
				    		child.put("select", true);
				    	}
						child.put("key", "\"" + collection.url + "\"");
				    	List<DCollection> childCollections = DCollection.getChildLevelCollections(collection.url);
				    	if (childCollections.size() > 0) {
				    		child.put("children", Json.toJson(getCollectionTreeElements(childCollections, collectionUrl, false)));
				    	}
						result.add(child);
		    		}
	    		}
	    	}
    	}
//    	Logger.info("getTreeElements() res: " + result);
    	return result;
    }
}

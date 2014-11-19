package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Collection;
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
	 * Display the Collections.
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
		
		return ok(list.render("Collections",
				User.findByEmail(request().username()), filter,
				Collection.pager(pageNo, 10, sortBy, order, filter), sortBy,
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
    	    	Collection collection = new Collection();
    	    	collection.name = query;
    			Logger.info("add collection with url: " + collection.url + ", and title: " + collection.name);
    			JsonNode node = getCollectionsTree(collection.url);
//    			Logger.info("node tree: " + node.toString());
    			Form<Collection> collectionForm = Form.form(Collection.class);
    			collectionForm = collectionForm.fill(collection);
    	        return ok(edit.render(collectionForm, User.findByEmail(request().username()), node));    			
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
	        List<Collection> dCollections = Collection.filterByCollectionName(name);
	        jsonData = Json.toJson(dCollections);
        }
        return ok(jsonData);
    }
	    
	  
    public static Result view(String url) {
        return ok(
                view.render(
                        Collection.findByUrl(url), User.findByEmail(request().username())
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
    	Collection collection = new Collection();
    	collection.name = title;
		Logger.info("add collection with url: " + collection.url + ", and title: " + collection.name);
		JsonNode node = getCollectionsTree(collection.url);
		Form<Collection> collectionForm = Form.form(Collection.class);
		collectionForm = collectionForm.fill(collection);
        return ok(edit.render(collectionForm, User.findByEmail(request().username()), node));
    }
    
    /**
     * Display the collection edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("collection url: " + url);
		Collection collection = Collection.findByUrl(url);
		Logger.info("collection title: " + collection.name + ", url: " + url);
		JsonNode node = getCollectionsTree(collection.url);
		Form<Collection> collectionForm = Form.form(Collection.class);
		collectionForm = collectionForm.fill(collection);
        return ok(edit.render(collectionForm, User.findByEmail(request().username()), node));
    }

	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Collection collection = new Collection();
    	collection.id = Long.valueOf(getFormParam(Const.ID));
    	collection.url = getFormParam(Const.URL);
    	collection.name = getFormParam(Const.TITLE);
    	collection.publish = Utils.getNormalizeBooleanString(getFormParam(Const.PUBLISH));
        if (getFormParam(Const.TREE_KEYS) != null) {
    		collection.parent = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
    		Logger.debug("collection parent: " + collection.parent);
        }
	    if (getFormParam(Const.SUMMARY) != null) {
	    	collection.description = getFormParam(Const.SUMMARY);
	    }
	    if (collection.revision == null) {
	    	collection.revision = "";
	    }
	    if (getFormParam(Const.REVISION) != null) {
	    	collection.revision = getFormParam(Const.REVISION);
	    }
		JsonNode node = getCollectionsTree(collection.url);
		Form<Collection> collectionFormNew = Form.form(Collection.class);
		collectionFormNew = collectionFormNew.fill(collection);
      	return ok(
	              edit.render(collectionFormNew, User.findByEmail(request().username()), node)
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
        	Logger.info("input data for saving collection nid: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION));
        	
        	Form<Collection> collectionForm = Form.form(Collection.class).bindFromRequest();
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
	  			return info();
            }
        	
        	Collection collection = null;
            boolean isExisting = true;
            try {
                try {
                	collection = Collection.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	collection = new Collection();
                	collection.id = Long.valueOf(getFormParam(Const.ID));
                	collection.url = getFormParam(Const.URL);
                }
                if (collection == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	collection = new Collection();
                	collection.id = Long.valueOf(getFormParam(Const.ID));
                	collection.url = getFormParam(Const.URL);
                }
                
                collection.name = getFormParam(Const.TITLE);
                collection.publish = Utils.getNormalizeBooleanString(getFormParam(Const.PUBLISH));
//        	    if (getFormParam(Const.PARENT) != null) {
//                	if (!getFormParam(Const.PARENT).toLowerCase().contains(Const.NONE)) {
//                		collection.parent = DCollection.findByTitleExt(getFormParam(Const.PARENT)).url;
//                	}
//        	    }
                if (getFormParam(Const.TREE_KEYS) != null) {
            		collection.parent = Utils.removeDuplicatesFromList(getFormParam(Const.TREE_KEYS));
            		Logger.debug("collection parent: " + collection.parent);
            		if (StringUtils.isNotEmpty(collection.parent) && collection.parent.contains(Const.COMMA)) {
                    	Logger.info("Please select only one parent.");
        	  			flash("message", "Please select only one parent.");
        	  			return info();
                    }
            		if (StringUtils.isNotEmpty(collection.parent) && collection.parent.equals(collection.url)) {
                    	Logger.info("It is not possible to assign a node to itself as a parent. Please select one parent.");
        	  			flash("message", "It is not possible to assign a node to itself as a parent. Please select one parent.");
        	  			return info();
                    }
                }
        	    if (getFormParam(Const.SUMMARY) != null) {
        	    	collection.description = getFormParam(Const.SUMMARY);
        	    }
        	    if (collection.revision == null) {
        	    	collection.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
        	    	collection.revision = getFormParam(Const.REVISION);
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
        	Collection collection = Collection.findByUrl(url);
        	/**
        	 * Check whether children exist
        	 */
            if (Collection.hasChildren(collection.url)) {
            	Logger.info("This collection has children nodes. Please re-assign children to other nodes first.");
	  			flash("message", "This collection has children nodes. Please re-assign children to other nodes first.");
	  			return info();
            } 
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
    	List<Collection> collections = Collection.getFirstLevelCollections();
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
    public static List<ObjectNode> getCollectionTreeElements(List<Collection> collectionList, String collectionUrl, boolean parent) { 
		List<ObjectNode> result = new ArrayList<ObjectNode>();
		JsonNodeFactory nodeFactory = new JsonNodeFactory(false);

		if (collectionList.size() > 0) {
	    	Iterator<Collection> itr = collectionList.iterator();
	    	while (itr.hasNext()) {
	    		Collection collection = itr.next();
	    		
	    		if (collectionUrl.isEmpty() || (StringUtils.isNotEmpty(collectionUrl) && StringUtils.containsIgnoreCase(collection.name, collectionUrl))) {	    		
		    		if ((parent && collection.parent.length() == 0) || !parent || collection.parent.equals(Const.NONE_VALUE)) {
						ObjectNode child = nodeFactory.objectNode();
						child.put("title", collection.name + " (" + Target.findAllforCollection(collection.url).size() + ")");
						child.put("url", String.valueOf(routes.Collections.view(collection.url)));
				    	if (StringUtils.isNotEmpty(collection.url) && collection.url.equalsIgnoreCase(collectionUrl)) {
				    		child.put("select", true);
				    	}
						child.put("key", "\"" + collection.url + "\"");
				    	List<Collection> childCollections = Collection.getChildLevelCollections(collection.url);
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
    
    /**
     * This method presents collections in a tree form.
     * @param url
     * @return
     */
    private static JsonNode getCollectionsTree(String url) {
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Collection> suggestedCollections = Collection.getFirstLevelCollections();
    	if (url != null && url.length() > 0) {
    		try {
	    		Collection collection = Collection.findByUrl(url);
	    		if (StringUtils.isNotEmpty(collection.parent)) {
	    			url = collection.parent;
	    		}
    		} catch (Exception e) {
    			Logger.info("New collection has no parent yet.");
    		}
    	}    	
    	sb.append(getTreeElements(suggestedCollections, url, true));
//    	Logger.info("collections main level size: " + suggestedCollections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getCollections() json: " + jsonData.toString());
        return jsonData;
    }
    
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param url This is an identifier for current collection object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getTreeElements(List<Collection> collectionList, String url, boolean parent) { 
//    	Logger.info("getTreeElements() target URL: " + targetUrl);
    	String res = "";
    	if (collectionList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	        if (parent) {
	        	sb.append("{\"title\": \"" + "None" + "\"," + checkNone(url) + 
	        			" \"key\": \"" + "None" + "\"" + "}, ");
	        }
	    	Iterator<Collection> itr = collectionList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		Collection collection = itr.next();
//    			Logger.debug("getTreeElements() add collection: " + collection.title + ", with url: " + collection.url +
//    					", parent:" + collection.parent + ", parent size: " + collection.parent.length());
	    		if ((parent && collection.parent.length() == 0) || !parent || collection.parent.equals(Const.NONE_VALUE)) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.name + "\"," + checkSelection(collection.url, url) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, url) + "}");
	    		}
	    	}
	    	Logger.info("collectionList level size: " + collectionList.size());
	    	sb.append("]");
	    	res = sb.toString();
//	    	Logger.info("getTreeElements() res: " + res);
    	}
    	return res;
    }
        
    /**
     * Check if none value is selected
     * @param url This is an identifier for current collection object
     * @return
     */
    public static String checkNone(String url) {
    	String res = "";
//    	if (url != null && url.length() > 0) {
//    		Logger.info("checkNone: " + url);
//    		DCollection collection = DCollection.findByUrl(url);
//    		Logger.info("checkNone parent: " + collection.parent);
//    		if (collection.parent != null 
//    				&& (collection.parent.toLowerCase().contains(Const.NONE.toLowerCase()))) {
    		if (StringUtils.isNotEmpty(url) && url.toLowerCase().equals(Const.NONE.toLowerCase())) {
    			res = "\"select\": true ,";
    		}
//    	}
    	return res;
    }
    
    /**
     * Mark collections that are stored in target object as selected
     * @param collectionUrl The collection identifier
     * @param currentUrl This is an identifier for current collection object
     * @return
     */
    public static String checkSelection(String collectionUrl, String currentUrl) {
    	String res = "";
    	if (currentUrl != null && currentUrl.length() > 0) {
    		if (currentUrl.equals(collectionUrl)) {
    			res = "\"select\": true ,";
    		}
    	}
    	return res;
    }
    
    /**
     * This method calculates collection children - objects that have parents.
     * @param url The identifier for parent 
     * @param currentUrl This is an identifier for current collection object
     * @return child collection in JSON form
     */
    public static String getChildren(String url, String currentUrl) {
//    	Logger.info("getChildren() target URL: " + targetUrl);
    	String res = "";
        final StringBuffer sb = new StringBuffer();
    	sb.append(", \"children\":");
    	List<Collection> childSuggestedCollections = Collection.getChildLevelCollections(url);
    	if (childSuggestedCollections.size() > 0) {
	    	sb.append(getTreeElements(childSuggestedCollections, currentUrl, false));
	    	res = sb.toString();
//	    	Logger.info("getChildren() res: " + res);
    	}
    	return res;
    }
    
}

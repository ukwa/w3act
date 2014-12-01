package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Collection;
import models.Target;
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
import views.html.qa.list;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage QA.
 */
@Security.Authenticated(Secured.class)
public class QAController extends AbstractController {
  
    /**
     * Display the QA dashboard.
     */
    public static Result index() {
    	Logger.info("QA.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.QAController.list(0, "title", "asc", "", "act-", "", "")
        );
    
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter, String collection, String qaStatus, String keyQaStatus) {
    	
    	Page<Target> page = Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus);
    	Logger.info("Calling QAController.list() collection: " + collection + " - " + qaStatus);
//    	if (page.getTotalRowCount() == 0) {
//    		pageNo = 0;
//        	page = Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus);
//    	}
    	Logger.info("Called QAController.list() collection: " + collection + " - " + qaStatus);
    	
        return ok(
        	list.render(
        			"QA", 
        			User.findByEmail(request().username()), 
        			filter, 
        			page,
//        			Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus), 
        			sortBy, 
        			order,
        			collection,
        			qaStatus, keyQaStatus)
//        			Taxonomy.findQaStatus(qaStatus))
        	);
    }
	
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
//    	Logger.info("QAController.search");
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url");
    	Logger.info("QAController.search() query: " + query);
//    	Logger.info("treeKeys: " + form.get(Const.TREE_KEYS));
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Target name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.QAController.list(0, "title", "asc", "", "act-", "", "")
	        );
    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String query_qa_status_name = form.get(Const.QA_STATUS);
//    	String query_collection_name = form.get(Const.FIELD_SUGGESTED_COLLECTIONS);
//    	Logger.info("QAController.search() query_collection_name: " + query_collection_name);
    	Logger.info("query_qa_status_name: " + query_qa_status_name);
    	String query_collection = "";
//    	if (query_collection_name != null && !query_collection_name.toLowerCase().equals(Const.NONE)) {
//    		try {
//    			query_collection = DCollection.findByTitle(query_collection_name).url;
//    		} catch (Exception e) {
//    			Logger.info("Can't find collection for URL: " + query_collection_name + ". " + e);
//    		}
//    	} 
        if (form.get(Const.TREE_KEYS) != null) {
        	query_collection = Utils.removeDuplicatesFromList(form.get(Const.TREE_KEYS));
    		Logger.debug("query_collection: " + query_collection);
        }
    	String query_qa_status = "";
    	if (query_qa_status_name != null && !query_qa_status_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			query_qa_status = Taxonomy.findQaStatusUrl(query_qa_status_name);
    		} catch (Exception e) {
    			Logger.info("Can't find QA status for URL: " + query_qa_status_name + ". " + e);
    		}
    	} 

    	Logger.info("query_qa_status: " + query_qa_status);
    	
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    			if (query_collection == null || query_collection.length() == 0) {
				    query_collection = Const.ACT_URL;
				}
    			Logger.info("values: " + pageNo + " - " + sort + " - " + order + " - " + query + " - " + query_collection + " - " + query_qa_status);
    	    	return redirect(routes.QAController.list(pageNo, sort, order, query, query_collection, query_qa_status, query_qa_status_name));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String url) {
        JsonNode jsonData = null;
        if (url != null) {
	        List<Target> targets = Target.filterUrl(url);
	        jsonData = Json.toJson(targets);
        }
        return ok(jsonData);
    }
        
    /**
     * This method returns a list of all QA status types.
     * @return
     */
    public static List<String> getAllQAStatusTypes() {
    	List<String> res = new ArrayList<String>();
	    Const.QAStatusType[] resArray = Const.QAStatusType.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
	    }
	    return res;
    }    
    
    /**
     * This method returns a list of all QA issue categories.
     * @return
     */
    public static List<String> getAllQaIssueCategories() {
    	List<String> res = new ArrayList<String>();
	    Const.QAIssueCategory[] resArray = Const.QAIssueCategory.values();
	    for (int i=0; i < resArray.length; i++) {
		    res.add(resArray[i].name());
//		    Logger.info("add category: " + resArray[i].name());
	    }
	    return res;
    }    
    
    /**
     * This method computes a tree of collections in JSON format. 
     * @param collectionUrl This is an identifier for current selected object
     * @return tree structure
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result getCollections(String collectionUrl) {
    	Logger.info("QA dashboard getCollections()");
    	if (collectionUrl == null || collectionUrl.length() == 0) {
    		collectionUrl = Const.ACT_URL;
    	}
        JsonNode jsonData = null;
        final StringBuffer sb = new StringBuffer();
    	List<Collection> collections = Collection.getFirstLevelCollections();
    	sb.append(getCollectionTreeElements(collections, collectionUrl, true));
    	Logger.info("collections main level size: " + collections.size());
        jsonData = Json.toJson(Json.parse(sb.toString()));
//    	Logger.info("getCollections() json: " + jsonData.toString());
        return ok(jsonData);
    }
        
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param collectionUrl This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
    public static String getCollectionTreeElements(List<Collection> collectionList, String collectionUrl, boolean parent) { 
    	String res = "";
    	if (collectionList.size() > 0) {
	        final StringBuffer sb = new StringBuffer();
	        sb.append("[");
	    	Iterator<Collection> itr = collectionList.iterator();
	    	boolean firstTime = true;
	    	while (itr.hasNext()) {
	    		Collection collection = itr.next();
//    			Logger.debug("add collection: " + collection.title + ", with url: " + collection.url +
//    					", parent:" + collection.parent + ", parent size: " + collection.parent.length());
	    		if ((parent && (collection.parent == null || collection.parent == null)) 
	    				|| !parent) {
		    		if (firstTime) {
		    			firstTime = false;
		    		} else {
		    			sb.append(", ");
		    		}
//	    			Logger.debug("added");
					sb.append("{\"title\": \"" + collection.name + "\"," + checkCollectionSelection(collection.url, collectionUrl) + 
							" \"key\": \"" + collection.url + "\"" + 
							getChildren(collection.url, collectionUrl) + "}");
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
     * Mark collections that are stored in target object as selected
     * @param collectionUrl The collection identifier
     * @param checkedUrl This is an identifier for current target object
     * @return
     */
    public static String checkCollectionSelection(String collectionUrl, String checkedUrl) {
    	String res = "";
    	if (checkedUrl != null && checkedUrl.length() > 0 && checkedUrl.equals(collectionUrl)) {
    		res = "\"select\": true ,";
    	}
    	return res;
    }
        
    /**
     * This method calculates collection children - objects that have parents.
     * @param url The identifier for parent 
     * @param collectionUrl This is an identifier for current collection object
     * @return child collection in JSON form
     */
    public static String getChildren(String url, String collectionUrl) {
    	String res = "";
        final StringBuffer sb = new StringBuffer();
    	sb.append(", \"children\":");
    	List<Collection> childCollections = Collection.getChildLevelCollections(url);
    	if (childCollections.size() > 0) {
	    	sb.append(getCollectionTreeElements(childCollections, collectionUrl, false));
	    	res = sb.toString();
//	    	Logger.info("getChildren() res: " + res);
    	}
    	return res;
    }
    
}


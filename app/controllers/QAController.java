package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.QaIssue;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.qa.list;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage QA.
 */
@Security.Authenticated(SecuredController.class)
public class QAController extends AbstractController {
  
    /**
     * Display the QA dashboard.
     */
    public static Result index() {
    	Logger.debug("QA.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.QAController.list(0, "title", "asc", "", "", 0)
        );
    
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter, String collection, Long qaIssueId) {
    	
    	Page<Target> page = Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaIssueId);
    	Logger.debug("Calling QAController.list() collection: " + collection + " - " + qaIssueId);
//    	if (page.getTotalRowCount() == 0) {
//    		pageNo = 0;
//        	page = Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus);
//    	}
    	Logger.debug("Called QAController.list() collection: " + collection + " - " + qaIssueId + " - " + page);
    	
		User user = User.findByEmail(request().username());
		
        String collectionSelect = collection.replace("\"", "");
    	List<Long> collectionIds = new ArrayList<Long>();
        String[] collections = collectionSelect.split(", ");
        for (String cId : collections) {
        	if (StringUtils.isNotEmpty(cId)) {
	        	Long collectionId = Long.valueOf(cId);
	        	collectionIds.add(collectionId);
        	}
        }
        JsonNode collectionData = getCollectionsDataByIds(collectionIds);
		List<QaIssue> qaIssues = QaIssue.findAllQaIssue();
		
		Logger.debug("qaIssue: " + qaIssueId);
		
        return ok(
        	list.render(
        			"QA", 
        			user, 
        			filter, 
        			page,
//        			Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus), 
        			sortBy, 
        			order,
        			collection,
        			qaIssueId, collectionData, qaIssues)
//        			Taxonomy.findQaStatus(qaStatus))
        	);
    }
	
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
//    	Logger.debug("QAController.search");
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");
    	String query = requestData.get("url");
    	
    	Logger.debug("QAController.search() query: " + query);
    	
//    	if (StringUtils.isBlank(query)) {
//			Logger.debug("Target name is empty. Please write name in search window.");
//			flash("message", "Please enter a name in the search window");
//	        return redirect(
//	        		routes.QAController.list(0, "title", "asc", "", "", 0)
//	        );
//    	}    	
//
    	int pageNo = Integer.parseInt(requestData.get("p"));
    	String sort = requestData.get("s");
    	String order = requestData.get("o");
    	String qaStatus = requestData.get("qaIssueId");
    	Long qaStatusId = null;
    	if (StringUtils.isNotBlank(qaStatus)) {
    		qaStatusId = Long.valueOf(qaStatus);
    	}
    			
    	String collectionSelect = requestData.get("collectionSelect");
    	if (StringUtils.isEmpty(collectionSelect)) {
    		collectionSelect = "";
    	}
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else if (action.equals("search")) {
			Logger.debug("searching: " + pageNo + " - " + sort + " - " + order + " - " + query + " - " + collectionSelect + " - " + qaStatusId);
	    	return redirect(routes.QAController.list(pageNo, sort, order, query, collectionSelect, qaStatusId));
	    } else {
		      return badRequest("This action is not allowed");
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
//		    Logger.debug("add category: " + resArray[i].name());
	    }
	    return res;
    }    
    
    /**
     * This method computes a tree of collections in JSON format. 
     * @param collectionUrl This is an identifier for current selected object
     * @return tree structure
     */
//    @BodyParser.Of(BodyParser.Json.class)
//    public static Result getCollections(String collectionUrl) {
//    	Logger.debug("QA dashboard getCollections()");
//    	if (collectionUrl == null || collectionUrl.length() == 0) {
//    		collectionUrl = Const.ACT_URL;
//    	}
//        JsonNode jsonData = null;
//        final StringBuffer sb = new StringBuffer();
//    	List<Collection> collections = Collection.getFirstLevelCollections();
//    	sb.append(getCollectionTreeElements(collections, collectionUrl, true));
//    	Logger.debug("collections main level size: " + collections.size());
//        jsonData = Json.toJson(Json.parse(sb.toString()));
////    	Logger.debug("getCollections() json: " + jsonData.toString());
//        return ok(jsonData);
//    }
        
    /**
   	 * This method calculates first order collections.
     * @param collectionList The list of all collections
     * @param collectionUrl This is an identifier for current selected object
     * @param parent This parameter is used to differentiate between root and children nodes
     * @return collection object in JSON form
     */
//    public static String getCollectionTreeElements(List<Collection> collectionList, String collectionUrl, boolean parent) { 
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
//	    		if ((parent && (collection.parent == null || collection.parent == null)) 
//	    				|| !parent) {
//		    		if (firstTime) {
//		    			firstTime = false;
//		    		} else {
//		    			sb.append(", ");
//		    		}
////	    			Logger.debug("added");
//					sb.append("{\"title\": \"" + collection.name + "\"," + checkCollectionSelection(collection.url, collectionUrl) + 
//							" \"key\": \"" + collection.url + "\"" + 
//							getChildren(collection.url, collectionUrl) + "}");
//	    		}
//	    	}
////	    	Logger.debug("collectionList level size: " + collectionList.size());
//	    	sb.append("]");
//	    	res = sb.toString();
////	    	Logger.debug("getTreeElements() res: " + res);
//    	}
//    	return res;
//    }
    
//    /**
//     * Mark collections that are stored in target object as selected
//     * @param collectionUrl The collection identifier
//     * @param checkedUrl This is an identifier for current target object
//     * @return
//     */
//    public static String checkCollectionSelection(String collectionUrl, String checkedUrl) {
//    	String res = "";
//    	if (checkedUrl != null && checkedUrl.length() > 0 && checkedUrl.equals(collectionUrl)) {
//    		res = "\"select\": true ,";
//    	}
//    	return res;
//    }
        
//    /**
//     * This method calculates collection children - objects that have parents.
//     * @param url The identifier for parent 
//     * @param collectionUrl This is an identifier for current collection object
//     * @return child collection in JSON form
//     */
//    public static String getChildren(String url, String collectionUrl) {
//    	String res = "";
//        final StringBuffer sb = new StringBuffer();
//    	sb.append(", \"children\":");
//    	List<Taxonomy> childCollections = Collection.getChildLevelCollections(url);
//    	if (childCollections.size() > 0) {
//	    	sb.append(getCollectionTreeElements(childCollections, collectionUrl, false));
//	    	res = sb.toString();
////	    	Logger.debug("getChildren() res: " + res);
//    	}
//    	return res;
//    }
    
}


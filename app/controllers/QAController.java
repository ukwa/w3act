package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.DCollection;
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
            routes.QAController.list(0, "title", "asc", "", "", "")
        );
    
    /**
     * Display the paginated list of targets.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter, String collection, String qaStatus) {
    	Logger.info("QAController.list() collection: " + collection);
    	Page<Target> page = Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus);
    	if (page.getTotalRowCount() == 0) {
    		pageNo = 0;
        	page = Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus);
    	}
        return ok(
        	list.render(
        			"QA", 
        			User.find.byId(request().username()), 
        			filter, 
        			page,
//        			Target.pageQa(pageNo, 10, sortBy, order, filter, collection, qaStatus), 
        			sortBy, 
        			order,
        			collection,
        			Taxonomy.findQaStatus(qaStatus))
        	);
    }
	
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	Logger.info("QAController.search");
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get("url");
    	Logger.info("QAController.search() query: " + query);

    	if (StringUtils.isBlank(query)) {
			Logger.info("Target name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.QAController.list(0, "title", "asc", "", "", "")
	        );
    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
    	String query_qa_status_name = form.get(Const.QA_STATUS);
    	String query_collection_name = form.get(Const.FIELD_SUGGESTED_COLLECTIONS);
//    	Logger.info("QAController.search() query_collection_name: " + query_collection_name);
    	Logger.info("QAController.search() query_qa_status_name: " + query_qa_status_name);
    	String query_collection = "";
    	if (query_collection_name != null && !query_collection_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			query_collection = DCollection.findByTitle(query_collection_name).url;
    		} catch (Exception e) {
    			Logger.info("Can't find collection for URL: " + query_collection_name + ". " + e);
    		}
    	} 
    	String query_qa_status = "";
    	if (query_qa_status_name != null && !query_qa_status_name.toLowerCase().equals(Const.NONE)) {
    		try {
    			query_qa_status = Taxonomy.findQaStatusUrl(query_qa_status_name);
    		} catch (Exception e) {
    			Logger.info("Can't find QA status for URL: " + query_qa_status_name + ". " + e);
    		}
    	} 
    	Logger.info("QAController.search() query_qa_status: " + query_qa_status);
    	
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.QAController.list(pageNo, sort, order, query, query_collection, query_qa_status));
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
    
}


package controllers;

import static play.data.Form.form;

import java.util.List;

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
            routes.QAController.list(0, "title", "asc", "")
        );
    
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
        			"QA", 
        			User.find.byId(request().username()), 
        			filter, 
        			Target.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
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

    	if (StringUtils.isBlank(query)) {
			Logger.info("Target name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.QAController.list(0, "title", "asc", "")
	        );
    	}    	

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.SEARCH.equals(action)) {
    			Logger.info("searching " + pageNo + " " + sort + " " + order);
    	    	return redirect(routes.QAController.list(pageNo, sort, order, query));
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
        
}


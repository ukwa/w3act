package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Flag;
import models.Target;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.flags.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage flags.
 */
@Security.Authenticated(SecuredController.class)
public class FlagController extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
    	Logger.info("Flags.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.FlagController.list(0, "name", "asc", "")
        );
    

    public static Result view(Long id) {
        return ok(
                view.render(
                		models.Flag.findById(id), User.findByEmail(request().username())
                )
            );
    }

    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.QUERY);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Flag name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.FlagController.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.FlagController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}      
    }	   
    
    /**
     * This method applies filters to the list of flags.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<Flag> processFilterFlags(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<Flag> exp = Flag.find.where();
    	List<Flag> res = new ArrayList<Flag>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.Flag.findAllFlags();
    	}
        return res;
    }
        

    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<Flag> flagForm = Form.form(Flag.class);
		Flag flag = new Flag();
		flagForm = flagForm.fill(flag);
        return ok(newForm.render(flagForm, user));
    	
    }

    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
    	Flag flag = Flag.findById(id);
		Form<Flag> flagForm = Form.form(Flag.class);
		flagForm = flagForm.fill(flag);
        return ok(edit.render(flagForm, user, id));
    }

    public static Result info(Form<Flag> form, Long id) {
    	User user = User.findByEmail(request().username());
		return badRequest(edit.render(form, user, id));
    }
    
	public static Result newInfo(Form<Flag> form) {
		User user = User.findByEmail(request().username());
        return badRequest(newForm.render(form, user));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Flag> filledForm = form(Flag.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
		        filledForm.get().save();
		        flash("message", "Flag " + filledForm.get().name + " has been created");
		        return redirect(routes.FlagController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<Flag> filledForm = form(Flag.class).bindFromRequest();
    	Logger.debug("hasGlobalErrors: " + filledForm.hasGlobalErrors());
    	Logger.debug("hasErrors: " + filledForm.hasErrors());

    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {    
		        if (filledForm.hasErrors()) {
		        	Logger.debug("hasErrors: " + filledForm.errors());
		            return info(filledForm, id);
		        }
		        
		        filledForm.get().update(id);
		        flash("message", "Flag " + filledForm.get().name + " has been updated");
		        return redirect(routes.FlagController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		Flag flag = Flag.findById(id);
		        flash("message", "Flag " + filledForm.get().name + " has been deleted");
            	flag.delete();
            	
        		return redirect(routes.FlagController.index()); 
        	}
        }
        return null;
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Flag> flags = Flag.filterByFlagName(name);
	        jsonData = Json.toJson(flags);
        }
        return ok(jsonData);
    }
    
    /**
     * Display the paginated list of Flags.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Flags
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Flags.list()");
        return ok(
        	list.render(
        			"Flags", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Flag.pager(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }       
}


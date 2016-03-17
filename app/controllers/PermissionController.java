package controllers;

import static play.data.Form.form;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import models.MailTemplate;
import models.Permission;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.permissions.*;

/**
 * Manage permissions.
 */
@Security.Authenticated(SecuredController.class)
public class PermissionController extends AbstractController {
  
    /**
     * Display the permission.
     */
    public static Result index() {
    	Logger.debug("Permissions.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.PermissionController.list(0, "name", "asc", "")
        );
    
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<Permission> permissionForm = Form.form(Permission.class);
		Permission permissions = new Permission();
		permissionForm = permissionForm.fill(permissions);
        return ok(newForm.render(permissionForm, user));
    }
    
    public static Result edit(Long id) {
		Permission permission = Permission.findById(id);
		if (permission == null) return notFound("There is no Permission with ID " + id);
		
		Form<Permission> permissionsForm = Form.form(Permission.class);
		permissionsForm = permissionsForm.fill(permission);
      	return ok(edit.render(permissionsForm, User.findByEmail(request().username()), id));
    }
    
    public static Result view(Long id) {
		Permission permission = Permission.findById(id);
		if (permission == null) return notFound("There is no Permission with ID " + id);
		
        return ok(
                view.render(permission, User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
        
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");
    	String query = requestData.get(Const.QUERY);
		Logger.debug("query: " + query);
		Logger.debug("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Permission name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.PermissionController.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = 0;
    	String page = requestData.get("p");
    	if (StringUtils.isNotBlank(page)) {
    		pageNo = Integer.valueOf(page);
    	}
    	String sort = requestData.get("s");
    	String order = requestData.get("o");

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.PermissionController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}               
        
    }	   
    
	/**
	 * This method prepares Permission form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info(Form<Permission> form, Long id) {
		User user = User.findByEmail(request().username());
        return badRequest(edit.render(form, user, id));
	}

	public static Result newInfo(Form<Permission> form) {
		User user = User.findByEmail(request().username());
		return badRequest(newForm.render(form, user));
	}
    /**
     * This method saves new object or changes on given Permission in the same object
     * completed by revision comment. The "version" field in the Permission object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<Permission> filledForm = form(Permission.class).bindFromRequest();
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
		        flash("message", "Permission " + filledForm.get().name + " has been updated");
		        return redirect(routes.PermissionController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		Permission permission = Permission.findById(id);
		        flash("message", "Permission " + filledForm.get().name + " has been deleted");
            	permission.delete();
        		return redirect(routes.PermissionController.index()); 
        	}
        }
        return null;
    }	   

    public static Result save() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Permission> filledForm = form(Permission.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        filledForm.get().save();
		        flash("message", "Permission " + filledForm.get().name + " has been created");
		        return redirect(routes.PermissionController.view(filledForm.get().id));
        	}
        }
        return null;
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Permission> permissions = Permission.filterByName(name);
	        jsonData = Json.toJson(permissions);
        }
        return ok(jsonData);
    }
    
    /**
     * Display the paginated list of Curators.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.debug("Permissions.list()");
        return ok(
        	list.render(
        			"Permissions", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Permission.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }    
}


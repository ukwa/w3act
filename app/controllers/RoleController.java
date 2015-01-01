package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.Flag;
import models.Organisation;
import models.Permission;
import models.Role;
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
import views.html.roles.newForm;
import views.html.roles.list;
import views.html.roles.admin;
import views.html.roles.edit;
import views.html.roles.view;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage roles.
 */
@Security.Authenticated(SecuredController.class)
public class RoleController extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
    	Logger.debug("Roles.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.RoleController.list(0, "name", "asc", "")
        );
    
    /**
     * Display the role edit panel for this URL.
     */
    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		Role role = Role.findById(id);
		Form<Role> roleForm = Form.form(Role.class);
		roleForm = roleForm.fill(role);
		Map<String, Boolean> permissions = Permission.options(role.permissions);
      	return ok(
	              edit.render(roleForm, user, id, permissions)
	            );
    }
    
    public static Result view(Long id) {
        return ok(
                view.render(
                        Role.findById(id), User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * Administer roles
     * @param url
     * @return
     */
    public static Result admin(Long id) {
        return ok(
                admin.render(
                        Role.findById(id), User.findByEmail(request().username())
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
		Logger.debug("query: " + query);
		Logger.debug("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Role name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.RoleController.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.RoleController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}        
    }	   
    
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<Role> roleForm = Form.form(Role.class);
		Role role = new Role();
		roleForm = roleForm.fill(role);
		List<Permission> permissions = Permission.findAll();
        return ok(newForm.render(roleForm, user, permissions));
    	
    }
    public static Result info(Form<Role> form, Long id) {
    	User user = User.findByEmail(request().username());
    	Role role = Role.findById(id);
		Map<String, Boolean> permissions = Permission.options(role.permissions);
		return badRequest(edit.render(form, user, id, permissions));
    }
    
	public static Result newInfo(Form<Role> form) {
		User user = User.findByEmail(request().username());
		List<Permission> permissions = Permission.findAll();
        return badRequest(newForm.render(form, user, permissions));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Role> filledForm = form(Role.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
	            Map<String, String[]> formParams = request().body().asFormUrlEncoded();

	            String[] permissionValues = formParams.get("permissionsList");

	            List<Permission> newPermissions = new ArrayList<Permission>();
	            if (permissionValues != null) {
		            for(String permissionValue: permissionValues) {
		            	Long permissionsId = Long.valueOf(permissionValue);
		            	Permission permission = Permission.findById(permissionsId);
		            	newPermissions.add(permission);
		            }
		            filledForm.get().permissions = newPermissions;
	            }
	            
		        filledForm.get().save();
		        flash("message", "Role " + filledForm.get().name + " has been created");
		        return redirect(routes.RoleController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
	public static Result update(Long id) {

    	DynamicForm requestData = form().bindFromRequest();
        Form<Role> filledForm = form(Role.class).bindFromRequest();
    	Logger.debug("hasGlobalErrors: " + filledForm.hasGlobalErrors());
    	Logger.debug("hasErrors: " + filledForm.hasErrors());

    	String action = requestData.get("action");

        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        if (filledForm.hasErrors()) {
		        	Logger.debug("hasErrors: " + filledForm.errors());
		            return info(filledForm, id);
		        }        		
        		
	            Map<String, String[]> formParams = request().body().asFormUrlEncoded();

	            String[] permissionValues = formParams.get("permissionsList");

	            List<Permission> newPermissions = new ArrayList<Permission>();
	            if (permissionValues != null) {
		            for(String permissionValue: permissionValues) {
		            	Long permissionsId = Long.valueOf(permissionValue);
		            	Permission permission = Permission.findById(permissionsId);
		            	newPermissions.add(permission);
		            }
		            filledForm.get().permissions = newPermissions;
	            }
	            
		        filledForm.get().update(id);
		        flash("message", "Role " + filledForm.get().name + " has been updated");
		        return redirect(routes.RoleController.view(filledForm.get().id));
        	}
        	else if (action.equals("delete")) {
        		Role role = Role.findById(id);
		        flash("message", "Role " + role.name + " has been deleted");
		        role.delete();
		        return redirect(routes.RoleController.index());
        	}
        }
        return null;
	}	   
    
    public static Result saveAdmin() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
        		
		        Long roleId = Long.valueOf(requestData.get("id"));
		        Role role = Role.findById(roleId);
		        
		        Map<String, String[]> map = request().body().asFormUrlEncoded();
		        
		        // for adding
		        String[] unassignedUsers = map.get("unassigned");
		        if (unassignedUsers != null) {
			        for (String assign : unassignedUsers) {
				        Logger.debug("assign" + assign);
			        	Long permissionId = Long.valueOf(assign);
			        	Permission permission = Permission.findById(permissionId);
			        	if (permission != null) {
			        		role.permissions.add(permission);
			        	}
			        }
		        }
		        // for removing
		        String[] assignedUsers = map.get("assigned");
		        if (assignedUsers != null) {
			        for (String unassign : assignedUsers) {
				        Logger.debug("unassign: " + unassign);
			        	Long permissionId = Long.valueOf(unassign);
			        	Permission permission = Permission.findById(permissionId);
			        	if (permission != null) {
			        		role.permissions.remove(permission);
			        	}
			        }
		        }
		        role.save();
		        return redirect(routes.RoleController.admin(roleId));
    		}
		}
    	return null;
    }
	    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Role> roles = Role.filterByName(name);
	        jsonData = Json.toJson(roles);
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
    	Logger.debug("Roles.list()");
        return ok(
        	list.render(
        			"Roles", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Role.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }    
    
}


package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.Flag;
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
    	Logger.info("Roles.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.RoleController.list(0, "name", "asc", "")
        );
    
    /**
     * Display the role edit panel for this URL.
     */
    public static Result edit(Long id) {
		Role role = Role.findById(id);
		Form<Role> roleFormNew = Form.form(Role.class);
		roleFormNew = roleFormNew.fill(role);
      	return ok(
	              edit.render(roleFormNew, User.findByEmail(request().username()))
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
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Role name is empty. Please write name in search window.");
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
    		if (Const.ADDENTRY.equals(action)) {
    	    	Role role = new Role();
    	    	role.name = query;
    	        role.id = Target.createId();
    	        role.url = Const.ACT_URL + role.id;
    			Logger.info("add entry with url: " + role.url + ", and name: " + role.name);
    			Form<Role> roleFormNew = Form.form(Role.class);
    			roleFormNew = roleFormNew.fill(role);
    	      	return ok(
    		              edit.render(roleFormNew, User.findByEmail(request().username()))
    		            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.RoleController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}        
    }	   
    
    /**
     * Add new role entry.
     * @param role title
     * @return
     */
    public static Result create(String name) {
    	Role role = new Role();
    	role.name = name;
        role.id = Target.createId();
        role.url = Const.ACT_URL + role.id;
		Logger.info("add entry with url: " + role.url + ", and name: " + role.name);
		Form<Role> roleFormNew = Form.form(Role.class);
		roleFormNew = roleFormNew.fill(role);
      	return ok(
	              edit.render(roleFormNew, User.findByEmail(request().username()))
	            );
    }
      
//	/**
//	 * This method prepares Role form for sending info message
//	 * about errors 
//	 * @return edit page with form and info message
//	 */
//	public static Result info() {
//    	Role role = new Role();
//    	role.id = Long.valueOf(getFormParam(Const.ID));
//    	role.url = getFormParam(Const.URL);
//        role.name = getFormParam(Const.NAME);
//	    if (getFormParam(Const.DESCRIPTION) != null) {
//	    	role.description = getFormParam(Const.DESCRIPTION);
//	    }
//	    
//        String permissionStr = "";
//        List<Permission> permissionList = Permission.findAll();
//        Iterator<Permission> permissionItr = permissionList.iterator();
//        while (permissionItr.hasNext()) {
//        	Permission permission = permissionItr.next();
//            if (getFormParam(permission.name) != null) {
//                boolean permissionFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
//                if (permissionFlag) {
//                	if (permissionStr.length() == 0) {
//                		permissionStr = permission.name;
//                	} else {
//                		permissionStr = permissionStr + ", " + permission.name;
//                	}
//                }
//            }
//        }
//        if (permissionStr.length() == 0) {
//        	role.setPermissions(new ArrayList<Permission>());
//        } else {
//        	role.permissions(Permission.convertUrlsToObjects(permissionStr));
//        }
//        Logger.info("permissionStr: "+ permissionStr + ", role.permissions: " + role.getPermissionsMap().size());
//	    
//	    if (role.revision == null) {
//	    	role.revision = "";
//	    }
//        if (getFormParam(Const.REVISION) != null) {
//        	role.revision = getFormParam(Const.REVISION);
//        }
//		Form<Role> roleFormNew = Form.form(Role.class);
//		roleFormNew = roleFormNew.fill(role);
//      	return ok(
//	              roleedit.render(roleFormNew, User.findByEmail(request().username()))
//	            );
//    }
//    
    /**
     * This method saves new object or changes on given Role in the same object
     * completed by revision comment. The "version" field in the Role object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        
        User user = User.findByEmail(request().username());
    	Form<Role> roleForm = Form.form(Role.class).bindFromRequest();
    	
    	Logger.info("role data: " + roleForm.get());
        DynamicForm requestData = Form.form().bindFromRequest();
        
        String action = requestData.get("action");
        
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
	            if(roleForm.hasErrors()) {
	            	String missingFields = "";
	            	for (String key : roleForm.errors().keySet()) {
	            	    Logger.debug("key: " +  key);
	            	    if (missingFields.length() == 0) {
	            	    	missingFields = key;
	            	    } else {
	            	    	missingFields = missingFields + Const.COMMA + " " + key;
	            	    }
	            	}
	            	Logger.info("form errors size: " + roleForm.errors().size() + ", " + missingFields);
		  			flash("message", "Please fill out all the required fields, marked with a red star." + 
		  					" Missing fields are: " + missingFields);
		  			
		  			return ok(
		  	              edit.render(roleForm, user));
	            }
	            Role roleFromDB = Role.findById(roleForm.get().id);
	            Role roleFromForm = roleForm.get();

	            Map<String, String[]> formParams = request().body().asFormUrlEncoded();

	            roleFromDB.name = roleFromForm.name;
	            
	            String[] permissionValues = formParams.get("permissionsList");

	            List<Permission> newPermissions = new ArrayList<Permission>();
	            if (permissionValues != null) {
		            for(String permissionValue: permissionValues) {
		            	Long permissionsId = Long.valueOf(permissionValue);
		            	Permission permission = Permission.findById(permissionsId);
		            	newPermissions.add(permission);
		            }
		            roleFromDB.permissions = newPermissions;
	            }
	            
	            roleFromDB.description = roleFromForm.description;
	            roleFromDB.revision = roleFromForm.revision;
	            roleFromDB.save();
		        res = redirect(routes.RoleController.edit(roleFromDB.id));
        	}
        	else if (action.equals("delete")) {
	        	Role role = Role.findById(roleForm.get().id);
	        	role.delete();
		        res = redirect(routes.RoleController.index());
        	}
        }
        return res;
    }	   
    
    /**
     * This method implements administration for roles associated with particular organisation.
     * @return
     */
    public static Result saveAdmin() {
    	Result res = null;

        User user = User.findByEmail(request().username());
    	
        DynamicForm requestData = Form.form().bindFromRequest();
        
        String action = requestData.get("action");

        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
        		
                Long id = Long.valueOf(requestData.get("id"));
        		
	            Role roleFromDB = Role.findById(id);

	            Map<String, String[]> formParams = request().body().asFormUrlEncoded();
	            String[] permissionsList = formParams.get("permissionsList");
	            String[] notPermissionsList = formParams.get("notPermissionsList");

//	            List<Permission> newPermissions = new ArrayList<Permission>();
//	            if (permissionValues != null) {
//		            for(String permissionValue: permissionValues) {
//		            	Long permissionsId = Long.valueOf(permissionValue);
//		            	Permission permission = Permission.findById(permissionsId);
//		            	newPermissions.add(permission);
//		            }
//		            roleFromDB.permissions = newPermissions;
//	            }
		        res = redirect(routes.RoleController.admin(roleFromDB.id));
        	}
    	
    	
//    	
//               	String assignedPermissions = "";
//		        List<Permission> permissionList = Permission.findAll();
//		        Iterator<Permission> permissionItr = permissionList.iterator();
//		        while (permissionItr.hasNext()) {
//		        	Permission permission = permissionItr.next();
//	                if (getFormParam(permission.name) != null) {
//                		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
//		                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
//		                if (userFlag) {
//		                	if (assignedPermissions.length() == 0) {
//		                		assignedPermissions = permission.name;
//		                	} else {
//		                		assignedPermissions = assignedPermissions + Const.COMMA + " " + permission.name;
//		                	}
//		                }
//	                }
//		        }
//        		Logger.info("assignedPermissions: " + assignedPermissions);
////		        role.permissions = assignedPermissions;
//		        if (assignedPermissions.length() == 0) {
//		        	role.setPermissions(new ArrayList<Permission>());
//		        } else {
//		        	role.setPermissions(Permission.convertUrlsToObjects(assignedPermissions));
//		        }
        }
        return res;
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
    	Logger.info("Roles.list()");
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

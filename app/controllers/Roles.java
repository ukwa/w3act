package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import views.html.roles.roleadmin;
import views.html.roles.roleedit;
import views.html.roles.roleview;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage roles.
 */
@Security.Authenticated(Secured.class)
public class Roles extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
    	Logger.info("Roles.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.Roles.list(0, "name", "asc", "")
        );
    
    /**
     * Display the role edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("role url: " + url);
		Role role = Role.findByUrl(url);
		Logger.info("role name: " + role.name + ", url: " + url);
		Form<Role> roleFormNew = Form.form(Role.class);
		roleFormNew = roleFormNew.fill(role);
      	return ok(
	              roleedit.render(roleFormNew, User.findByEmail(request().username()))
	            );
    }
    
    public static Result view(String url) {
        return ok(
                roleview.render(
                        Role.findByUrl(url), User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * Administer roles
     * @param url
     * @return
     */
    public static Result admin(String url) {
        return ok(
                roleadmin.render(
                        Role.findByUrl(url), User.findByEmail(request().username())
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
	        		routes.Roles.list(0, "name", "asc", "")
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
    		              roleedit.render(roleFormNew, User.findByEmail(request().username()))
    		            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Roles.list(pageNo, sort, order, query));
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
	              roleedit.render(roleFormNew, User.findByEmail(request().username()))
	            );
    }
      
	/**
	 * This method prepares Role form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Role role = new Role();
    	role.id = Long.valueOf(getFormParam(Const.NID));
    	role.url = getFormParam(Const.URL);
        role.name = getFormParam(Const.NAME);
	    if (getFormParam(Const.DESCRIPTION) != null) {
	    	role.description = getFormParam(Const.DESCRIPTION);
	    }
	    
        String permissionStr = "";
        List<Permission> permissionList = Permission.findAll();
        Iterator<Permission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	Permission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
                boolean permissionFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (permissionFlag) {
                	if (permissionStr.length() == 0) {
                		permissionStr = permission.name;
                	} else {
                		permissionStr = permissionStr + ", " + permission.name;
                	}
                }
            }
        }
        if (permissionStr.length() == 0) {
        	role.setPermissions(new ArrayList<Permission>());
        } else {
        	role.setPermissions(Permission.convertUrlsToObjects(permissionStr));
        }
        Logger.info("permissionStr: "+ permissionStr + ", role.permissions: " + role.getPermissionsMap().size());
	    
	    if (role.revision == null) {
	    	role.revision = "";
	    }
        if (getFormParam(Const.REVISION) != null) {
        	role.revision = getFormParam(Const.REVISION);
        }
		Form<Role> roleFormNew = Form.form(Role.class);
		roleFormNew = roleFormNew.fill(role);
      	return ok(
	              roleedit.render(roleFormNew, User.findByEmail(request().username()))
	            );
    }
    
    /**
     * This method saves new object or changes on given Role in the same object
     * completed by revision comment. The "version" field in the Role object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.debug("save role: " + save);
        if (save != null) {
        	Logger.debug("save role nid: " + getFormParam(Const.NID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", revision: " + getFormParam(Const.REVISION));
        	Form<Role> roleForm = Form.form(Role.class).bindFromRequest();
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
	  			return info();
            }
        	Role role = null;
            boolean isExisting = true;
            try {
                try {
                	role = Role.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.debug("is not existing exception");
                	isExisting = false;
                	role = new Role();
                	role.id = Long.valueOf(getFormParam(Const.NID));
                	role.url = getFormParam(Const.URL);
                }
                if (role == null) {
                	Logger.debug("is not existing");
                	isExisting = false;
                	role = new Role();
                	role.id = Long.valueOf(getFormParam(Const.NID));
                	role.url = getFormParam(Const.URL);
                }
                
                role.name = getFormParam(Const.NAME);
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	role.description = getFormParam(Const.DESCRIPTION);
        	    }
        	    
                String permissionStr = "";
		        List<Permission> permissionList = Permission.findAll();
		        Iterator<Permission> permissionItr = permissionList.iterator();
		        while (permissionItr.hasNext()) {
		        	Permission permission = permissionItr.next();
	                if (getFormParam(permission.name) != null) {
		                boolean permissionFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
		                if (permissionFlag) {
		                	if (permissionStr.length() == 0) {
		                		permissionStr = permission.name;
		                	} else {
		                		permissionStr = permissionStr + ", " + permission.name;
		                	}
		                }
	                }
		        }
		        if (permissionStr.length() == 0) {
		        	role.setPermissions(new ArrayList<Permission>());
		        } else {
		        	role.setPermissions(Permission.convertUrlsToObjects(permissionStr));
		        }
		        Logger.info("permissionStr: "+ permissionStr + ", role.permissions: " + role.getPermissionsMap().size());
        	    
        	    if (role.revision == null) {
        	    	role.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	role.revision = getFormParam(Const.REVISION);
                }
            } catch (Exception e) {
            	Logger.info("Role not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(role);
    	        Logger.info("save role: " + role.toString());
        	} else {
           		Logger.info("update role: " + role.toString());
               	Ebean.update(role);
        	}
        	// update association to permissions
	        List<Permission> permissionList = Permission.findAll();
	        Iterator<Permission> permissionItr = permissionList.iterator();
	        while (permissionItr.hasNext()) {
	        	Permission permission = permissionItr.next();
	        	Logger.debug("Update role - permission: " + permission.toString() + ", role.permissions: " + role.getPermissionsMap().size());
                if (permission.name != null
                		&& Permission.isIncluded(permission.id, role.getPermissionsMap())) {
                	permission.role = role;
                	Ebean.update(permission);
                } else {
                	permission.role = null;
                	Ebean.update(permission);
                }
	        }
	        res = redirect(routes.Roles.edit(role.url));
        } 
        if (delete != null) {
        	Role role = Role.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(role);
	        res = redirect(routes.Roles.index()); 
        }
        return res;
    }	   
    
    /**
     * This method implements administration for roles associated with particular organisation.
     * @return
     */
    public static Result saveAdmin() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        if (save != null) {
        	Role role = null;
            try {
               	role = Role.findByUrl(getFormParam(Const.URL));
               	String assignedPermissions = "";
		        List<Permission> permissionList = Permission.findAll();
		        Iterator<Permission> permissionItr = permissionList.iterator();
		        while (permissionItr.hasNext()) {
		        	Permission permission = permissionItr.next();
	                if (getFormParam(permission.name) != null) {
                		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
		                if (userFlag) {
		                	if (assignedPermissions.length() == 0) {
		                		assignedPermissions = permission.name;
		                	} else {
		                		assignedPermissions = assignedPermissions + Const.COMMA + " " + permission.name;
		                	}
		                }
	                }
		        }
        		Logger.info("assignedPermissions: " + assignedPermissions);
//		        role.permissions = assignedPermissions;
		        if (assignedPermissions.length() == 0) {
		        	role.setPermissions(new ArrayList<Permission>());
		        } else {
		        	role.setPermissions(Permission.convertUrlsToObjects(assignedPermissions));
		        }
               	Ebean.update(role);
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
	        res = redirect(routes.Roles.admin(role.url));
        } else {
        	res = ok();
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
    
    /**
     * This method presents permissions as a human readable string.
     */
	public static String showPermissions(Long roleId) {
    	String res = "";
    	Role role = Role.findById(roleId);
    	if (role.getPermissionsMap() != null && role.getPermissionsMap().size() > 0) {
    		Iterator<Permission> itr = role.getPermissionsMap().iterator();
    		while (itr.hasNext()) {
    			Permission permission = itr.next();
    			if (res.length() == 0) {
    				res = permission.name;
    			} else {
    				res = res + Const.COMMA + " " + permission.name;
    			}
    		}
    	}
    	return res;
	}
	    
}


package controllers;

import static play.data.Form.form;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.Organisation;
import models.Role;
import models.Tag;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.PasswordHash;
import views.html.users.edit;
import views.html.users.newForm;
import views.html.users.list;
import views.html.users.view;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage curators.
 */
@Security.Authenticated(SecuredController.class)
public class UserController extends AbstractController {
  
    /**
     * Display the Curators.
     */
    public static Result index() {
    	Logger.debug("Curators.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.UserController.list(0, "name", "asc", "")
        );
    
    /**
     * Display the paginated list of Curators.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.debug("Curators.list() " + filter);
        return ok(
        	list.render(
        			"Curators", 
        			User.findByEmail(request().username()), 
        			filter, 
        			User.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }
    
    /**
     * Searching
     */
    public static Result search() {
    	
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.NAME);
		Logger.debug("query: " + query);
		Logger.debug("action: " + action);
		
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Curator's name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.UserController.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("add")) {
    			return redirect(
    	        		routes.UserController.newForm(query)
    			        );
    		} else if (action.equals("search")) {
    	    	return redirect(routes.UserController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<User> users = User.filterByName(name);
	        jsonData = Json.toJson(users);
        }
        return ok(jsonData);
    }
    
    public static Result view(Long id) {
    	User curator = User.findById(id);
    	User user = User.findByEmail(request().username());
    	List<Role> roles = Role.findAll();
    	List<Organisation> organisations = Organisation.findAll();
        return ok(view.render(curator, user, roles, organisations));
    }
    
    public static Result viewAct(String url) {
    	User curator = User.findByUrl(url);
    	User user = User.findByEmail(request().username());
    	List<Role> roles = Role.findAll();
    	List<Organisation> organisations = Organisation.findAll();
        return ok(view.render(curator, user, roles, organisations));
    }

    public static Result viewWct(String url) {
    	User curator = User.findByWct(url);
    	User user = User.findByEmail(request().username());
    	List<Role> roles = Role.findAll();
    	List<Organisation> organisations = Organisation.findAll();
        return ok(view.render(curator, user, roles, organisations));
    }
    
    public static Result newForm(String name) {
    	User user = User.findByEmail(request().username());
		Form<User> userForm = Form.form(User.class);
		User curator = new User();
		if (StringUtils.isNotBlank(name)) {
			curator.name = name;
		}
		userForm = userForm.fill(curator);
		List<Role> roles = Role.findAll();
		Map<String,String> organisations = Organisation.options();
        return ok(newForm.render(userForm, user, roles, organisations));
    }

    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		User curator = User.findById(id);
		Form<User> userForm = Form.form(User.class);
		userForm = userForm.fill(curator);
		List<Role> roles = Role.findAll();
		List<Role> curatorRoles = curator.roles;
		Map<String,String> organisations = Organisation.options();
		Logger.debug("roles: " + curator.roles.size());
        return ok(edit.render(userForm, user, id, roles, organisations, curatorRoles)); 
    }

    public static Result info(Form<User> form, Long id) {
    	User user = User.findByEmail(request().username());
		User curator = User.findById(id);
		List<Role> roles = Role.findAll();
		List<Role> curatorRoles = curator.roles;
		Map<String,String> organisations = Organisation.options();
		return badRequest(edit.render(form, user, id, roles, organisations, curatorRoles));
    }
    
	public static Result newInfo(Form<User> form) {
		User user = User.findByEmail(request().username());
		List<Role> roles = Role.findAll();
		Map<String,String> organisations = Organisation.options();		

		return badRequest(newForm.render(form, user, roles, organisations));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<User> filledForm = form(User.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
			    Map<String, String[]> formParams = request().body().asFormUrlEncoded();

		        List<Role> newRoles = new ArrayList<Role>();
		        
		        String[] roleValues = formParams.get("rolesList");

		        if (roleValues != null) {
		            for(String roleValue: roleValues) {
		            	Long roleId = Long.valueOf(roleValue);
		            	Role tag = Role.findById(roleId);
		            	newRoles.add(tag);
		            }
		            filledForm.get().roles = newRoles;
		        }		        

		        String password = requestData.get("password");
		    	
		        if (StringUtils.isEmpty(password)) {
		        	Logger.debug("The password field is empty.");
//		        	flash("message", "The password field is empty.");
		            ValidationError ve = new ValidationError("password", "The password field is empty.");
		            filledForm.reject(ve);
		            return newInfo(filledForm);
		        }
		        
	        	try {
	        		filledForm.get().password = PasswordHash.createHash(password);
	        	} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
		        	Logger.error("change password - no algorithm error: " + e);
		            ValidationError ve = new ValidationError("password", e.getMessage());
		            filledForm.reject(ve);
		            return newInfo(filledForm);
	        	}
		        
		        filledForm.get().save();
		        flash("message", "Curator " + filledForm.get().name + " has been created");
		        return redirect(routes.UserController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<User> filledForm = form(User.class).bindFromRequest();
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
		        
		    	String oldPassword = requestData.get("oldpassword");
		    	String newPassword = requestData.get("newpassword");

		    	Logger.debug(oldPassword + " " + newPassword);
		    	
	            if (StringUtils.isEmpty(newPassword) != StringUtils.isEmpty(oldPassword)) {
	            	Logger.debug("To change password, both password fields need to be filled in.");
//		  			flash("message", "The password field is empty.");
    	            ValidationError e = new ValidationError("password", "To change password, both password fields need to be filled in.");
    	            filledForm.reject(e);
		  			return info(filledForm, id);
	            } 

	            /**
	             * Change password only if both filled in
	             */                
	            if (StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(oldPassword)) {
			    	try {
			    		User dbUser = User.findById(id);
	                	String userDbPassword = dbUser.password;
	            		boolean isValidOldPassword = PasswordHash.validatePassword(oldPassword, userDbPassword);
	            		if (!isValidOldPassword) {
	                    	Logger.debug("The old password is not correct.");
	        	  			flash("message", "The old password is not correct.");
				  			return info(filledForm, id);
	        	  		} else {
	        	  			filledForm.get().password = PasswordHash.createHash(newPassword);
	        	  		}
					} catch (NoSuchAlgorithmException e) {
						Logger.debug("change password - no algorithm error: " + e);
					} catch (InvalidKeySpecException e) {
						Logger.debug("change password - key specification error: " + e);
					}
	    	    }		        
		        
			    Map<String, String[]> formParams = request().body().asFormUrlEncoded();

		        List<Role> newRoles = new ArrayList<Role>();
		        
		        String[] roleValues = formParams.get("rolesList");

		        if (roleValues != null) {
		            for(String roleValue: roleValues) {
		            	Long roleId = Long.valueOf(roleValue);
		            	Role tag = Role.findById(roleId);
		            	newRoles.add(tag);
		            }
		            filledForm.get().roles = newRoles;
		        }		        
	            
//    	    	if (getFormParam(Const.EMAIL).length() > 0 
//    			&& User.findByEmail(getFormParam(Const.EMAIL)) != null
//    			&& !getFormParam(Const.EMAIL).equals(user.email)) {
//    		String msg = "The given email '" + getFormParam(Const.EMAIL) + 
//        			"' already exists in database. Please give another email or use existing user.";
//        	Logger.debug(msg);
//  			flash("message", msg);
		        
//              if (getFormParam(Const.PASSWORD) == null || getFormParam(Const.PASSWORD).length() == 0) {
//            	Logger.debug("The password field is empty.");
//	  			flash("message", "The password field is empty.");
//	  			return info();
//            } else {
//    	    	user.password = getFormParam(Const.PASSWORD);
//		    	try {
//					user.password = PasswordHash.createHash(user.password);
//				} catch (NoSuchAlgorithmException e) {
//					Logger.debug("change password - no algorithm error: " + e);
//				} catch (InvalidKeySpecException e) {
//					Logger.debug("change password - key specification error: " + e);
//				}
		        
		        filledForm.get().update(id);
		        flash("message", "Curator " + filledForm.get().name + " has been updated");
		        return redirect(routes.UserController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		User curator = User.findById(id);
		        flash("message", "Curator " + filledForm.get().name + " has been deleted");
		        curator.delete();
            	
        		return redirect(routes.UserController.index()); 
        	}
        }
        return null;
    }
    
    public static Result sites(Long id) {
    	// user.targets?? or have to get again for paging
        return redirect(routes.TargetController.userTargets(0, "title", "asc", "", id, 0L, 0L));
    }
    
//    public static Result save() {
//    	Result res = null;
//
////        	    user.name = getFormParam(Const.NAME);
////        	    if (getFormParam(Const.EMAIL) != null) {
////        	    	try {
//
////	        	  			return info();
////	        	    	}
////        	    	} catch (Exception e) {
////        	    		Logger.debug("Given email is not yet in database");
////        	    	}
////        	    	user.email = getFormParam(Const.EMAIL);
////        	    }
////                if (getFormParam(Const.ORGANISATION) != null) {
////                	if (!getFormParam(Const.ORGANISATION).toLowerCase().contains(Const.NONE)) {
//////                		Logger.debug("organisation: " + getFormParam(Const.ORGANISATION));
////                		user.affiliation = Organisation.findByTitle(getFormParam(Const.ORGANISATION)).url;
////                		user.updateOrganisation();
////                	} else {
////                		user.affiliation = Const.NONE;
////                	}
////                }
////                String roleStr = "";
////		        List<Role> roleList = Role.findAll();
////		        Iterator<Role> roleItr = roleList.iterator();
////		        while (roleItr.hasNext()) {
////		        	Role role = roleItr.next();
////	                if (getFormParam(role.name) != null) {
////		                boolean roleFlag = Utils.getNormalizeBooleanString(getFormParam(role.name));
////		                if (roleFlag) {
////		                	if (roleStr.length() == 0) {
////		                		roleStr = role.name;
////		                	} else {
////		                		roleStr = roleStr + ", " + role.name;
////		                	}
////		                }
////	                }
////		        }
////                Utils.removeAssociationFromDb(Const.ROLE_USER, Const.ID + "_" + Const.USER, user.id);
////		        if (roleStr.length() == 0) {
////		        	user.roles = null;
////		        } else {
////		        	user.roles = Role.convertUrlsToObjects(roleStr);
////		        }
//////		        Logger.debug("roleStr: "+ roleStr + ", user.role_to_user size: " + user.role_to_user.size());
////                if (getFormParam(Const.REVISION) != null) {
////                	user.revision = getFormParam(Const.REVISION);
////                }
////            } catch (Exception e) {
////            	Logger.debug("User not existing exception");
////            }
////            
////        	if (!isExisting) {
//
////        	    }
////               	Ebean.save(user);
////    	        Logger.debug("save user: " + user.toString());
////        	} else {
////                if (!(getFormParam(Const.PASSWORD) == null || getFormParam(Const.PASSWORD).length() == 0
////                		|| getFormParam(Const.OLD_PASSWORD) == null || getFormParam(Const.OLD_PASSWORD).length() == 0)) {
////            		String oldInputPassword = getFormParam(Const.OLD_PASSWORD);
////            		user.password = getFormParam(Const.PASSWORD);
////			    	try {
////	                	String userDbPassword = User.findByUid(user.id).password;
////	            		boolean isValidOldPassword = PasswordHash.validatePassword(oldInputPassword, userDbPassword);
////	            		if (!isValidOldPassword) {
////	                    	Logger.debug("The old password is not correct.");
////	        	  			flash("message", "The old password is not correct.");
////	        	  			return info();	            		
////	        	  		} else {
////	        	  			user.password = PasswordHash.createHash(user.password);
////	        	  		}
////					} catch (NoSuchAlgorithmException e) {
////						Logger.debug("change password - no algorithm error: " + e);
////					} catch (InvalidKeySpecException e) {
////						Logger.debug("change password - key specification error: " + e);
////					}
////        	    }
////           		Logger.debug("update user: " + user.toString());
////                Ebean.update(user);
////        	}
////	        res = redirect(routes.Curators.edit(user.id));
////        } 
////        if (delete != null) {
////        	User user = User.findByUrl(getFormParam(Const.URL));
////        	Ebean.delete(user);
////	        res = redirect(routes.Curators.index()); 
////        }
//        return res;
//    }
    
    /**
     * This method checks if this User has a role passed by its id.
     * @param userId
     * @return true if exists
     */
    public static String showRoles(Long userId) {
    	String res = "";
    	User user = User.findById(userId);
    	if (user.roles != null && user.roles.size() > 0) {
    		Iterator<Role> itr = user.roles.iterator();
    		while (itr.hasNext()) {
    			Role role = itr.next();
    			if (res.length() == 0) {
    				res = role.name;
    			} else {
    				res = res + Const.COMMA + " " + role.name;
    			}
    		}
    	}
    	return res;
    }
        
}


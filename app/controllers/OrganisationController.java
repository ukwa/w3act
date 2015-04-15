package controllers;

import static play.data.Form.form;

import java.util.List;
import java.util.Map;

import models.Organisation;
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
import views.html.organisations.newForm;
import views.html.organisations.admin;
import views.html.organisations.edit;
import views.html.organisations.list;
import views.html.organisations.view;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage organisations.
 */
@Security.Authenticated(SecuredController.class)
public class OrganisationController extends AbstractController {
  
    /**
     * Display the organisations.
     */

    public static Result index() {
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.OrganisationController.list(0, "title", "asc", "")
        );

    /**
     * Searching
     */
    public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.QUERY);
		Logger.debug("query: " + query);
		Logger.debug("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Organisation name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.OrganisationController.list(0, "title", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.OrganisationController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }

    /**
     * filter for typeahead lookup
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Organisation> organisations = Organisation.filterByName(name);
	        jsonData = Json.toJson(organisations);
        }
        return ok(jsonData);
    }

    /**
     * Display the paginated list of Organisations.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on target urls
     */
    public static Result list(int pageNo, String sortBy, String order, String query) {
    	Logger.debug("Organisations.list() " + query);
        return ok(
        	list.render(
        			"Organisations", 
        			User.findByEmail(request().username()), 
        			query, 
        			Organisation.page(pageNo, 10, sortBy, order, query), 
        			sortBy, 
        			order)
        	);
    }

    public static Result view(Long id) {
    	Organisation organisation = Organisation.findById(id);
    	User user = User.findByEmail(request().username());
        return ok(view.render(organisation, user));
    }
    
    public static Result viewAct(String url) {
    	Organisation organisation = Organisation.findByUrl(url);
    	User user = User.findByEmail(request().username());
        return ok(view.render(organisation, user));
    }

    public static Result viewWct(String url) {
    	Organisation organisation = Organisation.findByWct(url);
    	User user = User.findByEmail(request().username());
        return ok(view.render(organisation, user));
    }

    public static Result sites(Long id) {
        return redirect(routes.TargetController.organisationTargets(0, Const.TITLE, Const.ASC, "", id));
    }
    
    /**
     * Administer users
     * @param url
     * @return
     */
    public static Result admin(Long id) {
    	Organisation organisation = Organisation.findById(id);
    	User user = User.findByEmail(request().username());
    	List<User> nonUsers = User.findByNotEqualOrganisation(organisation.id);
        return ok(admin.render(organisation, user, nonUsers));
    }

    public static Result newForm() {
        if(SecuredController.isSysAdmin(request().username())) {
	    	User user = User.findByEmail(request().username());
			Form<Organisation> organisationForm = Form.form(Organisation.class);
			Organisation organisation = new Organisation();
			organisationForm = organisationForm.fill(organisation);
	        return ok(newForm.render(organisationForm, user));
        } else {
        	return forbidden("Your do not have the right privileges to view this page");
        }
    }

    public static Result edit(Long id) {
        if(SecuredController.isSysAdmin(request().username())) {
	    	User user = User.findByEmail(request().username());
	    	Organisation organisation = Organisation.findById(id);
			Form<Organisation> organisationForm = Form.form(Organisation.class);
			organisationForm = organisationForm.fill(organisation);
	        return ok(edit.render(organisationForm, user, id));
        } else {
        	return forbidden("Your do not have the right privileges to view this page");
        }
    }

    public static Result info(Form<Organisation> form, Long id) {
    	User user = User.findByEmail(request().username());
		return badRequest(edit.render(form, user, id));
    }
    
	public static Result newInfo(Form<Organisation> form) {
		User user = User.findByEmail(request().username());
        return badRequest(newForm.render(form, user));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Organisation> filledForm = form(Organisation.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
		        filledForm.get().save();
		        flash("message", "Organisation " + filledForm.get().title + " has been created");
		        return redirect(routes.OrganisationController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<Organisation> filledForm = form(Organisation.class).bindFromRequest();
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
		        flash("message", "Organisation " + filledForm.get().title + " has been updated");
		        return redirect(routes.OrganisationController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		Organisation organisation = Organisation.findById(id);
		        flash("message", "Organisation " + filledForm.get().title + " has been deleted");
            	organisation.delete();
            	
        		return redirect(routes.OrganisationController.index()); 
        	}
        }
        return null;
    }
	
    /**
     * This method implements administration for users associated with particular organisation.
     * @return
     */
    public static Result saveAdmin() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
        		
		        Long organisationId = Long.valueOf(requestData.get("id"));
		        Organisation organisation = Organisation.findById(organisationId);
		        
		        Map<String, String[]> map = request().body().asFormUrlEncoded();
		        
		        // for adding
		        String[] unassignedUsers = map.get("unassignedUsers");
		        if (unassignedUsers != null) {
			        for (String assign : unassignedUsers) {
				        Logger.debug("assign" + assign);
			        	Long userId = Long.valueOf(assign);
			        	User user = User.findById(userId);
			        	if (user != null) {
			        		user.organisation = organisation;
			        		user.affiliation = organisation.url;
			        		user.save();
			        	}
			        }
		        }
		        // for removing
		        String[] assignedUsers = map.get("assignedUsers");
		        if (assignedUsers != null) {
			        for (String unassign : assignedUsers) {
				        Logger.debug("unassign: " + unassign);
			        	Long userId = Long.valueOf(unassign);
			        	User user = User.findById(userId);
			        	if (user != null) {
			        		user.organisation = null;
			        		user.affiliation = null;
			        		user.save();
			        	}
			        }
		        }
		        return redirect(routes.OrganisationController.admin(organisationId));
    		}
		}
    	return null;
    }
}

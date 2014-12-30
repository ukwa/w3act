package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.Organisation;
import models.User;

import org.apache.commons.lang3.BooleanUtils;
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
import views.html.organisations.newForm;
import views.html.organisations.admin;
import views.html.organisations.edit;
import views.html.organisations.list;
import views.html.organisations.view;

import com.avaje.ebean.Ebean;
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
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Organisation name is empty. Please write name in search window.");
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
    	Logger.info("Organisations.list() " + query);
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

    /**
     * Rename a organisation.
     */
    public static Result rename(Long organisation) {
        return ok(
            Organisation.rename(
                organisation, 
                form().bindFromRequest().get("title")
            )
        );
    }
    
    /**
     * Delete a organisation.
     */
    public static Result delete(Long organisation) {
        Organisation.find.ref(organisation).delete();
        return ok();
    }

    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<Organisation> organisationForm = Form.form(Organisation.class);
		Organisation organisation = new Organisation();
		organisationForm = organisationForm.fill(organisation);
        return ok(newForm.render(organisationForm, user));
    	
    }

    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
    	Organisation organisation = Organisation.findById(id);
		Form<Organisation> organisationForm = Form.form(Organisation.class);
		organisationForm = organisationForm.fill(organisation);
        return ok(edit.render(organisationForm, user, id));
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
     * This method checks if organisation object with given title already
     * exists in database.
     * @param newOrganisation
     * @return true if exists
     */
    public static boolean isInDb(Organisation newOrganisation) {
    	boolean res = true;
    	if (newOrganisation.title != null && newOrganisation.title.length() > 0) {
    		/**
    		 * Check if this organisation title already exists in database
    		 */
    		res = Organisation.existsByTitle(newOrganisation.title);
    	}
		Logger.info("isInDb() res: " + res);
        return res;
    }
    
    /**
     * This method ensures that no multiple organisations with the same name can be stored in 
     * database during the data import from the Drupal.
     * @return
     */
    public static List<Object> skipExistingObjects(List<Object> newOrganisations) {
    	List<Object> res = new ArrayList<Object>();
    	
    	/**
    	 * Iterate over all new organisations that supposed to be stored in database
    	 */
    	Iterator<Object> newItr = newOrganisations.iterator();
        while (newItr.hasNext()) {
        	Organisation newOrganisation = (Organisation) newItr.next();
        	boolean inDb = isInDb(newOrganisation);
       		if (!inDb) {
    			res.add(newOrganisation);
    		}
        }
        return res;
    }	   
    
    /**
     * This method checks if given user belongs to given organisation. This objects 
     * are linked by organisation URL.
     * @param user
     * @param organisation
     * @return
     */
    private static boolean isOrganisationLink(User user, Organisation organisation) {
    	boolean res = false;
		if (user.affiliation != null && organisation.url != null 
				&& user.affiliation.equals(organisation.url)) {
			res = true;
		} 
		return res;
	}
    
    /**
     * This method adds link to passed organisation in given User object if 
     * link does not already exists.
     * @param user
     * @param organisation
     */
    private static void addLink(User user, Organisation organisation) {
//		Logger.info("flag true add link: " + user.name);
    	if (!isOrganisationLink(user, organisation)) {
    		user.affiliation = organisation.url;
//    		user.updateOrganisation();
        	Ebean.update(user);
    	}
	}
    
    /**
     * This method removes link to passed organisation from given User object if 
     * link exists.
     * @param user
     * @param organisation
     */
    private static void removeLink(User user, Organisation organisation) {
//		Logger.info("flag false remove link: " + user.name);
    	if (isOrganisationLink(user, organisation)) {
    		Logger.info("remove link: " + user.name);
    		user.affiliation = "";
    		user.organisation = null;
        	Ebean.update(user);
    	}
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
		        String[] checkedVal = map.get("nonOrganisationUser");
		        for (String check : checkedVal) {
			        Logger.debug("check: " + check);
		        	Long userId = Long.valueOf(check);
		        	User user = User.findById(userId);
		        	if (map.get(user.name) != null) {
		        		boolean userFlag = BooleanUtils.toBoolean(map.get(user.name)[0]);
		        		if (userFlag) {
		                	addLink(user, organisation); 
		        		} else {
		                	removeLink(user, organisation); 
		        		}
		        	} else {
	                	removeLink(user, organisation); 	                	
		        	}
		        }
		        return redirect(routes.OrganisationController.admin(organisationId));
        	}
        }
        return null;
    }
}

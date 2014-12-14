package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import uk.bl.api.Utils;
import views.html.organisations.admin;
import views.html.organisations.edit;
import views.html.organisations.list;
import views.html.organisations.view;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage organisations.
 */
@Security.Authenticated(Secured.class)
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
    		if (Const.ADDENTRY.equals(action)) {
    	    	Organisation organisation = new Organisation();
    	    	organisation.title = query;
    			Logger.info("add with url: " + organisation.url + ", and title: " + organisation.title);
    			Form<Organisation> organisationForm = Form.form(Organisation.class);
    			organisationForm = organisationForm.fill(organisation);
    	        return ok(edit.render(organisationForm, User.findByEmail(request().username())));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.OrganisationController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }

    /**
     * Add an organisation.
     */
    public static Result create(String title) {
    	Organisation organisation = new Organisation();
    	organisation.title = title;
		Logger.info("add organisation with url: " + organisation.url + ", and title: " + organisation.title);
		Form<Organisation> organisationForm = Form.form(Organisation.class);
		organisationForm = organisationForm.fill(organisation);
        return ok(edit.render(organisationForm, User.findByEmail(request().username())));
    }
    
    /**
     * Display the organisation edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("organisation url: " + url);
		Organisation organisation = Organisation.findByUrl(url);
		Logger.info("organisation title: " + organisation.title + ", url: " + url);
		Form<Organisation> organisationForm = Form.form(Organisation.class);
		organisationForm = organisationForm.fill(organisation);
        return ok(edit.render(organisationForm, User.findByEmail(request().username())));

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

    public static Result sites(String url) {
        return redirect(routes.TargetController.organisationTargets(0, Const.TITLE, Const.ASC, "", url));
    }
    
    /**
     * Administer users
     * @param url
     * @return
     */
    public static Result admin(String url) {
        return ok(
                admin.render(
                        Organisation.findByUrl(url), User.findByEmail(request().username())
                )
            );
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

	/**
	 * This method prepares Organisation form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Organisation organisation = new Organisation();
    	organisation.id = Long.valueOf(getFormParam(Const.ID));
    	organisation.url = getFormParam(Const.URL);
    	organisation.title = getFormParam(Const.TITLE);
	    if (getFormParam(Const.FIELD_ABBREVIATION) != null) {
	    	organisation.field_abbreviation = getFormParam(Const.FIELD_ABBREVIATION);
	    }
	    if (getFormParam(Const.SUMMARY) != null) {
	    	organisation.summary = getFormParam(Const.SUMMARY);
	    }
	    if (organisation.revision == null) {
	    	organisation.revision = "";
	    }
	    if (getFormParam(Const.REVISION) != null) {
	    	organisation.revision = getFormParam(Const.REVISION);
	    }
		Form<Organisation> organisationFormNew = Form.form(Organisation.class);
		organisationFormNew = organisationFormNew.fill(organisation);
      	return ok(
	              edit.render(organisationFormNew, User.findByEmail(request().username()))
	            );
    }
    
    /**
     * This method saves new object or changes on given Organisation in the same object
     * completed by revision comment. The "version" field in the Organisation object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save organisation nid: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION) + 
        			", abbreviation: " + getFormParam(Const.FIELD_ABBREVIATION));
        	Form<Organisation> organisationForm = Form.form(Organisation.class).bindFromRequest();
            if(organisationForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : organisationForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + organisationForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
	  					" Missing fields are: " + missingFields);
	  			return info();
            }
        	
        	Organisation organisation = null;
            boolean isExisting = true;
            try {
                try {
                	if (Organisation.existsByUrl(getFormParam(Const.URL))) {
                		organisation = Organisation.findByUrl(getFormParam(Const.URL));
                	}
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	organisation = new Organisation();
                	organisation.id = Long.valueOf(getFormParam(Const.ID));
                	organisation.url = getFormParam(Const.URL);
                }
                if (organisation == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	organisation = new Organisation();
                	organisation.id = Long.valueOf(getFormParam(Const.ID));
                	organisation.url = getFormParam(Const.URL);
                }
                
//            	if (StringUtils.isBlank(getFormParam(Const.TITLE)) 
//            			|| StringUtils.isBlank(getFormParam(Const.FIELD_ABBREVIATION))) {
//                	Logger.info("title: " + getFormParam(Const.TITLE) + ", abbreviation: " + getFormParam(Const.FIELD_ABBREVIATION));
//                	Logger.info("Please fill out all the required fields, marked with a red star.");
//                    return ok(infomessage.render("Please fill out all the required fields, marked with a red star."));
//            	}    	
        	    if (getFormParam(Const.TITLE) != null) {
        	    	organisation.title = getFormParam(Const.TITLE);
        	    }
        	    if (getFormParam(Const.FIELD_ABBREVIATION) != null) {
        	    	organisation.field_abbreviation = getFormParam(Const.FIELD_ABBREVIATION);
        	    }
        	    if (getFormParam(Const.SUMMARY) != null) {
        	    	organisation.summary = getFormParam(Const.SUMMARY);
        	    }
        	    if (organisation.revision == null) {
        	    	organisation.revision = "";
        	    }
                if (getFormParam(Const.REVISION) != null) {
                	organisation.revision = getFormParam(Const.REVISION);
                }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
            
        	if (!isExisting) {
            	boolean inDb = isInDb(organisation);
                if (!inDb) {
	               	Ebean.save(organisation);
	    	        Logger.info("save organisation: " + organisation.toString());
                } else {
	    	        Logger.info("Organisation title already exists in database: " + organisation.toString());
        	        return redirect(routes.OrganisationController.index()); 
                }
        	} else {
           		Logger.info("update organisation: " + organisation.toString());
               	Ebean.update(organisation);
        	}
	        res = redirect(routes.OrganisationController.edit(organisation.url));
        } 
        if (delete != null) {
        	Organisation organisation = Organisation.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(organisation);
	        res = redirect(routes.OrganisationController.index()); 
        }
        return res;
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
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        if (save != null) {
        	Organisation organisation = null;
            try {
               	organisation = Organisation.findByUrl(getFormParam(Const.URL));
               	
		        List<User> userList = User.findAll();
		        Iterator<User> userItr = userList.iterator();
		        while (userItr.hasNext()) {
		        	User user = userItr.next();
	                if (getFormParam(user.name) != null) {
//                		Logger.info("getFormParam(user.name): " + getFormParam(user.name) + " " + user.name);
		                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(user.name));
		                if (userFlag) {
		                	addLink(user, organisation); 
		                } else {
		                	removeLink(user, organisation); 
		                }
	                } else {
	                	removeLink(user, organisation); 	                	
	                }
		        }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
	        res = redirect(routes.OrganisationController.admin(organisation.url));
        } else {
        	res = ok();
        }
        return res;
    }
}

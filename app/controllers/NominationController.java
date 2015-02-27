package controllers;

import static play.data.Form.form;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.Nomination;
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
import uk.bl.api.PatchedForm;
import uk.bl.api.Utils;
import views.html.licence.ukwalicenceresult;
import views.html.nominations.newForm;
import views.html.nominations.edit;
import views.html.nominations.list;
import views.html.nominations.nominationform;
import views.html.nominations.view;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Manage nominations.
 */
@Security.Authenticated(SecuredController.class)
public class NominationController extends AbstractController {
  
    final static Form<Nomination> nominationForm = new PatchedForm<Nomination>(Nomination.class).bindFromRequest();
    
    /**
     * Display the nomination.
     */
    public static Result index() {
    	Logger.debug("Nominations.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.NominationController.list(0, "name", "asc", "")
        );
    
    public static Result view(Long id) {
        return ok(
                view.render(
                        Nomination.findById(id), User.findByEmail(request().username())
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
			Logger.debug("Nomination name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.NominationController.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    	    	Nomination nomination = new Nomination();
    	    	nomination.name = query;
    			Logger.debug("add nomination name: " + nomination.name);
    			Form<Nomination> nominationFormNew = Form.form(Nomination.class);
    			nominationFormNew = nominationFormNew.fill(nomination);
    			User user = User.findByEmail(request().username());
    	      	return ok(
    		              newForm.render(nominationFormNew, user)
    		            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.NominationController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}               
        
    }	   
    
    /**
     * This method loads new Nomination object using POST RESTful interface.
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result load() {
    	Logger.debug("nomination load()");
        JsonNode json = request().body().asJson();
    	Logger.debug("nomination load() json: " + json);
        ObjectNode result = Json.newObject();
    	Logger.debug("nomination load() result: " + result);
        String name = json.findPath(Const.NAME).textValue();
    	Logger.debug("load nomination name: " + name);
        if(name == null) {
            result.put("status", "Not OK");
            result.put("message", "Missing parameter [name]");
            return badRequest(result);
        } else {
            result.put("status", "OK");
            result.put("message", "Nomination " + name);
            Logger.debug("load nomination: " + name);
            Logger.debug("current nominations count for given name: " + Nomination.filterByName(name).size());
            String title = json.findPath(Const.TITLE).textValue();
            Logger.debug("load nomination title: " + title);
            String websiteUrl = json.findPath(Const.WEBSITE_URL).textValue();
            Logger.debug("load nomination website_url: " + websiteUrl);
            String email = json.findPath(Const.EMAIL).textValue();
            Logger.debug("load nomination email: " + email);
            String tel = json.findPath(Const.TEL).textValue();
            Logger.debug("load nomination tel: " + tel);
            String address = json.findPath(Const.ADDRESS).textValue();
            Logger.debug("load nomination address: " + address);
            boolean nominatedWebsiteOwner = json.findPath(Const.NOMINATED_WEBSITE_OWNER).booleanValue();
            Logger.debug("load nomination nominated_website_owner: " + nominatedWebsiteOwner);
            String justification = json.findPath(Const.JUSTIFICATION).textValue();
            Logger.debug("load nomination justification: " + justification);
            String notes = json.findPath(Const.NOTES).textValue();
            Logger.debug("load nomination notes: " + notes);
            String nominationDate = json.findPath(Const.NOMINATION_DATE).textValue();
            Logger.debug("load nomination nomination_date: " + nominationDate);
            Nomination nomination = new Nomination();
            nomination.name = name;
            nomination.title = title;
            nomination.websiteUrl = websiteUrl;
            nomination.email = email;
            nomination.tel = tel;
            nomination.address = address;
            nomination.nominatedWebsiteOwner = nominatedWebsiteOwner;
            nomination.justification = justification;
            nomination.notes = notes;
            // TODO: UNIX DATE
//            nomination.nominationDate = nominationDate;
            nomination.save();
	        Logger.debug("saved nomination: " + nomination.toString());
            Logger.debug("new nominations count for given name: " + Nomination.filterByName(name).size());
            return ok(result);
        }
    }
    
    public static Result newForm() {
		Nomination nomination = new Nomination();
		Form<Nomination> nominationForm = Form.form(Nomination.class);
		nominationForm = nominationForm.fill(nomination);
      	return ok(newForm.render(nominationForm, User.findByEmail(request().username())));
    }
    
    public static Result edit(Long id) {
		Nomination nomination = Nomination.findById(id);
		Form<Nomination> nominationForm = Form.form(Nomination.class);
		nominationForm = nominationForm.fill(nomination);
      	return ok(edit.render(nominationForm, User.findByEmail(request().username()), id));
    }

    
    public static Result update(Long id) {
        Form<Nomination> filledForm = form(Nomination.class).bindFromRequest();
        User user = User.findByEmail(request().username());
        if (filledForm.hasErrors()) {
            return badRequest(edit.render(filledForm, user, id));
        }
        String nomDate = filledForm.get().nominationDateText;
    	if (StringUtils.isNotEmpty(nomDate)) {
			try {
				Date date = Utils.INSTANCE.convertDate(nomDate);
				filledForm.get().nominationDate = date;
			} catch (ParseException e) {
	  			flash("message", "Nomination Date (dd-mm-yy) - Incorrect Format");
	            return badRequest(newForm.render(filledForm, user));
			}
    	}
        filledForm.get().update(id);
        flash("success", "Nomination " + filledForm.get().name + " has been updated");
    	return redirect(routes.NominationController.view(filledForm.get().id));
    }
    
    /**
     * This method saves new object or changes on given Nomination in the same object
     * completed by revision comment. The "version" field in the Nomination object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");
    	User user = User.findByEmail(request().username());
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {    
		        Form<Nomination> filledForm = form(Nomination.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
		            return badRequest(newForm.render(filledForm, user));
		        }

                String nomDate = filledForm.get().nominationDateText;
            	if (StringUtils.isNotEmpty(nomDate)) {
        			try {
    					Date date = Utils.INSTANCE.convertDate(nomDate);
    					filledForm.get().nominationDate = date;
    				} catch (ParseException e) {
    		  			flash("message", "Nomination Date (dd-mm-yy) - Incorrect Format");
    		            return badRequest(newForm.render(filledForm, user));
    				}
            	}
		        filledForm.get().save();
		        flash("success", "Nomination " + filledForm.get().name + " has been created");
		    	return redirect(routes.NominationController.view(filledForm.get().id));
            	
        	} else if(action.equals("delete")) {
            	Form<Nomination> filledForm = nominationForm.bindFromRequest();
            	Nomination nomination = Nomination.findById(filledForm.get().id);
            	nomination.delete();
    	        return redirect(routes.NominationController.index()); 
        	}
        }
        return null;
    }



    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Nomination> nominations = Nomination.filterByName(name);
	        jsonData = Json.toJson(nominations);
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
    	Logger.debug("Nominations.list()");
        return ok(
        	list.render(
        			"Nominations", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Nomination.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }  
    
    /**
     * This method presents licence form for selected nomination request
     * that is identified by the given permission URL. 
     * @param permissionUrl
     * @return
     */
    public static Result formpage() {
		return ok(
            nominationform.render()
        );
    }
    
    public static String getCurrentDate() {
    	return Utils.INSTANCE.getCurrentDate();
    }
    
    /**
     * This method submits owner settings for UKWA licence.
     * @return
     */
    public static Result submit() {
    	Result res = null;
        Logger.debug("Nomination submit()");
        String submit = getFormParam(Const.SUBMIT);
        Logger.debug("submit: " + submit);
        if (submit != null) {
        	Logger.debug("save UKWA licence - name: " + getFormParam(Const.NAME));
    		Logger.debug("agree: " + getFormParam(Const.AGREE));
            boolean isAgreed = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(Const.AGREE));
        	Logger.debug("flags isAgreed: " + isAgreed );
            if (getFormParam(Const.WEBSITE_URL) != null) {
        	    String target = getFormParam(Const.WEBSITE_URL);
        	    Nomination nomination = new Nomination();
                nomination.id = Utils.INSTANCE.createId();
                nomination.url = Const.ACT_URL + nomination.id;
        		Logger.debug("add nomination with url: " + nomination.url + ", and name: " + nomination.name);
            	nomination.websiteUrl = target;
                if (getFormParam(Const.NAME) != null) {
                	nomination.name = getFormParam(Const.NAME);
                }
                if (getFormParam(Const.TITLE) != null) {
                	nomination.title = getFormParam(Const.TITLE);
                }
                if (getFormParam(Const.DESCRIPTION) != null) {
                	nomination.notes = getFormParam(Const.DESCRIPTION);
                }
                if (getFormParam(Const.LOG_DATE) != null) {
                	// TODO: UNIX DATE
//                	nomination.nominationDate = getFormParam(Const.LOG_DATE);
                }
                if (isAgreed) {
//                        if (isAgreed && noThirdPartyContent && mayPublish) {
                	nomination.justification = Const.CrawlPermissionStatus.GRANTED.name();
                } else {
                	nomination.justification = Const.CrawlPermissionStatus.REFUSED.name();
                }
                if (getFormParam(Const.POSITION) != null) {
                	nomination.address = getFormParam(Const.POSITION);
                }
                if (getFormParam(Const.EMAIL) != null) {
                	nomination.email = getFormParam(Const.EMAIL);
                }
                if (getFormParam(Const.PHONE) != null) {
                	nomination.tel = getFormParam(Const.PHONE);
                }                
            	Ebean.save(nomination);
            } 
            // contact person
	        res = redirect(routes.NominationController.result());
        } 
        return res;
    }	   
	
    /**
     * Display the result of the licence form submission.
     */
    public static Result result() {
		return ok(
            ukwalicenceresult.render()
        );
    }
        
}


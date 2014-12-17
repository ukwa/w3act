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
import uk.bl.api.Utils;
import views.html.licence.ukwalicenceresult;
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
  
    final static Form<Nomination> nominationForm = play.data.Form.form(Nomination.class);

    /**
     * Display the nomination.
     */
    public static Result index() {
    	Logger.info("Nominations.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.NominationController.list(0, "name", "asc", "")
        );
    
    /**
     * Display the nomination edit panel for this URL.
     */
    public static Result edit(Long id) {
		Nomination nomination = Nomination.findById(id);
		Form<Nomination> nominationForm = Form.form(Nomination.class);
		nominationForm = nominationForm.fill(nomination);
      	return ok(edit.render(nominationForm, User.findByEmail(request().username())));
    }
    
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
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Nomination name is empty. Please write name in search window.");
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
    	        nomination.id = Utils.createId();
    	        nomination.url = Const.ACT_URL + nomination.id;
    			Logger.info("add nomination with url: " + nomination.url + ", and name: " + nomination.name);
    			Form<Nomination> nominationFormNew = Form.form(Nomination.class);
    			nominationFormNew = nominationFormNew.fill(nomination);
    	      	return ok(
    		              edit.render(nominationFormNew, User.findByEmail(request().username()))
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
     * Add new nomination entry.
     * @param nomination title
     * @return
     */
    public static Result create(String name) {
    	Nomination nomination = new Nomination();
    	nomination.name = name;
        nomination.id = Utils.createId();
        nomination.url = Const.ACT_URL + nomination.id;
		Logger.info("add nomination with url: " + nomination.url + ", and name: " + nomination.name);
		Form<Nomination> nominationFormNew = Form.form(Nomination.class);
		nominationFormNew = nominationFormNew.fill(nomination);
      	return ok(
	              edit.render(nominationFormNew, User.findByEmail(request().username()))
	            );
    }
    
    /**
     * This method loads new Nomination object using POST RESTful interface.
     * @return
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result load() {
    	Logger.info("nomination load()");
        JsonNode json = request().body().asJson();
    	Logger.info("nomination load() json: " + json);
        ObjectNode result = Json.newObject();
    	Logger.info("nomination load() result: " + result);
        String name = json.findPath(Const.NAME).textValue();
    	Logger.info("load nomination name: " + name);
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
            Logger.info("load nomination title: " + title);
            String websiteUrl = json.findPath(Const.WEBSITE_URL).textValue();
            Logger.info("load nomination website_url: " + websiteUrl);
            String email = json.findPath(Const.EMAIL).textValue();
            Logger.info("load nomination email: " + email);
            String tel = json.findPath(Const.TEL).textValue();
            Logger.info("load nomination tel: " + tel);
            String address = json.findPath(Const.ADDRESS).textValue();
            Logger.info("load nomination address: " + address);
            boolean nominatedWebsiteOwner = json.findPath(Const.NOMINATED_WEBSITE_OWNER).booleanValue();
            Logger.info("load nomination nominated_website_owner: " + nominatedWebsiteOwner);
            String justification = json.findPath(Const.JUSTIFICATION).textValue();
            Logger.info("load nomination justification: " + justification);
            String notes = json.findPath(Const.NOTES).textValue();
            Logger.info("load nomination notes: " + notes);
            String nominationDate = json.findPath(Const.NOMINATION_DATE).textValue();
            Logger.info("load nomination nomination_date: " + nominationDate);
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
	        Logger.info("saved nomination: " + nomination.toString());
            Logger.debug("new nominations count for given name: " + Nomination.filterByName(name).size());
            return ok(result);
        }
    }
    
    /**
     * This method saves new object or changes on given Nomination in the same object
     * completed by revision comment. The "version" field in the Nomination object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {        	
            	Form<Nomination> filledForm = nominationForm.bindFromRequest();
            	User user = User.findByEmail(request().username());
	            if(filledForm.hasErrors()) {
	            	String missingFields = "";
	            	for (String key : filledForm.errors().keySet()) {
	            	    Logger.info("key: " +  key);
	            	    if (missingFields.length() == 0) {
	            	    	missingFields = key;
	            	    } else {
	            	    	missingFields = missingFields + ", " + key;
	            	    }
	            	}
	            	Logger.info("form errors size: " + filledForm.errors().size() + ", " + missingFields);
		  			flash("message", "Please fill out all the required fields, marked with a red star." + 
		  					" Missing fields are: " + missingFields);
		            return badRequest(edit.render(filledForm, user));

//		  	      	return ok(edit.render(filledForm, user));
	            }
	            
	            Nomination nominationFromForm = filledForm.get();
	            Long id = Long.valueOf(nominationFromForm.id);

	            Nomination nominationFromDB = Nomination.findById(id);
	            if (nominationFromDB == null) {
	            	nominationFromDB = new Nomination();
	            }
	            nominationFromDB.name = nominationFromForm.name;
	            nominationFromDB.title = nominationFromForm.title;
	            nominationFromDB.websiteUrl = nominationFromForm.websiteUrl;
	            nominationFromDB.email = nominationFromForm.email;
	            nominationFromDB.tel = nominationFromForm.tel;
	            nominationFromDB.address = nominationFromForm.address;
	            nominationFromDB.notes = nominationFromForm.notes;
    	    	nominationFromDB.justification = nominationFromForm.justification;
	    	    
                String nomDate = requestData.get("nomDate");
            	if (StringUtils.isNotEmpty(nomDate)) {
        			DateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        			try {
    					Date date = formatter.parse(nomDate);
    					nominationFromDB.nominationDate = date;
    				} catch (ParseException e) {
    		  			flash("message", "Nomination Date (dd-mm-yy) - Incorrect Format");
    					return ok(edit.render(filledForm, user)); 
    				}
            	}
    	    	nominationFromDB.nominatedWebsiteOwner = nominationFromForm.nominatedWebsiteOwner;
    	    	nominationFromDB.nominationChecked = nominationFromForm.nominationChecked;
    	    	nominationFromDB.save();
    	    	res = redirect(routes.NominationController.view(nominationFromDB.id));
        	} else if(action.equals("delete")) {
            	Form<Nomination> filledForm = nominationForm.bindFromRequest();
            	Nomination nomination = Nomination.findById(filledForm.get().id);
            	nomination.delete();
    	        res = redirect(routes.NominationController.index()); 
        	}
        } 
        return res;
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
    	Logger.info("Nominations.list()");
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
    	return Utils.getCurrentDate();
    }
    
    /**
     * This method submits owner settings for UKWA licence.
     * @return
     */
    public static Result submit() {
    	Result res = null;
        Logger.info("Nomination submit()");
        String submit = getFormParam(Const.SUBMIT);
        Logger.info("submit: " + submit);
        if (submit != null) {
        	Logger.info("save UKWA licence - name: " + getFormParam(Const.NAME));
    		Logger.info("agree: " + getFormParam(Const.AGREE));
            boolean isAgreed = Utils.getNormalizeBooleanString(getFormParam(Const.AGREE));
        	Logger.info("flags isAgreed: " + isAgreed );
            if (getFormParam(Const.WEBSITE_URL) != null) {
        	    String target = getFormParam(Const.WEBSITE_URL);
        	    Nomination nomination = new Nomination();
                nomination.id = Utils.createId();
                nomination.url = Const.ACT_URL + nomination.id;
        		Logger.info("add nomination with url: " + nomination.url + ", and name: " + nomination.name);
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


package controllers;

import static play.data.Form.form;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.CommunicationLog;
import models.ContactPerson;
import models.CrawlPermission;
import models.Nomination;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.licence.ukwalicence;
import views.html.licence.ukwalicenceresult;
import views.html.licence.ukwalicenceview;
import views.html.nominations.*;

/**
 * Manage nominations.
 */
@Security.Authenticated(Secured.class)
public class Nominations extends AbstractController {
  
    /**
     * Display the nomination.
     */
    public static Result index() {
    	Logger.info("Nominations.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.Nominations.list(0, "name", "asc", "")
        );
    
    /**
     * Display the nomination edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("nomination url: " + url);
		Nomination nomination = Nomination.findByUrl(url);
		Logger.info("nomination name: " + nomination.name + ", url: " + url);
        return ok(
                edit.render(
                        Nomination.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                view.render(
                        Nomination.findByUrl(url), User.find.byId(request().username())
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
	        		routes.Nominations.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		return redirect(routes.Nominations.create(query));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Nominations.list(pageNo, sort, order, query));
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
        return ok(
                edit.render(
                      nomination, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given Nomination in the same object
     * completed by revision comment. The "version" field in the Nomination object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save nomination id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", revision: " + getFormParam(Const.REVISION));
        	Nomination nomination = null;
            boolean isExisting = true;
            try {
                try {
                	nomination = Nomination.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	nomination = new Nomination();
                	nomination.id = Long.valueOf(getFormParam(Const.ID));
                	nomination.url = getFormParam(Const.URL);
                }
                if (nomination == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	nomination = new Nomination();
                	nomination.id = Long.valueOf(getFormParam(Const.ID));
                	nomination.url = getFormParam(Const.URL);
                }
                
                nomination.name = getFormParam(Const.NAME);
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	nomination.notes = getFormParam(Const.DESCRIPTION);
        	    }
            } catch (Exception e) {
            	Logger.info("Nomination not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(nomination);
    	        Logger.info("save nomination: " + nomination.toString());
        	} else {
           		Logger.info("update nomination: " + nomination.toString());
               	Ebean.update(nomination);
        	}
	        res = redirect(routes.Nominations.view(nomination.url));
        } 
        if (delete != null) {
        	Nomination nomination = Nomination.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(nomination);
	        res = redirect(routes.Nominations.index()); 
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
        			User.find.byId(request().username()), 
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
            if (getFormParam(Const.TARGET) != null) {
        	    String target = getFormParam(Const.TARGET);
        	    Nomination nomination = new Nomination();
                nomination.id = Utils.createId();
                nomination.url = Const.ACT_URL + nomination.id;
        		Logger.info("add nomination with url: " + nomination.url + ", and name: " + nomination.name);
            	nomination.website_url = target;
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
                	nomination.nomination_date = getFormParam(Const.LOG_DATE);
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
	        res = redirect(routes.Nominations.result());
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


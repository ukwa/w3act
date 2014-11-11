package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.PermissionRefusal;
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
import views.html.refusals.edit;
import views.html.refusals.refusals;
import views.html.refusals.view;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage refusals.
 */
@Security.Authenticated(Secured.class)
public class PermissionRefusals extends AbstractController {
  
    /**
     * Display the refusal.
     */
    public static Result index() {
        List<PermissionRefusal> resList = processFilterPermissionRefusals("");
        return ok(
                refusals.render(
                    "PermissionRefusals", User.findByEmail(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the refusal edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("refusal url: " + url);
		PermissionRefusal refusal = PermissionRefusal.findByUrl(url);
		Logger.info("refusal name: " + refusal.name + ", url: " + url);
		Form<PermissionRefusal> refusalFormNew = Form.form(PermissionRefusal.class);
		refusalFormNew = refusalFormNew.fill(refusal);
      	return ok(
	              edit.render(refusalFormNew, User.findByEmail(request().username()))
	            );
    }
    
    public static Result view(String url) {
        return ok(
                view.render(
                		models.PermissionRefusal.findByUrl(url), User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	Result res = null;
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.URL);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
        List<PermissionRefusal> resList = processFilterPermissionRefusals(query);
    	if (StringUtils.isBlank(query)) {
			Logger.info("Flag name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
            res = ok(
            		refusals.render(
                        "PermissionRefusals", User.findByEmail(request().username()), resList, ""
                    )
                );
    	}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    	    	PermissionRefusal refusal = new PermissionRefusal();
    	    	refusal.name = query;
    	        refusal.id = Target.createId();
    	        refusal.url = Const.ACT_URL + refusal.id;
    			Logger.info("add entry with url: " + refusal.url + ", and name: " + refusal.name);
    			Form<PermissionRefusal> refusalFormNew = Form.form(PermissionRefusal.class);
    			refusalFormNew = refusalFormNew.fill(refusal);
    	      	return ok(
    		              edit.render(refusalFormNew, User.findByEmail(request().username()))
    		            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
                res = ok(
                		refusals.render(
                            "PermissionRefusals", User.findByEmail(request().username()), resList, query
                        )
                    );
		    } else {
		        return badRequest("This action is not allowed");
		    }
    	}      
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl refusals.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<PermissionRefusal> processFilterPermissionRefusals(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<PermissionRefusal> exp = PermissionRefusal.find.where();
    	List<PermissionRefusal> res = new ArrayList<PermissionRefusal>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.PermissionRefusal.findAll();
    	}
        return res;
    }
        
    /**
     * Add new refusal entry.
     * @param refusal title
     * @return
     */
    public static Result create(String name) {
    	PermissionRefusal refusal = new PermissionRefusal();
    	refusal.name = name;
        refusal.id = Target.createId();
        refusal.url = Const.ACT_URL + refusal.id;
		Logger.info("add permission refusal with url: " + refusal.url + ", and name: " + refusal.name);
		Form<PermissionRefusal> refusalFormNew = Form.form(PermissionRefusal.class);
		refusalFormNew = refusalFormNew.fill(refusal);
      	return ok(
	              edit.render(refusalFormNew, User.findByEmail(request().username()))
	            );
    }
      
	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	PermissionRefusal refusal = new PermissionRefusal();
    	refusal.id = Long.valueOf(getFormParam(Const.ID));
    	refusal.url = getFormParam(Const.URL);
	    if (getFormParam(Const.NAME) != null) {
	    	refusal.name = getFormParam(Const.NAME);
	    }
	    if (getFormParam(Const.REFUSAL_DATE) != null) {
	    	refusal.date = getFormParam(Const.REFUSAL_DATE);
	    }
	    if (getFormParam(Const.TYPE) != null) {
	    	refusal.ttype = getFormParam(Const.TYPE);
	    }
	    if (getFormParam(Const.REASON) != null) {
	    	refusal.reason = getFormParam(Const.REASON);
	    }    	
		Form<PermissionRefusal> refusalFormNew = Form.form(PermissionRefusal.class);
		refusalFormNew = refusalFormNew.fill(refusal);
      	return ok(
	              edit.render(refusalFormNew, User.findByEmail(request().username()))
	            );
    }
    
    /**
     * This method saves new object or changes on given refusal in the same object
     * completed by revision comment. The "version" field in the refusal object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save refusal id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	Form<PermissionRefusal> refusalForm = Form.form(PermissionRefusal.class).bindFromRequest();
            if(refusalForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : refusalForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + refusalForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
	  					" Missing fields are: " + missingFields);
	  			return info();
            }
        	PermissionRefusal refusal = null;
            boolean isExisting = true;
            try {
                try {
                	refusal = PermissionRefusal.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	refusal = new PermissionRefusal();
                	refusal.id = Long.valueOf(getFormParam(Const.ID));
                	refusal.url = getFormParam(Const.URL);
                }
                if (refusal == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	refusal = new PermissionRefusal();
                	refusal.id = Long.valueOf(getFormParam(Const.ID));
                	refusal.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.NAME) != null) {
        	    	refusal.name = getFormParam(Const.NAME);
        	    }
        	    if (getFormParam(Const.REFUSAL_DATE) != null) {
        	    	refusal.date = getFormParam(Const.REFUSAL_DATE);
        	    }
        	    if (getFormParam(Const.TYPE) != null) {
        	    	refusal.ttype = getFormParam(Const.TYPE);
        	    }
        	    if (getFormParam(Const.REASON) != null) {
        	    	refusal.reason = getFormParam(Const.REASON);
        	    }
            } catch (Exception e) {
            	Logger.info("PermissionRefusal not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(refusal);
    	        Logger.info("save refusal: " + refusal.toString());
        	} else {
           		Logger.info("update refusal: " + refusal.toString());
               	Ebean.update(refusal);
        	}
	        return redirect(routes.PermissionRefusals.edit(refusal.url));
        } 
        if (delete != null) {
        	PermissionRefusal refusal = PermissionRefusal.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(refusal);
	        res = redirect(routes.PermissionRefusals.index()); 
        }
    	res = redirect(routes.PermissionRefusals.index()); 
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<PermissionRefusal> refusals = PermissionRefusal.filterByName(name);
	        jsonData = Json.toJson(refusals);
        }
        return ok(jsonData);
    }
}


package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.CommunicationLog;
import models.Target;
import models.User;
import models.CrawlPermission;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.communicationlogs.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage logs.
 */
@Security.Authenticated(SecuredController.class)
public class CommunicationLogController extends AbstractController {
  
    /**
     * Display the log.
     */
    public static Result index() {
        List<CommunicationLog> resList = CommunicationLog.findAll();
        return ok(
        		logs.render(
                    "CommunicationLogs", User.findByEmail(request().username()), resList, "", ""
                )
            );
    }

    /**
     * Display the log edit panel for this URL.
     */
    public static Result edit(Long id) {
		CommunicationLog log = CommunicationLog.findById(id);
		Form<CommunicationLog> communicationLogForm = Form.form(CommunicationLog.class);
		communicationLogForm = communicationLogForm.fill(log);
		
		Map<String,String> communicationLogTypes = CommunicationLog.options();
		Map<String,String> crawlPermissions = CrawlPermission.options();
      	return ok(
	              newForm.render(communicationLogForm, User.findByEmail(request().username()), communicationLogTypes, crawlPermissions)
	            );
    }
    
    public static Result view(Long id) {
        return ok(
                view.render(
                		models.CommunicationLog.findById(id), User.findByEmail(request().username())
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
    	Logger.debug("Edit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        String permissions = getFormParam(Const.PERMISSIONS);
        if (StringUtils.isNotEmpty(permissions) && !permissions.toLowerCase().equals(Const.NONE)) {
        	permissions = CrawlPermission.findByName(permissions).url;
        }
        Logger.debug("filter permission: " + permissions);
        if (permissions == null) {
        	permissions = Const.NONE;
        }

        List<CommunicationLog> resList = processFilterCommunicationLogs(name, permissions);
        Logger.debug("addentry: " + addentry + ", search: " + search + ", name: " + name + ", permissions: " + permissions);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
            	CommunicationLog log = new CommunicationLog();
            	log.name = name;
                log.user = User.findByEmail(request().username());        
        		Logger.debug("add communication log entry with url: " + log.url + ", and name: " + 
        				log.name + ", curator: " + log.user);
        		Form<CommunicationLog> logFormNew = Form.form(CommunicationLog.class);
        		logFormNew = logFormNew.fill(log);
        		Map<String,String> communicationLogTypes = CommunicationLog.options();
        		Map<String,String> crawlPermissions = CrawlPermission.options();
              	return ok(
        	              newForm.render(logFormNew, User.findByEmail(request().username()), communicationLogTypes, crawlPermissions)
        	            );
        	} else {
        		Logger.debug("CommunicationLog name is empty. Please write name in search window.");
                res = ok(
                        logs.render(
                            "CommunicationLogs", User.findByEmail(request().username()), resList, "", permissions
                        )
                    );
        	}
        } else {
            res = ok(
            		logs.render(
                        "CommunicationLogs", User.findByEmail(request().username()), resList, name, permissions
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl logs.
     * @param filterUrl The filter string
     * @param permission The permission identifier URL
     * @return
     */
    public static List<CommunicationLog> processFilterCommunicationLogs(String filterUrl, String permission) {
//    	Logger.debug("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<CommunicationLog> exp = CommunicationLog.find.where();
    	List<CommunicationLog> res = new ArrayList<CommunicationLog>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.debug("process name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	if (permission != null && !permission.toLowerCase().equals(Const.NONE)) {
    		Logger.debug("process permission: " + permission);
    		exp = exp.eq(Const.PERMISSION, permission);
//    		exp = exp.eq(Const.PERMISSION, CrawlPermission.findByName(permission).url);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.debug("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.CommunicationLog.findAll();
    	}
        return res;
    }
        
    /**
     * Add new log entry.
     * @param log title
     * @return
     */
    public static Result create(String name) {
    	CommunicationLog log = new CommunicationLog();
    	log.name = name;
        log.user = User.findByEmail(request().username());        
		Logger.debug("add communication log entry with url: " + log.url + ", and name: " + 
				log.name + ", curator: " + log.user);
		Form<CommunicationLog> logFormNew = Form.form(CommunicationLog.class);
		logFormNew = logFormNew.fill(log);
		Map<String,String> communicationLogTypes = CommunicationLog.options();
		Map<String,String> crawlPermissions = CrawlPermission.options();
      	return ok(
	              newForm.render(logFormNew, User.findByEmail(request().username()), communicationLogTypes, crawlPermissions)
	            );
    }
    
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<CommunicationLog> communicationLogForm = Form.form(CommunicationLog.class);
		CommunicationLog communicationLog = new CommunicationLog();
		communicationLogForm = communicationLogForm.fill(communicationLog);
		Map<String,String> communicationLogTypes = CommunicationLog.options();
		Map<String,String> crawlPermissions = CrawlPermission.options();

		return ok(
              newForm.render(communicationLogForm, user, communicationLogTypes, crawlPermissions)
				);
    	
    }

      
    public static Result info(Form<CommunicationLog> form, Long id) {
    	Logger.debug("info");
    	User user = User.findByEmail(request().username());
		Map<String,String> communicationLogTypes = CommunicationLog.options();
		Map<String,String> crawlPermissions = CrawlPermission.options();
		return badRequest(edit.render(form, user, id, communicationLogTypes, crawlPermissions));
    }
    
	public static Result newInfo(Form<CommunicationLog> form) {
		User user = User.findByEmail(request().username());
		Map<String,String> communicationLogTypes = CommunicationLog.options();
		Map<String,String> crawlPermissions = CrawlPermission.options();
        return badRequest(newForm.render(form, user, communicationLogTypes, crawlPermissions));
	}
	
    /**
     * This method saves new object or changes on given log in the same object
     * completed by revision comment. The "version" field in the log object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<CommunicationLog> filledForm = form(CommunicationLog.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        filledForm.get().save();
		        flash("message", "Communication Log " + filledForm.get().name + " has been created");
		        return redirect(routes.CommunicationLogController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }	   

    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<CommunicationLog> filledForm = form(CommunicationLog.class).bindFromRequest();
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
		        flash("message", "Communication Log " + filledForm.get().name + " has been updated");
		        return redirect(routes.CommunicationLogController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		CommunicationLog communicationLog = CommunicationLog.findById(id);
		        flash("message", "Communication Log " + filledForm.get().name + " has been deleted");
            	communicationLog.delete();
            	
        		return redirect(routes.CommunicationLogController.index()); 
        	}
        }
        return null;
    }
    /**
     * This method supports link in crawl permissions view.
     * @param permission
     * @return
     */
    public static Result showLogs(String permission) {
    	Result res = null;
        List<CommunicationLog> resList = processFilterCommunicationLogs("", permission);
        res = ok(
        		logs.render(
                    "CommunicationLogs", User.findByEmail(request().username()), resList, "", permission
                )
            );
        return res;
    }	   
        
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<CommunicationLog> logs = CommunicationLog.filterByName(name);
	        jsonData = Json.toJson(logs);
        }
        return ok(jsonData);
    }
}


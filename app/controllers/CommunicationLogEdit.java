package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.CommunicationLog;
import models.DCollection;
import models.Organisation;
import models.Role;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;

import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.communicationlogs.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage logs.
 */
@Security.Authenticated(Secured.class)
public class CommunicationLogEdit extends AbstractController {
  
    /**
     * Display the log.
     */
    public static Result index() {
        List<CommunicationLog> resList = processFilterCommunicationLogs("");
        return ok(
        		communicationlogs.render(
                    "CommunicationLogs", User.find.byId(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the log edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("log url: " + url);
		CommunicationLog log = CommunicationLog.findByUrl(url);
		Logger.info("log name: " + log.name + ", url: " + url);
        return ok(
                communicationlogedit.render(
                		models.CommunicationLog.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                communicationlogview.render(
                		models.CommunicationLog.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("CommunicationLogEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<CommunicationLog> resList = processFilterCommunicationLogs(name);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.CommunicationLogEdit.addEntry(name));
        	} else {
        		Logger.info("CommunicationLog name is empty. Please write name in search window.");
                res = ok(
                        communicationlogs.render(
                            "CommunicationLogs", User.find.byId(request().username()), resList, ""
                        )
                    );
        	}
        } else {
            res = ok(
            		communicationlogs.render(
                        "CommunicationLogs", User.find.byId(request().username()), resList, name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl logs.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<CommunicationLog> processFilterCommunicationLogs(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<CommunicationLog> exp = CommunicationLog.find.where();
    	List<CommunicationLog> res = new ArrayList<CommunicationLog>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

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
    public static Result addEntry(String name) {
    	CommunicationLog log = new CommunicationLog();
    	log.name = name;
        log.id = Target.createId();
        log.url = Const.ACT_URL + log.id;
        log.curator = User.find.byId(request().username()).url;        
		Logger.info("add entry with url: " + log.url + ", and name: " + log.name + ", curator: " + log.curator);
        return ok(
                communicationlogedit.render(
                      log, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given log in the same object
     * completed by revision comment. The "version" field in the log object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save log id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	CommunicationLog log = null;
            boolean isExisting = true;
            try {
                try {
                	log = CommunicationLog.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	log = new CommunicationLog();
                	log.id = Long.valueOf(getFormParam(Const.ID));
                	log.url = getFormParam(Const.URL);
                }
                if (log == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	log = new CommunicationLog();
                	log.id = Long.valueOf(getFormParam(Const.ID));
                	log.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.NAME) != null) {
        	    	log.name = getFormParam(Const.NAME);
        	    }
        	    if (getFormParam(Const.LOG_DATE) != null) {
        	    	log.date = getFormParam(Const.LOG_DATE);
        	    }
        	    if (getFormParam(Const.TYPE) != null) {
        	    	log.ttype = getFormParam(Const.TYPE);
        	    }
        	    if (getFormParam(Const.NOTES) != null) {
        	    	log.notes = getFormParam(Const.NOTES);
        	    }
        	    if (getFormParam(Const.CURATOR) != null) {
        	    	log.curator = User.findByName(getFormParam(Const.CURATOR)).url;
        	    }
            } catch (Exception e) {
            	Logger.info("CommunicationLog not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(log);
    	        Logger.info("save log: " + log.toString());
        	} else {
           		Logger.info("update log: " + log.toString());
               	Ebean.update(log);
        	}
	        res = redirect(routes.CommunicationLogEdit.view(log.url));
        } 
        if (delete != null) {
        	CommunicationLog log = CommunicationLog.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(log);
	        res = redirect(routes.CommunicationLogEdit.index()); 
        }
    	res = redirect(routes.CommunicationLogEdit.index()); 
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


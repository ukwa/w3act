package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.PermissionRefusal;
import models.DCollection;
import models.Organisation;
import models.Role;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
//import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.refusals.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage refusals.
 */
@Security.Authenticated(Secured.class)
public class PermissionRefusalEdit extends AbstractController {
  
    /**
     * Display the refusal.
     */
    public static Result index() {
        List<PermissionRefusal> resList = processFilterPermissionRefusals("");
        return ok(
                refusals.render(
                    "PermissionRefusals", User.find.byId(request().username()), resList, ""
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
        return ok(
                refusaledit.render(
                		models.PermissionRefusal.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                refusalview.render(
                		models.PermissionRefusal.findByUrl(url), User.find.byId(request().username())
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
    	Logger.info("PermissionRefusalEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<PermissionRefusal> resList = processFilterPermissionRefusals(name);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.PermissionRefusalEdit.addEntry(name));
        	} else {
        		Logger.info("PermissionRefusal name is empty. Please write name in search window.");
                res = ok(
                        refusals.render(
                            "PermissionRefusals", User.find.byId(request().username()), resList, ""
                        )
                    );
        	}
        } else {
            res = ok(
            		refusals.render(
                        "PermissionRefusals", User.find.byId(request().username()), resList, name
                    )
                );
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
    public static Result addEntry(String name) {
    	PermissionRefusal refusal = new PermissionRefusal();
    	refusal.name = name;
        refusal.id = Target.createId();
        refusal.url = Const.ACT_URL + refusal.id;
		Logger.info("add entry with url: " + refusal.url + ", and name: " + refusal.name);
        return ok(
                refusaledit.render(
                      refusal, User.find.byId(request().username())
                )
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
	        res = redirect(routes.PermissionRefusalEdit.view(refusal.url));
        } 
        if (delete != null) {
        	PermissionRefusal refusal = PermissionRefusal.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(refusal);
	        res = redirect(routes.PermissionRefusalEdit.index()); 
        }
    	res = redirect(routes.PermissionRefusalEdit.index()); 
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


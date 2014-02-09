package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Flag;
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
import views.html.flags.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage flags.
 */
@Security.Authenticated(Secured.class)
public class FlagEdit extends AbstractController {
  
    /**
     * Display the flag.
     */
    public static Result index() {
        List<Flag> resList = processFilterFlags("");
        return ok(
                flags.render(
                    "Flags", User.find.byId(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the flag edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("flag url: " + url);
		Flag flag = Flag.findByUrl(url);
		Logger.info("flag name: " + flag.name + ", url: " + url);
        return ok(
                flagedit.render(
                		models.Flag.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                flagview.render(
                		models.Flag.findByUrl(url), User.find.byId(request().username())
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
    	Logger.info("FlagEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<Flag> resList = processFilterFlags(name);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.FlagEdit.addEntry(name));
        	} else {
        		Logger.info("Flag name is empty. Please write name in search window.");
                res = ok(
                        flags.render(
                            "Flags", User.find.byId(request().username()), resList, ""
                        )
                    );
        	}
        } else {
            res = ok(
            		flags.render(
                        "Flags", User.find.byId(request().username()), resList, name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applies filters to the list of flags.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<Flag> processFilterFlags(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<Flag> exp = Flag.find.where();
    	List<Flag> res = new ArrayList<Flag>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.Flag.findAll();
    	}
        return res;
    }
        
    /**
     * Add new flag entry.
     * @param flag title
     * @return
     */
    public static Result addEntry(String name) {
    	Flag flag = new Flag();
    	flag.name = name;
        flag.id = Target.createId();
        flag.url = Const.ACT_URL + flag.id;
		Logger.info("add entry with url: " + flag.url + ", and name: " + flag.name);
        return ok(
                flagedit.render(
                      flag, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given flag in the same object
     * completed by revision comment. The "version" field in the flag object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save flag id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	Flag flag = null;
            boolean isExisting = true;
            try {
                try {
                	flag = Flag.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	flag = new Flag();
                	flag.id = Long.valueOf(getFormParam(Const.ID));
                	flag.url = getFormParam(Const.URL);
                }
                if (flag == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	flag = new Flag();
                	flag.id = Long.valueOf(getFormParam(Const.ID));
                	flag.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.NAME) != null) {
        	    	flag.name = getFormParam(Const.NAME);
        	    }
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	flag.description = getFormParam(Const.DESCRIPTION);
        	    }
            } catch (Exception e) {
            	Logger.info("Flag not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(flag);
    	        Logger.info("save flag: " + flag.toString());
        	} else {
           		Logger.info("update flag: " + flag.toString());
               	Ebean.update(flag);
        	}
	        res = redirect(routes.FlagEdit.view(flag.url));
        } 
        if (delete != null) {
        	Flag flag = Flag.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(flag);
	        res = redirect(routes.FlagEdit.index()); 
        }
    	res = redirect(routes.FlagEdit.index()); 
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Flag> flags = Flag.filterByName(name);
	        jsonData = Json.toJson(flags);
        }
        return ok(jsonData);
    }
}


package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Tag;
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
import views.html.tags.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage tags.
 */
@Security.Authenticated(Secured.class)
public class TagEdit extends AbstractController {
  
    /**
     * Display the tag.
     */
    public static Result index() {
        List<Tag> resList = processFilterTags("");
        return ok(
                tags.render(
                    "Tags", User.find.byId(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the tag edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("tag url: " + url);
		Tag tag = Tag.findByUrl(url);
		Logger.info("tag name: " + tag.name + ", url: " + url);
        return ok(
                tagedit.render(
                		models.Tag.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                tagview.render(
                		models.Tag.findByUrl(url), User.find.byId(request().username())
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
    	Logger.info("TagEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<Tag> resList = processFilterTags(name);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.TagEdit.addEntry(name));
        	} else {
        		Logger.info("Tag name is empty. Please write name in search window.");
                res = ok(
                        tags.render(
                            "Tags", User.find.byId(request().username()), resList, ""
                        )
                    );
        	}
        } else {
            res = ok(
            		tags.render(
                        "Tags", User.find.byId(request().username()), resList, name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl tags.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<Tag> processFilterTags(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<Tag> exp = Tag.find.where();
    	List<Tag> res = new ArrayList<Tag>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.Tag.findAll();
    	}
        return res;
    }
        
    /**
     * Add new tag entry.
     * @param tag title
     * @return
     */
    public static Result addEntry(String name) {
    	Tag tag = new Tag();
    	tag.name = name;
        tag.id = Target.createId();
        tag.url = Const.ACT_URL + tag.id;
		Logger.info("add entry with url: " + tag.url + ", and name: " + tag.name);
        return ok(
                tagedit.render(
                      tag, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given tag in the same object
     * completed by revision comment. The "version" field in the tag object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save tag id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	Tag tag = null;
            boolean isExisting = true;
            try {
                try {
                	tag = Tag.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	tag = new Tag();
                	tag.id = Long.valueOf(getFormParam(Const.ID));
                	tag.url = getFormParam(Const.URL);
                }
                if (tag == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	tag = new Tag();
                	tag.id = Long.valueOf(getFormParam(Const.ID));
                	tag.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.NAME) != null) {
        	    	tag.name = getFormParam(Const.NAME);
        	    }
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	tag.description = getFormParam(Const.DESCRIPTION);
        	    }
            } catch (Exception e) {
            	Logger.info("Tag not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(tag);
    	        Logger.info("save tag: " + tag.toString());
        	} else {
           		Logger.info("update tag: " + tag.toString());
               	Ebean.update(tag);
        	}
	        res = redirect(routes.TagEdit.view(tag.url));
        } 
        if (delete != null) {
        	Tag tag = Tag.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(tag);
	        res = redirect(routes.TagEdit.index()); 
        }
    	res = redirect(routes.TagEdit.index()); 
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Tag> tags = Tag.filterByName(name);
	        jsonData = Json.toJson(tags);
        }
        return ok(jsonData);
    }
}


package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.MailTemplate;
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
import views.html.mailtemplates.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage templates.
 */
@Security.Authenticated(Secured.class)
public class MailTemplateEdit extends AbstractController {
  
    /**
     * Display the template.
     */
    public static Result index() {
        List<MailTemplate> resList = processFilterMailTemplates("");
        return ok(
                mailtemplates.render(
                    "MailTemplates", User.find.byId(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the template edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("template url: " + url);
		MailTemplate template = MailTemplate.findByUrl(url);
		Logger.info("template name: " + template.name + ", url: " + url);
        return ok(
                mailtemplateedit.render(
                		models.MailTemplate.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                mailtemplateview.render(
                		models.MailTemplate.findByUrl(url), User.find.byId(request().username())
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
    	Logger.info("MailTemplateEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<MailTemplate> resList = processFilterMailTemplates(name);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.MailTemplateEdit.addEntry(name));
        	} else {
        		Logger.info("MailTemplate name is empty. Please write name in search window.");
                res = ok(
                        mailtemplates.render(
                            "MailTemplates", User.find.byId(request().username()), resList, ""
                        )
                    );
        	}
        } else {
            res = ok(
            		mailtemplates.render(
                        "MailTemplates", User.find.byId(request().username()), resList, name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl templates.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<MailTemplate> processFilterMailTemplates(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<MailTemplate> exp = MailTemplate.find.where();
    	List<MailTemplate> res = new ArrayList<MailTemplate>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.MailTemplate.findAll();
    	}
        return res;
    }
        
    /**
     * Add new template entry.
     * @param template title
     * @return
     */
    public static Result addEntry(String name) {
    	MailTemplate template = new MailTemplate();
    	template.name = name;
        template.id = Target.createId();
        template.url = Const.ACT_URL + template.id;
		Logger.info("add entry with url: " + template.url + ", and name: " + template.name);
        return ok(
                mailtemplateedit.render(
                      template, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given template in the same object
     * completed by revision comment. The "version" field in the template object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save template id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	MailTemplate template = null;
            boolean isExisting = true;
            try {
                try {
                	template = MailTemplate.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	template = new MailTemplate();
                	template.id = Long.valueOf(getFormParam(Const.ID));
                	template.url = getFormParam(Const.URL);
                }
                if (template == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	template = new MailTemplate();
                	template.id = Long.valueOf(getFormParam(Const.ID));
                	template.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.NAME) != null) {
        	    	template.name = getFormParam(Const.NAME);
        	    }
        	    if (getFormParam(Const.TEXT) != null) {
        	    	template.text = getFormParam(Const.TEXT);
        	    }
        	    if (getFormParam(Const.SUBJECT) != null) {
        	    	template.subject = getFormParam(Const.SUBJECT);
        	    }
        	    if (getFormParam(Const.FROM_EMAIL) != null) {
        	    	template.fromEmail = getFormParam(Const.FROM_EMAIL);
        	    }
        	    if (getFormParam(Const.PLACE_HOLDERS) != null) {
        	    	template.placeHolders = getFormParam(Const.PLACE_HOLDERS);
        	    }
       	    	template.defaultEmail = Utils.getNormalizeBooleanString(getFormParam(Const.DEFAULT_EMAIL_FLAG));
            } catch (Exception e) {
            	Logger.info("MailTemplate not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(template);
    	        Logger.info("save crawl template: " + template.toString());
        	} else {
           		Logger.info("update crawl template: " + template.toString());
               	Ebean.update(template);
        	}
	        res = redirect(routes.MailTemplateEdit.view(template.url));
        } 
        if (delete != null) {
        	MailTemplate template = MailTemplate.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(template);
	        res = redirect(routes.MailTemplateEdit.index()); 
        }
    	res = redirect(routes.MailTemplateEdit.index()); 
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<MailTemplate> templates = MailTemplate.filterByName(name);
	        jsonData = Json.toJson(templates);
        }
        return ok(jsonData);
    }
}


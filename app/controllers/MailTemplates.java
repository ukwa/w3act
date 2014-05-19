package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import models.DCollection;
import models.MailTemplate;
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
import views.html.mailtemplates.edit;
import views.html.mailtemplates.mailtemplates;
import views.html.mailtemplates.view;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage templates.
 */
@Security.Authenticated(Secured.class)
public class MailTemplates extends AbstractController {
  
    /**
     * Display the template.
     */
    public static Result index() {
        List<MailTemplate> resList = processFilterMailTemplates("");
        return ok(
                mailtemplates.render(
                    "MailTemplates", User.find.byId(request().username()), resList, "", ""
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
		Form<MailTemplate> templateForm = Form.form(MailTemplate.class);
		templateForm = templateForm.fill(template);
      	return ok(
	              edit.render(templateForm, User.find.byId(request().username()))
	            );
    }
    
    public static Result view(String url) {
        return ok(
                view.render(
                		models.MailTemplate.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	Logger.info("MailTemplates.search()");
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.NAME);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
        List<MailTemplate> resList = processFilterMailTemplates(query);

        if (StringUtils.isBlank(query)) {
			Logger.info("Role name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return ok(
	                mailtemplates.render(
	                    "MailTemplates", User.find.byId(request().username()), resList, "", ""
	                )
	            );
    	}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
//        		return redirect(routes.MailTemplates.create(query));
            	MailTemplate template = new MailTemplate();
            	template.name = query;
                template.id = Target.createId();
                template.url = Const.ACT_URL + template.id;
        		Logger.info("add email template with url: " + template.url + ", and name: " + template.name);
        		Form<MailTemplate> templateForm = Form.form(MailTemplate.class);
        		templateForm = templateForm.fill(template);
              	return ok(
        	              edit.render(templateForm, User.find.byId(request().username()))
        	            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	        return ok(
    	                mailtemplates.render(
    	                    "MailTemplates", User.find.byId(request().username()), resList, "", ""
    	                )
    	            );
    	    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }	   
    
    /**
     * This method applies filters to the list of mail templates.
     * @param filterUrl
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
     * This method applies filter by type to the list of mail templates.
     * @param templateType
     * @return
     */
    public static List<MailTemplate> getMailTemplatesByType(String templateType) {
    	boolean isProcessed = false;
    	ExpressionList<MailTemplate> exp = MailTemplate.find.where();
    	List<MailTemplate> res = new ArrayList<MailTemplate>();
    	if (templateType != null && templateType.length() > 0) {
    		Logger.info("getMailTemplatesByType() type: " + templateType);
    		exp = exp.contains(Const.TTYPE, templateType);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("getMailTemplatesByType() resulting list size: " + res.size());

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
    public static Result create(String name) {
    	MailTemplate template = new MailTemplate();
    	template.name = name;
        template.id = Target.createId();
        template.url = Const.ACT_URL + template.id;
		Logger.info("add email template with url: " + template.url + ", and name: " + template.name);
		Form<MailTemplate> templateForm = Form.form(MailTemplate.class);
		templateForm = templateForm.fill(template);
      	return ok(
	              edit.render(templateForm, User.find.byId(request().username()))
	            );
    }
      
	/**
	 * This method prepares MailTemplate form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	MailTemplate template = new MailTemplate();
       	template.id = Long.valueOf(getFormParam(Const.ID));
       	template.url = getFormParam(Const.URL);
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
	    if (getFormParam(Const.TTYPE) != null) {
	    	template.ttype = getFormParam(Const.TTYPE);
	    }
    	template.defaultEmail = Utils.getNormalizeBooleanString(getFormParam(Const.DEFAULT_EMAIL_FLAG));
		Form<MailTemplate> templateFormNew = Form.form(MailTemplate.class);
		templateFormNew = templateFormNew.fill(template);
      	return ok(
	              edit.render(templateFormNew, User.find.byId(request().username()))
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
        	Form<MailTemplate> templateForm = Form.form(MailTemplate.class).bindFromRequest();
            if(templateForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : templateForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + templateForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
	  					"Missing fields are " + missingFields);
	  			return info();
            }
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
        	    if (getFormParam(Const.TTYPE) != null) {
        	    	template.ttype = getFormParam(Const.TTYPE);
        	    }
            	Logger.info("template type: " + template.ttype);
       	    	template.defaultEmail = Utils.getNormalizeBooleanString(getFormParam(Const.DEFAULT_EMAIL_FLAG));
            } catch (Exception e) {
            	Logger.info("MailTemplate not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(template);
    	        Logger.info("save mail template: " + template.toString());
        	} else {
           		Logger.info("update mail template: " + template.toString());
               	Ebean.update(template);
        	}
	        return redirect(routes.MailTemplates.edit(template.url));
        } 
        if (delete != null) {
        	MailTemplate template = MailTemplate.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(template);
	        return redirect(routes.MailTemplates.index()); 
        }
    	res = redirect(routes.MailTemplates.index()); 
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


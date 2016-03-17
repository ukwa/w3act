package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.MailTemplate;
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
import uk.bl.Const.MailTemplateType;
import views.html.mailtemplates.edit;
import views.html.mailtemplates.list;
import views.html.mailtemplates.view;
import views.html.mailtemplates.newForm;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage templates.
 */
@Security.Authenticated(SecuredController.class)
public class MailTemplateController extends AbstractController {
  
    /**
     * Display the template.
     */
    public static Result index() {
    	User user = User.findByEmail(request().username());
    	List<MailTemplate> mailTemplates = MailTemplate.findAll();
        return ok(
                list.render(
                    "MailTemplates", user, mailTemplates, "", ""
                )
            );
    }
    
    public static Result view(Long id) {
		MailTemplate mailTemplate = MailTemplate.findById(id);
		if (mailTemplate == null) return notFound("There is no Mail Template with ID " + id);
		
        return ok(
                view.render(mailTemplate, User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	Logger.debug("MailTemplates.search()");
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.NAME);
		Logger.debug("query: " + query);
		Logger.debug("action: " + action);
    	
        List<MailTemplate> resList = processFilterMailTemplates(query);

        if (StringUtils.isBlank(query)) {
			Logger.debug("Role name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return ok(
	        		list.render(
	                    "MailTemplates", User.findByEmail(request().username()), resList, "", ""
	                )
	            );
    	}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	        return ok(
    	        		list.render(
    	                    "MailTemplates", User.findByEmail(request().username()), resList, "", ""
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
//    	Logger.debug("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<MailTemplate> exp = MailTemplate.find.where();
    	List<MailTemplate> res = new ArrayList<MailTemplate>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.debug("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.debug("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

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
    		Logger.debug("getMailTemplatesByType() type: " + templateType);
    		exp = exp.contains(Const.TTYPE, templateType);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.debug("getMailTemplatesByType() resulting list size: " + res.size());

        if (!isProcessed) {
    		res = models.MailTemplate.findAll();
    	}
        return res;
    }
        
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<MailTemplate> templateForm = Form.form(MailTemplate.class);
		MailTemplate template = new MailTemplate();
		templateForm = templateForm.fill(template);
		Map<String,String> templates = MailTemplateType.options();
        return ok(newForm.render(templateForm, user, templates));
    }

    public static Result edit(Long id) {
    	MailTemplate template = MailTemplate.findById(id);
        if (template == null) return notFound("There is no Mail Template with ID " + id);
		
    	User user = User.findByEmail(request().username());
		Form<MailTemplate> templateForm = Form.form(MailTemplate.class);
		templateForm = templateForm.fill(template);
		Map<String,String> templates = MailTemplateType.options();
        return ok(edit.render(templateForm, user, id, templates));
    }

    public static Result info(Form<MailTemplate> form, Long id) {
    	User user = User.findByEmail(request().username());
		Map<String,String> templates = MailTemplateType.options();
		return badRequest(edit.render(form, user, id, templates));
    }
    
	public static Result newInfo(Form<MailTemplate> form) {
		User user = User.findByEmail(request().username());
		Map<String,String> templates = MailTemplateType.options();
        return badRequest(newForm.render(form, user, templates));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<MailTemplate> filledForm = form(MailTemplate.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
		        filledForm.get().save();
		        flash("message", "Mail Template " + filledForm.get().name + " has been created");
		        return redirect(routes.MailTemplateController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<MailTemplate> filledForm = form(MailTemplate.class).bindFromRequest();
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
		        flash("message", "Mail Template " + filledForm.get().name + " has been updated");
		        return redirect(routes.MailTemplateController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		MailTemplate template = MailTemplate.findById(id);
		        flash("message", "Mail Template " + filledForm.get().name + " has been deleted");
		        template.delete();
            	
        		return redirect(routes.MailTemplateController.index()); 
        	}
        }
        return null;
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


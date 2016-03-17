package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.PermissionRefusal;
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
import views.html.refusals.newForm;
import views.html.refusals.edit;
import views.html.refusals.list;
import views.html.refusals.view;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Manage refusals.
 */
@Security.Authenticated(SecuredController.class)
public class PermissionRefusalController extends AbstractController {
  
    public static Result index() {
        return ok(
        		views.html.refusals.list.render(
                	"Refusals", User.findByEmail(request().username()), models.PermissionRefusal.findAll(), ""
                )
            );
    }

    /**
     * Display the refusal.
     */
    public static Result indexFilter() {
        List<PermissionRefusal> resList = processFilterPermissionRefusals("");
        return ok(
                list.render(
                    "PermissionRefusals", User.findByEmail(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the refusal edit panel for this URL.
     */
    public static Result edit(Long id) {
		PermissionRefusal refusal = PermissionRefusal.findById(id);
		if (refusal == null) return notFound("There is no Permission Refusal with ID " + id);
			
		User user = User.findByEmail(request().username());
		Form<PermissionRefusal> refusalForm = Form.form(PermissionRefusal.class);
		refusalForm = refusalForm.fill(refusal);
		Map<String,String> refusalTypes = Const.RefusalType.options();
      	return ok(
	              edit.render(refusalForm, user, id, refusalTypes)
	            );
    }
    
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<PermissionRefusal> refusalForm = Form.form(PermissionRefusal.class);
		PermissionRefusal template = new PermissionRefusal();
		refusalForm = refusalForm.fill(template);
		Map<String,String> refusalTypes = Const.RefusalType.options();
        return ok(newForm.render(refusalForm, user, refusalTypes));
    }

    public static Result info(Form<PermissionRefusal> form, Long id) {
    	User user = User.findByEmail(request().username());
		Map<String,String> refusalTypes = Const.RefusalType.options();
		return badRequest(edit.render(form, user, id, refusalTypes));
    }
    
	public static Result newInfo(Form<PermissionRefusal> form) {
		User user = User.findByEmail(request().username());
		Map<String,String> refusalTypes = Const.RefusalType.options();
        return badRequest(newForm.render(form, user, refusalTypes));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<PermissionRefusal> filledForm = form(PermissionRefusal.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
		        filledForm.get().save();
		        flash("message", "Permission Refusal " + filledForm.get().name + " has been created");
		        return redirect(routes.PermissionRefusalController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<PermissionRefusal> filledForm = form(PermissionRefusal.class).bindFromRequest();
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
		        flash("message", "Permission Refusal " + filledForm.get().name + " has been updated");
		        return redirect(routes.PermissionRefusalController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		PermissionRefusal refusal = PermissionRefusal.findById(id);
		        flash("message", "Permission Refusal " + filledForm.get().name + " has been deleted");
		        refusal.delete();
            	
        		return redirect(routes.PermissionRefusalController.indexFilter()); 
        	}
        }
        return null;
    }    
    
    public static Result view(Long id) {
		PermissionRefusal refusal = PermissionRefusal.findById(id);
		if (refusal == null) return notFound("There is no Permission Refusal with ID " +id);
		
        return ok(
                view.render(refusal, User.findByEmail(request().username())
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
		Logger.debug("query: " + query);
		Logger.debug("action: " + action);
    	
        List<PermissionRefusal> resList = processFilterPermissionRefusals(query);
        
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Refusal name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
            res = ok(
            		list.render(
                        "PermissionRefusals", User.findByEmail(request().username()), resList, ""
                    )
                );
    	}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
                res = ok(
                		list.render(
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
//    	Logger.debug("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<PermissionRefusal> exp = PermissionRefusal.find.where();
    	List<PermissionRefusal> res = new ArrayList<PermissionRefusal>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.debug("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.debug("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.PermissionRefusal.findAll();
    	}
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


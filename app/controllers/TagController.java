package controllers;

import static play.data.Form.form;

import java.util.List;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Tag;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.tags.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage tags.
 */
@Security.Authenticated(SecuredController.class)
public class TagController extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
    	Logger.info("Tags.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.TagController.list(0, "name", "asc", "")
        );
    
    public static Result view(Long id) {
        return ok(
                view.render(
                		models.Tag.findById(id), User.findByEmail(request().username())
                )
            );
    }

    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<Tag> tagForm = Form.form(Tag.class);
		Tag tag = new Tag();
		tagForm = tagForm.fill(tag);
        return ok(newForm.render(tagForm, user));
    }

    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
    	Tag tag = Tag.findById(id);
		Form<Tag> tagForm = Form.form(Tag.class);
		tagForm = tagForm.fill(tag);
        return ok(edit.render(tagForm, user, id));
    }

    public static Result info(Form<Tag> form, Long id) {
    	User user = User.findByEmail(request().username());
		return badRequest(edit.render(form, user, id));
    }
    
	public static Result newInfo(Form<Tag> form) {
		User user = User.findByEmail(request().username());
        return badRequest(newForm.render(form, user));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<Tag> filledForm = form(Tag.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
		        filledForm.get().save();
		        flash("message", "Tag " + filledForm.get().name + " has been created");
		        return redirect(routes.TagController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<Tag> filledForm = form(Tag.class).bindFromRequest();
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
		        flash("message", "Tag " + filledForm.get().name + " has been updated");
		        return redirect(routes.TagController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		Tag tag = Tag.findById(id);
		        flash("message", "Tag " + filledForm.get().name + " has been deleted");
            	tag.delete();
            	
        		return redirect(routes.TagController.index()); 
        	}
        }
        return null;
    }    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.QUERY);
		Logger.info("query: " + query);
		Logger.info("action: " + action);
    	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Tag name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.TagController.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    	    	return redirect(routes.TagController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}                
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
    		res = models.Tag.findAllTags();
    	}
        return res;
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Tag> tags = Tag.filterByTagName(name);
	        jsonData = Json.toJson(tags);
        }
        return ok(jsonData);
    }
    
    /**
     * Display the paginated list of Tags.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Tags
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Tags.list()");
        return ok(
        	list.render(
        			"Tags", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Tag.pager(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }   
}


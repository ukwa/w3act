package controllers;

import static play.data.Form.form;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.DCollection;
import models.Tag;
import models.Target;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.tags.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage tags.
 */
@Security.Authenticated(Secured.class)
public class Tags extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
    	Logger.info("Tags.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.Tags.list(0, "name", "asc", "")
        );
    
    /**
     * Display the tag edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("tag url: " + url);
		Tag tag = Tag.findByUrl(url);
		Logger.info("tag name: " + tag.name + ", url: " + url);
		Form<Tag> tagFormNew = Form.form(Tag.class);
		tagFormNew = tagFormNew.fill(tag);
      	return ok(
	              edit.render(tagFormNew, User.find.byId(request().username()))
	            );
    }
    
    public static Result view(String url) {
        return ok(
                view.render(
                		models.Tag.findByUrl(url), User.find.byId(request().username())
                )
            );
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
	        		routes.Tags.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    	    	Tag tag = new Tag();
    	    	tag.name = query;
    	        tag.id = Target.createId();
    	        tag.url = Const.ACT_URL + tag.id;
    			Logger.info("add tag with url: " + tag.url + ", and name: " + tag.name);
    			Form<Tag> tagFormNew = Form.form(Tag.class);
    			tagFormNew = tagFormNew.fill(tag);
    	      	return ok(
    		              edit.render(tagFormNew, User.find.byId(request().username()))
    		            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Tags.list(pageNo, sort, order, query));
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
    		res = models.Tag.findAll();
    	}
        return res;
    }
        
    /**
     * Add new tag entry.
     * @param tag title
     * @return
     */
    public static Result create(String name) {
    	Tag tag = new Tag();
    	tag.name = name;
        tag.id = Target.createId();
        tag.url = Const.ACT_URL + tag.id;
		Logger.info("add tag with url: " + tag.url + ", and name: " + tag.name);
		Form<Tag> tagFormNew = Form.form(Tag.class);
		tagFormNew = tagFormNew.fill(tag);
      	return ok(
	              edit.render(tagFormNew, User.find.byId(request().username()))
	            );
    }
      
	/**
	 * This method prepares Collection form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Tag tag = new Tag();
    	tag.id = Long.valueOf(getFormParam(Const.ID));
    	tag.url = getFormParam(Const.URL);
	    if (getFormParam(Const.NAME) != null) {
	    	tag.name = getFormParam(Const.NAME);
	    }
	    if (getFormParam(Const.DESCRIPTION) != null) {
	    	tag.description = getFormParam(Const.DESCRIPTION);
	    }
		Form<Tag> tagFormNew = Form.form(Tag.class);
		tagFormNew = tagFormNew.fill(tag);
      	return ok(
	              edit.render(tagFormNew, User.find.byId(request().username()))
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
        	Form<Tag> tagForm = Form.form(Tag.class).bindFromRequest();
            if(tagForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : tagForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + tagForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
	  					" Missing fields are: " + missingFields);
	  			return info();
            }
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
	        return redirect(routes.Tags.edit(tag.url));
        } 
        if (delete != null) {
        	Tag tag = Tag.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(tag);
	        res = redirect(routes.Tags.index()); 
        }
    	res = redirect(routes.Tags.index()); 
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
        			User.find.byId(request().username()), 
        			filter, 
        			Tag.page(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }   
}


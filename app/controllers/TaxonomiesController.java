package controllers;

import static play.data.Form.form;

import java.util.List;

import models.Taxonomy;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.taxonomies.taxonomyedit;
import views.html.taxonomies.taxonomyview;
import views.html.taxonomies.list;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

@Security.Authenticated(Secured.class)
public class TaxonomiesController extends AbstractController {

	/**
	 * Display the taxonomies.
	 */
	public static Result index() {
		Logger.info("Taxonomies.index()");
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.TaxonomiesController.list(0, "name", "asc", ""));

	/**
	 * Display the paginated list of taxonomies.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filter
	 *            Filter applied on target urls
	 */
	public static Result list(int pageNo, String sortBy, String order,
			String filter) {
		Logger.info("LookUp.list()");
		return ok(list.render("TaxonomiesList",
				User.findByEmail(request().username()), filter,
				Taxonomy.page(pageNo, 10, sortBy, order, filter), sortBy,
				order));
	}
	
	/**
	 * This method enables searching for given URL and redirection in order to
	 * add new entry if required.
	 * 
	 * @return
	 */
	public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String query = form.get(Const.URL);

		Logger.info("action: " + action);
    	Logger.info("taxonomy search() query: " + query);
    	   	
    	if (StringUtils.isBlank(query)) {
			Logger.info("Taxonomy name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.TaxonomiesController.list(0, Const.NAME, "asc", "")
	        );
    	}

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
		
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		return redirect(routes.TaxonomiesController.create(query));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.TaxonomiesController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
	}

	@BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
    	JsonNode jsonData = null;
        if (name != null) {
	        List<Taxonomy> taxonomiesList = Taxonomy.filterByName(name);
	        jsonData = Json.toJson(taxonomiesList);
        }
        return ok(jsonData);
    }
	    
	  
    public static Result view(String url) {
        return ok(
                taxonomyview.render(
                        Taxonomy.findByUrl(url), User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * Add new taxonomy entry.
     * @param taxonomy name
     * @return
     */
    public static Result create(String name) {
    	Taxonomy taxonomy = new Taxonomy();
    	taxonomy.name = name;
    	// TODO: createId
        taxonomy.id = Target.createId();
        taxonomy.url = Const.ACT_URL + taxonomy.id;
		Logger.info("add entry with url: " + taxonomy.url + ", and name: " + taxonomy.name);
        return ok(
                taxonomyedit.render(
                      taxonomy, User.findByEmail(request().username())
                )
            );
    }
    
    /**
     * Display the taxonomy edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("taxonomy url: " + url);
		Taxonomy taxonomy = Taxonomy.findByUrl(url);
		Logger.info("taxonomy name: " + taxonomy.name + ", url: " + url);
        return ok(
                taxonomyedit.render(
                        Taxonomy.findByUrl(url), User.findByEmail(request().username())
                )
            );
    }

    /**
     * This method saves new object or changes on given Taxonomy in the same object
     * completed by revision comment. The "version" field in the Taxonomy object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save taxonomy nid: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION));
        	Taxonomy taxonomy = null;
            boolean isExisting = true;
            try {
                try {
                	taxonomy = Taxonomy.getByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	taxonomy = new Taxonomy();
                	// TODO: createId
                	taxonomy.id = Long.valueOf(getFormParam(Const.ID));
                	taxonomy.url = getFormParam(Const.URL);
                }
                if (taxonomy == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	taxonomy = new Taxonomy();
                	// TODO: createId
                	taxonomy.id = Long.valueOf(getFormParam(Const.ID));
                	taxonomy.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.TITLE) != null) {
        	    	taxonomy.name = getFormParam(Const.TITLE);
        	    }
        	    if (getFormParam(Const.TYPE) != null) {
        	    	taxonomy.ttype = getFormParam(Const.TYPE);
        	    }
        	    if (getFormParam(Const.SUMMARY) != null) {
        	    	taxonomy.description = getFormParam(Const.SUMMARY);
        	    }
            } catch (Exception e) {
            	Logger.info("User not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(taxonomy);
    	        Logger.info("save taxonomy: " + taxonomy.toString());
        	} else {
           		Logger.info("update taxonomy: " + taxonomy.toString());
               	Ebean.update(taxonomy);
        	}
	        res = redirect(routes.TaxonomiesController.view(taxonomy.url));
        } 
        if (delete != null) {
        	String url = getFormParam(Const.URL);
        	Logger.info("deleting: " + url);
        	Taxonomy taxonomy = Taxonomy.findByUrl(url);
        	Ebean.delete(taxonomy);
	        res = redirect(routes.TaxonomiesController.index()); 
        }
        return res;
    }
	    
}

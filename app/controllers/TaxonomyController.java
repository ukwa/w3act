package controllers;

import static play.data.Form.form;

import models.Taxonomy;
import models.Target;
import models.User;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.taxonomies.edit;
import views.html.taxonomies.view;

import com.avaje.ebean.Ebean;

@Security.Authenticated(SecuredController.class)
public class TaxonomyController extends AbstractController {

	/**
	 * Display the taxonomies.
	 */
	public static Result index() {
		Logger.debug("Taxonomies.index()");
		return GO_HOME;
	}

	public static Result GO_HOME = redirect(routes.TaxonomyController.list(0, "name", "asc", ""));

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
	public static Result list(int pageNo, String sortBy, String order, String filter) {
		return null;
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

		Logger.debug("action: " + action);
    	Logger.debug("taxonomy search() query: " + query);
    	   	
    	if (StringUtils.isBlank(query)) {
			Logger.debug("Taxonomy name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.TaxonomyController.list(0, Const.NAME, "asc", "")
	        );
    	}

    	int pageNo = Integer.parseInt(form.get(Const.PAGE_NO));
    	String sort = form.get(Const.SORT_BY);
    	String order = form.get(Const.ORDER);
		
    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		return redirect(routes.TaxonomyController.create(query));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.TaxonomyController.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
	}

	@BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
		return null;
    }
	    
	public static Result edit(Long id) {
        return ok(
                edit.render(
                        Taxonomy.findById(id), User.findByEmail(request().username())
                )
            );
	}
	
    public static Result view(Long id) {
        return ok(
                view.render(
                        Taxonomy.findById(id), User.findByEmail(request().username())
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
		Logger.debug("add entry with url: " + taxonomy.url + ", and name: " + taxonomy.name);
        return ok(
                edit.render(
                      taxonomy, User.findByEmail(request().username())
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
//        Logger.debug("save: " + save);
        if (save != null) {
        	Logger.debug("save taxonomy nid: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", title: " + getFormParam(Const.TITLE) + ", revision: " + getFormParam(Const.REVISION));
        	Taxonomy taxonomy = null;
            boolean isExisting = true;
            try {
                try {
                	taxonomy = Taxonomy.getByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.debug("is not existing exception");
                	isExisting = false;
                	taxonomy = new Taxonomy();
                	// TODO: createId
                	taxonomy.id = Long.valueOf(getFormParam(Const.ID));
                	taxonomy.url = getFormParam(Const.URL);
                }
                if (taxonomy == null) {
                	Logger.debug("is not existing");
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
            	Logger.debug("User not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(taxonomy);
    	        Logger.debug("save taxonomy: " + taxonomy.toString());
        	} else {
           		Logger.debug("update taxonomy: " + taxonomy.toString());
               	Ebean.update(taxonomy);
        	}
	        res = redirect(routes.TaxonomyController.view(taxonomy.id));
        } 
        if (delete != null) {
        	String url = getFormParam(Const.URL);
        	Logger.debug("deleting: " + url);
        	Taxonomy taxonomy = Taxonomy.findByUrl(url);
        	Ebean.delete(taxonomy);
	        res = redirect(routes.TaxonomyController.index()); 
        }
        return res;
    }
	    
}

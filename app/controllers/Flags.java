package controllers;

import static play.data.Form.form;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.Flag;
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
import views.html.flags.edit;
import views.html.flags.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage flags.
 */
@Security.Authenticated(Secured.class)
public class Flags extends AbstractController {
  
    /**
     * Display the role.
     */
    public static Result index() {
    	Logger.info("Flags.index()");
        return GO_HOME;
    }
    
    public static Result GO_HOME = redirect(
            routes.Flags.list(0, "name", "asc", "")
        );
    

    /**
     * Display the flag edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("flag url: " + url);
		Flag flag = Flag.findByUrl(url);
		Logger.info("flag name: " + flag.name + ", url: " + url);
		Form<Flag> flagFormNew = Form.form(Flag.class);
		flagFormNew = flagFormNew.fill(flag);
      	return ok(
	              edit.render(flagFormNew, User.findByEmail(request().username()))
	            );
    }
    
    public static Result view(String url) {
        return ok(
                view.render(
                		models.Flag.findByUrl(url), User.findByEmail(request().username())
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
			Logger.info("Flag name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.Flags.list(0, "name", "asc", "")
	        );
    	}

    	int pageNo = getQueryParamAsInt(Const.PAGE_NO, 0);
    	String sort = getQueryParam(Const.SORT_BY);
    	String order = getQueryParam(Const.ORDER);

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
    	    	Flag flag = new Flag();
    	    	flag.name = query;
    	        flag.id = Target.createId();
    	        flag.url = Const.ACT_URL + flag.id;
    			Logger.info("add flag with url: " + flag.url + ", and name: " + flag.name);
    			Form<Flag> flagFormNew = Form.form(Flag.class);
    			flagFormNew = flagFormNew.fill(flag);
    	      	return ok(
    		              edit.render(flagFormNew, User.findByEmail(request().username()))
    		            );
    		} 
    		else if (Const.SEARCH.equals(action)) {
    	    	return redirect(routes.Flags.list(pageNo, sort, order, query));
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}      
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
    		res = models.Flag.findAllFlags();
    	}
        return res;
    }
        
    /**
     * Add new flag entry.
     * @param flag title
     * @return
     */
    public static Result create(String name) {
    	Flag flag = new Flag();
    	flag.name = name;
        flag.id = Target.createId();
        flag.url = Const.ACT_URL + flag.id;
		Logger.info("add flag with url: " + flag.url + ", and name: " + flag.name);
		Form<Flag> flagFormNew = Form.form(Flag.class);
		flagFormNew = flagFormNew.fill(flag);
      	return ok(
	              edit.render(flagFormNew, User.findByEmail(request().username()))
	            );
    }
      
	/**
	 * This method prepares Flag form for sending info message
	 * about errors 
	 * @return edit page with form and info message
	 */
	public static Result info() {
    	Flag flag = new Flag();
    	flag.id = Long.valueOf(getFormParam(Const.ID));
    	flag.url = getFormParam(Const.URL);
	    if (getFormParam(Const.DESCRIPTION) != null) {
	    	flag.description = getFormParam(Const.DESCRIPTION);
	    }
		Form<Flag> flagFormNew = Form.form(Flag.class);
		flagFormNew = flagFormNew.fill(flag);
      	return ok(
	              edit.render(flagFormNew, User.findByEmail(request().username()))
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
        	Form<Flag> flagForm = Form.form(Flag.class).bindFromRequest();
            if(flagForm.hasErrors()) {
            	String missingFields = "";
            	for (String key : flagForm.errors().keySet()) {
            	    Logger.debug("key: " +  key);
            	    key = Utils.showMissingField(key);
            	    if (missingFields.length() == 0) {
            	    	missingFields = key;
            	    } else {
            	    	missingFields = missingFields + Const.COMMA + " " + key;
            	    }
            	}
            	Logger.info("form errors size: " + flagForm.errors().size() + ", " + missingFields);
	  			flash("message", "Please fill out all the required fields, marked with a red star." + 
	  					" Missing fields are: " + missingFields);
	  			return info();
            }
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
	        return redirect(routes.Flags.edit(flag.url));
        } 
        if (delete != null) {
        	Flag flag = Flag.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(flag);
	        res = redirect(routes.Flags.index()); 
        }
    	res = redirect(routes.Flags.index()); 
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Flag> flags = Flag.filterByFlagName(name);
	        jsonData = Json.toJson(flags);
        }
        return ok(jsonData);
    }
    
    /**
     * This method maps flag value to present predefined Flag values in GUI.
     * @param name The predefined flag value.
     * @return flag name that should be presented in GUI
     */
    public static String getGuiName(String name) {
    	String res = name;
    	if (name != null && name.length() > 0) {
    		String guiName = Const.guiFlagMap.get(name);
    		if (guiName != null && guiName.length() > 0) {
    			res = guiName;
    		}
    	}
    	return res;
    }
    
    /**
     * This method calculates flag value from the GUI flag name.
     * @param name The GUI flag value.
     * @return original flag name 
     */
    public static String getNameFromGuiName(String name) {
    	String res = name;
    	if (name != null && name.length() > 0) {
			for (Map.Entry<String, String> entry : Const.guiFlagMap.entrySet()) {
				if (entry.getValue().equals(name)) {
					res = entry.getKey();
					break;
				}
			}
    	}
    	return res;
    }
    
    /**
     * Display the paginated list of Flags.
     *
     * @param page Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order Sort order (either asc or desc)
     * @param filter Filter applied on Flags
     */
    public static Result list(int pageNo, String sortBy, String order, String filter) {
    	Logger.info("Flags.list()");
        return ok(
        	list.render(
        			"Flags", 
        			User.findByEmail(request().username()), 
        			filter, 
        			Flag.pager(pageNo, 10, sortBy, order, filter), 
        			sortBy, 
        			order)
        	);
    }       
}


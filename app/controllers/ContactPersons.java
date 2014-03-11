package controllers;

import static play.data.Form.form;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.ContactPerson;
import models.Target;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.contactpersons.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage persons.
 */
@Security.Authenticated(Secured.class)
public class ContactPersons extends AbstractController {
  
    /**
     * Display the person.
     */
    public static Result index() {
        List<ContactPerson> resList = processFilterContactPersons("");
        return ok(
                list.render(
                    "ContactPersons", User.find.byId(request().username()), resList, ""
                )
            );
    }

    /**
     * Display the person edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("person url: " + url);
		ContactPerson person = ContactPerson.findByUrl(url);
		Logger.info("person name: " + person.name + ", url: " + url);
        return ok(
                edit.render(
                		models.ContactPerson.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
    	Logger.info("view contact person");
        return ok(
                view.render(
                		models.ContactPerson.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {

    	Logger.info("ContactPersons.search()");
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String name = form.get(Const.NAME);

        if (StringUtils.isBlank(name)) {
			Logger.info("Contact Person name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.ContactPersons.index()
	        );
    	}

        List<ContactPerson> resList = processFilterContactPersons(name);

        if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		return redirect(routes.ContactPersons.create(name));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
                		list.render(
                                "ContactPersons", User.find.byId(request().username()), resList, name
                            )
                        );
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }	   
    
    /**
     * This method applyies filters to the list of crawl persons.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<ContactPerson> processFilterContactPersons(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<ContactPerson> exp = ContactPerson.find.where();
    	List<ContactPerson> res = new ArrayList<ContactPerson>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.ContactPerson.findAll();
    	}
        return res;
    }
        
    /**
     * Add new person entry.
     * @param person title
     * @return
     */
    public static Result create(String name) {
    	Logger.info("create contact person");
    	ContactPerson person = new ContactPerson();
    	person.name = name;
        person.id = Target.createId();
        person.url = Const.ACT_URL + person.id;
		Logger.info("add entry with url: " + person.url + ", and name: " + person.name);
        return ok(
                edit.render(
                      person, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given person in the same object
     * completed by revision comment. The "version" field in the person object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save person id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	ContactPerson person = null;
            boolean isExisting = true;
            try {
                try {
                	person = ContactPerson.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	person = new ContactPerson();
                	person.id = Long.valueOf(getFormParam(Const.ID));
                	person.url = getFormParam(Const.URL);
                }
                if (person == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	person = new ContactPerson();
                	person.id = Long.valueOf(getFormParam(Const.ID));
                	person.url = getFormParam(Const.URL);
                }
                
        	    if (getFormParam(Const.NAME) != null) {
        	    	person.name = getFormParam(Const.NAME);
        	    }
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	person.description = getFormParam(Const.DESCRIPTION);
        	    }
        	    if (getFormParam(Const.POSITION) != null) {
        	    	person.position = getFormParam(Const.POSITION);
        	    }
        	    if (getFormParam(Const.EMAIL) != null) {
        	    	person.email = getFormParam(Const.EMAIL);
        	    }
        	    if (getFormParam(Const.CONTACT_ORGANISATION) != null) {
        	    	person.contactOrganisation = getFormParam(Const.CONTACT_ORGANISATION);
        	    }
        	    if (getFormParam(Const.PHONE) != null) {
        	    	person.phone = getFormParam(Const.PHONE);
        	    }
        	    if (getFormParam(Const.POSTAL_ADDRESS) != null) {
        	    	person.postalAddress = getFormParam(Const.POSTAL_ADDRESS);
        	    }
        	    if (getFormParam(Const.WEB_FORM) != null) {
        	    	person.webForm = getFormParam(Const.WEB_FORM);
        	    }
       	    	person.defaultContact = Utils.getNormalizeBooleanString(getFormParam(Const.DEFAULT_CONTACT));
       	    	person.permissionChecked = Utils.getNormalizeBooleanString(getFormParam(Const.PERMISSION_CHECKED));
            } catch (Exception e) {
            	Logger.info("ContactPerson not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(person);
    	        Logger.info("save crawl person: " + person.toString());
        	} else {
           		Logger.info("update crawl person: " + person.toString());
               	Ebean.update(person);
        	}
	        res = redirect(routes.ContactPersons.view(person.url));
        } 
        if (delete != null) {
        	ContactPerson person = ContactPerson.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(person);
	        res = redirect(routes.ContactPersons.index()); 
        }
    	res = redirect(routes.ContactPersons.index()); 
        return res;
    }	   

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<ContactPerson> persons = ContactPerson.filterByName(name);
	        jsonData = Json.toJson(persons);
        }
        return ok(jsonData);
    }
}


package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.ContactPerson;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.contactpersons.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Manage persons.
 */
@Security.Authenticated(SecuredController.class)
public class ContactPersonController extends AbstractController {
  
    /**
     * Display the person.
     */
    public static Result index() {
    	User user = User.findByEmail(request().username());
        List<ContactPerson> resList = ContactPerson.findAll();
        return ok(
                list.render(
                    "ContactPersons", user, resList, ""
                )
            );        
    }

    public static Result view(Long id) {
    	User user = User.findByEmail(request().username());
    	Logger.debug("view contact person");
        return ok(
                view.render(
                		models.ContactPerson.findById(id), user
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {

    	Logger.debug("ContactPersons.search()");
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String name = form.get(Const.NAME);

        if (StringUtils.isBlank(name)) {
			Logger.debug("Contact Person name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
	        return redirect(
	        		routes.ContactPersonController.index()
	        );
    	}

        List<ContactPerson> resList = processFilterContactPersons(name);

        if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (action.equals("search")) {
    			return ok(
                		list.render(
                                "ContactPersons", User.findByEmail(request().username()), resList, name
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
//    	Logger.debug("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<ContactPerson> exp = ContactPerson.find.where();
    	List<ContactPerson> res = new ArrayList<ContactPerson>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.debug("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.debug("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.ContactPerson.findAll();
    	}
        return res;
    }

    
    public static Result newForm() {
    	User user = User.findByEmail(request().username());
		Form<ContactPerson> contactPersonForm = Form.form(ContactPerson.class);
		ContactPerson contactPerson = new ContactPerson();
		contactPersonForm = contactPersonForm.fill(contactPerson);
        return ok(newForm.render(contactPersonForm, user));
    	
    }
    
    public static Result edit(Long id) {
    	User user = User.findByEmail(request().username());
		ContactPerson person = ContactPerson.findById(id);
		Form<ContactPerson> contactPersonForm = Form.form(ContactPerson.class);
		contactPersonForm = contactPersonForm.fill(person);
      	return ok(
	              edit.render(contactPersonForm, user, id)
	            );
    }


    public static Result info(Form<ContactPerson> form, Long id) {
    	Logger.debug("info");
    	User user = User.findByEmail(request().username());
		return badRequest(edit.render(form, user, id));
    }
    
	public static Result newInfo(Form<ContactPerson> form) {
		User user = User.findByEmail(request().username());
        return badRequest(newForm.render(form, user));
	}
	
    public static Result save() {
    	
    	DynamicForm requestData = form().bindFromRequest();
    	String action = requestData.get("action");

    	Logger.debug("action: " + action);
    	
        if (StringUtils.isNotEmpty(action)) {
        	if (action.equals("save")) {
		        Form<ContactPerson> filledForm = form(ContactPerson.class).bindFromRequest();
		        if(filledForm.hasErrors()) {
	        		Logger.debug("errors: " + filledForm.errors());
		            return newInfo(filledForm);
		        }
		        
            	ContactPerson existingContact = ContactPerson.findByEmail(requestData.get("email"));
            	if (StringUtils.isNotEmpty(existingContact.name) && StringUtils.isNotEmpty(existingContact.email)
            			&& StringUtils.isNotBlank(filledForm.get().name) 
            			&& existingContact.email.equals(filledForm.get().email)
            			&& !existingContact.name.equals(filledForm.get().name)) {
            		String msg = "A contact person with email '" + getFormParam(Const.EMAIL) + 
    	  					"' is already in the Contact Persons list, but with the Name '" + existingContact.name + 
    	  					"' which is different from the given name '" + filledForm.get().name + 
    	  					"'. Please review the revised details below and click Save, or enter an alternative contact email address.";
            		
    	            ValidationError e = new ValidationError("email", msg);
    	            filledForm.reject(e);
    	  			return newInfo(filledForm);
            	}
            	
            	filledForm.get().defaultContact = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(Const.DEFAULT_CONTACT));
            	filledForm.get().permissionChecked = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(Const.PERMISSION_CHECKED));

		        filledForm.get().save();
		        flash("message", "Contact Person " + filledForm.get().name + " has been created");
		        return redirect(routes.ContactPersonController.view(filledForm.get().id));
        	}
        }
        return null;    	
    }
    
    public static Result update(Long id) {
    	DynamicForm requestData = form().bindFromRequest();
        Form<ContactPerson> filledForm = form(ContactPerson.class).bindFromRequest();
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
		        
            	ContactPerson existingContact = ContactPerson.findByEmail(requestData.get("email"));
            	if (StringUtils.isNotEmpty(existingContact.name) && StringUtils.isNotEmpty(existingContact.email)
            			&& StringUtils.isNotBlank(filledForm.get().name) 
            			&& existingContact.email.equals(filledForm.get().email)
            			&& !existingContact.name.equals(filledForm.get().name)) {
            		String msg = "A contact person with email '" + getFormParam(Const.EMAIL) + 
    	  					"' is already in the Contact Persons list, but with the Name '" + existingContact.name + 
    	  					"' which is different from the given name '" + filledForm.get().name + 
    	  					"'. Please review the revised details below and click Save, or enter an alternative contact email address.";
            		
    	            ValidationError e = new ValidationError("email", msg);
    	            filledForm.reject(e);
    	  			return info(filledForm, id);
            	}
		        if( existingContact.id == id) {
		        	filledForm.get().crawlPermissions = existingContact.crawlPermissions;
		        }
		        filledForm.get().update(id);
		        flash("message", "Contact Person " + filledForm.get().name + " has been updated");
		        return redirect(routes.ContactPersonController.view(filledForm.get().id));
        	} else if (action.equals("delete")) {
        		ContactPerson contactPerson = ContactPerson.findById(id);
		        flash("message", "Contact Person " + filledForm.get().name + " has been deleted");
            	contactPerson.delete();
            	
        		return redirect(routes.ContactPersonController.index()); 
        	}
        }
        return null;
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


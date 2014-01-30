package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.ContactPerson;
import models.DCollection;
import models.Organisation;
import models.Role;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
//import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.contactpersons.*;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage persons.
 */
@Security.Authenticated(Secured.class)
public class ContactPersonEdit extends AbstractController {
  
    /**
     * Display the person.
     */
    public static Result index() {
        List<ContactPerson> resList = processFilterContactPersons("");
        return ok(
                contactpersons.render(
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
                contactpersonedit.render(
                		models.ContactPerson.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                contactpersonview.render(
                		models.ContactPerson.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("ContactPersonEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<ContactPerson> resList = processFilterContactPersons(name);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.ContactPersonEdit.addEntry(name));
        	} else {
        		Logger.info("ContactPerson name is empty. Please write name in search window.");
                res = ok(
                        contactpersons.render(
                            "ContactPersons", User.find.byId(request().username()), resList, ""
                        )
                    );
        	}
        } else {
            res = ok(
            		contactpersons.render(
                        "ContactPersons", User.find.byId(request().username()), resList, name
                    )
                );
        }
        return res;
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
    public static Result addEntry(String name) {
    	ContactPerson person = new ContactPerson();
    	person.name = name;
        person.id = Target.createId();
        person.url = Const.ACT_URL + person.id;
		Logger.info("add entry with url: " + person.url + ", and name: " + person.name);
        return ok(
                contactpersonedit.render(
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
//        Logger.info("save: " + save);
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
	        res = redirect(routes.ContactPersonEdit.view(person.url));
        } 
        if (delete != null) {
        	ContactPerson person = ContactPerson.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(person);
	        res = redirect(routes.ContactPersonEdit.index()); 
        }
    	res = redirect(routes.ContactPersonEdit.index()); 
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


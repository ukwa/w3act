package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.text.SimpleDateFormat;
import java.util.*;

import models.*;
import views.html.*;
import views.html.licence.*;
import uk.bl.api.*;
import uk.bl.scope.Scope;

import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuilder;
import java.io.FileNotFoundException;

import com.avaje.ebean.Ebean;

import play.libs.Json;
import uk.bl.Const;

/**
 * Support for adding owner licence.
 */
public class LicenceController extends AbstractController {
  
    /**
     * Display the licence form.
     */
    public static Result index() {
		return ok(
            ukwalicence.render()
        );
    }
    
    public static String getCurrentDate() {
    	return Utils.getCurrentDate();
    }
    
    /**
     * This method submits owner settings for UKWA licence.
     * @return
     */
    public static Result submit() {
    	Result res = null;
        Logger.info("Licence controller submit()");
        String submit = getFormParam(Const.SUBMIT);
        Logger.info("submit: " + submit);
        if (submit != null) {
        	Logger.info("save UKWA licence - name: " + getFormParam(Const.NAME));
    		Logger.info("agree: " + getFormParam(Const.AGREE));
            boolean isAgreed = Utils.getNormalizeBooleanString(getFormParam(Const.AGREE));
            boolean noThirdPartyContent = false;
            if (getFormParam(Const.CONTENT) != null) {
        		Logger.info("content: " + getFormParam(Const.CONTENT));
            	Long noThirdPartyContentValue = Long.valueOf(getFormParam(Const.CONTENT));
                if (noThirdPartyContentValue == 1L) {
                	noThirdPartyContent = true;
            	}
            } 
            boolean mayPublish = false;
            if (getFormParam(Const.PUBLISH) != null) {
        		Logger.info("mayPublish: " + getFormParam(Const.PUBLISH));
            	Long mayPublishValue = Long.valueOf(getFormParam(Const.PUBLISH));
                if (mayPublishValue == 1L) {
                	mayPublish = true;
            	}
            } 
        	Logger.info("flags isAgreed: " + isAgreed + ", noThirdPartyContent: " + noThirdPartyContent + ", mayPublish: " + mayPublish);
            if (getFormParam(Const.TARGET) != null && isAgreed && noThirdPartyContent && mayPublish) {
        	    String target = getFormParam(Const.TARGET);
        	    List<CrawlPermission> permissionList = CrawlPermission.filterByTarget(target);
        	    if (permissionList != null && permissionList.size() > 0) {
        	    	CrawlPermission permission = permissionList.get(0);
                	Logger.info("found crawl permission: " + permission);
                    if (getFormParam(Const.NAME) != null) {
                        permission.name = getFormParam(Const.NAME);
                    }
                    if (getFormParam(Const.NAME) != null) {
                        permission.name = getFormParam(Const.NAME);
                    }
                    if (getFormParam(Const.CONTACT_PERSON) != null) {
                        permission.contactPerson = getFormParam(Const.CONTACT_PERSON);
                    }
                    if (getFormParam(Const.DESCRIPTION) != null) {
                        permission.description = getFormParam(Const.DESCRIPTION);
                    }
                    if (getFormParam(Const.CONTACT_PERSON) != null) {
                        permission.contactPerson = getFormParam(Const.CONTACT_PERSON);
                    }
                    if (getFormParam(Const.DATE) != null) {
                        permission.licenseDate = getFormParam(Const.DATE);
                    }
                    permission.status = Const.CrawlPermissionStatus.GRANTED.name();
                	Ebean.update(permission);
        	        Logger.info("update crawl permission: " + permission.toString());                    
        	    }
            } 
            // contact person
            if (getFormParam(Const.CONTACT_PERSON) != null) {
                String ownerName = getFormParam(Const.CONTACT_PERSON);
                ContactPerson contactPerson = null;
                try {
                	contactPerson = ContactPerson.findByName(ownerName);
                	Logger.info("found contact person: " + contactPerson);
                    if (getFormParam(Const.POSITION) != null) {
                        contactPerson.position = getFormParam(Const.POSITION);
                    }
                    if (getFormParam(Const.EMAIL) != null) {
                        contactPerson.email = getFormParam(Const.EMAIL);
                    }
                    if (getFormParam(Const.PHONE) != null) {
                        contactPerson.phone = getFormParam(Const.PHONE);
                    }
                	// update existing contact person
                	Ebean.update(contactPerson);
        	        Logger.info("update contact person: " + contactPerson.toString());
                } catch (Exception e) {
                	System.out.println("Owner not found: " + ownerName);
                }
                if (contactPerson == null) {
                	// create new contact person
                	ContactPerson person = new ContactPerson();
                	person.name = ownerName;
                    person.id = Target.createId();
                    person.url = Const.ACT_URL + person.id;
                    if (getFormParam(Const.POSITION) != null) {
                        person.position = getFormParam(Const.POSITION);
                    }
                    if (getFormParam(Const.EMAIL) != null) {
                        person.email = getFormParam(Const.EMAIL);
                    }
                    if (getFormParam(Const.PHONE) != null) {
                        person.phone = getFormParam(Const.PHONE);
                    }
                	Ebean.save(person);
        	        Logger.info("save contact person: " + person.toString());
                }
            }
	        res = redirect(routes.LicenceController.result());
        } 
        return res;
    }	   
	
    /**
     * Display the result of th licence form submission.
     */
    public static Result result() {
		return ok(
            ukwalicenceresult.render()
        );
    }
    
    
}


package controllers;

import java.util.Iterator;
import java.util.List;

import models.CommunicationLog;
import models.ContactPerson;
import models.CrawlPermission;
import models.Target;
import models.Taxonomy;
import play.Logger;
import play.mvc.Result;
import uk.bl.Const;
import uk.bl.api.Utils;
import views.html.licence.ukwalicence;
import views.html.licence.ukwalicenceresult;
import views.html.licence.ukwalicenceview;

import com.avaje.ebean.Ebean;

/**
 * Support for adding owner licence.
 */
public class LicenceController extends AbstractController {
  
    /**
     * Display the licence form.
     */
    public static Result index() {
		return ok(
            ukwalicence.render("")
        );
    }
    
    /**
     * This method presents licence form for selected premission request
     * that is identified by the given permission URL. 
     * @param permissionUrl
     * @return
     */
    public static Result form(String permissionUrl) {
		return ok(
            ukwalicence.render(permissionUrl)
        );
    }
    
    /**
     * This method presents licence form view for selected premission request
     * that is identified by the given permission URL. All fields are disabled. 
     * @param permissionUrl
     * @return
     */
    public static Result formview(String permissionUrl) {
    	Logger.info("formview: " + permissionUrl);
		return ok(
            ukwalicenceview.render(permissionUrl)
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
            if (getFormParam(Const.TARGET) != null) {
        	    String target = getFormParam(Const.TARGET);
        	    List<CrawlPermission> permissionList = CrawlPermission.filterByTarget(target);
        	    if (permissionList != null && permissionList.size() > 0) {
        	    	CrawlPermission permission = permissionList.get(0);
                	Logger.info("found crawl permission: " + permission);
            	    permission.target = target;
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
                            permission.contactPerson = contactPerson.url;
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
                            permission.contactPerson = person.url;
                        }
                    }
                    if (getFormParam(Const.LOG_DATE) != null) {
                        permission.licenseDate = getFormParam(Const.LOG_DATE);
                    }
                    if (isAgreed) {
//                        if (isAgreed && noThirdPartyContent && mayPublish) {
                    	permission.status = Const.CrawlPermissionStatus.GRANTED.name();
                    } else {
                    	permission.status = Const.CrawlPermissionStatus.REFUSED.name();
                    }
                    permission.agree = isAgreed;
                    permission.thirdPartyContent = noThirdPartyContent;
                    permission.publish = mayPublish;
                	Ebean.update(permission);
        	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
        	        Ebean.save(log);
        	        try {
	                    if (getFormParam(Const.LICENCE) != null) {
	                    	String licenceName = getFormParam(Const.LICENCE);
	                    	Taxonomy licenceTaxonomy = Taxonomy.findByName(licenceName);
	                    	Logger.info("Permission target: " + permission.target);
	                    	Target targetObj = Target.findByTarget(permission.target);
	                    	Logger.info("Target object: " + targetObj);
	                    	if (targetObj != null) {
	                    		targetObj.field_license = licenceTaxonomy.url;
	                    		Ebean.update(targetObj);
	                    		// lookup for all targets with lower level and update licence
	                    		List<Target> associatedTargetList = Target.findAllTargetsWithLowerLevel(target);
	                    		Iterator<Target> itr = associatedTargetList.iterator();
	                    		while (itr.hasNext()) {
	                    			Target current_target = itr.next();
	                    	    	if (current_target.field_url != null) {
	                    	    		if (current_target.field_url.contains(Const.SLASH_DELIMITER)) {
	                    			    	String[] parts = current_target.field_url.split(Const.SLASH_DELIMITER);
	                    			    	if (parts[0].equals(permission.target)) {
				                    			current_target.field_license = licenceTaxonomy.url;
					                    		Ebean.update(current_target);
	                    			    	}
	                    	    		}
	                    	    	}
	                    		}
	                    	}
	                    }
        	        } catch (Exception e) {
        	        	Logger.info("Update target for licence failed. " + e);
        	        }
        	        Logger.info("update crawl permission: " + permission.toString());                    
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


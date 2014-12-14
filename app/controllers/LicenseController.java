package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import models.CommunicationLog;
import models.ContactPerson;
import models.CrawlPermission;
import models.MailTemplate;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.licence.licences;
import views.html.licence.ukwalicence;
import views.html.licence.ukwalicenceresult;
import views.html.licence.ukwalicenceview;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Support for adding owner licence.
 */
public class LicenseController extends AbstractController {
  
    /**
     * Display the licence form.
     */
    public static Result index() {
		return ok(
            ukwalicence.render("", "", "", "", "", "", "", "", "", "")
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
            ukwalicence.render(permissionUrl, CrawlPermission.showByUrl(permissionUrl).name, 
            		CrawlPermission.showByUrl(permissionUrl).target.title, "", "", "", "", "", "", "")
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
     * Send acknowledgment to the site owner
     * @param ownerEmail
     * @param permission The crawl permission that comprises outputs the data entered into the licence form
     */
    public static void sendAcknowledgementToSiteOwner(String ownerEmail, CrawlPermission permission) {
    	MailTemplate mailTemplate = MailTemplate.findByName(Const.ACKNOWLEDGEMENT);
    	Logger.debug("sendAcknowledgementToSiteOwner mailTemplate: " + mailTemplate);
    	String messageSubject = mailTemplate.subject;
//    	Logger.debug("sendAcknowledgementToSiteOwner text: " + mailTemplate.text);
//    	String messageBody = mailTemplate.readTemplate();
    	String messageBody = mailTemplate.text;
		StringBuilder sb = new StringBuilder();
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.LICENCE_ACK + Const.TWO_POINTS + permission.license + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.WEBSITE_TITLE_ACK + Const.TWO_POINTS + permission.name + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.WEB_ADDRESS_ACK + Const.TWO_POINTS + permission.target.title + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.NAME_ACK + Const.TWO_POINTS + permission.contactPerson.name + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.POSITION_ACK + Const.TWO_POINTS + permission.contactPerson.position + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.EMAIL_ACK + Const.TWO_POINTS + permission.contactPerson.email + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.CONTACT_ORGANISATION_ACK + Const.TWO_POINTS + permission.contactPerson.contactOrganisation + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.TEL_ACK + Const.TWO_POINTS + permission.contactPerson.phone + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.POSTAL_ADDRESS_ACK + Const.TWO_POINTS + permission.contactPerson.postalAddress + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.DESCRIPTION_ACK + Const.TWO_POINTS + permission.anyOtherInformation + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.THIRD_PARTY_ACK + Const.TWO_POINTS + Utils.showBooleanAsString(permission.thirdPartyContent) + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.AGREE_ACK + Const.TWO_POINTS + Utils.showBooleanAsString(permission.agree) + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.DATE_ACK + Const.TWO_POINTS + permission.createdAt + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.PUBLICITY_ACK + Const.TWO_POINTS + Utils.showBooleanAsString(permission.publish) + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
    	messageBody = CrawlPermission.
            	replaceStringInText(
            			messageBody
						, Const.PLACE_HOLDER_DELIMITER + mailTemplate.placeHolders + Const.PLACE_HOLDER_DELIMITER
						, sb.toString());
    	Logger.debug("sendAcknowledgementToSiteOwner messageBody: " + messageBody);
        EmailHelper.sendMessage(ownerEmail, messageSubject, messageBody);                	
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
        	if (StringUtils.isBlank(getFormParam(Const.TARGET)) 
        			|| StringUtils.isBlank(getFormParam(Const.NAME))
        			|| StringUtils.isBlank(getFormParam(Const.POSITION))
        			|| StringUtils.isBlank(getFormParam(Const.CONTACT_PERSON))
        			|| StringUtils.isBlank(getFormParam(Const.EMAIL))) {
    			Logger.info("One of the required fields is empty. Please fill out all required fields marked by red star in the form.");
    			flash("message", "Please fill out all required fields marked by red star in the form");
    			return ok(
    		            ukwalicence.render(getFormParam(Const.URL), getFormParam(Const.NAME), 
    	   						getFormParam(Const.TARGET), getFormParam(Const.CONTACT_PERSON), getFormParam(Const.POSITION), 
    	   						getFormParam(Const.EMAIL), getFormParam(Const.POSTAL_ADDRESS), 
    	   						getFormParam(Const.CONTACT_ORGANISATION), getFormParam(Const.PHONE), 
    	   						getFormParam(Const.DESCRIPTION))
    		        );
        	}  
        	Logger.info("save UKWA licence - name: " + getFormParam(Const.NAME));
    		Logger.info("agree: " + getFormParam(Const.AGREE));
            boolean isAgreed = Utils.getNormalizeBooleanString(getFormParam(Const.AGREE));
    		if (!isAgreed || StringUtils.isBlank(getFormParam(Const.CONTENT)) 
        			|| StringUtils.isBlank(getFormParam(Const.PUBLISH))) {
    			Logger.info("The form cannot be submitted without selecting 'Yes' for field 'I/We agree' and selecting fields 'Third-Party Content' and 'publicity for the Web Archive'. Please agree for licence granting.");
    			flash("message", "The form cannot be submitted without selecting 'Yes' for field 'I/We agree' and selecting fields 'Third-Party Content' and 'publicity for the Web Archive'. Please agree for licence granting.");
    			return ok(
    		            ukwalicence.render(getFormParam(Const.URL), getFormParam(Const.NAME), 
    	   						getFormParam(Const.TARGET), getFormParam(Const.CONTACT_PERSON), getFormParam(Const.POSITION), 
    	   						getFormParam(Const.EMAIL), getFormParam(Const.POSTAL_ADDRESS), 
    	   						getFormParam(Const.CONTACT_ORGANISATION), getFormParam(Const.PHONE), 
    	   						getFormParam(Const.DESCRIPTION))
    		        );
    		}
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
            	    permission.target.title = target;
                    if (getFormParam(Const.NAME) != null) {
                        permission.name = getFormParam(Const.NAME);
                    }
                    if (getFormParam(Const.CONTACT_PERSON) != null) {
                        permission.contactPerson.name = getFormParam(Const.CONTACT_PERSON);
                    }
                    if (getFormParam(Const.DESCRIPTION) != null) {
                        permission.anyOtherInformation = getFormParam(Const.DESCRIPTION);
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
                            if (getFormParam(Const.POSTAL_ADDRESS) != null) {
                                contactPerson.postalAddress = getFormParam(Const.POSTAL_ADDRESS);
                            }
                            if (getFormParam(Const.CONTACT_ORGANISATION) != null) {
                                contactPerson.contactOrganisation = getFormParam(Const.CONTACT_ORGANISATION);
                            }
                            if (getFormParam(Const.PHONE) != null) {
                                contactPerson.phone = getFormParam(Const.PHONE);
                            }
                        	// update existing contact person
                        	Ebean.update(contactPerson);
                	        Logger.info("update contact person: " + contactPerson.toString());
                            permission.contactPerson = contactPerson;
                        } catch (Exception e) {
                        	Logger.error("Owner not found: " + ownerName);
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
                            if (getFormParam(Const.POSTAL_ADDRESS) != null) {
                                person.postalAddress = getFormParam(Const.POSTAL_ADDRESS);
                            }
                            if (getFormParam(Const.CONTACT_ORGANISATION) != null) {
                                person.contactOrganisation = getFormParam(Const.CONTACT_ORGANISATION);
                            }
                            if (getFormParam(Const.PHONE) != null) {
                                person.phone = getFormParam(Const.PHONE);
                            }
                        	Ebean.save(person);
                	        Logger.info("save contact person: " + person.toString());
                            permission.contactPerson = person;
                        }
                    }

                    if (getFormParam(Const.LICENCE) != null) {
                    	permission.license.name = getFormParam(Const.LICENCE);
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
        	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission, permission.user, Const.UPDATE);
        	        Ebean.save(log);
        	        CrawlPermissionController.updateAllByTarget(permission.url, permission.target.title, permission.status);
        	        TargetController.updateQaStatus(permission.target.title, permission.status);
        	    	Logger.debug("before sendAcknowledgementToSiteOwner mailTemplate: " + getFormParam(Const.EMAIL));
        	        if (getFormParam(Const.EMAIL) != null) {
        	        	sendAcknowledgementToSiteOwner(getFormParam(Const.EMAIL), permission);
        	        }
        	        try {
	                    if (getFormParam(Const.LICENCE) != null) {
	                    	String licenceName = getFormParam(Const.LICENCE);
	                    	Taxonomy licenceTaxonomy = Taxonomy.findByName(licenceName);
	                    	Logger.info("Permission target: " + permission.target.title);
	                    	Target targetObj = Target.findByTarget(permission.target.title);
	                    	Logger.info("Target object: " + targetObj);
	                    	if (targetObj != null) {
//	                    		targetObj.fieldLicense = licenceTaxonomy.url;
	                    		Ebean.update(targetObj);
	                    		// lookup for all targets with lower level and update licence
	                    		List<Target> associatedTargetList = Target.findAllTargetsWithLowerLevel(target);
	                    		Iterator<Target> itr = associatedTargetList.iterator();
	                    		while (itr.hasNext()) {
	                    			Target current_target = itr.next();
	                    	    	if (current_target.fieldUrl() != null) {
	                    	    		if (current_target.fieldUrl().contains(Const.SLASH_DELIMITER)) {
	                    			    	String[] parts = current_target.fieldUrl().split(Const.SLASH_DELIMITER);
	                    			    	if (parts[0].equals(permission.target.title)) {
//				                    			current_target.fieldLicense = licenceTaxonomy.url;
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
	        res = redirect(routes.LicenseController.result());
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
    
    /**
     * Display the person.
     */
    public static Result indexFilter() {
        List<Taxonomy> resList = processFilterLicences("");
        return ok(
                licences.render(
                    "Licences", User.findByEmail(request().username()), resList, ""
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
    	Logger.info("LicenseController.filter()");
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<Taxonomy> resList = processFilterLicences(name);
        Logger.info("search: " + search + ", name: " + name);
        if (search != null) {
            res = ok(
            		licences.render(
                        "Licences", User.findByEmail(request().username()), resList, name
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
    public static List<Taxonomy> processFilterLicences(String filterUrl) {
//    	Logger.info("process filter filterUrl: " + filterUrl);
    	boolean isProcessed = false;
    	ExpressionList<Taxonomy> exp = Taxonomy.find.where();
    	List<Taxonomy> res = new ArrayList<Taxonomy>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);    		
    		exp = exp.contains(Const.TTYPE, Const.LICENCE);
    		isProcessed = true;
    	}
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.Taxonomy.findListByType(Const.LICENCE);
    	}
        return res;
    }        

    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<Taxonomy> licences = Taxonomy.filterByName(name);
	        jsonData = Json.toJson(licences);
        }
        return ok(jsonData);
    }
    
    
}


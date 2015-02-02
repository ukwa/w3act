package controllers;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import models.CommunicationLog;
import models.ContactPerson;
import models.CrawlPermission;
import models.FieldUrl;
import models.License;
import models.MailTemplate;
import models.Target;
import models.Taxonomy;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.FormHelper;
import uk.bl.api.Utils;
import uk.bl.exception.ActException;
import uk.bl.scope.EmailHelper;
import views.html.licence.licences;
import views.html.licence.ukwalicence;
import views.html.licence.ukwalicenceresult;
import views.html.licence.ukwalicenceview;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Support for adding owner licence.
 */
@Security.Authenticated(SecuredController.class)
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
     * @throws ActException 
     */
    public static Result form(String token) throws ActException {
    	CrawlPermission crawlPermission = CrawlPermission.showByToken(token);
    	if (crawlPermission == null) {
    		throw new ActException("CrawlPermission Not Found found for token: " + token);
    	}

		return ok(
            ukwalicence.render(crawlPermission.url, crawlPermission.name, 
            		crawlPermission.target.title, "", "", "", "", "", "", "")
        );
    }
    
    /**
     * This method presents licence form view for selected premission request
     * that is identified by the given permission URL. All fields are disabled. 
     * @param permissionUrl
     * @return
     * @throws ActException 
     */
    public static Result formview(String token) throws ActException {
    	CrawlPermission crawlPermission = CrawlPermission.showByToken(token);
    	if (crawlPermission == null) {
    		throw new ActException("CrawlPermission Not Found found for token: " + token);
    	}
    	
		return ok(
            ukwalicenceview.render(crawlPermission.url, crawlPermission.target.fieldUrl())
        );
    }
    
    public static String getCurrentDate() {
    	return Utils.INSTANCE.getCurrentDate();
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
		sb.append(Const.THIRD_PARTY_ACK + Const.TWO_POINTS + Utils.INSTANCE.showBooleanAsString(permission.thirdPartyContent) + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.AGREE_ACK + Const.TWO_POINTS + Utils.INSTANCE.showBooleanAsString(permission.agree) + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.DATE_ACK + Const.TWO_POINTS + permission.createdAt + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		sb.append(Const.PUBLICITY_ACK + Const.TWO_POINTS + Utils.INSTANCE.showBooleanAsString(permission.publish) + Const.CSV_LINE_END);
		sb.append(Const.CSV_LINE_END);
		
    	messageBody = mailTemplate.readTemplate();
		
//    	messageBody = CrawlPermission.
//            	replaceStringInText(
//            			messageBody
//						, Const.PLACE_HOLDER_DELIMITER + mailTemplate.placeHolders + Const.PLACE_HOLDER_DELIMITER
//						, sb.toString());
    	Logger.debug("sendAcknowledgementToSiteOwner messageBody: " + messageBody);
        EmailHelper.sendMessage(ownerEmail, messageSubject, messageBody);                	
    }
    
    /**
     * This method submits owner settings for UKWA licence.
     * @return
     * @throws ActException 
     */
    public static Result submit() throws ActException {
    	Result res = null;
        Logger.debug("Licence controller submit()");
        
        DynamicForm requestData = Form.form().bindFromRequest();
    	
    	String action = requestData.get("action");
        
    	try {
	    	if (StringUtils.isNotEmpty(action)) {
	        
		        if (action.equals("submit")) {
		        	String actUrl = requestData.get("url");
		        	CrawlPermission permission = CrawlPermission.findByUrl(actUrl);
		        	
		        	if (StringUtils.isBlank(getFormParam(Const.TARGET)) 
		        			|| StringUtils.isBlank(getFormParam(Const.NAME))
		        			|| StringUtils.isBlank(getFormParam(Const.POSITION))
		        			|| StringUtils.isBlank(getFormParam(Const.CONTACT_PERSON))
		        			|| StringUtils.isBlank(getFormParam(Const.EMAIL))) {
		    			Logger.debug("One of the required fields is empty. Please fill out all required fields marked by red star in the form.");
		    			flash("message", "Please fill out all required fields marked by red star in the form");
		    			
		    			return ok(
		    		            ukwalicence.render(getFormParam(Const.URL), getFormParam(Const.NAME), 
		    	   						getFormParam(Const.TARGET), getFormParam(Const.CONTACT_PERSON), getFormParam(Const.POSITION), 
		    	   						getFormParam(Const.EMAIL), getFormParam(Const.POSTAL_ADDRESS), 
		    	   						getFormParam(Const.CONTACT_ORGANISATION), getFormParam(Const.PHONE), 
		    	   						getFormParam(Const.DESCRIPTION))
		    		        );
		        	}  
		        	Logger.debug("save UKWA licence - name: " + getFormParam(Const.NAME));
		    		Logger.debug("agree: " + getFormParam(Const.AGREE));
		            boolean isAgreed = Utils.INSTANCE.getNormalizeBooleanString(getFormParam(Const.AGREE));
		    		if (!isAgreed || StringUtils.isBlank(getFormParam(Const.CONTENT)) 
		        			|| StringUtils.isBlank(getFormParam(Const.PUBLISH))) {
		    			Logger.debug("The form cannot be submitted without checking the 'I/We agree' box, or without answering the questions about 'Third-Party Content' and 'Publicity for the Web Archive'. Please check your input and try again.");
		    			flash("message", "The form cannot be submitted without checking the 'I/We agree' box, or without answering the questions about 'Third-Party Content' and 'Publicity for the Web Archive'. Please check your input and try again.");
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
		        		Logger.debug("content: " + getFormParam(Const.CONTENT));
		            	Long noThirdPartyContentValue = Long.valueOf(getFormParam(Const.CONTENT));
		                if (noThirdPartyContentValue == 1L) {
		                	noThirdPartyContent = true;
		            	}
		            } 
		            boolean mayPublish = false;
		            if (getFormParam(Const.PUBLISH) != null) {
		        		Logger.debug("mayPublish: " + getFormParam(Const.PUBLISH));
		            	Long mayPublishValue = Long.valueOf(getFormParam(Const.PUBLISH));
		                if (mayPublishValue == 1L) {
		                	mayPublish = true;
		            	}
		            } 
		            
		        	Logger.debug("flags isAgreed: " + isAgreed + ", noThirdPartyContent: " + noThirdPartyContent + ", mayPublish: " + mayPublish);
		        	
		        	// we already have target
		        	
	            	Logger.debug("found crawl permission: " + permission);
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
	                    
	                	contactPerson = ContactPerson.findByName(ownerName);
	                	if (contactPerson != null) {
	                    	Logger.debug("found contact person: " + contactPerson);
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
	                        contactPerson.update();
	            	        Logger.debug("update contact person: " + contactPerson.toString());
	                	} else {
	                    	// create new contact person
	                		contactPerson = new ContactPerson();
	                		
	                		contactPerson.name = ownerName;
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
	                        contactPerson.save();
	            	        Logger.debug("save contact person: " + contactPerson.toString());
	                    }
	                    permission.contactPerson = contactPerson;
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
	                
	                
	    	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission, permission.user, Const.UPDATE);
	    	        log.save();
	    	        CrawlPermissionController.updateAllByTarget(permission.id, permission.target.id, permission.status);
	    	    	Logger.debug("before sendAcknowledgementToSiteOwner mailTemplate: " + getFormParam(Const.EMAIL));
	    	        if (getFormParam(Const.EMAIL) != null) {
	    	        	sendAcknowledgementToSiteOwner(getFormParam(Const.EMAIL), permission);
	    	        }
	    	        
	                if (getFormParam(Const.LICENCE) != null) {
	                	String licenceName = getFormParam(Const.LICENCE);
	                	License license = License.findByName(licenceName);
	                	permission.license = license;
	                	
	                	Target target = permission.target;
	                	target.licenses.add(license);
	                	target.licenseStatus = permission.status;
	                	target.update();
	
	            		// lookup for all targets with lower level and update licence
	                	for (FieldUrl fieldUrl : target.fieldUrls) {
	                    	Set<Target> lowerTargets = FormHelper.getLowerTargets(fieldUrl);
	                    	for (Target lowerTarget : lowerTargets) {
	                    		lowerTarget.licenses.add(license);
	                    		lowerTarget.licenseStatus = permission.status;
	                    		lowerTarget.update();
	                    	}
	                	}
	                    	
	                }
	                permission.update();
	    	        Logger.debug("update crawl permission: " + permission.toString());                    
			        res = redirect(routes.LicenseController.result());
		        }
	        } 
        } catch (Exception e) {
        	Logger.debug("Update target for licence failed. " + e);
        	throw new ActException(e);
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
    	User user = User.findByEmail(request().username());
        List<License> licenses = License.findAllLicenses();
        String searchStr = "";
        Logger.debug("user: " + user);
        Logger.debug("licenses: " + licenses);
        Logger.debug("searchStr: " + searchStr);
        return ok(licences.render("Licences", user, licenses, searchStr));
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.debug("LicenseController.filter()");
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);

        List<License> licenses = processFilterLicences(name);

        Logger.debug("search: " + search + ", name: " + name);
        if (search != null) {
            res = ok(
            		licences.render(
                        "Licences", User.findByEmail(request().username()), licenses, name
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
    public static List<License> processFilterLicences(String filterUrl) {
//    	Logger.debug("process filter filterUrl: " + filterUrl);
    	ExpressionList<License> exp = License.find.where();
    	if (StringUtils.isNotBlank(filterUrl)) {
    		exp = exp.contains("name", filterUrl);    		
    	}
    	List<License> res = exp.query().findList();
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


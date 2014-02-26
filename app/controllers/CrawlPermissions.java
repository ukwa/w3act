package controllers;

import static play.data.Form.form;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.CrawlPermission;

import models.Target;
import models.User;
import models.ContactPerson;
import models.MailTemplate;
import models.CommunicationLog;
import play.Logger;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import uk.bl.api.Utils;
import uk.bl.scope.EmailHelper;
import views.html.contactpersons.*;
import views.html.mailtemplates.*;
import views.html.crawlpermissions.*;
import views.html.licence.*;
import views.html.refusals.*;
import views.html.communicationlogs.*;

import java.util.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Manage permissions.
 */
@Security.Authenticated(Secured.class)
public class CrawlPermissions extends AbstractController {
  
    /**
     * Display the permission.
     */
    public static Result index() {
        List<CrawlPermission> resList = processFilterCrawlPermissions("", Const.DEFAULT_CRAWL_PERMISSION_STATUS, "");
        return ok(
                crawlpermissions.render(
                    "CrawlPermissions", User.find.byId(request().username()), resList, "", Const.DEFAULT_CRAWL_PERMISSION_STATUS
                )
            );
    }

    /**
     * Display the permission edit panel for this URL.
     */
    public static Result edit(String url) {
		Logger.info("permission url: " + url);
		CrawlPermission permission = CrawlPermission.findByUrl(url);
		Logger.info("permission name: " + permission.name + ", url: " + url);
        return ok(
                crawlpermissionedit.render(
                		models.CrawlPermission.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    public static Result view(String url) {
        return ok(
                crawlpermissionview.render(
                		models.CrawlPermission.findByUrl(url), User.find.byId(request().username())
                )
            );
    }
    
    /**
     * This method shows crawl permissions associated with given target. It is called from
     * target edit page.
     * @param targetUrl
     * @return
     */
    public static Result showCrawlPermissions(String targetUrl) {
    	Result res = null;
    	String target = "";
    	if (targetUrl != null && targetUrl.length() > 0) {
    		target = Target.findByUrl(targetUrl).field_url;
    	}
    	Logger.info("showCrawlPermissions: " + targetUrl + ", target: " + target);
        List<CrawlPermission> resList = processFilterCrawlPermissions("", "", target);
    	Logger.info("showCrawlPermissions count: " + resList.size());
        res = ok(
        		crawlpermissions.render(
                    "CrawlPermissions", User.find.byId(request().username()), resList, "", ""
                )
            );
        return res;    	
    }
    
    /**
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result search() {
    	DynamicForm form = form().bindFromRequest();
    	String action = form.get("action");
    	String name = form.get(Const.NAME);
        String status = form.get(Const.STATUS);
        
        if (status == null) {
        	status = Const.DEFAULT_CRAWL_PERMISSION_STATUS;
        }

        List<CrawlPermission> resList = processFilterCrawlPermissions(name, status, "");

        if (StringUtils.isBlank(name)) {
			Logger.info("Organisation name is empty. Please write name in search window.");
			flash("message", "Please enter a name in the search window");
			return ok(
            		crawlpermissions.render(
                        "CrawlPermissions", User.find.byId(request().username()), resList, "", status
                    )
                );
		}

    	if (StringUtils.isEmpty(action)) {
    		return badRequest("You must provide a valid action");
    	} else {
    		if (Const.ADDENTRY.equals(action)) {
        		return redirect(routes.CrawlPermissions.create(name));
    		} 
    		else if (Const.SEARCH.equals(action)) {
    			return ok(
                		crawlpermissions.render(
                            "CrawlPermissions", User.find.byId(request().username()), resList, name, status
                        )
                    );
		    } else {
		      return badRequest("This action is not allowed");
		    }
    	}
    }	   
    
    /**
     * This method applyies filters to the list of crawl permissions.
     * @param filterUrl The search string
     * @param status The status of the permission workflow
     * @param target The domain name (URL)
     * @return
     */
    public static List<CrawlPermission> processFilterCrawlPermissions(String filterUrl, String status, String target) {
//    	Logger.info("process filter filterUrl: " + filterUrl + ", status: " + status);
    	boolean isProcessed = false;
    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
    	List<CrawlPermission> res = new ArrayList<CrawlPermission>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE) && filterUrl.length() > 0) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	if (status != null && !status.toLowerCase().equals(Const.NONE) && status.length() > 0) {
    		Logger.info("status: " + status);
    		exp = exp.eq(Const.STATUS, status);
    		isProcessed = true;
    	} 
    	if (target != null && !target.toLowerCase().equals(Const.NONE) && target.length() > 0) {
    		Logger.info("target: " + target);
    		exp = exp.eq(Const.TARGET, target);
    		isProcessed = true;
    	} 
    	res = exp.query().findList();
    	Logger.info("Expression list size: " + res.size() + ", isProcessed: " + isProcessed);

        if (!isProcessed) {
    		res = models.CrawlPermission.findAll();
    	}
        return res;
    }
        
    /**
     * Add new permission entry.
     * @param permission title
     * @return
     */
    public static Result create(String name) {
    	CrawlPermission permission = new CrawlPermission();
    	permission.name = name;
        permission.id = Target.createId();
        permission.url = Const.ACT_URL + permission.id;
        permission.creatorUser = User.find.byId(request().username()).url;
        permission.status = Const.CrawlPermissionStatus.QUEUED.name();
        permission.template = Const.MailTemplateType.PERMISSION_REQUEST.name();
        permission.thirdPartyContent = false;
        permission.agree = false;
        permission.publish = true;
		Logger.info("add entry with url: " + permission.url + ", and name: " + permission.name);
        return ok(
                crawlpermissionedit.render(
                      permission, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * Create new crawl permission request for particular target.
     * @param permission title
     * @param target
     * @return
     */
    public static Result licenceRequestForTarget(String name, String target) {
    	CrawlPermission permission = new CrawlPermission();
    	permission.name = name;
        permission.id = Utils.createId();
        permission.url = Const.ACT_URL + permission.id;
        permission.creatorUser = User.find.byId(request().username()).url;
        permission.status = Const.CrawlPermissionStatus.QUEUED.name();
        permission.template = Const.MailTemplateType.PERMISSION_REQUEST.name();
        permission.target = target;
		Logger.info("add entry with url: " + permission.url + ", name: " + permission.name + ", and target: " + permission.target);
        return ok(
                crawlpermissionedit.render(
                      permission, User.find.byId(request().username())
                )
            );
    }
      
    /**
     * This method saves new object or changes on given Permission in the same object
     * completed by revision comment. The "version" field in the Permission object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save permission id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME));
        	CrawlPermission permission = null;
            boolean isExisting = true;
            try {
                try {
                	permission = CrawlPermission.findByUrl(getFormParam(Const.URL));
                } catch (Exception e) {
                	Logger.info("is not existing exception");
                	isExisting = false;
                	permission = new CrawlPermission();
                	permission.id = Long.valueOf(getFormParam(Const.ID));
                	permission.url = getFormParam(Const.URL);
                }
                if (permission == null) {
                	Logger.info("is not existing");
                	isExisting = false;
                	permission = new CrawlPermission();
                	permission.id = Long.valueOf(getFormParam(Const.ID));
                	permission.url = getFormParam(Const.URL);
                }
                
                permission.name = getFormParam(Const.NAME);
        	    if (getFormParam(Const.DESCRIPTION) != null) {
        	    	permission.description = getFormParam(Const.DESCRIPTION);
        	    }
        	    if (getFormParam(Const.FILTER) != null) {
        	    	permission.target = getFormParam(Const.FILTER);
        	    }
                if (getFormParam(Const.CONTACT_PERSON) != null) {
                	if (!getFormParam(Const.CONTACT_PERSON).toLowerCase().contains(Const.NONE)) {
    	            	String[] contactPersons = getFormParams(Const.CONTACT_PERSON);
    	            	String resContactPersons = "";
    	            	for (String contactPerson : contactPersons)
    	                {
    	            		if (contactPerson != null && contactPerson.length() > 0) {
//    	                		Logger.info("add contactPerson: " + contactPerson);
    	                		resContactPersons = resContactPersons + ContactPerson.findByName(contactPerson).url + Const.LIST_DELIMITER;
    	            		}
    	                }
    	            	permission.contactPerson = resContactPersons;
                	} else {
                		permission.contactPerson = Const.NONE;
                	}
                }
        	    
        	    Logger.info("creator user: " + getFormParam(Const.CREATOR_USER));
//        	    Logger.info("creator user url: " + User.findByName(getFormParam(Const.CREATOR_USER)).url);
        	    if (getFormParam(Const.CREATOR_USER) != null) {
        	    	permission.creatorUser = User.findByName(getFormParam(Const.CREATOR_USER)).url;
        	    }
        	    if (getFormParam(Const.TEMPLATE) != null) {
        	    	permission.template = getFormParam(Const.TEMPLATE);
        	    }
        	    if (getFormParam(Const.STATUS) != null) {
        	    	permission.status = getFormParam(Const.STATUS);
        	    }
        	    if (getFormParam(Const.REQUEST_FOLLOW_UP) != null) {
        	    	permission.requestFollowup = Utils.getNormalizeBooleanString(getFormParam(Const.REQUEST_FOLLOW_UP));
        	    }

            } catch (Exception e) {
            	Logger.info("CrawlPermission not existing exception");
            }
            
        	if (!isExisting) {
                permission.thirdPartyContent = false;
                permission.agree = false;
                permission.publish = true;
               	Ebean.save(permission);
    	        Logger.info("save crawl permission: " + permission.toString());
    	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.SAVE);
    	        Ebean.save(log);
        	} else {
           		Logger.info("update crawl permission: " + permission.toString());
               	Ebean.update(permission);
               	CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
    	        Ebean.save(log);
        	}
	        res = redirect(routes.CrawlPermissions.view(permission.url));
        } 
        if (delete != null) {
        	CrawlPermission permission = CrawlPermission.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(permission);
	        res = redirect(routes.CrawlPermissions.index()); 
        }
    	res = redirect(routes.CrawlPermissions.index()); 
        return res;
    }	   

    public static Result templates() {
        return ok(
                mailtemplates.render(
                    "MailTemplates", User.find.byId(request().username()), models.MailTemplate.findAll(), "", ""
                )
            );
    }
    
    public static Result contactpersons() {
        return ok(
                contactpersons.render(
                    "ContactPersons", User.find.byId(request().username()), models.ContactPerson.findAll(), ""
                )
            );
    }
    
    public static Result licences() {
        return ok(
                licences.render(
                	"Licences", User.find.byId(request().username()), models.Taxonomy.findListByType("license"), ""
                )
            );
    }

    public static Result refusals() {
        return ok(
                refusals.render(
                	"Refusals", User.find.byId(request().username()), models.PermissionRefusal.findAll(), ""
                )
            );
    }

    public static Result communicationLogs() {
        return ok(
                communicationlogs.render(
                	"CommunicationLogs", User.find.byId(request().username()), models.CommunicationLog.findAll(), "", ""
                )
            );
    }

    /**
     * This method selects crawl permissions selected using checkboxes 
     * on the crawl permission overview page.
     * @return
     */
    public static String getAssignedPermissions() {
    	String assignedPermissions = "";
		try {		   	
		    List<CrawlPermission> permissionList = CrawlPermission.findAll();
		    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
		    while (permissionItr.hasNext()) {
		    	CrawlPermission permission = permissionItr.next();
		        if (getFormParam(permission.name) != null) {
		    		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		            boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
		            if (userFlag) {
		            	if (assignedPermissions.length() == 0) {
		            		assignedPermissions = permission.name;
		            	} else {
		            		assignedPermissions = assignedPermissions + Const.COMMA + " " + permission.name;
		            	}
		            }
		        }
		    }
			Logger.info("assignedPermissions: " + assignedPermissions);
		} catch (Exception e) {
			Logger.info("send some exception" + e);
		}    
		return assignedPermissions;
    }
    
    /**
     * This method selects crawl permissions selected using checkboxes 
     * on the crawl permission overview page.
     * @return
     */
    public static List<CrawlPermission> getAssignedPermissionsList() {
    	List<CrawlPermission> assignedPermissionsList = new ArrayList<CrawlPermission>();
		try {		   	
		    List<CrawlPermission> permissionList = CrawlPermission.findAll();
		    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
		    while (permissionItr.hasNext()) {
		    	CrawlPermission permission = permissionItr.next();
		        if (getFormParam(permission.name) != null) {
		    		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
		            boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
		            if (userFlag) {
	            		assignedPermissionsList.add(CrawlPermission.findByName(permission.name));
		            }
		        }
		    }
			Logger.info("assignedPermissions: " + assignedPermissionsList);
		} catch (Exception e) {
			Logger.info("send some exception" + e);
		}    
		return assignedPermissionsList;
    }
    
    /**
     * This method aggregates 'to' emails.
     * @return
     */
    public static String evaluateToEmails() {
       	String assignedPermissions = "";
        List<CrawlPermission> permissionList = CrawlPermission.findAll();
        Iterator<CrawlPermission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	CrawlPermission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
//        		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag) {
                	if (assignedPermissions.length() == 0) {
                		assignedPermissions = ContactPerson.findEmailsByUrls(permission.contactPerson, assignedPermissions);
                	} else {
                		assignedPermissions = assignedPermissions + Const.COMMA + " " + 
                				ContactPerson.findEmailsByUrls(permission.contactPerson, assignedPermissions);
                	}
                }
            }
        }
        assignedPermissions = assignedPermissions.replace(" ,", "");
		Logger.info("assignedPermissions: " + assignedPermissions);
        return assignedPermissions;
    }
    
    /**
     * This method rejects selected crawl permissions and changes their status to 'EMAIL_REJECTED'.
     * @return
     */
    public static void rejectSelectedCrawlPermissions() {
        List<CrawlPermission> permissionList = CrawlPermission.findAll();
        Iterator<CrawlPermission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	CrawlPermission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
//        		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag) {
                	permission.status = Const.CrawlPermissionStatus.EMAIL_REJECTED.name();
                	Logger.info("new permission staus: " + permission.status);
                   	Ebean.update(permission);                	
        	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
        	        Ebean.save(log);
                	Logger.info("updated permission name: " + permission.name + ", staus: " + permission.status);
                }
            }
        }
    }
    
    /**
     * This method sets status "PENDING" for selected crawl permissions.
     * If parameter all is true - do it for all queued permissions,
     * otherwise only selected by checkbox.
     * @return
     */
    public static void setPendingSelectedCrawlPermissions(boolean all, String messageBody, String messageSubject) {
        List<CrawlPermission> permissionList = CrawlPermission.findAll();
        Iterator<CrawlPermission> permissionItr = permissionList.iterator();
        while (permissionItr.hasNext()) {
        	CrawlPermission permission = permissionItr.next();
            if (getFormParam(permission.name) != null) {
//        		Logger.info("getFormParam(permission.name): " + getFormParam(permission.name) + " " + permission.name);
                boolean userFlag = Utils.getNormalizeBooleanString(getFormParam(permission.name));
                if (userFlag || all) {
                	Logger.info("mail to contact person:" + permission.contactPerson.replace(Const.LIST_DELIMITER,"") + ".");
            		String email = ContactPerson.findByUrl(permission.contactPerson.replace(Const.LIST_DELIMITER,"")).email;
//                	String[] toMailAddresses = Utils.getMailArray(email);
                	messageBody = CrawlPermission.replaceStringInText(messageBody, "||URL||", 
                			routes.LicenceController.form(permission.url).absoluteURL(request()).toString());
                    EmailHelper.sendMessage(email, messageSubject, messageBody);                	
//                    EmailHelper.sendMessage(toMailAddresses, messageSubject, messageBody);                	
                	permission.status = Const.CrawlPermissionStatus.PENDING.name();
                	Logger.info("new permission staus: " + permission.status);
                   	Ebean.update(permission);   
        	        CommunicationLog log = CommunicationLog.logHistory(Const.PERMISSION + " " + permission.status, permission.url, permission.creatorUser, Const.UPDATE);
        	        Ebean.save(log);
                	Logger.info("updated permission name: " + permission.name + ", staus: " + permission.status);
                }
            }
        }
    }
    
    /**
     * This method handles queued crawl permissions.
     */
    public static Result send() {
		Logger.info("send crawl permission");
    	Result res = ok();
        String send = getFormParam(Const.SEND);
        String sendall = getFormParam(Const.SEND_ALL);
        String sendsome = getFormParam(Const.SEND_SOME);
        String preview = getFormParam(Const.PREVIEW);
        String reject = getFormParam(Const.REJECT);
        Logger.info("send: " + send + ", sendall: " + sendall + ", sendsome: " + sendsome + ", preview: " + preview + ", reject: " + reject);
	    String template = Const.DEFAULT_TEMPLATE;
        if (getFormParam(Const.TEMPLATE) != null) {
	    	template = getFormParam(Const.TEMPLATE);
	    }
    	String toMails = evaluateToEmails();
    	Logger.info("toMails: " + toMails);
//    	String[] toMailAddresses = Utils.getMailArray(toMails);
//    	Logger.info("toMailAddresses: " + toMailAddresses[0]);
    	String messageSubject = MailTemplate.findByName(template).subject;
    	String messageBody = MailTemplate.findByName(template).readTemplate();

    	if (sendall != null) {
        	Logger.info("send all crawl permission requests");
//            EmailHelper.sendMessage(toMailAddresses, messageSubject, messageBody);
            setPendingSelectedCrawlPermissions(true, messageBody, messageSubject); 
	        res = redirect(routes.CrawlPermissions.index()); 
//        	res = ok(
//		        crawlpermissionsend.render(
//		            CrawlPermission.filterByStatus(Const.DEFAULT_CRAWL_PERMISSION_STATUS), User.find.byId(request().username())
//		            )
//		        );
        }
        if (sendsome != null) {
        	Logger.info("send some crawl permission requests");
//            EmailHelper.sendMessage(toMailAddresses, messageSubject, messageBody);
            setPendingSelectedCrawlPermissions(false, messageBody, messageSubject); 
	        res = redirect(routes.CrawlPermissions.index()); 
//	        res = ok(
//		        crawlpermissionsend.render(
//		            getAssignedPermissionsList(), User.find.byId(request().username())
//		            )
//		        );
        }
        if (preview != null) {
        	Logger.info("preview crawl permission requests");        	
	        res = ok(
	            crawlpermissionpreview.render(
		            	getAssignedPermissionsList().get(0), User.find.byId(request().username()), toMails, template
//		            	getAssignedPermissionsList(), User.find.byId(request().username()), toMails, template
	            )
	        );
        }
        if (reject != null) {
        	Logger.info("reject crawl permission requests");
        	rejectSelectedCrawlPermissions();        	
	        res = redirect(routes.CrawlPermissions.index()); 
        }
        return res;
    }
    
    /**
     * This method checks if crawl permission for given target already exists.
     * @param target
     * @return true if exists false otherwise
     */
    public static boolean checkCrawlPermissionTarget(String target) {
    	Logger.info("checkCrawlPermissionTarget target: " + target);
    	boolean res = false;
    	List<CrawlPermission> list = CrawlPermission.filterByTarget(target);
        if (list != null && list.size() > 0) {
        	res = true;
        }
        return res;
    }
    
    /**
     * This method is checking if crawl permission for given target already exists and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result crawlPermissionExist(String target) {
    	Logger.info("crawlPermissionExist target: " + target);
    	boolean res = checkCrawlPermissionTarget(target);
    	Logger.info("crawl permission exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method is checking if crawl permission for given target already exists and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result crawlPermissionExistAtHigherLevel(String target) {
    	Logger.info("crawlPermissionExistAtHigherLevel target: " + target);
    	boolean res = false;
    	String path = "";
    	if (target != null) {
    		if (target.contains(Const.SLASH_DELIMITER)) {
		    	String[] parts = target.split(Const.SLASH_DELIMITER);
		    	for (String part: parts)
		        {
		    		try {
		    			res = checkCrawlPermissionTarget(path + part);
		    			if (res) {
		    				break;
		    			} else {
		    				path = path + part + Const.SLASH_DELIMITER;
		    			}
		    		} catch (Exception e) {
		    			Logger.info("crawlPermissionExistAtHigherLevel error: " + e);
		    		}
		        }
	    	}
    	}
    	Logger.info("crawl permission in higher level exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method is checking if crawl permission for given target for higher level
     * would be possible and returns result in JSON format.
     * @param target
     * @return JSON result
     */
    public static Result checkForHigherLevelPrompt(String target) {
    	Logger.info("checkForHigherLevelPrompt target: " + target);
    	boolean res = false;
    	if (target != null) {
    		if (target.contains(Const.SLASH_DELIMITER)) {
		    	String[] parts = target.split(Const.SLASH_DELIMITER);
		    	if (parts.length > 1) {
		    		res = true;
		    	}
	    	}
    	}
    	Logger.info("crawl permission in higher level exists res: " + res + ", target: " + target);
    	return ok(Json.toJson(res));
    }
    
    /**
     * This method checks if permission contains a contact person. It is
     * necessary in order to send permission request to this person.
     * Otherwise send buttons are disabled.
     * @param list
     * @return check result
     */
    public static boolean haveContactPerson(List<CrawlPermission> permissionList) {
    	boolean res = true;
	    Iterator<CrawlPermission> permissionItr = permissionList.iterator();
	    while (permissionItr.hasNext()) {
	    	CrawlPermission permission = permissionItr.next();
	        if (permission.contactPerson == null || permission.contactPerson.length() == 0) {
	        	res = false;
	        }
	    }
    	return res;
    }
    
    @BodyParser.Of(BodyParser.Json.class)
    public static Result filterByJson(String name) {
        JsonNode jsonData = null;
        if (name != null) {
	        List<CrawlPermission> permissions = CrawlPermission.filterByName(name);
	        jsonData = Json.toJson(permissions);
        }
        return ok(jsonData);
    }
}


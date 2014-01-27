package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;

import models.CrawlPermission;
import models.DCollection;
import models.Organisation;
import models.Permission;
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
import views.html.crawlpermissions.*;
import views.html.permissions.permissions;
import views.html.targets.targets;

import javax.mail.*;

import java.io.*;
import java.util.*;
import java.util.*;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.activation.*;

/**
 * Manage permissions.
 */
@Security.Authenticated(Secured.class)
public class CrawlPermissionEdit extends AbstractController {
  
    /**
     * Display the permission.
     */
    public static Result index() {
        List<CrawlPermission> resList = processFilterCrawlPermissions("", Const.DEFAULT_CRAWL_PERMISSION_STATUS);
        return ok(
                crawlpermissions.render(
                    "CrawlPermissions", User.find.byId(request().username()), resList, "", Const.DEFAULT_CRAWL_PERMISSION_STATUS
//                        "CrawlPermissions", User.find.byId(request().username()), models.CrawlPermission.findAll(), "", Const.DEFAULT_CRAWL_PERMISSION_STATUS
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
     * This method enables searching for given URL and redirection in order to add new entry
     * if required.
     * @return
     */
    public static Result filter() {
    	Result res = null;
    	Logger.info("CrawlPermissionEdit.filter()");
        String addentry = getFormParam(Const.ADDENTRY);
        String search = getFormParam(Const.SEARCH);
        String name = getFormParam(Const.NAME);
        String status = getFormParam(Const.STATUS);
        if (status == null) {
        	status = Const.DEFAULT_CRAWL_PERMISSION_STATUS;
        }
        List<CrawlPermission> resList = processFilterCrawlPermissions(name, status);
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name + ", status: " + status);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.CrawlPermissionEdit.addEntry(name));
        	} else {
        		Logger.info("CrawlPermission name is empty. Please write name in search window.");
                res = ok(
                        crawlpermissions.render(
                            "CrawlPermissions", User.find.byId(request().username()), resList, "", status
//                                "CrawlPermissions", User.find.byId(request().username()), models.CrawlPermission.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
            		crawlpermissions.render(
                        "CrawlPermissions", User.find.byId(request().username()), resList, name, status
//                            "CrawlPermissions", User.find.byId(request().username()), models.CrawlPermission.filterByName(name), name
                    )
                );
        }
        return res;
    }	   
    
    /**
     * This method applyies filters to the list of crawl permissions.
     * @param filterUrl
     * @param status
     * @return
     */
    public static List<CrawlPermission> processFilterCrawlPermissions(String filterUrl, String status) {
//    	Logger.info("process filter filterUrl: " + filterUrl + ", status: " + status);
    	boolean isProcessed = false;
    	ExpressionList<CrawlPermission> exp = CrawlPermission.find.where();
    	List<CrawlPermission> res = new ArrayList<CrawlPermission>();
    	if (filterUrl != null && !filterUrl.equals(Const.NONE)) {
    		Logger.info("name: " + filterUrl);
    		exp = exp.contains(Const.NAME, filterUrl);
    		isProcessed = true;
    	}
    	if (status != null && !status.toLowerCase().equals(Const.NONE)) {
    		Logger.info("status: " + status);
    		exp = exp.eq(Const.STATUS, status);
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
    public static Result addEntry(String name) {
    	sendEmail();

    	CrawlPermission permission = new CrawlPermission();
    	permission.name = name;
        permission.id = Target.createId();
        permission.url = Const.ACT_URL + permission.id;
        permission.creatorUser = User.find.byId(request().username()).url;
        permission.status = Const.CrawlPermissionStatus.NOT_INITIATED.name();
        permission.template = Const.MailTemplateType.PERMISSION_REQUEST.name();
		Logger.info("add entry with url: " + permission.url + ", and name: " + permission.name);
        return ok(
                crawlpermissionedit.render(
                      permission, User.find.byId(request().username())
                )
            );
    }
      
    public static void sendEmail() {
    	try {
            Logger.info("send mail1");
            String host = "smtp.gmail.com";
            String username = "test@gmail.com";
            String password = "1234";
            InternetAddress[] addresses = {new InternetAddress("roman.graf@ait.ac.at")};
//            InternetAddress[] addresses = {new InternetAddress("user@anymail.com"),
//                    new InternetAddress(bid.email), connect to SMTP host: smtp.gmail.com, port: 465;
//                    new InternetAddress("another-user@anymail.com")};
            Properties props = new Properties();

            // set any needed mail.smtps.* properties here
            Session session = Session.getInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setSubject("my subject placed here");
            message.setContent("my message placed here:\n\n"
                    , "text/plain");
//            + part.toString(), "text/plain");
            message.setRecipients(Message.RecipientType.TO, addresses);

            // set the message content here
            Transport t = session.getTransport("smtps");
            try {
                t.connect(host, username, password);
                Logger.info("send mail");
                t.sendMessage(message, message.getAllRecipients());
            } finally {
                t.close();
            }          
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    
    /**
     * This method saves new object or changes on given Permission in the same object
     * completed by revision comment. The "version" field in the Permission object
     * contains the timestamp of the change. 
     * @return
     */
    public static Result save() {
    	Result res = null;
    	sendEmail();
    	
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
        	    if (getFormParam(Const.TARGET) != null) {
        	    	permission.target = getFormParam(Const.TARGET);
        	    }
        	    if (getFormParam(Const.CONTACT_PERSON) != null) {
        	    	permission.contactPerson = getFormParam(Const.CONTACT_PERSON);
        	    }
        	    Logger.info("creator user: " + getFormParam(Const.CREATOR_USER));
        	    Logger.info("creator user url: " + User.findByName(getFormParam(Const.CREATOR_USER)).url);
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
// TODO license
            } catch (Exception e) {
            	Logger.info("CrawlPermission not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(permission);
    	        Logger.info("save crawl permission: " + permission.toString());
        	} else {
           		Logger.info("update crawl permission: " + permission.toString());
               	Ebean.update(permission);
        	}
	        res = redirect(routes.CrawlPermissionEdit.view(permission.url));
        } 
        if (delete != null) {
        	CrawlPermission permission = CrawlPermission.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(permission);
	        res = redirect(routes.CrawlPermissionEdit.index()); 
        }
    	res = redirect(routes.CrawlPermissionEdit.index()); 
        return res;
    }	   

    public static Result templates() {
        return ok(
                templates.render(
                    "MailTemplates", User.find.byId(request().username()), models.MailTemplate.findAll(), ""
                )
            );
    }
    
//    public static Result licenses() {
//        return ok(
//                licenses.render(
//                		User.find.byId(request().username())
//                )
//            );
//    }

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
        if (send != null) {
        	Logger.info("send some crawl permission requests");
	        res = ok(
		        crawlpermissionsend.render(
		            CrawlPermission.filterByStatus(Const.DEFAULT_CRAWL_PERMISSION_STATUS), User.find.byId(request().username())
		            )
		        );
        }
        if (sendsome != null) {
        	Logger.info("send some crawl permission requests");
	        res = ok(
		        crawlpermissionsend.render(
		            getAssignedPermissionsList(), User.find.byId(request().username())
		            )
		        );
        }
        if (preview != null) {
        	Logger.info("preview crawl permission requests");
	        res = ok(
	            crawlpermissionpreview.render(
	            	getAssignedPermissionsList(), User.find.byId(request().username())
	            )
	        );
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


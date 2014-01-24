package controllers;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.CrawlPermission;
import models.Target;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
//import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;
import uk.bl.Const;
import views.html.crawlpermissions.*;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

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
        return ok(
                crawlpermissions.render(
                    "CrawlPermissions", User.find.byId(request().username()), models.CrawlPermission.findAll(), ""
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
        Logger.info("addentry: " + addentry + ", search: " + search + ", name: " + name);
        if (addentry != null) {
        	if (name != null && name.length() > 0) {
        		res = redirect(routes.CrawlPermissionEdit.addEntry(name));
        	} else {
        		Logger.info("CrawlPermission name is empty. Please write name in search window.");
                res = ok(
                        crawlpermissions.render(
                            "CrawlPermissions", User.find.byId(request().username()), models.CrawlPermission.filterByName(name), ""
                        )
                    );
        	}
        } else {
            res = ok(
            		crawlpermissions.render(
                        "CrawlPermissions", User.find.byId(request().username()), models.CrawlPermission.filterByName(name), name
                    )
                );
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
    	
/*        String save = getFormParam(Const.SAVE);
        String delete = getFormParam(Const.DELETE);
//        Logger.info("save: " + save);
        if (save != null) {
        	Logger.info("save permission id: " + getFormParam(Const.ID) + ", url: " + getFormParam(Const.URL) + 
        			", name: " + getFormParam(Const.NAME) + ", revision: " + getFormParam(Const.REVISION));
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
//        	    if (permission.revision == null) {
//        	    	permission.revision = "";
//        	    }
//                if (getFormParam(Const.REVISION) != null) {
//                	permission.revision = permission.revision.concat(", " + getFormParam(Const.REVISION));
//                }
            } catch (Exception e) {
            	Logger.info("Permission not existing exception");
            }
            
        	if (!isExisting) {
               	Ebean.save(permission);
    	        Logger.info("save permission: " + permission.toString());
        	} else {
           		Logger.info("update permission: " + permission.toString());
               	Ebean.update(permission);
        	}
	        res = redirect(routes.CrawlPermissionEdit.view(permission.url));
        } 
        if (delete != null) {
        	CrawlPermission permission = CrawlPermission.findByUrl(getFormParam(Const.URL));
        	Ebean.delete(permission);
	        res = redirect(routes.CrawlPermissionEdit.index()); 
        }*/
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


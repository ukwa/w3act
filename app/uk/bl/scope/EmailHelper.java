package uk.bl.scope;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import play.Logger;
import uk.bl.Const;


/**
 * This class supports e-mail configuration and sending.
 *
 */
public class EmailHelper {

    public static void sendMessage(String to, String subject, String msg) {

    	Logger.debug("sendMessage() to: " + to + ", subject: " + subject + ", message body: " + msg);
    	if (to != null) {
	        Properties props = System.getProperties();
	    	Properties customProps = new Properties();
	    	String user = "";
	    	String from = "";
	    	String password = "";
	    	try {
	    		customProps.load(new FileInputStream(Const.PROJECT_PROPERTY_FILE));
	    	    for(String key : customProps.stringPropertyNames()) {
	    	    	  String value = customProps.getProperty(key);
	//    	      	  Logger.debug("sendMessage() key: " + key + " => " + value);
	    	    	  if (key.equals(Const.HOST)) {
	  	    	          props.put("mail.smtp.host", value);
	  	    	          props.put("mail.smtp.ssl.trust", value);
	  	    	      	  Logger.debug("sendMessage() host: " + value);
	    	    	  }
	    	    	  if (key.equals(Const.USER)) {
	  	    	          props.put("mail.smtp.user", value);
	  	    	          user = value;
	  	    	      	  Logger.debug("sendMessage() user: " + user);
	    	    	  }
	    	    	  if (key.equals(Const.PASSWORD)) {
	  	    	          props.put("mail.smtp.password", value);
	  	    	          password = value;
	//  	    	      	  Logger.debug("sendMessage() password: " + password);
	    	    	  }
	    	    	  if (key.equals(Const.PORT)) {
	    	    	      props.put("mail.smtp.port", value);
	  	    	      	  Logger.debug("sendMessage() port: " + value);
	      	    	  }
	    	    	  if (key.equals(Const.FROM)) {
	    	    	      from = value;
	  	    	      	  Logger.debug("sendMessage() from: " + value);
	      	    	  }
	    	    }
	    	} catch (IOException e) {
	    		throw new RuntimeException(e);
	    	}
	    	
	        props.put("mail.smtp.starttls.enable", Const.TRUE); 
	        props.put("mail.smtp.auth", Const.FALSE);
	        Session session = Session.getInstance(props);
	    	if (user != null && user.length() > 0 && password != null && password.length() > 0) {
		        props.put("mail.smtp.auth", Const.TRUE);
		        session = Session.getInstance(props,
			      new javax.mail.Authenticator() {
			        public PasswordAuthentication getPasswordAuthentication() {
			        	String user = "";
			        	String password = "";
			        	try {
			            	Properties customProps = new Properties();
			        		customProps.load(new FileInputStream(Const.PROJECT_PROPERTY_FILE));
			        	    for(String key : customProps.stringPropertyNames()) {
			        	    	  String value = customProps.getProperty(key);
			        	    	  if (key.equals(Const.USER)) {
			      	    	          user = value;
			        	    	  }
			        	    	  if (key.equals(Const.PASSWORD)) {
			      	    	          password = value;
			        	    	  }
			        	    }
			        	} catch (IOException e) {
			    	      	Logger.debug("sendMessage() error: " + e);
			        		throw new RuntimeException(e);
			        	}
			            return new PasswordAuthentication(user, password);
			        }
			      });
	    	}
	
		    try {
		        Message message = new MimeMessage(session);
		        message.setFrom(new InternetAddress(from));
		        message.setRecipients(Message.RecipientType.TO,
			            InternetAddress.parse(to));
		        message.setSubject(subject);
		        message.setText(msg);
		        Transport.send(message);
		        Logger.info("E-mail message to " + to + ", with subject '" + subject + "' was sent");
		        Logger.debug("E-mail message to " + to + ", with subject '" + subject + "' was sent");
		    } catch (MessagingException e) {
		        Logger.debug("E-mail message error: " + e);
		        throw new RuntimeException(e);
		    }
	    }
    }     
    
    public static String getServerNameFromPropertyFile() {
    	Logger.debug("getServerNameFromPropertyFile()");
    	Properties customProps = new Properties();
	    String res = "";
    	try {
    		customProps.load(new FileInputStream(Const.PROJECT_PROPERTY_FILE));
    	    for(String key : customProps.stringPropertyNames()) {
    	    	  String value = customProps.getProperty(key);
    	    	  if (key.equals(Const.SERVER_NAME)) {
    	    		  res = value;
  	    	      	  Logger.debug("getServerNameFromPropertyFile() server name: " + res);
    	    	  }
    	    }
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    	return res;
   }          
    
}
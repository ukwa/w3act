package uk.bl.scope;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;

import play.Logger;

import uk.bl.api.Utils;

import java.io.FileInputStream;
import java.io.IOException;

import uk.bl.Const;

//import org.junit.Test;


/**
 * This class supports e-mail configuration and sending.
 *
 */
public class EmailHelper {

//    @Test
//    public void sendMail(){
//        String[] to = {"roman@ait.ac.at","max@ait.ac.at"};
//        sendMessage(to,"Message test","Message body");
//
//    }

    public static void sendMessage(String to, String subject, String msg) {

        Properties props = System.getProperties();
    	Properties customProps = new Properties();
    	String user = "";
    	String from = "";
    	String password = "";
    	try {
    		customProps.load(new FileInputStream(Const.PROJECT_PROPERTY_FILE));
    	    for(String key : customProps.stringPropertyNames()) {
    	    	  String value = customProps.getProperty(key);
    	    	  System.out.println(key + " => " + value);
    	    	  if (key.equals(Const.HOST)) {
  	    	          props.put("mail.smtp.host", value);
  	    	          props.put("mail.smtp.ssl.trust", value);
    	    	  }
    	    	  if (key.equals(Const.USER)) {
  	    	          props.put("mail.smtp.user", value);
  	    	          user = value;
    	    	  }
    	    	  if (key.equals(Const.PASSWORD)) {
  	    	          props.put("mail.smtp.password", value);
  	    	          password = value;
    	    	  }
    	    	  if (key.equals(Const.PORT)) {
    	    	      props.put("mail.smtp.port", value);
      	    	  }
    	    	  if (key.equals(Const.FROM)) {
    	    	      from = value;
      	    	  }
    	    }
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    	
        props.put("mail.smtp.starttls.enable", Const.TRUE); 
        props.put("mail.smtp.auth", Const.TRUE);

        Session session = Session.getInstance(props,
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
	        		throw new RuntimeException(e);
	        	}
	            return new PasswordAuthentication(user, password);
	        }
	      });

	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(from));
//	        message.setFrom(new InternetAddress(FROM));
	        message.setRecipients(Message.RecipientType.TO,
		            InternetAddress.parse(to));
//            InternetAddress.parse(TO));
	        message.setSubject(subject);
	        message.setText(msg);

	        Transport.send(message);

	        Logger.info("E-mail message to " + to + ", with subject '" +
//	    	        Logger.info("E-mail message to " + Utils.convertStringArrayToString(to) + ", with subject '" +
	        		subject + "' was sent");

	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
    }
           
}
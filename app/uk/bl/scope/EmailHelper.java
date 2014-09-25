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
import play.Play;
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
    		String host = Play.application().configuration().getString(Const.HOST);
    		final String user = Play.application().configuration().getString(Const.MAIL_USER);
    		final String password = Play.application().configuration().getString(Const.MAIL_PASSWORD);
    		String port = Play.application().configuration().getString(Const.PORT);
    		String from = Play.application().configuration().getString(Const.FROM);
    		
    		props.put("mail.smtp.host", host);
    		props.put("mail.smtp.ssl.trust", host);
			props.put("mail.smtp.user", user);
			props.put("mail.smtp.password", password);
  	    	props.put("mail.smtp.port", port);
	        props.put("mail.smtp.starttls.enable", Const.TRUE); 
	        props.put("mail.smtp.auth", Const.FALSE);

    		Logger.debug("sendMessage() host: " + host);
			Logger.debug("sendMessage() user: " + user);
  	    	Logger.debug("sendMessage() port: " + port);
  	    	Logger.debug("sendMessage() from: " + from);

	        Session session = Session.getInstance(props);
	    	if (user != null && user.length() > 0 && password != null && password.length() > 0) {
		        props.put("mail.smtp.auth", Const.TRUE);
		        session = Session.getInstance(props,
			      new javax.mail.Authenticator() {
			        public PasswordAuthentication getPasswordAuthentication() {
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
		String res = Play.application().configuration().getString(Const.SERVER_NAME);
    	return res;
   }          
    
}
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

//import org.junit.Test;


/**
 * This class supports e-mail configuration and sending.
 *
 */
public class EmailHelper {

    private static final String HOST = "0.0.0.0";
    private static final String USER = "username";
    private static final String FROM = "roman@ait.ac.at";
    private static final String TO   = "max@ait.ac.at";
    private static final String PASS = "***";
    private static final String PORT = "25";
    private static final String AUTH = "true";

//    @Test
//    public void sendMail(){
//        String[] to = {"roman@ait.ac.at","max@ait.ac.at"};
//        sendMessage(to,"Message test","Message body");
//
//    }

    public static void sendMessage(String[] to, String subject, String msg) {

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.user", USER);
        props.put("mail.smtp.password", PASS);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.ssl.trust", HOST);

        Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(USER, PASS);
	        }
	      });

	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(FROM));
	        message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(TO));
	        message.setSubject("Testing Subject");
	        message.setText("Dear x,"
	            + "\n\n test2!");

	        Transport.send(message);

	        System.out.println("Done");

	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
    }
           
}
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
    private static final String USER = "roman";
    private static final String FROM = "roman@ait.ac.at";
    private static final String TO   = "roman@ait.ac.at";
    private static final String PASS = "***";
    private static final String PORT = "25";
    private static final String AUTH = "true";

//    @Test
//    public void sendMail(){
//        String[] to = {"roman.graf@ait.ac.at","roman.graf@ait.ac.at"};
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
//            props.put("mail.smtp.socketFactory.port", PORT);
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "false");


        /*Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(FROM));

        InternetAddress[] toAddress = new InternetAddress[to.length];

        // To get the array of addresses
        for( int i=0; i < to.length; i++ ) { // changed from a while loop
            toAddress[i] = new InternetAddress(to[i]);
        }

        for( int i=0; i < toAddress.length; i++) { // changed from a while loop
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }

        message.setSubject(subject);
        message.setText(msg);

        Transport transport = session.getTransport("smtps");
        transport.connect(HOST, USER, PASS);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        
        
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
    
    public static void sendEmail() {
    	try {
            Logger.info("send mail1");
            String host = "smtp.gmail.com";
            String username = "test@gmail.com";
            String password = "1234";
        	
            InternetAddress[] addresses = {new InternetAddress("roman@ait.ac.at")};
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
        
}
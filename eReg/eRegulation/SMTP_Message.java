package eRegulation;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class SMTP_Message
{

	public void send_message()
	{
//      Properties properties 			= System.getProperties();
		Properties props 				= new Properties();
		props.setProperty("mail.user", 			"administrateur");
    	props.setProperty("mail.password", 		"llenkcarb");
    	props.setProperty("mail.smtp.host", 	"192.168.5.10");

        Session session 				= Session.getDefaultInstance(props);

        try
        {
            MimeMessage message 		= new MimeMessage(session);

            message.setFrom(new InternetAddress("HVAC@bapjg.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("andre@bapjg.com"));
            message.setSubject("This is the Subject Line!");
            message.setText("This is actual message");
            Transport.send(message);
            System.out.println("Sent message successfully....");
         }
        catch (MessagingException mex) 
        {
            mex.printStackTrace();
        }
	}
}

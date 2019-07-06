package pt.oofaround.support;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSupport {

	public EmailSupport() {
	}

	public static void sendEmailRegistration(String email, String username) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress("register@oofaround.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("Registration");
			msg.setText("Thank you for registering your account, " + username + ".");
			Transport.send(msg);
		} catch (AddressException e) {
			// ...
		} catch (MessagingException e) {
			// ...
		}
	}

	public static void sendRecoverCode(String email, String code) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress("register@oofaround.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setSubject("Registration");
			msg.setText("Your recovery code is " + code + ".");
			Transport.send(msg);
		} catch (AddressException e) {
		} catch (MessagingException e) {
		}
	}

}

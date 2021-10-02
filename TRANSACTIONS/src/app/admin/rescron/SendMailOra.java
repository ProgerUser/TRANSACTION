package app.admin.rescron;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailOra {
	
	public static Properties prop;

	public static String SendMail(String subj,String msg,String mailto) {
		String ret = "ok";
		try {
			// prop
			prop = new Properties();
			prop.put("mail.transport.protocol", "smtp");
			prop.put("mail.smtp.starttls.enable", "true");
			prop.put("mail.smtp.host", "mail.sbra.su");
			prop.put("mail.smtp.port", "587");
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.ssl.enable", "false");
			prop.put("mail.smtp.ssl.trust", "mail.sbra.su");
			prop.put("mail.smtp.user", "portal@sbra.su");
			prop.put("mail.smtp.password", "Cjktyysq_098");
			prop.put("mail.store.protocol", "imap");
			prop.put("mail.imap.host", "mail.sbra.su");
			prop.put("mail.imap.port", "993");
			prop.put("mail.imap.auth", "true");
			prop.put("mail.imap.ssl.enable", "false");
			prop.put("mail.imap.ssl.trust", "mail.sbra.su");
			prop.put("mail.imap.user", "portal@sbra.su");
			prop.put("mail.imap.password", "Cjktyysq_098");
			// session
			Session session = Session.getInstance(prop, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(prop.getProperty("mail.smtp.user"),
							prop.getProperty("mail.smtp.password"));
				}
			});
			Message message = new MimeMessage(session);
			
			message.setHeader("Content-Type", "text/plain; charset=UTF-8");
			
			message.setFrom(new InternetAddress(prop.getProperty("mail.smtp.user")));
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailto));
			
			message.setSubject(subj);


			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/plain; charset=UTF-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);

			Transport.send(message);
		} catch (Exception e) {
			ret = getStackTrace(e);
		}
		return ret;
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}

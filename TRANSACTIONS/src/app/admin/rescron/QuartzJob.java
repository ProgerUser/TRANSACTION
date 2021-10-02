package app.admin.rescron;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJob implements Job {
	/**
	 * Логин в XXI
	 */
	private static String DBUsername;
	/**
	 * Пароль в XXI
	 */
	private static String DBUserpass;
	/**
	 * Строка подключения в XXI
	 */
	private static String DBUrl;
	/**
	 * Адрес получателя
	 */
	private static String MailTo;

	/**
	 * Выполнение
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			DOMConfigurator.configure(ResCron.class.getResource("/log4j.xml"));
			MYLogger.info("Start " + Thread.currentThread().getName());
			// PROPERTY file
			try (InputStream input = new FileInputStream(System.getenv(ResCron.EnvName) + "/config.properties")) {
				Properties prop = new Properties();
				// load a properties file
				prop.load(input);
				DBUsername = prop.getProperty("user");
				DBUserpass = prop.getProperty("pass");
				DBUrl = prop.getProperty("url");
				MailTo = prop.getProperty("mailto");
			} catch (Exception e) {
				MYLogger.error(ExceptionUtils.getStackTrace(e));
			}
			// Create Session
			dbDisconnect();
			dbConnect();
			// ________________
			{
				
			}
		} catch (Exception e) {
			SendMail("", ExceptionUtils.getStackTrace(e));
			dbDisconnect();
			System.exit(0);
		}
	}

	/**
	 * Mail prop
	 */
	Properties props;

	/**
	 * Отправка письма с вложением
	 * 
	 * @param FileName
	 */
	void SendMail(String FileName, String Mes) {

		try (InputStream input = new FileInputStream(System.getenv(ResCron.EnvName) + "/mail.properties")) {
			props = new Properties();
			props.load(input);
		} catch (Exception e) {
			MYLogger.error(ExceptionUtils.getStackTrace(e));
		}
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.smtp.user"),
						props.getProperty("mail.smtp.password"));
			}
		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(props.getProperty("mail.smtp.user")));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(MailTo));

			// Set Header: char
			message.setHeader("Content-Type", "text/plain; charset=UTF-8");

			// Set Subject: header field
			message.setSubject("Истекают сертификаты! < 30 дней");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(Mes);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(FileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(FileName);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart, "text/plain; charset=UTF-8");

			// Send message
			Transport.send(message);

			MYLogger.info("Sent message successfully....");
		} catch (Exception e) {
			MYLogger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Сессия
	 */
	Connection conn;

	/**
	 * Log
	 */
	Logger MYLogger = Logger.getLogger(getClass());

	/**
	 * Открыть сессию
	 */
	void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection("jdbc:oracle:thin:" + DBUsername + "/" + DBUserpass + "@" + DBUrl,
					props);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			MYLogger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Отключить сессию
	 */
	void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			MYLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
	}
}
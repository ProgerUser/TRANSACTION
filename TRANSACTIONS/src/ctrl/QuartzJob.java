package ctrl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import app.model.Connect;

public class QuartzJob implements Job {

	DirContext ldapContext;
	String DBUsername;
	String DBUserpass;
	String DBUrl;
	String MailTo;

	/**
	 * Выполнение
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// _____________________LOG______________________________
			ConsoleAppender console = new ConsoleAppender(); // create appender
			// configure the appender
			String PATTERN = "%d [%p|%c|%C{1}] %m%n";
			console.setLayout(new PatternLayout(PATTERN));
			console.setThreshold(Level.DEBUG);
			console.activateOptions();
			// add appender to any Logger (here is root)
			org.apache.log4j.Logger.getRootLogger().addAppender(console);

			FileAppender fa = new FileAppender();
			fa.setName("CERTS");
			fa.setFile("CERTS.log");
			fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
			fa.setThreshold(Level.DEBUG);
			fa.setAppend(true);
			fa.activateOptions();

			// add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fa);
			// repeat with all other desired appenders
			// ______________________________________________________
			MYLogger.info("Start " + Thread.currentThread().getName());
			// ______________________PROPERTY__FILE___________________
			try (InputStream input = new FileInputStream(System.getenv("CERTS_SHEDULER") + "/config.properties")) {
				Properties prop = new Properties();
				// load a properties file
				prop.load(input);
				DBUsername = prop.getProperty("user");
				DBUserpass = prop.getProperty("pass");
				DBUrl = prop.getProperty("url");
				MailTo = prop.getProperty("url");
			} catch (Exception e) {
				MYLogger.error(e.getMessage());
			}
			// _____________Get__XML____________________
			String users = UserList();
			// ______Create_Session___________
			if (conn != null) {
				dbConnect();
			} else {
				dbDisconnect();
				dbConnect();
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * Mail prop
	 */
	Properties prop;

	/**
	 * Отправка письма с вложением
	 * @param FileName
	 */
	void SendMail(String FileName) {
		try (InputStream input = new FileInputStream(System.getenv("CERTS_SHEDULER") + "/mail.properties")) {
			prop = new Properties();
			prop.load(input);
		} catch (Exception e) {
			MYLogger.error(e.getMessage());
		}
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(prop.getProperty("mail.smtp.user"),
						prop.getProperty("mail.smtp.password"));
			}
		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(prop.getProperty("mail.smtp.user")));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(MailTo));

			// Set Subject: header field
			message.setSubject("Testing Subject");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("This is message body");

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
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			MYLogger.error("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			MYLogger.error(mex);
		}

	}

	/**
	 * XML со списком пользователей
	 * 
	 * @return
	 */
	String UserList() {
		String ret = null;
		try {
			Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			ldapEnv.put(Context.PROVIDER_URL, "ldap://sbra.com");
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapEnv.put(Context.SECURITY_PRINCIPAL, "saidp@sbra.com");
			ldapEnv.put(Context.SECURITY_CREDENTIALS, "ipman165");
			ldapContext = new InitialDirContext(ldapEnv);

			// Create the search controls
			SearchControls searchCtls = new SearchControls();

			// Specify the attributes to return
			String returnedAtts[] = { "sn", "givenName", "samAccountName", "cn", "ou", "dc", "sn", "name" };
			searchCtls.setReturningAttributes(returnedAtts);

			// Specify the search scopes
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// specify the LDAP search filter
			String searchFilter = "(&(objectclass=user)(!(objectclass=computer)))";// "(&(objectClass=user))";

			// Specify the Base for the search
			String searchBase = "dc=sbra,dc=com";

			// Search for objects using the filter
			NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

			// Loop through the search results
			/* XML */
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element root = document.createElement("ACDUSR");
			document.appendChild(root);
			/* Loop */
			Element usr = null;
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				// ----------------------------------
				usr = document.createElement("usr");

				Attr getName = document.createAttribute("getName");
				getName.setValue(sr.getName());
				usr.setAttributeNode(getName);

				Attr samAccountName = document.createAttribute("samAccountName");
				samAccountName
						.setValue((attrs.get("samAccountName") != null) ? attrs.get("samAccountName").toString() : "");
				usr.setAttributeNode(samAccountName);

				Attr givenName = document.createAttribute("givenName");
				givenName.setValue((attrs.get("givenName") != null) ? attrs.get("givenName").toString() : "");
				usr.setAttributeNode(givenName);

				Attr sn = document.createAttribute("sn");
				sn.setValue((attrs.get("sn") != null) ? attrs.get("sn").toString() : "");
				usr.setAttributeNode(sn);

				Attr name = document.createAttribute("name");
				name.setValue((attrs.get("name") != null) ? attrs.get("name").toString() : "");
				usr.setAttributeNode(name);
				root.appendChild(usr);
			}
			ldapContext.close();

			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			// System.out.println(writer.toString());
			ret = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			MYLogger.error(e.getMessage());
		}
		return ret;
	}

	/**
	 * Сессия
	 */
	Connection conn;

	/**
	 * Логирование
	 */
	Logger MYLogger = Logger.getLogger(getClass());

	/**
	 * Открыть сессию
	 */
	void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program", "CertsAdminsJAVAFX");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			MYLogger.error(e.getMessage());
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
			MYLogger.error(e.getMessage());
		}
	}
}
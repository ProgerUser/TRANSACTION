package ctrl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QuartzJob implements Job {

	DirContext ldapContext;
	String DBUsername;
	String DBUserpass;
	String DBUrl;
	String MailTo;

	/**
	 * Выполнение скрипта и выгрузка csv файла
	 */
	void ExecPowerShall() {
		try {
			// String command = "powershell.exe your command";
			// Getting the version
			String command = "powershell.exe  -file " + System.getenv("CERTS_SHEDULER") + "/CA_QUERY.ps1";
			// Executing the command
			Process powerShellProcess = Runtime.getRuntime().exec(command);
			// Getting the results
			powerShellProcess.getOutputStream().close();

			String line;

			System.out.println("Standard Output:");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
			while ((line = stdout.readLine()) != null) {
				System.out.println(line);
			}
			stdout.close();

			System.out.println("Standard Error:");
			BufferedReader stderr = new BufferedReader(new InputStreamReader(powerShellProcess.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
				System.out.println(line);
			}
			stderr.close();
			System.out.println("Done");

		} catch (Exception e) {
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
	}

	/**
	 * Выполнение
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			DOMConfigurator.configure(TestAD.class.getResource("/log4j.xml"));
			/*
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
			*/
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
				MailTo = prop.getProperty("mailto");
			} catch (Exception e) {
				MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
						+ Thread.currentThread().getStackTrace()[2].getLineNumber());
			}
			// _____________Get__XML____________________
			String users = UserList();
			// ______Create_Session___________
			dbDisconnect();
			dbConnect();
			
			//if (conn == null) {
			//	dbConnect();
			//} else {
			//	dbConnect();
			//}
			MYLogger.info("StartJob");
			// ________Insert_Users_________
			String retusr = InsertUsrDb(users);
			if (!retusr.equals("OK")) {
				MYLogger.error(retusr + "~" + Thread.currentThread().getName() + " line = "
						+ Thread.currentThread().getStackTrace()[2].getLineNumber());
			}
			// ___EXEC_POWERSHALL___________
			ExecPowerShall();
			// ___get_xlsx___________
			if (InsertCertsDb() > 0) {
				SendMail(System.getenv("CERTS_SHEDULER") + "/certs.xlsx");
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

	/**
	 * Get the current line number.
	 * 
	 * @return int - Current line number.
	 */
	public int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	/**
	 * Mail prop
	 */
	Properties props;

	/**
	 * Чтение файла
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unused")
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(System.lineSeparator());
			}
			br.close();
			String clobData = sb.toString();
			return clobData;
		} catch (Exception e) {
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
		return null;
	}

	/**
	 * Вставить сертификаты
	 * 
	 * @param XMLf
	 * @return
	 */
	Integer InsertCertsDb() {
		Integer ret = null;
		try {
			// String reviewStr = readFile(System.getenv("CERTS_SHEDULER") +
			// "/processes.csv");
			Path path = Paths.get(System.getenv("CERTS_SHEDULER") + "/processes.csv");
			byte[] bArray = java.nio.file.Files.readAllBytes(path);
			InputStream is = new ByteArrayInputStream(bArray);

			CallableStatement clstmt = conn.prepareCall("{ ? = call SB_CERTS_USR.CertList(?,?,?,?) }");
			clstmt.registerOutParameter(1, Types.VARCHAR);
			clstmt.setBlob(2, is, bArray.length);
			clstmt.registerOutParameter(3, Types.VARCHAR);
			clstmt.registerOutParameter(4, Types.BLOB);
			clstmt.registerOutParameter(5, Types.INTEGER);
			clstmt.execute();
			if (!clstmt.getString(1).equals("OK")) {
				conn.rollback();
				ret = 0;
				MYLogger.error(clstmt.getString(3) + "~" + Thread.currentThread().getName() + " line = "
						+ Thread.currentThread().getStackTrace()[2].getLineNumber());
			} else {
				conn.commit();
				if (clstmt.getInt(5) > 0) {
					Blob blob = clstmt.getBlob(4);
					int blobLength = (int) blob.length();
					byte[] blobAsBytes = blob.getBytes(1, blobLength);
					// release the blob and free up memory. (since JDBC 4.0)
					blob.free();
					FileUtils.writeByteArrayToFile(new File(System.getenv("CERTS_SHEDULER") + "/certs.xlsx"),
							blobAsBytes);
					ret = clstmt.getInt(5);
				}

			}
		} catch (SQLException | IOException e) {
			ret = 0;
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
		return ret;
	}

	String InsertUsrDb(String XMLf) {
		String ret = null;
		try {
			Clob XML = conn.createClob();
			XML.setString(1, XMLf);

			CallableStatement clstmt = conn.prepareCall("{ ? = call SB_CERTS_USR.DomainUsr(?,?) }");
			clstmt.registerOutParameter(1, Types.VARCHAR);
			clstmt.setClob(2, XML);
			clstmt.registerOutParameter(3, Types.VARCHAR);
			clstmt.execute();
			if (!clstmt.getString(1).equals("OK")) {
				ret = clstmt.getString(3);
				conn.rollback();
			} else {
				ret = "OK";
				conn.commit();
			}
		} catch (SQLException e) {
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
		return ret;
	}

	/**
	 * Отправка письма с вложением
	 * 
	 * @param FileName
	 */
	void SendMail(String FileName) {
		try (InputStream input = new FileInputStream(System.getenv("CERTS_SHEDULER") + "/mail.properties")) {
			props = new Properties();
			props.load(input);
		} catch (Exception e) {
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
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

			// Set Subject: header field
			message.setSubject("Expiring certificates!!!");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("Re-registration required.");

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

			MYLogger.info("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			MYLogger.error(mex + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
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
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
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
			conn = DriverManager.getConnection("jdbc:oracle:thin:" + DBUsername + "/" + DBUserpass + "@" + DBUrl,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
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
			MYLogger.error(e.getMessage() + "~" + Thread.currentThread().getName() + " line = "
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
		}
	}
}
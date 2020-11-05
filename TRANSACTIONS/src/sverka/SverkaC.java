package sverka;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import sb_tr.model.Connect;

/**
 * Сверка по Кор. счету, выгрузка в формате 1с
 * 
 * 05.11.2020
 * 
 * @author Said
 *
 */
public class SverkaC {

	@FXML
	private TableColumn<AMRA_STMT_CALC, String> Status;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> DEB_OB;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> END_REST;

	@FXML
	private TableColumn<AMRA_STMT_CALC, LocalDate> STMT_BEGIN;

	@FXML
	private TableColumn<AMRA_STMT_CALC, LocalDate> STMT_END;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Integer> ID;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> BEGIN_REST;

	@FXML
	private TableView<AMRA_STMT_CALC> STMT;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> CRED_OB;

	/**
	 * Загрузить файл
	 * 
	 * @param event
	 */
	@FXML
	void LoadFile(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Текстовый файл", "*.txt"));
			if (System.getenv("IBANK_EXPORT") != null) {
				fileChooser.setInitialDirectory(new File(System.getenv("IBANK_EXPORT")));
			} else {
				fileChooser.setInitialDirectory(
						new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
			}

			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				CrXml(file.getParent() + "/" + file.getName());
				/*
				 * String reviewStr = readFile(file.getParent() + "\\" + file.getName()); Clob
				 * clob = conn.createClob(); clob.setString(1, reviewStr); CallableStatement
				 * callStmt = conn.prepareCall("{ ? = call AMRA_STMT_EXEC(?,?,?)");
				 * callStmt.registerOutParameter(1, Types.VARCHAR); callStmt.setClob(2, clob);
				 * callStmt.registerOutParameter(3, Types.INTEGER);
				 * callStmt.registerOutParameter(4, Types.VARCHAR); callStmt.execute(); if
				 * (callStmt.getString(1).equals("OK")) {
				 * 
				 * }
				 */
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}

	}

	String CrXml(String path) {
		String ret = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			while (line != null) {
				if (line.contains("=")) {
					System.out.println(line.substring(0, line.indexOf("=")));
					System.out.println(line.substring(line.indexOf("=") + 1, line.length()));
				}
				line = reader.readLine();
			}
			reader.close();

			// xml
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

			Document document = documentBuilder.newDocument();

			// root element
			Element root = document.createElement("company");
			document.appendChild(root);

			// employee element
			Element employee = document.createElement("employee");

			root.appendChild(employee);

			// set an attribute to staff element
			Attr attr = document.createAttribute("id");
			attr.setValue("10");
			employee.setAttributeNode(attr);

			// you can also use staff.setAttribute("id", "1") for this

			// firstname element
			Element firstName = document.createElement("firstname");
			firstName.appendChild(document.createTextNode("James"));
			employee.appendChild(firstName);

			// lastname element
			Element lastname = document.createElement("lastname");
			lastname.appendChild(document.createTextNode("Harley"));
			employee.appendChild(lastname);

			// email element
			Element email = document.createElement("email");
			email.appendChild(document.createTextNode("james@example.org"));
			employee.appendChild(email);

			// department elements
			Element department = document.createElement("department");
			department.appendChild(document.createTextNode("Human Resources"));
			employee.appendChild(department);

			// create the xml file
			// transform the DOM Object to an XML File
			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			System.out.println("XML IN String format is: \n" + writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
		return ret;
	}

	/**
	 * Открыть выписку
	 * 
	 * @param event
	 */
	@FXML
	void OpenStmt(ActionEvent event) {

	}

	void InitTable() {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String selectStmt = "select id, \r\n" + "load_date, \r\n" + "creation_datetime, \r\n" + "stmt_begin, \r\n"
					+ "stmt_end, \r\n" + "ch_account, \r\n" + "begin_rest, \r\n" + "cred_ob, \r\n" + "deb_ob, \r\n"
					+ "end_rest, \r\n" + "file_cl, \r\n" + "oper, \r\n" + "status from AMRA_STMT_CALC t ";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AMRA_STMT_CALC> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AMRA_STMT_CALC list = new AMRA_STMT_CALC();
				list.setOPER(rs.getString("OPER"));
				list.setEND_REST(rs.getDouble("END_REST"));
				list.setDEB_OB(rs.getDouble("DEB_OB"));
				list.setCRED_OB(rs.getDouble("CRED_OB"));
				list.setBEGIN_REST(rs.getDouble("BEGIN_REST"));
				list.setCH_ACCOUNT(rs.getString("CH_ACCOUNT"));
				list.setSTMT_END((rs.getDate("STMT_END") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("STMT_END")), formatter)
						: null);
				list.setSTMT_BEGIN((rs.getDate("STMT_BEGIN") != null) ? LocalDate
						.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("STMT_BEGIN")), formatter) : null);
				list.setCREATION_DATETIME((rs.getDate("CREATION_DATETIME") != null) ? LocalDate.parse(
						new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("CREATION_DATETIME")), formatter) : null);
				list.setLOAD_DATE((rs.getDate("LOAD_DATE") != null) ? LocalDateTime
						.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("LOAD_DATE")), formatterwt)
						: null);
				list.setID(rs.getInt("ID"));
				list.setSTATUS(rs.getString("STATUS"));

				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			STMT.setItems(dlist);

			TableFilter<AMRA_STMT_CALC> tableFilter = TableFilter.forTableView(STMT).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
	}

	/**
	 * Чтение файла
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName), getFileCharset(fileName)));
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
			e.printStackTrace();
			Message(e.getMessage());
		}
		return null;
	}

	/**
	 * Универсальный декодер
	 * 
	 * @param file
	 * @return
	 */
	public String getFileCharset(String file) {
		try {
			byte[] buf = new byte[4096];
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			final UniversalDetector universalDetector = new UniversalDetector(null);
			int numberOfBytesRead;
			while ((numberOfBytesRead = bufferedInputStream.read(buf)) > 0 && !universalDetector.isDone()) {
				universalDetector.handleData(buf, 0, numberOfBytesRead);
			}
			universalDetector.dataEnd();
			String encoding = universalDetector.getDetectedCharset();
			universalDetector.reset();
			bufferedInputStream.close();
			return encoding;
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
		return null;
	}

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			dbConnect();

			Status.setCellValueFactory(cellData -> cellData.getValue().STATUSProperty());
			DEB_OB.setCellValueFactory(cellData -> cellData.getValue().DEB_OBProperty().asObject());
			END_REST.setCellValueFactory(cellData -> cellData.getValue().END_RESTProperty().asObject());
			END_REST.setCellValueFactory(cellData -> cellData.getValue().END_RESTProperty().asObject());
			CRED_OB.setCellValueFactory(cellData -> cellData.getValue().CRED_OBProperty().asObject());
			BEGIN_REST.setCellValueFactory(cellData -> cellData.getValue().BEGIN_RESTProperty().asObject());
			STMT_BEGIN.setCellValueFactory(cellData -> cellData.getValue().STMT_BEGINProperty());
			STMT_END.setCellValueFactory(cellData -> cellData.getValue().STMT_ENDProperty());
			ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());

		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
	}

	/**
	 * Вывод сообщения
	 * 
	 * @param mess
	 */
	public static void Message(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("icon.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	/**
	 * Сессия
	 */
	private Connection conn;

	/**
	 * Открыть сессию
	 */
	private void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program", "CusList");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			Message(e.getMessage());
		}
	}

	/**
	 * Отключить сессию
	 */
	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Message(e.getMessage());
		}
	}

}

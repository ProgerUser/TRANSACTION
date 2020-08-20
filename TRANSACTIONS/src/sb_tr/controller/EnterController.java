package sb_tr.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sb_tr.Main;
import sb_tr.model.Connect;
import sb_tr.model.InputFilter;
import sb_tr.model.SqlMap;
import sb_tr.util.DBUtil;

public class EnterController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button enter_id;

	@FXML
	private ComboBox<String> login;

	@FXML
	private PasswordField pass;

	@FXML
	private ComboBox<String> conurl;

	@FXML
	private TextField dateend;

	@FXML
	private TextField datestart;

	@FXML
	private Button find;

	@FXML
	private TextField fio;

	@FXML
	private TextField tr_number;

	final static String driverClass = "oracle.jdbc.OracleDriver";

	Connection conn = null;
	Statement sqlStatement = null;

	void ent() {

		/* try { */
		/*
		 * String mDateStr; Date startDate = null; SimpleDateFormat sdf = new
		 * SimpleDateFormat("yyyy-MM-dd");
		 * 
		 * @SuppressWarnings("deprecation") HttpClient httpclient = new
		 * DefaultHttpClient(); HttpResponse response = httpclient.execute(new
		 * HttpGet("https://google.com/")); StatusLine statusLine =
		 * response.getStatusLine(); if (statusLine.getStatusCode() == HttpStatus.SC_OK)
		 * { DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",
		 * Locale.ENGLISH); mDateStr = response.getFirstHeader("Date").getValue();
		 * startDate = df.parse(mDateStr); mDateStr = String.valueOf(startDate.getTime()
		 * / 1000); } else { // Closes the connection.
		 * response.getEntity().getContent().close(); Alert alert = new
		 * Alert(Alert.AlertType.INFORMATION); Stage stage = (Stage)
		 * alert.getDialogPane().getScene().getWindow(); stage.getIcons().add(new
		 * Image("terminal.png")); alert.setTitle("��������");
		 * alert.setHeaderText(null);
		 * alert.setContentText(statusLine.getReasonPhrase()); alert.showAndWait(); }
		 * Date date2 = sdf.parse("2019-11-25");
		 */
		/*
		 * if (startDate.after(date2)) { Alert alert = new
		 * Alert(Alert.AlertType.INFORMATION); Stage stage = (Stage)
		 * alert.getDialogPane().getScene().getWindow(); stage.getIcons().add(new
		 * Image("terminal.png")); alert.setTitle("��������");
		 * alert.setHeaderText(null);
		 * alert.setContentText("���� ������ ��� 2019-11-25, ��� �������� ;)");
		 * alert.showAndWait(); } else {
		 */

		/* ��������� �������� ���������� � ����� */
		try {
			/*
			 * conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ +
			 * "/" + Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
			 */
			DBUtil.dbConnect();
			Connection conn = DBUtil.conn;
			sqlStatement = conn.createStatement();
			String readRecordSQL = "SELECT user FROM dual";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

			if (!myResultSet.next()) {
				Alert alert_1 = new Alert(Alert.AlertType.INFORMATION);
				Stage stage_1 = (Stage) alert_1.getDialogPane().getScene().getWindow();
				stage_1.getIcons().add(new Image("icon.png"));
				alert_1.setTitle("��������");
				alert_1.setHeaderText(null);
				alert_1.setContentText("������ ����� ������ ��� ������");
				alert_1.showAndWait();
			} else if (chk_rigth("enter.fxml", Connect.userID_) == 1) {
				Stage stage_ = (Stage) enter_id.getScene().getWindow();
				// stage_.setMaximized(true);
				stage_.setTitle(Connect.userID_ + "@" + Connect.connectionURL_);
				Main.initRootLayout();
				Main.showFirst();
			} else {
				Alert alert_1 = new Alert(Alert.AlertType.INFORMATION);
				Stage stage_1 = (Stage) alert_1.getDialogPane().getScene().getWindow();
				stage_1.getIcons().add(new Image("icon.png"));
				alert_1.setTitle("��������");
				alert_1.setHeaderText(null);
				alert_1.setContentText("��� ����!");
				alert_1.showAndWait();
			}

		} catch (SQLException sql) {
			Alert alert_2 = new Alert(Alert.AlertType.INFORMATION);
			Stage stage_2 = (Stage) alert_2.getDialogPane().getScene().getWindow();
			stage_2.getIcons().add(new Image("icon.png"));
			alert_2.setTitle("��������");
			alert_2.setHeaderText(null);
			alert_2.setContentText(sql.toString());
			alert_2.showAndWait();
		} finally {
			if (sqlStatement != null) {
				try {
					sqlStatement.close();
				} catch (SQLException e) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("��������");
					alert.setHeaderText(null);
					alert.setContentText(e.getMessage());
					alert.showAndWait();
				}

			}
			if (conn != null) {
				// conn.close();
				DBUtil.dbDisconnect();
			}
			/* ������� ������� ����� */
			// Stage stage_ = (Stage) enter_id.getScene().getWindow();
			// stage_.close();
			/* ������� ������� ����� */
		}
		/* } */
		/*
		 * } catch (ParseException | e) { Alert alert = new
		 * Alert(Alert.AlertType.INFORMATION); Stage stage = (Stage)
		 * alert.getDialogPane().getScene().getWindow(); stage.getIcons().add(new
		 * Image("terminal.png")); alert.setTitle("��������");
		 * alert.setHeaderText(null); alert.setContentText(e.getMessage());
		 * alert.showAndWait(); }
		 */
	}

	@FXML
	void enter(ActionEvent event) {
		/* ��������� ���������� �������� ��� ���������� � ����� */
		// Connect con = new Connect();
		// con.setconnectionURL(conurl.getText());
		// con.setuserID(login.getText());
		// con.setuserPassword(pass.getText());

		try {
			Connect.connectionURL_ = conurl.getValue().toString();
			Connect.userID_ = login.getValue().toString();
			Connect.userPassword_ = pass.getText();
			ent();
		} catch (Exception e) {
			// TODO: handle exception
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

	}

	@FXML
	void enter_(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			try {
				Connect.connectionURL_ = conurl.getValue().toString();
				Connect.userID_ = login.getValue().toString();
				Connect.userPassword_ = pass.getText();
				ent();
			} catch (Exception e) {
				// TODO: handle exception
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
	}

	@FXML
	void upper_case(ActionEvent event) {
	}

	@FXML
	void initialize() {
		// System.out.println(System.getenv("TRANSACT_PATH")/*
		// System.getProperty("user.dir") */ + "connect.properties");
		try (InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties")) {

			Properties prop = new Properties();
			// load a properties file
			prop.load(input);

			ObservableList<String> items = FXCollections.observableArrayList();

			ObservableList<String> items_2 = FXCollections.observableArrayList();

			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
			while (enums.hasMoreElements()) {
				String key = enums.nextElement();
				String value = prop.getProperty(key);
				if (key.contains("user")) {
					// login.getItems().addAll(value);
					items.add(value);
				} else if (key.contains("url")) {
					// conurl.getItems().addAll(value);
					items_2.add(value);
				}
			}

//			login.getItems().addAll(prop.getProperty("user1"), prop.getProperty("user2"));
//			conurl.getItems().addAll(prop.getProperty("url1"), prop.getProperty("url2"));

			FilteredList<String> filteredItems = new FilteredList<String>(items);
			FilteredList<String> filteredItems_2 = new FilteredList<String>(items_2);
			// Then you need to provide the InputFilter with the FilteredList in the
			// constructor call.
			login.getEditor().textProperty().addListener(new InputFilter(login, filteredItems, false));

			login.setItems(filteredItems);

			conurl.getEditor().textProperty().addListener(new InputFilter(conurl, filteredItems_2, false));

			conurl.setItems(filteredItems_2);

		} catch (Exception e) {
			alert(e.getMessage());
		}
		conurl.getSelectionModel().select(0);
	}

	public int chk_rigth(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		Connection conn = DBUtil.conn;
		try {
			SqlMap sql = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String selectStmt = sql.getSql("acces_enter");
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			prepStmt.setString(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);

			System.out.println(selectStmt);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			// conn.close();
		} catch (Exception e) {
			alert(e.getMessage());
		}
		return ret;
	}

	public static void alert(String mes) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("��������");
		alert.setHeaderText(null);
		alert.setContentText(mes);
		alert.showAndWait();
	}

}
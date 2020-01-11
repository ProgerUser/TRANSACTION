package sample.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Main;
import sample.model.Connect;

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
		/*try {*/
			/*
			String mDateStr;
			Date startDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			@SuppressWarnings("deprecation")
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
				mDateStr = response.getFirstHeader("Date").getValue();
				startDate = df.parse(mDateStr);
				mDateStr = String.valueOf(startDate.getTime() / 1000);
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText(statusLine.getReasonPhrase());
				alert.showAndWait();
			}
			Date date2 = sdf.parse("2019-11-25");
*/
			/*if (startDate.after(date2)) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Дата больше чем 2019-11-25, ищи исходник ;)");
				alert.showAndWait();
			} else {*/

				/* Выполнить проверку соединения с базой */
				try {

					conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
							+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
					sqlStatement = conn.createStatement();
					String readRecordSQL = "SELECT user FROM dual";
					ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

					if (!myResultSet.next()) {
						Alert alert_1 = new Alert(Alert.AlertType.INFORMATION);
						Stage stage_1 = (Stage) alert_1.getDialogPane().getScene().getWindow();
						stage_1.getIcons().add(new Image("icon.png"));
						alert_1.setTitle("Внимание");
						alert_1.setHeaderText(null);
						alert_1.setContentText("Ошибка ввода логина или пароля");
						alert_1.showAndWait();
					} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("U146")
							| Connect.userID_.equals("AMRA_IMPORT")) {
						Stage stage_ = (Stage) enter_id.getScene().getWindow();
						stage_.setMaximized(true);
						
						stage_.setTitle("<Транзакции>____<Пользователь:"+Connect.userID_+">____<База:"+Connect.connectionURL_+">");
						
						Main.showFirst();
					}

				} catch (SQLException sql) {
					Alert alert_2 = new Alert(Alert.AlertType.INFORMATION);
					Stage stage_2 = (Stage) alert_2.getDialogPane().getScene().getWindow();
					stage_2.getIcons().add(new Image("icon.png"));
					alert_2.setTitle("Внимание");
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
							alert.setTitle("Внимание");
							alert.setHeaderText(null);
							alert.setContentText(e.getMessage());
							alert.showAndWait();
						}

					}
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("terminal.png"));
							alert.setTitle("Внимание");
							alert.setHeaderText(null);
							alert.setContentText(e.getMessage());
							alert.showAndWait();
						}
					}
					/* Закрыть текущую форму */
					// Stage stage_ = (Stage) enter_id.getScene().getWindow();
					// stage_.close();
					/* Закрыть текущую форму */
				}
			/*}*/
		/*} catch (ParseException |  e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}*/
	}

	@FXML
	void enter(ActionEvent event) {
		/* Присвоить переменным значения для соединения с базой */
		// Connect con = new Connect();
		// con.setconnectionURL(conurl.getText());
		// con.setuserID(login.getText());
		// con.setuserPassword(pass.getText());
		Connect.connectionURL_ = conurl.getValue().toString();
		Connect.userID_ = login.getValue().toString();
		Connect.userPassword_ = pass.getText();
		/* Присвоить переменным значения для соединения с базой */
		ent();
	}

	@FXML
	void enter_(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			Connect.connectionURL_ = conurl.getValue().toString();
			Connect.userID_ = login.getValue().toString();
			Connect.userPassword_ = pass.getText();
			/* Присвоить переменным значения для соединения с базой */
			ent();
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

			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
			while (enums.hasMoreElements()) {
				String key = enums.nextElement();
				String value = prop.getProperty(key);
				if (key.contains("user")) {
					login.getItems().addAll(value);
				} else if (key.contains("url")) {
					conurl.getItems().addAll(value);
				}
			}

//			login.getItems().addAll(prop.getProperty("user1"), prop.getProperty("user2"));
//			conurl.getItems().addAll(prop.getProperty("url1"), prop.getProperty("url2"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// conurl.getSelectionModel().select(0);
	}
}
package sample.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
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

	void ent() throws SQLException {
		/* ��������� �������� ���������� � ����� */
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@"
					+ Connect.connectionURL_ + "");
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
			} else {
				Main.showFirst();
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
				sqlStatement.close();
			}
			if (conn != null) {
				conn.close();
			}
			/* ������� ������� ����� */
			// Stage stage_ = (Stage) enter_id.getScene().getWindow();
			// stage_.close();
			/* ������� ������� ����� */
		}
	}

	@FXML
	void enter(ActionEvent event) throws IOException, SQLException {
		/* ��������� ���������� �������� ��� ���������� � ����� */
		// Connect con = new Connect();
		// con.setconnectionURL(conurl.getText());
		// con.setuserID(login.getText());
		// con.setuserPassword(pass.getText());
		Connect.connectionURL_ = conurl.getValue().toString();
		Connect.userID_ = login.getValue().toString();
		Connect.userPassword_ = pass.getText();
		/* ��������� ���������� �������� ��� ���������� � ����� */
		ent();
	}
    @FXML
    void enter_(KeyEvent ke) throws SQLException {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			Connect.connectionURL_ = conurl.getValue().toString();
			Connect.userID_ = login.getValue().toString();
			Connect.userPassword_ = pass.getText();
			/* ��������� ���������� �������� ��� ���������� � ����� */
			ent();
		}
    }
    
	@FXML
	void upper_case(ActionEvent event) {
	}

	@FXML
	void initialize() {
		System.out.println(System.getProperty("user.dir")+"\\connect.properties");
		try (InputStream input = new FileInputStream(System.getProperty("user.dir")+"\\connect.properties")) {
			
			
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            login.getItems().addAll(prop.getProperty("user1"),prop.getProperty("user2"));
            conurl.getItems().addAll(prop.getProperty("url1"),prop.getProperty("url2"));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }	
		//conurl.getSelectionModel().select(0);		
	}
}
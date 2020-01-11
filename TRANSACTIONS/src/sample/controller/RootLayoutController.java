package sample.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import sample.Main;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.model.Connect;
import sample.model.ViewerDAO;

@SuppressWarnings("unused")
public class RootLayoutController {

	/*
	 * //Reference to the main application private Main main;
	 * 
	 * //Is called by the main application to give a reference back to itself.
	 * public void setMain (Main main) { this.main = main; }
	 */
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private MenuItem chekreport;
	
	@FXML
	private    Menu statusbar;

	@FXML
	void handleExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void chektransact(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")) {
				Main.showEmployeeView();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void DBCONNECT(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Program Information");
				alert.setHeaderText("Информация о DB");
				alert.setContentText(
						"Схема: " + Connect.connectionURL_ + "\r\n" + "Пользователь: " + Connect.userID_ + "\r\n");
				alert.show();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void term_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("U146")
					| Connect.userID_.equals("AMRA_IMPORT")) {
				Main.showAmTr();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void loadtransact(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")) {
				Main.Transact();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void loadhistory(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")
					|| Connect.userID_.equals("U146")) {
				Main.Show_Hist();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void Termdial_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")) {
				Main.Termdial_view_();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void amra_trans(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")) {
				Main.Transact_Amra();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void termview(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")
					| Connect.userID_.equals("U146")) {
				Main.Terminal();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void Kash(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")) {
				Main.showKash();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void service(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (Connect.userID_.equals("XXI") | Connect.userID_.equals("AMRA_IMPORT")
					| Connect.userID_.equals("U146")) {
				Main.Service();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Ошибка!");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	// Help Menu button behavior
	public void handleHelp(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Информация");
		alert.setHeaderText("Работа с терминалами 1.3.0");
		alert.setContentText("Загрузка транзакции, администрирование терминалов\n PACHULIYA_S_V 2018");
		alert.show();
	}

	public void inis() {
		statusbar.setText(Connect.userID_+"/"+Connect.connectionURL_);
	}
	@FXML
	 void initialize() {
		assert chekreport != null : "fx:id=\"chekreport\" was not injected: check your FXML file 'RootLayout.fxml'.";
	}
}

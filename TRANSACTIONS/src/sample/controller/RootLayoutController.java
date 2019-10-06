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
	void handleExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void chektransact(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else {
				Main.showEmployeeView();
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

			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Program Information");
				alert.setHeaderText("Информация о DB");
				alert.setContentText(
						"Схема: " + Connect.connectionURL_ + "\r\n" + "Пользователь: " + Connect.userID_ + "\r\n");
				alert.show();
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

			} else {
				Main.Transact();
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

			} else {
				Main.Show_Hist();
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

			} else {
				Main.Termdial_view_();
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

			} else {
				Main.Transact_Amra();
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

			} else {
				Main.Terminal();
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

			} else {
				Main.showKash();
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

			} else {
				Main.Service();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			// alert.setTitle("Error");
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	// Help Menu button behavior
	public void handleHelp(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Program Information");
		alert.setHeaderText("Работа с терминалами 1.0");
		alert.setContentText("Загрузка транзакции, администрирование терминалов");
		alert.show();
	}

	@FXML
	void initialize() {
		assert chekreport != null : "fx:id=\"chekreport\" was not injected: check your FXML file 'RootLayout.fxml'.";
	}
}

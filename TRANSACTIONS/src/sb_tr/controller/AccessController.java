package sb_tr.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sb_tr.Main;
import sb_tr.model.Connect;
import sb_tr.model.TransactClass;
import sb_tr.model.ViewerDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Пачулия Саид 04.06.2020.
 */
public class AccessController {
	
	@FXML
    private TableView<?> FORMS;
    @FXML
    private TableColumn<?, ?> ID_FORM;
    @FXML
    private TableColumn<?, ?> FORM_NAME;
    @FXML
    private TableColumn<?, ?> FORMN_DESC;
    
    @FXML
    private TableView<?> USER_OUT;
    @FXML
    private TableColumn<?, ?> USR_ID_O;
    @FXML
    private TableColumn<?, ?> FIO_O;
    
    @FXML
    private TableView<?> USERS_IN;
    @FXML
    private TableColumn<?, ?> USR_ID_I;
    @FXML
    private TableColumn<?, ?> FIO_I;
    @FXML
    private TableColumn<?, ?> TYPE_ACCESS_I;

	@FXML
	private void initialize() {
		/*
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "";
			System.out.println(readRecordSQL);
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			ObservableList<String> combolist = FXCollections.observableArrayList();
			while (rs.next()) {

			}

			conn.close();
			sqlStatement.close();
			rs.close();
			combolist.clear();

		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		*/
		
	}

}

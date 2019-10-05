package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.model.Connect;
import sample.model.KashClass;
import sample.model.TerminalClass;
import sample.Main;
import sample.model.ViewerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
 * Саид 04.04.2019.
 */
@SuppressWarnings("unused")
public class KashController {

	@FXML
	private TextArea resultArea;

	@FXML
	private TableView<KashClass> employeeTable;

	@FXML
	private TableColumn<KashClass, String> cnameoper;
	@FXML
	private TableColumn<KashClass, String> ckbk;
	@FXML
	private TableColumn<KashClass, String> cpsevdo;

	// For MultiThreading
	private Executor exec;

	@FXML
	private Button report;


	@FXML
	private void initialize() {
		
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		cnameoper.setCellValueFactory(cellData -> cellData.getValue().cnameoperProperty());
		ckbk.setCellValueFactory(cellData -> cellData.getValue().ckbkProperty());
		cpsevdo.setCellValueFactory(cellData -> cellData.getValue().cpsevdoProperty());
	}

	// Search all transacts
	@FXML
	private void create_psevdo(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
			Statement chekStatement = conn.createStatement();
			String chek = "select IDOV_PLAT from ov_plat where CPSEVDO is null";
			ResultSet rs = chekStatement.executeQuery(chek);
			if (rs.next()) {
				ViewerDAO.kash_psevdo();
				ObservableList<KashClass> empData = ViewerDAO.searchKash();
				populateKash(empData);
				ViewerDAO.delete_kash_psevdo();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Error");
				alert.setContentText("Нет данных!");
				alert.show();
			}
			rs.close();
		} catch (SQLException e) {
			resultArea.setText("Произошла ошибка при получении информации о транзакциях из БД.\n" + e.getMessage());
			throw e;
		}
	}

	@FXML
	private AnchorPane ap;

	private void populateKash(ObservableList<KashClass> trData) throws ClassNotFoundException {
		employeeTable.setItems(trData);
	}
}

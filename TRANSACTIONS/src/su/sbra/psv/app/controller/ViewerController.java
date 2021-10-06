package su.sbra.psv.app.controller;

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
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.TransactClass;
import su.sbra.psv.app.model.ViewerDAO;

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

import org.apache.commons.lang3.exception.ExceptionUtils;

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
public class ViewerController {

	final static String driverClass = "oracle.jdbc.OracleDriver";
	static String connectionURL = null;
	static String userID = null;
	static String userPassword = null;
	Connection conn = null;
	Statement sqlStatement = null;

	@FXML
	private TextField empIdText;
	@FXML
	private TextArea resultArea;
	@FXML
	private TextField newEmailText;
	@FXML
	private TextField nameText;
	@FXML
	private TextField surnameText;

	@FXML
	private TextField datestart;

	@FXML
	private TextField dateend;

	@FXML
	private TextField fio;

	@FXML
	private TextField trnumber;

	@FXML
	private TextField conurl;

	@FXML
	private Button enter_id;

	@FXML
	private TextField login;

	@FXML
	private PasswordField pass;

	@FXML
	private TextField emailText;
	@FXML
	private TableView<TransactClass> employeeTable;

	@FXML
	private MenuItem loadtransact;

	@FXML
	private MenuItem chekreport;

	@FXML
	private Menu menu;

	@FXML
	private TableColumn<TransactClass, String> PAYMENTNUMBER;
	@FXML
	private TableColumn<TransactClass, String> FIO;
	@FXML
	private TableColumn<TransactClass, String> DATETIMEPAYMEN;
	@FXML
	private TableColumn<TransactClass, String> ACCOUNT;
	@FXML
	private TableColumn<TransactClass, String> PAYMENTDATA;
	@FXML
	private TableColumn<TransactClass, String> INSUM;
	@FXML
	private TableColumn<TransactClass, String> FEESUM;
	@FXML
	private TableColumn<TransactClass, String> SESS_ID;

	// For MultiThreading
	private Executor exec;

	@FXML
	private Button report;

	// Initializing the controller class.
	// This method is automatically called after the fxml file has been loaded.

	@FXML
	private void initialize() {
		/*
		 * The setCellValueFactory(...) that we set on the table columns are used to
		 * determine which field inside the Employee objects should be used for the
		 * particular column. The arrow -> indicates that we're using a Java 8 feature
		 * called Lambdas. (Another option would be to use a PropertyValueFactory, but
		 * this is not type-safe
		 * 
		 * We're only using StringProperty values for our table columns in this example.
		 * When you want to use IntegerProperty or DoubleProperty, the
		 * setCellValueFactory(...) must have an additional asObject():
		 */

		// For multithreading: Create executor that uses daemon threads:
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		PAYMENTNUMBER.setCellValueFactory(cellData -> cellData.getValue().PAYMENTNUMBERProperty());
		FIO.setCellValueFactory(cellData -> cellData.getValue().FIOProperty());
		DATETIMEPAYMEN.setCellValueFactory(cellData -> cellData.getValue().DATETIMEPAYMENTProperty());
		ACCOUNT.setCellValueFactory(cellData -> cellData.getValue().ACCOUNTProperty());
		PAYMENTDATA.setCellValueFactory(cellData -> cellData.getValue().PAYMENTDATAProperty());
		INSUM.setCellValueFactory(cellData -> cellData.getValue().INSUMProperty());
		FEESUM.setCellValueFactory(cellData -> cellData.getValue().FEESUMProperty());
		SESS_ID.setCellValueFactory(cellData -> cellData.getValue().SESS_IDProperty());
	}

	// Search all transacts
	@FXML
	private void searchEmployee(ActionEvent actionEvent) throws ClassNotFoundException {
		if (fio.getText().equals("")) {
			resultArea.setText("Поле ФИО пустое, введите значение!\n");
			return;
		}
		// Get all Employees information
		ObservableList<TransactClass> empData = ViewerDAO.searchEmployees(fio.getText(), trnumber.getText(),
				datestart.getText(), dateend.getText());
		// Populate Employees on TableView
		populateEmployees(empData);
	}

	// Populate Employees for TableView with MultiThreading (This is for example
	// usage)
	private void fillEmployeeTable(ActionEvent event) {
		Task<List<TransactClass>> task = new Task<List<TransactClass>>() {
			@Override
			public ObservableList<TransactClass> call() throws ClassNotFoundException {
				return ViewerDAO.searchEmployees(fio.getText(), trnumber.getText(), datestart.getText(),
						dateend.getText());
			}
		};

		task.setOnFailed(e -> task.getException().printStackTrace());
		task.setOnSucceeded(e -> employeeTable.setItems((ObservableList<TransactClass>) task.getValue()));
		exec.execute(task);
	}

	// Populate Employees for TableView
	private void populateEmployees(ObservableList<TransactClass> trData) {
		// Set items to the employeeTable
		employeeTable.setItems(trData);
	}

	@FXML
	void chekreport(ActionEvent event) {

	}

	@FXML
	void report(ActionEvent event) {
		try {
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
			String strDate = dateFormat.format(date);
			if (employeeTable.getSelectionModel().getSelectedItem() == null) {
				resultArea.setText("Выберите сначала данные из таблицы!\n");
			} else {
				TransactClass tr = employeeTable.getSelectionModel().getSelectedItem();
				PrintWriter writer = new PrintWriter(
						System.getenv("TRANSACT_PATH").toString() + "\\" + strDate + "CHEK.txt");
				writer.write("*********************************\r\n" + "*****СБЕРБАНК  АБХАЗИИ (ОАО)*****\r\n"
						+ "*********************************\r\n" + "Терминал N: " + tr.getIDTERM() + "\r\n"
						+ "Адрес терминала:\r\n" + "" + tr.getADDRESS() + "\r\n" + "Чек N: " + tr.getPAYMENTNUMBER()
						+ "\r\n" + "Дата: " + tr.getDATETIMEPAYMENT() + "\r\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n"
						+ "~~~~~~~~~ КОПИЯ КЛИЕНТА~~~~~~~~~\r\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n"
						+ "Детский сад: " + tr.getkindergarten() + "\r\n" + "Группа:  " + tr.getchgroup() + "\r\n"
						+ "Период: " + tr.getperiod() + "\r\n" + "ФИО ребенка: " + tr.getFIO() + "\r\n"
						+ "Контактный телефон: +7(940)" + tr.getACCOUNT() + "\r\n" + "\r\n" + "Сумма платежа: "
						+ tr.getRECEIVERSUM() + " р\r\n" + "Комиссия: " + tr.getFEESUM() + "p.\r\n");
				writer.close();
				ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
						System.getenv("TRANSACT_PATH").toString() + "\\" + strDate + "CHEK.txt");
				pb.start();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
	}
}

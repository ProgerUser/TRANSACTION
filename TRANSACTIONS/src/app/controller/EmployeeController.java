package app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;

import app.model.TerminalDAO;
import app.model.Transact;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Саид 04.04.2019.
 */
@SuppressWarnings("unused")
public class EmployeeController {

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
	private TextField emailText;
	@FXML
	private TableView<Transact> employeeTable;

	@FXML
	private MenuItem loadtransact;

	@FXML
	private MenuItem chekreport;

	@FXML
	private TableColumn<Transact, String> PAYMENTNUMBER;
	@FXML
	private TableColumn<Transact, String> FIO;
	@FXML
	private TableColumn<Transact, String> DATETIMEPAYMEN;
	@FXML
	private TableColumn<Transact, String> ACCOUNT;
	@FXML
	private TableColumn<Transact, String> PAYMENTDATA;
	@FXML
	private TableColumn<Transact, String> INSUM;
	@FXML
	private TableColumn<Transact, String> FEESUM;
	@FXML
	private TableColumn<Transact, String> SESS_ID;

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

	// Search an transact
	@FXML
	private void searchEmployees(ActionEvent actionEvent) {
		// Get Employee information
		Transact emp = TerminalDAO.searchTransact(fio.getText());
		// Populate Employee on TableView and Display on TextArea
		populateAndShowEmployee(emp);
	}

	// Search all transacts
	@FXML
	private void searchEmployee(ActionEvent actionEvent) {
		if (fio.getText().equals("")) {
			resultArea.setText("Поле ФИО пустое, введите значение!\n");
			return;
		}
		// Get all Employees information
		ObservableList<Transact> empData = TerminalDAO.searchEmployees(fio.getText(), trnumber.getText(),
				datestart.getText(), dateend.getText());
		// Populate Employees on TableView
		populateEmployees(empData);

	}

	// Populate Employees for TableView with MultiThreading (This is for example
	// usage)
	private void fillEmployeeTable(ActionEvent event) {
		Task<List<Transact>> task = new Task<List<Transact>>() {
			@Override
			public ObservableList<Transact> call() {
				return TerminalDAO.searchEmployees(fio.getText(), trnumber.getText(), datestart.getText(),
						dateend.getText());
			}
		};

		task.setOnFailed(e -> task.getException().printStackTrace());
		task.setOnSucceeded(e -> employeeTable.setItems((ObservableList<Transact>) task.getValue()));
		exec.execute(task);
	}

	// Populate Employee

	private void populateEmployee(Transact emp) {
		// Declare and ObservableList for table view
		ObservableList<Transact> empData = FXCollections.observableArrayList();
		// Add employee to the ObservableList
		empData.add(emp);
		// Set items to the employeeTable
		employeeTable.setItems(empData);
	}

	// Set Employee information to Text Area

	private void setEmpInfoToTextArea(Transact emp) {
		resultArea.setText("Счет: " + emp.getACCOUNT() + "\n" + "ФИО: " + emp.getFIO() + "\n" + "Назначение: "
				+ emp.getPAYMENTDATA() + "\n");
	}

	// Populate Employee for TableView and Display Employee on TextArea
	
	private void populateAndShowEmployee(Transact emp) {
		if (emp != null) {
			populateEmployee(emp);
			setEmpInfoToTextArea(emp);
		} else {
			resultArea.setText("Транзакция не найдена\n");
		}
	}

	// Populate Employees for TableView

	private void populateEmployees(ObservableList<Transact> trData) {
		// Set items to the employeeTable
		employeeTable.setItems(trData);
	}

	@FXML
	void loadtransact(ActionEvent event) {

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
			/*
			 * if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			 * resultArea.setText("Выберите сначала данные из таблицы!\n"); } else {
			 */
			Transact tr = employeeTable.getSelectionModel().getSelectedItem();

			PrintWriter writer = new PrintWriter(
					System.getenv("TRANSACT_PATH") + "Files/"  + strDate + "CHEK.txt");
			writer.write("*********************************\r\n" + "*****СБЕРБАНК  АБХАЗИИ (ОАО)*****\r\n"
					+ "*********************************\r\n" + "Терминал N: СБ 0002\r\n" + "Адрес терминала:\r\n"
					+ "г. Сухум, ул. Аидгылара 10/12\r\n" + "Чек N: " + tr.getACCOUNT() + "\r\n"
					+ "Дата: 2019.02.07 13.35.41\r\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n"
					+ "~~~~~~~~~ КОПИЯ КЛИЕНТА~~~~~~~~~\r\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n"
					+ "Детский сад: Амра \r\n" + "Группа: Младшая группа\r\n" + "Период: февраль , март,апрель\r\n"
					+ "ФИО ребенка: " + tr.getFIO() + "\r\n" + "Контактный телефон: +7(940)" + tr.getACCOUNT() + "\r\n"
					+ "\r\n" + "Сумма платежа: " + tr.getINSUM() + "  р\r\n" + "Комиссия: 50 p.\r\n");
			writer.close();
			ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
					System.getProperty("user.dir").toString() + "\\" + strDate + "CHEK.txt");
			pb.start();
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

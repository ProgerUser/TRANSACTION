package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.model.Transact;
import sample.model.TerminalDAO;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
	private void searchEmployees(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		try {
			// Get Employee information
			Transact emp = TerminalDAO.searchTransact(fio.getText());
			// Populate Employee on TableView and Display on TextArea
			populateAndShowEmployee(emp);
		} catch (SQLException e) {
			e.printStackTrace();
			resultArea.setText("Произошла ошибка при получении информации о транзакциях из БД.\n" + e);
			throw e;
		}
	}

	// Search all transacts
	@FXML
	private void searchEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			if (fio.getText().equals("")) {
				resultArea.setText("Поле ФИО пустое, введите значение!\n");
				return;
			}
			// Get all Employees information
			ObservableList<Transact> empData = TerminalDAO.searchEmployees(fio.getText(), trnumber.getText(),
					datestart.getText(), dateend.getText());
			// Populate Employees on TableView
			populateEmployees(empData);
		} catch (SQLException e) {
			// System.out.println("Произошла ошибка при получении информации о транзакциях
			// из БД.\n" + e);
			resultArea.setText("Произошла ошибка при получении информации о транзакциях из БД.\n" + e);
			throw e;
		}
	}

	// Populate Employees for TableView with MultiThreading (This is for example
	// usage)
	private void fillEmployeeTable(ActionEvent event) throws SQLException, ClassNotFoundException {
		Task<List<Transact>> task = new Task<List<Transact>>() {
			@Override
			public ObservableList<Transact> call() throws Exception {
				return TerminalDAO.searchEmployees(fio.getText(), trnumber.getText(), datestart.getText(),
						dateend.getText());
			}
		};

		task.setOnFailed(e -> task.getException().printStackTrace());
		task.setOnSucceeded(e -> employeeTable.setItems((ObservableList<Transact>) task.getValue()));
		exec.execute(task);
	}

	// Populate Employee
	@FXML
	private void populateEmployee(Transact emp) throws ClassNotFoundException {
		// Declare and ObservableList for table view
		ObservableList<Transact> empData = FXCollections.observableArrayList();
		// Add employee to the ObservableList
		empData.add(emp);
		// Set items to the employeeTable
		employeeTable.setItems(empData);
	}

	// Set Employee information to Text Area
	@FXML
	private void setEmpInfoToTextArea(Transact emp) {
		resultArea.setText("Счет: " + emp.getACCOUNT() + "\n" + "ФИО: " + emp.getFIO() + "\n" + "Назначение: "
				+ emp.getPAYMENTDATA() + "\n");
	}

	// Populate Employee for TableView and Display Employee on TextArea
	@FXML
	private void populateAndShowEmployee(Transact emp) throws ClassNotFoundException {
		if (emp != null) {
			populateEmployee(emp);
			setEmpInfoToTextArea(emp);
		} else {
			resultArea.setText("Транзакция не найдена\n");
		}
	}

	// Populate Employees for TableView
	@FXML
	private void populateEmployees(ObservableList<Transact> trData) throws ClassNotFoundException {
		// Set items to the employeeTable
		employeeTable.setItems(trData);
	}

	// Update employee's email with the email which is written on newEmailText field
	@FXML
	private void updateEmployeeEmail(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			TerminalDAO.updateEmpEmail(empIdText.getText(), newEmailText.getText());
			resultArea.setText("Email has been updated for, employee id: " + empIdText.getText() + "\n");
		} catch (SQLException e) {
			resultArea.setText("Problem occurred while updating email: " + e);
		}
	}

	// Insert an employee to the DB
	@FXML
	private void insertEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			TerminalDAO.insertEmp(nameText.getText(), surnameText.getText(), emailText.getText());
			resultArea.setText("Employee inserted! \n");
		} catch (SQLException e) {
			resultArea.setText("Problem occurred while inserting employee " + e);
			throw e;
		}
	}

	// Delete an employee with a given employee Id from DB
	@FXML
	private void deleteEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			TerminalDAO.deleteEmpWithId(empIdText.getText());
			resultArea.setText("Employee deleted! Employee id: " + empIdText.getText() + "\n");
		} catch (SQLException e) {
			resultArea.setText("Problem occurred while deleting employee " + e);
			throw e;
		}
	}

	@FXML
	void loadtransact(ActionEvent event) {

	}

	@FXML
	void chekreport(ActionEvent event) {

	}

	@FXML
	void report(ActionEvent event) throws IOException {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
		String strDate = dateFormat.format(date);
		/* 
		if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			resultArea.setText("Выберите сначала данные из таблицы!\n");
		} else {*/
			Transact tr = employeeTable.getSelectionModel().getSelectedItem();
			
			PrintWriter writer = new PrintWriter(System.getProperty("user.dir").toString() +"\\" + strDate + "CHEK.txt"); 
			  writer.write("*********************************\r\n"
					  +"*****СБЕРБАНК  АБХАЗИИ (ОАО)*****\r\n"
					  +"*********************************\r\n"
					  +"Терминал N: СБ 0002\r\n"
					  +"Адрес терминала:\r\n"
					  +"г. Сухум, ул. Аидгылара 10/12\r\n"
					  +"Чек N: "+tr.getACCOUNT()+"\r\n"
					  +"Дата: 2019.02.07 13.35.41\r\n"
					  +"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n"
					  +"~~~~~~~~~ КОПИЯ КЛИЕНТА~~~~~~~~~\r\n"
					  +"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n"
					  +"Детский сад: Амра \r\n"
					  +"Группа: Младшая группа\r\n"
					  +"Период: февраль , март,апрель\r\n"
					  +"ФИО ребенка: "+tr.getFIO()+"\r\n"
					  +"Контактный телефон: +7(940)"+tr.getACCOUNT()+"\r\n"
	                  +"\r\n"
					  +"Сумма платежа: "+tr.getINSUM()+"  р\r\n"
					  +"Комиссия: 50 p.\r\n"); 
			  writer.close();
			ProcessBuilder pb = new ProcessBuilder("Notepad.exe", System.getProperty("user.dir").toString() + "\\" + strDate + "CHEK.txt");
			pb.start();	
	}
}

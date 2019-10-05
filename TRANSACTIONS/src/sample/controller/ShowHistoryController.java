package sample.controller;

import javafx.beans.property.StringProperty;
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
import sample.model.EmployeeDAO;
import sample.model.FN_SESS_AMRA;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.Date;
/**
 * Саид 04.04.2019.
 */

public class ShowHistoryController {

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
	private TableColumn<FN_SESS_AMRA, String> PAYMENTNUMBER;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> FIO;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> DATETIMEPAYMEN;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> ACCOUNT;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> PAYMENTDATA;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> INSUM;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> FEESUM;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> SESS_ID;

	// For MultiThreading
	private Executor exec;

	@FXML
	private Button report;

	// Initializing the controller class.
	// This method is automatically called after the fxml file has been loaded.

	@FXML
	private void initialize() {
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		private StringProperty sess_id;
		private StringProperty file_name;
		private StringProperty date_time;
		private StringProperty fileclob;
		
		PAYMENTNUMBER.setCellValueFactory(cellData -> cellData.getValue().PAYMENTNUMBERProperty());
		FIO.setCellValueFactory(cellData -> cellData.getValue().FIOProperty());
		DATETIMEPAYMEN.setCellValueFactory(cellData -> cellData.getValue().DATETIMEPAYMENTProperty());
		ACCOUNT.setCellValueFactory(cellData -> cellData.getValue().ACCOUNTProperty());
	}

	// Search an transact
	@FXML
	private void searchEmployees(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
		try {
			// Get Employee information
			Transact emp = EmployeeDAO.searchTransact(fio.getText());
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
			ObservableList<Transact> empData = EmployeeDAO.searchEmployees(fio.getText(), trnumber.getText(),
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
				return EmployeeDAO.searchEmployees(fio.getText(), trnumber.getText(), datestart.getText(),
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
			EmployeeDAO.updateEmpEmail(empIdText.getText(), newEmailText.getText());
			resultArea.setText("Email has been updated for, employee id: " + empIdText.getText() + "\n");
		} catch (SQLException e) {
			resultArea.setText("Problem occurred while updating email: " + e);
		}
	}

	// Insert an employee to the DB
	@FXML
	private void insertEmployee(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			EmployeeDAO.insertEmp(nameText.getText(), surnameText.getText(), emailText.getText());
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
			EmployeeDAO.deleteEmpWithId(empIdText.getText());
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

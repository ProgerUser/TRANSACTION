package sample.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.model.Transact;
import sample.model.TransactClass;
import sample.Main;
import sample.model.Connect;
import sample.model.EmployeeDAO;
import sample.model.FN_SESS_AMRA;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.Date;

/**
 * Саид 04.04.2019.
 */

@SuppressWarnings("unused")
public class ShowHistoryController {

	@FXML
	private TableView<FN_SESS_AMRA> fn_sess_table;
	@FXML
	private TextField trnumber;
	@FXML
	private TextField sess_id_t;
	@FXML
	private TextField dateend;
	@FXML
	private TextField datestart;
	@FXML
	private TextArea resultArea;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> SESS_ID;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> FILE_NAME;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> DATE_TIME;

	// For MultiThreading
	private Executor exec;

	// Initializing the controller class.
	// This method is automatically called after the fxml file has been loaded.

	@FXML
	private void initialize() {
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		SESS_ID.setCellValueFactory(cellData -> cellData.getValue().sess_idProperty());
		FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().file_nameProperty());
		DATE_TIME.setCellValueFactory(cellData -> cellData.getValue().date_timeProperty());
	}

	// Найти загрузки
	@FXML
	private void view_clob(ActionEvent actionEvent) {
		if (fn_sess_table.getSelectionModel().getSelectedItem() == null) {
			resultArea.setText("Выберите сначала данные из таблицы!\n");
		} else {
			FN_SESS_AMRA fn = fn_sess_table.getSelectionModel().getSelectedItem();

			Connect.SESS_ID_ = fn.getsess_id();
			Main.showAmTr();
		}
	}

	// Найти загрузки
	@FXML
	private void fn_sess_search(ActionEvent actionEvent) {
		try {
			// Get all Employees information
			ObservableList<FN_SESS_AMRA> empData = EmployeeDAO.srch_fn_sess(sess_id_t.getText(), trnumber.getText(),
					datestart.getText(), dateend.getText());
			// Populate Employees on TableView
			populate_fn_sess(empData);
		} catch (SQLException | ParseException | ClassNotFoundException e) {
			resultArea.setText(e.getMessage());
		}
	}

	// Заполнить таблицу
	@FXML
	private void populate_fn_sess(ObservableList<FN_SESS_AMRA> trData) {
		// Set items to the employeeTable
		fn_sess_table.setItems(trData);
	}
}
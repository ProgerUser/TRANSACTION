package sample.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Transact;
import sample.model.TransactClass;
import sample.Main;
import sample.model.TerminalDAO;
import sample.model.FN_SESS_AMRA;
import sample.model.Termdial;
import sample.model.Amra_Trans;
import sample.model.Attributes;
import sample.model.Connect;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.Date;

/**
 * Саид 04.04.2019.
 */

@SuppressWarnings("unused")
public class Termdial_Controller {

	@FXML
	private TableColumn<Termdial, String> dealstartdate;

	@FXML
	private TableColumn<Termdial, String> sum_;

	@FXML
	private TableColumn<Termdial, String> paymentnumber;

	@FXML
	private TableColumn<Termdial, String> sess_id;

	@FXML
	private TableColumn<Termdial, String> dealenddate;

	@FXML
	private TextArea resultArea;

	@FXML
	private TableView<Termdial> termdeal_table;

	@FXML
	private TableColumn<Termdial, String> dealpaymentnumber;

	@FXML
	private TableColumn<Termdial, String> department;

	@FXML
	private TableColumn<Termdial, String> recdate;

	@FXML
	private TableColumn<Termdial, String> status;

	// For MultiThreading
	private Executor exec;

	@FXML
	private TextField sess_id_t;
	@FXML
	private TextField trnumber;
	@FXML
	private TextField datestart;
	@FXML
	private TextField dateend;

	@FXML
	private void initialize() {
		termdeal_table.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		recdate.setCellValueFactory(cellData -> cellData.getValue().recdateProperty());
		department.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
		paymentnumber.setCellValueFactory(cellData -> cellData.getValue().paymentnumberProperty());
		dealstartdate.setCellValueFactory(cellData -> cellData.getValue().dealstartdateProperty());
		sum_.setCellValueFactory(cellData -> cellData.getValue().sum_Property());
		dealenddate.setCellValueFactory(cellData -> cellData.getValue().dealenddateProperty());
		dealpaymentnumber.setCellValueFactory(cellData -> cellData.getValue().dealpaymentnumberProperty());
		status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		sess_id.setCellValueFactory(cellData -> cellData.getValue().sess_idProperty());

		recdate.setCellFactory(TextFieldTableCell.forTableColumn());
		department.setCellFactory(TextFieldTableCell.forTableColumn());
		paymentnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		dealstartdate.setCellFactory(TextFieldTableCell.forTableColumn());
		sum_.setCellFactory(TextFieldTableCell.forTableColumn());
		dealenddate.setCellFactory(TextFieldTableCell.forTableColumn());
		dealpaymentnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		status.setCellFactory(TextFieldTableCell.forTableColumn());
		sess_id.setCellFactory(TextFieldTableCell.forTableColumn());

		recdate.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_recdate(t.getNewValue());
			}
		});
		department.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_department(t.getNewValue());
			}
		});
		paymentnumber.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_paymentnumber(t.getNewValue());
			}
		});
		dealstartdate.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dealstartdate(t.getNewValue());
			}
		});
		sum_.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_sum_(t.getNewValue());
			}
		});
		dealenddate.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dealenddate(t.getNewValue());
			}
		});
		dealpaymentnumber.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dealpaymentnumber(t.getNewValue());
			}
		});
		status.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_status(t.getNewValue());
			}
		});
		sess_id.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sess_id(t.getNewValue());
			}
		});

	}

	@FXML
	private void termdial_srch(ActionEvent actionEvent) throws IOException {
		try {
			ObservableList<Termdial> empData = TerminalDAO.Termdial_(datestart.getText(), dateend.getText(),
					trnumber.getText(), sess_id_t.getText());
			populate_termdial(empData);
		} catch (SQLException | ParseException | ClassNotFoundException e) {
			resultArea.setText(e.getMessage());
		}
	}

	// Заполнить таблицу
	@FXML
	private void populate_termdial(ObservableList<Termdial> trData) {
		// Set items to the employeeTable
		termdeal_table.setItems(trData);
	}
}
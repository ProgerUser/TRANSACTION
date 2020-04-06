package sb_tr.controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sb_tr.Main;
import sb_tr.model.Amra_Trans;
import sb_tr.model.Attributes;
import sb_tr.model.Connect;
import sb_tr.model.CustomDate;
import sb_tr.model.FN_SESS_AMRA;
import sb_tr.model.Termdial;
import sb_tr.model.TerminalDAO;
import sb_tr.model.Transact;
import sb_tr.model.TransactClass;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.controlsfx.control.table.TableFilter;

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
	private TableView<Termdial> termdeal_table;

	@FXML
	private TableColumn<Termdial, String> dealpaymentnumber;

	@FXML
	private TableColumn<Termdial, String> department;

	@FXML
	private TableColumn<Termdial, String> recdate;

	@FXML
	private TableColumn<Termdial, String> vector;

	@FXML
	private TableColumn<Termdial, String> status;

	// For MultiThreading
	private Executor exec;

	@FXML
	private TextField sess_id_t;
	@FXML
	private TextField trnumber;
	@FXML
	private DatePicker datestart;
	@FXML
	private DatePicker dateend;
	@FXML
	private CheckBox feb;

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
		vector.setCellValueFactory(cellData -> cellData.getValue().VECTORProperty());

		recdate.setCellFactory(TextFieldTableCell.forTableColumn());
		department.setCellFactory(TextFieldTableCell.forTableColumn());
		paymentnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		dealstartdate.setCellFactory(TextFieldTableCell.forTableColumn());
		sum_.setCellFactory(TextFieldTableCell.forTableColumn());
		dealenddate.setCellFactory(TextFieldTableCell.forTableColumn());
		dealpaymentnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		status.setCellFactory(TextFieldTableCell.forTableColumn());
		sess_id.setCellFactory(TextFieldTableCell.forTableColumn());
		vector.setCellFactory(TextFieldTableCell.forTableColumn());

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
		vector.setOnEditCommit(new EventHandler<CellEditEvent<Termdial, String>>() {
			@Override
			public void handle(CellEditEvent<Termdial, String> t) {
				((Termdial) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sess_id(t.getNewValue());
			}
		});
	}

	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("sess_id")) {

			} else {
				// Minimal width = columnheader
				Text t = new Text(column.getText());
				double max = t.getLayoutBounds().getWidth();
				for (int i = 0; i < table.getItems().size(); i++) {
					// cell must not be empty
					if (column.getCellData(i) != null) {
						t = new Text(column.getCellData(i).toString());
						double calcwidth = t.getLayoutBounds().getWidth();
						// remember new max-width
						if (calcwidth > max) {
							max = calcwidth;
						}
					}
				}
				// set the new max-widht with some extra space
				column.setPrefWidth(max + 10.0d);
			}
		});
	}

	@FXML
	private void termdial_srch(ActionEvent actionEvent) {

		ObservableList<Termdial> empData = null;
		if (feb.isSelected()) {
			empData = TerminalDAO.Termdial_(datestart.getValue(), dateend.getValue(), trnumber.getText(),
					sess_id_t.getText(), false);
		} else {
			empData = TerminalDAO.Termdial_(datestart.getValue(), dateend.getValue(), trnumber.getText(),
					sess_id_t.getText(), true);
		}
		populate_termdial(empData);
		autoResizeColumns(termdeal_table);
		@SuppressWarnings("deprecation")
		TableFilter<Termdial> filter = new TableFilter<>(termdeal_table);
	}

	// Заполнить таблицу

	private void populate_termdial(ObservableList<Termdial> trData) {
		// Set items to the employeeTable
		termdeal_table.setItems(trData);
	}
}
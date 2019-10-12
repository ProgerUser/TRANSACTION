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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Transact;
import sample.model.TransactClass;
import sample.Main;
import sample.model.Amra_Trans;
import sample.model.Connect;
import sample.model.TerminalDAO;
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
	private DatePicker dateend;
	@FXML
	private DatePicker datestart;

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
		
		fn_sess_table.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		SESS_ID.setCellValueFactory(cellData -> cellData.getValue().sess_idProperty());
		FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().file_nameProperty());
		DATE_TIME.setCellValueFactory(cellData -> cellData.getValue().date_timeProperty());

		DATE_TIME.setCellFactory(TextFieldTableCell.forTableColumn());
		FILE_NAME.setCellFactory(TextFieldTableCell.forTableColumn());
		SESS_ID.setCellFactory(TextFieldTableCell.forTableColumn());

		DATE_TIME.setOnEditCommit(new EventHandler<CellEditEvent<FN_SESS_AMRA, String>>() {
			@Override
			public void handle(CellEditEvent<FN_SESS_AMRA, String> t) {
				((FN_SESS_AMRA) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setdate_time(t.getNewValue());
			}
		});

		FILE_NAME.setOnEditCommit(new EventHandler<CellEditEvent<FN_SESS_AMRA, String>>() {
			@Override
			public void handle(CellEditEvent<FN_SESS_AMRA, String> t) {
				((FN_SESS_AMRA) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setfile_name(t.getNewValue());
			}
		});

		SESS_ID.setOnEditCommit(new EventHandler<CellEditEvent<FN_SESS_AMRA, String>>() {
			@Override
			public void handle(CellEditEvent<FN_SESS_AMRA, String> t) {
				((FN_SESS_AMRA) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setsess_id(t.getNewValue());
			}
		});
	}

	// Найти загрузки
	@FXML
	private void view_clob(ActionEvent actionEvent) throws IOException {
		if (fn_sess_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(("Выберите сначала данные из таблицы!\n"));
			alert.showAndWait();
		} else {
			FN_SESS_AMRA fn = fn_sess_table.getSelectionModel().getSelectedItem();

			Connect.SESS_ID_ = fn.getsess_id();

			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Транзакции");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
			stage.show();
		}
	}

	// Найти загрузки
	@FXML
	private void fn_sess_search(ActionEvent actionEvent) {
		try {
			// Get all Employees information
			ObservableList<FN_SESS_AMRA> empData = TerminalDAO.srch_fn_sess(sess_id_t.getText(), trnumber.getText(),
					datestart.getValue(), dateend.getValue());
			// Populate Employees on TableView
			populate_fn_sess(empData);

		} catch (SQLException | ParseException | ClassNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// Заполнить таблицу
	@FXML
	private void populate_fn_sess(ObservableList<FN_SESS_AMRA> trData) {
		// Set items to the employeeTable
		fn_sess_table.setItems(trData);
	}
}
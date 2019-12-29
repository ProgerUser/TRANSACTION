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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.model.Amra_Trans;
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

import org.controlsfx.control.table.TableFilter;

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
	private TableView<KashClass> employeeTable;

	@FXML
	private TableColumn<KashClass, String> cnameoper;
	@FXML
	private TableColumn<KashClass, String> ckbk;
	@FXML
	private TableColumn<KashClass, String> cpsevdo;
	@FXML
	private TableColumn<KashClass, String> C_CASHNAME;

	// For MultiThreading
	private Executor exec;

	@FXML
	private Button report;

	@FXML
	private void initialize() {
		employeeTable.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		cnameoper.setCellValueFactory(cellData -> cellData.getValue().cnameoperProperty());
		ckbk.setCellValueFactory(cellData -> cellData.getValue().ckbkProperty());
		cpsevdo.setCellValueFactory(cellData -> cellData.getValue().cpsevdoProperty());
		C_CASHNAME.setCellValueFactory(cellData -> cellData.getValue().C_CASHNAMEProperty());
		
		cnameoper.setCellFactory(TextFieldTableCell.forTableColumn());
		ckbk.setCellFactory(TextFieldTableCell.forTableColumn());
		cpsevdo.setCellFactory(TextFieldTableCell.forTableColumn());
		C_CASHNAME.setCellFactory(TextFieldTableCell.forTableColumn());
		
		cnameoper.setOnEditCommit(new EventHandler<CellEditEvent<KashClass, String>>() {
			@Override
			public void handle(CellEditEvent<KashClass, String> t) {
				((KashClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setcnameoper(t.getNewValue());
			}
		});
		
		ckbk.setOnEditCommit(new EventHandler<CellEditEvent<KashClass, String>>() {
			@Override
			public void handle(CellEditEvent<KashClass, String> t) {
				((KashClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setckbk(t.getNewValue());
			}
		});
		
		cpsevdo.setOnEditCommit(new EventHandler<CellEditEvent<KashClass, String>>() {
			@Override
			public void handle(CellEditEvent<KashClass, String> t) {
				((KashClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setcpsevdo(t.getNewValue());
			}
		});
		
		C_CASHNAME.setOnEditCommit(new EventHandler<CellEditEvent<KashClass, String>>() {
			@Override
			public void handle(CellEditEvent<KashClass, String> t) {
				((KashClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setC_CASHNAME(t.getNewValue());
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
	// Search all transacts
	@FXML
	private void create_psevdo(ActionEvent actionEvent) {
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
				autoResizeColumns(employeeTable);
				TableFilter<KashClass> filter = new TableFilter<>(employeeTable);
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Warning");
				alert.setContentText("Нет данных!");
				alert.show();
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private void populateKash(ObservableList<KashClass> trData) {
		employeeTable.setItems(trData);
	}
}

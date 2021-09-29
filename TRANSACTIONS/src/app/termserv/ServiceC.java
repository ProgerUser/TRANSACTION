package app.termserv;

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
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import sbalert.Msg;

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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;

import app.Main;
import app.model.Amra_Trans;
import app.model.Connect;
import app.model.ServiceClass;
import app.model.TerminalClass;
import app.model.TerminalForCombo;
import app.model.TransactClass;
import app.model.ViewerDAO;
import app.util.DBUtil;

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
public class ServiceC {
	@FXML
	private TableView<ServiceClass> employeeTable;
	@FXML
	private ComboBox<String> terms;
	@FXML
	private TableColumn<ServiceClass, String> acc_name;
	@FXML
	private TableColumn<ServiceClass, String> acc_rec;
	@FXML
	private TableColumn<ServiceClass, String> account;
	@FXML
	private TableColumn<ServiceClass, String> idterm;
	@FXML
	private TableColumn<ServiceClass, String> inn;
	@FXML
	private TableColumn<ServiceClass, String> kbk;
	@FXML
	private TableColumn<ServiceClass, String> kpp;
	@FXML
	private TableColumn<ServiceClass, String> name;
	@FXML
	private TableColumn<ServiceClass, String> okato;
	@FXML
	private TableColumn<ServiceClass, String> bo1;
	@FXML
	private TableColumn<ServiceClass, String> bo2;
	@FXML
	private TableColumn<ServiceClass, String> comission;
	
	private Executor exec;

	@FXML
	private void initialize() {
		try {
			employeeTable.setEditable(true);
			
			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});
			
			acc_name.setCellValueFactory(cellData -> cellData.getValue().acc_nameProperty());
			acc_rec.setCellValueFactory(cellData -> cellData.getValue().acc_recProperty());
			account.setCellValueFactory(cellData -> cellData.getValue().accountProperty());
			idterm.setCellValueFactory(cellData -> cellData.getValue().idtermProperty());
			inn.setCellValueFactory(cellData -> cellData.getValue().innProperty());
			kbk.setCellValueFactory(cellData -> cellData.getValue().kbkProperty());
			kpp.setCellValueFactory(cellData -> cellData.getValue().kppProperty());
			name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			okato.setCellValueFactory(cellData -> cellData.getValue().okatoProperty());
			bo1.setCellValueFactory(cellData -> cellData.getValue().bo1Property());
			bo2.setCellValueFactory(cellData -> cellData.getValue().bo2Property());
			comission.setCellValueFactory(cellData -> cellData.getValue().comissionProperty());

			{
				Statement sqlStatement = DBUtil.conn.createStatement();
				String readRecordSQL = "select NAME from Z_SB_TERMINAL_AMRA_DBT t";
				ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
				ObservableList<String> combolist = FXCollections.observableArrayList();
				while (rs.next()) {
					combolist.add(rs.getString("NAME"));
				}
				terms.setItems(combolist);
				terms.getSelectionModel().select(0);
				rs.close();
				sqlStatement.close();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("bo2")) {

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
				column.setPrefWidth(max + 20.0d);
			}
		});
	}

	/**
	 * 
	 * @param actionEvent
	 */
	@FXML
	void searchService(ActionEvent actionEvent) {

	}

	@FXML
	void Delete(ActionEvent actionEvent_) {
		if (employeeTable.getSelectionModel().getSelectedItem() != null) {
			ServiceClass tr = employeeTable.getSelectionModel().getSelectedItem();
		}
	}

	@FXML
	void UpdateService(ActionEvent actionEvent_) {
		if (employeeTable.getSelectionModel().getSelectedItem() != null) {
			ServiceClass tr = employeeTable.getSelectionModel().getSelectedItem();
		}
	}

	@FXML
	void add(ActionEvent actionEvent_) {
	}
}

package su.sbra.psv.app.controller;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Amra_Trans;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.FN_SESS_AMRA;
import su.sbra.psv.app.model.TerminalDAO;
import su.sbra.psv.app.model.Transact;
import su.sbra.psv.app.model.TransactClass;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;

import javafx.application.Platform;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
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
 * ���� 04.04.2019.
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
	private Button search;

	@FXML
	private TableColumn<FN_SESS_AMRA, String> DATE_;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> SESS_ID;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> user_;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> status;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> FILE_NAME;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> path_;
	@FXML
	private TableColumn<FN_SESS_AMRA, String> DATE_TIME;
	@FXML
	private ProgressIndicator pb;

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
		fn_sess_table.setEditable(true);
		SESS_ID.setCellValueFactory(cellData -> cellData.getValue().sess_idProperty());
		FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().file_nameProperty());
		DATE_TIME.setCellValueFactory(cellData -> cellData.getValue().date_timeProperty());
		path_.setCellValueFactory(cellData -> cellData.getValue().path_Property());
		status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		user_.setCellValueFactory(cellData -> cellData.getValue().userProperty());
		DATE_.setCellValueFactory(cellData -> cellData.getValue().date_Property());

		DATE_TIME.setCellFactory(TextFieldTableCell.forTableColumn());
		FILE_NAME.setCellFactory(TextFieldTableCell.forTableColumn());
		SESS_ID.setCellFactory(TextFieldTableCell.forTableColumn());
		path_.setCellFactory(TextFieldTableCell.forTableColumn());
		user_.setCellFactory(TextFieldTableCell.forTableColumn());
		DATE_.setCellFactory(TextFieldTableCell.forTableColumn());

		DATE_.setOnEditCommit(new EventHandler<CellEditEvent<FN_SESS_AMRA, String>>() {
			@Override
			public void handle(CellEditEvent<FN_SESS_AMRA, String> t) {
				((FN_SESS_AMRA) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setdate_(t.getNewValue());
			}
		});
		user_.setOnEditCommit(new EventHandler<CellEditEvent<FN_SESS_AMRA, String>>() {
			@Override
			public void handle(CellEditEvent<FN_SESS_AMRA, String> t) {
				((FN_SESS_AMRA) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setuser(t.getNewValue());
			}
		});

		path_.setOnEditCommit(new EventHandler<CellEditEvent<FN_SESS_AMRA, String>>() {
			@Override
			public void handle(CellEditEvent<FN_SESS_AMRA, String> t) {
				((FN_SESS_AMRA) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setpath_(t.getNewValue());
			}
		});

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

		/*
		 * fn_sess_table.setRowFactory(tv -> new TableRow<FN_SESS_AMRA>() {
		 * 
		 * @Override public void updateItem(FN_SESS_AMRA item, boolean empty) {
		 * super.updateItem(item, empty); if (item == null) { setStyle(""); } else if
		 * (item.getstatus_().equals("���������")) { setStyle(""); } else {
		 * status.setStyle("-fx-background-color: #F9E02C;"); } } });
		 */

		/*
		 * status.setCellFactory(column -> { return new TableCell<FN_SESS_AMRA,
		 * String>() {
		 * 
		 * @Override protected void updateItem(String item, boolean empty) {
		 * super.updateItem(item, empty);
		 * 
		 * setText(empty ? "" : getItem().toString()); setGraphic(null);
		 * 
		 * TableRow<FN_SESS_AMRA> currentRow = getTableRow();
		 * 
		 * if (!isEmpty()) { if(item.equals("���������")) {
		 * 
		 * currentRow.get setStyle(""); } else {
		 * currentRow.setStyle("-fx-background-color: #F9E02C;"); } } } }; });
		 */
	}

	// Populate Employees for TableView with MultiThreading (This is for example
	// usage)
	@FXML
	void Search_(ActionEvent event) throws SQLException, ClassNotFoundException {
		search.setDisable(true);
		pb.setVisible(true);
		Task<List<FN_SESS_AMRA>> task = new Task<List<FN_SESS_AMRA>>() {
			@Override
			public ObservableList<FN_SESS_AMRA> call() throws Exception {
				return TerminalDAO.srch_fn_sess(sess_id_t.getText(), trnumber.getText(), datestart.getValue(),
						dateend.getValue());
			}
		};

		task.setOnFailed(e -> Alert(task.getException().getMessage()));
		task.setOnSucceeded(e -> test((ObservableList<FN_SESS_AMRA>) task.getValue()));

		exec.execute(task);
		/*-----------------------------------------*/

	}

	// ����� ��������
	@FXML
	void OpenLoadTr(ActionEvent actionEvent) {
		try {
			if (fn_sess_table.getSelectionModel().getSelectedItem() != null) {
				FN_SESS_AMRA fn = fn_sess_table.getSelectionModel().getSelectedItem();

				Connect.SESS_ID_ = fn.getsess_id();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/view/Transact_Amra_viewer_from_show.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("����������� ���������� SESS_ID = " + fn.getsess_id());
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();

			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public void Alert(String mes) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Msg.Message(mes);
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

	/**
	 * 
	 * @param actionEvent
	 */
	@FXML
	void ViewReport(ActionEvent actionEvent) {
		try {
			if (fn_sess_table.getSelectionModel().getSelectedItem() != null) {
				FN_SESS_AMRA fn = fn_sess_table.getSelectionModel().getSelectedItem();
				new PrintReport().showReport(fn.getsess_id());
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	// ����� ��������
	@FXML
	void Search(ActionEvent actionEvent) throws ClassNotFoundException, UnknownHostException {
		search.setDisable(true);
		// Get all Employees information
		ObservableList<FN_SESS_AMRA> empData = TerminalDAO.srch_fn_sess(sess_id_t.getText(), trnumber.getText(),
				datestart.getValue(), dateend.getValue());
		// Populate Employees on TableView
		populate_fn_sess(empData);
		autoResizeColumns(fn_sess_table);
		status.setCellFactory(col -> new TableCell<FN_SESS_AMRA, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
					if (item.equals("���������")) {
						setStyle("");
					} else {
						setStyle("-fx-background-color: #F9E02C;");
					}
				}
			}
		});
		search.setDisable(false);
		// GUIUtils.autoFitTable(fn_sess_table);
	}

	// ��������� �������

	void populate_fn_sess(ObservableList<FN_SESS_AMRA> trData) {
		// Set items to the employeeTable
		fn_sess_table.setItems(trData);
	}

	void test(ObservableList<FN_SESS_AMRA> trData) {
		// Set items to the employeeTable
		fn_sess_table.setItems(trData);

		autoResizeColumns(fn_sess_table);
		status.setCellFactory(col -> new TableCell<FN_SESS_AMRA, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
					if (item.equals("���������")) {
						setStyle("");
					} else {
						setStyle("-fx-background-color: #F9E02C;");
					}
				}
			}
		});
		@SuppressWarnings("deprecation")
		TableFilter<FN_SESS_AMRA> filter = new TableFilter<>(fn_sess_table);
		pb.setVisible(false);
		search.setDisable(false);
	}
}
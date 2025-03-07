package su.sbra.psv.app.terminals;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.contact.SBRA_CONTACT_ACC_CODE;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Amra_Trans;
import su.sbra.psv.app.model.ViewerDAO;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.sverka.SverkaC;
import su.sbra.psv.app.termserv.Z_SB_TERMSERV_AMRA_DBT;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * ���� 04.04.2019 <br>
 * ���������� 28.09.2021
 */
@SuppressWarnings("unused")
public class TerminalC {

	@FXML
	private TableView<Z_SB_TERMINAL_AMRA_DBT> SbTerminal;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> ACCOUNT;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> ADDRESS;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, Integer> DEPARTMENT;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> NAME;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> GENERAL_ACC;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> CRASH_ACC;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> DEAL_ACC;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> GENERAL_COMIS;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> CLEAR_SUM;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> INCOME;
	@FXML
	private TableColumn<Z_SB_TERMINAL_AMRA_DBT, String> SDNAME;

	@FXML
	private VBox vbox;
	private Executor exec;

	/**
	 * �������������
	 */
	@FXML
	private void initialize() {
		try {
			SbTerminal.setEditable(true);
			// For multi threading: Create executor that uses daemon threads:
			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			ACCOUNT.setCellValueFactory(cellData -> cellData.getValue().ACCOUNTProperty());
			ADDRESS.setCellValueFactory(cellData -> cellData.getValue().ADDRESSProperty());
			DEPARTMENT.setCellValueFactory(cellData -> cellData.getValue().DEPARTMENTProperty().asObject());
			NAME.setCellValueFactory(cellData -> cellData.getValue().NAMEProperty());
			GENERAL_ACC.setCellValueFactory(cellData -> cellData.getValue().GENERAL_ACCProperty());
			CRASH_ACC.setCellValueFactory(cellData -> cellData.getValue().CRASH_ACCProperty());
			DEAL_ACC.setCellValueFactory(cellData -> cellData.getValue().DEAL_ACCProperty());
			GENERAL_COMIS.setCellValueFactory(cellData -> cellData.getValue().GENERAL_COMISProperty());
			CLEAR_SUM.setCellValueFactory(cellData -> cellData.getValue().CLEAR_SUMProperty());
			INCOME.setCellValueFactory(cellData -> cellData.getValue().INCOMEProperty());
			SDNAME.setCellValueFactory(cellData -> cellData.getValue().SDNAMEProperty());
			//
			LoadTable();
			//
			SbTerminal.setRowFactory(tv -> {
				TableRow<Z_SB_TERMINAL_AMRA_DBT> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Z_SB_TERMINAL_AMRA_DBT rowData = row.getItem();
						Edit(null);
					}
				});
				return row;
			});
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * ���� ����������
	 * 
	 * @param table
	 */
	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("SDNAME")) {

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
	 * �����
	 * 
	 * @param actionEvent
	 */
	@FXML
	void Search(ActionEvent actionEvent) {
		try {
			LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * �������
	 * 
	 * @param actionEvent_
	 */
	@FXML
	void Delete(ActionEvent actionEvent_) {
		try {

			if (DbUtil.Odb_Action(104l) == 0) {
				Msg.Message("��� �������!");
				return;
			}

			if (SbTerminal.getSelectionModel().getSelectedItem() != null) {
				Z_SB_TERMINAL_AMRA_DBT sel = SbTerminal.getSelectionModel().getSelectedItem();
				final Alert alert = new Alert(AlertType.CONFIRMATION, "������� \"" + sel.getNAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

				}
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}

	}

	/**
	 * Initialize table
	 */
	void LoadTable() {
		try {
			String selectStmt = "select * from Z_SB_TERMINAL_AMRA_DBT t order by NAME";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<Z_SB_TERMINAL_AMRA_DBT> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Z_SB_TERMINAL_AMRA_DBT list = new Z_SB_TERMINAL_AMRA_DBT();
				list.setSDNAME(rs.getString("SDNAME"));
				list.setINCOME(rs.getString("INCOME"));
				list.setCLEAR_SUM(rs.getString("CLEAR_SUM"));
				list.setGENERAL_COMIS(rs.getString("GENERAL_COMIS"));
				list.setDEAL_ACC(rs.getString("DEAL_ACC"));
				list.setCRASH_ACC(rs.getString("CRASH_ACC"));
				list.setGENERAL_ACC(rs.getString("GENERAL_ACC"));
				list.setACCOUNT(rs.getString("ACCOUNT"));
				list.setADDRESS(rs.getString("ADDRESS"));
				list.setDEPARTMENT(rs.getInt("DEPARTMENT"));
				list.setNAME(rs.getString("NAME"));
				cus_list.add(list);
			}
			// add data
			SbTerminal.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<Z_SB_TERMINAL_AMRA_DBT> tableFilter = TableFilter.forTableView(SbTerminal).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SbTerminal);
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void Edit(ActionEvent actionEvent_) {
		try {

			if (DbUtil.Odb_Action(103l) == 0) {
				Msg.Message("��� �������!");
				return;
			}

			if (SbTerminal.getSelectionModel().getSelectedItem() != null) {

				Z_SB_TERMINAL_AMRA_DBT sel = SbTerminal.getSelectionModel().getSelectedItem();

				Stage stage = new Stage();
				Stage stage_ = (Stage) vbox.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/su/sbra/psv/app/terminals/IUTerminal.fxml"));

				EditTerm controller = new EditTerm();
				controller.SetClass(sel);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������������: " + sel.getNAME());
				stage.initOwner(stage_);
				stage.setResizable(true);
				//stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						LoadTable();
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void Add(ActionEvent actionEvent_) {
		try {
			if (DbUtil.Odb_Action(102l) == 0) {
				Msg.Message("��� �������!");
				return;
			}

			Stage stage = new Stage();
			Stage stage_ = (Stage) vbox.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/su/sbra/psv/app/terminals/IUTerminal.fxml"));

			AddTerm controller = new AddTerm();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("�������� ��������");
			stage.initOwner(stage_);
			stage.setResizable(true);
			//stage.initModality(Modality.WINDOW_MODAL);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					LoadTable();
				}
			});
			stage.show();

		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

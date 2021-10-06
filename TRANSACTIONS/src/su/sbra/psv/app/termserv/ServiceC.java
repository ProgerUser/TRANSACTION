package su.sbra.psv.app.termserv;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Amra_Trans;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.TerminalClass;
import su.sbra.psv.app.model.TerminalForCombo;
import su.sbra.psv.app.model.TransactClass;
import su.sbra.psv.app.model.ViewerDAO;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.swift.SWIFT_FILES;
import su.sbra.psv.app.terminals.AddTerm;
import su.sbra.psv.app.terminals.EditTerm;
import su.sbra.psv.app.terminals.Z_SB_TERMINAL_AMRA_DBT;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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
 * Саид 04.04.2019. <br>
 * 29.09.2021
 */
@SuppressWarnings("unused")
public class ServiceC {
	@FXML
	private TableView<Z_SB_TERMSERV_AMRA_DBT> Service;
	@FXML
	private ComboBox<String> Terminals;
	@FXML
	private Text TermName;
	@FXML
	private Button DelFilter;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, Long> ID;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> ACC_NAME;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> ACC_REC;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> ACCOUNT;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> IDTERM;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> INN;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> KBK;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> KPP;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> NAME;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, String> OKATO;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, Integer> BO1;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, Integer> BO2;
	@FXML
	private TableColumn<Z_SB_TERMSERV_AMRA_DBT, Double> COMISSION;
	private Executor exec;

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			Service.setEditable(true);

			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			ACC_NAME.setCellValueFactory(cellData -> cellData.getValue().ACC_NAMEProperty());
			ACC_REC.setCellValueFactory(cellData -> cellData.getValue().ACC_RECProperty());
			ACCOUNT.setCellValueFactory(cellData -> cellData.getValue().ACCOUNTProperty());
			IDTERM.setCellValueFactory(cellData -> cellData.getValue().IDTERMProperty());
			INN.setCellValueFactory(cellData -> cellData.getValue().INNProperty());
			KBK.setCellValueFactory(cellData -> cellData.getValue().KBKProperty());
			KPP.setCellValueFactory(cellData -> cellData.getValue().KPPProperty());
			NAME.setCellValueFactory(cellData -> cellData.getValue().NAMEProperty());
			OKATO.setCellValueFactory(cellData -> cellData.getValue().OKATOProperty());
			BO1.setCellValueFactory(cellData -> cellData.getValue().BO1Property().asObject());
			BO2.setCellValueFactory(cellData -> cellData.getValue().BO2Property().asObject());
			COMISSION.setCellValueFactory(cellData -> cellData.getValue().COMISSIONProperty().asObject());
			ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
			// ____________________________________
			{
				Statement sqlStatement = DBUtil.conn.createStatement();
				String readRecordSQL = "select NAME from Z_SB_TERMINAL_AMRA_DBT t";
				ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
				ObservableList<String> combolist = FXCollections.observableArrayList();
				while (rs.next()) {
					combolist.add(rs.getString("NAME"));
				}
				Terminals.setItems(combolist);
				// Terminals.getSelectionModel().select(0);
				rs.close();
				sqlStatement.close();
			}
			// ---------------
			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
			DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols();
			decimalFormatSymbols.setCurrencySymbol("");
			((DecimalFormat) currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);
			COMISSION.setCellFactory(tc -> new TableCell<Z_SB_TERMSERV_AMRA_DBT, Double>() {

				@Override
				protected void updateItem(Double price, boolean empty) {
					super.updateItem(price, empty);
					if (empty) {
						setText(null);
					} else {
						setText(currencyFormat.format(price));
					}
				}
			});
			LoadTable();

			Service.setRowFactory(tv -> {
				TableRow<Z_SB_TERMSERV_AMRA_DBT> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Z_SB_TERMSERV_AMRA_DBT rowData = row.getItem();
						Edit(null);
					}
				});
				return row;
			});

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void LoadTable() {
		try {
			// System.out.println(Terminals.getSelectionModel().getSelectedItem());
			String term = "";
			if (Terminals.getSelectionModel().getSelectedItem() != null) {
				term = " and IDTERM = '" + Terminals.getSelectionModel().getSelectedItem() + "'";
			}

			String selectStmt = "select * from Z_SB_TERMSERV_AMRA_DBT t where 1=1 " + term + " order by IDTERM";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<Z_SB_TERMSERV_AMRA_DBT> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Z_SB_TERMSERV_AMRA_DBT list = new Z_SB_TERMSERV_AMRA_DBT();
				list.setCOMISSION(rs.getDouble("COMISSION"));
				list.setBO2(rs.getInt("BO2"));
				list.setBO1(rs.getInt("BO1"));
				list.setACC_NAME(rs.getString("ACC_NAME"));
				list.setOKATO(rs.getString("OKATO"));
				list.setKBK(rs.getString("KBK"));
				list.setACC_REC(rs.getString("ACC_REC"));
				list.setKPP(rs.getString("KPP"));
				list.setINN(rs.getString("INN"));
				list.setACCOUNT(rs.getString("ACCOUNT"));
				list.setIDTERM(rs.getString("IDTERM"));
				list.setNAME(rs.getString("NAME"));
				list.setID(rs.getLong("ID"));
				cus_list.add(list);
			}
			// add data
			Service.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<Z_SB_TERMSERV_AMRA_DBT> tableFilter = TableFilter.forTableView(Service).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(Service);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Авто расширение
	 * 
	 * @param table
	 */
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
	void Refresh(ActionEvent actionEvent) {
		try {
			LoadTable();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void Delete(ActionEvent actionEvent) {
		try {
			if (DbUtil.Odb_Action(124l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (Service.getSelectionModel().getSelectedItem() != null) {
				Z_SB_TERMSERV_AMRA_DBT tr = Service.getSelectionModel().getSelectedItem();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void Edit(ActionEvent actionEvent) {
		try {
			if (DbUtil.Odb_Action(123l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (Service.getSelectionModel().getSelectedItem() != null) {
				Z_SB_TERMSERV_AMRA_DBT sel = Service.getSelectionModel().getSelectedItem();
				
				Stage stage = new Stage();
				Stage stage_ = (Stage) Service.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/app/termserv/IUService.fxml"));

				EditServ controller = new EditServ();
				controller.SetClass(sel);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Редактировать: " + sel.getNAME());
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void DelFilter(ActionEvent actionEvent) {
		try {
			TermName.setText("");
			Terminals.getSelectionModel().select(null);
			LoadTable();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void Terminals(ActionEvent actionEvent) {
		try {
			{
				Statement sqlStatement = DBUtil.conn.createStatement();
				String readRecordSQL = "select 'Отд. \"' || DEPARTMENT || '\", адр. \"' || ADDRESS || '\"' otdadr\r\n"
						+ "  from Z_SB_TERMINAL_AMRA_DBT t where name = '"
						+ Terminals.getSelectionModel().getSelectedItem() + "'";
				// System.out.println(readRecordSQL);
				ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
				ObservableList<String> combolist = FXCollections.observableArrayList();
				if (rs.next()) {
					TermName.setText(rs.getString("otdadr"));
				}
				rs.close();
				sqlStatement.close();
			}
			LoadTable();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void Add(ActionEvent actionEvent) {
		try {
			if (DbUtil.Odb_Action(122l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			Stage stage = new Stage();
			Stage stage_ = (Stage) Service.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/app/termserv/IUService.fxml"));

			AddServ controller = new AddServ();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Добавить сервис");
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}
}

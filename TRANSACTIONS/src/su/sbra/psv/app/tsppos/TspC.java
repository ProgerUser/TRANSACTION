package su.sbra.psv.app.tsppos;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
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
import javafx.scene.control.TableCell;
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
import su.sbra.psv.app.tr.pl.ConvConst;
import su.sbra.psv.app.tr.pl.PlModel;
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
import org.controlsfx.control.StatusBar;
import org.controlsfx.control.table.TableFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
 * POS-терминалы
 * 
 * PSV 07.02.2024 <br>
 * 
 */
@SuppressWarnings("unused")
public class TspC {

	@FXML
	private TableView<SBRA_TSP_POS> termList;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_ID;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_MODEL;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_SERIAL;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_ADDR;
	@FXML
	private TableColumn<SBRA_TSP_POS, Long> TERM_INTEGRATION;
	@FXML
	private TableColumn<SBRA_TSP_POS, LocalDate> TERM_REGDATE;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_SIM_OPER;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_SIM_NUMBER;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_SIM_IP;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_COMMENT;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_PORTHOST;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_GEO;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_IPIFNOTSIM;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> CLIACC;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> CLINAME;
	@FXML
	private TableColumn<SBRA_TSP_POS, String> TERM_TYPE;
	@FXML
	private TableColumn<SBRA_TSP_POS, Long> TERM_KTM;
	@FXML
	private StatusBar RowCount;
	@FXML
	private VBox vbox;

	private Executor exec;

	
	public void setId(Long temp) {
		this.seltemp = temp;
	}
	
	Long seltemp;
	
	/**
	 * Выбор строки
	 */
	void SelRow() {
		try {
			for (SBRA_TSP_POS site : termList.getItems()) {
				if (site.getID().equals(seltemp)) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							termList.requestFocus();
							termList.getSelectionModel().select(site);
							termList.scrollTo(site);
						}
					});
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
	

	@FXML
	private void Select() {
		try {
			setRowCount();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
		
		
	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {

			termList.setEditable(true);
			// For multi threading: Create executor that uses daemon threads:
			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			TERM_ID.setCellValueFactory(cellData -> cellData.getValue().TERM_IDProperty());
			TERM_MODEL.setCellValueFactory(cellData -> cellData.getValue().TERM_MODELProperty());
			TERM_REGDATE.setCellValueFactory(cellData -> cellData.getValue().TERM_REGDATEProperty());
			TERM_SERIAL.setCellValueFactory(cellData -> cellData.getValue().TERM_SERIALProperty());
			TERM_ADDR.setCellValueFactory(cellData -> cellData.getValue().TERM_ADDRProperty());
			TERM_INTEGRATION.setCellValueFactory(cellData -> cellData.getValue().TERM_INTEGRATIONProperty().asObject());
			TERM_SIM_OPER.setCellValueFactory(cellData -> cellData.getValue().TERM_SIM_OPERProperty());
			TERM_SIM_NUMBER.setCellValueFactory(cellData -> cellData.getValue().TERM_SIM_NUMBERProperty());
			TERM_SIM_IP.setCellValueFactory(cellData -> cellData.getValue().TERM_SIM_IPProperty());
			TERM_COMMENT.setCellValueFactory(cellData -> cellData.getValue().TERM_COMMENTProperty());
			TERM_PORTHOST.setCellValueFactory(cellData -> cellData.getValue().TERM_PORTHOSTProperty());
			TERM_GEO.setCellValueFactory(cellData -> cellData.getValue().TERM_GEOProperty());
			TERM_IPIFNOTSIM.setCellValueFactory(cellData -> cellData.getValue().TERM_IPIFNOTSIMProperty());
			CLIACC.setCellValueFactory(cellData -> cellData.getValue().CLIACCProperty());
			CLINAME.setCellValueFactory(cellData -> cellData.getValue().CLINAMEProperty());
			TERM_KTM.setCellValueFactory(cellData -> cellData.getValue().TERM_KTMProperty().asObject());
			TERM_TYPE.setCellValueFactory(cellData -> cellData.getValue().TERM_TYPE_SProperty());
			
			//
			LoadTable();
			//
			termList.setRowFactory(tv -> {
				TableRow<SBRA_TSP_POS> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						SBRA_TSP_POS rowData = row.getItem();
						Edit(null);
					}
				});
				return row;
			});
			termList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					seltemp = termList.getSelectionModel().getSelectedItem().getID();
				}
			});
			TableColumnDate(TERM_REGDATE);
			
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}
	
	/**
	 * Формат <br>
	 * dd.MM.yyyy
	 */
	public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	/**
	 * Форматирование столбцов <br>
	 * dd.MM.yyyy
	 * 
	 * @param TC
	 */
	public void TableColumnDate(TableColumn<SBRA_TSP_POS, LocalDate> TC) {
		try {
			TC.setCellFactory(column -> {
				TableCell<SBRA_TSP_POS, LocalDate> cell = new TableCell<SBRA_TSP_POS, LocalDate>() {
					@Override
					protected void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateFormat.format(item));
								setGraphic(null);
							}
						}
					}
				};
				return cell;
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
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
	 * Поиск
	 * 
	 * @param actionEvent
	 */
	@FXML
	void Search(ActionEvent actionEvent) {
		try {
			LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Удалить
	 * 
	 * @param actionEvent_
	 */
	@FXML
	void Delete(ActionEvent actionEvent_) {
		try {

//			if (DbUtil.Odb_Action(104l) == 0) {
//				Msg.Message("Нет доступа!");
//				return;
//			}

			if (termList.getSelectionModel().getSelectedItem() != null) {
				SBRA_TSP_POS sel = termList.getSelectionModel().getSelectedItem();
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить \"" + sel.getTERM_ID() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					
					PreparedStatement prp = DBUtil.conn.prepareStatement(
							"delete from  SBRA_TSP_POS  WHERE ID = ?");
					prp.setLong(1, sel.getID());
					prp.executeUpdate();
					prp.close();
					
					DBUtil.conn.commit();
					LoadTable();
				}
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}

	}

	/**
	 * Количество строк
	 */
	private void setRowCount() {
		rowCount = 0;
		termList.getItems().forEach(item -> {
			rowCount++;
		});
		RowCount.setText("Кол-во строк: " + rowCount);
	}
	
	/**
	 * Количество строк
	 */
	private int rowCount = 0;
	
	/**
	 * Initialize table
	 */
	void LoadTable() {
		try {
			String selectStmt = "select * from V_SBRA_TSP_POS t order by TERM_ID";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SBRA_TSP_POS> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				SBRA_TSP_POS list = new SBRA_TSP_POS();

				list.setID(rs.getLong("ID"));
				list.setTERM_MODEL(rs.getString("TERM_MODEL"));
				list.setTERM_ADDR(rs.getString("TERM_ADDR"));
				list.setTERM_INTEGRATION(rs.getLong("TERM_INTEGRATION"));
				list.setTERM_SERIAL(rs.getString("TERM_SERIAL"));
				list.setTERM_ID(rs.getString("TERM_ID"));
				list.setTERM_SIM_NUMBER(rs.getString("TERM_SIM_NUMBER"));
				list.setTERM_SIM_OPER(rs.getString("TERM_SIM_OPER"));
				list.setTERM_SIM_IP(rs.getString("TERM_SIM_IP"));
				list.setTERM_REGDATE((rs.getDate("TERM_REGDATE") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("TERM_REGDATE")),
								DateTimeFormatter.ofPattern("dd.MM.yyyy"))
						: null);
				list.setTERM_COMMENT(rs.getString("TERM_COMMENT"));
				list.setTERM_TYPE(rs.getLong("TERM_TYPE"));
				list.setTERM_GEO(rs.getString("TERM_GEO"));
				list.setTERM_PORTHOST(rs.getString("TERM_PORTHOST"));
				list.setTERM_IPIFNOTSIM(rs.getString("TERM_IPIFNOTSIM"));
				list.setCLINAME(rs.getString("CLINAME"));
				list.setCLIACC(rs.getString("CLIACC"));
				list.setTERM_KTM(rs.getLong("TERM_KTM"));
				list.setTERM_TYPE_S(rs.getString("TERM_TYPE_S"));

				cus_list.add(list);
			}
			// add data
			termList.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<SBRA_TSP_POS> tableFilter = TableFilter.forTableView(termList).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(termList);
			
			setRowCount();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Редактировать
	 * @param actionEvent_
	 */
	@FXML
	void Edit(ActionEvent actionEvent_) {
		try {

//			if (DbUtil.Odb_Action(103l) == 0) {
//				Msg.Message("Нет доступа!");
//				return;
//			}

			if (termList.getSelectionModel().getSelectedItem() != null) {

				SBRA_TSP_POS sel = termList.getSelectionModel().getSelectedItem();

				Stage stage = new Stage();
				Stage stage_ = (Stage) vbox.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/su/sbra/psv/app/tsppos/IUTsp.fxml"));

				EditTsp controller = new EditTsp();
				controller.SetClass(sel);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Редактировать: " + sel.getTERM_ID());
				stage.initOwner(stage_);
				stage.setResizable(true);
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						LoadTable();
						SelRow();
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Добавить терминал
	 * @param actionEvent_
	 */
	@FXML
	void Add(ActionEvent actionEvent_) {
		try {
//			if (DbUtil.Odb_Action(102l) == 0) {
//				Msg.Message("Нет доступа!");
//				return;
//			}

			Stage stage = new Stage();
			Stage stage_ = (Stage) vbox.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/su/sbra/psv/app/tsppos/IUTsp.fxml"));

			AddTsp controller = new AddTsp();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Добавить терминал");
			stage.initOwner(stage_);
			stage.setResizable(true);
			// stage.initModality(Modality.WINDOW_MODAL);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					LoadTable();

					if (controller.getId() != -1)
						setId(controller.getId());
					SelRow();
				}
			});
			stage.show();

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

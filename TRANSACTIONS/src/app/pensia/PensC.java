package app.pensia;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;

import app.model.Connect;
import app.model.TerminalDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import sb.utils.DbUtil;
import sbalert.Msg;
import tr.pl.ConvConst;

/**
 * Said 13.07.2020. 18.09.2021 Загрузка пенсии
 */
public class PensC {

	@FXML
	private ToolBar TLB;
	@FXML
	private ToolBar PTLB;

	@FXML
	private ProgressBar Progress;

	@FXML
	private TableView<pensmodel> sep_pens;
	@FXML
	private Button separate;
	@FXML
	private Button save_sep;
	@FXML
	private TableColumn<pensmodel, LocalDateTime> DateLoad;
	@FXML
	private TableColumn<pensmodel, String> Filename;
	@FXML
	private TableColumn<pensmodel, Integer> ID;
	@FXML
	private TableColumn<pensmodel, String> ONE_PART;
	@FXML
	private TableColumn<pensmodel, String> TWO_PART;
	@FXML
	private TableColumn<pensmodel, String> THREE_PART;
	@FXML
	private TableColumn<pensmodel, String> FOUR_PART;
	@FXML
	private CheckBox pensrachk;

	@FXML
	private TableView<PENS_LOAD_ROWSUM> PENS_LOAD_ROWSUM;
	@FXML
	private TableColumn<PENS_LOAD_ROWSUM, Long> LOAD_ID;
	@FXML
	private TableColumn<PENS_LOAD_ROWSUM, String> FILE_NAME;
	@FXML
	private TableColumn<PENS_LOAD_ROWSUM, LocalDateTime> DATE_LOAD;
	@FXML
	private TableColumn<PENS_LOAD_ROWSUM, Boolean> CHK;

	@FXML
	private TableView<SBRA_YEAR_BET> SBRA_YEAR_BET;
	@FXML
	private TableColumn<SBRA_YEAR_BET, Long> PART;
	@FXML
	private TableColumn<SBRA_YEAR_BET, LocalDate> START_Y;
	@FXML
	private TableColumn<SBRA_YEAR_BET, LocalDate> END_Y;

	@FXML
	private TableView<SBRA_PENS_LOG> SBRA_PENS_LOG;
	@FXML
	private TableColumn<SBRA_PENS_LOG, Long> NSTR;
	@FXML
	private TableColumn<SBRA_PENS_LOG, LocalDateTime> TM$TIME_;
	@FXML
	private TableColumn<SBRA_PENS_LOG, String> CSTR;
	@FXML
	private TableColumn<SBRA_PENS_LOG, String> F_STR;
	@FXML
	private TableColumn<SBRA_PENS_LOG, String> ERR;

	@FXML
	private TableView<PENS_STAT> PENS_STAT;
	@FXML
	private TableColumn<PENS_STAT, String> NAMES;
	@FXML
	private TableColumn<PENS_STAT, Long> CNT;
	@FXML
	private TableColumn<PENS_STAT, Double> SUMM;

	@FXML
	private Button SaveError;
	@FXML
	private Button Pens4083_40831;
	@FXML
	private Button DelFilePens;
	@FXML
	private ProgressIndicator PrgInd;

	@FXML
	void pensrachk(ActionEvent event) {
		try {
			if (pensrachk.isSelected()) {
				System.out.println("true");
				CallableStatement callStmt = conn.prepareCall("{ ? = call z_sb_pens_sepfile.ALLOWLOADABKHPENS(?)");
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setInt(2, 1);
				callStmt.execute();
				String ret = callStmt.getString(1);
				if (!ret.equals("OK")) {
					Msg.Message(ret);
				}
				pensrachk.setSelected(true);
			} else {
				System.out.println("false");
				CallableStatement callStmt = conn.prepareCall("{ ? = call z_sb_pens_sepfile.ALLOWLOADABKHPENS(?)");
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setInt(2, 0);
				callStmt.execute();
				String ret = callStmt.getString(1);
				if (!ret.equals("OK")) {
					Msg.Message(ret);
				}
				pensrachk.setSelected(false);
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				Msg.Message(ExceptionUtils.getStackTrace(e1));
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	private Executor exec;

	DecimalFormat decimalFormat = new DecimalFormat("###,###.###");

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			dbConnect();

			try {
				String sql = "select t.BOOLEAN from Z_SB_PENSRACHK t";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					{
						if (rs.getString("BOOLEAN").equals("Y"))
							pensrachk.setSelected(true);
						else
							pensrachk.setSelected(false);
					}
				}
			} catch (Exception e) {
				Msg.Message(ExceptionUtils.getStackTrace(e));
			}

			sep_pens.setEditable(true);

			NAMES.setCellValueFactory(cellData -> cellData.getValue().NAMESProperty());
			CNT.setCellValueFactory(cellData -> cellData.getValue().CNTProperty().asObject());
			SUMM.setCellValueFactory(cellData -> cellData.getValue().SUMMProperty().asObject());

			ID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
			Filename.setCellValueFactory(cellData -> cellData.getValue().filenameProperty());
			DateLoad.setCellValueFactory(cellData -> cellData.getValue().dateloadProperty());
			Filename.setCellFactory(TextFieldTableCell.forTableColumn());
			ID.setCellFactory(TextFieldTableCell.<pensmodel, Integer>forTableColumn(new IntegerStringConverter()));
			DateLoad.setCellFactory(
					TextFieldTableCell.<pensmodel, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));

			ID.setOnEditCommit(new EventHandler<CellEditEvent<pensmodel, Integer>>() {
				@Override
				public void handle(CellEditEvent<pensmodel, Integer> t) {
					((pensmodel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setid(t.getNewValue());
				}
			});
			Filename.setOnEditCommit(new EventHandler<CellEditEvent<pensmodel, String>>() {
				@Override
				public void handle(CellEditEvent<pensmodel, String> t) {
					((pensmodel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setfilename(t.getNewValue());
				}
			});
			DateLoad.setOnEditCommit(new EventHandler<CellEditEvent<pensmodel, LocalDateTime>>() {
				@Override
				public void handle(CellEditEvent<pensmodel, LocalDateTime> t) {
					((pensmodel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setdateload(t.getNewValue());
				}
			});

			ObservableList<pensmodel> empData = TerminalDAO.Z_SB_PENS_4FILE();
			populate(empData);
			autoResizeColumns(sep_pens);
			TableFilter.forTableView(sep_pens).apply();

			/**/
			PENS_LOAD_ROWSUM.setEditable(true);

			LOAD_ID.setCellValueFactory(cellData -> cellData.getValue().LOAD_IDProperty().asObject());
			FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().FILE_NAMEProperty());
			DATE_LOAD.setCellValueFactory(cellData -> cellData.getValue().DATE_LOADProperty());

			END_Y.setCellValueFactory(cellData -> cellData.getValue().END_YProperty());
			START_Y.setCellValueFactory(cellData -> cellData.getValue().START_YProperty());
			PART.setCellValueFactory(cellData -> cellData.getValue().PARTProperty().asObject());

			// TM$TIME_, NSTR, CSTR, ERR, F_STR
			TM$TIME_.setCellValueFactory(cellData -> cellData.getValue().TM$TIME_Property());
			NSTR.setCellValueFactory(cellData -> cellData.getValue().NSTRProperty().asObject());
			CSTR.setCellValueFactory(cellData -> cellData.getValue().CSTRProperty());
			ERR.setCellValueFactory(cellData -> cellData.getValue().ERRProperty());
			F_STR.setCellValueFactory(cellData -> cellData.getValue().F_STRProperty());
			//
			LoadTablePensExec();
			//
			LoadTableSet();
			//
			DATE_LOAD.setCellFactory(column -> {
				TableCell<PENS_LOAD_ROWSUM, LocalDateTime> cell = new TableCell<PENS_LOAD_ROWSUM, LocalDateTime>() {
					@Override
					protected void updateItem(LocalDateTime item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateTimeFormat.format(item));
							}
						}
					}
				};
				return cell;
			});

			START_Y.setCellFactory(column -> {
				TableCell<app.pensia.SBRA_YEAR_BET, LocalDate> cell = new TableCell<SBRA_YEAR_BET, LocalDate>() {
					@Override
					protected void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateFormat.format(item));
							}
						}
					}
				};
				return cell;
			});

			END_Y.setCellFactory(column -> {
				TableCell<SBRA_YEAR_BET, LocalDate> cell = new TableCell<SBRA_YEAR_BET, LocalDate>() {
					@Override
					protected void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateFormat.format(item));
							}
						}
					}
				};
				return cell;
			});

			TM$TIME_.setCellFactory(column -> {
				TableCell<SBRA_PENS_LOG, LocalDateTime> cell = new TableCell<SBRA_PENS_LOG, LocalDateTime>() {
					@Override
					protected void updateItem(LocalDateTime item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText(null);
						} else {
							if (item != null) {
								setText(DateTimeFormat.format(item));
							}
						}
					}
				};
				return cell;
			});

			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
			DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) currencyFormat).getDecimalFormatSymbols();
			decimalFormatSymbols.setCurrencySymbol("");
			((DecimalFormat) currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);

			SUMM.setCellFactory(tc -> new TableCell<PENS_STAT, Double>() {

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

			// --

			PENS_LOAD_ROWSUM.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				PENS_LOAD_ROWSUM sel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();
				if (sel != null) {
					PrgInd.setVisible(true);
					PENS_LOAD_ROWSUM.setDisable(true);
					Task<Object> task = new Task<Object>() {
						@Override
						public Object call() throws Exception {
							try {
								// trn pl
								{
									PreparedStatement prp = conn.prepareCall("SELECT COUNT(*),sum(trn.MTRNRSUM)\r\n"
											+ "  FROM TRN\r\n"
											+ " WHERE trunc(DTRNTRAN) = TRUNC((SELECT F.DATE_LOAD\r\n"
											+ "                            FROM SBRA_PENS_LOAD_ROWSUM F\r\n"
											+ "                           WHERE F.LOAD_ID = ?))\r\n"
											+ "   AND ITRNBATNUM = 996 AND (CTRNPURP LIKE '%{'||?||'}%' or CTRNPURP LIKE '%{'||?||'}%')");
									prp.setLong(1, sel.getLOAD_ID());
									prp.setLong(2, sel.getLOAD_ID());
									prp.setLong(3, sel.getLOAD_ID() + 1);
									ResultSet rs = prp.executeQuery();
									if (rs.next()) {
										if (rs.getLong(1) > 0) {
											Pens4083_40831.setDisable(true);
										} else {
											Pens4083_40831.setDisable(false);
										}
									}
									rs.close();
									prp.close();
								}
								// Stat
								Platform.runLater(() -> InitStat(sel.getLOAD_ID(), "null", "null"));
								// init error
								Platform.runLater(() -> PensError(sel.getLOAD_ID()));

							} catch (Exception e) {
								ShowMes(ExceptionUtils.getStackTrace(e));
							}
							return null;
						}
					};
					task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
					task.setOnSucceeded(e -> {
						PrgInd.setVisible(false);
						PENS_LOAD_ROWSUM.setDisable(false);
					});
					exec.execute(task);
				}
			});

			// __________________
//			CheckBox selecteAllCheckBox = new CheckBox();
//			selecteAllCheckBox.setOnAction(event -> {
//				event.consume();
//				PENS_LOAD_ROWSUM.getItems().forEach(item -> item.setCHK(selecteAllCheckBox.isSelected()));
//				LoadTablePensExec();
//			});
//
//			CHK.setGraphic(selecteAllCheckBox);

			CHK.setCellValueFactory(cellData -> cellData.getValue().CHKProperty());

			// ==== CHK? (CHECH BOX) ===
			CHK.setCellValueFactory(
					new Callback<CellDataFeatures<PENS_LOAD_ROWSUM, Boolean>, ObservableValue<Boolean>>() {

						@Override
						public ObservableValue<Boolean> call(CellDataFeatures<PENS_LOAD_ROWSUM, Boolean> param) {
							PENS_LOAD_ROWSUM person = param.getValue();

							SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(person.getCHK());

							// Note: singleCol.setOnEditCommit(): Not work for
							// CheckBoxTableCell.

							// When "Single?" column change.
							booleanProp.addListener(new ChangeListener<Boolean>() {

								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
										Boolean newValue) {
									person.setCHK(newValue);
								}
							});
							return booleanProp;
						}
					});

			CHK.setCellFactory(new Callback<TableColumn<PENS_LOAD_ROWSUM, Boolean>, //
					TableCell<PENS_LOAD_ROWSUM, Boolean>>() {
				@Override
				public TableCell<PENS_LOAD_ROWSUM, Boolean> call(TableColumn<PENS_LOAD_ROWSUM, Boolean> p) {
					CheckBoxTableCell<PENS_LOAD_ROWSUM, Boolean> cell = new CheckBoxTableCell<PENS_LOAD_ROWSUM, Boolean>();
					cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Формат <br>
	 * dd.MM.yyyy <br>
	 * HH:mm:ss
	 */
	public static final DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	/**
	 * Формат <br>
	 * dd.MM.yyyy <br>
	 */
	public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	/**
	 * Initialize table
	 */
	void LoadTableSet() {
		try {
			// date time formatter
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			// Prepared Statement
			PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM SBRA_YEAR_BET order by part asc");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SBRA_YEAR_BET> cus_list = FXCollections.observableArrayList();
			// looping
			while (rs.next()) {
				SBRA_YEAR_BET list = new SBRA_YEAR_BET();

				list.setEND_Y((rs.getDate("END_Y") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("END_Y")), formatter)
						: null);
				list.setSTART_Y((rs.getDate("START_Y") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("START_Y")), formatter)
						: null);
				list.setPART(rs.getLong("PART"));

				cus_list.add(list);
			}
			// add data
			SBRA_YEAR_BET.setItems(cus_list);
			// close
			prepStmt.close();
			rs.close();
			// add filter
			TableFilter<SBRA_YEAR_BET> tableFilter = TableFilter.forTableView(SBRA_YEAR_BET).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SBRA_YEAR_BET);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void LoadTablePensExec() {
		try {
			// date time formatter
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			// Prepared Statement
			PreparedStatement prepStmt = conn.prepareStatement("SELECT * FROM SBRA_PENS_LOAD_ROWSUM");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<PENS_LOAD_ROWSUM> cus_list = FXCollections.observableArrayList();
			// looping
			while (rs.next()) {
				PENS_LOAD_ROWSUM list = new PENS_LOAD_ROWSUM();
				list.setLOAD_ID(rs.getLong("LOAD_ID"));
				list.setDATE_LOAD((rs.getDate("DATE_LOAD") != null) ? LocalDateTime
						.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("DATE_LOAD")), formatterwt)
						: null);
				list.setROW_COUNT(rs.getLong("ROW_COUNT"));
				list.setFILE_NAME(rs.getString("FILE_NAME"));
				cus_list.add(list);
			}
			// add data
			PENS_LOAD_ROWSUM.setItems(cus_list);
			// close
			prepStmt.close();
			rs.close();
			// add filter
			TableFilter<PENS_LOAD_ROWSUM> tableFilter = TableFilter.forTableView(PENS_LOAD_ROWSUM).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(PENS_LOAD_ROWSUM);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	String PensCountS = "";
	String PensSumS = "";

	String PensTrnCountS = "";
	String PensTrnSumS = "";

	Long all_cnt = 0l;
	Long rec_cnt = 0l;

	int SelTbl;

	/**
	 * Запустить процесс и обновлять прогресс бар пока не закончится процесс
	 */
	void RunPB(String taskname, Long id) {
		try {

			{
				PreparedStatement prp = conn
						.prepareCall("SELECT COUNT(*), SUM(SUMM) FROM SBRA_PENS_ROW_CL T WHERE ID_ = ?");
				prp.setLong(1, id);
				ResultSet rs = prp.executeQuery();
				if (rs.next()) {
					all_cnt = rs.getLong(1);
				}
				rs.close();
				prp.close();
			}

			{
				PreparedStatement prp = null;

				if (taskname.equals("T_PENS")) {
					prp = conn.prepareCall("select count(*) cnt, sum(trn.MTRNRSUM) summ\r\n" + "  from trn\r\n"
							+ " where DTRNCREATE = trunc((select f.DATE_LOAD\r\n"
							+ "                            from SBRA_PENS_LOAD_ROWSUM f\r\n"
							+ "                           where f.LOAD_ID = ?))\r\n" + "   and ITRNBATNUM = 999\r\n"
							+ "   and CTRNPURP like '%{' || ? || '}%'\r\n" + "");
					prp.setLong(1, id);
					prp.setLong(2, id);
				} else if (taskname.equals("T_PENS_PLAST")) {
					prp = conn.prepareCall("SELECT count(*) cnt, sum(trn.MTRNRSUM) summ  FROM TRN\r\n"
							+ " WHERE DTRNCREATE = TRUNC((SELECT F.DATE_LOAD\r\n"
							+ "                            FROM SBRA_PENS_LOAD_ROWSUM F\r\n"
							+ "                           WHERE F.LOAD_ID = ?))\r\n" + "   AND ITRNBATNUM = 996\r\n"
							+ "   AND (CTRNPURP LIKE '%{'||?||'}%' or CTRNPURP LIKE '%{'||?||'}%')");
					prp.setLong(1, id);
					prp.setLong(2, id);
					prp.setLong(3, id + 1);
				}

				ResultSet rs = prp.executeQuery();
				if (rs.next()) {

					rec_cnt = rs.getLong(1);
				}
				rs.close();
				prp.close();
			}

			{
				PreparedStatement prp = conn.prepareStatement("SELECT (SELECT COUNT(*)\r\n"
						+ "          FROM sys.dba_parallel_execute_tasks T\r\n"
						+ "         WHERE T.TASK_NAME = ?) t_pens_ex,\r\n" + "       (SELECT COUNT(*)\r\n"
						+ "          FROM sys.dba_parallel_execute_tasks T\r\n" + "         WHERE T.TASK_NAME = ?\r\n"
						+ "           AND STATUS = 'FINISHED') t_pens_ex_st\r\n" + "  FROM DUAL");
				prp.setString(1, taskname);
				prp.setString(2, taskname);
				ResultSet rs = prp.executeQuery();
				if (rs.next()) {

					System.out.println("____________________");
					System.out.println("t_pens_ex=" + rs.getInt("t_pens_ex"));
					System.out.println("t_pens_ex_st=" + rs.getInt("t_pens_ex_st"));
					System.out.println("____________________");
					System.out.println("taskname=" + taskname);

					Date Curdate = new Date();

					long diff = (Curdate.getTime() - DateBeforeStartPens.getTime()) / 1000;

					if (rs.getInt("t_pens_ex") == 0 | rs.getInt("t_pens_ex_st") == 1) {

						Platform.runLater(() -> {
							PensError(id);

							PENS_LOAD_ROWSUM.requestFocus();
							PENS_LOAD_ROWSUM.getSelectionModel().select(0);
							PENS_LOAD_ROWSUM.scrollTo(0);
							// Init
							InitStat(id, "null", "null");
						});

						if (diff >= 30) {
							EndTask();
							Enabled();
							System.out.println("<EndTask>");
							ProgressPens.setProgress(0);
						}
					} else if (rs.getInt("t_pens_ex") > 0 & rs.getInt("t_pens_ex_st") == 0) {
						Platform.runLater(() -> {
							double prc = ((double) rec_cnt / all_cnt);
							ProgressPens.setProgress(prc);
							// Init
							InitStat(id, "null", "null");
						});

						double prc = ((double) rec_cnt / all_cnt);
						System.out.println("__________%__________");
						System.out.println("rec_cnt=" + rec_cnt);
						System.out.println("all_cnt=" + all_cnt);
						System.out.println("rec_cnt/all_cnt=" + prc);
						System.out.println("__________%__________");

						Platform.runLater(() -> {
							PensError(id);
							PENS_LOAD_ROWSUM.requestFocus();
							PENS_LOAD_ROWSUM.getSelectionModel().select(0);
							PENS_LOAD_ROWSUM.scrollTo(0);
						});
					}
				}
				prp.close();
				rs.close();
			}
		} catch (Exception e) {
			ShowMes(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	public Button RefreshBTN;
	@FXML
	public Button OpenFilePens;
	@FXML
	public Button LoadComisss;
	@FXML
	public Button OpenAbss;

	void Enabled() {
		try {
			Platform.runLater(() -> {
				PENS_LOAD_ROWSUM.setDisable(false);
				RefreshBTN.setDisable(false);
				OpenFilePens.setDisable(false);
				// Pens4083_40831.setDisable(false);
				LoadComisss.setDisable(false);
				OpenAbss.setDisable(false);
				DelFilePens.setDisable(false);
				PTLB.setDisable(false);
			});
		} catch (Exception e) {
			ShowMes(ExceptionUtils.getStackTrace(e));
		}
	}

	void Disable() {
		try {
			Platform.runLater(() -> {
				PENS_LOAD_ROWSUM.setDisable(true);
				RefreshBTN.setDisable(true);
				OpenFilePens.setDisable(true);
				// Pens4083_40831.setDisable(true);
				LoadComisss.setDisable(true);
				OpenAbss.setDisable(true);
				DelFilePens.setDisable(true);
				PTLB.setDisable(true);
			});
		} catch (Exception e) {
			ShowMes(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void PensError(Long ids) {
		try {
			// date time formatter
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			// Prepared Statement
			PreparedStatement prepStmt = conn
					.prepareStatement(DbUtil.Sql_From_Prop("/app/pensia/SQL.properties", "RefreshError"));
			prepStmt.setLong(1, ids);
			prepStmt.setLong(2, ids);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SBRA_PENS_LOG> cus_list = FXCollections.observableArrayList();
			// looping
			SBRA_PENS_LOG list = null;
			int i = 0;
			while (rs.next()) {
				i++;
				list = new SBRA_PENS_LOG();

				list.setF_STR(rs.getString("F_STR"));
				list.setTIME_(rs.getString("TIME_"));
				list.setNSTR(rs.getLong("NSTR"));
				list.setCSTR(rs.getString("CSTR"));
				list.setERR(rs.getString("ERR"));
				list.setID_(rs.getLong("ID_"));
				list.setDP_DOC_ID(rs.getLong("DP_DOC_ID"));
				list.setROW_NO(rs.getLong("ROW_NO"));
				list.setTM$TIME_((rs.getDate("TM$TIME_") != null)
						? LocalDateTime.parse(
								new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("TM$TIME_")), formatterwt)
						: null);

				cus_list.add(list);
			}
			if (i == 0) {
				SaveError.setDisable(true);
			} else {
				SaveError.setDisable(false);
			}
			// add data
			SBRA_PENS_LOG.setItems(cus_list);
			list = null;
			cus_list = null;
			// close
			prepStmt.close();
			rs.close();
			// add filter
			TableFilter<SBRA_PENS_LOG> tableFilter = TableFilter.forTableView(SBRA_PENS_LOG).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SBRA_PENS_LOG);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void InitStat(Long ids, String idss, String idss2) {
		try {
			PreparedStatement prepStmt = null;
			ResultSet rs = null;

			if (idss.equals("null")) {
				prepStmt = conn.prepareStatement(DbUtil.Sql_From_Prop("/app/pensia/SQL.properties", "PensStat"));
				prepStmt.setLong(1, ids);
				rs = prepStmt.executeQuery();
			} else {
				System.out.println(DbUtil.Sql_From_Prop("/app/pensia/SQL.properties", "PensStatList")
						.replace("$list$", idss).replace("$list2$", idss2));

				prepStmt = conn.prepareStatement(DbUtil.Sql_From_Prop("/app/pensia/SQL.properties", "PensStatList")
						.replace("$list$", idss).replace("$list2$", idss2));
				rs = prepStmt.executeQuery();
			}

			ObservableList<PENS_STAT> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				PENS_STAT list = new PENS_STAT();
				list.setNAMES(rs.getString("NAMES"));
				list.setCNT(rs.getLong("CNT"));
				list.setSUMM(rs.getDouble("SUMM"));
				cus_list.add(list);
			}
			// add data
			PENS_STAT.setItems(cus_list);
			// close
			prepStmt.close();
			rs.close();
			// resize
			autoResizeColumns(PENS_STAT);
			// add filter
			TableFilter<PENS_STAT> tableFilter = TableFilter.forTableView(PENS_STAT).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Заполнить
	 * 
	 * @param pensmodel
	 */
	private void populate(ObservableList<pensmodel> pensmodel) {
		sep_pens.setItems(pensmodel);
	}

	/**
	 * Сохранить
	 * 
	 * @param event
	 */
	@FXML
	void save_seps(ActionEvent event) {
		try {
			if (sep_pens.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите сначала данные из таблицы!");
			} else {
				pensmodel pens = sep_pens.getSelectionModel().getSelectedItem();
				System.out.println(pens.getid());
				FileChooser fileChooser = new FileChooser();

				System.setProperty("javax.xml.transform.TransformerFactory",
						"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

				InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties");
				Properties prop = new Properties();
				// load a properties file
				prop.load(input);
				fileChooser.setInitialDirectory(new File(prop.getProperty("PensiaLoadFolderDef")));
				input.close();

				// Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xlsx");

				fileChooser.setInitialFileName("File");

				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(null);

				if (file != null) {

					final Alert alert = new Alert(AlertType.CONFIRMATION, "Сформировать один файл?", ButtonType.YES,
							ButtonType.NO);
					if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait()
							.orElse(ButtonType.NO) == ButtonType.YES) {
						retxlsx(0, pens.getid(), conn, file,
								sep_pens.getSelectionModel().getSelectedItem().getfilename(), "ALL");
					} else {
						PreparedStatement prp_part = conn
								.prepareStatement("SELECT * FROM SBRA_YEAR_BET ORDER BY PART ASC");
						ResultSet rs = prp_part.executeQuery();
						while (rs.next()) {
							retxlsx(rs.getInt("PART"), pens.getid(), conn, file,
									sep_pens.getSelectionModel().getSelectedItem().getfilename(), "NOT_ALL");
						}
						rs.close();
						prp_part.close();
					}

				}

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
			if (column.getText().equals("Количество строк") | column.getText().equals("")) {

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
	 * Получить кодировку файла
	 * 
	 * @param file
	 * @return
	 */
	public String getFileCharset(String file) {
		try {
			byte[] buf = new byte[500000000];
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			final UniversalDetector universalDetector = new UniversalDetector(null);
			int numberOfBytesRead;
			while ((numberOfBytesRead = bufferedInputStream.read(buf)) > 0 && !universalDetector.isDone()) {
				universalDetector.handleData(buf, 0, numberOfBytesRead);
			}
			universalDetector.dataEnd();
			String encoding = universalDetector.getDetectedCharset();
			universalDetector.reset();
			bufferedInputStream.close();
			return encoding;
		} catch (IOException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Чтение файла
	 * 
	 * @param fileName
	 * @return
	 */
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName), "CP1251"/* getFileCharset(fileName) */));
			System.out.println("File_encode=" + getFileCharset(fileName));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(System.lineSeparator());
			}
			String clobData = sb.toString();
			br.close();
			return clobData;
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return "Error";
	}

	/**
	 * Старт
	 * 
	 * @param event
	 */
	@FXML
	private void separate(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text file", "*.txt"),
					new ExtensionFilter("Comma separated", "*.csv"));

			InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties");
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			fileChooser.setInitialDirectory(new File(prop.getProperty("PensiaLoadFolderDef")));
			input.close();

			File file = fileChooser.showOpenDialog(null);
			if (file != null) {

				CallableStatement callStmt = null;
				String reviewContent = null;
				callStmt = conn.prepareCall("{ ? = call z_sb_pens_sepfile.z_sb_pens_sepfile(?,?)}");

				String reviewStr = readFile(file.getParent() + "\\" + file.getName());
				Clob clob = conn.createClob();
				clob.setString(1, reviewStr);
				callStmt.registerOutParameter(1, Types.VARCHAR);

//				File blob = new File(file.getParent() + "\\" + file.getName());
//				FileInputStream in = new FileInputStream(blob);
//				callStmt.setBinaryStream(2, in, (int)blob.length());
				callStmt.setClob(2, clob);

				callStmt.setString(3, file.getName());
				callStmt.execute();
				reviewContent = callStmt.getString(1);

				String[] parts = reviewContent.split(";");
				String part1 = parts[0].trim();
				String part2 = parts[1].trim();
				System.out.println(part1);
				System.out.println(part2);

				if (part2.equals("Error")) {
					System.out.println("Error--");
					write_error(conn, file, Integer.valueOf(part1));
				} else if (part2.equals("ok")) {
					System.out.println("OK--");

					PreparedStatement prp_part = conn.prepareStatement("SELECT * FROM SBRA_YEAR_BET ORDER BY PART ASC");
					ResultSet rs = prp_part.executeQuery();
					while (rs.next()) {
						String str = "";
						Clob clobb = conn.createClob();
						str = retclob(rs.getInt("PART"), Integer.valueOf(part1), conn, file);
						clobb.setString(1, str);
						String upd = "insert into Z_SB_PENS_4FILE_FILES (PART_FILE,FILE_CL,LOAD_ID) values  (?,?,?) ";
						System.out.println(upd);
						PreparedStatement prepStmt = conn.prepareStatement(upd);
						prepStmt.setInt(1, rs.getInt("PART"));
						prepStmt.setClob(2, clobb);
						prepStmt.setInt(3, Integer.valueOf(part1));
						prepStmt.executeUpdate();
					}

					conn.commit();
					/* Вывод сообщения */
					Msg.Message("Файлы сформированы в папку=" + file.getParent());
					ObservableList<pensmodel> empData = TerminalDAO.Z_SB_PENS_4FILE();
					populate(empData);
					autoResizeColumns(sep_pens);
					TableFilter.forTableView(sep_pens).apply();
				}
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				Msg.Message(ExceptionUtils.getStackTrace(e1));
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Вывод ошибок
	 * 
	 * @param conn
	 * @param file
	 * @param sess_id
	 */
	public void write_error(Connection conn, File file, int sess_id) {
		try {
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select * from z_sb_pens_4file_log t where t.SESS_ID = " + sess_id;

			String createfolder = file.getParent() + "\\" + file.getName() + "_Error.txt";
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			PrintWriter writer = new PrintWriter(createfolder);
			while (rs.next()) {
				writer.write(rs.getString("ERROR"));
			}
			writer.close();
			rs.close();
			sqlStatement.close();

			ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
					file.getParent() + "\\" + file.getName() + "_Error.txt");
			pb.start();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Удалить загрузку для разбивки
	 */
	public void DeleteLoadSep() {
		try {
			if (DbUtil.Odb_Action(47l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			pensmodel sel = sep_pens.getSelectionModel().getSelectedItem();
			if (sel == null) {
				Msg.Message("Выберите строку");
			} else {
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить файл \"" + sel.getfilename() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = conn.prepareStatement("delete from Z_SB_PENS_4FILE where ID = ?");
					prp.setInt(1, sel.getid());
					prp.executeUpdate();
					conn.commit();
					prp.close();
					// populate
					ObservableList<pensmodel> empData = TerminalDAO.Z_SB_PENS_4FILE();
					populate(empData);
					autoResizeColumns(sep_pens);
					TableFilter.forTableView(sep_pens).apply();
				}
			}

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				Msg.Message(ExceptionUtils.getStackTrace(e1));
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Открыть в АБС
	 */
	public void RefreshPens() {
		try {
			LoadTablePensExec();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Прогресс бар
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void UpdateProress() {
		try {
			Service service = new Service() {
				@Override
				protected Task createTask() {
					return new Task() {
						@Override
						protected Object call() throws Exception {

							try {
								updateProgress(0, 0);
							} catch (Exception e) {
								ShowMes(ExceptionUtils.getStackTrace(e));
							}

							return null;
						}
					};
				}
			};
			ProgressPens.progressProperty().bind(service.progressProperty());
			service.setOnFailed(e -> ShowMes(service.getException().getMessage()));
			// service.setOnSucceeded(e -> TLB.setDisable(false));
			service.start();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private ProgressBar ProgressPens;

	/**
	 * Обновить часть
	 */
	public void RefreshDatePart() {
		LoadTableSet();
	}

	/**
	 * Редактировать часть
	 */
	public void EditDatePart() {
		try {
			if (DbUtil.Odb_Action(46l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			if (SBRA_YEAR_BET.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите строку!");
			} else {
				SBRA_YEAR_BET sel = SBRA_YEAR_BET.getSelectionModel().getSelectedItem();
				// Create the custom dialog.
				Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
				dialog.setTitle("Редактировать часть \"" + sel.getPART() + "\"");

				Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image(this.getClass().getResource("/icon.png").toString()));

				// Set the button types.
				ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

				GridPane gridPane = new GridPane();
				gridPane.setHgap(10);
				gridPane.setVgap(10);
				gridPane.setPadding(new Insets(20, 150, 10, 10));

				DatePicker from = new DatePicker();
				from.setPrefWidth(120);
				from.setValue(sel.getSTART_Y());
				// from.setPromptText("From");
				DatePicker to = new DatePicker();
				to.setPrefWidth(120);
				to.setValue(sel.getEND_Y());
				// to.setPromptText("To");

				new ConvConst().FormatDatePiker(to);
				new ConvConst().FormatDatePiker(from);

				gridPane.add(new Label("С:"), 0, 0);
				gridPane.add(from, 1, 0);
				gridPane.add(new Label("По:"), 2, 0);
				gridPane.add(to, 3, 0);

				dialog.getDialogPane().setContent(gridPane);

				// Request focus on the username field by default.
				Platform.runLater(() -> from.requestFocus());

				// Convert the result to a username-password-pair when the login button is
				// clicked.
				dialog.setResultConverter(dialogButton -> {
					if (dialogButton == loginButtonType) {
						return new Pair<>(from.getValue(), to.getValue());
					}
					return null;
				});

				Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();

				result.ifPresent(pair -> {
					final Alert alert = new Alert(AlertType.CONFIRMATION,
							"Редактировать часть \"" + sel.getPART() + "\" ?", ButtonType.YES, ButtonType.NO);
					if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait()
							.orElse(ButtonType.NO) == ButtonType.YES) {
						try {
							PreparedStatement prp = conn
									.prepareStatement("update SBRA_YEAR_BET set START_Y = ?,END_Y =? where PART = ?");
							prp.setDate(1, (from.getValue() != null) ? java.sql.Date.valueOf(from.getValue()) : null);
							prp.setDate(2, (to.getValue() != null) ? java.sql.Date.valueOf(to.getValue()) : null);
							prp.setLong(3, sel.getPART());
							prp.executeUpdate();
							conn.commit();
							prp.close();
							// populate
							LoadTableSet();
						} catch (Exception e) {
							Msg.Message(ExceptionUtils.getStackTrace(e));
						}
					}
					System.out.println("From=" + pair.getKey() + ", To=" + pair.getValue());
				});
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Добавить часть
	 */
	public void AddDatePart() {
		try {
			if (DbUtil.Odb_Action(45l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}
			// Create the custom dialog.
			Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
			dialog.setTitle("Добавить часть");

			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image(this.getClass().getResource("/icon.png").toString()));

			// Set the button types.
			ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

			GridPane gridPane = new GridPane();
			gridPane.setHgap(10);
			gridPane.setVgap(10);
			gridPane.setPadding(new Insets(20, 150, 10, 10));

			DatePicker from = new DatePicker();
			from.setPrefWidth(120);
			// from.setPromptText("From");
			DatePicker to = new DatePicker();
			to.setPrefWidth(120);
			// to.setPromptText("To");

			TextField part = new TextField();
			part.setPrefWidth(50);

			new ConvConst().FormatDatePiker(to);
			new ConvConst().FormatDatePiker(from);
			from.setPrefWidth(120);
			gridPane.add(new Label("С:"), 0, 0);
			gridPane.add(from, 1, 0);
			gridPane.add(new Label("По:"), 2, 0);
			gridPane.add(to, 3, 0);
			gridPane.add(new Label("Часть:"), 4, 0);
			gridPane.add(part, 5, 0);

			dialog.getDialogPane().setContent(gridPane);

			// Request focus on the username field by default.
			Platform.runLater(() -> from.requestFocus());

			// Convert the result to a username-password-pair when the login button is
			// clicked.
			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == loginButtonType) {
					return new Pair<>(from.getValue(), to.getValue());
				}
				return null;
			});

			Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();

			result.ifPresent(pair -> {
				Msg.Message("From=" + pair.getKey() + ", To=" + pair.getValue());
			});

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	String load_id = "(";
	String load_id2 = "(";

	/**
	 * Статистика по выделенным
	 */
	@FXML
	public void SelStat(ActionEvent Event) {
		try {
			load_id = "(";
			load_id2 = "(";
			// Цикл по ячейкам
			for (int i = 0; i < PENS_LOAD_ROWSUM.getItems().size(); i++) {
				// Цикл по столбцам
				for (int j = 0; j < PENS_LOAD_ROWSUM.getColumns().size(); j++) {
					// Если Не пусто
					if (PENS_LOAD_ROWSUM.getColumns().get(j).getCellData(i) != null) {
						// Если выделена строка
						if (j == 0) {
							if ((Boolean) PENS_LOAD_ROWSUM.getColumns().get(j).getCellData(i) == true) {
								if (PENS_LOAD_ROWSUM.getColumns().get(1).getCellData(i) != null) {
									load_id = load_id + ((Long) PENS_LOAD_ROWSUM.getColumns().get(1).getCellData(i))
											+ ",";
									load_id2 = load_id2
											+ (((Long) PENS_LOAD_ROWSUM.getColumns().get(1).getCellData(i)) + 1) + ",";
								}
							}
						}
					}
				}
			}
			load_id = (load_id.substring(0, load_id.length() - 1)) + ")";
			load_id2 = (load_id2.substring(0, load_id2.length() - 1)) + ")";
			System.out.println(load_id);
			System.out.println(load_id2);
			// -------------------------------
			if (!load_id.equals(")")) {
				PrgInd.setVisible(true);
				PENS_LOAD_ROWSUM.setDisable(true);
				Task<Object> task = new Task<Object>() {
					@Override
					public Object call() throws Exception {
						try {
							InitStat(null, load_id, load_id2);
						} catch (Exception e) {
							ShowMes(ExceptionUtils.getStackTrace(e));
						}
						return null;
					}
				};
				task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
				task.setOnSucceeded(e -> {
					PrgInd.setVisible(false);
					PENS_LOAD_ROWSUM.setDisable(false);
				});
				exec.execute(task);
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Загрузить файл
	 */
	public void ExecPlast() {
		try {

			if (DbUtil.Odb_Action(64l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			if (!Connect.userID_.equals("PENSIA")) {
				Msg.Message("Пользователь не PENSIA!");
				return;
			}

			if (PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите строку!");
			} else {
				PENS_LOAD_ROWSUM sel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();

				final Alert alert = new Alert(AlertType.CONFIRMATION, "Зачислить на счета ?", ButtonType.YES,
						ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = conn.prepareCall("SELECT COUNT(*) FROM Z_PENS_VACC WHERE OST > 0");
					ResultSet rs = prp.executeQuery();
					if (rs.next()) {
						if (rs.getLong(1) > 0) {
							CallableStatement callStmt_j = conn
									.prepareCall("{ ? = call Z_PENS_KERNEL.plastExec(?,?,?)}");
							callStmt_j.registerOutParameter(1, Types.BIGINT);
							callStmt_j.setLong(2, sel.getLOAD_ID());
							callStmt_j.setInt(3, 1000);
							callStmt_j.setInt(4, 7);
							// catch
							try {
								callStmt_j.execute();
							} catch (Exception e) {
								conn.rollback();
								callStmt_j.close();
								Msg.Message(ExceptionUtils.getStackTrace(e));
							}
							callStmt_j.close();
							conn.commit();
							// Go stat______________

							Disable();

							DateBeforeStartPens = new Date();
							RunProcess("T_PENS_PLAST", sel.getLOAD_ID());
						} else {
							Msg.Message("Уже есть переброс!");
						}
					}
					rs.close();
					prp.close();
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Загрузить файл
	 */
	public void SaveComiss() {
		try {

			if (PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите сначала данные из таблицы!");
			} else {
				InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties");
				Properties prop = new Properties();
				// load a properties file
				prop.load(input);

				PENS_LOAD_ROWSUM pens = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();

				FileChooser fileChooser = new FileChooser();

				System.setProperty("javax.xml.transform.TransformerFactory",
						"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
				fileChooser.setInitialDirectory(new File(prop.getProperty("PensiaLoadFolderDef")));
				input.close();

				// Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xlsx");

				fileChooser.setInitialFileName("File");

				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(null);

				if (file != null) {

					String createfolder = file.getParent() + "\\Комиссия_" + pens.getFILE_NAME() + ".xlsx";

					PreparedStatement prp_part = conn.prepareStatement("with data_v as\n"
							+ " (select regexp_substr(CTRNPURP, 'otd={\\d+}') otd, a.*\n" + "    from trn a\n"
							+ "   where (trunc(a.DTRNTRAN),\n"
							+ "          replace(substr(regexp_substr(CTRNPURP, 'id={\\d+}'), 5), '}', '')) in\n"
							+ "         (select trunc(time_), id_\n" + "            from z_pens_cl\n"
							+ "           where type_ = 'FIN'\n" + "             and ID_ = ?)\n"
							+ "     and ITRNBATNUM = 997)\n" + "select otd,\n" + "       sum(MTRNRSUM) comm,\n"
							+ "       count(MTRNRSUM) * 5 comm_mt,\n" + "       count(MTRNRSUM) * 1 comm_ba,\n"
							+ "       sum(MTRNRSUM) - count(MTRNRSUM) * 5 - count(MTRNRSUM) * 1 comm_sb\n"
							+ "  from data_v\n" + " group by otd\n" + " order by otd\n" + "");
					prp_part.setLong(1, pens.getLOAD_ID());
					ResultSet rs = prp_part.executeQuery();

					SXSSFWorkbook wb = new SXSSFWorkbook(100);
					Sheet sh = wb.createSheet("Таблица");
					Row row = sh.createRow(0);
					// header
					row.createCell(0).setCellValue("OTD");
					row.createCell(1).setCellValue("COMM");
					row.createCell(2).setCellValue("COMM_MT");
					row.createCell(3).setCellValue("COMM_BA");
					row.createCell(4).setCellValue("COMM_SB");

					int i = 0;
					while (rs.next()) {
						row = sh.createRow(i + 1);
						row.createCell(0).setCellValue(rs.getString("OTD"));
						row.createCell(1).setCellValue(rs.getDouble("COMM"));
						row.createCell(2).setCellValue(rs.getDouble("COMM_MT"));
						row.createCell(3).setCellValue(rs.getDouble("COMM_BA"));
						row.createCell(4).setCellValue(rs.getDouble("COMM_SB"));
						i++;
					}

					wb.write(new FileOutputStream(createfolder));
					wb.close();

					rs.close();
					prp_part.close();
				}

			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Закрытие задачи при закрытии формы
	 */
	public void EndTask() {
		this.st.cancel();
	}

	/**
	 * Задача
	 */
	private ScheduledTask st;

	/**
	 * Запуск процесса
	 */
	void RunProcess(String taskname, Long id) {
		Timer time = new Timer(); // Instantiate Timer Object
		st = new ScheduledTask(); // Instantiate SheduledTask class
		st.setPens(this, taskname, id);
		time.schedule(st, 0, 3000); // Create task repeating every 1 sec
	}

	Date DateBeforeStartPens = null;

	/**
	 * Загрузить файл
	 */
	public void LoadPens() {
		try {

			if (DbUtil.Odb_Action(63l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			if (!Connect.userID_.equals("PENSIA")) {
				Msg.Message("Пользователь не PENSIA!");
				return;
			}

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text file", "*.txt"));

			InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties");
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			fileChooser.setInitialDirectory(new File(prop.getProperty("PensiaLoadFolderDef")));
			input.close();

			File file = fileChooser.showOpenDialog(null);
			if (file != null) {

				final Alert alert = new Alert(AlertType.CONFIRMATION, "Загрузить файл \"" + file.getName() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

					CallableStatement callStmt = conn.prepareCall("{ call Z_PENS_KERNEL.LOADFROMJAVA(?,?,?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.registerOutParameter(4, Types.BIGINT);
					// txt to clob
					String reviewStr = readFile(file.getAbsolutePath());
					Clob clob = conn.createClob();
					clob.setString(1, reviewStr);
					// add parameters
					callStmt.setString(2, file.getName());
					callStmt.setClob(3, clob);
					// catch
					try {
						callStmt.execute();
					} catch (Exception e) {
						conn.rollback();
						callStmt.close();
						Msg.Message(ExceptionUtils.getStackTrace(e));
					}
					// return
					String ret = callStmt.getString(1);
					Long ret_id = callStmt.getLong(4);
					// check
					if (ret != null) {
						// roll back
						conn.rollback();
						Msg.Message(ret);
					} else {
						conn.commit();
						// ___________
						{
							System.out.println("ret_id=" + ret_id);
							CallableStatement callStmt_j = conn.prepareCall("{ call Z_PENS_KERNEL.jobLoad(?,?,?,?)}");
							callStmt_j.setLong(1, ret_id);
							callStmt_j.setInt(2, 1000);
							callStmt_j.setInt(3, 7);
							callStmt_j.setString(4, "JAVA");
							// catch
							try {
								callStmt_j.execute();
							} catch (Exception e) {
								conn.rollback();
								callStmt_j.close();
								Msg.Message(ExceptionUtils.getStackTrace(e));
							}
							callStmt_j.close();
							conn.commit();
							// Go stat______________

							Disable();

							DateBeforeStartPens = new Date();
							RunProcess("T_PENS", ret_id);
							// _____________________
						}
						// ___________
						// Msg.Message("Файл " + file.getName() + " загружен");
						// reload
						LoadTablePensExec();
					}
					// close
					callStmt.close();
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Удалить файл
	 */
	public void DelFilePens() {
		try {
			if (PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите сначала данные из таблицы!");
			} else {

				if (DbUtil.Odb_Action(47l) == 0) {
					Msg.Message("Нет доступа!");
					return;
				}

				PENS_LOAD_ROWSUM sel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();

				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить файл \"" + sel.getFILE_NAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					{
						PreparedStatement prp = conn.prepareCall("select count(*) cnt, sum(trn.MTRNRSUM) summ\r\n"
								+ "  from trn\r\n" + " where trunc(DTRNTRAN) = trunc((select f.DATE_LOAD\r\n"
								+ "                            from SBRA_PENS_LOAD_ROWSUM f\r\n"
								+ "                           where f.LOAD_ID = ?))\r\n" + "   and ITRNBATNUM = 999\r\n"
								+ "   and CTRNPURP like '%{' || ? || '}%'\r\n" + "");
						prp.setLong(1, sel.getLOAD_ID());
						prp.setLong(2, sel.getLOAD_ID());
						ResultSet rs = prp.executeQuery();
						if (rs.next()) {
							if (rs.getLong(1) > 0) {
								Msg.Message("Есть связи в TRN 60322% -> 4083%, удаление запрещено!");
								return;
							}
						}
						rs.close();
						prp.close();
					}

					{
						PreparedStatement prp = conn.prepareCall("SELECT COUNT(*)\r\n" + "  FROM TRN\r\n"
								+ " WHERE trunc(DTRNTRAN) = TRUNC((SELECT F.DATE_LOAD\r\n"
								+ "                            FROM SBRA_PENS_LOAD_ROWSUM F\r\n"
								+ "                           WHERE F.LOAD_ID = ?))\r\n"
								+ "   AND ITRNBATNUM = 996 AND (CTRNPURP LIKE '%{'||?||'}%' or CTRNPURP LIKE '%{'||?||'}%')");
						prp.setLong(1, sel.getLOAD_ID());
						prp.setLong(2, sel.getLOAD_ID());
						prp.setLong(3, sel.getLOAD_ID() + 1);
						ResultSet rs = prp.executeQuery();
						if (rs.next()) {
							if (rs.getLong(1) > 0) {
								Msg.Message("Есть связи в TRN 4083% -> 40831%, удаление запрещено!");
								return;
							}
						}
						rs.close();
						prp.close();
					}
					// если нет ошибок
					{
						PreparedStatement prp = conn.prepareCall("" + "declare\r\n" + "loadid number := ?;\r\n"
								+ "begin \r\n" + "delete from sbra_pens_row_cl t where ID_ = loadid;\r\n"
								+ "delete from SBRA_PENS_TRN_ROW t where LOAD_ID in (loadid,loadid+1);\r\n"
								+ "DELETE FROM Z_PENS_CL WHERE id_ = loadid;\r\n" + "end;\r\n");
						prp.setLong(1, sel.getLOAD_ID());
						prp.executeUpdate();
						prp.close();

						conn.commit();

						PensError(sel.getLOAD_ID());
						LoadTablePensExec();
					}
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Сохранить ошибки
	 */
	public void SaveError() {
		try {
			if (SBRA_PENS_LOG.getSelectionModel().getSelectedItem() == null
					& PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem() == null) {
				Msg.Message("Выберите сначала данные из таблицы!");
			} else {
				InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties");
				Properties prop = new Properties();
				// load a properties file
				prop.load(input);

				// SBRA_PENS_LOG errorsel = SBRA_PENS_LOG.getSelectionModel().getSelectedItem();
				PENS_LOAD_ROWSUM penssel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();

				FileChooser fileChooser = new FileChooser();

				System.setProperty("javax.xml.transform.TransformerFactory",
						"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
				fileChooser.setInitialDirectory(new File(prop.getProperty("PensiaLoadFolderDef")));
				input.close();

				// Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text File", "*.txt");

				fileChooser.setInitialFileName("File");

				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(null);

				if (file != null) {
					String createfolder = file.getParent() + "\\ERROR_" + penssel.getFILE_NAME() + ".txt";
					PrintWriter writer = new PrintWriter(createfolder);

					PreparedStatement prepStmt = conn
							.prepareStatement(DbUtil.Sql_From_Prop("/app/pensia/SQL.properties", "RefreshError"));
					prepStmt.setLong(1, penssel.getLOAD_ID());
					prepStmt.setLong(2, penssel.getLOAD_ID());
					ResultSet rs = prepStmt.executeQuery();
					String str = "";
					while (rs.next()) {
						str = str + rs.getString("F_STR") + "\r\n";
					}
					writer.write(str);
					writer.flush();
					writer.close();
					rs.close();
					prepStmt.close();
					Msg.Message("Файл сформирован!");
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Открыть в АБС
	 */
	public void OpenAbs() {
		try {

			if (DbUtil.Odb_Action(62l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			PENS_LOAD_ROWSUM sel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();
			if (sel == null) {
				Msg.Message("Выберите строку");
			} else {
				String call = "";

				final Alert alert = new Alert(AlertType.CONFIRMATION,
						" Yes = 60322% -> 4083% \r\n No = 4083% -> 40831%", ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
							+ "@ODB WHERE=\" trunc(DTRNTRAN) = trunc((select f.DATE_LOAD from SBRA_PENS_LOAD_ROWSUM f where f.LOAD_ID = "
							+ sel.getLOAD_ID() + ")) and ITRNBATNUM = 999 and CTRNPURP like '%{" + sel.getLOAD_ID()
							+ "}%'\"";
				} else {
					call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
							+ "@ODB WHERE=\" trunc(DTRNTRAN) = trunc((select f.DATE_LOAD from SBRA_PENS_LOAD_ROWSUM f where f.LOAD_ID = "
							+ sel.getLOAD_ID() + ")) and ITRNBATNUM = 996 and (CTRNPURP like '%{" + (sel.getLOAD_ID())
							+ "}%' or CTRNPURP like '%{" + (sel.getLOAD_ID() + 1) + "}%')\"";
				}

				ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
				System.out.println(call);
				builder.redirectErrorStream(true);
				Process p;
				p = builder.start();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while (true) {
					line = r.readLine();
					if (line == null) {
						break;
					}
					System.out.println(line);
				}
			}

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Удалить загрузку для разбивки
	 */
	public void DelDtBtnPart() {
		try {

			if (DbUtil.Odb_Action(44l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			SBRA_YEAR_BET sel = SBRA_YEAR_BET.getSelectionModel().getSelectedItem();
			if (sel == null) {
				Msg.Message("Выберите строку");
			} else {
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить часть \"" + sel.getPART() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
//					PreparedStatement prp = conn.prepareStatement("delete from SBRA_YEAR_BET where PART = ?");
//					prp.setLong(1, sel.getPART());
//					prp.executeUpdate();
//					conn.commit();
//					prp.close();
					// populate
					LoadTableSet();
				}
			}

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				Msg.Message(ExceptionUtils.getStackTrace(e1));
			}
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Возврат самого файла
	 * 
	 * @param id
	 * @param sess_id
	 * @param conn
	 * @param file
	 * @return
	 */
	public String retclob(int id, int sess_id, Connection conn, File file) {
		String str = "";
		try {
//			SqlMap s = new SqlMap().load("/SQL.xml");
//			String readRecordSQL = s.getSql("getPens");
			PreparedStatement prepStmt = conn.prepareStatement("" + "select RN||'|'||\n" + "       LAST_NAME||'|'||\n"
					+ "       FIRST_NAME||'|'||\n" + "       MIDDLE_NAME||'|'||\n" + "       COLUMN5||'|'||\n"
					+ "       ACC||'|'||\n" + "       replace(to_char(SUMM),',','.')||'|'||\n"
					+ "       to_char(ABS_BDATE,'dd.mm.rrrr')||'|'||\n" + "       COLUMN9||'|'||\n"
					+ "       COLUMN10||'|'||\n" + "       ACC_VTB||'|'||\n" + "       COLUMN12||'|'||\n"
					+ "       SNILS  str\n" + "  from Z_SB_PENS_WDP t\n" + " where part = ?\n"
					+ "order by ABS_BDATE asc, rn");
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();

			String createfolder = file.getParent() + "\\" + file.getName() + "_0" + id + ".txt";
//			System.out.println(readRecordSQL);

			PrintWriter writer = new PrintWriter(createfolder);

			while (rs.next()) {
				str = str + rs.getString("STR") + "\r\n";
			}
			writer.write(str);
			writer.flush();
			writer.close();
			rs.close();
			prepStmt.close();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return str;
	}

	/**
	 * Возврат самого файла
	 * 
	 * @param id
	 * @param sess_id
	 * @param conn
	 * @param file
	 */
	@Deprecated
	public void retclob_1(int id, int sess_id, Connection conn, File file) {
		try {
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select * from Z_SB_PENS_4FILE t where t.ID = " + sess_id;

			String createfolder = file.getParent() + "\\" + file.getName() + "_0" + id + ".txt";
			System.out.println(createfolder);
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			PrintWriter writer = new PrintWriter(createfolder);
			while (rs.next()) {
				if (id == 1) {
					writer.write(rs.getString("ONE_PART"));
				} else if (id == 2) {
					writer.write(rs.getString("TWO_PART"));
				} else if (id == 3) {
					writer.write(rs.getString("THREE_PART"));
				} else if (id == 4) {
					writer.write(rs.getString("FOUR_PART"));
				} else if (id == 5) {
					writer.write(rs.getString("FIVE_PART"));
				} else if (id == 6) {
					writer.write(rs.getString("SIX_PART"));
				}
			}
			writer.close();
			rs.close();
			sqlStatement.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Возврат самого файла
	 * 
	 * @param id
	 * @param sess_id
	 * @param conn
	 * @param file
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void retxlsx(int id, int sess_id, Connection conn, File file, String filename, String All) {
		try {
			TLB.setDisable(true);
			Service service = new Service() {
				@Override
				protected Task createTask() {
					return new Task() {
						@Override
						protected Object call() throws Exception {
							// ----------------------------------------------------------------
							try {
								// file name
								String createfolder = file.getParent() + "\\" + filename + "_0" + id + ".xlsx";
								// _____________________________
								Long cnt_row = 0l;

								PreparedStatement sqlStatement = null;
								String readRecordSQL = "";
								if (!All.equals("ALL")) {
									PreparedStatement cnt_prgrs = conn.prepareStatement("select count(*) cnt\n"
											+ "  from table(lob2table.separatedcolumns((SELECT FILE_CL\n"
											+ "                                          FROM Z_SB_PENS_4FILE_FILES\n"
											+ "                                         WHERE LOAD_ID = ?\n"
											+ "                                           AND PART_FILE = ?),\n"
											+ "                                        chr(13) || chr(10),\n"
											+ "                                        '|',\n"
											+ "                                        '')) h\n" + "");
									cnt_prgrs.setInt(1, sess_id);
									cnt_prgrs.setInt(2, id);

									ResultSet cnt_rs = cnt_prgrs.executeQuery();

									if (cnt_rs.next()) {
										cnt_row = cnt_rs.getLong("cnt");
									}
									cnt_prgrs.close();
									cnt_rs.close();

									System.out.println(cnt_row);

									readRecordSQL = "select to_number(COLUMN1) row_num,\n"
											+ "       COLUMN2 last_name,\n" + "       COLUMN3 first_name,\n"
											+ "       COLUMN4 middle_name,\n" + "       COLUMN5,\n"
											+ "       COLUMN6 acc,\n"
											+ "       TO_NUMBER(REPLACE(COLUMN7, '.', ',')) summ,\n"
											+ "       TO_CHAR(TO_DATE(COLUMN8, 'DD.MM.YYYY'), 'DD.MM.YYYY') BDATE,\n"
											+ "       COLUMN9,\n" + "       COLUMN10,\n" + "       COLUMN11 acc_vtb,\n"
											+ "       COLUMN12,\n" + "       COLUMN13 snils\n"
											+ "  from table(lob2table.separatedcolumns((SELECT FILE_CL\n"
											+ "                                          FROM Z_SB_PENS_4FILE_FILES\n"
											+ "                                         WHERE LOAD_ID = ?\n"
											+ "                                           AND PART_FILE = ?),\n"
											+ "                                        chr(13) || chr(10),\n"
											+ "                                        '|',\n"
											+ "                                        '')) h\n" + "";
									sqlStatement = conn.prepareStatement(readRecordSQL);

									sqlStatement.setInt(1, sess_id);
									sqlStatement.setInt(2, id);
									// _________________________________________
								} else {
									PreparedStatement cnt_prgrs = conn.prepareStatement("select count(*) cnt\r\n"
											+ "  from table(lob2table.separatedcolumns((select ALL_\r\n"
											+ "                                          from Z_SB_PENS_4FILE f\r\n"
											+ "                                         where f.id = ?),\r\n"
											+ "                                        chr(13) || chr(10),\r\n"
											+ "                                        '|',\r\n"
											+ "                                        '')) h\r\n" + "");
									cnt_prgrs.setInt(1, sess_id);

									ResultSet cnt_rs = cnt_prgrs.executeQuery();

									if (cnt_rs.next()) {
										cnt_row = cnt_rs.getLong("cnt");
									}
									cnt_prgrs.close();
									cnt_rs.close();

									System.out.println(cnt_row);

									readRecordSQL = "select to_number(COLUMN1) row_num,\r\n"
											+ "       COLUMN2 last_name,\r\n" + "       COLUMN3 first_name,\r\n"
											+ "       COLUMN4 middle_name,\r\n" + "       COLUMN5,\r\n"
											+ "       COLUMN6 acc,\r\n"
											+ "       TO_NUMBER(REPLACE(COLUMN7, '.', ',')) summ,\r\n"
											+ "       TO_CHAR(TO_DATE(COLUMN8, 'DD.MM.YYYY'), 'DD.MM.YYYY') BDATE,\r\n"
											+ "       COLUMN9,\r\n" + "       COLUMN10,\r\n"
											+ "       COLUMN11 acc_vtb,\r\n" + "       COLUMN12,\r\n"
											+ "       COLUMN13 snils\r\n"
											+ "  from table(lob2table.separatedcolumns((select ALL_\r\n"
											+ "                                          from Z_SB_PENS_4FILE f\r\n"
											+ "                                         where f.id = ?),\r\n"
											+ "                                        chr(13) || chr(10),\r\n"
											+ "                                        '|',\r\n"
											+ "                                        '')) h\r\n" + "";
									sqlStatement = conn.prepareStatement(readRecordSQL);

									sqlStatement.setInt(1, sess_id);
								}
								ResultSet rs = sqlStatement.executeQuery();
								// System.out.println(readRecordSQL);
								SXSSFWorkbook wb = new SXSSFWorkbook(100);
								Sheet sh = wb.createSheet("Таблица");
								Row row = sh.createRow(0);
								// header
								row.createCell(0).setCellValue("ROW_NUM");
								row.createCell(1).setCellValue("LAST_NAME");
								row.createCell(2).setCellValue("FIRST_NAME");
								row.createCell(3).setCellValue("MIDDLE_NAME");
								row.createCell(4).setCellValue("COLUMN5");
								row.createCell(5).setCellValue("ACC");
								row.createCell(6).setCellValue("SUMM");
								row.createCell(7).setCellValue("BDATE");
								row.createCell(8).setCellValue("COLUMN9");
								row.createCell(9).setCellValue("COLUMN10");
								row.createCell(10).setCellValue("ACC_VTB");
								row.createCell(11).setCellValue("COLUMN12");
								row.createCell(12).setCellValue("SNILS");

								System.out.println(readRecordSQL);

								// __________________________
								int i = 0;
								while (rs.next()) {
									System.out.println(i);
									row = sh.createRow(i + 1);
									row.createCell(0).setCellValue(rs.getInt("ROW_NUM"));
									row.createCell(1).setCellValue(rs.getString("LAST_NAME"));
									row.createCell(2).setCellValue(rs.getString("FIRST_NAME"));
									row.createCell(3).setCellValue(rs.getString("MIDDLE_NAME"));
									row.createCell(4).setCellValue(rs.getString("COLUMN5"));
									row.createCell(5).setCellValue(rs.getString("ACC"));
									row.createCell(6).setCellValue(rs.getDouble("SUMM"));
									row.createCell(7).setCellValue(rs.getString("BDATE"));
									row.createCell(8).setCellValue(rs.getString("COLUMN9"));
									row.createCell(9).setCellValue(rs.getString("COLUMN10"));
									row.createCell(10).setCellValue(rs.getString("ACC_VTB"));
									row.createCell(11).setCellValue(rs.getString("COLUMN12"));
									row.createCell(12).setCellValue(rs.getString("SNILS"));

									updateProgress(i, cnt_row);

									// System.out.println(i);
									i++;
								}
								rs.close();
								sqlStatement.close();

								wb.write(new FileOutputStream(createfolder));
								wb.close();
								Thread.sleep(100);
								updateProgress(cnt_row, cnt_row);
							} catch (Exception e) {
								ShowMes(ExceptionUtils.getStackTrace(e));
							}
							// TLB.setDisable(true);
							// ----------------------------------------------------------------
							return null;
						}
					};
				}
			};
			Progress.progressProperty().bind(service.progressProperty());
			service.setOnFailed(e -> ShowMes(ExceptionUtils.getStackTrace(service.getException())));
			service.setOnSucceeded(e -> TLB.setDisable(false));
			service.start();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Error message in new thread
	 * 
	 * @param error
	 */
	void ShowMes(String error) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Msg.Message(error);
			}
		});

	}

	/**
	 * Сессия
	 */
	private Connection conn;

	/**
	 * Открыть сессию
	 */
	private void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				conn.close();
			}
		} catch (SQLException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}

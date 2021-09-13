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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;

import app.model.Connect;
import app.model.TerminalDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import sb.utils.DbUtil;
import sbalert.Msg;

/**
 * Said 13.07.2020.
 */
public class PensController {

	@FXML
	private ToolBar TLB;

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
	private TableView<SBRA_YEAR_BET> SBRA_YEAR_BET;
	@FXML
	private TableColumn<SBRA_YEAR_BET, Long> PART;
	@FXML
	private TableColumn<SBRA_YEAR_BET, LocalDate> START_Y;
	@FXML
	private TableColumn<SBRA_YEAR_BET, LocalDate> END_Y;

	@FXML
	private TextField PensSum;
	@FXML
	private TextField PensCount;

	@FXML
	private Button Pens60322_408;

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

	@SuppressWarnings("unused")
	private Executor exec;

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
			// --
			DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
			PENS_LOAD_ROWSUM.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				PENS_LOAD_ROWSUM sel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();
				if (sel != null) {
					try {
						{
							PreparedStatement prp = conn
									.prepareCall("SELECT COUNT(*),SUM(TO_NUMBER(REPLACE(COLUMN7, '.', ',')))\n"
											+ "  FROM TABLE(LOB2TABLE.SEPARATEDCOLUMNS((SELECT CL_\n"
											+ "                                          FROM Z_PENS_CL T\n"
											+ "                                         WHERE T.TYPE_ = 'FILE'\n"
											+ "                                           AND ID_ = ?), /* THE DATA LOB */\n"
											+ "                                        CHR(13) || CHR(10), /* ROW SEPARATOR */\n"
											+ "                                        '|', /* COLUMN SEPARATOR */\n"
											+ "                                        '' /* DELIMITER (OPTIONAL) */))");
							prp.setLong(1, sel.getLOAD_ID());
							ResultSet rs = prp.executeQuery();
							if (rs.next()) {
								PensCount.setText(decimalFormat.format(rs.getLong(1)));
								PensSum.setText(decimalFormat.format(rs.getDouble(2)));
							}
							rs.close();
							prp.close();
						}

					} catch (Exception e) {
						Msg.Message(ExceptionUtils.getStackTrace(e));
					}
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
	
	/**
	 * Initialize table
	 */
	void PensError() {
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
				fileChooser.setInitialDirectory(
						new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
				// Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xlsx");

				fileChooser.setInitialFileName("File");

				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File file = fileChooser.showSaveDialog(null);

				if (file != null) {
					PreparedStatement prp_part = conn.prepareStatement("SELECT * FROM SBRA_YEAR_BET ORDER BY PART ASC");
					ResultSet rs = prp_part.executeQuery();
					while (rs.next()) {
						retxlsx(rs.getInt("PART"), pens.getid(), conn, file,
								sep_pens.getSelectionModel().getSelectedItem().getfilename());
					}
					rs.close();
					prp_part.close();
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
			if (column.getText().equals("Количество строк")) {

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
		} catch (IOException e) {
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
			fileChooser
					.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
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
	void RunProcess(String type) {
		Timer time = new Timer(); // Instantiate Timer Object
		st = new ScheduledTask(); // Instantiate SheduledTask class
		st.setSWC(this, type);
		time.schedule(st, 0, 3000); // Create task repeating every 1 sec
	}

	/**
	 * Загрузить файл
	 */
	public void LoadPens() {
		try {
			InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties");
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text file", "*.txt"));
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
							CallableStatement callStmt_j = conn.prepareCall("{ call Z_PENS_KERNEL.jobLoad(?,?,?)}");
							callStmt_j.setLong(1, ret_id);
							callStmt_j.setInt(2, 1000);
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
							// Go stat______________
							RunProcess("");
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
	 * Открыть в АБС
	 */
	public void OpenAbs() {
		try {
//			if (DbUtil.Odb_Action(44l) == 0) {
//				Msg.Message("Нет доступа!");
//				return;
//			}
			PENS_LOAD_ROWSUM sel = PENS_LOAD_ROWSUM.getSelectionModel().getSelectedItem();
			if (sel == null) {
				Msg.Message("Выберите строку");
			} else {
				String call = "";

				final Alert alert = new Alert(AlertType.CONFIRMATION,
						" Yes = 60322% -> 4083% \r\n No = 4083% -> 40831%", ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
							+ "@ODB WHERE=\" DTRNCREATE = trunc((select f.DATE_LOAD from SBRA_PENS_LOAD_ROWSUM f where f.LOAD_ID = "
							+ sel.getLOAD_ID() + ")) and ITRNBATNUM = 999 and CTRNPURP like '%{" + sel.getLOAD_ID()
							+ "}%'\"";
				} else {
					call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
							+ "@ODB WHERE=\" DTRNCREATE = trunc((select f.DATE_LOAD from SBRA_PENS_LOAD_ROWSUM f where f.LOAD_ID = "
							+ sel.getLOAD_ID() + ")) and ITRNBATNUM = 996 and CTRNPURP like '%{"
							+ (sel.getLOAD_ID() + 1) + "}%'\"";
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
					PreparedStatement prp = conn.prepareStatement("delete from SBRA_YEAR_BET where PART = ?");
					prp.setLong(1, sel.getPART());
					prp.executeUpdate();
					conn.commit();
					prp.close();
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
	public void retxlsx(int id, int sess_id, Connection conn, File file, String filename) {
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

								String readRecordSQL = "select to_number(COLUMN1) row_num,\n"
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
								PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);

								sqlStatement.setInt(1, sess_id);
								sqlStatement.setInt(2, id);

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
			service.setOnFailed(e -> ShowMes(service.getException().getMessage()));
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

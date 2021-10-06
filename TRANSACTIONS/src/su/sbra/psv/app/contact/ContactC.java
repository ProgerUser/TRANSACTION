package su.sbra.psv.app.contact;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.table.TableFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.swift.ConvConst;

public class ContactC {

	@FXML
	private DatePicker LoadDate;

	// Table
	@FXML
	private TableView<SBRA_LOADF_CONTACT> SBRA_LOADF_CONTACT;
	@FXML
	private TableColumn<SBRA_LOADF_CONTACT, String> LD_FILENAME;
	@FXML
	private TableColumn<SBRA_LOADF_CONTACT, Long> LOAD_ID;
	@FXML
	private TableColumn<SBRA_LOADF_CONTACT, LocalDateTime> LOAD_DATE;
	@FXML
	private TableColumn<SBRA_LOADF_CONTACT, String> LD_USER;

	// ---------------------
	@FXML
	private TableView<SBRA_CONTACT_ACC_CODE> SBRA_CONTACT_ACC_CODE;
	@FXML
	private TableColumn<SBRA_CONTACT_ACC_CODE, String> COD_;
	@FXML
	private TableColumn<SBRA_CONTACT_ACC_CODE, String> CODE_NAME;
	@FXML
	private TableColumn<SBRA_CONTACT_ACC_CODE, String> ACC_701;
	@FXML
	private TableColumn<SBRA_CONTACT_ACC_CODE, String> CACCNAME;
	@FXML
	private TableColumn<SBRA_CONTACT_ACC_CODE, Long> IACCOTD;
	// --------------------------

	@FXML
	void Choose(ActionEvent event) {
		try {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser
					.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Excel File", "*.xls"));
			File file = fileChooser.showOpenDialog(null);

			if (file != null) {

				final Alert alert = new Alert(AlertType.CONFIRMATION, "Загрузить файл \"" + file.getName() + "\" ?",
						ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					XSSFWorkbook xlsx = XlsToXlsx(file.getAbsolutePath());

					CallableStatement callStmt = conn.prepareCall("{ call Z_SB_CALC_CONTACT.LOAD(?,?,?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.registerOutParameter(4, Types.VARCHAR);

					// xlsx to blob
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					xlsx.write(baos);
					byte[] byteArray = baos.toByteArray();
					// add parameters
					callStmt.setBlob(2, new ByteArrayInputStream(byteArray), byteArray.length);
					callStmt.setString(3, file.getName());
					// catch
					try {
						callStmt.execute();
					} catch (Exception e) {
						Msg.Message(ExceptionUtils.getStackTrace(e));
						conn.rollback();
						callStmt.close();
					}
					// return
					String ret = callStmt.getString(1);
					String load_id = callStmt.getString(4);
					// check
					if (ret != null) {
						// view error
						ViewError();
						// roll back
						conn.rollback();
						Msg.Message(ret);

					} else {
						conn.commit();
						Msg.Message("Файл " + file.getName() + " загружен");
						// reload
						LoadTableContact();
						// view in abs
						OpenAbsForm(load_id);
					}
					// close
					callStmt.close();
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static final LocalDate NOW_LOCAL_DATE() {
		String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate;
	}

	@FXML
	void OpenAbsForm2(ActionEvent event) {
		try {
			if (SBRA_LOADF_CONTACT.getSelectionModel().getSelectedItem() != null) {
				SBRA_LOADF_CONTACT selrow = SBRA_LOADF_CONTACT.getSelectionModel().getSelectedItem();
				String call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
						+ "@ODB WHERE=\"" + "ITRNNUM IN (SELECT TRNNUM_DP FROM SBRA_LOADROW_CONTACT WHERE LOAD_ID = "
						+ selrow.getLOAD_ID() + ") \"";
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

	@FXML
	void DelLoad(ActionEvent event) {
		try {
			if (SBRA_LOADF_CONTACT.getSelectionModel().getSelectedItem() != null) {
				SBRA_LOADF_CONTACT selrow = SBRA_LOADF_CONTACT.getSelectionModel().getSelectedItem();
				
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить файл \"" + selrow.getLD_FILENAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					
					CallableStatement callStmt = conn.prepareCall("{ call Z_SB_CALC_CONTACT.DEL_LOAD(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, selrow.getLOAD_ID());
					// catch
					try {
						callStmt.execute();
					} catch (Exception e) {
						Msg.Message(ExceptionUtils.getStackTrace(e));
						conn.rollback();
						callStmt.close();
					}

					// return
					String ret = callStmt.getString(1);
					// check
					if (ret != null) {
						// roll back
						conn.rollback();
						Msg.Message(ret);

					} else {
						conn.commit();
						Msg.Message("Файл " + selrow.getLD_FILENAME() + " удален");
						// reload
						LoadTableContact();
					}
					// close
					callStmt.close();
				}
			}

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	void OpenAbsForm(String load_id) {
		try {
			String call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
					+ "@ODB WHERE=\"" + "ITRNNUM IN (SELECT TRNNUM_DP FROM SBRA_LOADROW_CONTACT WHERE LOAD_ID = "
					+ load_id + ") \"";
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

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	void ViewError() {
		try {
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
			String strDate = dateFormat.format(date);

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "SELECT * FROM Z_SB_CONTACT_ERROR";

			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");

			String strDate_ = dateFormat_.format(date);
			String createfolder = System.getenv("TRANSACT_PATH") + "ContactLog/" + strDate_;
			File file = new File(createfolder);

			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}

			String path_file = createfolder + "\\" + strDate + "_ERROR.txt";
			PrintWriter writer = new PrintWriter(path_file);
			while (myResultSet.next()) {
				writer.write(myResultSet.getString("ID") + "__" + myResultSet.getString("TEXT") + "__"
						+ myResultSet.getString("SUMM") + "__" + myResultSet.getString("NUMBER_") + "\r\n");
			}

			writer.close();

			ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_ERROR.txt");
			pb.start();
			myResultSet.close();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Авто расширение
	 * 
	 * @param table
	 */
	void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column_) -> {
			// column1.getColumns().stream().forEach((column_) -> {
			// System.out.println(column_.getText());
			if (column_.getText().equals("Дата изменения")) {

			} else {
				// Minimal width = columnheader
				Text t = new Text(column_.getText());
				double max = t.getLayoutBounds().getWidth();
				for (int i = 0; i < table.getItems().size(); i++) {
					// cell must not be empty
					if (column_.getCellData(i) != null) {
						if (column_.getCellData(i) != null) {
							t = new Text(column_.getCellData(i).toString());
							double calcwidth = t.getLayoutBounds().getWidth();
							// remember new max-width
							if (calcwidth > max) {
								max = calcwidth;
							}
						}
					}
				}
				// set the new max-widht with some extra space
				column_.setPrefWidth(max + 20.0d);
			}
			// });

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
			props.put("v$session.program", "ContactXlsx");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Отключить сессию
	 */
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

	@SuppressWarnings({ "resource", "incomplete-switch" })
	XSSFWorkbook XlsToXlsx(String FileName) throws IOException {
		XSSFWorkbook wbOut = new XSSFWorkbook();
		String inpFn = FileName;

		InputStream in = new BufferedInputStream(new FileInputStream(inpFn));
		try {
			Workbook wbIn = new HSSFWorkbook(in);

			int sheetCnt = wbIn.getNumberOfSheets();
			for (int i = 0; i < sheetCnt; i++) {
				Sheet sIn = wbIn.getSheetAt(0);
				Sheet sOut = wbOut.createSheet(sIn.getSheetName());
				Iterator<Row> rowIt = sIn.rowIterator();
				while (rowIt.hasNext()) {
					Row rowIn = rowIt.next();
					Row rowOut = sOut.createRow(rowIn.getRowNum());

					Iterator<Cell> cellIt = rowIn.cellIterator();
					while (cellIt.hasNext()) {
						Cell cellIn = cellIt.next();
						Cell cellOut = rowOut.createCell(cellIn.getColumnIndex(), cellIn.getCellType());

						switch (cellIn.getCellType()) {
						case BLANK:
							break;

						case BOOLEAN:
							cellOut.setCellValue(cellIn.getBooleanCellValue());
							break;

						case ERROR:
							cellOut.setCellValue(cellIn.getErrorCellValue());
							break;

						case FORMULA:
							cellOut.setCellFormula(cellIn.getCellFormula());
							break;

						case NUMERIC:
							cellOut.setCellValue(cellIn.getNumericCellValue());
							break;

						case STRING:
							cellOut.setCellValue(cellIn.getStringCellValue());
							break;
						}

						{
							CellStyle styleIn = cellIn.getCellStyle();
							CellStyle styleOut = cellOut.getCellStyle();
							styleOut.setDataFormat(styleIn.getDataFormat());
						}
						cellOut.setCellComment(cellIn.getCellComment());
					}
				}
			}
		} finally {
			in.close();
		}
		return wbOut;
	}

	/**
	 * Initialize table
	 */
	void LoadTableContact() {
		try {
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			String dt = "";

			if (LoadDate.getValue() != null) {
				dt = "and trunc(LOAD_DATE) = to_date('" + LoadDate.getValue().format(formatter) + "','dd.mm.yyyy')";
			}

			String selectStmt = "select * from SBRA_LOADF_CONTACT where 1=1 " + dt + " order by LOAD_ID desc";

			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SBRA_LOADF_CONTACT> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				SBRA_LOADF_CONTACT list = new SBRA_LOADF_CONTACT();

				list.setLD_USER(rs.getString("LD_USER"));
				list.setLD_FILENAME(rs.getString("LD_FILENAME"));
				list.setLOAD_DATE((rs.getDate("LOAD_DATE") != null) ? LocalDateTime
						.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("LOAD_DATE")), formatterwt)
						: null);
				list.setLOAD_ID(rs.getLong("LOAD_ID"));
				cus_list.add(list);
			}
			// add data
			SBRA_LOADF_CONTACT.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<SBRA_LOADF_CONTACT> tableFilter = TableFilter.forTableView(SBRA_LOADF_CONTACT).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});

			// resize
			autoResizeColumns(SBRA_LOADF_CONTACT);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Initialize table
	 */
	void LoadTableContactAcc() {
		try {
			String selectStmt = "select * from SBRA_CONTACT_ACC_CODE";

			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SBRA_CONTACT_ACC_CODE> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				SBRA_CONTACT_ACC_CODE list = new SBRA_CONTACT_ACC_CODE();

				list.setCOD_(rs.getString("COD_"));
				list.setACC_701(rs.getString("ACC_701"));
				list.setCODE_NAME(rs.getString("CODE_NAME"));
				list.setCACCNAME(rs.getString("CACCNAME"));
				list.setIACCOTD(rs.getLong("IACCOTD"));

				cus_list.add(list);
			}
			// add data
			SBRA_CONTACT_ACC_CODE.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<SBRA_CONTACT_ACC_CODE> tableFilter = TableFilter.forTableView(SBRA_CONTACT_ACC_CODE).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(SBRA_CONTACT_ACC_CODE);
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

	@FXML
	private void ChangeDate() {
		try {
			LoadTableContact();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private void DelDate() {
		try {
			LoadDate.setValue(null);
			LoadTableContact();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private void initialize() {
		try {
			// connect
			dbConnect();
			// fast date
			new ConvConst().FormatDatePiker(LoadDate);

			LOAD_DATE.setCellFactory(column -> {
				TableCell<SBRA_LOADF_CONTACT, LocalDateTime> cell = new TableCell<SBRA_LOADF_CONTACT, LocalDateTime>() {
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

			// date piker now date
			LoadDate.setValue(NOW_LOCAL_DATE());
			// table column data
			LD_FILENAME.setCellValueFactory(cellData -> cellData.getValue().LD_FILENAMEProperty());
			LD_USER.setCellValueFactory(cellData -> cellData.getValue().LD_USERProperty());
			LOAD_DATE.setCellValueFactory(cellData -> cellData.getValue().LOAD_DATEProperty());
			LOAD_ID.setCellValueFactory(cellData -> cellData.getValue().LOAD_IDProperty().asObject());
			// ------------------
			COD_.setCellValueFactory(cellData -> cellData.getValue().COD_Property());
			CODE_NAME.setCellValueFactory(cellData -> cellData.getValue().CODE_NAMEProperty());
			ACC_701.setCellValueFactory(cellData -> cellData.getValue().ACC_701Property());
			CACCNAME.setCellValueFactory(cellData -> cellData.getValue().CACCNAMEProperty());
			IACCOTD.setCellValueFactory(cellData -> cellData.getValue().IACCOTDProperty().asObject());
			// load
			LoadTableContact();
			// --
			LoadTableContactAcc();
			//

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}

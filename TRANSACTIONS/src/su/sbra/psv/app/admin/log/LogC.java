package su.sbra.psv.app.admin.log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.swift.ConvConst;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class LogC {
	@FXML
	private TableView<LOGS> LogTable;
	@FXML
	private TableColumn<LOGS, LocalDateTime> DateTime;
	@FXML
	private TableColumn<LOGS, Long> RowNumber;
	@FXML
	private TableColumn<LOGS, Long> ClassRowNumber;
	@FXML
	private TableColumn<LOGS, String> Class;
	@FXML
	private TableColumn<LOGS, String> TypeMessage;
	@FXML
	private TableColumn<LOGS, String> Mesage;

	@FXML
	void Refresh(ActionEvent event) {
		try {
			LoadTable();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
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
	 * Кодировка
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileCharset(String file) {
		try {
			byte[] buf = new byte[4096];
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
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	private static String readFile(String fileName) {
		try {

			FileInputStream fionstr = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fionstr, getFileCharset(fileName)));

			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(System.lineSeparator());
			}
			String clobData = sb.toString();
			br.close();
			fionstr.close();
			return clobData;
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Initialize table
	 */
	void LoadTable() {
		try {
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			String selectStmt = "select to_number(ROW_NO) RowNumber,\r\n"
					+ "       to_date(COLUMN1, 'dd.mm.yyyy HH24:mi:ss') TM$DateTime,\r\n" + "       COLUMN2 Class,\r\n"
					+ "       COLUMN3 TypeMessage,\r\n" + "       to_number(COLUMN4) ClassRowNumber,\r\n"
					+ "       COLUMN5 Mesage\r\n" + "  from table(lob2table.separatedcolumns(?, /* the data LOB */\r\n"
					+ "                                        '$End$', /* row separator */\r\n"
					+ "                                        '$Sep$', /* column separator */\r\n"
					+ "                                        '' /* delimiter (optional) */))";

			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);

			String reviewStr = readFile("//fsrv/obmen/TR/trapp.txt");

			Clob clob = DBUtil.conn.createClob();
			clob.setString(1, reviewStr);

			prepStmt.setClob(2, clob);

			ResultSet rs = prepStmt.executeQuery();
			ObservableList<LOGS> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				LOGS list = new LOGS();
				list.setOPER(rs.getString("OPER"));
				list.setLOGDATE((rs.getDate("LOGDATE") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("LOGDATE")), formatterwt) : null);
				list.setMETHODNAME(rs.getString("METHODNAME"));
				if (rs.getClob("ERROR") != null) {
					list.setERROR(new ConvConst().ClobToString(rs.getClob("ERROR")));
				}
				list.setCLASSNAME(rs.getString("CLASSNAME"));
				list.setLINENUMBER(rs.getLong("LINENUMBER"));

				cus_list.add(list);
			}
			// add data
			LogTable.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<LOGS> tableFilter = TableFilter.forTableView(LogTable).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});

			// resize
			autoResizeColumns(LogTable);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private void initialize() {
		try {
//			Mesage.setCellValueFactory(cellData -> cellData.getValue().MESAGEProperty());
//			TypeMessage.setCellValueFactory(cellData -> cellData.getValue().TYPEMESSAGEProperty());
//			Class.setCellValueFactory(cellData -> cellData.getValue().CLASSProperty());
//			ClassRowNumber.setCellValueFactory(cellData -> cellData.getValue().CLASSROWNUMBERProperty().asObject());
//			RowNumber.setCellValueFactory(cellData -> cellData.getValue().ROWNUMBERProperty().asObject());
//			DateTime.setCellValueFactory(cellData -> cellData.getValue().TM$DATETIMEProperty());

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}

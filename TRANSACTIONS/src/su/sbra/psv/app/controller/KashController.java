package su.sbra.psv.app.controller;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import su.sbra.psv.app.contact.SBRA_CONTACT_ACC_CODE;
import su.sbra.psv.app.contact.SBRA_LOADF_CONTACT;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Amra_Trans;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.KashClass;
import su.sbra.psv.app.model.TerminalClass;
import su.sbra.psv.app.model.ViewerDAO;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.swift.SWC;
import su.sbra.psv.app.tr.pl.Pl;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.table.TableFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
				((KashClass) t.getTableView().getItems().get(t.getTablePosition().getRow())).setckbk(t.getNewValue());
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

	@FXML
	void loadexcel(ActionEvent actionEvent) {
		System.setProperty("javax.xml.transform.TransformerFactory",
				"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser
					.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Excel File after 2007", "*.xlsx"),
					new ExtensionFilter("Excel File defore 2007", "*.xls"));
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Загрузить файл \"" + file.getName() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					if (file.getName().substring(file.getName().indexOf(".") + 1, file.getName().length())
							.equals("xls")) {
						Workbook xlsx = xls2xlsx(file.getPath());
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						try {
							xlsx.write(bos);
						} finally {
							bos.close();
						}
						byte[] bytes = bos.toByteArray();
						InputStream is = new ByteArrayInputStream(bytes);
						Connection conn = DBUtil.conn;
						CallableStatement callStmt = null;
						callStmt = conn.prepareCall("{ ? =  call Get_XlsAFile(?)} ");
						callStmt.registerOutParameter(1, Types.VARCHAR);
						callStmt.setBlob(2, is, bytes.length);
						callStmt.execute();
						String ret = callStmt.getString(1);
						if (!ret.equals("ok")) {
							Msg.Message(ret);
						} else {
							Msg.Message("Файл загружен успешно!");
							create_psevdo(null);
						}
					} else {
						FileInputStream in = new FileInputStream(file);
						Connection conn = DBUtil.conn;
						CallableStatement callStmt = conn.prepareCall("{ ? =  call Get_XlsAFile(?)} ");
						callStmt.registerOutParameter(1, Types.VARCHAR);
						callStmt.setBlob(2, in, file.length());
						callStmt.execute();
						String ret = callStmt.getString(1);
						if (!ret.equals("ok")) {
							Msg.Message(ret);
						} else {
							Msg.Message("Файл загружен успешно!");
							create_psevdo(null);
						}
					}
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public Workbook xls2xlsx(String inpFn) throws Exception {
		InputStream in = new BufferedInputStream(new FileInputStream(inpFn));
		Workbook wbIn = new HSSFWorkbook(in);
		Workbook wbOut = new XSSFWorkbook();
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
					Cell cellOut = rowOut.createCell(cellIn.getColumnIndex(), CellType.STRING);

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
						String str = String.valueOf(cellIn.getNumericCellValue());
						cellOut.setCellValue(str);
						break;
					case STRING:
						cellOut.setCellValue(cellIn.getStringCellValue());
						break;
					default:
						break;
					}
					{
						CellStyle styleIn = cellIn.getCellStyle();
						CellStyle styleOut = cellOut.getCellStyle();
						styleOut.setDataFormat(styleIn.getDataFormat());
					}
					cellOut.setCellComment(cellIn.getCellComment());
					// HSSFCellStyle cannot be cast to XSSFCellStyle
					// cellOut.setCellStyle(cellIn.getCellStyle());
					sOut.autoSizeColumn(cellIn.getColumnIndex());
				}
			}
		}
		in.close();
		wbIn.close();
		return wbOut;
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
				column.setPrefWidth(max + 20.0d);
			}
		});
	}

	// Search all transacts
	@FXML
	void create_psevdo(ActionEvent actionEvent) throws ClassNotFoundException {
		try {
			Connection conn = DBUtil.conn;
			Statement chekStatement = conn.createStatement();
			String chek = "select count(*) from ov_plat where CPSEVDO is null";
			ResultSet rs = chekStatement.executeQuery(chek);
			if (rs.next() & rs.getInt(1) > 0) {
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Сформировать псевдонимы?", ButtonType.YES,
						ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					DBUtil.dbExecuteUpdate("BEGIN update_psevdonim_idovplat; COMMIT; END;");
					LoadTable();
					DBUtil.dbExecuteUpdate("BEGIN delete from z_sb_psevdo_aggregate; COMMIT; END;");
				}
			} else {
				Msg.Message("Нет данных!");
			}
			rs.close();
			// conn.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	void LoadTable() {
		try {
			String selectStmt = "select dp.cnameoper, dp.ckbk, dp.cpsevdo, a.C_CASHNAME \n"
					+ "  from ov_plat dp, OV_VCPLAT a\n" + " where dp.IDOV_PLAT = a.IDOV_PLAT\n"
					+ "   and dp.idov_plat in (SELECT idov_plat_ FROM z_sb_psevdo_aggregate)\n";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<KashClass> cus_list = FXCollections.observableArrayList();
			while (rs.next()) {
				KashClass tr = new KashClass();
				tr.setckbk(rs.getString("ckbk"));
				tr.setcnameoper(rs.getString("cnameoper"));
				tr.setcpsevdo(rs.getString("cpsevdo"));
				tr.setC_CASHNAME(rs.getString("C_CASHNAME"));
				cus_list.add(tr);
			}
			// add data
			employeeTable.setItems(cus_list);

			// close
			prepStmt.close();
			rs.close();

			// add filter
			TableFilter<KashClass> tableFilter = TableFilter.forTableView(employeeTable).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			autoResizeColumns(employeeTable);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@Deprecated
	@FXML
	void Rash(ActionEvent event) {
		try {

			if (DbUtil.Odb_Action(14l) == 0) {
				Msg.Message("Нет доступа!");
				return;
			}

			Stage stage = new Stage();
			Stage stage_ = (Stage) employeeTable.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/tr/pl/Pl.fxml"));

			Pl controller = new Pl();
			loader.setController(controller);
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("Расход с пластика");
			stage.initOwner(stage_);
			stage.setResizable(true);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					
				}
			});
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}

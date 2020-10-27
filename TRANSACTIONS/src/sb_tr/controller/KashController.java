package sb_tr.controller;

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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sb_tr.Main;
import sb_tr.model.Amra_Trans;
import sb_tr.model.Connect;
import sb_tr.model.KashClass;
import sb_tr.model.TerminalClass;
import sb_tr.model.ViewerDAO;
import sb_tr.util.DBUtil;

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
	private void loadexcel(ActionEvent actionEvent) {
		System.setProperty("javax.xml.transform.TransformerFactory",
				"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");

			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Excel File", "*.xlsx"));

			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				/*
				if (file.getName().substring(file.getName().indexOf(".") + 1, file.getName().length()).equals("xls")) {

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
						TextArea alert = new TextArea();
						alert.setPrefSize(600, 400);
						AnchorPane yn = new AnchorPane();
						Scene ynScene = new Scene(yn, 600, 400);
						yn.getChildren().add(alert);
						Stage newWindow_yn = new Stage();
						newWindow_yn.setTitle("Внимание");
						newWindow_yn.setScene(ynScene);
						// Specifies the modality for new window.
						newWindow_yn.initModality(Modality.WINDOW_MODAL);

						Stage stage = (Stage) employeeTable.getScene().getWindow();
						// Specifies the owner Window (parent) for new window
						newWindow_yn.initOwner(stage);
						newWindow_yn.getIcons().add(new Image("icon.png"));
						newWindow_yn.show();
						alert.setText(ret);
					}

				} else {
				*/
					FileInputStream in = new FileInputStream(file);
					Connection conn = DBUtil.conn;
					CallableStatement callStmt = null;
					callStmt = conn.prepareCall("{ ? =  call Get_XlsAFile(?)} ");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setBlob(2, in, file.length());
					callStmt.execute();
					String ret = callStmt.getString(1);
					if (!ret.equals("ok")) {
						TextArea alert = new TextArea();
						alert.setPrefSize(600, 400);
						AnchorPane yn = new AnchorPane();
						Scene ynScene = new Scene(yn, 600, 400);
						yn.getChildren().add(alert);
						Stage newWindow_yn = new Stage();
						newWindow_yn.setTitle("Внимание");
						newWindow_yn.setScene(ynScene);
						// Specifies the modality for new window.
						newWindow_yn.initModality(Modality.WINDOW_MODAL);
						Stage stage = (Stage) employeeTable.getScene().getWindow();
						// Specifies the owner Window (parent) for new window
						newWindow_yn.initOwner(stage);
						newWindow_yn.getIcons().add(new Image("icon.png"));
						newWindow_yn.show();
						alert.setText(ret);
					}
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert(e.getMessage());
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
				column.setPrefWidth(max + 10.0d);
			}
		});
	}

	// Search all transacts
	@FXML
	private void create_psevdo(ActionEvent actionEvent) {
		try {
			/*
			 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
			 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
			 * + "");
			 */
			Connection conn = DBUtil.conn;
			Statement chekStatement = conn.createStatement();
			String chek = "select IDOV_PLAT from ov_plat where CPSEVDO is null";
			ResultSet rs = chekStatement.executeQuery(chek);
			if (rs.next()) {
				ViewerDAO.kash_psevdo();
				ObservableList<KashClass> empData = ViewerDAO.searchKash();
				populateKash(empData);
				ViewerDAO.delete_kash_psevdo();
				autoResizeColumns(employeeTable);
				@SuppressWarnings("deprecation")
				TableFilter<KashClass> filter = new TableFilter<>(employeeTable);
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setHeaderText("Warning");
				alert.setContentText("Нет данных!");
				alert.show();
			}
			rs.close();
			// conn.close();
		} catch (SQLException e) {
			Alert(e.getMessage());
		}
	}

	public void Alert(String mes) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mes);
		alert.showAndWait();
	}

	private void populateKash(ObservableList<KashClass> trData) {
		employeeTable.setItems(trData);
	}
}

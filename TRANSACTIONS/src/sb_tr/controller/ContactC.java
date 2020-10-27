package sb_tr.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import sb_tr.util.DBUtil;
import sbalert.Msg;

public class ContactC {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField data;

	@FXML
	private TextArea out;

	@FXML
	private TextField path;

	@FXML
	private TextField summall;

	@FXML
	private TextField summaminus;

	@FXML
	private TextField summaotmen;

	@FXML
	void choose(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Выбрать файл");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Excel File", "*.xls"));
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			path.setText(file.getParent() + "\\" + file.getName());
		}
	}

	@SuppressWarnings("resource")
	@FXML
	void insert_db(ActionEvent event) {
		try {
			if (path.getText().equals("")) {
				Msg.MessageBox("Выберите файл для загрузки!", (Stage) path.getScene().getWindow());
			} else {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
				String strDate = dateFormat.format(date);
				DataFormatter dataFormatter = new DataFormatter();
				Statement statement = null;
				DBUtil.dbDisconnect();
				DBUtil.dbConnect();
				Connection conn = DBUtil.conn;
				PreparedStatement sql_statement = null;
				String jdbc_insert_sql = "INSERT INTO Z_SB_CONTACT"
						+ "(cod,code_name,summ,purp,cardnumber,NUMBEP) "
						+ "VALUES " + "(?,?,?,?,?,?)";
				sql_statement = conn.prepareStatement(jdbc_insert_sql);
				FileInputStream input_document = new FileInputStream(new File(path.getText()));
				HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
				HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
				Iterator<Row> rowIterator = my_worksheet.rowIterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() > 9) {
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							if (cell.getColumnIndex() == 4) {// code
								sql_statement.setString(1, dataFormatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 5) {// code_name
								sql_statement.setString(2, dataFormatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 9) {// summ
								sql_statement.setString(3, dataFormatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 11) {// purp
								sql_statement.setString(4, dataFormatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 12) {// cardnumber
								sql_statement.setString(5, dataFormatter.formatCellValue(cell));
							} else if (cell.getColumnIndex() == 1) {// NUMBEP
								sql_statement.setString(6, dataFormatter.formatCellValue(cell));
							} 
						}
						sql_statement.executeUpdate();
					}
				}
				
				String execute_ = "begin \n" + 
				                   "z_sb_calc_contact.create_;\n" + 
						          "end;\n ";
				statement = conn.createStatement();
				statement.execute(execute_);
				Statement sqlStatement = conn.createStatement();
				String readRecordSQL = "SELECT * FROM Z_SB_CONTACT_ERROR";
				ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
				DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
				String strDate_ = dateFormat_.format(date);
				String createfolder = System.getenv("TRANSACT_PATH")+"ContactLog/"+ strDate_;
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
				input_document.close();
				sql_statement.close();
			}
		} catch (SQLException | IOException e) {
			Msg.MessageBox(e.getMessage(), (Stage) path.getScene().getWindow());
		}
	}
	@FXML
	void initialize() {
		
	}
}

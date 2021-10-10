package su.sbra.psv.app.trlist;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Amra_Trans;
import su.sbra.psv.app.model.Attributes;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.FN_SESS_AMRA;
import su.sbra.psv.app.model.TerminalDAO;
import su.sbra.psv.app.model.Transact;
import su.sbra.psv.app.model.TransactClass;
import su.sbra.psv.app.model.Unpiv;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.controlsfx.control.table.TableFilter;

import java.util.Date;

/**
 * Саид 04.04.2019.
 * 101021
 */

@SuppressWarnings("unused")
public class Unpiv_Controller {

	@FXML
	private TableColumn<Unpiv, String> COL;
	@FXML
	private TableColumn<Unpiv, String> COLVALUE;
	@FXML
	private TableView<Unpiv> trans_table;

	// For MultiThreading
	private Executor exec;

	@FXML
	private void initialize() {
		trans_table.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		COL.setCellValueFactory(cellData -> cellData.getValue().COLProperty());
		COLVALUE.setCellValueFactory(cellData -> cellData.getValue().COLVALUEProperty());

		//COL.setCellFactory(TextFieldTableCell.forTableColumn());
		COLVALUE.setCellFactory(TextFieldTableCell.forTableColumn());

		COL.setOnEditCommit(new EventHandler<CellEditEvent<Unpiv, String>>() {
			@Override
			public void handle(CellEditEvent<Unpiv, String> t) {
				((Unpiv) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_COL(t.getNewValue());
			}
		});
		COLVALUE.setOnEditCommit(new EventHandler<CellEditEvent<Unpiv, String>>() {
			@Override
			public void handle(CellEditEvent<Unpiv, String> t) {
				((Unpiv) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_COLVALUE(t.getNewValue());
			}
		});
		ObservableList<Unpiv> empData = TerminalDAO.Unpiv_View();
		populate_attr(empData);
		autoResizeColumns(trans_table);
		@SuppressWarnings("deprecation")
		TableFilter<Unpiv> filter = new TableFilter<>(trans_table);
		
		COL.setCellFactory(col -> new TextFieldTableCell<Unpiv, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
					setStyle("");
				} else {
					setText(item.toString());  
					if (item.equals("ДатаОперации=DATEOFOPERATION")) {
						setStyle("-fx-text-fill: #8B9FFA;");
					} else if (item.equals("Дилер=DEALER")) {
						setStyle("-fx-text-fill: rgb(162, 189, 48);");
					}
					else if (item.equals("НомерЧека=CHECKNUMBER")) {
						setStyle("-fx-text-fill: #E6F06E;");
					}
					else if (item.equals("Провайдер=PROVIDER")) {
						setStyle("-fx-text-fill: #D0D1BF;");
					}
					else if (item.equals("Статус=STATUS")) {
						setStyle("-fx-text-fill: #E6B2F6 ;");
					}
					else if (item.equals("СуммаКомиссии=COMMISSIONAMOUNT")) {
						setStyle("-fx-text-fill: #83C0F2;");
					}
					else if (item.equals("СуммаНК=NKAMOUNT")) {
						setStyle("-fx-text-fill: #5EC395;");
					}
					else if (item.equals("СуммаНаличных=CASHAMOUNT")) {
						setStyle("-fx-text-fill: #CAA2E5;");
					}
					else if (item.equals("СуммаНаЧек=AMOUNTTOCHECK")) {
						setStyle("-fx-text-fill: #EECCAF;");
					}
					else if (item.equals("СуммаПлатежа=AMOUNTOFPAYMENT")) {
						setStyle("-fx-text-fill: #C3F19A;");
					}
					else if (item.equals("СуммаСЧеков=AMOUNTWITHCHECKS")) {
						setStyle("-fx-text-fill: #B2EFD1;");
					}
					else if (item.equals("Услуга=SERVICE")) {
						setStyle("-fx-text-fill: #ACCCE8;");
					}
					else if (item.equals("statusabs")) {
						setStyle("-fx-text-fill: #CCC7DD;");
					}
					else
					{
						setStyle("");
					}
				}
			}
		});
	}
	
	@FXML
	public void view_attr(ActionEvent event) throws IOException {
		try {
			FileChooser fileChooser = new FileChooser();
			System.setProperty("javax.xml.transform.TransformerFactory",
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
			// Set extension filter for text files
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xls");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setInitialFileName("Транзакция "+Connect.PNMB_);
			// Show save file dialog
			File file = fileChooser.showSaveDialog(null);
			
			if (file != null) {
				Workbook workbook = new HSSFWorkbook();
				Sheet spreadsheet = workbook.createSheet("Таблица");

				Row row = spreadsheet.createRow(0);

				for (int j = 0; j < trans_table.getColumns().size(); j++) {
					row.createCell(j).setCellValue(trans_table.getColumns().get(j).getText());
				}

				for (int i = 0; i < trans_table.getItems().size(); i++) {
					row = spreadsheet.createRow(i + 1);
					for (int j = 0; j < trans_table.getColumns().size(); j++) {
						if (trans_table.getColumns().get(j).getText() == ""){
							
						}
						if (trans_table.getColumns().get(j).getCellData(i) != null) {
							row.createCell(j).setCellValue(trans_table.getColumns().get(j).getCellData(i).toString());
						} else {
							row.createCell(j).setCellValue("");
						}
					}
				}
				workbook.write(new FileOutputStream(file.getPath()));
				workbook.close();
				Msg.Message("Файл сформирован в папку "+file.getPath());
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}


	@FXML
	private void view_attr_(ActionEvent actionEvent) {
		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			Msg.Message("Выберите сначала данные из таблицы!\n");
		} else {
			Msg.Message("Не реализовано!");
		}
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

	// Заполнить таблицу
	private void populate_attr(ObservableList<Unpiv> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}
}
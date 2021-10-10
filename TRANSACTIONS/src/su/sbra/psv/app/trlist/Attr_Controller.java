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
import su.sbra.psv.app.model.Deal;
import su.sbra.psv.app.model.FN_SESS_AMRA;
import su.sbra.psv.app.model.TerminalDAO;
import su.sbra.psv.app.model.Transact;
import su.sbra.psv.app.model.TransactClass;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.sverka.AMRA_STMT_CALC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
 */

@SuppressWarnings("unused")
public class Attr_Controller {

	@FXML
	private TableColumn<Attributes, String> AttributeValue;
	@FXML
	private TableColumn<Attributes, String> Service;
	@FXML
	private TableView<Attributes> trans_table;
	@FXML
	private TableColumn<Attributes, String> AttributeName;
	@FXML
	private TableColumn<Attributes, String> CheckNumber;
	@FXML
	private TextField summ;
	@FXML
	private TextField counts;

	// For MultiThreading
	private Executor exec;

	@FXML
	private void initialize() throws ClassNotFoundException, UnknownHostException {
		trans_table.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		Service.setCellValueFactory(cellData -> cellData.getValue().ServiceProperty());
		AttributeName.setCellValueFactory(cellData -> cellData.getValue().AttributeNameProperty());
		CheckNumber.setCellValueFactory(cellData -> cellData.getValue().CheckNumberProperty());
		AttributeValue.setCellValueFactory(cellData -> cellData.getValue().AttributeValueProperty());

		Service.setCellFactory(TextFieldTableCell.forTableColumn());
		AttributeName.setCellFactory(TextFieldTableCell.forTableColumn());
		CheckNumber.setCellFactory(TextFieldTableCell.forTableColumn());
		AttributeValue.setCellFactory(TextFieldTableCell.forTableColumn());

		Service.setOnEditCommit(new EventHandler<CellEditEvent<Attributes, String>>() {
			@Override
			public void handle(CellEditEvent<Attributes, String> t) {
				((Attributes) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_Service(t.getNewValue());
			}
		});
		AttributeName.setOnEditCommit(new EventHandler<CellEditEvent<Attributes, String>>() {
			@Override
			public void handle(CellEditEvent<Attributes, String> t) {
				((Attributes) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_AttributeName(t.getNewValue());
			}
		});
		CheckNumber.setOnEditCommit(new EventHandler<CellEditEvent<Attributes, String>>() {
			@Override
			public void handle(CellEditEvent<Attributes, String> t) {
				((Attributes) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_CheckNumber(t.getNewValue());
			}
		});
		AttributeValue.setOnEditCommit(new EventHandler<CellEditEvent<Attributes, String>>() {
			@Override
			public void handle(CellEditEvent<Attributes, String> t) {
				((Attributes) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_AttributeValue(t.getNewValue());
			}
		});

		ObservableList<Attributes> empData = TerminalDAO.Attributes_();
		populate_attr(empData);
		autoResizeColumns(trans_table);
	
		TableFilter<Attributes> tableFilter = TableFilter.forTableView(trans_table).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		AttributeName.setCellFactory(col -> new TextFieldTableCell<Attributes, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
					setStyle("");
				} else {
					setText(item.toString());
					if (item.equals("Сумма")) {
						setStyle("-fx-text-fill: rgb(162, 189, 48);");
					} else if (item.equals("Основание")) {
						setStyle("-fx-text-fill: #96C2D2;" );
					} else {
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
			fileChooser.setInitialFileName("Атрибуты " + Connect.PNMB_);
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
						if (trans_table.getColumns().get(j).getText() == "") {

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
				Msg.Message("Файл сформирован в папку " + file.getPath());
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}

	}

	double all_sum = 0;
	int cnt = 0;

	@FXML
	void chk_sum(ActionEvent event) {
		trans_table.getColumns().stream().forEach((column) -> {
			if (column.getText().equals("ЗначениеАтрибута")) {
				for (int i = 0; i < trans_table.getItems().size(); i++) {
					// System.out.println(trans_table.getColumns().get(2).getCellData(i).toString());
					if (column.getCellData(i) != null
							& trans_table.getColumns().get(2).getCellData(i).toString().equals("Сумма")) {
						all_sum = all_sum
								+ Double.valueOf(column.getCellData(i).toString().replace(",", ".").replace(" ", ""));
						cnt++;
					}
				}
			}

		});

		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String format = decimalFormat.format(all_sum);
		counts.setText(String.valueOf(cnt));
		summ.setText(String.valueOf(format));

		all_sum = 0;
		cnt = 0;
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
			if (column.getText().equals("")) {

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
	private void populate_attr(ObservableList<Attributes> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}
}
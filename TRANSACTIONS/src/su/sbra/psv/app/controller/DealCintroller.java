package su.sbra.psv.app.controller;

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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import su.sbra.psv.app.ibank.Ibank2;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Amra_Trans;
import su.sbra.psv.app.model.Attributes;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.Deal;
import su.sbra.psv.app.model.FN_SESS_AMRA;
import su.sbra.psv.app.model.TerminalDAO;
import su.sbra.psv.app.model.Transact;
import su.sbra.psv.app.model.TransactClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class DealCintroller {

	@FXML
	private TableColumn<Deal, Integer> ROWNUMBER;

	@FXML
	private TableColumn<Deal, String> CHEKNUMBER;

	@FXML
	private TableView<Deal> trans_table;

	@FXML
	private TableColumn<Deal, Double> SUMMA;

	@FXML
	private TableColumn<Deal, String> TERMINAL;

	@FXML
	private TableColumn<Deal, LocalDateTime> DATEOPERATION;
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
		ROWNUMBER.setCellValueFactory(cellData -> cellData.getValue().ROWNUMBERProperty().asObject());
		CHEKNUMBER.setCellValueFactory(cellData -> cellData.getValue().CHEKNUMBERProperty());
		SUMMA.setCellValueFactory(cellData -> cellData.getValue().SUMMAProperty().asObject());
		TERMINAL.setCellValueFactory(cellData -> cellData.getValue().TERMINALProperty());
		DATEOPERATION.setCellValueFactory(cellData -> cellData.getValue().DATEOPERATIONProperty());

		ROWNUMBER.setCellFactory(TextFieldTableCell.<Deal, Integer>forTableColumn(new IntegerStringConverter()));
		CHEKNUMBER.setCellFactory(TextFieldTableCell.forTableColumn());
		SUMMA.setCellFactory(TextFieldTableCell.<Deal, Double>forTableColumn(new DoubleStringConverter()));
		TERMINAL.setCellFactory(TextFieldTableCell.forTableColumn());
		DATEOPERATION.setCellFactory(
				TextFieldTableCell.<Deal, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));

		ROWNUMBER.setOnEditCommit(new EventHandler<CellEditEvent<Deal, Integer>>() {
			@Override
			public void handle(CellEditEvent<Deal, Integer> t) {
				((Deal) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_ROWNUMBER(t.getNewValue());
			}
		});
		CHEKNUMBER.setOnEditCommit(new EventHandler<CellEditEvent<Deal, String>>() {
			@Override
			public void handle(CellEditEvent<Deal, String> t) {
				((Deal) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_CHEKNUMBER(t.getNewValue());
			}
		});
		SUMMA.setOnEditCommit(new EventHandler<CellEditEvent<Deal, Double>>() {
			@Override
			public void handle(CellEditEvent<Deal, Double> t) {
				((Deal) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_SUMMA(t.getNewValue());
			}
		});
		TERMINAL.setOnEditCommit(new EventHandler<CellEditEvent<Deal, String>>() {
			@Override
			public void handle(CellEditEvent<Deal, String> t) {
				((Deal) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_TERMINAL(t.getNewValue());
			}
		});

		DATEOPERATION.setOnEditCommit(new EventHandler<CellEditEvent<Deal, LocalDateTime>>() {
			@Override
			public void handle(CellEditEvent<Deal, LocalDateTime> t) {
				((Deal) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_DATEOPERATION(t.getNewValue());
			}
		});

		ObservableList<Deal> empData = TerminalDAO.Deals();
		populate_attr(empData);
		autoResizeColumns(trans_table);
		
		TableFilter<Deal> tableFilter = TableFilter.forTableView(trans_table).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		
		TERMINAL.setCellFactory(col -> new TextFieldTableCell<Deal, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
					if (item.contains("SB")) {
						setStyle("-fx-background-color: rgb(162, 189, 48);" + "-fx-border-color:black;"
								+ " -fx-border-width :  1 1 1 1 ");
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
			fileChooser.setInitialFileName("Сдачи " + Connect.PNMB_);
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
				Alerts("Файл сформирован в папку " + file.getPath());
			}
		} catch (Exception e) {
			Alerts(ExceptionUtils.getStackTrace(e));
		}

	}

	private void Alerts(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	@FXML
	private void view_attr_(ActionEvent actionEvent) {
		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText("Выберите сначала данные из таблицы!\n");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText("Не реализовано!\n");
			alert.showAndWait();

			/*
			 * Attributes fn = trans_table.getSelectionModel().getSelectedItem();
			 * 
			 * Stage stage = new Stage(); Parent root =
			 * FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/view/Attributes.fxml"));
			 * stage.setScene(new Scene(root)); stage.getIcons().add(new Image("icon.png"));
			 * stage.setTitle("Атрибуты"); stage.initModality(Modality.WINDOW_MODAL);
			 * stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
			 * stage.show();
			 */
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
				column.setPrefWidth(max + 10.0d);
			}
		});
	}

	// Заполнить таблицу
	private void populate_attr(ObservableList<Deal> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}
}
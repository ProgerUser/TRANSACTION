package app.controller;

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

import app.Main;
import app.model.Amra_Trans;
import app.model.Attributes;
import app.model.Connect;
import app.model.FN_SESS_AMRA;
import app.model.TerminalDAO;
import app.model.Transact;
import app.model.TransactClass;
import app.model.Unpiv;

import java.util.Date;

/**
 * ���� 04.04.2019.
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
					if (item.equals("������������=DATEOFOPERATION")) {
						setStyle("-fx-background-color: #8B9FFA;"+
					                "-fx-border-color:black;"
								+ " -fx-border-width :  1 1 1 1 ");
					} else if (item.equals("�����=DEALER")) {
						setStyle("-fx-background-color: rgb(162, 189, 48);"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("���������=CHECKNUMBER")) {
						setStyle("-fx-background-color: #E6F06E;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("���������=PROVIDER")) {
						setStyle("-fx-background-color: #D0D1BF;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("������=STATUS")) {
						setStyle("-fx-background-color: #E6B2F6 ;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("�������������=COMMISSIONAMOUNT")) {
						setStyle("-fx-background-color: #83C0F2;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("�������=NKAMOUNT")) {
						setStyle("-fx-background-color: #5EC395;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("�������������=CASHAMOUNT")) {
						setStyle("-fx-background-color: #CAA2E5;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("����������=AMOUNTTOCHECK")) {
						setStyle("-fx-background-color: #EECCAF;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("������������=AMOUNTOFPAYMENT")) {
						setStyle("-fx-background-color: #C3F19A;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("�����������=AMOUNTWITHCHECKS")) {
						setStyle("-fx-background-color: #B2EFD1;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("������=SERVICE")) {
						setStyle("-fx-background-color: #ACCCE8;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("statusabs")) {
						setStyle("-fx-background-color: #CCC7DD;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
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
			fileChooser.setInitialFileName("���������� "+Connect.PNMB_);
			// Show save file dialog
			File file = fileChooser.showSaveDialog(null);
			
			if (file != null) {
				Workbook workbook = new HSSFWorkbook();
				Sheet spreadsheet = workbook.createSheet("�������");

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
				Alerts("���� ����������� � ����� "+file.getPath());
			}
		} catch (Exception e) {
			Alerts(ExceptionUtils.getStackTrace(e));
		}

	}


	private void Alerts(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("��������");
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
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText("�������� ������� ������ �� �������!\n");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText("�� �����������!\n");
			alert.showAndWait();

			/*
			 * Attributes fn = trans_table.getSelectionModel().getSelectedItem();
			 * 
			 * Stage stage = new Stage(); Parent root =
			 * FXMLLoader.load(Main.class.getResource("view/Attributes.fxml"));
			 * stage.setScene(new Scene(root)); stage.getIcons().add(new Image("icon.png"));
			 * stage.setTitle("��������"); stage.initModality(Modality.WINDOW_MODAL);
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

	// ��������� �������
	private void populate_attr(ObservableList<Unpiv> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}
}
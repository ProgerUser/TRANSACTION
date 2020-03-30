package sample.controller;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Transact;
import sample.model.TransactClass;
import sample.model.Unpiv;
import sample.Main;
import sample.model.TerminalDAO;
import sample.model.FN_SESS_AMRA;
import sample.model.Amra_Trans;
import sample.model.Attributes;
import sample.model.Connect;
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

import org.controlsfx.control.table.TableFilter;

import java.util.Date;

/**
 * Саид 04.04.2019.
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
				} else {
					setText(item.toString());  
					if (item.equals("ДатаОперации=DATEOFOPERATION")) {
						setStyle("-fx-background-color: #8B9FFA;"+
					                "-fx-border-color:black;"
								+ " -fx-border-width :  1 1 1 1 ");
					} else if (item.equals("Дилер=DEALER")) {
						setStyle("-fx-background-color: rgb(162, 189, 48);"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("НомерЧека=CHECKNUMBER")) {
						setStyle("-fx-background-color: #E6F06E;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("Провайдер=PROVIDER")) {
						setStyle("-fx-background-color: #D0D1BF;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("Статус=STATUS")) {
						setStyle("-fx-background-color: #E6B2F6 ;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("СуммаКомиссии=COMMISSIONAMOUNT")) {
						setStyle("-fx-background-color: #83C0F2;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("СуммаНК=NKAMOUNT")) {
						setStyle("-fx-background-color: #5EC395;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("СуммаНаличных=CASHAMOUNT")) {
						setStyle("-fx-background-color: #CAA2E5;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("СуммаНаЧек=AMOUNTTOCHECK")) {
						setStyle("-fx-background-color: #EECCAF;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("СуммаПлатежа=AMOUNTOFPAYMENT")) {
						setStyle("-fx-background-color: #C3F19A;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("СуммаСЧеков=AMOUNTWITHCHECKS")) {
						setStyle("-fx-background-color: #B2EFD1;"+ 
					             "-fx-border-color:black;"+ 
								 "-fx-border-width :  1 1 1 1 ");
					}
					else if (item.equals("Услуга=SERVICE")) {
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
	private void view_attr(ActionEvent actionEvent) {
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
			 * FXMLLoader.load(Main.class.getResource("view/Attributes.fxml"));
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
	@FXML
	private void populate_attr(ObservableList<Unpiv> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}
}
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
import sample.Main;
import sample.model.TerminalDAO;
import sample.model.FN_SESS_AMRA;
import sample.model.Amra_Trans;
import sample.model.Attributes;
import sample.model.Connect;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
	private void populate_attr(ObservableList<Attributes> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}
}
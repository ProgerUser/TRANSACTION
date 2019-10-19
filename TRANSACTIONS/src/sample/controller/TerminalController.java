package sample.controller;

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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.model.TerminalClass;
import sample.Main;
import sample.model.ViewerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Саид 04.04.2019.
 */
@SuppressWarnings("unused")
public class TerminalController {

	final static String driverClass = "oracle.jdbc.OracleDriver";
	static String connectionURL = null;
	static String userID = null;
	static String userPassword = null;
	Connection conn = null;
	Statement sqlStatement = null;

	@FXML
	private TableView<TerminalClass> employeeTable;

	@FXML
	private MenuItem loadtransact;

	@FXML
	private MenuItem chekreport;

	@FXML
	private Menu menu;

	@FXML
	private TableColumn<TerminalClass, String> ACC_30232_06;
	@FXML
	private TableColumn<TerminalClass, String> ACCOUNT;
	@FXML
	private TableColumn<TerminalClass, String> ADDRESS;
	@FXML
	private TableColumn<TerminalClass, String> DEPARTMENT;
	@FXML
	private TableColumn<TerminalClass, String> NAME;
	@FXML
	private TableColumn<TerminalClass, String> acc_30232_01;
	@FXML
	private TableColumn<TerminalClass, String> acc_30232_02;
	@FXML
	private TableColumn<TerminalClass, String> acc_30232_03;
	@FXML
	private TableColumn<TerminalClass, String> acc_30232_04;
	@FXML
	private TableColumn<TerminalClass, String> acc_30232_05;
	@FXML
	private TableColumn<TerminalClass, String> acc_70107;

	// For MultiThreading
	private Executor exec;

	@FXML
	private Button report;

	// Initializing the controller class.
	// This method is automatically called after the fxml file has been loaded.

	@FXML
	private void initialize() {
		/*
		 * The setCellValueFactory(...) that we set on the table columns are used to
		 * determine which field inside the Employee objects should be used for the
		 * particular column. The arrow -> indicates that we're using a Java 8 feature
		 * called Lambdas. (Another option would be to use a PropertyValueFactory, but
		 * this is not type-safe
		 * 
		 * We're only using StringProperty values for our table columns in this example.
		 * When you want to use IntegerProperty or DoubleProperty, the
		 * setCellValueFactory(...) must have an additional asObject():
		 */

		// For multithreading: Create executor that uses daemon threads:
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		ACCOUNT.setCellValueFactory(cellData -> cellData.getValue().ACCOUNTProperty());
		ADDRESS.setCellValueFactory(cellData -> cellData.getValue().ADDRESSProperty());
		DEPARTMENT.setCellValueFactory(cellData -> cellData.getValue().DEPARTMENTProperty());
		NAME.setCellValueFactory(cellData -> cellData.getValue().NAMEProperty());
		acc_30232_01.setCellValueFactory(cellData -> cellData.getValue().acc_30232_01Property());
		acc_30232_02.setCellValueFactory(cellData -> cellData.getValue().acc_30232_02Property());
		acc_30232_03.setCellValueFactory(cellData -> cellData.getValue().acc_30232_03Property());
		acc_30232_04.setCellValueFactory(cellData -> cellData.getValue().acc_30232_04Property());
		acc_30232_05.setCellValueFactory(cellData -> cellData.getValue().acc_30232_05Property());
		acc_70107.setCellValueFactory(cellData -> cellData.getValue().acc_70107Property());
		ACC_30232_06.setCellValueFactory(cellData -> cellData.getValue().ACC_30232_06Property());

	}

	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("11ACC_30232_06")) {

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
	private void searchTerminal(ActionEvent actionEvent) {
		ObservableList<TerminalClass> empData = ViewerDAO.searchTerminal();
		populateTerminal(empData);
		autoResizeColumns(employeeTable);
	}

	@FXML
	private BorderPane bp;
	@FXML
	private AnchorPane ap;

	@FXML
	private void Delete(ActionEvent actionEvent_) {
		if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText("Выберите сначала данные из таблицы!\n");
			alert.showAndWait();
		} else {
			TerminalClass tr = employeeTable.getSelectionModel().getSelectedItem();
			Stage stage = (Stage) ap.getScene().getWindow();
			Label alert = new Label("Вы уверены?");
			alert.setLayoutX(75.0);
			alert.setLayoutY(11.0);
			alert.setPrefHeight(17.0);

			Button no = new Button();
			no.setText("Нет");
			no.setLayoutX(111.0);
			no.setLayoutY(56.0);
			no.setPrefWidth(72.0);
			no.setPrefHeight(21.0);

			Button yes = new Button();
			yes.setText("Да");
			yes.setLayoutX(14.0);
			yes.setLayoutY(56.0);
			yes.setPrefWidth(72.0);
			yes.setPrefHeight(21.0);

			AnchorPane yn = new AnchorPane();
			yn.getChildren().add(alert);
			yn.getChildren().add(no);
			yn.getChildren().add(yes);
			Scene ynScene = new Scene(yn, 250, 100);
			Stage newWindow_yn = new Stage();
			no.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					newWindow_yn.close();
				}
			});
			yes.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					ViewerDAO.deleteTerminal(tr.getNAME());
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Терминал: " + tr.getNAME() + " удален!\n");
					alert.showAndWait();
					ObservableList<TerminalClass> empData = ViewerDAO.searchTerminal();
					populateTerminal(empData);
					newWindow_yn.close();
				}
			});
			newWindow_yn.setTitle("Внимание");
			newWindow_yn.setScene(ynScene);
			// Specifies the modality for new window.
			newWindow_yn.initModality(Modality.WINDOW_MODAL);
			// Specifies the owner Window (parent) for new window
			newWindow_yn.initOwner(stage);
			newWindow_yn.getIcons().add(new Image("icon.png"));
			newWindow_yn.show();
		}
	}

	@FXML
	private void UpdateTerminal(ActionEvent actionEvent_) {
		if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert_ = new Alert(Alert.AlertType.INFORMATION);
			Stage stage_ = (Stage) alert_.getDialogPane().getScene().getWindow();
			stage_.getIcons().add(new Image("terminal.png"));
			alert_.setTitle("Внимание");
			alert_.setHeaderText(null);
			alert_.setContentText("Выберите сначала данные из таблицы!\n");
			alert_.showAndWait();
		} else {
			TerminalClass tr = employeeTable.getSelectionModel().getSelectedItem();
			Stage stage = (Stage) ap.getScene().getWindow();
			Label NAME = new Label("Название терминала:");
			NAME.setLayoutX(18.0);
			NAME.setLayoutY(28.0);
			Label DEPARTMENT = new Label("Отделение:");
			DEPARTMENT.setLayoutX(73.0);
			DEPARTMENT.setLayoutY(64.0);
			Label ADDRESS = new Label("Адрес:");
			ADDRESS.setLayoutX(100.0);
			ADDRESS.setLayoutY(100.0);
			Label ACCOUNT = new Label("Счет:");
			ACCOUNT.setLayoutX(107.0);
			ACCOUNT.setLayoutY(139.0);
			Label acc_30232_01 = new Label("acc_30232_01:");
			acc_30232_01.setLayoutX(60.0);
			acc_30232_01.setLayoutY(174.0);
			Label acc_30232_02 = new Label("acc_30232_02:");
			acc_30232_02.setLayoutX(60.0);
			acc_30232_02.setLayoutY(210.0);
			Label acc_30232_03 = new Label("acc_30232_03:");
			acc_30232_03.setLayoutX(60.0);
			acc_30232_03.setLayoutY(250.0);
			Label acc_30232_04 = new Label("acc_30232_04:");
			acc_30232_04.setLayoutX(60.0);
			acc_30232_04.setLayoutY(289.0);
			Label acc_30232_05 = new Label("acc_30232_05:");
			acc_30232_05.setLayoutX(60.0);
			acc_30232_05.setLayoutY(328.0);

			Label acc_701071 = new Label("acc_701071:");
			acc_701071.setLayoutX(72.0);
			acc_701071.setLayoutY(366.0);

			Label ACC_30232_06 = new Label("ACC_30232_06:");
			ACC_30232_06.setLayoutX(55.0);
			ACC_30232_06.setLayoutY(404.0);

			TextField NAME_T = new TextField();
			NAME_T.setPrefHeight(28.0);
			NAME_T.setPrefWidth(198.0);
			NAME_T.setLayoutX(150.0);
			NAME_T.setLayoutY(28.0);
			NAME_T.setText(tr.getNAME());
			TextField DEPARTMENT_T = new TextField();
			DEPARTMENT_T.setPrefHeight(28.0);
			DEPARTMENT_T.setPrefWidth(100.0);
			DEPARTMENT_T.setLayoutX(150.0);
			DEPARTMENT_T.setLayoutY(66.0);
			DEPARTMENT_T.setText(tr.getDEPARTMENT());
			TextField ADDRESS_T = new TextField();
			ADDRESS_T.setPrefHeight(28.0);
			ADDRESS_T.setPrefWidth(274.0);
			ADDRESS_T.setLayoutX(150.0);
			ADDRESS_T.setLayoutY(102.0);
			ADDRESS_T.setText(tr.getADDRESS());
			TextField ACCOUNT_T = new TextField();
			ACCOUNT_T.setPrefHeight(28.0);
			ACCOUNT_T.setPrefWidth(198.0);
			ACCOUNT_T.setLayoutX(150.0);
			ACCOUNT_T.setLayoutY(139.0);
			ACCOUNT_T.setText(tr.getACCOUNT());
			TextField acc_30232_01_T = new TextField();
			acc_30232_01_T.setPrefHeight(28.0);
			acc_30232_01_T.setPrefWidth(198.0);
			acc_30232_01_T.setLayoutX(150.0);
			acc_30232_01_T.setLayoutY(176.0);
			acc_30232_01_T.setText(tr.getacc_30232_01());
			TextField acc_30232_02_T = new TextField();
			acc_30232_02_T.setPrefHeight(28.0);
			acc_30232_02_T.setPrefWidth(198.0);
			acc_30232_02_T.setLayoutX(150.0);
			acc_30232_02_T.setLayoutY(212.0);
			acc_30232_02_T.setText(tr.getacc_30232_02());
			TextField acc_30232_03_T = new TextField();
			acc_30232_03_T.setPrefHeight(28.0);
			acc_30232_03_T.setPrefWidth(198.0);
			acc_30232_03_T.setLayoutX(150.0);
			acc_30232_03_T.setLayoutY(252.0);
			acc_30232_03_T.setText(tr.getacc_30232_03());
			TextField acc_30232_04_T = new TextField();
			acc_30232_04_T.setPrefHeight(28.0);
			acc_30232_04_T.setPrefWidth(198.0);
			acc_30232_04_T.setLayoutX(150.0);
			acc_30232_04_T.setLayoutY(291.0);
			acc_30232_04_T.setText(tr.getacc_30232_04());
			TextField acc_30232_05_T = new TextField();
			acc_30232_05_T.setPrefHeight(28.0);
			acc_30232_05_T.setPrefWidth(198.0);
			acc_30232_05_T.setLayoutX(150.0);
			acc_30232_05_T.setLayoutY(330.0);
			acc_30232_05_T.setText(tr.getacc_30232_05());
			TextField acc_701071_T = new TextField();
			acc_701071_T.setPrefHeight(28.0);
			acc_701071_T.setPrefWidth(198.0);
			acc_701071_T.setLayoutX(150.0);
			acc_701071_T.setLayoutY(368.0);
			acc_701071_T.setText(tr.getacc_70107());

			TextField ACC_30232_06_T = new TextField();
			ACC_30232_06_T.setPrefHeight(28.0);
			ACC_30232_06_T.setPrefWidth(198.0);
			ACC_30232_06_T.setLayoutX(150.0);
			ACC_30232_06_T.setLayoutY(404.0);
			ACC_30232_06_T.setText(tr.getACC_30232_06());

			Button Update = new Button();
			Update.setText("Обновить");
			Update.setLayoutX(80.0);
			Update.setLayoutY(460.0);
			AnchorPane secondaryLayout = new AnchorPane();
			secondaryLayout.getChildren().add(NAME);
			secondaryLayout.getChildren().add(DEPARTMENT);
			secondaryLayout.getChildren().add(ADDRESS);
			secondaryLayout.getChildren().add(ACCOUNT);
			secondaryLayout.getChildren().add(acc_30232_01);
			secondaryLayout.getChildren().add(acc_30232_02);
			secondaryLayout.getChildren().add(acc_30232_03);
			secondaryLayout.getChildren().add(acc_30232_04);
			secondaryLayout.getChildren().add(acc_30232_05);
			secondaryLayout.getChildren().add(acc_701071);
			secondaryLayout.getChildren().add(ACC_30232_06);

			secondaryLayout.getChildren().add(NAME_T);
			secondaryLayout.getChildren().add(DEPARTMENT_T);
			secondaryLayout.getChildren().add(ADDRESS_T);
			secondaryLayout.getChildren().add(ACCOUNT_T);
			secondaryLayout.getChildren().add(acc_30232_01_T);
			secondaryLayout.getChildren().add(acc_30232_02_T);
			secondaryLayout.getChildren().add(acc_30232_03_T);
			secondaryLayout.getChildren().add(acc_30232_04_T);
			secondaryLayout.getChildren().add(acc_30232_05_T);
			secondaryLayout.getChildren().add(acc_701071_T);
			secondaryLayout.getChildren().add(ACC_30232_06_T);
			secondaryLayout.getChildren().add(Update);

			Scene secondScene = new Scene(secondaryLayout, 499, 500);
			// New window (Stage)

			Stage newWindow = new Stage();
			Update.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					Label alert = new Label("Вы уверены?");
					alert.setLayoutX(75.0);
					alert.setLayoutY(11.0);
					alert.setPrefHeight(17.0);
					Button no = new Button();
					no.setText("Нет");
					no.setLayoutX(111.0);
					no.setLayoutY(56.0);
					no.setPrefWidth(72.0);
					no.setPrefHeight(21.0);

					Button yes = new Button();
					yes.setText("Да");
					yes.setLayoutX(14.0);
					yes.setLayoutY(56.0);
					yes.setPrefWidth(72.0);
					yes.setPrefHeight(21.0);
					AnchorPane yn = new AnchorPane();
					yn.getChildren().add(alert);
					yn.getChildren().add(no);
					yn.getChildren().add(yes);
					Scene ynScene = new Scene(yn, 250, 100);
					Stage newWindow_yn = new Stage();
					no.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent event) {
							newWindow_yn.close();
						}
					});
					yes.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent event) {
							ViewerDAO.updateTerminal(NAME_T.getText(), ACCOUNT_T.getText(), DEPARTMENT_T.getText(),
									ADDRESS_T.getText(), acc_30232_01_T.getText(), acc_30232_02_T.getText(),
									acc_30232_03_T.getText(), acc_30232_04_T.getText(), acc_30232_05_T.getText(),
									acc_701071_T.getText(), tr.getNAME(), ACC_30232_06_T.getText());

							Alert alert_ = new Alert(Alert.AlertType.INFORMATION);
							Stage stage_ = (Stage) alert_.getDialogPane().getScene().getWindow();
							stage_.getIcons().add(new Image("terminal.png"));
							alert_.setTitle("Внимание");
							alert_.setHeaderText(null);
							alert_.setContentText("Данные терминала: " + tr.getNAME() + " обновлены!\n");
							alert_.showAndWait();
							ObservableList<TerminalClass> empData = ViewerDAO.searchTerminal();
							populateTerminal(empData);
							newWindow_yn.close();
						}
					});
					newWindow_yn.setTitle("Внимание");
					newWindow_yn.setScene(ynScene);
					// Specifies the modality for new window.
					newWindow_yn.initModality(Modality.WINDOW_MODAL);
					// Specifies the owner Window (parent) for new window
					newWindow_yn.initOwner(newWindow);
					newWindow_yn.getIcons().add(new Image("icon.png"));
					newWindow_yn.show();
				}
			});
			newWindow.setTitle("Update Terminal");
			newWindow.setScene(secondScene);
			// Specifies the modality for new window.
			newWindow.initModality(Modality.WINDOW_MODAL);
			// Specifies the owner Window (parent) for new window
			newWindow.initOwner(stage);
			newWindow.getIcons().add(new Image("icon.png"));
			newWindow.show();

		}
	}

	@FXML
	private void add(ActionEvent actionEvent_) {
		TerminalClass tr = employeeTable.getSelectionModel().getSelectedItem();
		Stage stage = (Stage) ap.getScene().getWindow();
		Label NAME = new Label("Название терминала:");
		NAME.setLayoutX(18.0);
		NAME.setLayoutY(28.0);
		Label DEPARTMENT = new Label("Отделение:");
		DEPARTMENT.setLayoutX(73.0);
		DEPARTMENT.setLayoutY(64.0);
		Label ADDRESS = new Label("Адрес:");
		ADDRESS.setLayoutX(100.0);
		ADDRESS.setLayoutY(100.0);
		Label ACCOUNT = new Label("Счет:");
		ACCOUNT.setLayoutX(107.0);
		ACCOUNT.setLayoutY(139.0);
		Label acc_30232_01 = new Label("acc_30232_01:");
		acc_30232_01.setLayoutX(60.0);
		acc_30232_01.setLayoutY(174.0);
		Label acc_30232_02 = new Label("acc_30232_02:");
		acc_30232_02.setLayoutX(60.0);
		acc_30232_02.setLayoutY(210.0);
		Label acc_30232_03 = new Label("acc_30232_03:");
		acc_30232_03.setLayoutX(60.0);
		acc_30232_03.setLayoutY(250.0);
		Label acc_30232_04 = new Label("acc_30232_04:");
		acc_30232_04.setLayoutX(60.0);
		acc_30232_04.setLayoutY(289.0);
		Label acc_30232_05 = new Label("acc_30232_05:");
		acc_30232_05.setLayoutX(60.0);
		acc_30232_05.setLayoutY(328.0);
		Label acc_701071 = new Label("acc_701071:");
		acc_701071.setLayoutX(72.0);
		acc_701071.setLayoutY(366.0);

		Label ACC_30232_06 = new Label("ACC_30232_06:");
		ACC_30232_06.setLayoutX(55.0);
		ACC_30232_06.setLayoutY(404.0);

		TextField NAME_T = new TextField();
		NAME_T.setPrefHeight(28.0);
		NAME_T.setPrefWidth(198.0);
		NAME_T.setLayoutX(150.0);
		NAME_T.setLayoutY(28.0);
		NAME_T.setPromptText("Обязательно!");
		// NAME_T.setText(tr.getNAME());
		TextField DEPARTMENT_T = new TextField();
		DEPARTMENT_T.setPrefHeight(28.0);
		DEPARTMENT_T.setPrefWidth(100.0);
		DEPARTMENT_T.setLayoutX(150.0);
		DEPARTMENT_T.setLayoutY(66.0);
		DEPARTMENT_T.setPromptText("Обязательно!");
		// DEPARTMENT_T.setText(tr.getDEPARTMENT());
		TextField ADDRESS_T = new TextField();
		ADDRESS_T.setPrefHeight(28.0);
		ADDRESS_T.setPrefWidth(274.0);
		ADDRESS_T.setLayoutX(150.0);
		ADDRESS_T.setLayoutY(102.0);
		ADDRESS_T.setPromptText("Обязательно!");
		// ADDRESS_T.setText(tr.getADDRESS());
		TextField ACCOUNT_T = new TextField();
		ACCOUNT_T.setPrefHeight(28.0);
		ACCOUNT_T.setPrefWidth(198.0);
		ACCOUNT_T.setLayoutX(150.0);
		ACCOUNT_T.setLayoutY(139.0);
		ACCOUNT_T.setPromptText("Обязательно!");
		// ACCOUNT_T.setText(tr.getACCOUNT());
		TextField acc_30232_01_T = new TextField();
		acc_30232_01_T.setPrefHeight(28.0);
		acc_30232_01_T.setPrefWidth(198.0);
		acc_30232_01_T.setLayoutX(150.0);
		acc_30232_01_T.setLayoutY(176.0);
		acc_30232_01_T.setPromptText("Обязательно!");
		// acc_30232_01_T.setText(tr.getacc_30232_01());
		TextField acc_30232_02_T = new TextField();
		acc_30232_02_T.setPrefHeight(28.0);
		acc_30232_02_T.setPrefWidth(198.0);
		acc_30232_02_T.setLayoutX(150.0);
		acc_30232_02_T.setLayoutY(212.0);
		acc_30232_02_T.setPromptText("Обязательно!");
		// acc_30232_02_T.setText(tr.getacc_30232_02());
		TextField acc_30232_03_T = new TextField();
		acc_30232_03_T.setPrefHeight(28.0);
		acc_30232_03_T.setPrefWidth(198.0);
		acc_30232_03_T.setLayoutX(150.0);
		acc_30232_03_T.setLayoutY(252.0);
		acc_30232_03_T.setPromptText("Обязательно!");
		// acc_30232_03_T.setText(tr.getacc_30232_03());
		TextField acc_30232_04_T = new TextField();
		acc_30232_04_T.setPrefHeight(28.0);
		acc_30232_04_T.setPrefWidth(198.0);
		acc_30232_04_T.setLayoutX(150.0);
		acc_30232_04_T.setLayoutY(291.0);
		acc_30232_04_T.setPromptText("Не обязательно!");
		// acc_30232_04_T.setText(tr.getacc_30232_04());
		TextField acc_30232_05_T = new TextField();
		acc_30232_05_T.setPrefHeight(28.0);
		acc_30232_05_T.setPrefWidth(198.0);
		acc_30232_05_T.setLayoutX(150.0);
		acc_30232_05_T.setLayoutY(330.0);
		acc_30232_05_T.setPromptText("Обязательно!");
		// acc_30232_05_T.setText(tr.getacc_30232_05());
		TextField acc_701071_T = new TextField();
		acc_701071_T.setPrefHeight(28.0);
		acc_701071_T.setPrefWidth(198.0);
		acc_701071_T.setLayoutX(150.0);
		acc_701071_T.setLayoutY(368.0);
		acc_701071_T.setPromptText("Обязательно!");
		// acc_701071_T.setText(tr.getacc_70107());

		TextField ACC_30232_06_T = new TextField();
		ACC_30232_06_T.setPrefHeight(28.0);
		ACC_30232_06_T.setPrefWidth(198.0);
		ACC_30232_06_T.setLayoutX(150.0);
		ACC_30232_06_T.setLayoutY(404.0);
		// ACC_30232_06_T.setText(tr.getACC_30232_06());
		ACC_30232_06_T.setPromptText("Обязательно!");

		Button Add = new Button();
		Add.setText("Добавить");
		Add.setLayoutX(80.0);
		Add.setLayoutY(460.0);

		AnchorPane secondaryLayout = new AnchorPane();
		secondaryLayout.getChildren().add(NAME);
		secondaryLayout.getChildren().add(DEPARTMENT);
		secondaryLayout.getChildren().add(ADDRESS);
		secondaryLayout.getChildren().add(ACCOUNT);
		secondaryLayout.getChildren().add(acc_30232_01);
		secondaryLayout.getChildren().add(acc_30232_02);
		secondaryLayout.getChildren().add(acc_30232_03);
		secondaryLayout.getChildren().add(acc_30232_04);
		secondaryLayout.getChildren().add(acc_30232_05);
		secondaryLayout.getChildren().add(acc_701071);
		secondaryLayout.getChildren().add(ACC_30232_06);

		secondaryLayout.getChildren().add(NAME_T);
		secondaryLayout.getChildren().add(DEPARTMENT_T);
		secondaryLayout.getChildren().add(ADDRESS_T);
		secondaryLayout.getChildren().add(ACCOUNT_T);
		secondaryLayout.getChildren().add(acc_30232_01_T);
		secondaryLayout.getChildren().add(acc_30232_02_T);
		secondaryLayout.getChildren().add(acc_30232_03_T);
		secondaryLayout.getChildren().add(acc_30232_04_T);
		secondaryLayout.getChildren().add(acc_30232_05_T);
		secondaryLayout.getChildren().add(acc_701071_T);
		secondaryLayout.getChildren().add(ACC_30232_06_T);
		secondaryLayout.getChildren().add(Add);

		Scene secondScene = new Scene(secondaryLayout, 499, 500);
		// New window (Stage)

		Stage newWindow = new Stage();
		Add.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Label alert = new Label("Вы уверены?");
				alert.setLayoutX(75.0);
				alert.setLayoutY(11.0);
				alert.setPrefHeight(17.0);
				Button no = new Button();
				no.setText("Нет");
				no.setLayoutX(111.0);
				no.setLayoutY(56.0);
				no.setPrefWidth(72.0);
				no.setPrefHeight(21.0);

				Button yes = new Button();
				yes.setText("Да");
				yes.setLayoutX(14.0);
				yes.setLayoutY(56.0);
				yes.setPrefWidth(72.0);
				yes.setPrefHeight(21.0);
				AnchorPane yn = new AnchorPane();
				yn.getChildren().add(alert);
				yn.getChildren().add(no);
				yn.getChildren().add(yes);
				Scene ynScene = new Scene(yn, 250, 100);
				Stage newWindow_yn = new Stage();
				no.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						newWindow_yn.close();
					}
				});
				yes.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						ViewerDAO.InsertTerminal(NAME_T.getText(), ACCOUNT_T.getText(), DEPARTMENT_T.getText(),
								ADDRESS_T.getText(), acc_30232_01_T.getText(), acc_30232_02_T.getText(),
								acc_30232_03_T.getText(), acc_30232_04_T.getText(), acc_30232_05_T.getText(),
								acc_701071_T.getText(), ACC_30232_06_T.getText());
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("terminal.png"));
						alert.setTitle("Внимание");
						alert.setHeaderText(null);
						alert.setContentText("Добавлен терминал: " + NAME.getText() + " !\n");
						alert.showAndWait();
						ObservableList<TerminalClass> empData = ViewerDAO.searchTerminal();
						populateTerminal(empData);
						newWindow_yn.close();
					}
				});
				newWindow_yn.setTitle("Внимание");
				newWindow_yn.setScene(ynScene);
				// Specifies the modality for new window.
				newWindow_yn.initModality(Modality.WINDOW_MODAL);
				// Specifies the owner Window (parent) for new window
				newWindow_yn.initOwner(newWindow);
				newWindow_yn.getIcons().add(new Image("icon.png"));
				newWindow_yn.show();
			}
		});
		newWindow.setTitle("Add Terminal");
		newWindow.setScene(secondScene);
		// Specifies the modality for new window.
		newWindow.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow.initOwner(stage);
		newWindow.getIcons().add(new Image("icon.png"));
		newWindow.show();
	}

	private void populateTerminal(ObservableList<TerminalClass> trData) {
		employeeTable.setItems(trData);
	}
}

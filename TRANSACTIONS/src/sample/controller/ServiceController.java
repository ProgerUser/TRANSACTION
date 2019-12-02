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
import javafx.scene.control.ComboBox;
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
import net.sf.jasperreports.engine.JRException;
import sample.Main;
import sample.model.Connect;
import sample.model.ServiceClass;
import sample.model.TerminalForCombo;
import sample.model.TransactClass;
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
public class ServiceController {

	final static String driverClass = "oracle.jdbc.OracleDriver";
	static String connectionURL = null;
	static String userID = null;
	static String userPassword = null;
	Connection conn = null;
	Statement sqlStatement = null;

	@FXML
	private TableView<ServiceClass> employeeTable;

	@FXML
	private ComboBox<String> terms;

	@FXML
	private MenuItem loadtransact;

	@FXML
	private MenuItem chekreport;

	@FXML
	private Menu menu;

	@FXML
	private TableColumn<ServiceClass, String> acc_name;
	@FXML
	private TableColumn<ServiceClass, String> acc_rec;
	@FXML
	private TableColumn<ServiceClass, String> account;
	@FXML
	private TableColumn<ServiceClass, String> account2;
	@FXML
	private TableColumn<ServiceClass, String> account3;
	@FXML
	private TableColumn<ServiceClass, String> account4;
	@FXML
	private TableColumn<ServiceClass, String> account5;
	@FXML
	private TableColumn<ServiceClass, String> idterm;
	@FXML
	private TableColumn<ServiceClass, String> inn;
	@FXML
	private TableColumn<ServiceClass, String> kbk;
	@FXML
	private TableColumn<ServiceClass, String> kor_bank_nbra;
	@FXML
	private TableColumn<ServiceClass, String> kpp;
	@FXML
	private TableColumn<ServiceClass, String> name;
	@FXML
	private TableColumn<ServiceClass, String> okato;
	@FXML
	private TableColumn<ServiceClass, String> bo1;
	@FXML
	private TableColumn<ServiceClass, String> bo2;
	@FXML
	private TableColumn<ServiceClass, String> stat;
	@FXML
	private TableColumn<ServiceClass, String> comission;
	// For MultiThreading
	private Executor exec;

	@FXML
	private Button report;

	// Initializing the controller class.
	// This method is automatically called after the fxml file has been loaded.

	@FXML
	private void initialize() {
		try {
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
			acc_name.setCellValueFactory(cellData -> cellData.getValue().acc_nameProperty());
			acc_rec.setCellValueFactory(cellData -> cellData.getValue().acc_recProperty());
			account.setCellValueFactory(cellData -> cellData.getValue().accountProperty());
			account2.setCellValueFactory(cellData -> cellData.getValue().account2Property());
			account3.setCellValueFactory(cellData -> cellData.getValue().account3Property());
			account4.setCellValueFactory(cellData -> cellData.getValue().account4Property());
			account5.setCellValueFactory(cellData -> cellData.getValue().account5Property());
			idterm.setCellValueFactory(cellData -> cellData.getValue().idtermProperty());
			inn.setCellValueFactory(cellData -> cellData.getValue().innProperty());
			kbk.setCellValueFactory(cellData -> cellData.getValue().kbkProperty());
			kor_bank_nbra.setCellValueFactory(cellData -> cellData.getValue().kor_bank_nbraProperty());
			kpp.setCellValueFactory(cellData -> cellData.getValue().kppProperty());
			name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			okato.setCellValueFactory(cellData -> cellData.getValue().okatoProperty());
			bo1.setCellValueFactory(cellData -> cellData.getValue().bo1Property());
			bo2.setCellValueFactory(cellData -> cellData.getValue().bo2Property());
			stat.setCellValueFactory(cellData -> cellData.getValue().statProperty());
			comission.setCellValueFactory(cellData -> cellData.getValue().comissionProperty());
			
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select NAME from Z_SB_TERMINAL_DBT t";
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			ObservableList<String> combolist = FXCollections.observableArrayList();
			while (rs.next()) {
				TerminalForCombo tr = new TerminalForCombo();
				tr.setTERMS(rs.getString("NAME"));
				// System.out.println(tr.getTERMS());
				combolist.add(rs.getString("NAME"));
			}
			terms.setItems(combolist);
			terms.getSelectionModel().select(0);
			// System.out.println(terms.getValue().toString());
			rs.close();

		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("bo2")) {

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
	private void searchService(ActionEvent actionEvent) {
		ObservableList<ServiceClass> empData = ViewerDAO.searchService(terms.getValue().toString());
		populateService(empData);
		autoResizeColumns(employeeTable);
	}

	@FXML
	private BorderPane ap;

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
			ServiceClass tr = employeeTable.getSelectionModel().getSelectedItem();
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
					ViewerDAO.deleteService(tr.getidterm(), tr.getaccount(), tr.getname());
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Сервис: " + tr.getname() + " удален!\n");
					alert.showAndWait();
					ObservableList<ServiceClass> empData = ViewerDAO.searchService(terms.getValue().toString());
					populateService(empData);
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
	private void UpdateService(ActionEvent actionEvent_) {
		if (employeeTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert_ = new Alert(Alert.AlertType.INFORMATION);
			Stage stage_ = (Stage) alert_.getDialogPane().getScene().getWindow();
			stage_.getIcons().add(new Image("terminal.png"));
			alert_.setTitle("Внимание");
			alert_.setHeaderText(null);
			alert_.setContentText("Выберите сначала данные из таблицы!\n");
			alert_.showAndWait();
		} else {
			ServiceClass tr = employeeTable.getSelectionModel().getSelectedItem();
			Stage stage = (Stage) ap.getScene().getWindow();
			Label name = new Label("Название Сервиса:");
			name.setLayoutX(32.0);
			name.setLayoutY(28.0);
			Label idterm = new Label("Отделение:");
			idterm.setLayoutX(74.0);
			idterm.setLayoutY(66.0);
			Label account = new Label("Счет:");
			account.setLayoutX(108.0);
			account.setLayoutY(102.0);
			Label account2 = new Label("account2:");
			account2.setLayoutX(86.0);
			account2.setLayoutY(139.0);
			Label account3 = new Label("account3:");
			account3.setLayoutX(86.0);
			account3.setLayoutY(176.0);
			Label account4 = new Label("account4:");
			account4.setLayoutX(86.0);
			account4.setLayoutY(212.0);
			Label account5 = new Label("account5:");
			account5.setLayoutX(86.0);
			account5.setLayoutY(252.0);
			Label inn = new Label("inn:");
			inn.setLayoutX(117);
			inn.setLayoutY(291.0);
			Label kpp = new Label("kpp:");
			kpp.setLayoutX(114.0);
			kpp.setLayoutY(330.0);
			Label kor_bank_nbra = new Label("kor_bank_nbra:");
			kor_bank_nbra.setLayoutX(57.0);
			kor_bank_nbra.setLayoutY(368.0);
			Label acc_rec = new Label("acc_rec:");
			acc_rec.setLayoutX(96.0);
			acc_rec.setLayoutY(405.0);
			Label kbk = new Label("kbk:");
			kbk.setLayoutX(116.0);
			kbk.setLayoutY(442.0);
			Label okato = new Label("okato:");
			okato.setLayoutX(104.0);
			okato.setLayoutY(478.0);
			Label stat = new Label("stat:");
			stat.setLayoutX(116.0);
			stat.setLayoutY(518.0);
			Label acc_name = new Label("acc_name:");
			acc_name.setLayoutX(82.0);
			acc_name.setLayoutY(557.0);
			Label bo1 = new Label("bo1:");
			bo1.setLayoutX(114.0);
			bo1.setLayoutY(596.0);
			Label bo2 = new Label("bo2:");
			bo2.setLayoutX(112.0);
			bo2.setLayoutY(634.0);
			Label comission_ = new Label("Комисиия:");
			comission_.setLayoutX(82.0);
			comission_.setLayoutY(672.0);

			TextField name_T = new TextField();
			name_T.setPrefHeight(28.0);
			name_T.setPrefWidth(274.0);
			name_T.setLayoutX(150.0);
			name_T.setLayoutY(28.0);
			name_T.setText(tr.getname());
			TextField idterm_T = new TextField();
			idterm_T.setPrefHeight(28.0);
			idterm_T.setPrefWidth(100.0);
			idterm_T.setLayoutX(150.0);
			idterm_T.setLayoutY(66.0);
			idterm_T.setText(tr.getidterm());
			TextField account_T = new TextField();
			account_T.setPrefHeight(28.0);
			account_T.setPrefWidth(274.0);
			account_T.setLayoutX(150.0);
			account_T.setLayoutY(102.0);
			account_T.setText(tr.getaccount());
			TextField account2_T = new TextField();
			account2_T.setPrefHeight(28.0);
			account2_T.setPrefWidth(198.0);
			account2_T.setLayoutX(150.0);
			account2_T.setLayoutY(139.0);
			account2_T.setText(tr.getaccount2());
			TextField account3_T = new TextField();
			account3_T.setPrefHeight(28.0);
			account3_T.setPrefWidth(198.0);
			account3_T.setLayoutX(150.0);
			account3_T.setLayoutY(176.0);
			account3_T.setText(tr.getaccount3());
			TextField account4_T = new TextField();
			account4_T.setPrefHeight(28.0);
			account4_T.setPrefWidth(198.0);
			account4_T.setLayoutX(150.0);
			account4_T.setLayoutY(212.0);
			account4_T.setText(tr.getaccount4());
			TextField account5_T = new TextField();
			account5_T.setPrefHeight(28.0);
			account5_T.setPrefWidth(198.0);
			account5_T.setLayoutX(150.0);
			account5_T.setLayoutY(252.0);
			account5_T.setText(tr.getaccount5());
			TextField inn_T = new TextField();
			inn_T.setPrefHeight(28.0);
			inn_T.setPrefWidth(198.0);
			inn_T.setLayoutX(150.0);
			inn_T.setLayoutY(291.0);
			inn_T.setText(tr.getinn());
			TextField kpp_T = new TextField();
			kpp_T.setPrefHeight(28.0);
			kpp_T.setPrefWidth(198.0);
			kpp_T.setLayoutX(150.0);
			kpp_T.setLayoutY(330.0);
			kpp_T.setText(tr.getkpp());
			TextField kor_bank_nbra_T = new TextField();
			kor_bank_nbra_T.setPrefHeight(28.0);
			kor_bank_nbra_T.setPrefWidth(198.0);
			kor_bank_nbra_T.setLayoutX(150.0);
			kor_bank_nbra_T.setLayoutY(368.0);
			kor_bank_nbra_T.setText(tr.getkor_bank_nbra());
			TextField acc_rec_T = new TextField();
			acc_rec_T.setPrefHeight(28.0);
			acc_rec_T.setPrefWidth(198.0);
			acc_rec_T.setLayoutX(150.0);
			acc_rec_T.setLayoutY(405.0);
			acc_rec_T.setText(tr.getacc_rec());
			TextField kbk_T = new TextField();
			kbk_T.setPrefHeight(28.0);
			kbk_T.setPrefWidth(198.0);
			kbk_T.setLayoutX(150.0);
			kbk_T.setLayoutY(442.0);
			kbk_T.setText(tr.getkbk());
			TextField okato_T = new TextField();
			okato_T.setPrefHeight(28.0);
			okato_T.setPrefWidth(198.0);
			okato_T.setLayoutX(150.0);
			okato_T.setLayoutY(478.0);
			okato_T.setText(tr.getokato());
			TextField stat_T = new TextField();
			stat_T.setPrefHeight(28.0);
			stat_T.setPrefWidth(198.0);
			stat_T.setLayoutX(150.0);
			stat_T.setLayoutY(518.0);
			stat_T.setText(tr.getstat());
			TextField acc_name_T = new TextField();
			acc_name_T.setPrefHeight(28.0);
			acc_name_T.setPrefWidth(198.0);
			acc_name_T.setLayoutX(150.0);
			acc_name_T.setLayoutY(557.0);
			acc_name_T.setText(tr.getacc_name());
			TextField bo1_T = new TextField();
			bo1_T.setPrefHeight(28.0);
			bo1_T.setPrefWidth(198.0);
			bo1_T.setLayoutX(150.0);
			bo1_T.setLayoutY(596.0);
			bo1_T.setText(tr.getbo1());
			TextField bo2_T = new TextField();
			bo2_T.setPrefHeight(28.0);
			bo2_T.setPrefWidth(198.0);
			bo2_T.setLayoutX(150.0);
			bo2_T.setLayoutY(634.0);
			bo2_T.setText(tr.getbo2());
			TextField comission_T = new TextField();
			comission_T.setPrefHeight(28.0);
			comission_T.setPrefWidth(198.0);
			comission_T.setLayoutX(150.0);
			comission_T.setLayoutY(672.0);
			comission_T.setText(tr.getcomission());
			Button Update = new Button();
			Update.setText("Обновить");
			Update.setLayoutX(29.0);
			Update.setLayoutY(702.0);
			AnchorPane secondaryLayout = new AnchorPane();

			secondaryLayout.getChildren().add(acc_name);
			secondaryLayout.getChildren().add(acc_rec);
			secondaryLayout.getChildren().add(account);
			secondaryLayout.getChildren().add(account2);
			secondaryLayout.getChildren().add(account3);
			secondaryLayout.getChildren().add(account4);
			secondaryLayout.getChildren().add(account5);
			secondaryLayout.getChildren().add(idterm);
			secondaryLayout.getChildren().add(inn);
			secondaryLayout.getChildren().add(kbk);
			secondaryLayout.getChildren().add(kor_bank_nbra);
			secondaryLayout.getChildren().add(kpp);
			secondaryLayout.getChildren().add(name);
			secondaryLayout.getChildren().add(okato);
			secondaryLayout.getChildren().add(bo1);
			secondaryLayout.getChildren().add(bo2);
			secondaryLayout.getChildren().add(stat);
			secondaryLayout.getChildren().add(acc_name_T);
			secondaryLayout.getChildren().add(acc_rec_T);
			secondaryLayout.getChildren().add(account_T);
			secondaryLayout.getChildren().add(account2_T);
			secondaryLayout.getChildren().add(account3_T);
			secondaryLayout.getChildren().add(account4_T);
			secondaryLayout.getChildren().add(account5_T);
			secondaryLayout.getChildren().add(idterm_T);
			secondaryLayout.getChildren().add(inn_T);
			secondaryLayout.getChildren().add(kbk_T);
			secondaryLayout.getChildren().add(kor_bank_nbra_T);
			secondaryLayout.getChildren().add(kpp_T);
			secondaryLayout.getChildren().add(name_T);
			secondaryLayout.getChildren().add(okato_T);
			secondaryLayout.getChildren().add(bo1_T);
			secondaryLayout.getChildren().add(bo2_T);
			secondaryLayout.getChildren().add(stat_T);
			secondaryLayout.getChildren().add(comission_T);
			secondaryLayout.getChildren().add(comission_);
			
			secondaryLayout.getChildren().add(Update);
			Scene secondScene = new Scene(secondaryLayout, 518, 751);
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
							ViewerDAO.updateService(acc_name_T.getText(), acc_rec_T.getText(), account_T.getText(),
									account2_T.getText(), account3_T.getText(), account4_T.getText(),
									account5_T.getText(), idterm_T.getText(), inn_T.getText(), kbk_T.getText(),
									kor_bank_nbra_T.getText(), kpp_T.getText(), name_T.getText(), okato_T.getText(),
									bo1_T.getText(), bo2_T.getText(), stat_T.getText(), tr.getaccount(),
									tr.getidterm(), tr.getname(),comission_T.getText());
							Alert alert_ = new Alert(Alert.AlertType.INFORMATION);
							Stage stage_ = (Stage) alert_.getDialogPane().getScene().getWindow();
							stage_.getIcons().add(new Image("terminal.png"));
							alert_.setTitle("Внимание");
							alert_.setHeaderText(null);
							alert_.setContentText("Данные сервиса: " + tr.getname() + " обновлены!\n");
							alert_.showAndWait();

							ObservableList<ServiceClass> empData = ViewerDAO
									.searchService(terms.getValue().toString());
							populateService(empData);
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
			newWindow.setTitle("Update Serivce");
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
		ServiceClass tr = employeeTable.getSelectionModel().getSelectedItem();
		Stage stage = (Stage) ap.getScene().getWindow();
		Label name = new Label("Название Сервиса:");
		name.setLayoutX(32.0);
		name.setLayoutY(28.0);
		Label idterm = new Label("Отделение:");
		idterm.setLayoutX(74.0);
		idterm.setLayoutY(66.0);
		Label account = new Label("Счет:");
		account.setLayoutX(108.0);
		account.setLayoutY(102.0);
		Label account2 = new Label("account2:");
		account2.setLayoutX(86.0);
		account2.setLayoutY(139.0);
		Label account3 = new Label("account3:");
		account3.setLayoutX(86.0);
		account3.setLayoutY(176.0);
		Label account4 = new Label("account4:");
		account4.setLayoutX(86.0);
		account4.setLayoutY(212.0);
		Label account5 = new Label("account5:");
		account5.setLayoutX(86.0);
		account5.setLayoutY(252.0);
		Label inn = new Label("inn:");
		inn.setLayoutX(117);
		inn.setLayoutY(291.0);
		Label kpp = new Label("kpp:");
		kpp.setLayoutX(114.0);
		kpp.setLayoutY(330.0);
		Label kor_bank_nbra = new Label("kor_bank_nbra:");
		kor_bank_nbra.setLayoutX(57.0);
		kor_bank_nbra.setLayoutY(368.0);
		Label acc_rec = new Label("acc_rec:");
		acc_rec.setLayoutX(96.0);
		acc_rec.setLayoutY(405.0);
		Label kbk = new Label("kbk:");
		kbk.setLayoutX(116.0);
		kbk.setLayoutY(442.0);
		Label okato = new Label("okato:");
		okato.setLayoutX(104.0);
		okato.setLayoutY(478.0);
		Label stat = new Label("stat:");
		stat.setLayoutX(116.0);
		stat.setLayoutY(518.0);
		Label acc_name = new Label("acc_name:");
		acc_name.setLayoutX(82.0);
		acc_name.setLayoutY(557.0);
		Label bo1 = new Label("bo1:");
		bo1.setLayoutX(114.0);
		bo1.setLayoutY(596.0);
		Label bo2 = new Label("bo2:");
		bo2.setLayoutX(112.0);
		bo2.setLayoutY(634.0);
		Label comission_ = new Label("Комисиия:");
		comission_.setLayoutX(82.0);
		comission_.setLayoutY(672.0);
		
		TextField name_T = new TextField();
		name_T.setPrefHeight(28.0);
		name_T.setPrefWidth(274.0);
		name_T.setLayoutX(150.0);
		name_T.setLayoutY(28.0);
		name_T.setPromptText("Обязательно!");
		TextField idterm_T = new TextField();
		idterm_T.setPrefHeight(28.0);
		idterm_T.setPrefWidth(100.0);
		idterm_T.setLayoutX(150.0);
		idterm_T.setLayoutY(66.0);
		idterm_T.setPromptText("Обязательно!");
		idterm_T.setText(terms.getValue().toString());
		TextField account_T = new TextField();
		account_T.setPrefHeight(28.0);
		account_T.setPrefWidth(274.0);
		account_T.setLayoutX(150.0);
		account_T.setLayoutY(102.0);
		account_T.setPromptText("Обязательно!");
		TextField account2_T = new TextField();
		account2_T.setPrefHeight(28.0);
		account2_T.setPrefWidth(198.0);
		account2_T.setLayoutX(150.0);
		account2_T.setLayoutY(139.0);
		account2_T.setPromptText("Не обязательно!");
		TextField account3_T = new TextField();
		account3_T.setPrefHeight(28.0);
		account3_T.setPrefWidth(198.0);
		account3_T.setLayoutX(150.0);
		account3_T.setLayoutY(176.0);
		account3_T.setPromptText("Не обязательно!");
		TextField account4_T = new TextField();
		account4_T.setPrefHeight(28.0);
		account4_T.setPrefWidth(198.0);
		account4_T.setLayoutX(150.0);
		account4_T.setLayoutY(212.0);
		account4_T.setPromptText("Не обязательно!");
		TextField account5_T = new TextField();
		account5_T.setPrefHeight(28.0);
		account5_T.setPrefWidth(198.0);
		account5_T.setLayoutX(150.0);
		account5_T.setLayoutY(252.0);
		account5_T.setPromptText("Не обязательно!");
		TextField inn_T = new TextField();
		inn_T.setPrefHeight(28.0);
		inn_T.setPrefWidth(198.0);
		inn_T.setLayoutX(150.0);
		inn_T.setLayoutY(291.0);
		inn_T.setPromptText("Обязательно!");
		TextField kpp_T = new TextField();
		kpp_T.setPrefHeight(28.0);
		kpp_T.setPrefWidth(198.0);
		kpp_T.setLayoutX(150.0);
		kpp_T.setLayoutY(330.0);
		kpp_T.setPromptText("Обязательно!");
		TextField kor_bank_nbra_T = new TextField();
		kor_bank_nbra_T.setPrefHeight(28.0);
		kor_bank_nbra_T.setPrefWidth(198.0);
		kor_bank_nbra_T.setLayoutX(150.0);
		kor_bank_nbra_T.setLayoutY(368.0);
		kor_bank_nbra_T.setPromptText("Не обязательно!");
		TextField acc_rec_T = new TextField();
		acc_rec_T.setPrefHeight(28.0);
		acc_rec_T.setPrefWidth(198.0);
		acc_rec_T.setLayoutX(150.0);
		acc_rec_T.setLayoutY(405.0);
		acc_rec_T.setPromptText("Обязательно!");
		TextField kbk_T = new TextField();
		kbk_T.setPrefHeight(28.0);
		kbk_T.setPrefWidth(198.0);
		kbk_T.setLayoutX(150.0);
		kbk_T.setLayoutY(442.0);
		kbk_T.setPromptText("Обязательно!");
		TextField okato_T = new TextField();
		okato_T.setPrefHeight(28.0);
		okato_T.setPrefWidth(198.0);
		okato_T.setLayoutX(150.0);
		okato_T.setLayoutY(478.0);
		okato_T.setPromptText("Обязательно!");
		TextField stat_T = new TextField();
		stat_T.setPrefHeight(28.0);
		stat_T.setPrefWidth(198.0);
		stat_T.setLayoutX(150.0);
		stat_T.setLayoutY(518.0);
		stat_T.setPromptText("Обязательно!");
		TextField acc_name_T = new TextField();
		acc_name_T.setPrefHeight(28.0);
		acc_name_T.setPrefWidth(198.0);
		acc_name_T.setLayoutX(150.0);
		acc_name_T.setLayoutY(557.0);
		acc_name_T.setPromptText("Обязательно!");
		TextField bo1_T = new TextField();
		bo1_T.setPrefHeight(28.0);
		bo1_T.setPrefWidth(198.0);
		bo1_T.setLayoutX(150.0);
		bo1_T.setLayoutY(596.0);
		bo1_T.setPromptText("Обязательно!");
		TextField bo2_T = new TextField();
		bo2_T.setPrefHeight(28.0);
		bo2_T.setPrefWidth(198.0);
		bo2_T.setLayoutX(150.0);
		bo2_T.setLayoutY(634.0);
		bo2_T.setPromptText("Обязательно!");
		TextField comission_T = new TextField();
		comission_T.setPrefHeight(28.0);
		comission_T.setPrefWidth(198.0);
		comission_T.setLayoutX(150.0);
		comission_T.setLayoutY(672.0);
		comission_T.setPromptText("Обязательно!");
		Button add = new Button();
		add.setText("Добавить");
		add.setLayoutX(29.0);
		add.setLayoutY(702.0);
		AnchorPane secondaryLayout = new AnchorPane();

		secondaryLayout.getChildren().add(acc_name);
		secondaryLayout.getChildren().add(acc_rec);
		secondaryLayout.getChildren().add(account);
		secondaryLayout.getChildren().add(account2);
		secondaryLayout.getChildren().add(account3);
		secondaryLayout.getChildren().add(account4);
		secondaryLayout.getChildren().add(account5);
		secondaryLayout.getChildren().add(idterm);
		secondaryLayout.getChildren().add(inn);
		secondaryLayout.getChildren().add(kbk);
		secondaryLayout.getChildren().add(kor_bank_nbra);
		secondaryLayout.getChildren().add(kpp);
		secondaryLayout.getChildren().add(name);
		secondaryLayout.getChildren().add(okato);
		secondaryLayout.getChildren().add(bo1);
		secondaryLayout.getChildren().add(bo2);
		secondaryLayout.getChildren().add(stat);
		secondaryLayout.getChildren().add(acc_name_T);
		secondaryLayout.getChildren().add(acc_rec_T);
		secondaryLayout.getChildren().add(account_T);
		secondaryLayout.getChildren().add(account2_T);
		secondaryLayout.getChildren().add(account3_T);
		secondaryLayout.getChildren().add(account4_T);
		secondaryLayout.getChildren().add(account5_T);
		secondaryLayout.getChildren().add(idterm_T);
		secondaryLayout.getChildren().add(inn_T);
		secondaryLayout.getChildren().add(kbk_T);
		secondaryLayout.getChildren().add(kor_bank_nbra_T);
		secondaryLayout.getChildren().add(kpp_T);
		secondaryLayout.getChildren().add(name_T);
		secondaryLayout.getChildren().add(okato_T);
		secondaryLayout.getChildren().add(bo1_T);
		secondaryLayout.getChildren().add(bo2_T);
		secondaryLayout.getChildren().add(stat_T);
		secondaryLayout.getChildren().add(add);
		secondaryLayout.getChildren().add(comission_T);
		secondaryLayout.getChildren().add(comission_);
		Scene secondScene = new Scene(secondaryLayout, 518, 751);

		// New window (Stage)

		Stage newWindow = new Stage();
		add.setOnAction(new EventHandler<ActionEvent>() {
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
						try {
							ViewerDAO.InsertService(acc_name_T.getText(), acc_rec_T.getText(), account_T.getText(),
									account2_T.getText(), account3_T.getText(), account4_T.getText(),
									account5_T.getText(), idterm_T.getText(), inn_T.getText(), kbk_T.getText(),
									kor_bank_nbra_T.getText(), kpp_T.getText(), name_T.getText(), okato_T.getText(),
									bo1_T.getText(), bo2_T.getText(), stat_T.getText(),comission_T.getText());
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
							stage.getIcons().add(new Image("terminal.png"));
							alert.setTitle("Внимание");
							alert.setHeaderText(null);
							alert.setContentText("Добавлен сервис: " + acc_name_T.getText() + " !\n");
							alert.showAndWait();

							Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
									+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

							Statement sqlStatement = conn.createStatement();
							String readRecordSQL = "select NAME from Z_SB_TERMINAL_DBT t";
							ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
							ObservableList<String> combolist = FXCollections.observableArrayList();
							while (rs.next()) {
								TerminalForCombo tr = new TerminalForCombo();
								tr.setTERMS(rs.getString("NAME"));
								// System.out.println(tr.getTERMS());
								combolist.add(rs.getString("NAME"));
							}
							terms.setItems(combolist);
							terms.getSelectionModel().select(0);
							// System.out.println(terms.getValue().toString());
							rs.close();
							ObservableList<ServiceClass> empData = ViewerDAO
									.searchService(terms.getValue().toString());
							populateService(empData);
							newWindow_yn.close();
						} catch (SQLException e) {
							Alert alert_ = new Alert(Alert.AlertType.INFORMATION);
							Stage stage_ = (Stage) alert_.getDialogPane().getScene().getWindow();
							stage_.getIcons().add(new Image("terminal.png"));
							alert_.setTitle("Внимание");
							alert_.setHeaderText(null);
							alert_.setContentText(e.getMessage());
							alert_.showAndWait();
						}
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

	private void populateService(ObservableList<ServiceClass> trData) {
		employeeTable.setItems(trData);
	}
}

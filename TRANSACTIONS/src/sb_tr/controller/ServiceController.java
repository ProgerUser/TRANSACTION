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
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import sb_tr.Main;
import sb_tr.model.Amra_Trans;
import sb_tr.model.Connect;
import sb_tr.model.ServiceClass;
import sb_tr.model.TerminalClass;
import sb_tr.model.TerminalForCombo;
import sb_tr.model.TransactClass;
import sb_tr.model.ViewerDAO;

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

import org.controlsfx.control.table.TableFilter;

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
	private TableColumn<ServiceClass, String> idterm;
	@FXML
	private TableColumn<ServiceClass, String> inn;
	@FXML
	private TableColumn<ServiceClass, String> kbk;
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
			employeeTable.setEditable(true);
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
			idterm.setCellValueFactory(cellData -> cellData.getValue().idtermProperty());
			inn.setCellValueFactory(cellData -> cellData.getValue().innProperty());
			kbk.setCellValueFactory(cellData -> cellData.getValue().kbkProperty());
			kpp.setCellValueFactory(cellData -> cellData.getValue().kppProperty());
			name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
			okato.setCellValueFactory(cellData -> cellData.getValue().okatoProperty());
			bo1.setCellValueFactory(cellData -> cellData.getValue().bo1Property());
			bo2.setCellValueFactory(cellData -> cellData.getValue().bo2Property());
			comission.setCellValueFactory(cellData -> cellData.getValue().comissionProperty());
			
			acc_name.setCellFactory(TextFieldTableCell.forTableColumn());
			acc_rec.setCellFactory(TextFieldTableCell.forTableColumn());
			account.setCellFactory(TextFieldTableCell.forTableColumn());
			idterm.setCellFactory(TextFieldTableCell.forTableColumn());
			inn.setCellFactory(TextFieldTableCell.forTableColumn());
			kbk.setCellFactory(TextFieldTableCell.forTableColumn());
			kpp.setCellFactory(TextFieldTableCell.forTableColumn());
			name.setCellFactory(TextFieldTableCell.forTableColumn());
			okato.setCellFactory(TextFieldTableCell.forTableColumn());
			bo1.setCellFactory(TextFieldTableCell.forTableColumn());
			bo2.setCellFactory(TextFieldTableCell.forTableColumn());
			comission.setCellFactory(TextFieldTableCell.forTableColumn());
			
			
			acc_name.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setacc_name(t.getNewValue());
				}
			});
			
			acc_rec.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setacc_rec(t.getNewValue());
				}
			});
			account.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setaccount(t.getNewValue());
				}
			});
			idterm.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setidterm(t.getNewValue());
				}
			});
			acc_name.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setacc_name(t.getNewValue());
				}
			});
			inn.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setinn(t.getNewValue());
				}
			});
			kbk.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setkbk(t.getNewValue());
				}
			});
			kpp.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setkpp(t.getNewValue());
				}
			});
			name.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setname(t.getNewValue());
				}
			});
			okato.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setokato(t.getNewValue());
				}
			});
			bo1.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setbo1(t.getNewValue());
				}
			});
			bo2.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setbo2(t.getNewValue());
				}
			});
			
			comission.setOnEditCommit(new EventHandler<CellEditEvent<ServiceClass, String>>() {
				@Override
				public void handle(CellEditEvent<ServiceClass, String> t) {
					((ServiceClass) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setcomission(t.getNewValue());
				}
			});
			
			
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select NAME from Z_SB_TERMINAL_AMRA_DBT t";
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
		@SuppressWarnings("deprecation")
		TableFilter<ServiceClass> filter = new TableFilter<>(employeeTable);
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
			Label inn = new Label("inn:");
			inn.setLayoutX(117);
			inn.setLayoutY(138.0);
			Label kpp = new Label("kpp:");
			kpp.setLayoutX(114.0);
			kpp.setLayoutY(174.0);
			Label acc_rec = new Label("acc_rec:");
			acc_rec.setLayoutX(96.0);
			acc_rec.setLayoutY(210.0);
			Label kbk = new Label("kbk:");
			kbk.setLayoutX(116.0);
			kbk.setLayoutY(246.0);
			Label okato = new Label("okato:");
			okato.setLayoutX(104.0);
			okato.setLayoutY(282.0);
			Label acc_name = new Label("acc_name:");
			acc_name.setLayoutX(82.0);
			acc_name.setLayoutY(318.0);
			Label bo1 = new Label("bo1:");
			bo1.setLayoutX(114.0);
			bo1.setLayoutY(354.0);
			Label bo2 = new Label("bo2:");
			bo2.setLayoutX(112.0);
			bo2.setLayoutY(390.0);
			Label comission_ = new Label("Комисиия:");
			comission_.setLayoutX(82.0);
			comission_.setLayoutY(426.0);

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
			TextField inn_T = new TextField();
			inn_T.setPrefHeight(28.0);
			inn_T.setPrefWidth(198.0);
			inn_T.setLayoutX(150.0);
			inn_T.setLayoutY(138.0);
			inn_T.setText(tr.getinn());
			TextField kpp_T = new TextField();
			kpp_T.setPrefHeight(28.0);
			kpp_T.setPrefWidth(198.0);
			kpp_T.setLayoutX(150.0);
			kpp_T.setLayoutY(174.0);
			kpp_T.setText(tr.getkpp());
			TextField acc_rec_T = new TextField();
			acc_rec_T.setPrefHeight(28.0);
			acc_rec_T.setPrefWidth(198.0);
			acc_rec_T.setLayoutX(150.0);
			acc_rec_T.setLayoutY(210.0);
			acc_rec_T.setText(tr.getacc_rec());
			TextField kbk_T = new TextField();
			kbk_T.setPrefHeight(28.0);
			kbk_T.setPrefWidth(198.0);
			kbk_T.setLayoutX(150.0);
			kbk_T.setLayoutY(246.0);
			kbk_T.setText(tr.getkbk());
			TextField okato_T = new TextField();
			okato_T.setPrefHeight(28.0);
			okato_T.setPrefWidth(198.0);
			okato_T.setLayoutX(150.0);
			okato_T.setLayoutY(282.0);
			okato_T.setText(tr.getokato());
			TextField acc_name_T = new TextField();
			acc_name_T.setPrefHeight(28.0);
			acc_name_T.setPrefWidth(198.0);
			acc_name_T.setLayoutX(150.0);
			acc_name_T.setLayoutY(318.0);
			acc_name_T.setText(tr.getacc_name());
			TextField bo1_T = new TextField();
			bo1_T.setPrefHeight(28.0);
			bo1_T.setPrefWidth(198.0);
			bo1_T.setLayoutX(150.0);
			bo1_T.setLayoutY(354.0);
			bo1_T.setText(tr.getbo1());
			TextField bo2_T = new TextField();
			bo2_T.setPrefHeight(28.0);
			bo2_T.setPrefWidth(198.0);
			bo2_T.setLayoutX(150.0);
			bo2_T.setLayoutY(390.0);
			bo2_T.setText(tr.getbo2());
			TextField comission_T = new TextField();
			comission_T.setPrefHeight(28.0);
			comission_T.setPrefWidth(198.0);
			comission_T.setLayoutX(150.0);
			comission_T.setLayoutY(426.0);
			comission_T.setText(tr.getcomission());
			Button Update = new Button();
			Update.setText("Обновить");
			Update.setLayoutX(29.0);
			Update.setLayoutY(462.0);
			AnchorPane secondaryLayout = new AnchorPane();

			secondaryLayout.getChildren().add(acc_name);
			secondaryLayout.getChildren().add(acc_rec);
			secondaryLayout.getChildren().add(account);
			secondaryLayout.getChildren().add(idterm);
			secondaryLayout.getChildren().add(inn);
			secondaryLayout.getChildren().add(kbk);
			secondaryLayout.getChildren().add(kpp);
			secondaryLayout.getChildren().add(name);
			secondaryLayout.getChildren().add(okato);
			secondaryLayout.getChildren().add(bo1);
			secondaryLayout.getChildren().add(bo2);
			secondaryLayout.getChildren().add(acc_name_T);
			secondaryLayout.getChildren().add(acc_rec_T);
			secondaryLayout.getChildren().add(account_T);
			secondaryLayout.getChildren().add(idterm_T);
			secondaryLayout.getChildren().add(inn_T);
			secondaryLayout.getChildren().add(kbk_T);
			secondaryLayout.getChildren().add(kpp_T);
			secondaryLayout.getChildren().add(name_T);
			secondaryLayout.getChildren().add(okato_T);
			secondaryLayout.getChildren().add(bo1_T);
			secondaryLayout.getChildren().add(bo2_T);
			secondaryLayout.getChildren().add(comission_T);
			secondaryLayout.getChildren().add(comission_);
			
			secondaryLayout.getChildren().add(Update);
			Scene secondScene = new Scene(secondaryLayout, 518, 500);
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
							ViewerDAO.updateService(
									acc_name_T.getText(),
									acc_rec_T.getText(),
									account_T.getText(),
									idterm_T.getText(),
									inn_T.getText(),
									kbk_T.getText(),
									kpp_T.getText(),
									name_T.getText(),
									okato_T.getText(),
									bo1_T.getText(),
									bo2_T.getText(),
									tr.getaccount(),
									tr.getidterm(),
									tr.getname(),
									comission_T.getText()
									);
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
		Label inn = new Label("inn:");
		inn.setLayoutX(117);
		inn.setLayoutY(138.0);
		Label kpp = new Label("kpp:");
		kpp.setLayoutX(114.0);
		kpp.setLayoutY(174.0);
		Label acc_rec = new Label("acc_rec:");
		acc_rec.setLayoutX(96.0);
		acc_rec.setLayoutY(210.0);
		Label kbk = new Label("kbk:");
		kbk.setLayoutX(116.0);
		kbk.setLayoutY(246.0);
		Label okato = new Label("okato:");
		okato.setLayoutX(104.0);
		okato.setLayoutY(282.0);
		Label acc_name = new Label("acc_name:");
		acc_name.setLayoutX(82.0);
		acc_name.setLayoutY(318.0);
		Label bo1 = new Label("bo1:");
		bo1.setLayoutX(114.0);
		bo1.setLayoutY(354.0);
		Label bo2 = new Label("bo2:");
		bo2.setLayoutX(112.0);
		bo2.setLayoutY(390.0);
		Label comission_ = new Label("Комисиия:");
		comission_.setLayoutX(82.0);
		comission_.setLayoutY(426.0);
		
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
		TextField inn_T = new TextField();
		inn_T.setPrefHeight(28.0);
		inn_T.setPrefWidth(198.0);
		inn_T.setLayoutX(150.0);
		inn_T.setLayoutY(138.0);
		inn_T.setPromptText("Обязательно!");
		TextField kpp_T = new TextField();
		kpp_T.setPrefHeight(28.0);
		kpp_T.setPrefWidth(198.0);
		kpp_T.setLayoutX(150.0);
		kpp_T.setLayoutY(174.0);
		kpp_T.setPromptText("Обязательно!");
		TextField acc_rec_T = new TextField();
		acc_rec_T.setPrefHeight(28.0);
		acc_rec_T.setPrefWidth(198.0);
		acc_rec_T.setLayoutX(150.0);
		acc_rec_T.setLayoutY(210.0);
		acc_rec_T.setPromptText("Обязательно!");
		TextField kbk_T = new TextField();
		kbk_T.setPrefHeight(28.0);
		kbk_T.setPrefWidth(198.0);
		kbk_T.setLayoutX(150.0);
		kbk_T.setLayoutY(246.0);
		kbk_T.setPromptText("Обязательно!");
		TextField okato_T = new TextField();
		okato_T.setPrefHeight(28.0);
		okato_T.setPrefWidth(198.0);
		okato_T.setLayoutX(150.0);
		okato_T.setLayoutY(282.0);
		okato_T.setPromptText("Обязательно!");
		TextField acc_name_T = new TextField();
		acc_name_T.setPrefHeight(28.0);
		acc_name_T.setPrefWidth(198.0);
		acc_name_T.setLayoutX(150.0);
		acc_name_T.setLayoutY(318.0);
		acc_name_T.setPromptText("Обязательно!");
		TextField bo1_T = new TextField();
		bo1_T.setPrefHeight(28.0);
		bo1_T.setPrefWidth(198.0);
		bo1_T.setLayoutX(150.0);
		bo1_T.setLayoutY(354.0);
		bo1_T.setPromptText("Обязательно!");
		TextField bo2_T = new TextField();
		bo2_T.setPrefHeight(28.0);
		bo2_T.setPrefWidth(198.0);
		bo2_T.setLayoutX(150.0);
		bo2_T.setLayoutY(390.0);
		bo2_T.setPromptText("Обязательно!");
		TextField comission_T = new TextField();
		comission_T.setPrefHeight(28.0);
		comission_T.setPrefWidth(198.0);
		comission_T.setLayoutX(150.0);
		comission_T.setLayoutY(426.0);
		comission_T.setPromptText("Обязательно!");
		Button add = new Button();
		add.setText("Добавить");
		add.setLayoutX(29.0);
		add.setLayoutY(462.0);
		AnchorPane secondaryLayout = new AnchorPane();

		secondaryLayout.getChildren().add(acc_name);
		secondaryLayout.getChildren().add(acc_rec);
		secondaryLayout.getChildren().add(account);
		secondaryLayout.getChildren().add(idterm);
		secondaryLayout.getChildren().add(inn);
		secondaryLayout.getChildren().add(kbk);
		secondaryLayout.getChildren().add(kpp);
		secondaryLayout.getChildren().add(name);
		secondaryLayout.getChildren().add(okato);
		secondaryLayout.getChildren().add(bo1);
		secondaryLayout.getChildren().add(bo2);
		secondaryLayout.getChildren().add(acc_name_T);
		secondaryLayout.getChildren().add(acc_rec_T);
		secondaryLayout.getChildren().add(account_T);
		secondaryLayout.getChildren().add(idterm_T);
		secondaryLayout.getChildren().add(inn_T);
		secondaryLayout.getChildren().add(kbk_T);
		secondaryLayout.getChildren().add(kpp_T);
		secondaryLayout.getChildren().add(name_T);
		secondaryLayout.getChildren().add(okato_T);
		secondaryLayout.getChildren().add(bo1_T);
		secondaryLayout.getChildren().add(bo2_T);
		secondaryLayout.getChildren().add(add);
		secondaryLayout.getChildren().add(comission_T);
		secondaryLayout.getChildren().add(comission_);
		Scene secondScene = new Scene(secondaryLayout, 518, 500);

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
							ViewerDAO.InsertService(
									acc_name_T.getText(),
									acc_rec_T.getText(),
									account_T.getText(),
									idterm_T.getText(), 
									inn_T.getText(),
									kbk_T.getText(),
									kpp_T.getText(),
									name_T.getText(), 
									okato_T.getText(),
									bo1_T.getText(), 
									bo2_T.getText(),
									comission_T.getText());
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

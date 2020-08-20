package sb_tr.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.controlsfx.control.table.TableFilter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sb_tr.model.BUDCODE;
import sb_tr.model.Connect;
import sb_tr.model.SqlMap;
import sb_tr.model.TerminalDAO;
import sb_tr.util.DBUtil;

/**
 * Саид 04.04.2019.
 */
public class Debt_InfoController {

	public static boolean ff101 = false;
	public static boolean ff104 = false;
	public static boolean ff106 = false;
	public static boolean ff107 = false;
	public static boolean ff110 = false;
	@FXML
	private TextField f104;

	@FXML
	private TextField f101;

	@FXML
	private TextField f110;

	@FXML
	private TextField f22;

	@FXML
	private TextField f109;

	@FXML
	private TextField f108;

	@FXML
	private TextField f107;

	@FXML
	private TextField f106;

	@FXML
	private TextField f105;

	@FXML
	void save(ActionEvent eventt) {
		
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
			public void handle(ActionEvent events) {
				try {
					{
						Connection conn = DBUtil.conn;
						String readRecordSQL = "SELECT count(*) FROM DNV_CREATSTATUS where CCODE = ?";
						PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);
						sqlStatement.setString(1, f101.getText());
						ResultSet rs = sqlStatement.executeQuery();
						if (rs.next()) {
							if (rs.getInt(1) == 0) {
								Alert("Код 101 должен быть из справочника!");
								ff101 = false;
							} else {
								ff101 = true;
							}
						}
						rs.close();
					}
					{
						Connection conn = DBUtil.conn;
						String readRecordSQL = "SELECT count(*) FROM DNV_BUDCODE where CCODE = ?";
						PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);
						sqlStatement.setString(1, f104.getText());
						ResultSet rs = sqlStatement.executeQuery();
						if (rs.next()) {
							if (rs.getInt(1) == 0) {
								Alert("Код 104 должен быть из справочника!");
								ff104 = false;
							} else {
								ff104 = true;
							}
						}
					}
					{
						Connection conn = DBUtil.conn;
						String readRecordSQL = "SELECT count(*) FROM DNV_NALPURP where CCODE =? ";
						PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);
						sqlStatement.setString(1, f106.getText());
						ResultSet rs = sqlStatement.executeQuery();
						if (rs.next()) {
							if (rs.getInt(1) == 0) {
								Alert("Код 106 должен быть из справочника!");
								ff106 = false;
							} else {
								ff106 = true;
							}
						}
						rs.close();
					}
					/*
					{
						Connection conn = DBUtil.conn;
						String readRecordSQL = "SELECT count(*) FROM DNV_NALPERIOD where  CCODE||'.'||to_char(trunc(sysdate),'yyyy') = ?";
						PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);
						sqlStatement.setString(1, f107.getText());
						ResultSet rs = sqlStatement.executeQuery();
						if (rs.next()) {
							if (rs.getInt(1) == 0) {
								Alert("Код 107 должен быть из справочника!");
								ff107 = false;
							} else {
								ff107 = true;
							}
						}
						rs.close();
					}*/
					{
						Connection conn = DBUtil.conn;
						String readRecordSQL = "SELECT count(*) FROM DNV_NALTYPE where CCODE = ?";
						PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);
						sqlStatement.setString(1, f110.getText());
						ResultSet rs = sqlStatement.executeQuery();
						if (rs.next()) {
							if (rs.getInt(1) == 0) {
								Alert("Код 110 должен быть из справочника!");
								ff110 = false;
							} else {
								ff110 = true;
							}
						}
						rs.close();
					}

					/******************************/
					{
						if (ff101 & ff106 & ff110 & ff104) {
							Connection conn = DBUtil.conn;
							CallableStatement callStmt = null;
							callStmt = conn.prepareCall("{ ? = call z_sb_deptinfo(?,?,?,?,?,?,?,?,?,?,?)}");
							callStmt.registerOutParameter(1, Types.VARCHAR);
							callStmt.setString(2, f101.getText());
							callStmt.setString(3, f104.getText());
							callStmt.setString(4, f105.getText());
							callStmt.setString(5, f106.getText());
							callStmt.setString(6, f107.getText());
							callStmt.setString(7, f108.getText());
							callStmt.setString(8, f109.getText());
							callStmt.setString(9, f110.getText());
							callStmt.setString(10, f22.getText());
							callStmt.setInt(11, Integer.valueOf(Connect.trnnum));
							callStmt.setInt(12, Integer.valueOf(Connect.trnanum));
							callStmt.execute();
							
							if(!callStmt.getString(1).equals("ok")) {
								Alert(callStmt.getString(1));
							}
							Alert("Обновлено успешно!");
							((Node) (eventt.getSource())).getScene().getWindow().hide();
						} else {
							Alert("Поля не из справочников!");
						}
						newWindow_yn.close();
					}
				} catch (Exception e) {
					Alert(e.getMessage());
				}
			}
		});

		newWindow_yn.setTitle("Внимание");
		newWindow_yn.setScene(ynScene);
		// Specifies the modality for new window.
		newWindow_yn.initModality(Modality.WINDOW_MODAL);

		Stage stage = (Stage) f101.getScene().getWindow();
		// Specifies the owner Window (parent) for new window
		newWindow_yn.initOwner(stage);
		newWindow_yn.getIcons().add(new Image("icon.png"));
		newWindow_yn.show();
	}

	@FXML
	void b101(ActionEvent event) {
		Button Update = new Button();
		Update.setText("Выбрать");
		Update.setLayoutX(29.0);
		Update.setLayoutY(462.0);
		AnchorPane secondaryLayout = new AnchorPane();

		TableView<BUDCODE> debtinfo = new TableView();
		TableColumn<BUDCODE, String> code = new TableColumn<>("Код");
		code.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<BUDCODE, String> codename = new TableColumn<>("Наименование");
		codename.setCellValueFactory(new PropertyValueFactory<>("codename"));
		debtinfo.getColumns().add(code);
		debtinfo.getColumns().add(codename);
		debtinfo.prefWidth(341);
		debtinfo.prefHeight(490);
		/**/
		code.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
		codename.setCellValueFactory(cellData -> cellData.getValue().codenameProperty());
		ObservableList<BUDCODE> empData = TerminalDAO
				.bud("SELECT CCODE,CDESCRIPTION FROM DNV_CREATSTATUS order by cCode");
		debtinfo.setItems(empData);
		autoResizeColumns(debtinfo);
		TableFilter<BUDCODE> tableFilter = TableFilter.forTableView(debtinfo).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		/**/
		Update.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (debtinfo.getSelectionModel().getSelectedItem() == null) {
					Alert("Выберите сначала данные из таблицы!");
				} else {
					String form_id = debtinfo.getSelectionModel().getSelectedItem().getcode();
					f101.setText(form_id);
					((Node) (event.getSource())).getScene().getWindow().hide();
				}
			}

		});

		secondaryLayout.getChildren().add(Update);
		secondaryLayout.getChildren().add(debtinfo);

		// VBox vbox = new VBox(debtinfo);
		Scene secondScene = new Scene(secondaryLayout, 518, 500);
		Stage stage = (Stage) f101.getScene().getWindow();

		Stage newWindow = new Stage();
		newWindow.setTitle("Статусы составителей расчетных документов");
		newWindow.setScene(secondScene);
		// Specifies the modality for new window.
		newWindow.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow.initOwner(stage);
		newWindow.getIcons().add(new Image("icon.png"));
		newWindow.show();
	}

	@FXML
	void b104(ActionEvent event) {

		Button Update = new Button();
		Update.setText("Выбрать");
		Update.setLayoutX(29.0);
		Update.setLayoutY(462.0);
		AnchorPane secondaryLayout = new AnchorPane();

		TableView<BUDCODE> debtinfo = new TableView();
		TableColumn<BUDCODE, String> code = new TableColumn<>("Код");
		code.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<BUDCODE, String> codename = new TableColumn<>("Наименование");
		codename.setCellValueFactory(new PropertyValueFactory<>("codename"));
		debtinfo.getColumns().add(code);
		debtinfo.getColumns().add(codename);
		debtinfo.prefWidth(341);
		debtinfo.prefHeight(490);
		/**/
		code.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
		codename.setCellValueFactory(cellData -> cellData.getValue().codenameProperty());
		ObservableList<BUDCODE> empData = TerminalDAO.bud("SELECT CCODE,CDESCRIPTION FROM DNV_BUDCODE order by cCode");
		debtinfo.setItems(empData);
		autoResizeColumns(debtinfo);
		TableFilter<BUDCODE> tableFilter = TableFilter.forTableView(debtinfo).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		/**/
		Update.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (debtinfo.getSelectionModel().getSelectedItem() == null) {
					Alert("Выберите сначала данные из таблицы!");
				} else {
					String form_id = debtinfo.getSelectionModel().getSelectedItem().getcode();
					f104.setText(form_id);
					((Node) (event.getSource())).getScene().getWindow().hide();
				}
			}

		});

		secondaryLayout.getChildren().add(Update);
		secondaryLayout.getChildren().add(debtinfo);

		// VBox vbox = new VBox(debtinfo);
		Scene secondScene = new Scene(secondaryLayout, 518, 500);
		Stage stage = (Stage) f101.getScene().getWindow();

		Stage newWindow = new Stage();
		newWindow.setTitle("Коды бюджетной классификаций");
		newWindow.setScene(secondScene);
		// Specifies the modality for new window.
		newWindow.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow.initOwner(stage);
		newWindow.getIcons().add(new Image("icon.png"));
		newWindow.show();
	}

	@FXML
	void b106(ActionEvent event) {
		Button Update = new Button();
		Update.setText("Выбрать");
		Update.setLayoutX(29.0);
		Update.setLayoutY(462.0);
		AnchorPane secondaryLayout = new AnchorPane();

		TableView<BUDCODE> debtinfo = new TableView();
		TableColumn<BUDCODE, String> code = new TableColumn<>("Код");
		code.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<BUDCODE, String> codename = new TableColumn<>("Наименование");
		codename.setCellValueFactory(new PropertyValueFactory<>("codename"));
		debtinfo.getColumns().add(code);
		debtinfo.getColumns().add(codename);
		debtinfo.prefWidth(341);
		debtinfo.prefHeight(490);
		/**/
		code.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
		codename.setCellValueFactory(cellData -> cellData.getValue().codenameProperty());
		ObservableList<BUDCODE> empData = TerminalDAO.bud("SELECT CCODE,CDESCRIPTION FROM DNV_NALPURP  order by cCode");
		debtinfo.setItems(empData);
		autoResizeColumns(debtinfo);
		TableFilter<BUDCODE> tableFilter = TableFilter.forTableView(debtinfo).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		/**/
		Update.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (debtinfo.getSelectionModel().getSelectedItem() == null) {
					Alert("Выберите сначала данные из таблицы!");
				} else {
					String form_id = debtinfo.getSelectionModel().getSelectedItem().getcode();
					f106.setText(form_id);
					((Node) (event.getSource())).getScene().getWindow().hide();
				}
			}

		});

		secondaryLayout.getChildren().add(Update);
		secondaryLayout.getChildren().add(debtinfo);

		// VBox vbox = new VBox(debtinfo);
		Scene secondScene = new Scene(secondaryLayout, 518, 500);
		Stage stage = (Stage) f101.getScene().getWindow();

		Stage newWindow = new Stage();
		newWindow.setTitle("Основания налоговых платежей");
		newWindow.setScene(secondScene);
		// Specifies the modality for new window.
		newWindow.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow.initOwner(stage);
		newWindow.getIcons().add(new Image("icon.png"));
		newWindow.show();
	}

	@FXML
	void b107(ActionEvent event) {
		Button Update = new Button();
		Update.setText("Выбрать");
		Update.setLayoutX(29.0);
		Update.setLayoutY(462.0);
		AnchorPane secondaryLayout = new AnchorPane();

		TableView<BUDCODE> debtinfo = new TableView();
		TableColumn<BUDCODE, String> code = new TableColumn<>("Код");
		code.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<BUDCODE, String> codename = new TableColumn<>("Наименование");
		codename.setCellValueFactory(new PropertyValueFactory<>("codename"));
		debtinfo.getColumns().add(code);
		debtinfo.getColumns().add(codename);
		debtinfo.prefWidth(341);
		debtinfo.prefHeight(490);
		/**/
		code.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
		codename.setCellValueFactory(cellData -> cellData.getValue().codenameProperty());
		ObservableList<BUDCODE> empData = TerminalDAO.bud(
				"SELECT CCODE||'.'||to_char(trunc(sysdate),'yyyy') CCODE,CDESCRIPTION FROM DNV_NALPERIOD order by cCode");
		debtinfo.setItems(empData);
		autoResizeColumns(debtinfo);
		TableFilter<BUDCODE> tableFilter = TableFilter.forTableView(debtinfo).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		/**/
		Update.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (debtinfo.getSelectionModel().getSelectedItem() == null) {
					Alert("Выберите сначала данные из таблицы!");
				} else {
					String form_id = debtinfo.getSelectionModel().getSelectedItem().getcode();
					f107.setText(form_id);
					((Node) (event.getSource())).getScene().getWindow().hide();
				}
			}

		});

		secondaryLayout.getChildren().add(Update);
		secondaryLayout.getChildren().add(debtinfo);

		// VBox vbox = new VBox(debtinfo);
		Scene secondScene = new Scene(secondaryLayout, 518, 500);
		Stage stage = (Stage) f101.getScene().getWindow();

		Stage newWindow = new Stage();
		newWindow.setTitle("Префиксы показателей налоговых периодов");
		newWindow.setScene(secondScene);
		// Specifies the modality for new window.
		newWindow.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow.initOwner(stage);
		newWindow.getIcons().add(new Image("icon.png"));
		newWindow.show();
	}

	@FXML
	void b110(ActionEvent event) {
		Button Update = new Button();
		Update.setText("Выбрать");
		Update.setLayoutX(29.0);
		Update.setLayoutY(462.0);
		AnchorPane secondaryLayout = new AnchorPane();

		TableView<BUDCODE> debtinfo = new TableView();
		TableColumn<BUDCODE, String> code = new TableColumn<>("Код");
		code.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<BUDCODE, String> codename = new TableColumn<>("Наименование");
		codename.setCellValueFactory(new PropertyValueFactory<>("codename"));
		debtinfo.getColumns().add(code);
		debtinfo.getColumns().add(codename);
		debtinfo.prefWidth(341);
		debtinfo.prefHeight(490);
		/**/
		code.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
		codename.setCellValueFactory(cellData -> cellData.getValue().codenameProperty());
		ObservableList<BUDCODE> empData = TerminalDAO.bud("SELECT CCODE,CDESCRIPTION FROM DNV_NALTYPE order by cCode");
		debtinfo.setItems(empData);
		autoResizeColumns(debtinfo);
		TableFilter<BUDCODE> tableFilter = TableFilter.forTableView(debtinfo).apply();
		tableFilter.setSearchStrategy((input, target) -> {
			try {
				return target.toLowerCase().contains(input.toLowerCase());
			} catch (Exception e) {
				return false;
			}
		});
		/**/
		Update.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (debtinfo.getSelectionModel().getSelectedItem() == null) {
					Alert("Выберите сначала данные из таблицы!");
				} else {
					String form_id = debtinfo.getSelectionModel().getSelectedItem().getcode();
					f110.setText(form_id);
					((Node) (event.getSource())).getScene().getWindow().hide();
				}
			}

		});

		secondaryLayout.getChildren().add(Update);
		secondaryLayout.getChildren().add(debtinfo);

		// VBox vbox = new VBox(debtinfo);
		Scene secondScene = new Scene(secondaryLayout, 518, 500);
		Stage stage = (Stage) f101.getScene().getWindow();

		Stage newWindow = new Stage();
		newWindow.setTitle("Типы налоговых платежей");
		newWindow.setScene(secondScene);
		// Specifies the modality for new window.
		newWindow.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow.initOwner(stage);
		newWindow.getIcons().add(new Image("icon.png"));
		newWindow.show();
	}

	// For MultiThreading
	private Executor exec;

	public static void Alert(String mes) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mes);
		alert.showAndWait();
	}

	public void autoResizeColumns(TableView<?> table) {
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

	@FXML
	private void initialize() {
		// For multithreading: Create executor that uses daemon threads:
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		if (chk_menu("savebudcode", Connect.userID_) == 1) {
			
		} else {
			Alert("Нет доступа!");
			Platform.exit();
	        System.exit(0);
		}

		try {
			Connection conn = DBUtil.conn;
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select inum,\n" + "       ianum,\n" + "       ccreatstatus f101,\n"
					+ "       cbudcode f104,\n" + "       cokatocode f105,\n" + "       cnalpurp f106,\n"
					+ "       cnalperiod f107,\n" + "       cnaldocnum f108,\n" + "       cnaldocdate f109,\n"
					+ "       cnaltype f110,\n" + "       cnalflag,\n" + "       cdocindex f22,\n"
					+ "       cdocindex_nz\n" + "  from TRN_DEPT_INFO t\n" + " where INUM = " + Connect.trnnum + "\n"
					+ "and IANUM = " + Connect.trnanum + "\n";
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			while (rs.next()) {
				f101.setText(rs.getString("f101"));
				f104.setText(rs.getString("f104"));
				f105.setText(rs.getString("f105"));
				f106.setText(rs.getString("f106"));
				f107.setText(rs.getString("f107"));
				f108.setText(rs.getString("f108"));
				f109.setText(rs.getString("f109"));
				f110.setText(rs.getString("f110"));
				f22.setText(rs.getString("f22"));
			}
			rs.close();
		} catch (Exception e) {
			Alert(e.getMessage());
		}

	}

	public int chk_menu(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		// Connection conn;
		Connection conn = DBUtil.conn;
		try {
			/*
			 * conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ +
			 * "/" + Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
			 */

			SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String readRecordSQL = s.getSql("acces_menu");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			System.out.println(readRecordSQL);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<String> combolist = FXCollections.observableArrayList();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			// conn.close();
			prepStmt.close();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return ret;
	}

}

/*
 * Connection conn = DBUtil.conn;
						String ifex = "select count(*)  from TRN_DEPT_INFO  where INUM = ? and IANUM = ? \n";
						PreparedStatement prstm = conn.prepareStatement(ifex);
						prstm.setInt(1, Integer.valueOf(Connect.trnnum));
						prstm.setInt(2, Integer.valueOf(Connect.trnanum));
						ResultSet rs1 = prstm.executeQuery();
						if (rs1.next()) {
							if (rs1.getInt(1) == 0) {
								String fff101 = (f101.getText().equals("") | f101.getText().isEmpty()) ? "null": f101.getText();
								String fff104 = (f104.getText().equals("") | f104.getText().isEmpty()) ? "null": f104.getText();
								String fff105 = (f105.getText().equals("") | f105.getText().isEmpty()) ? "null": f105.getText();
								String fff106 = (f106.getText().equals("") | f106.getText().isEmpty()) ? "null": f106.getText();
								String fff107 = (f107.getText().equals("") | f107.getText().isEmpty()) ? "null": f107.getText();
								String fff108 = (f108.getText().equals("") | f108.getText().isEmpty()) ? "null": f108.getText();
								String fff109 = (f109.getText().equals("") | f109.getText().isEmpty()) ? "null": f109.getText();
								String fff110 = (f110.getText().equals("") | f110.getText().isEmpty()) ? "null": f110.getText();
								String fff22 = (f22.getText().equals("") | f22.getText().isEmpty()) ? "null": f22.getText();
								String insert = "insert into TRN_DEPT_INFO (" +
							                                  "inum, "+ 
							                                  "ianum, "+ 
							                                  "ccreatstatus, " + 
							                                  "cbudcode, " + 
							                                  "cokatocode, " + 
							                                  "cnalpurp, "+ 
							                                  "cnalperiod, " + 
							                                  "cnaldocnum, " + 
							                                  "cnaldocdate, " + 
							                                  "cnaltype, "+ 
							                                  "cdocindex " + 
							                                  "values ("
							                                  + ""+Connect.trnnum+","
							                                  + ""+Connect.trnanum+","
							                                  + "'"+ fff101+"',"
							                                  + "'"+ fff104+"',"
							                                  + "'"+ fff105+"',"
							                                  + "'"+ fff106+"',"
							                                  + "'"+ fff107+"',"
							                                  + "'"+ fff108+"',"
							                                  + "'"+ fff109+"',"
							                                  + "'"+ fff110+"',"
							                                  + "'"+ fff22+"')";
									PreparedStatement insprp = conn.prepareStatement(insert);
									
									insprp.setInt(1, Integer.valueOf(Connect.trnnum));
									insprp.setInt(2, Integer.valueOf(Connect.trnanum));
									insprp.setString(3, (f101.getText().equals("") | f101.getText().isEmpty()) ? "null": f101.getText());
									insprp.setString(4, (f104.getText().equals("") | f104.getText().isEmpty()) ? null
											: f104.getText());
									insprp.setString(5, (f105.getText().equals("") | f105.getText().isEmpty()) ? null
											: f105.getText());
									insprp.setString(6, (f106.getText().equals("") | f106.getText().isEmpty()) ? null
											: f106.getText());
									insprp.setString(7, (f107.getText().equals("") | f107.getText().isEmpty()) ? null
											: f107.getText());
									insprp.setString(8, (f108.getText().equals("") | f108.getText().isEmpty()) ? null
											: f108.getText());
									insprp.setString(9, (f109.getText().equals("") | f109.getText().isEmpty()) ? null
											: f109.getText());
									insprp.setString(10, (f110.getText().equals("") | f110.getText().isEmpty()) ? null
											: f110.getText());
									insprp.setString(11, (f22.getText().equals("") | f22.getText().isEmpty()) ? null
											: f22.getText());
											
									insprp.executeUpdate();
								Alert("Успешно создана запись!");
							} else {
								String fff101 = (f101.getText().equals("") | f101.getText().isEmpty()) ? "null": f101.getText();
								String fff104 = (f104.getText().equals("") | f104.getText().isEmpty()) ? "null": f104.getText();
								String fff105 = (f105.getText().equals("") | f105.getText().isEmpty()) ? "null": f105.getText();
								String fff106 = (f106.getText().equals("") | f106.getText().isEmpty()) ? "null": f106.getText();
								String fff107 = (f107.getText().equals("") | f107.getText().isEmpty()) ? "null": f107.getText();
								String fff108 = (f108.getText().equals("") | f108.getText().isEmpty()) ? "null": f108.getText();
								String fff109 = (f109.getText().equals("") | f109.getText().isEmpty()) ? "null": f109.getText();
								String fff110 = (f110.getText().equals("") | f110.getText().isEmpty()) ? "null": f110.getText();
								String fff22 = (f22.getText().equals("") | f22.getText().isEmpty()) ? "null": f22.getText();
								String update = "update TRN_DEPT_INFO " + 
							                     "set ccreatstatus = '"+fff101+"'," + 
										            " cbudcode = '"+fff104+"',\n" + 
							                        " cokatocode ='"+fff105+"',\n" + 
										            " cnalpurp = "+fff106+"',\n" + 
							                        " cnalperiod = '"+fff107+"',\n"+ 
										            " cnaldocnum = '"+fff108+"',\n" + 
							                        " cnaldocdate = '"+fff109+"',\n" + 
										            " cnaltype = '"+fff110+"',\n"+ 
							                        " cdocindex = '"+fff22+"' \n" + 
										         "where inum = "+Connect.trnnum+" and ianum = "+Connect.trnnum+"\n";
								PreparedStatement updprp = conn.prepareStatement(update);
								
								updprp.setString(1, f101.getText());
								updprp.setString(2, f104.getText());
								updprp.setString(3, f105.getText());
								updprp.setString(4, f106.getText());
								updprp.setString(5, f107.getText());
								updprp.setString(6, f108.getText());
								updprp.setString(7, f109.getText());
								updprp.setString(8, f110.getText());
								updprp.setString(9, f22.getText());
							
								updprp.setInt(10, Integer.valueOf(Connect.trnnum));
								updprp.setInt(11, Integer.valueOf(Connect.trnanum));
								
								updprp.executeUpdate();
								*/

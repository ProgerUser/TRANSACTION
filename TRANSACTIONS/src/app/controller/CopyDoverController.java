package app.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;

import app.model.Connect;
import app.model.DJTRUST;
import app.model.SqlMap;
import app.util.DBUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Саид 24.08.2020.
 */
public class CopyDoverController {
	@FXML
	private TableColumn<DJTRUST, String> ID;
	@FXML
	private TableColumn<DJTRUST, String> dogid;

	@FXML
	private TableColumn<DJTRUST, String> clifioo;

	@FXML
	private TableColumn<DJTRUST, String> dognum;

	@FXML
	private TextField searchf;

	@FXML
	private TableView<DJTRUST> dover;

	@FXML
	void find(ActionEvent event) {
		try {
			Connection conn = DBUtil.conn;
			SqlMap s = new SqlMap().load("/SQL.xml");
			String readRecordSQL = s.getSql("DJ");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, "%" + searchf.getText() + "%");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<DJTRUST> empData = getbud(rs);
			dover.setItems(empData);
			autoResizeColumns(dover);
			TableFilter<DJTRUST> tableFilter = TableFilter.forTableView(dover).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Alert(ExceptionUtils.getStackTrace(e));
		}
	}

	private static ObservableList<DJTRUST> getbud(ResultSet rs) {
		try {
			ObservableList<DJTRUST> forms_list = FXCollections.observableArrayList();
			while (rs.next()) {
				DJTRUST frms = new DJTRUST();
				frms.setid_trust(rs.getString("TRUST_NUM"));
				frms.setfio(rs.getString("fio"));
				frms.setacc(rs.getString("acc"));
				frms.setiddover(rs.getString("id_trust"));
				forms_list.add(frms);
			}
			return forms_list;
		} catch (Exception e) {
			Alert(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@FXML
	void copy(ActionEvent event) {
		if (dover.getSelectionModel().getSelectedItem() == null) {
			Alert("Выберите сначала данные из таблицы!");
		} else {
			try {
				DJTRUST dj = dover.getSelectionModel().getSelectedItem();
				Connection conn = DBUtil.conn;
				CallableStatement callStmt = null;
				callStmt = conn.prepareCall("{ ? = call z_sb_copytrust(?,?)}");
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setInt(2, Integer.valueOf(dj.getiddover()));
				callStmt.setInt(3, Integer.valueOf(Connect.djdog_id));
				callStmt.execute();
				if (!callStmt.getString(1).equals("ok")) {
					Alert(callStmt.getString(1));
				} else {
					Alert("Обновлено успешно!");
					//((Node) (event.getSource())).getScene().getWindow().hide();
					Platform.exit();
					System.exit(0);
				}

			} catch (Exception e) {
				Alert(ExceptionUtils.getStackTrace(e));
			}
		}

	}

	// For MultiThreading

	@SuppressWarnings("unused")
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
		dogid.setCellValueFactory(cellData -> cellData.getValue().id_trustProperty());
		clifioo.setCellValueFactory(cellData -> cellData.getValue().fioProperty());
		dognum.setCellValueFactory(cellData -> cellData.getValue().accProperty());
		ID.setCellValueFactory(cellData -> cellData.getValue().iddoverProperty());

	}
}

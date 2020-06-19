package sb_tr.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sb_tr.Main;
import sb_tr.model.Add_File;
import sb_tr.model.Attributes;
import sb_tr.model.Connect;
import sb_tr.model.Forms;
import sb_tr.model.Ibank2;
import sb_tr.model.TerminalDAO;
import sb_tr.model.TransactClass;
import sb_tr.model.User_in;
import sb_tr.model.User_out;
import sb_tr.model.ViewerDAO;
import sb_tr.util.DBUtil;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Пачулия Саид 04.06.2020.
 */
public class Ibank {

	@FXML
	private TableView<Ibank2> CLI;
	@FXML
	private TableColumn<Ibank2, Integer> CLIENT_ID;
	@FXML
	private TableColumn<Ibank2, String> NAME_CLN;

	@FXML
	private PasswordField password;

	@FXML
	private TextArea acc;

	@FXML
	private TextField login;

	@FXML
	private TextField db;

	public void altert(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	@FXML
	private void initialize() {
		CLIENT_ID.setCellValueFactory(cellData -> cellData.getValue().CLIENT_ID_Property().asObject());
		NAME_CLN.setCellValueFactory(cellData -> cellData.getValue().NAME_CLN_Property());

		/* Listener */
		CLI.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				try {
					String acc_ = "";
					int id = CLI.getSelectionModel().getSelectedItem().get_CLIENT_ID();
					Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
							+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
					Statement sqlStatement = conn.createStatement();
					String readRecordSQL = "select filtering\n" + 
							"  from (select '[224100017]' filtering\n" + 
							"          from dual\n" + 
							"        union all\n" + 
							"        select ACCOUNT filtering\n" + 
							"          from ibank2.ACCOUNTS t\n" + 
							"         where ID in\n" + 
							"               (select ACCOUNT_ID from ibank2.C2ACCOUNTS t where client_id = "+id+"))\n" + 
							" order by case\n" + 
							"            when substr(filtering, 1, 1) = '[' then\n" + 
							"             1\n" + 
							"            else\n" + 
							"             2\n" + 
							"          end";
					ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
					while (myResultSet.next()) {
						acc_ = acc_+"\r\n"+myResultSet.getString("FILTERING");
					}
					acc.setText(acc_);
				} catch (Exception e) {
					altert(e.getMessage());
				}
			}
		});
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

	@FXML
	void search(ActionEvent event) {
		if (login.getText().length() == 0 | password.getText().length() == 0 | db.getText().length() == 0) {
			altert("Заполните поля!");
		} else {
			Connect.connectionURL_ = db.getText();
			Connect.userID_ = login.getText();
			Connect.userPassword_ = password.getText();
			ObservableList<Ibank2> empData = TerminalDAO.CLIENTS(db.getText(), login.getText(), password.getText());
			populate_cli(empData);
			autoResizeColumns(CLI);
			TableFilter.forTableView(CLI).apply();
		}

	}

	private void populate_cli(ObservableList<Ibank2> trData) {
		CLI.setItems(trData);
	}
}

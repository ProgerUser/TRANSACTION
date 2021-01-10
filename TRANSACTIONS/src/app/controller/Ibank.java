package app.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;
import com.sun.rowset.CachedRowSetImpl;
import app.Main;
import app.model.Connect;
import app.model.Ibank2;
import app.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sbalert.Msg;

/**
 * Пачулия Саид 04.06.2020.
 */
@SuppressWarnings({"restriction" })
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

	public Connection conn = null;

	public void altert(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	public void dbConnect() {
		try {
			@SuppressWarnings("unused")
			Connect con = new Connect();

			// Setting Oracle JDBC Driver
			Class.forName("oracle.jdbc.OracleDriver");

			System.out.println("Драйвер Oracle JDBC зарегистрирован!");

			// Establish the Oracle Connection using Connection String
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + login.getText() + "/" + password.getText() + "@" + db.getText() + "");

		} catch (SQLException | ClassNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
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
					Ibank2 IbMod = CLI.getSelectionModel().getSelectedItem();
					Statement sqlStatement = conn.createStatement();
					String readRecordSQL = "select filtering\n" + "  from (select '[224100017]' filtering\n"
							+ "          from dual\n" + "        union all\n" + "        select ACCOUNT filtering\n"
							+ "          from ibank2.ACCOUNTS t\n" + "         where ID in\n"
							+ "               (select ACCOUNT_ID from ibank2.C2ACCOUNTS t where client_id = "
							+ IbMod.get_CLIENT_ID() + "))\n" + " order by case\n"
							+ "            when substr(filtering, 1, 1) = '[' then\n" + "             1\n"
							+ "            else\n" + "             2\n" + "          end";
					ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
					while (myResultSet.next()) {
						if (acc_.equals("")) {
							acc_ = myResultSet.getString("FILTERING");
						} else {
							acc_ = acc_ + "\r\n" + myResultSet.getString("FILTERING");
						}
					}
					acc.setText(acc_);
				} catch (Exception e) {
					altert(e.getMessage());
				}
			}
		});
	}

	public  void autoResizeColumns(TableView<?> table) {
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
			this.dbConnect();
			ObservableList<Ibank2> empData = CLIENTS();
			populate_cli(empData);
			autoResizeColumns(CLI);
			TableFilter<Ibank2> tableFilter = TableFilter.forTableView(CLI).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		}

	}

	private void populate_cli(ObservableList<Ibank2> trData) {
		CLI.setItems(trData);
	}

	public ObservableList<Ibank2> CLIENTS() {
		String selectStmt = "select CLIENT_ID, NAME_CLN\n" + "  from ibank2.CLIENTS t";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Ibank2> Cli_lst = get_cli(rsEmps);

		// Return Cli_lst object
		return Cli_lst;
	}

	private static ObservableList<Ibank2> get_cli(ResultSet rs) {
		try {
			ObservableList<Ibank2> user_in_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Ibank2 user_in = new Ibank2();
				user_in.set_CLIENT_ID(rs.getInt("CLIENT_ID"));
				user_in.set_NAME_CLN(rs.getString("NAME_CLN"));
				user_in_list.add(user_in);
			}
			return user_in_list;
		} catch (SQLException e) {
			Msg.Message(e.getMessage());
		}
		return null;
	}

	// DB Execute Query Operation
	@SuppressWarnings({})
	public ResultSet dbExecuteQuery(String queryStmt) {
		// Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		CachedRowSetImpl crs = null;
		try {
			Main.logger = Logger.getLogger(DBUtil.class);
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				dbConnect();
			}
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(queryStmt);
			crs = new CachedRowSetImpl();
			crs.populate(resultSet);
		} catch (SQLException e) {
			Msg.Message(e.getMessage());
		} finally {
			if (resultSet != null) {
				// Close resultSet
				try {
					resultSet.close();
				} catch (SQLException e) {
					Msg.Message(e.getMessage());
				}
			}
			if (stmt != null) {
				// Close Statement
				try {
					stmt.close();
				} catch (SQLException e) {
					Msg.Message(e.getMessage());
					Main.logger.error(e.getMessage() + "~" + Thread.currentThread().getName());
				}
			}
		}
		// Return CachedRowSet
		return crs;
	}
}

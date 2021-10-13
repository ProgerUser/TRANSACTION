package su.sbra.psv.app.ibank;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import com.sun.rowset.CachedRowSetImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

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
	public Connection conn = null;


	public void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.setProperty("password", password.getText());
			props.setProperty("user", login.getText());
			props.put("v$session.osuser", System.getProperty("user.name").toString());
			props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
			props.put("v$session.program", getClass().getName());
			conn  = DriverManager.getConnection("jdbc:oracle:thin:@" + db.getText(), props);
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
					String readRecordSQL = "select filtering\n"
							+ "  from (select '[224100017]' filtering\n"
							+ "          from dual\n"
							+ "        union all\n"
							+ "        select ACCOUNT filtering\n"
							+ "          from ibank2.ACCOUNTS t\n"
							+ "         where ID in\n"
							+ "               (select ACCOUNT_ID from ibank2.C2ACCOUNTS t where client_id = ?))\n"
							+ " order by case\n"
							+ "            when substr(filtering, 1, 1) = '[' then\n"
							+ "             1\n"
							+ "            else\n"
							+ "             2\n"
							+ "          end";
					//Main.logger.info(readRecordSQL);
					PreparedStatement sqlStatement = conn.prepareStatement(readRecordSQL);
					sqlStatement.setInt(1, IbMod.get_CLIENT_ID());
					ResultSet myResultSet = sqlStatement.executeQuery();
					while (myResultSet.next()) {
						if (acc_.equals("")) {
							acc_ = myResultSet.getString("FILTERING");
						} else {
							acc_ = acc_ + "\r\n" + myResultSet.getString("FILTERING");
						}
					}
					acc.setText(acc_);
				} catch (Exception e) {
					DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			Msg.Message("Заполните поля!");
		} else {
			this.dbConnect();
			//load
			LoadTable();
		}

	}

	/**
	 * Load table
	 */
	void LoadTable() {
		try {
			String selectStmt = "select CLIENT_ID, NAME_CLN from ibank2.CLIENTS t order by NAME_CLN asc";
			PreparedStatement sqlStatement = conn.prepareStatement(selectStmt);
			ResultSet rs = sqlStatement.executeQuery();
			ObservableList<Ibank2> trData = FXCollections.observableArrayList();
			while (rs.next()) {
				Ibank2 user_in = new Ibank2();
				user_in.set_CLIENT_ID(rs.getInt("CLIENT_ID"));
				user_in.set_NAME_CLN(rs.getString("NAME_CLN"));
				trData.add(user_in);
			}
			CLI.setItems(trData);
			
			TableFilter<Ibank2> tableFilter = TableFilter.forTableView(CLI).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
	

	// DB Execute Query Operation
	@SuppressWarnings({})
	public ResultSet dbExecuteQuery(String queryStmt) {
		// Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		CachedRowSetImpl crs = null;
		try {
			Main.logger = Logger.getLogger(getClass());
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				dbConnect();
			}
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(queryStmt);
			crs = new CachedRowSetImpl();
			crs.populate(resultSet);
		} catch (SQLException e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (resultSet != null) {
				// Close resultSet
				try {
					resultSet.close();
				} catch (SQLException e) {
					DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
			if (stmt != null) {
				// Close Statement
				try {
					stmt.close();
				} catch (SQLException e) {
					DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		// Return CachedRowSet
		return crs;
	}
}

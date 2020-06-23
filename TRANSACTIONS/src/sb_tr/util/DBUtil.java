package sb_tr.util;

import com.sun.rowset.CachedRowSetImpl;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sb_tr.model.Connect;

import java.sql.*;

/**
 * Created by Pachuliy Said on 22.02.2016.
 */
public class DBUtil {
	// Declare JDBC Driver
	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	// Connection
	public static Connection conn = null;

	// Connection String
	// String connStr = "jdbc:oracle:thin:Username/Password@IP:Port/SID";
	// Username=HR, Password=HR, IP=localhost, IP=1521, SID=xe
	// private static final String connStr =
	// "jdbc:oracle:thin@10.111.64.21:1521/odb";

	// static String connectionURL =
	// "jdbc:oracle:thin:"++"/xxx@10.111.64.21:1521/odb";

	// Connect to DB
	public static void dbConnect() {
		try {
			@SuppressWarnings("unused")
			Connect con = new Connect();

			// Setting Oracle JDBC Driver
			Class.forName(JDBC_DRIVER);

			System.out.println("Драйвер Oracle JDBC зарегистрирован!");

			// Establish the Oracle Connection using Connection String
			conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@"
					+ Connect.connectionURL_ + "");

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

	// Close Connection
	public static void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
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

	// DB Execute Query Operation
	public static ResultSet dbExecuteQuery(String queryStmt) {
		// Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		CachedRowSetImpl crs = null;
		try {
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				dbConnect();
			}
			System.out.println("Выборка данных: " + queryStmt + "\n");

			// Create statement
			stmt = conn.createStatement();

			// Execute select (query) operation
			resultSet = stmt.executeQuery(queryStmt);

			// CachedRowSet Implementation
			// In order to prevent "java.sql.SQLRecoverableException: Closed
			// Connection: next" error
			// We are using CachedRowSet
			crs = new CachedRowSetImpl();
			crs.populate(resultSet);
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} finally {
			if (resultSet != null) {
				// Close resultSet
				try {
					resultSet.close();
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
			if (stmt != null) {
				// Close Statement
				try {
					stmt.close();
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
			// Close connection
			//dbDisconnect();
		}
		// Return CachedRowSet
		return crs;
	}

	// DB Execute Update (For Update/Insert/Delete) Operation
	public static void dbExecuteUpdate(String sqlStmt) {
		// Declare statement as null
		Statement stmt = null;
		System.out.println("Update: " + sqlStmt + "\n");
		try {
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				dbConnect();
			}
			// Create Statement
			stmt = conn.createStatement();
			// Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
			
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} finally {
			if (stmt != null) {
				// Close statement
				try {
					stmt.close();
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
			// Close connection
			//dbDisconnect();
		}
	}
}

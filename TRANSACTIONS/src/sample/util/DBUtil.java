package sample.util;

import com.sun.rowset.CachedRowSetImpl;
import sample.model.Connect;
import java.sql.*;

/**
 * Created by ONUR BASKIRT on 22.02.2016.
 */
public class DBUtil {
	// Declare JDBC Driver
	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	// Connection
	private static Connection conn = null;

	// Connection String
	// String connStr = "jdbc:oracle:thin:Username/Password@IP:Port/SID";
	// Username=HR, Password=HR, IP=localhost, IP=1521, SID=xe
	// private static final String connStr =
	// "jdbc:oracle:thin@10.111.64.21:1521/odb";

	// static String connectionURL =
	// "jdbc:oracle:thin:"++"/ver8i@10.111.64.21:1521/odb";

	// Connect to DB
	public static void dbConnect() throws SQLException, ClassNotFoundException {
		@SuppressWarnings("unused")
		Connect con = new Connect();

		// Setting Oracle JDBC Driver
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Где находится ваш драйвер Oracle JDBC?");
			e.printStackTrace();
			throw e;
		}

		System.out.println("Драйвер Oracle JDBC зарегистрирован!");

		// Establish the Oracle Connection using Connection String
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@"
					+ Connect.connectionURL_ + "");
		} catch (SQLException e) {
			System.out.println("Соединение не установлено! Проверьте вывод консоли" + e);
			e.printStackTrace();
			throw e;
		}
	}

	// Close Connection
	public static void dbDisconnect() throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// DB Execute Query Operation
	public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
		// Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		CachedRowSetImpl crs = null;
		try {
			// Connect to DB (Establish Oracle Connection)
			dbConnect();
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
			System.out.println("Проблема возникла при выполнении executeQuery : " + e);
			throw e;
		} finally {
			if (resultSet != null) {
				// Close resultSet
				resultSet.close();
			}
			if (stmt != null) {
				// Close Statement
				stmt.close();
			}
			// Close connection
			dbDisconnect();
		}
		// Return CachedRowSet
		return crs;
	}

	// DB Execute Update (For Update/Insert/Delete) Operation
	public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
		// Declare statement as null
		Statement stmt = null;
		try {
			// Connect to DB (Establish Oracle Connection)
			dbConnect();
			// Create Statement
			stmt = conn.createStatement();
			// Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
		} catch (SQLException e) {
			System.out.println("Проблема возникла при выполнении executeUpdate : " + e);
			throw e;
		} finally {
			if (stmt != null) {
				// Close statement
				stmt.close();
			}
			// Close connection
			dbDisconnect();
		}
	}
}

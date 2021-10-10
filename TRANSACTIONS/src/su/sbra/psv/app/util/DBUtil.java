package su.sbra.psv.app.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.SqlMap;
import su.sbra.psv.app.utils.DbUtil;

@SuppressWarnings("restriction")
public class DBUtil {
	// Declare JDBC Driver
	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	// Connection
	public static Connection conn = null;

	// Connect to DB
	public static void dbConnect() throws ClassNotFoundException, SQLException, UnknownHostException {
		// Setting Oracle JDBC Driver
		Class.forName(JDBC_DRIVER);
		Main.logger = Logger.getLogger(DBUtil.class);
		// Establish the Oracle Connection using Connection String

		Properties props = new Properties();
		props.setProperty("password", Connect.userPassword_);
		props.setProperty("user", Connect.userID_);
		props.put("v$session.osuser", System.getProperty("user.name").toString());
		props.put("v$session.action", "DDD");
		props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
		props.put("v$session.program", DBUtil.class.getName());
		conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
		conn.setAutoCommit(false);
		//DBUtil.ExecPlSql("begin dbms_application_info.set_action('q'); end;",conn);
	}

	// Close Connection
	public static void dbDisconnect() {
		try {
			Main.logger = Logger.getLogger(DBUtil.class);
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				conn.close();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	// DB Execute Query Operation
	public static ResultSet dbExecuteQuery(String queryStmt) throws ClassNotFoundException, UnknownHostException {
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
			DbUtil.Log_Error(e); 
			Main.logger.error(ExceptionUtils.getStackTrace(e));
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
					Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
				}
			}
		}
		// Return CachedRowSet
		return crs;
	}

	public static Integer ODB_ACTION(Integer usrid, Integer actid) {
		Main.logger = Logger.getLogger(DBUtil.class);
		Integer ret = 0;
		Connection conn = DBUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbAccess(?,?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setInt(2, usrid);
			callStmt.setInt(3, actid);
			callStmt.execute();
			ret = callStmt.getInt(1);
			callStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	public static Integer OdbAction(Integer actid) {
		Main.logger = Logger.getLogger(DBUtil.class);
		Integer ret = 0;
		Connection conn = DBUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbAccess(?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setInt(2, actid);
			callStmt.execute();
			ret = callStmt.getInt(1);
			callStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/* Проверка прав доступа */
	public static int chk_accesss(String ACT_NAME, String CUSRLOGNAME) {
		Main.logger = Logger.getLogger(DBUtil.class);
		int ret = 0;
		Connection conn = DBUtil.conn;
		try {
			SqlMap sql = new SqlMap().load("/SQL.xml");
			String readRecordSQL = sql.getSql("acces_act");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, ACT_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			prepStmt.close();
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	public static void Rollback() {
		try {
			try {
				conn.rollback();
			} catch (SQLException e) {
				DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
				Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	public static void Commit() {
		try {
			conn.commit();
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	// DB Execute Update (For Update/Insert/Delete) Operation
	public static void dbExecuteUpdate(String sqlStmt) {
		// Declare statement as null
		Statement stmt = null;
		Main.logger = Logger.getLogger(DBUtil.class);
		try {
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				dbConnect();
			}
			// Create Statement
			stmt = conn.createStatement();
			// Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
			conn.commit();
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		} finally {
			if (stmt != null) {
				// Close statement
				try {
					stmt.close();
				} catch (SQLException e) {
					DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
					Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
				}
			}
		}
	}

	public static void ExecPlSql(String sqlStmt, Connection conn) {
		Statement stmt = null;
		Main.logger = Logger.getLogger(DBUtil.class);
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlStmt);
			conn.commit();
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
}

package su.sbra.psv.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.model.SqlMap;
import su.sbra.psv.app.sbalert.Msg;


/**
 * Класс для работы с БД <br>
 * Доступ...
 * 
 * @author Said
 *
 */
public class DbUtil {

	public DbUtil() {
		Main.logger = Logger.getLogger(getClass());
	}

	// Declare JDBC Driver
	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	// Connection
	public static Connection conn = null;

	// Connect to DB
	public static void Db_Connect() throws ClassNotFoundException, SQLException {
			// Setting Oracle JDBC Driver
			Class.forName(JDBC_DRIVER);
			// Establish the Oracle Connection using Connection String
			Properties props = new Properties();
			props.put("v$session.program", DbUtil.class.getName());
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
	}

	// Close Connection
	public static void Db_Disconnect() {
		try {
			Main.logger = Logger.getLogger(DbUtil.class);
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	public static String Sql_From_Prop(String path, String prpname) {
		String ret = null;
		try {
			InputStream is = DbUtil.class.getResourceAsStream(path);
			Properties props = new Properties();
			props.load(is);
			ret = props.getProperty(prpname);
			is.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return ret;
	}

	public static boolean Check_Connect() {
		boolean ret = true;
		try {
			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_);
			conn.close();
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}


	/**
	 * Запись лога
	 */
	public static void Log_To_Db(Long linenumber, String classname, String error, String METHODNAME) {
		try {
			if (linenumber != null & (classname != null && !classname.equals("")) & (error != null && !error.equals(""))
					& (METHODNAME != null && !METHODNAME.equals(""))) {
//				Class.forName(JDBC_DRIVER);
//				Properties props = new Properties();
//				props.put("v$session.program", DbUtil.class.getName());
//				Connection conn = DriverManager.getConnection(
//						"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
//						props);
//				conn.setAutoCommit(false);
//				PreparedStatement stmt = conn.prepareStatement(" declare\n" + "pragma autonomous_transaction; begin \n"
//						+ " insert into logs (\n" + "linenumber, \n" + "classname, \n" + "error,METHODNAME) \n"
//						+ "values\n" + "(?,?,?,?);\n" + "commit;\n" + "end;");
//				stmt.setLong(1, linenumber);
//				stmt.setString(2, classname);
//				Clob lob = conn.createClob();
//				lob.setString(1, error);
//				stmt.setClob(3, lob);
//				stmt.setString(4, METHODNAME);
//				stmt.executeUpdate();
//				stmt.close();
//				conn.close();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Запись "залоченной" строки
	 */
	public static String Lock_Row(Long Id, String TableName, Connection conn) {
		String ret = null;
		try {
			if (Id != null & (TableName != null && !TableName.equals(""))) {
				CallableStatement callStmt = conn.prepareCall("{ call LOCKS.LOCK_ROW_ADD(?,?,?,?)}");
				/* Ошибка */
				callStmt.registerOutParameter(4, Types.VARCHAR);
				/* первичный ключ */
				callStmt.setLong(3, Id);
				/* Пользователь */ callStmt.setString(2, Connect.userID_.toUpperCase());
				callStmt.setString(1, TableName.toUpperCase());
				callStmt.execute();
				ret = callStmt.getString(4);
				callStmt.close();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	public static void Log_Error(Exception e) {
		// Если есть соединение или пока нет
		if (conn != null || Check_Connect()) {
			e.printStackTrace();
			String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			Long lineNumber = (long) Thread.currentThread().getStackTrace()[2].getLineNumber();
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			if (!Connect.userID_.toLowerCase().equals("xxi")) {
				Log_To_Db(lineNumber, fullClassName, ExceptionUtils.getStackTrace(e), methodName);
			}
		} else {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Кто залочил
	 */
	public static String Lock_Row_View(Long Id, String TableName) {
		String ret = null;
		String error = null;
		try {
			if (Id != null & (TableName != null && !TableName.equals(""))) {
				CallableStatement callStmt = conn.prepareCall("{ call LOCKS.LOCK_ROW_VIEW(?,?,?,?)}");
				callStmt.setString(1, TableName.toUpperCase());
				callStmt.setLong(2, Id);
				callStmt.registerOutParameter(3, Types.VARCHAR);
				callStmt.registerOutParameter(4, Types.VARCHAR);
				callStmt.execute();
				ret = callStmt.getString(3);
				error = callStmt.getString(4);
				callStmt.close();
				if (error != null)
					Msg.Message(error);
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	/**
	 * Удалить инф. о залоченной строке
	 */
	public static String Lock_Row_Delete(Long Id, String TableName, Connection conn) {
		String ret = null;
		try {
			if (Id != null & (TableName != null && !TableName.equals(""))) {
				CallableStatement callStmt = conn.prepareCall("{ call LOCKS.LOCK_ROW_DEL(?,?,?,?)}");
				callStmt.setString(1, TableName.toUpperCase());
				callStmt.setString(2, Connect.userID_.toUpperCase());
				callStmt.setLong(3, Id);
				callStmt.registerOutParameter(4, Types.VARCHAR);
				callStmt.execute();
				ret = callStmt.getString(4);
				callStmt.close();
				// System.out.println("~~~~~~~~~~");
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	// DB Execute Query Operation
	public static ResultSet Db_Execute_Query(String queryStmt) {
		// Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			Main.logger = Logger.getLogger(DbUtil.class);
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				Db_Connect();
			}
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(queryStmt);

			stmt.close();
			resultSet.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		} finally {
			if (resultSet != null) {
				// Close resultSet
				try {
					resultSet.close();
				} catch (SQLException e) {
					Msg.Message(ExceptionUtils.getStackTrace(e));
				}
			}
			if (stmt != null) {
				// Close Statement
				try {
					stmt.close();
				} catch (SQLException e) {
					DbUtil.Log_Error(e);
				}
			}
		}
		// Return CachedRowSet
		return resultSet;
	}

	public static Long Odb_Aaction(Long usrid, Long actid) {
		Main.logger = Logger.getLogger(DbUtil.class);
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbAccess(?,?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setLong(2, usrid);
			callStmt.setLong(3, actid);
			callStmt.execute();
			ret = callStmt.getLong(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	/**
	 * Учреждение, Загс, Нотариус, Оба...
	 * 
	 * @return
	 */
	public static String Access_Level() {
		String ret = null;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.ACC_LEV(?)}");
			callStmt.registerOutParameter(1, Types.VARCHAR);
			callStmt.setString(2, Connect.userID_.toUpperCase());
			callStmt.execute();
			ret = callStmt.getString(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	public static Long Odb_Mnu(Long usrid, Long actid) {
		Main.logger = Logger.getLogger(DbUtil.class);
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbMnuAccess(?,?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setLong(2, usrid);
			callStmt.setLong(3, actid);
			callStmt.execute();
			ret = callStmt.getLong(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	public static Long Odb_Mnu_Grp(Long grpid, Long actid) {
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbMnuAccessGrp(?,?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setLong(2, grpid);
			callStmt.setLong(3, actid);
			callStmt.execute();
			ret = callStmt.getLong(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	public static Long Odb_Act_Grp(Long grpid, Long actid) {
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.ODB_ACT_ACCESS_GRP(?,?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setLong(2, grpid);
			callStmt.setLong(3, actid);
			callStmt.execute();
			ret = callStmt.getLong(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	public static Long Odb_Mnu2(Long actid) {
		Main.logger = Logger.getLogger(DbUtil.class);
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.OdbMnuAccess(?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setLong(2, actid);
			callStmt.execute();
			ret = callStmt.getLong(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	/**
	 * Проверка доступа к действию
	 * 
	 * @param actid
	 * @return
	 */
	public static Long Odb_Action(Long actid) {
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.ACT_ACCESS(?)}");
			callStmt.registerOutParameter(1, Types.INTEGER);
			callStmt.setLong(2, actid);
			callStmt.execute();
			ret = callStmt.getLong(1);
			callStmt.close();
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	/**
	 * Проверка прав доступа
	 * 
	 * @param ACT_NAME
	 * @param CUSRLOGNAME
	 * @return
	 */
	public static Long Chk_Accesss(String ACT_NAME, String CUSRLOGNAME) {
		Main.logger = Logger.getLogger(DbUtil.class);
		Long ret = 0l;
		Connection conn = DbUtil.conn;
		try {
			SqlMap sql = new SqlMap().load("/SQL.xml");
			String readRecordSQL = sql.getSql("acces_act");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, ACT_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getLong("CNT");
			}
			prepStmt.close();
			rs.close();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	/**
	 * Откат
	 */
	public static void Rollback() {
		try {
			try {
				conn.rollback();
			} catch (SQLException e) {
				DbUtil.Log_Error(e);
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Фиксация
	 */
	public static void Commit() {
		try {
			conn.commit();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	// DB Execute Update (For Update/Insert/Delete) Operation
	public static void Db_Execute_Update(String sqlStmt) {
		// Declare statement as null
		Statement stmt = null;
		Main.logger = Logger.getLogger(DbUtil.class);
		try {
			// Connect to DB (Establish Oracle Connection)
			if (conn == null && !conn.isClosed()) {
				Db_Connect();
			}
			// Create Statement
			stmt = conn.createStatement();
			// Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
			conn.commit();
			stmt.close();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		} finally {
			if (stmt != null) {
				// Close statement
				try {
					stmt.close();
				} catch (SQLException e) {
					DbUtil.Log_Error(e);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static String getResource(final String path) {
		final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		try {
			return IOUtils.toString(stream, "UTF-8");
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}

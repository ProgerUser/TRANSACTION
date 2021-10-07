package su.sbra.psv.ispirer.test;
import java.sql.*;
import java.math.*;

public class ZSbCardIden {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ZSbCardIden.class);
	public void spZSbCardIden(BigDecimal num)  throws SQLException, Exception {
		Connection mConn = JDBCDataSource.getConnection();
		mConn.setAutoCommit(false);

		try {
			try(PreparedStatement mStmt = mConn.prepareStatement("UPDATE Z_SB_STOP_IDEN \r\n" +
				                              " SET STATUS       =  ?, \r\n" +
				                              " IP           = SYS_CONTEXT('USERENV', 'IP_ADDRESS'), \r\n" +
				                              " MACHINE_NAME = SYS_CONTEXT('USERENV', 'HOST'), \r\n" +
				                              " OS_USER      = SYS_CONTEXT('USERENV', 'OS_USER'), \r\n" +
				                              " ID_SESSIONID = USERENV('SESSIONID'), \r\n" +
				                              " IDEN_DT      = SYSDATE");) {
				mStmt.setBigDecimal(1, num);
				SQLCursorHandler.getInstance().setRowcount(mStmt.executeUpdate());
			}
			catch (SQLException se) {
				SQLCursorHandler.getInstance().handleExeption(se);
				throw se;
			}

			if (!mConn.getAutoCommit()) {
				mConn.commit();
			}
		}
		catch (Exception e) {
			LOGGER.error(String.valueOf(e));
			throw e;
		}
		finally {
			if (mConn != null) {
				mConn.close();
			}
		}
	}
}
package su.sbra.psv.app.loadamra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbmsOutputCapture implements AutoCloseable {
	private static final int DEFAULT_LINE_BUF_SZ = 1024;
	private int lineBufferSize;
	private CallableStatement enableStmt;
	private CallableStatement readLineStmt;
	private CallableStatement disableStmt;

	public DbmsOutputCapture(Connection dbConn) throws SQLException {
		this(dbConn, DEFAULT_LINE_BUF_SZ);
	}

	public DbmsOutputCapture(Connection dbConn, int lineBufferSize) throws SQLException {
		this.lineBufferSize = lineBufferSize;
		enableStmt = dbConn.prepareCall("begin dbms_output.enable(NULL); end;");
		disableStmt = dbConn.prepareCall("begin dbms_output.disable(); end;");
		readLineStmt = dbConn.prepareCall("begin dbms_output.get_lines(?, ?); end;");
		readLineStmt.registerOutParameter(1, Types.ARRAY, "DBMSOUTPUT_LINESARRAY");
		readLineStmt.registerOutParameter(2, Types.INTEGER, "INTEGER");
		readLineStmt.setInt(2, lineBufferSize);
	}

	public List<String> execute(CallableStatement userCall) throws SQLException {
		List<String> retLines = new ArrayList<>();
		try {
			enableStmt.executeUpdate();
			userCall.execute();
			int fetchedLines;
			do {
				readLineStmt.execute();
				fetchedLines = readLineStmt.getInt(2);
				Array array = null;
				try {
					array = readLineStmt.getArray(1);
					String[] lines = (String[]) array.getArray();
					/* loop over number of returned lines, not array size */
					for (int i = 0; i < fetchedLines; i++) {
						String line = lines[i];
						retLines.add(line != null ? i+": "+line+"\r\n" : "");
					}
				} finally {
					if (array != null) {
						array.free();
					}
				}
			} while (fetchedLines == lineBufferSize);
		} finally {
			disableStmt.execute();
		}
		return retLines;
	}

	@Override
	public void close() throws SQLException {
		if (!quietClose(enableStmt, readLineStmt, disableStmt)) {
			throw new SQLException("Could not close all callable statements");
		}
	}

	private boolean quietClose(CallableStatement... callableStatements) {
		boolean allSuccess = true;
		for (CallableStatement stmt : callableStatements) {
			try {
				stmt.close();
			} catch (SQLException e) {
				allSuccess = false;
			}
		}
		return allSuccess;
	}
}
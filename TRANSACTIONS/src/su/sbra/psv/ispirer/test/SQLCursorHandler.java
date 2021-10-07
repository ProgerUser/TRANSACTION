package su.sbra.psv.ispirer.test;

import java.sql.SQLException;

public class SQLCursorHandler {
	private static SQLCursorHandler instance = new SQLCursorHandler();
	private Integer errorCode = 0;
	private String sqlState = "";
	private Boolean notFound = null;
	private Boolean found = null;
	private Boolean open = null;
	private Integer rowcount = null;

	private SQLCursorHandler() {
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getSqlState() {
		return sqlState;
	}

	public boolean isNotFound() {
		return notFound;
	}

	public void setNotFound() {
		this.notFound = true;
		this.found = false;
		this.open = false;
		this.errorCode = 0;
		this.sqlState = "";
		this.rowcount = 0;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound() {
		this.found = true;
		this.notFound = false;
		this.open = false;
		this.errorCode = 0;
		this.sqlState = "";
		this.rowcount = 1;
	}

	public boolean isOpen() {
		return open;
	}

	public Integer getRowcount() {
		return rowcount;
	}

	public void setRowcount(int rowcount) {
		this.rowcount = rowcount;
		this.open = false;
		this.errorCode = 0;
		this.sqlState = "";
		this.found = rowcount > 0;
		this.notFound = rowcount == 0;
	}

	public void handleExeption(final SQLException exc) {
		this.errorCode = exc.getErrorCode();
		this.sqlState = exc.getSQLState();
		this.rowcount = 0;
		this.found = false;
		this.notFound = true;
		this.open = false;
	}

	public static SQLCursorHandler getInstance() {
		return instance;
	}
}

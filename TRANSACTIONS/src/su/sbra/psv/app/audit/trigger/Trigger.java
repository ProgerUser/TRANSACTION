package su.sbra.psv.app.audit.trigger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.utils.DbUtil;

public class Trigger {

	@FXML
	private TextArea TriggerText;

	@FXML
	void Execute(ActionEvent event) {
		try {
			String exec = " begin execute immediate ? ; end;";
			PreparedStatement execimm = conn.prepareStatement(exec);
			execimm.setString(1, TriggerText.getText());
			execimm.executeUpdate();
			execimm.close();
			onclose();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				Msg.Message(ExceptionUtils.getStackTrace(e1));
				Main.logger.error(ExceptionUtils.getStackTrace(e1) + "~" + Thread.currentThread().getName());
			}
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Отмена
	 * 
	 * @param event
	 */
	@FXML
	void Cencel(ActionEvent event) {
		onclose();
	}

	/**
	 * Закрытие формы
	 */
	void onclose() {
		Stage stage = (Stage) TriggerText.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			Main.logger = Logger.getLogger(getClass());
			dbConnect();

			INIT();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	void INIT() {
		try {
			String sql = "";
			Main.logger = Logger.getLogger(getClass());
			String selectStmt = "select CTEXT, CTABLE, NLINE\n" + "  from V_AU_TRIGGER2 t\n" + " where CTABLE = ?\n";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			prepStmt.setString(1, TableName);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				sql = sql + ((rs.getString("CTEXT") != null) ? rs.getString("CTEXT") : "") + "\r\n";
			}
			prepStmt.close();
			rs.close();
			TriggerText.setText(sql);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Строка соединения
	 */
	private Connection conn;

	/**
	 * Открыть сессию
	 * @throws UnknownHostException 
	 */
	private void dbConnect() throws UnknownHostException {
		try {
			Main.logger = Logger.getLogger(getClass());
			Class.forName("oracle.jdbc.OracleDriver");
			
			Properties props = new Properties();
			props.setProperty("password", Connect.userPassword_);
			props.setProperty("user", Connect.userID_);
			props.put("v$session.osuser", System.getProperty("user.name").toString());
			props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
			
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Закрыть
	 */
	public void dbDisconnect() {
		try {
			Main.logger = Logger.getLogger(getClass());
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

	private BooleanProperty Status;

	public void setStatus(Boolean value) {
		this.Status.set(value);
	}

	public boolean getStatus() {
		return this.Status.get();
	}

	public Trigger() {
		Main.logger = Logger.getLogger(getClass());
		this.Status = new SimpleBooleanProperty();
	}

	String TableName;

	public void SetTableName(String tbl) {
		this.TableName = tbl;
	}

}

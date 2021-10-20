package su.sbra.psv.app.access.grp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.utils.DbUtil;

public class AddGrp {

	public AddGrp() {
		Main.logger = Logger.getLogger(getClass());
	}

	@FXML
	private TextField GRP_NAME;

	@FXML
	private TextField NAME;

	@FXML
	void AddUpdate(ActionEvent event) {
		try {
			PreparedStatement prp = conn.prepareStatement("insert into ODB_GROUP_USR (grp_id,GRP_NAME,NAME) values ((SELECT NVL(MAX(grp_id), 0) + 1 FROM ODB_GROUP_USR),?,?) ");
			prp.setString(1, GRP_NAME.getText());
			prp.setString(2, NAME.getText());
			prp.executeUpdate();
			prp.close();
			conn.commit();
			onclose();
		} catch (Exception e) {
			DbUtil.Log_Error(e); 
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	void onclose() {
		Stage stage = (Stage) NAME.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	@FXML
	void Cencel(ActionEvent event) {
		onclose();
	}

	@FXML
	private void initialize() {
		try {
			dbConnect();
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private Connection conn;

	private void dbConnect() throws UnknownHostException {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			Properties props = new Properties();
			props.setProperty("password", Connect.userPassword_);
			props.setProperty("user", Connect.userID_);
			props.put("v$session.osuser", System.getProperty("user.name").toString());
			props.put("v$session.machine", InetAddress.getLocalHost().getHostAddress());
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
			
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				conn.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}

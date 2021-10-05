package app.admin.usr;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.apache.log4j.Logger;

import app.Main;
import app.model.Connect;
import app.sbalert.Msg;
import app.utils.DbUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddUser {

	@FXML
	private Button saveaddusr;

	@FXML
	private PasswordField PASS;

	@FXML
	private TextField CUSRNAME;

	@FXML
	private TextField CUSRLOGNAME;

	private BooleanProperty Status;
	private LongProperty Id;

	public void setStatus(Boolean value) {
		this.Status.set(value);
	}

	public boolean getStatus() {
		return this.Status.get();
	}

	public void setId(Long value) {
		this.Id.set(value);
	}

	public Long getId() {
		return this.Id.get();
	}

	public AddUser() {
		Main.logger = Logger.getLogger(getClass());
		this.Status = new SimpleBooleanProperty();
		this.Id = new SimpleLongProperty();
	}

	/**
	 * �������� �����
	 */
	void onclose() {
		Stage stage = (Stage) CUSRNAME.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@FXML
	private void initialize() {
		dbConnect();
		UpperCase(CUSRLOGNAME);
		FirstWUpp(CUSRNAME);
	}

	/**
	 * ������ ����� ���������
	 * 
	 * @param TxtFld
	 */
	void FirstWUpp(TextField TxtFld) {
		TxtFld.textProperty().addListener((ov, oldValue, newValue) -> {
			if (newValue.length() > 0) {
				TxtFld.setText(upperCaseAllFirst(newValue));
			}
		});
	}

	/**
	 * ������ ����� ���������
	 * 
	 * @param value
	 * @return
	 */
	public static String upperCaseAllFirst(String value) {
		char[] array = value.toCharArray();
		// Uppercase first letter.
		array[0] = Character.toUpperCase(array[0]);
		// Uppercase all letters that follow a whitespace character.
		for (int i = 1; i < array.length; i++) {
			if (Character.isWhitespace(array[i - 1])) {
				array[i] = Character.toUpperCase(array[i]);
			}
		}
		return new String(array);
	}

	/**
	 * � ������� ��������
	 * 
	 * @param tf
	 */
	void UpperCase(TextField tf) {
		tf.setTextFormatter(new TextFormatter<>((change) -> {
			change.setText(change.getText().toUpperCase());
			return change;
		}));
	}

	@FXML
	void save(ActionEvent event) {
		try {
			Main.logger = Logger.getLogger(getClass());
			CallableStatement callStmt = conn.prepareCall("{ ? = call MJUsers.createusr(?,?,?)}");
			callStmt.registerOutParameter(1, Types.VARCHAR);
			callStmt.setString(2, CUSRLOGNAME.getText());
			callStmt.setString(3, CUSRNAME.getText());
			callStmt.setString(4, PASS.getText());
			callStmt.execute();
			String ret = callStmt.getString(1);
			if (ret.equals("ok")) {
				setStatus(true);
				conn.commit();
				Msg.Message("������������ " + CUSRLOGNAME.getText() + " ������");
				Main.logger.info("������������ " + CUSRLOGNAME + " ������" + "~" + Thread.currentThread().getName());
				callStmt.close();
				onclose();
			} else {
				conn.rollback();
				setStatus(false);
				Msg.Message("CreateUser");
				callStmt.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * ������
	 */
	private Connection conn;

	/**
	 * ������� ������
	 */
	private void dbConnect() {
		try {
			Main.logger = Logger.getLogger(getClass());
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program",getClass().getName());
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * ��������� ������
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
}

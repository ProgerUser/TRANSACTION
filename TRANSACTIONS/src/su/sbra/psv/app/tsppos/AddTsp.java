package su.sbra.psv.app.tsppos;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class AddTsp {

	@FXML
	private MaskedTextField TERM_ID;
	@FXML
	private ComboBox<String> TERM_MODEL;
	@FXML
	private TextField TERM_ADDR;
	@FXML
	private CheckBox TERM_INTEGRATION;
	@FXML
	private TextField TERM_SERIAL;
	@FXML
	private ComboBox<String> TERM_PORTHOST;
	@FXML
	private TextField TERM_GEO;
	@FXML
	private TextField TERM_IPIFNOTSIM;
	@FXML
	private DatePicker TERM_REGDATE;
	@FXML
	private ComboBox<String> TERM_SIM_OPER;
	@FXML
	private MaskedTextField TERM_SIM_NUMBER;
	@FXML
	private TextField TERM_SIM_IP;
	@FXML
	private TextField SDNAME;
	@FXML
	private TextField TERM_COMMENT;
	@FXML
	private MaskedTextField TERM_KTM;
	@FXML
	private ComboBox<String> TERM_TYPE;
	@FXML
	private Button Ok;

	/**
	 * Отмена
	 * 
	 * @param event
	 */
	@FXML
	void Cencel(ActionEvent event) {
		try {
			OnClose();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * При закрытии
	 */
	void OnClose() {
		Stage stage = (Stage) Ok.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	public void setId(Long temp) {
		this.seltemp = temp;
	}
	
	public long getId() {
		return (this.seltemp == null) ? -1 : this.seltemp;
	}

	Long seltemp;
	
	/**
	 * OK
	 * 
	 * @param event
	 */
	@FXML
	void Ok(ActionEvent event) {
		try {
			CallableStatement callStmt = DBUtil.conn.prepareCall(
					  "begin "
					+ "INSERT INTO SBRA_TSP_POS \r\n"
					+ "  (TERM_MODEL,\r\n"
					+ "   TERM_ADDR,\r\n"
					+ "   TERM_INTEGRATION,\r\n"
					+ "   TERM_SERIAL,\r\n"
					+ "   TERM_ID,\r\n"
					+ "   TERM_SIM_NUMBER,\r\n"
					+ "   TERM_SIM_OPER,\r\n"
					+ "   TERM_SIM_IP,\r\n"
					+ "   TERM_REGDATE,\r\n"
					+ "   TERM_COMMENT,\r\n"
					+ "   TERM_GEO,\r\n"
					+ "   TERM_PORTHOST,\r\n"
					+ "   TERM_IPIFNOTSIM,\r\n"
					+ "   TERM_KTM,\r\n"
					+ "   TERM_TYPE)\r\n"
					+ "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id into ?; "
					+ "end;");

			callStmt.setString(1, TERM_MODEL.getSelectionModel().getSelectedItem());
			callStmt.setString(2, TERM_ADDR.getText());
			callStmt.setInt(3, (TERM_INTEGRATION.isSelected()) ? 1 : 0);
			callStmt.setString(4, TERM_SERIAL.getText());
			callStmt.setString(5, TERM_ID.getText());
			callStmt.setString(6, TERM_SIM_NUMBER.getText());
			callStmt.setString(7, TERM_SIM_OPER.getSelectionModel().getSelectedItem());
			callStmt.setString(8, TERM_SIM_IP.getText());
			callStmt.setDate(9, ((TERM_REGDATE.getValue() == null) ? null : java.sql.Date.valueOf(TERM_REGDATE.getValue())));
			callStmt.setString(10, TERM_COMMENT.getText());
			callStmt.setString(11, TERM_GEO.getText());
			callStmt.setString(12, TERM_PORTHOST.getSelectionModel().getSelectedItem());
			callStmt.setString(13, TERM_IPIFNOTSIM.getText());
			
			if (!TERM_KTM.getText().equals("")) {
				callStmt.setLong(14, Long.valueOf(TERM_KTM.getText()));
			} else {
				callStmt.setNull(14, java.sql.Types.INTEGER);
			}
			
			if (TERM_TYPE.getSelectionModel().getSelectedItem().equals("ТСП")) {
				callStmt.setLong(15, 3);
			} else if (TERM_TYPE.getSelectionModel().getSelectedItem().equals("ПВН")) {
				callStmt.setLong(15, 2);
			}
			
			callStmt.registerOutParameter(16, Types.INTEGER);
			callStmt.execute();
			
			
			Long retid = callStmt.getLong(16);
			
			setId(retid);
			
			callStmt.close();
			DBUtil.conn.commit();
			// -----------------------------------
			OnClose();
			// -----------------------------------
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

    
	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			
			TERM_ID.setTextFormatter(new TextFormatter<>((change) -> {
				change.setText(change.getText().toUpperCase());
				return change;
			}));
			// ------------------------
			ObservableList<String> model = FXCollections.observableArrayList();
			{
				PreparedStatement prp = DBUtil.conn.prepareStatement(
						"SELECT TERM_MODEL\r\n"
						+ "  FROM SBRA_TSP_POS t\r\n" 
						+ " WHERE t.TERM_MODEL IS NOT NULL\r\n" 
						+ " GROUP BY TERM_MODEL");
				ResultSet rs = prp.executeQuery();
				while (rs.next()) {
					model.add(rs.getString("TERM_MODEL"));
				}
				rs.close();
				prp.close();
			}
			ObservableList<String> port = FXCollections.observableArrayList();
			{
				PreparedStatement prp = DBUtil.conn
						.prepareStatement(
								"SELECT t.TERM_PORTHOST\r\n" 
						    + "  FROM SBRA_TSP_POS t\r\n"
						    + " WHERE t.TERM_PORTHOST IS NOT NULL\r\n" 
						    + " GROUP BY TERM_PORTHOST");
				ResultSet rs = prp.executeQuery();
				while (rs.next()) {
					port.add(rs.getString("TERM_PORTHOST"));
				}
				rs.close();
				prp.close();
			}

			this.TERM_MODEL.setItems(model);
			this.TERM_PORTHOST.setItems(port);
			String operators[] = { "Aquafon", "A-mobile" };
			this.TERM_SIM_OPER.setItems(FXCollections.observableArrayList(operators));
			
			String types[] = { "ТСП", "ПВН" };
			this.TERM_TYPE.setItems(FXCollections.observableArrayList(types));

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

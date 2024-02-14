package su.sbra.psv.app.tsppos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class EditTsp {

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

	/**
	 * OK
	 * 
	 * @param event
	 */
	@FXML
	void Ok(ActionEvent event) {
		try {
			PreparedStatement prp = DBUtil.conn.prepareStatement(
					"UPDATE SBRA_TSP_POS SET\r\n"
					+ "TERM_MODEL = ?, \r\n"
					+ "TERM_ADDR = ?, \r\n"
					+ "TERM_INTEGRATION = ?, \r\n"
					+ "TERM_SERIAL = ?, \r\n"
					+ "TERM_ID = ?, \r\n"
					+ "TERM_SIM_NUMBER = ?, \r\n"
					+ "TERM_SIM_OPER = ?, \r\n"
					+ "TERM_SIM_IP = ?, \r\n"
					+ "TERM_REGDATE = ?, \r\n"
					+ "TERM_COMMENT = ?, \r\n"
					+ "TERM_GEO = ?, \r\n"
					+ "TERM_PORTHOST = ?, \r\n"
					+ "TERM_IPIFNOTSIM =?\r\n"
					+ "WHERE ID = ?");
			
			prp.setString(1, TERM_MODEL.getSelectionModel().getSelectedItem());
			prp.setString(2, TERM_ADDR.getText());
			prp.setInt(3, (TERM_INTEGRATION.isSelected()) ? 1 : 0);
			prp.setString(4, TERM_SERIAL.getText());
			prp.setString(5, TERM_ID.getText());
			prp.setString(6, TERM_SIM_NUMBER.getText());
			prp.setString(7, TERM_SIM_OPER.getSelectionModel().getSelectedItem());
			prp.setString(8, TERM_SIM_IP.getText());
			prp.setDate(9,
					((TERM_REGDATE.getValue()== null) ? null : java.sql.Date.valueOf(TERM_REGDATE.getValue())));
			prp.setString(10, TERM_COMMENT.getText());
			prp.setString(11, TERM_GEO.getText());
			prp.setString(12, TERM_PORTHOST.getSelectionModel().getSelectedItem());
			prp.setString(13, TERM_IPIFNOTSIM.getText());
			prp.setLong(14, tsp.getID());

			prp.executeUpdate();
			prp.close();
			DBUtil.conn.commit();
			// -----------------------------------
			OnClose();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * класс
	 */
	SBRA_TSP_POS tsp = null;

	void SetClass(SBRA_TSP_POS tsp) {
		try {
			this.tsp = tsp;
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
				PreparedStatement prp = DBUtil.conn
						.prepareStatement(
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
			// -----------------------------
			this.TERM_MODEL.setItems(model);
			this.TERM_MODEL.getSelectionModel().select(tsp.getTERM_MODEL());
			this.TERM_ID.setText(tsp.getTERM_ID());
			this.TERM_ADDR.setText(tsp.getTERM_ADDR());

			if (tsp.getTERM_INTEGRATION().equals(1l)) {
				TERM_INTEGRATION.setSelected(true);
			} else {
				TERM_INTEGRATION.setSelected(false);
			}
			this.TERM_SERIAL.setText(tsp.getTERM_SERIAL());
			this.TERM_REGDATE.setValue(tsp.getTERM_REGDATE());

			this.TERM_PORTHOST.setItems(port);
			this.TERM_PORTHOST.getSelectionModel().select(tsp.getTERM_PORTHOST());
			this.TERM_GEO.setText(tsp.getTERM_GEO());
			this.TERM_IPIFNOTSIM.setText(tsp.getTERM_IPIFNOTSIM());
			this.TERM_SERIAL.setText(tsp.getTERM_SERIAL());

			String operators[] = { "Aquafon", "A-mobile" };
			this.TERM_SIM_OPER.setItems(FXCollections.observableArrayList(operators));
			this.TERM_SIM_OPER.getSelectionModel().select(tsp.getTERM_SIM_OPER());
			this.TERM_SIM_NUMBER.setText(tsp.getTERM_SIM_NUMBER());
			this.TERM_SIM_IP.setText(tsp.getTERM_SIM_IP());
			this.TERM_COMMENT.setText(tsp.getTERM_COMMENT());
			

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

package su.sbra.psv.app.terminals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.util.DBUtil;

public class EditTerm {

	@FXML
	private TextField NAME;
	@FXML
	private ComboBox<Integer> DEPARTMENT;
	@FXML
	private TextField ADDRESS;
	@FXML
	private TextField ACCOUNT;
	@FXML
	private TextField GENERAL_ACC;
	@FXML
	private TextField CRASH_ACC;
	@FXML
	private TextField DEAL_ACC;
	@FXML
	private TextField GENERAL_COMIS;
	@FXML
	private TextField CLEAR_SUM;
	@FXML
	private TextField INCOME;
	@FXML
	private TextField SDNAME;
	// ----------------------
	@FXML
	private Text ACCOUNT_T;
	@FXML
	private Text GENERAL_ACC_T;
	@FXML
	private Text CRASH_ACC_T;
	@FXML
	private Text DEAL_ACC_T;
	@FXML
	private Text GENERAL_COMIS_T;
	@FXML
	private Text CLEAR_SUM_T;
	@FXML
	private Text INCOME_T;
	// ----------------------
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
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
			PreparedStatement prp = DBUtil.conn.prepareStatement(""
					+ "update Z_SB_TERMINAL_AMRA_DBT "
					+ "DEPARTMENT = ?,\r\n"
					+ "ADDRESS = ?,\r\n"
					+ "ACCOUNT = ?,\r\n"
					+ "GENERAL_ACC = ?,\r\n"
					+ "CRASH_ACC = ?,\r\n"
					+ "DEAL_ACC = ?,\r\n"
					+ "GENERAL_COMIS = ?,\r\n"
					+ "CLEAR_SUM = ?,\r\n"
					+ "INCOME = ?,\r\n"
					+ "SDNAME  = ? 'r'n"
					+ "where NAME = ?");
			prp.setInt(1, DEPARTMENT.getSelectionModel().getSelectedItem());
			prp.setString(2, ADDRESS.getText());
			prp.setString(3, ACCOUNT.getText());
			prp.setString(4, GENERAL_ACC.getText());
			prp.setString(5, CRASH_ACC.getText());
			prp.setString(6, DEAL_ACC.getText());
			prp.setString(7, GENERAL_COMIS.getText());
			prp.setString(8, CLEAR_SUM.getText());
			prp.setString(9, INCOME.getText());
			prp.setString(10, SDNAME.getText());
			prp.setString(11, this.term.getNAME());
			prp.executeUpdate();
			prp.close();
			DBUtil.conn.commit();
			//-----------------------------------
			OnClose();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * класс
	 */
	Z_SB_TERMINAL_AMRA_DBT term = null;

	void SetClass(Z_SB_TERMINAL_AMRA_DBT term) {
		try {
			this.term = term;
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Получить название счета
	 * 
	 * @param acc
	 * @return
	 */
	private String GetAccName(String acc) {
		String ret = "";
		try {
			PreparedStatement prp = DBUtil.conn.prepareStatement("select CACCNAME from acc where caccacc = ?");
			prp.setString(1, acc);
			ResultSet rs = prp.executeQuery();
			if (rs.next()) {
				ret = rs.getString(1);
			}
			rs.close();
			prp.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			this.ACCOUNT.setText(term.getACCOUNT());
			this.ADDRESS.setText(term.getADDRESS());
			this.CLEAR_SUM.setText(term.getCLEAR_SUM());
			this.CRASH_ACC.setText(term.getCRASH_ACC());
			this.DEAL_ACC.setText(term.getDEAL_ACC());
			// ------------------------
			ObservableList<Integer> otd = FXCollections.observableArrayList();
			{
				PreparedStatement prp = DBUtil.conn.prepareStatement("select * from otd");
				ResultSet rs = prp.executeQuery();
				while (rs.next()) {
					otd.add(rs.getInt("IOTDNUM"));
				}
				rs.close();
				prp.close();
			}
			// -----------------------------
			this.DEPARTMENT.setItems(otd);
			this.DEPARTMENT.getSelectionModel().select(term.getDEPARTMENT());
			this.GENERAL_ACC.setText(term.getGENERAL_ACC());
			this.GENERAL_COMIS.setText(term.getGENERAL_COMIS());
			this.INCOME.setText(term.getINCOME());
			this.NAME.setText(term.getNAME());
			this.Ok.setText("Сохранить");
			this.SDNAME.setText(term.getSDNAME());
			// ---------------------------------------
			this.ACCOUNT_T.setText(GetAccName(term.getACCOUNT()));
			this.GENERAL_ACC_T.setText(GetAccName(term.getGENERAL_ACC()));
			this.CRASH_ACC_T.setText(GetAccName(term.getCRASH_ACC()));
			this.DEAL_ACC_T.setText(GetAccName(term.getDEAL_ACC()));
			this.GENERAL_COMIS_T.setText(GetAccName(term.getGENERAL_COMIS()));
			this.CLEAR_SUM_T.setText(GetAccName(term.getCLEAR_SUM()));
			this.INCOME_T.setText(GetAccName(term.getINCOME()));
			//----------------------------------------
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

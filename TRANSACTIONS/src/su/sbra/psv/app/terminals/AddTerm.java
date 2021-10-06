package su.sbra.psv.app.terminals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class AddTerm {

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
					+ "insert into z_sb_terminal_amra_dbt\r\n"
					+ "  (name,\r\n"
					+ "   department,\r\n"
					+ "   address,\r\n"
					+ "   account,\r\n"
					+ "   general_acc,\r\n"
					+ "   crash_acc,\r\n"
					+ "   deal_acc,\r\n"
					+ "   general_comis,\r\n"
					+ "   clear_sum,\r\n"
					+ "   income,\r\n"
					+ "   sdname)\r\n"
					+ "values\r\n"
					+ "  (?,?,?,?,?,?,?,?,?,?,?)");
			//----------------------------------
			prp.setString(1, NAME.getText());
			
			if (DEPARTMENT.getSelectionModel().getSelectedItem() != null) {
				prp.setInt(2, DEPARTMENT.getSelectionModel().getSelectedItem());
			} else {
				prp.setNull(2, Types.NUMERIC);
			}
			
			prp.setString(3, ADDRESS.getText());
			prp.setString(4, ACCOUNT.getText());
			prp.setString(5, GENERAL_ACC.getText());
			prp.setString(6, CRASH_ACC.getText());
			prp.setString(7, DEAL_ACC.getText());
			prp.setString(8, GENERAL_COMIS.getText());
			prp.setString(9, CLEAR_SUM.getText());
			prp.setString(10, INCOME.getText());
			prp.setString(11, SDNAME.getText());
			prp.executeUpdate();
			prp.close();
			DBUtil.conn.commit();
			//-----------------------------------
			OnClose();
			//-----------------------------------
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
			NAME.setEditable(true);
			//----------------
			AfterLosteFocus(ACCOUNT);
			AfterLosteFocus(GENERAL_ACC);
			AfterLosteFocus(CRASH_ACC);
			AfterLosteFocus(DEAL_ACC);
			AfterLosteFocus(GENERAL_COMIS);
			AfterLosteFocus(CLEAR_SUM);
			AfterLosteFocus(INCOME);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}
	
	/**
	 * После потери фокуса
	 * @param txtfld
	 */
	void AfterLosteFocus(TextField txtfld) {
		try {
			txtfld.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					if (newPropertyValue == true) {
						// Text field on focus
					} else {
						// Text field out focus
						if (!txtfld.getText().equals("") & txtfld.getText().length() >= 20) {
							if(txtfld.getId().equals("ACCOUNT")) {
								ACCOUNT_T.setText(GetAccName(txtfld.getText()));
							}else if(txtfld.getId().equals("GENERAL_ACC")) {
								GENERAL_ACC_T.setText(GetAccName(txtfld.getText()));
							}else if(txtfld.getId().equals("CRASH_ACC")) {
								CRASH_ACC_T.setText(GetAccName(txtfld.getText()));
							}else if(txtfld.getId().equals("DEAL_ACC")) {
								DEAL_ACC_T.setText(GetAccName(txtfld.getText()));
							}else if(txtfld.getId().equals("GENERAL_COMIS")) {
								GENERAL_COMIS_T.setText(GetAccName(txtfld.getText()));
							}else if(txtfld.getId().equals("CLEAR_SUM")) {
								CLEAR_SUM_T.setText(GetAccName(txtfld.getText()));
							}else if(txtfld.getId().equals("INCOME")) {
								INCOME_T.setText(GetAccName(txtfld.getText()));
							}
						}
					}
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

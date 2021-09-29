package app.termserv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.lang3.exception.ExceptionUtils;
import app.Main;
import app.util.DBUtil;
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
import sbalert.Msg;

public class EditServ {

	@FXML
    private TextField NAME;
    @FXML
    private ComboBox<String> IDTERM;
    @FXML
    private Text ACCOUNT_T;
    @FXML
    private TextField ACCOUNT;
    @FXML
    private TextField INN;
    @FXML
    private TextField KPP;
    @FXML
    private TextField ACC_REC;
    @FXML
    private TextField KBK;
    @FXML
    private TextField OKATO;
    @FXML
    private TextField ACC_NAME;
    @FXML
    private TextField BO1;
    @FXML
    private TextField BO2;
    @FXML
    private TextField COMISSION;
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
					+ "update Z_SB_TERMSERV_AMRA_DBT set "
					+ "NAME = ?,\r\n"
					+ "IDTERM = ?,\r\n"
					+ "ACCOUNT = ?,\r\n"
					+ "INN = ?,\r\n"
					+ "KPP = ?,\r\n"
					+ "ACC_REC = ?,\r\n"
					+ "KBK = ?,\r\n"
					+ "OKATO = ?,\r\n"
					+ "ACC_NAME = ?,\r\n"
					+ "BO1 = ?,\r\n"
					+ "BO2 = ?,\r\n"
					+ "COMISSION  = ? "
					+ "where ID = ?");
			prp.setString(1, NAME.getText());
			prp.setString(2, IDTERM.getSelectionModel().getSelectedItem());
			prp.setString(3, ACCOUNT.getText());
			prp.setString(4, INN.getText());
			prp.setString(5, KPP.getText());
			prp.setString(6, ACC_REC.getText());
			prp.setString(7, KBK.getText());
			prp.setString(8, OKATO.getText());
			prp.setString(9, ACC_NAME.getText());
			prp.setInt(10, Integer.valueOf(BO1.getText()));
			prp.setInt(11, Integer.valueOf(BO2.getText()));
			prp.setDouble(12, Double.valueOf(COMISSION.getText()));
			prp.setLong(13, this.serv.getID());
			prp.executeUpdate();
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
	Z_SB_TERMSERV_AMRA_DBT serv = null;

	void SetClass(Z_SB_TERMSERV_AMRA_DBT serv) {
		try {
			this.serv = serv;
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
			//NAME, IDTERM, ACCOUNT, INN, KPP, ACC_REC, KBK, OKATO, ACC_NAME, BO1, BO2, COMISSION
			this.ACCOUNT.setText(serv.getACCOUNT());
			this.NAME.setText(serv.getNAME());
			this.INN.setText(serv.getINN());
			this.KPP.setText(serv.getKPP());
			this.ACC_REC.setText(serv.getACC_REC());
			this.KBK.setText(serv.getKBK());
			this.OKATO.setText(serv.getOKATO());
			this.ACC_NAME.setText(serv.getACC_NAME());
			this.BO1.setText(String.valueOf(serv.getBO1()));
			this.BO2.setText(String.valueOf(serv.getBO2()));
			this.COMISSION.setText(String.valueOf(serv.getCOMISSION()));
			// ------------------------
			ObservableList<String> otd = FXCollections.observableArrayList();
			{
				PreparedStatement prp = DBUtil.conn.prepareStatement("select * from Z_SB_TERMINAL_AMRA_DBT");
				ResultSet rs = prp.executeQuery();
				while (rs.next()) {
					otd.add(rs.getString("NAME"));
				}
				rs.close();
				prp.close();
			}
			// -----------------------------
			this.IDTERM.setItems(otd);
			this.IDTERM.getSelectionModel().select(serv.getIDTERM());
			// ---------------------------------------
			this.ACCOUNT_T.setText(GetAccName(serv.getACCOUNT()));
			//----------------------------------------
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

}

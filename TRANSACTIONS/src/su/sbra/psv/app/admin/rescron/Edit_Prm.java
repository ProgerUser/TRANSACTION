package su.sbra.psv.app.admin.rescron;

import java.sql.PreparedStatement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class Edit_Prm {

	@FXML
    private TextField PRM_F_PRMNAME;
    @FXML
    private TextField PRM_F_PRMVAL;
	@FXML
	private Button Ok;

	/**
	 * класс
	 */
	SB_CERT_EXP_PRM prm = null;

	void SetClass(SB_CERT_EXP_PRM prm) {
		try {
			this.prm = prm;
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * При закрытии
	 */
	void OnClose() {
		Stage stage = (Stage) Ok.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@FXML
	void Cencel(ActionEvent event) {
		try {
			OnClose();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	private void initialize() {
		try {
			PRM_F_PRMNAME.setText(prm.getPRMNAME());
			PRM_F_PRMVAL.setText(prm.getPRMVAL());
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Ok(ActionEvent event) {
		try {

			final Alert alert = new Alert(AlertType.CONFIRMATION, "Редактировать \"" + prm.getPRMNAME() + "\" ?",
					ButtonType.YES, ButtonType.NO);
			if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

			} else {
				OnClose();
			}
			
			PreparedStatement prp = DBUtil.conn.prepareStatement(""
					+ "update SB_CERT_EXP_PRM set PRMNAME = ?, PRMVAL =? where PRMNAME = ?");
			// ----------------------------------
			prp.setString(1, PRM_F_PRMNAME.getText());
			prp.setString(2, PRM_F_PRMVAL.getText());
			prp.setString(3, prm.getPRMNAME());
			prp.executeUpdate();
			prp.close();
			DBUtil.conn.commit();
			// -----------------------------------
			OnClose();
			// -----------------------------------
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

}

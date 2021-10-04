package app.admin.rescron;

import java.sql.PreparedStatement;
import app.sbalert.Msg;
import app.util.DBUtil;
import app.utils.DbUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Edit_Grp {

    @FXML
    private TextField GRP_F_GRP_ID;
    @FXML
    private TextField GRP_F_GRP_NAME;
    
	@FXML
	private Button Ok;

	/**
	 * класс
	 */
	SB_CERT_EXP_GRP grp = null;

	void SetClass(SB_CERT_EXP_GRP grp) {
		try {
			this.grp = grp;
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
			GRP_F_GRP_ID.setText(String.valueOf(grp.getGRP_ID()));
			GRP_F_GRP_NAME.setText(grp.getGRP_NAME());
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Ok(ActionEvent event) {
		try {

			final Alert alert = new Alert(AlertType.CONFIRMATION, "Редактировать \"" + grp.getGRP_NAME() + "\" ?",
					ButtonType.YES, ButtonType.NO);
			if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

			} else {
				OnClose();
			}
			
			PreparedStatement prp = DBUtil.conn.prepareStatement(""
					+ "update SB_CERT_EXP_GRP set GRP_ID = ?, GRP_NAME =? where GRP_ID = ?");
			// ----------------------------------
			prp.setLong(1, Long.valueOf(GRP_F_GRP_ID.getText()));
			prp.setString(2, GRP_F_GRP_NAME.getText());
			prp.setLong(3, grp.getGRP_ID());
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

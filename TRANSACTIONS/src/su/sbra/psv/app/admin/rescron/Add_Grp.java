package su.sbra.psv.app.admin.rescron;

import java.sql.PreparedStatement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class Add_Grp {
    @FXML
    private TextField GRP_F_GRP_ID;
    @FXML
    private TextField GRP_F_GRP_NAME;
    
	@FXML
	private Button Ok;

	/**
	 * ��� ��������
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
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Ok(ActionEvent event) {
		try {
			
			PreparedStatement prp = DBUtil.conn.prepareStatement(" insert into SB_CERT_EXP_GRP (GRP_ID,GRP_NAME) values (?,?)");
			// ----------------------------------
			prp.setLong(1, Long.valueOf(GRP_F_GRP_ID.getText()));
			prp.setString(2, GRP_F_GRP_NAME.getText());
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

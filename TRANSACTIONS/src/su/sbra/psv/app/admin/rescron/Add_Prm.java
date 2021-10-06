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

public class Add_Prm {

	@FXML
    private TextField PRM_F_PRMNAME;
    @FXML
    private TextField PRM_F_PRMVAL;
    
	@FXML
	private Button Ok;

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
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Ok(ActionEvent event) {
		try {
			
			PreparedStatement prp = DBUtil.conn.prepareStatement(" insert into SB_CERT_EXP_PRM (PRMNAME,PRMVAL) values (?,?)");
			// ----------------------------------
			prp.setString(1, PRM_F_PRMNAME.getText());
			prp.setString(2, PRM_F_PRMVAL.getText());
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

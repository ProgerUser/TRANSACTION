package su.sbra.psv.app.admin.rescron;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import su.sbra.psv.app.tr.pl.ConvConst;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class Add_Res {

	@FXML
	private TextField RES_F_CERTNAME;
	@FXML
	private ComboBox<SB_CERT_EXP_GRP> RES_F_CERTGRP;
	@FXML
	private TextField RES_F_CERTRES;
	@FXML
	private DatePicker RES_F_CERTBEG;
	@FXML
	private DatePicker RES_F_CERTEND;
	@FXML
	private CheckBox RES_F_CERTSTAT;
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
			Grp();
			
			new ConvConst().FormatDatePiker(RES_F_CERTBEG);
			new ConvConst().FormatDatePiker(RES_F_CERTEND);
			
			
			
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	@FXML
	void Ok(ActionEvent event) {
		try {
			
//			UrlValidator urlValidator = new UrlValidator();
//			
//			if(!urlValidator.isValid(RES_F_CERTRES.getText())) {
//				Msg.Message(RES_F_CERTRES.getText()+" не сайт!");
//				return;
//			}
			
			PreparedStatement prp = DBUtil.conn.prepareStatement("" + "insert into sb_cert_exp\r\n" + "  ("
					+ "certname, \r\n" + "certbeg, \r\n" + "certend, \r\n" + "certres, \r\n" + "certgrp, \r\n"
					+ "certstat)\r\n" + "values\r\n" + "  (?, ?, ?, ?, ?, ?)\r\n" + "");
			// ----------------------------------
			prp.setString(1, RES_F_CERTNAME.getText());
			prp.setDate(2, java.sql.Date.valueOf(RES_F_CERTBEG.getValue()));
			prp.setDate(3, java.sql.Date.valueOf(RES_F_CERTEND.getValue()));
			prp.setString(4, RES_F_CERTRES.getText());
			prp.setLong(5, RES_F_CERTGRP.getSelectionModel().getSelectedItem().getGRP_ID());
			prp.setString(6, ((RES_F_CERTSTAT.isSelected()) ? "Y" : "N"));
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

	void Grp() {
		try {
			Statement sqlStatement = DBUtil.conn.createStatement();
			String readRecordSQL = "select * from SB_CERT_EXP_GRP order by GRP_ID";
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			ObservableList<SB_CERT_EXP_GRP> grp = FXCollections.observableArrayList();
			while (rs.next()) {
				SB_CERT_EXP_GRP list = new SB_CERT_EXP_GRP();
				list.setGRP_NAME(rs.getString("GRP_NAME"));
				list.setGRP_ID(rs.getLong("GRP_ID"));
				grp.add(list);
			}
			RES_F_CERTGRP.setItems(grp);
			rs.close();
			sqlStatement.close();
			CombGrp(RES_F_CERTGRP);

			RES_F_CERTGRP.getSelectionModel().select(0);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Для стран
	 */
	private void CombGrp(ComboBox<SB_CERT_EXP_GRP> cmb) {
		cmb.setConverter(new StringConverter<SB_CERT_EXP_GRP>() {
			@Override
			public String toString(SB_CERT_EXP_GRP product) {
				return (product != null) ? product.getGRP_NAME() : "";
			}

			@Override
			public SB_CERT_EXP_GRP fromString(final String string) {
				return cmb.getItems().stream().filter(product -> product.getGRP_NAME().equals(string)).findFirst()
						.orElse(null);
			}
		});
	}

}

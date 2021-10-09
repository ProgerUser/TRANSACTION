package su.sbra.psv.app.controller;

import java.awt.SplashScreen;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.util.DBUtil;

public class First {

	public First() {
		Main.logger = Logger.getLogger(getClass());
	}

	@FXML
	private ImageView imageView;

	@FXML
	private void initialize() {
		try {
			//splash - не используется
			final SplashScreen splash = SplashScreen.getSplashScreen();
			if (splash != null) {
				splash.close();
			}
			//логин и ФИО
			String usrlogin = "";
			PreparedStatement prp = DBUtil.conn
					.prepareStatement("select CUSRNAME||' - '||CUSRLOGNAME login from usr t where upper(cusrlogname) = ?");
			prp.setString(1, Connect.userID_.toUpperCase());
			ResultSet rs = prp.executeQuery();
			if (rs.next()) {
				usrlogin = rs.getString("login");
			}
			rs.close();
			prp.close();
			Main.primaryStage.setTitle("(" + usrlogin + ") (" + Connect.connectionURL_+")");
			//картинка в центр
			Image image = new Image(getClass().getResourceAsStream("/logo.png"));
			imageView.setImage(image);
			// Setting the image view parameters
			imageView.setX(500);
			imageView.setY(50);
			imageView.setFitWidth(300);
			imageView.setFitHeight(300);
			imageView.setPreserveRatio(true);
			imageView.setPickOnBounds(true);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}

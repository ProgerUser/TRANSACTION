package sbalert;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Msg {
	
	public static void MessageBox(String Mess,Stage stage) {
		TextArea alert = new TextArea();
		alert.setPrefSize(600, 400);
		AnchorPane yn = new AnchorPane();
		Scene ynScene = new Scene(yn, 600, 400);
		yn.getChildren().add(alert);
		Stage newWindow_yn = new Stage();
		newWindow_yn.setTitle("Внимание");
		newWindow_yn.setScene(ynScene);
		// Specifies the modality for new window.
		newWindow_yn.initModality(Modality.WINDOW_MODAL);
		// Specifies the owner Window (parent) for new window
		newWindow_yn.initOwner(stage);
		newWindow_yn.getIcons().add(new Image("icon.png"));
		newWindow_yn.show();
		alert.setText(Mess);
	}
	
	public static void Message(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}
}

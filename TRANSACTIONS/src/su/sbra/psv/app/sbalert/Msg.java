package su.sbra.psv.app.sbalert;

import org.apache.log4j.Logger;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import su.sbra.psv.app.main.Main;

public class Msg {

	public Msg() {
		Main.logger = Logger.getLogger(getClass());
	}

	public static Alert setDefaultButton(Alert alert, ButtonType defBtn) {
		DialogPane pane = alert.getDialogPane();
		for (ButtonType t : alert.getButtonTypes())
			((Button) pane.lookupButton(t)).setDefaultButton(t == defBtn);
		return alert;
	}
	public static void MessageBox(String Mess, Stage stage) {
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
		newWindow_yn.getIcons().add(new Image("/icon.png"));
		newWindow_yn.show();
		alert.setText(Mess);
	}

	public static void Message(String mess) {
		if (mess != null && !mess.equals("")) {

			AlertType AlertTp = null;
			String error = null;
			if (mess.length() >= 200) {
				AlertTp = AlertType.ERROR;
				error = mess.substring(0, 150);
			} else {
				AlertTp = AlertType.INFORMATION;
				error = mess;
			}
			Alert alert = new Alert(AlertTp);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/icon.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(error);
			// alert.setContentText(mess.substring(0, mess.indexOf("\r\n")));
			Label label = new Label("Трассировка стека исключения:");

			TextArea textArea = new TextArea(mess);
			textArea.setEditable(false);
			textArea.setFont(Font.font(14));
			textArea.setWrapText(true);
			
			textArea.getStyleClass().add("mylistview");
			textArea.getStylesheets().add("/ScrPane.css");

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);

			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);

			alert.showAndWait();
		}
	}
}

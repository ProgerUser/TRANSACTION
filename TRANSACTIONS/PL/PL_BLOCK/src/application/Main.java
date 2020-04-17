package application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	public static String id_card;
	public static String db_;
	public static String user_;
	public static String pass = "man9o";

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("icon.png"));
			primaryStage.setTitle("¬ведите основание блокировки карты!_" + Main.db_ + "_" + Main.user_);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			String fromforms = args[4];
			String[] parts = fromforms.split("~");
			String db = parts[0].trim();
			String user = parts[1].trim();
			String cardid = parts[2].trim();

			if (db.equals("oradb-prm.sbra.com")) {
				Main.db_ = "oradb-prm:1521/odb";
			} else if (db.equals("oradb-test.sbra.com")) {
				Main.db_ = "10.111.64.128:1521/odb";
			}

			Main.user_ = user;
			Main.id_card = cardid;
			launch(args);

		} catch (ArrayIndexOutOfBoundsException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("¬нимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}

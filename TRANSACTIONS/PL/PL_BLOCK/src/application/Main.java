package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	public static String id_card;
	public static String db;
	public static String user;
	public static String pass;
	public static String act_name;

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("icon.png"));
			primaryStage.setTitle(Main.user+"@"+Main.db);
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> e.consume());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			Main.user = args[0];
			Main.pass = args[1];
			Main.db = args[2];
			Main.id_card = args[3];
			Main.act_name = args[4];
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

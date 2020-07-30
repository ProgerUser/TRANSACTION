package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SaveController {

	@FXML
	private TextField gr;

	@FXML
	private AnchorPane ap;

	@FXML
	private TextField card_Id;
	
	@FXML
    private Text action;

	@FXML
	void save(ActionEvent event) {
		try {
			if (gr.getText().equals("") | gr.getText().length() < 10) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("icon.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Введите корректные данные!");
				alert.showAndWait();
			} else {
				Connection conn = DriverManager
						.getConnection("jdbc:oracle:thin:" +Main.user +"/" + Main.pass + "@" + Main.db);
				System.out.println("jdbc:oracle:thin:" +Main.user +"/" + Main.pass + "@" + Main.db);
				Statement chekStatement = conn.createStatement();
				String chek = 
						"insert into Z_SB_CARDBLOCK "
				+ "(CARDID,DATEOPER,DOMAINAC,GROUND,ACT_NAME) "
				+ "values ("
						+ Main.id_card + ",sysdate,'" + Main.user + "','" + gr.getText() + "','"+Main.act_name+"')";
				ResultSet rs = chekStatement.executeQuery(chek);
				rs.close();
				conn.close();
				Platform.exit();
				System.exit(0);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("icon.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void initialize() {
		card_Id.setText(Main.id_card);
		
		gr.setTextFormatter(new TextFormatter<>((change) -> {
		    change.setText(change.getText().toUpperCase());
		    return change;
		}));
		action.setText("Введите основание для "+Main.act_name);
	}
}

package sb_tr.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sb_tr.Main;
import sb_tr.model.Connect;
import sb_tr.model.TerminalForCombo;
import sb_tr.model.ViewerDAO;

@SuppressWarnings("unused")
public class RootLayoutController {

	private Executor exec;
	/*
	 * //Reference to the main application private Main main;
	 * 
	 * //Is called by the main application to give a reference back to itself.
	 * public void setMain (Main main) { this.main = main; }
	 */
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private MenuItem chekreport;

	@FXML
	private Menu statusbar;

	@FXML
	void handleExit(ActionEvent event) {
		//File file = new File(System.getProperty("user.home") + "/XXI.AP_TEST_MAIN.properties");
		//file.delete();
		//System.out.print("------------------------------------------------------");
		//Platform.exit();
		//System.exit(0);
	}

	@FXML
	void chektransact(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Viewer.fxml",Connect.userID_) == 1) {
				Main.showEmployeeView();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void DBCONNECT(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Program Information");
				alert.setHeaderText("Информация о DB");
				alert.setContentText(
						"Схема: " + Connect.connectionURL_ + "\r\n" + "Пользователь: " + Connect.userID_ + "\r\n");
				alert.show();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void term_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Transact_Amra_viewer.fxml",Connect.userID_) == 1) {
				Main.showAmTr();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void loadtransact(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("TransactLoad.fxml",Connect.userID_) == 1) {
				Main.Transact();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void loadhistory(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("ShowHist.fxml",Connect.userID_) == 1) {
				Main.Show_Hist();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void Termdial_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Termdial.fxml",Connect.userID_) == 1) {
				Main.Termdial_view_();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void amra_trans(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Amra_Trans.fxml",Connect.userID_) == 1) {
				Main.Transact_Amra();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void termview(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Terminal.fxml",Connect.userID_) == 1) {
				Main.Terminal();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void Kash(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Kash.fxml",Connect.userID_) == 1) {
				Main.showKash();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void ap_print(ActionEvent event) throws Exception {
		/*
		 * При каждом вызове происходит создание файла с паролем,логином и базой, при
		 * закрытии удаляется Хоть так :)
		 **/
		/*
		 * try { Properties properties = new Properties();
		 * properties.setProperty("login", Connect.userID_);
		 * properties.setProperty("password", Connect.userPassword_);
		 * properties.setProperty("db",
		 * Connect.connectionURL_.substring(Connect.connectionURL_.indexOf("/")+1,
		 * Connect.connectionURL_.length()));
		 * properties.setProperty("pseudoConnecton","false");
		 * properties.setProperty("tns_admin","J:/dev6i/NET80/ADMIN");
		 * 
		 * File file = new
		 * File(System.getProperty("user.home")+"/XXI.AP_TEST_MAIN.properties");
		 * file.getParentFile().mkdirs(); file.createNewFile(); FileOutputStream fileOut
		 * = new FileOutputStream(file); properties.store(fileOut,
		 * "Parametry dlia pechati"); fileOut.close(); } catch (FileNotFoundException e)
		 * { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 */
		/*
		 * Вызов jar файла из cmd и другого процесса, вынужденная мера, больше никак не
		 * получается вызвать FXBicomp
		 */
		Task<Object> task = new Task<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start javaw -splash:"+System.getenv("TRANSACT_PATH")+"SPLASH/splash.gif -jar "
							+ System.getenv("TRANSACT_PATH") + "/AP.jar 666 1 1 no "
							+ Connect.userID_ + " " + Connect.userPassword_ + " " + Connect.connectionURL_
									.substring(Connect.connectionURL_.indexOf("/") + 1, Connect.connectionURL_.length())
							+ " J:\\dev6i\\NET80\\ADMIN");
					builder.redirectErrorStream(true);
					Process p;
					p = builder.start();
					BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while (true) {
						line = r.readLine();
						if (line == null) {
							break;
						}
						System.out.println(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Alert(e.getMessage());
				}
				return null;
			}
		};
		task.setOnFailed(e -> Alert(task.getException().getMessage()));
		// task.setOnSucceeded(e -> );

		exec.execute(task);
	}

	private void Alert(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText("Выберите сначала данные из таблицы!");
		alert.showAndWait();
	}

	@FXML
	void service(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Service.fxml",Connect.userID_) == 1) {
				Main.Service();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Нет прав!");
				alert.showAndWait();
			}

		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Ошибка!");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	// Help Menu button behavior
	public void handleHelp(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Информация");
		alert.setHeaderText("Работа с терминалами 1.3.0");
		alert.setContentText("Загрузка транзакции, администрирование терминалов\n PACHULIYA_S_V 2018");
		alert.show();
	}

	public void inis() {
		statusbar.setText(Connect.userID_ + "/" + Connect.connectionURL_);
	}

	public int chk_rigth(String FORM_NAME,String CUSRLOGNAME) {
		int ret = 0;
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@"
					+ Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select count(*)\n" + 
					"  from z_sb_access_amra a,\n" + 
					"       z_sb_access_gr_amra b,\n" + 
					"       z_sb_access_gr_type_amra c,\n" + 
					"       (select t.cusrlogname, t.iusrid from usr t) d\n" + 
					" where a.id_form = b.form_id\n" + 
					"   and b.gr_id = c.id_type\n" + 
					"   and b.usr_id = d.iusrid\n" + 
					"   and upper(FORM_NAME) = upper('"+FORM_NAME+"')\n" + 
					"   and upper(CUSRLOGNAME) = upper('"+CUSRLOGNAME+"')\n" + 
					"   and T_NAME = 'Y'";
			System.out.println(readRecordSQL);
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			ObservableList<String> combolist = FXCollections.observableArrayList();
			if (rs.next()) {
				ret = 1;
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return ret;
	}
    
	@FXML
	void initialize() {
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		assert chekreport != null : "fx:id=\"chekreport\" was not injected: check your FXML file 'RootLayout.fxml'.";
	}
}

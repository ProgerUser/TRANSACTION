package sb_tr.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sb_tr.Main;
import sb_tr.model.Connect;
import sb_tr.model.SqlMap;
import sb_tr.model.TerminalForCombo;
import sb_tr.model.ViewerDAO;
import sb_tr.util.DBUtil;

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
	private MenuItem adminmenuitems;

	@FXML
	private URL location;

	@FXML
	private MenuItem chekreport;

	@FXML
	private Menu statusbar;

	@FXML
	private MenuItem adminright;

	@FXML
	private MenuItem executeamratrans;

	@FXML
	private MenuItem createpsevdo;

	@FXML
	private MenuItem seporatepensrf;

	@FXML
	private MenuItem services;

	@FXML
	private MenuItem seporatepensra;

	@FXML
	private MenuItem terminals;

	@FXML
	private MenuBar menubar;

	@FXML
	private MenuItem transactlist;

	@FXML
	private MenuItem print;

	@FXML
	private Menu administrator;

	@FXML
	private Menu pensiara;

	@FXML
	private Menu file;

	@FXML
	private Menu pensiarf;

	@FXML
	private MenuItem deals;

	@FXML
	private MenuItem bankklients;

	@FXML
	private Menu kash;

	@FXML
	private MenuItem exitapp;

	@FXML
	private MenuItem historyload;

	@FXML
	private Menu amraterminal;

	@FXML
	private MenuItem printapmain;

	@FXML
	private Menu bankklient;

	@FXML
	void handleExit(ActionEvent event) {
		// File file = new File(System.getProperty("user.home") +
		// "/XXI.AP_TEST_MAIN.properties");
		// file.delete();
		// System.out.print("------------------------------------------------------");
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void chektransact(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Viewer.fxml", Connect.userID_) == 1) {
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
	void Ibank(ActionEvent event) {
		try {
			Main.Ibankk();
			/*
			 * if (Connect.userPassword_.equals("")) {
			 * 
			 * } else if (chk_rigth("Ibank.fxml",Connect.userID_) == 1) { Main.Ibankk(); }
			 * else { Alert alert = new Alert(Alert.AlertType.INFORMATION); Stage stage =
			 * (Stage) alert.getDialogPane().getScene().getWindow();
			 * stage.getIcons().add(new Image("terminal.png")); alert.setTitle("Внимание");
			 * alert.setHeaderText(null); alert.setContentText("Нет прав!");
			 * alert.showAndWait(); }
			 */
		} catch (NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Error");
			alert.setContentText("Введите учетные данные");
			alert.show();
		}
	}

	@FXML
	void access_menuitems(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Admin.fxml", Connect.userID_) == 1) {
				System.out.println(chk_rigth("Admin_Menu.fxml", Connect.userID_));
				Main.Admin_Menu();
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

	@FXML
	void term_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Transact_Amra_viewer.fxml", Connect.userID_) == 1) {
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

			} else if (chk_rigth("TransactLoad.fxml", Connect.userID_) == 1) {
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

			} else if (chk_rigth("ShowHist.fxml", Connect.userID_) == 1) {
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

			} else if (chk_rigth("Termdial.fxml", Connect.userID_) == 1) {
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

			} else if (chk_rigth("Amra_Trans.fxml", Connect.userID_) == 1) {
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

			} else if (chk_rigth("Terminal.fxml", Connect.userID_) == 1) {
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

			} else if (chk_rigth("Kash.fxml", Connect.userID_) == 1) {
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
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start javaw -splash:"
							+ System.getenv("TRANSACT_PATH") + "SPLASH/splash.gif -jar "
							+ System.getenv("TRANSACT_PATH") + "/AP.jar 666 1 1 no " + Connect.userID_ + " "
							+ Connect.userPassword_ + " " + Connect.connectionURL_
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

	@FXML
	void ap_printfmx(ActionEvent event) throws Exception {

		System.out.println("ifrun60.exe I:\\KERNEL\\ap_main.fmx " + Connect.userID_ + "/" + Connect.userPassword_ + "@"
				+ Connect.connectionURL_
						.substring(Connect.connectionURL_.indexOf("/") + 1, Connect.connectionURL_.length())
						.toUpperCase()
				+ " \"report_type_id = 666\"");

		Task<Object> task = new Task<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
							"ifrun60.exe I:\\KERNEL\\ap_main.fmx " + Connect.userID_ + "/" + Connect.userPassword_ + "@"
									+ Connect.connectionURL_.substring(Connect.connectionURL_.indexOf("/") + 1,
											Connect.connectionURL_.length()).toUpperCase()
									+ " \"report_type_id = 666\"");
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

	private void Alerts(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	@FXML
	void Access(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Admin.fxml", Connect.userID_) == 1) {
				System.out.println(chk_rigth("Admin.fxml", Connect.userID_));
				Main.Admin();
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

	@FXML
	void service(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Service.fxml", Connect.userID_) == 1) {
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
		alert.setHeaderText("Работа с терминалами 1.4.0");
		alert.setContentText("Загрузка транзакции, администрирование терминалов\n PACHULIYA_S_V 2018");
		alert.show();
	}

	public void inis() {
		statusbar.setText(Connect.userID_ + "/" + Connect.connectionURL_);
	}

	public int chk_rigth(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		// Connection conn;
		Connection conn = DBUtil.conn;
		try {
			/*
			 * conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ +
			 * "/" + Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
			 */

			SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String readRecordSQL = s.getSql("acces_enter");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);

			System.out.println(readRecordSQL);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<String> combolist = FXCollections.observableArrayList();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			// conn.close();
			prepStmt.close();
		} catch (Exception e) {
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

	public int chk_menu(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		// Connection conn;
		Connection conn = DBUtil.conn;
		try {
			/*
			 * conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ +
			 * "/" + Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
			 */

			SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String readRecordSQL = s.getSql("acces_menu");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			System.out.println(readRecordSQL);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<String> combolist = FXCollections.observableArrayList();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			// conn.close();
			prepStmt.close();
		} catch (Exception e) {
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
	void divide_RA(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Pens_RA.fxml", Connect.userID_) == 1) {
				System.out.println(chk_rigth("Pens_RA.fxml", Connect.userID_));
				Main.sepRA();
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

	@FXML
	void divide(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Pens_divide.fxml", Connect.userID_) == 1) {
				System.out.println(chk_rigth("Pens_divide.fxml", Connect.userID_));
				Main.sep();
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

	@FXML
	void initialize() {
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		menubar.getMenus().forEach(menu -> {
			if (chk_menu(menu.getId(), Connect.userID_) == 1) {
				menu.setVisible(true);
			} else {
				menu.setVisible(false);
			}
			menu.getItems().forEach(menuItem -> {
				if (chk_menu(menuItem.getId(), Connect.userID_) == 1) {
					menuItem.setVisible(true);
				} else {
					menuItem.setVisible(false);
				}
			});
		});
	}
}

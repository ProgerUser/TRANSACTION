package sb_tr.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import sb_tr.Main;
import sb_tr.model.Connect;
import sb_tr.model.SqlMap;
import sb_tr.util.DBUtil;
import sbalert.Msg;

public class RootLayoutController {

	private Executor exec;
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
	private MenuItem contactload;

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
	private Menu budcode;

	@FXML
	private MenuItem savebudcode;

	@FXML
	void handleExit(ActionEvent event) {
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
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void save_budcode(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("DebtInfo.fxml", Connect.userID_) == 1) {
				System.out.println(chk_rigth("DebtInfo.fxml", Connect.userID_));
				Main.Debtinfo();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
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
			if (chk_rigth("Ibank.fxml", Connect.userID_) == 1) {
				Main.Ibankk();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
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
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void term_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Transact_Amra_viewer.fxml", Connect.userID_) == 1) {
				Main.showAmTr();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void loadtransact(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("TransactLoad.fxml", Connect.userID_) == 1) {
				Main.Transact();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void loadhistory(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("ShowHist.fxml", Connect.userID_) == 1) {
				Main.Show_Hist();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void Termdial_view(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Termdial.fxml", Connect.userID_) == 1) {
				Main.Termdial_view_();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void amra_trans(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Amra_Trans.fxml", Connect.userID_) == 1) {
				Main.Transact_Amra();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void termview(ActionEvent event) {
		try {

			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Terminal.fxml", Connect.userID_) == 1) {
				Main.Terminal();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void Kash(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Kash.fxml", Connect.userID_) == 1) {
				Main.showKash();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void ContactLoad(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {
				
			} else if (chk_rigth("Contact.fxml", Connect.userID_) == 1) {
				Main.Contact();
			} else {
				Msg.Messge("Нет прав");
			}
		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void ap_print(ActionEvent event) throws Exception {
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

					}
				} catch (IOException e) {
					Msg.Messge(e.getMessage());
				}
				return null;
			}
		};
		task.setOnFailed(e -> Msg.Messge(task.getException().getMessage()));
		/* task.setOnSucceeded(e -> ); */
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
					}
				} catch (IOException e) {

					Msg.Messge(e.getMessage());
				}
				return null;
			}
		};
		task.setOnFailed(e -> Msg.Messge(task.getException().getMessage()));
		/* task.setOnSucceeded(e -> ); */
		exec.execute(task);
	}

	@FXML
	void Access(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Admin.fxml", Connect.userID_) == 1) {
				System.out.println(chk_rigth("Admin.fxml", Connect.userID_));
				Main.Admin();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	@FXML
	void service(ActionEvent event) {
		try {
			if (Connect.userPassword_.equals("")) {

			} else if (chk_rigth("Service.fxml", Connect.userID_) == 1) {
				Main.Service();
			} else {
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}

	public void inis() {
		statusbar.setText(Connect.userID_ + "/" + Connect.connectionURL_);
	}

	/* Проверка прав на доступ к формам */
	public int chk_rigth(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		Connection conn = DBUtil.conn;
		try {
			SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String readRecordSQL = s.getSql("acces_enter");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			prepStmt.close();
		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
		return ret;
	}

	/* Проверка прав доступа к меню */
	public int chk_menu(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		Connection conn = DBUtil.conn;
		try {
			SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String readRecordSQL = s.getSql("acces_menu");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getInt("CNT");
			}
			prepStmt.close();
		} catch (Exception e) {
			Msg.Messge(e.getMessage());
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
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
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
				Msg.Messge("Нет прав");
			}

		} catch (Exception e) {
			Msg.Messge(e.getMessage());
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

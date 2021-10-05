package app.rootlayout;

import java.awt.SplashScreen;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import app.Main;
import app.model.Connect;
import app.model.SqlMap;
import app.report.Report;
import app.sbalert.Msg;
import app.util.DBUtil;
import app.utils.DbUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class RootLayoutController {

	private Executor exec;
	@FXML
	private MenuItem adminmenuitems;
	@FXML
	private MenuBar menubar;

	/**
	 * Для сверки
	 */
	@FXML
	private MenuItem Sverka;

	/**
	 * Для сверки
	 * 
	 * @param event
	 */
	@FXML
	void Sverka(ActionEvent event) {
		try {
			Main.Sverka();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void ResMon(ActionEvent event) {
		try {
			Main.ResMon();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void handleExit(ActionEvent event) {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	void swift(ActionEvent event) {
		try {
			Main.swift();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void PlastRash(ActionEvent event) {
		try {
			Main.PlastRash();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@FXML
	void AudView(ActionEvent event) {
		try {
			Main.AudView();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@FXML
	void AudSet(ActionEvent event) {
		try {
			Main.AudSet();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void chektransact(ActionEvent event) {
		try {
			Main.showEmployeeView();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void save_budcode(ActionEvent event) {
		try {
			Main.Debtinfo();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
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
			Msg.Message("Введите учетные данные");
		}
	}

	@FXML
	void Ibank(ActionEvent event) {
		try {
			Main.Ibankk();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void access_menuitems(ActionEvent event) {
		try {
			Main.Admin_Menu();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void term_view(ActionEvent event) {
		try {
			Main.showAmTr();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void loadhistory(ActionEvent event) {
		try {
			Main.Show_Hist();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void Termdial_view(ActionEvent event) {
		try {
			Main.Termdial_view_();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void amra_trans(ActionEvent event) {
		try {
			Main.Transact_Amra();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void termview(ActionEvent event) {
		try {
			Main.Terminal();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void KashPsevdo(ActionEvent event) {
		try {
			Main.showKash();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@FXML
	void Usr(ActionEvent event) {
		try {
			Main.Usr();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void ContactLoad(ActionEvent event) {
		try {
			Main.Contact();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean ReportsWin = true;

	@FXML
	void ap_print(ActionEvent event) throws Exception {
		try {
			if (ReportsWin) {
				ReportsWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/app/report/Report.fxml"));

				Report controller = new Report();
				controller.setId(666l);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("(" + controller.getId() + ") Печать");
				stage.initOwner(Main.primaryStage);

				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						ReportsWin = true;
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

//	@FXML
//	void ap_print(ActionEvent event) throws Exception {
//		Task<Object> task = new Task<Object>() {
//			@Override
//			public Object call() throws Exception {
//				try {
//					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "start javaw -splash:"
//							+ System.getenv("TRANSACT_PATH") + "SPLASH/splash.gif -jar "
//							+ System.getenv("TRANSACT_PATH") + "/AP.jar 666 1 1 no " + Connect.userID_ + " "
//							+ Connect.userPassword_ + " " + Connect.connectionURL_
//									.substring(Connect.connectionURL_.indexOf("/") + 1, Connect.connectionURL_.length())
//							+ " J:\\dev6i\\NET80\\ADMIN");
//					builder.redirectErrorStream(true);
//					Process p;
//					p = builder.start();
//					BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//					String line;
//					while (true) {
//						line = r.readLine();
//						if (line == null) {
//							break;
//						}
//
//					}
//				} catch (IOException e) {
//					Msg.Message(ExceptionUtils.getStackTrace(e));
//				}
//				return null;
//			}
//		};
//		task.setOnFailed(e -> Msg.Message(task.getException().getMessage()));
//		/* task.setOnSucceeded(e -> ); */
//		exec.execute(task);
//	}

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

					Msg.Message(ExceptionUtils.getStackTrace(e));
				}
				return null;
			}
		};
		task.setOnFailed(e -> Msg.Message(task.getException().getMessage()));
		/* task.setOnSucceeded(e -> ); */
		exec.execute(task);
	}

	@FXML
	void Access(ActionEvent event) {
		try {
			Main.Admin();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void AccessGroup(ActionEvent event) {
		try {
			Main.AccessGroup();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void service(ActionEvent event) {
		try {
			Main.Service();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Проверка прав на доступ к формам
	 * 
	 * @param FORM_NAME
	 * @param CUSRLOGNAME
	 * @return
	 */
	public int chk_rigth_(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		Connection conn = DBUtil.conn;
		try {
			SqlMap s = new SqlMap().load("/SQL.xml");
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return ret;
	}

	/**
	 * Проверка прав доступа к меню
	 * 
	 * @param FORM_NAME
	 * @param CUSRLOGNAME
	 * @return
	 */
	public Long chk_menu(Long FORM_NAME, String CUSRLOGNAME) {
		Long ret = 0l;
		Connection conn = DBUtil.conn;
		try {
			PreparedStatement prepStmt = conn
					.prepareStatement("SELECT MJUsers.MNU_ACCESS(MNU_ID => ?, USR_LOGIN => ?) CNT FROM DUAL");
			prepStmt.setLong(1, FORM_NAME);
			prepStmt.setString(2, CUSRLOGNAME);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getLong("CNT");
			}
			prepStmt.close();
			rs.close();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
		return ret;
	}

	/**
	 * Проверка прав доступа к меню
	 * 
	 * @param FORM_NAME
	 * @param CUSRLOGNAME
	 * @return
	 */
	public int chk_menu(String FORM_NAME, String CUSRLOGNAME) {
		int ret = 0;
		Connection conn = DBUtil.conn;
		try {
			SqlMap s = new SqlMap().load("/SQL.xml");
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return ret;
	}

	@FXML
	void divide_RA(ActionEvent event) {
		try {
			Main.sepRA();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void divide(ActionEvent event) {
		try {
			Main.sep();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private void initialize() {
		final SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash != null) {
			splash.close();
		}
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		menubar.getMenus().forEach(menu -> {
			if (chk_menu(Long.valueOf(menu.getId()), Connect.userID_) == 1) {
				menu.setVisible(true);
			} else {
				menu.setVisible(false);
			}
			menu.getItems().forEach(menuItem -> {
				if (chk_menu(Long.valueOf(menuItem.getId()), Connect.userID_) == 1) {
					menuItem.setVisible(true);
				} else {
					menuItem.setVisible(false);
				}
			});
		});

	}
}

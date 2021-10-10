package su.sbra.psv.app.main;

import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.jyloo.syntheticafx.RootPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.access.action.OdbActions;
import su.sbra.psv.app.access.grp.GrpController;
import su.sbra.psv.app.access.menu.OdbMNU;
import su.sbra.psv.app.admin.rescron.SBResJob;
import su.sbra.psv.app.admin.usr.UsrC;
import su.sbra.psv.app.audit.trigger.AuList;
import su.sbra.psv.app.audit.view.Audit;
import su.sbra.psv.app.contact.ContactC;
import su.sbra.psv.app.loadamra.Amra_Transact;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.pensia.PensC;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.sverka.SverkaC;
import su.sbra.psv.app.swift.SWC;
import su.sbra.psv.app.tr.pl.Pl;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;
import su.sbra.psv.app.valconv.ConvVal;

/**
 * Точка входа
 * 
 * @author Said
 *
 */
public class Main extends Application {

	public static Logger logger = Logger.getLogger(Main.class);
	public static Stage primaryStage;
	public static BorderPane rootLayout;
	public static String MODULE = null;

	@Override
	public void start(Stage primaryStage) {
		try {
			// log4j
			DOMConfigurator.configure(getClass().getResource("/log4j.xml"));
			Main.primaryStage = primaryStage;
			primaryStage.getIcons().add(new Image("icon.png"));
			Main.primaryStage.setTitle("Транзакции");
			logger.setLevel(Level.INFO);

//			if (MODULE == null) {
//				Logon();
//			} else if (MODULE.equals("DEBTINFO")) {
//				DBUtil.dbConnect();
//				Debtinfo();
//			} else if (MODULE.equals("BUH")) {
//				DBUtil.dbConnect();
//				InitAppRootLayout();
//				ShFirstView();
//			} else if (MODULE.equals("SWIFT")) {
//				DBUtil.dbConnect();
//				SwiftFromAbs();
//			} else if (MODULE.equals("VTB_CONV")) {
//				DBUtil.dbConnect();
//				ConvVal();
//			}

			{
				Connect.connectionURL_ = "10.111.64.21:1521/ODB";
				Connect.userID_ = "saidp";
				Connect.userPassword_ = "vector165";
				DbUtil.Db_Connect();
				DBUtil.dbConnect();
				InitAppRootLayout();
				ShFirstView();
//				swift2();
//				ResMon();
			}
//			{
//				String sql = "SELECT :name from dual";
//				NamedParamStatement stmt = new NamedParamStatement(DbUtil.conn, sql);
//				stmt.setString("name", "Said");
//				ResultSet rs = stmt.executeQuery();
//				if (rs.next())
//					System.out.println(rs.getString(1));
//				stmt.close();
//			}

			primaryStage.setOnCloseRequest(e -> {
				DBUtil.dbDisconnect();
				DbUtil.Db_Disconnect();
				Platform.exit();
				System.exit(0);
			});

			// this.primaryStage.setMaximized(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Initializes the root layout.
	 */
	public static void InitAppRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/rootlayout/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.setResizable(true);
			primaryStage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Не используется
	 */
	@Deprecated
	public static void showEmployeeView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/Viewer.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean MenuWin = true;

	/**
	 * Доступ по действиям
	 */
	public static void GrantAccessMenu() {
		try {
			if (MenuWin) {
				MenuWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/su/sbra/psv/app/access/menu/ODB_MNU.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Права доступа к пунктам меню");
				stage.initOwner(primaryStage);
				// stage.initModality(Modality.WINDOW_MODAL);
				OdbMNU controller = loader.getController();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						MenuWin = true;
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Формирование псевдонимов
	 */
	public static void CassaOvPlat() {
		try {
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/view/Kash.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Псевдонимы");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Пользователи
	 */
	public static boolean UsrWin = true;

	public static void UserControl() {
		try {
			if (UsrWin) {
				UsrWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/su/sbra/psv/app/admin/usr/Usr.fxml"));
				Parent root = loader.load();
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Пользователи");
				stage.initOwner(primaryStage);
				UsrC controller = loader.getController();
				Scene scene = new Scene(new RootPane(stage, root, true, true));
				stage.setScene(scene);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						UsrWin = true;
						controller.dbDisconnect();
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Разрешить расход с пластика
	 */
	public static void PlAccessRashod() {
		try {
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/tr/pl/Pl.fxml"));

			Pl controller = new Pl();
			loader.setController(controller);
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("Расход с пластика");
			stage.initOwner(stage_);
			stage.setResizable(true);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {

				}
			});
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean AuSetupWin = true;

	/**
	 * Настройка аудита
	 */
	public static void AudSet() {
		try {
			if (AuSetupWin) {
				AuSetupWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(
						Main.class.getResource("/su/sbra/psv/app/audit/trigger/AuList.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Настройка аудита");
				stage.initOwner(primaryStage);
				AuList controller = loader.getController();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						AuSetupWin = true;
					}
				});
				stage.show();

			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	public static boolean AuditWin = true;

	/**
	 * Форма аудита
	 */
	public static void AudView() {
		try {
			if (AuditWin) {
				AuditWin = false;
				Stage stage = new Stage();

				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/su/sbra/psv/app/audit/view/Audit.fxml"));

				Audit controller = new Audit();
				loader.setController(controller);
				Parent root = loader.load();

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Аудит");
				stage.initOwner(primaryStage);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						AuditWin = true;
					}
				});
				stage.show();
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Contact
	 */
	public static void ContactComiss() {
		try {
			Stage stage = new Stage();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/contact/Contact.fxml"));

			ContactC controller = new ContactC();

			loader.setController(controller);

			Parent root = loader.load();

			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Комиссии Контакт");
			stage.initOwner(primaryStage);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					controller.dbDisconnect();
				}
			});

			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Сдачи Амра
	 */
	public static void TermDealView() {
		try {
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/view/Termdial.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Сдачи");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Транзакции просмотр Амра
	 */
	public static void TermTrView() {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/trlist/Transact_Amra_viewer.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Загруженные транзакции");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Банк-Клиент
	 */
	public static void BkIbank() {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/ibank/Ibank.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Клиенты");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * swift
	 */
	public static void SwiftFromAbs() {
		try {
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/swift/SWTR.fxml"));

			SWC controller = new SWC();
			controller.SetStageForClose(stage);
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("SWIFT");
			stage.initOwner(stage_);
			stage.setResizable(true);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					// controller.EndTask();
					controller.dbDisconnect();
				}
			});
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * swift
	 */
	public static void SwiftFromMenu() {
		try {
			FXMLLoader loader = new FXMLLoader();

			SWC controller = new SWC();
			controller.SetStageForClose(primaryStage);
			loader.setController(controller);

			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/swift/SWTR.fxml"));
			StackPane employeeOperationsView = (StackPane) loader.load();

			Scene scene = new Scene(employeeOperationsView); // We are sending rootLayout to the Scene.

//			Style startingStyle = Style.LIGHT;
//			JMetro jMetro = new JMetro(startingStyle);
//			System.setProperty("prism.lcdtext", "false");
//			jMetro.setScene(scene);

			primaryStage.setTitle("S.W.I.F.T " + Connect.userID_ + "@" + Connect.connectionURL_);
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					// controller.EndTask();
					controller.dbDisconnect();
				}
			});
			primaryStage.show(); // Display the primary stage

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static void ConvVal() {
		try {
			FXMLLoader loader = new FXMLLoader();
			ConvVal controller = new ConvVal();
			loader.setController(controller);
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/valconv/ConvVal.fxml"));
			VBox employeeOperationsView = (VBox) loader.load();
			Scene scene = new Scene(employeeOperationsView);
			primaryStage.setTitle("CONV_VAL " + Connect.userID_ + "@" + Connect.connectionURL_);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					controller.dbDisconnect();
				}
			});
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Сверка
	 */
	public static void TermClBkSverka() {
		try {
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/sverka/Sverka.fxml"));

			SverkaC controller = new SverkaC();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Сверка");
			stage.initOwner(stage_);
			stage.setResizable(false);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					controller.dbDisconnect();
				}
			});
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Сверка
	 */
	public static void SbraResourcesMonitor() {
		try {
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/admin/rescron/SBResJob.fxml"));

			SBResJob controller = new SBResJob();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Ресурсы:");
			stage.initOwner(stage_);
			stage.setResizable(true);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					// controller.dbDisconnect();
				}
			});
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * История загрузок Квант
	 */
	@Deprecated
	public static void Load_Hist() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/LoadHist.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Первая форма
	 */
	public static void ShFirstView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/First.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Меню
	 */
	public static void RT() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/RootLayout.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/* Вход */
	public void Logon() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/Enter.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.setResizable(false);
			primaryStage.centerOnScreen();
			primaryStage.show(); // Display the primary stage
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/* Терминалы */
	public static void TerminalControl() {
		try {

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/terminals/Terminal.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Терминалы");
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * История загрузок
	 */
	public static void TermTrLoadHist() {
		try {
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/view/ShowHist.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("История загрузок");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Услуги
	 */
	public static void TermServiceCtrl() {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/termserv/Service.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Сервисы");
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean ActWin = true;

	/**
	 * Доступ не используется
	 */
	public static void GrantAccessAction() {
		try {
			if (ActWin) {
				ActWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(
						Main.class.getResource("/su/sbra/psv/app/access/action/ODB_ACTION.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Доступ по действиям");
				stage.initOwner(primaryStage);
				// stage.initModality(Modality.WINDOW_MODAL);
				OdbActions controller = loader.getController();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						ActWin = true;
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean GrpAccessWin = true;

	public static void GrantAccessGrp() {
		try {
			if (GrpAccessWin) {
				GrpAccessWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(
						Main.class.getResource("/su/sbra/psv/app/access/grp/GrpMember.fxml"));

				GrpController controller = new GrpController();
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.setResizable(true);
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Группы доступа по функционалу");
				stage.initOwner(primaryStage);

				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						GrpAccessWin = true;
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	/**
	 * Доступ Ведомственная информация
	 */
	public static void Debtinfo() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/DebtInfo.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			Scene scene = new Scene(employeeOperationsView, 600, 450); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Копировать доверенность Не используется
	 */
	@Deprecated
	public static void CopyDover() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/view/CopyDover.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			Scene scene = new Scene(employeeOperationsView, 600, 450); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean PensWin = true;

	/**
	 * Пенсия
	 */
	public static void sep() {
		try {
			if (PensWin) {
				PensWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/su/sbra/psv/app/pensia/Pens_divide.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Пенсия РФ");
				stage.initOwner(primaryStage);
				PensC controller = loader.getController();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						PensWin = true;
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Пенсия
	 */
	@Deprecated
	public static void PensiaRa() {
		try {
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/view/Pens_RA.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Пенсия РА");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Загрузка транз. Квант-Капитал
	 */
	@Deprecated
	public static void Transact() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/su/sbra/psv/app/trload/TransactLoad.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean Tr = true;

	/**
	 * Загрузка транзакции
	 */
	public static void TermTrLoad() {
		try {
			if (Tr) {
				Tr = false;
				Stage stage = new Stage();
				// Stage stage_ = (Stage) primaryStage.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("/su/sbra/psv/app/loadamra/Amra_Trans.fxml"));

				Amra_Transact controller = new Amra_Transact();
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Загрузка транзакции:");
				// stage.initOwner(stage_);
				stage.setResizable(true);

				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						try {
							Tr = true;
							controller.dbDisconnect();
						} catch (SQLException e) {
							Msg.Message(ExceptionUtils.getStackTrace(e));
						}
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Точка входа
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 0 & args.length == 5) {
				Connect.userID_ = args[0];
				Connect.userPassword_ = args[1];
				Connect.connectionURL_ = args[2];
				Connect.trnnum = args[3];
				Connect.trnanum = args[4];
				MODULE = "DEBTINFO";
			} else if (args.length != 0 & args.length == 4) {
				Connect.userID_ = args[0];
				Connect.userPassword_ = args[1];
				Connect.connectionURL_ = args[2];
				MODULE = args[3];
				System.out.println(args[3]);
			} else if (args.length != 0 & args.length == 7) {
				Connect.userID_ = args[0];
				Connect.userPassword_ = args[1];
				Connect.connectionURL_ = args[2];
				MODULE = args[3];
				Connect.trnnum = args[4];
				Connect.trnanum = args[5];
			}
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}

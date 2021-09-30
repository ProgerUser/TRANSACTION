package app;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import access.action.OdbActions;
import access.grp.GrpController;
import access.menu.OdbMNU;
import app.model.Connect;
import app.pensia.PensC;
import app.util.DBUtil;
import contact.ContactC;
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
import sb.utils.DbUtil;
import sbalert.Msg;
import sverka.SverkaC;
import swift.SWC;
import tr.pl.Pl;
import valconv.ConvVal;

/**
 * ����� �����
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
			Main.primaryStage.setTitle("����������");
			logger.setLevel(Level.INFO);

			if (MODULE == null) {
				Enter();
			} else if (MODULE.equals("DEBTINFO")) {
				DBUtil.dbConnect();
				Debtinfo();
			} else if (MODULE.equals("BUH")) {
				DBUtil.dbConnect();
				initRootLayout();
				showFirst();
			} else if (MODULE.equals("SWIFT")) {
				DBUtil.dbConnect();
				swift2();
			} else if (MODULE.equals("VTB_CONV")) {
				DBUtil.dbConnect();
				ConvVal();
			}
			
			{
//				Connect.connectionURL_ = "10.111.64.21:1521/ODB";
//				Connect.userID_ = "SAIDP";
//				Connect.userPassword_ = "";
//				DbUtil.Db_Connect();
//				DBUtil.dbConnect();
				//showKash();
//				initRootLayout();
//				showFirst();
				//Terminal();
				//Service();
			}
			
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
	public static void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/rootlayout/RootLayout.fxml"));
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
	 * �� ������������
	 */
	public static void showEmployeeView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Viewer.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean MenuWin = true;

	/**
	 * ������ �� ���������
	 */
	public static void Admin_Menu() {
		try {
			if (MenuWin) {
				MenuWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/access/menu/ODB_MNU.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("����� ������� � ������� ����");
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
	 * ������������ �����������
	 */
	public static void showKash() {
		try {
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Kash.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("����������");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * ������������ �����������
	 */
	public static void PlastRash() {
		try {
			
			if (DbUtil.Odb_Action(14l) == 0) {
				Msg.Message("��� �������!");
				return;
			}
			
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/tr/pl/Pl.fxml"));

			Pl controller = new Pl();
			loader.setController(controller);
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("/icon.png"));
			stage.setTitle("������ � ��������");
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

	/**
	 * Contact
	 */
	public static void Contact() {
		try {
			Stage stage = new Stage();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/contact/Contact.fxml"));

			ContactC controller = new ContactC();

			loader.setController(controller);

			Parent root = loader.load();

			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("�������� �������");
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
	 * ����� ����
	 */
	public static void Termdial_view_() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Termdial.fxml")); BorderPane
			 * employeeOperationsView = (BorderPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Termdial.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("�����");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ���������� �������� ����
	 */
	public static void showAmTr() {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("/trlist/Transact_Amra_viewer.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("����������� ����������");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ����-������
	 */
	public static void Ibankk() {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("/app/ibank/Ibank.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("�������");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * swift
	 */
	public static void swift() {
		try {
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/swift/SWTR.fxml"));

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
	public static void swift2() {
		try {
			FXMLLoader loader = new FXMLLoader();

			SWC controller = new SWC();
			controller.SetStageForClose(primaryStage);
			loader.setController(controller);

			loader.setLocation(Main.class.getResource("/swift/SWTR.fxml"));
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
			loader.setLocation(Main.class.getResource("/valconv/ConvVal.fxml"));
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
	 * ������
	 */
	public static void Sverka() {
		try {
			Stage stage = new Stage();
			Stage stage_ = (Stage) primaryStage.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/sverka/Sverka.fxml"));

			SverkaC controller = new SverkaC();
			loader.setController(controller);

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("������");
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
	 * ������� �������� �����
	 */
	public static void Load_Hist() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoadHist.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ������ �����
	 */
	public static void showFirst() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/app/view/First.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ����
	 */
	public static void RT() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/* ���� */
	public void Enter() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Enter.fxml"));
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

	/* ��������� */
	public static void Terminal() {
		try {

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/app/terminals/Terminal.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("���������");
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/* ������� �������� */
	public static void Show_Hist() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/ShowHist.fxml")); BorderPane
			 * employeeOperationsView = (BorderPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/ShowHist.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("������� ��������");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/* ������ */
	public static void Service() {
		try {
			Stage stage = new Stage();
			Parent root= FXMLLoader.load(Main.class.getResource("/app/termserv/Service.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("�������");
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	public static boolean ActWin = true;

	/* ������ �� ������������ */
	public static void Admin() {
		try {
			if (ActWin) {
				ActWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/access/action/ODB_ACTION.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("������ �� ���������");
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

	/* ������ �� ������������ */
	public static void AccessGroup() {
		try {
			if (GrpAccessWin) {
				GrpAccessWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/access/grp/GrpMember.fxml"));

				GrpController controller = new GrpController();
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.setResizable(false);
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("������ ������� �� �����������");
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

//	/* ������ ���� */
//	public static void Admin_Menu_() {
//		try {
//			/*
//			 * FXMLLoader loader = new FXMLLoader();
//			 * loader.setLocation(Main.class.getResource("view/Admin_Menu.fxml"));
//			 * AnchorPane employeeOperationsView = (AnchorPane) loader.load();
//			 * rootLayout.setCenter(employeeOperationsView);
//			 */
//
//			Stage stage = new Stage();
//			Parent root;
//			root = FXMLLoader.load(Main.class.getResource("view/Admin_Menu.fxml"));
//			stage.setScene(new Scene(root));
//			stage.getIcons().add(new Image("icon.png"));
//			stage.setTitle("������ �� ������� ����");
//			stage.initOwner(primaryStage);
//			stage.show();
//		} catch (Exception e) {
//			Msg.Message(ExceptionUtils.getStackTrace(e));
//		}
//	}

	/**
	 * ������ ������������� ����������
	 */
	public static void Debtinfo() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/DebtInfo.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			Scene scene = new Scene(employeeOperationsView, 600, 450); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ���������� ������������ �� ������������
	 */
	public static void CopyDover() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CopyDover.fxml"));
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
	 * ������
	 */
	public static void sep() {
		try {
			if (PensWin) {
				PensWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/app/pensia/Pens_divide.fxml"));
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("������ ��");
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
	 * ������
	 */
	public static void sepRA() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Pens_RA.fxml")); AnchorPane
			 * employeeOperationsView = (AnchorPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Pens_RA.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("������ ��");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * �������� �����. �����-�������
	 */
	@Deprecated
	public static void Transact() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/app/trload/TransactLoad.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * �������� ����������
	 */
	public static void Transact_Amra() {
		try {
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/app/loadamra/Amra_Trans.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("�������� ����������");
			stage.setResizable(true);
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

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

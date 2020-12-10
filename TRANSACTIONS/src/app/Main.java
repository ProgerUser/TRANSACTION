package app;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import app.model.Connect;
import app.util.DBUtil;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sbalert.Msg;
import sverka.SverkaC;
import swift.SWC;

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
			/* log4j */
			DOMConfigurator.configure(getClass().getResource("/log4j.xml"));
			Main.primaryStage = primaryStage;
			primaryStage.getIcons().add(new Image("icon.png"));
			Main.primaryStage.setTitle("Транзакции");
			logger.setLevel(Level.INFO);
			//System.out.println(MODULE);
			
			
			if (MODULE == null) {
				Enter();
			} else if (MODULE.equals("DEBTINFO")) {
				DBUtil.dbConnect();
				initRootLayout();
				Debtinfo();
			} else if (MODULE.equals("BUH")) {
				DBUtil.dbConnect();
				initRootLayout();
				showFirst();
			} else if (MODULE.equals("SWIFT")) {
				//DBUtil.dbConnect();
				//initRootLayout();
				//swift2();
			}
			
			

			/*
			  Connect.connectionURL_ = "10.111.64.21:1521/odb";
			  Connect.userID_ = "SAIDP";
			  Connect.userPassword_ = "xxx"; 
			  DBUtil.dbConnect(); 
			  initRootLayout();
			  swift2();
			*/
			primaryStage.setOnCloseRequest(e -> {
				DBUtil.dbDisconnect();
				Platform.exit();
				System.exit(0);

			});

			// this.primaryStage.setMaximized(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage() + "~" + Thread.currentThread().getName());
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
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Не используется
	 */
	public static void showEmployeeView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Viewer.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Формирование псевдонимов
	 */
	public static void showKash() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Kash.fxml")); AnchorPane
			 * employeeOperationsView = (AnchorPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Kash.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Псевдонимы");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Contact
	 */
	public static void Contact() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Contact.fxml")); AnchorPane
			 * employeeOperationsView = (AnchorPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Contact.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Комиссии Контакт");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Сдачи Амра
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
			stage.setTitle("Сдачи");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Транзакции просмотр Амра
	 */
	public static void showAmTr() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
			 * BorderPane employeeOperationsView = (BorderPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("/trlist/Transact_Amra_viewer.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Загруженные транзакции");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Банк-Клиент
	 */
	public static void Ibankk() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Ibank.fxml")); VBox
			 * employeeOperationsView = (VBox) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Ibank.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Клиенты");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
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
			stage.setResizable(false);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					controller.EndTask();
					controller.dbDisconnect();
				}
			});
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
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

			primaryStage.setTitle("S.W.I.F.T "+Connect.userID_+"@"+Connect.connectionURL_);
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent paramT) {
					controller.EndTask();
					controller.dbDisconnect();
				}
			});
			primaryStage.show(); // Display the primary stage

		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Сверка
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
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * История загрузок Квант
	 */
	public static void Load_Hist() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoadHist.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Первая форма
	 */
	public static void showFirst() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/First.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Меню
	 */
	public static void RT() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/* Вход */
	public void Enter() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Enter.fxml")); BorderPane
			 * employeeOperationsView = (BorderPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Enter.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.setResizable(false);
			primaryStage.centerOnScreen();
			primaryStage.show(); // Display the primary stage
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/* Терминалы */
	public static void Terminal() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Terminal.fxml")); BorderPane
			 * employeeOperationsView = (BorderPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Terminal.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Терминалы");
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/* История загрузок */
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
			stage.setTitle("История загрузок");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/* Услуги */
	public static void Service() {
		try {
			// FXMLLoader loader = new FXMLLoader();
			// loader.setLocation(Main.class.getResource("view/Service.fxml"));
			// BorderPane employeeOperationsView = (BorderPane) loader.load();
			// rootLayout.setCenter(employeeOperationsView);

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Service.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Сервисы");
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/* Доступ не используется */
	public static void Admin() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Admin.fxml")); AnchorPane
			 * employeeOperationsView = (AnchorPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Admin.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/* Доступ Меню */
	public static void Admin_Menu() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Admin_Menu.fxml"));
			 * AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Admin_Menu.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Доступ по пунктам меню");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Доступ Ведомственная информация
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
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Копировать доверенность Не используется
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
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Пенсия
	 */
	public static void sep() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Pens_divide.fxml"));
			 * AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 */

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Pens_divide.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Пенсия РФ");
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();

		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Пенсия
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
			stage.setTitle("Пенсия РА");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Загрузка транз. Квант-Капитал
	 */
	public static void Transact() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/TransactLoad.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (Exception e) {
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Загрузка транзакции
	 */
	public static void Transact_Amra() {
		try {
			/*
			 * FXMLLoader loader = new FXMLLoader();
			 * loader.setLocation(Main.class.getResource("view/Amra_Trans.fxml"));
			 * BorderPane employeeOperationsView = (BorderPane) loader.load();
			 * rootLayout.setCenter(employeeOperationsView);
			 * 
			 */
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Amra_Trans.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Загрузка транзакции");
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
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
			}
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
	}
}

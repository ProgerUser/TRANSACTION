package sb_tr;

import java.awt.SplashScreen;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sb_tr.model.Connect;
import sb_tr.util.DBUtil;
import sbalert.Msg;

public class Main extends Application {

	public static Logger logger = Logger.getLogger(Main.class);
	public static Stage primaryStage;
	public static BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {

		/* log4j */
		String log4jConfigFile = System.getenv("TRANSACT_PATH") + "log4j.xml";
		System.out.print(log4jConfigFile + "\r\n");
		DOMConfigurator.configure(log4jConfigFile);
		/*logger.info("Transact Start: " + Thread.currentThread().getName());*/
		this.primaryStage = primaryStage;
		primaryStage.getIcons().add(new Image("icon.png"));
		this.primaryStage.setTitle("Транзакции");

		
		if (Connect.userID_ != null & Connect.trnnum == null & Connect.trnanum == null & Connect.userPassword_ != null
				& Connect.djdog_id == null) { // primaryStage.setMaximized(true);
			primaryStage.setTitle(Connect.userID_ + "@" + Connect.connectionURL_);
			DBUtil.dbConnect();
			initRootLayout();
			showFirst();
		} else if (Connect.userID_ != null & Connect.trnnum != null & Connect.trnanum != null
				& Connect.userPassword_ != null) {
			primaryStage.setTitle(Connect.userID_ + "@" + Connect.connectionURL_);
			DBUtil.dbConnect();
			initRootLayout();
			Debtinfo();
		} else if (Connect.userID_ != null & Connect.userPassword_ != null & Connect.djdog_id != null) {
			primaryStage.setTitle(Connect.userID_ + "@" + Connect.connectionURL_);
			DBUtil.dbConnect();
			CopyDover();
		} else if (Connect.trnnum == null & Connect.trnanum == null & Connect.userID_ != null
				& Connect.userPassword_ != null) {
			Platform.exit();
			System.exit(0);
		} else if (Connect.userID_ == null & Connect.userPassword_ == null) {
			Enter();
		}
		
		/*
		  Connect.connectionURL_ = "10.111.64.21:1521/odb"; Connect.userID_ ="SAIDP";
		  Connect.userPassword_ = ""; 
		  DBUtil.dbConnect(); 
		  initRootLayout();
		  */
		 //CopyDover();
		 
 

		primaryStage.setOnCloseRequest(e -> {
			DBUtil.dbDisconnect();
			Platform.exit();
			System.exit(0);

		});

		/*this.primaryStage.setMaximized(true);*/
	}

	// Initializes the root layout.
	public static void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Не используется */
	public static void showEmployeeView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Viewer.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Формирование псевдонимов */
	public static void showKash() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Kash.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
			*/
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Kash.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Псевдонимы");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/*Contact */
	public static void Contact() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Contact.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}
	
	/* Сдачи Амра */
	public static void Termdial_view_() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Termdial.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Тразнакции просмотр Амра */
	public static void showAmTr() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
			*/
			
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Загруженные транзакции");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Банк-Клиент */
	public static void Ibankk() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Ibank.fxml"));
			VBox employeeOperationsView = (VBox) loader.load();
			rootLayout.setCenter(employeeOperationsView);
			*/
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Ibank.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Клиенты");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* История загрузок Квант */
	public static void Load_Hist() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoadHist.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Первая форма */
	public static void showFirst() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/First.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Первая форма */
	public static void RT() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
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
			primaryStage.show(); // Display the primary stage
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Терминалы */
	public static void Terminal() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Terminal.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
			*/
			
			
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Terminal.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Терминалы");
			stage.initOwner(primaryStage);
			stage.show();
			
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* История загрузок */
	public static void Show_Hist() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ShowHist.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
			*/
			
			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/ShowHist.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("История загрузок");
			stage.initOwner(primaryStage);
			stage.show();
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Услуги */
	public static void Service() {
		try {
			//FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(Main.class.getResource("view/Service.fxml"));
			//BorderPane employeeOperationsView = (BorderPane) loader.load();
			//rootLayout.setCenter(employeeOperationsView);

			Stage stage = new Stage();
			Parent root;
			root = FXMLLoader.load(Main.class.getResource("view/Service.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Сервисы");
			stage.initOwner(primaryStage);
			stage.show();
			
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Доступ */
	public static void Admin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Admin.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Доступ Меню */
	public static void Admin_Menu() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Admin_Menu.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}
	
	
	/* Доступ Вединоформация */
	public static void Debtinfo() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/DebtInfo.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			Scene scene = new Scene(employeeOperationsView,600,450); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}
	
	/* Копировать доверенность */
	public static void CopyDover() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/CopyDover.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			Scene scene = new Scene(employeeOperationsView,600,450); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Пенсия */
	public static void sep() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Pens_divide.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
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
			
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Пенсия */
	public static void sepRA() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Pens_RA.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Загрузка транз. Квант-Капитал */
	public static void Transact() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/TransactLoad.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	/* Загрузка транзакции */
	public static void Transact_Amra() {
		try {
			/*
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Amra_Trans.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
			
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
		} catch (IOException e) {
			Msg.Messge(e.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			final SplashScreen splash = SplashScreen.getSplashScreen();
		    if (splash != null) {
		        splash.close();
		    }
			if (args.length != 0 & args.length == 3) {
				Connect.userID_ = args[0];
				Connect.userPassword_ = args[1];
				Connect.connectionURL_ = args[2];
			} else if (args.length != 0 & args.length == 5) {
				Connect.userID_ = args[0];
				Connect.userPassword_ = args[1];
				Connect.connectionURL_ = args[2];
				Connect.trnnum = args[3];
				Connect.trnanum = args[4];
			} else if (args.length != 0 & args.length == 4) {
				Connect.userID_ = args[0];
				Connect.userPassword_ = args[1];
				Connect.connectionURL_ = args[2];
				Connect.djdog_id = args[3];
			}
			launch(args);
		} catch (Exception e) {
			Msg.Messge(e.getMessage());
		}
	}
}

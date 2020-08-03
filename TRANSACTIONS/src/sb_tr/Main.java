package sb_tr;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sb_tr.model.Connect;
import sb_tr.util.DBUtil;

//Main class which extends from Application Class

public class Main extends Application {

	// This is our PrimaryStage (It contains everything)
	public Stage primaryStage;

	// This is the BorderPane of RootLayout
	public static BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {

		// 1) Declare a primary stage (Everything will be on this stage)
		this.primaryStage = primaryStage;

		primaryStage.getIcons().add(new Image("icon.png"));
		// Optional: Set a title for primary stage

		this.primaryStage.setTitle("Транзакции");

		// 2) Initialize RootLayout
		initRootLayout();

		// 3) Display the EmployeeOperations View
		// showEmployeeView();

		
		if (Connect.userID_ != null) { // primaryStage.setMaximized(true);
			primaryStage.setTitle(Connect.userID_ + "@" + Connect.connectionURL_);
			DBUtil.dbConnect();
			Main.showFirst();
		} else {
			Enter();
		}
		
		/*
		 * Connect.connectionURL_ = "10.111.64.21:1521/odb"; Connect.userID_ =
		 * "AMRA_IMPORT"; Connect.userPassword_ = "xxx"; DBUtil.dbConnect();
		 * showFirst();
		 */
		primaryStage.setOnCloseRequest(e -> {
			DBUtil.dbDisconnect();
			/*
			 * File file = new File(System.getProperty("user.home") +
			 * "/XXI.AP_TEST_MAIN.properties"); file.delete();
			 * System.out.print("------------------------------------------------------");
			 */
			Platform.exit();
			System.exit(0);

		});

		// this.primaryStage.setMaximized(true);
	}

	// Initializes the root layout.
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.
			primaryStage.show(); // Display the primary stage
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/* Формирование псевдонимов */
	public static void showKash() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Kash.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/* Тразнакции просмотр Амра */
	public static void showAmTr() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Банк-Клиент */
	public static void Ibankk() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Ibank.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/* Вход */
	public void Enter() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Enter.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Терминалы */
	public static void Terminal() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Terminal.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* История загрузок */
	public static void Show_Hist() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ShowHist.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Услуги */
	public static void Service() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Service.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/* Пенсия */
	public static void sep() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Pens_divide.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/* Загрузка транзакции */
	public static void Transact_Amra() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Amra_Trans.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length != 0) {
			Connect.userID_ = args[0];
			Connect.userPassword_ = args[1];
			Connect.connectionURL_ = args[2];
		}
		launch(args);
	}
}

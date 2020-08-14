package sb_tr;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
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

public class Main extends Application {

	// Define a static logger variable so that it references the
	// Logger instance named "MyApp".
	public static Logger logger = Logger.getLogger(Main.class);
	// This is our PrimaryStage (It contains everything)
	public static Stage primaryStage;

	// This is the BorderPane of RootLayout
	public static BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {

		/* log4j */
		String log4jConfigFile = System.getenv("TRANSACT_PATH") + "log4j.xml";
		System.out.print(log4jConfigFile + "\r\n");
		DOMConfigurator.configure(log4jConfigFile);
		//logger.info("Transact Start: " + Thread.currentThread().getName());

		// 1) Declare a primary stage (Everything will be on this stage)
		this.primaryStage = primaryStage;

		primaryStage.getIcons().add(new Image("icon.png"));
		// Optional: Set a title for primary stage

		this.primaryStage.setTitle("����������");

		// 2) Initialize RootLayout
		// 3) Display the EmployeeOperations View
		// showEmployeeView();

		// initRootLayout();
		if (Connect.userID_ != null) { // primaryStage.setMaximized(true);
			primaryStage.setTitle(Connect.userID_ + "@" + Connect.connectionURL_);
			DBUtil.dbConnect();
			initRootLayout();
			showFirst();
		} else {
			Enter();
		}

		/*
		 * Connect.connectionURL_ = "10.111.64.21:1521/odb"; Connect.userID_ ="SAIDP";
		 * Connect.userPassword_ = "xxx"; DBUtil.dbConnect(); showFirst();
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
	public static void initRootLayout() {
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

	/* �� ������������ */
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

	/* ������������ ����������� */
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

	/* ����� ���� */
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

	/* ���������� �������� ���� */
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

	/* ����-������ */
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

	/* ������� �������� ����� */
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

	/* ������ ����� */
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

	/* ������ ����� */
	public static void RT() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* ���� */
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
			e.printStackTrace();
		}
	}

	/* ��������� */
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

	/* ������� �������� */
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

	/* ������ */
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

	/* ������ */
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

	/* ������ ���� */
	public static void Admin_Menu() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Admin_Menu.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* ������ */
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

	/* ������ */
	public static void sepRA() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Pens_RA.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* �������� �����. �����-������� */
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

	/* �������� ���������� */
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

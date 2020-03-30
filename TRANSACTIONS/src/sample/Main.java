package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Connect;

import java.io.File;
import java.io.IOException;

//Main class which extends from Application Class

public class Main extends Application {

	// This is our PrimaryStage (It contains everything)
	public  Stage primaryStage;

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

		Enter();
/*
		Connect.connectionURL_ = "10.111.64.21:1521/odb";
		Connect.userID_ = "XXI";
		Connect.userPassword_ = "man9o";
		showFirst();
*/
		primaryStage.setOnCloseRequest(e->{
			File file = new File(System.getProperty("user.home")+"/XXI.AP_TEST_MAIN.properties");
			file.delete();
			System.out.print("------------------------------------------------------");
			Platform.exit();
			System.exit(0);
			});
		//this.primaryStage.setMaximized(true);
	}

	// Initializes the root layout.
	public void initRootLayout() {
		try {
			// First, load root layout from RootLayout.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Second, show the scene containing the root layout.
			Scene scene = new Scene(rootLayout); // We are sending rootLayout to the Scene.
			primaryStage.setScene(scene); // Set the scene in primary stage.

			/*
			 * //Give the controller access to the main. RootLayoutController controller =
			 * loader.getController(); controller.setMain(this);
			 */

			// Third, show the primary stage
			primaryStage.show(); // Display the primary stage
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows the employee operations view inside the root layout.
	public static void showEmployeeView() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Viewer.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void showKash() {
		try {
			// First, load EmployeeView from EmployeeView.fxml Kash AnchorPane
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Kash.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Termdial_view_() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Termdial.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void showAmTr() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Load_Hist() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LoadHist.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows the employee operations view inside the root layout.
	public static void showFirst() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/First.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Shows the employee operations view inside the root layout.
	public void Enter() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Enter.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();
			// TabPane tp = (TabPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);

			// Set Employee Operations view into the center of root layout.
			// rootLayout.setCenter(tp);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Terminal() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Terminal.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Show_Hist() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ShowHist.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Service() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Service.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Transact() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/TransactLoad.fxml"));
			AnchorPane employeeOperationsView = (AnchorPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Transact_Amra() {
		try {
			// First, load EmployeeView from EmployeeView.fxml
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Amra_Trans.fxml"));
			BorderPane employeeOperationsView = (BorderPane) loader.load();

			// Set Employee Operations view into the center of root layout.
			rootLayout.setCenter(employeeOperationsView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

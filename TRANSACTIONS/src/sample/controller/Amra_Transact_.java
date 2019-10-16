package sample.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sample.model.Connect;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Amra_Transact_ {
	/*
	 * final static String driverClass = "oracle.jdbc.OracleDriver"; final static
	 * String connectionURL = "jdbc:oracle:thin:@oradb-prm:1521/odb"; final static
	 * String userID = "xxi"; final static String userPassword = "xxx";
	 */
	static String sql = "{ ? = call z_sb_create_tr_amra.load_pack(?,?)}";

	static String sql_calc = "{ ? = call z_sb_calc_tr_amra.make(?)}";

	static String sessid_ = null;

	@SuppressWarnings("resource")
	private static String readFile(String fileName) throws FileNotFoundException, IOException {
		/*
		 * FileInputStream fis = null; InputStreamReader isr = null; String encoding;
		 * fis = new FileInputStream(fileName); isr = new InputStreamReader(fis); // the
		 * name of the character encoding returned encoding = isr.getEncoding();
		 */

// System.out.print("Character Encoding: "+s);
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(fileName), getFileCharset(fileName)));
		String nextLine = "";
		StringBuffer sb = new StringBuffer();
		while ((nextLine = br.readLine()) != null) {
			sb.append(nextLine);
			sb.append(System.lineSeparator());
		}
		String clobData = sb.toString();
		return clobData;
	}

	private Integer sess_id = null;

	private String full_pach = null;

	@FXML
	private Button open_new;

	@FXML
	private ResourceBundle resources;

	@FXML
	private Button calc;

	@FXML
	private URL location;

	@FXML
	private Button browse;

	@FXML
	private AnchorPane anchorpane;

	@FXML
	private Button import_;

	@FXML
	private TextField textbox;

	@FXML
	private CheckBox chk;

	@FXML
	private ProgressIndicator pb;

	@FXML
	private Label trsum;
	@FXML
	private Label txtfilecount;
	@FXML
	private Label sessid;
	@FXML
	private Label loadtrcount;
	@FXML
	private Label dealsum;
	@FXML
	private Label deal;

	static PrintWriter writer;

	static int rowline = 0;

	@FXML
	void Choose(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Выбрать файл");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("eXtensible Markup Language",
				"*.xml")/*
						 * , new ExtensionFilter("Text Files", "*.txt")
						 */);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			textbox.setText(file.getParent() + "::_" + file.getName());
		}
	}

	/* create Runnable using anonymous inner class */
	Thread t1 = new Thread(new Runnable() {
		public void run() {
			try {
				if (!textbox.getText().equals("") & textbox.getText().contains("\\")) {

					Date date = new Date();

					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");

					String strDate = dateFormat.format(date);

					Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
							+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

					CallableStatement callStmt = null;
					String reviewContent = null;

					callStmt = conn.prepareCall(sql);

					String reviewStr = readFile(textbox.getText().replace("::_", "\\"));

					Clob clob = conn.createClob();
					clob.setString(1, reviewStr);

					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setClob(2, clob);
					callStmt.setString(3, textbox.getText());

					callStmt.execute();

					reviewContent = callStmt.getString(1);

					String[] parts = reviewContent.split(":");
					String part1 = parts[0].trim();
					String part2 = parts[1].trim();

					sessid_ = part2;

					String[] path_ = textbox.getText().toString().split("::_");
					String path1_ = path_[0].trim();

					Integer rowid = 1;
					if (part1.equals("1")) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("terminal.png"));
						alert.setTitle("Внимание");
						alert.setHeaderText(null);
						alert.setContentText("Найдены ошибки, скоро откроется файл с описанием.");
						alert.showAndWait();

						Statement sqlStatement = conn.createStatement();
						String readRecordSQL = "SELECT * FROM Z_SB_LOG_AMRA WHERE sess_id = " + part2 + "";
						ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

						// String[] path =
						// textbox.getText().toString().split("::_");
						// String path1 = path[0].trim();

						DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
						String strDate_ = dateFormat_.format(date);
						String createfolder = System.getenv("TRANSACT_PATH")/*System.getProperty("user.dir") + "\\"*/ + strDate_ + "_SESSID_" + sessid_;

						File file = new File(createfolder);
						if (!file.exists()) {
							if (file.mkdir()) {
								System.out.println("Directory is created!");
							} else {
								System.out.println("Failed to create directory!");
							}
						}

						String path_file = createfolder + "\\" + strDate + "_ERROR.txt";
						PrintWriter writer = new PrintWriter(path_file);
						while (myResultSet.next()) {
							writer.write(rowid + " ____ " + myResultSet.getString("recdate") + " ____ "
									+ myResultSet.getString("paydate") + " ____ " + myResultSet.getString("desc_")
									+ " ____ " + myResultSet.getString("sess_id") + "\r\n");
							rowid++;
						}
						writer.close();
						ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
								createfolder + "\\" + strDate + "_ERROR.txt");
						pb.start();
						myResultSet.close();
						textbox.setText("");
					} else {
						// --------------------------------------
						Protocol(part2);
						// --------------------------------------
						chk.setDisable(false);
						chk.setSelected(true);
						calc.setDisable(false);

						full_pach = path1_;

						textbox.setText("");
						sess_id = Integer.parseInt(part2);
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("terminal.png"));
						alert.setTitle("Внимание");
						alert.setHeaderText(null);
						alert.setContentText("Загрузка прошла успешна. Можете перейти к расчету");
						alert.showAndWait();
					}
					callStmt.close();
					conn.close();
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Выберите сначала файл для загрузки");
					alert.showAndWait();
				}
			} catch (SQLException | IOException e) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
	});

	@FXML
	void Load_Transact(ActionEvent event) {
		/* start processing on new threads */
//		ScrollPane scrollPane = new ScrollPane();
//		
//		progressbar.setVisible(true);
//        scrollPane.setDisable(true);
//        
//        Task<Void> testTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                // Send the message
//                return null;
//            }
//        };
//        testTask.setOnFailed(event -> {
//        	progressbar.setVisible(false);
//            scrollPane.setDisable(false);
//        });
//        new Thread(testTask).start();
        
		pb.setVisible(true);
		t1.start();
	}

	static int count_ = 1;

	public static String getFileCharset(String file) throws IOException {
		byte[] buf = new byte[4096];
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		final UniversalDetector universalDetector = new UniversalDetector(null);
		int numberOfBytesRead;
		while ((numberOfBytesRead = bufferedInputStream.read(buf)) > 0 && !universalDetector.isDone()) {
			universalDetector.handleData(buf, 0, numberOfBytesRead);
		}
		universalDetector.dataEnd();
		String encoding = universalDetector.getDetectedCharset();
		universalDetector.reset();
		bufferedInputStream.close();
		return encoding;
	}

// запись в текстовый файл протокола загрузки
	void Protocol(String sessid) {
		try {
			System.out.println(sessid);
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
			String strDate = dateFormat.format(date);
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

// String[] path = textbox.getText().toString().split("::_");
// String path1 = path[0].trim();

			Statement sqlStatement = conn.createStatement();
// String count = "SELECT count(*) FROM Z_SB_TRANSACT_DBT WHERE
// sess_id
// = " + sessid + "";
			String readRecordSQL = "SELECT * FROM z_sb_transact_amra_dbt WHERE sess_id = " + sessid + "";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

			Integer rowid = 1;

			DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
			String strDate_ = dateFormat_.format(date);
			String createfolder = System.getenv("TRANSACT_PATH")/*System.getProperty("user.dir") + "\\"*/ + strDate_ + "_SESSID_" + sessid;

			File file = new File(createfolder);
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}

			String path_file = createfolder + "\\" + strDate + "_PROTOCOL.txt";

			writer = new PrintWriter(path_file);

			boolean chk = false;
			while (myResultSet.next()) {
				if (chk == false) {
					/*
					 * BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(new
					 * FileInputStream(textbox.getText().replace("::_", "\\")),
					 * getFileCharset(textbox.getText().replace("::_", "\\"))));
					 */
// System.out.println(getFileCharset(textbox.getText().replace("::_",
// "\\")));
					String line = null;
					int rowcount = 0;
					writer.write("Протокол загрузки файла.\r\n");
					/*
					 * while ((line = bufferedReader.readLine()) != null) { rowcount = rowcount + 1;
					 * // System.out.println(line); writer.write("Номер строки: " + rowcount + ";" +
					 * line + "\r\n"); }
					 */

					File fXmlFile = new File(textbox.getText().replace("::_", "\\"));
					DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
// writer.write("Root element :" +
// doc.getDocumentElement().getNodeName() + "\r\n");
					if (doc.hasChildNodes()) {
						printNote(doc.getChildNodes());
					}
					rowline = 0;

// bufferedReader.close();
					writer.write(
							"\r\n-----------------------------------------------------------------------------------------\r\n\r\n");
					writer.write("\r\nПротокол загрузки транзакции.\r\n\r\n");
				}
				writer.write("Номер строки: " + rowid + "|\r\n" + myResultSet.getString("RECDATE") + "|"
						+ myResultSet.getString("PAYDATE") + "|" + myResultSet.getString("CURRENCY") + "|"
						+ myResultSet.getString("PAYMENTTYPE") + "|" + myResultSet.getString("VK") + "|"
						+ myResultSet.getString("DATEOFOPERATION") + "|" + myResultSet.getString("DATAPS") + "|"
						+ myResultSet.getString("DATECLEARING") + "|" + myResultSet.getString("DEALER") + "|"
						+ myResultSet.getString("ACCOUNTPAYER") + "|" + myResultSet.getString("CARDNUMBER") + "|"
						+ myResultSet.getString("OPERATIONNUMBER") + "|"
						+ myResultSet.getString("OPERATIONNUMBERDELIVERY") + "|" + myResultSet.getString("CHECKNUMBER")
						+ "|" + myResultSet.getString("CHECKPARENT") + "|" + myResultSet.getString("ORDEROFPROVIDENCE")
						+ "|" + myResultSet.getString("PROVIDER") + "|" + myResultSet.getString("OWNINOWN") + "|"
						+ myResultSet.getString("CORRECTED") + "|" + myResultSet.getString("COMMISSIONRATE") + "|"
						+ myResultSet.getString("STATUS") + "|" + myResultSet.getString("STRINGFROMFILE") + "|"
						+ myResultSet.getString("REWARDAMOUNT") + "|" + myResultSet.getString("OWNERINCOMEAMOUNT") + "|"
						+ myResultSet.getString("COMMISSIONAMOUNT") + "|" + myResultSet.getString("NKAMOUNT") + "|"
						+ myResultSet.getString("MAXCOMMISSIONAMOUNT") + "|"
						+ myResultSet.getString("MINCOMMISSIONAMOUNT") + "|" + myResultSet.getString("CASHAMOUNT") + "|"
						+ myResultSet.getString("SUMNALPRIMAL") + "|" + myResultSet.getString("AMOUNTTOCHECK") + "|"
						+ myResultSet.getString("AMOUNTOFPAYMENT") + "|" + myResultSet.getString("SUMOFSPLITTING") + "|"
						+ myResultSet.getString("AMOUNTINTERMEDIARY") + "|" + myResultSet.getString("AMOUNTOFSCS") + "|"
						+ myResultSet.getString("AMOUNTWITHCHECKS") + "|" + myResultSet.getString("COUNTER") + "|"
						+ myResultSet.getString("TERMINAL") + "|" + myResultSet.getString("TERMINALNETWORK") + "|"
						+ myResultSet.getString("TRANSACTIONTYPE") + "|" + myResultSet.getString("SERVICE") + "|"
						+ myResultSet.getString("FILETRANSACTIONS") + "|" + myResultSet.getString("FIO") + "|"
						+ myResultSet.getString("CHECKSINCOMING") + "|" + myResultSet.getString("BARCODE") + "|"
						+ myResultSet.getString("ISARESIDENT") + "|" + myResultSet.getString("VALUENOTFOUND") + "|"
						+ myResultSet.getString("PROVIDERTARIFF") + "|" + myResultSet.getString("COUNTERCHECKS") + "|"
						+ myResultSet.getString("COUNTERCHECK") + "|" + myResultSet.getString("ID_") + "|"
						+ myResultSet.getString("DETAILING") + "|" + myResultSet.getString("WALLETPAYER") + "|"
						+ myResultSet
								.getString("WALLETRECEIVER")
						+ "|" + myResultSet.getString("PURPOSEOFPAYMENT") + "|" + myResultSet.getString("DATAPROVIDER")
						+ "|" /* + myResultSet.getString("ATTRIBUTES_") + "|" */
						+ myResultSet.getString("STATUSABS") + "|" + myResultSet.getString("SESS_ID") + "|" + "\r\n");
				rowid++;
				chk = true;
			}
			chk = false;
			writer.close();
			count_ = 1;
			ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_PROTOCOL.txt");
			pb.start();
			myResultSet.close();
		} catch (SQLException | IOException | ParserConfigurationException | SAXException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private static void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
// убедитесь, что это элемент узла.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getNodeName().toString().contains("Трн")) {
					rowline++;
					writer.write("\r\nНомер строки_: " + rowline + "|\r\n");
				}
// получить имя и значение узла
// writer.write("\nNode Name =" +
// tempNode.getNodeName().replaceAll("\\s+", "") + " [OPEN]" +
// "\n");
				if (tempNode.getNodeName().toString().contains("Трн")) {
					count_ = count_ + 1;
				}
// writer.write("Node Value =" +
// tempNode.getTextContent().replaceAll("\\s+", "") + "\r\n");
				if (tempNode.hasAttributes()) {
// получить имена и значения атрибутов
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node node = nodeMap.item(i);
						/*
						 * writer.write("attr name : " + node.getNodeName() + "\r\n");
						 * writer.write("attr value : " + node.getNodeValue() + "\r\n");
						 */
						if (tempNode.getNodeName().toString().contains("Трн")) {
							writer.write(node.getNodeName().replaceAll("\\s+", "") + ":\""
									+ node.getNodeValue().replaceAll("\\s+", "") + "\"|");
						}
					}
				}
				if (tempNode.hasChildNodes()) {
// цикл снова, если есть дочерние узлы
					printNote(tempNode.getChildNodes());
				}
// writer.write("Node Name =" + tempNode.getNodeName() + "
// [CLOSE]" + "\r\n");
				if (tempNode.getNodeName().toString().contains("Трн")) {
					writer.write("\r\n");
				}
			}
		}
	}

	@FXML
	void Calc_Transact(ActionEvent event) {
		try {
			if (sess_id != null & full_pach != null) {
				Date date = new Date();

				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");

				String strDate = dateFormat.format(date);

				CallableStatement callStmt = null;
				Clob reviewContent = null;
				Connection conn;

				conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_
						+ "@" + Connect.connectionURL_ + "");
				callStmt = conn.prepareCall(sql_calc);

				callStmt.registerOutParameter(1, Types.CLOB);
				callStmt.setInt(2, sess_id);

				callStmt.execute();

				reviewContent = callStmt.getClob(1);

				char clobVal[] = new char[(int) reviewContent.length()];
				Reader r = reviewContent.getCharacterStream();
				r.read(clobVal);
				StringWriter sw = new StringWriter();
				sw.write(clobVal);

				DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
				String strDate_ = dateFormat_.format(date);
				String createfolder = System.getenv("TRANSACT_PATH")/*System.getProperty("user.dir") + "\\"*/ + "\\" + strDate_ + "_SESSID_" + sessid_;

				File file = new File(createfolder);
				if (!file.exists()) {
					if (file.mkdir()) {
						System.out.println("Directory is created!");
					} else {
						System.out.println("Failed to create directory!");
					}
				}
				PrintWriter writer = new PrintWriter(createfolder + "\\" + strDate + "_CLOB_.txt");
				writer.write(sw.toString());
				writer.close();
				r.close();

				callStmt.close();
				ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_CLOB_.txt");
				pb.start();

				sess_id = null;
				full_pach = null;

				chk.setDisable(true);
				chk.setSelected(false);
				calc.setDisable(true);

				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Расчет прошел успешно!");
				alert.showAndWait();
				callStmt.close();
				conn.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Все плохо");
				alert.showAndWait();
			}
		} catch (SQLException | IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void chk_p(ActionEvent event) {
		if (!chk.isSelected()) {
			chk.setSelected(true);
		}
	}

	@FXML
	void view(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("View.fxml"));
			/*
			 * if "fx:controller" is not set in fxml
			 * fxmlLoader.setController(NewWindowController);
			 */
			Scene scene = new Scene(fxmlLoader.load());
			Stage stage = new Stage();
			stage.setTitle("New Window");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

}
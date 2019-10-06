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

import org.mozilla.universalchardet.UniversalDetector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sample.model.Connect;
import javafx.stage.Stage;

public class TransactLoad {
	/*
	 * final static String driverClass = "oracle.jdbc.OracleDriver"; final
	 * static String connectionURL = "jdbc:oracle:thin:@oradb-prm:1521/odb";
	 * final static String userID = "xxi"; final static String userPassword =
	 * "ver8i";
	 */
	static String sql = "{ ? = call Z_SB_CREATE_TR.load_pack(?,?)}";

	static String sql_calc = "{ ? = call z_sb_transact_calc.make(?)}";

	static String sessid_ = null;

	@SuppressWarnings("resource")
	
	private static String readFile(String fileName) throws FileNotFoundException, IOException {
		/*
		 * FileInputStream fis = null; InputStreamReader isr = null; String
		 * encoding; fis = new FileInputStream(fileName); isr = new
		 * InputStreamReader(fis); // the name of the character encoding
		 * returned encoding = isr.getEncoding();
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
	private TextArea result;

	@FXML
	private Button import_;

	@FXML
	private TextField textbox;

	@FXML
	private CheckBox chk;

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

	@FXML
	void Choose(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("������� ����");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Comma separated", "*.csv"),
				new ExtensionFilter("Text Files", "*.txt"));
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			textbox.setText(file.getParent() + "::_" + file.getName());
		}
	}

	@FXML
	void Load_Transact(ActionEvent event) {
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
					alert.setTitle("��������");
					alert.setHeaderText(null);
					alert.setContentText("������� ������, ����� ��������� ���� � ���������.");
					alert.showAndWait();

					Statement sqlStatement = conn.createStatement();
					String readRecordSQL = "SELECT * FROM Z_SB_LOG_DBT WHERE sess_id = " + part2 + "";
					ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

					// String[] path =
					// textbox.getText().toString().split("::_");
					// String path1 = path[0].trim();

					DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
					String strDate_ = dateFormat_.format(date);				
					String createfolder = System.getProperty("user.dir") + "\\" + strDate_ + "_SESSID_" + sessid_;
					
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
						writer.write(rowid + "____" + myResultSet.getString("recdate") + "____"
								+ myResultSet.getString("paydate") + "____" + myResultSet.getString("trid") + "____"
								+ myResultSet.getString("desc_") + "____" + myResultSet.getString("sess_id") + "\r\n");
						rowid++;
					}
					writer.close();
					ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_ERROR.txt");
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
					alert.setTitle("��������");
					alert.setHeaderText(null);
					alert.setContentText("�������� ������ �������. ������ ������� � �������");
					alert.showAndWait();
				}
				callStmt.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("�������� ������� ���� ��� ��������");
				alert.showAndWait();
			}
		} catch (SQLException |

				IOException e) {
			// TODO Auto-generated catch block
			result.setText(e.getMessage());
		}
	}

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

	// ������ � ��������� ���� ��������� ��������
	void Protocol(String sessid) {
		try {
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
			String readRecordSQL = "SELECT * FROM Z_SB_TRANSACT_DBT WHERE sess_id = " + sessid + "";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

			Integer rowid = 1;

			DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
			String strDate_ = dateFormat_.format(date);				
			String createfolder = System.getProperty("user.dir") + "\\" + strDate_ + "_SESSID_" + sessid_;
			
			File file = new File(createfolder);
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}

			String path_file = createfolder + "\\" + strDate + "_PROTOCOL.txt";
			PrintWriter writer = new PrintWriter(path_file);

			boolean chk = false;
			while (myResultSet.next()) {
				if (chk == false) {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new FileInputStream(textbox.getText().replace("::_", "\\")),
									getFileCharset(textbox.getText().replace("::_", "\\"))));
					// System.out.println(getFileCharset(textbox.getText().replace("::_",
					// "\\")));
					String line = null;
					int rowcount = 0;
					writer.write("�������� �������� �����.\r\n");
					while ((line = bufferedReader.readLine()) != null) {
						rowcount = rowcount + 1;
						// System.out.println(line);
						writer.write("����� ������: " + rowcount + ";" + line + "\r\n");
					}
					bufferedReader.close();
					writer.write("\r\n�������� �������� ����������.\r\n");
				}
				writer.write("����� ������: " + rowid + ";" + myResultSet.getString("recdate") + ";"
						+ myResultSet.getString("paydate") + ";" + myResultSet.getString("department") + ";"
						+ myResultSet.getString("trid") + ";" + myResultSet.getString("number_") + ";"
						+ myResultSet.getString("paymentnumber") + ";" + myResultSet.getString("receiptnumber") + ";"
						+ myResultSet.getString("idterm") + ";" + myResultSet.getString("receiver") + ";"
						+ myResultSet.getString("service") + ";" + myResultSet.getString("account") + ";"
						+ myResultSet.getString("paymentsum") + ";" + myResultSet.getString("insum") + ";"
						+ myResultSet.getString("receiversum") + ";" + myResultSet.getString("outsum") + ";"
						+ myResultSet.getString("feesum") + ";" + myResultSet.getString("startdeal") + ";"
						+ myResultSet.getString("deal") + ";" + myResultSet.getString("fromdeal") + ";"
						+ myResultSet.getString("isdeal") + ";" + myResultSet.getString("dealaccount") + ";"
						+ myResultSet.getString("datetimepayment") + ";" + myResultSet.getString("datetimesrv") + ";"
						+ myResultSet.getString("datetimestatus") + ";" + myResultSet.getString("status") + ";"
						+ myResultSet.getString("currency") + ";" + myResultSet.getString("badpaymentnumber") + ";"
						+ myResultSet.getString("fio") + ";" + myResultSet.getString("paymentdata") + ";"
						+ myResultSet.getString("compositedata").replace("\r", "") + ";"
						+ myResultSet.getString("statusabs") + ";" + myResultSet.getString("sess_id") + ";" + "\r\n");
				rowid++;
				chk = true;
			}
			chk = false;
			writer.close();
			ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_PROTOCOL.txt");
			pb.start();
			myResultSet.close();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			result.setText(e.getMessage());
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
				String createfolder = System.getProperty("user.dir") + "\\" + strDate_ + "_SESSID_" + sessid_;
				
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
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("������ ������ �������!");
				alert.showAndWait();

			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("��� �����");
				alert.showAndWait();
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			result.setText(e.getMessage());
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
			// TODO Auto-generated catch block
			result.setText(e.getMessage());
		}
	}

	@FXML
	void initialize() {
		assert browse != null : "fx:id=\"browse\" was not injected: check your FXML file 'TransactLoad.fxml'.";
		assert import_ != null : "fx:id=\"import_\" was not injected: check your FXML file 'TransactLoad.fxml'.";
		assert textbox != null : "fx:id=\"textbox\" was not injected: check your FXML file 'TransactLoad.fxml'.";
		assert calc != null : "fx:id=\"calc\" was not injected: check your FXML file 'TransactLoad.fxml'.";
		assert open_new != null : "fx:id=\"open_new\" was not injected: check your FXML file 'TransactLoad.fxml'.";
	}
}
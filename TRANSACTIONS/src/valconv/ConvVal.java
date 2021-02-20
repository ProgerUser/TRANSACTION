package valconv;

import java.awt.SplashScreen;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import app.Main;
import app.model.Connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sbalert.Msg;

public class ConvVal {

	public ConvVal() {
		Main.logger = Logger.getLogger(getClass());
	}

	static String fl20;
	static String fl32a_date;
	static String fl32a_cur;
	static String fl32a_sum;
	static String fl58a;
	static String fl58a_detail;
	static String fl53b;
	static String fl72;
	
	@FXML
	private TextField FILENAME;

	@FXML
	private TextField f20;

	@FXML
	private TextField f21;

	@FXML
	private TextField f32a_date;

	@FXML
	private TextField f32a_cur;

	@FXML
	private TextField f32a_sum;

	@FXML
	private TextField f58a;

	@FXML
	private TextArea f58a_detail;

	@FXML
	private TextField f53b;

	@FXML
	private TextArea f72;

	@FXML
	void cencel(ActionEvent event) {
		onclose();
	}

	@SuppressWarnings("resource")
	@FXML
	void save(ActionEvent event) {
		try {
			// Проверка полей
			if (f20.getText() == null || f20.getText().equals("")) {
				Msg.Message("Поле f20 не может быть пустым!");
				return;
			}
			if (f21.getText() == null || f21.getText().equals("")) {
				Msg.Message("Поле f21 не может быть пустым!");
				return;
			}
			if (f32a_date.getText() == null || f32a_date.getText().equals("")) {
				Msg.Message("Поле f32a_date не может быть пустым!");
				return;
			}
			if (f32a_cur.getText() == null || f32a_cur.getText().equals("")) {
				Msg.Message("Поле f32a_cur не может быть пустым!");
				return;
			}
			if (f32a_sum.getText() == null || f32a_sum.getText().equals("")) {
				Msg.Message("Поле f32a_sum не может быть пустым!");
				return;
			}
			if (f53b.getText() == null || f53b.getText().equals("")) {
				Msg.Message("Поле f53b не может быть пустым!");
				return;
			}
			if (f58a.getText() == null || f58a.getText().equals("")) {
				Msg.Message("Поле f58a не может быть пустым!");
				return;
			}
			if (f58a_detail.getText() == null || f58a_detail.getText().equals("")) {
				Msg.Message("Поле f58a_detail не может быть пустым!");
				return;
			}
			if (f72.getText() == null || f72.getText().equals("")) {
				Msg.Message("Поле f72 не может быть пустым!");
				return;
			}
			if (FILENAME.getText() == null || FILENAME.getText().equals("")) {
				Msg.Message("Поле FILENAME не может быть пустым!");
				return;
			}
			// Формирование файла
//1
			//Создаем 227! пробелов, хз для чего, но "Инверсия" делает
			String space = "";
			for (int i = 1; i <= 227; i++) {
				space = space + " ";
			}
			
			String txt = "{1:F01SBRARUMMAXXX0000000000}{2:I202VTBRRUMMXXXXN}{3:{113:RUR6}}{4:\r\n"
					+ ":20:" + fl20 + "\r\n"
					+ ":21:NONREF\r\n"
					+ ":32A:" + fl32a_date + fl32a_cur + fl32a_sum + "\r\n"
					+ ":53B:" + fl53b + "\r\n"
					+ ":58A:" + fl58a + "\r\n" + fl58a_detail + "\r\n"
					+ ":72:" + fl72
					+ "-}" + space;

//			OutputStream os = new FileOutputStream(System.getenv("SWIFT_OUTLOCAL") + "/" + FILENAME.getText() + ".swt");
//			PrintWriter out = new PrintWriter(new OutputStreamWriter(os, "CP1251"));
//			out.write(txt);
//			out.print(txt);
//			out.close();
//			os.close();

//1.1
			
//			FileUtils.writeByteArrayToFile(
//					new File(System.getenv("SWIFT_OUTLOCAL") + "/" + FILENAME.getText() + ".swt"),
//					txt.getBytes(StandardCharsets.UTF_8));
			
//1.2
			File swtFile = new File(System.getenv("SWIFT_OUTLOCAL") + "/" + FILENAME.getText() + ".swt");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(swtFile), "Cp1252"));
			writer.write(txt);
			writer.close();
			
//			List<String> lines = new ArrayList<String>();
//			lines.add("{1:F01SBRARUMMAXXX0000000000}{2:I202VTBRRUMMXXXXN}{3:{113:RUR6}}{4:");
//			lines.add(":21:NONREF");
//			lines.add(":32A:" + f32a_date.getText() + f32a_cur.getText() + f32a_sum.getText());
//			lines.add(":53B:" + f53b.getText());
//			lines.add(":58A:" + f58a.getText());
//			lines.add(f58a_detail.getText());
//			lines.add(":72:" + f72.getText() + "-}");
//
//			Files.write(Paths.get(System.getenv("SWIFT_OUTLOCAL") + "/" + FILENAME.getText() + ".swt"), lines,
//					StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//3
//			FileOutputStream outputStream = new FileOutputStream("MyFile.txt");
//			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "CP1252");
//			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//
//			bufferedWriter.write("{1:F01SBRARUMMAXXX0000000000}{2:I202VTBRRUMMXXXXN}{3:{113:RUR6}}{4:");
//			bufferedWriter.newLine();
//			bufferedWriter.write(":21:NONREF");
//			bufferedWriter.newLine();
//			bufferedWriter.write(":32A:" + f32a_date.getText() + f32a_cur.getText() + f32a_sum.getText());
//			bufferedWriter.newLine();
//			bufferedWriter.write(":53B:" + f53b.getText());
//			bufferedWriter.newLine();
//			bufferedWriter.write(":58A:" + f58a.getText());
//			bufferedWriter.newLine();
//			bufferedWriter.write(f58a_detail.getText());
//			bufferedWriter.newLine();
//			bufferedWriter.write(":72:" + f72.getText());
//			bufferedWriter.newLine();
//			bufferedWriter.write("-}");
//			bufferedWriter.close();
//			outputStream.close();
//			outputStreamWriter.close();
			
			//Сохраним ссылки для возврата
			{
				PreparedStatement prp = conn.prepareStatement("insert into VTB_MT202_CONV (REF,TRN_NUM,TRN_ANUM,FILE_T) values (?,?,?,?)");
				prp.setString(1, fl20);
				prp.setInt(2, Integer.valueOf(Connect.trnnum));
				prp.setInt(3, Integer.valueOf(Connect.trnanum));
				prp.setString(4, txt);
				prp.executeUpdate();
				prp.close();
				conn.commit();
			}
			
			Msg.Message("Файл \"" + FILENAME.getText() + ".swt\"" + " успешно создан в папке "
					+ System.getenv("SWIFT_OUTLOCAL"));
			onclose();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * При закрытии
	 */
	void onclose() {
		Stage stage = (Stage) f20.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	/**
	 * Сессия
	 */
	private Connection conn;

	/**
	 * Открыть сессию
	 */
	private void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program", "CONS_VAL");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Отключить сессию
	 */
	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	private void initialize() {
		try {
			
			final SplashScreen splash = SplashScreen.getSplashScreen();
			if (splash != null) {
				splash.close();
			}
			
			dbConnect();
			// Инициализация полей
			{
				CallableStatement clstmt = conn.prepareCall("{ call SBRA_VTB_SWIF.CONV_VAL(?,?,?,?,?,?,?,?,?,?,?,?) }");
				clstmt.setString(1, Connect.trnnum);
				clstmt.setString(2, Connect.trnanum);
				clstmt.registerOutParameter(3, Types.VARCHAR);
				clstmt.registerOutParameter(4, Types.VARCHAR);
				clstmt.registerOutParameter(5, Types.VARCHAR);
				clstmt.registerOutParameter(6, Types.VARCHAR);
				clstmt.registerOutParameter(7, Types.VARCHAR);
				clstmt.registerOutParameter(8, Types.VARCHAR);
				clstmt.registerOutParameter(9, Types.VARCHAR);
				clstmt.registerOutParameter(10, Types.VARCHAR);
				clstmt.registerOutParameter(11, Types.VARCHAR);
				clstmt.registerOutParameter(12, Types.VARCHAR);
				clstmt.execute();
				if (clstmt.getString(3) != null) {
					Msg.Message(clstmt.getString(3));
				} else {
					fl20=clstmt.getString(4);f20.setText(fl20);
					f21.setText("NONREF");
					fl32a_date = clstmt.getString(5);f32a_date.setText(fl32a_date);
					fl32a_cur = clstmt.getString(6);f32a_cur.setText(fl32a_cur);
					fl32a_sum = clstmt.getString(7);f32a_sum.setText(fl32a_sum);
					fl58a = clstmt.getString(8);f58a.setText(fl58a);
					fl58a_detail = clstmt.getString(9);f58a_detail.setText(fl58a_detail);
					fl53b = clstmt.getString(10);f53b.setText(fl53b);
					fl72 = clstmt.getString(11);f72.setText(fl72);
					FILENAME.setText(clstmt.getString(12));
				}
				clstmt.close();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}
}

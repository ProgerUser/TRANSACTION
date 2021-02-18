package valconv;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
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

	@FXML
	void sava(ActionEvent event) {
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
			//Создаем 227! пробелов, ? для чего но "Инверсия" делает
			String space = "";
			for (int i = 1; i <= 227; i++) {
				space = space + " ";
			}
			
			String txt="{1:F01SBRARUMMAXXX0000000000}{2:I202VTBRRUMMXXXXN}{3:{113:RUR6}}{4:\r\n"
					+":20:"+f20.getText()+"\r\n"
					+":21:NONREF\r\n"
					+":32A:"+f32a_date.getText()+f32a_cur.getText()+f32a_sum.getText()+"\r\n"
					+":53B:"+f53b.getText()+"\r\n"
					+":58A:"+f58a.getText()+"\r\n"+f58a_detail.getText()+"\r\n"
					+":72:"+f72.getText()
					+"-}"+space;

			OutputStream os = new FileOutputStream(System.getenv("SWIFT_OUTLOCAL") + "/" + FILENAME.getText() + ".swt");
			PrintWriter out = new PrintWriter(new OutputStreamWriter(os, "windows-1251"));
			out.print(txt);
			out.close();
			os.close();
//2
			
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
					f20.setText(clstmt.getString(4));
					f21.setText("NONREF");
					f32a_date.setText(clstmt.getString(5));
					f32a_cur.setText(clstmt.getString(6));
					f32a_sum.setText(clstmt.getString(7));
					f58a.setText(clstmt.getString(8));
					f58a_detail.setText(clstmt.getString(9));
					f53b.setText(clstmt.getString(10));
					f72.setText(clstmt.getString(11));
					FILENAME.setText(clstmt.getString(12));
				}
			}

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}
}

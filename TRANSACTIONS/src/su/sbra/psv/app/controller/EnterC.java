package su.sbra.psv.app.controller;

import java.awt.SplashScreen;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import su.sbra.psv.app.audit.view.FxUtilTest;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class EnterC {

	@FXML
	private ComboBox<String> login;
	@FXML
	private PasswordField pass;
	@FXML
	private ComboBox<String> conurl;

	final static String driverClass = "oracle.jdbc.OracleDriver";

	Connection conn = null;
	private Executor exec;
	@FXML
	private VBox Root;
	@FXML
	private ProgressIndicator PB;

	/**
	 * Разные проверки перед входом
	 */
	void Check_Enter() {
		// Выполнить проверку соединения с базой
		try {
			// ----------------------------------
			PB.setVisible(true);
			Root.setDisable(true);
			Task<Object> task = new Task<Object>() {
				@Override
				public Object call() throws Exception {
					try {
						// --------------------------------------
						// __________________Проверки____________
						DBUtil.dbConnect();
						DbUtil.Db_Connect();

						conn = DbUtil.conn;
						if (conn != null) {
							String sql = "SELECT count(*) cnt FROM usr where usr.DUSRFIRE is null and CUSRLOGNAME = ?";
							PreparedStatement sqlStatement = conn.prepareStatement(sql);
							sqlStatement.setString(1, Connect.userID_);
							ResultSet myResultSet = sqlStatement.executeQuery();
							if (!myResultSet.next()) {
								Msg.Message("Ошибка ввода логина или пароля");
							} else {
								if (myResultSet.getLong("cnt") > 0) {
									PreparedStatement stsmt = conn.prepareStatement("select null from usr "
											+ "where upper(usr.CUSRLOGNAME) = ? " + "and MUST_CHANGE_PASSWORD = 'Y'");
									stsmt.setString(1, Connect.userID_.toUpperCase());
									ResultSet rs = stsmt.executeQuery();
									if (rs.next()) {
										Msg.Message("Необходимо изменить пароль!");
									} else {
										Platform.runLater(() -> {
											Main.InitAppRootLayout();
											Main.ShFirstView();
										});
									}
									stsmt.close();
									rs.close();
								} else {
									Msg.Message("Пользователь заблокирован!");
								}
							}
							myResultSet.close();
							sqlStatement.close();
						}
					} catch (Exception e) {
						ShowMes(ExceptionUtils.getStackTrace(e));
					}
					// ----------------------------------
					return null;
				}
			};
			task.setOnFailed(e -> ShowMes(ExceptionUtils.getStackTrace(task.getException())));
			task.setOnSucceeded(e -> {
				PB.setVisible(false);
				Root.setDisable(false);
			});
			exec.execute(task);
			// ---------------
		} catch (Exception e) {
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Error message in new thread
	 * 
	 * @param error
	 */
	void ShowMes(String error) {
		Platform.runLater(() -> {
			Msg.Message(error);
		});
	}

	@FXML
	void Enter(ActionEvent event) {
		try {
			String conurls = conurl.getSelectionModel().getSelectedItem();
			String logins = login.getSelectionModel().getSelectedItem();
			String passs = pass.getText();

//			System.out.println("conurls=" + conurls);
//			System.out.println("logins=" + logins);
//			System.out.println("passs=" + passs);

			if (conurls != null & logins != null & !passs.equals("")) {
				Connect.connectionURL_ = conurl.getValue().toString();
				Connect.userID_ = login.getValue().toString();
				Connect.userPassword_ = pass.getText();
				Check_Enter();
			}
		} catch (Exception e) {
			Main.logger.error(ExceptionUtils.getStackTrace(e));
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * При закрытии
	 */
	void OnClose() {
		Stage stage = (Stage) pass.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	/**
	 * Отмена
	 * 
	 * @param event
	 */
	@FXML
	void Cencel(ActionEvent event) {
		try {
			OnClose();
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	@FXML
	void KeyEnter(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			try {
				Enter(null);
			} catch (Exception e) {
				Main.logger.error(ExceptionUtils.getStackTrace(e));
				Msg.Message(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	@FXML
	private void initialize() {
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		// закрыть splash картинку
		final SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash != null) {
			splash.close();
		}
		// ----------------------
		try (InputStream input = new FileInputStream(System.getenv("TRANSACT_PATH") + "connect.properties")) {

			Properties prop = new Properties();
			prop.load(input);

			ObservableList<String> ItemsLogins = FXCollections.observableArrayList();
			ObservableList<String> ItemsConurl = FXCollections.observableArrayList();

			@SuppressWarnings("unchecked")
			Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
			while (enums.hasMoreElements()) {
				String key = enums.nextElement();
				String value = prop.getProperty(key);
				if (key.contains("user")) {
					ItemsLogins.add(value);
				} else if (key.contains("url")) {
					ItemsConurl.add(value);
				}
			}

			// FilteredList<String> filteredItems = new FilteredList<String>(items);
			// FilteredList<String> filteredItems_2 = new FilteredList<String>(items_2);

			// login.getEditor().textProperty().addListener(new InputFilter(login,
			// filteredItems, false));
			login.setItems(ItemsLogins);
			// conurl.getEditor().textProperty().addListener(new InputFilter(conurl,
			// filteredItems_2, false));
			conurl.setItems(ItemsConurl);

			// Filtered Combo
			FxUtilTest.getComboBoxValue(login);
			FxUtilTest.autoCompleteComboBoxPlus(login,
					(typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
			// 2
			FxUtilTest.getComboBoxValue(conurl);
			FxUtilTest.autoCompleteComboBoxPlus(conurl,
					(typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()));
			// --------------------------------------------------------------------------------------------------
			conurl.getSelectionModel().select(0);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
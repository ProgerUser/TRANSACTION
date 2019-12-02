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
import java.io.UnsupportedEncodingException;
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
	 * final static String driverClass = "oracle.jdbc.OracleDriver"; final static
	 * String connectionURL = "jdbc:oracle:thin:@oradb-prm:1521/odb"; final static
	 * String userID = "xxi"; final static String userPassword = "xxx";
	 */
	static String sql = "{ ? = call Z_SB_CREATE_TR.load_pack(?,?)}";

	static String sql_calc = "{ ? = call z_sb_transact_calc.make(?)}";

	static String sessid_ = null;

	@SuppressWarnings("resource")

	private static String readFile(String fileName) {
		try {
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
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return null;
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
		fileChooser.setTitle("Выбрать файл");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Comma separated", "*.csv"),
				new ExtensionFilter("Text Files", "*.txt"));
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
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

				callStmt = conn.prepareCall("\r\n" + 
						"declare\r\n" + 
						"  /*Функция из bool в char*/\r\n" + 
						"  FUNCTION boolean_to_char(status IN BOOLEAN) RETURN VARCHAR2 IS\r\n" + 
						"  BEGIN\r\n" + 
						"    RETURN CASE status WHEN TRUE THEN 'TRUE' WHEN FALSE THEN 'FALSE' ELSE 'NULL' END;\r\n" + 
						"  END;\r\n" + 
						"  /*Функция из bool в char*/\r\n" + 
						"\r\n" + 
						"  function dubl_data(paymentnumber_ varchar2) return boolean is\r\n" + 
						"    chek_ number := 0;\r\n" + 
						"  begin\r\n" + 
						"    begin\r\n" + 
						"      select count(t.paymentnumber)\r\n" + 
						"        into chek_\r\n" + 
						"        from Z_SB_TRANSACT_DBT t\r\n" + 
						"       where t.paymentnumber = paymentnumber_;\r\n" + 
						"    \r\n" + 
						"      if chek_ <> 0 then\r\n" + 
						"        return true;\r\n" + 
						"      end if;\r\n" + 
						"    exception\r\n" + 
						"      when others then\r\n" + 
						"        null;\r\n" + 
						"    end;\r\n" + 
						"    return false;\r\n" + 
						"  end dubl_data;\r\n" + 
						"  /*Процедура записи лога*/\r\n" + 
						"  PROCEDURE writelog(datetimepayment_ VARCHAR2,\r\n" + 
						"                     trid_            VARCHAR2,\r\n" + 
						"                     descripion       VARCHAR2,\r\n" + 
						"                     sess_id_         NUMBER) IS\r\n" + 
						"  BEGIN\r\n" + 
						"    INSERT INTO z_sb_log_dbt\r\n" + 
						"      (recdate, paydate, trid, desc_, sess_id)\r\n" + 
						"    VALUES\r\n" + 
						"      (SYSTIMESTAMP,\r\n" + 
						"       TO_DATE(datetimepayment_, 'DD-MM-RRRR HH24:MI:SS') /*MakeDate(DateTimePayment_), 'DD-MM-YYYY')*/,\r\n" + 
						"       trid_,\r\n" + 
						"       descripion,\r\n" + 
						"       sess_id_);\r\n" + 
						"    COMMIT;\r\n" + 
						"  END;\r\n" + 
						"  /*Процедура записи лога*/\r\n" + 
						"\r\n" + 
						"  /*Процедура записи транзакции*/\r\n" + 
						"  PROCEDURE writedata(paymentnumber_   VARCHAR2,\r\n" + 
						"                      trid             VARCHAR2,\r\n" + 
						"                      datetimepayment  VARCHAR2,\r\n" + 
						"                      compositedata    VARCHAR2,\r\n" + 
						"                      makepaymentdata  VARCHAR2,\r\n" + 
						"                      fio              VARCHAR2,\r\n" + 
						"                      badpaymentnumber VARCHAR2,\r\n" + 
						"                      currency         VARCHAR2,\r\n" + 
						"                      stat             VARCHAR2,\r\n" + 
						"                      datetimestatus   VARCHAR2,\r\n" + 
						"                      datetimesrv      VARCHAR2,\r\n" + 
						"                      dealaccount      VARCHAR2,\r\n" + 
						"                      isdeal           VARCHAR2,\r\n" + 
						"                      fromdeal         VARCHAR2,\r\n" + 
						"                      deal             VARCHAR2,\r\n" + 
						"                      startdeal        VARCHAR2,\r\n" + 
						"                      feesum           VARCHAR2,\r\n" + 
						"                      outsum           VARCHAR2,\r\n" + 
						"                      receiversum      VARCHAR2,\r\n" + 
						"                      insum            VARCHAR2,\r\n" + 
						"                      paymentsum       VARCHAR2,\r\n" + 
						"                      service          VARCHAR2,\r\n" + 
						"                      receiver         VARCHAR2,\r\n" + 
						"                      idterm           VARCHAR2,\r\n" + 
						"                      receiptnumber    VARCHAR2,\r\n" + 
						"                      number_          VARCHAR2,\r\n" + 
						"                      account          VARCHAR2,\r\n" + 
						"                      sess_id_         NUMBER) IS\r\n" + 
						"    chk BOOLEAN;\r\n" + 
						"  BEGIN\r\n" + 
						"    chk := FALSE;\r\n" + 
						"  \r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"    FOR tr IN (SELECT tr.STATUSABS status, tr.recdate recdate\r\n" + 
						"                 FROM z_sb_transact_dbt tr\r\n" + 
						"                WHERE /*tr.paydate =\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') \\*MakeDate(DateTimePayment)*\\\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    AND*/\r\n" + 
						"                tr.paymentnumber = paymentnumber_) LOOP\r\n" + 
						"      IF tr.status = 1 THEN\r\n" + 
						"        writelog(datetimepayment,\r\n" + 
						"                 trid,\r\n" + 
						"                 'Внимание! Транзакция PaymentNumber = ' || paymentnumber_ ||\r\n" + 
						"                 ' уже загружена в АБС ' || tr.recdate ||\r\n" + 
						"                 ' и помечена как удачная.',\r\n" + 
						"                 sess_id_);\r\n" + 
						"        chk := TRUE;\r\n" + 
						"      ELSE\r\n" + 
						"        writelog(datetimepayment,\r\n" + 
						"                 trid,\r\n" + 
						"                 'Внимание! Транзакция PaymentNumber = ' || paymentnumber_ ||\r\n" + 
						"                 ' уже загружена в АБС ' || tr.recdate ||\r\n" + 
						"                 ' и помечена как аварийная.',\r\n" + 
						"                 sess_id_);\r\n" + 
						"      END IF;\r\n" + 
						"    \r\n" + 
						"      EXIT WHEN chk = TRUE;\r\n" + 
						"    END LOOP;\r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"  \r\n" + 
						"    IF chk = FALSE THEN\r\n" + 
						"      INSERT INTO z_sb_transact_dbt\r\n" + 
						"        (recdate,\r\n" + 
						"         paydate,\r\n" + 
						"         department,\r\n" + 
						"         trid,\r\n" + 
						"         number_,\r\n" + 
						"         paymentnumber,\r\n" + 
						"         receiptnumber,\r\n" + 
						"         idterm,\r\n" + 
						"         receiver,\r\n" + 
						"         service,\r\n" + 
						"         account,\r\n" + 
						"         paymentsum,\r\n" + 
						"         insum,\r\n" + 
						"         receiversum,\r\n" + 
						"         outsum,\r\n" + 
						"         feesum,\r\n" + 
						"         startdeal,\r\n" + 
						"         deal,\r\n" + 
						"         fromdeal,\r\n" + 
						"         isdeal,\r\n" + 
						"         dealaccount,\r\n" + 
						"         datetimepayment,\r\n" + 
						"         datetimesrv,\r\n" + 
						"         datetimestatus,\r\n" + 
						"         status,\r\n" + 
						"         currency,\r\n" + 
						"         badpaymentnumber,\r\n" + 
						"         fio,\r\n" + 
						"         paymentdata,\r\n" + 
						"         compositedata,\r\n" + 
						"         statusabs,\r\n" + 
						"         sess_id)\r\n" + 
						"      VALUES\r\n" + 
						"        (SYSDATE,\r\n" + 
						"         TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') /*MakeDate(DateTimePayment), 'DD-MM-YYYY'*/,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(makedprt(idterm), '.', ','))),\r\n" + 
						"         trid,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(number_, '.', ','))),\r\n" + 
						"         paymentnumber_,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(receiptnumber, '.', ','))),\r\n" + 
						"         idterm,\r\n" + 
						"         receiver,\r\n" + 
						"         service,\r\n" + 
						"         account,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(paymentsum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(insum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(receiversum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(outsum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(feesum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(startdeal, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(deal, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(fromdeal, '.', ','))),\r\n" + 
						"         isdeal,\r\n" + 
						"         REPLACE(dealaccount, '|', ''),\r\n" + 
						"         datetimepayment,\r\n" + 
						"         datetimesrv,\r\n" + 
						"         datetimestatus,\r\n" + 
						"         makestat(stat),\r\n" + 
						"         currency,\r\n" + 
						"         badpaymentnumber,\r\n" + 
						"         UPPER(fio),\r\n" + 
						"         makepaymentdata,\r\n" + 
						"         compositedata,\r\n" + 
						"         0,\r\n" + 
						"         sess_id_);\r\n" + 
						"      COMMIT;\r\n" + 
						"    END IF;\r\n" + 
						"  END;\r\n" + 
						"  /*Процедура записи транзакции*/\r\n" + 
						"\r\n" + 
						"  /*Процедура записи транзакции*/\r\n" + 
						"  PROCEDURE WriteDataToBad(paymentnumber_   VARCHAR2,\r\n" + 
						"                           trid             VARCHAR2,\r\n" + 
						"                           datetimepayment  VARCHAR2,\r\n" + 
						"                           compositedata    VARCHAR2,\r\n" + 
						"                           makepaymentdata  VARCHAR2,\r\n" + 
						"                           fio              VARCHAR2,\r\n" + 
						"                           badpaymentnumber VARCHAR2,\r\n" + 
						"                           currency         VARCHAR2,\r\n" + 
						"                           stat             VARCHAR2,\r\n" + 
						"                           datetimestatus   VARCHAR2,\r\n" + 
						"                           datetimesrv      VARCHAR2,\r\n" + 
						"                           dealaccount      VARCHAR2,\r\n" + 
						"                           isdeal           VARCHAR2,\r\n" + 
						"                           fromdeal         VARCHAR2,\r\n" + 
						"                           deal             VARCHAR2,\r\n" + 
						"                           startdeal        VARCHAR2,\r\n" + 
						"                           feesum           VARCHAR2,\r\n" + 
						"                           outsum           VARCHAR2,\r\n" + 
						"                           receiversum      VARCHAR2,\r\n" + 
						"                           insum            VARCHAR2,\r\n" + 
						"                           paymentsum       VARCHAR2,\r\n" + 
						"                           service          VARCHAR2,\r\n" + 
						"                           receiver         VARCHAR2,\r\n" + 
						"                           idterm           VARCHAR2,\r\n" + 
						"                           receiptnumber    VARCHAR2,\r\n" + 
						"                           number_          VARCHAR2,\r\n" + 
						"                           account          VARCHAR2,\r\n" + 
						"                           sess_id_         NUMBER) IS\r\n" + 
						"    chk BOOLEAN;\r\n" + 
						"  BEGIN\r\n" + 
						"    chk := FALSE;\r\n" + 
						"  \r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"    FOR tr IN (SELECT tr.status status, tr.recdate recdate\r\n" + 
						"                 FROM z_sb_transact_dbt tr\r\n" + 
						"                WHERE /*\\*tr.paydate =\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') \\*MakeDate(DateTimePayment)*\\\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    AND*/\r\n" + 
						"                tr.paymentnumber = paymentnumber_) LOOP\r\n" + 
						"      IF tr.status = 1 THEN\r\n" + 
						"        writelog(datetimepayment,\r\n" + 
						"                 trid,\r\n" + 
						"                 'Внимание! Транзакция PaymentNumber = ' || paymentnumber_ ||\r\n" + 
						"                 ' уже загружена в АБС ' || tr.recdate ||\r\n" + 
						"                 ' и помечена как удачная.',\r\n" + 
						"                 sess_id_);\r\n" + 
						"        chk := TRUE;\r\n" + 
						"      ELSE\r\n" + 
						"        writelog(datetimepayment,\r\n" + 
						"                 trid,\r\n" + 
						"                 'Внимание! Транзакция PaymentNumber = ' || paymentnumber_ ||\r\n" + 
						"                 ' уже загружена в АБС ' || tr.recdate ||\r\n" + 
						"                 ' и помечена как аварийная.',\r\n" + 
						"                 sess_id_);\r\n" + 
						"      END IF;\r\n" + 
						"    \r\n" + 
						"      EXIT WHEN chk = TRUE;\r\n" + 
						"    END LOOP;\r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"  \r\n" + 
						"    IF chk = FALSE THEN\r\n" + 
						"      INSERT INTO z_sb_transact_dbt\r\n" + 
						"        (recdate,\r\n" + 
						"         paydate,\r\n" + 
						"         department,\r\n" + 
						"         trid,\r\n" + 
						"         number_,\r\n" + 
						"         paymentnumber,\r\n" + 
						"         receiptnumber,\r\n" + 
						"         idterm,\r\n" + 
						"         receiver,\r\n" + 
						"         service,\r\n" + 
						"         account,\r\n" + 
						"         paymentsum,\r\n" + 
						"         insum,\r\n" + 
						"         receiversum,\r\n" + 
						"         outsum,\r\n" + 
						"         feesum,\r\n" + 
						"         startdeal,\r\n" + 
						"         deal,\r\n" + 
						"         fromdeal,\r\n" + 
						"         isdeal,\r\n" + 
						"         dealaccount,\r\n" + 
						"         datetimepayment,\r\n" + 
						"         datetimesrv,\r\n" + 
						"         datetimestatus,\r\n" + 
						"         status,\r\n" + 
						"         currency,\r\n" + 
						"         badpaymentnumber,\r\n" + 
						"         fio,\r\n" + 
						"         paymentdata,\r\n" + 
						"         compositedata,\r\n" + 
						"         statusabs,\r\n" + 
						"         sess_id)\r\n" + 
						"      VALUES\r\n" + 
						"        (SYSDATE,\r\n" + 
						"         TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') /*MakeDate(DateTimePayment), 'DD-MM-YYYY'*/,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(makedprt(idterm), '.', ','))),\r\n" + 
						"         trid,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(number_, '.', ','))),\r\n" + 
						"         paymentnumber_,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(receiptnumber, '.', ','))),\r\n" + 
						"         idterm,\r\n" + 
						"         receiver,\r\n" + 
						"         service,\r\n" + 
						"         account,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(paymentsum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(insum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(receiversum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(outsum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(feesum, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(startdeal, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(deal, '.', ','))),\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(fromdeal, '.', ','))),\r\n" + 
						"         isdeal,\r\n" + 
						"         REPLACE(dealaccount, '|', ''),\r\n" + 
						"         datetimepayment,\r\n" + 
						"         datetimesrv,\r\n" + 
						"         datetimestatus,\r\n" + 
						"         11,\r\n" + 
						"         currency,\r\n" + 
						"         badpaymentnumber,\r\n" + 
						"         UPPER(fio),\r\n" + 
						"         makepaymentdata,\r\n" + 
						"         compositedata,\r\n" + 
						"         0,\r\n" + 
						"         sess_id_);\r\n" + 
						"      COMMIT;\r\n" + 
						"    END IF;\r\n" + 
						"  END;\r\n" + 
						"  /*Процедура записи транзакции*/\r\n" + 
						"\r\n" + 
						"  /*Процедура записи сдач с транзакции*/\r\n" + 
						"  PROCEDURE writedeal(datetimepayment VARCHAR2,\r\n" + 
						"                      paymentnumber_  VARCHAR2,\r\n" + 
						"                      trid            VARCHAR2,\r\n" + 
						"                      startdeal       VARCHAR2,\r\n" + 
						"                      idterm          VARCHAR2,\r\n" + 
						"                      sess_id_        NUMBER,\r\n" + 
						"                      sts             number default null) IS\r\n" + 
						"    chek BOOLEAN;\r\n" + 
						"  BEGIN\r\n" + 
						"    chek := FALSE;\r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"    FOR dl IN (SELECT dl.recdate recdate\r\n" + 
						"                 FROM z_sb_termdeal_dbt dl\r\n" + 
						"                WHERE /*dl.dealstartdate =\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') \\*MakeDate(DateTimePayment)*\\\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    AND*/\r\n" + 
						"                dl.paymentnumber = paymentnumber_) LOOP\r\n" + 
						"      chek := TRUE;\r\n" + 
						"      writelog(datetimepayment,\r\n" + 
						"               trid,\r\n" + 
						"               'Сдача с номером платежа = ' || paymentnumber_ ||\r\n" + 
						"               ' уже загружена в АБС ' || dl.recdate,\r\n" + 
						"               sess_id_);\r\n" + 
						"    END LOOP;\r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"  \r\n" + 
						"    IF chek = FALSE THEN\r\n" + 
						"      INSERT INTO z_sb_termdeal_dbt\r\n" + 
						"        (recdate,\r\n" + 
						"         department,\r\n" + 
						"         paymentnumber,\r\n" + 
						"         dealstartdate,\r\n" + 
						"         sum_,\r\n" + 
						"         status,\r\n" + 
						"         sess_id)\r\n" + 
						"      VALUES\r\n" + 
						"        (SYSDATE,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(makedprt(idterm), '.', ','))),\r\n" + 
						"         paymentnumber_,\r\n" + 
						"         TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') /*MakeDate(DateTimePayment)*/,\r\n" + 
						"         TRIM(REPLACE(startdeal, '.', ',')),\r\n" + 
						"         decode(sts, null, 0, sts),\r\n" + 
						"         sess_id_);\r\n" + 
						"    \r\n" + 
						"      COMMIT;\r\n" + 
						"    END IF;\r\n" + 
						"  END;\r\n" + 
						"  /*Процедура записи сдач с транзакции*/\r\n" + 
						"\r\n" + 
						"  /*Процедура записи плохих транзакции*/\r\n" + 
						"  PROCEDURE writebad(datetimepayment  VARCHAR2,\r\n" + 
						"                     paymentnumber    VARCHAR2,\r\n" + 
						"                     trid             VARCHAR2,\r\n" + 
						"                     fromdeal         VARCHAR2,\r\n" + 
						"                     insum            VARCHAR2,\r\n" + 
						"                     badpaymentnumber VARCHAR2,\r\n" + 
						"                     idterm           VARCHAR2,\r\n" + 
						"                     sess_id_         NUMBER) IS\r\n" + 
						"    chek BOOLEAN;\r\n" + 
						"  BEGIN\r\n" + 
						"    chek := FALSE;\r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"    FOR bad IN (SELECT bad.recdate recdate, bad.badpaydate badpaydate\r\n" + 
						"                  FROM z_sb_badtr_dbt bad\r\n" + 
						"                 WHERE bad.badpaymentnumber = paymentnumber) LOOP\r\n" + 
						"      chek := TRUE;\r\n" + 
						"    \r\n" + 
						"      IF bad.badpaydate = TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') /*MakeDate(DateTimePayment)*/\r\n" + 
						"       THEN\r\n" + 
						"        writelog(datetimepayment,\r\n" + 
						"                 trid,\r\n" + 
						"                 'Аварийная транзакция с номером платежа = ' ||\r\n" + 
						"                 paymentnumber || ' уже загружена в АБС ' || bad.recdate,\r\n" + 
						"                 sess_id_);\r\n" + 
						"      ELSE\r\n" + 
						"        writelog(datetimepayment,\r\n" + 
						"                 trid,\r\n" + 
						"                 'Неудачная попытка проведения аварийной транзакции с номером платежа = ' ||\r\n" + 
						"                 paymentnumber || 'от ' || bad.recdate,\r\n" + 
						"                 sess_id_);\r\n" + 
						"      END IF;\r\n" + 
						"    END LOOP;\r\n" + 
						"    /*Разные проверки и Логирование*/\r\n" + 
						"  \r\n" + 
						"    IF chek = FALSE THEN\r\n" + 
						"      INSERT INTO z_sb_badtr_dbt\r\n" + 
						"        (recdate,\r\n" + 
						"         badpaydate,\r\n" + 
						"         department,\r\n" + 
						"         paymentnumber,\r\n" + 
						"         SUM,\r\n" + 
						"         status,\r\n" + 
						"         sess_id)\r\n" + 
						"      VALUES\r\n" + 
						"        (SYSDATE,\r\n" + 
						"         TO_DATE(datetimepayment, 'DD-MM-RRRR HH24:MI:SS') /*MakeDate(DateTimePayment)*/,\r\n" + 
						"         makedprt(idterm),\r\n" + 
						"         /*badpaymentnumber*/\r\n" + 
						"         PaymentNumber,\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(insum, '.', ','))) +\r\n" + 
						"         TO_NUMBER(TRIM(REPLACE(fromdeal, '.', ','))),\r\n" + 
						"         0,\r\n" + 
						"         sess_id_);\r\n" + 
						"    \r\n" + 
						"      COMMIT;\r\n" + 
						"    END IF;\r\n" + 
						"  END;\r\n" + 
						"  /*Процедура записи плохих транзакции*/\r\n" + 
						"\r\n" + 
						"  /*Функция формирования статуса транзакции*/\r\n" + 
						"  FUNCTION makestat(str IN VARCHAR2) RETURN VARCHAR IS\r\n" + 
						"    ret VARCHAR2(1000);\r\n" + 
						"  BEGIN\r\n" + 
						"    IF str = 'Завершен' THEN\r\n" + 
						"      ret := '1';\r\n" + 
						"    ELSIF str = 'Отвергнут' THEN\r\n" + 
						"      ret := '2';\r\n" + 
						"    ELSIF str = 'В обработке' THEN\r\n" + 
						"      ret := '3';\r\n" + 
						"    ELSIF str = 'Возврат' THEN\r\n" + 
						"      ret := '4';\r\n" + 
						"    ELSIF str = 'Не завершен' THEN\r\n" + 
						"      ret := '5';\r\n" + 
						"    ELSIF str = 'Новый' THEN\r\n" + 
						"      ret := '6';\r\n" + 
						"    ELSIF str = 'Отзыв' THEN\r\n" + 
						"      ret := '7';\r\n" + 
						"    ELSIF str = 'Отложен' THEN\r\n" + 
						"      ret := '8';\r\n" + 
						"    ELSIF str = 'Принят' THEN\r\n" + 
						"      ret := '9';\r\n" + 
						"    ELSIF str = 'Сторно' THEN\r\n" + 
						"      ret := '10';\r\n" + 
						"    ELSE\r\n" + 
						"      ret := '99';\r\n" + 
						"    END IF;\r\n" + 
						"    RETURN ret;\r\n" + 
						"  END makestat;\r\n" + 
						"  /*Функция формирования статуса транзакции*/\r\n" + 
						"\r\n" + 
						"  /*Функция формирования основания*/\r\n" + 
						"  FUNCTION makepaymentdata(service_ IN VARCHAR2,\r\n" + 
						"                           data1_   IN VARCHAR2,\r\n" + 
						"                           data2_   IN VARCHAR2,\r\n" + 
						"                           data3_   IN VARCHAR2,\r\n" + 
						"                           data4_   IN VARCHAR2,\r\n" + 
						"                           account_ IN VARCHAR2) RETURN VARCHAR2 IS\r\n" + 
						"    datastr VARCHAR2(32767);\r\n" + 
						"  BEGIN\r\n" + 
						"    IF service_ = 'Штрафы ГАИ МВД РА СБ' THEN\r\n" + 
						"      datastr := data3_ || ' а/м №' || data2_ || /* ', дата и время фиксации ' ||\r\n" + 
						"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 data1_ || */\r\n" + 
						"                 ', сумма ' || data4_;\r\n" + 
						"    ELSIF service_ = 'Оплата детского сада СБ' THEN\r\n" + 
						"      datastr := data3_ || ' ,оплата за ' || data1_;\r\n" + 
						"    ELSIF service_ = 'А-Мобайл СБ' THEN\r\n" + 
						"      datastr := account_;\r\n" + 
						"    ELSIF service_ = 'Налог Специальный СБ' THEN\r\n" + 
						"      datastr := data1_ || ' ,оплата за ' || data2_ || ' ' || data3_;\r\n" + 
						"    ELSIF service_ = 'Таможня онлайн СБ' THEN\r\n" + 
						"      datastr := 'Таможенные платежи - счет №' || data1_ || ' ,' || data2_;\r\n" + 
						"    END IF;\r\n" + 
						"    RETURN datastr;\r\n" + 
						"  END makepaymentdata;\r\n" + 
						"  /*Функция формирования основания*/\r\n" + 
						"\r\n" + 
						"  /*Функция формирования даты из DATETIMEPAYMENT, не используется*/\r\n" + 
						"  /*function MakeDate(str varchar2) return varchar is\r\n" + 
						"    date_ varchar2(32767);\r\n" + 
						"    dd    varchar2(32767);\r\n" + 
						"    mm    varchar2(32767);\r\n" + 
						"    yyyy  varchar2(32767);\r\n" + 
						"  begin\r\n" + 
						"    dd    := Substr(str, 1, 2);\r\n" + 
						"    mm    := Substr(str, 4, 2);\r\n" + 
						"    yyyy  := Substr(str, 7, 4);\r\n" + 
						"    date_ := dd || mm || yyyy;\r\n" + 
						"    return date_;\r\n" + 
						"  end MakeDate;*/\r\n" + 
						"  /*Функция формирования даты из DATETIMEPAYMENT, не используется*/\r\n" + 
						"\r\n" + 
						"  /*Функция отделения из наименования терминала оплаты*/\r\n" + 
						"  FUNCTION makedprt(str VARCHAR2) RETURN VARCHAR IS\r\n" + 
						"    ret VARCHAR2(32767);\r\n" + 
						"    dep VARCHAR2(32767);\r\n" + 
						"  BEGIN\r\n" + 
						"    BEGIN\r\n" + 
						"      SELECT LOWER(department)\r\n" + 
						"        INTO ret\r\n" + 
						"        FROM z_sb_terminal_dbt\r\n" + 
						"       WHERE LOWER(TRIM(name)) = LOWER(TRIM(str));\r\n" + 
						"    \r\n" + 
						"      RETURN ret;\r\n" + 
						"    EXCEPTION\r\n" + 
						"      WHEN OTHERS THEN\r\n" + 
						"        ret := 'not_found';\r\n" + 
						"        RETURN ret;\r\n" + 
						"    END;\r\n" + 
						"  END makedprt;\r\n" + 
						"  /*Функция отделения из наименования терминала оплаты\r\n" + 
						"  \r\n" + 
						"  /*Быстрый способ обработки CLOB не используется...\r\n" + 
						"  Чтения всего файла и вставка в z_sb_tr_from_txt_file*/\r\n" + 
						"  PROCEDURE dump_clob(CLOB CLOB) IS\r\n" + 
						"    offset NUMBER := 1;\r\n" + 
						"    amount NUMBER;\r\n" + 
						"    len    NUMBER := DBMS_LOB.getlength(CLOB);\r\n" + 
						"    buf    VARCHAR2(32767); --32767\r\n" + 
						"  \r\n" + 
						"    /*\r\n" + 
						"    offset NUMBER := 1;\r\n" + 
						"    amount NUMBER := 32767;\r\n" + 
						"    len    NUMBER := DBMS_LOB.getLength(clob);\r\n" + 
						"    buf    VARCHAR2(32767);\r\n" + 
						"    arr    APEX_APPLICATION_GLOBAL.VC_ARR2;*/\r\n" + 
						"    -- Поля\r\n" + 
						"    trid             VARCHAR2(32767);\r\n" + 
						"    number_          VARCHAR2(32767);\r\n" + 
						"    paymentnumber    VARCHAR2(32767);\r\n" + 
						"    receiptnumber    VARCHAR2(32767);\r\n" + 
						"    idterm           VARCHAR2(32767);\r\n" + 
						"    receiver         VARCHAR2(32767);\r\n" + 
						"    service          VARCHAR2(32767);\r\n" + 
						"    account          VARCHAR2(32767);\r\n" + 
						"    paymentsum       VARCHAR2(32767);\r\n" + 
						"    insum            VARCHAR2(32767);\r\n" + 
						"    receiversum      VARCHAR2(32767);\r\n" + 
						"    outsum           VARCHAR2(32767);\r\n" + 
						"    feesum           VARCHAR2(32767);\r\n" + 
						"    startdeal        VARCHAR2(32767);\r\n" + 
						"    deal             VARCHAR2(32767);\r\n" + 
						"    fromdeal         VARCHAR2(32767);\r\n" + 
						"    isdeal           VARCHAR2(32767);\r\n" + 
						"    dealaccount      VARCHAR2(32767);\r\n" + 
						"    datetimepayment  VARCHAR2(32767);\r\n" + 
						"    datetimesrv      VARCHAR2(32767);\r\n" + 
						"    datetimestatus   VARCHAR2(32767);\r\n" + 
						"    stat             VARCHAR2(32767);\r\n" + 
						"    currency         VARCHAR2(32767);\r\n" + 
						"    badpaymentnumber VARCHAR2(32767);\r\n" + 
						"    fio              VARCHAR2(32767);\r\n" + 
						"    paymentdata1     VARCHAR2(32767);\r\n" + 
						"    paymentdata2     VARCHAR2(32767);\r\n" + 
						"    paymentdata3     VARCHAR2(32767);\r\n" + 
						"    paymentdata4     VARCHAR2(32767);\r\n" + 
						"    compositedata    VARCHAR2(32767);\r\n" + 
						"  BEGIN\r\n" + 
						"    /*Чистка таблицы со всеми транзакциями*/\r\n" + 
						"    DELETE FROM z_sb_tr_from_txt_file;\r\n" + 
						"    /*Чистка таблицы со всеми транзакциями*/\r\n" + 
						"  \r\n" + 
						"    COMMIT;\r\n" + 
						"    WHILE offset < len LOOP\r\n" + 
						"      amount := LEAST(DBMS_LOB.INSTR(CLOB, CHR(10), offset) - offset, 32767);\r\n" + 
						"    \r\n" + 
						"      IF amount > 0 THEN\r\n" + 
						"        -- this is slow...\r\n" + 
						"        DBMS_LOB.read(CLOB, amount, offset, buf);\r\n" + 
						"        offset := offset + amount + 1;\r\n" + 
						"      ELSE\r\n" + 
						"        buf    := NULL;\r\n" + 
						"        offset := offset + 1;\r\n" + 
						"      END IF;\r\n" + 
						"    \r\n" + 
						"      /* WHILE offset < len LOOP\r\n" + 
						"      DBMS_LOB.read(clob, amount, offset, buf);\r\n" + 
						"      offset := offset + amount;\r\n" + 
						"      arr    := APEX_UTIL.string_to_table(buf, CHR(10));\r\n" + 
						"      FOR i IN 1 .. arr.COUNT LOOP\r\n" + 
						"        IF i < arr.COUNT and\r\n" + 
						"           Substr(arr(i), 1, instr(arr(i), ';') - 1) like '%-%' THEN*/\r\n" + 
						"      -- ID\r\n" + 
						"      trid := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер\r\n" + 
						"      number_ := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер платежа\r\n" + 
						"      paymentnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf           := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер квитанции\r\n" + 
						"      receiptnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf           := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Точка\r\n" + 
						"      idterm := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Клиент\r\n" + 
						"      receiver := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Услуга\r\n" + 
						"      service := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Аккаунт\r\n" + 
						"      account := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма платежа\r\n" + 
						"      paymentsum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf        := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма наличными\r\n" + 
						"      insum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf   := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма получателя\r\n" + 
						"      receiversum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Выданная сумма\r\n" + 
						"      outsum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Комиссия\r\n" + 
						"      feesum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма проводки\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Начальная сдача\r\n" + 
						"      startdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf       := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сдача\r\n" + 
						"      deal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Со сдачи\r\n" + 
						"      fromdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Использование сдачи\r\n" + 
						"      isdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время платежа\r\n" + 
						"      datetimepayment := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf             := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время сервера\r\n" + 
						"      datetimesrv := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время статуса\r\n" + 
						"      datetimestatus := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf            := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      --  Статус\r\n" + 
						"      stat := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Получатель\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Архивирование\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Шлюз\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Валюта получения\r\n" + 
						"      currency := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Переотправлен\r\n" + 
						"      badpaymentnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf              := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Тип платежа\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Идентификатор сервера\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- ФИО\r\n" + 
						"      fio := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- ДатаВремя штрафа\r\n" + 
						"      paymentdata1 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер автомобиля\r\n" + 
						"      paymentdata2 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Пост\r\n" + 
						"      paymentdata3 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма штрафа\r\n" + 
						"      paymentdata4 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер чека сдачи\r\n" + 
						"      dealaccount := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"    \r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Данные сложного платежа\r\n" + 
						"      compositedata := TRIM(buf);\r\n" + 
						"    \r\n" + 
						"      --begin\r\n" + 
						"      INSERT INTO z_sb_tr_from_txt_file\r\n" + 
						"        (trid_t,\r\n" + 
						"         number_t,\r\n" + 
						"         paymentnumber_t,\r\n" + 
						"         receiptnumber_t,\r\n" + 
						"         idterm_t,\r\n" + 
						"         receiver_t,\r\n" + 
						"         service_t,\r\n" + 
						"         account_t,\r\n" + 
						"         paymentsum_t,\r\n" + 
						"         insum_t,\r\n" + 
						"         receiversum_t,\r\n" + 
						"         outsum_t,\r\n" + 
						"         feesum_t,\r\n" + 
						"         startdeal_t,\r\n" + 
						"         deal_t,\r\n" + 
						"         fromdeal_t,\r\n" + 
						"         isdeal_t,\r\n" + 
						"         dealaccount_t,\r\n" + 
						"         datetimepayment_t,\r\n" + 
						"         datetimesrv_t,\r\n" + 
						"         datetimestatus_t,\r\n" + 
						"         stat_t,\r\n" + 
						"         currency_t,\r\n" + 
						"         badpaymentnumber_t,\r\n" + 
						"         fio_t,\r\n" + 
						"         paymentdata1_t,\r\n" + 
						"         paymentdata2_t,\r\n" + 
						"         paymentdata3_t,\r\n" + 
						"         paymentdata4_t,\r\n" + 
						"         compositedata_t)\r\n" + 
						"      VALUES\r\n" + 
						"        (trid,\r\n" + 
						"         number_,\r\n" + 
						"         paymentnumber,\r\n" + 
						"         receiptnumber,\r\n" + 
						"         idterm,\r\n" + 
						"         receiver,\r\n" + 
						"         service,\r\n" + 
						"         account,\r\n" + 
						"         paymentsum,\r\n" + 
						"         insum,\r\n" + 
						"         receiversum,\r\n" + 
						"         outsum,\r\n" + 
						"         feesum,\r\n" + 
						"         startdeal,\r\n" + 
						"         deal,\r\n" + 
						"         fromdeal,\r\n" + 
						"         isdeal,\r\n" + 
						"         dealaccount,\r\n" + 
						"         datetimepayment,\r\n" + 
						"         datetimesrv,\r\n" + 
						"         datetimestatus,\r\n" + 
						"         stat,\r\n" + 
						"         currency,\r\n" + 
						"         badpaymentnumber,\r\n" + 
						"         fio,\r\n" + 
						"         paymentdata1,\r\n" + 
						"         paymentdata2,\r\n" + 
						"         paymentdata3,\r\n" + 
						"         paymentdata4,\r\n" + 
						"         compositedata);\r\n" + 
						"      COMMIT;\r\n" + 
						"    END LOOP;\r\n" + 
						"  END dump_clob;\r\n" + 
						"  /*Быстрый способ обработки CLOB не используется...\r\n" + 
						"  Чтения всего файла и вставка в z_sb_tr_from_txt_file*/\r\n" + 
						"\r\n" + 
						"  /*Основная Функция обработки транзакции*/\r\n" + 
						"  FUNCTION load_pack(file      CLOB,\r\n" + 
						"                     file_name VARCHAR2,\r\n" + 
						"                     sess_id__ NUMBER := NULL) RETURN VARCHAR IS\r\n" + 
						"    ret_   VARCHAR(1000);\r\n" + 
						"    ret    CLOB;\r\n" + 
						"    offset NUMBER := 1;\r\n" + 
						"    amount NUMBER;\r\n" + 
						"    len    NUMBER := DBMS_LOB.getlength(file);\r\n" + 
						"    buf    VARCHAR2(32767); --32767\r\n" + 
						"  \r\n" + 
						"    result_ NUMBER(1);\r\n" + 
						"    -------------------------\r\n" + 
						"    l_sep_index PLS_INTEGER;\r\n" + 
						"    l_index     PLS_INTEGER := 1;\r\n" + 
						"    --l_tab       t_my_list := t_my_list();\r\n" + 
						"    -------------------------\r\n" + 
						"  \r\n" + 
						"    -- Поля\r\n" + 
						"    trid             VARCHAR2(32767);\r\n" + 
						"    number_          VARCHAR2(32767);\r\n" + 
						"    paymentnumber    VARCHAR2(32767);\r\n" + 
						"    receiptnumber    VARCHAR2(32767);\r\n" + 
						"    idterm           VARCHAR2(32767);\r\n" + 
						"    receiver         VARCHAR2(32767);\r\n" + 
						"    service          VARCHAR2(32767);\r\n" + 
						"    account          VARCHAR2(32767);\r\n" + 
						"    paymentsum       VARCHAR2(32767);\r\n" + 
						"    insum            VARCHAR2(32767);\r\n" + 
						"    receiversum      VARCHAR2(32767);\r\n" + 
						"    outsum           VARCHAR2(32767);\r\n" + 
						"    feesum           VARCHAR2(32767);\r\n" + 
						"    startdeal        VARCHAR2(32767);\r\n" + 
						"    deal             VARCHAR2(32767);\r\n" + 
						"    fromdeal         VARCHAR2(32767);\r\n" + 
						"    isdeal           VARCHAR2(32767);\r\n" + 
						"    dealaccount      VARCHAR2(32767);\r\n" + 
						"    datetimepayment  VARCHAR2(32767);\r\n" + 
						"    datetimesrv      VARCHAR2(32767);\r\n" + 
						"    datetimestatus   VARCHAR2(32767);\r\n" + 
						"    stat             VARCHAR2(32767);\r\n" + 
						"    currency         VARCHAR2(32767);\r\n" + 
						"    badpaymentnumber VARCHAR2(32767);\r\n" + 
						"    fio              VARCHAR2(32767);\r\n" + 
						"    paymentdata1     VARCHAR2(32767);\r\n" + 
						"    paymentdata2     VARCHAR2(32767);\r\n" + 
						"    paymentdata3     VARCHAR2(32767);\r\n" + 
						"    paymentdata4     VARCHAR2(32767);\r\n" + 
						"    compositedata    VARCHAR2(32767);\r\n" + 
						"  \r\n" + 
						"    dealsmall VARCHAR2(32767);\r\n" + 
						"    dealbig   VARCHAR2(32767);\r\n" + 
						"  \r\n" + 
						"    dealsmall_ VARCHAR2(32767);\r\n" + 
						"    dealbig_   VARCHAR2(32767);\r\n" + 
						"  \r\n" + 
						"    paydate VARCHAR2(32767);\r\n" + 
						"    --\r\n" + 
						"  \r\n" + 
						"    iterator NUMBER;\r\n" + 
						"  \r\n" + 
						"    chkpaynmb DBMS_SQL.varchar2_table;\r\n" + 
						"  \r\n" + 
						"    chkstat DBMS_SQL.number_table;\r\n" + 
						"  \r\n" + 
						"    chkdeal DBMS_SQL.number_table;\r\n" + 
						"  \r\n" + 
						"    id1      NUMBER(30);\r\n" + 
						"    id2      NUMBER(30);\r\n" + 
						"    testflag NUMBER(1);\r\n" + 
						"  \r\n" + 
						"    alldealsum NUMBER;\r\n" + 
						"  \r\n" + 
						"    flagdeal BOOLEAN;\r\n" + 
						"    flagbad  BOOLEAN;\r\n" + 
						"  \r\n" + 
						"    final_   BOOLEAN;\r\n" + 
						"    err      NUMBER(1);\r\n" + 
						"    sess_id_ NUMBER;\r\n" + 
						"  \r\n" + 
						"    chkduplicate_deal BOOLEAN;\r\n" + 
						"    chkduplicate_bad  BOOLEAN;\r\n" + 
						"  \r\n" + 
						"    dprt VARCHAR2(32767);\r\n" + 
						"  \r\n" + 
						"    NO_DATA_FOUND EXCEPTION;\r\n" + 
						"  \r\n" + 
						"    chek_ varchar(30);\r\n" + 
						"  \r\n" + 
						"    chek_composite_data NUMBER := NULL;\r\n" + 
						"  BEGIN\r\n" + 
						"    /*Вставка данных о загрузке*/\r\n" + 
						"    BEGIN\r\n" + 
						"      INSERT INTO z_sb_fn_sess\r\n" + 
						"      VALUES\r\n" + 
						"        (NVL(sess_id__, z_sb_sq_sess_id.NEXTVAL),\r\n" + 
						"         file_name,\r\n" + 
						"         CURRENT_TIMESTAMP,\r\n" + 
						"         file) RETURN sess_id INTO sess_id_;\r\n" + 
						"    \r\n" + 
						"      COMMIT;\r\n" + 
						"    EXCEPTION\r\n" + 
						"      WHEN OTHERS THEN\r\n" + 
						"        sess_id_ := sess_id__;\r\n" + 
						"    END;\r\n" + 
						"    /*Вставка данных о загрузке*/\r\n" + 
						"  \r\n" + 
						"    id1 := 1;\r\n" + 
						"    dump_clob(file);\r\n" + 
						"  \r\n" + 
						"    WHILE offset < len LOOP\r\n" + 
						"      alldealsum := 0;\r\n" + 
						"      -- this is slowwwwww...\r\n" + 
						"      amount := LEAST(DBMS_LOB.INSTR(file, CHR(10), offset) - offset, 32767);\r\n" + 
						"    \r\n" + 
						"      IF amount > 0 THEN\r\n" + 
						"        -- this is slow...\r\n" + 
						"        DBMS_LOB.read(file, amount, offset, buf);\r\n" + 
						"        offset := offset + amount + 1;\r\n" + 
						"      ELSE\r\n" + 
						"        buf    := NULL;\r\n" + 
						"        offset := offset + 1;\r\n" + 
						"      END IF;\r\n" + 
						"    \r\n" + 
						"      --DBMS_OUTPUT.put_line(buf);\r\n" + 
						"    \r\n" + 
						"      -- ID\r\n" + 
						"      trid := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер\r\n" + 
						"      number_ := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер платежа\r\n" + 
						"      paymentnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf           := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер квитанции\r\n" + 
						"      receiptnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf           := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Точка\r\n" + 
						"      idterm := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Клиент\r\n" + 
						"      receiver := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Услуга\r\n" + 
						"      service := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Аккаунт\r\n" + 
						"      account := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма платежа\r\n" + 
						"      paymentsum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf        := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма наличными\r\n" + 
						"      insum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf   := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма получателя\r\n" + 
						"      receiversum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Выданная сумма\r\n" + 
						"      outsum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Комиссия\r\n" + 
						"      feesum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма проводки\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Начальная сдача\r\n" + 
						"      startdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf       := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сдача\r\n" + 
						"      deal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Со сдачи\r\n" + 
						"      fromdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Использование сдачи\r\n" + 
						"      isdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время платежа\r\n" + 
						"      datetimepayment := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf             := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время сервера\r\n" + 
						"      datetimesrv := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время статуса\r\n" + 
						"      datetimestatus := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf            := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      --  Статус\r\n" + 
						"      stat := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Получатель\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Архивирование\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Шлюз\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Валюта получения\r\n" + 
						"      currency := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Переотправлен\r\n" + 
						"      badpaymentnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf              := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Тип платежа\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Идентификатор сервера\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- ФИО\r\n" + 
						"      fio := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- ДатаВремя штрафа\r\n" + 
						"      paymentdata1 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер автомобиля\r\n" + 
						"      paymentdata2 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Пост\r\n" + 
						"      paymentdata3 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма штрафа\r\n" + 
						"      paymentdata4 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер чека сдачи\r\n" + 
						"      dealaccount := REPLACE(TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1)),\r\n" + 
						"                             '|',\r\n" + 
						"                             '');\r\n" + 
						"      if PaymentNumber = '07696686510803621790' then\r\n" + 
						"        AllDealSum := null;\r\n" + 
						"      end if;\r\n" + 
						"    \r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"    \r\n" + 
						"      -- Данные сложного платежа\r\n" + 
						"      compositedata := TRIM(buf);\r\n" + 
						"    \r\n" + 
						"      flagdeal := TRUE; -- Ищем номер платежа сдачи\r\n" + 
						"      flagbad  := TRUE; -- Ищем номер платежа\r\n" + 
						"      err      := 0;\r\n" + 
						"    \r\n" + 
						"      -- Проверки\r\n" + 
						"    \r\n" + 
						"      /*ChkPayNMB(id1) := PaymentNumber;\r\n" + 
						"      ChkDeal(id1) := to_number(replace(StartDeal, '.', ','));\r\n" + 
						"      id1 := id1 + 1;*/\r\n" + 
						"      if badpaymentnumber = 'yes' then\r\n" + 
						"        null;\r\n" + 
						"      else\r\n" + 
						"        /*        if paymentnumber = '21406780331811514860' then\r\n" + 
						"          paymentnumber := paymentnumber;\r\n" + 
						"        end if;*/\r\n" + 
						"        IF dealaccount IS NOT NULL THEN\r\n" + 
						"          dealbig := dealaccount;\r\n" + 
						"        \r\n" + 
						"          WHILE LENGTH(dealbig) <> 0 AND flagdeal = TRUE LOOP\r\n" + 
						"            dealsmall := SUBSTR(dealbig, 1, 20);\r\n" + 
						"            dealbig   := SUBSTR(dealbig, 21);\r\n" + 
						"            flagdeal  := FALSE; -- Ищем номер платежа сдачи\r\n" + 
						"          \r\n" + 
						"            FOR dl IN (SELECT dl_.recdate       AS recdate,\r\n" + 
						"                              dl_.status        AS status,\r\n" + 
						"                              dl_.sum_          AS SUM,\r\n" + 
						"                              dl_.paymentnumber AS paymentnumber\r\n" + 
						"                         FROM z_sb_termdeal_dbt dl_\r\n" + 
						"                        WHERE dl_.paymentnumber = dealsmall) LOOP\r\n" + 
						"              IF dl.status = 0 THEN\r\n" + 
						"                --AllDealSum := null;\r\n" + 
						"                alldealsum := NVL(alldealsum, 0) + TO_NUMBER(dl.SUM);\r\n" + 
						"                flagdeal   := TRUE;\r\n" + 
						"              ELSE\r\n" + 
						"                writelog(datetimepayment,\r\n" + 
						"                         trid,\r\n" + 
						"                         'Транзакция номер ' || paymentnumber ||\r\n" + 
						"                         ' использует сдачу по чеку номер ' || dealsmall ||\r\n" + 
						"                         ' который уже погашен!|Транзакции загружены не будут!|Обратитесь к администратору!' ||\r\n" + 
						"                         dl.paymentnumber || dl.status,\r\n" + 
						"                         sess_id_);\r\n" + 
						"                flagdeal := FALSE;\r\n" + 
						"              END IF;\r\n" + 
						"            END LOOP;\r\n" + 
						"          \r\n" + 
						"            IF flagdeal = FALSE THEN\r\n" + 
						"              chkduplicate_deal := FALSE;\r\n" + 
						"            \r\n" + 
						"              FOR tr IN (SELECT *\r\n" + 
						"                           FROM z_sb_tr_from_txt_file t\r\n" + 
						"                          WHERE t.paymentnumber_t = dealsmall) LOOP\r\n" + 
						"                IF chkduplicate_deal = FALSE THEN\r\n" + 
						"                  chkduplicate_deal := TRUE;\r\n" + 
						"                  --AllDealSum        := null;\r\n" + 
						"                  alldealsum := NVL(alldealsum, 0) +\r\n" + 
						"                                NVL(TO_NUMBER(REPLACE(tr.startdeal_t,\r\n" + 
						"                                                      '.',\r\n" + 
						"                                                      ',')),\r\n" + 
						"                                    0);\r\n" + 
						"                  flagdeal   := TRUE;\r\n" + 
						"                ELSE\r\n" + 
						"                  writelog(datetimepayment,\r\n" + 
						"                           trid,\r\n" + 
						"                           'Транзакция номер' || paymentnumber ||\r\n" + 
						"                           ' использует сдачу по чеку номер ' || dealsmall ||\r\n" + 
						"                           ' который встречается в текущих транзакциях дважды!|Транзакции загружены не будут!|Обратитесь к администратору!',\r\n" + 
						"                           sess_id_);\r\n" + 
						"                  flagdeal := FALSE; -- Нашли в танзакциях но во второй раз\r\n" + 
						"                END IF;\r\n" + 
						"              END LOOP;\r\n" + 
						"            \r\n" + 
						"              IF flagdeal = FALSE THEN\r\n" + 
						"                /*                writedeal(datetimepayment,\r\n" + 
						"                paymentnumber,\r\n" + 
						"                trid,\r\n" + 
						"                startdeal,\r\n" + 
						"                idterm,\r\n" + 
						"                sess_id_,\r\n" + 
						"                sts => 999);*/\r\n" + 
						"              \r\n" + 
						"                writelog(datetimepayment,\r\n" + 
						"                         trid,\r\n" + 
						"                         'Транзакция номер ' || paymentnumber ||\r\n" + 
						"                         ' использует сдачу по чеку номер ' || dealsmall ||\r\n" + 
						"                         ' которого нет ни в базе, ни в файле',\r\n" + 
						"                         sess_id_);\r\n" + 
						"                null;\r\n" + 
						"              END IF;\r\n" + 
						"            END IF;\r\n" + 
						"          END LOOP;\r\n" + 
						"        ELSE\r\n" + 
						"          flagdeal := TRUE;\r\n" + 
						"        END IF;\r\n" + 
						"      \r\n" + 
						"        IF TRIM(badpaymentnumber) IS NOT NULL THEN\r\n" + 
						"          flagbad := FALSE; -- Ищем номер платежа\r\n" + 
						"        \r\n" + 
						"          FOR bad IN (SELECT bad.recdate recdate, bad.status status\r\n" + 
						"                        FROM z_sb_badtr_dbt bad\r\n" + 
						"                       WHERE bad.badpaymentnumber = paymentnumber) LOOP\r\n" + 
						"            IF bad.status = 0 THEN\r\n" + 
						"              flagbad := TRUE; -- Нашли в базе\r\n" + 
						"            ELSE\r\n" + 
						"              writelog(datetimepayment,\r\n" + 
						"                       trid,\r\n" + 
						"                       'Транзакция номер ' || paymentnumber ||\r\n" + 
						"                       ' порождена аварийной транзакцией номер ' ||\r\n" + 
						"                       badpaymentnumber ||\r\n" + 
						"                       ' которая уже обработана!|Транзакции загружены не будут!|Обратитесь к администратору!',\r\n" + 
						"                       sess_id_);\r\n" + 
						"              flagbad := FALSE;\r\n" + 
						"            END IF;\r\n" + 
						"          END LOOP;\r\n" + 
						"        \r\n" + 
						"          IF flagbad = FALSE THEN\r\n" + 
						"            chkduplicate_bad := FALSE;\r\n" + 
						"          \r\n" + 
						"            FOR tr IN (SELECT *\r\n" + 
						"                         FROM z_sb_tr_from_txt_file t\r\n" + 
						"                        WHERE t.badpaymentnumber_t = badpaymentnumber) LOOP\r\n" + 
						"              IF chkduplicate_bad = FALSE THEN\r\n" + 
						"                flagbad := TRUE;\r\n" + 
						"              ELSE\r\n" + 
						"                writelog(datetimepayment,\r\n" + 
						"                         trid,\r\n" + 
						"                         'Транзакция номер ' || paymentnumber ||\r\n" + 
						"                         ' порождена аварийной транзакцией номер ' ||\r\n" + 
						"                         badpaymentnumber ||\r\n" + 
						"                         ' которая встречается в текущих транзакциях дважды!|Транзакции загружены не будут!|Обратитесь к администратору!',\r\n" + 
						"                         sess_id_);\r\n" + 
						"                flagbad := FALSE;\r\n" + 
						"              END IF;\r\n" + 
						"            END LOOP;\r\n" + 
						"          END IF;\r\n" + 
						"        ELSE\r\n" + 
						"          flagbad := TRUE;\r\n" + 
						"        END IF;\r\n" + 
						"      \r\n" + 
						"        --\r\n" + 
						"        IF TO_NUMBER(REPLACE(fromdeal, '.', ',')) <>\r\n" + 
						"           NVL(REPLACE(alldealsum, '.', ','), 0) THEN\r\n" + 
						"          err := err + 1;\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'Обнаружена ошибка в платеже номер:' || paymentnumber ||\r\n" + 
						"                   ' Общая сумма оплаты со сдачи ' || fromdeal ||\r\n" + 
						"                   ' не соответствует сумме по чекам сдачи ' || alldealsum,\r\n" + 
						"                   sess_id_);\r\n" + 
						"          null;\r\n" + 
						"        END IF;\r\n" + 
						"      \r\n" + 
						"        IF (flagdeal = FALSE) OR (flagbad = FALSE) THEN\r\n" + 
						"          err := err + 1;\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'Обнаружена ошибка в платеже номер:' || paymentnumber ||\r\n" + 
						"                   ' порожденного сдачей/аварийной транзакцией:' ||\r\n" + 
						"                   dealaccount || '/' || badpaymentnumber || ' с флагами: ' ||\r\n" + 
						"                   boolean_to_char(flagdeal) || '/' ||\r\n" + 
						"                   boolean_to_char(flagbad),\r\n" + 
						"                   sess_id_);\r\n" + 
						"        END IF;\r\n" + 
						"      \r\n" + 
						"        IF compositedata = 'Null' THEN\r\n" + 
						"          err := err + 1;\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'Встретился NULL :)!',\r\n" + 
						"                   sess_id_);\r\n" + 
						"        END IF;\r\n" + 
						"      \r\n" + 
						"        IF TRIM(compositedata) = ';' THEN\r\n" + 
						"          err := err + 1;\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'Встретился символ \";\" :)!',\r\n" + 
						"                   sess_id_);\r\n" + 
						"        END IF;\r\n" + 
						"      \r\n" + 
						"        /*Проверка AQUAFON*/\r\n" + 
						"        IF service = 'Аквафон' then\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'В транзакции с номером платежа ' || paymentnumber ||\r\n" + 
						"                   ' проходит оплата за Aquafon' ||\r\n" + 
						"                   '. Обратитесь к администратору!',\r\n" + 
						"                   sess_id_);\r\n" + 
						"          null;\r\n" + 
						"        end if;\r\n" + 
						"        /*Проверка AQUAFON*/\r\n" + 
						"      \r\n" + 
						"        /*Проверка IDTERM*/\r\n" + 
						"        dprt := makedprt(idterm);\r\n" + 
						"      \r\n" + 
						"        IF dprt = 'not_found' THEN\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'В транзакции с номером платежа ' || paymentnumber ||\r\n" + 
						"                   ' найден отсутствующий в базе терминал ' || idterm ||\r\n" + 
						"                   '. Обратитесь к администратору!',\r\n" + 
						"                   sess_id_);\r\n" + 
						"          null;\r\n" + 
						"        END IF;\r\n" + 
						"        /*Проверка IDTERM*/\r\n" + 
						"        if dubl_data(paymentnumber_ => paymentnumber) = true then\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   'Внимание! Транзакция PaymentNumber = ' || paymentnumber ||\r\n" + 
						"                   ' уже загружена в АБС!',\r\n" + 
						"                   sess_id_);\r\n" + 
						"          null;\r\n" + 
						"        end if;\r\n" + 
						"      \r\n" + 
						"        /*Отмененные транзакции*/\r\n" + 
						"        if paymentnumber in\r\n" + 
						"           ('21406780330693235029', '21406780331793863006') then\r\n" + 
						"          writelog(datetimepayment,\r\n" + 
						"                   trid,\r\n" + 
						"                   ' Транзакция' || paymentnumber || ' уже отменена!' ||\r\n" + 
						"                   ' .Обратитесь к администратору!',\r\n" + 
						"                   sess_id_);\r\n" + 
						"          null;\r\n" + 
						"        end if;\r\n" + 
						"        /*Отмененные транзакции\r\n" + 
						"        /*Проверка соответствия composite_data с receiversum*/\r\n" + 
						"        BEGIN\r\n" + 
						"          IF LOWER(REPLACE(service, ' ', '')) =\r\n" + 
						"             LOWER(REPLACE('Таможня онлайн СБ', ' ', '')) THEN\r\n" + 
						"            SELECT SUM(TO_NUMBER(REPLACE(REGEXP_SUBSTR(SUBSTR(REGEXP_SUBSTR(compositedata,\r\n" + 
						"                                                                            '[^]]+',\r\n" + 
						"                                                                            1,\r\n" + 
						"                                                                            LEVEL) || '|',\r\n" + 
						"                                                              2),\r\n" + 
						"                                                       '[^\\|]+',\r\n" + 
						"                                                       1,\r\n" + 
						"                                                       3),\r\n" + 
						"                                         '.',\r\n" + 
						"                                         ','))) str\r\n" + 
						"              INTO chek_composite_data\r\n" + 
						"              FROM DUAL\r\n" + 
						"            CONNECT BY LEVEL <= REGEXP_COUNT(compositedata, ']');\r\n" + 
						"            IF chek_composite_data !=\r\n" + 
						"               TO_NUMBER(REPLACE(receiversum, '.', ',')) THEN\r\n" + 
						"              writelog(datetimepayment,\r\n" + 
						"                       trid,\r\n" + 
						"                       'В транзакции с номером платежа ' || paymentnumber ||\r\n" + 
						"                       ' сумма сложного платежа ' || chek_composite_data ||\r\n" + 
						"                       ' не совпадает с суммой ReceiverSum ' || receiversum ||\r\n" + 
						"                       ' .Обратитесь к администратору!',\r\n" + 
						"                       sess_id_);\r\n" + 
						"              /* if abs(chek_composite_data -\r\n" + 
						"                     TO_NUMBER(REPLACE(receiversum, '.', ','))) = 100 and\r\n" + 
						"                 idterm = 'СБ 0003' then\r\n" + 
						"                \\*writelog(datetimepayment,\r\n" + 
						"                trid,\r\n" + 
						"                'В транзакции с номером платежа ' || paymentnumber ||\r\n" + 
						"                ' сумма сложного платежа ' || chek_composite_data ||\r\n" + 
						"                ' не совпадает с суммой ReceiverSum ' ||\r\n" + 
						"                receiversum ||\r\n" + 
						"                ' на 100р. .Обратитесь к администратору!',\r\n" + 
						"                sess_id_);*\\\r\n" + 
						"                NULL;\r\n" + 
						"              \r\n" + 
						"              else\r\n" + 
						"                \\*writelog(datetimepayment,\r\n" + 
						"                         trid,\r\n" + 
						"                         'В транзакции с номером платежа ' || paymentnumber ||\r\n" + 
						"                         ' сумма сложного платежа ' || chek_composite_data ||\r\n" + 
						"                         ' не совпадает с суммой ReceiverSum ' ||\r\n" + 
						"                         receiversum || ' .Обратитесь к администратору!',\r\n" + 
						"                         sess_id_);*\\\r\n" + 
						"                NULL;\r\n" + 
						"              end if;*/\r\n" + 
						"              null;\r\n" + 
						"            END IF;\r\n" + 
						"          END IF;\r\n" + 
						"        EXCEPTION\r\n" + 
						"          WHEN OTHERS THEN\r\n" + 
						"            NULL;\r\n" + 
						"        END;\r\n" + 
						"      end if;\r\n" + 
						"    END LOOP;\r\n" + 
						"  \r\n" + 
						"    /*Проверка не соответствия composite_data с receiversum*/\r\n" + 
						"  \r\n" + 
						"    /*Обнуляем переменные */\r\n" + 
						"    offset := 1;\r\n" + 
						"    len    := DBMS_LOB.getlength(file);\r\n" + 
						"    amount := NULL;\r\n" + 
						"    buf    := NULL;\r\n" + 
						"    /*Обнуляем переменные */\r\n" + 
						"  \r\n" + 
						"    WHILE offset < len LOOP\r\n" + 
						"      -- this is slowwwwww...\r\n" + 
						"      amount := LEAST(DBMS_LOB.INSTR(file, CHR(10), offset) - offset, 32767);\r\n" + 
						"    \r\n" + 
						"      IF amount > 0 THEN\r\n" + 
						"        -- this is slow...\r\n" + 
						"        DBMS_LOB.read(file, amount, offset, buf);\r\n" + 
						"        offset := offset + amount + 1;\r\n" + 
						"      ELSE\r\n" + 
						"        buf    := NULL;\r\n" + 
						"        offset := offset + 1;\r\n" + 
						"      END IF;\r\n" + 
						"    \r\n" + 
						"      -- ID\r\n" + 
						"      trid := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер\r\n" + 
						"      number_ := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер платежа\r\n" + 
						"      paymentnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf           := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер квитанции\r\n" + 
						"      receiptnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf           := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Точка\r\n" + 
						"      idterm := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Клиент\r\n" + 
						"      receiver := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Услуга\r\n" + 
						"      service := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Аккаунт\r\n" + 
						"      account := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf     := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма платежа\r\n" + 
						"      paymentsum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf        := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма наличными\r\n" + 
						"      insum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf   := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма получателя\r\n" + 
						"      receiversum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Выданная сумма\r\n" + 
						"      outsum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Комиссия\r\n" + 
						"      feesum := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма проводки\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Начальная сдача\r\n" + 
						"      startdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf       := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сдача\r\n" + 
						"      deal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Со сдачи\r\n" + 
						"      fromdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Использование сдачи\r\n" + 
						"      isdeal := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf    := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время платежа\r\n" + 
						"      datetimepayment := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf             := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время сервера\r\n" + 
						"      datetimesrv := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Время статуса\r\n" + 
						"      datetimestatus := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf            := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      --  Статус\r\n" + 
						"      stat := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf  := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Получатель\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Архивирование\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Шлюз\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Валюта получения\r\n" + 
						"      currency := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf      := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Переотправлен\r\n" + 
						"      badpaymentnumber := SUBSTR(buf, 1, INSTR(buf, ';') - 1);\r\n" + 
						"      buf              := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Тип платежа\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Идентификатор сервера\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- ФИО\r\n" + 
						"      fio := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- ДатаВремя штрафа\r\n" + 
						"      paymentdata1 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер автомобиля\r\n" + 
						"      paymentdata2 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Пост\r\n" + 
						"      paymentdata3 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Сумма штрафа\r\n" + 
						"      paymentdata4 := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf          := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"      -- Номер чека сдачи\r\n" + 
						"      dealaccount := TRIM(SUBSTR(buf, 1, INSTR(buf, ';') - 1));\r\n" + 
						"      buf         := SUBSTR(buf, INSTR(buf, ';') + 1);\r\n" + 
						"    \r\n" + 
						"      -- Данные сложного платежа\r\n" + 
						"      compositedata := TRIM(buf);\r\n" + 
						"    \r\n" + 
						"      if PaymentNumber = '07696686510803621790' then\r\n" + 
						"        AllDealSum := null;\r\n" + 
						"      end if;\r\n" + 
						"    \r\n" + 
						"      --Возврат\r\n" + 
						"      final_ := FALSE;\r\n" + 
						"    \r\n" + 
						"      FOR dl IN (SELECT lg.sess_id\r\n" + 
						"                   FROM z_sb_log_dbt lg\r\n" + 
						"                  WHERE lg.sess_id = sess_id_) LOOP\r\n" + 
						"        final_ := TRUE;\r\n" + 
						"      END LOOP;\r\n" + 
						"    \r\n" + 
						"      IF final_ = FALSE THEN\r\n" + 
						"        if badpaymentnumber = 'yes' then\r\n" + 
						"          WriteDataToBad(paymentnumber,\r\n" + 
						"                         trid,\r\n" + 
						"                         datetimepayment,\r\n" + 
						"                         compositedata,\r\n" + 
						"                         makepaymentdata(service,\r\n" + 
						"                                         paymentdata1,\r\n" + 
						"                                         paymentdata2,\r\n" + 
						"                                         paymentdata3,\r\n" + 
						"                                         paymentdata4,\r\n" + 
						"                                         account),\r\n" + 
						"                         fio,\r\n" + 
						"                         badpaymentnumber,\r\n" + 
						"                         currency,\r\n" + 
						"                         stat,\r\n" + 
						"                         datetimestatus,\r\n" + 
						"                         datetimesrv,\r\n" + 
						"                         dealaccount,\r\n" + 
						"                         isdeal,\r\n" + 
						"                         fromdeal,\r\n" + 
						"                         deal,\r\n" + 
						"                         startdeal,\r\n" + 
						"                         feesum,\r\n" + 
						"                         outsum,\r\n" + 
						"                         receiversum,\r\n" + 
						"                         insum,\r\n" + 
						"                         paymentsum,\r\n" + 
						"                         service,\r\n" + 
						"                         receiver,\r\n" + 
						"                         idterm,\r\n" + 
						"                         receiptnumber,\r\n" + 
						"                         number_,\r\n" + 
						"                         account,\r\n" + 
						"                         sess_id_);\r\n" + 
						"        \r\n" + 
						"          writebad(datetimepayment,\r\n" + 
						"                   paymentnumber,\r\n" + 
						"                   trid,\r\n" + 
						"                   fromdeal,\r\n" + 
						"                   insum,\r\n" + 
						"                   badpaymentnumber,\r\n" + 
						"                   idterm,\r\n" + 
						"                   sess_id_);\r\n" + 
						"        else\r\n" + 
						"          writedata(paymentnumber,\r\n" + 
						"                    trid,\r\n" + 
						"                    datetimepayment,\r\n" + 
						"                    compositedata,\r\n" + 
						"                    makepaymentdata(service,\r\n" + 
						"                                    paymentdata1,\r\n" + 
						"                                    paymentdata2,\r\n" + 
						"                                    paymentdata3,\r\n" + 
						"                                    paymentdata4,\r\n" + 
						"                                    account),\r\n" + 
						"                    fio,\r\n" + 
						"                    badpaymentnumber,\r\n" + 
						"                    currency,\r\n" + 
						"                    stat,\r\n" + 
						"                    datetimestatus,\r\n" + 
						"                    datetimesrv,\r\n" + 
						"                    dealaccount,\r\n" + 
						"                    isdeal,\r\n" + 
						"                    fromdeal,\r\n" + 
						"                    deal,\r\n" + 
						"                    startdeal,\r\n" + 
						"                    feesum,\r\n" + 
						"                    outsum,\r\n" + 
						"                    receiversum,\r\n" + 
						"                    insum,\r\n" + 
						"                    paymentsum,\r\n" + 
						"                    service,\r\n" + 
						"                    receiver,\r\n" + 
						"                    idterm,\r\n" + 
						"                    receiptnumber,\r\n" + 
						"                    number_,\r\n" + 
						"                    account,\r\n" + 
						"                    sess_id_);\r\n" + 
						"        \r\n" + 
						"          IF (startdeal <> '0.00') AND (startdeal <> '0,00') THEN\r\n" + 
						"            writedeal(datetimepayment,\r\n" + 
						"                      paymentnumber,\r\n" + 
						"                      trid,\r\n" + 
						"                      startdeal,\r\n" + 
						"                      idterm,\r\n" + 
						"                      sess_id_);\r\n" + 
						"          END IF;\r\n" + 
						"        \r\n" + 
						"          IF makestat(stat) <> 1 THEN\r\n" + 
						"            writebad(datetimepayment,\r\n" + 
						"                     paymentnumber,\r\n" + 
						"                     trid,\r\n" + 
						"                     fromdeal,\r\n" + 
						"                     insum,\r\n" + 
						"                     badpaymentnumber,\r\n" + 
						"                     idterm,\r\n" + 
						"                     sess_id_);\r\n" + 
						"          END IF;\r\n" + 
						"        END IF;\r\n" + 
						"      end if;\r\n" + 
						"    END LOOP;\r\n" + 
						"  \r\n" + 
						"    /*Чистка таблицы со всеми транзакциями*/\r\n" + 
						"    DELETE FROM z_sb_tr_from_txt_file;\r\n" + 
						"    /*Чистка таблицы со всеми транзакциями*/\r\n" + 
						"  \r\n" + 
						"    COMMIT;\r\n" + 
						"  \r\n" + 
						"    --Возврат\r\n" + 
						"    result_ := '0';\r\n" + 
						"  \r\n" + 
						"    FOR dl IN (SELECT lg.sess_id\r\n" + 
						"                 FROM z_sb_log_dbt lg\r\n" + 
						"                WHERE lg.sess_id = sess_id_) LOOP\r\n" + 
						"      result_ := '1';\r\n" + 
						"    END LOOP;\r\n" + 
						"  \r\n" + 
						"    IF result_ = '0' THEN\r\n" + 
						"      ret_ := '0';\r\n" + 
						"    ELSE\r\n" + 
						"      ret_ := '1';\r\n" + 
						"    END IF;\r\n" + 
						"  \r\n" + 
						"    RETURN ret_ || ':' || TO_CHAR(sess_id_);\r\n" + 
						"    NULL;\r\n" + 
						"  END;\r\n" + 
						"  /*Основная Функция обработки транзакции*/\r\n" + 
						"begin\r\n" + 
						"  load_pack(?, ?);\r\n" + 
						"end;\r\n" + 
						"");

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
					String readRecordSQL = "SELECT * FROM Z_SB_LOG_DBT WHERE sess_id = " + part2 + "";
					ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

					// String[] path =
					// textbox.getText().toString().split("::_");
					// String path1 = path[0].trim();

					DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
					String strDate_ = dateFormat_.format(date);
					String createfolder = System.getenv("TRANSACT_PATH")/* System.getProperty("user.dir") */ + strDate_
							+ "_SESSID_" + sessid_;

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
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Загрузка прошла успешна. Можете перейти к расчету");
					alert.showAndWait();
				}
				callStmt.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Выберите сначала файл для загрузки");
				alert.showAndWait();
			}
		} catch (SQLException |

				IOException e) {
			// TODO Auto-generated catch block
			result.setText(e.getMessage());
		}
	}

	public static String getFileCharset(String file) {
		try {
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
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return null;
	}

	// запись в текстовый файл протокола загрузки
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
			String createfolder = System.getenv("TRANSACT_PATH")/* System.getProperty("user.dir") */ + strDate_
					+ "_SESSID_" + sessid_;

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
					writer.write("Протокол загрузки файла.\r\n");
					while ((line = bufferedReader.readLine()) != null) {
						rowcount = rowcount + 1;
						// System.out.println(line);
						writer.write("Номер строки: " + rowcount + ";" + line + "\r\n");
					}
					bufferedReader.close();
					writer.write("\r\nПротокол загрузки транзакции.\r\n");
				}
				writer.write("Номер строки: " + rowid + ";" + myResultSet.getString("recdate") + ";"
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
				String createfolder = System.getenv("TRANSACT_PATH")/* System.getProperty("user.dir") */ + strDate_
						+ "_SESSID_" + sessid_;

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
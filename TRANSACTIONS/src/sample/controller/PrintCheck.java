package sample.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import sample.model.Connect;
import sample.model.Item3;

public class PrintCheck extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void showReport(String paymnt_number,String sess_id) {
		
		try {
			String reportSrcFile = System.getenv("TRANSACT_PATH") + "\\" + "report\\Blank_A4.jrxml";
			 
	        // First, compile jrxml file.
	        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
	        // Fields for report
	        HashMap<String, Object> parameters = new HashMap<String, Object>();

			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL ="select t.terminal,\r\n" + 
					"               (select ADDRESS\r\n" + 
					"                  from z_sb_terminal_amra_dbt j\r\n" + 
					"                 where j.name = t.terminal) street,\r\n" + 
					"               t.checknumber,\r\n" + 
					"               t.PAYDATE,\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/јтрибуты/јтр' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@”слуга',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@Ќомер„ека',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@»м€јтрибута',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@«начениејтрибута')\r\n" + 
					"                 where ATTRIBUTENAME = 'ds_name') kindergarten,\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/јтрибуты/јтр' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@”слуга',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@Ќомер„ека',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@»м€јтрибута',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@«начениејтрибута')\r\n" + 
					"                 where ATTRIBUTENAME = 'group_name') group_,\r\n" + 
					"               upper((SELECT AttributeValue\r\n" + 
					"                       FROM XMLTABLE('/јтрибуты/јтр' PASSING\r\n" + 
					"                                     xmltype(ATTRIBUTES_) COLUMNS Service\r\n" + 
					"                                     VARCHAR2(500) PATH '@”слуга',\r\n" + 
					"                                     CheckNumber VARCHAR2(500) PATH\r\n" + 
					"                                     '@Ќомер„ека',\r\n" + 
					"                                     AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                     '@»м€јтрибута',\r\n" + 
					"                                     AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                     '@«начениејтрибута')\r\n" + 
					"                      where ATTRIBUTENAME = 'fio_children')) fio,\r\n" + 
					"               '+7 ' ||\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/јтрибуты/јтр' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@”слуга',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@Ќомер„ека',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@»м€јтрибута',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@«начениејтрибута')\r\n" + 
					"                 where ATTRIBUTENAME = 'account') number_,\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/јтрибуты/јтр' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@”слуга',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@Ќомер„ека',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@»м€јтрибута',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@«начениејтрибута')\r\n" + 
					"                 where ATTRIBUTENAME = 'period') period,\r\n" + 
					"               t.amountofpayment psum,\r\n" + 
					"               t.COMMISSIONAMOUNT fee\r\n" + 
					"          from z_sb_transact_amra_dbt t\r\n" + 
					"         where t.checknumber = '"+paymnt_number+"'";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(readRecordSQL);
			while (myResultSet.next()) {
		        parameters.put("terminal", myResultSet.getString("TERMINAL"));
		        parameters.put("checknumber", myResultSet.getString("CHECKNUMBER"));
		        parameters.put("datetime", myResultSet.getString("PAYDATE"));
		        parameters.put("kindergarten", myResultSet.getString("KINDERGARTEN"));
		        parameters.put("group", myResultSet.getString("GROUP_"));
		        parameters.put("fio",myResultSet.getString("FIO"));
		        parameters.put("number", myResultSet.getString("NUMBER_"));
		        parameters.put("psum", myResultSet.getString("PSUM"));
		        parameters.put("street", myResultSet.getString("STREET"));
		        parameters.put("fee", myResultSet.getString("FEE"));
		        parameters.put("period", myResultSet.getString("PERIOD"));
			}
			myResultSet.close();
			conn.close();
	        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	        list.add(parameters);
	 
	        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
	        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, beanColDataSource);
	        JRViewer viewer = new JRViewer(print);
	        viewer.setOpaque(true);
	        viewer.setVisible(true);
	        this.add(viewer);
	        this.setSize(700, 500);
	        this.setVisible(true);
	        //this.setIconImages((List<? extends java.awt.Image>) new Image("terminal.png"));
			
		} catch (JRException | SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("¬нимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}

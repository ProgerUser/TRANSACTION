package su.sbra.psv.app.controller;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.util.DBUtil;

public class PrintCheck extends JFrame {

	/**
	 * ������ ����
	 */
	private static final long serialVersionUID = 1L;

	public void showReport(String paymnt_number,String sess_id) {
		
		try {
			InputStream input = this.getClass().getResourceAsStream("/Blank_A4.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);
	        HashMap<String, Object> parameters = new HashMap<String, Object>();
			Connection conn = DBUtil.conn;
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL ="select t.terminal,\r\n" + 
					"               (select ADDRESS\r\n" + 
					"                  from z_sb_terminal_amra_dbt j\r\n" + 
					"                 where j.name = t.terminal) street,\r\n" + 
					"               t.checknumber,\r\n" + 
					"               t.PAYDATE,\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@������',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@���������',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@�����������',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@����������������')\r\n" + 
					"                 where ATTRIBUTENAME = 'ds_name') kindergarten,\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@������',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@���������',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@�����������',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@����������������')\r\n" + 
					"                 where ATTRIBUTENAME = 'group_name') group_,\r\n" + 
					"               upper((SELECT AttributeValue\r\n" + 
					"                       FROM XMLTABLE('/��������/���' PASSING\r\n" + 
					"                                     xmltype(ATTRIBUTES_) COLUMNS Service\r\n" + 
					"                                     VARCHAR2(500) PATH '@������',\r\n" + 
					"                                     CheckNumber VARCHAR2(500) PATH\r\n" + 
					"                                     '@���������',\r\n" + 
					"                                     AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                     '@�����������',\r\n" + 
					"                                     AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                     '@����������������')\r\n" + 
					"                      where ATTRIBUTENAME = 'fio_children')) fio,\r\n" + 
					"               '+7 ' ||\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@������',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@���������',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@�����������',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@����������������')\r\n" + 
					"                 where ATTRIBUTENAME = 'account') number_,\r\n" + 
					"               (SELECT AttributeValue\r\n" + 
					"                  FROM XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_)\r\n" + 
					"                                COLUMNS Service VARCHAR2(500) PATH '@������',\r\n" + 
					"                                CheckNumber VARCHAR2(500) PATH '@���������',\r\n" + 
					"                                AttributeName VARCHAR2(500) PATH\r\n" + 
					"                                '@�����������',\r\n" + 
					"                                AttributeValue VARCHAR2(500) PATH\r\n" + 
					"                                '@����������������')\r\n" + 
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
			//conn.close();
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
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
			Main.logger.error(ExceptionUtils.getStackTrace(e) + Thread.currentThread().getName());
		}
	}
}

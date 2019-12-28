package sample.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import sample.model.Item;
import sample.model.Item2;

public class PrintReport2 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void showReport(String paymnt_number,String sess_id) {
		try {
			String reportSrcFile = System.getenv("TRANSACT_PATH") + "\\" + "report\\postdoc.jrxml";

			// First, compile jrxml file.
			JasperReport jasperReport;
			jasperReport = JasperCompileManager.compileReport(reportSrcFile);
			/* User home directory location */

			/* List to hold Items */
			List<Item2> listItems = new ArrayList<Item2>();

			/* Create Items */
			Item2 list = null;

			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select CTRNACCD,\r\n" + 
					"       CTRNACCC,\r\n" + 
					"       MTRNRSUM,\r\n" + 
					"       CTRNPURP,\r\n" + 
					"       DTRNTRAN,\r\n" + 
					"       t.DTRNCREATE,\r\n" + 
					"       case\r\n" + 
					"         when ITRNNUM is null then\r\n" + 
					"          'Отсутствует в главной книге'\r\n" + 
					"         else\r\n" + 
					"          'Документ найден'\r\n" + 
					"       end stat\r\n" + 
					"  from trn t, z_sb_postdoc_amra_dbt g\r\n" + 
					" where t.ITRNNUM(+) = g.KINDPAYMENT\r\n" + 
					"   and PAYMENTNUMBERS like '%"+paymnt_number+"%'\r\n" + 
					"   and sess_id = "+sess_id+"\r\n" + 
					"   order by CTRNACCD,MTRNRSUM desc";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(readRecordSQL);
			while (myResultSet.next()) {
				list = new Item2();
				list.setdebet(myResultSet.getString("CTRNACCD"));
				list.setcredit(myResultSet.getString("CTRNACCC"));
				list.setsumm(myResultSet.getDouble("MTRNRSUM"));
				list.setstat(myResultSet.getString("STAT"));
				list.setdate_(myResultSet.getDate("DTRNCREATE"));
				list.setdate_reg(myResultSet.getDate("DTRNTRAN"));

				System.out.println(myResultSet.getDate("CTRNACCD"));
				listItems.add(list);
			}
			myResultSet.close();
			conn.close();

			/* Convert List to JRBeanCollectionDataSource */
			JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);

			/* Map to hold Jasper report Parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ItemDataSource", itemsJRBean);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			JRViewer viewer = new JRViewer(print);
			viewer.setOpaque(true);
			viewer.setVisible(true);
			this.add(viewer);
			this.setSize(1000, 900);
			this.setVisible(true);
		} catch (JRException | SQLException e) {
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

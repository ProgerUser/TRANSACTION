package sb_tr.controller;

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
import sb_tr.model.Connect;
import sb_tr.model.Item2;
import sb_tr.util.DBUtil;

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
/*
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
					*/
			Connection conn = DBUtil.conn;
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = 
					"select "+
							"nvl(CTRNACCD,g.account_payer) debet,\r\n" + 
							"       nvl(CTRNACCC,g.account_receiver) credit,\r\n" + 
							"       nvl(MTRNRSUM,g.sum) summ,\r\n" + 
							"       nvl(CTRNPURP,g.ground) ground,\r\n" + 
							"       nvl(DTRNTRAN,g.date_value) date_reg_,\r\n" + 
							"       nvl(t.DTRNCREATE,g.date_document) date_,"+
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
					" order by CTRNACCD, MTRNRSUM desc\r\n" + 
					"";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
			System.out.println(readRecordSQL);
			while (myResultSet.next()) {
				list = new Item2();
				list.setdebet(myResultSet.getString("debet"));
				list.setcredit(myResultSet.getString("credit"));
				list.setsumm(myResultSet.getDouble("summ"));
				list.setstat(myResultSet.getString("stat"));
				list.setdate_(myResultSet.getDate("date_"));
				list.setground(myResultSet.getString("ground"));
				//list.setdate_reg(myResultSet.getTimestamp("date_reg"));
				String date_reg_ = null;
				if(myResultSet.getString("date_reg_") != null) {
					date_reg_ = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(myResultSet.getTimestamp("date_reg_"));
				}else
				{
					date_reg_ = "Не проведена!"/*myResultSet.getString("date_reg_")*/;
				}
				
				list.setdate_reg_(date_reg_);
				

				//System.out.println(myResultSet.getDate("CTRNACCD"));
				listItems.add(list);
			}
			myResultSet.close();
			//conn.close();

			/* Convert List to JRBeanCollectionDataSource */
			JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);

			/* Map to hold Jasper report Parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ItemDataSource", itemsJRBean);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			// java - get screen size using the Toolkit class
			@SuppressWarnings("unused")
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			// the screen height
			//int width = (int) screenSize.getHeight();

			// the screen width
			//int height = (int) screenSize.getWidth();
			
			//System.out.println("width = "+width);
			//System.out.println("height = "+height);
			
			JRViewer viewer = new JRViewer(print);
			viewer.setOpaque(true);
			viewer.setVisible(true);
			viewer.setSize(1000,800);//;(width, height);;
			this.add(viewer);
			this.setSize(1000,800);
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

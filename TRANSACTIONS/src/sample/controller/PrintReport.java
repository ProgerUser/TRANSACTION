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
import sample.model.Transact;

public class PrintReport extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void showReport(String sess_id) {
		try {
			String reportSrcFile = System.getenv("TRANSACT_PATH") + "\\" + "report\\QUERY.jrxml";

			// First, compile jrxml file.
			JasperReport jasperReport;
			jasperReport = JasperCompileManager.compileReport(reportSrcFile);
			/* User home directory location */

			/* List to hold Items */
			List<Item> listItems = new ArrayList<Item>();

			/* Create Items */
			Item list = null;

			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
					+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select acc,\r\n" + "        debet,\r\n" + "       credit,\r\n" + "       case\r\n"
					+ "         when debet < credit then\r\n" + "          credit - debet\r\n" + "         else\r\n"
					+ "          debet - credit\r\n" + "       end razn , date_\r\n"
					+ "  from (select distinct acc,\r\n"
					+ "                        BALANCE.AccCredTO(date_, date_ + 1, acc, 'RUR') credit,\r\n"
					+ "                        BALANCE.AccDebTO(date_, date_ + 1, acc, 'RUR') debet , date_\r\n"
					+ "          from (select DEBET acc, DTRNCREATE date_\r\n"
					+ "                  from (select DTRNCREATE,\r\n"
					+ "                               ITRNDOCNUM DOC_NUM,\r\n"
					+ "                               CTRNACCD   debet,\r\n"
					+ "                               CTRNACCC   credit,\r\n"
					+ "                               MTRNRSUM   summ\r\n" + "                          from trn t\r\n"
					+ "                         where CTRNIDOPEN = (select upper(user) from dual)\r\n"
					+ "                           AND (DTRNCREATE, CTRNACCD, CTRNACCC, MTRNRSUM,\r\n"
					+ "                                CTRNPURP) in\r\n"
					+ "                               (select DATE_VALUE,\r\n"
					+ "                                       ACCOUNT_PAYER,\r\n"
					+ "                                       ACCOUNT_RECEIVER,\r\n"
					+ "                                       SUM,\r\n"
					+ "                                       GROUND\r\n"
					+ "                                  from z_sb_postdoc_amra_dbt\r\n"
					+ "                                 where sess_id = " + sess_id + ")\r\n"
					+ "                         order by DOC_NUM)\r\n"
					+ "                 group by DEBET, CREDIT, DTRNCREATE\r\n" + "                union all\r\n"
					+ "                select CREDIT acc, DTRNCREATE date_\r\n"
					+ "                  from (select DTRNCREATE,\r\n"
					+ "                               ITRNDOCNUM DOC_NUM,\r\n"
					+ "                               CTRNACCD   debet,\r\n"
					+ "                               CTRNACCC   credit,\r\n"
					+ "                               MTRNRSUM   summ\r\n" + "                          from trn t\r\n"
					+ "                         where CTRNIDOPEN = (select upper(user) from dual)\r\n"
					+ "                           AND (DTRNCREATE, CTRNACCD, CTRNACCC, MTRNRSUM,\r\n"
					+ "                                CTRNPURP) in\r\n"
					+ "                               (select DATE_VALUE,\r\n"
					+ "                                       ACCOUNT_PAYER,\r\n"
					+ "                                       ACCOUNT_RECEIVER,\r\n"
					+ "                                       SUM,\r\n"
					+ "                                       GROUND\r\n"
					+ "                                  from z_sb_postdoc_amra_dbt\r\n"
					+ "                                 where sess_id = " + sess_id + ")\r\n"
					+ "                         order by DOC_NUM)\r\n"
					+ "                 group by DEBET, CREDIT, DTRNCREATE)\r\n" + "         order by ACC asc)";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

			while (myResultSet.next()) {
				list = new Item();
				list.setacc(myResultSet.getString("acc"));
				list.setdebet(myResultSet.getDouble("debet"));
				list.setcredit(myResultSet.getDouble("credit"));
				list.setd_c(myResultSet.getDouble("razn"));
				list.setdate_(myResultSet.getDate("date_"));

				// System.out.println(myResultSet.getInt("debet"));
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

package app.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import app.model.Item2;
import app.model.SqlMap;
import app.util.DBUtil;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class PrintReport2 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public void showReport(String paymnt_number, String sess_id, String znak) {
		try {
			InputStream input = this.getClass().getResourceAsStream("/postdoc.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);

			/* List to hold Items */
			List<Item2> listItems = new ArrayList<Item2>();

			/* Create Items */
			Item2 list = null;

			Connection conn = DBUtil.conn;

			SqlMap s = new SqlMap().load("/SQL.xml");
			String readRecordSQL = s.getSql("getPOSTTRN");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setString(1, paymnt_number);
			prepStmt.setInt(2, Integer.valueOf(sess_id));
			ResultSet myResultSet = prepStmt.executeQuery();

			System.out.println(readRecordSQL);
			while (myResultSet.next()) {
				list = new Item2();
				list.setdebet(myResultSet.getString("debet"));
				list.setcredit(myResultSet.getString("credit"));
				list.setsumm(myResultSet.getDouble("summ"));
				list.setstat(myResultSet.getString("stat"));
				list.setdate_(myResultSet.getDate("date_"));
				list.setground(myResultSet.getString("ground"));
				list.settrsum(myResultSet.getString("trsum"));
				// list.settr(myResultSet.getString("tr"));
				// list.setdate_reg(myResultSet.getTimestamp("date_reg"));
				String date_reg_ = null;
				if (myResultSet.getString("date_reg_") != null) {
					date_reg_ = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(myResultSet.getTimestamp("date_reg_"));
				} else {
					date_reg_ = "Не проведена!"/* myResultSet.getString("date_reg_") */;
				}

				list.setdate_reg_(date_reg_);

				// System.out.println(myResultSet.getDate("CTRNACCD"));
				listItems.add(list);
			}
			myResultSet.close();
			// conn.close();

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
			// int width = (int) screenSize.getHeight();

			// the screen width
			// int height = (int) screenSize.getWidth();

			// System.out.println("width = "+width);
			// System.out.println("height = "+height);

			JRViewer viewer = new JRViewer(print);
			viewer.setOpaque(true);
			viewer.setVisible(true);
			viewer.setSize(1000, 800);// ;(width, height);;
			this.add(viewer);
			this.setSize(1000, 800);
			this.setVisible(true);
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
	}
}

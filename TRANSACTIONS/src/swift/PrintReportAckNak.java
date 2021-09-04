package swift;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.lang3.exception.ExceptionUtils;

import app.util.DBUtil;
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
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class PrintReportAckNak extends JFrame {

	private static final long serialVersionUID = 1L;

	public void showReport(String ref) {
		try {
			InputStream input = this.getClass().getResourceAsStream("/swift/AckNak.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);

			/* List to hold Items */
			List<AckNakModel> listItems = new ArrayList<AckNakModel>();

			/* Create Items */
			AckNakModel list = null;

			Connection conn = DBUtil.conn;
			String readRecordSQL = "select is_ack_nak, to_char(date_time,'dd.mm.yyyy hh24:mi:ss') date_time, filename, sw_ref\r\n"
					+ "  from swift_files_others sw\r\n"
					+ " where sw.sw_ref =?\r\n";
			PreparedStatement stmt = conn.prepareStatement(readRecordSQL);
			stmt.setString(1, ref);
			
			ResultSet myResultSet = stmt.executeQuery();
			System.out.println(readRecordSQL);
			while (myResultSet.next()) {
				list = new AckNakModel();
				list.setIS_ACK_NAK(myResultSet.getString("is_ack_nak"));
				list.setFILENAME(myResultSet.getString("filename"));
				list.setSW_REF(myResultSet.getString("sw_ref"));
				list.setDATE_TIME(myResultSet.getString("date_time"));

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
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
	}
}

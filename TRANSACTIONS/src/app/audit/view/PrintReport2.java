package app.audit.view;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import app.Main;
import app.model.SqlMap;
import app.utils.DbUtil;
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

	private static final long serialVersionUID = 1L;

	public void showReport(String tablename, Long actid) {
		try {
			Main.logger = Logger.getLogger(getClass());
			InputStream input = this.getClass().getResourceAsStream("/app/audit/view/Audit.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);
			/* List to hold Items */
			List<AUDIT_REPORT> listItems = new ArrayList<AUDIT_REPORT>();
			/* Create Items */

			Connection conn = DbUtil.conn;
			SqlMap sql = new SqlMap().load("/SQL.xml");
			String readRecordSQL = sql.getSql("AUDIT_REPORT");
			Main.logger.info(readRecordSQL);
			PreparedStatement prepStmt = conn.prepareStatement("select d.IACTION_ID,\r\n"
					+ "       CFIELD,\r\n"
					+ "       AUP_UTIL.Get_Col_Comment(Tab_Name   => CTABLE,\r\n"
					+ "                                Tab_Owner  => 'XXI',\r\n"
					+ "                                Field_Name => CFIELD) CFIELDNAME,\r\n"
					+ "       CNEWDATA,\r\n"
					+ "       COLDDATA,\r\n"
					+ "       DAUDDATE,\r\n"
					+ "       CTABLE,\r\n"
					+ "       AUP_UTIL.Get_Tab_Comment(Tab_Name => CTABLE, Tab_Owner => 'XXI') CTABLENAME,\r\n"
					+ "       CAUDMACHINE,\r\n"
					+ "       CAUDPROGRAM,\r\n"
					+ "       CAUDOPERATION\r\n"
					+ "  from AU_DATA d, AU_ACTION a\r\n"
					+ " where d.iaction_id = a.iaction_id\r\n"
					+ "   and a.ctable = ?\r\n"
					+ "   and d.IACTION_ID = ?");
			prepStmt.setString(1, tablename);
			prepStmt.setLong(2, actid);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				AUDIT_REPORT list = new AUDIT_REPORT();
				list.setCAUDOPERATION(rs.getString("CAUDOPERATION"));
				list.setCAUDPROGRAM(rs.getString("CAUDPROGRAM"));
				list.setCAUDMACHINE(rs.getString("CAUDMACHINE"));
				list.setCTABLENAME(rs.getString("CTABLENAME"));
				list.setCTABLE(rs.getString("CTABLE"));
				list.setDAUDDATE(rs.getTimestamp("DAUDDATE"));
				list.setCOLDDATA(rs.getString("COLDDATA"));
				list.setCNEWDATA(rs.getString("CNEWDATA"));
				list.setCFIELDNAME(rs.getString("CFIELDNAME"));
				list.setCFIELD(rs.getString("CFIELD"));
				list.setIACTIONID(rs.getLong("IACTION_ID"));
				listItems.add(list);
			}
			rs.close();
			prepStmt.close();
			JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ItemDataSource", itemsJRBean);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			
			//JasperExportManager.exportReportToPdfFile(print, "AUDIT.rtf");

			/*
			File destFile = new File(System.getenv("MJ_PATH")+"/reports/", print.getName() + ".xls");
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
			
			//Открытие выгруженного файла
			try {
			    Runtime.getRuntime().exec("cmd /c start excel.exe \""+System.getenv("MJ_PATH")+"/reports/"+print.getName() + ".xls\"");
			} catch (Exception e) {
			    e.printStackTrace();
			}
			*/

			
			JRViewer viewer = new JRViewer(print);
			viewer.setOpaque(true);
			viewer.setVisible(true);
			viewer.setSize(900, 800);
			this.add(viewer);
			this.setSize(900, 800);
			this.setVisible(true);
			
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}
}

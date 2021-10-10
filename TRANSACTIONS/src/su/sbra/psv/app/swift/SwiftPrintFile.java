package su.sbra.psv.app.swift;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.utils.DbUtil;

public class SwiftPrintFile extends JFrame {

	private static final long serialVersionUID = 1L;

	public void showReport(InputStream inputstream) {
		try {
			Main.logger = Logger.getLogger(getClass());
			Main.logger.setLevel(Level.INFO);
			InputStream input = this.getClass().getResourceAsStream("/su/sbra/psv/app/swift/SwRep.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);

			List<SwiftPrintModel> listItems = new ArrayList<SwiftPrintModel>();
			/* Create Items */
			SwiftPrintModel list = null;

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
			while (reader.ready()) {
				list = new SwiftPrintModel();
				list.setmessage(reader.readLine());
				listItems.add(list);
			}

			/* Convert List to JRBeanCollectionDataSource */
			JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);
			/* Map to hold Jasper report Parameters */
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ItemDataSource", itemsJRBean);
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
			JRViewer viewer = new JRViewer(print);
			viewer.setOpaque(true);
			viewer.setVisible(true);
			viewer.setLocation(50,100);
			this.add(viewer);
			this.setSize(800, 700);
			this.setVisible(true);
			inputstream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
			Main.logger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}
}

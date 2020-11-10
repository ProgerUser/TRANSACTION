package swift;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field;

import app.Main;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import sbalert.Msg;

public class SwiftPrint extends JFrame {

	private static final long serialVersionUID = 1L;

	public void showReport(String path) {
		try {
			Main.logger = Logger.getLogger(getClass());
			InputStream input = this.getClass().getResourceAsStream("/swift/SwRep.jrxml");
			JasperDesign design = JRXmlLoader.load(input);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);

			List<SwiftPrintModel> listItems = new ArrayList<SwiftPrintModel>();
			/* Create Items */
			SwiftPrintModel list = null;

			Locale locale = new Locale("ru", "RU");
			InputStream inputstream = new FileInputStream(path);
			String result = CharStreams.toString(new InputStreamReader(inputstream, Charsets.UTF_8));
			SwiftMessage sm = SwiftMessage.parse(result);

			/*
			 * С одним значением в поле
			 */
			list = new SwiftPrintModel();
			list.setmessage("Отправитель: " + sm.getSender());
			listItems.add(list);
			list = new SwiftPrintModel();
			list.setmessage("Получатель: " + sm.getReceiver());
			listItems.add(list);

			for (Tag tag : sm.getBlock4().getTags()) {
				list = new SwiftPrintModel();
				Field field = tag.asField();
				list.setmessage(Field.getLabel(field.getName(), "103", null, locale));
				list.setmessage(field.getValueDisplay(locale));
				listItems.add(list);
			}
			/*
			 * Со значениями для каждого компонента
			 */
			for (Tag tag : sm.getBlock4().getTags()) {
				Field field = tag.asField();
				list = new SwiftPrintModel();
				list.setmessage(Field.getLabel(field.getName(), "103", null, locale));

				for (int component = 1; component <= field.componentsSize(); component++) {
					if (field.getComponent(component) != null) {
						list.setmessage(field.getComponentLabel(component) + ": ");
						list.setmessage(field.getValueDisplay(component, locale));
					}
				}
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
			this.add(viewer);
			this.setSize(800, 700);
			this.setVisible(true);
			inputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
			Main.logger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}
}

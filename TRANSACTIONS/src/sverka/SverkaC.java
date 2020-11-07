package sverka;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import app.model.Connect;
import app.model.SqlMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
 * ������ �� ���. �����, �������� � ������� 1�
 * 
 * 05.11.2020
 * 
 * @author Said
 *
 */
public class SverkaC {

	@FXML
	private TableColumn<AMRA_STMT_CALC, String> Status;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> DEB_OB;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> END_REST;

	@FXML
	private TableColumn<AMRA_STMT_CALC, LocalDate> STMT_BEGIN;

	@FXML
	private TableColumn<AMRA_STMT_CALC, LocalDate> STMT_END;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Integer> ID;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> BEGIN_REST;

	@FXML
	private TableView<AMRA_STMT_CALC> STMT;

	@FXML
	private TableColumn<AMRA_STMT_CALC, Double> CRED_OB;

	@FXML
	private TableColumn<AMRA_STMT_CALC, LocalDateTime> LOAD_DATE;

	@FXML
	private TableColumn<AMRA_STMT_CALC, LocalDateTime> CREATION_DATETIME;

	/**
	 * ��������� ����
	 * 
	 * @param event
	 */
	@FXML
	void LoadFile(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("������� ����");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("��������� ����", "*.txt"));
			if (System.getenv("IBANK_EXPORT") != null) {
				fileChooser.setInitialDirectory(new File(System.getenv("IBANK_EXPORT")));
			} else {
				fileChooser.setInitialDirectory(
						new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
			}

			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				String xml = CrXml(file.getParent() + "/" + file.getName());
				String reviewStr = readFile(file.getParent() + "\\" + file.getName());
				Clob TXT = conn.createClob();
				TXT.setString(1, reviewStr);
				Clob XML = conn.createClob();
				XML.setString(1, xml);
				CallableStatement callStmt = conn.prepareCall("{ ? = call z_sb_create_tr_amra.AMRA_STMT_EXEC(?,?,?,?)");
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setClob(2, TXT);
				callStmt.registerOutParameter(3, Types.INTEGER);
				callStmt.registerOutParameter(4, Types.VARCHAR);
				callStmt.setClob(5, XML);
				callStmt.execute();
				if (callStmt.getString(1).equals("OK")) {
					Message("�������");
					conn.commit();
					InitTable();
				} else {
					Message(callStmt.getString(4));
				}
				callStmt.close();
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}

	}

	String CrXml(String path) {
		String ret = null;
		try {
			/* XML */
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element root = document.createElement("stmt");
			document.appendChild(root);
			/* Loop */
			Element doc = null;
			Element sys = document.createElement("sys");
			try (BufferedReader br = new BufferedReader(new FileReader(path))) {
				for (String line = null; (line = br.readLine()) != null;) {
					/* sys element */
					if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("������������")) {
						Attr attr = document.createAttribute("������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
						System.out.println("<sys>");
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������")) {
						Attr attr = document.createAttribute("����������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("���������")) {
						Attr attr = document.createAttribute("���������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������������")) {
						Attr attr = document.createAttribute("����������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������������")) {
						Attr attr = document.createAttribute("��������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("������������")) {
						Attr attr = document.createAttribute("������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("���������������")) {
						Attr attr = document.createAttribute("���������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������")) {
						Attr attr = document.createAttribute("��������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						sys.setAttributeNode(attr);
					} else if (line.trim().equals("�������������")) {
						root.appendChild(sys);
					}
					/* doc element */
					if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������������")) {
						doc = document.createElement("doc");
						Attr attr = document.createAttribute("��������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
						// System.out.println("<doc>");
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����")) {
						Attr attr = document.createAttribute("�����");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����")) {
						Attr attr = document.createAttribute("����");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����")) {
						Attr attr = document.createAttribute("�����");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������������")) {
						Attr attr = document.createAttribute("��������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������")) {
						Attr attr = document.createAttribute("����������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������1")) {
						Attr attr = document.createAttribute("����������1");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����������������")) {
						Attr attr = document.createAttribute("�����������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������������1")) {
						Attr attr = document.createAttribute("��������������1");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=")
							&& line.substring(0, line.indexOf("=")).equals("������������������")) {
						Attr attr = document.createAttribute("������������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������������")) {
						Attr attr = document.createAttribute("��������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������")) {
						Attr attr = document.createAttribute("����������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������1")) {
						Attr attr = document.createAttribute("����������1");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=")
							&& line.substring(0, line.indexOf("=")).equals("������������������")) {
						Attr attr = document.createAttribute("������������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("��������������1")) {
						Attr attr = document.createAttribute("��������������1");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����������������")) {
						Attr attr = document.createAttribute("�����������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�������������")) {
						Attr attr = document.createAttribute("�������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("����������")) {
						Attr attr = document.createAttribute("����������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("���������")) {
						Attr attr = document.createAttribute("���������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����������")) {
						Attr attr = document.createAttribute("�����������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����������")) {
						Attr attr = document.createAttribute("�����������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("���")) {
						Attr attr = document.createAttribute("���");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.contains("=") && line.substring(0, line.indexOf("=")).equals("�����������������")) {
						Attr attr = document.createAttribute("�����������������");
						attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
						doc.setAttributeNode(attr);
					} else if (line.trim().equals("��������������")) {
						root.appendChild(doc);
						// System.out.println("</doc>");
					}
				}
			}
			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			// System.out.println(writer.toString());
			ret = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
		return ret;
	}

	/**
	 * ������� �������
	 * 
	 * @param event
	 */
	@FXML
	void OpenStmt(ActionEvent event) {

	}

	void InitTable() {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String selectStmt = "select id, \r\n" + "load_date, \r\n" + "creation_datetime, \r\n" + "stmt_begin, \r\n"
					+ "stmt_end, \r\n" + "ch_account, \r\n" + "begin_rest, \r\n" + "cred_ob, \r\n" + "deb_ob, \r\n"
					+ "end_rest, \r\n" + "file_cl, \r\n" + "oper, \r\n"
					+ " decode(status,0,'��������') status from AMRA_STMT_CALC t " + "order by ID desc ";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<AMRA_STMT_CALC> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				AMRA_STMT_CALC list = new AMRA_STMT_CALC();
				list.setOPER(rs.getString("OPER"));
				list.setEND_REST(rs.getDouble("END_REST"));
				list.setDEB_OB(rs.getDouble("DEB_OB"));
				list.setCRED_OB(rs.getDouble("CRED_OB"));
				list.setBEGIN_REST(rs.getDouble("BEGIN_REST"));
				list.setCH_ACCOUNT(rs.getString("CH_ACCOUNT"));
				list.setSTMT_END((rs.getDate("STMT_END") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("STMT_END")), formatter)
						: null);
				list.setSTMT_BEGIN((rs.getDate("STMT_BEGIN") != null) ? LocalDate
						.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("STMT_BEGIN")), formatter) : null);
				list.setCREATION_DATETIME((rs.getDate("CREATION_DATETIME") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("CREATION_DATETIME")),
						formatterwt) : null);
				list.setLOAD_DATE((rs.getDate("LOAD_DATE") != null) ? LocalDateTime
						.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("LOAD_DATE")), formatterwt)
						: null);
				list.setID(rs.getInt("ID"));
				list.setSTATUS(rs.getString("STATUS"));

				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			STMT.setItems(dlist);

			TableFilter<AMRA_STMT_CALC> tableFilter = TableFilter.forTableView(STMT).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
	}

	/**
	 * �������������� �������� DTTM
	 * 
	 * @param TC
	 */
	void DateFormatDTTM(TableColumn<AMRA_STMT_CALC, LocalDateTime> TC) {
		TC.setCellFactory(column -> {
			TableCell<AMRA_STMT_CALC, LocalDateTime> cell = new TableCell<AMRA_STMT_CALC, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(format.format(item));
					}
				}
			};
			return cell;
		});
	}

	/**
	 * �������������� �������� DT
	 * 
	 * @param TC
	 */
	void DateFormatDT(TableColumn<AMRA_STMT_CALC, LocalDate> TC) {
		TC.setCellFactory(column -> {
			TableCell<AMRA_STMT_CALC, LocalDate> cell = new TableCell<AMRA_STMT_CALC, LocalDate>() {
				private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(format.format(item));
					}
				}
			};
			return cell;
		});
	}

	/**
	 * ������ �����
	 * 
	 * @param fileName
	 * @return
	 */
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName), getFileCharset(fileName)));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(System.lineSeparator());
			}
			br.close();
			String clobData = sb.toString();
			return clobData;
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
		return null;
	}

	/**
	 * ������������� �������
	 * 
	 * @param file
	 * @return
	 */
	public String getFileCharset(String file) {
		try {
			byte[] buf = new byte[4096];
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			final UniversalDetector universalDetector = new UniversalDetector(null);
			int numberOfBytesRead;
			while ((numberOfBytesRead = bufferedInputStream.read(buf)) > 0 && !universalDetector.isDone()) {
				universalDetector.handleData(buf, 0, numberOfBytesRead);
			}
			universalDetector.dataEnd();
			String encoding = universalDetector.getDetectedCharset();
			universalDetector.reset();
			bufferedInputStream.close();
			return encoding;
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
		return null;
	}

	/**
	 * �������������
	 */
	@FXML
	private void initialize() {
		try {
			STMT.setEditable(true);
			dbConnect();

			Status.setCellValueFactory(cellData -> cellData.getValue().STATUSProperty());
			DEB_OB.setCellValueFactory(cellData -> cellData.getValue().DEB_OBProperty().asObject());
			END_REST.setCellValueFactory(cellData -> cellData.getValue().END_RESTProperty().asObject());
			CRED_OB.setCellValueFactory(cellData -> cellData.getValue().CRED_OBProperty().asObject());
			BEGIN_REST.setCellValueFactory(cellData -> cellData.getValue().BEGIN_RESTProperty().asObject());
			STMT_BEGIN.setCellValueFactory(cellData -> cellData.getValue().STMT_BEGINProperty());
			STMT_END.setCellValueFactory(cellData -> cellData.getValue().STMT_ENDProperty());
			ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
			LOAD_DATE.setCellValueFactory(cellData -> cellData.getValue().LOAD_DATEProperty());
			CREATION_DATETIME.setCellValueFactory(cellData -> cellData.getValue().CREATION_DATETIMEProperty());

			Status.setCellFactory(TextFieldTableCell.forTableColumn());
			DEB_OB.setCellFactory(
					TextFieldTableCell.<AMRA_STMT_CALC, Double>forTableColumn(new DoubleStringConverter()));
			END_REST.setCellFactory(
					TextFieldTableCell.<AMRA_STMT_CALC, Double>forTableColumn(new DoubleStringConverter()));
			CRED_OB.setCellFactory(
					TextFieldTableCell.<AMRA_STMT_CALC, Double>forTableColumn(new DoubleStringConverter()));
			BEGIN_REST.setCellFactory(
					TextFieldTableCell.<AMRA_STMT_CALC, Double>forTableColumn(new DoubleStringConverter()));
			STMT_BEGIN.setCellFactory(
					TextFieldTableCell.<AMRA_STMT_CALC, LocalDate>forTableColumn(new LocalDateStringConverter()));
			STMT_END.setCellFactory(
					TextFieldTableCell.<AMRA_STMT_CALC, LocalDate>forTableColumn(new LocalDateStringConverter()));
			ID.setCellFactory(TextFieldTableCell.<AMRA_STMT_CALC, Integer>forTableColumn(new IntegerStringConverter()));
			LOAD_DATE.setCellFactory(TextFieldTableCell
					.<AMRA_STMT_CALC, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));
			CREATION_DATETIME.setCellFactory(TextFieldTableCell
					.<AMRA_STMT_CALC, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));

			CREATION_DATETIME.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, LocalDateTime>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, LocalDateTime> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setCREATION_DATETIME(t.getNewValue());
				}
			});

			LOAD_DATE.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, LocalDateTime>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, LocalDateTime> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setLOAD_DATE(t.getNewValue());
				}
			});

			ID.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, Integer>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, Integer> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setID(t.getNewValue());
				}
			});

			STMT_END.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, LocalDate>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, LocalDate> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setSTMT_END(t.getNewValue());
				}
			});

			STMT_BEGIN.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, LocalDate>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, LocalDate> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setSTMT_BEGIN(t.getNewValue());
				}
			});

			BEGIN_REST.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, Double>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, Double> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setBEGIN_REST(t.getNewValue());
				}
			});

			CRED_OB.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, Double>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, Double> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setCRED_OB(t.getNewValue());
				}
			});

			END_REST.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, Double>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, Double> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setEND_REST(t.getNewValue());
				}
			});

			Status.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, String>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, String> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setSTATUS(t.getNewValue());
				}
			});

			DEB_OB.setOnEditCommit(new EventHandler<CellEditEvent<AMRA_STMT_CALC, Double>>() {
				@Override
				public void handle(CellEditEvent<AMRA_STMT_CALC, Double> t) {
					((AMRA_STMT_CALC) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setDEB_OB(t.getNewValue());
				}
			});

			DateFormatDTTM(LOAD_DATE);
			DateFormatDTTM(CREATION_DATETIME);
			DateFormatDT(STMT_BEGIN);
			DateFormatDT(STMT_END);

			InitTable();

			Status.setCellFactory(col -> new TextFieldTableCell<AMRA_STMT_CALC, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					} else {
						setText(item.toString());
						if (item.equals("OK")) {
							setStyle("-fx-background-color: #7ede80;" + "-fx-border-color:black;"
									+ " -fx-border-width :  1 1 1 1 ");
						} else if (item.equals("��������")) {
							setStyle("-fx-background-color: #ebaf2f;" + "-fx-border-color:black;"
									+ " -fx-border-width :  1 1 1 1 ");
						} else {
							setStyle("-fx-background-color: #e65591;" + "-fx-border-color:black;"
									+ " -fx-border-width :  1 1 1 1 ");
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
	}

	/**
	 * ����� ���������
	 * 
	 * @param mess
	 */
	public static void Message(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("icon.png"));
		alert.setTitle("��������");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	/**
	 * ������
	 */
	private Connection conn;

	/**
	 * ������� ������
	 */
	private void dbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Properties props = new Properties();
			props.put("v$session.program", "CusList");
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:" + Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_,
					props);
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			Message(e.getMessage());
		}
	}

	/**
	 * ��������� ������
	 */
	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Message(e.getMessage());
		}
	}

	/**
	 * 
	 * @param event
	 */
	@FXML
	void Check(ActionEvent event) {
		try {
			if (STMT.getSelectionModel().getSelectedItem() != null) {
				String pattern_ = "###,###.###";
				DecimalFormat decimalFormat_ = new DecimalFormat(pattern_);

				String error = "";
				AMRA_STMT_CALC rec = STMT.getSelectionModel().getSelectedItem();
				PreparedStatement prepStmt = conn.prepareStatement(
						" select distinct DT_DATE from AMRA_STMT_CALC_ROW where FILE_ID = ? order by DT_DATE");
				prepStmt.setInt(1, rec.getID());
				ResultSet rs = prepStmt.executeQuery();
				while (rs.next()) {
					{
						PreparedStatement prepStmt1 = conn.prepareStatement(
								"select ST_ID, ST_DATE, ST_SUM from table(z_sb_create_tr_amra.STMT_CHECK(?))");
						prepStmt1.setDate(1, rs.getDate("DT_DATE"));
						ResultSet rs1 = prepStmt1.executeQuery();
						while (rs1.next()) {
							error = error + "������ �� "
									+ new SimpleDateFormat("dd.MM.yyyy").format(rs1.getDate("ST_DATE")) + " �� ����� "
									+ decimalFormat_.format(rs1.getDouble("ST_SUM")) + "\r\n";
						}
						prepStmt1.close();
						rs1.close();
					}
					{
						SqlMap sql = new SqlMap().load("/Sverka.xml");
						String readRecordSQL = sql.getSql("GetVector");
						PreparedStatement prepStmt1 = conn.prepareStatement(readRecordSQL);
						prepStmt1.setDate(1, rs.getDate("DT_DATE"));
						prepStmt1.setDate(2, rs.getDate("DT_DATE"));
						prepStmt1.setDate(3, rs.getDate("DT_DATE"));
						prepStmt1.setDate(4, rs.getDate("DT_DATE"));
						prepStmt1.setDate(5, rs.getDate("DT_DATE"));
						prepStmt1.setDate(6, rs.getDate("DT_DATE"));
						ResultSet rs1 = prepStmt1.executeQuery();
						while (rs1.next()) {
							error = error + "����������� ���� "
									+ new SimpleDateFormat("dd.MM.yyyy").format(rs1.getDate("DT")) + " ����� "
									+ decimalFormat_.format(rs1.getDouble("SUMM")) + " ����������� "
									+ rs1.getString("N") + "\r\n";
						}
						prepStmt1.close();
						rs1.close();
					}
				}
				prepStmt.close();
				rs.close();

				if (!error.equals("")) {

					ShowError(rec.getID(), error);
				}
			} else {
				Message("�������� ������!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
	}

	/**
	 * ����� ������
	 * 
	 * @param ID
	 * @param txt
	 */
	void ShowError(Integer ID, String txt) {
		try {
			Date date = new Date();

			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
			String strDate = dateFormat.format(date);

			DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
			String strDate_ = dateFormat_.format(date);
			String createfolder = System.getenv("TRANSACT_PATH") + "Sverka/" + strDate_ + "_Error_" + ID;

			File file = new File(createfolder);
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			PrintWriter writer = new PrintWriter(createfolder + "\\" + strDate + "_Error_.txt");
			writer.write(txt);
			writer.close();
			ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
					createfolder + "\\" + dateFormat.format(date) + "_Error_.txt");
			pb.start();
		} catch (Exception e) {
			e.printStackTrace();
			Message(e.getMessage());
		}
	}
}

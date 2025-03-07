package su.sbra.psv.app.sverka;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.swift.ConvConst;
import su.sbra.psv.app.utils.DbUtil;

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
	@FXML
	private DatePicker dateLoad;
	@FXML
	private Button ExecButton;

	/**
	 * Stage ��� ��������
	 */
	@SuppressWarnings("unused")
	private Stage STFCLS;

	/**
	 * ������������� Stage ��� ��������
	 */
	public void SetStageForClose(Stage mnst) {
		this.STFCLS = mnst;
	}

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

				final Alert alert = new Alert(AlertType.CONFIRMATION, "��������� ���� \"" + file.getName() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					String xml = CrXml(file.getParent() + "/" + file.getName());
					String reviewStr = readFile(file.getParent() + "\\" + file.getName());
					Clob TXT = conn.createClob();
					TXT.setString(1, reviewStr);
					Clob XML = conn.createClob();
					XML.setString(1, xml);
					CallableStatement callStmt = conn.prepareCall("{ ? = call Calc1c.LoadFile(?,?,?,?)");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setClob(2, TXT);
					callStmt.registerOutParameter(3, Types.INTEGER);
					callStmt.registerOutParameter(4, Types.VARCHAR);
					callStmt.setClob(5, XML);
					callStmt.execute();
					if (callStmt.getString(1).equals("OK")) {
						Msg.Message("�������");
						conn.commit();
						InitTable(NOW_LOCAL_DATE());
					} else {
						Msg.Message(callStmt.getString(4));
					}
					callStmt.close();
				}

			}
		} catch (

		Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}

	}

	/**
	 * �������� XML
	 * 
	 * @param path
	 * @return
	 */
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
						if (!line.substring(line.indexOf("=") + 1, line.length()).equals("1�:�����������")) {
							Attr attr = document.createAttribute("����������");
							attr.setValue(line.substring(line.indexOf("=") + 1, line.length()));
							doc.setAttributeNode(attr);
						}
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return ret;
	}

	/**
	 * ������� �������
	 * 
	 * @param event
	 */
	@FXML
	void ChangeDate(ActionEvent event) {
		try {
			InitTable(dateLoad.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ������� �������
	 * 
	 * @param event
	 */
	@FXML
	void OpenStmt(ActionEvent event) {

	}

	/**
	 * 
	 */
	void InitTable(LocalDate dt) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			String selectStmt = "SELECT * FROM V_AMRA_STMT_CALC t where TRUNC(LOAD_DATE) = ? order by id desc";
			PreparedStatement prp = conn.prepareStatement(selectStmt);
			prp.setDate(1, java.sql.Date.valueOf(dt));
			ResultSet rs = prp.executeQuery();
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
			prp.close();
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * ������� ����
	 * 
	 * @return
	 */
	public LocalDate NOW_LOCAL_DATE() {
		String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate;
	}

	/**
	 * �������������
	 */
	@FXML
	private void initialize() {
		try {

			new ConvConst().FormatDatePiker(dateLoad);
			dateLoad.setValue(NOW_LOCAL_DATE());

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

			InitTable(dateLoad.getValue());

			Status.setCellFactory(col -> new TextFieldTableCell<AMRA_STMT_CALC, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					} else {
						setText(item.toString());
						if (item.equals("��������")) {
							setStyle("-fx-text-fill: #7ede80;-fx-font-weight: bold;");
						} else if (item.equals("��������")) {
							setStyle("-fx-text-fill: #ebaf2f;-fx-font-weight: bold;");
						} else if (item.equals("������")) {
							setStyle("-fx-text-fill: #e65591;-fx-font-weight: bold;");
						}
					}
				}
			});

			DEB_OB.setCellFactory(c -> new TableCell<AMRA_STMT_CALC, Double>() {
				@Override
				protected void updateItem(Double balance, boolean empty) {
					super.updateItem(balance, empty);
					if (balance == null || empty) {
						setText(null);
					} else {
						setText(String.format("%.2f", balance.doubleValue()));
					}
				}
			});

			CRED_OB.setCellFactory(c -> new TableCell<AMRA_STMT_CALC, Double>() {
				@Override
				protected void updateItem(Double balance, boolean empty) {
					super.updateItem(balance, empty);
					if (balance == null || empty) {
						setText(null);
					} else {
						setText(String.format("%.2f", balance.doubleValue()));
					}
				}
			});

			BEGIN_REST.setCellFactory(c -> new TableCell<AMRA_STMT_CALC, Double>() {
				@Override
				protected void updateItem(Double balance, boolean empty) {
					super.updateItem(balance, empty);
					if (balance == null || empty) {
						setText(null);
					} else {
						setText(String.format("%.2f", balance.doubleValue()));
					}
				}
			});
			END_REST.setCellFactory(c -> new TableCell<AMRA_STMT_CALC, Double>() {
				@Override
				protected void updateItem(Double balance, boolean empty) {
					super.updateItem(balance, empty);
					if (balance == null || empty) {
						setText(null);
					} else {
						setText(String.format("%.2f", balance.doubleValue()));
					}
				}
			});

			// sel row
			STMT.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
				AMRA_STMT_CALC sel = STMT.getSelectionModel().getSelectedItem();
				if (sel != null) {
					if (sel.getSTATUS().equals("��������")) {
						ExecButton.setDisable(false);
					} else {
						ExecButton.setDisable(true);
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ������
	 */
	private Connection conn;

	/**
	 * ������� ������
	 * 
	 * @throws UnknownHostException
	 */
	private void dbConnect() throws UnknownHostException {
		try {
			Class.forName("oracle.jdbc.OracleDriver");

			Properties props = new Properties();
			props.setProperty("password", Connect.userPassword_);
			props.setProperty("user", Connect.userID_);
			props.put("v$session.osuser", System.getProperty("user.name").toString());
			props.put("v$session.machine", InetAddress.getLocalHost().getHostAddress());
			props.put("v$session.program", getClass().getName());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);

			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ������� ���������� ���������
	 * 
	 * @param event
	 */
	void OpenAbsForm2(int fileid) {
		try {

			String call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/" + Connect.userPassword_
					+ "@ODB WHERE=\"" + "ITRNNUM IN (SELECT trnnum FROM amra_stmt_calc_row WHERE file_id = " + fileid
					+ " ) and ITRNANUM = 0\"";
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
			System.out.println(call);
			builder.redirectErrorStream(true);
			Process p;
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
				System.out.println(line);
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Open ABS
	 * 
	 * @param event
	 */
	@FXML
	void Link(ActionEvent event) {
		try {
			if (STMT.getSelectionModel().getSelectedItem() != null) {
				OpenAbsForm2(STMT.getSelectionModel().getSelectedItem().getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Open ABS
	 * 
	 * @param event
	 */
	@FXML
	void Refresh(ActionEvent event) {
		try {
			InitTable(dateLoad.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * ������������ ��������
	 * 
	 * @param event
	 */
	@FXML
	void Exec(ActionEvent event) {
		try {
			if (STMT.getSelectionModel().getSelectedItem() != null) {

				AMRA_STMT_CALC sel = STMT.getSelectionModel().getSelectedItem();

				final Alert alert = new Alert(AlertType.CONFIRMATION, "��������� ���� \"" + sel.getID() + "\" ?",
						ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					if (sel.getSTATUS().equals("��������")) {

						Date date = new Date();
						// Create the custom dialog.
						Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
						dialog.setTitle("����� ����!");

						Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image(this.getClass().getResource("/icon.png").toString()));

						// Set the button types.
						ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
						dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

						GridPane gridPane = new GridPane();
						gridPane.setHgap(10);
						gridPane.setVgap(10);
						gridPane.setPadding(new Insets(20, 150, 10, 10));

						DatePicker dt = new DatePicker();
						dt.setPrefWidth(120);
						dt.setValue(NOW_LOCAL_DATE());

						gridPane.add(new Label("���� ����������:"), 0, 0);
						gridPane.add(dt, 1, 0);

						dialog.getDialogPane().setContent(gridPane);

						Platform.runLater(() -> dt.requestFocus());
						// Convert the result to
						// clicked.
						dialog.setResultConverter(dialogButton -> {
							if (dialogButton == loginButtonType) {
								return new Pair<>(dt.getValue(), dt.getValue());
							}
							return null;
						});

						Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();

						result.ifPresent(pair -> {
							final Alert alert2 = new Alert(AlertType.CONFIRMATION, "������������ ?", ButtonType.YES,
									ButtonType.NO);
							if (su.sbra.psv.app.sbalert.Msg.setDefaultButton(alert2, ButtonType.NO).showAndWait()
									.orElse(ButtonType.NO) == ButtonType.YES) {
								try {

									DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
									String strDate = dateFormat.format(date);

									CallableStatement callStmt = conn.prepareCall("{ ? = call Calc1c.Reg2DP(?,?)}");
									callStmt.registerOutParameter(1, Types.VARCHAR);
									callStmt.setLong(2, STMT.getSelectionModel().getSelectedItem().getID());
									callStmt.setDate(3, java.sql.Date.valueOf(dt.getValue()));
									callStmt.execute();

									Integer rowid = 1;

									if (!callStmt.getString(1).equals("OK")) {

										Msg.Message("������� ������, ����� ��������� ���� � ���������.");

										Statement sqlStatement = conn.createStatement();
										String readRecordSQL = "SELECT * FROM Z_SB_MMBANK_LOG WHERE sess_id = "
												+ STMT.getSelectionModel().getSelectedItem().getID();
										ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

										DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
										String strDate_ = dateFormat_.format(date);
										String createfolder = System.getenv("TRANSACT_PATH") + "Files/" + strDate_
												+ "_FileID_" + STMT.getSelectionModel().getSelectedItem().getID();

										File file = new File(createfolder);
										if (!file.exists()) {
											if (file.mkdir()) {
											} else {
												Msg.Message("������ ������������� �����! = " + createfolder);
											}
										}

										String path_file = createfolder + "\\" + strDate + "_ERROR.txt";
										PrintWriter writer = new PrintWriter(path_file);
										while (myResultSet.next()) {
											writer.write(rowid + " | " + myResultSet.getTimestamp("recdate") + " | "
													+ myResultSet.getString("ERROR") + " | "
													+ myResultSet.getString("sess_id") + "\r\n");
											rowid++;
										}
										writer.close();
										ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
												createfolder + "\\" + strDate + "_ERROR.txt");
										pb.start();
										myResultSet.close();
										callStmt.close();
										conn.commit();

										InitTable(dateLoad.getValue());

									} else {
										callStmt.close();
										conn.commit();

										OpenAbsForm2(STMT.getSelectionModel().getSelectedItem().getID());

										InitTable(dateLoad.getValue());
									}
								} catch (Exception e) {
									DbUtil.Log_Error(e);
									Main.logger.error(ExceptionUtils.getStackTrace(e));
								}
							}

						});

					} else {
						Msg.Message("����� ��� " + STMT.getSelectionModel().getSelectedItem().getSTATUS());
					}
				}
			} else {
				Msg.Message("�������� ������!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void Delete(ActionEvent event) {
		try {
			if (STMT.getSelectionModel().getSelectedItem() != null) {
				AMRA_STMT_CALC selrow = STMT.getSelectionModel().getSelectedItem();

				final Alert alert = new Alert(AlertType.CONFIRMATION, "������� ���� \"" + selrow.getID() + "\" ?",
						ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

					CallableStatement callStmt = conn.prepareCall("{ call Calc1c.DEL_LOAD(?,?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setLong(2, selrow.getID());
					// catch
					try {
						callStmt.execute();
					} catch (Exception e) {
						DbUtil.Log_Error(e);
						Main.logger.error(ExceptionUtils.getStackTrace(e));
						conn.rollback();
						callStmt.close();
					}

					// return
					String ret = callStmt.getString(1);
					// check
					if (ret != null) {
						// roll back
						conn.rollback();
						Msg.Message(ret);

					} else {
						conn.commit();
						Msg.Message("���� " + selrow.getID() + " ������");
						// reload
						InitTable(dateLoad.getValue());
					}
					// close
					callStmt.close();
				}
			}

		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}

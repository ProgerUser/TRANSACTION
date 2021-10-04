package app.loadamra;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import app.Main;
import app.model.Add_File;
import app.model.Connect;
import app.sbalert.Msg;
import app.swift.ConvConst;
import app.trlist.Tr_Am_View_con;
import app.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Amra_Transact {

	static int rcff = 0;
	static int rcft = 0;

	/**
	 * Удалить дату
	 */
	@FXML
	private void DelDate() {
		try {
			date_load.setValue(null);
			LoadTable("", date_load.getValue());
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@SuppressWarnings("resource")
	private static String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName), getFileCharset(fileName)));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(System.lineSeparator());
			}
			String clobData = sb.toString();
			return clobData;
		} catch (IOException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@FXML
	private TextArea DBMS;

	@FXML
	private TableColumn<Add_File, String> DateFile;
	@FXML
	private TableColumn<Add_File, String> FileName;
	@FXML
	private TableColumn<Add_File, String> StatusFile;
	@FXML
	private TableColumn<Add_File, String> UserFile;
	@FXML
	private TableColumn<Add_File, String> IdFile;
	@FXML
	private TableColumn<Add_File, String> PathFile;
	@FXML
	private TableView<Add_File> load_file;

	@FXML
	private ProgressIndicator progress;
	static PrintWriter writer;
	static int rowline = 0;

	/**
	 * Загрузить файл
	 * 
	 * @param event
	 */
	@FXML
	void Choose(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("eXtensible Markup Language", "*.xml"));
			fileChooser
					.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				DBUtil.dbDisconnect();
				DBUtil.dbConnect();
				Connection conn = DBUtil.conn;
				CallableStatement callStmt = conn.prepareCall("{ ? = call z_sb_create_tr_amra.fn_sess_add(?,?,?)}");
				String reviewContent = null;

				String reviewStr = readFile(file.getParent() + "\\" + file.getName());
				Clob clob = conn.createClob();
				clob.setString(1, reviewStr);
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setClob(2, clob);
				callStmt.setString(3, file.getParent());
				callStmt.setString(4, file.getName());

//				try (DbmsOutputCapture capture = new DbmsOutputCapture(conn)) {
//					//List<String> lines = capture.execute(callStmt);
//					//DBMS.setText(String.join(", ", lines));
//				} catch (Exception e) {
//					Msg.Message(ExceptionUtils.getStackTrace(e));
//				}
				callStmt.execute();

				reviewContent = callStmt.getString(1);

				String[] parts = reviewContent.split(";");
				String part1 = parts[0].trim();
				String part2 = parts[1].trim();
				if (part1.equals("Exception")) {
					Msg.Message(part2);
				} else if (part1.equals("Inserted")) {
					LoadTable("", date_load.getValue());
				} else if (part1.equals("Dublicate")) {
					Msg.Message("Файл был уже загружен!");
				}
			}

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Авто расширение столбцов
	 * 
	 * @param table
	 */
	public static void autoResizeColumns(TableView<?> table) {
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			if (column.getText().equals("sess_id")) {

			} else {
				Text t = new Text(column.getText());
				double max = t.getLayoutBounds().getWidth();
				for (int i = 0; i < table.getItems().size(); i++) {
					if (column.getCellData(i) != null) {
						t = new Text(column.getCellData(i).toString());
						double calcwidth = t.getLayoutBounds().getWidth();
						if (calcwidth > max) {
							max = calcwidth;
						}
					}
				}
				column.setPrefWidth(max + 10.0d);
			}
		});
	}

	@FXML
	private DatePicker date_load;

	@FXML
	private void initialize() {
		// fast date
		new ConvConst().FormatDatePiker(date_load);

		load_file.setEditable(true);

		DateFile.setCellValueFactory(cellData -> cellData.getValue().DateProperty());
		FileName.setCellValueFactory(cellData -> cellData.getValue().FileNameProperty());
		StatusFile.setCellValueFactory(cellData -> cellData.getValue().StatusProperty());
		UserFile.setCellValueFactory(cellData -> cellData.getValue().UserProperty());
		IdFile.setCellValueFactory(cellData -> cellData.getValue().FileIdProperty());
		PathFile.setCellValueFactory(cellData -> cellData.getValue().PathProperty());

		DateFile.setCellFactory(TextFieldTableCell.forTableColumn());
		FileName.setCellFactory(TextFieldTableCell.forTableColumn());
		StatusFile.setCellFactory(TextFieldTableCell.forTableColumn());
		UserFile.setCellFactory(TextFieldTableCell.forTableColumn());
		IdFile.setCellFactory(TextFieldTableCell.forTableColumn());
		PathFile.setCellFactory(TextFieldTableCell.forTableColumn());

		DateFile.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, String>>() {
			@Override
			public void handle(CellEditEvent<Add_File, String> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_Date(t.getNewValue());
			}
		});

		FileName.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, String>>() {
			@Override
			public void handle(CellEditEvent<Add_File, String> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_FileName(t.getNewValue());
			}
		});

		StatusFile.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, String>>() {
			@Override
			public void handle(CellEditEvent<Add_File, String> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_Status(t.getNewValue());
			}
		});
		UserFile.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, String>>() {
			@Override
			public void handle(CellEditEvent<Add_File, String> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_User(t.getNewValue());
			}
		});
		IdFile.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, String>>() {
			@Override
			public void handle(CellEditEvent<Add_File, String> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_FileId(t.getNewValue());
			}
		});
		PathFile.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, String>>() {
			@Override
			public void handle(CellEditEvent<Add_File, String> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_Path(t.getNewValue());
			}
		});

		date_load.setValue(NOW_LOCAL_DATE());

		LoadTable("", date_load.getValue());

		StatusFile.setCellFactory(col -> new TextFieldTableCell<Add_File, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
					if (item.equals("Рассчитан")) {
						setStyle("-fx-text-fill: #7ede80;-fx-font-weight: bold;");
					} else if (item.equals("Разобран")) {
						setStyle("-fx-text-fill: #ebaf2f;-fx-font-weight: bold;");
					} else {
						setStyle("-fx-text-fill: #e65591;-fx-font-weight: bold;");
					}
				}
			}
		});

	}

	/**
	 * Текущий день
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
	 * Показать загруженные строки
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void show_tr(ActionEvent event) throws IOException {
		try {
			if (load_file.getSelectionModel().getSelectedItem() == null) {
				Tr_Am_View_con.Alert("Выберите сначала данные из таблицы!");
			} else {
				Stage stage_ = (Stage) load_file.getScene().getWindow();
				Add_File fn = load_file.getSelectionModel().getSelectedItem();
				Connect.SESSID = fn.get_FileId();
				Stage stage = new Stage();
				Parent root = FXMLLoader.load(Main.class.getResource("/trlist/Transact_Amra_viewer.fxml"));
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Подробно " + fn.get_FileId());
				stage.initOwner(stage_);
				stage.show();
			}
		} catch (IOException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
			Connect.SESSID = null;
		}
	}

	/**
	 * Удалить логи
	 * 
	 * @param event
	 */
	@FXML
	void del_log(ActionEvent event) {
		try {
			if (load_file.getSelectionModel().getSelectedItem() != null) {
				Add_File af = load_file.getSelectionModel().getSelectedItem();

				DBUtil.dbDisconnect();
				DBUtil.dbConnect();

				Connection conn = DBUtil.conn;
				String sql_txt = "delete from z_sb_log_amra where sess_id = ?";
				CallableStatement cs = conn.prepareCall(sql_txt);
				cs.setString(1, af.get_FileId());
				cs.execute();

				Msg.Message("Лог файл с ID = " + af.get_FileId() + " удален!");
				cs.close();
			} else {
				Msg.Message("Выберите строку!");
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void view_fn(ActionEvent event) {
		LoadTable("", date_load.getValue());

		StatusFile.setCellFactory(col -> new TextFieldTableCell<Add_File, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.toString());
					if (item.equals("Рассчитан")) {
						setStyle("-fx-text-fill: #7ede80;-fx-font-weight: bold;");
					} else if (item.equals("Разобран")) {
						setStyle("-fx-text-fill: #ebaf2f;-fx-font-weight: bold;");
					} else {
						setStyle("-fx-text-fill: #e65591;-fx-font-weight: bold;");
					}
				}
			}
		});
	}

	/**
	 * Разбор файла
	 * 
	 * @param event
	 */
	@FXML
	void Load_Transact(ActionEvent event) {
		try {
			if (load_file.getSelectionModel().getSelectedItem() != null) {
				Add_File af = load_file.getSelectionModel().getSelectedItem();
				if (af.get_Status().equals("Загружен")) {

					Date date = new Date();

					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");

					String strDate = dateFormat.format(date);

					DBUtil.dbDisconnect();
					DBUtil.dbConnect();

					Connection conn = DBUtil.conn;

					CallableStatement callStmt = null;
					String reviewContent = null;

					callStmt = conn.prepareCall("{ ? = call z_sb_create_tr_amra.load_pack(?)}");
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setInt(2, Integer.valueOf(af.get_FileId()));
					callStmt.execute();
					reviewContent = callStmt.getString(1);

					String[] parts = reviewContent.split(":");
					String part1 = parts[0].trim();
					String part2 = parts[1].trim();
					Integer rowid = 1;
					if (part1.equals("1")) {

						Msg.Message("Найдены ошибки, скоро откроется файл с описанием.");

						Statement sqlStatement = conn.createStatement();
						String readRecordSQL = "SELECT * FROM Z_SB_LOG_AMRA WHERE sess_id = " + part2 + "";
						ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

						DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
						String strDate_ = dateFormat_.format(date);
						String createfolder = System.getenv("TRANSACT_PATH") + "Files/" + strDate_ + "_SESSID_"
								+ af.get_FileId();

						File file = new File(createfolder);
						if (!file.exists()) {
							if (file.mkdir()) {
								System.out.println("Directory is created!");
							} else {
								System.out.println("Failed to create directory!");
							}
						}

						String path_file = createfolder + "\\" + strDate + "_ERROR.txt";
						PrintWriter writer = new PrintWriter(path_file);
						while (myResultSet.next()) {
							writer.write(rowid + " | " + myResultSet.getTimestamp("recdate") + " | "
									+ myResultSet.getString("desc_") + " | " + myResultSet.getString("sess_id")
									+ "\r\n");
							rowid++;
						}
						writer.close();
						ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
								createfolder + "\\" + strDate + "_ERROR.txt");
						pb.start();
						myResultSet.close();
					} else {

					}
					callStmt.close();
					DBUtil.conn.commit();
					LoadTable("", date_load.getValue());
				} else {
					Msg.Message("Файле уже " + af.get_Status());
				}
			} else {
				Msg.Message("Выберите сначала файл для загрузки");
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		rcff = 0;
		rcft = 0;
	}

	/**
	 * Load table
	 */
	void LoadTable(String SESS_ID, LocalDate dt) {
		try {
			String ldt = "\n";
			String ldt_ = "\n";
			String p_n = "\n";
			if (SESS_ID.equals("")) {

			} else {
				p_n = "and SESS_ID = '" + SESS_ID + "'\n";
			}
			if (dt != null)
				ldt_ = dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

			if (dt != null) {
				ldt = " and trunc(date_time) = to_date('" + ldt_ + "','dd.mm.yyyy')\n";
			}
			String selectStmt = "select sess_id,\n" + "       file_name,\n" + "       date_time,\n"
					+ "       fileclob,\n" + "       case\n" + "         when status = 0 then\n"
					+ "          'Загружен'\n" + "         when status = 1 then\n" + "          'Разобран'\n"
					+ "         when status = 2 then\n" + "          'Рассчитан'\n" + "       end status,\n"
					+ "       path,\n" + "       user_ " + "from Z_SB_FN_SESS_AMRA \n" + "where 1=1" + p_n + ldt
					+ "order by date_time desc";

			PreparedStatement sqlStatement = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = sqlStatement.executeQuery();
			ObservableList<Add_File> trData = FXCollections.observableArrayList();
			while (rs.next()) {
				Add_File adf = new Add_File();
				String date_ = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("date_time"));
				adf.set_FileName(rs.getString("file_name"));
				adf.set_Status(rs.getString("status"));
				adf.set_Date(date_);
				adf.set_User(rs.getString("user_"));
				adf.set_FileId(rs.getString("sess_id"));
				adf.set_Path(rs.getString("path"));
				trData.add(adf);
			}
			load_file.setItems(trData);

			TableFilter<Add_File> tableFilter = TableFilter.forTableView(load_file).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			autoResizeColumns(load_file);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Кодировка
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileCharset(String file) {
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
		} catch (IOException e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * запись в текстовый файл протокола загрузки
	 * 
	 * @param sessid
	 * @param path
	 */
	void Protocol(String sessid, String path) {
		try {
			Connection conn = DBUtil.conn;

			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "SELECT * FROM z_sb_transact_amra_dbt WHERE sess_id = " + sessid + "";
			ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Build Document
			Document document = builder.parse(new File(path));

			// Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();

			// Here comes the root node
			Element root = document.getDocumentElement();
			System.out.println(root.getNodeName());

			// Get all employees
			NodeList nList = root.getElementsByTagName("Трн");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					// Print each employee's detail
					rcff++;
				}
			}
			while (myResultSet.next()) {
				rcft++;
			}
			myResultSet.close();
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Расчет
	 * 
	 * @param event
	 */
	@FXML
	void Calc_Transact(ActionEvent event) {
		try {
			if (load_file.getSelectionModel().getSelectedItem() != null) {
				Add_File af = load_file.getSelectionModel().getSelectedItem();
				if (af.get_Status().equals("Разобран")) {
					Date date = new Date();
					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");

					String strDate = dateFormat.format(date);

					CallableStatement callStmt = null;
					Clob reviewContent = null;

					DBUtil.dbDisconnect();
					DBUtil.dbConnect();

					Connection conn = DBUtil.conn;

					callStmt = conn.prepareCall("{ ? = call z_sb_calc_tr_amra.make(?)}");

					callStmt.registerOutParameter(1, Types.CLOB);
					callStmt.setInt(2, Integer.parseInt(af.get_FileId()));
					
					//_________________________________________
					callStmt.execute();
//					try (DbmsOutputCapture capture = new DbmsOutputCapture(conn)) {
//						//List<String> lines = capture.execute(callStmt);
//						//DBMS.setText(String.join(", ", lines));
//					} catch (Exception e) {
//						Msg.Message(ExceptionUtils.getStackTrace(e));
//					}
					//_________________________________________
					
					reviewContent = callStmt.getClob(1);

					char clobVal[] = new char[(int) reviewContent.length()];
					Reader r = reviewContent.getCharacterStream();
					r.read(clobVal);
					StringWriter sw = new StringWriter();
					sw.write(clobVal);

					DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
					String strDate_ = dateFormat_.format(date);
					String createfolder = System.getenv("TRANSACT_PATH") + "Files/" + strDate_ + "_SESSID_"
							+ af.get_FileId();

					File file = new File(createfolder);
					if (!file.exists()) {
						if (file.mkdir()) {
							System.out.println("Directory is created!");
						} else {
							System.out.println("Failed to create directory!");
						}
					}
					PrintWriter writer = new PrintWriter(createfolder + "\\" + strDate + "_CLOB_.txt");
					writer.write(sw.toString());
					writer.close();
					r.close();

					callStmt.close();
					ProcessBuilder pb = new ProcessBuilder("Notepad.exe", createfolder + "\\" + strDate + "_CLOB_.txt");
					pb.start();

					callStmt.close();
					DBUtil.conn.commit();

					LoadTable("", date_load.getValue());
				} else if (af.get_Status().equals("Загружен")) {
					Msg.Message("Файл не разобран!");
				} else if (af.get_Status().equals("Рассчитан")) {
					Msg.Message("Файл уже " + af.get_Status() + "!");
				}
			} else {
				Msg.Message("Выберите строку из таблицы!");
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}
}
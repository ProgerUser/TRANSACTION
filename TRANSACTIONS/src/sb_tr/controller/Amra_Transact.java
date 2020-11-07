package sb_tr.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sb_tr.Main;
import sb_tr.model.Add_File;
import sb_tr.model.Amra_Trans;
import sb_tr.model.Connect;
import sb_tr.model.FN_SESS_AMRA;
import sb_tr.model.TerminalDAO;
import sb_tr.util.DBUtil;
import trlist.Tr_Am_View_con;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Amra_Transact {
	static String sql = "{ ? = call z_sb_create_tr_amra.load_pack(?)}";

	static String sql_calc = "{ ? = call z_sb_calc_tr_amra.make(?)}";

	static String sql_snsess = "{ ? = call z_sb_create_tr_amra.fn_sess_add(?,?,?)}";

	static int rcff = 0;
	static int rcft = 0;

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
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return null;
	}

	@FXML
	private Button open_new;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button browse;

	@FXML
	private BorderPane ap1;
	
	@FXML
	private AnchorPane ap;
	
	@FXML
	private Button import_;

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

	// For MultiThreading
	private Executor exec;

	@FXML
	private ProgressIndicator progress;

	static PrintWriter writer;

	static int rowline = 0;

	@FXML
	void Choose(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("eXtensible Markup Language",
					"*.xml")/*
							 * , new ExtensionFilter("Text Files", "*.txt")
							 */);
			fileChooser.setInitialDirectory(
					new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				// textbox.setText(file.getParent() + "::_" + file.getName());

				/*
				 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
				 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
				 * + "");
				 */

				DBUtil.dbDisconnect();
				DBUtil.dbConnect();

				Connection conn = DBUtil.conn;
				CallableStatement callStmt = null;
				String reviewContent = null;
				callStmt = conn.prepareCall(sql_snsess);
				String reviewStr = readFile(file.getParent() + "\\" + file.getName());
				Clob clob = conn.createClob();
				clob.setString(1, reviewStr);
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setClob(2, clob);
				callStmt.setString(3, file.getParent());
				callStmt.setString(4, file.getName());
				callStmt.execute();
				reviewContent = callStmt.getString(1);

				String[] parts = reviewContent.split(";");
				String part1 = parts[0].trim();
				String part2 = parts[1].trim();
				if (part1.equals("Exception")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText(part2);
					alert.showAndWait();
				} else if (part1.equals("Inserted")) {
					// Get all Employees information
					ObservableList<Add_File> empData = TerminalDAO.add_file(/* part2 */"", date_load.getValue());
					// Populate Employees on TableView
					populate_fn_sess(empData);
					autoResizeColumns(load_file);

					/*
					 * Alert alert = new Alert(Alert.AlertType.INFORMATION); Stage stage = (Stage)
					 * alert.getDialogPane().getScene().getWindow(); stage.getIcons().add(new
					 * Image("terminal.png")); alert.setTitle("Внимание");
					 * alert.setHeaderText(null); alert.setContentText("Добавлен файл с ID = " +
					 * part2); alert.showAndWait();
					 */
				} else if (part1.equals("Dublicate")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Файл был уже загружен!");
					alert.showAndWait();
				}
			}

		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private void populate_fn_sess(ObservableList<Add_File> trData) {
		// Set items to the employeeTable
		load_file.setItems(trData);
	}

	public static void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("sess_id")) {

			} else {
				// Minimal width = columnheader
				Text t = new Text(column.getText());
				double max = t.getLayoutBounds().getWidth();
				for (int i = 0; i < table.getItems().size(); i++) {
					// cell must not be empty
					if (column.getCellData(i) != null) {
						t = new Text(column.getCellData(i).toString());
						double calcwidth = t.getLayoutBounds().getWidth();
						// remember new max-width
						if (calcwidth > max) {
							max = calcwidth;
						}
					}
				}
				// set the new max-widht with some extra space
				column.setPrefWidth(max + 10.0d);
			}
		});
	}

	@FXML
	private DatePicker date_load;

	@FXML
	private void initialize() {

		load_file.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

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
		ObservableList<Add_File> empData = TerminalDAO.add_file("", date_load.getValue());
		// Populate Employees on TableView
		populate_fn_sess(empData);
		autoResizeColumns(load_file);

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
						setStyle("-fx-background-color: #7ede80;" + "-fx-border-color:black;"
								+ " -fx-border-width :  1 1 1 1 ");
					} else if (item.equals("Разобран")) {
						setStyle("-fx-background-color: #ebaf2f;" + "-fx-border-color:black;"
								+ " -fx-border-width :  1 1 1 1 ");
					} else {
						setStyle("-fx-background-color: #e65591;" + "-fx-border-color:black;"
								+ " -fx-border-width :  1 1 1 1 ");
					}
				}
			}
		});

	}

	public static final LocalDate NOW_LOCAL_DATE() {
		String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate localDate = LocalDate.parse(date, formatter);
		return localDate;
	}

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
				Parent root;
				root = FXMLLoader.load(Main.class.getResource("view/Transact_Amra_viewer.fxml"));
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Подробно " + fn.get_FileId());
				stage.initOwner(stage_);
				stage.show();
			}
		} catch (IOException e) {
			Tr_Am_View_con.Alert(e.getMessage());
			Connect.SESSID = null;
		}
	}

	@FXML
	void del_log(ActionEvent event) {
		try {
			if (load_file.getSelectionModel().getSelectedItem() != null) {
				Add_File af = load_file.getSelectionModel().getSelectedItem();
				/*
				 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
				 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
				 * + "");
				 */

				DBUtil.dbDisconnect();
				DBUtil.dbConnect();

				Connection conn = DBUtil.conn;
				String sql_txt = "delete from z_sb_log_amra where sess_id = ?";
				CallableStatement cs = conn.prepareCall(sql_txt);
				cs.setString(1, af.get_FileId());
				cs.execute();

				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Лог файл с ID = " + af.get_FileId() + " удален!");
				alert.showAndWait();
				cs.close();
				// conn.close();
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Выберите строку!");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void view_fn(ActionEvent event) {
		LocalDate date = date_load.getValue();
		ObservableList<Add_File> empData = TerminalDAO.add_file_d(date);
		// Populate Employees on TableView
		populate_fn_sess(empData);
		autoResizeColumns(load_file);
	}

	/*
	 * @FXML void Calc_Transact(ActionEvent event) { progress.setVisible(true);
	 * Task<Add_File> task = new Task<Add_File>() {
	 * 
	 * @Override public Add_File call() throws Exception { Calc_Transact_(); return
	 * null; } }; task.setOnFailed(e -> print_mess(task.getException().toString()));
	 * // task.setOnSucceeded(e -> Load_Transact()); exec.execute(task);
	 * progress.setVisible(false); }
	 */
	/*
	 * @FXML void Load_Transact(ActionEvent event) { progress.setVisible(true);
	 * Task<Add_File> task = new Task<Add_File>() {
	 * 
	 * @Override public Add_File call() throws Exception { Load_Transact_(); return
	 * null; } }; task.setOnFailed(e -> print_mess(task.getException().toString()));
	 * // task.setOnSucceeded(e -> Load_Transact()); exec.execute(task);
	 * progress.setVisible(false); }
	 */
	void print_mess(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText(text);
				alert.showAndWait();
			}
		});
	}

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
					/*
					 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
					 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
					 * + "");
					 */
					CallableStatement callStmt = null;
					String reviewContent = null;

					Integer a = Integer.valueOf(af.get_FileId());
					callStmt = conn.prepareCall(sql);
					callStmt.registerOutParameter(1, Types.VARCHAR);
					callStmt.setInt(2, Integer.valueOf(af.get_FileId()));
					callStmt.execute();
					reviewContent = callStmt.getString(1);

					String[] parts = reviewContent.split(":");
					String part1 = parts[0].trim();
					String part2 = parts[1].trim();
					Integer rowid = 1;
					if (part1.equals("1")) {

						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("terminal.png"));
						alert.setTitle("Внимание");
						alert.setHeaderText(null);
						alert.setContentText("Найдены ошибки, скоро откроется файл с описанием.");
						alert.showAndWait();

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
						// --------------------------------------
						// Protocol(part2, af.get_Path() + "\\" + af.get_FileName());
						// --------------------------------------
						/*
						 * Alert alert = new Alert(Alert.AlertType.INFORMATION); Stage stage = (Stage)
						 * alert.getDialogPane().getScene().getWindow(); stage.getIcons().add(new
						 * Image("terminal.png")); alert.setTitle("Внимание");
						 * alert.setHeaderText(null);
						 * alert.setContentText("Загрузка прошла успешна. Можете перейти к расчету");
						 * alert.showAndWait();
						 */
					}
					callStmt.close();
					// conn.close();

					ObservableList<Add_File> empData = TerminalDAO.add_file(/* af.get_FileId() */"",
							date_load.getValue());
					// Populate Employees on TableView
					populate_fn_sess(empData);
					autoResizeColumns(load_file);
				} else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Файле уже " + af.get_Status());
					alert.showAndWait();

				}
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Выберите сначала файл для загрузки");
				alert.showAndWait();

			}
		} catch (SQLException | IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		rcff = 0;
		rcft = 0;
	}

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
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return null;
	}

	// запись в текстовый файл протокола загрузки
	void Protocol(String sessid, String path) {
		try {
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
			String strDate = dateFormat.format(date);
			Connection conn = DBUtil.conn;
			/*
			 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
			 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
			 * + "");
			 */
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
					Element eElement = (Element) node;
					rcff++;
				}
			}
			while (myResultSet.next()) {
				rcft++;
			}
			myResultSet.close();
		} catch (SQLException | IOException | ParserConfigurationException | SAXException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void Calc_Transact(ActionEvent event) {
		try {
			if (load_file.getSelectionModel().getSelectedItem() != null) {
				Add_File af = load_file.getSelectionModel().getSelectedItem();
				String aa = af.get_Status();
				if (af.get_Status().equals("Разобран")) {
					Date date = new Date();

					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");

					String strDate = dateFormat.format(date);

					CallableStatement callStmt = null;
					Clob reviewContent = null;

					DBUtil.dbDisconnect();
					DBUtil.dbConnect();

					Connection conn = DBUtil.conn;
					/*
					 * Connection conn;
					 * 
					 * conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ +
					 * "/" + Connect.userPassword_ + "@" + Connect.connectionURL_ + "");
					 */
					callStmt = conn.prepareCall(sql_calc);

					callStmt.registerOutParameter(1, Types.CLOB);
					callStmt.setInt(2, Integer.parseInt(af.get_FileId()));

					callStmt.execute();

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

					/*
					 * Alert alert = new Alert(Alert.AlertType.INFORMATION); Stage stage = (Stage)
					 * alert.getDialogPane().getScene().getWindow(); stage.getIcons().add(new
					 * Image("terminal.png")); alert.setTitle("Внимание");
					 * alert.setHeaderText(null); alert.setContentText("Расчет прошел успешно!");
					 * alert.showAndWait();
					 */
					callStmt.close();
					// conn.close();
					ObservableList<Add_File> empData = TerminalDAO.add_file(/* af.get_FileId() */"",
							date_load.getValue());
					// Populate Employees on TableView
					populate_fn_sess(empData);
					autoResizeColumns(load_file);
				} else if (af.get_Status().equals("Загружен")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Файл не разобран!");
					alert.showAndWait();
				} else if (af.get_Status().equals("Рассчитан")) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("Внимание");
					alert.setHeaderText(null);
					alert.setContentText("Файл уже " + af.get_Status() + "!");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Выберите строку из таблицы!");
				alert.showAndWait();
			}
		} catch (SQLException | IOException e) {
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
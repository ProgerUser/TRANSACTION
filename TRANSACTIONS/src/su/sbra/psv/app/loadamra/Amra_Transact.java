package su.sbra.psv.app.loadamra;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.LongStringConverter;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.report.Report;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.swift.ConvConst;
import su.sbra.psv.app.trlist.Tr_Am_View_con;
import su.sbra.psv.app.utils.DbUtil;

public class Amra_Transact {

	static int rcff = 0;
	static int rcft = 0;
	
	//--------------------
    @FXML
    private TableView<TrLog> LogTr;
    @FXML
    private TableColumn<TrLog, LocalDateTime> RECDATE;
    @FXML
    private TableColumn<TrLog, LocalDateTime> PAYDATE;
    @FXML
    private TableColumn<TrLog, String> DESC_;
    @FXML
    private TableColumn<TrLog, String> DEB_CRED;
	//--------------------
	@FXML
	private CheckBox DBMS;
	@FXML
	private TableColumn<Add_File, LocalDateTime> DATE_TIME;
	@FXML
	private TableColumn<Add_File, LocalDate> FD;
	@FXML
	private TableColumn<Add_File, String> FILE_NAME;
	@FXML
	private TableColumn<Add_File, String> STATUS;
	@FXML
	private TableColumn<Add_File, String> USER_;
	@FXML
	private TableColumn<Add_File, Long> SESS_ID;
	@FXML
	private TableColumn<Add_File, String> PATH;
	@FXML
	private TableView<Add_File> load_file;
	@FXML
	private VBox Root;
    @FXML
    private ProgressIndicator PB;
    
    @FXML
    private Button DelLogB;
    
	static PrintWriter writer;
	static int rowline = 0;

	// Connection
	public Connection conn = null;
		
	void dbConnect() throws ClassNotFoundException, SQLException, UnknownHostException {
		// Setting Oracle JDBC Driver
		Class.forName("oracle.jdbc.OracleDriver");
		Main.logger = Logger.getLogger(getClass());
		// Establish the Oracle Connection using Connection String

		Properties props = new Properties();
		props.setProperty("password", Connect.userPassword_);
		props.setProperty("user", Connect.userID_);
		props.put("v$session.osuser", System.getProperty("user.name").toString());
		props.put("v$session.action", "LoadTransact");
		props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
		props.put("v$session.program", getClass().getName());
		conn = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
		conn.setAutoCommit(false);
	}

	public void dbDisconnect() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.rollback();
			conn.close();
		}
	}
	
	
	/**
	 * Удалить дату
	 */
	@FXML
	private void DelDate() {
		try {
			// ----------------------------------
			PB.setVisible(true);
			Root.setDisable(true);
			Task<Object> task = new Task<Object>() {
				@Override
				public Object call() throws Exception {
					try {
						// --------------------------------------
						date_load.setValue(null);
						LoadTable("", date_load.getValue());
						// ----------------------------------
					} catch (Exception e) {
						ShowMes(ExceptionUtils.getStackTrace(e));
					}
					return null;
				}
			};
			task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
			task.setOnSucceeded(e -> {
				PB.setVisible(false);
				Root.setDisable(false);
			});
			exec.execute(task);
// --------------------------------------
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	
	@FXML
	void OpenAbs() {
		try {
			Add_File sel = load_file.getSelectionModel().getSelectedItem();
			if (sel != null) {
				// ----------------------------------
				PB.setVisible(true);
				Root.setDisable(true);
				Task<Object> task = new Task<Object>() {
					@Override
					public Object call() throws Exception {
						try {
							// --------------------------------------
							String call = "ifrun60.exe I:/KERNEL/OPERLIST.fmx " + Connect.userID_ + "/"
									+ Connect.userPassword_
									+ "@ODB WHERE=\" ITRNNUM in (select p.kindpayment from Z_SB_POSTDOC_AMRA_DBT p where p.sess_id = "
									+ sel.getSESS_ID() + ") \"";
							ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
							System.out.println(call);
							builder.redirectErrorStream(true);
							Process p = builder.start();
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
								System.out.println(line);
							}
							// ----------------------------------
						} catch (Exception e) {
							ShowMes(ExceptionUtils.getStackTrace(e));
						}
						return null;
					}
				};
				task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
				task.setOnSucceeded(e -> {
					PB.setVisible(false);
					Root.setDisable(false);
				});
				exec.execute(task);
				// --------------------------------------
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Удалить file
	 */
	@FXML
	private void DeleteLoad() {
		try {
			Add_File sel = load_file.getSelectionModel().getSelectedItem();
			if (sel != null) {

				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить \"" + sel.getFILE_NAME() + "\" ?",
						ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					CallableStatement cl = conn.prepareCall("{call Z_SB_CREATE_TR_AMRA.DELETELOAD(?,?)}");
					cl.registerOutParameter(1, Types.VARCHAR);
					cl.setLong(2, sel.getSESS_ID());
					cl.execute();
					if (cl.getString(1) != null) {
						conn.rollback();
						Msg.Message(cl.getString(1));
					} else {
						conn.commit();
						Msg.Message("Файл " + sel.getFILE_NAME() + " удален!");
					}
					cl.close();
					LoadTable("", date_load.getValue());
					LoadTableError(sel.getSESS_ID());
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	/**
	 * Загрузить файл
	 * 
	 * @param event
	 */
	@FXML
	void Choose(ActionEvent event) {
		try {
			
			if (!Connect.userID_.equals("AMRA_IMPORT")) {
				Msg.Message("Пользователь не AMRA_IMPORT!");
				return;
			}
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("XML File", "*.xml"));
			fileChooser
					.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {

				final Alert alert = new Alert(AlertType.CONFIRMATION, "Загрузить \"" + file.getName() + "\" ?",
						ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					
				//----------------------------------
					PB.setVisible(true);
					Root.setDisable(true);
					Task<Object> task = new Task<Object>() {
						@Override
						public Object call() throws Exception {
							try {
								
								
								//--------------------------------------
								CallableStatement callStmt = conn
										.prepareCall("{ ? = call z_sb_create_tr_amra.fn_sess_add(?,?,?)}");
								String reviewContent = null;

								String reviewStr = readFile(file.getParent() + "\\" + file.getName());
								Clob clob = conn.createClob();
								clob.setString(1, reviewStr);
								callStmt.registerOutParameter(1, Types.VARCHAR);
								callStmt.setClob(2, clob);
								callStmt.setString(3, file.getParent());
								callStmt.setString(4, file.getName());

								// _________________________________________
								if (DBMS.isSelected()) {
									try (DbmsOutputCapture capture = new DbmsOutputCapture(conn)) {
										List<String> lines = capture.execute(callStmt);
										System.out.println(lines);
										Msg.Message(String.join("", lines));
									} catch (Exception e) {
										DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
									}
								} else {
									callStmt.execute();
								}
								// _________________________________________

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
								callStmt.close();
								//----------------------------------
							} catch (Exception e) {
								ShowMes(ExceptionUtils.getStackTrace(e));
							}
							return null;
						}
					};
					task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
					task.setOnSucceeded(e -> {
						PB.setVisible(false);
						Root.setDisable(false);
					});
					exec.execute(task);
					//---------------
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Error message in new thread
	 * 
	 * @param error
	 */
	void ShowMes(String error) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Msg.Message(error);
			}
		});

	}
	/**
	 * Авто расширение столбцов
	 * 
	 * @param table
	 */
	public static void autoResizeColumns(TableView<?> table) {
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			if (column.getText().equals("1Путь загрузки")) {

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
				column.setPrefWidth(max + 20.0d);
			}
		});
	}

	@FXML
	private DatePicker date_load;

	
	/**
	 * Формат <br>
	 * dd.MM.yyyy <br>
	 * HH:mm:ss
	 */
	public static final DateTimeFormatter DateTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	/**
	 * Формат <br>
	 * dd.MM.yyyy <br>
	 */
	public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	private Executor exec;
	
	@FXML
	private void initialize() {
		try {

			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});
			//
		dbConnect();
		// fast date
		new ConvConst().FormatDatePiker(date_load);

		load_file.setEditable(true);

		DATE_TIME.setCellValueFactory(cellData -> cellData.getValue().DATE_TIMEProperty());
		FD.setCellValueFactory(cellData -> cellData.getValue().FDProperty());
		FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().FILE_NAMEProperty());
		STATUS.setCellValueFactory(cellData -> cellData.getValue().STATUSProperty());
		SESS_ID.setCellValueFactory(cellData -> cellData.getValue().SESS_IDProperty().asObject());
		PATH.setCellValueFactory(cellData -> cellData.getValue().PATHProperty());
		USER_.setCellValueFactory(cellData -> cellData.getValue().USER_Property());
		
		RECDATE.setCellValueFactory(cellData -> cellData.getValue().RECDATEProperty());
		PAYDATE.setCellValueFactory(cellData -> cellData.getValue().PAYDATEProperty());
		DESC_.setCellValueFactory(cellData -> cellData.getValue().DESC_Property());
		DEB_CRED.setCellValueFactory(cellData -> cellData.getValue().DEB_CREDProperty());
		
		
		
		SESS_ID.setCellFactory(TextFieldTableCell.<Add_File, Long>forTableColumn(new LongStringConverter()));
		SESS_ID.setOnEditCommit(new EventHandler<CellEditEvent<Add_File, Long>>() {
			@Override
			public void handle(CellEditEvent<Add_File, Long> t) {
				((Add_File) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSESS_ID(t.getNewValue());
			}
		});
		
		date_load.setValue(NOW_LOCAL_DATE());

		LoadTable("", date_load.getValue());

		STATUS.setCellFactory(col -> new TextFieldTableCell<Add_File, String>() {
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
		DATE_TIME.setCellFactory(column -> {
			TableCell<Add_File, LocalDateTime> cell = new TableCell<Add_File, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null) {
							setText(DateTimeFormat.format(item));
						}
					}
				}
			};
			return cell;
		});
		
		RECDATE.setCellFactory(column -> {
			TableCell<TrLog, LocalDateTime> cell = new TableCell<TrLog, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null) {
							setText(DateTimeFormat.format(item));
						}
					}
				}
			};
			return cell;
		});
		
		PAYDATE.setCellFactory(column -> {
			TableCell<TrLog, LocalDateTime> cell = new TableCell<TrLog, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null) {
							setText(DateTimeFormat.format(item));
						}
					}
				}
			};
			return cell;
		});
		
		FD.setCellFactory(column -> {
			TableCell<Add_File, LocalDate> cell = new TableCell<Add_File, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null) {
							setText(DateFormat.format(item));
						}
					}
				}
			};
			return cell;
		});
		
		//sel row
		load_file.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
			Add_File sel = load_file.getSelectionModel().getSelectedItem();
			if (sel != null) {
				LoadTableError(sel.getSESS_ID());
			}
		});
		
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
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
				Connect.SESSID = String.valueOf(fn.getSESS_ID());
				Stage stage = new Stage();
				Parent root = FXMLLoader.load(Main.class.getResource("/su/sbra/psv/app/trlist/Transact_Amra_viewer.fxml"));
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Подробно " + fn.getSESS_ID());
				stage.initOwner(stage_);
				stage.show();
			}
		} catch (IOException e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			Add_File sel = load_file.getSelectionModel().getSelectedItem();
			if (sel != null) {
				final Alert alert = new Alert(AlertType.CONFIRMATION,
						"Удалить лог \"" + sel.getFILE_NAME() + "\" ?", ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					String sql_txt = "DELETE FROM Z_SB_LOG_AMRA WHERE SESS_ID = ?";
					CallableStatement cs = conn.prepareCall(sql_txt);
					cs.setLong(1, sel.getSESS_ID());
					cs.execute();
					cs.close();
					LoadTableError(sel.getSESS_ID());
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void view_fn(ActionEvent event) {
		LoadTable("", date_load.getValue());
		STATUS.setCellFactory(col -> new TextFieldTableCell<Add_File, String>() {
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
			if (!Connect.userID_.equals("AMRA_IMPORT")) {
				Msg.Message("Пользователь не AMRA_IMPORT!");
				return;
			}
			Add_File sel = load_file.getSelectionModel().getSelectedItem();

			if (sel != null) {

				int SelInd = load_file.getSelectionModel().getSelectedIndex();

				final Alert alert = new Alert(AlertType.CONFIRMATION,
						"Разобрать файл \"" + sel.getFILE_NAME() + "\" ?", ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

					if (sel.getSTATUS().equals("Загружен")) {
						// ----------------------------------
						PB.setVisible(true);
						Root.setDisable(true);
						Task<Object> task = new Task<Object>() {
							@Override
							public Object call() throws Exception {
								try {
									// --------------------------------------
									Date date = new Date();
									DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
									String strDate = dateFormat.format(date);

									String reviewContent = null;

									CallableStatement callStmt = conn
											.prepareCall("{ ? = call z_sb_create_tr_amra.load_pack(?)}");
									callStmt.registerOutParameter(1, Types.VARCHAR);
									callStmt.setLong(2, sel.getSESS_ID());

									// _________________________________________
									if (DBMS.isSelected()) {
										try (DbmsOutputCapture capture = new DbmsOutputCapture(conn)) {
											List<String> lines = capture.execute(callStmt);
											Msg.Message(String.join(", ", lines + "\r\n"));
										} catch (Exception e) {
											DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
										}
									} else {
										callStmt.execute();
									}
									// _________________________________________

									reviewContent = callStmt.getString(1);

									String[] parts = reviewContent.split(":");
									String part1 = parts[0].trim();
									String part2 = parts[1].trim();
									Integer rowid = 1;
									if (part1.equals("1")) {

										Msg.Message("Найдены ошибки, скоро откроется файл с описанием.");

										Statement sqlStatement = conn.createStatement();
										String readRecordSQL = "SELECT * FROM Z_SB_LOG_AMRA WHERE SESS_ID = " + part2
												+ "";
										ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);

										DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
										String strDate_ = dateFormat_.format(date);
										String createfolder = System.getenv("TRANSACT_PATH") + "Files/" + strDate_
												+ "_SESSID_" + sel.getSESS_ID();

										File file = new File(createfolder);
										if (!file.exists()) {
											if (file.mkdir()) {
											} else {
												Msg.Message("Failed to create directory! = " + createfolder);
											}
										}

										String path_file = createfolder + "\\" + strDate + "_ERROR.txt";
										PrintWriter writer = new PrintWriter(path_file);
										while (myResultSet.next()) {
											writer.write(rowid + " | " + myResultSet.getTimestamp("recdate") + " | "
													+ myResultSet.getString("desc_") + " | "
													+ myResultSet.getString("sess_id") + "\r\n");
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
									conn.commit();
									LoadTable("", date_load.getValue());
									LoadTableError(sel.getSESS_ID());
									// Select Table Row
									Platform.runLater(() -> {
										load_file.requestFocus();
										load_file.getSelectionModel().select(SelInd);
										load_file.scrollTo(SelInd);
									});
									// ----------------------------------
								} catch (Exception e) {
									ShowMes(ExceptionUtils.getStackTrace(e));
								}
								return null;
							}
						};
						task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
						task.setOnSucceeded(e -> {
							PB.setVisible(false);
							Root.setDisable(false);
						});
						exec.execute(task);
						// --------------------------------------
					} else {
						Msg.Message("Файле уже " + sel.getSTATUS());
					}
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			String selectStmt = "SELECT SESS_ID,\r\n"
					+ "       FILE_NAME,\r\n"
					+ "       DATE_TIME,\r\n"
					//+ "       FILECLOB,\r\n"
					+ "       CASE\r\n"
					+ "         WHEN STATUS = 0 THEN\r\n"
					+ "          'Загружен'\r\n"
					+ "         WHEN STATUS = 1 THEN\r\n"
					+ "          'Разобран'\r\n"
					+ "         WHEN STATUS = 2 THEN\r\n"
					+ "          'Рассчитан'\r\n"
					+ "       END STATUS,\r\n"
					+ "       PATH,\r\n"
					+ "       USER_,\r\n"
					+ "       cast(z_sb_fn_sess_getdate_clob(SESS_ID) as date) FD\r\n"
					+ "  FROM Z_SB_FN_SESS_AMRA\r\n"
					+ " WHERE 1 = 1 "+p_n+ldt+"\r\n"
					+ " ORDER BY DATE_TIME DESC\r\n"
					+ "";
			System.out.println(selectStmt);
			PreparedStatement prp = conn.prepareStatement(selectStmt);
			ResultSet rs = prp.executeQuery();
			ObservableList<Add_File> trData = FXCollections.observableArrayList();
			
			while (rs.next()) {
				Add_File list = new Add_File();
				
				list.setSESS_ID(rs.getLong("SESS_ID"));
				list.setFILE_NAME(rs.getString("FILE_NAME"));
				list.setDATE_TIME((rs.getDate("DATE_TIME") != null)
						? LocalDateTime.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("DATE_TIME")), DateTimeFormat)
						: null);
//				if (rs.getClob("FILECLOB") != null) {
//					list.setFILECLOB(new ConvConst().ClobToString(rs.getClob("FILECLOB")));
//				}
				list.setSTATUS(rs.getString("STATUS"));
				list.setPATH(rs.getString("PATH"));
				list.setUSER_(rs.getString("USER_"));
				list.setFD((rs.getDate("FD") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("FD")), DateFormat)
						: null);
				trData.add(list);
			}
			rs.close();
			prp.close();
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
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Load table
	 */
	void LoadTableError(Long SessId) {
		try {
			String selectStmt = "SELECT CAST(RECDATE AS DATE) RECDATE, PAYDATE, DESC_, SESS_ID, DEB_CRED\r\n"
					+ "  FROM Z_SB_LOG_AMRA where SESS_ID = ? order by RECDATE";
			PreparedStatement prp = conn.prepareStatement(selectStmt);
			prp.setLong(1, SessId);
			ResultSet rs = prp.executeQuery();
			ObservableList<TrLog> trData = FXCollections.observableArrayList();
			
			int row = 0;
			while (rs.next()) {
				row++;
				TrLog list = new TrLog();
				list.setRECDATE((rs.getDate("RECDATE") != null)
						? LocalDateTime.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("RECDATE")), DateTimeFormat)
						: null);
				list.setPAYDATE((rs.getDate("PAYDATE") != null)
						? LocalDateTime.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("PAYDATE")), DateTimeFormat)
						: null);
				list.setDESC_(rs.getString("DESC_"));
				list.setSESS_ID(rs.getLong("SESS_ID"));
				list.setDEB_CRED(rs.getString("DEB_CRED"));

				trData.add(list);
			}
			rs.close();
			prp.close();
			
			if(row==0) {
				DelLogB.setDisable(true);
			}else {
				DelLogB.setDisable(false);
			}
			LogTr.setItems(trData);

			TableFilter<TrLog> tableFilter = TableFilter.forTableView(LogTr).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			autoResizeColumns(LogTr);
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "SELECT * FROM Z_SB_TRANSACT_AMRA_DBT WHERE SESS_ID = " + sessid + "";
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
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}


	public static boolean ReportsWin = true;

	@FXML
	void AltPrint(ActionEvent event) {
		try {
			if (ReportsWin) {
				ReportsWin = false;
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/su/sbra/psv/app/report/Report.fxml"));

				Report controller = new Report();
				controller.setId(666l);
				loader.setController(controller);

				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("(" + controller.getId() + ") Печать");
				stage.initModality(Modality.WINDOW_MODAL);
				
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						controller.dbDisconnect();
						ReportsWin = true;
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
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
			if (!Connect.userID_.equals("AMRA_IMPORT")) {
				Msg.Message("Пользователь не AMRA_IMPORT!");
				return;
			}
			Add_File sel = load_file.getSelectionModel().getSelectedItem();

			if (sel != null) {

				final Alert alert = new Alert(AlertType.CONFIRMATION,
						"Сформировать документы \"" + sel.getFILE_NAME() + "\" ?", ButtonType.YES, ButtonType.NO);

				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

					if (sel.getSTATUS().equals("Разобран")) {
						// ----------------------------------
						PB.setVisible(true);
						Root.setDisable(true);
						Task<Object> task = new Task<Object>() {
							@Override
							public Object call() throws Exception {
								try {
									// --------------------------------------
									Date date = new Date();
									DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
									String strDate = dateFormat.format(date);

									CallableStatement callStmt = null;
									Clob reviewContent = null;

									callStmt = conn.prepareCall("{ ? = call z_sb_calc_tr_amra.make(?)}");

									callStmt.registerOutParameter(1, Types.CLOB);
									callStmt.setLong(2, sel.getSESS_ID());

									// _________________________________________
									if (DBMS.isSelected()) {
										try (DbmsOutputCapture capture = new DbmsOutputCapture(conn)) {
											List<String> lines = capture.execute(callStmt);
											Msg.Message(String.join(", ", lines + "\r\n"));
										} catch (Exception e) {
											DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
										}
									} else {
										callStmt.execute();
									}
									// _________________________________________

									reviewContent = callStmt.getClob(1);

									char clobVal[] = new char[(int) reviewContent.length()];
									Reader r = reviewContent.getCharacterStream();
									r.read(clobVal);
									StringWriter sw = new StringWriter();
									sw.write(clobVal);

									DateFormat dateFormat_ = new SimpleDateFormat("dd.MM.yyyy HH");
									String strDate_ = dateFormat_.format(date);
									String createfolder = System.getenv("TRANSACT_PATH") + "Files/" + strDate_
											+ "_SESSID_" + sel.getSESS_ID();

									File file = new File(createfolder);
									if (!file.exists()) {
										if (file.mkdir()) {
										} else {
											Msg.Message("Failed to create directory! = " + createfolder);
										}
									}

									PrintWriter writer = new PrintWriter(createfolder + "\\" + strDate + "_CLOB_.txt");
									writer.write(sw.toString());
									writer.close();
									r.close();

									callStmt.close();
									ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
											createfolder + "\\" + strDate + "_CLOB_.txt");
									pb.start();

									callStmt.close();
									// commit
									conn.commit();

									LoadTable("", date_load.getValue());
									LoadTableError(sel.getSESS_ID());
									// ----------------------------------
								} catch (Exception e) {
									ShowMes(ExceptionUtils.getStackTrace(e));
								}
								return null;
							}
						};
						task.setOnFailed(e -> ShowMes(task.getException().getMessage()));
						task.setOnSucceeded(e -> {
							PB.setVisible(false);
							Root.setDisable(false);
						});
						exec.execute(task);
						// --------------------------------------
					} else if (sel.getSTATUS().equals("Загружен")) {
						Msg.Message("Файл не разобран!");
					} else if (sel.getSTATUS().equals("Рассчитан")) {
						Msg.Message("Файл уже " + sel.getSTATUS() + "!");
					}
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e); Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
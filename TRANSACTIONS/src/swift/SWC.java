package swift;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;
import com.google.common.io.Files;
import com.prowidesoftware.swift.model.field.Field32A;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import app.model.Connect;
import javafx.animation.KeyValue.Type;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import sbalert.Msg;

/**
 * SWIFT
 * 
 * 07.11.2020
 * 
 * @author Said
 *
 */
public class SWC {

	/**
	 * Не используется, интервал обновления
	 */
	@FXML
	private ComboBox<String> TMREFR;

	/**
	 * Выделена ли строка
	 */
	@FXML
	private TableColumn<SWIFT_FILES, Boolean> CHK;

	/**
	 * Валюта
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> CUR;

	/**
	 * Сумма документа, если MT103...
	 */
	@FXML
	private TableColumn<SWIFT_FILES, Double> SUMM;

	/**
	 * Название операции
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> MTNAME;

	/**
	 * MT.., из базы
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> MTTYPE;

	/**
	 * Дата создания файла
	 */
	@FXML
	private TableColumn<SWIFT_FILES, LocalDateTime> DT_CH;

	/**
	 * Дата документа
	 */
	@FXML
	private TableColumn<SWIFT_FILES, LocalDate> DOCDATE;

	/**
	 * Название файла
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> FILE_NAME;

	/**
	 * Таблица
	 */
	@FXML
	private TableView<SWIFT_FILES> STMT;

	/**
	 * Возврат суммы документа , если MT103...пока
	 * 
	 * @param path
	 * @return
	 */
	Double getMtAmount(String path) {
		Double ret = null;
		try {
			InputStream inputstream = new FileInputStream(path);
			AbstractMT msg = AbstractMT.parse(inputstream);
			if (msg.isType(103)) {
				MT103 mt = (MT103) msg;
				Field32A f = mt.getField32A();
				ret = f.getAmountAsNumber().doubleValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
		return (ret == null) ? 0 : ret;
	}

	/**
	 * Возврат Валюты , если MT103...пока
	 * 
	 * @param path
	 * @return
	 */
	String getMtCur(String path) {
		String ret = null;
		try {
			InputStream inputstream = new FileInputStream(path);
			AbstractMT msg = AbstractMT.parse(inputstream);
			if (msg.isType(103)) {
				MT103 mt = (MT103) msg;
				Field32A f = mt.getField32A();
				ret = f.getCurrency();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
		return ret;
	}

	/**
	 * Возврат Валюты , если MT103...пока
	 * 
	 * @param path
	 * @return
	 */
	String getMtDate(String path) {
		String ret = null;
		try {
			InputStream inputstream = new FileInputStream(path);
			AbstractMT msg = AbstractMT.parse(inputstream);
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			if (msg.isType(103)) {
				MT103 mt = (MT103) msg;
				Field32A f = mt.getField32A();
				ret = sdf.format(f.getDateAsCalendar().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
		return ret;
	}

	/**
	 * Тип файла
	 * 
	 * @param path
	 * @return
	 */
	String getMtType(String path) {
		String ret = null;
		try {
			InputStream inputstream = new FileInputStream(path);
			AbstractMT msg = AbstractMT.parse(inputstream);
			ret = msg.getMessageType();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
		return ret;
	}

	String InsertDB(LocalDate DOCDATE_, Double SUMM_, String CUR_, String MTNAME_, String MTTYPE_, LocalDateTime DT_CH_,
			String FILENAME_) {
		String ret = "error";
		try {
			CallableStatement clstmt = conn.prepareCall("{ ? = call z_sb_mmbank.VTB_SWIFT(?,?,?,?,?,?,?,?,?,?,?) }");
			clstmt.registerOutParameter(1, Types.VARCHAR);
			clstmt.setDate(2, (DOCDATE_ != null) ? java.sql.Date.valueOf(DOCDATE_) : null);
			if (SUMM_ != null) {
				clstmt.setDouble(3, SUMM_);
			} else {
				clstmt.setNull(3, java.sql.Types.DOUBLE);
			}
			clstmt.setString(4, "IN");
			clstmt.setString(5, CUR_);
			clstmt.setString(6, MTNAME_);
			clstmt.setString(7, MTTYPE_);

			byte[] bArray = null;
			Path path = Paths.get(System.getenv("SWIFT_MSG") + "/" + FILENAME_);
			try {
				bArray = java.nio.file.Files.readAllBytes(path);
				// reading content from byte array
				// for (int i = 0; i < bArray.length; i++) {
				// System.out.print((char) bArray[i]);
				// }
			} catch (IOException e) {
				e.printStackTrace();
			}
			InputStream is = new ByteArrayInputStream(bArray);
			clstmt.setBlob(8, is, bArray.length);
			clstmt.setTimestamp(9, (DT_CH_ != null) ? java.sql.Timestamp.valueOf(DT_CH_) : null);
			clstmt.setString(10, FILENAME_);
			clstmt.registerOutParameter(11, java.sql.Types.VARCHAR);
			clstmt.registerOutParameter(12, java.sql.Types.INTEGER);
			clstmt.execute();

			if (!clstmt.getString(1).equals("ok")) {
				ret = clstmt.getString(11);
				conn.rollback();
			} else {
				ret = "ok";
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Перенести файл в локальную директорию и записать в базу, по помеченным
	 * строкам
	 * 
	 * @param event
	 */
	@FXML
	void LoadFile(ActionEvent event) {
		try {
			LocalDate docdt = null;
			LocalDateTime crdate = null;
			Double amount = null;
			String cur = null;
			String docname = null;
			String doctype = null;
			String filename = null;
			String ret = null;
			// Цикл по ячейкам
			for (int i = 0; i < STMT.getItems().size(); i++) {
				// Цикл по столбцам
				for (int j = 0; j < STMT.getColumns().size(); j++) {
					// Если Не пусто
					if (STMT.getColumns().get(j).getCellData(i) != null) {
						// Если выделена строка
						if (j == 0) {
							if ((Boolean) STMT.getColumns().get(j).getCellData(i) == true) {

								// инициализация переменных
								docdt = null;
								crdate = null;
								amount = null;
								cur = null;
								docname = null;
								doctype = null;
								filename = null;
								ret = null;
								// проверка на наличие данных
								if (STMT.getColumns().get(3).getCellData(i) != null) {
									docdt = (LocalDate) STMT.getColumns().get(3).getCellData(i);
								}
								if (STMT.getColumns().get(5).getCellData(i) != null) {
									amount = (Double) STMT.getColumns().get(5).getCellData(i);
								}
								if (STMT.getColumns().get(7).getCellData(i) != null) {
									crdate = (LocalDateTime) STMT.getColumns().get(7).getCellData(i);
								}
								if (STMT.getColumns().get(4).getCellData(i) != null) {
									cur = (String) STMT.getColumns().get(4).getCellData(i);
								}
								if (STMT.getColumns().get(2).getCellData(i) != null) {
									docname = (String) STMT.getColumns().get(2).getCellData(i);
								}
								if (STMT.getColumns().get(1).getCellData(i) != null) {
									doctype = (String) STMT.getColumns().get(1).getCellData(i);
								}
								if (STMT.getColumns().get(6).getCellData(i) != null) {
									filename = (String) STMT.getColumns().get(6).getCellData(i);
								}
								ret = InsertDB(docdt, amount, cur, docname, doctype, crdate, filename);
								if (ret.equals("ok")) {
									Msg.Message(
											"Файл " + filename + " перенесен в локальную директорию и архивирован!");
								} else {
									Msg.Message(ret);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Форматирование столбцов DTTM
	 * 
	 * @param TC
	 */
	void DateFormatDTTM(TableColumn<SWIFT_FILES, LocalDateTime> TC) {
		TC.setCellFactory(column -> {
			TableCell<SWIFT_FILES, LocalDateTime> cell = new TableCell<SWIFT_FILES, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null)
							setText(format.format(item));
					}
				}
			};
			return cell;
		});
	}

	/**
	 * Форматирование столбцов DT
	 * 
	 * @param TC
	 */
	void DateFormatDT(TableColumn<SWIFT_FILES, LocalDate> TC) {
		TC.setCellFactory(column -> {
			TableCell<SWIFT_FILES, LocalDate> cell = new TableCell<SWIFT_FILES, LocalDate>() {
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
	 * Универсальный декодер
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
			Msg.Message(e.getMessage());
		}
		return null;
	}

	/**
	 * Чтение файла
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unused")
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
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
			Msg.Message(e.getMessage());
		}
		return null;
	}

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		try {
			STMT.setEditable(true);
			dbConnect();
			// TMREFR.getItems().addAll("1", "5", "10");
			// TMREFR.getSelectionModel().select(0);
			CHK.setCellValueFactory(cellData -> cellData.getValue().CHKProperty());
			DT_CH.setCellValueFactory(cellData -> cellData.getValue().DT_CHProperty());
			DOCDATE.setCellValueFactory(cellData -> cellData.getValue().DOCDATEProperty());
			FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().FILENAMEProperty());
			MTTYPE.setCellValueFactory(cellData -> cellData.getValue().MTTYPEProperty());
			MTNAME.setCellValueFactory(cellData -> cellData.getValue().MTNAMEProperty());
			CUR.setCellValueFactory(cellData -> cellData.getValue().CURProperty());
			SUMM.setCellValueFactory(cellData -> cellData.getValue().SUMMProperty().asObject());
			// Редактирование
			DOCDATE.setCellFactory(
					TextFieldTableCell.<SWIFT_FILES, LocalDate>forTableColumn(new LocalDateStringConverter()));
			MTTYPE.setCellFactory(TextFieldTableCell.forTableColumn());
			MTNAME.setCellFactory(TextFieldTableCell.forTableColumn());
			CUR.setCellFactory(TextFieldTableCell.forTableColumn());
			SUMM.setCellFactory(TextFieldTableCell.<SWIFT_FILES, Double>forTableColumn(new DoubleStringConverter()));
			FILE_NAME.setCellFactory(TextFieldTableCell.forTableColumn());
			DT_CH.setCellFactory(
					TextFieldTableCell.<SWIFT_FILES, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));

			DOCDATE.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, LocalDate>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, LocalDate> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setDOCDATE(t.getNewValue());
				}
			});

			SUMM.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, Double>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, Double> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setSUMM(t.getNewValue());
				}
			});

			MTTYPE.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setMTTYPE(t.getNewValue());
				}
			});
			MTNAME.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setMTNAME(t.getNewValue());
				}
			});
			CUR.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setCUR(t.getNewValue());
				}
			});
			DT_CH.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, LocalDateTime>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, LocalDateTime> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setDT_CH(t.getNewValue());
				}
			});

			FILE_NAME.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setFILENAME(t.getNewValue());
				}
			});

			DateFormatDTTM(DT_CH);

			// ==== CHK? (CHECH BOX) ===
			CHK.setCellValueFactory(new Callback<CellDataFeatures<SWIFT_FILES, Boolean>, ObservableValue<Boolean>>() {

				@Override
				public ObservableValue<Boolean> call(CellDataFeatures<SWIFT_FILES, Boolean> param) {
					SWIFT_FILES person = param.getValue();

					SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(person.getCHK());

					// Note: singleCol.setOnEditCommit(): Not work for
					// CheckBoxTableCell.

					// When "Single?" column change.
					booleanProp.addListener(new ChangeListener<Boolean>() {

						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							person.setCHK(newValue);
						}
					});
					return booleanProp;
				}
			});

			CHK.setCellFactory(new Callback<TableColumn<SWIFT_FILES, Boolean>, //
					TableCell<SWIFT_FILES, Boolean>>() {
				@Override
				public TableCell<SWIFT_FILES, Boolean> call(TableColumn<SWIFT_FILES, Boolean> p) {
					CheckBoxTableCell<SWIFT_FILES, Boolean> cell = new CheckBoxTableCell<SWIFT_FILES, Boolean>();
					cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});

			/**
			 * Load Date
			 */
			InitTable();

			/**
			 * Auto Refresh
			 */
			Timer time = new Timer(); // Instantiate Timer Object
			st = new ScheduledTask(); // Instantiate SheduledTask class
			st.setSWC(this);
			time.schedule(st, 0, 1000); // Create task repeating every 1 sec
			// st.cancel();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Выражение CRON, не используется
	 */
	String CRON_EXPRESSION = "*/10 * * ? * * *";

	/**
	 * Обновить
	 * 
	 * @param event
	 */
	@FXML
	void Refresh(ActionEvent event) {
		try {
			InitTable();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Получить расширение файла
	 * 
	 * @param filename
	 * @return
	 */
	public String getFExt(String filename) {
		return Files.getFileExtension(filename);
	}

	/**
	 * Закрытие задачи при закрытии формы
	 */
	public void EndTask() {
		this.st.cancel();
	}

	/**
	 * Задача
	 */
	private ScheduledTask st;

	/**
	 * Получить название MT и тип
	 * 
	 * @param MT
	 * @param Col
	 * @return
	 */
	String getMT(String MT, String Col) {
		String ret = null;
		try {
			String sel = "select TYPE, NAME, MT_CAT from VTB_MTTYPE a, VTB_MTCAT b where a.cat = b.mtc_id and lower(TYPE) like '%"
					+ MT + "%'";
			PreparedStatement prepStmt = conn.prepareStatement(sel);
			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				ret = rs.getString(Col);
			}
			rs.close();
			prepStmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}
		return ret;
	}

	/**
	 * Инициализация данных
	 */
	void InitTable() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			// MSG
			File dir = new File(System.getenv("SWIFT_MSG"));
			File[] directoryListing = dir.listFiles();
			ObservableList<SWIFT_FILES> dlist = FXCollections.observableArrayList();
			Boolean ifchk = false;
			if (directoryListing != null) {
				for (File child : directoryListing) {

					Path filePath = child.toPath();
					/**
					 * Атрибуты файла...
					 */
					BasicFileAttributes attr = java.nio.file.Files.readAttributes(filePath, BasicFileAttributes.class);
					SWIFT_FILES list = new SWIFT_FILES();
					list.setFILENAME(child.getName());
					list.setDT_CH(
							LocalDateTime.parse(format.format(new Date(attr.creationTime().toMillis())), formatterwt));
					list.setCUR(getMtCur(child.getAbsolutePath()));
					list.setSUMM(getMtAmount(child.getAbsolutePath()));
					list.setMTTYPE(getMT(getMtType(child.getAbsolutePath()), "TYPE"));
					list.setMTNAME(getMT(getMtType(child.getAbsolutePath()), "NAME"));
					list.setDOCDATE((getMtDate(child.getAbsolutePath()) != null)
							? LocalDate.parse(getMtDate(child.getAbsolutePath()), formatter)
							: null);
					/**
					 * Перебор отмеченных
					 */
					for (int i = 0; i < STMT.getItems().size(); i++) {
						for (int j = 0; j < STMT.getColumns().size(); j++) {
							if (STMT.getColumns().get(j).getCellData(i) != null) {
								if (j == 6 & STMT.getColumns().get(j).getCellData(i).equals(child.getName())) {
									ifchk = (Boolean) STMT.getColumns().get(0).getCellData(i);
								}
							}
						}
					}
					list.setCHK(ifchk);
					dlist.add(list);
				}
			}
			STMT.setItems(dlist);
			autoResizeColumns(STMT);
			TableFilter<SWIFT_FILES> tableFilter = TableFilter.forTableView(STMT).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Msg.Message(e.getMessage());
		}

	}

	/**
	 * Авто расширение
	 * 
	 * @param table
	 */
	void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column_) -> {
			// column1.getColumns().stream().forEach((column_) -> {
			// System.out.println(column_.getText());
			if (column_.getText().equals("Дата изменения")) {

			} else {
				// Minimal width = columnheader
				Text t = new Text(column_.getText());
				double max = t.getLayoutBounds().getWidth();
				for (int i = 0; i < table.getItems().size(); i++) {
					// cell must not be empty
					if (column_.getCellData(i) != null) {
						t = new Text(column_.getCellData(i).toString());
						double calcwidth = t.getLayoutBounds().getWidth();
						// remember new max-width
						if (calcwidth > max) {
							max = calcwidth;
						}
					}
				}
				// set the new max-widht with some extra space
				column_.setPrefWidth(max + 10.0d);
			}
			// });

		});
	}

	/**
	 * Сессия
	 */
	private Connection conn;

	/**
	 * Открыть сессию
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
			Msg.Message(e.getMessage());
		}
	}

	/**
	 * Отключить сессию
	 */
	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Msg.Message(e.getMessage());
		}
	}

}

package su.sbra.psv.app.swift;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;

import com.google.common.io.Files;
import com.jyloo.syntheticafx.ComparableColumnFilter;
import com.jyloo.syntheticafx.DateColumnFilter;
import com.jyloo.syntheticafx.PatternColumnFilter;
import com.jyloo.syntheticafx.SyntheticaFX;
import com.jyloo.syntheticafx.TextFormatterFactory;
import com.jyloo.syntheticafx.XTableColumn;
import com.jyloo.syntheticafx.XTableView;
import com.jyloo.syntheticafx.filter.ComparableFilterModel;
import com.jyloo.syntheticafx.filter.ComparisonType;
import com.prowidesoftware.swift.model.field.Field32A;
import com.prowidesoftware.swift.model.field.Field50K;
import com.prowidesoftware.swift.model.field.Field52D;
import com.prowidesoftware.swift.model.field.Field59;
import com.prowidesoftware.swift.model.field.Field60F;
import com.prowidesoftware.swift.model.field.Field70;
import com.prowidesoftware.swift.model.field.Field72;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.model.mt.mt2xx.MT202;
import com.prowidesoftware.swift.model.mt.mt9xx.MT950;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import su.sbra.psv.app.model.Connect;

/**
 * SWIFT
 * 
 * 07.11.2020
 * 
 * @author Said
 *
 */
public class SWC2 {
	@FXML
	private TableView<BIK_TO_SW_VTB> BIK_TO_SW_VTB;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, String> BIK_TRN;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, String> SW_TRN;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, LocalDate> DTRNCREATE;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, LocalDate> DTRNTRAN;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, String> CTRNACCD;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, String> CTRNACCC;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, Integer> MTRNSUM;

	@FXML
	private TableColumn<BIK_TO_SW_VTB, String> CTRNCORACCO;
	/* __________Кнопки_для_быстрого_перемещения__________ */

	@FXML
	private TextField PLAT_KORR;

	@FXML
	private TextField CDETAIL;

	@FXML
	private TextField PAY_ACC;

	@FXML
	private TextField PAY_NAME;

	@FXML
	private TextField REC_NAME;

	@FXML
	private TextField REC_ACC;

	@FXML
	private RadioButton Out;
	@FXML
	private RadioButton Kvt;
	@FXML
	private RadioButton OutLocal;
	@FXML
	private RadioButton InLocal;
	@FXML
	private RadioButton Msg;
	@FXML
	private RadioButton Ack;
	@FXML
	private RadioButton Other;

	@FXML
	private DatePicker FileDate;

	@FXML
	private DatePicker DT2;

	@FXML
	private ComboBox<String> FileExtens;

	@FXML
	private TextArea FileTextArea;

	/**
	 * Расшифровка типа каталога
	 */
	@FXML
	private Text FolderN;

	/**
	 * Дата с
	 */
	@FXML
	private DatePicker dt1;

	/**
	 * Дата по
	 */
	@FXML
	private DatePicker dt2;
	/**
	 * Таб архива
	 */
	@FXML
	private Tab ArchiveInOut;

	/**
	 * Наш TabPane
	 */
	@FXML
	private TabPane RootTab;

	/**
	 * Кнопка обработки только, если их входящих папок
	 */
	@FXML
	private Button ModeINbox;

	/**
	 * Поиск их базы
	 */
	@FXML
	private Button RefreshDB;

	/**
	 * Контейнер входящих сообщении
	 */
	@FXML
	private StackPane StPn;

	/**
	 * Индикатор завершения процесса
	 */
	@FXML
	private ProgressIndicator PrgInd;

	/**
	 * Тип вкладки
	 */
	@FXML
	private Tab INOUT;
	/**
	 * Не используется, тип папки
	 */
	@FXML
	private ComboBox<String> DIRNAME;

	/**
	 * Тип архива
	 */
	@FXML
	private ComboBox<String> ArchType;

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

	/*---------------------------*/
	/**
	 * Валюта
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, String> CURdb;

	/**
	 * Сумма документа, если MT103...
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, Double> SUMMdb;

	/**
	 * Название операции
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, String> MTNAMEdb;

	/**
	 * MT.., из базы
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, String> MTTYPEdb;

	/**
	 * Дата создания файла
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, LocalDateTime> DT_CHdb;

	/**
	 * Дата документа
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, LocalDate> DOCDATEdb;

	/**
	 * Название файла
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, String> FILE_NAMEdb;

	/**
	 * IN-OUT база
	 */
	@FXML
	private XTableColumn<SWIFT_FILES, String> Typedb;

	@FXML
	private XTableColumn<SWIFT_FILES, String> OPERdb;

	@FXML
	private XTableColumn<SWIFT_FILES, String> REFdb;
	/**
	 * Таблица Вх-Исх каталогов
	 */
	@FXML
	private TableView<SWIFT_FILES> STMT;

	/**
	 * Таблица Архива
	 */
	@FXML
	private XTableView<SWIFT_FILES> Achive;

	@FXML
	private TableColumn<VAVAL, String> CATTR_NAME;

	@FXML
	private TableColumn<VAVAL, String> CVALUE;

	@FXML
	private TableView<VAVAL> AVAL;

	@FXML
	private RadioButton SWIFT_VTB;

	@FXML
	private RadioButton BK_VTB;

	@FXML
	void OpenAbsForm(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() != null) {
				SWIFT_FILES selrow = Achive.getSelectionModel().getSelectedItem();
				if (selrow.getVECTOR().equals("IN")) {
					PrgInd.setVisible(true);
					Task<Object> task = new Task<Object>() {
						@Override
						public Object call() throws Exception {
							try {
								String call = "ifrun60.exe I:/SWIFT/IM_MSG.fmx " + Connect.userID_ + "/"
										+ Connect.userPassword_ + "@ODB WHERE_CLAUSE=\"" + "CREF = '" + selrow.getREF()
										+ "'\"";
								ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
								System.out.println(call);
								// System.out.println(call);
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
								ErrorMessage(ExceptionUtils.getStackTrace(e));
							}
							return null;
						}
					};
					task.setOnFailed(e -> ErrorMessage(task.getException().getMessage()));
					task.setOnSucceeded(e -> PrgInd.setVisible(false));

					exec.execute(task);
				} else if (selrow.getVECTOR().equals("OUT")) {
					PrgInd.setVisible(true);
					Task<Object> task = new Task<Object>() {
						@Override
						public Object call() throws Exception {
							try {
								String call = "ifrun60.exe I:/SWIFT/INT_C_S.fmx " + Connect.userID_ + "/"
										+ Connect.userPassword_ + "@ODB ARCHIVE=\"ARCHIVE\" WHERE_STR=\""
										+ "CSWB_F20 = '" + selrow.getREF() + "'\"";
								ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
								System.out.println(call);
								// System.out.println(call);
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
								ErrorMessage(ExceptionUtils.getStackTrace(e));
							}
							return null;
						}
					};
					task.setOnFailed(e -> ErrorMessage(task.getException().getMessage()));
					task.setOnSucceeded(e -> PrgInd.setVisible(false));

					exec.execute(task);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void OpenRelation(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() != null) {

				SWIFT_FILES selrow = Achive.getSelectionModel().getSelectedItem();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

				PreparedStatement prepStmt = conn.prepareStatement("select *\r\n" + "  from swift_files t\r\n"
						+ " where utl_raw.cast_to_varchar2(swfile) like '%' || ? || '%'");
				prepStmt.setString(1, selrow.getREF());

				ResultSet rs = prepStmt.executeQuery();
				ObservableList<SWIFT_FILES> cus_list = FXCollections.observableArrayList();
				DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				while (rs.next()) {
					SWIFT_FILES list = new SWIFT_FILES();

					list.setDOCDATE((rs.getDate("DOCDATE") != null) ? LocalDate
							.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("DOCDATE")), formatter) : null);
					list.setSUMM(rs.getDouble("SUMM"));
					list.setVECTOR(rs.getString("VECTOR"));
					list.setCUR(rs.getString("CUR"));
					list.setMTNAME(rs.getString("MTNAME"));
					list.setMTTYPE(rs.getString("MTTYPE"));
					list.setCR_DT((rs.getDate("CR_DT") != null) ? LocalDateTime
							.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("CR_DT")), dtformatter)
							: null);
					list.setOPER(rs.getString("OPER"));
					list.setDT_CH((rs.getDate("DT_CH") != null) ? LocalDateTime
							.parse(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("DT_CH")), dtformatter)
							: null);
					list.setFILENAME(rs.getString("FILENAME"));
					list.setID(rs.getInt("ID"));
					list.setREF(rs.getString("REF"));
					cus_list.add(list);
				}
				prepStmt.close();
				rs.close();
				Achive.setItems(cus_list);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						TableFilter<SWIFT_FILES> tableFilter = TableFilter.forTableView(Achive).apply();
						tableFilter.setSearchStrategy((input, target) -> {
							try {
								return target.toLowerCase().contains(input.toLowerCase());
							} catch (Exception e) {
								return false;
							}
						});
					}
				});
				NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
				SUMMdb.setCellFactory(tc -> new TableCell<SWIFT_FILES, Double>() {

					@Override
					protected void updateItem(Double price, boolean empty) {
						super.updateItem(price, empty);
						if (empty) {
							setText(null);
						} else {
							setText(currencyFormat.format(price));
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Печать статуса
	 * 
	 * @param event
	 */
	@FXML
	void AckNak(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() != null) {
				SWIFT_FILES selrow = Achive.getSelectionModel().getSelectedItem();
				new PrintReportAckNak().showReport(selrow.getREF());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void BK_VTB(ActionEvent event) {
		try {
//			// Specify this setting to fetch server output explicitly
//		    DSLContext ctx = DSL.using(conn, 
//		        new Settings().withFetchServerOutputSize(10000));
//		    ctx.execute("begin SBRA_VTB_SWIF.GO_BK end;");
//		    ctx.close();
			CallableStatement clbstmt = conn.prepareCall("{ call SBRA_VTB_SWIF.GO_BK }");
			clbstmt.executeUpdate();
			clbstmt.close();

			REFRESH_AVAL();
			System.out.println("BK_VTB");
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void SWIFT_VTB(ActionEvent event) {
		try {
			CallableStatement clbstmt = conn.prepareCall("{ call SBRA_VTB_SWIF.GO_SW }");
			clbstmt.executeUpdate();
			clbstmt.close();
			REFRESH_AVAL();
			System.out.println("SWIFT_VTB");
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Возврат суммы документа , если MT103...пока
	 * 
	 * @param path
	 * @return
	 */
	String getMtAmount(String path) {
		String ret = null;

		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field32A f = mt.getField32A();
					if (f != null) {
						ret = f.getAmount();
					} else {
						ret = null;
					}
				}
				if (msg != null && msg.isType(202)) {
					MT202 mt = (MT202) msg;
					Field32A f = mt.getField32A();
					if (f != null) {
						ret = f.getAmount();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * Получить детали платежа
	 * 
	 * @param path
	 * @return
	 */
	String getMtDetail(String path) {
		String ret = null;

		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field70 f70 = mt.getField70();
					if (f70 != null) {
						ret = f70.getValue();
					} else {
						ret = null;
					}
				}
				if (msg != null && msg.isType(202)) {
					MT202 mt = (MT202) msg;
					Field72 f72 = mt.getField72();
					if (f72 != null) {
						ret = f72.getValue();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String get50F(String path) {
		String ret = null;
		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field50K f50 = mt.getField50K();
					if (f50 != null) {
						ret = f50.getComponent1();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String get59ACC(String path) {
		String ret = null;
		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field59 f59 = mt.getField59();
					if (f59 != null) {
						ret = f59.getAccount();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String get52CORR(String path) {
		String ret = null;
		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field52D f52 = mt.getField52D();
					if (f52 != null) {
						ret = f52.getAccount();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String get59NAME(String path) {
		String ret = null;
		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field59 f59 = mt.getField59();
					if (f59 != null) {
						ret = f59.getComponent2();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String get50FNAME(String path) {
		String ret = null;
		try {
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field50K f50 = mt.getField50K();
					if (f50 != null) {
						ret = f50.getComponent2() + " " + f50.getComponent3() + " " + f50.getComponent4() + " "
								+ f50.getComponent5();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
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
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field32A f = mt.getField32A();

					if (f != null) {
						ret = f.getCurrency();
					} else {
						ret = null;
					}

				}
				if (msg != null && msg.isType(202)) {
					MT202 mt = (MT202) msg;
					Field32A f = mt.getField32A();
					if (f != null) {
						ret = f.getCurrency();
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
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
			if (!getFExt(path).toLowerCase().equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				if (msg != null && msg.isType(103)) {
					MT103 mt = (MT103) msg;
					Field32A f = mt.getField32A();
					if (f != null) {
						ret = sdf.format(f.getDateAsCalendar().getTime());
					} else {
						ret = null;
					}
				}
				if (msg != null && msg.isType(202)) {
					MT202 mt = (MT202) msg;
					Field32A f = mt.getField32A();
					if (f != null) {
						ret = sdf.format(f.getDateAsCalendar().getTime());
					} else {
						ret = null;
					}
				}
				if (msg != null && msg.isType(950)) {
					MT950 mt = (MT950) msg;
					Field60F f = mt.getField60F();
					if (f != null) {
						ret = sdf.format(f.getDateAsCalendar().getTime());
					} else {
						ret = null;
					}
				}
				inputstream.close();
			}
		} catch (Exception e) {
			// ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
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
			if (!getFExt(path).equals("xml")) {
				InputStream inputstream = new FileInputStream(path);
				AbstractMT msg = AbstractMT.parse(inputstream);
				if (msg != null)
					ret = msg.getMessageType();
				inputstream.close();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String InsertDB(LocalDate DOCDATE_, String SUMM_, String CUR_, String MTNAME_, String MTTYPE_, LocalDateTime DT_CH_,
			String FILENAME_, String VECTOR) {
		String ret = "error";
		try {
			CallableStatement clstmt = conn.prepareCall("{ ? = call SBRA_VTB_SWIF.VTB_SWIFT(?,?,?,?,?,?,?,?,?,?,?) }");
			clstmt.registerOutParameter(1, Types.VARCHAR);
			clstmt.setDate(2, (DOCDATE_ != null) ? java.sql.Date.valueOf(DOCDATE_) : null);
			// if (SUMM_ != null) {
			clstmt.setString(3, SUMM_);
			// } else {
			// clstmt.setNull(3, java.sql.Types.DOUBLE);
			// }
			clstmt.setString(4, VECTOR);
			clstmt.setString(5, CUR_);
			clstmt.setString(6, MTNAME_);
			clstmt.setString(7, MTTYPE_);

			byte[] bArray = null;
			Path path = Paths.get(System.getenv(FolderName) + "/" + FILENAME_);
			try {
				bArray = java.nio.file.Files.readAllBytes(path);
				// reading content from byte array
				// for (int i = 0; i < bArray.length; i++) {
				// System.out.print((char) bArray[i]);
				// }
			} catch (IOException e) {

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
				// conn.rollback();
			} else {
				ret = "ok";
				// conn.commit();
			}
			clstmt.close();
			is.close();
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * Перенести файл в локальную директорию из базы, по выделенным строкам
	 * 
	 * @param event
	 */
	@FXML
	void LoadFileDB(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() == null) {
				ErrorMessage("Выберите строку");
			} else {
				SWIFT_FILES st = Achive.getSelectionModel().getSelectedItem();
				String mes = "";
				if (st.getVECTOR().equals("IN")) {
					mes = "Скопировать в локальную директорию?";
				} else if (st.getVECTOR().equals("OUT")) {
					mes = "Переотправить в ВТБ?";
				}
				Stage stage = (Stage) Achive.getScene().getWindow();
				Label alert = new Label(mes);
				alert.setLayoutX(10.0);
				alert.setLayoutY(11.0);
				alert.setPrefHeight(17.0);

				Button no = new Button();
				no.setText("Нет");
				no.setLayoutX(111.0);
				no.setLayoutY(56.0);
				no.setPrefWidth(72.0);
				no.setPrefHeight(21.0);

				Button yes = new Button();
				yes.setText("Да");
				yes.setLayoutX(14.0);
				yes.setLayoutY(56.0);
				yes.setPrefWidth(72.0);
				yes.setPrefHeight(21.0);

				AnchorPane yn = new AnchorPane();
				yn.getChildren().add(alert);
				yn.getChildren().add(no);
				yn.getChildren().add(yes);
				Scene ynScene = new Scene(yn, 250, 100);
				Stage newWindow_yn = new Stage();
				no.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						newWindow_yn.close();
					}
				});
				yes.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						if (st.getVECTOR().equals("IN")) {
							try {
								String sel = "select * from SWIFT_FILES where ID = ?";
								PreparedStatement prepStmt = conn.prepareStatement(sel);
								prepStmt.setInt(1, st.getID());
								ResultSet rs = prepStmt.executeQuery();
								if (rs.next()) {
									Blob blob = rs.getBlob("SWFILE");
									int blobLength = (int) blob.length();
									byte[] blobAsBytes = blob.getBytes(1, blobLength);
									// release the blob and free up memory. (since JDBC 4.0)
									blob.free();
									FileUtils.writeByteArrayToFile(
											new File(System.getenv("SWIFT_INLOCAL") + "/" + rs.getString("FILENAME")),
											blobAsBytes);
								}
								rs.close();
								prepStmt.close();
							} catch (Exception e) {
								ErrorMessage(ExceptionUtils.getStackTrace(e));
								SWLogger.error(
										ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
							}
						} else if (st.getVECTOR().equals("OUT")) {
							try {
								String sel = "select * from SWIFT_FILES where ID = ?";
								PreparedStatement prepStmt = conn.prepareStatement(sel);
								prepStmt.setInt(1, st.getID());
								ResultSet rs = prepStmt.executeQuery();
								if (rs.next()) {
									Blob blob = rs.getBlob("SWFILE");
									int blobLength = (int) blob.length();
									byte[] blobAsBytes = blob.getBytes(1, blobLength);
									// release the blob and free up memory. (since JDBC 4.0)
									blob.free();
									FileUtils.writeByteArrayToFile(
											new File(System.getenv("SWIFT_OUT") + "/" + rs.getString("FILENAME")),
											blobAsBytes);
								}
								rs.close();
								prepStmt.close();
							} catch (Exception e) {
								ErrorMessage(ExceptionUtils.getStackTrace(e));
								SWLogger.error(
										ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
							}
						}
						newWindow_yn.close();
					}
				});
				newWindow_yn.setTitle("Внимание");
				newWindow_yn.setScene(ynScene);
				newWindow_yn.initModality(Modality.WINDOW_MODAL);
				newWindow_yn.initOwner(stage);
				newWindow_yn.setResizable(false);
				newWindow_yn.getIcons().add(new Image("/icon.png"));
				newWindow_yn.show();
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Перенести файл в локальную директорию и записать в базу, по помеченным
	 * строкам
	 * 
	 * или другие действия...
	 * 
	 * @param event
	 */
	@FXML
	void LoadFile(ActionEvent event) {
		try {
//			RootTab.setDisable(true);
//			PrgInd.setVisible(true);
//			Task<Object> task = new Task<Object>() {
//				@Override
//				public Object call() throws Exception {
			LocalDate docdt = null;
			LocalDateTime crdate = null;
			String amount = null;
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
									amount = (String) STMT.getColumns().get(5).getCellData(i);
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
								// если папки входящие
								if (FolderName.equals("SWIFT_MSG") | FolderName.equals("SWIFT_ACK")
										| FolderName.equals("SWIFT_KVT") || FolderName.equals("SWIFT_OTHER")) {
									ret = InsertDB(docdt, amount, cur, docname, doctype, crdate, filename, "IN");
									if (ret.equals("ok")) {
										File destinationFolder = new File(System.getenv("SWIFT_INLOCAL") + "/"
												+ STMT.getColumns().get(6).getCellData(i));
										File sourceFolder = new File(System.getenv(FolderName) + "/"
												+ STMT.getColumns().get(6).getCellData(i));
										try {
											moveFileWithOverwrite(sourceFolder, destinationFolder);
											InitTable();
											conn.commit();
										} catch (IOException e) {
											conn.rollback();
											SWLogger.error(ExceptionUtils.getStackTrace(e) + "~"
													+ Thread.currentThread().getName());
											ErrorMessage(ExceptionUtils.getStackTrace(e));
										}
									} else {
										ErrorMessage(ret);
									}
									// если папка исходящая
								} else if (FolderName.equals("SWIFT_OUTLOCAL")) {
									ret = InsertDB(docdt, amount, cur, docname, doctype, crdate, filename, "OUT");
									if (ret.equals("ok")) {
										File destinationFolder = new File(System.getenv("SWIFT_OUT") + "/"
												+ STMT.getColumns().get(6).getCellData(i));
										File sourceFolder = new File(System.getenv(FolderName) + "/"
												+ STMT.getColumns().get(6).getCellData(i));
										try {
											moveFileWithOverwrite(sourceFolder, destinationFolder);
											InitTable();
											conn.commit();
										} catch (IOException e) {
											conn.rollback();
											SWLogger.error(ExceptionUtils.getStackTrace(e) + "~"
													+ Thread.currentThread().getName());
											ErrorMessage(ExceptionUtils.getStackTrace(e));
										}
									} else {
										ErrorMessage(ret);
									}
								}
							}
						}
					}
				}
			}
//					return null;
//				}
//			};
//			task.setOnFailed(e -> {
//				ErrorMessage(task.getException().getMessage());
//				SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
//			});
//			task.setOnSucceeded(e -> {
//				try {
//					RootTab.setDisable(false);
//					PrgInd.setVisible(false);
//				} catch (Exception e1) {
//					ErrorMessage(e1.getMessage());
//					SWLogger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
//				}
//			});
//			exec.execute(task);
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Deletes the destination file if it exists then calls org.apache.commons
	 * moveFile.
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public void moveFileWithOverwrite(File srcFile, File destFile) throws IOException {
		if (destFile.exists()) {
			org.apache.commons.io.FileUtils.deleteQuietly(destFile);
		}
		org.apache.commons.io.FileUtils.moveFile(srcFile, destFile);
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
						if (item != null)
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
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
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
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return null;
	}

	/**
	 * Изменить название вкладки исходя из типа папки
	 * 
	 * @param event
	 */
	@FXML
	private void ChTabName(ActionEvent event) {
		try {

			if (DIRNAME.getValue().toUpperCase().equals("MSG")) {

				MTTYPE.setVisible(true);
				MTNAME.setVisible(true);
				DOCDATE.setVisible(true);
				CUR.setVisible(true);
				SUMM.setVisible(true);

				INOUT.setText("Входящие");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("Входящие документы ВТБ " + System.getenv("SWIFT_MSG"));
				InitTable();
			} else if (DIRNAME.getValue().toUpperCase().equals("ACK")) {

				MTTYPE.setVisible(true);
				MTNAME.setVisible(true);
				DOCDATE.setVisible(true);
				CUR.setVisible(true);
				SUMM.setVisible(true);

				INOUT.setText("Входящие");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(true);

				FolderN.setText("Входящие документы ВТБ " + System.getenv("SWIFT_ACK"));

			} else if (DIRNAME.getValue().toUpperCase().equals("KVT")) {

				MTTYPE.setVisible(true);
				MTNAME.setVisible(true);
				DOCDATE.setVisible(true);
				CUR.setVisible(true);
				SUMM.setVisible(true);

				INOUT.setText("Входящие");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(true);

				FolderN.setText("Входящие документы ВТБ для квитанции " + System.getenv("SWIFT_KVT"));

			} else if (DIRNAME.getValue().toUpperCase().equals("OTHER")) {

				MTTYPE.setVisible(false);
				MTNAME.setVisible(false);
				DOCDATE.setVisible(false);
				CUR.setVisible(false);
				SUMM.setVisible(false);

				INOUT.setText("Входящие");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(true);

				FolderN.setText("Входящие документы ВТБ, другие=" + System.getenv("SWIFT_OTHER"));

			} else if (DIRNAME.getValue().toUpperCase().equals("OUT")) {

				MTTYPE.setVisible(true);
				MTNAME.setVisible(true);
				DOCDATE.setVisible(true);
				CUR.setVisible(true);
				SUMM.setVisible(true);

				INOUT.setText("Исходящие");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(true);

				FolderN.setText("Исходящие документы ВТБ " + System.getenv("SWIFT_OUT"));
				InitTable();
			} else if (DIRNAME.getValue().toUpperCase().equals("INLOCAL")) {

				MTTYPE.setVisible(true);
				MTNAME.setVisible(true);
				DOCDATE.setVisible(true);
				CUR.setVisible(true);
				SUMM.setVisible(true);

				INOUT.setText("Входящие,локальный каталог");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("Входящие,локальный каталог " + System.getenv("SWIFT_INLOCAL"));
				InitTable();
			}

			else if (DIRNAME.getValue().toUpperCase().equals("OUTLOCAL")) {

				MTTYPE.setVisible(true);
				MTNAME.setVisible(true);
				DOCDATE.setVisible(true);
				CUR.setVisible(true);
				SUMM.setVisible(true);

				INOUT.setText("Исходящие, локальный каталог");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("Исходящие, локальный каталог " + System.getenv("SWIFT_OUTLOCAL"));
				InitTable();
			}

		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Stage для закрытия
	 */
	@SuppressWarnings("unused")
	private Stage STFCLS;

	/**
	 * Инициализация Stage для закрытия
	 */
	public void SetStageForClose(Stage mnst) {
		this.STFCLS = mnst;
	}

	/**
	 * Закрытие формы
	 */
	void onclose() {
		Stage stage = (Stage) StPn.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	/**
	 * Запуск процесса
	 */
	void RunProcess(String type) {
		Timer time = new Timer(); // Instantiate Timer Object
		st = new ScheduledTask(); // Instantiate SheduledTask class
		//st.setSWC2(this, type);
		time.schedule(st, 0, 3000); // Create task repeating every 1 sec
	}

	void ErrorMessage(String mes) {
		try {
			if (mes != null && !mes.equals("")) {
				Platform.runLater(() -> {
					try {
						AlertType AlertTp = null;
						String error = null;
						if (mes.length() >= 200) {
							AlertTp = AlertType.ERROR;
							error = mes.substring(0, 150);
						} else {
							AlertTp = AlertType.INFORMATION;
							error = mes;
						}
						Alert alert = new Alert(AlertTp);
						Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
						stage.getIcons().add(new Image("/icon.png"));
						alert.setTitle("Внимание");
						alert.setHeaderText(error);
						// alert.setContentText(mess.substring(0, mess.indexOf("\r\n")));
						Label label = new Label("Трассировка стека исключения:");

						TextArea textArea = new TextArea(mes);
						textArea.setEditable(false);
						textArea.setWrapText(true);

						textArea.setMaxWidth(Double.MAX_VALUE);
						textArea.setMaxHeight(Double.MAX_VALUE);

						GridPane.setVgrow(textArea, Priority.ALWAYS);
						GridPane.setHgrow(textArea, Priority.ALWAYS);

						GridPane expContent = new GridPane();
						expContent.setMaxWidth(Double.MAX_VALUE);
						expContent.add(label, 0, 0);
						expContent.add(textArea, 0, 1);

						// Set expandable Exception into the dialog pane.
						alert.getDialogPane().setExpandableContent(expContent);

						alert.showAndWait();
					} catch (Exception e) {
						SWLogger.error(ExceptionUtils.getStackTrace(e));
					}
				});
			}
		} catch (Exception e) {
			SWLogger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private void ClearFilter(ActionEvent event) {
		FileDate.setValue(null);
		DT2.setValue(null);
		FileExtens.setValue(null);
	}

	@FXML
	private Button ClearFilter;

	/**
	 * Логирование
	 */
	Logger SWLogger = Logger.getLogger(getClass());

	Properties swift_mt;

	void REFRESH_AVAL() {
		try {
			PreparedStatement prp = conn.prepareStatement("select CATTR_NAME, CVALUE\r\n" + "    from AVAL, BATTR\r\n"
					+ "   where nbnk = 1683\r\n" + "     AND AVAL.nattr = BATTR.NATTR_ID\r\n" + "   ORDER BY NPP ASC");
			ResultSet rs = prp.executeQuery();
			ObservableList<VAVAL> list = FXCollections.observableArrayList();
			while (rs.next()) {
				VAVAL val = new VAVAL();
				val.setCATTR_NAME(rs.getString("CATTR_NAME"));
				val.setCVALUE(rs.getString("CVALUE"));
				list.add(val);
			}

			AVAL.setItems(list);
			rs.close();
			prp.close();
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@FXML
	void RESF_BIK_TO_SW_VTB(ActionEvent event) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String selectStmt = "select * from BIK_TO_SW_VTB t";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<BIK_TO_SW_VTB> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				BIK_TO_SW_VTB list = new BIK_TO_SW_VTB();
				list.setBIK_TRN(rs.getString("BIK_TRN"));
				list.setSW_TRN(rs.getString("SW_TRN"));
				list.setDTRNCREATE((rs.getDate("DTRNCREATE") != null) ? LocalDate
						.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("DTRNCREATE")), formatter) : null);
				list.setDTRNTRAN((rs.getDate("DTRNTRAN") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("DTRNTRAN")), formatter)
						: null);
				list.setCTRNACCD(rs.getString("CTRNACCD"));
				list.setCTRNACCC(rs.getString("CTRNACCC"));
				list.setMTRNSUM(rs.getInt("MTRNSUM"));
				list.setCTRNCORACCO(rs.getString("CTRNCORACCO"));
				list.setCTRNCORACCA(rs.getString("CTRNCORACCA"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();
			BIK_TO_SW_VTB.setItems(dlist);
			TableFilter<BIK_TO_SW_VTB> tableFilter = TableFilter.forTableView(BIK_TO_SW_VTB).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			SWLogger.error(ExceptionUtils.getStackTrace(e));
			ErrorMessage(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	private XTableView<VTB_MT202_CONV> CONV_TBL;

	@FXML
	private XTableColumn<VTB_MT202_CONV, Integer> CONV_ID;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_REF;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_FL32A_DATE;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_FL32A_CUR;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_FL32A_SUM;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_F53B;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_F58A;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_F72;

	@FXML
	private XTableColumn<VTB_MT202_CONV, LocalDateTime> CONV_DATETIME;

	@FXML
	private XTableColumn<VTB_MT202_CONV, String> CONV_OPER;
	@FXML
	private TextArea FileTextAreaDB;

	@FXML
	void AUDIT_CONV(ActionEvent event) {
		try {

			Path customBaseDir = FileSystems.getDefault().getPath(System.getenv("TRANSACT_PATH") + "PARAM");
			String customFilePrefix = "invparam_";
			String customFileSuffix = ".xml";
			Path tmpFile = java.nio.file.Files.createTempFile(customBaseDir, customFilePrefix, customFileSuffix);
			BufferedWriter bw = java.nio.file.Files.newBufferedWriter(tmpFile, StandardCharsets.UTF_8);
			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<Paramlist>\r\n"
					+ "	<Parameter Name=\"TableOwner\"/>\r\n" + "	<Parameter Name=\"TableName\">USR</Parameter>\r\n"
					+ "	<Parameter Name=\"PrimaryKey1\">1105</Parameter>\r\n"
					+ "	<Parameter Name=\"PrimaryKey2\"/>\r\n" + "	<Parameter Name=\"RowId\"/>\r\n" + "</Paramlist>");
			bw.close();
			tmpFile.toFile().deleteOnExit();

			String tmp_path = tmpFile.toFile().getAbsolutePath();

			SbEncode encode = new SbEncode();
			String mdi_totleString = encode.ascii2base64("Oracle Forms Runtime");
			String userid = "";// encode.ascii2base64(Connect.userID_.toUpperCase() + "/" +
								// Connect.userPassword_.toUpperCase() + "@odb");
			String xml = "java -jar I:/japp/FXPdoc.jar " + " \"--ru.inversion.mdi_title=" + mdi_totleString
					+ "\" \"--ru.inversion.userid=F" + userid
					+ "\" \"--ru.inversion.start_class=ru.inversion.fxpdoc.auview.PAuActionMain\""
					+ " \"--ru.inversion.start_method=showViewAuAction\"" + " \"--ru.inversion.start_file_params="
					+ tmp_path + "\"";
			System.out.println(xml);
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "java -jar I:/japp/FXPdoc.jar");
			builder.redirectErrorStream(true);
			Process p;
			p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				System.out.println(line);
				if (line == null) {
					break;
				}
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Инициализация
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private void initialize() {
		try {
			dbConnect();

			SyntheticaFX.init("com.jyloo.syntheticafx.SyntheticaFXModena");

			CONV_ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
			CONV_REF.setCellValueFactory(cellData -> cellData.getValue().REFProperty());
			CONV_FL32A_DATE.setCellValueFactory(cellData -> cellData.getValue().FL32A_DATEProperty());
			CONV_FL32A_CUR.setCellValueFactory(cellData -> cellData.getValue().FL32A_CURProperty());
			//CONV_FL32A_SUM.setCellValueFactory(cellData -> cellData.getValue().FL32A_SUMProperty());
			CONV_F53B.setCellValueFactory(cellData -> cellData.getValue().F53BProperty());
			CONV_F58A.setCellValueFactory(cellData -> cellData.getValue().F58AProperty());
			CONV_F72.setCellValueFactory(cellData -> cellData.getValue().F72Property());
			CONV_DATETIME.setCellValueFactory(cellData -> cellData.getValue().DATETIMEProperty());
			CONV_OPER.setCellValueFactory(cellData -> cellData.getValue().OPERProperty());

			CellDateFormatD(CONV_DATETIME);

			ObservableList rules = FXCollections.observableArrayList(ComparisonType.values());

			CONV_ID.setColumnFilter(new ComparableColumnFilter(new ComparableFilterModel(rules),
					TextFormatterFactory.LONG_TEXTFORMATTER_FACTORY));
			CONV_REF.setColumnFilter(new PatternColumnFilter<>());
			CONV_FL32A_DATE.setColumnFilter(new PatternColumnFilter<>());
			CONV_FL32A_CUR.setColumnFilter(new PatternColumnFilter<>());
			CONV_FL32A_SUM.setColumnFilter(new PatternColumnFilter<>());
			CONV_F53B.setColumnFilter(new PatternColumnFilter<>());
			CONV_F58A.setColumnFilter(new PatternColumnFilter<>());
			CONV_F72.setColumnFilter(new PatternColumnFilter<>());
			CONV_OPER.setColumnFilter(new PatternColumnFilter<>());

			BIK_TRN.setCellValueFactory(cellData -> cellData.getValue().BIK_TRNProperty());
			SW_TRN.setCellValueFactory(cellData -> cellData.getValue().SW_TRNProperty());
			DTRNCREATE.setCellValueFactory(cellData -> cellData.getValue().DTRNCREATEProperty());
			DTRNTRAN.setCellValueFactory(cellData -> cellData.getValue().DTRNTRANProperty());
			CTRNACCD.setCellValueFactory(cellData -> cellData.getValue().CTRNACCDProperty());
			CTRNACCC.setCellValueFactory(cellData -> cellData.getValue().CTRNACCCProperty());
			MTRNSUM.setCellValueFactory(cellData -> cellData.getValue().MTRNSUMProperty().asObject());
			CTRNCORACCO.setCellValueFactory(cellData -> cellData.getValue().CTRNCORACCOProperty());

			CATTR_NAME.setCellValueFactory(cellData -> cellData.getValue().CATTR_NAMEProperty());
			CVALUE.setCellValueFactory(cellData -> cellData.getValue().CVALUEProperty());

			CATTR_NAME.setOnEditCommit(new EventHandler<CellEditEvent<VAVAL, String>>() {
				@Override
				public void handle(CellEditEvent<VAVAL, String> t) {
					((VAVAL) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setCATTR_NAME(t.getNewValue());
				}
			});

			CVALUE.setOnEditCommit(new EventHandler<CellEditEvent<VAVAL, String>>() {
				@Override
				public void handle(CellEditEvent<VAVAL, String> t) {
					((VAVAL) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCVALUE(t.getNewValue());
				}
			});

			{
				// Group
				ToggleGroup group = new ToggleGroup();
				SWIFT_VTB.setToggleGroup(group);
				BK_VTB.setToggleGroup(group);
			}

			REFRESH_AVAL();

			{
				CallableStatement clbstmt = conn.prepareCall("{  ? = call SBRA_VTB_SWIF.SW_BK }");
				clbstmt.registerOutParameter(1, Types.VARCHAR);
				clbstmt.executeUpdate();
				if (clbstmt.getString(1).equals("BK")) {
					BK_VTB.setSelected(true);
				} else if (clbstmt.getString(1).equals("SW")) {
					SWIFT_VTB.setSelected(true);
				}
				clbstmt.close();
			}

			// _______________________________________________________________________________

			STMT.getColumns().addListener(new ListChangeListener() {
				@Override
				public void onChanged(Change change) {
					change.next();
					if (change.wasReplaced()) {
						STMT.getColumns().clear();
						STMT.getColumns().addAll(CHK, MTTYPE, MTNAME, DOCDATE, CUR, SUMM, FILE_NAME, DT_CH);
					}
				}
			});

//			addIfNotPresent(StPn.getStyleClass(), JMetroStyleClass.UNDERLINE_TAB_PANE);

			CheckBox selecteAllCheckBox = new CheckBox();
			selecteAllCheckBox.setOnAction(event -> {
				event.consume();
				STMT.getItems().forEach(item -> item.setCHK(selecteAllCheckBox.isSelected()));
			});

			CHK.setGraphic(selecteAllCheckBox);

			Msg.setSelected(true);

//			addIfNotPresent(StPn.getStyleClass(), JMetroStyleClass.BACKGROUND);
//			addIfNotPresent(STMT.getStyleClass(), JMetroStyleClass.TABLE_GRID_LINES);
//			addIfNotPresent(STMT.getStyleClass(), JMetroStyleClass.ALTERNATING_ROW_COLORS);
//
//			addIfNotPresent(Achive.getStyleClass(), JMetroStyleClass.TABLE_GRID_LINES);
//			addIfNotPresent(Achive.getStyleClass(), JMetroStyleClass.ALTERNATING_ROW_COLORS);

			FileTextArea.setEditable(false);
			FileTextAreaDB.setEditable(false);

			FileInputStream input = new FileInputStream(new File(System.getenv("TRANSACT_PATH") + "sw_mt.properties"));
			swift_mt = new Properties();
			swift_mt.load(new InputStreamReader(input, Charset.forName("UTF-8")));
			// System.out.println(System.getenv("SWIFT_MSG"));
			// System.out.println(System.getenv("SWIFT_ACK"));
			// System.out.println(System.getenv("SWIFT_KVT"));
			// System.out.println(System.getenv("SWIFT_OTHER"));
			// System.out.println(System.getenv("SWIFT_OUT"));
			// System.out.println(System.getenv("SWIFT_INLOCAL"));
			// System.out.println(System.getenv("SWIFT_OUTLOCAL"));

			// ______________________RadioButtonGroup_________________

			// Kvt; OutLocal; InLocal; Msg; Ack; Other

			// FileDate.setValue(LocalDate.now());

			WebView web = new WebView();
			WebEngine webEngine = web.getEngine();
			webEngine.loadContent(
					"<p style='margin-top:0cm;margin-right:0cm;margin-bottom:8.0pt;margin-left:0cm;line-height:107%;font-size:15px;font-family:\"Calibri\",sans-serif;'><u>ТИПЫ ФАЙЛОВ</u><u>:</u></p>\r\n"
							+ "<ol style=\"list-style-type: decimal;\">\r\n"
							+ "    <li><span style=\"color:#385723;\">xml &nbsp;=&nbsp;</span><span style=\"color:#385723;\">Протокол отправки (принятия)</span></li>\r\n"
							+ "    <li><span style=\"color:#C00000;\">nak &nbsp;=&nbsp;</span><span style=\"color:#C00000;\">Отвергнут</span></li>\r\n"
							+ "    <li><span style=\"color:#00B050;\">ack &nbsp;=&nbsp;</span><span style=\"color:#00B050;\">Принят</span></li>\r\n"
							+ "</ol>");
			Tooltip tip = new Tooltip();
			tip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			tip.setPrefSize(300, 200);
			tip.setGraphic(web);

			FileExtens.setTooltip(tip);

			FileExtens.getItems().addAll("xml", "ack", "nak");

			STMT.setRowFactory(tv -> {
				TableRow<SWIFT_FILES> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Open(null);
					}
				});
				return row;
			});

			// Group
			ToggleGroup group = new ToggleGroup();

			group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
					// Has selection.
					if (group.getSelectedToggle() != null) {
						RadioButton button = (RadioButton) group.getSelectedToggle();
						DIRNAME.getSelectionModel().select(button.getText());
					}
				}
			});
			Out.setToggleGroup(group);
			Kvt.setToggleGroup(group);
			OutLocal.setToggleGroup(group);
			InLocal.setToggleGroup(group);
			Msg.setToggleGroup(group);
			Ack.setToggleGroup(group);
			Other.setToggleGroup(group);
			// _____________________LOG______________________________
			ConsoleAppender console = new ConsoleAppender(); // create appender
			// configure the appender
			String PATTERN = "%d [%p|%c|%C{1}] %m%n";
			console.setLayout(new PatternLayout(PATTERN));
			console.setThreshold(Level.DEBUG);
			console.activateOptions();
			// add appender to any Logger (here is root)
			org.apache.log4j.Logger.getRootLogger().addAppender(console);

			FileAppender fa = new FileAppender();
			fa.setName("SWIFT");
			fa.setFile("SWIFT.log");
			fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
			fa.setThreshold(Level.DEBUG);
			fa.setAppend(true);
			fa.activateOptions();

			// add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fa);
			// repeat with all other desired appenders
			// ______________________________________________________
			// Перемещение по Tab-ам
			{
				RootTab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
					@Override
					public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {

						if (t1.getId().equals("INOUT")) {
							// System.out.println("Start task!!!!! " + t1.getId());
							// st.cancel();
							// RunProcess("INOUT");
						} else {
							// System.out.println("Closed task!!!!! " + t1.getId());
							// st.cancel();
						}
					}
				});
			}
			DIRNAME.getItems().addAll("Msg", "Out", "Ack", "Kvt", "Other", "InLocal", "OutLocal");
			DIRNAME.getSelectionModel().select(0);
			FolderName = "SWIFT_MSG";
			INOUT.setText("Входящие");
			FolderN.setText("Входящие документы ВТБ " + System.getenv("SWIFT_MSG"));
//			{
//				InputStream svgFile = getClass().getResourceAsStream("/search_swift.svg");
//				SvgLoader loader = new SvgLoader();
//				Group svgImage = loader.loadSvg(svgFile);
//				svgImage.setScaleX(0.05);
//				svgImage.setScaleY(0.05);
//				Group graphic = new Group(svgImage);
//				RefreshDB.setGraphic(graphic);
//			}
//			{
//				InputStream svgFile = getClass().getResourceAsStream("/file.svg");
//				SvgLoader loader = new SvgLoader();
//				Group svgImage = loader.loadSvg(svgFile);
//				svgImage.setScaleX(0.4);
//				svgImage.setScaleY(0.4);
//				Group graphic = new Group(svgImage);
//				ModeINbox.setGraphic(graphic);
//			}
			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			STMT.setEditable(true);
			Achive.setEditable(true);

			Achive.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					SWIFT_FILES sw = Achive.getSelectionModel().getSelectedItem();
					if (sw != null) {
						try {
							PreparedStatement prp = conn
									.prepareStatement("select SWFILE from SWIFT_FILES t where t.ID = ?");
							prp.setInt(1, sw.getID());
							ResultSet rs = prp.executeQuery();
							if (rs.next()) {
								Blob blob = rs.getBlob("SWFILE");
								int blobLength = (int) blob.length();
								byte[] blobAsBytes = blob.getBytes(1, blobLength);
								// release the blob and free up memory. (since JDBC 4.0)
								blob.free();
								FileTextAreaDB.setText(new String(blobAsBytes, StandardCharsets.UTF_8));
							}
							rs.close();
							prp.close();

						} catch (Exception e) {
							SWLogger.error(ExceptionUtils.getStackTrace(e));
							ErrorMessage(ExceptionUtils.getStackTrace(e));
						}
					}
				}
			});

			// При выборе строки, что бы не исчезало после
			// обновления_________________________________________________________________________-
			STMT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					selrow = STMT.getSelectionModel().getSelectedIndex();
				}
				if (newSelection != null) {
					// Заполнить поля платежа, если MT103!
					SWIFT_FILES sw = STMT.getSelectionModel().getSelectedItem();

					if (sw != null && (sw.getMTTYPE() != null
							&& (sw.getMTTYPE().equals("MT103") | sw.getMTTYPE().equals("MT202")))) {
						String TF = CDETAIL.getText().replace("\r\n", "");
						String GE = getMtDetail(sw.getPATH()).replace("\r\n", "");
						// System.out.println("TF=" + TF);
						// System.out.println("GE=" + GE);
						if (!TF.equals(GE)) {
							CDETAIL.setText(GE != null ? GE : "");
							PAY_NAME.setText(get50FNAME(sw.getPATH()) != null ? get50FNAME(sw.getPATH()) : "");
							PAY_ACC.setText(get50F(sw.getPATH()) != null ? get50F(sw.getPATH()) : "");
							REC_NAME.setText(get59NAME(sw.getPATH()) != null ? get59NAME(sw.getPATH()) : "");
							REC_ACC.setText(get59ACC(sw.getPATH()) != null ? get59ACC(sw.getPATH()) : "");
							PLAT_KORR.setText(get52CORR(sw.getPATH()) != null ? get52CORR(sw.getPATH()) : "");
							// System.out.println("~~~~~~~~~~~~");
						}
					} else {
						PLAT_KORR.setText("");
						CDETAIL.setText("");
						PAY_NAME.setText("");
						PAY_ACC.setText("");
						REC_NAME.setText("");
						REC_ACC.setText("");
					}
					// Файл
					if (sw != null) {
						try {
							InputStream is = new FileInputStream(sw.getPATH());
							BufferedReader buf = new BufferedReader(new InputStreamReader(is));
							String line = buf.readLine();
							StringBuilder sb = new StringBuilder();
							while (line != null) {
								sb.append(line).append("\n");
								line = buf.readLine();
							}
							if (FileTextArea != null && !FileTextArea.getText().equals(sb.toString())) {
								FileTextArea.setText(sb.toString());
							}
							is.close();
							buf.close();
						} catch (Exception e) {
							SWLogger.error(ExceptionUtils.getStackTrace(e));
							ErrorMessage(ExceptionUtils.getStackTrace(e));
						}
					} else {
						FileTextArea.setText("");
					}
				}
			});

			// Тип архива
			ArchType.getItems().addAll("IN", "OUT", "ВСЕ");

			SUMMdb.setColumnFilter(new ComparableColumnFilter(new ComparableFilterModel(rules),
					TextFormatterFactory.LONG_TEXTFORMATTER_FACTORY));

			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
			SUMMdb.setCellFactory(tc -> new TableCell<SWIFT_FILES, Double>() {

				@Override
				protected void updateItem(Double price, boolean empty) {
					super.updateItem(price, empty);
					if (empty) {
						setText(null);
					} else {
						setText(currencyFormat.format(price));
					}
				}
			});
			SUMMdb.setCellValueFactory(cellData -> cellData.getValue().SUMMProperty().asObject());

			// *****************************Архив IN-OUT********************
			DT_CHdb.setCellValueFactory(cellData -> cellData.getValue().DT_CHProperty());
			DOCDATEdb.setCellValueFactory(cellData -> cellData.getValue().DOCDATEProperty());
			FILE_NAMEdb.setCellValueFactory(cellData -> cellData.getValue().FILENAMEProperty());
			MTTYPEdb.setCellValueFactory(cellData -> cellData.getValue().MTTYPEProperty());
			MTNAMEdb.setCellValueFactory(cellData -> cellData.getValue().MTNAMEProperty());
			CURdb.setCellValueFactory(cellData -> cellData.getValue().CURProperty());

			Typedb.setCellValueFactory(cellData -> cellData.getValue().VECTORProperty());
			OPERdb.setCellValueFactory(cellData -> cellData.getValue().OPERProperty());
			REFdb.setCellValueFactory(cellData -> cellData.getValue().REFProperty());

			DOCDATEdb.setColumnFilter(new DateColumnFilter<>());
			FILE_NAMEdb.setColumnFilter(new PatternColumnFilter<>());
			OPERdb.setColumnFilter(new PatternColumnFilter<>());
			Typedb.setColumnFilter(new PatternColumnFilter<>());

			CURdb.setColumnFilter(new PatternColumnFilter<>());
			MTNAMEdb.setColumnFilter(new PatternColumnFilter<>());
			MTTYPEdb.setColumnFilter(new PatternColumnFilter<>());
			REFdb.setColumnFilter(new PatternColumnFilter<>());
			// Редактирование
			DOCDATEdb.setCellFactory(
					TextFieldTableCell.<SWIFT_FILES, LocalDate>forTableColumn(new LocalDateStringConverter()));
			MTTYPEdb.setCellFactory(TextFieldTableCell.forTableColumn());
			MTNAMEdb.setCellFactory(TextFieldTableCell.forTableColumn());
			CURdb.setCellFactory(TextFieldTableCell.forTableColumn());

			SUMMdb.setCellFactory(TextFieldTableCell.<SWIFT_FILES, Double>forTableColumn(new DoubleStringConverter()));

			FILE_NAMEdb.setCellFactory(TextFieldTableCell.forTableColumn());
			DT_CHdb.setCellFactory(
					TextFieldTableCell.<SWIFT_FILES, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));
			Typedb.setCellFactory(TextFieldTableCell.forTableColumn());

			DOCDATEdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, LocalDate>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, LocalDate> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setDOCDATE(t.getNewValue());
				}
			});

			Typedb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setVECTOR(t.getNewValue());
				}
			});

			SUMMdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, Double>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, Double> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setSUMM(t.getNewValue());
				}
			});

			MTTYPEdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setMTTYPE(t.getNewValue());
				}
			});
			MTNAMEdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setMTNAME(t.getNewValue());
				}
			});
			CURdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setCUR(t.getNewValue());
				}
			});
			DT_CHdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, LocalDateTime>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, LocalDateTime> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setDT_CH(t.getNewValue());
				}
			});

			FILE_NAMEdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
					((SWIFT_FILES) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setFILENAME(t.getNewValue());
				}
			});

			DateFormatDTTM(DT_CHdb);
			DateFormatDT(DOCDATEdb);
			// ________________________________________________________
			// *****************************Транзит********************
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

			// SUMM.setCellFactory(TextFieldTableCell.forTableColumn());

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
			DateFormatDT(DOCDATE);
			// ________________________________________________________
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
			 * Auto Refresh Запуск задачи через одну секунду
			 */
//			RunProcess("INOUT");
			SWIFT_VTB.setDisable(true);
			BK_VTB.setDisable(true);
		} catch (Exception e) {
			SWLogger.error(ExceptionUtils.getStackTrace(e));
			ErrorMessage(ExceptionUtils.getStackTrace(e));
		}
	}

	@FXML
	void RefreshTable(ActionEvent event) {
		InitTable();
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
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Обновить Из базы
	 * 
	 * @param event
	 */
	@FXML
	void RefreshDB(ActionEvent event) {
		try {
//			RootTab.setDisable(true);
//			PrgInd.setVisible(true);
//			Task<Object> task = new Task<Object>() {
//				@Override
//				public Object call() throws Exception {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			String dt1_ = "";
			String dt2_ = "";
			String in_out = "";

			if (dt1.getValue() != null) {
				dt1_ = "and trunc(CR_DT) >= to_date('" + dt1.getValue().format(formatter) + "','dd.mm.yyyy') \r\n";
			}

			if (dt2.getValue() != null) {
				dt2_ = "and trunc(CR_DT) <= to_date('" + dt2.getValue().format(formatter) + "','dd.mm.yyyy') \r\n";
			}

			if (ArchType.getValue() != null && !ArchType.getValue().equals("ВСЕ")) {
				in_out = "and upper(VECTOR) = '" + ArchType.getValue() + "' \r\n";
			}

			String selectStmt = "select id,\r\n" + "       filename,\r\n" + "       dt_ch,\r\n" + "       swfile,\r\n"
					+ "       oper,\r\n" + "       cr_dt,\r\n" + "       mttype,\r\n" + "       mtname,\r\n"
					+ "       cur,\r\n" + "       vector,\r\n" + "       nvl(summ, '') summ,\r\n"
					+ "       docdate,\r\n" + "       REF\r\n" + "  from SWIFT_FILES\r\n" + " where 1 = 1\r\n" + dt1_
					+ dt2_ + in_out + "order by CR_DT desc";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SWIFT_FILES> cus_list = FXCollections.observableArrayList();
			DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			while (rs.next()) {
				SWIFT_FILES list = new SWIFT_FILES();

				list.setDOCDATE((rs.getDate("DOCDATE") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("DOCDATE")), formatter)
						: null);
				list.setSUMM(rs.getDouble("SUMM"));
				list.setVECTOR(rs.getString("VECTOR"));
				list.setCUR(rs.getString("CUR"));
				list.setMTNAME(rs.getString("MTNAME"));
				list.setMTTYPE(rs.getString("MTTYPE"));
				list.setCR_DT((rs.getDate("CR_DT") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("CR_DT")), dtformatter) : null);
				list.setOPER(rs.getString("OPER"));
				list.setDT_CH((rs.getDate("DT_CH") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("DT_CH")), dtformatter) : null);
				list.setFILENAME(rs.getString("FILENAME"));
				list.setID(rs.getInt("ID"));
				list.setREF(rs.getString("REF"));
				cus_list.add(list);
			}
			prepStmt.close();
			rs.close();
			Achive.setItems(cus_list);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					TableFilter<SWIFT_FILES> tableFilter = TableFilter.forTableView(Achive).apply();
					tableFilter.setSearchStrategy((input, target) -> {
						try {
							return target.toLowerCase().contains(input.toLowerCase());
						} catch (Exception e) {
							return false;
						}
					});
				}
			});
			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
			SUMMdb.setCellFactory(tc -> new TableCell<SWIFT_FILES, Double>() {

				@Override
				protected void updateItem(Double price, boolean empty) {
					super.updateItem(price, empty);
					if (empty) {
						setText(null);
					} else {
						setText(currencyFormat.format(price));
					}
				}
			});
//					return null;
//				}
//			};
//			task.setOnFailed(e -> {
//				ErrorMessage(task.getException().getMessage());
//				SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
//			});
//			task.setOnSucceeded(e -> {
//				try {
//					RootTab.setDisable(false);
//					PrgInd.setVisible(false);
//				} catch (Exception e1) {
//					ErrorMessage(e1.getMessage());
//					SWLogger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
//				}
//			});
//			exec.execute(task);

		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	void CellDateFormatD(XTableColumn<VTB_MT202_CONV, LocalDateTime> tc) {
		tc.setCellFactory(column -> {
			TableCell<VTB_MT202_CONV, LocalDateTime> cell = new TableCell<VTB_MT202_CONV, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null) {
							setText(format.format(item));
						}
					}
				}
			};
			return cell;
		});
	}

	@FXML
	void REFR_CONV(ActionEvent event) {
		try {
//			RootTab.setDisable(true);
//			PrgInd.setVisible(true);
//			Task<Object> task = new Task<Object>() {
//				@Override
//				public Object call() throws Exception {

			PreparedStatement prepStmt = conn.prepareStatement("select * from VTB_MT202_CONV t order by ID desc");
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<VTB_MT202_CONV> cus_list = FXCollections.observableArrayList();
			DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			while (rs.next()) {
				VTB_MT202_CONV list = new VTB_MT202_CONV();
				list.setTRN_ANUM(rs.getInt("TRN_ANUM"));
				//list.setTRN_NUM(rs.getInt("TRN_NUM"));
				list.setREF(rs.getString("REF"));
				list.setF21(rs.getString("F21"));
				//list.setFL32A_SUM(rs.getString("FL32A_SUM"));
				list.setFL32A_CUR(rs.getString("FL32A_CUR"));
				list.setFL32A_DATE(rs.getString("FL32A_DATE"));
				list.setF53B(rs.getString("F53B"));
				list.setF58A(rs.getString("F58A"));
				list.setF72(rs.getString("F72"));
				list.setID(rs.getInt("ID"));
				list.setDATETIME((rs.getDate("DATETIME") != null)
						? LocalDateTime.parse(
								new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("DATETIME")), dtformatter)
						: null);
				list.setOPER(rs.getString("OPER"));
				list.setFL58A_DETAIL(rs.getString("FL58A_DETAIL"));

				cus_list.add(list);
			}
			prepStmt.close();
			rs.close();
			CONV_TBL.setItems(cus_list);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					TableFilter<VTB_MT202_CONV> tableFilter = TableFilter.forTableView(CONV_TBL).apply();
					tableFilter.setSearchStrategy((input, target) -> {
						try {
							return target.toLowerCase().contains(input.toLowerCase());
						} catch (Exception e) {
							return false;
						}
					});
				}
			});

//					return null;
//				}
//			};
//			task.setOnFailed(e -> {
//				ErrorMessage(task.getException().getMessage());
//				SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
//			});
//			task.setOnSucceeded(e -> {
//				try {
//					RootTab.setDisable(false);
//					PrgInd.setVisible(false);
//				} catch (Exception e1) {
//					ErrorMessage(e1.getMessage());
//					SWLogger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
//				}
//			});
//			exec.execute(task);

		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
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
	public void EndTask_() {
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
	String getMT_2(String MT, String Col) {
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
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * Получить название MT и тип, из properties файла
	 * 
	 * @param MT
	 * @param Col
	 * @return
	 */
	String getMT(String MT, String Col) {
		String ret = null;
		try {
			if (Col.equals("NAME") & (MT != null && !MT.equals(""))) {
				ret = swift_mt.getProperty(MT);
				if (ret == null) {
					for (Map.Entry<Object, Object> entry : swift_mt.entrySet()) {
						if (entry.getKey().toString().contains(MT)) {
							ret = entry.getValue().toString();
						}
					}
				}
			} else if (Col.equals("TYPE") & (MT != null && !MT.equals(""))) {
				// ret = MT;
				for (Map.Entry<Object, Object> entry : swift_mt.entrySet()) {
					if (entry.getKey().toString().contains(MT)) {
						ret = entry.getKey().toString();
					}
				}
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * Название переменной среды для каталогов
	 */
	String FolderName;
	/**
	 * Выделенная строка
	 */
	Integer selrow;

	/**
	 * Инициализация данных
	 */
	void InitTable() {
		try {
			if (System.getenv(FolderName) != null) {

				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				SimpleDateFormat formatdt = new SimpleDateFormat("dd.MM.yyyy");
				DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

				// MSG or another folder
				File dir = new File(System.getenv(FolderName));
				File[] directoryListing = dir.listFiles();

				Arrays.sort(directoryListing, Comparator.comparingLong(File::lastModified).reversed());

				ObservableList<SWIFT_FILES> dlist = FXCollections.observableArrayList();
				Boolean ifchk = false;
				if (directoryListing != null) {
					for (File child : directoryListing) {
						Path filePath = child.toPath();
						// Атрибуты файла...
						BasicFileAttributes attr = java.nio.file.Files.readAttributes(filePath,
								BasicFileAttributes.class);
						// если файл и если существует
						if ((child.isFile() & child.exists()) & ((FileDate.getValue() != null &&
						// FileDate.getValue().equals(LocalDate.parse(formatdt.format(new
						// Date(attr.creationTime().toMillis())), formatter))
								(FileDate.getValue().compareTo(LocalDate.parse(
										formatdt.format(new Date(attr.creationTime().toMillis())), formatter)) <= 0)

						) | FileDate.getValue() == null) & ((DT2.getValue() != null &&
						// FileDate.getValue().equals(LocalDate.parse(formatdt.format(new
						// Date(attr.creationTime().toMillis())), formatter))
								(DT2.getValue().compareTo(LocalDate.parse(
										formatdt.format(new Date(attr.creationTime().toMillis())), formatter)) >= 0)

						) | DT2.getValue() == null)
								& ((FileExtens.getValue() != null
										& getFExt(child.getAbsolutePath()).toLowerCase().equals(FileExtens.getValue()))
										| FileExtens.getValue() == null)) {

							SWIFT_FILES list = new SWIFT_FILES();
							list.setFILENAME(child.getName());
							list.setDT_CH(LocalDateTime.parse(format.format(new Date(attr.creationTime().toMillis())),
									formatterwt));
							list.setCUR(getMtCur(child.getAbsolutePath()));

							String summs = getMtAmount(child.getAbsolutePath());
							if (summs != null) {
								Double summ = Double.parseDouble(summs.replace(",", ".").trim());
								if (summ != null) {
									list.setSUMM(summ);
								}
							}

							list.setMTTYPE(getMT(getMtType(child.getAbsolutePath()), "TYPE"));
							list.setMTNAME(getMT(getMtType(child.getAbsolutePath()), "NAME"));

							list.setDOCDATE((getMtDate(child.getAbsolutePath()) != null)
									? LocalDate.parse(getMtDate(child.getAbsolutePath()), formatter)
									: null);

							list.setPATH(child.getAbsolutePath());
							// Перебор отмеченных
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
					Platform.runLater(() -> {
						try {
							STMT.setItems(dlist);
							if (selrow != null) {
								STMT.getSelectionModel().select(selrow);
								STMT.getFocusModel().focus(selrow);
							}

							autoResizeColumns(STMT);
							TableFilter<SWIFT_FILES> tableFilter = TableFilter.forTableView(STMT).apply();
							tableFilter.setSearchStrategy((input, target) -> {
								try {
									return target.toLowerCase().contains(input.toLowerCase());
								} catch (Exception e) {
									return false;
								}
							});

							NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
							SUMM.setCellFactory(tc -> new TableCell<SWIFT_FILES, Double>() {

								@Override
								protected void updateItem(Double price, boolean empty) {
									super.updateItem(price, empty);
									if (empty) {
										setText(null);
									} else {
										setText(currencyFormat.format(price));
									}
								}
							});
							// clear
							// dlist.clear();
						} catch (Exception e) {
							ErrorMessage(ExceptionUtils.getStackTrace(e));
							SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
						}
					});
					// autoResizeColumns(STMT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
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
						if (column_.getCellData(i) != null) {
							t = new Text(column_.getCellData(i).toString());
							double calcwidth = t.getLayoutBounds().getWidth();
							// remember new max-width
							if (calcwidth > max) {
								max = calcwidth;
							}
						}
					}
				}
				// set the new max-widht with some extra space
				column_.setPrefWidth(max + 20.0d);
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
	 * @throws UnknownHostException 
	 */
	private void dbConnect() throws UnknownHostException {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			Properties props = new Properties();
			props.setProperty("password", Connect.userPassword_);
			props.setProperty("user", Connect.userID_);
			props.put("v$session.osuser", System.getProperty("user.name").toString());
			props.put("v$session.machine", InetAddress.getLocalHost().getCanonicalHostName());
			props.put("v$session.program", getClass().getName());
			conn  = DriverManager.getConnection("jdbc:oracle:thin:@" + Connect.connectionURL_, props);
			
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
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
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	@SuppressWarnings("unused")
	private Executor exec;

	/**
	 * Печать из файла
	 * 
	 * @param event
	 */
	@FXML
	private void Open(ActionEvent event) {
		try {
			if (STMT.getSelectionModel().getSelectedItem() != null /* & !FolderName.equals("SWIFT_OUT") */) {
//				RootTab.setDisable(true);
//				PrgInd.setVisible(true);
//				Task<Object> task = new Task<Object>() {
//					@Override
//					public Object call() throws Exception {
				SWIFT_FILES selrow = STMT.getSelectionModel().getSelectedItem();
				InputStream inputstream = new FileInputStream(System.getenv(FolderName) + "/" + selrow.getFILENAME());

				new SwiftPrintFile().showReport(inputstream);
//
//						return null;
//					}
//				};
//				task.setOnFailed(e -> {
//					ErrorMessage(task.getException().getMessage());
//					SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
//				});
//				task.setOnSucceeded(e -> {
//					try {
//						RootTab.setDisable(false);
//						PrgInd.setVisible(false);
//					} catch (Exception e1) {
//						ErrorMessage(e1.getMessage());
//						SWLogger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
//					}
//				});
//				exec.execute(task);
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * Печать из базы
	 * 
	 * @param event
	 */
	@FXML
	private void OpenDB(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() != null) {
//				RootTab.setDisable(true);
//				PrgInd.setVisible(true);
//				Task<Object> task = new Task<Object>() {
//					@Override
//					public Object call() throws Exception {

				SWIFT_FILES selrow = Achive.getSelectionModel().getSelectedItem();

				String sel = "select SWFILE from SWIFT_FILES where ID = ?";
				PreparedStatement prepStmt = conn.prepareStatement(sel);
				prepStmt.setInt(1, selrow.getID());
				ResultSet rs = prepStmt.executeQuery();

				if (rs.next()) {
					Blob blob = rs.getBlob("SWFILE");
					int blobLength = (int) blob.length();
					byte[] blobAsBytes = blob.getBytes(1, blobLength);
					// release the blob and free up memory. (since JDBC 4.0)
					blob.free();
					InputStream targetStream = new ByteArrayInputStream(blobAsBytes);
					new SwiftPrintFile().showReport(targetStream);
					targetStream.close();
				}
				rs.close();
				prepStmt.close();

//						return null;
//					}
//				};
//				task.setOnFailed(e -> {
//					ErrorMessage(task.getException().getMessage());
//					SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
//				});
//				task.setOnSucceeded(e -> {
//					try {
//						RootTab.setDisable(false);
//						PrgInd.setVisible(false);
//
//					} catch (Exception e1) {
//						e1.printStackTrace();
//						ErrorMessage(e1.getMessage());
//						Main.logger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
//					}
//				});
//				exec.execute(task);
			}
		} catch (Exception e) {
			ErrorMessage(ExceptionUtils.getStackTrace(e));
			SWLogger.error(ExceptionUtils.getStackTrace(e) + "~" + Thread.currentThread().getName());
		}

	}
}

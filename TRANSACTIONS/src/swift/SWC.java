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
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;

import com.google.common.io.Files;
import com.prowidesoftware.swift.model.field.Field32A;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;

import afester.javafx.svg.SvgLoader;
import app.Main;
import app.model.Connect;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
 * SWIFT
 * 
 * 07.11.2020
 * 
 * @author Said
 *
 */
public class SWC {
	/* __________������_���_��������_�����������__________ */
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

	/**
	 * ����������� ���� ��������
	 */
	@FXML
	private Text FolderN;

	/**
	 * ���� �
	 */
	@FXML
	private DatePicker dt1;

	/**
	 * ���� ��
	 */
	@FXML
	private DatePicker dt2;
	/**
	 * ��� ������
	 */
	@FXML
	private Tab ArchiveInOut;

	/**
	 * ��� TabPane
	 */
	@FXML
	private TabPane RootTab;

	/**
	 * ������ ��������� ������, ���� �� �������� �����
	 */
	@FXML
	private Button ModeINbox;

	/**
	 * ����� �� ����
	 */
	@FXML
	private Button RefreshDB;

	/**
	 * ��������� �������� ���������
	 */
	@FXML
	private StackPane StPn;

	/**
	 * ��������� ���������� ��������
	 */
	@FXML
	private ProgressIndicator PrgInd;

	/**
	 * ��� �������
	 */
	@FXML
	private Tab INOUT;
	/**
	 * �� ������������, ��� �����
	 */
	@FXML
	private ComboBox<String> DIRNAME;

	/**
	 * ��� ������
	 */
	@FXML
	private ComboBox<String> ArchType;

	/**
	 * �������� �� ������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, Boolean> CHK;

	/**
	 * ������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> CUR;

	/**
	 * ����� ���������, ���� MT103...
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> SUMM;

	/**
	 * �������� ��������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> MTNAME;

	/**
	 * MT.., �� ����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> MTTYPE;

	/**
	 * ���� �������� �����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, LocalDateTime> DT_CH;

	/**
	 * ���� ���������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, LocalDate> DOCDATE;

	/**
	 * �������� �����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> FILE_NAME;

	/*---------------------------*/
	/**
	 * ������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> CURdb;

	/**
	 * ����� ���������, ���� MT103...
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> SUMMdb;

	/**
	 * �������� ��������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> MTNAMEdb;

	/**
	 * MT.., �� ����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> MTTYPEdb;

	/**
	 * ���� �������� �����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, LocalDateTime> DT_CHdb;

	/**
	 * ���� ���������
	 */
	@FXML
	private TableColumn<SWIFT_FILES, LocalDate> DOCDATEdb;

	/**
	 * �������� �����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> FILE_NAMEdb;

	/**
	 * IN-OUT ����
	 */
	@FXML
	private TableColumn<SWIFT_FILES, String> Typedb;

	/**
	 * ������� ��-��� ���������
	 */
	@FXML
	private TableView<SWIFT_FILES> STMT;

	/**
	 * ������� ������
	 */
	@FXML
	private TableView<SWIFT_FILES> Achive;

	/**
	 * ������� ����� ��������� , ���� MT103...����
	 * 
	 * @param path
	 * @return
	 */
	String getMtAmount(String path) {
		String ret = null;
		try {
			InputStream inputstream = new FileInputStream(path);
			AbstractMT msg = AbstractMT.parse(inputstream);
			if (msg.isType(103)) {
				MT103 mt = (MT103) msg;
				Field32A f = mt.getField32A();
				ret = f.getAmount();
			}
			inputstream.close();
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * ������� ������ , ���� MT103...����
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
			inputstream.close();
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * ������� ������ , ���� MT103...����
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
			inputstream.close();
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * ��� �����
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
			inputstream.close();
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	String InsertDB(LocalDate DOCDATE_, String SUMM_, String CUR_, String MTNAME_, String MTTYPE_, LocalDateTime DT_CH_,
			String FILENAME_, String VECTOR) {
		String ret = "error";
		try {
			CallableStatement clstmt = conn.prepareCall("{ ? = call z_sb_mmbank.VTB_SWIFT(?,?,?,?,?,?,?,?,?,?,?) }");
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
		} catch (SQLException | IOException e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * ��������� ���� � ��������� ���������� �� ����, �� ���������� �������
	 * 
	 * @param event
	 */
	@FXML
	void LoadFileDB(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() == null) {
				ErrorMessage("�������� ������");
			} else {
				SWIFT_FILES st = Achive.getSelectionModel().getSelectedItem();
				String mes = "";
				if (st.getVECTOR().equals("IN")) {
					mes = "����������� � ��������� ����������?";
				} else if (st.getVECTOR().equals("OUT")) {
					mes = "������������� � ���?";
				}
				Stage stage = (Stage) Achive.getScene().getWindow();
				Label alert = new Label(mes);
				alert.setLayoutX(10.0);
				alert.setLayoutY(11.0);
				alert.setPrefHeight(17.0);

				Button no = new Button();
				no.setText("���");
				no.setLayoutX(111.0);
				no.setLayoutY(56.0);
				no.setPrefWidth(72.0);
				no.setPrefHeight(21.0);

				Button yes = new Button();
				yes.setText("��");
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
								ErrorMessage(e.getMessage());
								SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
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
								ErrorMessage(e.getMessage());
								SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
							}
						}
						newWindow_yn.close();
					}
				});
				newWindow_yn.setTitle("��������");
				newWindow_yn.setScene(ynScene);
				newWindow_yn.initModality(Modality.WINDOW_MODAL);
				newWindow_yn.initOwner(stage);
				newWindow_yn.setResizable(false);
				newWindow_yn.getIcons().add(new Image("/icon.png"));
				newWindow_yn.show();
			}
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * ��������� ���� � ��������� ���������� � �������� � ����, �� ����������
	 * �������
	 * 
	 * ��� ������ ��������...
	 * 
	 * @param event
	 */
	@FXML
	void LoadFile(ActionEvent event) {
		try {

			StPn.setDisable(true);
			PrgInd.setVisible(true);
			Task<Object> task = new Task<Object>() {
				@Override
				public Object call() throws Exception {
					LocalDate docdt = null;
					LocalDateTime crdate = null;
					String amount = null;
					String cur = null;
					String docname = null;
					String doctype = null;
					String filename = null;
					String ret = null;
					// ���� �� �������
					for (int i = 0; i < STMT.getItems().size(); i++) {
						// ���� �� ��������
						for (int j = 0; j < STMT.getColumns().size(); j++) {
							// ���� �� �����
							if (STMT.getColumns().get(j).getCellData(i) != null) {
								// ���� �������� ������
								if (j == 0) {
									if ((Boolean) STMT.getColumns().get(j).getCellData(i) == true) {
										// ������������� ����������
										docdt = null;
										crdate = null;
										amount = null;
										cur = null;
										docname = null;
										doctype = null;
										filename = null;
										ret = null;
										// �������� �� ������� ������
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
										// ���� ����� ��������
										if (FolderName.equals("SWIFT_MSG") | FolderName.equals("SWIFT_ACK")
												| FolderName.equals("SWIFT_KVT") || FolderName.equals("SWIFT_OTHER")) {
											ret = InsertDB(docdt, amount, cur, docname, doctype, crdate, filename,
													"IN");
											if (ret.equals("ok")) {
												File destinationFolder = new File(System.getenv("SWIFT_IN") + "/"
														+ STMT.getColumns().get(6).getCellData(i));
												File sourceFolder = new File(System.getenv(FolderName) + "/"
														+ STMT.getColumns().get(6).getCellData(i));
												try {
													moveFileWithOverwrite(sourceFolder, destinationFolder);
													InitTable();
													conn.commit();
												} catch (IOException e) {
													conn.rollback();
													SWLogger.error(
															e.getMessage() + "~" + Thread.currentThread().getName());
													ErrorMessage(e.getMessage());
												}
											} else {
												ErrorMessage(ret);
											}
											// ���� ����� ���������
										} else if (FolderName.equals("SWIFT_OUTLOCAL")) {
											ret = InsertDB(docdt, amount, cur, docname, doctype, crdate, filename,
													"OUT");
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
													SWLogger.error(
															e.getMessage() + "~" + Thread.currentThread().getName());
													ErrorMessage(e.getMessage());
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
					return null;
				}
			};
			task.setOnFailed(e -> {

				ErrorMessage(task.getException().getMessage());
				SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
			});
			task.setOnSucceeded(e -> {
				try {
					StPn.setDisable(false);
					PrgInd.setVisible(false);
				} catch (Exception e1) {
					ErrorMessage(e1.getMessage());
					SWLogger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
				}
			});
			exec.execute(task);

		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
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
	 * �������������� �������� DTTM
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
	 * �������������� �������� DT
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
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return null;
	}

	/**
	 * ������ �����
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
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return null;
	}

	/**
	 * �������� �������� ������� ������ �� ���� �����
	 * 
	 * @param event
	 */
	@FXML
	private void ChTabName(ActionEvent event) {
		try {

			if (DIRNAME.getValue().toUpperCase().equals("MSG")) {

				INOUT.setText("��������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("�������� ��������� ��� " + System.getenv("SWIFT_MSG"));

			} else if (DIRNAME.getValue().toUpperCase().equals("ACK")) {

				INOUT.setText("��������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("�������� ��������� ��� " + System.getenv("SWIFT_ACK"));

			} else if (DIRNAME.getValue().toUpperCase().equals("KVT")) {

				INOUT.setText("��������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("�������� ��������� ��� ��� ��������� " + System.getenv("SWIFT_KVT"));

			} else if (DIRNAME.getValue().toUpperCase().equals("OTHER")) {

				INOUT.setText("��������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("�������� ��������� ���, ������=" + System.getenv("SWIFT_OTHER"));

			} else if (DIRNAME.getValue().toUpperCase().equals("OUT")) {

				INOUT.setText("���������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(true);

				FolderN.setText("��������� ��������� ��� " + System.getenv("SWIFT_OUT"));
			} else if (DIRNAME.getValue().toUpperCase().equals("INLOCAL")) {

				INOUT.setText("��������,��������� �������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(true);

				FolderN.setText("��������,��������� ������� " + System.getenv("SWIFT_INLOCAL"));
			}

			else if (DIRNAME.getValue().toUpperCase().equals("OUTLOCAL")) {

				INOUT.setText("���������, ��������� �������");
				FolderName = "SWIFT_" + DIRNAME.getValue().toUpperCase();
				ModeINbox.setDisable(false);

				FolderN.setText("���������, ��������� ������� " + System.getenv("SWIFT_OUTLOCAL"));
			}

		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}

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
	 * �������� �����
	 */
	void onclose() {
		Stage stage = (Stage) StPn.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	/**
	 * ������ ��������
	 */
	void RunProcess(String type) {
		Timer time = new Timer(); // Instantiate Timer Object
		st = new ScheduledTask(); // Instantiate SheduledTask class
		st.setSWC(this, type);
		time.schedule(st, 0, 1000); // Create task repeating every 1 sec
	}

	void ErrorMessage(String mes) {
		try {
			Platform.runLater(() -> {
				try {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("terminal.png"));
					alert.setTitle("��������");
					alert.setHeaderText(null);
					alert.setContentText(mes);
					alert.showAndWait();
				} catch (Exception e) {
					SWLogger.error(e.getMessage());
				}
			});
		} catch (Exception e) {
			SWLogger.error(e.getMessage());
		}
	}

	/**
	 * �����������
	 */
	Logger SWLogger = Logger.getLogger(getClass());

	/**
	 * �������������
	 */
	@FXML
	private void initialize() {
		try {

			// System.out.println(System.getenv("SWIFT_MSG"));
			// System.out.println(System.getenv("SWIFT_ACK"));
			// System.out.println(System.getenv("SWIFT_KVT"));
			// System.out.println(System.getenv("SWIFT_OTHER"));
			// System.out.println(System.getenv("SWIFT_OUT"));
			// System.out.println(System.getenv("SWIFT_INLOCAL"));
			// System.out.println(System.getenv("SWIFT_OUTLOCAL"));

			// ______________________RadioButtonGroup_________________

			// Kvt; OutLocal; InLocal; Msg; Ack; Other

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
			// ����������� �� Tab-��
			{
				RootTab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
					@Override
					public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {

						if (t1.getId().equals("INOUT")) {
							// System.out.println("Start task!!!!! " + t1.getId());
							st.cancel();
							RunProcess("INOUT");
						} else {
							// System.out.println("Closed task!!!!! " + t1.getId());
							st.cancel();
						}
					}
				});
			}
			DIRNAME.getItems().addAll("Msg", "Out", "Ack", "Kvt", "Other", "InLocal", "OutLocal");
			DIRNAME.getSelectionModel().select(0);
			FolderName = "SWIFT_MSG";
			INOUT.setText("��������");
			FolderN.setText("�������� ��������� ��� " + System.getenv("SWIFT_MSG"));
			{
				InputStream svgFile = getClass().getResourceAsStream("/search_swift.svg");
				SvgLoader loader = new SvgLoader();
				Group svgImage = loader.loadSvg(svgFile);
				svgImage.setScaleX(0.05);
				svgImage.setScaleY(0.05);
				Group graphic = new Group(svgImage);
				RefreshDB.setGraphic(graphic);
			}
			{
				InputStream svgFile = getClass().getResourceAsStream("/file.svg");
				SvgLoader loader = new SvgLoader();
				Group svgImage = loader.loadSvg(svgFile);
				svgImage.setScaleX(0.4);
				svgImage.setScaleY(0.4);
				Group graphic = new Group(svgImage);
				ModeINbox.setGraphic(graphic);
			}
			exec = Executors.newCachedThreadPool((runnable) -> {
				Thread t = new Thread(runnable);
				t.setDaemon(true);
				return t;
			});

			STMT.setEditable(true);
			Achive.setEditable(true);

			dbConnect();

			// ��� ������ ������, ��� �� �� �������� ����� ����������
			STMT.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					selrow = STMT.getSelectionModel().getSelectedIndex();
				}
			});
			// ��� ������
			ArchType.getItems().addAll("IN", "OUT", "���");
			// *****************************����� IN-OUT********************
			DT_CHdb.setCellValueFactory(cellData -> cellData.getValue().DT_CHProperty());
			DOCDATEdb.setCellValueFactory(cellData -> cellData.getValue().DOCDATEProperty());
			FILE_NAMEdb.setCellValueFactory(cellData -> cellData.getValue().FILENAMEProperty());
			MTTYPEdb.setCellValueFactory(cellData -> cellData.getValue().MTTYPEProperty());
			MTNAMEdb.setCellValueFactory(cellData -> cellData.getValue().MTNAMEProperty());
			CURdb.setCellValueFactory(cellData -> cellData.getValue().CURProperty());
			SUMMdb.setCellValueFactory(cellData -> cellData.getValue().SUMMProperty());
			Typedb.setCellValueFactory(cellData -> cellData.getValue().VECTORProperty());
			// ��������������
			DOCDATEdb.setCellFactory(
					TextFieldTableCell.<SWIFT_FILES, LocalDate>forTableColumn(new LocalDateStringConverter()));
			MTTYPEdb.setCellFactory(TextFieldTableCell.forTableColumn());
			MTNAMEdb.setCellFactory(TextFieldTableCell.forTableColumn());
			CURdb.setCellFactory(TextFieldTableCell.forTableColumn());
			SUMMdb.setCellFactory(TextFieldTableCell.forTableColumn());
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

			SUMMdb.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
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
			// *****************************�������********************
			CHK.setCellValueFactory(cellData -> cellData.getValue().CHKProperty());
			DT_CH.setCellValueFactory(cellData -> cellData.getValue().DT_CHProperty());
			DOCDATE.setCellValueFactory(cellData -> cellData.getValue().DOCDATEProperty());
			FILE_NAME.setCellValueFactory(cellData -> cellData.getValue().FILENAMEProperty());
			MTTYPE.setCellValueFactory(cellData -> cellData.getValue().MTTYPEProperty());
			MTNAME.setCellValueFactory(cellData -> cellData.getValue().MTNAMEProperty());
			CUR.setCellValueFactory(cellData -> cellData.getValue().CURProperty());
			SUMM.setCellValueFactory(cellData -> cellData.getValue().SUMMProperty());
			// ��������������
			DOCDATE.setCellFactory(
					TextFieldTableCell.<SWIFT_FILES, LocalDate>forTableColumn(new LocalDateStringConverter()));
			MTTYPE.setCellFactory(TextFieldTableCell.forTableColumn());
			MTNAME.setCellFactory(TextFieldTableCell.forTableColumn());
			CUR.setCellFactory(TextFieldTableCell.forTableColumn());
			SUMM.setCellFactory(TextFieldTableCell.forTableColumn());
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

			SUMM.setOnEditCommit(new EventHandler<CellEditEvent<SWIFT_FILES, String>>() {
				@Override
				public void handle(CellEditEvent<SWIFT_FILES, String> t) {
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
			 * Auto Refresh ������ ������ ����� ���� �������
			 */
			RunProcess("INOUT");
		} catch (Exception e) {

			SWLogger.error(e.getMessage());
			ErrorMessage(e.getMessage());
		}
	}

	/**
	 * ��������� CRON, �� ������������
	 */
	String CRON_EXPRESSION = "*/10 * * ? * * *";

	/**
	 * ��������
	 * 
	 * @param event
	 */
	@FXML
	void Refresh(ActionEvent event) {
		try {
			InitTable();
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * �������� �� ����
	 * 
	 * @param event
	 */
	@FXML
	void RefreshDB(ActionEvent event) {
		try {

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

			if (ArchType.getValue() != null && !ArchType.getValue().equals("���")) {
				in_out = "and upper(VECTOR) = '" + ArchType.getValue() + "' \r\n";
			}

			String selectStmt = "select id,\r\n" + "       filename,\r\n" + "       dt_ch,\r\n" + "       swfile,\r\n"
					+ "       oper,\r\n" + "       cr_dt,\r\n" + "       mttype,\r\n" + "       mtname,\r\n"
					+ "       cur,\r\n" + "       vector,\r\n" + "       nvl(summ,'') summ,\r\n"
					+ "       docdate from SWIFT_FILES where 1=1\r\n" + dt1_ + dt2_ + in_out + "order by CR_DT desc";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<SWIFT_FILES> cus_list = FXCollections.observableArrayList();
			DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			while (rs.next()) {
				SWIFT_FILES list = new SWIFT_FILES();

				list.setDOCDATE((rs.getDate("DOCDATE") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("DOCDATE")), formatter)
						: null);
				list.setSUMM(String.valueOf(rs.getInt("SUMM")));
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

		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * �������� ���������� �����
	 * 
	 * @param filename
	 * @return
	 */
	public String getFExt(String filename) {
		return Files.getFileExtension(filename);
	}

	/**
	 * �������� ������ ��� �������� �����
	 */
	public void EndTask() {
		this.st.cancel();
	}

	/**
	 * ������
	 */
	private ScheduledTask st;

	/**
	 * �������� �������� MT � ���
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
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
		return ret;
	}

	/**
	 * �������� ���������� ����� ��� ���������
	 */
	String FolderName;
	/**
	 * ���������� ������
	 */
	Integer selrow;

	/**
	 * ������������� ������
	 */
	void InitTable() {
		try {
			if (System.getenv(FolderName) != null) {
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				DateTimeFormatter formatterwt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
				// MSG or another folder
				File dir = new File(System.getenv(FolderName));
				File[] directoryListing = dir.listFiles();
				ObservableList<SWIFT_FILES> dlist = FXCollections.observableArrayList();
				Boolean ifchk = false;
				if (directoryListing != null) {
					for (File child : directoryListing) {
						//���� ����� � ���� ����������
						if (child.isFile() & child.exists()) {
							Path filePath = child.toPath();
							/**
							 * �������� �����...
							 */
							BasicFileAttributes attr = java.nio.file.Files.readAttributes(filePath,
									BasicFileAttributes.class);
							SWIFT_FILES list = new SWIFT_FILES();
							list.setFILENAME(child.getName());
							list.setDT_CH(LocalDateTime.parse(format.format(new Date(attr.creationTime().toMillis())),
									formatterwt));
							list.setCUR(getMtCur(child.getAbsolutePath()));
							list.setSUMM(getMtAmount(child.getAbsolutePath()));
							list.setMTTYPE(getMT(getMtType(child.getAbsolutePath()), "TYPE"));
							list.setMTNAME(getMT(getMtType(child.getAbsolutePath()), "NAME"));
							list.setDOCDATE((getMtDate(child.getAbsolutePath()) != null)
									? LocalDate.parse(getMtDate(child.getAbsolutePath()), formatter)
									: null);
							/**
							 * ������� ����������
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
					Platform.runLater(() -> {
						try {
							STMT.setItems(dlist);
							if (selrow != null) {
								STMT.getSelectionModel().select(selrow);
								STMT.getFocusModel().focus(selrow);
							}
							/*
							 * autoResizeColumns(STMT); TableFilter<SWIFT_FILES> tableFilter =
							 * TableFilter.forTableView(STMT).apply(); tableFilter.setSearchStrategy((input,
							 * target) -> { try { return target.toLowerCase().contains(input.toLowerCase());
							 * } catch (Exception e) { return false; } });
							 */
							// clear
							// dlist.clear();
						} catch (Exception e) {
							ErrorMessage(e.getMessage());
							SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
						}
					});
				}
			}
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}

	/**
	 * ���� ����������
	 * 
	 * @param table
	 */
	void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column_) -> {
			// column1.getColumns().stream().forEach((column_) -> {
			// System.out.println(column_.getText());
			if (column_.getText().equals("���� ���������")) {

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
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
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
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}
	}

	private Executor exec;

	/**
	 * ������ �� �����
	 * 
	 * @param event
	 */
	@FXML
	private void Open(ActionEvent event) {
		try {

			if (STMT.getSelectionModel().getSelectedItem() != null /* & !FolderName.equals("SWIFT_OUT") */) {
				StPn.setDisable(true);
				PrgInd.setVisible(true);
				Task<Object> task = new Task<Object>() {
					@Override
					public Object call() throws Exception {
						SWIFT_FILES selrow = STMT.getSelectionModel().getSelectedItem();
						InputStream inputstream = new FileInputStream(
								System.getenv(FolderName) + "/" + selrow.getFILENAME());

						new SwiftPrint().showReport(inputstream);

						return null;
					}
				};
				task.setOnFailed(e -> {
					ErrorMessage(task.getException().getMessage());
					SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
				});
				task.setOnSucceeded(e -> {
					try {
						StPn.setDisable(false);
						PrgInd.setVisible(false);
					} catch (Exception e1) {
						ErrorMessage(e1.getMessage());
						SWLogger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
					}
				});
				exec.execute(task);
			}
		} catch (Exception e) {

			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}

	}

	/**
	 * ������ �� ����
	 * 
	 * @param event
	 */
	@FXML
	private void OpenDB(ActionEvent event) {
		try {
			if (Achive.getSelectionModel().getSelectedItem() != null) {
				StPn.setDisable(true);
				PrgInd.setVisible(true);
				Task<Object> task = new Task<Object>() {
					@Override
					public Object call() throws Exception {

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
							new SwiftPrint().showReport(targetStream);
							targetStream.close();
						}
						rs.close();
						prepStmt.close();

						return null;
					}
				};
				task.setOnFailed(e -> {
					ErrorMessage(task.getException().getMessage());
					SWLogger.error(task.getException().getMessage() + "~" + Thread.currentThread().getName());
				});
				task.setOnSucceeded(e -> {
					try {
						StPn.setDisable(false);
						PrgInd.setVisible(false);

					} catch (Exception e1) {
						e1.printStackTrace();
						ErrorMessage(e1.getMessage());
						Main.logger.error(e1.getMessage() + "~" + Thread.currentThread().getName());
					}
				});
				exec.execute(task);
			}
		} catch (Exception e) {
			ErrorMessage(e.getMessage());
			SWLogger.error(e.getMessage() + "~" + Thread.currentThread().getName());
		}

	}
}

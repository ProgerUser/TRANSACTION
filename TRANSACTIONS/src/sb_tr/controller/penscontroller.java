package sb_tr.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;

import org.controlsfx.control.table.TableFilter;
import org.mozilla.universalchardet.UniversalDetector;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import sb_tr.model.SqlMap;
import sb_tr.model.TerminalDAO;
import sb_tr.model.pensmodel;
import sb_tr.util.DBUtil;

/**
 * Пачулия Саид 13.07.2020.
 */
public class penscontroller {

	@FXML
	private TableView<pensmodel> sep_pens;

	@FXML
	private Button separate;
	@FXML
	private Button save_sep;

	@FXML
	private TableColumn<pensmodel, Timestamp> DateLoad;

	@FXML
	private TableColumn<pensmodel, String> Filename;

	@FXML
	private TableColumn<pensmodel, Integer> ID;

	@FXML
	private TableColumn<pensmodel, String> ONE_PART;

	@FXML
	private TableColumn<pensmodel, String> TWO_PART;

	@FXML
	private TableColumn<pensmodel, String> THREE_PART;

	@FXML
	private TableColumn<pensmodel, String> FOUR_PART;

	/* Пакет для разбора */
	private final String sepfile = "{ ? = call z_sb_pens_sepfile.z_sb_pens_sepfile(?,?)}";

	/* Инициализация */
	@FXML
	private void initialize() {

		sep_pens.setEditable(true);
		
		ID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		Filename.setCellValueFactory(cellData -> cellData.getValue().filenameProperty());
		DateLoad.setCellValueFactory(cellData -> cellData.getValue().dateloadProperty());
		/*
		 * ONE_PART.setCellValueFactory(cellData ->
		 * cellData.getValue().one_partProperty());
		 * TWO_PART.setCellValueFactory(cellData ->
		 * cellData.getValue().TWO_PARTProperty());
		 * THREE_PART.setCellValueFactory(cellData ->
		 * cellData.getValue().THREE_PARTProperty());
		 * FOUR_PART.setCellValueFactory(cellData ->
		 * cellData.getValue().FOUR_PARTProperty());
		 */
		Filename.setCellFactory(TextFieldTableCell.forTableColumn());
		ID.setCellFactory(TextFieldTableCell.<pensmodel, Integer>forTableColumn(new IntegerStringConverter()));
		/*DateLoad.setCellFactory(TextFieldTableCell.<pensmodel,Timestamp>forTableColumn(StringConverter<T> converter));*/

		DateLoad.setCellFactory(column -> {
			TableCell<pensmodel, Timestamp> cell = new TableCell<pensmodel, Timestamp>() {
				private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

				@Override
				protected void updateItem(Timestamp item, boolean empty) {
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

		ID.setOnEditCommit(new EventHandler<CellEditEvent<pensmodel, Integer>>() {
			@Override
			public void handle(CellEditEvent<pensmodel, Integer> t) {
				((pensmodel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setid(t.getNewValue());
			}
		});

		Filename.setOnEditCommit(new EventHandler<CellEditEvent<pensmodel, String>>() {
			@Override
			public void handle(CellEditEvent<pensmodel, String> t) {
				((pensmodel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setfilename(t.getNewValue());
			}
		});

		DateLoad.setOnEditCommit(new EventHandler<CellEditEvent<pensmodel, Timestamp>>() {
			@Override
			public void handle(CellEditEvent<pensmodel, Timestamp> t) {
				((pensmodel) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setdateload(t.getNewValue());
			}
		});

		ObservableList<pensmodel> empData = TerminalDAO.Z_SB_PENS_4FILE();
		populate(empData);
		autoResizeColumns(sep_pens);
		TableFilter.forTableView(sep_pens).apply();
	}

	private void populate(ObservableList<pensmodel> pensmodel) {
		sep_pens.setItems(pensmodel);
	}

	private void Alerts(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText("Выберите сначала данные из таблицы!");
		alert.showAndWait();
	}

	@FXML
	void save_seps(ActionEvent event) {
		if (sep_pens.getSelectionModel().getSelectedItem() == null) {
			Alerts("Выберите сначала данные из таблицы!");
		} else {
			pensmodel pens = sep_pens.getSelectionModel().getSelectedItem();
			System.out.println(pens.getid());
			FileChooser fileChooser = new FileChooser();

			System.setProperty("javax.xml.transform.TransformerFactory",
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

			// Set extension filter for text files
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT File", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);

			// Show save file dialog
			File file = fileChooser.showSaveDialog(null);

			if (file != null) {
				for (int i = 1; i <= 4; i++) {
					retclob_(i, pens.getid(), DBUtil.conn, file);
				}
				showalert("Файлы сформированы в папку=" + file.getParent());
			}

		}
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

	/* Получить кодировку файла */
	public String getFileCharset(String file) {
		try {
			byte[] buf = new byte[500000000];
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
			showalert(e.getMessage());
		}
		return null;
	}

	/* Чтение файла */
	private String readFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName), "CP1251"/* getFileCharset(fileName) */));
			System.out.println("File_encode=" + getFileCharset(fileName));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(System.lineSeparator());
			}
			String clobData = sb.toString();
			br.close();
			return clobData;
		} catch (IOException e) {
			showalert(e.getMessage());
		}
		return "Error";
	}

	/* Старт */
	@FXML
	private void separate(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Выбрать файл");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text file", "*.txt"),
					new ExtensionFilter("Comma separated", "*.csv"));
			fileChooser.setInitialDirectory(
					new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {

				DBUtil.dbDisconnect();
				DBUtil.dbConnect();

				Connection conn = DBUtil.conn;
				CallableStatement callStmt = null;
				String reviewContent = null;
				callStmt = conn.prepareCall(sepfile);
				String reviewStr = readFile(file.getParent() + "\\" + file.getName());
				Clob clob = conn.createClob();
				clob.setString(1, reviewStr);
				callStmt.registerOutParameter(1, Types.VARCHAR);
				callStmt.setClob(2, clob);
				callStmt.setString(3, file.getName());
				callStmt.execute();
				reviewContent = callStmt.getString(1);

				String[] parts = reviewContent.split(";");
				String part1 = parts[0].trim();
				String part2 = parts[1].trim();
				System.out.println(part1);
				System.out.println(part2);

				if (part2.equals("Error")) {
					System.out.println("Error--");
					write_error(conn, file, Integer.valueOf(part1));
				} else if (part2.equals("ok")) {
					System.out.println("OK--");
					for (int i = 1; i <= 4; i++) {
						System.out.println(i);
						String str = "";
						if (i == 1) {
							Clob clobb = conn.createClob();
							str = retclob(i, Integer.valueOf(part1), conn, file);
							clobb.setString(1, str);
							String upd = "update z_sb_pens_4file\r\n" + "set ONE_PART = ?\r\n" + "where ID = ?\r\n";
							System.out.println(upd);
							PreparedStatement prepStmt = conn.prepareStatement(upd);
							prepStmt.setClob(1, clobb);
							prepStmt.setInt(2, Integer.valueOf(part1));
							prepStmt.executeUpdate();
						} else if (i == 2) {
							Clob clobb = conn.createClob();
							str = retclob(i, Integer.valueOf(part1), conn, file);
							clobb.setString(1, str);
							String upd = "update z_sb_pens_4file\r\n" + "set TWO_PART = ?\r\n" + "where ID = ?\r\n";
							System.out.println(upd);
							PreparedStatement prepStmt = conn.prepareStatement(upd);
							prepStmt.setClob(1, clobb);
							prepStmt.setInt(2, Integer.valueOf(part1));
							prepStmt.executeUpdate();
						} else if (i == 3) {
							Clob clobb = conn.createClob();
							str = retclob(i, Integer.valueOf(part1), conn, file);
							clobb.setString(1, str);
							String upd = "update z_sb_pens_4file\r\n" + "set THREE_PART = ?\r\n" + "where ID = ?\r\n";
							System.out.println(upd);
							PreparedStatement prepStmt = conn.prepareStatement(upd);
							prepStmt.setClob(1, clobb);
							prepStmt.setInt(2, Integer.valueOf(part1));
							prepStmt.executeUpdate();
						} else if (i == 4) {
							Clob clobb = conn.createClob();
							str = retclob(i, Integer.valueOf(part1), conn, file);
							clobb.setString(1, str);
							String upd = "update z_sb_pens_4file\r\n" + "set FOUR_PART = ?\r\n" + "where ID = ?\r\n";
							System.out.println(upd);
							PreparedStatement prepStmt = conn.prepareStatement(upd);
							prepStmt.setClob(1, clobb);
							prepStmt.setInt(2, Integer.valueOf(part1));
							prepStmt.executeUpdate();
						}
					}
					/* Вывод сообщения */
					showalert("Файлы сформированы в папку=" + file.getParent());
					ObservableList<pensmodel> empData = TerminalDAO.Z_SB_PENS_4FILE();
					populate(empData);
					autoResizeColumns(sep_pens);
					TableFilter.forTableView(sep_pens).apply();
				}
			}
		} catch (SQLException e) {
			showalert(e.getMessage());
		}
	}

	/* Вывод ошибок */
	public void write_error(Connection conn, File file, int sess_id) {
		try {
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select * from z_sb_pens_4file_log t where t.SESS_ID = " + sess_id;

			String createfolder = file.getParent() + "\\" + file.getName() + "_Error.txt";
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			PrintWriter writer = new PrintWriter(createfolder);
			while (rs.next()) {
				writer.write(rs.getString("ERROR"));
			}
			writer.close();
			rs.close();
			sqlStatement.close();

			ProcessBuilder pb = new ProcessBuilder("Notepad.exe",
					file.getParent() + "\\" + file.getName() + "_Error.txt");
			pb.start();

		} catch (Exception e) {
			showalert(e.getMessage());
		}
	}

	/* Возврат самого файла */
	public String retclob(int id, int sess_id, Connection conn, File file) {
		String str = "";
		try {
			SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
			String readRecordSQL = s.getSql("getPens");
			PreparedStatement prepStmt = conn.prepareStatement(readRecordSQL);
			prepStmt.setInt(1, id);
			ResultSet rs = prepStmt.executeQuery();

			String createfolder = file.getParent() + "\\" + file.getName() + "_0" + id + ".txt";
			System.out.println(readRecordSQL);

			PrintWriter writer = new PrintWriter(createfolder);

			while (rs.next()) {
				str = str + rs.getString("STR") + "\r\n";
			}
			writer.write(str);
			writer.flush();
			writer.close();
			rs.close();
			prepStmt.close();

		} catch (Exception e) {
			showalert(e.getMessage());
		}
		return str;
	}

	/* Возврат самого файла */
	public void retclob_(int id, int sess_id, Connection conn, File file) {
		try {
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select * from Z_SB_PENS_4FILE t where t.ID = " + sess_id;

			String createfolder = file.getParent() + "\\" + file.getName() + "_0" + id + ".txt";
			System.out.println(createfolder);
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			PrintWriter writer = new PrintWriter(createfolder);
			while (rs.next()) {
				if (id == 1) {
					writer.write(rs.getString("ONE_PART"));
				} else if (id == 2) {
					writer.write(rs.getString("TWO_PART"));
				} else if (id == 3) {
					writer.write(rs.getString("THREE_PART"));
				} else if (id == 4) {
					writer.write(rs.getString("FOUR_PART"));
				}
			}
			writer.close();
			rs.close();
			sqlStatement.close();

		} catch (Exception e) {
			showalert(e.getMessage());
		}
	}

	/* Вывод сообщения */
	public void showalert(String mes) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mes);
		alert.showAndWait();
	}
}

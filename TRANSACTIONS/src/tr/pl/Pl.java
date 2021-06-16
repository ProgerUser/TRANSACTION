package tr.pl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import app.Main;
import app.util.DBUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sbalert.Msg;

public class Pl {

	public Pl() {
		Main.logger = Logger.getLogger(getClass());
	}

	@FXML
	private TableView<PlModel> UsrLst;

	@FXML
	private TableView<PlAccIn> ExAcc;

	@FXML
	private TableColumn<PlModel, String> Login;

	@FXML
	private TableColumn<PlModel, String> Fio;

	@FXML
	private TableColumn<PlModel, String> Stat;

	@FXML
	private TableColumn<PlAccIn, String> ExAccount;

	@FXML
	private TableColumn<PlAccIn, String> ExFio;

	@FXML
	private TableColumn<Object, LocalDateTime> D_START;

	@FXML
	private TableColumn<Object, LocalDateTime> D_END;

	@FXML
	void Minus(ActionEvent event) {
		try {
			PlAccIn val = ExAcc.getSelectionModel().getSelectedItem();
			PlModel vals = UsrLst.getSelectionModel().getSelectedItem();
			if (val != null & vals != null) {
				final Alert alert = new Alert(AlertType.CONFIRMATION, "�������?", ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = DBUtil.conn
							.prepareStatement("delete from SBRA_PL_RASH_USR where usr = ? and acc = ?");
					prp.setString(1, vals.getLOGIN());
					prp.setString(2, val.getCACCACC());
					prp.executeUpdate();
					DBUtil.conn.commit();

					InitUsr();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							UsrLst.requestFocus();
							UsrLst.getSelectionModel().select(seltemp);
							UsrLst.scrollTo(seltemp);
						}
					});
				}
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	int seltemp;

	@FXML
	void Plus(ActionEvent event) {
		try {
			PlModel val = UsrLst.getSelectionModel().getSelectedItem();
			if (val != null) {
				Stage stage = new Stage();
				Stage stage_ = (Stage) ExAcc.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/tr/pl/ParamList.fxml"));

				Accs controller = new Accs();
				loader.setController(controller);
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("�����");
				stage.initOwner(stage_);
				stage.setResizable(true);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						if (controller.caccacc != null) {
							try {
								PreparedStatement prp = DBUtil.conn
										.prepareStatement("insert into SBRA_PL_RASH_USR (USR,ACC) " + "values (?,?) ");
								prp.setString(1, val.getLOGIN());
								prp.setString(2, controller.caccacc);
								prp.executeUpdate();
								DBUtil.conn.commit();

								InitUsr();

								InitEx(val);

								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										UsrLst.requestFocus();
										UsrLst.getSelectionModel().select(seltemp);
										UsrLst.scrollTo(seltemp);
									}
								});
							} catch (Exception e) {
								Msg.Message(ExceptionUtils.getStackTrace(e));
							}
						}
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getStackTrace(e));
		}
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	void InitUsr() {
		try {
			String selectStmt = "select cusrlogname login,\r\n"
					+ "       cusrname fio,\r\n"
					+ "       case\r\n"
					+ "         when (select count(*)\r\n"
					+ "                 from sbra_pl_rash_usr gr\r\n"
					+ "                where usr.cusrlogname = gr.usr) > 0 then\r\n"
					+ "          'Y'\r\n"
					+ "         else\r\n"
					+ "          'N'\r\n"
					+ "       end stat\r\n"
					+ "  from usr\r\n"
					+ " where usr.dusrfire is null\r\n"
					+ " order by cusrname asc\r\n"
					+ "";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<PlModel> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				PlModel list = new PlModel();
				list.setLOGIN(rs.getString("LOGIN"));
				list.setFIO(rs.getString("FIO"));
				list.setSTAT(rs.getString("STAT"));
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			UsrLst.setItems(dlist);

			TableFilter<PlModel> tableFilter = TableFilter.forTableView(UsrLst).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	void InitEx(PlModel val) {
		try {
			String selectStmt = "select acc.CACCACC, acc.CACCNAME, gr.d_start, gr.d_end\r\n"
					+ "  from acc, SBRA_PL_RASH_USR gr\r\n" + " where acc.CACCACC = gr.acc\r\n"
					+ "   and gr.usr = ?\r\n" + "";
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			prepStmt.setString(1, val.getLOGIN());
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<PlAccIn> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				PlAccIn list = new PlAccIn();
				list.setCACCACC(rs.getString("CACCACC"));
				list.setCACCNAME(rs.getString("CACCNAME"));
				list.setD_END((rs.getDate("D_END") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("D_END")), formatter) : null);
				list.setD_START((rs.getDate("D_START") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("D_START")), formatter) : null);
				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			ExAcc.setItems(dlist);

			TableFilter<PlAccIn> tableFilter = TableFilter.forTableView(ExAcc).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	@FXML
	private void initialize() {
		try {
			// --
			Login.setCellValueFactory(cellData -> cellData.getValue().LOGINProperty());
			Fio.setCellValueFactory(cellData -> cellData.getValue().FIOProperty());
			Stat.setCellValueFactory(cellData -> cellData.getValue().STATProperty());
			// --
			D_START.setCellValueFactory(cellData -> ((PlAccIn) cellData.getValue()).D_STARTProperty());
			D_END.setCellValueFactory(cellData -> ((PlAccIn) cellData.getValue()).D_ENDProperty());
			// --
			ExAccount.setCellValueFactory(cellData -> cellData.getValue().CACCACCProperty());
			ExFio.setCellValueFactory(cellData -> cellData.getValue().CACCNAMEProperty());
			// --
			InitUsr();
			// --
			UsrLst.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					seltemp = UsrLst.getSelectionModel().getSelectedIndex();
					PlModel val = UsrLst.getSelectionModel().getSelectedItem();
					InitEx(val);
				}
			});
			new ConvConst().TableColumnDateTime(D_START);
			new ConvConst().TableColumnDateTime(D_END);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}
}

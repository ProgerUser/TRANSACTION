package su.sbra.psv.app.tr.pl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

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
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

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
	private TableColumn<PlAccIn, String> cardnum;
	@FXML
	private TableColumn<PlAccIn, String> USRS;
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
				final Alert alert = new Alert(AlertType.CONFIRMATION, "Удалить?", ButtonType.YES, ButtonType.NO);
				if (Msg.setDefaultButton(alert, ButtonType.NO).showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
					PreparedStatement prp = DBUtil.conn
							.prepareStatement("delete from SBRA_PL_RASH_USR where usr = ? and acc = ?");
					prp.setString(1, vals.getLOGIN());
					prp.setString(2, val.getCACCACC());
					prp.executeUpdate();
					DBUtil.conn.commit();

					InitUsr();
					InitEx(vals.getLOGIN());
					SelRow();
				}
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	String seltemp;

	@FXML
	void Plus(ActionEvent event) {
		try {
			PlModel val = UsrLst.getSelectionModel().getSelectedItem();
			if (val != null) {
				Stage stage = new Stage();
				Stage stage_ = (Stage) ExAcc.getScene().getWindow();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/su/sbra/psv/app/tr/pl/ParamList.fxml"));

				Accs controller = new Accs();
				loader.setController(controller);
				Parent root = loader.load();
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("/icon.png"));
				stage.setTitle("Счета");
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

								InitEx(val.getLOGIN());
								InitUsr();
								SelRow();
							} catch (Exception e) {
								DbUtil.Log_Error(e);
								Main.logger.error(ExceptionUtils.getStackTrace(e));
							}
						}
					}
				});
				stage.show();
			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Выбор строки
	 */
	void SelRow() {
		try {
			for (PlModel site : UsrLst.getItems()) {
				if (site.getLOGIN().equals(seltemp)) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							UsrLst.requestFocus();
							UsrLst.getSelectionModel().select(site);
							UsrLst.scrollTo(site);
						}
					});
				}
			}

//			// Цикл по ячейкам
//			for (int i = 0; i < UsrLst.getItems().size(); i++) {
//				// Цикл по столбцам
//				for (int j = 0; j < UsrLst.getColumns().size(); j++) {
//					// Если Не пусто
//					if (UsrLst.getColumns().get(j).getCellData(i) != null) {
//						if (UsrLst.getColumns().get(0).getCellData(i) != null) {
//							if (UsrLst.getColumns().get(0).getCellData(i).equals(seltemp)) {
//								System.out.println("________________");
//								System.out.println("Login=" + UsrLst.getColumns().get(0).getCellData(i));
//								System.out.println("i="+i);
//								System.out.println("j="+j);
//								System.out.println("________________");
//							}
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

	void InitUsr() {
		try {
			String selectStmt = "WITH dat AS\n"
					+ " (SELECT cusrlogname login,\n"
					+ "         cusrname fio,\n"
					+ "         CASE\n"
					+ "           WHEN (SELECT COUNT(*)\n"
					+ "                   FROM sbra_pl_rash_usr gr\n"
					+ "                  WHERE usr.cusrlogname = gr.usr) > 0 THEN\n"
					+ "            'Y'\n"
					+ "           ELSE\n"
					+ "            'N'\n"
					+ "         END stat\n"
					+ "    FROM usr\n"
					+ "   WHERE usr.dusrfire IS NULL)\n"
					+ "SELECT *\n"
					+ "  FROM dat\n"
					+ " ORDER BY CASE\n"
					+ "            WHEN stat = 'Y' THEN\n"
					+ "             1\n"
					+ "            ELSE\n"
					+ "             2\n"
					+ "          END,fio\n"
					+ "";
			//System.out.println(selectStmt);
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

	void InitEx(String login) {
		try {
			String selectStmt = "select acc.CACCACC,\n"
					+ "       acc.CACCNAME,\n"
					+ "       gr.d_start,\n"
					+ "       gr.d_end,\n"
					+ "       (SELECT CPLCNUM\n"
					+ "          FROM v_PLA\n"
					+ "         WHERE V_PLA.IPLATYPE in (1, 2)\n"
					+ "           and iplastatus != 6\n"
					+ "           and v_PLA.caccacc = pl_ca.caccacc) cardnum,USRS\n"
					+ "  from acc, pl_ca, SBRA_PL_RASH_USR gr\n"
					+ " where acc.CACCACC = gr.acc\n"
					+ "   and pl_ca.caccacc = acc.caccacc\n"
					+ "   and pl_ca.iplscatype = 14\n"
					+ "   and gr.usr = ? order by CACCNAME asc, d_start desc";
			//System.out.println(selectStmt);
			PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
			prepStmt.setString(1, login);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<PlAccIn> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				PlAccIn list = new PlAccIn();
				list.setcardnum(rs.getString("cardnum"));
				list.setUSRS(rs.getString("USRS"));
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
			cardnum.setCellValueFactory(cellData -> cellData.getValue().cardnumProperty());
			ExAccount.setCellValueFactory(cellData -> cellData.getValue().CACCACCProperty());
			ExFio.setCellValueFactory(cellData -> cellData.getValue().CACCNAMEProperty());
			USRS.setCellValueFactory(cellData -> cellData.getValue().USRSProperty());
			// --
			InitUsr();
			// --
			UsrLst.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				if (newSelection != null) {
					seltemp = UsrLst.getSelectionModel().getSelectedItem().getLOGIN();
					// System.out.println(seltemp);
					PlModel val = UsrLst.getSelectionModel().getSelectedItem();
					InitEx(val.getLOGIN());
				}
			});
			new ConvConst().TableColumnDateTime(D_START);
			new ConvConst().TableColumnDateTime(D_END);
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}
}

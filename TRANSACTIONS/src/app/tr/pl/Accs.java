package app.tr.pl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import app.Main;
import app.sbalert.Msg;
import app.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Accs {

	public Accs() {
		Main.logger = Logger.getLogger(getClass());
	}

	public String caccacc = null;
	@FXML
	private TextField Search;
	@FXML
	private TableView<PlAccIn> List;
	@FXML
	private TableColumn<PlAccIn, String> Acc;
	@FXML
	private TableColumn<PlAccIn, String> cardnum;
	@FXML
	private TableColumn<PlAccIn, String> Fio;

	@FXML
	void Cencel(ActionEvent event) {
		onclose();
	}

	void onclose() {
		Stage stage = (Stage) Search.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	@FXML
	void Search(ActionEvent event) {
		try {
			if (Search.getText().length() > 5) {
//				String selectStmt = "select acc.CACCACC,\r\n"
//						+ "       acc.CACCNAME,\r\n"
//						+ "       (SELECT CPLCNUM\r\n"
//						+ "          FROM v_PLA\r\n"
//						+ "         WHERE V_PLA.IPLATYPE in (1, 2)\r\n"
//						+ "           and iplastatus != 6\r\n"
//						+ "           and v_PLA.caccacc = pl_ca.caccacc) cardnum\r\n"
//						+ "  from acc, pl_ca\r\n"
//						+ " where lower(acc.CACCNAME) like lower('%' || ? || '%')\r\n"
//						+ "   and acc.CACCACC = pl_ca.caccacc\r\n"
//						+ "   and pl_ca.iplscatype = 14\r\n"
//						+ " order by CACCNAME";
				String selectStmt ="SELECT CACCACC, CCUSNAME CACCNAME, CPLCNUM cardnum\r\n"
						+ "  FROM v_PLA\r\n"
						+ " WHERE V_PLA.IPLATYPE in (1, 2)\r\n"
						+ "   and iplastatus != 6\r\n"
						+ "   and lower(CCUSNAME) like lower('%' || ? || '%')";
				//System.out.println(selectStmt);
				PreparedStatement prepStmt = DBUtil.conn.prepareStatement(selectStmt);
				prepStmt.setString(1, Search.getText());
				ResultSet rs = prepStmt.executeQuery();
				ObservableList<PlAccIn> dlist = FXCollections.observableArrayList();
				while (rs.next()) {
					PlAccIn list = new PlAccIn();
					list.setCACCACC(rs.getString("CACCACC"));
					list.setCACCNAME(rs.getString("CACCNAME"));
					list.setcardnum(rs.getString("cardnum"));
					dlist.add(list);
				}
				prepStmt.close();
				rs.close();

				List.setItems(dlist);

				TableFilter<PlAccIn> tableFilter = TableFilter.forTableView(List).apply();
				tableFilter.setSearchStrategy((input, target) -> {
					try {
						return target.toLowerCase().contains(input.toLowerCase());
					} catch (Exception e) {
						return false;
					}
				});
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	@FXML
	void Select(ActionEvent event) {
		try {
			PlAccIn val = List.getSelectionModel().getSelectedItem();
			if (val != null) {
				caccacc = val.getCACCACC();
				onclose();
			}
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}

	@FXML
	private void initialize() {
		try {
			cardnum.setCellValueFactory(cellData -> cellData.getValue().cardnumProperty());
			Acc.setCellValueFactory(cellData -> cellData.getValue().CACCACCProperty());
			Fio.setCellValueFactory(cellData -> cellData.getValue().CACCNAMEProperty());

			List.setRowFactory(tv -> {
				TableRow<PlAccIn> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						Select(null);
					}
				});
				return row;
			});
			
		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}
}

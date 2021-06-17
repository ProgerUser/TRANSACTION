package tr.pl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import app.Main;
import app.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sbalert.Msg;

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
				String selectStmt = "select acc.CACCACC,\n"
						+ "					       acc.CACCNAME,\n"
						+ "					       (select plc.cplcnum\n"
						+ "					          from plc\n"
						+ "					         where plc.iplaagrid = pl_ca.iplaagrid\n"
						+ "					           and dplcend > sysdate\n"
						+ "					           and plc.iplcprimary =\n"
						+ "					               (select max(iplcprimary)\n"
						+ "					                  from plc\n"
						+ "					                 where plc.iplaagrid = pl_ca.iplaagrid\n"
						+ "					                   and dplcend > sysdate)) cardnum\n"
						+ "					  from acc, pl_ca\n"
						+ "					 where lower(acc.CACCNAME) like lower('%'||?||'%')\n"
						+ "					   and acc.CACCACC = pl_ca.caccacc\n"
						+ "					   and pl_ca.iplscatype = 14\n"
						+ "					 order by CACCNAME";
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

		} catch (Exception e) {
			Msg.Message(ExceptionUtils.getMessage(e));
		}
	}
}

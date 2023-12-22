package su.sbra.psv.app.tr.pl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

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
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.sbalert.Msg;
import su.sbra.psv.app.util.DBUtil;

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
	private TableColumn<PlAccIn, String> Dog;
	@FXML
	private TableColumn<PlAccIn, Double> Ostt;

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

				String selectStmt = "SELECT * FROM v_sbra_pl_rash_usr t WHERE lower(t.CACCNAME) LIKE lower('%' || ? || '%')\n";
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
					list.setDog(rs.getString("dogovor"));
					list.setOstt(rs.getDouble("ostt"));
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
			Dog.setCellValueFactory(cellData -> cellData.getValue().DogProperty());
			Ostt.setCellValueFactory(cellData -> cellData.getValue().OsttProperty().asObject());

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

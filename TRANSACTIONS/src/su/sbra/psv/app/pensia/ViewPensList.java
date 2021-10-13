package su.sbra.psv.app.pensia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.controlsfx.control.table.TableFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.util.DBUtil;
import su.sbra.psv.app.utils.DbUtil;

public class ViewPensList {

	@FXML
	private TableView<PENS_ROW> PensRow;
	@FXML
	private TableColumn<PENS_ROW, Long> PART;
	@FXML
	private TableColumn<PENS_ROW, Long> ROW_NUM;
	@FXML
	private TableColumn<PENS_ROW, String> LAST_NAME;
	@FXML
	private TableColumn<PENS_ROW, String> FIRST_NAME;
	@FXML
	private TableColumn<PENS_ROW, String> MIDDLE_NAME;
	@FXML
	private TableColumn<PENS_ROW, String> COLUMN5;
	@FXML
	private TableColumn<PENS_ROW, String> ACC;
	@FXML
	private TableColumn<PENS_ROW, Double> SUMM;
	@FXML
	private TableColumn<PENS_ROW, String> BDATE;
	@FXML
	private TableColumn<PENS_ROW, String> COLUMN9;
	@FXML
	private TableColumn<PENS_ROW, String> COLUMN10;
	@FXML
	private TableColumn<PENS_ROW, String> ACC_VTB;
	@FXML
	private TableColumn<PENS_ROW, String> COLUMN12;
	@FXML
	private TableColumn<PENS_ROW, String> SNILS;
	@FXML
	private TableColumn<PENS_ROW, LocalDate> ABS_BDATE;
	@FXML
	private TableColumn<PENS_ROW, String> CCUSSNILS;

	/**
	 * Инициализация
	 */
	@FXML
	private void initialize() {
		PART.setCellValueFactory(cellData -> cellData.getValue().PARTProperty().asObject());
		ROW_NUM.setCellValueFactory(cellData -> cellData.getValue().ROW_NUMProperty().asObject());
		LAST_NAME.setCellValueFactory(cellData -> cellData.getValue().LAST_NAMEProperty());
		FIRST_NAME.setCellValueFactory(cellData -> cellData.getValue().FIRST_NAMEProperty());
		MIDDLE_NAME.setCellValueFactory(cellData -> cellData.getValue().MIDDLE_NAMEProperty());
		COLUMN5.setCellValueFactory(cellData -> cellData.getValue().COLUMN5Property());
		ACC.setCellValueFactory(cellData -> cellData.getValue().ACCProperty());
		SUMM.setCellValueFactory(cellData -> cellData.getValue().D$SUMMProperty().asObject());
		BDATE.setCellValueFactory(cellData -> cellData.getValue().BDATEProperty());
		COLUMN9.setCellValueFactory(cellData -> cellData.getValue().COLUMN9Property());
		COLUMN10.setCellValueFactory(cellData -> cellData.getValue().COLUMN10Property());
		ACC_VTB.setCellValueFactory(cellData -> cellData.getValue().ACC_VTBProperty());
		COLUMN12.setCellValueFactory(cellData -> cellData.getValue().COLUMN12Property());
		SNILS.setCellValueFactory(cellData -> cellData.getValue().SNILSProperty());
		ABS_BDATE.setCellValueFactory(cellData -> cellData.getValue().ABS_BDATEProperty());
		CCUSSNILS.setCellValueFactory(cellData -> cellData.getValue().CCUSSNILSProperty());
		
		LoadTableSet();
		
	}

	public void autoResizeColumns(TableView<?> table) {
		// Set the right policy
		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach((column) -> {
			// System.out.println(column.getText());
			if (column.getText().equals("Количество строк") | column.getText().equals("")) {

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
				column.setPrefWidth(max + 20.0d);
			}
		});
	}

	pensmodel sel;

	public void SetClass(pensmodel sel) {
		this.sel = sel;
	}

	/**
	 * Initialize table
	 */
	void LoadTableSet() {
		try {
			// Prepared Statement
			PreparedStatement prp = DBUtil.conn
					.prepareStatement(DbUtil.Sql_From_Prop("/su/sbra/psv/app/pensia/SQL.properties", "SelPensView"));
			prp.setLong(1, sel.getid());
			ResultSet rs = prp.executeQuery();
			ObservableList<PENS_ROW> cus_list = FXCollections.observableArrayList();
			// looping
			while (rs.next()) {
				PENS_ROW list = new PENS_ROW();
				list.setPART(rs.getLong("PART"));
				list.setROW_NUM(rs.getLong("ROW_NUM"));
				list.setLAST_NAME(rs.getString("LAST_NAME"));
				list.setFIRST_NAME(rs.getString("FIRST_NAME"));
				list.setMIDDLE_NAME(rs.getString("MIDDLE_NAME"));
				list.setCOLUMN5(rs.getString("COLUMN5"));
				list.setACC(rs.getString("ACC"));
				list.setD$SUMM(rs.getDouble("D$SUMM"));
				list.setBDATE(rs.getString("BDATE"));
				list.setCOLUMN9(rs.getString("COLUMN9"));
				list.setCOLUMN10(rs.getString("COLUMN10"));
				list.setACC_VTB(rs.getString("ACC_VTB"));
				list.setCOLUMN12(rs.getString("COLUMN12"));
				list.setSNILS(rs.getString("SNILS"));
				list.setABS_BDATE((rs.getDate("ABS_BDATE") != null)
						? LocalDate.parse(new SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("ABS_BDATE")),
								DateTimeFormatter.ofPattern("dd.MM.yyyy"))
						: null);
				list.setCCUSSNILS(rs.getString("CCUSSNILS"));

				cus_list.add(list);
			}
			// add data
			PensRow.setItems(cus_list);
			// close
			prp.close();
			rs.close();
			// add filter
			TableFilter<PENS_ROW> tableFilter = TableFilter.forTableView(PensRow).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
			// resize
			autoResizeColumns(PensRow);
		} catch (Exception e) {
			DbUtil.Log_Error(e);
			Main.logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}

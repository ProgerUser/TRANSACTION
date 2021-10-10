package su.sbra.psv.app.admin.log;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.controlsfx.control.table.TableFilter;

import com.jyloo.syntheticafx.ComparableColumnFilter;
import com.jyloo.syntheticafx.PatternColumnFilter;
import com.jyloo.syntheticafx.SyntheticaFX;
import com.jyloo.syntheticafx.TextFormatterFactory;
import com.jyloo.syntheticafx.TitledBorderPane;
import com.jyloo.syntheticafx.XTableColumn;
import com.jyloo.syntheticafx.XTableView;
import com.jyloo.syntheticafx.filter.ComparableFilterModel;
import com.jyloo.syntheticafx.filter.ComparisonType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.converter.LocalDateTimeStringConverter;
import javafx.util.converter.LongStringConverter;
import su.sbra.psv.app.main.Main;
import su.sbra.psv.app.model.Connect;
import su.sbra.psv.app.swift.ConvConst;
import su.sbra.psv.app.utils.DbUtil;


public class LogList {

	public LogList() {
		Main.logger = Logger.getLogger(getClass());
	}

	@FXML
	private XTableColumn<LOGS, String> METHODNAME;
	@FXML
	private XTableColumn<LOGS, LocalDateTime> LOGDATED;
	@FXML
	private XTableView<LOGS> LOGS;
	@FXML
	private XTableColumn<LOGS, String> ERROR;
	@FXML
	private XTableColumn<LOGS, Long> ID;
	@FXML
	private XTableColumn<LOGS, String> OPER;
	@FXML
	private XTableColumn<LOGS, Long> LINENUMBER;
	@FXML
	private XTableColumn<LOGS, String> CLASSNAME;
	@FXML
	private DatePicker DT1;
	@FXML
	private DatePicker DT2;

	/**
	 * Форматирование столбцов
	 * 
	 * @param TC
	 */
	void DateFormatCol(TableColumn<LOGS, LocalDateTime> TC) {
		TC.setCellFactory(column -> {
			TableCell<LOGS, LocalDateTime> cell = new TableCell<LOGS, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
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

	@FXML
	private BorderPane ROOT;

	private Pane createOptionPane(XTableView<?> table) {
		FlowPane pane = new FlowPane(10, 10);
		pane.setStyle("-fx-padding: 10 4");

		CheckBox filterVisible = new CheckBox("Показать фильтр");
		filterVisible.selectedProperty().bindBidirectional(table.filterRowVisibleProperty());

		CheckBox menuButtonVisible = new CheckBox("Показать кнопку меню");
		menuButtonVisible.selectedProperty().bindBidirectional(table.tableMenuButtonVisibleProperty());

		CheckBox firstFilterable = new CheckBox("Фильтруемый первый столбец");
		// XTableColumn<VCUS, Long> firstColumn = (XTableColumn<VCUS, Long>)
		// table.getColumns().get(0);
		firstFilterable.selectedProperty().bindBidirectional(ID.filterableProperty());

		CheckBox includeHidden = new CheckBox("Включить скрытые столбцы");
		includeHidden.selectedProperty().bindBidirectional(table.getFilterController().includeHiddenProperty());

		CheckBox andFilters = new CheckBox("Используйте операцию \"И\" для многоколоночного фильтра");
		andFilters.selectedProperty().bindBidirectional(table.getFilterController().andFiltersProperty());

		pane.getChildren().addAll(filterVisible, menuButtonVisible, firstFilterable, includeHidden, andFilters);

		TitledBorderPane p = new TitledBorderPane("Настройки", pane);
		p.getStyleClass().add("top-border-only");
		p.setStyle("-fx-border-insets: 10 0 0 0");
		return p;
	}

	@FXML
	private void DT1() {
		Init();
	}

	@FXML
	private void DT2() {
		Init();
	}

	@FXML
	private void Clear() {
		DT1.setValue(null);
		DT2.setValue(null);
		Init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
	private void initialize() {
		try {
			dbConnect();
			
			
			ROOT.setBottom(createOptionPane(LOGS));

			METHODNAME.setCellValueFactory(cellData -> cellData.getValue().METHODNAMEProperty());
			LOGDATED.setCellValueFactory(cellData -> cellData.getValue().LOGDATEProperty());
			CLASSNAME.setCellValueFactory(cellData -> cellData.getValue().CLASSNAMEProperty());
			OPER.setCellValueFactory(cellData -> cellData.getValue().OPERProperty());
			ERROR.setCellValueFactory(cellData -> cellData.getValue().ERRORProperty());
			ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
			LINENUMBER.setCellValueFactory(cellData -> cellData.getValue().LINENUMBERProperty().asObject());

			SyntheticaFX.init("com.jyloo.syntheticafx.SyntheticaFXModena");
			ObservableList rules = FXCollections.observableArrayList(ComparisonType.values());

			ID.setColumnFilter(new ComparableColumnFilter(new ComparableFilterModel(rules),
					TextFormatterFactory.LONG_TEXTFORMATTER_FACTORY));
			LINENUMBER.setColumnFilter(new ComparableColumnFilter(new ComparableFilterModel(rules),
					TextFormatterFactory.LONG_TEXTFORMATTER_FACTORY));
			CLASSNAME.setColumnFilter(new PatternColumnFilter<>());
			OPER.setColumnFilter(new PatternColumnFilter<>());
			ERROR.setColumnFilter(new PatternColumnFilter<>());
			METHODNAME.setColumnFilter(new PatternColumnFilter<>());

			DateFormatCol(LOGDATED);
			Init();
			new ConvConst().FormatDatePiker(DT1);
			new ConvConst().FormatDatePiker(DT2);
//
//			
//			
			ID.setCellFactory(TextFieldTableCell.<LOGS, Long>forTableColumn(new LongStringConverter()));
			ID.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, Long>>() {
				@Override
				public void handle(CellEditEvent<LOGS, Long> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow())).setID(t.getNewValue());
				}
			});
//			
//			
//			
			LOGDATED.setCellFactory(
					TextFieldTableCell.<LOGS, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));
			LOGDATED.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, LocalDateTime>>() {
				@Override
				public void handle(CellEditEvent<LOGS, LocalDateTime> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLOGDATE(t.getNewValue());
				}
			});
//
//			
//			
			LINENUMBER.setCellFactory(TextFieldTableCell.<LOGS, Long>forTableColumn(new LongStringConverter()));
			LINENUMBER.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, Long>>() {
				@Override
				public void handle(CellEditEvent<LOGS, Long> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setLINENUMBER(t.getNewValue());
				}
			});
//
//			
//			
			CLASSNAME.setCellFactory(TextFieldTableCell.forTableColumn());
			CLASSNAME.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, String>>() {
				@Override
				public void handle(CellEditEvent<LOGS, String> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setCLASSNAME(t.getNewValue());
				}
			});
//
//			
//			
			OPER.setCellFactory(TextFieldTableCell.forTableColumn());
			OPER.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, String>>() {
				@Override
				public void handle(CellEditEvent<LOGS, String> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow())).setOPER(t.getNewValue());
				}
			});
//			
//			
//			
			ERROR.setCellFactory(TextFieldTableCell.forTableColumn());
			ERROR.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, String>>() {
				@Override
				public void handle(CellEditEvent<LOGS, String> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow())).setERROR(t.getNewValue());
				}
			});
//			
//			
//			
			METHODNAME.setCellFactory(TextFieldTableCell.forTableColumn());
			METHODNAME.setOnEditCommit(new EventHandler<CellEditEvent<LOGS, String>>() {
				@Override
				public void handle(CellEditEvent<LOGS, String> t) {
					((LOGS) t.getTableView().getItems().get(t.getTablePosition().getRow()))
							.setMETHODNAME(t.getNewValue());
				}
			});
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	void Init() {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			DateTimeFormatter formatterd = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			String dt1 = "";
			String dt2 = "";

			if (DT1.getValue() != null) {
				dt1 = " and LOGDATE >= to_date('" + DT1.getValue().format(formatterd) + "','dd.mm.yyyy') \r\n";
			}
			if (DT2.getValue() != null) {
				dt2 = " and LOGDATE <= to_date('" + DT2.getValue().format(formatterd) + "','dd.mm.yyyy') \r\n";
			}

			String selectStmt = "select * from SU_SBRA_ADMIN_LOG where 1=1 " + dt1 + dt2 + " order by LOGDATE desc";
			PreparedStatement prepStmt = conn.prepareStatement(selectStmt);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<LOGS> dlist = FXCollections.observableArrayList();
			while (rs.next()) {
				LOGS list = new LOGS();
				list.setOPER(rs.getString("OPER"));
				list.setLOGDATE((rs.getDate("LOGDATE") != null) ? LocalDateTime.parse(
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(rs.getDate("LOGDATE")), formatter) : null);
				list.setMETHODNAME(rs.getString("METHODNAME"));
				if (rs.getClob("ERROR") != null) {
					list.setERROR(new ConvConst().ClobToString(rs.getClob("ERROR")));
				}
				list.setCLASSNAME(rs.getString("CLASSNAME"));
				list.setLINENUMBER(rs.getLong("LINENUMBER"));

				dlist.add(list);
			}
			prepStmt.close();
			rs.close();

			LOGS.setItems(dlist);

			TableFilter<LOGS> tableFilter = TableFilter.forTableView(LOGS).apply();
			tableFilter.setSearchStrategy((input, target) -> {
				try {
					return target.toLowerCase().contains(input.toLowerCase());
				} catch (Exception e) {
					return false;
				}
			});
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	Connection conn;

	private void dbConnect() {
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
		} catch (Exception e) {
			DbUtil.Log_Error(e);
		}
	}

	public void dbDisconnect() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.rollback();
				conn.close();
			}
		} catch (SQLException e) {
			DbUtil.Log_Error(e);
		}
	}

}

package sb_tr.controller;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import net.sf.jasperreports.engine.JRException;
import sb_tr.Main;
import sb_tr.model.Amra_Trans;
import sb_tr.model.Attributes;
import sb_tr.model.Connect;
import sb_tr.model.FN_SESS_AMRA;
import sb_tr.model.GUIUtils;
import sb_tr.model.Ibank2;
import sb_tr.model.TerminalClass;
import sb_tr.model.TerminalDAO;
import sb_tr.model.TerminalForCombo;
import sb_tr.model.Transact;
import sb_tr.model.TransactClass;
import sb_tr.model.ViewerDAO;
import sb_tr.model.pensmodel;
import sb_tr.util.DBUtil;

import java.util.Random;
import java.util.function.Function;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.control.table.TableFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.sun.prism.impl.Disposer.Record;

/**
 * ���� 04.04.2019.
 */

@SuppressWarnings("unused")
public class Tr_Am_View_con {

	@FXML
	private TableColumn<Amra_Trans, String> filetransactions;

	@FXML
	private CheckBox inkass;

	@FXML
	private CheckBox ret_pay;

	@FXML
	private Button search;

	@FXML
	private TableColumn<Amra_Trans, Boolean> chk_row;

	@FXML
	private TableColumn<Amra_Trans, String> fio;

	@FXML
	private TableColumn<Amra_Trans, String> rewardamount;

	@FXML
	private TableColumn<Amra_Trans, String> operationnumber;

	@FXML
	private TableColumn<Amra_Trans, String> detailing;

	@FXML
	private TableColumn<Amra_Trans, Integer> sess_id;

	@FXML
	private TableColumn<Amra_Trans, String> valuenotfound;

	@FXML
	private TableColumn<Amra_Trans, String> counterchecks;

	@FXML
	private TableColumn<Amra_Trans, String> walletreceiver;

	@FXML
	private TableColumn<Amra_Trans, String> dataps;

	@FXML
	private TableColumn<Amra_Trans, String> barcode;

	@FXML
	private TableColumn<Amra_Trans, String> purposeofpayment;

	@FXML
	private TableColumn<Amra_Trans, String> terminalnetwork;

	@FXML
	private TableColumn<Amra_Trans, String> owninown;

	@FXML
	private TableColumn<Amra_Trans, String> id_;

	@FXML
	private TableColumn<Amra_Trans, String> amountintermediary;

	@FXML
	private TableColumn<Amra_Trans, Double> commissionamount;

	@FXML
	private TableColumn<Amra_Trans, String> vk;

	@FXML
	private TableColumn<Amra_Trans, Double> sumnalprimal;

	@FXML
	private TableColumn<Amra_Trans, String> dateofoperation;

	@FXML
	private TableColumn<Amra_Trans, String> amountofscs;

	@FXML
	private TableColumn<Amra_Trans, String> cardnumber;

	@FXML
	private TableColumn<Amra_Trans, String> accountpayer;

	@FXML
	private TableColumn<Amra_Trans, LocalDateTime> recdate;

	@FXML
	private TableColumn<Amra_Trans, String> checknumber;

	@FXML
	private TableColumn<Amra_Trans, String> status;

	@FXML
	private TableColumn<Amra_Trans, String> mincommissionamount;

	@FXML
	private TableColumn<Amra_Trans, Integer> rownum;

	@FXML
	private TableColumn<Amra_Trans, Double> amounttocheck;

	@FXML
	private TableColumn<Amra_Trans, String> dateclearing;

	@FXML
	private TableColumn<Amra_Trans, LocalDateTime> paydate;

	@FXML
	private TextField summa_plat;

	@FXML
	private TextField summa_nal;

	@FXML
	private TextField cnt_all_;

	@FXML
	private Button xlsx;

	@FXML
	private TableColumn<Amra_Trans, String> ownerincomeamount;

	@FXML
	private TableColumn<Amra_Trans, String> maxcommissionamount;

	@FXML
	private TableColumn<Amra_Trans, String> transactiontype;

	@FXML
	private TableColumn<Amra_Trans, String> isaresident;

	@FXML
	private TableColumn<Amra_Trans, String> countercheck;

	@FXML
	private TableColumn<Amra_Trans, String> operationnumberdelivery;

	@FXML
	private TableColumn<Amra_Trans, String> provider;

	@FXML
	private TableColumn<Amra_Trans, String> currency;

	@FXML
	private TableColumn<Amra_Trans, Double> amountwithchecks;

	@FXML
	private TableColumn<Amra_Trans, String> statusabs;

	@FXML
	private TableColumn<Amra_Trans, String> walletpayer;

	@FXML
	private TableColumn<Amra_Trans, String> sumofsplitting;

	@FXML
	private TableColumn<Amra_Trans, String> providertariff;

	@FXML
	private TableColumn<Amra_Trans, Double> nkamount;

	@FXML
	private TableColumn<Amra_Trans, Double> amountofpayment;

	@FXML
	private TableColumn<Amra_Trans, String> counter;

	@FXML
	private TableColumn<Amra_Trans, String> terminal;

	@FXML
	private ProgressBar progress_export;

	@FXML
	private TableColumn<Amra_Trans, String> checksincoming;

	@FXML
	private TableColumn<Amra_Trans, String> corrected;

	@FXML
	private TableColumn<Amra_Trans, String> stringfromfile;

	@FXML
	private TableColumn<Amra_Trans, String> checkparent;

	@FXML
	private TableColumn<Amra_Trans, String> commissionrate;

	@FXML
	private TableColumn<Amra_Trans, Double> cashamount;

	@FXML
	private TableColumn<Amra_Trans, String> service;

	@FXML
	private TableColumn<Amra_Trans, String> dealer;

	@FXML
	private TableView<Amra_Trans> trans_table;

	@FXML
	private TableColumn<Amra_Trans, String> paymenttype;

	@FXML
	private TableColumn<Amra_Trans, String> dataprovider;

	@FXML
	private TextField id_sess;

	@FXML
	private TextField FIO;

	@FXML
	private TableColumn<Amra_Trans, String> orderofprovidence;

	// For MultiThreading
	private Executor exec;

	@FXML
	private DatePicker dt1;

	@FXML
	private BorderPane pane_1;

	@FXML
	private DatePicker dt2;

	@FXML
	private ProgressIndicator pb;

	@FXML
	private ComboBox<String> terminal_name;

	@FXML
	void show_(ActionEvent l) {

	}

	@FXML
	private BorderPane pane_;

	@FXML
	private AnchorPane anch_b4;

	private void validate(CheckBox checkBox, Amra_Trans item, Event event) throws Exception {
		// Validate here
		event.consume();
		/*
		 * Alert alert = new Alert(AlertType.CONFIRMATION); alert.setTitle("������");
		 * alert.setHeaderText("����������� �� �������");
		 * alert.setContentText("��������?");
		 * 
		 * // Set the checkbox if the user want to continue Optional<ButtonType> result
		 * = alert.showAndWait(); if (result.get() == ButtonType.OK) {
		 * checkBox.setSelected(!checkBox.isSelected()); }
		 */
		if (!checkBox.isSelected()) {
			if (!summa_plat.getText().equals("") | !summa_plat.getText().equals("0")
					| !summa_plat.getText().trim().isEmpty()) {
				all_sum_nal = Double.parseDouble(summa_nal.getText().replace(",", ".").replace("�", ""))
						+ item.cashamountProperty().getValue();
				all_sum = Double.parseDouble(summa_plat.getText().replace(",", ".").replace("�", ""))
						+ item.amountofpaymentProperty().getValue();
				cnt = Integer.parseInt(cnt_all_.getText()) + 1;

				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String format = decimalFormat.format(all_sum);
				String format_nal = decimalFormat.format(all_sum_nal);
				cnt_all_.setText(String.valueOf(cnt));
				summa_plat.setText(String.valueOf(format));
				summa_nal.setText(format_nal);
				checkBox.setSelected(true);

				// System.out.println("Check=" + item.get_chk_row());

			} else {
				all_sum_nal = all_sum_nal + item.cashamountProperty().getValue();
				all_sum = all_sum + item.amountofpaymentProperty().getValue();
				cnt = cnt + 1;

				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String format = decimalFormat.format(all_sum);
				String format_nal = decimalFormat.format(all_sum_nal);
				cnt_all_.setText(String.valueOf(cnt));
				summa_plat.setText(String.valueOf(format));
				summa_nal.setText(format_nal);
				checkBox.setSelected(true);
				// System.out.println("Check=" + item.get_chk_row());
			}
			// item.set_chk_row(true);

		} else {
			if (!summa_plat.getText().equals("") | summa_plat.getText().equals("0")
					| !summa_plat.getText().trim().isEmpty()) {
				all_sum_nal = Double.parseDouble(summa_nal.getText().replace(",", ".").replace("�", ""))
						- item.cashamountProperty().getValue();
				all_sum = Double.parseDouble(summa_plat.getText().replace(",", ".").replace("�", ""))
						- item.amountofpaymentProperty().getValue();
				cnt = Integer.parseInt(cnt_all_.getText()) - 1;

				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String format = decimalFormat.format(all_sum);
				String format_nal = decimalFormat.format(all_sum_nal);
				cnt_all_.setText(String.valueOf(cnt));
				summa_plat.setText(String.valueOf(format));
				summa_nal.setText(format_nal);
				checkBox.setSelected(false);
				// System.out.println("Check=" + item.get_chk_row());
			} else {
				all_sum_nal = all_sum_nal - item.cashamountProperty().getValue();
				all_sum = all_sum - item.amountofpaymentProperty().getValue();
				cnt = cnt - 1;

				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String format = decimalFormat.format(all_sum);
				String format_nal = decimalFormat.format(all_sum_nal);
				cnt_all_.setText(String.valueOf(cnt));
				summa_plat.setText(String.valueOf(format));
				summa_nal.setText(format_nal);
				checkBox.setSelected(false);
				// System.out.println("Check=" + item.get_chk_row());
			}

			// item.set_chk_row(false);
		}
		// trans_table.refresh();
	}

	@FXML
	private void initialize() throws Exception {
		trans_table.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		chk_row.setCellValueFactory(cellData -> cellData.getValue().chk_rowProperty());

		rownum.setCellValueFactory(cellData -> cellData.getValue().rownumProperty().asObject());
		recdate.setCellValueFactory(cellData -> cellData.getValue().recdateProperty());
		paydate.setCellValueFactory(cellData -> cellData.getValue().paydateProperty());
		currency.setCellValueFactory(cellData -> cellData.getValue().currencyProperty());
		// paymenttype.setCellValueFactory(cellData ->
		// cellData.getValue().paymenttypeProperty());
		vk.setCellValueFactory(cellData -> cellData.getValue().vkProperty());
		// dateofoperation.setCellValueFactory(cellData ->
		// cellData.getValue().dateofoperationProperty());
		// dataps.setCellValueFactory(cellData -> cellData.getValue().datapsProperty());
		// dateclearing.setCellValueFactory(cellData ->
		// cellData.getValue().dateclearingProperty());
		dealer.setCellValueFactory(cellData -> cellData.getValue().dealerProperty());
		accountpayer.setCellValueFactory(cellData -> cellData.getValue().accountpayerProperty());
		// cardnumber.setCellValueFactory(cellData ->
		// cellData.getValue().cardnumberProperty());
		operationnumber.setCellValueFactory(cellData -> cellData.getValue().operationnumberProperty());
		// operationnumberdelivery.setCellValueFactory(cellData ->
		// cellData.getValue().operationnumberdeliveryProperty());
		checknumber.setCellValueFactory(cellData -> cellData.getValue().checknumberProperty());
		checkparent.setCellValueFactory(cellData -> cellData.getValue().checkparentProperty());
		// orderofprovidence.setCellValueFactory(cellData ->
		// cellData.getValue().orderofprovidenceProperty());
		provider.setCellValueFactory(cellData -> cellData.getValue().providerProperty());
		owninown.setCellValueFactory(cellData -> cellData.getValue().owninownProperty());
		// corrected.setCellValueFactory(cellData ->
		// cellData.getValue().correctedProperty());
		// commissionrate.setCellValueFactory(cellData ->
		// cellData.getValue().commissionrateProperty());
		status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		// stringfromfile.setCellValueFactory(cellData ->
		// cellData.getValue().stringfromfileProperty());
		// rewardamount.setCellValueFactory(cellData ->
		// cellData.getValue().rewardamountProperty());
		// ownerincomeamount.setCellValueFactory(cellData ->
		// cellData.getValue().ownerincomeamountProperty());
		commissionamount.setCellValueFactory(cellData -> cellData.getValue().commissionamountProperty().asObject());
		nkamount.setCellValueFactory(cellData -> cellData.getValue().nkamountProperty().asObject());
		// maxcommissionamount.setCellValueFactory(cellData ->
		// cellData.getValue().maxcommissionamountProperty());
		// mincommissionamount.setCellValueFactory(cellData ->
		// cellData.getValue().mincommissionamountProperty());
		cashamount.setCellValueFactory(cellData -> cellData.getValue().cashamountProperty().asObject());
		sumnalprimal.setCellValueFactory(cellData -> cellData.getValue().sumnalprimalProperty().asObject());
		amounttocheck.setCellValueFactory(cellData -> cellData.getValue().amounttocheckProperty().asObject());
		amountofpayment.setCellValueFactory(cellData -> cellData.getValue().amountofpaymentProperty().asObject());
		// sumofsplitting.setCellValueFactory(cellData ->
		// cellData.getValue().sumofsplittingProperty());
		// amountintermediary.setCellValueFactory(cellData ->
		// cellData.getValue().amountintermediaryProperty());
		// amountofscs.setCellValueFactory(cellData ->
		// cellData.getValue().amountofscsProperty());
		amountwithchecks.setCellValueFactory(cellData -> cellData.getValue().amountwithchecksProperty().asObject());
		// counter.setCellValueFactory(cellData ->
		// cellData.getValue().counterProperty());
		terminal.setCellValueFactory(cellData -> cellData.getValue().terminalProperty());
		terminalnetwork.setCellValueFactory(cellData -> cellData.getValue().terminalnetworkProperty());
		transactiontype.setCellValueFactory(cellData -> cellData.getValue().transactiontypeProperty());
		service.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
		// filetransactions.setCellValueFactory(cellData ->
		// cellData.getValue().filetransactionsProperty());
		// fio.setCellValueFactory(cellData -> cellData.getValue().fioProperty());
		// checksincoming.setCellValueFactory(cellData ->
		// cellData.getValue().checksincomingProperty());
		/*
		 * barcode.setCellValueFactory(cellData ->
		 * cellData.getValue().barcodeProperty());
		 * isaresident.setCellValueFactory(cellData ->
		 * cellData.getValue().isaresidentProperty());
		 * valuenotfound.setCellValueFactory(cellData ->
		 * cellData.getValue().valuenotfoundProperty());
		 * providertariff.setCellValueFactory(cellData ->
		 * cellData.getValue().providertariffProperty());
		 * counterchecks.setCellValueFactory(cellData ->
		 * cellData.getValue().counterchecksProperty());
		 * countercheck.setCellValueFactory(cellData ->
		 * cellData.getValue().countercheckProperty()); id_.setCellValueFactory(cellData
		 * -> cellData.getValue().id_Property()); detailing.setCellValueFactory(cellData
		 * -> cellData.getValue().detailingProperty());
		 * walletpayer.setCellValueFactory(cellData ->
		 * cellData.getValue().walletpayerProperty());
		 * walletreceiver.setCellValueFactory(cellData ->
		 * cellData.getValue().walletreceiverProperty());
		 * purposeofpayment.setCellValueFactory(cellData ->
		 * cellData.getValue().purposeofpaymentProperty());
		 */
		// dataprovider.setCellValueFactory(cellData ->
		// cellData.getValue().dataproviderProperty());
		statusabs.setCellValueFactory(cellData -> cellData.getValue().statusabsProperty());
		sess_id.setCellValueFactory(cellData -> cellData.getValue().sess_idProperty().asObject());

		// chk_row.setCellFactory(p -> new CheckBoxTableCell<>());

		/*
		 * trans_table.setRowFactory(row -> new TableRow<Amra_Trans>() {
		 * 
		 * @Override public void updateItem(Amra_Trans item, boolean empty) {
		 * super.updateItem(item, empty);
		 * 
		 * if (item == null || empty) { setStyle(""); } else { // Now 'item' has all the
		 * info of the Person in this row if (chk_row.getCellData(item)) {
		 * setStyle("-fx-background-color: tomato;"); } else { } } } });
		 */
		/*
		 * chk_row.setCellFactory(column -> { return new TableCell<Amra_Trans,
		 * Boolean>() {
		 * 
		 * @Override protected void updateItem(Boolean item, boolean empty) {
		 * super.updateItem(item, empty);
		 * 
		 * setText(empty ? "" : getItem().toString()); setGraphic(null);
		 * 
		 * TableRow<Amra_Trans> currentRow = getTableRow();
		 * 
		 * if (!isEmpty()) {
		 * 
		 * if(item.equals("a")) currentRow.setStyle("-fx-background-color:lightcoral");
		 * else currentRow.setStyle("-fx-background-color:lightgreen"); } } }; });
		 */

		recdate.setCellFactory(column -> {
			TableCell<Amra_Trans, LocalDateTime> cell = new TableCell<Amra_Trans, LocalDateTime>() {
				private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

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

		paydate.setCellFactory(column -> {
			TableCell<Amra_Trans, LocalDateTime> cell = new TableCell<Amra_Trans, LocalDateTime>() {
				private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

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

		recdate.setCellFactory(
				TextFieldTableCell.<Amra_Trans, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));
		paydate.setCellFactory(
				TextFieldTableCell.<Amra_Trans, LocalDateTime>forTableColumn(new LocalDateTimeStringConverter()));
		currency.setCellFactory(TextFieldTableCell.forTableColumn());
		// paymenttype.setCellFactory(TextFieldTableCell.forTableColumn());
		vk.setCellFactory(TextFieldTableCell.forTableColumn());
		// dateofoperation.setCellFactory(TextFieldTableCell.forTableColumn());
		// dataps.setCellFactory(TextFieldTableCell.forTableColumn());
		// dateclearing.setCellFactory(TextFieldTableCell.forTableColumn());
		dealer.setCellFactory(TextFieldTableCell.forTableColumn());
		accountpayer.setCellFactory(TextFieldTableCell.forTableColumn());
		// cardnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		operationnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		// operationnumberdelivery.setCellFactory(TextFieldTableCell.forTableColumn());
		checknumber.setCellFactory(TextFieldTableCell.forTableColumn());
		checkparent.setCellFactory(TextFieldTableCell.forTableColumn());
		// orderofprovidence.setCellFactory(TextFieldTableCell.forTableColumn());
		// provider.setCellFactory(TextFieldTableCell.forTableColumn());
		owninown.setCellFactory(TextFieldTableCell.forTableColumn());
		// corrected.setCellFactory(TextFieldTableCell.forTableColumn());
		// commissionrate.setCellFactory(TextFieldTableCell.forTableColumn());
		// status.setCellFactory(TextFieldTableCell.forTableColumn());
		// stringfromfile.setCellFactory(TextFieldTableCell.forTableColumn());
		// rewardamount.setCellFactory(TextFieldTableCell.forTableColumn());
		// ownerincomeamount.setCellFactory(TextFieldTableCell.forTableColumn());
		commissionamount
				.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		nkamount.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		// maxcommissionamount.setCellFactory(TextFieldTableCell.forTableColumn());
		// mincommissionamount.setCellFactory(TextFieldTableCell.forTableColumn());
		cashamount.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		sumnalprimal.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		amounttocheck
				.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		amountofpayment
				.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		// sumofsplitting.setCellFactory(TextFieldTableCell.forTableColumn());
		// amountintermediary.setCellFactory(TextFieldTableCell.forTableColumn());
		// amountofscs.setCellFactory(TextFieldTableCell.forTableColumn());
		amountwithchecks
				.setCellFactory(TextFieldTableCell.<Amra_Trans, Double>forTableColumn(new DoubleStringConverter()));
		// counter.setCellFactory(TextFieldTableCell.forTableColumn());
		terminal.setCellFactory(TextFieldTableCell.forTableColumn());
		terminalnetwork.setCellFactory(TextFieldTableCell.forTableColumn());
		transactiontype.setCellFactory(TextFieldTableCell.forTableColumn());
		service.setCellFactory(TextFieldTableCell.forTableColumn());
		// filetransactions.setCellFactory(TextFieldTableCell.forTableColumn());
		// fio.setCellFactory(TextFieldTableCell.forTableColumn());
		// checksincoming.setCellFactory(TextFieldTableCell.forTableColumn());
		/*
		 * barcode.setCellFactory(TextFieldTableCell.forTableColumn());
		 * isaresident.setCellFactory(TextFieldTableCell.forTableColumn());
		 * valuenotfound.setCellFactory(TextFieldTableCell.forTableColumn());
		 * providertariff.setCellFactory(TextFieldTableCell.forTableColumn());
		 * counterchecks.setCellFactory(TextFieldTableCell.forTableColumn());
		 * countercheck.setCellFactory(TextFieldTableCell.forTableColumn());
		 * id_.setCellFactory(TextFieldTableCell.forTableColumn());
		 * detailing.setCellFactory(TextFieldTableCell.forTableColumn());
		 * walletpayer.setCellFactory(TextFieldTableCell.forTableColumn());
		 * walletreceiver.setCellFactory(TextFieldTableCell.forTableColumn());
		 * purposeofpayment.setCellFactory(TextFieldTableCell.forTableColumn());
		 */
		// dataprovider.setCellFactory(TextFieldTableCell.forTableColumn());
		statusabs.setCellFactory(TextFieldTableCell.forTableColumn());
		sess_id.setCellFactory(TextFieldTableCell.<Amra_Trans, Integer>forTableColumn(new IntegerStringConverter()));

		recdate.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, LocalDateTime>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, LocalDateTime> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_recdate(t.getNewValue());
			}
		});
		paydate.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, LocalDateTime>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, LocalDateTime> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_paydate(t.getNewValue());
			}
		});
		currency.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_currency(t.getNewValue());
			}
		});
		/*
		 * paymenttype.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_paymenttype(t.getNewValue()); } });
		 */
		vk.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_vk(t.getNewValue());
			}
		});
		chk_row.setCellFactory(p -> {
			CheckBox checkBox = new CheckBox();
			TableCell<Amra_Trans, Boolean> tableCell = new TableCell<Amra_Trans, Boolean>() {
				@Override
				protected void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setGraphic(null);
					} else {
						setGraphic(checkBox);
						checkBox.setSelected(item);
					}
				}
			};

			checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				try {
					validate(checkBox, (Amra_Trans) tableCell.getTableRow().getItem(), event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Alert(e.getMessage());
				}
			});

			checkBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.SPACE)
					try {
						validate(checkBox, (Amra_Trans) tableCell.getTableRow().getItem(), event);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Alert(e.getMessage());
					}
			});

			tableCell.setAlignment(Pos.CENTER);
			tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			return tableCell;
		});

		/*
		 * dateofoperation.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_dateofoperation(t.getNewValue()); } }); dataps.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_dataps(t.getNewValue()); } }); dateclearing.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_dateclearing(t.getNewValue()); } });
		 */
		dealer.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dealer(t.getNewValue());
			}
		});
		accountpayer.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_accountpayer(t.getNewValue());
			}
		});
		/*
		 * cardnumber.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_cardnumber(t.getNewValue()); } });
		 */
		operationnumber.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_operationnumber(t.getNewValue());
			}
		});
		/*
		 * operationnumberdelivery.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_operationnumberdelivery(t.getNewValue()); } });
		 */
		checknumber.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_checknumber(t.getNewValue());
			}
		});
		checkparent.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_checkparent(t.getNewValue());
			}
		});
		/*
		 * orderofprovidence.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_orderofprovidence(t.getNewValue()); } });
		 */
		provider.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_provider(t.getNewValue());
			}
		});
		owninown.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_owninown(t.getNewValue());
			}
		});
		/*
		 * corrected.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_corrected(t.getNewValue()); } });
		 */
		/*
		 * commissionrate.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_commissionrate(t.getNewValue()); } });
		 */
		/*
		 * stringfromfile.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_stringfromfile(t.getNewValue()); } });
		 */
		/*
		 * rewardamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_rewardamount(t.getNewValue()); } });
		 */
		/*
		 * ownerincomeamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_ownerincomeamount(t.getNewValue()); } });
		 */
		commissionamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_commissionamount(t.getNewValue());
			}
		});
		nkamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_nkamount(t.getNewValue());
			}
		});
		/*
		 * maxcommissionamount.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_maxcommissionamount(t.getNewValue()); } });
		 */
		/*
		 * mincommissionamount.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_mincommissionamount(t.getNewValue()); } });
		 */
		cashamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_cashamount(t.getNewValue());
			}
		});
		sumnalprimal.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sumnalprimal(t.getNewValue());
			}
		});
		amounttocheck.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amounttocheck(t.getNewValue());
			}
		});
		amountofpayment.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amountofpayment(t.getNewValue());
			}
		});
		/*
		 * sumofsplitting.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_sumofsplitting(t.getNewValue()); } });
		 */
		/*
		 * amountintermediary.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_amountintermediary(t.getNewValue()); } });
		 */
		/*
		 * amountofscs.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_amountofscs(t.getNewValue()); } });
		 */
		amountwithchecks.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Double>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Double> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amountwithchecks(t.getNewValue());
			}
		});
		/*
		 * counter.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>()
		 * {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_counter(t.getNewValue()); } });
		 */
		terminal.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_terminal(t.getNewValue());
			}
		});
		terminalnetwork.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_terminalnetwork(t.getNewValue());
			}
		});
		transactiontype.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_transactiontype(t.getNewValue());
			}
		});
		service.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_service(t.getNewValue());
			}
		});
		/*
		 * filetransactions.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_filetransactions(t.getNewValue()); } });
		 */
		/*
		 * fio.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans)
		 * t.getTableView().getItems().get(t.getTablePosition().getRow())).set_fio(t.
		 * getNewValue()); } });
		 */
		/*
		 * checksincoming.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_checksincoming(t.getNewValue()); } });
		 */
		/*
		 * barcode.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>()
		 * {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_barcode(t.getNewValue()); } }); isaresident.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_isaresident(t.getNewValue()); } }); valuenotfound.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_valuenotfound(t.getNewValue()); } }); providertariff.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_providertariff(t.getNewValue()); } }); counterchecks.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_counterchecks(t.getNewValue()); } }); countercheck.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_countercheck(t.getNewValue()); } }); id_.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans)
		 * t.getTableView().getItems().get(t.getTablePosition().getRow())).set_id_(t.
		 * getNewValue()); } }); detailing.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_detailing(t.getNewValue()); } }); walletpayer.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_walletpayer(t.getNewValue()); } }); walletreceiver.setOnEditCommit(new
		 * EventHandler<CellEditEvent<Amra_Trans, String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_walletreceiver(t.getNewValue()); } });
		 * purposeofpayment.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_purposeofpayment(t.getNewValue()); } });
		 */
		/*
		 * dataprovider.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans,
		 * String>>() {
		 * 
		 * @Override public void handle(CellEditEvent<Amra_Trans, String> t) {
		 * ((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
		 * .set_dataprovider(t.getNewValue()); } });
		 */
		statusabs.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_statusabs(t.getNewValue());
			}
		});
		sess_id.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, Integer>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, Integer> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sess_id(t.getNewValue());
			}
		});

		status.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_status(t.getNewValue());
			}
		});
		cnt_all_.setText(String.valueOf(0));
		summa_plat.setText(String.valueOf(0));
		summa_nal.setText(String.valueOf(0));

		try {
			/*
			 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
			 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
			 * + "");
			 */
			Connection conn = DBUtil.conn;
			Statement sqlStatement = conn.createStatement();
			String readRecordSQL = "select NAME\r\n" + "  from (select NAME\r\n"
					+ "          from Z_SB_TERMINAL_AMRA_DBT t\r\n" + "        union all\r\n"
					+ "        select '���' NAME\r\n" + "          from dual)\r\n"
					+ " order by decode(NAME, '���', 0, substr(NAME, length(NAME), 1)) asc\r\n" + "";
			ResultSet rs = sqlStatement.executeQuery(readRecordSQL);
			ObservableList<String> combolist = FXCollections.observableArrayList();
			while (rs.next()) {
				TerminalForCombo tr = new TerminalForCombo();
				tr.setTERMS(rs.getString("NAME"));
				combolist.add(rs.getString("NAME"));
			}
			terminal_name.setItems(combolist);
			terminal_name.getSelectionModel().select(0);

			rs.close();
			// conn.close();
			sqlStatement.close();

		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

	}

	void on_filter() {
		autoResizeColumns(trans_table);
		@SuppressWarnings("deprecation")
		TableFilter<Amra_Trans> filter = new TableFilter<>(trans_table);
	}

	/*
	 * @FXML private void filter(ActionEvent event) { Task<List<Amra_Trans>> task_ =
	 * new Task<List<Amra_Trans>>() {
	 * 
	 * @Override public ObservableList<Amra_Trans> call() { return null; } };
	 * task_.setOnFailed(e -> print_mess(task_.getException().toString()));
	 * task_.setOnSucceeded(e -> pb.setVisible(true)); exec.execute(task_);
	 * 
	 * Task<List<Amra_Trans>> task = new Task<List<Amra_Trans>>() {
	 * 
	 * @Override public ObservableList<Amra_Trans> call() { if (inkass.isSelected())
	 * { return TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(),
	 * dt2.getValue(), false); } else { return
	 * TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(),
	 * true); } } }; task.setOnFailed(e ->
	 * print_mess(task.getException().toString())); task.setOnSucceeded(e ->
	 * trans_table.setItems((ObservableList<Amra_Trans>) task.getValue()));
	 * exec.execute(task); // on_filter(); pb.setVisible(false); }
	 */
	void print_mess(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText(text);
				alert.showAndWait();
			}
		});
	}

	@FXML
	private void excel_export_(ActionEvent actionEvent) {
		try {
			FileChooser fileChooser = new FileChooser();

			System.setProperty("javax.xml.transform.TransformerFactory",
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

			// Set extension filter for text files
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xlsx");
			fileChooser.getExtensionFilters().add(extFilter);

			// Show save file dialog
			File file = fileChooser.showSaveDialog(null);

			if (file != null) {

				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
				String strDate = dateFormat.format(date);
				XSSFWorkbook book = new XSSFWorkbook();
				XSSFSheet sheet = book.createSheet(strDate);
				Row row_d = sheet.createRow(0);

				XSSFFont defaultFont = book.createFont();
				defaultFont.setFontHeightInPoints((short) 12);
				defaultFont.setFontName("Arial");
				defaultFont.setColor(IndexedColors.BLACK.getIndex());
				defaultFont.setBold(true);
				defaultFont.setItalic(false);

				CellStyle cellStyle_border_for_cell = book.createCellStyle();
				cellStyle_border_for_cell.setBorderTop(BorderStyle.THIN);
				cellStyle_border_for_cell.setBorderBottom(BorderStyle.THIN);
				cellStyle_border_for_cell.setBorderLeft(BorderStyle.THIN);
				cellStyle_border_for_cell.setBorderRight(BorderStyle.THIN);
				cellStyle_border_for_cell.setFont(defaultFont);

				Cell recdate_c = row_d.createCell(0);
				recdate_c.setCellValue("���� ��������");
				Cell paydate_c = row_d.createCell(1);
				paydate_c.setCellValue("���� ����������");
				Cell currency_c = row_d.createCell(2);
				currency_c.setCellValue("������");
				Cell paymenttype_c = row_d.createCell(3);
				paymenttype_c.setCellValue("����������");
				Cell vk_c = row_d.createCell(4);
				vk_c.setCellValue("��");
				Cell dateofoperation_c = row_d.createCell(5);
				dateofoperation_c.setCellValue("������������");
				Cell dataps_c = row_d.createCell(6);
				dataps_c.setCellValue("������");
				Cell dateclearing_c = row_d.createCell(7);
				dateclearing_c.setCellValue("������������");
				Cell dealer_c = row_d.createCell(8);
				dealer_c.setCellValue("�����");
				Cell accountpayer_c = row_d.createCell(9);
				accountpayer_c.setCellValue("�������������");
				Cell cardnumber_c = row_d.createCell(10);
				cardnumber_c.setCellValue("����������");
				Cell operationnumber_c = row_d.createCell(11);
				operationnumber_c.setCellValue("�������������");
				Cell operationnumberdelivery_c = row_d.createCell(12);
				operationnumberdelivery_c.setCellValue("������������������");
				Cell checknumber_c = row_d.createCell(13);
				checknumber_c.setCellValue("���������");
				Cell checkparent_c = row_d.createCell(14);
				checkparent_c.setCellValue("�����������");
				Cell orderofprovidence_c = row_d.createCell(15);
				orderofprovidence_c.setCellValue("����������������");
				Cell provider_c = row_d.createCell(16);
				provider_c.setCellValue("���������");
				Cell owninown_c = row_d.createCell(17);
				owninown_c.setCellValue("����������");
				Cell corrected_c = row_d.createCell(18);
				corrected_c.setCellValue("���������������");
				Cell commissionrate_c = row_d.createCell(19);
				commissionrate_c.setCellValue("��������������");
				Cell status_c = row_d.createCell(20);
				status_c.setCellValue("������");
				Cell stringfromfile_c = row_d.createCell(21);
				stringfromfile_c.setCellValue("�������������");
				Cell rewardamount_c = row_d.createCell(22);
				rewardamount_c.setCellValue("�������������������");
				Cell ownerincomeamount_c = row_d.createCell(23);
				ownerincomeamount_c.setCellValue("�������������������");
				Cell commissionamount_c = row_d.createCell(24);
				commissionamount_c.setCellValue("�������������");
				Cell nkamount_c = row_d.createCell(25);
				nkamount_c.setCellValue("�������");
				Cell maxcommissionamount_c = row_d.createCell(26);
				maxcommissionamount_c.setCellValue("�����������������");
				Cell mincommissionamount_c = row_d.createCell(27);
				mincommissionamount_c.setCellValue("����������������");
				Cell cashamount_c = row_d.createCell(28);
				cashamount_c.setCellValue("�������������");
				Cell sumnalprimal_c = row_d.createCell(29);
				sumnalprimal_c.setCellValue("�������������������");
				Cell amounttocheck_c = row_d.createCell(30);
				amounttocheck_c.setCellValue("����������");
				Cell amountofpayment_c = row_d.createCell(31);
				amountofpayment_c.setCellValue("������������");
				Cell sumofsplitting_c = row_d.createCell(32);
				sumofsplitting_c.setCellValue("������������������");
				Cell amountintermediary_c = row_d.createCell(33);
				amountintermediary_c.setCellValue("���������������");
				Cell amountofscs_c = row_d.createCell(34);
				amountofscs_c.setCellValue("��������");
				Cell amountwithchecks_c = row_d.createCell(35);
				amountwithchecks_c.setCellValue("�����������");
				Cell counter_c = row_d.createCell(36);
				counter_c.setCellValue("�������");
				Cell terminal_c = row_d.createCell(37);
				terminal_c.setCellValue("��������");
				Cell terminalnetwork_c = row_d.createCell(38);
				terminalnetwork_c.setCellValue("����������������");
				Cell transactiontype_c = row_d.createCell(39);
				transactiontype_c.setCellValue("�������������");
				Cell service_c = row_d.createCell(40);
				service_c.setCellValue("������");
				Cell filetransactions_c = row_d.createCell(41);
				filetransactions_c.setCellValue("��������������");
				Cell fio_c = row_d.createCell(42);
				fio_c.setCellValue("���");
				Cell checksincoming_c = row_d.createCell(43);
				checksincoming_c.setCellValue("������������");
				Cell barcode_c = row_d.createCell(44);
				barcode_c.setCellValue("��������");
				Cell isaresident_c = row_d.createCell(45);
				isaresident_c.setCellValue("������������������");
				Cell valuenotfound_c = row_d.createCell(46);
				valuenotfound_c.setCellValue("�����������������");
				Cell providertariff_c = row_d.createCell(47);
				providertariff_c.setCellValue("���������������");
				Cell counterchecks_c = row_d.createCell(48);
				counterchecks_c.setCellValue("�������������");
				Cell countercheck_c = row_d.createCell(49);
				countercheck_c.setCellValue("������������");
				Cell id__c = row_d.createCell(50);
				id__c.setCellValue("Id");
				Cell detailing_c = row_d.createCell(51);
				detailing_c.setCellValue("�����������");
				Cell walletpayer_c = row_d.createCell(52);
				walletpayer_c.setCellValue("�����������������");
				Cell walletreceiver_c = row_d.createCell(53);
				walletreceiver_c.setCellValue("�����������������");
				Cell purposeofpayment_c = row_d.createCell(54);
				purposeofpayment_c.setCellValue("�����������������");
				Cell dataprovider_c = row_d.createCell(55);
				dataprovider_c.setCellValue("��������������");
				Cell attributes__c = row_d.createCell(56);
				attributes__c.setCellValue("��������");
				Cell statusabs_c = row_d.createCell(57);
				statusabs_c.setCellValue("������ ����������");
				Cell sess_id_c = row_d.createCell(58);
				sess_id_c.setCellValue("�� ������");

				recdate_c.setCellStyle(cellStyle_border_for_cell);
				paydate_c.setCellStyle(cellStyle_border_for_cell);
				currency_c.setCellStyle(cellStyle_border_for_cell);
				paymenttype_c.setCellStyle(cellStyle_border_for_cell);
				vk_c.setCellStyle(cellStyle_border_for_cell);
				dateofoperation_c.setCellStyle(cellStyle_border_for_cell);
				dataps_c.setCellStyle(cellStyle_border_for_cell);
				dateclearing_c.setCellStyle(cellStyle_border_for_cell);
				dealer_c.setCellStyle(cellStyle_border_for_cell);
				accountpayer_c.setCellStyle(cellStyle_border_for_cell);
				cardnumber_c.setCellStyle(cellStyle_border_for_cell);
				operationnumber_c.setCellStyle(cellStyle_border_for_cell);
				operationnumberdelivery_c.setCellStyle(cellStyle_border_for_cell);
				checknumber_c.setCellStyle(cellStyle_border_for_cell);
				checkparent_c.setCellStyle(cellStyle_border_for_cell);
				orderofprovidence_c.setCellStyle(cellStyle_border_for_cell);
				provider_c.setCellStyle(cellStyle_border_for_cell);
				owninown_c.setCellStyle(cellStyle_border_for_cell);
				corrected_c.setCellStyle(cellStyle_border_for_cell);
				commissionrate_c.setCellStyle(cellStyle_border_for_cell);
				status_c.setCellStyle(cellStyle_border_for_cell);
				stringfromfile_c.setCellStyle(cellStyle_border_for_cell);
				rewardamount_c.setCellStyle(cellStyle_border_for_cell);
				ownerincomeamount_c.setCellStyle(cellStyle_border_for_cell);
				commissionamount_c.setCellStyle(cellStyle_border_for_cell);
				nkamount_c.setCellStyle(cellStyle_border_for_cell);
				maxcommissionamount_c.setCellStyle(cellStyle_border_for_cell);
				mincommissionamount_c.setCellStyle(cellStyle_border_for_cell);
				cashamount_c.setCellStyle(cellStyle_border_for_cell);
				sumnalprimal_c.setCellStyle(cellStyle_border_for_cell);
				amounttocheck_c.setCellStyle(cellStyle_border_for_cell);
				amountofpayment_c.setCellStyle(cellStyle_border_for_cell);
				sumofsplitting_c.setCellStyle(cellStyle_border_for_cell);
				amountintermediary_c.setCellStyle(cellStyle_border_for_cell);
				amountofscs_c.setCellStyle(cellStyle_border_for_cell);
				amountwithchecks_c.setCellStyle(cellStyle_border_for_cell);
				counter_c.setCellStyle(cellStyle_border_for_cell);
				terminal_c.setCellStyle(cellStyle_border_for_cell);
				terminalnetwork_c.setCellStyle(cellStyle_border_for_cell);
				transactiontype_c.setCellStyle(cellStyle_border_for_cell);
				service_c.setCellStyle(cellStyle_border_for_cell);
				filetransactions_c.setCellStyle(cellStyle_border_for_cell);
				fio_c.setCellStyle(cellStyle_border_for_cell);
				checksincoming_c.setCellStyle(cellStyle_border_for_cell);
				barcode_c.setCellStyle(cellStyle_border_for_cell);
				isaresident_c.setCellStyle(cellStyle_border_for_cell);
				valuenotfound_c.setCellStyle(cellStyle_border_for_cell);
				providertariff_c.setCellStyle(cellStyle_border_for_cell);
				counterchecks_c.setCellStyle(cellStyle_border_for_cell);
				countercheck_c.setCellStyle(cellStyle_border_for_cell);
				id__c.setCellStyle(cellStyle_border_for_cell);
				detailing_c.setCellStyle(cellStyle_border_for_cell);
				walletpayer_c.setCellStyle(cellStyle_border_for_cell);
				walletreceiver_c.setCellStyle(cellStyle_border_for_cell);
				purposeofpayment_c.setCellStyle(cellStyle_border_for_cell);
				dataprovider_c.setCellStyle(cellStyle_border_for_cell);
				attributes__c.setCellStyle(cellStyle_border_for_cell);
				statusabs_c.setCellStyle(cellStyle_border_for_cell);
				sess_id_c.setCellStyle(cellStyle_border_for_cell);
				/*
				 * Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" +
				 * Connect.userID_ + "/" + Connect.userPassword_ + "@" + Connect.connectionURL_
				 * + "");
				 */
				Connection conn = DBUtil.conn;
				// id_sess.getText(), dt1.getValue(),
				// dt2.getValue()

				// ------------------------
				String ldt1 = null;
				String ldt2 = null;

				if (dt1.getValue() != null)
					ldt1 = dt1.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
				if (dt2.getValue() != null)
					ldt2 = dt2.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

				String sess = "\n";
				String ldt1_ = "\n";
				String ldt2_ = "\n";
				String bt = "\n";
				String rownum = "\n";
				if (dt1.getValue() != null & dt2.getValue() != null) {
					bt = " and trunc(paydate) between to_date('" + ldt1 + "','dd.mm.yyyy') and to_date('" + ldt2
							+ "','dd.mm.yyyy') \n";
				} else if (dt1.getValue() != null & dt2.getValue() == null) {
					ldt1_ = " and trunc(paydate) >= to_date('" + ldt1 + "','dd.mm.yyyy')\n";
				} else if (dt1.getValue() == null & dt2.getValue() != null) {
					ldt2_ = " and trunc(paydate) <= to_date('" + ldt2 + "','dd.mm.yyyy')\n";
				}

				if (id_sess.getText().equals("")) {

				} else {
					sess = " and sess_id = " + id_sess.getText() + "\n";
				}
				String selectStmt = " select rownum,t.* from (select rownum,t.* from Z_SB_TRANSACT_AMRA_DBT t where 1=1"
						+ sess + ldt1_ + ldt2_ + bt + " order by PAYDATE desc) t";
				// ----------------------------------------------------------------------------

				Statement sqlStatement = conn.createStatement();
				// String readRecordSQL = "SELECT * FROM Z_SB_TRANSACT_AMRA_DBT WHERE sess_id =
				// " + Connect.SESS_ID_ + "";
				ResultSet myResultSet = sqlStatement.executeQuery(selectStmt);

				int i = 1;

				while (myResultSet.next()) {

					Row row_d_ = sheet.createRow(i);

					CellStyle cellStyle_border_d = book.createCellStyle();
					cellStyle_border_d.setBorderTop(BorderStyle.THIN);
					cellStyle_border_d.setBorderBottom(BorderStyle.THIN);
					cellStyle_border_d.setBorderLeft(BorderStyle.THIN);
					cellStyle_border_d.setBorderRight(BorderStyle.THIN);
					CreationHelper createHelper = book.getCreationHelper();
					cellStyle_border_d.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy HH:mm:ss"));

					CellStyle cellStyle_border = book.createCellStyle();
					cellStyle_border.setBorderTop(BorderStyle.THIN);
					cellStyle_border.setBorderBottom(BorderStyle.THIN);
					cellStyle_border.setBorderLeft(BorderStyle.THIN);
					cellStyle_border.setBorderRight(BorderStyle.THIN);

					Cell RECDATE_c = row_d_.createCell(0);
					RECDATE_c.setCellStyle(cellStyle_border_d);
					RECDATE_c.setCellValue(myResultSet.getTimestamp("RECDATE"));
					sheet.autoSizeColumn(0);
					Cell PAYDATE_c = row_d_.createCell(1);
					PAYDATE_c.setCellStyle(cellStyle_border_d);
					PAYDATE_c.setCellValue(myResultSet.getTimestamp("PAYDATE"));
					sheet.autoSizeColumn(1);
					Cell CURRENCY_c = row_d_.createCell(2);
					CURRENCY_c.setCellValue(myResultSet.getString("CURRENCY"));
					sheet.autoSizeColumn(2);
					Cell PAYMENTTYPE_c = row_d_.createCell(3);
					PAYMENTTYPE_c.setCellValue(myResultSet.getString("PAYMENTTYPE"));
					sheet.autoSizeColumn(3);
					Cell VK_c = row_d_.createCell(4);
					VK_c.setCellValue(myResultSet.getString("VK"));
					sheet.autoSizeColumn(4);
					Cell DATEOFOPERATION_c = row_d_.createCell(5);
					DATEOFOPERATION_c.setCellValue(myResultSet.getString("DATEOFOPERATION"));
					sheet.autoSizeColumn(5);
					Cell DATAPS_c = row_d_.createCell(6);
					DATAPS_c.setCellValue(myResultSet.getString("DATAPS"));
					sheet.autoSizeColumn(6);
					Cell DATECLEARING_c = row_d_.createCell(7);
					DATECLEARING_c.setCellValue(myResultSet.getString("DATECLEARING"));
					sheet.autoSizeColumn(7);
					Cell DEALER_c = row_d_.createCell(8);
					DEALER_c.setCellValue(myResultSet.getString("DEALER"));
					sheet.autoSizeColumn(8);
					Cell ACCOUNTPAYER_c = row_d_.createCell(9);
					ACCOUNTPAYER_c.setCellValue(myResultSet.getString("ACCOUNTPAYER"));
					sheet.autoSizeColumn(9);
					Cell CARDNUMBER_c = row_d_.createCell(10);
					CARDNUMBER_c.setCellValue(myResultSet.getString("CARDNUMBER"));
					sheet.autoSizeColumn(10);
					Cell OPERATIONNUMBER_c = row_d_.createCell(11);
					OPERATIONNUMBER_c.setCellValue(myResultSet.getString("OPERATIONNUMBER"));
					sheet.autoSizeColumn(11);
					Cell OPERATIONNUMBERDELIVERY_c = row_d_.createCell(12);
					OPERATIONNUMBERDELIVERY_c.setCellValue(myResultSet.getString("OPERATIONNUMBERDELIVERY"));
					sheet.autoSizeColumn(12);
					Cell CHECKNUMBER_c = row_d_.createCell(13);
					CHECKNUMBER_c.setCellValue(myResultSet.getString("CHECKNUMBER"));
					sheet.autoSizeColumn(13);
					Cell CHECKPARENT_c = row_d_.createCell(14);
					CHECKPARENT_c.setCellValue(myResultSet.getString("CHECKPARENT"));
					sheet.autoSizeColumn(14);
					Cell ORDEROFPROVIDENCE_c = row_d_.createCell(15);
					ORDEROFPROVIDENCE_c.setCellValue(myResultSet.getString("ORDEROFPROVIDENCE"));
					sheet.autoSizeColumn(15);
					Cell PROVIDER_c = row_d_.createCell(16);
					PROVIDER_c.setCellValue(myResultSet.getString("PROVIDER"));
					sheet.autoSizeColumn(16);
					Cell OWNINOWN_c = row_d_.createCell(17);
					OWNINOWN_c.setCellValue(myResultSet.getString("OWNINOWN"));
					sheet.autoSizeColumn(17);
					Cell CORRECTED_c = row_d_.createCell(18);
					CORRECTED_c.setCellValue(myResultSet.getString("CORRECTED"));
					sheet.autoSizeColumn(18);
					Cell COMMISSIONRATE_c = row_d_.createCell(19);
					COMMISSIONRATE_c.setCellValue(myResultSet.getString("COMMISSIONRATE"));
					sheet.autoSizeColumn(19);
					Cell STATUS_c = row_d_.createCell(20);
					STATUS_c.setCellValue(myResultSet.getString("STATUS"));
					sheet.autoSizeColumn(20);
					Cell STRINGFROMFILE_c = row_d_.createCell(21);
					STRINGFROMFILE_c.setCellValue(myResultSet.getString("STRINGFROMFILE"));
					sheet.autoSizeColumn(21);
					Cell REWARDAMOUNT_c = row_d_.createCell(22);
					REWARDAMOUNT_c.setCellValue(myResultSet.getString("REWARDAMOUNT"));
					sheet.autoSizeColumn(22);
					Cell OWNERINCOMEAMOUNT_c = row_d_.createCell(23);
					OWNERINCOMEAMOUNT_c.setCellValue(myResultSet.getString("OWNERINCOMEAMOUNT"));
					sheet.autoSizeColumn(23);
					Cell COMMISSIONAMOUNT_c = row_d_.createCell(24);
					COMMISSIONAMOUNT_c.setCellValue(myResultSet.getString("COMMISSIONAMOUNT"));
					sheet.autoSizeColumn(24);
					Cell NKAMOUNT_c = row_d_.createCell(25);
					NKAMOUNT_c.setCellValue(myResultSet.getString("NKAMOUNT"));
					sheet.autoSizeColumn(25);
					Cell MAXCOMMISSIONAMOUNT_c = row_d_.createCell(26);
					MAXCOMMISSIONAMOUNT_c.setCellValue(myResultSet.getString("MAXCOMMISSIONAMOUNT"));
					sheet.autoSizeColumn(26);
					Cell MINCOMMISSIONAMOUNT_c = row_d_.createCell(27);
					MINCOMMISSIONAMOUNT_c.setCellValue(myResultSet.getString("MINCOMMISSIONAMOUNT"));
					sheet.autoSizeColumn(27);
					Cell CASHAMOUNT_c = row_d_.createCell(28);
					CASHAMOUNT_c.setCellValue(myResultSet.getString("CASHAMOUNT"));
					sheet.autoSizeColumn(28);
					Cell SUMNALPRIMAL_c = row_d_.createCell(29);
					SUMNALPRIMAL_c.setCellValue(myResultSet.getString("SUMNALPRIMAL"));
					sheet.autoSizeColumn(29);
					Cell AMOUNTTOCHECK_c = row_d_.createCell(30);
					AMOUNTTOCHECK_c.setCellValue(myResultSet.getString("AMOUNTTOCHECK"));
					sheet.autoSizeColumn(30);
					Cell AMOUNTOFPAYMENT_c = row_d_.createCell(31);
					AMOUNTOFPAYMENT_c.setCellValue(myResultSet.getString("AMOUNTOFPAYMENT"));
					sheet.autoSizeColumn(31);
					Cell SUMOFSPLITTING_c = row_d_.createCell(32);
					SUMOFSPLITTING_c.setCellValue(myResultSet.getString("SUMOFSPLITTING"));
					sheet.autoSizeColumn(32);
					Cell AMOUNTINTERMEDIARY_c = row_d_.createCell(33);
					AMOUNTINTERMEDIARY_c.setCellValue(myResultSet.getString("AMOUNTINTERMEDIARY"));
					sheet.autoSizeColumn(33);
					Cell AMOUNTOFSCS_c = row_d_.createCell(34);
					AMOUNTOFSCS_c.setCellValue(myResultSet.getString("AMOUNTOFSCS"));
					sheet.autoSizeColumn(34);
					Cell AMOUNTWITHCHECKS_c = row_d_.createCell(35);
					AMOUNTWITHCHECKS_c.setCellValue(myResultSet.getString("AMOUNTWITHCHECKS"));
					sheet.autoSizeColumn(35);
					Cell COUNTER_c = row_d_.createCell(36);
					COUNTER_c.setCellValue(myResultSet.getString("COUNTER"));
					sheet.autoSizeColumn(36);
					Cell TERMINAL_c = row_d_.createCell(37);
					TERMINAL_c.setCellValue(myResultSet.getString("TERMINAL"));
					sheet.autoSizeColumn(37);
					Cell TERMINALNETWORK_c = row_d_.createCell(38);
					TERMINALNETWORK_c.setCellValue(myResultSet.getString("TERMINALNETWORK"));
					sheet.autoSizeColumn(38);
					Cell TRANSACTIONTYPE_c = row_d_.createCell(39);
					TRANSACTIONTYPE_c.setCellValue(myResultSet.getString("TRANSACTIONTYPE"));
					sheet.autoSizeColumn(39);
					Cell SERVICE_c = row_d_.createCell(40);
					SERVICE_c.setCellValue(myResultSet.getString("SERVICE"));
					sheet.autoSizeColumn(40);
					Cell FILETRANSACTIONS_c = row_d_.createCell(41);
					FILETRANSACTIONS_c.setCellValue(myResultSet.getString("FILETRANSACTIONS"));
					sheet.autoSizeColumn(41);
					Cell FIO_c = row_d_.createCell(42);
					FIO_c.setCellValue(myResultSet.getString("FIO"));
					sheet.autoSizeColumn(42);
					Cell CHECKSINCOMING_c = row_d_.createCell(43);
					CHECKSINCOMING_c.setCellValue(myResultSet.getString("CHECKSINCOMING"));
					sheet.autoSizeColumn(43);
					Cell BARCODE_c = row_d_.createCell(44);
					BARCODE_c.setCellValue(myResultSet.getString("BARCODE"));
					sheet.autoSizeColumn(44);
					Cell ISARESIDENT_c = row_d_.createCell(45);
					ISARESIDENT_c.setCellValue(myResultSet.getString("ISARESIDENT"));
					sheet.autoSizeColumn(45);
					Cell VALUENOTFOUND_c = row_d_.createCell(46);
					VALUENOTFOUND_c.setCellValue(myResultSet.getString("VALUENOTFOUND"));
					sheet.autoSizeColumn(46);
					Cell PROVIDERTARIFF_c = row_d_.createCell(47);
					PROVIDERTARIFF_c.setCellValue(myResultSet.getString("PROVIDERTARIFF"));
					sheet.autoSizeColumn(47);
					Cell COUNTERCHECKS_c = row_d_.createCell(48);
					COUNTERCHECKS_c.setCellValue(myResultSet.getString("COUNTERCHECKS"));
					sheet.autoSizeColumn(48);
					Cell COUNTERCHECK_c = row_d_.createCell(49);
					COUNTERCHECK_c.setCellValue(myResultSet.getString("COUNTERCHECK"));
					sheet.autoSizeColumn(49);
					Cell ID__c = row_d_.createCell(50);
					ID__c.setCellValue(myResultSet.getString("ID_"));
					sheet.autoSizeColumn(50);
					Cell DETAILING_c = row_d_.createCell(51);
					DETAILING_c.setCellValue(myResultSet.getString("DETAILING"));
					sheet.autoSizeColumn(51);
					Cell WALLETPAYER_c = row_d_.createCell(52);
					WALLETPAYER_c.setCellValue(myResultSet.getString("WALLETPAYER"));
					sheet.autoSizeColumn(52);
					Cell WALLETRECEIVER_c = row_d_.createCell(53);
					WALLETRECEIVER_c.setCellValue(myResultSet.getString("WALLETRECEIVER"));
					sheet.autoSizeColumn(53);
					Cell PURPOSEOFPAYMENT_c = row_d_.createCell(54);
					PURPOSEOFPAYMENT_c.setCellValue(myResultSet.getString("PURPOSEOFPAYMENT"));
					sheet.autoSizeColumn(54);
					Cell DATAPROVIDER_c = row_d_.createCell(55);
					DATAPROVIDER_c.setCellValue(myResultSet.getString("DATAPROVIDER"));
					sheet.autoSizeColumn(55);
					Cell ATTRIBUTES__c = row_d_.createCell(56);
					ATTRIBUTES__c.setCellValue(myResultSet.getString("ATTRIBUTES_"));
					sheet.autoSizeColumn(56);
					Cell STATUSABS_c = row_d_.createCell(57);
					STATUSABS_c.setCellValue(myResultSet.getString("STATUSABS"));
					sheet.autoSizeColumn(57);
					Cell SESS_ID_c = row_d_.createCell(58);
					SESS_ID_c.setCellValue(myResultSet.getString("SESS_ID"));
					sheet.autoSizeColumn(58);

					CURRENCY_c.setCellStyle(cellStyle_border);
					PAYMENTTYPE_c.setCellStyle(cellStyle_border);
					VK_c.setCellStyle(cellStyle_border);
					DATEOFOPERATION_c.setCellStyle(cellStyle_border);
					DATAPS_c.setCellStyle(cellStyle_border);
					DATECLEARING_c.setCellStyle(cellStyle_border);
					DEALER_c.setCellStyle(cellStyle_border);
					ACCOUNTPAYER_c.setCellStyle(cellStyle_border);
					CARDNUMBER_c.setCellStyle(cellStyle_border);
					OPERATIONNUMBER_c.setCellStyle(cellStyle_border);
					OPERATIONNUMBERDELIVERY_c.setCellStyle(cellStyle_border);
					CHECKNUMBER_c.setCellStyle(cellStyle_border);
					CHECKPARENT_c.setCellStyle(cellStyle_border);
					ORDEROFPROVIDENCE_c.setCellStyle(cellStyle_border);
					PROVIDER_c.setCellStyle(cellStyle_border);
					OWNINOWN_c.setCellStyle(cellStyle_border);
					CORRECTED_c.setCellStyle(cellStyle_border);
					COMMISSIONRATE_c.setCellStyle(cellStyle_border);
					STATUS_c.setCellStyle(cellStyle_border);
					STRINGFROMFILE_c.setCellStyle(cellStyle_border);
					REWARDAMOUNT_c.setCellStyle(cellStyle_border);
					OWNERINCOMEAMOUNT_c.setCellStyle(cellStyle_border);
					COMMISSIONAMOUNT_c.setCellStyle(cellStyle_border);
					NKAMOUNT_c.setCellStyle(cellStyle_border);
					MAXCOMMISSIONAMOUNT_c.setCellStyle(cellStyle_border);
					MINCOMMISSIONAMOUNT_c.setCellStyle(cellStyle_border);
					CASHAMOUNT_c.setCellStyle(cellStyle_border);
					SUMNALPRIMAL_c.setCellStyle(cellStyle_border);
					AMOUNTTOCHECK_c.setCellStyle(cellStyle_border);
					AMOUNTOFPAYMENT_c.setCellStyle(cellStyle_border);
					SUMOFSPLITTING_c.setCellStyle(cellStyle_border);
					AMOUNTINTERMEDIARY_c.setCellStyle(cellStyle_border);
					AMOUNTOFSCS_c.setCellStyle(cellStyle_border);
					AMOUNTWITHCHECKS_c.setCellStyle(cellStyle_border);
					COUNTER_c.setCellStyle(cellStyle_border);
					TERMINAL_c.setCellStyle(cellStyle_border);
					TERMINALNETWORK_c.setCellStyle(cellStyle_border);
					TRANSACTIONTYPE_c.setCellStyle(cellStyle_border);
					SERVICE_c.setCellStyle(cellStyle_border);
					FILETRANSACTIONS_c.setCellStyle(cellStyle_border);
					FIO_c.setCellStyle(cellStyle_border);
					CHECKSINCOMING_c.setCellStyle(cellStyle_border);
					BARCODE_c.setCellStyle(cellStyle_border);
					ISARESIDENT_c.setCellStyle(cellStyle_border);
					VALUENOTFOUND_c.setCellStyle(cellStyle_border);
					PROVIDERTARIFF_c.setCellStyle(cellStyle_border);
					COUNTERCHECKS_c.setCellStyle(cellStyle_border);
					COUNTERCHECK_c.setCellStyle(cellStyle_border);
					ID__c.setCellStyle(cellStyle_border);
					DETAILING_c.setCellStyle(cellStyle_border);
					WALLETPAYER_c.setCellStyle(cellStyle_border);
					WALLETRECEIVER_c.setCellStyle(cellStyle_border);
					PURPOSEOFPAYMENT_c.setCellStyle(cellStyle_border);
					DATAPROVIDER_c.setCellStyle(cellStyle_border);
					ATTRIBUTES__c.setCellStyle(cellStyle_border);
					STATUSABS_c.setCellStyle(cellStyle_border);
					SESS_ID_c.setCellStyle(cellStyle_border);
					i++;
				}
				sheet.setAutoFilter(CellRangeAddress.valueOf("A1:BG" + i + ""));
				for (int j = 0; j <= 58; j++) {
					sheet.autoSizeColumn(j);
				}
				myResultSet.close();
				// conn.close();
				book.write(new FileOutputStream(file.getPath()));
				book.close();
			}
		} catch (SQLException | IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	public void excel_exportsbe(File file, String mess) {
		Alerts(mess);
		pb.setVisible(false);
		xlsx.setDisable(false);
	}

	public void excel_exportsb(File file) {
		Alerts("���� ����������� � ����� " + file.getPath());
		pb.setVisible(false);
		xlsx.setDisable(false);
	}

	public int excel_exports(File file) throws FileNotFoundException, IOException, ParseException {
		if (file != null) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet spreadsheet = workbook.createSheet("�������");
			Row row = spreadsheet.createRow(0);

			XSSFCellStyle cellStyle_border_d = workbook.createCellStyle();
			cellStyle_border_d.setBorderTop(BorderStyle.THIN);
			cellStyle_border_d.setBorderBottom(BorderStyle.THIN);
			cellStyle_border_d.setBorderLeft(BorderStyle.THIN);
			cellStyle_border_d.setBorderRight(BorderStyle.THIN);
			CreationHelper createHelper = workbook.getCreationHelper();
			cellStyle_border_d.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy HH:mm:ss"));

			XSSFCellStyle style = spreadsheet.getWorkbook().createCellStyle();
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle style_sb = spreadsheet.getWorkbook().createCellStyle();
			style_sb.setBorderTop(BorderStyle.THIN);
			style_sb.setBorderLeft(BorderStyle.THIN);
			style_sb.setBorderRight(BorderStyle.THIN);
			style_sb.setBorderBottom(BorderStyle.THIN);
			style_sb.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			style_sb.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle cellStyle_border = workbook.createCellStyle();
			cellStyle_border.setBorderTop(BorderStyle.THIN);
			cellStyle_border.setBorderBottom(BorderStyle.THIN);
			cellStyle_border.setBorderLeft(BorderStyle.THIN);
			cellStyle_border.setBorderRight(BorderStyle.THIN);

			XSSFFont defaultFont = workbook.createFont();
			defaultFont.setFontHeightInPoints((short) 12);
			defaultFont.setFontName("Arial");
			defaultFont.setColor(IndexedColors.BLACK.getIndex());
			defaultFont.setBold(true);
			defaultFont.setItalic(false);

			CellStyle cellStyle_border_for_cell = workbook.createCellStyle();
			cellStyle_border_for_cell.setBorderTop(BorderStyle.THIN);
			cellStyle_border_for_cell.setBorderBottom(BorderStyle.THIN);
			cellStyle_border_for_cell.setBorderLeft(BorderStyle.THIN);
			cellStyle_border_for_cell.setBorderRight(BorderStyle.THIN);
			cellStyle_border_for_cell.setFont(defaultFont);

			for (int j = 0; j < trans_table.getColumns().size(); j++) {
				row.createCell(j).setCellValue(trans_table.getColumns().get(j).getText());
				row.getCell(j).setCellStyle(cellStyle_border_for_cell);
			}

			for (int i = 0; i < trans_table.getItems().size(); i++) {
				row = spreadsheet.createRow(i + 1);
				for (int j = 0; j < trans_table.getColumns().size(); j++) {
					if (trans_table.getColumns().get(j).getCellData(i) != null) {
						if (j == 2) {
							LocalDateTime now = (LocalDateTime) trans_table.getColumns().get(j).getCellData(i);
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.mm.yyyy HH:mm:ss");
							String formatDateTime = now.format(formatter);

							DateFormat df = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss");
							Date parsedDate = df.parse(formatDateTime);

							row.createCell(j).setCellValue(parsedDate);
							row.getCell(j).setCellStyle(cellStyle_border_d);
						} else if (j == 24) {
							LocalDateTime now = (LocalDateTime) trans_table.getColumns().get(j).getCellData(i);
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.mm.yyyy HH:mm:ss");
							String formatDateTime = now.format(formatter);

							DateFormat df = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss");
							Date parsedDate = df.parse(formatDateTime);

							row.createCell(j).setCellValue(parsedDate);
							row.getCell(j).setCellStyle(cellStyle_border_d);
						} else if (j >= 8 & j <= 14) {
							row.createCell(j).setCellValue(
									Double.valueOf(trans_table.getColumns().get(j).getCellData(i).toString()));
							row.getCell(j).setCellStyle(cellStyle_border);
						} else if (j == 1) {
							row.createCell(j).setCellValue(trans_table.getColumns().get(j).getCellData(i).toString());
							if (trans_table.getColumns().get(j).getCellData(i).toString().length() == 20) {
								row.getCell(j).setCellStyle(style);
							} else {
								row.getCell(j).setCellStyle(cellStyle_border);
							}
						} else if (j == 3) {
							row.createCell(j).setCellValue(trans_table.getColumns().get(j).getCellData(i).toString());
							if (trans_table.getColumns().get(j).getCellData(i).toString().equals("00")) {
								row.getCell(j).setCellStyle(style);
							} else {
								row.getCell(j).setCellStyle(cellStyle_border);
							}
						} else if (j == 6) {
							row.createCell(j).setCellValue(trans_table.getColumns().get(j).getCellData(i).toString());
							if (trans_table.getColumns().get(j).getCellData(i).toString().equals("��������")) {
								row.getCell(j).setCellStyle(style_sb);
							} else {
								row.getCell(j).setCellStyle(cellStyle_border);
							}

						} else {
							row.createCell(j).setCellValue(trans_table.getColumns().get(j).getCellData(i).toString());
							row.getCell(j).setCellStyle(cellStyle_border);
						}
					} else {
						row.createCell(j).setCellValue("");
						row.getCell(j).setCellStyle(cellStyle_border);
					}
					spreadsheet.autoSizeColumn(i);
				}
			}
			spreadsheet.setAutoFilter(CellRangeAddress.valueOf("A1:AA" + trans_table.getItems().size()));
			workbook.write(new FileOutputStream(file.getPath()));
			workbook.close();
		}
		return 0;
	}

	@FXML
	public void excel_export(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		System.setProperty("javax.xml.transform.TransformerFactory",
				"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel File", "*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialFileName("���������� " + dt1.getValue() + "-" + dt2.getValue());
		// Show save file dialog
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					pb.setVisible(true);
					xlsx.setDisable(true);
				}
			});

			Task<Integer> task = new Task<Integer>() {
				@Override
				public Integer call() throws Exception {
					return excel_exports(file);
				}
			};

			task.setOnFailed(e -> excel_exportsbe(file, task.getException().getMessage()));
			task.setOnSucceeded(e -> excel_exportsb(file));
			exec.execute(task);
		}
	}

	@FXML
	private void view_post(ActionEvent actionEvent) {
		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(("�������� ������� ������ �� �������!\n"));
			alert.showAndWait();

		} else {

			pb.setVisible(true);
			Task<Object> task = new Task<Object>() {
				@Override
				public Object call() throws Exception {
					Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
					new PrintReport2().showReport(fn.get_checknumber(), String.valueOf(fn.get_sess_id()), "=");
					return null;
				}
			};
			task.setOnFailed(e -> Alert(task.getException().getMessage()));
			task.setOnSucceeded(e -> pb.setVisible(false));

			exec.execute(task);

			/*
			 * Stage stage = (Stage) trans_table.getScene().getWindow(); Label alert = new
			 * Label("������ ��������� ����?"); alert.setLayoutX(75.0);
			 * alert.setLayoutY(11.0); alert.setPrefHeight(17.0);
			 * 
			 * Button no = new Button(); no.setText("���"); no.setLayoutX(111.0);
			 * no.setLayoutY(56.0); no.setPrefWidth(72.0); no.setPrefHeight(21.0);
			 * 
			 * Button yes = new Button(); yes.setText("��"); yes.setLayoutX(14.0);
			 * yes.setLayoutY(56.0); yes.setPrefWidth(72.0); yes.setPrefHeight(21.0);
			 * 
			 * AnchorPane yn = new AnchorPane(); yn.getChildren().add(alert);
			 * yn.getChildren().add(no); yn.getChildren().add(yes); Scene ynScene = new
			 * Scene(yn, 250, 100); Stage newWindow_yn = new Stage();
			 * 
			 * no.setOnAction(new EventHandler<ActionEvent>() { public void
			 * handle(ActionEvent event) { Amra_Trans fn =
			 * trans_table.getSelectionModel().getSelectedItem(); new
			 * PrintReport2().showReport(fn.get_checknumber(), fn.get_sess_id(),">");
			 * newWindow_yn.close(); } }); yes.setOnAction(new EventHandler<ActionEvent>() {
			 * public void handle(ActionEvent event) { Amra_Trans fn =
			 * trans_table.getSelectionModel().getSelectedItem(); new
			 * PrintReport2().showReport(fn.get_checknumber(), fn.get_sess_id(),"=");
			 * newWindow_yn.close(); } });
			 * 
			 * newWindow_yn.setTitle("��������"); newWindow_yn.setScene(ynScene); //
			 * Specifies the modality for new window.
			 * newWindow_yn.initModality(Modality.WINDOW_MODAL); // Specifies the owner
			 * Window (parent) for new window newWindow_yn.initOwner(stage);
			 * newWindow_yn.getIcons().add(new Image("icon.png")); newWindow_yn.show();
			 */
		}
	}

	private void Alerts(String mess) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("��������");
		alert.setHeaderText(null);
		alert.setContentText(mess);
		alert.showAndWait();
	}

	@FXML
	private void print_(ActionEvent actionEvent) {
		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			Alerts("�������� ������� ������ �� �������!");
		} else {

			Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
			/*
			 * new PrintCheck().showReport(fn.get_checknumber(), fn.get_sess_id());
			 */
			pb.setVisible(true);
			Task<Object> task = new Task<Object>() {
				@Override
				public Object call() throws Exception {
					try {
						ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
								"start javaw -splash:" + System.getenv("TRANSACT_PATH") + "SPLASH/splash.gif -jar "
										+ System.getenv("TRANSACT_PATH") + "/AP.jar 666 2 " + fn.get_checknumber()
										+ " no " + Connect.userID_ + " " + Connect.userPassword_ + " "
										+ Connect.connectionURL_.substring(Connect.connectionURL_.indexOf("/") + 1,
												Connect.connectionURL_.length())
										+ " J:\\dev6i\\NET80\\ADMIN");

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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Alerts(e.getMessage());
					}
					return null;
				}
			};
			task.setOnFailed(e -> Alert(task.getException().getMessage()));
			task.setOnSucceeded(e -> pb.setVisible(false));

			exec.execute(task);

			System.out.println("start javaw -splash:" + System.getenv("TRANSACT_PATH") + "SPLASH/splash.gif -jar "
					+ System.getenv("TRANSACT_PATH") + "/AP.jar 666 2 " + fn.get_checknumber() + " no "
					+ Connect.userID_ + " " + Connect.userPassword_ + " " + Connect.connectionURL_
							.substring(Connect.connectionURL_.indexOf("/") + 1, Connect.connectionURL_.length())
					+ " J:\\dev6i\\NET80\\ADMIN");
			/*
			 * Runnable task = () -> { try { ProcessBuilder builder = new
			 * ProcessBuilder("cmd.exe", "/c", "java -jar " + System.getenv("TRANSACT_PATH")
			 * + "/AP.jar 666 2 " + fn.get_checknumber() + " no " + Connect.userID_ + " " +
			 * Connect.userPassword_ + " " + Connect.connectionURL_
			 * .substring(Connect.connectionURL_.indexOf("/") + 1,
			 * Connect.connectionURL_.length()) + " J:\\dev6i\\NET80\\ADMIN");
			 * builder.redirectErrorStream(true); Process p; p = builder.start();
			 * BufferedReader r = new BufferedReader(new
			 * InputStreamReader(p.getInputStream())); String line; while (true) { line =
			 * r.readLine(); if (line == null) { break; } System.out.println(line); } }
			 * catch (IOException e) { // TODO Auto-generated catch block
			 * Alerts(e.getMessage()); } }; Thread thread = new Thread(task);
			 * thread.start();
			 */
		}
	}

	@FXML
	private void term_view_(ActionEvent actionEvent) {
		ObservableList<Amra_Trans> empData = TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(),
				"", false, false, terminal_name.getValue().toString());
		populate_fn_sess(empData);

		autoResizeColumns(trans_table);
		// GUIUtils.autoFitTable(trans_table);
	}

	@FXML
	private void check_table(ActionEvent actionEvent) {
		if (ret_pay.isSelected()) {
			ret_pay.setSelected(false);
		}
	}

	@FXML
	private void check_table_ret(ActionEvent actionEvent) {
		if (inkass.isSelected()) {
			inkass.setSelected(false);
		}
	}

	@FXML
	private void view_attr(ActionEvent actionEvent) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("�������� ������� ������ �� �������!");
				alert.showAndWait();
			} else {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

				Connect.PNMB_ = fn.get_checknumber();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("view/Attributes_.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������� ���������� " + fn.get_checknumber());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	/* �������� ����� */
	@FXML
	private void showdeal(ActionEvent actionEvent) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("�������� ������� ������ �� �������!");
				alert.showAndWait();
			} else {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

				Connect.PNMB_ = fn.get_checknumber();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("view/Deals.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("���� ������ ���������� " + fn.get_checknumber());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// ����� ��������
	private void exec_filter(ObservableList<Amra_Trans> trData) throws Exception {
		Runnable task = () -> {
			trans_table.setItems(trData);
			autoResizeColumns(trans_table);

			Runnable task_ = () -> {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						TableFilter<Amra_Trans> tableFilter = TableFilter.forTableView(trans_table).apply();
						tableFilter.setSearchStrategy((input, target) -> {
							try {
								return target.toLowerCase().contains(input.toLowerCase());
							} catch (Exception e) {
								return false;
							}
						});
					}
				});

				provider.setCellFactory(col -> new TextFieldTableCell<Amra_Trans, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
							setStyle("");
						} else {
							setText(item.toString());
							if (item.equals("��������")) {
								setStyle("-fx-background-color: rgb(162, 189, 48);" + "-fx-border-color:black;"
										+ " -fx-border-width :  1 1 1 1 ");
							} else {
								setStyle("");
							}
						}
					}
				});
				terminal.setCellFactory(col -> new TextFieldTableCell<Amra_Trans, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
							setStyle("");
						} else {
							setText(item.toString());
							if (item.contains("SB")) {
								setStyle("");
								/*
								 * setStyle("-fx-background-color: rgb(210, 236, 126);" +
								 * "-fx-border-color:black;" + " -fx-border-width :  1 1 1 1 ");
								 */
							} else {
								setStyle("-fx-background-color: rgb(169, 53, 107);" + "-fx-border-color:black;"
										+ " -fx-border-width :  1 1 1 1 ");
							}
						}
					}
				});

				dealer.setCellFactory(col -> new TextFieldTableCell<Amra_Trans, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
							setStyle("");
						} else {
							setText(item.toString());
							if (item.contains("��������")) {
								setStyle("-fx-background-color: rgb(210, 236, 126);" + "-fx-border-color:black;"
										+ " -fx-border-width :  1 1 1 1 ");
							} else {
								setStyle("-fx-background-color: rgb(169, 53, 107);" + "-fx-border-color:black;"
										+ " -fx-border-width :  1 1 1 1 ");
							}
						}
					}
				});

				checkparent.setCellFactory(col -> new TextFieldTableCell<Amra_Trans, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
							setStyle("");
						} else {
							setText(item.toString());
							if (item.toString().length() == 20) {
								setStyle("-fx-background-color: #F9E02C;" + "-fx-border-color:black;"
										+ " -fx-border-width :  1 1 1 1 ");
							} else {
								setStyle("");
							}
						}
					}
				});

				status.setCellFactory(list -> {
					TextFieldTableCell<Amra_Trans, String> cell = new TextFieldTableCell<Amra_Trans, String>() {
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (empty || item == null) {
								setText(null);
								setGraphic(null);
								setStyle("");
							} else {
								setText(item.toString());
								if (item.equals("00")) {
									setStyle("");
								} else {
									setStyle("-fx-background-color: #F9E02C;" + "-fx-border-color:black;"
											+ " -fx-border-width : 1 1 1 1;");
								}
							}
						}
					};

					/*
					 * cell.setOnMouseClicked(value -> { if (value.getClickCount() == 2) { //
					 * System.out.println("<Double mouse click>"); } });
					 */
					return cell;
				});

				/*
				 * trans_table.addEventHandler(KeyEvent.KEY_PRESSED, e -> { if (e.getCode() ==
				 * KeyCode.F7) { if (trans_table.getSelectionModel().getSelectedItem() != null)
				 * { System.out.println("<KEY_PRESSED>");
				 * status.setStyle("-fx-background-color: rgb(255, 255, 255);");
				 * terminal.setStyle("-fx-background-color: rgb(255, 255, 255);");
				 * provider.setStyle("-fx-background-color: rgb(255, 255, 255);"); } } });
				 */
			};

			Thread thread_ = new Thread(task_);
			thread_.start();

		};

		Thread thread = new Thread(task);
		thread.start();

		pb.setVisible(false);
		search.setDisable(false);
	}

	@FXML
	private void filter(ActionEvent actionEvent) {
		/*
		 * trans_table.setOnKeyReleased((KeyEvent t)-> { KeyCode key=t.getCode();
		 * ActionEvent l; if (key == KeyCode.F4){ view_unpivot2(actionEvent); } });
		 */
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					pb.setVisible(true);
					search.setDisable(true);
				}
			});

			Task<List<Amra_Trans>> task = new Task<List<Amra_Trans>>() {
				@Override
				public ObservableList<Amra_Trans> call() throws Exception {
					return TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(), FIO.getText(),
							(inkass.isSelected()) ? true : false, (ret_pay.isSelected()) ? true : false,
							terminal_name.getValue().toString());
				}
			};

			task.setOnFailed(e -> Alert(task.getException().getMessage()));
			task.setOnSucceeded(e -> {
				try {
					exec_filter((ObservableList<Amra_Trans>) task.getValue());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					Alert(e1.getMessage());
					;
				}
			});

			exec.execute(task);
			/*-----------------------------------------*/
		} catch (Exception e) {
			Alert(e.getMessage());
		}
	}

	public void Alert(String mes) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText(mes);
				alert.showAndWait();
			}
		});

	}

	public static void autoResizeColumns(TableView<?> table) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Set the right policy
				table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
				table.getColumns().stream().forEach((column) -> {
					if (column.getText().equals("sess_id")) {

					} /*
						 * else if (column.getText().equals("������������") |
						 * column.getText().equals("�������������") | column.getText().equals("�������")
						 * | column.getText().equals("�������������") |
						 * column.getText().equals("�����������") |
						 * column.getText().equals("�������������������") |
						 * column.getText().equals("����������")) { //
						 * System.out.println(column.getText()); }
						 */else {
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
		});

	}

	// ��������� �������
	private void populate_fn_sess(ObservableList<Amra_Trans> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}

	public int cnt = 0;

	public double all_sum = 0;

	public double all_sum_nal = 0;

	@FXML
	void chk_all(ActionEvent event_) {

		chk_row.setCellFactory(list -> {
			CheckBox checkBox = new CheckBox();
			TableCell<Amra_Trans, Boolean> tableCell = new TableCell<Amra_Trans, Boolean>() {
				@Override
				protected void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null)
						setGraphic(null);
					else {
						setGraphic(checkBox);
						checkBox.setSelected(true);
					}
				}
			};

			checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				try {
					validate(checkBox, (Amra_Trans) tableCell.getTableRow().getItem(), event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Alert(e.getMessage());
				}
			});

			checkBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.SPACE)
					try {
						validate(checkBox, (Amra_Trans) tableCell.getTableRow().getItem(), event);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Alert(e.getMessage());
					}
			});

			tableCell.setAlignment(Pos.CENTER);
			tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

			// list.setItem(true);
			return tableCell;
		});

		all_sum_nal = 0;
		all_sum = 0;
		cnt = 0;

		trans_table.getColumns().stream().forEach((column) -> {
			if (column.getText().equals("������������")) {
				for (int i = 0; i < trans_table.getItems().size(); i++) {
					if (column.getCellData(i) != null) {
						all_sum = all_sum + Double.parseDouble(column.getCellData(i).toString());
						cnt = cnt + 1;
					}
				}
			} else if (column.getText().equals("�������������")) {
				for (int i = 0; i < trans_table.getItems().size(); i++) {
					if (column.getCellData(i) != null) {
						all_sum_nal = all_sum_nal + Double.parseDouble(column.getCellData(i).toString());
					}
				}
			}
		});

		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String format = decimalFormat.format(all_sum);
		String format_nal = decimalFormat.format(all_sum_nal);
		cnt_all_.setText(String.valueOf(cnt));
		summa_plat.setText(String.valueOf(format));
		summa_nal.setText(format_nal);
		all_sum_nal = 0;
		all_sum = 0;
		cnt = 0;
	}

	@FXML
	void chk_one(ActionEvent event) {
		trans_table.getSelectionModel().getSelectedItem().set_chk_row(true);
	}

	void prop_un(Amra_Trans item, CheckBox checkBox) {
		all_sum_nal = all_sum_nal + item.cashamountProperty().getValue();
		all_sum = all_sum + item.amountofpaymentProperty().getValue();
		cnt = cnt + 1;

		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String format = decimalFormat.format(all_sum);
		String format_nal = decimalFormat.format(all_sum_nal);
		cnt_all_.setText(String.valueOf(cnt));
		summa_plat.setText(String.valueOf(format));
		summa_nal.setText(format_nal);
		checkBox.setSelected(true);
		System.out.println("Check=true");
		item.set_chk_row(true);
	}

	@FXML
	void unchk_all(ActionEvent event_) {

		chk_row.setCellFactory(list -> {
			CheckBox checkBox = new CheckBox();
			TableCell<Amra_Trans, Boolean> tableCell = new TableCell<Amra_Trans, Boolean>() {
				@Override
				protected void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null)
						setGraphic(null);
					else {
						setGraphic(checkBox);
						checkBox.setSelected(false);
					}
				}
			};

			checkBox.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				try {
					validate(checkBox, (Amra_Trans) tableCell.getTableRow().getItem(), event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Alert(e.getMessage());
				}
			});

			checkBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.SPACE)
					try {
						validate(checkBox, (Amra_Trans) tableCell.getTableRow().getItem(), event);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Alert(e.getMessage());
					}
			});

			tableCell.setAlignment(Pos.CENTER);
			tableCell.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			tableCell.setItem(false);
			return tableCell;
		});
		all_sum_nal = 0;
		all_sum = 0;
		cnt = 0;

		summa_plat.setText(String.valueOf(all_sum));
		summa_nal.setText(String.valueOf(all_sum_nal));
		cnt_all_.setText(String.valueOf(cnt));
	}

	@FXML
	void show_rel(ActionEvent event) {
		Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
		if (!(fn.get_checkparent() == null)) {
			ObservableList<Amra_Trans> trData = TerminalDAO.Amra_Trans_rel(fn.get_checknumber(), fn.get_checkparent());
			trans_table.setItems(trData);
			autoResizeColumns(trans_table);
			TableFilter.forTableView(trans_table).apply();
			trans_table.refresh();
		} else {
			Alert("��� ������������ ����������");
		}

	}

	@FXML
	private ContextMenu menubar;

	@FXML
	void show_all_col(ActionEvent event) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert("�������� ������� ������ �� �������!");
			} else {
				Stage stage_ = (Stage) anch_b4.getScene().getWindow();
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
				Connect.PNMB_ = fn.get_checknumber();
				Stage stage = new Stage();
				Parent root;
				root = FXMLLoader.load(Main.class.getResource("view/Transact_Unpiv.fxml"));
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������� " + fn.get_checknumber());
				stage.initOwner(stage_);
				stage.show();
			}
		} catch (IOException e) {
			Alert(e.getMessage());
		}
	}

	@FXML
	void show_all_deal(ActionEvent event) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert("�������� ������� ������ �� �������!");
			} else {
				Stage stage_ = (Stage) anch_b4.getScene().getWindow();
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
				Connect.PNMB_ = fn.get_checknumber();
				Stage stage = new Stage();
				Parent root;
				root = FXMLLoader.load(Main.class.getResource("view/Deals.fxml"));
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������� " + fn.get_checknumber());
				stage.initOwner(stage_);
				stage.show();
			}
		} catch (IOException e) {
			Alert(e.getMessage());
		}
	}

	@FXML
	void show_all_atr(ActionEvent event) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert("�������� ������� ������ �� �������!");
			} else {
				Stage stage_ = (Stage) anch_b4.getScene().getWindow();
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
				Connect.PNMB_ = fn.get_checknumber();
				Stage stage = new Stage();
				Parent root;
				root = FXMLLoader.load(Main.class.getResource("view/Attributes_.fxml"));
				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������� " + fn.get_checknumber());
				stage.initOwner(stage_);
				stage.show();
			}
		} catch (IOException e) {
			Alert(e.getMessage());
		}
	}

	@FXML
	void show_all_trn(ActionEvent event) {
		Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
		pb.setVisible(true);
		Task<Object> task = new Task<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					String call = "ifrun60.exe I:/KERNEL/operlist.fmx " + Connect.userID_ + "/" + Connect.userPassword_
							+ "@ODB where=\"" + "ITRNNUM in (select t.ITRNNUM "
							+ "  from trn t, z_sb_postdoc_amra_dbt g " + " where t.ITRNNUM(+) = g.KINDPAYMENT "
							+ "   and exists " + " (select null "
							+ "          from table(lob2table.separatedcolumns(paymentnumbers, "
							+ "                                                chr(13) || chr(10), "
							+ "                                                ';', "
							+ "                                                '')) " + "         where COLUMN1 = '"
							+ fn.get_checknumber() + "') " + "   and sess_id = " + fn.get_sess_id() + ")\"";
					ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Alerts(e.getMessage());
				}
				return null;
			}
		};
		task.setOnFailed(e -> Alert(task.getException().getMessage()));
		task.setOnSucceeded(e -> pb.setVisible(false));

		exec.execute(task);
	}

	@FXML
	void jasperkinder(ActionEvent event) {
		pb.setVisible(true);
		Task<Object> task = new Task<Object>() {
			@Override
			public Object call() throws Exception {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
				new PrintCheck().showReport(String.valueOf(fn.get_checknumber()), String.valueOf(fn.get_sess_id()));
				return null;
			}
		};
		task.setOnFailed(e -> Alert(task.getException().getMessage()));
		task.setOnSucceeded(e -> pb.setVisible(false));

		exec.execute(task);
	}

	@FXML
	void view_trn(ActionEvent event) {

		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			Alerts("�������� ������� ������ �� �������!");
		} else {

			Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
			pb.setVisible(true);
			Task<Object> task = new Task<Object>() {
				@Override
				public Object call() throws Exception {
					try {
						String call = "ifrun60.exe I:/KERNEL/operlist.fmx " + Connect.userID_ + "/"
								+ Connect.userPassword_ + "@ODB where=\"" + "ITRNNUM in (select t.ITRNNUM "
								+ "  from trn t, z_sb_postdoc_amra_dbt g " + " where t.ITRNNUM(+) = g.KINDPAYMENT "
								+ "   and exists " + " (select null "
								+ "          from table(lob2table.separatedcolumns(paymentnumbers, "
								+ "                                                chr(13) || chr(10), "
								+ "                                                ';', "
								+ "                                                '')) " + "         where COLUMN1 = '"
								+ fn.get_checknumber() + "') " + "   and sess_id = " + fn.get_sess_id() + ")\"";
						ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", call);
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Alerts(e.getMessage());
					}
					return null;
				}
			};
			task.setOnFailed(e -> Alert(task.getException().getMessage()));
			task.setOnSucceeded(e -> pb.setVisible(false));

			exec.execute(task);
		}
	}

	@FXML
	private void view_unpivot(ActionEvent actionEvent) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("�������� ������� ������ �� �������!");
				alert.showAndWait();
			} else {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

				Connect.PNMB_ = fn.get_checknumber();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("view/Transact_Unpiv.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������� " + fn.get_checknumber());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private void view_unpivot2(ActionEvent actionEvent) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("��������");
				alert.setHeaderText(null);
				alert.setContentText("�������� ������� ������ �� �������!");
				alert.showAndWait();
			} else {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

				Connect.PNMB_ = fn.get_checknumber();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("view/Transact_Unpiv.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("�������� " + fn.get_checknumber());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("��������");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
package sample.controller;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Transact;
import sample.model.TransactClass;
import sample.Main;
import sample.model.TerminalDAO;
import sample.model.FN_SESS_AMRA;
import sample.model.GUIUtils;
import sample.model.Amra_Trans;
import sample.model.Connect;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
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
import org.controlsfx.control.table.TableFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.prism.impl.Disposer.Record;

/**
 * Саид 04.04.2019.
 */

@SuppressWarnings("unused")
public class Tr_Am_View_con {

	@FXML
	private TableColumn<Amra_Trans, String> filetransactions;

	@FXML
	private CheckBox inkass;

	@FXML
	private TableColumn<Amra_Trans, String> fio;

	@FXML
	private TableColumn<Amra_Trans, String> rewardamount;

	@FXML
	private TableColumn<Amra_Trans, String> operationnumber;

	@FXML
	private TableColumn<Amra_Trans, String> detailing;

	@FXML
	private TableColumn<Amra_Trans, String> sess_id;

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
	private TableColumn<Amra_Trans, String> commissionamount;

	@FXML
	private TableColumn<Amra_Trans, String> vk;

	@FXML
	private TableColumn<Amra_Trans, String> sumnalprimal;

	@FXML
	private TableColumn<Amra_Trans, String> dateofoperation;

	@FXML
	private TableColumn<Amra_Trans, String> amountofscs;

	@FXML
	private TableColumn<Amra_Trans, String> cardnumber;

	@FXML
	private TableColumn<Amra_Trans, String> accountpayer;

	@FXML
	private TableColumn<Amra_Trans, String> recdate;

	@FXML
	private TableColumn<Amra_Trans, String> checknumber;

	@FXML
	private TableColumn<Amra_Trans, String> status;

	@FXML
	private TableColumn<Amra_Trans, String> mincommissionamount;

	@FXML
	private TableColumn<Amra_Trans, String> rownum;

	@FXML
	private TableColumn<Amra_Trans, String> amounttocheck;

	@FXML
	private TableColumn<Amra_Trans, String> dateclearing;

	@FXML
	private TableColumn<Amra_Trans, String> paydate;

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
	private TableColumn<Amra_Trans, String> amountwithchecks;

	@FXML
	private TableColumn<Amra_Trans, String> statusabs;

	@FXML
	private TableColumn<Amra_Trans, String> walletpayer;

	@FXML
	private TableColumn<Amra_Trans, String> sumofsplitting;

	@FXML
	private TableColumn<Amra_Trans, String> providertariff;

	@FXML
	private TableColumn<Amra_Trans, String> nkamount;

	@FXML
	private TableColumn<Amra_Trans, String> amountofpayment;

	@FXML
	private TableColumn<Amra_Trans, String> counter;

	@FXML
	private TableColumn<Amra_Trans, String> terminal;

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
	private TableColumn<Amra_Trans, String> cashamount;

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
	private DatePicker dt2;

	@FXML
	private ProgressIndicator pb;

	@FXML
	private void initialize() {
		trans_table.setEditable(true);
		exec = Executors.newCachedThreadPool((runnable) -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		rownum.setCellValueFactory(cellData -> cellData.getValue().rownumProperty());
		recdate.setCellValueFactory(cellData -> cellData.getValue().recdateProperty());
		paydate.setCellValueFactory(cellData -> cellData.getValue().paydateProperty());
		currency.setCellValueFactory(cellData -> cellData.getValue().currencyProperty());
		paymenttype.setCellValueFactory(cellData -> cellData.getValue().paymenttypeProperty());
		vk.setCellValueFactory(cellData -> cellData.getValue().vkProperty());
		dateofoperation.setCellValueFactory(cellData -> cellData.getValue().dateofoperationProperty());
		dataps.setCellValueFactory(cellData -> cellData.getValue().datapsProperty());
		dateclearing.setCellValueFactory(cellData -> cellData.getValue().dateclearingProperty());
		dealer.setCellValueFactory(cellData -> cellData.getValue().dealerProperty());
		accountpayer.setCellValueFactory(cellData -> cellData.getValue().accountpayerProperty());
		cardnumber.setCellValueFactory(cellData -> cellData.getValue().cardnumberProperty());
		operationnumber.setCellValueFactory(cellData -> cellData.getValue().operationnumberProperty());
		operationnumberdelivery.setCellValueFactory(cellData -> cellData.getValue().operationnumberdeliveryProperty());
		checknumber.setCellValueFactory(cellData -> cellData.getValue().checknumberProperty());
		checkparent.setCellValueFactory(cellData -> cellData.getValue().checkparentProperty());
		orderofprovidence.setCellValueFactory(cellData -> cellData.getValue().orderofprovidenceProperty());
		provider.setCellValueFactory(cellData -> cellData.getValue().providerProperty());
		owninown.setCellValueFactory(cellData -> cellData.getValue().owninownProperty());
		corrected.setCellValueFactory(cellData -> cellData.getValue().correctedProperty());
		commissionrate.setCellValueFactory(cellData -> cellData.getValue().commissionrateProperty());
		status.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		stringfromfile.setCellValueFactory(cellData -> cellData.getValue().stringfromfileProperty());
		rewardamount.setCellValueFactory(cellData -> cellData.getValue().rewardamountProperty());
		ownerincomeamount.setCellValueFactory(cellData -> cellData.getValue().ownerincomeamountProperty());
		commissionamount.setCellValueFactory(cellData -> cellData.getValue().commissionamountProperty());
		nkamount.setCellValueFactory(cellData -> cellData.getValue().nkamountProperty());
		maxcommissionamount.setCellValueFactory(cellData -> cellData.getValue().maxcommissionamountProperty());
		mincommissionamount.setCellValueFactory(cellData -> cellData.getValue().mincommissionamountProperty());
		cashamount.setCellValueFactory(cellData -> cellData.getValue().cashamountProperty());
		sumnalprimal.setCellValueFactory(cellData -> cellData.getValue().sumnalprimalProperty());
		amounttocheck.setCellValueFactory(cellData -> cellData.getValue().amounttocheckProperty());
		amountofpayment.setCellValueFactory(cellData -> cellData.getValue().amountofpaymentProperty());
		sumofsplitting.setCellValueFactory(cellData -> cellData.getValue().sumofsplittingProperty());
		amountintermediary.setCellValueFactory(cellData -> cellData.getValue().amountintermediaryProperty());
		amountofscs.setCellValueFactory(cellData -> cellData.getValue().amountofscsProperty());
		amountwithchecks.setCellValueFactory(cellData -> cellData.getValue().amountwithchecksProperty());
		counter.setCellValueFactory(cellData -> cellData.getValue().counterProperty());
		terminal.setCellValueFactory(cellData -> cellData.getValue().terminalProperty());
		terminalnetwork.setCellValueFactory(cellData -> cellData.getValue().terminalnetworkProperty());
		transactiontype.setCellValueFactory(cellData -> cellData.getValue().transactiontypeProperty());
		service.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
		filetransactions.setCellValueFactory(cellData -> cellData.getValue().filetransactionsProperty());
		fio.setCellValueFactory(cellData -> cellData.getValue().fioProperty());
		checksincoming.setCellValueFactory(cellData -> cellData.getValue().checksincomingProperty());
		barcode.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
		isaresident.setCellValueFactory(cellData -> cellData.getValue().isaresidentProperty());
		valuenotfound.setCellValueFactory(cellData -> cellData.getValue().valuenotfoundProperty());
		providertariff.setCellValueFactory(cellData -> cellData.getValue().providertariffProperty());
		counterchecks.setCellValueFactory(cellData -> cellData.getValue().counterchecksProperty());
		countercheck.setCellValueFactory(cellData -> cellData.getValue().countercheckProperty());
		id_.setCellValueFactory(cellData -> cellData.getValue().id_Property());
		detailing.setCellValueFactory(cellData -> cellData.getValue().detailingProperty());
		walletpayer.setCellValueFactory(cellData -> cellData.getValue().walletpayerProperty());
		walletreceiver.setCellValueFactory(cellData -> cellData.getValue().walletreceiverProperty());
		purposeofpayment.setCellValueFactory(cellData -> cellData.getValue().purposeofpaymentProperty());
		dataprovider.setCellValueFactory(cellData -> cellData.getValue().dataproviderProperty());
		statusabs.setCellValueFactory(cellData -> cellData.getValue().statusabsProperty());
		sess_id.setCellValueFactory(cellData -> cellData.getValue().sess_idProperty());

		recdate.setCellFactory(TextFieldTableCell.forTableColumn());
		paydate.setCellFactory(TextFieldTableCell.forTableColumn());
		currency.setCellFactory(TextFieldTableCell.forTableColumn());
		paymenttype.setCellFactory(TextFieldTableCell.forTableColumn());
		vk.setCellFactory(TextFieldTableCell.forTableColumn());
		dateofoperation.setCellFactory(TextFieldTableCell.forTableColumn());
		dataps.setCellFactory(TextFieldTableCell.forTableColumn());
		dateclearing.setCellFactory(TextFieldTableCell.forTableColumn());
		dealer.setCellFactory(TextFieldTableCell.forTableColumn());
		accountpayer.setCellFactory(TextFieldTableCell.forTableColumn());
		cardnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		operationnumber.setCellFactory(TextFieldTableCell.forTableColumn());
		operationnumberdelivery.setCellFactory(TextFieldTableCell.forTableColumn());
		checknumber.setCellFactory(TextFieldTableCell.forTableColumn());
		checkparent.setCellFactory(TextFieldTableCell.forTableColumn());
		orderofprovidence.setCellFactory(TextFieldTableCell.forTableColumn());
		provider.setCellFactory(TextFieldTableCell.forTableColumn());
		owninown.setCellFactory(TextFieldTableCell.forTableColumn());
		corrected.setCellFactory(TextFieldTableCell.forTableColumn());
		commissionrate.setCellFactory(TextFieldTableCell.forTableColumn());
		status.setCellFactory(TextFieldTableCell.forTableColumn());
		stringfromfile.setCellFactory(TextFieldTableCell.forTableColumn());
		rewardamount.setCellFactory(TextFieldTableCell.forTableColumn());
		ownerincomeamount.setCellFactory(TextFieldTableCell.forTableColumn());
		commissionamount.setCellFactory(TextFieldTableCell.forTableColumn());
		nkamount.setCellFactory(TextFieldTableCell.forTableColumn());
		maxcommissionamount.setCellFactory(TextFieldTableCell.forTableColumn());
		mincommissionamount.setCellFactory(TextFieldTableCell.forTableColumn());
		cashamount.setCellFactory(TextFieldTableCell.forTableColumn());
		sumnalprimal.setCellFactory(TextFieldTableCell.forTableColumn());
		amounttocheck.setCellFactory(TextFieldTableCell.forTableColumn());
		amountofpayment.setCellFactory(TextFieldTableCell.forTableColumn());
		sumofsplitting.setCellFactory(TextFieldTableCell.forTableColumn());
		amountintermediary.setCellFactory(TextFieldTableCell.forTableColumn());
		amountofscs.setCellFactory(TextFieldTableCell.forTableColumn());
		amountwithchecks.setCellFactory(TextFieldTableCell.forTableColumn());
		counter.setCellFactory(TextFieldTableCell.forTableColumn());
		terminal.setCellFactory(TextFieldTableCell.forTableColumn());
		terminalnetwork.setCellFactory(TextFieldTableCell.forTableColumn());
		transactiontype.setCellFactory(TextFieldTableCell.forTableColumn());
		service.setCellFactory(TextFieldTableCell.forTableColumn());
		filetransactions.setCellFactory(TextFieldTableCell.forTableColumn());
		fio.setCellFactory(TextFieldTableCell.forTableColumn());
		checksincoming.setCellFactory(TextFieldTableCell.forTableColumn());
		barcode.setCellFactory(TextFieldTableCell.forTableColumn());
		isaresident.setCellFactory(TextFieldTableCell.forTableColumn());
		valuenotfound.setCellFactory(TextFieldTableCell.forTableColumn());
		providertariff.setCellFactory(TextFieldTableCell.forTableColumn());
		counterchecks.setCellFactory(TextFieldTableCell.forTableColumn());
		countercheck.setCellFactory(TextFieldTableCell.forTableColumn());
		id_.setCellFactory(TextFieldTableCell.forTableColumn());
		detailing.setCellFactory(TextFieldTableCell.forTableColumn());
		walletpayer.setCellFactory(TextFieldTableCell.forTableColumn());
		walletreceiver.setCellFactory(TextFieldTableCell.forTableColumn());
		purposeofpayment.setCellFactory(TextFieldTableCell.forTableColumn());
		dataprovider.setCellFactory(TextFieldTableCell.forTableColumn());
		statusabs.setCellFactory(TextFieldTableCell.forTableColumn());
		sess_id.setCellFactory(TextFieldTableCell.forTableColumn());

		recdate.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_recdate(t.getNewValue());
			}
		});
		paydate.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
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
		paymenttype.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_paymenttype(t.getNewValue());
			}
		});
		vk.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_vk(t.getNewValue());
			}
		});
		dateofoperation.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dateofoperation(t.getNewValue());
			}
		});
		dataps.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dataps(t.getNewValue());
			}
		});
		dateclearing.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dateclearing(t.getNewValue());
			}
		});
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
		cardnumber.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_cardnumber(t.getNewValue());
			}
		});
		operationnumber.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_operationnumber(t.getNewValue());
			}
		});
		operationnumberdelivery.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_operationnumberdelivery(t.getNewValue());
			}
		});
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
		orderofprovidence.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_orderofprovidence(t.getNewValue());
			}
		});
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
		corrected.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_corrected(t.getNewValue());
			}
		});
		commissionrate.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_commissionrate(t.getNewValue());
			}
		});
		status.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_status(t.getNewValue());
			}
		});
		stringfromfile.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_stringfromfile(t.getNewValue());
			}
		});
		rewardamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_rewardamount(t.getNewValue());
			}
		});
		ownerincomeamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_ownerincomeamount(t.getNewValue());
			}
		});
		commissionamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_commissionamount(t.getNewValue());
			}
		});
		nkamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_nkamount(t.getNewValue());
			}
		});
		maxcommissionamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_maxcommissionamount(t.getNewValue());
			}
		});
		mincommissionamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_mincommissionamount(t.getNewValue());
			}
		});
		cashamount.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_cashamount(t.getNewValue());
			}
		});
		sumnalprimal.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sumnalprimal(t.getNewValue());
			}
		});
		amounttocheck.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amounttocheck(t.getNewValue());
			}
		});
		amountofpayment.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amountofpayment(t.getNewValue());
			}
		});
		sumofsplitting.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sumofsplitting(t.getNewValue());
			}
		});
		amountintermediary.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amountintermediary(t.getNewValue());
			}
		});
		amountofscs.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amountofscs(t.getNewValue());
			}
		});
		amountwithchecks.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_amountwithchecks(t.getNewValue());
			}
		});
		counter.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_counter(t.getNewValue());
			}
		});
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
		filetransactions.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_filetransactions(t.getNewValue());
			}
		});
		fio.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_fio(t.getNewValue());
			}
		});
		checksincoming.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_checksincoming(t.getNewValue());
			}
		});
		barcode.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_barcode(t.getNewValue());
			}
		});
		isaresident.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_isaresident(t.getNewValue());
			}
		});
		valuenotfound.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_valuenotfound(t.getNewValue());
			}
		});
		providertariff.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_providertariff(t.getNewValue());
			}
		});
		counterchecks.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_counterchecks(t.getNewValue());
			}
		});
		countercheck.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_countercheck(t.getNewValue());
			}
		});
		id_.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow())).set_id_(t.getNewValue());
			}
		});
		detailing.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_detailing(t.getNewValue());
			}
		});
		walletpayer.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_walletpayer(t.getNewValue());
			}
		});
		walletreceiver.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_walletreceiver(t.getNewValue());
			}
		});
		purposeofpayment.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_purposeofpayment(t.getNewValue());
			}
		});
		dataprovider.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_dataprovider(t.getNewValue());
			}
		});
		statusabs.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_statusabs(t.getNewValue());
			}
		});
		sess_id.setOnEditCommit(new EventHandler<CellEditEvent<Amra_Trans, String>>() {
			@Override
			public void handle(CellEditEvent<Amra_Trans, String> t) {
				((Amra_Trans) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.set_sess_id(t.getNewValue());
			}
		});
	}

	void on_filter() {
		autoResizeColumns(trans_table);
		TableFilter<Amra_Trans> filter = new TableFilter<>(trans_table);
	}
/*
	@FXML
	private void filter(ActionEvent event) {
		Task<List<Amra_Trans>> task_ = new Task<List<Amra_Trans>>() {
			@Override
			public ObservableList<Amra_Trans> call() {
				return null;
			}
		};
		task_.setOnFailed(e -> print_mess(task_.getException().toString()));
		task_.setOnSucceeded(e -> pb.setVisible(true));
		exec.execute(task_);

		Task<List<Amra_Trans>> task = new Task<List<Amra_Trans>>() {
			@Override
			public ObservableList<Amra_Trans> call() {
				if (inkass.isSelected()) {
					return TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(), false);
				} else {
					return TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(), true);
				}
			}
		};
		task.setOnFailed(e -> print_mess(task.getException().toString()));
		task.setOnSucceeded(e -> trans_table.setItems((ObservableList<Amra_Trans>) task.getValue()));
		exec.execute(task);
		// on_filter();
		pb.setVisible(false);
	}
*/
	void print_mess(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText(text);
				alert.showAndWait();
			}
		});
	}

	@FXML
	private void excel_export(ActionEvent actionEvent) {
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
				recdate_c.setCellValue("Дата загрузки");
				Cell paydate_c = row_d.createCell(1);
				paydate_c.setCellValue("Дата транзакции");
				Cell currency_c = row_d.createCell(2);
				currency_c.setCellValue("Валюта");
				Cell paymenttype_c = row_d.createCell(3);
				paymenttype_c.setCellValue("ВидПлатежа");
				Cell vk_c = row_d.createCell(4);
				vk_c.setCellValue("ВК");
				Cell dateofoperation_c = row_d.createCell(5);
				dateofoperation_c.setCellValue("ДатаОперации");
				Cell dataps_c = row_d.createCell(6);
				dataps_c.setCellValue("ДатаПС");
				Cell dateclearing_c = row_d.createCell(7);
				dateclearing_c.setCellValue("ДатаКлиринга");
				Cell dealer_c = row_d.createCell(8);
				dealer_c.setCellValue("Дилер");
				Cell accountpayer_c = row_d.createCell(9);
				accountpayer_c.setCellValue("ЛСПлательщика");
				Cell cardnumber_c = row_d.createCell(10);
				cardnumber_c.setCellValue("НомерКарты");
				Cell operationnumber_c = row_d.createCell(11);
				operationnumber_c.setCellValue("НомерОперации");
				Cell operationnumberdelivery_c = row_d.createCell(12);
				operationnumberdelivery_c.setCellValue("НомерОперацииСдача");
				Cell checknumber_c = row_d.createCell(13);
				checknumber_c.setCellValue("НомерЧека");
				Cell checkparent_c = row_d.createCell(14);
				checkparent_c.setCellValue("ЧекРодитель");
				Cell orderofprovidence_c = row_d.createCell(15);
				orderofprovidence_c.setCellValue("ПорядокПровдения");
				Cell provider_c = row_d.createCell(16);
				provider_c.setCellValue("Провайдер");
				Cell owninown_c = row_d.createCell(17);
				owninown_c.setCellValue("СвойВСвоем");
				Cell corrected_c = row_d.createCell(18);
				corrected_c.setCellValue("Скорректирована");
				Cell commissionrate_c = row_d.createCell(19);
				commissionrate_c.setCellValue("СтавкаКомиссии");
				Cell status_c = row_d.createCell(20);
				status_c.setCellValue("Статус");
				Cell stringfromfile_c = row_d.createCell(21);
				stringfromfile_c.setCellValue("СтрокаИзФайла");
				Cell rewardamount_c = row_d.createCell(22);
				rewardamount_c.setCellValue("СуммаВознаграждения");
				Cell ownerincomeamount_c = row_d.createCell(23);
				ownerincomeamount_c.setCellValue("СуммаДоходВладельца");
				Cell commissionamount_c = row_d.createCell(24);
				commissionamount_c.setCellValue("СуммаКомиссии");
				Cell nkamount_c = row_d.createCell(25);
				nkamount_c.setCellValue("СуммаНК");
				Cell maxcommissionamount_c = row_d.createCell(26);
				maxcommissionamount_c.setCellValue("СуммаКомиссииМакс");
				Cell mincommissionamount_c = row_d.createCell(27);
				mincommissionamount_c.setCellValue("СуммаКомиссииМин");
				Cell cashamount_c = row_d.createCell(28);
				cashamount_c.setCellValue("СуммаНаличных");
				Cell sumnalprimal_c = row_d.createCell(29);
				sumnalprimal_c.setCellValue("СуммаНалИзначальная");
				Cell amounttocheck_c = row_d.createCell(30);
				amounttocheck_c.setCellValue("СуммаНаЧек");
				Cell amountofpayment_c = row_d.createCell(31);
				amountofpayment_c.setCellValue("СуммаПлатежа");
				Cell sumofsplitting_c = row_d.createCell(32);
				sumofsplitting_c.setCellValue("СуммаНаРасщепление");
				Cell amountintermediary_c = row_d.createCell(33);
				amountintermediary_c.setCellValue("СуммаПосредника");
				Cell amountofscs_c = row_d.createCell(34);
				amountofscs_c.setCellValue("СуммаСКС");
				Cell amountwithchecks_c = row_d.createCell(35);
				amountwithchecks_c.setCellValue("СуммаСЧеков");
				Cell counter_c = row_d.createCell(36);
				counter_c.setCellValue("Счетчик");
				Cell terminal_c = row_d.createCell(37);
				terminal_c.setCellValue("Терминал");
				Cell terminalnetwork_c = row_d.createCell(38);
				terminalnetwork_c.setCellValue("ТерминальнаяСеть");
				Cell transactiontype_c = row_d.createCell(39);
				transactiontype_c.setCellValue("ТипТранзакции");
				Cell service_c = row_d.createCell(40);
				service_c.setCellValue("Услуга");
				Cell filetransactions_c = row_d.createCell(41);
				filetransactions_c.setCellValue("ФайлТранзакции");
				Cell fio_c = row_d.createCell(42);
				fio_c.setCellValue("ФИО");
				Cell checksincoming_c = row_d.createCell(43);
				checksincoming_c.setCellValue("ЧекиВходящие");
				Cell barcode_c = row_d.createCell(44);
				barcode_c.setCellValue("ШтрихКод");
				Cell isaresident_c = row_d.createCell(45);
				isaresident_c.setCellValue("ЯвляетсяРезидентом");
				Cell valuenotfound_c = row_d.createCell(46);
				valuenotfound_c.setCellValue("ЗначениеНеНайдено");
				Cell providertariff_c = row_d.createCell(47);
				providertariff_c.setCellValue("ТарифПровайдера");
				Cell counterchecks_c = row_d.createCell(48);
				counterchecks_c.setCellValue("СчетчикСчеков");
				Cell countercheck_c = row_d.createCell(49);
				countercheck_c.setCellValue("СчетчикНаЧек");
				Cell id__c = row_d.createCell(50);
				id__c.setCellValue("Id");
				Cell detailing_c = row_d.createCell(51);
				detailing_c.setCellValue("Деталировка");
				Cell walletpayer_c = row_d.createCell(52);
				walletpayer_c.setCellValue("КошелекПлательщик");
				Cell walletreceiver_c = row_d.createCell(53);
				walletreceiver_c.setCellValue("КошелекПолучатель");
				Cell purposeofpayment_c = row_d.createCell(54);
				purposeofpayment_c.setCellValue("НазначениеПлатежа");
				Cell dataprovider_c = row_d.createCell(55);
				dataprovider_c.setCellValue("ДатаПровайдера");
				Cell attributes__c = row_d.createCell(56);
				attributes__c.setCellValue("Атрибуты");
				Cell statusabs_c = row_d.createCell(57);
				statusabs_c.setCellValue("Статус проведения");
				Cell sess_id_c = row_d.createCell(58);
				sess_id_c.setCellValue("ИД Сессии");

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

				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:" + Connect.userID_ + "/"
						+ Connect.userPassword_ + "@" + Connect.connectionURL_ + "");

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
				conn.close();
				book.write(new FileOutputStream(file.getPath()));
				book.close();
			}
		} catch (SQLException | IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	private void view_post(ActionEvent actionEvent) {
		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(("Выберите сначала данные из таблицы!\n"));
			alert.showAndWait();

		} else {
			Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();
			new PrintReport().showReport(fn.get_checknumber());
		}
	}
	
	@FXML
	private void term_view_(ActionEvent actionEvent) {
		ObservableList<Amra_Trans> empData = TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(),"",
				false);
		populate_fn_sess(empData);

		autoResizeColumns(trans_table);
		// GUIUtils.autoFitTable(trans_table);
	}

	@FXML
	private void view_attr(ActionEvent actionEvent) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Выберите сначала данные из таблицы!");
				alert.showAndWait();
			} else {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

				Connect.PNMB_ = fn.get_checknumber();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("view/Attributes_.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Атрибуты транзакции " + fn.get_checknumber());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	// Найти загрузки

	@FXML
	private void filter(ActionEvent actionEvent) {

		ObservableList<Amra_Trans> empData = null;
		if (inkass.isSelected()) {
			empData = TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(),FIO.getText(), false);
		} else {
			empData = TerminalDAO.Amra_Trans_(id_sess.getText(), dt1.getValue(), dt2.getValue(),FIO.getText(), true);
		}
		populate_fn_sess(empData);
		autoResizeColumns(trans_table);
		TableFilter<Amra_Trans> filter = new TableFilter<>(trans_table);
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

	// Заполнить таблицу
	@FXML
	private void populate_fn_sess(ObservableList<Amra_Trans> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}

	@FXML
	private void view_unpivot(ActionEvent actionEvent) {
		try {
			if (trans_table.getSelectionModel().getSelectedItem() == null) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("terminal.png"));
				alert.setTitle("Внимание");
				alert.setHeaderText(null);
				alert.setContentText("Выберите сначала данные из таблицы!");
				alert.showAndWait();
			} else {
				Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

				Connect.PNMB_ = fn.get_checknumber();

				Stage stage = new Stage();
				Parent root;

				root = FXMLLoader.load(Main.class.getResource("view/Transact_Unpiv.fxml"));

				stage.setScene(new Scene(root));
				stage.getIcons().add(new Image("icon.png"));
				stage.setTitle("Подробно " + fn.get_checknumber());
				// stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
				stage.show();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

}
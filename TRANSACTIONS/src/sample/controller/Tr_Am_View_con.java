package sample.controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Transact;
import sample.model.TransactClass;
import sample.Main;
import sample.model.EmployeeDAO;
import sample.model.FN_SESS_AMRA;
import sample.model.Amra_Trans;
import sample.model.Connect;

import java.io.IOException;
import java.io.PrintWriter;
//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.prism.impl.Disposer.Record;

import java.util.Date;

/**
 * Саид 04.04.2019.
 */

@SuppressWarnings("unused")
public class Tr_Am_View_con {

	@FXML
	private TableColumn<Amra_Trans, String> filetransactions;

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
	private TextArea resultArea;

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
	private TableColumn<Amra_Trans, String> orderofprovidence;

	// For MultiThreading
	private Executor exec;

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

		try {
			ObservableList<Amra_Trans> empData = EmployeeDAO.Amra_Trans_(Connect.SESS_ID_);
			populate_fn_sess(empData);
		} catch (SQLException | ParseException | ClassNotFoundException e) {
			resultArea.setText(e.getMessage());
		}
	}

	@FXML
	private void view_attr(ActionEvent actionEvent) throws IOException {
		if (trans_table.getSelectionModel().getSelectedItem() == null) {
			resultArea.setText("Выберите сначала данные из таблицы!\n");
		} else {
			Amra_Trans fn = trans_table.getSelectionModel().getSelectedItem();

			Connect.PNMB_ = fn.get_checknumber();

			Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("view/Attributes.fxml"));
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("icon.png"));
			stage.setTitle("Атрибуты");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
			stage.show();
		}
	}

	// Заполнить таблицу
	@FXML
	private void populate_fn_sess(ObservableList<Amra_Trans> trData) {
		// Set items to the employeeTable
		trans_table.setItems(trData);
	}

}
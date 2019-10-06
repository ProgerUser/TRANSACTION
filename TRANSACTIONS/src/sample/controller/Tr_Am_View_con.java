package sample.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
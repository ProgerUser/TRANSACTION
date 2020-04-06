package sb_tr.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Amra_Trans {

    //private SimpleObjectProperty<Date> paydate;
	private StringProperty rownum;
	private StringProperty recdate;
	private StringProperty paydate;
	private StringProperty currency;
	private StringProperty paymenttype;
	private StringProperty vk;
	private StringProperty dateofoperation;
	private StringProperty dataps;
	private StringProperty dateclearing;
	private StringProperty dealer;
	private StringProperty accountpayer;
	private StringProperty cardnumber;
	private StringProperty operationnumber;
	private StringProperty operationnumberdelivery;
	private StringProperty checknumber;
	private StringProperty checkparent;
	private StringProperty orderofprovidence;
	private StringProperty provider;
	private StringProperty owninown;
	private StringProperty corrected;
	private StringProperty commissionrate;
	private StringProperty status;
	private StringProperty stringfromfile;
	private StringProperty rewardamount;
	private StringProperty ownerincomeamount;
	private StringProperty commissionamount;
	private StringProperty nkamount;
	private StringProperty maxcommissionamount;
	private StringProperty mincommissionamount;
	private StringProperty cashamount;
	private StringProperty sumnalprimal;
	private StringProperty amounttocheck;
	private StringProperty amountofpayment;
	private StringProperty sumofsplitting;
	private StringProperty amountintermediary;
	private StringProperty amountofscs;
	private StringProperty amountwithchecks;
	private StringProperty counter;
	private StringProperty terminal;
	private StringProperty terminalnetwork;
	private StringProperty transactiontype;
	private StringProperty service;
	private StringProperty filetransactions;
	private StringProperty fio;
	private StringProperty checksincoming;
	private StringProperty barcode;
	private StringProperty isaresident;
	private StringProperty valuenotfound;
	private StringProperty providertariff;
	private StringProperty counterchecks;
	private StringProperty countercheck;
	private StringProperty id_;
	private StringProperty detailing;
	private StringProperty walletpayer;
	private StringProperty walletreceiver;
	private StringProperty purposeofpayment;
	private StringProperty dataprovider;
	private StringProperty statusabs;
	private StringProperty sess_id;

	// Constructor
	public Amra_Trans() {
		
		
		this.rownum = new SimpleStringProperty();
		this.recdate = new SimpleStringProperty();
		//this.paydate = new SimpleObjectProperty<>();
		this.paydate = new SimpleStringProperty();
		this.currency = new SimpleStringProperty();
		this.paymenttype = new SimpleStringProperty();
		this.vk = new SimpleStringProperty();
		this.dateofoperation = new SimpleStringProperty();
		this.dataps = new SimpleStringProperty();
		this.dateclearing = new SimpleStringProperty();
		this.dealer = new SimpleStringProperty();
		this.accountpayer = new SimpleStringProperty();
		this.cardnumber = new SimpleStringProperty();
		this.operationnumber = new SimpleStringProperty();
		this.operationnumberdelivery = new SimpleStringProperty();
		this.checknumber = new SimpleStringProperty();
		this.checkparent = new SimpleStringProperty();
		this.orderofprovidence = new SimpleStringProperty();
		this.provider = new SimpleStringProperty();
		this.owninown = new SimpleStringProperty();
		this.corrected = new SimpleStringProperty();
		this.commissionrate = new SimpleStringProperty();
		this.status = new SimpleStringProperty();
		this.stringfromfile = new SimpleStringProperty();
		this.rewardamount = new SimpleStringProperty();
		this.ownerincomeamount = new SimpleStringProperty();
		this.commissionamount = new SimpleStringProperty();
		this.nkamount = new SimpleStringProperty();
		this.maxcommissionamount = new SimpleStringProperty();
		this.mincommissionamount = new SimpleStringProperty();
		this.cashamount = new SimpleStringProperty();
		this.sumnalprimal = new SimpleStringProperty();
		this.amounttocheck = new SimpleStringProperty();
		this.amountofpayment = new SimpleStringProperty();
		this.sumofsplitting = new SimpleStringProperty();
		this.amountintermediary = new SimpleStringProperty();
		this.amountofscs = new SimpleStringProperty();
		this.amountwithchecks = new SimpleStringProperty();
		this.counter = new SimpleStringProperty();
		this.terminal = new SimpleStringProperty();
		this.terminalnetwork = new SimpleStringProperty();
		this.transactiontype = new SimpleStringProperty();
		this.service = new SimpleStringProperty();
		this.filetransactions = new SimpleStringProperty();
		this.fio = new SimpleStringProperty();
		this.checksincoming = new SimpleStringProperty();
		this.barcode = new SimpleStringProperty();
		this.isaresident = new SimpleStringProperty();
		this.valuenotfound = new SimpleStringProperty();
		this.providertariff = new SimpleStringProperty();
		this.counterchecks = new SimpleStringProperty();
		this.countercheck = new SimpleStringProperty();
		this.id_ = new SimpleStringProperty();
		this.detailing = new SimpleStringProperty();
		this.walletpayer = new SimpleStringProperty();
		this.walletreceiver = new SimpleStringProperty();
		this.purposeofpayment = new SimpleStringProperty();
		this.dataprovider = new SimpleStringProperty();
		this.statusabs = new SimpleStringProperty();
		this.sess_id = new SimpleStringProperty();

	}

	public String get_recdate() {
		return recdate.get();
	}

//	public String get_paydate() {
//		return paydate.get();
//	}

    //hire_date
    public Object get_paydate(){
        return paydate.get();
    }



	public String get_currency() {
		return currency.get();
	}

	public String get_paymenttype() {
		return paymenttype.get();
	}

	public String get_vk() {
		return vk.get();
	}

	public String get_dateofoperation() {
		return dateofoperation.get();
	}

	public String get_dataps() {
		return dataps.get();
	}

	public String get_dateclearing() {
		return dateclearing.get();
	}

	public String get_dealer() {
		return dealer.get();
	}

	public String get_accountpayer() {
		return accountpayer.get();
	}

	public String get_cardnumber() {
		return cardnumber.get();
	}

	public String get_operationnumber() {
		return operationnumber.get();
	}

	public String get_operationnumberdelivery() {
		return operationnumberdelivery.get();
	}

	public String get_checknumber() {
		return checknumber.get();
	}

	public String get_checkparent() {
		return checkparent.get();
	}

	public String get_orderofprovidence() {
		return orderofprovidence.get();
	}

	public String get_provider() {
		return provider.get();
	}

	public String get_owninown() {
		return owninown.get();
	}

	public String get_corrected() {
		return corrected.get();
	}

	public String get_commissionrate() {
		return commissionrate.get();
	}

	public String get_status() {
		return status.get();
	}

	public String get_stringfromfile() {
		return stringfromfile.get();
	}

	public String get_rewardamount() {
		return rewardamount.get();
	}

	public String get_ownerincomeamount() {
		return ownerincomeamount.get();
	}

	public String get_commissionamount() {
		return commissionamount.get();
	}

	public String get_nkamount() {
		return nkamount.get();
	}

	public String get_maxcommissionamount() {
		return maxcommissionamount.get();
	}

	public String get_mincommissionamount() {
		return mincommissionamount.get();
	}

	public String get_cashamount() {
		return cashamount.get();
	}

	public String get_sumnalprimal() {
		return sumnalprimal.get();
	}

	public String get_amounttocheck() {
		return amounttocheck.get();
	}

	public String get_amountofpayment() {
		return amountofpayment.get();
	}

	public String get_sumofsplitting() {
		return sumofsplitting.get();
	}

	public String get_amountintermediary() {
		return amountintermediary.get();
	}

	public String get_amountofscs() {
		return amountofscs.get();
	}

	public String get_amountwithchecks() {
		return amountwithchecks.get();
	}

	public String get_counter() {
		return counter.get();
	}

	public String get_terminal() {
		return terminal.get();
	}

	public String get_terminalnetwork() {
		return terminalnetwork.get();
	}

	public String get_transactiontype() {
		return transactiontype.get();
	}

	public String get_service() {
		return service.get();
	}

	public String get_filetransactions() {
		return filetransactions.get();
	}

	public String get_fio() {
		return fio.get();
	}

	public String get_checksincoming() {
		return checksincoming.get();
	}

	public String get_barcode() {
		return barcode.get();
	}

	public String get_isaresident() {
		return isaresident.get();
	}

	public String get_valuenotfound() {
		return valuenotfound.get();
	}

	public String get_providertariff() {
		return providertariff.get();
	}

	public String get_counterchecks() {
		return counterchecks.get();
	}

	public String get_countercheck() {
		return countercheck.get();
	}

	public String get_id_() {
		return id_.get();
	}

	public String get_detailing() {
		return detailing.get();
	}

	public String get_walletpayer() {
		return walletpayer.get();
	}

	public String get_walletreceiver() {
		return walletreceiver.get();
	}

	public String get_purposeofpayment() {
		return purposeofpayment.get();
	}

	public String get_dataprovider() {
		return dataprovider.get();
	}

	public String get_statusabs() {
		return statusabs.get();
	}

	public String get_sess_id() {
		return sess_id.get();
	}

	public void set_recdate(String recdate) {
		this.recdate.set(recdate);
	}

	public void set_paydate(String paydate) {
		this.paydate.set(paydate);
	}
	
//    public void set_paydate(Date paydate){
//        this.paydate.set(paydate);
//    }

	public void set_currency(String currency) {
		this.currency.set(currency);
	}

	public void set_paymenttype(String paymenttype) {
		this.paymenttype.set(paymenttype);
	}

	public void set_vk(String vk) {
		this.vk.set(vk);
	}

	public void set_dateofoperation(String dateofoperation) {
		this.dateofoperation.set(dateofoperation);
	}

	public void set_dataps(String dataps) {
		this.dataps.set(dataps);
	}

	public void set_dateclearing(String dateclearing) {
		this.dateclearing.set(dateclearing);
	}

	public void set_dealer(String dealer) {
		this.dealer.set(dealer);
	}

	public void set_accountpayer(String accountpayer) {
		this.accountpayer.set(accountpayer);
	}

	public void set_cardnumber(String cardnumber) {
		this.cardnumber.set(cardnumber);
	}

	public void set_operationnumber(String operationnumber) {
		this.operationnumber.set(operationnumber);
	}

	public void set_operationnumberdelivery(String operationnumberdelivery) {
		this.operationnumberdelivery.set(operationnumberdelivery);
	}

	public void set_checknumber(String checknumber) {
		this.checknumber.set(checknumber);
	}

	public void set_checkparent(String checkparent) {
		this.checkparent.set(checkparent);
	}

	public void set_rownum(String rownum) {
		this.rownum.set(rownum);
	}

	public void set_orderofprovidence(String orderofprovidence) {
		this.orderofprovidence.set(orderofprovidence);
	}

	public void set_provider(String provider) {
		this.provider.set(provider);
	}

	public void set_owninown(String owninown) {
		this.owninown.set(owninown);
	}

	public void set_corrected(String corrected) {
		this.corrected.set(corrected);
	}

	public void set_commissionrate(String commissionrate) {
		this.commissionrate.set(commissionrate);
	}

	public void set_status(String status) {
		this.status.set(status);
	}

	public void set_stringfromfile(String stringfromfile) {
		this.stringfromfile.set(stringfromfile);
	}

	public void set_rewardamount(String rewardamount) {
		this.rewardamount.set(rewardamount);
	}

	public void set_ownerincomeamount(String ownerincomeamount) {
		this.ownerincomeamount.set(ownerincomeamount);
	}

	public void set_commissionamount(String commissionamount) {
		this.commissionamount.set(commissionamount);
	}

	public void set_nkamount(String nkamount) {
		this.nkamount.set(nkamount);
	}

	public void set_maxcommissionamount(String maxcommissionamount) {
		this.maxcommissionamount.set(maxcommissionamount);
	}

	public void set_mincommissionamount(String mincommissionamount) {
		this.mincommissionamount.set(mincommissionamount);
	}

	public void set_cashamount(String cashamount) {
		this.cashamount.set(cashamount);
	}

	public void set_sumnalprimal(String sumnalprimal) {
		this.sumnalprimal.set(sumnalprimal);
	}

	public void set_amounttocheck(String amounttocheck) {
		this.amounttocheck.set(amounttocheck);
	}

	public void set_amountofpayment(String amountofpayment) {
		this.amountofpayment.set(amountofpayment);
	}

	public void set_sumofsplitting(String sumofsplitting) {
		this.sumofsplitting.set(sumofsplitting);
	}

	public void set_amountintermediary(String amountintermediary) {
		this.amountintermediary.set(amountintermediary);
	}

	public void set_amountofscs(String amountofscs) {
		this.amountofscs.set(amountofscs);
	}

	public void set_amountwithchecks(String amountwithchecks) {
		this.amountwithchecks.set(amountwithchecks);
	}

	public void set_counter(String counter) {
		this.counter.set(counter);
	}

	public void set_terminal(String terminal) {
		this.terminal.set(terminal);
	}

	public void set_terminalnetwork(String terminalnetwork) {
		this.terminalnetwork.set(terminalnetwork);
	}

	public void set_transactiontype(String transactiontype) {
		this.transactiontype.set(transactiontype);
	}

	public void set_service(String service) {
		this.service.set(service);
	}

	public void set_filetransactions(String filetransactions) {
		this.filetransactions.set(filetransactions);
	}

	public void set_fio(String fio) {
		this.fio.set(fio);
	}

	public void set_checksincoming(String checksincoming) {
		this.checksincoming.set(checksincoming);
	}

	public void set_barcode(String barcode) {
		this.barcode.set(barcode);
	}

	public void set_isaresident(String isaresident) {
		this.isaresident.set(isaresident);
	}

	public void set_valuenotfound(String valuenotfound) {
		this.valuenotfound.set(valuenotfound);
	}

	public void set_providertariff(String providertariff) {
		this.providertariff.set(providertariff);
	}

	public void set_counterchecks(String counterchecks) {
		this.counterchecks.set(counterchecks);
	}

	public void set_countercheck(String countercheck) {
		this.countercheck.set(countercheck);
	}

	public void set_id_(String id_) {
		this.id_.set(id_);
	}

	public void set_detailing(String detailing) {
		this.detailing.set(detailing);
	}

	public void set_walletpayer(String walletpayer) {
		this.walletpayer.set(walletpayer);
	}

	public void set_walletreceiver(String walletreceiver) {
		this.walletreceiver.set(walletreceiver);
	}

	public void set_purposeofpayment(String purposeofpayment) {
		this.purposeofpayment.set(purposeofpayment);
	}

	public void set_dataprovider(String dataprovider) {
		this.dataprovider.set(dataprovider);
	}

	public void set_statusabs(String statusabs) {
		this.statusabs.set(statusabs);
	}

	public void set_sess_id(String sess_id) {
		this.sess_id.set(sess_id);
	}

	public StringProperty recdateProperty() {
		return recdate;
	}

	public StringProperty paydateProperty() {
		return paydate;
	}
//    public SimpleObjectProperty<Date> paydateProperty(){
//        return paydate;
//    }


	public StringProperty currencyProperty() {
		return currency;
	}

	public StringProperty paymenttypeProperty() {
		return paymenttype;
	}

	public StringProperty vkProperty() {
		return vk;
	}

	public StringProperty dateofoperationProperty() {
		return dateofoperation;
	}

	public StringProperty datapsProperty() {
		return dataps;
	}

	public StringProperty dateclearingProperty() {
		return dateclearing;
	}

	public StringProperty dealerProperty() {
		return dealer;
	}

	public StringProperty accountpayerProperty() {
		return accountpayer;
	}

	public StringProperty cardnumberProperty() {
		return cardnumber;
	}

	public StringProperty operationnumberProperty() {
		return operationnumber;
	}

	public StringProperty operationnumberdeliveryProperty() {
		return operationnumberdelivery;
	}

	public StringProperty checknumberProperty() {
		return checknumber;
	}

	public StringProperty checkparentProperty() {
		return checkparent;
	}

	public StringProperty orderofprovidenceProperty() {
		return orderofprovidence;
	}

	public StringProperty providerProperty() {
		return provider;
	}

	public StringProperty owninownProperty() {
		return owninown;
	}

	public StringProperty correctedProperty() {
		return corrected;
	}

	public StringProperty commissionrateProperty() {
		return commissionrate;
	}

	public StringProperty statusProperty() {
		return status;
	}

	public StringProperty stringfromfileProperty() {
		return stringfromfile;
	}

	public StringProperty rewardamountProperty() {
		return rewardamount;
	}

	public StringProperty ownerincomeamountProperty() {
		return ownerincomeamount;
	}

	public StringProperty commissionamountProperty() {
		return commissionamount;
	}

	public StringProperty nkamountProperty() {
		return nkamount;
	}

	public StringProperty maxcommissionamountProperty() {
		return maxcommissionamount;
	}

	public StringProperty mincommissionamountProperty() {
		return mincommissionamount;
	}

	public StringProperty cashamountProperty() {
		return cashamount;
	}

	public StringProperty sumnalprimalProperty() {
		return sumnalprimal;
	}

	public StringProperty amounttocheckProperty() {
		return amounttocheck;
	}

	public StringProperty amountofpaymentProperty() {
		return amountofpayment;
	}

	public StringProperty sumofsplittingProperty() {
		return sumofsplitting;
	}

	public StringProperty amountintermediaryProperty() {
		return amountintermediary;
	}

	public StringProperty amountofscsProperty() {
		return amountofscs;
	}

	public StringProperty amountwithchecksProperty() {
		return amountwithchecks;
	}

	public StringProperty counterProperty() {
		return counter;
	}

	public StringProperty terminalProperty() {
		return terminal;
	}

	public StringProperty terminalnetworkProperty() {
		return terminalnetwork;
	}

	public StringProperty transactiontypeProperty() {
		return transactiontype;
	}

	public StringProperty serviceProperty() {
		return service;
	}

	public StringProperty filetransactionsProperty() {
		return filetransactions;
	}

	public StringProperty fioProperty() {
		return fio;
	}

	public StringProperty checksincomingProperty() {
		return checksincoming;
	}

	public StringProperty barcodeProperty() {
		return barcode;
	}

	public StringProperty isaresidentProperty() {
		return isaresident;
	}

	public StringProperty valuenotfoundProperty() {
		return valuenotfound;
	}

	public StringProperty providertariffProperty() {
		return providertariff;
	}

	public StringProperty counterchecksProperty() {
		return counterchecks;
	}

	public StringProperty countercheckProperty() {
		return countercheck;
	}

	public StringProperty id_Property() {
		return id_;
	}

	public StringProperty detailingProperty() {
		return detailing;
	}

	public StringProperty walletpayerProperty() {
		return walletpayer;
	}

	public StringProperty walletreceiverProperty() {
		return walletreceiver;
	}

	public StringProperty purposeofpaymentProperty() {
		return purposeofpayment;
	}

	public StringProperty dataproviderProperty() {
		return dataprovider;
	}

	public StringProperty statusabsProperty() {
		return statusabs;
	}

	public StringProperty sess_idProperty() {
		return sess_id;
	}

	public StringProperty rownumProperty() {
		return rownum;
	}

}

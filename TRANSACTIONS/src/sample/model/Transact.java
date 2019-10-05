package sample.model;

import javafx.beans.property.*;


public class Transact {
    private StringProperty PAYMENTNUMBER;
    private StringProperty FIO;
    private StringProperty DATETIMEPAYMENT;
    private StringProperty ACCOUNT;
    private StringProperty PAYMENTDATA;
    private StringProperty INSUM;
    private StringProperty FEESUM;
    private StringProperty SESS_ID;
    
    //Constructor
    public Transact() {
        this.PAYMENTNUMBER = new SimpleStringProperty();
        this.FIO = new SimpleStringProperty();
        this.DATETIMEPAYMENT = new SimpleStringProperty();
        this.ACCOUNT = new SimpleStringProperty();
        this.PAYMENTDATA = new SimpleStringProperty();
        this.INSUM = new SimpleStringProperty();
        this.FEESUM = new SimpleStringProperty();
        this.SESS_ID = new SimpleStringProperty();
    }

    //PAYMENTNUMBER
    public String getPAYMENTNUMBER() {
        return PAYMENTNUMBER.get();
    }

    public void setPAYMENTNUMBER(String PAYMENTNUMBER_){
        this.PAYMENTNUMBER.set(PAYMENTNUMBER_);
    }

    public StringProperty PAYMENTNUMBERProperty(){
        return PAYMENTNUMBER;
    }

    //FIO
    public String getFIO () {
        return FIO.get();
    }

    public void setFIO(String FIO_){
        this.FIO.set(FIO_);
    }

    public StringProperty FIOProperty() {
        return FIO;
    }

    //DATETIMEPAYMENT
    public String getDATETIMEPAYMENT () {
        return DATETIMEPAYMENT.get();
    }

    public void setDATETIMEPAYMENT(String DATETIMEPAYMENT_){
        this.DATETIMEPAYMENT.set(DATETIMEPAYMENT_);
    }

    public StringProperty DATETIMEPAYMENTProperty() {
        return DATETIMEPAYMENT;
    }

    //ACCOUNT
    public String getACCOUNT () {
        return ACCOUNT.get();
    }

    public void setACCOUNT (String ACCOUNT_){
        this.ACCOUNT.set(ACCOUNT_);
    }

    public StringProperty ACCOUNTProperty() {
        return ACCOUNT;
    }

    //PAYMENTDATA
    public String getPAYMENTDATA () {
        return PAYMENTDATA.get();
    }

    public void setPAYMENTDATA (String PAYMENTDATA_){
        this.PAYMENTDATA.set(PAYMENTDATA_);
    }

    public StringProperty PAYMENTDATAProperty() {
        return PAYMENTDATA;
    }
    
    //INSUM
    public String getINSUM () {
        return INSUM.get();
    }

    public void setINSUM (String INSUM_){
        this.INSUM.set(INSUM_);
    }

    public StringProperty INSUMProperty() {
        return INSUM;
    }
    
    //FEESUM
    public String getFEESUM () {
        return FEESUM.get();
    }

    public void setFEESUM (String FEESUM_){
        this.FEESUM.set(FEESUM_);
    }

    public StringProperty FEESUMProperty() {
        return FEESUM;
    }
    
    //SESS_ID
    public String getSESS_ID () {
        return SESS_ID.get();
    }

    public void setSESS_ID (String SESS_ID_){
        this.SESS_ID.set(SESS_ID_);
    }

    public StringProperty SESS_IDProperty() {
        return SESS_ID;
    }
    
}

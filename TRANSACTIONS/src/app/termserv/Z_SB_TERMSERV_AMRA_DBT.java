package app.termserv;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Z_SB_TERMSERV_AMRA_DBT {
	/** Сумма комиссии */
	private DoubleProperty COMISSION;
	/** БО2 */
	private IntegerProperty BO2;
	/** БО1 */
	private IntegerProperty BO1;
	/** Наименование получателя */
	private StringProperty ACC_NAME;
	/** ОКАТО */
	private StringProperty OKATO;
	/** КБК */
	private StringProperty KBK;
	/** Расчетный счет */
	private StringProperty ACC_REC;
	/** КПП */
	private StringProperty KPP;
	/** ИНН */
	private StringProperty INN;
	/** Транзитный счет */
	private StringProperty ACCOUNT;
	/** ID точки */
	private StringProperty IDTERM;
	/** Название услуги */
	private StringProperty NAME;
	/** ID */
	private LongProperty ID;

	public Z_SB_TERMSERV_AMRA_DBT() {
		this.ID = new SimpleLongProperty();
		this.COMISSION = new SimpleDoubleProperty();
		this.BO2 = new SimpleIntegerProperty();
		this.BO1 = new SimpleIntegerProperty();
		this.ACC_NAME = new SimpleStringProperty();
		this.OKATO = new SimpleStringProperty();
		this.KBK = new SimpleStringProperty();
		this.ACC_REC = new SimpleStringProperty();
		this.KPP = new SimpleStringProperty();
		this.INN = new SimpleStringProperty();
		this.ACCOUNT = new SimpleStringProperty();
		this.IDTERM = new SimpleStringProperty();
		this.NAME = new SimpleStringProperty();
	}
	
	public LongProperty IDProperty() {
		return ID;
	}
	
	public Long getID() {
		return ID.get();
	}
	
	public void setID(Long ID) {
		this.ID.set(ID);
	}
	
	public void setCOMISSION(Double COMISSION) {
		this.COMISSION.set(COMISSION);
	}

	public void setBO2(Integer BO2) {
		this.BO2.set(BO2);
	}

	public void setBO1(Integer BO1) {
		this.BO1.set(BO1);
	}

	public void setACC_NAME(String ACC_NAME) {
		this.ACC_NAME.set(ACC_NAME);
	}

	public void setOKATO(String OKATO) {
		this.OKATO.set(OKATO);
	}

	public void setKBK(String KBK) {
		this.KBK.set(KBK);
	}

	public void setACC_REC(String ACC_REC) {
		this.ACC_REC.set(ACC_REC);
	}

	public void setKPP(String KPP) {
		this.KPP.set(KPP);
	}

	public void setINN(String INN) {
		this.INN.set(INN);
	}

	public void setACCOUNT(String ACCOUNT) {
		this.ACCOUNT.set(ACCOUNT);
	}

	public void setIDTERM(String IDTERM) {
		this.IDTERM.set(IDTERM);
	}

	public void setNAME(String NAME) {
		this.NAME.set(NAME);
	}

	public Double getCOMISSION() {
		return COMISSION.get();
	}

	public Integer getBO2() {
		return BO2.get();
	}

	public Integer getBO1() {
		return BO1.get();
	}

	public String getACC_NAME() {
		return ACC_NAME.get();
	}

	public String getOKATO() {
		return OKATO.get();
	}

	public String getKBK() {
		return KBK.get();
	}

	public String getACC_REC() {
		return ACC_REC.get();
	}

	public String getKPP() {
		return KPP.get();
	}

	public String getINN() {
		return INN.get();
	}

	public String getACCOUNT() {
		return ACCOUNT.get();
	}

	public String getIDTERM() {
		return IDTERM.get();
	}

	public String getNAME() {
		return NAME.get();
	}

	public DoubleProperty COMISSIONProperty() {
		return COMISSION;
	}

	public IntegerProperty BO2Property() {
		return BO2;
	}

	public IntegerProperty BO1Property() {
		return BO1;
	}

	public StringProperty ACC_NAMEProperty() {
		return ACC_NAME;
	}

	public StringProperty OKATOProperty() {
		return OKATO;
	}

	public StringProperty KBKProperty() {
		return KBK;
	}

	public StringProperty ACC_RECProperty() {
		return ACC_REC;
	}

	public StringProperty KPPProperty() {
		return KPP;
	}

	public StringProperty INNProperty() {
		return INN;
	}

	public StringProperty ACCOUNTProperty() {
		return ACCOUNT;
	}

	public StringProperty IDTERMProperty() {
		return IDTERM;
	}

	public StringProperty NAMEProperty() {
		return NAME;
	}
}

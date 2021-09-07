package contact;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SBRA_CONTACT_ACC_CODE {
	/** Нет данных */
	private LongProperty IACCOTD;
	/** Нет данных */
	private StringProperty CACCNAME;
	/** Нет данных */
	private StringProperty CODE_NAME;
	/** Нет данных */
	private StringProperty ACC_701;
	/** Нет данных */
	private StringProperty COD_;

	public SBRA_CONTACT_ACC_CODE() {
		this.IACCOTD = new SimpleLongProperty();
		this.CACCNAME = new SimpleStringProperty();
		this.CODE_NAME = new SimpleStringProperty();
		this.ACC_701 = new SimpleStringProperty();
		this.COD_ = new SimpleStringProperty();
	}

	public void setIACCOTD(Long IACCOTD) {
		this.IACCOTD.set(IACCOTD);
	}

	public void setCACCNAME(String CACCNAME) {
		this.CACCNAME.set(CACCNAME);
	}

	public void setCODE_NAME(String CODE_NAME) {
		this.CODE_NAME.set(CODE_NAME);
	}

	public void setACC_701(String ACC_701) {
		this.ACC_701.set(ACC_701);
	}

	public void setCOD_(String COD_) {
		this.COD_.set(COD_);
	}

	public Long getIACCOTD() {
		return IACCOTD.get();
	}

	public String getCACCNAME() {
		return CACCNAME.get();
	}

	public String getCODE_NAME() {
		return CODE_NAME.get();
	}

	public String getACC_701() {
		return ACC_701.get();
	}

	public String getCOD_() {
		return COD_.get();
	}

	public LongProperty IACCOTDProperty() {
		return IACCOTD;
	}

	public StringProperty CACCNAMEProperty() {
		return CACCNAME;
	}

	public StringProperty CODE_NAMEProperty() {
		return CODE_NAME;
	}

	public StringProperty ACC_701Property() {
		return ACC_701;
	}

	public StringProperty COD_Property() {
		return COD_;
	}
}

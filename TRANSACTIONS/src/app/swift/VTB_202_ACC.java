package app.swift;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VTB_202_ACC {
	/** Нет данных */
	private StringProperty KBNK;
	/** Нет данных */
	private StringProperty CACCNAME;
	/** Нет данных */
	private StringProperty ACCOUNT_202;

	public VTB_202_ACC() {
		this.KBNK = new SimpleStringProperty();
		this.CACCNAME = new SimpleStringProperty();
		this.ACCOUNT_202 = new SimpleStringProperty();
	}

	public void setKBNK(String KBNK) {
		this.KBNK.set(KBNK);
	}

	public void setCACCNAME(String CACCNAME) {
		this.CACCNAME.set(CACCNAME);
	}

	public void setACCOUNT_202(String ACCOUNT_202) {
		this.ACCOUNT_202.set(ACCOUNT_202);
	}

	public String getKBNK() {
		return KBNK.get();
	}

	public String getCACCNAME() {
		return CACCNAME.get();
	}

	public String getACCOUNT_202() {
		return ACCOUNT_202.get();
	}

	public StringProperty KBNKProperty() {
		return KBNK;
	}

	public StringProperty CACCNAMEProperty() {
		return CACCNAME;
	}

	public StringProperty ACCOUNT_202Property() {
		return ACCOUNT_202;
	}
}

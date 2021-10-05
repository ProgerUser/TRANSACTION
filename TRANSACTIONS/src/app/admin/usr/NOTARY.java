package app.admin.usr;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NOTARY {
	/** Руководитель */
	private StringProperty NOT_RUK;
	/** Наименование ЗАГСа */
	private StringProperty NOT_NAME;
	/** Город */
	private LongProperty NOT_OTD;
	/** ИД */
	private LongProperty NOT_ID;

	public NOTARY() {
		this.NOT_RUK = new SimpleStringProperty();
		this.NOT_NAME = new SimpleStringProperty();
		this.NOT_OTD = new SimpleLongProperty();
		this.NOT_ID = new SimpleLongProperty();
	}

	public void setNOT_RUK(String NOT_RUK) {
		this.NOT_RUK.set(NOT_RUK);
	}

	public void setNOT_NAME(String NOT_NAME) {
		this.NOT_NAME.set(NOT_NAME);
	}

	public void setNOT_OTD(Long NOT_OTD) {
		this.NOT_OTD.set(NOT_OTD);
	}

	public void setNOT_ID(Long NOT_ID) {
		this.NOT_ID.set(NOT_ID);
	}

	public String getNOT_RUK() {
		return NOT_RUK.get();
	}

	public String getNOT_NAME() {
		return NOT_NAME.get();
	}

	public Long getNOT_OTD() {
		return NOT_OTD.get();
	}

	public Long getNOT_ID() {
		return NOT_ID.get();
	}

	public StringProperty NOT_RUKProperty() {
		return NOT_RUK;
	}

	public StringProperty NOT_NAMEProperty() {
		return NOT_NAME;
	}

	public LongProperty NOT_OTDProperty() {
		return NOT_OTD;
	}

	public LongProperty NOT_IDProperty() {
		return NOT_ID;
	}
}

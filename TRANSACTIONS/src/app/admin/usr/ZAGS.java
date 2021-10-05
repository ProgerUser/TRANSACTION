package app.admin.usr;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ZAGS {
	/** ИД */
	private LongProperty ZAGS_ID;
	/** Город */
	private LongProperty ZAGS_OTD;
	/** Наименование ЗАГСа */
	private StringProperty ZAGS_NAME;
	/** Руководитель */
	private StringProperty ZAGS_RUK;

	public ZAGS() {
		this.ZAGS_ID = new SimpleLongProperty();
		this.ZAGS_OTD = new SimpleLongProperty();
		this.ZAGS_NAME = new SimpleStringProperty();
		this.ZAGS_RUK = new SimpleStringProperty();
	}

	public void setZAGS_ID(Long ZAGS_ID) {
		this.ZAGS_ID.set(ZAGS_ID);
	}

	public void setZAGS_OTD(Long ZAGS_OTD) {
		this.ZAGS_OTD.set(ZAGS_OTD);
	}

	public void setZAGS_NAME(String ZAGS_NAME) {
		this.ZAGS_NAME.set(ZAGS_NAME);
	}

	public void setZAGS_RUK(String ZAGS_RUK) {
		this.ZAGS_RUK.set(ZAGS_RUK);
	}

	public Long getZAGS_ID() {
		return ZAGS_ID.get();
	}

	public Long getZAGS_OTD() {
		return ZAGS_OTD.get();
	}

	public String getZAGS_NAME() {
		return ZAGS_NAME.get();
	}

	public String getZAGS_RUK() {
		return ZAGS_RUK.get();
	}

	public LongProperty ZAGS_IDProperty() {
		return ZAGS_ID;
	}

	public LongProperty ZAGS_OTDProperty() {
		return ZAGS_OTD;
	}

	public StringProperty ZAGS_NAMEProperty() {
		return ZAGS_NAME;
	}

	public StringProperty ZAGS_RUKProperty() {
		return ZAGS_RUK;
	}
}

package app.swift;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VAVAL {
	private StringProperty CVALUE;/* Нет данных */
	private StringProperty CATTR_NAME;/* Нет данных */

	public VAVAL() {
		this.CVALUE = new SimpleStringProperty();
		this.CATTR_NAME = new SimpleStringProperty();
	}

	public void setCVALUE(String CVALUE) {
		this.CVALUE.set(CVALUE);
	}

	public void setCATTR_NAME(String CATTR_NAME) {
		this.CATTR_NAME.set(CATTR_NAME);
	}

	public String getCVALUE() {
		return CVALUE.get();
	}

	public String getCATTR_NAME() {
		return CATTR_NAME.get();
	}

	public StringProperty CVALUEProperty() {
		return CVALUE;
	}

	public StringProperty CATTR_NAMEProperty() {
		return CATTR_NAME;
	}
}

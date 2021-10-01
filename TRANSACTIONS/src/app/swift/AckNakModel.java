package app.swift;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AckNakModel {

	// Declare Employees Table Columns
	private StringProperty IS_ACK_NAK;
	private StringProperty FILENAME;
	private StringProperty SW_REF;
	private StringProperty DATE_TIME;
//	private SimpleObjectProperty<Timestamp> DATE_TIME;

	// Constructor
	public AckNakModel() {
		this.IS_ACK_NAK = new SimpleStringProperty();
		this.FILENAME = new SimpleStringProperty();
		this.SW_REF = new SimpleStringProperty();
		this.DATE_TIME = new SimpleStringProperty();
//		this.DATE_TIME = new SimpleObjectProperty<>();
	}

	// IS_ACK_NAK
	public String getIS_ACK_NAK() {
		return IS_ACK_NAK.get();
	}

	public void setIS_ACK_NAK(String IS_ACK_NAK) {
		this.IS_ACK_NAK.set(IS_ACK_NAK);
	}

	public StringProperty IS_ACK_NAKProperty() {
		return IS_ACK_NAK;
	}

	// FILENAME
	public String getFILENAME() {
		return FILENAME.get();
	}

	public void setFILENAME(String FILENAME) {
		this.FILENAME.set(FILENAME);
	}

	public StringProperty FILENAMEProperty() {
		return FILENAME;
	}

	// SW_REF
	public String getSW_REF() {
		return SW_REF.get();
	}

	public void setSW_REF(String SW_REF) {
		this.SW_REF.set(SW_REF);
	}

	public StringProperty SW_REFProperty() {
		return SW_REF;
	}

	// DATE_TIME
	public String getDATE_TIME() {
		return DATE_TIME.get();
	}

	public void setDATE_TIME(String DATE_TIME) {
		this.DATE_TIME.set(DATE_TIME);
	}

	public StringProperty DATE_TIMEProperty() {
		return DATE_TIME;
	}
//	public Object getDATE_TIME() {
//		return DATE_TIME.get();
//	}
//
//	public void setDATE_TIME(Timestamp DATE_TIME) {
//		this.DATE_TIME.set(DATE_TIME);
//	}
//
//	public SimpleObjectProperty<Timestamp> DATE_TIMEProperty() {
//		return DATE_TIME;
//	}
}

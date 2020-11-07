package app.model;

import javafx.beans.property.*;

public class Attributes {

	private StringProperty Service;
	private StringProperty CheckNumber;
	private StringProperty AttributeName;
	private StringProperty AttributeValue;

	// Constructor
	public Attributes() {
		this.Service = new SimpleStringProperty();
		this.CheckNumber = new SimpleStringProperty();
		this.AttributeName = new SimpleStringProperty();
		this.AttributeValue = new SimpleStringProperty();
	}

	public StringProperty ServiceProperty() {
		return Service;
	}

	public StringProperty CheckNumberProperty() {
		return CheckNumber;
	}

	public StringProperty AttributeNameProperty() {
		return AttributeName;
	}

	public StringProperty AttributeValueProperty() {
		return AttributeValue;
	}

	public void set_Service(String Service) {
		this.Service.set(Service);
	}

	public void set_CheckNumber(String CheckNumber) {
		this.CheckNumber.set(CheckNumber);
	}

	public void set_AttributeName(String AttributeName) {
		this.AttributeName.set(AttributeName);
	}

	public void set_AttributeValue(String AttributeValue) {
		this.AttributeValue.set(AttributeValue);
	}

	public String get_Service() {
		return Service.get();
	}

	public String get_CheckNumber() {
		return CheckNumber.get();
	}

	public String get_AttributeName() {
		return AttributeName.get();
	}

	public String get_AttributeValue() {
		return AttributeValue.get();
	}

}

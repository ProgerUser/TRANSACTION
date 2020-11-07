package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Forms {
	private IntegerProperty ID_FORM;
	private StringProperty FORM_NAME;
	private StringProperty FORMN_DESC;

	/*CONSTRUCTOR*/
	public Forms() {
		this.ID_FORM = new SimpleIntegerProperty();
		this.FORM_NAME = new SimpleStringProperty();
		this.FORMN_DESC = new SimpleStringProperty();
	}

	/*ID_FORM*/
	public int get_ID_FORM() {
		return ID_FORM.get();
	}

	public void set_ID_FORM(int ID_FORM) {
		this.ID_FORM.set(ID_FORM);
	}

	public IntegerProperty ID_FORM_Property() {
		return ID_FORM;
	}

	/*FORM_NAME*/
	public String get_FORM_NAME() {
		return FORM_NAME.get();
	}

	public void set_FORM_NAME(String FORM_NAME) {
		this.FORM_NAME.set(FORM_NAME);
	}

	public StringProperty FORM_NAME_Property() {
		return FORM_NAME;
	}

	/*FORMN_DESC*/
	public String get_FORMN_DESC() {
		return FORMN_DESC.get();
	}

	public void set_FORMN_DESC(String FORMN_DESC) {
		this.FORMN_DESC.set(FORMN_DESC);
	}

	public StringProperty FORMN_DESC_Property() {
		return FORMN_DESC;
	}
}

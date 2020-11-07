package app.model;

import javafx.beans.property.*;

public class TerminalForCombo {
	private StringProperty TERMS;
	// Constructor
	public TerminalForCombo() {
		this.TERMS = new SimpleStringProperty();
	}
	// TERMS
	public String getTERMS() {
		return TERMS.get();
	}

	public void setTERMS(String TERMS) {
		this.TERMS.set(TERMS);
	}

	public StringProperty TERMSProperty() {
		return TERMS;
	}
	
}

package app.model;

import javafx.beans.property.*;

public class BUDCODE {
	private StringProperty code;
	private StringProperty codename;
	// Constructor
	public BUDCODE() {
		this.code = new SimpleStringProperty();
		this.codename = new SimpleStringProperty();
	}
	// code
	public String getcode() {
		return code.get();
	}

	public void setcode(String code) {
		this.code.set(code);
	}

	public StringProperty codeProperty() {
		return code;
	}
	
	// codename
	public String getcodename() {
		return codename.get();
	}

	public void setcodename(String codename) {
		this.codename.set(codename);
	}

	public StringProperty codenameProperty() {
		return codename;
	}
	
}

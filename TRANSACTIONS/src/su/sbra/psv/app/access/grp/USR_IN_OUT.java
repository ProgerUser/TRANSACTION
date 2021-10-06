package su.sbra.psv.app.access.grp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class USR_IN_OUT {
	private StringProperty LOGIN;
	private StringProperty NAME;

	public USR_IN_OUT() {
		this.LOGIN = new SimpleStringProperty();
		this.NAME = new SimpleStringProperty();
	}

	public void setLOGIN(String LOGIN) {
		this.LOGIN.set(LOGIN);
	}

	public void setNAME(String NAME) {
		this.NAME.set(NAME);
	}

	public String getLOGIN() {
		return LOGIN.get();
	}

	public String getNAME() {
		return NAME.get();
	}

	public StringProperty LOGINProperty() {
		return LOGIN;
	}

	public StringProperty NAMEProperty() {
		return NAME;
	}
}

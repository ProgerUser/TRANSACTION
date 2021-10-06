package su.sbra.psv.app.ibank;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ibank2 {
	private IntegerProperty CLIENT_ID;
	private StringProperty NAME_CLN;

	/* CONSTRUCTOR */
	public Ibank2() {
		this.CLIENT_ID = new SimpleIntegerProperty();
		this.NAME_CLN = new SimpleStringProperty();
	}

	/* CLIENT_ID */
	public int get_CLIENT_ID() {
		return CLIENT_ID.get();
	}

	public void set_CLIENT_ID(int CLIENT_ID) {
		this.CLIENT_ID.set(CLIENT_ID);
	}

	public IntegerProperty CLIENT_ID_Property() {
		return CLIENT_ID;
	}

	/* NAME_CLN */
	public String get_NAME_CLN() {
		return NAME_CLN.get();
	}

	public void set_NAME_CLN(String NAME_CLN) {
		this.NAME_CLN.set(NAME_CLN);
	}

	public StringProperty NAME_CLN_Property() {
		return NAME_CLN;
	}

}

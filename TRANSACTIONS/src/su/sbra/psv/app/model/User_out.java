package su.sbra.psv.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User_out {
	private StringProperty USR_ID_O;
	private StringProperty FIO_O;

	/*CONSTRUCTOR*/
	public User_out() {
		this.USR_ID_O = new SimpleStringProperty();
		this.FIO_O = new SimpleStringProperty();
	}
	/*USR_ID_O*/
	public String get_USR_ID_O() {
		return USR_ID_O.get();
	}

	public void set_USR_ID_O(String USR_ID_O) {
		this.USR_ID_O.set(USR_ID_O);
	}

	public StringProperty USR_ID_O_Property() {
		return USR_ID_O;
	}

	/*FIO_O*/
	public String get_FIO_O() {
		return FIO_O.get();
	}

	public void set_FIO_O(String FIO_O) {
		this.FIO_O.set(FIO_O);
	}

	public StringProperty FIO_O_Property() {
		return FIO_O;
	}
}

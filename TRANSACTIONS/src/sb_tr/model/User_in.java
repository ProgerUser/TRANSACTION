package sb_tr.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User_in {
	private StringProperty USR_ID_I;
	private StringProperty FIO_I;
	private StringProperty TYPE_ACCESS_I;

	/*CONSTRUCTOR*/
	public User_in() {
		this.USR_ID_I = new SimpleStringProperty();
		this.FIO_I = new SimpleStringProperty();
		this.TYPE_ACCESS_I = new SimpleStringProperty();
	}
	/*USR_ID_I*/
	public String get_USR_ID_I() {
		return USR_ID_I.get();
	}

	public void set_USR_ID_I(String USR_ID_I) {
		this.USR_ID_I.set(USR_ID_I);
	}

	public StringProperty USR_ID_I_Property() {
		return USR_ID_I;
	}

	/*FIO_I*/
	public String get_FIO_I() {
		return FIO_I.get();
	}

	public void set_FIO_I(String FIO_I) {
		this.FIO_I.set(FIO_I);
	}

	public StringProperty FIO_I_Property() {
		return FIO_I;
	}
	
	/*TYPE_ACCESS_I*/
	public String get_TYPE_ACCESS_I() {
		return TYPE_ACCESS_I.get();
	}

	public void set_TYPE_ACCESS_I(String TYPE_ACCESS_I) {
		this.TYPE_ACCESS_I.set(TYPE_ACCESS_I);
	}

	public StringProperty TYPE_ACCESS_I_Property() {
		return TYPE_ACCESS_I;
	}
}

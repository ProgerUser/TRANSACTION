package sample.model;

import java.sql.Date;

import javafx.beans.property.*;

public class FN_SESS_AMRA {
	private StringProperty sess_id;
	private StringProperty file_name;
	private StringProperty date_time;

	// Constructor
	public FN_SESS_AMRA() {
		this.sess_id = new SimpleStringProperty();
		this.file_name = new SimpleStringProperty();
		this.date_time = new SimpleStringProperty();
	}

	// sess_id
	public String getsess_id() {
		return sess_id.get();
	}

	public void setsess_id(String sess_id) {
		this.sess_id.set(sess_id);
	}

	public StringProperty sess_idProperty() {
		return sess_id;
	}

	// file_name
	public String getfile_name() {
		return file_name.get();
	}

	public void setfile_name(String file_name) {
		this.file_name.set(file_name);
	}

	public StringProperty file_nameProperty() {
		return file_name;
	}

	// date_time
	public String getdate_time() {
		return date_time.get();
	}

	public void setdate_time(String date) {
		this.date_time.set(date);
	}

	public StringProperty date_timeProperty() {
		return date_time;
	}

}

package su.sbra.psv.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FN_SESS_AMRA {
	private StringProperty sess_id;
	private StringProperty file_name;
	private StringProperty date_time;
	private StringProperty date_;
	private StringProperty path_;
	private StringProperty status;
	private StringProperty user;

	// Constructor
	public FN_SESS_AMRA() {
		this.sess_id = new SimpleStringProperty();
		this.file_name = new SimpleStringProperty();
		this.date_time = new SimpleStringProperty();
		this.date_ = new SimpleStringProperty();
		this.status = new SimpleStringProperty();
		this.user = new SimpleStringProperty();
		this.path_ = new SimpleStringProperty();
	}

	public String getuser() {
		return user.get();
	}

	public void setuser(String user) {
		this.user.set(user);
	}
	
	public StringProperty userProperty() {
		return user;
	}
	
	public String getstatus_() {
		return status.get();
	}

	public void setstatus(String status) {
		this.status.set(status);
	}
	
	public StringProperty statusProperty() {
		return status;
	}
	
	
	public String getpath_() {
		return path_.get();
	}

	public void setpath_(String path_) {
		this.path_.set(path_);
	}
	
	public StringProperty path_Property() {
		return path_;
	}
	
	// date
	public String getdate_() {
		return date_.get();
	}

	public void setdate_(String date_) {
		this.date_.set(date_);
	}
	
	public StringProperty date_Property() {
		return date_;
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

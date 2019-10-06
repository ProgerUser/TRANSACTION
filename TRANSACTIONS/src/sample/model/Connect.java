package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Connect {
    
	public static String SESS_ID_;
	
	public static String PNMB_;
	
	public static String connectionURL_;
	public static String userID_;
	public static String userPassword_;
	
	// Declare Employees Table Columns
	private StringProperty connectionURL;
	private StringProperty userID;
	private StringProperty userPassword;

	// Constructor
	public Connect() {
		this.connectionURL = new SimpleStringProperty();
		this.userID = new SimpleStringProperty();
		this.userPassword = new SimpleStringProperty();
	}

	// connectionURL
	public String getconnectionURL() {
		return connectionURL.get();
	}

	public void setconnectionURL(String connectionURL_) {
		this.connectionURL.set(connectionURL_);
	}

	public StringProperty connectionURLProperty() {
		return connectionURL;
	}

	// userID
	public String getuserID() {
		return userID.get();
	}

	public void setuserID(String userID_) {
		this.userID.set(userID_);
	}

	public StringProperty userIDProperty() {
		return userID;
	}

	// userPassword
	public String getuserPassword() {
		return userPassword.get();
	}

	public void setuserPassword(String userPassword_) {
		this.userPassword.set(userPassword_);
	}

	public StringProperty userPasswordProperty() {
		return userPassword;
	}

}

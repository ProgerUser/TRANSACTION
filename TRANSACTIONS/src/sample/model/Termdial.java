package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Termdial {

	private StringProperty department;
	private StringProperty paymentnumber;
	private StringProperty dealstartdate;
	private StringProperty sum_;
	private StringProperty dealenddate;
	private StringProperty dealpaymentnumber;
	private StringProperty status;
	private StringProperty sess_id;
	private StringProperty VECTOR;

	//private SimpleObjectProperty<Timestamp> recdate;
	private StringProperty recdate;
	// Constructor
	public Termdial() {
		//this.recdate = new SimpleObjectProperty<>();
		this.recdate = new SimpleStringProperty();
		this.department = new SimpleStringProperty();
		this.paymentnumber = new SimpleStringProperty();
		this.dealstartdate = new SimpleStringProperty();
		this.sum_ = new SimpleStringProperty();
		this.dealenddate = new SimpleStringProperty();
		this.dealpaymentnumber = new SimpleStringProperty();
		this.status = new SimpleStringProperty();
		this.sess_id = new SimpleStringProperty();
		this.VECTOR = new SimpleStringProperty();

	}

	public Object get_VECTOR() {
		return VECTOR.get();
	}

	public Object get_recdate() {
		return recdate.get();
	}
	
	public String get_department() {
		return department.get();
	}

	public String get_paymentnumber() {
		return paymentnumber.get();
	}

	public String get_dealstartdate() {
		return dealstartdate.get();
	}

	public String get_sum_() {
		return sum_.get();
	}

	public String get_dealenddate() {
		return dealenddate.get();
	}

	public String get_dealpaymentnumber() {
		return dealpaymentnumber.get();
	}

	public String get_status() {
		return status.get();
	}

	public String get_sess_id() {
		return sess_id.get();
	}

	public void set_recdate(String recdate) {
		this.recdate.set(recdate);
	}
	
	/*public void set_recdate(Timestamp recdate) {
		this.recdate.set(recdate);
	}*/

	public void set_VECTOR(String VECTOR) {
		this.VECTOR.set(VECTOR);
	}
	
	public void set_department(String department) {
		this.department.set(department);
	}
	
	public void set_paymentnumber(String paymentnumber) {
		this.paymentnumber.set(paymentnumber);
	}

	public void set_dealstartdate(String dealstartdate) {
		this.dealstartdate.set(dealstartdate);
	}

	public void set_sum_(String sum_) {
		this.sum_.set(sum_);
	}

	public void set_dealenddate(String dealenddate) {
		this.dealenddate.set(dealenddate);
	}

	public void set_dealpaymentnumber(String dealpaymentnumber) {
		this.dealpaymentnumber.set(dealpaymentnumber);
	}

	public void set_status(String status) {
		this.status.set(status);
	}

	public void set_sess_id(String sess_id) {
		this.sess_id.set(sess_id);
	}

	/*public SimpleObjectProperty<Timestamp> recdateProperty() {
		return recdate;
	}*/
	
	public StringProperty VECTORProperty() {
		return VECTOR;
	}
	public StringProperty recdateProperty() {
		return recdate;
	}

	public StringProperty departmentProperty() {
		return department;
	}

	public StringProperty paymentnumberProperty() {
		return paymentnumber;
	}

	public StringProperty dealstartdateProperty() {
		return dealstartdate;
	}

	public StringProperty sum_Property() {
		return sum_;
	}

	public StringProperty dealenddateProperty() {
		return dealenddate;
	}

	public StringProperty dealpaymentnumberProperty() {
		return dealpaymentnumber;
	}

	public StringProperty statusProperty() {
		return status;
	}

	public StringProperty sess_idProperty() {
		return sess_id;
	}

}

package su.sbra.psv.app.model;

import java.sql.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {

//    private String name;
//    private Double price;

	// Declare Employees Table Columns
	private StringProperty acc;
	private DoubleProperty debet;
	private DoubleProperty credit;
	private DoubleProperty d_c;

	private SimpleObjectProperty<Date> date_;

	// Constructor
	public Item() {
		this.acc = new SimpleStringProperty();
		this.debet = new SimpleDoubleProperty();
		this.credit = new SimpleDoubleProperty();
		this.d_c = new SimpleDoubleProperty();
		this.date_ = new SimpleObjectProperty<>();
	}

	// acc
	public String getacc() {
		return acc.get();
	}

	public void setacc(String acc) {
		this.acc.set(acc);
	}

	public StringProperty accProperty() {
		return acc;
	}

	// debet
	public double getdebet() {
		return debet.get();
	}

	public void setdebet(double debet) {
		this.debet.set(debet);
	}

	public DoubleProperty debetProperty() {
		return debet;
	}

	// credit
	public double getcredit() {
		return credit.get();
	}

	public void setcredit(double credit) {
		this.credit.set(credit);
	}

	public DoubleProperty creditProperty() {
		return credit;
	}

	// d_c
	public double getd_c() {
		return d_c.get();
	}

	public void setd_c(double d_c) {
		this.d_c.set(d_c);
	}

	public DoubleProperty d_cProperty() {
		return d_c;
	}

	// date_
	public Object getdate_() {
		return date_.get();
	}

	public void setdate_(Date date_) {
		this.date_.set(date_);
	}

	public SimpleObjectProperty<Date> date_Property() {
		return date_;
	}
}

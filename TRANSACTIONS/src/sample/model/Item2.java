package sample.model;

import java.sql.Date;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item2 {

//    private String name;
//    private Double price;

	// Declare Employees Table Columns
	private StringProperty credit;
	private StringProperty debet;
	private StringProperty ground;
	private StringProperty stat;
	private DoubleProperty summ;

	private SimpleObjectProperty<Date> date_;
	private SimpleObjectProperty<Date> date_reg;

	// Constructor
	public Item2() {
		this.debet = new SimpleStringProperty();
		this.credit = new SimpleStringProperty();
		this.ground = new SimpleStringProperty();
		this.stat = new SimpleStringProperty();
		this.summ = new SimpleDoubleProperty();
		this.date_ = new SimpleObjectProperty<>();
	}

	// credit
	public String getcredit() {
		return credit.get();
	}

	public void setcredit(String credit) {
		this.credit.set(credit);
	}

	public StringProperty creditProperty() {
		return credit;
	}

	// debet
	public String getdebet() {
		return debet.get();
	}

	public void setdebet(String debet) {
		this.debet.set(debet);
	}

	public StringProperty debetProperty() {
		return debet;
	}

	// ground
	public String getground() {
		return ground.get();
	}

	public void setground(String ground) {
		this.ground.set(ground);
	}

	public StringProperty groundProperty() {
		return ground;
	}

	// summ
	public double getsumm() {
		return summ.get();
	}

	public void setsumm(double summ) {
		this.summ.set(summ);
	}

	public DoubleProperty summProperty() {
		return summ;
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
	
	// date_reg
	public Object getdate_reg() {
		return date_reg.get();
	}

	public void setdate_reg(Date date_reg) {
		this.date_reg.set(date_reg);
	}

	public SimpleObjectProperty<Date> date_regProperty() {
		return date_reg;
	}
	
	//stat
	public String getstat() {
		return stat.get();
	}

	public void setstat(String stat) {
		this.stat.set(stat);
	}

	public StringProperty statProperty() {
		return stat;
	}
}

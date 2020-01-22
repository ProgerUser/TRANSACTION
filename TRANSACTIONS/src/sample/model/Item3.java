package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item3 {

	private StringProperty terminal;
	private StringProperty street;
	private StringProperty checknumber;
	private StringProperty datetime;
	private StringProperty kindergarten;
	private StringProperty group;
	private StringProperty period;
	private StringProperty fio;
	private StringProperty number;
	private StringProperty psum;
	private StringProperty fee;


	// Constructor
	public Item3() {
		this.terminal = new SimpleStringProperty();
		this.street = new SimpleStringProperty();
		this.checknumber = new SimpleStringProperty();
		this.datetime = new SimpleStringProperty();
		this.kindergarten = new SimpleStringProperty();
		this.group = new SimpleStringProperty();
		this.period = new SimpleStringProperty();
		this.fio = new SimpleStringProperty();
		this.number = new SimpleStringProperty();
		this.psum = new SimpleStringProperty();
		this.fee = new SimpleStringProperty();
	}
	//terminal
	public String get_terminal() {
		return terminal.get();
	}
	public void set_terminal(String terminal) {
		this.terminal.set(terminal);
	}
	public StringProperty terminal_Property() {
		return terminal;
	}
	
	//street
	public String get_street() {
		return street.get();
	}
	public void set_street(String street) {
		this.street.set(street);
	}
	public StringProperty street_Property() {
		return street;
	}
	//checknumber
	public String get_checknumber() {
		return checknumber.get();
	}
	public void set_checknumber(String checknumber) {
		this.checknumber.set(checknumber);
	}
	public StringProperty checknumber_Property() {
		return checknumber;
	}
	//datetime
	public String get_datetime() {
		return datetime.get();
	}
	public void set_datetime(String datetime) {
		this.datetime.set(datetime);
	}
	public StringProperty datetime_Property() {
		return terminal;
	}
	//kindergarten
	public String get_kindergarten() {
		return kindergarten.get();
	}
	public void set_kindergarten(String kindergarten) {
		this.kindergarten.set(kindergarten);
	}
	public StringProperty kindergarten_Property() {
		return kindergarten;
	}
	//group
	public String get_group() {
		return group.get();
	}
	public void set_group(String group) {
		this.group.set(group);
	}
	public StringProperty group_Property() {
		return group;
	}
	//period
	public String get_period() {
		return period.get();
	}
	public void set_period(String period) {
		this.period.set(period);
	}
	public StringProperty period_Property() {
		return period;
	}
	//fio
	public String get_fio() {
		return fio.get();
	}
	public void set_fio(String fio) {
		this.fio.set(fio);
	}
	public StringProperty fio_Property() {
		return fio;
	}
	//number
	public String get_number() {
		return number.get();
	}
	public void set_number(String number) {
		this.number.set(number);
	}
	public StringProperty number_Property() {
		return terminal;
	}
	//psum
	public String get_psum() {
		return psum.get();
	}
	public void set_psum(String psum) {
		this.psum.set(psum);
	}
	public StringProperty psum_Property() {
		return psum;
	}
	//fee
	public String get_fee() {
		return psum.get();
	}
	public void set_fee(String fee) {
		this.fee.set(fee);
	}
	public StringProperty fee_Property() {
		return fee;
	}
}

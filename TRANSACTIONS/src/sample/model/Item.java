package sample.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {

//    private String name;
//    private Double price;

 // Declare Employees Table Columns
 	private StringProperty acc;
 	private DoubleProperty debet;

 	// Constructor
 	public Item() {
 		this.acc = new SimpleStringProperty();
 		this.debet = new SimpleDoubleProperty();
 	}
 	
 // name
 	public String getacc() {
 		return acc.get();
 	}

 	public void setacc(String acc) {
 		this.acc.set(acc);
 	}

 	public StringProperty accProperty() {
 		return acc;
 	}
 	
 	 // name
 	public double getdebet() {
 		return debet.get();
 	}

 	public void setdebet(double debet) {
 		this.debet.set(debet);
 	}

 	public DoubleProperty debetProperty() {
 		return debet;
 	}
}

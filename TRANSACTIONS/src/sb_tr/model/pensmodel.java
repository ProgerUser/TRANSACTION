package sb_tr.model;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class pensmodel {

	private IntegerProperty id;
	private StringProperty filename;
	private SimpleObjectProperty<LocalDateTime> dateload;
	private StringProperty one_part;
	private StringProperty THREE_PART;
	private StringProperty TWO_PART;
	private StringProperty FOUR_PART;

	// Constructor
	public pensmodel() {
		this.id = new SimpleIntegerProperty();
		this.dateload = new SimpleObjectProperty<>();
		this.one_part = new SimpleStringProperty();
		this.THREE_PART = new SimpleStringProperty();
		this.FOUR_PART = new SimpleStringProperty();
		this.TWO_PART = new SimpleStringProperty();
		this.filename = new SimpleStringProperty();
	}

	// FOUR_PART
	public String getFOUR_PART() {
		return FOUR_PART.get();
	}

	public void setFOUR_PART(String FOUR_PART) {
		this.FOUR_PART.set(FOUR_PART);
	}

	public StringProperty FOUR_PARTProperty() {
		return FOUR_PART;
	}
	
	// THREE_PART
	public String getTHREE_PART() {
		return THREE_PART.get();
	}

	public void setTHREE_PART(String THREE_PART) {
		this.THREE_PART.set(THREE_PART);
	}

	public StringProperty THREE_PARTProperty() {
		return THREE_PART;
	}

	// TWO_PART
	public String getTWO_PART() {
		return TWO_PART.get();
	}

	public void setTWO_PART(String TWO_PART) {
		this.TWO_PART.set(TWO_PART);
	}

	public StringProperty TWO_PARTProperty() {
		return TWO_PART;
	}

	// one_part
	public String getone_part() {
		return one_part.get();
	}

	public void setone_part(String one_part) {
		this.one_part.set(one_part);
	}

	public StringProperty one_partProperty() {
		return one_part;
	}

	// id
	public Integer getid() {
		return id.get();
	}

	public void setid(Integer id) {
		this.id.set(id);
	}

	public IntegerProperty idProperty() {
		return id;
	}

	// filename
	public String getfilename() {
		return filename.get();
	}

	public void setfilename(String filename) {
		this.filename.set(filename);
	}

	public StringProperty filenameProperty() {
		return filename;
	}

	// dateload
	public Object getdateload() {
		return dateload.get();
	}

	public void setdateload(LocalDateTime dateload) {
		this.dateload.set(dateload);
	}

	public SimpleObjectProperty<LocalDateTime> dateloadProperty() {
		return dateload;
	}
}

package app.audit.trigger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ALL_TABLE {
	/** Нет данных */
	private StringProperty TABLE_NAME;
	/** Нет данных */
	private StringProperty TABLECOMMENT;

	public ALL_TABLE() {
		this.TABLE_NAME = new SimpleStringProperty();
		this.TABLECOMMENT = new SimpleStringProperty();
	}

	public void setTABLE_NAME(String TABLE_NAME) {
		this.TABLE_NAME.set(TABLE_NAME);
	}

	public void setTABLECOMMENT(String TABLECOMMENT) {
		this.TABLECOMMENT.set(TABLECOMMENT);
	}

	public String getTABLE_NAME() {
		return TABLE_NAME.get();
	}

	public String getTABLECOMMENT() {
		return TABLECOMMENT.get();
	}

	public StringProperty TABLE_NAMEProperty() {
		return TABLE_NAME;
	}

	public StringProperty TABLECOMMENTProperty() {
		return TABLECOMMENT;
	}
}

package su.sbra.psv.app.audit.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AU_TABLE {
	/** Нет данных */
	private StringProperty CNAME;
	/** Нет данных */
	private StringProperty TABLENAME;

	public AU_TABLE() {
		this.CNAME = new SimpleStringProperty();
		this.TABLENAME = new SimpleStringProperty();
	}

	public void setCNAME(String CNAME) {
		this.CNAME.set(CNAME);
	}

	public void setTABLENAME(String TABLENAME) {
		this.TABLENAME.set(TABLENAME);
	}

	public String getCNAME() {
		return CNAME.get();
	}

	public String getTABLENAME() {
		return TABLENAME.get();
	}

	public StringProperty CNAMEProperty() {
		return CNAME;
	}

	public StringProperty TABLENAMEProperty() {
		return TABLENAME;
	}
}

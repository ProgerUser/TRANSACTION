package su.sbra.psv.app.audit.trigger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AU_TABLE {
	/** Нет данных */
	private StringProperty CNAME;
	/** Нет данных */
	private StringProperty TABLENAME;
	/** Нет данных */
	private StringProperty STATUS;
	/** Нет данных */
	private BooleanProperty CMODE;

	public AU_TABLE() {
		this.CNAME = new SimpleStringProperty();
		this.TABLENAME = new SimpleStringProperty();
		this.STATUS = new SimpleStringProperty();
		this.CMODE = new SimpleBooleanProperty();
	}

	public void setCNAME(String CNAME) {
		this.CNAME.set(CNAME);
	}

	public void setTABLENAME(String TABLENAME) {
		this.TABLENAME.set(TABLENAME);
	}

	public void setSTATUS(String STATUS) {
		this.STATUS.set(STATUS);
	}

	public void setCMODE(Boolean CMODE) {
		this.CMODE.set(CMODE);
	}

	public String getCNAME() {
		return CNAME.get();
	}

	public String getTABLENAME() {
		return TABLENAME.get();
	}

	public String getSTATUS() {
		return STATUS.get();
	}

	public Boolean getCMODE() {
		return CMODE.get();
	}

	public StringProperty CNAMEProperty() {
		return CNAME;
	}

	public StringProperty TABLENAMEProperty() {
		return TABLENAME;
	}

	public StringProperty STATUSProperty() {
		return STATUS;
	}

	public BooleanProperty CMODEProperty() {
		return CMODE;
	}
}

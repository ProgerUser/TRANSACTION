package su.sbra.psv.app.audit.view;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class V_AU_DATA {
	private LongProperty IACTION_ID;
	private StringProperty CFIELD;
	private StringProperty CNEWDATA;
	private StringProperty COLDDATA;

	/* CONSTRUCTOR */
	public V_AU_DATA() {
		this.IACTION_ID = new SimpleLongProperty();
		this.CFIELD = new SimpleStringProperty();
		this.CNEWDATA = new SimpleStringProperty();
		this.COLDDATA = new SimpleStringProperty();
	}

	/* COLDDATA */
	public String getCOLDDATA() {
		return COLDDATA.get();
	}

	public void setCOLDDATA(String COLDDATA) {
		this.COLDDATA.set(COLDDATA);
	}

	public StringProperty COLDDATAProperty() {
		return COLDDATA;
	}

	/* CNEWDATA */
	public String getCNEWDATA() {
		return CNEWDATA.get();
	}

	public void setCNEWDATA(String CNEWDATA) {
		this.CNEWDATA.set(CNEWDATA);
	}

	public StringProperty CNEWDATAProperty() {
		return CNEWDATA;
	}

	/* IACTION_ID */
	public Long getIACTION_ID() {
		return IACTION_ID.get();
	}

	public void setIACTION_ID(Long IACTION_ID) {
		this.IACTION_ID.set(IACTION_ID);
	}

	public LongProperty IACTION_IDProperty() {
		return IACTION_ID;
	}

	/* CFIELD */
	public String getCFIELD() {
		return CFIELD.get();
	}

	public void setCFIELD(String CFIELD) {
		this.CFIELD.set(CFIELD);
	}

	public StringProperty CFIELDProperty() {
		return CFIELD;
	}

}

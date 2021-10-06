package app.report;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class V_AP_REPORT_CAT_PARAM {
	/** Наименование курсора */
	private StringProperty CURSOR_NAME;
	/** Сохранять значение (Y/N) */
	private StringProperty SAVE;
	/** Id курсора */
	private LongProperty CURSOR_ID;
	/** Значение по умолчанию */
	private StringProperty CPARAMDEFAULT;
	/** Наименование */
	private StringProperty CPARAMDESCR;
	/** № */
	private LongProperty IPARAMNUM;
	/** Id типа отчета */
	private LongProperty REPORT_TYPE_ID;
	/** Id отчета */
	private LongProperty REPORT_ID;

	public V_AP_REPORT_CAT_PARAM() {
		this.CURSOR_NAME = new SimpleStringProperty();
		this.SAVE = new SimpleStringProperty();
		this.CURSOR_ID = new SimpleLongProperty();
		this.CPARAMDEFAULT = new SimpleStringProperty();
		this.CPARAMDESCR = new SimpleStringProperty();
		this.IPARAMNUM = new SimpleLongProperty();
		this.REPORT_TYPE_ID = new SimpleLongProperty();
		this.REPORT_ID = new SimpleLongProperty();
	}

	public void setCURSOR_NAME(String CURSOR_NAME) {
		this.CURSOR_NAME.set(CURSOR_NAME);
	}

	public void setSAVE(String SAVE) {
		this.SAVE.set(SAVE);
	}

	public void setCURSOR_ID(Long CURSOR_ID) {
		this.CURSOR_ID.set(CURSOR_ID);
	}

	public void setCPARAMDEFAULT(String CPARAMDEFAULT) {
		this.CPARAMDEFAULT.set(CPARAMDEFAULT);
	}

	public void setCPARAMDESCR(String CPARAMDESCR) {
		this.CPARAMDESCR.set(CPARAMDESCR);
	}

	public void setIPARAMNUM(Long IPARAMNUM) {
		this.IPARAMNUM.set(IPARAMNUM);
	}

	public void setREPORT_TYPE_ID(Long REPORT_TYPE_ID) {
		this.REPORT_TYPE_ID.set(REPORT_TYPE_ID);
	}

	public void setREPORT_ID(Long REPORT_ID) {
		this.REPORT_ID.set(REPORT_ID);
	}

	public String getCURSOR_NAME() {
		return CURSOR_NAME.get();
	}

	public String getSAVE() {
		return SAVE.get();
	}

	public Long getCURSOR_ID() {
		return CURSOR_ID.get();
	}

	public String getCPARAMDEFAULT() {
		return CPARAMDEFAULT.get();
	}

	public String getCPARAMDESCR() {
		return CPARAMDESCR.get();
	}

	public Long getIPARAMNUM() {
		return IPARAMNUM.get();
	}

	public Long getREPORT_TYPE_ID() {
		return REPORT_TYPE_ID.get();
	}

	public Long getREPORT_ID() {
		return REPORT_ID.get();
	}

	public StringProperty CURSOR_NAMEProperty() {
		return CURSOR_NAME;
	}

	public StringProperty SAVEProperty() {
		return SAVE;
	}

	public LongProperty CURSOR_IDProperty() {
		return CURSOR_ID;
	}

	public StringProperty CPARAMDEFAULTProperty() {
		return CPARAMDEFAULT;
	}

	public StringProperty CPARAMDESCRProperty() {
		return CPARAMDESCR;
	}

	public LongProperty IPARAMNUMProperty() {
		return IPARAMNUM;
	}

	public LongProperty REPORT_TYPE_IDProperty() {
		return REPORT_TYPE_ID;
	}

	public LongProperty REPORT_IDProperty() {
		return REPORT_ID;
	}
}

package su.sbra.psv.app.admin.rescron;

import java.time.LocalDate;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VSB_CERT_EXP {
	/** ��� ������ */
	private StringProperty CERTSTAT;
	/** ��� ������ */
	private StringProperty CERTGRP;
	/** ��� ������ */
	private StringProperty CERTRES;
	/** ��� ������ */
	private SimpleObjectProperty<LocalDate> CERTEND;
	/** ��� ������ */
	private SimpleObjectProperty<LocalDate> CERTBEG;
	/** ��� ������ */
	private StringProperty CERTNAME;
	/** ��� ������ */
	private LongProperty CRTID;

	public VSB_CERT_EXP() {
		this.CERTSTAT = new SimpleStringProperty();
		this.CERTGRP = new SimpleStringProperty();
		this.CERTRES = new SimpleStringProperty();
		this.CERTEND = new SimpleObjectProperty<>();
		this.CERTBEG = new SimpleObjectProperty<>();
		this.CERTNAME = new SimpleStringProperty();
		this.CRTID = new SimpleLongProperty();
	}

	public void setCERTSTAT(String CERTSTAT) {
		this.CERTSTAT.set(CERTSTAT);
	}

	public void setCERTGRP(String CERTGRP) {
		this.CERTGRP.set(CERTGRP);
	}

	public void setCERTRES(String CERTRES) {
		this.CERTRES.set(CERTRES);
	}

	public void setCERTEND(LocalDate CERTEND) {
		this.CERTEND.set(CERTEND);
	}

	public void setCERTBEG(LocalDate CERTBEG) {
		this.CERTBEG.set(CERTBEG);
	}

	public void setCERTNAME(String CERTNAME) {
		this.CERTNAME.set(CERTNAME);
	}

	public void setCRTID(Long CRTID) {
		this.CRTID.set(CRTID);
	}

	public String getCERTSTAT() {
		return CERTSTAT.get();
	}

	public String getCERTGRP() {
		return CERTGRP.get();
	}

	public String getCERTRES() {
		return CERTRES.get();
	}

	public LocalDate getCERTEND() {
		return CERTEND.get();
	}

	public LocalDate getCERTBEG() {
		return CERTBEG.get();
	}

	public String getCERTNAME() {
		return CERTNAME.get();
	}

	public Long getCRTID() {
		return CRTID.get();
	}

	public StringProperty CERTSTATProperty() {
		return CERTSTAT;
	}

	public StringProperty CERTGRPProperty() {
		return CERTGRP;
	}

	public StringProperty CERTRESProperty() {
		return CERTRES;
	}

	public SimpleObjectProperty<LocalDate> CERTENDProperty() {
		return CERTEND;
	}

	public SimpleObjectProperty<LocalDate> CERTBEGProperty() {
		return CERTBEG;
	}

	public StringProperty CERTNAMEProperty() {
		return CERTNAME;
	}

	public LongProperty CRTIDProperty() {
		return CRTID;
	}
}

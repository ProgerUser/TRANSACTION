package su.sbra.psv.app.loadamra;

import java.time.LocalDateTime;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrLog {
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> RECDATE;
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> PAYDATE;
	/** Нет данных */
	private StringProperty DESC_;
	/** Нет данных */
	private LongProperty SESS_ID;
	/** Нет данных */
	private StringProperty DEB_CRED;

	public TrLog() {
		this.RECDATE = new SimpleObjectProperty<>();
		this.PAYDATE = new SimpleObjectProperty<>();
		this.DESC_ = new SimpleStringProperty();
		this.SESS_ID = new SimpleLongProperty();
		this.DEB_CRED = new SimpleStringProperty();
	}

	public void setRECDATE(LocalDateTime RECDATE) {
		this.RECDATE.set(RECDATE);
	}

	public void setPAYDATE(LocalDateTime PAYDATE) {
		this.PAYDATE.set(PAYDATE);
	}

	public void setDESC_(String DESC_) {
		this.DESC_.set(DESC_);
	}

	public void setSESS_ID(Long SESS_ID) {
		this.SESS_ID.set(SESS_ID);
	}

	public void setDEB_CRED(String DEB_CRED) {
		this.DEB_CRED.set(DEB_CRED);
	}

	public LocalDateTime getRECDATE() {
		return RECDATE.get();
	}

	public LocalDateTime getPAYDATE() {
		return PAYDATE.get();
	}

	public String getDESC_() {
		return DESC_.get();
	}

	public Long getSESS_ID() {
		return SESS_ID.get();
	}

	public String getDEB_CRED() {
		return DEB_CRED.get();
	}

	public SimpleObjectProperty<LocalDateTime> RECDATEProperty() {
		return RECDATE;
	}

	public SimpleObjectProperty<LocalDateTime> PAYDATEProperty() {
		return PAYDATE;
	}

	public StringProperty DESC_Property() {
		return DESC_;
	}

	public LongProperty SESS_IDProperty() {
		return SESS_ID;
	}

	public StringProperty DEB_CREDProperty() {
		return DEB_CRED;
	}
}

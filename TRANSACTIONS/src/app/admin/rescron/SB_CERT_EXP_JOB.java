package app.admin.rescron;

import java.time.LocalDateTime;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SB_CERT_EXP_JOB {
	/** Нет данных */
	private StringProperty STATUS;
	/** Нет данных */
	private StringProperty OPERATION;
	/** Нет данных */
	private StringProperty JOB_NAME;
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> TM$LOG_DATE;
	/** Нет данных */
	private LongProperty LOG_ID;

	public SB_CERT_EXP_JOB() {
		this.STATUS = new SimpleStringProperty();
		this.OPERATION = new SimpleStringProperty();
		this.JOB_NAME = new SimpleStringProperty();
		this.TM$LOG_DATE = new SimpleObjectProperty<>();
		this.LOG_ID = new SimpleLongProperty();
	}

	public void setSTATUS(String STATUS) {
		this.STATUS.set(STATUS);
	}

	public void setOPERATION(String OPERATION) {
		this.OPERATION.set(OPERATION);
	}

	public void setJOB_NAME(String JOB_NAME) {
		this.JOB_NAME.set(JOB_NAME);
	}

	public void setTM$LOG_DATE(LocalDateTime TM$LOG_DATE) {
		this.TM$LOG_DATE.set(TM$LOG_DATE);
	}

	public void setLOG_ID(Long LOG_ID) {
		this.LOG_ID.set(LOG_ID);
	}

	public String getSTATUS() {
		return STATUS.get();
	}

	public String getOPERATION() {
		return OPERATION.get();
	}

	public String getJOB_NAME() {
		return JOB_NAME.get();
	}

	public LocalDateTime getTM$LOG_DATE() {
		return TM$LOG_DATE.get();
	}

	public Long getLOG_ID() {
		return LOG_ID.get();
	}

	public StringProperty STATUSProperty() {
		return STATUS;
	}

	public StringProperty OPERATIONProperty() {
		return OPERATION;
	}

	public StringProperty JOB_NAMEProperty() {
		return JOB_NAME;
	}

	public SimpleObjectProperty<LocalDateTime> TM$LOG_DATEProperty() {
		return TM$LOG_DATE;
	}

	public LongProperty LOG_IDProperty() {
		return LOG_ID;
	}
}

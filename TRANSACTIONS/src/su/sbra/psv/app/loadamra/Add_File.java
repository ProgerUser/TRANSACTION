package su.sbra.psv.app.loadamra;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Add_File {
	/** Нет данных */
	private LongProperty SESS_ID;
	/** Нет данных */
	private StringProperty FILE_NAME;
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> DATE_TIME;
	/** Нет данных */
	private StringProperty FILECLOB;
	/** Нет данных */
	private StringProperty STATUS;
	/** Нет данных */
	private StringProperty PATH;
	/** Нет данных */
	private StringProperty USER_;
	/** Нет данных */
	private SimpleObjectProperty<LocalDate> FD;

	public Add_File() {
		this.SESS_ID = new SimpleLongProperty();
		this.FILE_NAME = new SimpleStringProperty();
		this.DATE_TIME = new SimpleObjectProperty<>();
		this.FILECLOB = new SimpleStringProperty();
		this.STATUS = new SimpleStringProperty();
		this.PATH = new SimpleStringProperty();
		this.USER_ = new SimpleStringProperty();
		this.FD = new SimpleObjectProperty<>();
	}

	public void setSESS_ID(Long SESS_ID) {
		this.SESS_ID.set(SESS_ID);
	}

	public void setFILE_NAME(String FILE_NAME) {
		this.FILE_NAME.set(FILE_NAME);
	}

	public void setDATE_TIME(LocalDateTime DATE_TIME) {
		this.DATE_TIME.set(DATE_TIME);
	}

	public void setFILECLOB(String FILECLOB) {
		this.FILECLOB.set(FILECLOB);
	}

	public void setSTATUS(String STATUS) {
		this.STATUS.set(STATUS);
	}

	public void setPATH(String PATH) {
		this.PATH.set(PATH);
	}

	public void setUSER_(String USER_) {
		this.USER_.set(USER_);
	}

	public void setFD(LocalDate FD) {
		this.FD.set(FD);
	}

	public Long getSESS_ID() {
		return SESS_ID.get();
	}

	public String getFILE_NAME() {
		return FILE_NAME.get();
	}

	public LocalDateTime getDATE_TIME() {
		return DATE_TIME.get();
	}

	public String getFILECLOB() {
		return FILECLOB.get();
	}

	public String getSTATUS() {
		return STATUS.get();
	}

	public String getPATH() {
		return PATH.get();
	}

	public String getUSER_() {
		return USER_.get();
	}

	public LocalDate getFD() {
		return FD.get();
	}

	public LongProperty SESS_IDProperty() {
		return SESS_ID;
	}

	public StringProperty FILE_NAMEProperty() {
		return FILE_NAME;
	}

	public SimpleObjectProperty<LocalDateTime> DATE_TIMEProperty() {
		return DATE_TIME;
	}

	public StringProperty FILECLOBProperty() {
		return FILECLOB;
	}

	public StringProperty STATUSProperty() {
		return STATUS;
	}

	public StringProperty PATHProperty() {
		return PATH;
	}

	public StringProperty USER_Property() {
		return USER_;
	}

	public SimpleObjectProperty<LocalDate> FDProperty() {
		return FD;
	}
}

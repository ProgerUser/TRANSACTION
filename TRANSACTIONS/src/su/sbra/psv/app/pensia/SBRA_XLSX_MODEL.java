package su.sbra.psv.app.pensia;

import java.time.LocalDate;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SBRA_XLSX_MODEL {
	/** Нет данных */
	private StringProperty SNILS;
	/** Нет данных */
	private StringProperty COLUMN12;
	/** Нет данных */
	private StringProperty ACC_VTB;
	/** Нет данных */
	private StringProperty COLUMN10;
	/** Нет данных */
	private StringProperty COLUMN9;
	/** Нет данных */
	private SimpleObjectProperty<LocalDate> BDATE;
	/** Нет данных */
	private LongProperty SUMM;
	/** Нет данных */
	private StringProperty ACC;
	/** Нет данных */
	private StringProperty COLUMN5;
	/** Нет данных */
	private StringProperty MIDDLE_NAME;
	/** Нет данных */
	private StringProperty FIRST_NAME;
	/** Нет данных */
	private StringProperty LAST_NAME;
	/** Нет данных */
	private LongProperty ROW_NUM;

	public SBRA_XLSX_MODEL() {
		this.SNILS = new SimpleStringProperty();
		this.COLUMN12 = new SimpleStringProperty();
		this.ACC_VTB = new SimpleStringProperty();
		this.COLUMN10 = new SimpleStringProperty();
		this.COLUMN9 = new SimpleStringProperty();
		this.BDATE = new SimpleObjectProperty<>();
		this.SUMM = new SimpleLongProperty();
		this.ACC = new SimpleStringProperty();
		this.COLUMN5 = new SimpleStringProperty();
		this.MIDDLE_NAME = new SimpleStringProperty();
		this.FIRST_NAME = new SimpleStringProperty();
		this.LAST_NAME = new SimpleStringProperty();
		this.ROW_NUM = new SimpleLongProperty();
	}

	public void setSNILS(String SNILS) {
		this.SNILS.set(SNILS);
	}

	public void setCOLUMN12(String COLUMN12) {
		this.COLUMN12.set(COLUMN12);
	}

	public void setACC_VTB(String ACC_VTB) {
		this.ACC_VTB.set(ACC_VTB);
	}

	public void setCOLUMN10(String COLUMN10) {
		this.COLUMN10.set(COLUMN10);
	}

	public void setCOLUMN9(String COLUMN9) {
		this.COLUMN9.set(COLUMN9);
	}

	public void setBDATE(LocalDate BDATE) {
		this.BDATE.set(BDATE);
	}

	public void setSUMM(Long SUMM) {
		this.SUMM.set(SUMM);
	}

	public void setACC(String ACC) {
		this.ACC.set(ACC);
	}

	public void setCOLUMN5(String COLUMN5) {
		this.COLUMN5.set(COLUMN5);
	}

	public void setMIDDLE_NAME(String MIDDLE_NAME) {
		this.MIDDLE_NAME.set(MIDDLE_NAME);
	}

	public void setFIRST_NAME(String FIRST_NAME) {
		this.FIRST_NAME.set(FIRST_NAME);
	}

	public void setLAST_NAME(String LAST_NAME) {
		this.LAST_NAME.set(LAST_NAME);
	}

	public void setROW_NUM(Long ROW_NUM) {
		this.ROW_NUM.set(ROW_NUM);
	}

	public String getSNILS() {
		return SNILS.get();
	}

	public String getCOLUMN12() {
		return COLUMN12.get();
	}

	public String getACC_VTB() {
		return ACC_VTB.get();
	}

	public String getCOLUMN10() {
		return COLUMN10.get();
	}

	public String getCOLUMN9() {
		return COLUMN9.get();
	}

	public LocalDate getBDATE() {
		return BDATE.get();
	}

	public Long getSUMM() {
		return SUMM.get();
	}

	public String getACC() {
		return ACC.get();
	}

	public String getCOLUMN5() {
		return COLUMN5.get();
	}

	public String getMIDDLE_NAME() {
		return MIDDLE_NAME.get();
	}

	public String getFIRST_NAME() {
		return FIRST_NAME.get();
	}

	public String getLAST_NAME() {
		return LAST_NAME.get();
	}

	public Long getROW_NUM() {
		return ROW_NUM.get();
	}

	public StringProperty SNILSProperty() {
		return SNILS;
	}

	public StringProperty COLUMN12Property() {
		return COLUMN12;
	}

	public StringProperty ACC_VTBProperty() {
		return ACC_VTB;
	}

	public StringProperty COLUMN10Property() {
		return COLUMN10;
	}

	public StringProperty COLUMN9Property() {
		return COLUMN9;
	}

	public SimpleObjectProperty<LocalDate> BDATEProperty() {
		return BDATE;
	}

	public LongProperty SUMMProperty() {
		return SUMM;
	}

	public StringProperty ACCProperty() {
		return ACC;
	}

	public StringProperty COLUMN5Property() {
		return COLUMN5;
	}

	public StringProperty MIDDLE_NAMEProperty() {
		return MIDDLE_NAME;
	}

	public StringProperty FIRST_NAMEProperty() {
		return FIRST_NAME;
	}

	public StringProperty LAST_NAMEProperty() {
		return LAST_NAME;
	}

	public LongProperty ROW_NUMProperty() {
		return ROW_NUM;
	}
}

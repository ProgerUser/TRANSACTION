package su.sbra.psv.app.pensia;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import java.time.LocalDate;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PENS_ROW {
	/** ��� ������ */
	private StringProperty CCUSSNILS;
	/** ��� ������ */
	private SimpleObjectProperty<LocalDate> ABS_BDATE;
	/** ��� ������ */
	private StringProperty SNILS;
	/** ��� ������ */
	private StringProperty COLUMN12;
	/** ��� ������ */
	private StringProperty ACC_VTB;
	/** ��� ������ */
	private StringProperty COLUMN10;
	/** ��� ������ */
	private StringProperty COLUMN9;
	/** ��� ������ */
	private StringProperty BDATE;
	/** ��� ������ */
	private DoubleProperty D$SUMM;
	/** ��� ������ */
	private StringProperty ACC;
	/** ��� ������ */
	private StringProperty COLUMN5;
	/** ��� ������ */
	private StringProperty MIDDLE_NAME;
	/** ��� ������ */
	private StringProperty FIRST_NAME;
	/** ��� ������ */
	private StringProperty LAST_NAME;
	/** ��� ������ */
	private LongProperty ROW_NUM;
	/** ��� ������ */
	private LongProperty PART;

	public PENS_ROW() {
		this.CCUSSNILS = new SimpleStringProperty();
		this.ABS_BDATE = new SimpleObjectProperty<>();
		this.SNILS = new SimpleStringProperty();
		this.COLUMN12 = new SimpleStringProperty();
		this.ACC_VTB = new SimpleStringProperty();
		this.COLUMN10 = new SimpleStringProperty();
		this.COLUMN9 = new SimpleStringProperty();
		this.BDATE = new SimpleStringProperty();
		this.D$SUMM = new SimpleDoubleProperty();
		this.ACC = new SimpleStringProperty();
		this.COLUMN5 = new SimpleStringProperty();
		this.MIDDLE_NAME = new SimpleStringProperty();
		this.FIRST_NAME = new SimpleStringProperty();
		this.LAST_NAME = new SimpleStringProperty();
		this.ROW_NUM = new SimpleLongProperty();
		this.PART = new SimpleLongProperty();
	}

	public void setCCUSSNILS(String CCUSSNILS) {
		this.CCUSSNILS.set(CCUSSNILS);
	}

	public void setABS_BDATE(LocalDate ABS_BDATE) {
		this.ABS_BDATE.set(ABS_BDATE);
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

	public void setBDATE(String BDATE) {
		this.BDATE.set(BDATE);
	}

	public void setD$SUMM(Double D$SUMM) {
		this.D$SUMM.set(D$SUMM);
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

	public void setPART(Long PART) {
		this.PART.set(PART);
	}

	public String getCCUSSNILS() {
		return CCUSSNILS.get();
	}

	public LocalDate getABS_BDATE() {
		return ABS_BDATE.get();
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

	public String getBDATE() {
		return BDATE.get();
	}

	public Double getD$SUMM() {
		return D$SUMM.get();
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

	public Long getPART() {
		return PART.get();
	}

	public StringProperty CCUSSNILSProperty() {
		return CCUSSNILS;
	}

	public SimpleObjectProperty<LocalDate> ABS_BDATEProperty() {
		return ABS_BDATE;
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

	public StringProperty BDATEProperty() {
		return BDATE;
	}

	public DoubleProperty D$SUMMProperty() {
		return D$SUMM;
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

	public LongProperty PARTProperty() {
		return PART;
	}
}

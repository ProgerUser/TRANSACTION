package su.sbra.psv.app.swift;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BIK_TO_SW_VTB {
	private StringProperty CTRNCORACCA;/* Нет данных */
	private StringProperty CTRNCORACCO;/* Нет данных */
	private IntegerProperty MTRNSUM;/* Нет данных */
	private StringProperty CTRNACCC;/* Нет данных */
	private StringProperty CTRNACCD;/* Нет данных */
	private SimpleObjectProperty<LocalDate> DTRNTRAN;/* Нет данных */
	private SimpleObjectProperty<LocalDate> DTRNCREATE;/* Нет данных */
	private StringProperty SW_TRN;/* Нет данных */
	private StringProperty BIK_TRN;/* Нет данных */

	public BIK_TO_SW_VTB() {
		this.CTRNCORACCA = new SimpleStringProperty();
		this.CTRNCORACCO = new SimpleStringProperty();
		this.MTRNSUM = new SimpleIntegerProperty();
		this.CTRNACCC = new SimpleStringProperty();
		this.CTRNACCD = new SimpleStringProperty();
		this.DTRNTRAN = new SimpleObjectProperty<>();
		this.DTRNCREATE = new SimpleObjectProperty<>();
		this.SW_TRN = new SimpleStringProperty();
		this.BIK_TRN = new SimpleStringProperty();
	}

	public void setCTRNCORACCA(String CTRNCORACCA) {
		this.CTRNCORACCA.set(CTRNCORACCA);
	}

	public void setCTRNCORACCO(String CTRNCORACCO) {
		this.CTRNCORACCO.set(CTRNCORACCO);
	}

	public void setMTRNSUM(Integer MTRNSUM) {
		this.MTRNSUM.set(MTRNSUM);
	}

	public void setCTRNACCC(String CTRNACCC) {
		this.CTRNACCC.set(CTRNACCC);
	}

	public void setCTRNACCD(String CTRNACCD) {
		this.CTRNACCD.set(CTRNACCD);
	}

	public void setDTRNTRAN(LocalDate DTRNTRAN) {
		this.DTRNTRAN.set(DTRNTRAN);
	}

	public void setDTRNCREATE(LocalDate DTRNCREATE) {
		this.DTRNCREATE.set(DTRNCREATE);
	}

	public void setSW_TRN(String SW_TRN) {
		this.SW_TRN.set(SW_TRN);
	}

	public void setBIK_TRN(String BIK_TRN) {
		this.BIK_TRN.set(BIK_TRN);
	}

	public String getCTRNCORACCA() {
		return CTRNCORACCA.get();
	}

	public String getCTRNCORACCO() {
		return CTRNCORACCO.get();
	}

	public Integer getMTRNSUM() {
		return MTRNSUM.get();
	}

	public String getCTRNACCC() {
		return CTRNACCC.get();
	}

	public String getCTRNACCD() {
		return CTRNACCD.get();
	}

	public Object getDTRNTRAN() {
		return DTRNTRAN.get();
	}

	public Object getDTRNCREATE() {
		return DTRNCREATE.get();
	}

	public String getSW_TRN() {
		return SW_TRN.get();
	}

	public String getBIK_TRN() {
		return BIK_TRN.get();
	}

	public StringProperty CTRNCORACCAProperty() {
		return CTRNCORACCA;
	}

	public StringProperty CTRNCORACCOProperty() {
		return CTRNCORACCO;
	}

	public IntegerProperty MTRNSUMProperty() {
		return MTRNSUM;
	}

	public StringProperty CTRNACCCProperty() {
		return CTRNACCC;
	}

	public StringProperty CTRNACCDProperty() {
		return CTRNACCD;
	}

	public SimpleObjectProperty<LocalDate> DTRNTRANProperty() {
		return DTRNTRAN;
	}

	public SimpleObjectProperty<LocalDate> DTRNCREATEProperty() {
		return DTRNCREATE;
	}

	public StringProperty SW_TRNProperty() {
		return SW_TRN;
	}

	public StringProperty BIK_TRNProperty() {
		return BIK_TRN;
	}
}

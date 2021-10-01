package app.swift;

import java.time.LocalDateTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VTB_MT202_CONV {
	private IntegerProperty TRN_ANUM;/* Ссылка на трн */
	private LongProperty TRN_NUM;/* Ссылка на трн */
	private StringProperty REF;/* Референс */
	private StringProperty F21;/* ... */
	private DoubleProperty FL32A_SUM;/* ... */
	private StringProperty FL32A_CUR;/* ... */
	private StringProperty FL32A_DATE;/* ... */
	private StringProperty F53B;/* ... */
	private StringProperty F58A;/* ... */
	private StringProperty F72;/* 72 поле */
	private IntegerProperty ID;/* ИД */
	private SimpleObjectProperty<LocalDateTime> DATETIME;/* дата и время */
	private StringProperty OPER;/* пользователь */
	private StringProperty FL58A_DETAIL;/* ... */

	public VTB_MT202_CONV() {
		this.TRN_ANUM = new SimpleIntegerProperty();
		this.TRN_NUM = new SimpleLongProperty();
		this.REF = new SimpleStringProperty();
		this.F21 = new SimpleStringProperty();
		this.FL32A_SUM = new SimpleDoubleProperty();
		this.FL32A_CUR = new SimpleStringProperty();
		this.FL32A_DATE = new SimpleStringProperty();
		this.F53B = new SimpleStringProperty();
		this.F58A = new SimpleStringProperty();
		this.F72 = new SimpleStringProperty();
		this.ID = new SimpleIntegerProperty();
		this.DATETIME = new SimpleObjectProperty<>();
		this.OPER = new SimpleStringProperty();
		this.FL58A_DETAIL = new SimpleStringProperty();
	}

	public void setTRN_ANUM(Integer TRN_ANUM) {
		this.TRN_ANUM.set(TRN_ANUM);
	}

	public void setTRN_NUM(Long TRN_NUM) {
		this.TRN_NUM.set(TRN_NUM);
	}

	public void setREF(String REF) {
		this.REF.set(REF);
	}

	public void setF21(String F21) {
		this.F21.set(F21);
	}

	public void setFL32A_SUM(Double FL32A_SUM) {
		this.FL32A_SUM.set(FL32A_SUM);
	}

	public void setFL32A_CUR(String FL32A_CUR) {
		this.FL32A_CUR.set(FL32A_CUR);
	}

	public void setFL32A_DATE(String FL32A_DATE) {
		this.FL32A_DATE.set(FL32A_DATE);
	}

	public void setF53B(String F53B) {
		this.F53B.set(F53B);
	}

	public void setF58A(String F58A) {
		this.F58A.set(F58A);
	}

	public void setF72(String F72) {
		this.F72.set(F72);
	}

	public void setID(Integer ID) {
		this.ID.set(ID);
	}

	public void setDATETIME(LocalDateTime DATETIME) {
		this.DATETIME.set(DATETIME);
	}

	public void setOPER(String OPER) {
		this.OPER.set(OPER);
	}

	public void setFL58A_DETAIL(String FL58A_DETAIL) {
		this.FL58A_DETAIL.set(FL58A_DETAIL);
	}

	public Integer getTRN_ANUM() {
		return TRN_ANUM.get();
	}

	public Long getTRN_NUM() {
		return TRN_NUM.get();
	}

	public String getREF() {
		return REF.get();
	}

	public String getF21() {
		return F21.get();
	}

	public Double getFL32A_SUM() {
		return FL32A_SUM.get();
	}

	public String getFL32A_CUR() {
		return FL32A_CUR.get();
	}

	public String getFL32A_DATE() {
		return FL32A_DATE.get();
	}

	public String getF53B() {
		return F53B.get();
	}

	public String getF58A() {
		return F58A.get();
	}

	public String getF72() {
		return F72.get();
	}

	public Integer getID() {
		return ID.get();
	}

	public Object getDATETIME() {
		return DATETIME.get();
	}

	public String getOPER() {
		return OPER.get();
	}

	public String getFL58A_DETAIL() {
		return FL58A_DETAIL.get();
	}

	public IntegerProperty TRN_ANUMProperty() {
		return TRN_ANUM;
	}

	public LongProperty TRN_NUMProperty() {
		return TRN_NUM;
	}

	public StringProperty REFProperty() {
		return REF;
	}

	public StringProperty F21Property() {
		return F21;
	}

	public DoubleProperty FL32A_SUMProperty() {
		return FL32A_SUM;
	}

	public StringProperty FL32A_CURProperty() {
		return FL32A_CUR;
	}

	public StringProperty FL32A_DATEProperty() {
		return FL32A_DATE;
	}

	public StringProperty F53BProperty() {
		return F53B;
	}

	public StringProperty F58AProperty() {
		return F58A;
	}

	public StringProperty F72Property() {
		return F72;
	}

	public IntegerProperty IDProperty() {
		return ID;
	}

	public SimpleObjectProperty<LocalDateTime> DATETIMEProperty() {
		return DATETIME;
	}

	public StringProperty OPERProperty() {
		return OPER;
	}

	public StringProperty FL58A_DETAILProperty() {
		return FL58A_DETAIL;
	}
}

package swift;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SWIFT_FILES {
	private BooleanProperty CHK;/* Сумма */
	private DoubleProperty SUMM;/* Сумма */
	private StringProperty VECTOR;/* Направление */
	private StringProperty CUR;/* Валюта */
	private StringProperty MTNAME;/* Название MT */
	private StringProperty MTTYPE;/* Тип MT */
	private SimpleObjectProperty<LocalDateTime> CR_DT;/* ДАТА ЗАГРУЗКИ */
	private SimpleObjectProperty<LocalDate> DOCDATE;/* Дата документа */
	private StringProperty OPER;/* ЮЗЕР */
	private SimpleObjectProperty<LocalDateTime> DT_CH;/* ДАТА ИЗМЕНЕНИЯ */
	private StringProperty FILENAME;/* НАЗВАНИЕ ФАЙЛА */
	private StringProperty PATH;/* Путь */
	private IntegerProperty ID;/* ID */
	private StringProperty REF;/* Референс */
	private StringProperty STATUS;/* STATUS */

	public SWIFT_FILES() {
		this.STATUS = new SimpleStringProperty();
		
		this.REF = new SimpleStringProperty();
		this.PATH = new SimpleStringProperty();
		this.CHK = new SimpleBooleanProperty();
		this.SUMM = new SimpleDoubleProperty();
		this.VECTOR = new SimpleStringProperty();
		this.CUR = new SimpleStringProperty();
		this.MTNAME = new SimpleStringProperty();
		this.MTTYPE = new SimpleStringProperty();
		this.CR_DT = new SimpleObjectProperty<>();
		this.OPER = new SimpleStringProperty();
		this.DT_CH = new SimpleObjectProperty<>();
		this.FILENAME = new SimpleStringProperty();
		this.ID = new SimpleIntegerProperty();
		this.DOCDATE = new SimpleObjectProperty<>();
	}

	public void setSTATUS(String STATUS) {
		this.STATUS.set(STATUS);
	}

	public String getSTATUS() {
		return STATUS.get();
	}

	public StringProperty STATUSProperty() {
		return STATUS;
	}
	
	public void setREF(String STATUS) {
		this.STATUS.set(STATUS);
	}

	public String getREF() {
		return REF.get();
	}

	public StringProperty REFProperty() {
		return REF;
	}

	public void setPATH(String PATH) {
		this.PATH.set(PATH);
	}

	public String getPATH() {
		return PATH.get();
	}

	public void setDOCDATE(LocalDate DOCDATE) {
		this.DOCDATE.set(DOCDATE);
	}

	public Object getDOCDATE() {
		return DOCDATE.get();
	}

	public SimpleObjectProperty<LocalDate> DOCDATEProperty() {
		return DOCDATE;
	}

	public void setCHK(Boolean CHK) {
		this.CHK.set(CHK);
	}

	public void setSUMM(Double SUMM) {
		this.SUMM.set(SUMM);
	}

	public void setVECTOR(String VECTOR) {
		this.VECTOR.set(VECTOR);
	}

	public void setCUR(String CUR) {
		this.CUR.set(CUR);
	}

	public void setMTNAME(String MTNAME) {
		this.MTNAME.set(MTNAME);
	}

	public void setMTTYPE(String MTTYPE) {
		this.MTTYPE.set(MTTYPE);
	}

	public void setCR_DT(LocalDateTime CR_DT) {
		this.CR_DT.set(CR_DT);
	}

	public void setOPER(String OPER) {
		this.OPER.set(OPER);
	}

	public void setDT_CH(LocalDateTime DT_CH) {
		this.DT_CH.set(DT_CH);
	}

	public void setFILENAME(String FILENAME) {
		this.FILENAME.set(FILENAME);
	}

	public void setID(Integer ID) {
		this.ID.set(ID);
	}

	public Double getSUMM() {
		return SUMM.get();
	}

	public String getVECTOR() {
		return VECTOR.get();
	}

	public String getCUR() {
		return CUR.get();
	}

	public Boolean getCHK() {
		return CHK.get();
	}

	public String getMTNAME() {
		return MTNAME.get();
	}

	public String getMTTYPE() {
		return MTTYPE.get();
	}

	public Object getCR_DT() {
		return CR_DT.get();
	}

	public String getOPER() {
		return OPER.get();
	}

	public Object getDT_CH() {
		return DT_CH.get();
	}

	public String getFILENAME() {
		return FILENAME.get();
	}

	public Integer getID() {
		return ID.get();
	}

	public DoubleProperty SUMMProperty() {
		return SUMM;
	}

	public StringProperty VECTORProperty() {
		return VECTOR;
	}

	public StringProperty CURProperty() {
		return CUR;
	}

	public StringProperty MTNAMEProperty() {
		return MTNAME;
	}

	public StringProperty MTTYPEProperty() {
		return MTTYPE;
	}

	public BooleanProperty CHKProperty() {
		return CHK;
	}

	public SimpleObjectProperty<LocalDateTime> CR_DTProperty() {
		return CR_DT;
	}

	public StringProperty OPERProperty() {
		return OPER;
	}

	public SimpleObjectProperty<LocalDateTime> DT_CHProperty() {
		return DT_CH;
	}

	public StringProperty FILENAMEProperty() {
		return FILENAME;
	}

	public IntegerProperty IDProperty() {
		return ID;
	}
}

package swift;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SWIFT_FILES {
	private BooleanProperty CHK;/* —ÛÏÏ‡ */
	private StringProperty SUMM;/* —ÛÏÏ‡ */
	private StringProperty VECTOR;/* Õ‡Ô‡‚ÎÂÌËÂ */
	private StringProperty CUR;/* ¬‡Î˛Ú‡ */
	private StringProperty MTNAME;/* Õ‡Á‚‡ÌËÂ MT */
	private StringProperty MTTYPE;/* “ËÔ MT */
	private SimpleObjectProperty<LocalDateTime> CR_DT;/* ƒ¿“¿ «¿√–”« » */
	private SimpleObjectProperty<LocalDate> DOCDATE;/* ƒ‡Ú‡ ‰ÓÍÛÏÂÌÚ‡ */
	private StringProperty OPER;/* ﬁ«≈– */
	private SimpleObjectProperty<LocalDateTime> DT_CH;/* ƒ¿“¿ »«Ã≈Õ≈Õ»ﬂ */
	private StringProperty FILENAME;/* Õ¿«¬¿Õ»≈ ‘¿…À¿ */
	private StringProperty PATH;/* œÛÚ¸ */
	private IntegerProperty ID;/* ID */

	public SWIFT_FILES() {
		
		this.PATH = new SimpleStringProperty();
		this.CHK = new SimpleBooleanProperty();
		this.SUMM = new SimpleStringProperty();
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

	public void setSUMM(String SUMM) {
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

	public String getSUMM() {
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

	public StringProperty SUMMProperty() {
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

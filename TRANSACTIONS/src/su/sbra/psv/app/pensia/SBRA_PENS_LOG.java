package su.sbra.psv.app.pensia;

import java.time.LocalDateTime;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SBRA_PENS_LOG {
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> TM$TIME_;
	/** Нет данных */
	private LongProperty ROW_NO;
	/** Нет данных */
	private LongProperty DP_DOC_ID;
	/** Нет данных */
	private LongProperty ID_;
	/** Нет данных */
	private StringProperty ERR;
	/** Нет данных */
	private StringProperty CSTR;
	/** Нет данных */
	private LongProperty NSTR;
	/** Нет данных */
	private StringProperty TIME_;
	/** Нет данных */
	private StringProperty F_STR;

	public SBRA_PENS_LOG() {
		this.TM$TIME_ = new SimpleObjectProperty<>();
		this.ROW_NO = new SimpleLongProperty();
		this.DP_DOC_ID = new SimpleLongProperty();
		this.ID_ = new SimpleLongProperty();
		this.ERR = new SimpleStringProperty();
		this.CSTR = new SimpleStringProperty();
		this.NSTR = new SimpleLongProperty();
		this.TIME_ = new SimpleStringProperty();
		this.F_STR = new SimpleStringProperty();
	}

	public void setTM$TIME_(LocalDateTime TM$TIME_) {
		this.TM$TIME_.set(TM$TIME_);
	}

	public void setROW_NO(Long ROW_NO) {
		this.ROW_NO.set(ROW_NO);
	}

	public void setDP_DOC_ID(Long DP_DOC_ID) {
		this.DP_DOC_ID.set(DP_DOC_ID);
	}

	public void setID_(Long ID_) {
		this.ID_.set(ID_);
	}

	public void setERR(String ERR) {
		this.ERR.set(ERR);
	}

	public void setCSTR(String CSTR) {
		this.CSTR.set(CSTR);
	}

	public void setNSTR(Long NSTR) {
		this.NSTR.set(NSTR);
	}

	public void setTIME_(String TIME_) {
		this.TIME_.set(TIME_);
	}

	public void setF_STR(String F_STR) {
		this.F_STR.set(F_STR);
	}

	public LocalDateTime getTM$TIME_() {
		return TM$TIME_.get();
	}

	public Long getROW_NO() {
		return ROW_NO.get();
	}

	public Long getDP_DOC_ID() {
		return DP_DOC_ID.get();
	}

	public Long getID_() {
		return ID_.get();
	}

	public String getERR() {
		return ERR.get();
	}

	public String getCSTR() {
		return CSTR.get();
	}

	public Long getNSTR() {
		return NSTR.get();
	}

	public String getTIME_() {
		return TIME_.get();
	}

	public String getF_STR() {
		return F_STR.get();
	}

	public SimpleObjectProperty<LocalDateTime> TM$TIME_Property() {
		return TM$TIME_;
	}

	public LongProperty ROW_NOProperty() {
		return ROW_NO;
	}

	public LongProperty DP_DOC_IDProperty() {
		return DP_DOC_ID;
	}

	public LongProperty ID_Property() {
		return ID_;
	}

	public StringProperty ERRProperty() {
		return ERR;
	}

	public StringProperty CSTRProperty() {
		return CSTR;
	}

	public LongProperty NSTRProperty() {
		return NSTR;
	}

	public StringProperty TIME_Property() {
		return TIME_;
	}

	public StringProperty F_STRProperty() {
		return F_STR;
	}
}

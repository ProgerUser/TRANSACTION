package su.sbra.psv.app.admin.log;

import java.time.LocalDateTime;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SU_SBRA_ADMIN_LOG {
	/** Пользователь */
	private StringProperty OPER;
	/** Время создания */
	private SimpleObjectProperty<LocalDateTime> LOGDATE;
	/** Название метода */
	private StringProperty METHODNAME;
	/** Ошибка */
	private StringProperty ERROR;
	/** Название класса */
	private StringProperty CLASSNAME;
	/** Номер строки */
	private LongProperty LINENUMBER;

	public SU_SBRA_ADMIN_LOG() {
		this.OPER = new SimpleStringProperty();
		this.LOGDATE = new SimpleObjectProperty<>();
		this.METHODNAME = new SimpleStringProperty();
		this.ERROR = new SimpleStringProperty();
		this.CLASSNAME = new SimpleStringProperty();
		this.LINENUMBER = new SimpleLongProperty();
	}

	public void setOPER(String OPER) {
		this.OPER.set(OPER);
	}

	public void setLOGDATE(LocalDateTime LOGDATE) {
		this.LOGDATE.set(LOGDATE);
	}

	public void setMETHODNAME(String METHODNAME) {
		this.METHODNAME.set(METHODNAME);
	}

	public void setERROR(String ERROR) {
		this.ERROR.set(ERROR);
	}

	public void setCLASSNAME(String CLASSNAME) {
		this.CLASSNAME.set(CLASSNAME);
	}

	public void setLINENUMBER(Long LINENUMBER) {
		this.LINENUMBER.set(LINENUMBER);
	}

	public String getOPER() {
		return OPER.get();
	}

	public LocalDateTime getLOGDATE() {
		return LOGDATE.get();
	}

	public String getMETHODNAME() {
		return METHODNAME.get();
	}

	public String getERROR() {
		return ERROR.get();
	}

	public String getCLASSNAME() {
		return CLASSNAME.get();
	}

	public Long getLINENUMBER() {
		return LINENUMBER.get();
	}

	public StringProperty OPERProperty() {
		return OPER;
	}

	public SimpleObjectProperty<LocalDateTime> LOGDATEProperty() {
		return LOGDATE;
	}

	public StringProperty METHODNAMEProperty() {
		return METHODNAME;
	}

	public StringProperty ERRORProperty() {
		return ERROR;
	}

	public StringProperty CLASSNAMEProperty() {
		return CLASSNAME;
	}

	public LongProperty LINENUMBERProperty() {
		return LINENUMBER;
	}
}

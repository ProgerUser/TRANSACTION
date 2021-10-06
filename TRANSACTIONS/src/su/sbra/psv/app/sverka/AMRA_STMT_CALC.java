package su.sbra.psv.app.sverka;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AMRA_STMT_CALC {
	private StringProperty OPER;/* Пользователь */
	private DoubleProperty END_REST;/* Конечный Остаток */
	private DoubleProperty DEB_OB;/* Всего Списано */
	private DoubleProperty CRED_OB;/* Всего Поступило */
	private DoubleProperty BEGIN_REST;/* Начальный Остаток */
	private StringProperty CH_ACCOUNT;/* Расчетный счет */
	private SimpleObjectProperty<LocalDate> STMT_END;/* Дата конца выписки */
	private SimpleObjectProperty<LocalDate> STMT_BEGIN;/* Дата начала выписки */
	private SimpleObjectProperty<LocalDateTime> CREATION_DATETIME;/* Дата/Время создания */
	private SimpleObjectProperty<LocalDateTime> LOAD_DATE;/* Дата загрузки выписки */
	private IntegerProperty ID;/* ИД выписки */
	private StringProperty STATUS;/* Статус */

	public AMRA_STMT_CALC() {
		this.STATUS = new SimpleStringProperty();
		this.OPER = new SimpleStringProperty();
		this.END_REST = new SimpleDoubleProperty();
		this.DEB_OB = new SimpleDoubleProperty();
		this.CRED_OB = new SimpleDoubleProperty();
		this.BEGIN_REST = new SimpleDoubleProperty();
		this.CH_ACCOUNT = new SimpleStringProperty();
		this.STMT_END = new SimpleObjectProperty<>();
		this.STMT_BEGIN = new SimpleObjectProperty<>();
		this.CREATION_DATETIME = new SimpleObjectProperty<>();
		this.LOAD_DATE = new SimpleObjectProperty<>();
		this.ID = new SimpleIntegerProperty();
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

	public void setOPER(String OPER) {
		this.OPER.set(OPER);
	}

	public void setEND_REST(Double END_REST) {
		this.END_REST.set(END_REST);
	}

	public void setDEB_OB(Double DEB_OB) {
		this.DEB_OB.set(DEB_OB);
	}

	public void setCRED_OB(Double CRED_OB) {
		this.CRED_OB.set(CRED_OB);
	}

	public void setBEGIN_REST(Double BEGIN_REST) {
		this.BEGIN_REST.set(BEGIN_REST);
	}

	public void setCH_ACCOUNT(String CH_ACCOUNT) {
		this.CH_ACCOUNT.set(CH_ACCOUNT);
	}

	public void setSTMT_END(LocalDate STMT_END) {
		this.STMT_END.set(STMT_END);
	}

	public void setSTMT_BEGIN(LocalDate STMT_BEGIN) {
		this.STMT_BEGIN.set(STMT_BEGIN);
	}

	public void setCREATION_DATETIME(LocalDateTime CREATION_DATETIME) {
		this.CREATION_DATETIME.set(CREATION_DATETIME);
	}

	public void setLOAD_DATE(LocalDateTime LOAD_DATE) {
		this.LOAD_DATE.set(LOAD_DATE);
	}

	public void setID(Integer ID) {
		this.ID.set(ID);
	}

	public String getOPER() {
		return OPER.get();
	}

	public Double getEND_REST() {
		return END_REST.get();
	}

	public Double getDEB_OB() {
		return DEB_OB.get();
	}

	public Double getCRED_OB() {
		return CRED_OB.get();
	}

	public Double getBEGIN_REST() {
		return BEGIN_REST.get();
	}

	public String getCH_ACCOUNT() {
		return CH_ACCOUNT.get();
	}

	public Object getSTMT_END() {
		return STMT_END.get();
	}

	public Object getSTMT_BEGIN() {
		return STMT_BEGIN.get();
	}

	public Object getCREATION_DATETIME() {
		return CREATION_DATETIME.get();
	}

	public Object getLOAD_DATE() {
		return LOAD_DATE.get();
	}

	public Integer getID() {
		return ID.get();
	}

	public StringProperty OPERProperty() {
		return OPER;
	}

	public DoubleProperty END_RESTProperty() {
		return END_REST;
	}

	public DoubleProperty DEB_OBProperty() {
		return DEB_OB;
	}

	public DoubleProperty CRED_OBProperty() {
		return CRED_OB;
	}

	public DoubleProperty BEGIN_RESTProperty() {
		return BEGIN_REST;
	}

	public StringProperty CH_ACCOUNTProperty() {
		return CH_ACCOUNT;
	}

	public SimpleObjectProperty<LocalDate> STMT_ENDProperty() {
		return STMT_END;
	}

	public SimpleObjectProperty<LocalDate> STMT_BEGINProperty() {
		return STMT_BEGIN;
	}

	public SimpleObjectProperty<LocalDateTime> CREATION_DATETIMEProperty() {
		return CREATION_DATETIME;
	}

	public SimpleObjectProperty<LocalDateTime> LOAD_DATEProperty() {
		return LOAD_DATE;
	}

	public IntegerProperty IDProperty() {
		return ID;
	}
}

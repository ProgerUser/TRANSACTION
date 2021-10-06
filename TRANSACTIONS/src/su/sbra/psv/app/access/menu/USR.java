package su.sbra.psv.app.access.menu;


import java.time.LocalDate;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class USR {
	/** ID ЗАГСА */
	private LongProperty ZAGS_ID;
	/** Начало рабочего времени */
	private SimpleObjectProperty<LocalDate> WORKDAY_TIME_BEGIN;
	/** Окончание рабочего времени */
	private SimpleObjectProperty<LocalDate> WORKDAY_TIME_END;
	/** Сокращенная должность */
	private StringProperty SHORT_POSITION;
	/** Пользователь должен сменить пароль при входе (Y/N) */
	private StringProperty MUST_CHANGE_PASSWORD;
	/** Информация блокировки */
	private StringProperty LOCK_INFO;
	/** Дата/время блокировки */
	private SimpleObjectProperty<LocalDate> LOCK_DATE_TIME;
	/** Сокращенное ФИО */
	private StringProperty SHORT_NAME;
	/** Приветственное сообщение */
	private StringProperty WELCOME_MESSAGE;
	/** Минимальное количество специальных символов в пароле */
	private LongProperty IUSRSPEC_QUANTITY;
	/** Использовать старый пароль после N новых */
	private LongProperty IUSRPWDREUSE;
	/** Y если пользователь может открывать сессии только с одного терминала */
	private StringProperty CRESTRICT_TERM;
	/** Адрес электронной почты */
	private StringProperty CEMAIL;
	/** Окончание разрешения коннекта */
	private SimpleObjectProperty<LocalDate> TWRTEND;
	/** Начало разрешения коннекта */
	private SimpleObjectProperty<LocalDate> TWRTSTART;
	/** Номер внутреннего телефона */
	private StringProperty CUSROFFPHONE;
	/** Срок действия пароля в днях */
	private LongProperty IUSREXP_DAYS;
	/** Минимальное количество цифровых символов в пароле */
	private LongProperty IUSRNUM_QUANTITY;
	/** Минимальное количество буквенных символов в пароле */
	private LongProperty IUSRCHR_QUANTITY;
	/** Минимальная длина пароля */
	private LongProperty IUSRPWD_LENGTH;
	/** Дата увольнения */
	private SimpleObjectProperty<LocalDate> DUSRFIRE;
	/** Филиал ( ссылка на iOTDnum ) */
	private LongProperty IUSRBRANCH;
	/** Дата поступления на работу */
	private SimpleObjectProperty<LocalDate> DUSRHIRE;
	/** Должность */
	private StringProperty CUSRPOSITION;
	/** ФИО */
	private StringProperty CUSRNAME;
	/** LogName */
	private StringProperty CUSRLOGNAME;
	/** Идентификатор пользователя */
	private LongProperty IUSRID;
	/** Учреждение, NOT, ZAG,ADM */
	private StringProperty ACCESS_LEVEL;
	/** ID Нотариуса */
	private LongProperty NOTARY_ID;
	/** ФИО короткое */
	private StringProperty FIO_SH;
	/** ФИО короткое на Абх */
	private StringProperty FIO_ABH_SH;
	/** ФИО на абх */
	private StringProperty FIO_ABH;

	public USR() {
		this.FIO_SH = new SimpleStringProperty();
		this.FIO_ABH_SH = new SimpleStringProperty();
		this.FIO_ABH = new SimpleStringProperty();

		this.ZAGS_ID = new SimpleLongProperty();
		this.WORKDAY_TIME_BEGIN = new SimpleObjectProperty<>();
		this.WORKDAY_TIME_END = new SimpleObjectProperty<>();
		this.SHORT_POSITION = new SimpleStringProperty();
		this.MUST_CHANGE_PASSWORD = new SimpleStringProperty();
		this.LOCK_INFO = new SimpleStringProperty();
		this.LOCK_DATE_TIME = new SimpleObjectProperty<>();
		this.SHORT_NAME = new SimpleStringProperty();
		this.WELCOME_MESSAGE = new SimpleStringProperty();
		this.IUSRSPEC_QUANTITY = new SimpleLongProperty();
		this.IUSRPWDREUSE = new SimpleLongProperty();
		this.CRESTRICT_TERM = new SimpleStringProperty();
		this.CEMAIL = new SimpleStringProperty();
		this.TWRTEND = new SimpleObjectProperty<>();
		this.TWRTSTART = new SimpleObjectProperty<>();
		this.CUSROFFPHONE = new SimpleStringProperty();
		this.IUSREXP_DAYS = new SimpleLongProperty();
		this.IUSRNUM_QUANTITY = new SimpleLongProperty();
		this.IUSRCHR_QUANTITY = new SimpleLongProperty();
		this.IUSRPWD_LENGTH = new SimpleLongProperty();
		this.DUSRFIRE = new SimpleObjectProperty<>();
		this.IUSRBRANCH = new SimpleLongProperty();
		this.DUSRHIRE = new SimpleObjectProperty<>();
		this.CUSRPOSITION = new SimpleStringProperty();
		this.CUSRNAME = new SimpleStringProperty();
		this.CUSRLOGNAME = new SimpleStringProperty();
		this.IUSRID = new SimpleLongProperty();
		this.ACCESS_LEVEL = new SimpleStringProperty();
		this.NOTARY_ID = new SimpleLongProperty();
	}

	public void setFIO_SH(String FIO_SH) {
		this.FIO_SH.set(FIO_SH);
	}

	public void setFIO_ABH_SH(String FIO_ABH_SH) {
		this.FIO_ABH_SH.set(FIO_ABH_SH);
	}

	public void setFIO_ABH(String FIO_ABH) {
		this.FIO_ABH.set(FIO_ABH);
	}

	public String getFIO_SH() {
		return FIO_SH.get();
	}

	public String getFIO_ABH_SH() {
		return FIO_ABH_SH.get();
	}

	public String getFIO_ABH() {
		return FIO_ABH.get();
	}

	public StringProperty FIO_SHProperty() {
		return FIO_SH;
	}

	public StringProperty FIO_ABH_SHProperty() {
		return FIO_ABH_SH;
	}

	public StringProperty FIO_ABHProperty() {
		return FIO_ABH;
	}

	public void setZAGS_ID(Long ZAGS_ID) {
		this.ZAGS_ID.set(ZAGS_ID);
	}

	public void setWORKDAY_TIME_BEGIN(LocalDate WORKDAY_TIME_BEGIN) {
		this.WORKDAY_TIME_BEGIN.set(WORKDAY_TIME_BEGIN);
	}

	public void setWORKDAY_TIME_END(LocalDate WORKDAY_TIME_END) {
		this.WORKDAY_TIME_END.set(WORKDAY_TIME_END);
	}

	public void setSHORT_POSITION(String SHORT_POSITION) {
		this.SHORT_POSITION.set(SHORT_POSITION);
	}

	public void setMUST_CHANGE_PASSWORD(String MUST_CHANGE_PASSWORD) {
		this.MUST_CHANGE_PASSWORD.set(MUST_CHANGE_PASSWORD);
	}

	public void setLOCK_INFO(String LOCK_INFO) {
		this.LOCK_INFO.set(LOCK_INFO);
	}

	public void setLOCK_DATE_TIME(LocalDate LOCK_DATE_TIME) {
		this.LOCK_DATE_TIME.set(LOCK_DATE_TIME);
	}

	public void setSHORT_NAME(String SHORT_NAME) {
		this.SHORT_NAME.set(SHORT_NAME);
	}

	public void setWELCOME_MESSAGE(String WELCOME_MESSAGE) {
		this.WELCOME_MESSAGE.set(WELCOME_MESSAGE);
	}

	public void setIUSRSPEC_QUANTITY(Long IUSRSPEC_QUANTITY) {
		this.IUSRSPEC_QUANTITY.set(IUSRSPEC_QUANTITY);
	}

	public void setIUSRPWDREUSE(Long IUSRPWDREUSE) {
		this.IUSRPWDREUSE.set(IUSRPWDREUSE);
	}

	public void setCRESTRICT_TERM(String CRESTRICT_TERM) {
		this.CRESTRICT_TERM.set(CRESTRICT_TERM);
	}

	public void setCEMAIL(String CEMAIL) {
		this.CEMAIL.set(CEMAIL);
	}

	public void setTWRTEND(LocalDate TWRTEND) {
		this.TWRTEND.set(TWRTEND);
	}

	public void setTWRTSTART(LocalDate TWRTSTART) {
		this.TWRTSTART.set(TWRTSTART);
	}

	public void setCUSROFFPHONE(String CUSROFFPHONE) {
		this.CUSROFFPHONE.set(CUSROFFPHONE);
	}

	public void setIUSREXP_DAYS(Long IUSREXP_DAYS) {
		this.IUSREXP_DAYS.set(IUSREXP_DAYS);
	}

	public void setIUSRNUM_QUANTITY(Long IUSRNUM_QUANTITY) {
		this.IUSRNUM_QUANTITY.set(IUSRNUM_QUANTITY);
	}

	public void setIUSRCHR_QUANTITY(Long IUSRCHR_QUANTITY) {
		this.IUSRCHR_QUANTITY.set(IUSRCHR_QUANTITY);
	}

	public void setIUSRPWD_LENGTH(Long IUSRPWD_LENGTH) {
		this.IUSRPWD_LENGTH.set(IUSRPWD_LENGTH);
	}

	public void setDUSRFIRE(LocalDate DUSRFIRE) {
		this.DUSRFIRE.set(DUSRFIRE);
	}

	public void setIUSRBRANCH(Long IUSRBRANCH) {
		this.IUSRBRANCH.set(IUSRBRANCH);
	}

	public void setDUSRHIRE(LocalDate DUSRHIRE) {
		this.DUSRHIRE.set(DUSRHIRE);
	}

	public void setCUSRPOSITION(String CUSRPOSITION) {
		this.CUSRPOSITION.set(CUSRPOSITION);
	}

	public void setCUSRNAME(String CUSRNAME) {
		this.CUSRNAME.set(CUSRNAME);
	}

	public void setCUSRLOGNAME(String CUSRLOGNAME) {
		this.CUSRLOGNAME.set(CUSRLOGNAME);
	}

	public void setIUSRID(Long l) {
		this.IUSRID.set(l);
	}

	public void setACCESS_LEVEL(String ACCESS_LEVEL) {
		this.ACCESS_LEVEL.set(ACCESS_LEVEL);
	}

	public void setNOTARY_ID(Long NOTARY_ID) {
		this.NOTARY_ID.set(NOTARY_ID);
	}

	public Long getZAGS_ID() {
		return ZAGS_ID.get();
	}

	public LocalDate getWORKDAY_TIME_BEGIN() {
		return WORKDAY_TIME_BEGIN.get();
	}

	public LocalDate getWORKDAY_TIME_END() {
		return WORKDAY_TIME_END.get();
	}

	public String getSHORT_POSITION() {
		return SHORT_POSITION.get();
	}

	public String getMUST_CHANGE_PASSWORD() {
		return MUST_CHANGE_PASSWORD.get();
	}

	public String getLOCK_INFO() {
		return LOCK_INFO.get();
	}

	public LocalDate getLOCK_DATE_TIME() {
		return LOCK_DATE_TIME.get();
	}

	public String getSHORT_NAME() {
		return SHORT_NAME.get();
	}

	public String getWELCOME_MESSAGE() {
		return WELCOME_MESSAGE.get();
	}

	public Long getIUSRSPEC_QUANTITY() {
		return IUSRSPEC_QUANTITY.get();
	}

	public Long getIUSRPWDREUSE() {
		return IUSRPWDREUSE.get();
	}

	public String getCRESTRICT_TERM() {
		return CRESTRICT_TERM.get();
	}

	public String getCEMAIL() {
		return CEMAIL.get();
	}

	public LocalDate getTWRTEND() {
		return TWRTEND.get();
	}

	public LocalDate getTWRTSTART() {
		return TWRTSTART.get();
	}

	public String getCUSROFFPHONE() {
		return CUSROFFPHONE.get();
	}

	public Long getIUSREXP_DAYS() {
		return IUSREXP_DAYS.get();
	}

	public Long getIUSRNUM_QUANTITY() {
		return IUSRNUM_QUANTITY.get();
	}

	public Long getIUSRCHR_QUANTITY() {
		return IUSRCHR_QUANTITY.get();
	}

	public Long getIUSRPWD_LENGTH() {
		return IUSRPWD_LENGTH.get();
	}

	public LocalDate getDUSRFIRE() {
		return DUSRFIRE.get();
	}

	public Long getIUSRBRANCH() {
		return IUSRBRANCH.get();
	}

	public LocalDate getDUSRHIRE() {
		return DUSRHIRE.get();
	}

	public String getCUSRPOSITION() {
		return CUSRPOSITION.get();
	}

	public String getCUSRNAME() {
		return CUSRNAME.get();
	}

	public String getCUSRLOGNAME() {
		return CUSRLOGNAME.get();
	}

	public Long getIUSRID() {
		return IUSRID.get();
	}

	public String getACCESS_LEVEL() {
		return ACCESS_LEVEL.get();
	}

	public Long getNOTARY_ID() {
		return NOTARY_ID.get();
	}

	public LongProperty ZAGS_IDProperty() {
		return ZAGS_ID;
	}

	public SimpleObjectProperty<LocalDate> WORKDAY_TIME_BEGINProperty() {
		return WORKDAY_TIME_BEGIN;
	}

	public SimpleObjectProperty<LocalDate> WORKDAY_TIME_ENDProperty() {
		return WORKDAY_TIME_END;
	}

	public StringProperty SHORT_POSITIONProperty() {
		return SHORT_POSITION;
	}

	public StringProperty MUST_CHANGE_PASSWORDProperty() {
		return MUST_CHANGE_PASSWORD;
	}

	public StringProperty LOCK_INFOProperty() {
		return LOCK_INFO;
	}

	public SimpleObjectProperty<LocalDate> LOCK_DATE_TIMEProperty() {
		return LOCK_DATE_TIME;
	}

	public StringProperty SHORT_NAMEProperty() {
		return SHORT_NAME;
	}

	public StringProperty WELCOME_MESSAGEProperty() {
		return WELCOME_MESSAGE;
	}

	public LongProperty IUSRSPEC_QUANTITYProperty() {
		return IUSRSPEC_QUANTITY;
	}

	public LongProperty IUSRPWDREUSEProperty() {
		return IUSRPWDREUSE;
	}

	public StringProperty CRESTRICT_TERMProperty() {
		return CRESTRICT_TERM;
	}

	public StringProperty CEMAILProperty() {
		return CEMAIL;
	}

	public SimpleObjectProperty<LocalDate> TWRTENDProperty() {
		return TWRTEND;
	}

	public SimpleObjectProperty<LocalDate> TWRTSTARTProperty() {
		return TWRTSTART;
	}

	public StringProperty CUSROFFPHONEProperty() {
		return CUSROFFPHONE;
	}

	public LongProperty IUSREXP_DAYSProperty() {
		return IUSREXP_DAYS;
	}

	public LongProperty IUSRNUM_QUANTITYProperty() {
		return IUSRNUM_QUANTITY;
	}

	public LongProperty IUSRCHR_QUANTITYProperty() {
		return IUSRCHR_QUANTITY;
	}

	public LongProperty IUSRPWD_LENGTHProperty() {
		return IUSRPWD_LENGTH;
	}

	public SimpleObjectProperty<LocalDate> DUSRFIREProperty() {
		return DUSRFIRE;
	}

	public LongProperty IUSRBRANCHProperty() {
		return IUSRBRANCH;
	}

	public SimpleObjectProperty<LocalDate> DUSRHIREProperty() {
		return DUSRHIRE;
	}

	public StringProperty CUSRPOSITIONProperty() {
		return CUSRPOSITION;
	}

	public StringProperty CUSRNAMEProperty() {
		return CUSRNAME;
	}

	public StringProperty CUSRLOGNAMEProperty() {
		return CUSRLOGNAME;
	}

	public LongProperty IUSRIDProperty() {
		return IUSRID;
	}

	public StringProperty ACCESS_LEVELProperty() {
		return ACCESS_LEVEL;
	}

	public LongProperty NOTARY_IDProperty() {
		return NOTARY_ID;
	}
}

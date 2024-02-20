package su.sbra.psv.app.tsppos;

import java.time.LocalDate;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SBRA_TSP_POS {
	/** Нет данных */
	private StringProperty TERM_TYPE_S;
	/** Нет данных */
	private LongProperty TERM_KTM;
	/** Нет данных */
	private StringProperty CLIACC;
	/** Нет данных */
	private StringProperty CLINAME;
	/** Нет данных */
	private StringProperty TERM_IPIFNOTSIM;
	/** Нет данных */
	private StringProperty TERM_PORTHOST;
	/** Нет данных */
	private StringProperty TERM_GEO;
	/** Нет данных */
	private LongProperty TERM_TYPE;
	/** Нет данных */
	private StringProperty TERM_COMMENT;
	/** Нет данных */
	private SimpleObjectProperty<LocalDate> TERM_REGDATE;
	/** Нет данных */
	private StringProperty TERM_SIM_IP;
	/** Нет данных */
	private StringProperty TERM_SIM_OPER;
	/** Нет данных */
	private StringProperty TERM_SIM_NUMBER;
	/** Нет данных */
	private StringProperty TERM_ID;
	/** Нет данных */
	private StringProperty TERM_SERIAL;
	/** Нет данных */
	private LongProperty TERM_INTEGRATION;
	/** Нет данных */
	private StringProperty TERM_ADDR;
	/** Нет данных */
	private StringProperty TERM_MODEL;
	/** Нет данных */
	private LongProperty ID;

	public SBRA_TSP_POS() {
		this.TERM_TYPE_S = new SimpleStringProperty();
		this.TERM_KTM = new SimpleLongProperty();
		this.CLIACC = new SimpleStringProperty();
		this.CLINAME = new SimpleStringProperty();
		this.TERM_IPIFNOTSIM = new SimpleStringProperty();
		this.TERM_PORTHOST = new SimpleStringProperty();
		this.TERM_GEO = new SimpleStringProperty();
		this.TERM_TYPE = new SimpleLongProperty();
		this.TERM_COMMENT = new SimpleStringProperty();
		this.TERM_REGDATE = new SimpleObjectProperty<>();
		this.TERM_SIM_IP = new SimpleStringProperty();
		this.TERM_SIM_OPER = new SimpleStringProperty();
		this.TERM_SIM_NUMBER = new SimpleStringProperty();
		this.TERM_ID = new SimpleStringProperty();
		this.TERM_SERIAL = new SimpleStringProperty();
		this.TERM_INTEGRATION = new SimpleLongProperty();
		this.TERM_ADDR = new SimpleStringProperty();
		this.TERM_MODEL = new SimpleStringProperty();
		this.ID = new SimpleLongProperty();
	}

	public void setTERM_TYPE_S(String TERM_TYPE_S) {
		this.TERM_TYPE_S.set(TERM_TYPE_S);
	}

	public void setTERM_KTM(Long TERM_KTM) {
		this.TERM_KTM.set(TERM_KTM);
	}

	public void setCLIACC(String CLIACC) {
		this.CLIACC.set(CLIACC);
	}

	public void setCLINAME(String CLINAME) {
		this.CLINAME.set(CLINAME);
	}

	public void setTERM_IPIFNOTSIM(String TERM_IPIFNOTSIM) {
		this.TERM_IPIFNOTSIM.set(TERM_IPIFNOTSIM);
	}

	public void setTERM_PORTHOST(String TERM_PORTHOST) {
		this.TERM_PORTHOST.set(TERM_PORTHOST);
	}

	public void setTERM_GEO(String TERM_GEO) {
		this.TERM_GEO.set(TERM_GEO);
	}

	public void setTERM_TYPE(Long TERM_TYPE) {
		this.TERM_TYPE.set(TERM_TYPE);
	}

	public void setTERM_COMMENT(String TERM_COMMENT) {
		this.TERM_COMMENT.set(TERM_COMMENT);
	}

	public void setTERM_REGDATE(LocalDate TERM_REGDATE) {
		this.TERM_REGDATE.set(TERM_REGDATE);
	}

	public void setTERM_SIM_IP(String TERM_SIM_IP) {
		this.TERM_SIM_IP.set(TERM_SIM_IP);
	}

	public void setTERM_SIM_OPER(String TERM_SIM_OPER) {
		this.TERM_SIM_OPER.set(TERM_SIM_OPER);
	}

	public void setTERM_SIM_NUMBER(String TERM_SIM_NUMBER) {
		this.TERM_SIM_NUMBER.set(TERM_SIM_NUMBER);
	}

	public void setTERM_ID(String TERM_ID) {
		this.TERM_ID.set(TERM_ID);
	}

	public void setTERM_SERIAL(String TERM_SERIAL) {
		this.TERM_SERIAL.set(TERM_SERIAL);
	}

	public void setTERM_INTEGRATION(Long TERM_INTEGRATION) {
		this.TERM_INTEGRATION.set(TERM_INTEGRATION);
	}

	public void setTERM_ADDR(String TERM_ADDR) {
		this.TERM_ADDR.set(TERM_ADDR);
	}

	public void setTERM_MODEL(String TERM_MODEL) {
		this.TERM_MODEL.set(TERM_MODEL);
	}

	public void setID(Long ID) {
		this.ID.set(ID);
	}

	public String getTERM_TYPE_S() {
		return TERM_TYPE_S.get();
	}

	public Long getTERM_KTM() {
		return TERM_KTM.get();
	}

	public String getCLIACC() {
		return CLIACC.get();
	}

	public String getCLINAME() {
		return CLINAME.get();
	}

	public String getTERM_IPIFNOTSIM() {
		return TERM_IPIFNOTSIM.get();
	}

	public String getTERM_PORTHOST() {
		return TERM_PORTHOST.get();
	}

	public String getTERM_GEO() {
		return TERM_GEO.get();
	}

	public Long getTERM_TYPE() {
		return TERM_TYPE.get();
	}

	public String getTERM_COMMENT() {
		return TERM_COMMENT.get();
	}

	public LocalDate getTERM_REGDATE() {
		return TERM_REGDATE.get();
	}

	public String getTERM_SIM_IP() {
		return TERM_SIM_IP.get();
	}

	public String getTERM_SIM_OPER() {
		return TERM_SIM_OPER.get();
	}

	public String getTERM_SIM_NUMBER() {
		return TERM_SIM_NUMBER.get();
	}

	public String getTERM_ID() {
		return TERM_ID.get();
	}

	public String getTERM_SERIAL() {
		return TERM_SERIAL.get();
	}

	public Long getTERM_INTEGRATION() {
		return TERM_INTEGRATION.get();
	}

	public String getTERM_ADDR() {
		return TERM_ADDR.get();
	}

	public String getTERM_MODEL() {
		return TERM_MODEL.get();
	}

	public Long getID() {
		return ID.get();
	}

	public StringProperty TERM_TYPE_SProperty() {
		return TERM_TYPE_S;
	}

	public LongProperty TERM_KTMProperty() {
		return TERM_KTM;
	}

	public StringProperty CLIACCProperty() {
		return CLIACC;
	}

	public StringProperty CLINAMEProperty() {
		return CLINAME;
	}

	public StringProperty TERM_IPIFNOTSIMProperty() {
		return TERM_IPIFNOTSIM;
	}

	public StringProperty TERM_PORTHOSTProperty() {
		return TERM_PORTHOST;
	}

	public StringProperty TERM_GEOProperty() {
		return TERM_GEO;
	}

	public LongProperty TERM_TYPEProperty() {
		return TERM_TYPE;
	}

	public StringProperty TERM_COMMENTProperty() {
		return TERM_COMMENT;
	}

	public SimpleObjectProperty<LocalDate> TERM_REGDATEProperty() {
		return TERM_REGDATE;
	}

	public StringProperty TERM_SIM_IPProperty() {
		return TERM_SIM_IP;
	}

	public StringProperty TERM_SIM_OPERProperty() {
		return TERM_SIM_OPER;
	}

	public StringProperty TERM_SIM_NUMBERProperty() {
		return TERM_SIM_NUMBER;
	}

	public StringProperty TERM_IDProperty() {
		return TERM_ID;
	}

	public StringProperty TERM_SERIALProperty() {
		return TERM_SERIAL;
	}

	public LongProperty TERM_INTEGRATIONProperty() {
		return TERM_INTEGRATION;
	}

	public StringProperty TERM_ADDRProperty() {
		return TERM_ADDR;
	}

	public StringProperty TERM_MODELProperty() {
		return TERM_MODEL;
	}

	public LongProperty IDProperty() {
		return ID;
	}
}

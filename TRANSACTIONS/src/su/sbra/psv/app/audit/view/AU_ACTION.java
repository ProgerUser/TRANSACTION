package su.sbra.psv.app.audit.view;

import java.time.LocalDateTime;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AU_ACTION {
	private LongProperty IACTION_ID;
	private SimpleObjectProperty<LocalDateTime> DAUDDATE;
	private StringProperty CAUDUSER;
	private StringProperty CTABLE;
	private StringProperty CAUDMACHINE;
	private StringProperty CAUDPROGRAM;
	private StringProperty CAUDOPERATION;
	private StringProperty RROWID;
	private StringProperty CAUDACTION;
	private StringProperty CAUDMODULE;
	private LongProperty IAUDSESSION;
	private StringProperty CAUDIP_ADDRESS;
	private StringProperty ID_NUM;
	private StringProperty ID_ANUM;

	/* CONSTRUCTOR */
	public AU_ACTION() {
		this.IACTION_ID = new SimpleLongProperty();
		this.DAUDDATE = new SimpleObjectProperty<>();
		this.CAUDUSER = new SimpleStringProperty();
		this.CTABLE = new SimpleStringProperty();
		this.CAUDMACHINE = new SimpleStringProperty();
		this.CAUDPROGRAM = new SimpleStringProperty();
		this.CAUDOPERATION = new SimpleStringProperty();
		this.RROWID = new SimpleStringProperty();
		this.CAUDACTION = new SimpleStringProperty();
		this.CAUDMODULE = new SimpleStringProperty();
		this.IAUDSESSION = new SimpleLongProperty();
		this.CAUDIP_ADDRESS = new SimpleStringProperty();
		this.ID_NUM = new SimpleStringProperty();
		this.ID_ANUM = new SimpleStringProperty();
	}

	/* ID_ANUM */
	public String getID_ANUM() {
		return ID_ANUM.get();
	}

	public void setID_ANUM(String ID_ANUM) {
		this.ID_ANUM.set(ID_ANUM);
	}

	public StringProperty ID_ANUMProperty() {
		return ID_ANUM;
	}

	/* ID_NUM */
	public String getID_NUM() {
		return ID_NUM.get();
	}

	public void setID_NUM(String ID_NUM) {
		this.ID_NUM.set(ID_NUM);
	}

	public StringProperty ID_NUMProperty() {
		return ID_NUM;
	}

	/* CAUDIP_ADDRESS */
	public String getCAUDIP_ADDRESS() {
		return CAUDIP_ADDRESS.get();
	}

	public void setCAUDIP_ADDRESS(String CAUDIP_ADDRESS) {
		this.CAUDIP_ADDRESS.set(CAUDIP_ADDRESS);
	}

	public StringProperty CAUDIP_ADDRESSProperty() {
		return CAUDIP_ADDRESS;
	}

	/* IAUDSESSION */
	public Long getIAUDSESSION() {
		return IAUDSESSION.get();
	}

	public void setIAUDSESSION(Long IAUDSESSION) {
		this.IAUDSESSION.set(IAUDSESSION);
	}

	public LongProperty IAUDSESSIONProperty() {
		return IAUDSESSION;
	}

	/* CAUDMODULE */
	public String getCAUDMODULE() {
		return CAUDMODULE.get();
	}

	public void setCAUDMODULE(String CAUDMODULE) {
		this.CAUDMODULE.set(CAUDMODULE);
	}

	public StringProperty CAUDMODULEProperty() {
		return CAUDMODULE;
	}

	/* CAUDACTION */
	public String getCAUDACTION() {
		return CAUDACTION.get();
	}

	public void setCAUDACTION(String CAUDACTION) {
		this.CAUDACTION.set(CAUDACTION);
	}

	public StringProperty CAUDACTIONProperty() {
		return CAUDACTION;
	}

	/* RROWID */
	public String getRROWID() {
		return RROWID.get();
	}

	public void setRROWID(String RROWID) {
		this.RROWID.set(RROWID);
	}

	public StringProperty RROWIDProperty() {
		return RROWID;
	}

	/* CAUDOPERATION */
	public String getCAUDOPERATION() {
		return CAUDOPERATION.get();
	}

	public void setCAUDOPERATION(String CAUDOPERATION) {
		this.CAUDOPERATION.set(CAUDOPERATION);
	}

	public StringProperty CAUDOPERATIONProperty() {
		return CAUDOPERATION;
	}

	/* CAUDPROGRAM */
	public String getCAUDPROGRAM() {
		return CAUDPROGRAM.get();
	}

	public void setCAUDPROGRAM(String CAUDPROGRAM) {
		this.CAUDPROGRAM.set(CAUDPROGRAM);
	}

	public StringProperty CAUDPROGRAMProperty() {
		return CAUDPROGRAM;
	}

	/* CAUDMACHINE */
	public String getCAUDMACHINE() {
		return CAUDMACHINE.get();
	}

	public void setCAUDMACHINE(String CAUDMACHINE) {
		this.CAUDMACHINE.set(CAUDMACHINE);
	}

	public StringProperty CAUDMACHINEProperty() {
		return CAUDMACHINE;
	}

	/* CTABLE */
	public String getCTABLE() {
		return CTABLE.get();
	}

	public void setCTABLE(String CTABLE) {
		this.CTABLE.set(CTABLE);
	}

	public StringProperty CTABLEProperty() {
		return CTABLE;
	}

	/* IACTION_ID */
	public Long getIACTION_ID() {
		return IACTION_ID.get();
	}

	public void setIACTION_ID(Long IACTION_ID) {
		this.IACTION_ID.set(IACTION_ID);
	}

	public LongProperty IACTION_IDProperty() {
		return IACTION_ID;
	}

	/* DAUDDATE */
	public Object getDAUDDATE() {
		return DAUDDATE.get();
	}

	public void setDAUDDATE(LocalDateTime DAUDDATE) {
		this.DAUDDATE.set(DAUDDATE);
	}

	public SimpleObjectProperty<LocalDateTime> DAUDDATEProperty() {
		return DAUDDATE;
	}

	/* CAUDUSER */
	public String getCAUDUSER() {
		return CAUDUSER.get();
	}

	public void setCAUDUSER(String CAUDUSER) {
		this.CAUDUSER.set(CAUDUSER);
	}

	public StringProperty CAUDUSERProperty() {
		return CAUDUSER;
	}
}

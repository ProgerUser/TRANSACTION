package su.sbra.psv.app.model;

import javafx.beans.property.*;

public class KashClass {
	private StringProperty cnameoper;
	private StringProperty ckbk;
	private StringProperty cpsevdo;
	private StringProperty C_CASHNAME;

	// Constructor
	public KashClass() {
		this.cnameoper = new SimpleStringProperty();
		this.ckbk = new SimpleStringProperty();
		this.cpsevdo = new SimpleStringProperty();
		this.C_CASHNAME = new SimpleStringProperty();
	}

	// cnameoper
	public String getC_CASHNAME() {
		return C_CASHNAME.get();
	}

	public void setC_CASHNAME(String C_CASHNAME) {
		this.C_CASHNAME.set(C_CASHNAME);
	}

	public StringProperty C_CASHNAMEProperty() {
		return C_CASHNAME;
	}

	// cnameoper
	public String getcnameoper() {
		return cnameoper.get();
	}

	public void setcnameoper(String cnameoper) {
		this.cnameoper.set(cnameoper);
	}

	public StringProperty cnameoperProperty() {
		return cnameoper;
	}

	// ckbk
	public String getckbk() {
		return ckbk.get();
	}

	public void setckbk(String ckbk) {
		this.ckbk.set(ckbk);
	}

	public StringProperty ckbkProperty() {
		return ckbk;
	}

	// cpsevdo
	public String getcpsevdo() {
		return cpsevdo.get();
	}

	public void setcpsevdo(String cpsevdo) {
		this.cpsevdo.set(cpsevdo);
	}

	public StringProperty cpsevdoProperty() {
		return cpsevdo;
	}

}

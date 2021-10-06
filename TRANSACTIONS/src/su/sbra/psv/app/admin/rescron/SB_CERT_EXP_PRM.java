package su.sbra.psv.app.admin.rescron;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SB_CERT_EXP_PRM {
	/** Значение параметра */
	private StringProperty PRMVAL;
	/** Название параметра */
	private StringProperty PRMNAME;

	public SB_CERT_EXP_PRM() {
		this.PRMVAL = new SimpleStringProperty();
		this.PRMNAME = new SimpleStringProperty();
	}

	public void setPRMVAL(String PRMVAL) {
		this.PRMVAL.set(PRMVAL);
	}

	public void setPRMNAME(String PRMNAME) {
		this.PRMNAME.set(PRMNAME);
	}

	public String getPRMVAL() {
		return PRMVAL.get();
	}

	public String getPRMNAME() {
		return PRMNAME.get();
	}

	public StringProperty PRMVALProperty() {
		return PRMVAL;
	}

	public StringProperty PRMNAMEProperty() {
		return PRMNAME;
	}
}

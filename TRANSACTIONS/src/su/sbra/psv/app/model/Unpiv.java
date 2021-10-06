package su.sbra.psv.app.model;

import javafx.beans.property.*;

public class Unpiv {

	private StringProperty COL;
	private StringProperty COLVALUE;

	// Constructor
	public Unpiv() {
		this.COL = new SimpleStringProperty();
		this.COLVALUE = new SimpleStringProperty();
	}

	public StringProperty COLProperty() {
		return COL;
	}

	public StringProperty COLVALUEProperty() {
		return COLVALUE;
	}

	public void set_COL(String COL) {
		this.COL.set(COL);
	}

	public void set_COLVALUE(String COLVALUE) {
		this.COLVALUE.set(COLVALUE);
	}

	public String get_COL() {
		return COL.get();
	}

	public String get_COLVALUE() {
		return COLVALUE.get();
	}

}

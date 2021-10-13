package su.sbra.psv.app.pensia;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PENSFILES {
	/** Нет данных */
	private DoubleProperty FILE_KB;
	/** Нет данных */
	private IntegerProperty PART_FILE;

	public PENSFILES() {
		this.FILE_KB = new SimpleDoubleProperty();
		this.PART_FILE = new SimpleIntegerProperty();
	}

	public void setFILE_KB(Double FILE_KB) {
		this.FILE_KB.set(FILE_KB);
	}

	public void setPART_FILE(Integer PART_FILE) {
		this.PART_FILE.set(PART_FILE);
	}

	public Double getFILE_KB() {
		return FILE_KB.get();
	}

	public Integer getPART_FILE() {
		return PART_FILE.get();
	}

	public DoubleProperty FILE_KBProperty() {
		return FILE_KB;
	}

	public IntegerProperty PART_FILEProperty() {
		return PART_FILE;
	}
}

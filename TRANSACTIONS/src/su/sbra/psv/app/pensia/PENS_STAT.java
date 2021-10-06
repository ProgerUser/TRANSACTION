package su.sbra.psv.app.pensia;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class PENS_STAT {
	/** Нет данных */
	private StringProperty NAMES;
	/** Нет данных */
	private LongProperty CNT;
	/** Нет данных */
	private DoubleProperty SUMM;

	public PENS_STAT() {
		this.NAMES = new SimpleStringProperty();
		this.CNT = new SimpleLongProperty();
		this.SUMM = new SimpleDoubleProperty();
	}

	public void setNAMES(String NAMES) {
		this.NAMES.set(NAMES);
	}

	public void setCNT(Long CNT) {
		this.CNT.set(CNT);
	}

	public void setSUMM(Double SUMM) {
		this.SUMM.set(SUMM);
	}

	public String getNAMES() {
		return NAMES.get();
	}

	public Long getCNT() {
		return CNT.get();
	}

	public Double getSUMM() {
		return SUMM.get();
	}

	public StringProperty NAMESProperty() {
		return NAMES;
	}

	public LongProperty CNTProperty() {
		return CNT;
	}

	public DoubleProperty SUMMProperty() {
		return SUMM;
	}
}

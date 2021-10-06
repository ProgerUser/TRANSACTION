package su.sbra.psv.app.pensia;

import java.time.LocalDate;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SBRA_YEAR_BET {
	/** По */
	private SimpleObjectProperty<LocalDate> END_Y;
	/** С */
	private SimpleObjectProperty<LocalDate> START_Y;
	/** Часть */
	private LongProperty PART;

	public SBRA_YEAR_BET() {
		this.END_Y = new SimpleObjectProperty<>();
		this.START_Y = new SimpleObjectProperty<>();
		this.PART = new SimpleLongProperty();
	}

	public void setEND_Y(LocalDate END_Y) {
		this.END_Y.set(END_Y);
	}

	public void setSTART_Y(LocalDate START_Y) {
		this.START_Y.set(START_Y);
	}

	public void setPART(Long PART) {
		this.PART.set(PART);
	}

	public LocalDate getEND_Y() {
		return END_Y.get();
	}

	public LocalDate getSTART_Y() {
		return START_Y.get();
	}

	public Long getPART() {
		return PART.get();
	}

	public SimpleObjectProperty<LocalDate> END_YProperty() {
		return END_Y;
	}

	public SimpleObjectProperty<LocalDate> START_YProperty() {
		return START_Y;
	}

	public LongProperty PARTProperty() {
		return PART;
	}
}

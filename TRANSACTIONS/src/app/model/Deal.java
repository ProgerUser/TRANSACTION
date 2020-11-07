package app.model;

import java.time.LocalDateTime;

import javafx.beans.property.*;

public class Deal {

	private IntegerProperty ROWNUMBER;
	private StringProperty CHEKNUMBER;
	private DoubleProperty SUMMA;
	private StringProperty TERMINAL;
	private SimpleObjectProperty<LocalDateTime> DATEOPERATION;

	// Constructor
	public Deal() {
		this.ROWNUMBER = new SimpleIntegerProperty();
		this.CHEKNUMBER = new SimpleStringProperty();
		this.SUMMA = new SimpleDoubleProperty();
		this.TERMINAL = new SimpleStringProperty();
		this.DATEOPERATION = new SimpleObjectProperty<>();
	}

	public IntegerProperty ROWNUMBERProperty() {
		return ROWNUMBER;
	}

	public StringProperty CHEKNUMBERProperty() {
		return CHEKNUMBER;
	}

	public DoubleProperty SUMMAProperty() {
		return SUMMA;
	}

	public StringProperty TERMINALProperty() {
		return TERMINAL;
	}
	
	public SimpleObjectProperty<LocalDateTime> DATEOPERATIONProperty() {
		return DATEOPERATION;
	}
	

	public void set_ROWNUMBER(Integer ROWNUMBER) {
		this.ROWNUMBER.set(ROWNUMBER);
	}

	public void set_CHEKNUMBER(String CHEKNUMBER) {
		this.CHEKNUMBER.set(CHEKNUMBER);
	}

	public void set_SUMMA(Double SUMMA) {
		this.SUMMA.set(SUMMA);
	}

	public void set_DATEOPERATION(LocalDateTime DATEOPERATION) {
		this.DATEOPERATION.set(DATEOPERATION);
	}
	
	public void set_TERMINAL(String TERMINAL) {
		this.TERMINAL.set(TERMINAL);
	}

	public Integer get_ROWNUMBER() {
		return ROWNUMBER.get();
	}

	public String get_CHEKNUMBER() {
		return CHEKNUMBER.get();
	}

	public Double get_SUMMA() {
		return SUMMA.get();
	}

	public String get_TERMINAL() {
		return TERMINAL.get();
	}
	
	public Object get_DATEOPERATION() {
		return TERMINAL.get();
	}

}

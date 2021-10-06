package su.sbra.psv.app.pensia;

import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PENS_LOAD_ROWSUM {
	private BooleanProperty CHK;
	/** Нет данных */
	private StringProperty FILE_NAME;
	/** Нет данных */
	private LongProperty ROW_COUNT;
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> DATE_LOAD;
	/** Нет данных */
	private LongProperty LOAD_ID;

	public PENS_LOAD_ROWSUM() {
		this.FILE_NAME = new SimpleStringProperty();
		this.ROW_COUNT = new SimpleLongProperty();
		this.CHK = new SimpleBooleanProperty();
		this.DATE_LOAD = new SimpleObjectProperty<>();
		this.LOAD_ID = new SimpleLongProperty();
	}

	public void setCHK(Boolean CHK) {
		this.CHK.set(CHK);
	}
	
	public Boolean getCHK() {
		return CHK.get();
	}
	
	public BooleanProperty CHKProperty() {
		return CHK;
	}
	
	public void setFILE_NAME(String FILE_NAME) {
		this.FILE_NAME.set(FILE_NAME);
	}

	public void setROW_COUNT(Long ROW_COUNT) {
		this.ROW_COUNT.set(ROW_COUNT);
	}

	public void setDATE_LOAD(LocalDateTime DATE_LOAD) {
		this.DATE_LOAD.set(DATE_LOAD);
	}

	public void setLOAD_ID(Long LOAD_ID) {
		this.LOAD_ID.set(LOAD_ID);
	}

	public String getFILE_NAME() {
		return FILE_NAME.get();
	}

	public Long getROW_COUNT() {
		return ROW_COUNT.get();
	}

	public LocalDateTime getDATE_LOAD() {
		return DATE_LOAD.get();
	}

	public Long getLOAD_ID() {
		return LOAD_ID.get();
	}

	public StringProperty FILE_NAMEProperty() {
		return FILE_NAME;
	}

	public LongProperty ROW_COUNTProperty() {
		return ROW_COUNT;
	}

	public SimpleObjectProperty<LocalDateTime> DATE_LOADProperty() {
		return DATE_LOAD;
	}

	public LongProperty LOAD_IDProperty() {
		return LOAD_ID;
	}
}

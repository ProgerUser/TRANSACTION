package contact;

import java.time.LocalDateTime;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SBRA_LOADF_CONTACT {
	/** Пользователь */
	private StringProperty LD_USER;
	/** Наименование файла */
	private StringProperty LD_FILENAME;
	/** Дата загрузки */
	private SimpleObjectProperty<LocalDateTime> LOAD_DATE;
	/** Ид загрузки */
	private LongProperty LOAD_ID;

	public SBRA_LOADF_CONTACT() {
		this.LD_USER = new SimpleStringProperty();
		this.LD_FILENAME = new SimpleStringProperty();
		this.LOAD_DATE = new SimpleObjectProperty<>();
		this.LOAD_ID = new SimpleLongProperty();
	}

	public void setLD_USER(String LD_USER) {
		this.LD_USER.set(LD_USER);
	}

	public void setLD_FILENAME(String LD_FILENAME) {
		this.LD_FILENAME.set(LD_FILENAME);
	}

	public void setLOAD_DATE(LocalDateTime LOAD_DATE) {
		this.LOAD_DATE.set(LOAD_DATE);
	}

	public void setLOAD_ID(Long LOAD_ID) {
		this.LOAD_ID.set(LOAD_ID);
	}

	public String getLD_USER() {
		return LD_USER.get();
	}

	public String getLD_FILENAME() {
		return LD_FILENAME.get();
	}

	public LocalDateTime getLOAD_DATE() {
		return LOAD_DATE.get();
	}

	public Long getLOAD_ID() {
		return LOAD_ID.get();
	}

	public StringProperty LD_USERProperty() {
		return LD_USER;
	}

	public StringProperty LD_FILENAMEProperty() {
		return LD_FILENAME;
	}

	public SimpleObjectProperty<LocalDateTime> LOAD_DATEProperty() {
		return LOAD_DATE;
	}

	public LongProperty LOAD_IDProperty() {
		return LOAD_ID;
	}
}

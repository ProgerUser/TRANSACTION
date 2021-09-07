package access.menu;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ODB_MNU {
	/** Идентификатор действия */
	private LongProperty MNU_ID;
	/** Идентификатор родительского действия */
	private LongProperty MNU_PARENT;
	/** Порядковый номер внутри иерархии */
	private LongProperty MNU_NPP;
	/** Наименование действия */
	private StringProperty MNU_NAME;

	public ODB_MNU() {
		this.MNU_ID = new SimpleLongProperty();
		this.MNU_PARENT = new SimpleLongProperty();
		this.MNU_NPP = new SimpleLongProperty();
		this.MNU_NAME = new SimpleStringProperty();
	}

	public void setMNU_ID(Long MNU_ID) {
		this.MNU_ID.set(MNU_ID);
	}

	public void setMNU_PARENT(Long MNU_PARENT) {
		this.MNU_PARENT.set(MNU_PARENT);
	}

	public void setMNU_NPP(Long MNU_NPP) {
		this.MNU_NPP.set(MNU_NPP);
	}

	public void setMNU_NAME(String MNU_NAME) {
		this.MNU_NAME.set(MNU_NAME);
	}

	public Long getMNU_ID() {
		return MNU_ID.get();
	}

	public Long getMNU_PARENT() {
		return MNU_PARENT.get();
	}

	public Long getMNU_NPP() {
		return MNU_NPP.get();
	}

	public String getMNU_NAME() {
		return MNU_NAME.get();
	}

	public LongProperty MNU_IDProperty() {
		return MNU_ID;
	}

	public LongProperty MNU_PARENTProperty() {
		return MNU_PARENT;
	}

	public LongProperty MNU_NPPProperty() {
		return MNU_NPP;
	}

	public StringProperty MNU_NAMEProperty() {
		return MNU_NAME;
	}
}

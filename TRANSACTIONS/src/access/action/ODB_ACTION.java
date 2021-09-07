package access.action;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ODB_ACTION {
	private StringProperty ACT_NAME;/* Наименование действия */
	private LongProperty ACT_NPP;/* Порядковый номер внутри иерархии */
	private LongProperty ACT_PARENT;/* Идентификатор родительского действия */
	private LongProperty ACT_ID;/* Идентификатор действия */

	public ODB_ACTION() {
		this.ACT_NAME = new SimpleStringProperty();
		this.ACT_NPP = new SimpleLongProperty();
		this.ACT_PARENT = new SimpleLongProperty();
		this.ACT_ID = new SimpleLongProperty();
	}

	public void setACT_NAME(String ACT_NAME) {
		this.ACT_NAME.set(ACT_NAME);
	}

	public void setACT_NPP(Long ACT_NPP) {
		this.ACT_NPP.set(ACT_NPP);
	}

	public void setACT_PARENT(Long ACT_PARENT) {
		this.ACT_PARENT.set(ACT_PARENT);
	}

	public void setACT_ID(Long l) {
		this.ACT_ID.set(l);
	}

	public String getACT_NAME() {
		return ACT_NAME.get();
	}

	public Long getACT_NPP() {
		return ACT_NPP.get();
	}

	public Long getACT_PARENT() {
		return ACT_PARENT.get();
	}

	public Long getACT_ID() {
		return ACT_ID.get();
	}

	public StringProperty ACT_NAMEProperty() {
		return ACT_NAME;
	}

	public LongProperty ACT_NPPProperty() {
		return ACT_NPP;
	}

	public LongProperty ACT_PARENTProperty() {
		return ACT_PARENT;
	}

	public LongProperty ACT_IDProperty() {
		return ACT_ID;
	}
}

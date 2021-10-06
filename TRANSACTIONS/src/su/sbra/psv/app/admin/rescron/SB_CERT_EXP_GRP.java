package su.sbra.psv.app.admin.rescron;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SB_CERT_EXP_GRP {
	/** Нет данных */
	private StringProperty GRP_NAME;
	/** Нет данных */
	private LongProperty GRP_ID;

	public SB_CERT_EXP_GRP() {
		this.GRP_NAME = new SimpleStringProperty();
		this.GRP_ID = new SimpleLongProperty();
	}

	public void setGRP_NAME(String GRP_NAME) {
		this.GRP_NAME.set(GRP_NAME);
	}

	public void setGRP_ID(Long GRP_ID) {
		this.GRP_ID.set(GRP_ID);
	}

	public String getGRP_NAME() {
		return GRP_NAME.get();
	}

	public Long getGRP_ID() {
		return GRP_ID.get();
	}

	public StringProperty GRP_NAMEProperty() {
		return GRP_NAME;
	}

	public LongProperty GRP_IDProperty() {
		return GRP_ID;
	}
}

package su.sbra.psv.app.access.grp;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AP_REPORT_TYPE {
	/** Идентификатор типа отчета */
	private LongProperty REPORT_TYPE_ID;
	/** Наименование типа отчета */
	private StringProperty REPORT_TYPE_NAME;

	public AP_REPORT_TYPE() {
		this.REPORT_TYPE_ID = new SimpleLongProperty();
		this.REPORT_TYPE_NAME = new SimpleStringProperty();
	}

	public void setREPORT_TYPE_ID(Long REPORT_TYPE_ID) {
		this.REPORT_TYPE_ID.set(REPORT_TYPE_ID);
	}

	public void setREPORT_TYPE_NAME(String REPORT_TYPE_NAME) {
		this.REPORT_TYPE_NAME.set(REPORT_TYPE_NAME);
	}

	public Long getREPORT_TYPE_ID() {
		return REPORT_TYPE_ID.get();
	}

	public String getREPORT_TYPE_NAME() {
		return REPORT_TYPE_NAME.get();
	}

	public LongProperty REPORT_TYPE_IDProperty() {
		return REPORT_TYPE_ID;
	}

	public StringProperty REPORT_TYPE_NAMEProperty() {
		return REPORT_TYPE_NAME;
	}
}

package tr.pl;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlModel {
	/** ��� ������ */
	private StringProperty STAT;
	/** ��� ������ */
	private StringProperty FIO;
	/** ��� ������ */
	private StringProperty LOGIN;

	public PlModel() {
		this.STAT = new SimpleStringProperty();
		this.FIO = new SimpleStringProperty();
		this.LOGIN = new SimpleStringProperty();
	}

	public void setSTAT(String STAT) {
		this.STAT.set(STAT);
	}

	public void setFIO(String FIO) {
		this.FIO.set(FIO);
	}

	public void setLOGIN(String LOGIN) {
		this.LOGIN.set(LOGIN);
	}

	public String getSTAT() {
		return STAT.get();
	}

	public String getFIO() {
		return FIO.get();
	}

	public String getLOGIN() {
		return LOGIN.get();
	}

	public StringProperty STATProperty() {
		return STAT;
	}

	public StringProperty FIOProperty() {
		return FIO;
	}

	public StringProperty LOGINProperty() {
		return LOGIN;
	}

}

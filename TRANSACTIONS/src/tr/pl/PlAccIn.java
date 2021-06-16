package tr.pl;

import java.time.LocalDateTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlAccIn {
	/** ��� ������ */
	private StringProperty CACCNAME;
	/** ��� ������ */
	private StringProperty CACCACC;
	/** ��� ������ */
	private SimpleObjectProperty<LocalDateTime> D_END;
	/** ��� ������ */
	private SimpleObjectProperty<LocalDateTime> D_START;

	public PlAccIn() {
		this.CACCNAME = new SimpleStringProperty();
		this.CACCACC = new SimpleStringProperty();
		this.D_END = new SimpleObjectProperty<>();
		this.D_START = new SimpleObjectProperty<>();
	}

	public void setD_END(LocalDateTime D_END) {
		this.D_END.set(D_END);
	}

	public void setD_START(LocalDateTime D_START) {
		this.D_START.set(D_START);
	}

	public void setCACCNAME(String CACCNAME) {
		this.CACCNAME.set(CACCNAME);
	}

	public void setCACCACC(String CACCACC) {
		this.CACCACC.set(CACCACC);
	}

	public String getCACCNAME() {
		return CACCNAME.get();
	}

	public String getCACCACC() {
		return CACCACC.get();
	}

	public StringProperty CACCNAMEProperty() {
		return CACCNAME;
	}

	public StringProperty CACCACCProperty() {
		return CACCACC;
	}

	public Object getD_END() {
		return D_END.get();
	}

	public Object getD_START() {
		return D_START.get();
	}

	public SimpleObjectProperty<LocalDateTime> D_ENDProperty() {
		return D_END;
	}

	public SimpleObjectProperty<LocalDateTime> D_STARTProperty() {
		return D_START;
	}

}

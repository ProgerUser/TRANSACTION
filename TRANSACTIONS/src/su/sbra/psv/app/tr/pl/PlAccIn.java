package su.sbra.psv.app.tr.pl;

import java.time.LocalDateTime;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlAccIn {
	/** Нет данных */
	private StringProperty CACCNAME;
	/** Нет данных */
	private StringProperty CACCACC;
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> D_END;
	/** Нет данных */
	private SimpleObjectProperty<LocalDateTime> D_START;
	private StringProperty cardnum;
	private StringProperty USRS;
	private StringProperty Dog;
	private DoubleProperty Ostt;

	public PlAccIn() {
		this.CACCNAME = new SimpleStringProperty();
		this.CACCACC = new SimpleStringProperty();
		this.Dog = new SimpleStringProperty();
		this.Ostt = new SimpleDoubleProperty();
		this.cardnum = new SimpleStringProperty();
		this.USRS = new SimpleStringProperty();
		this.D_END = new SimpleObjectProperty<>();
		this.D_START = new SimpleObjectProperty<>();
	}

	public void setOstt(Double Ostt) {
		this.Ostt.set(Ostt);
	}

	public DoubleProperty OsttProperty() {
		return Ostt;
	}

	public Double getOstt() {
		return Ostt.get();
	}

	public void setDog(String Dog) {
		this.Dog.set(Dog);
	}

	public StringProperty DogProperty() {
		return Dog;
	}

	public String getDog() {
		return Dog.get();
	}

	public void setUSRS(String USRS) {
		this.USRS.set(USRS);
	}

	public StringProperty USRSProperty() {
		return USRS;
	}

	public String getUSRS() {
		return USRS.get();
	}

	public void setcardnum(String cardnum) {
		this.cardnum.set(cardnum);
	}

	public StringProperty cardnumProperty() {
		return cardnum;
	}

	public String getcardnum() {
		return cardnum.get();
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

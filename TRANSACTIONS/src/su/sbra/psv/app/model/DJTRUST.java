package su.sbra.psv.app.model;

import javafx.beans.property.*;

public class DJTRUST {
	private StringProperty id_trust;
	private StringProperty iddover;
	private StringProperty fio;
	private StringProperty acc;

	// Constructor
	public DJTRUST() {
		this.id_trust = new SimpleStringProperty();
		this.iddover = new SimpleStringProperty();
		this.fio = new SimpleStringProperty();
		this.acc = new SimpleStringProperty();
	}

	// iddover
	public String getiddover() {
		return iddover.get();
	}

	public void setiddover(String iddover) {
		this.iddover.set(iddover);
	}

	public StringProperty iddoverProperty() {
		return iddover;
	}

	// id_trust
	public String getid_trust() {
		return id_trust.get();
	}

	public void setid_trust(String id_trust) {
		this.id_trust.set(id_trust);
	}

	public StringProperty id_trustProperty() {
		return id_trust;
	}

	// fio
	public String getfio() {
		return fio.get();
	}

	public void setfio(String fio) {
		this.fio.set(fio);
	}

	public StringProperty fioProperty() {
		return fio;
	}

	// acc
	public String getacc() {
		return acc.get();
	}

	public void setacc(String acc) {
		this.acc.set(acc);
	}

	public StringProperty accProperty() {
		return acc;
	}
}

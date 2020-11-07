package app.model;

import javafx.beans.property.*;

public class Add_File {

	private StringProperty FileName;
	private StringProperty Status;
	private StringProperty Date;
	private StringProperty User;
	private StringProperty FileId;
	private StringProperty Path;
	private StringProperty SessId;

	// Constructor
	public Add_File() {
		this.FileName = new SimpleStringProperty();
		this.Status = new SimpleStringProperty();
		this.Date = new SimpleStringProperty();
		this.User = new SimpleStringProperty();
		this.FileId = new SimpleStringProperty();
		this.Path = new SimpleStringProperty();
		this.SessId = new SimpleStringProperty();
	}

	// ------------------------------
	public StringProperty SessIdProperty() {
		return SessId;
	}

	// ------------------------------
	public StringProperty FileNameProperty() {
		return FileName;
	}

	public StringProperty StatusProperty() {
		return Status;
	}

	public StringProperty DateProperty() {
		return Date;
	}

	public StringProperty UserProperty() {
		return User;
	}

	public StringProperty FileIdProperty() {
		return FileId;
	}

	public StringProperty PathProperty() {
		return Path;
	}

	// ------------------------------
	public void set_SessId(String SessId) {
		this.SessId.set(SessId);
	}

	// ------------------------------
	public void set_FileName(String FileName) {
		this.FileName.set(FileName);
	}

	public void set_Status(String Status) {
		this.Status.set(Status);
	}

	public void set_Date(String Date) {
		this.Date.set(Date);
	}

	public void set_User(String User) {
		this.User.set(User);
	}

	public void set_FileId(String FileId) {
		this.FileId.set(FileId);
	}

	public void set_Path(String Path) {
		this.Path.set(Path);
	}

	// ------------------------------

	public String get_SessId() {
		return SessId.get();
	}

	public String get_FileName() {
		return FileName.get();
	}

	public String get_Status() {
		return Status.get();
	}

	public String get_Date() {
		return Date.get();
	}

	public String get_User() {
		return User.get();
	}

	public String get_FileId() {
		return FileId.get();
	}

	public String get_Path() {
		return Path.get();
	}

}

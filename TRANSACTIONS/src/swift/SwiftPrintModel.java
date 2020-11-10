package swift;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SwiftPrintModel {
	private StringProperty message;/* Сообщение */

	public SwiftPrintModel() {
		this.message = new SimpleStringProperty();
	}

	public void setmessage(String message) {
		this.message.set(message);
	}

	public String getmessage() {
		return message.get();
	}

	public StringProperty messageProperty() {
		return message;
	}
}

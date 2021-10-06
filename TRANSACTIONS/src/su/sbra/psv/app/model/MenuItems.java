package su.sbra.psv.app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MenuItems {
	private IntegerProperty ID_MENU;
	private StringProperty MENU_NAME;
	private StringProperty MENU_DESC;
	private IntegerProperty MENU_POS;
	private IntegerProperty MENU_I;

	/*CONSTRUCTOR*/
	public MenuItems() {
		this.ID_MENU = new SimpleIntegerProperty();
		this.MENU_NAME = new SimpleStringProperty();
		this.MENU_DESC = new SimpleStringProperty();
		this.MENU_POS = new SimpleIntegerProperty();
		this.MENU_I = new SimpleIntegerProperty();
	}

	/*MENU_I*/
	public int getMENU_I() {
		return MENU_I.get();
	}

	public void setMENU_I(int MENU_I) {
		this.MENU_I.set(MENU_I);
	}

	public IntegerProperty MENU_IProperty() {
		return MENU_I;
	}
	
	/*MENU_POS*/
	public int getMENU_POS() {
		return MENU_POS.get();
	}

	public void setMENU_POS(int MENU_POS) {
		this.MENU_POS.set(MENU_POS);
	}

	public IntegerProperty MENU_POSProperty() {
		return MENU_POS;
	}

	
	/*ID_MENU*/
	public int getID_MENU() {
		return ID_MENU.get();
	}

	public void setID_MENU(int ID_MENU) {
		this.ID_MENU.set(ID_MENU);
	}

	public IntegerProperty ID_MENUProperty() {
		return ID_MENU;
	}

	/*MENU_NAME*/
	public String getMENU_NAME() {
		return MENU_NAME.get();
	}

	public void setMENU_NAME(String MENU_NAME) {
		this.MENU_NAME.set(MENU_NAME);
	}

	public StringProperty MENU_NAMEProperty() {
		return MENU_NAME;
	}

	/*MENU_DESC*/
	public String getMENU_DESC() {
		return MENU_DESC.get();
	}

	public void setMENU_DESC(String MENU_DESC) {
		this.MENU_DESC.set(MENU_DESC);
	}

	public StringProperty MENU_DESCProperty() {
		return MENU_DESC;
	}
}


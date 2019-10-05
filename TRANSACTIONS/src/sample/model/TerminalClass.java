package sample.model;

import javafx.beans.property.*;
import java.sql.Date;

@SuppressWarnings("unused")
public class TerminalClass {
	private StringProperty NAME;
	private StringProperty DEPARTMENT;
	private StringProperty ADDRESS;
	private StringProperty ACCOUNT;
	private StringProperty acc_30232_01;
	private StringProperty acc_30232_02;
	private StringProperty acc_30232_03;
	private StringProperty acc_30232_04;
	private StringProperty acc_30232_05;
	private StringProperty acc_70107;

	// Constructor
	public TerminalClass() {
		this.NAME = new SimpleStringProperty();
		this.DEPARTMENT = new SimpleStringProperty();
		this.ADDRESS = new SimpleStringProperty();
		this.ACCOUNT = new SimpleStringProperty();
		this.acc_30232_01 = new SimpleStringProperty();
		this.acc_30232_02 = new SimpleStringProperty();
		this.acc_30232_03 = new SimpleStringProperty();
		this.acc_30232_04 = new SimpleStringProperty();
		this.acc_30232_05 = new SimpleStringProperty();
		this.acc_70107 = new SimpleStringProperty();
	}

	// NAME
	public String getNAME() {
		return NAME.get();
	}

	public void setNAME(String NAME) {
		this.NAME.set(NAME);
	}

	public StringProperty NAMEProperty() {
		return NAME;
	}

	// DEPARTMENT
	public String getDEPARTMENT() {
		return DEPARTMENT.get();
	}

	public void setDEPARTMENT(String DEPARTMENT) {
		this.DEPARTMENT.set(DEPARTMENT);
	}

	public StringProperty DEPARTMENTProperty() {
		return DEPARTMENT;
	}

	// ADDRESS
	public String getADDRESS() {
		return ADDRESS.get();
	}

	public void setADDRESS(String ADDRESS) {
		this.ADDRESS.set(ADDRESS);
	}

	public StringProperty ADDRESSProperty() {
		return ADDRESS;
	}

	// ACCOUNT
	public String getACCOUNT() {
		return ACCOUNT.get();
	}

	public void setACCOUNT(String ACCOUNT) {
		this.ACCOUNT.set(ACCOUNT);
	}

	public StringProperty ACCOUNTProperty() {
		return ACCOUNT;
	}

	// acc_30232_01
	public String getacc_30232_01() {
		return acc_30232_01.get();
	}

	public void setacc_30232_01(String acc_30232_01) {
		this.acc_30232_01.set(acc_30232_01);
	}

	public StringProperty acc_30232_01Property() {
		return acc_30232_01;
	}

	// acc_30232_02
	public String getacc_30232_02() {
		return acc_30232_02.get();
	}

	public void setacc_30232_02(String acc_30232_02) {
		this.acc_30232_02.set(acc_30232_02);
	}

	public StringProperty acc_30232_02Property() {
		return acc_30232_02;
	}

	// acc_30232_03
	public String getacc_30232_03() {
		return acc_30232_03.get();
	}

	public void setacc_30232_03(String acc_30232_03) {
		this.acc_30232_03.set(acc_30232_03);
	}

	public StringProperty acc_30232_03Property() {
		return acc_30232_03;
	}

	// acc_30232_04
	public String getacc_30232_04() {
		return acc_30232_04.get();
	}

	public void setacc_30232_04(String acc_30232_04) {
		this.acc_30232_04.set(acc_30232_04);
	}

	public StringProperty acc_30232_04Property() {
		return acc_30232_04;
	}

	// acc_30232_05
	public String getacc_30232_05() {
		return acc_30232_05.get();
	}

	public void setacc_30232_05(String acc_30232_05) {
		this.acc_30232_05.set(acc_30232_05);
	}

	public StringProperty acc_30232_05Property() {
		return acc_30232_05;
	}

	// acc_70107
	public String getacc_70107() {
		return acc_70107.get();
	}

	public void setacc_70107(String acc_70107) {
		this.acc_70107.set(acc_70107);
	}

	public StringProperty acc_70107Property() {
		return acc_70107;
	}
}

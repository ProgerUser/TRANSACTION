package app.model;

import javafx.beans.property.*;
import java.sql.Date;

@SuppressWarnings("unused")
public class TerminalClass {
	private StringProperty NAME;
	private StringProperty DEPARTMENT;
	private StringProperty ADDRESS;
	private StringProperty ACCOUNT;
	private StringProperty GENERAL_ACC;
	private StringProperty CRASH_ACC;
	private StringProperty DEAL_ACC;
	private StringProperty GENERAL_COMIS;
	private StringProperty CLEAR_SUM;
	private StringProperty INCOME;

	// Constructor
	public TerminalClass() {
		this.NAME = new SimpleStringProperty();
		this.DEPARTMENT = new SimpleStringProperty();
		this.ADDRESS = new SimpleStringProperty();
		this.ACCOUNT = new SimpleStringProperty();
		this.GENERAL_ACC = new SimpleStringProperty();
		this.CRASH_ACC = new SimpleStringProperty();
		this.DEAL_ACC = new SimpleStringProperty();
		this.GENERAL_COMIS = new SimpleStringProperty();
		this.CLEAR_SUM = new SimpleStringProperty();
		this.INCOME = new SimpleStringProperty();
	}

	// ACC_30232_06
	public String getGENERAL_ACC() {
		return GENERAL_ACC.get();
	}

	public void setGENERAL_ACC(String GENERAL_ACC) {
		this.GENERAL_ACC.set(GENERAL_ACC);
	}

	public StringProperty GENERAL_ACCProperty() {
		return GENERAL_ACC;
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


	public String getCRASH_ACC() {
		return CRASH_ACC.get();
	}

	public void setCRASH_ACC(String CRASH_ACC) {
		this.CRASH_ACC.set(CRASH_ACC);
	}

	public StringProperty CRASH_ACCProperty() {
		return CRASH_ACC;
	}


	public String getDEAL_ACC() {
		return DEAL_ACC.get();
	}

	public void setDEAL_ACC(String DEAL_ACC) {
		this.DEAL_ACC.set(DEAL_ACC);
	}

	public StringProperty DEAL_ACCProperty() {
		return DEAL_ACC;
	}


	public String getGENERAL_COMIS() {
		return GENERAL_COMIS.get();
	}

	public void setGENERAL_COMIS(String GENERAL_COMIS) {
		this.GENERAL_COMIS.set(GENERAL_COMIS);
	}

	public StringProperty GENERAL_COMISProperty() {
		return GENERAL_COMIS;
	}


	public String getCLEAR_SUM() {
		return CLEAR_SUM.get();
	}

	public void setCLEAR_SUM(String CLEAR_SUM) {
		this.CLEAR_SUM.set(CLEAR_SUM);
	}

	public StringProperty CLEAR_SUMProperty() {
		return CLEAR_SUM;
	}


	public String getINCOME() {
		return INCOME.get();
	}

	public void setINCOME(String INCOME) {
		this.INCOME.set(INCOME);
	}

	public StringProperty INCOMEProperty() {
		return INCOME;
	}
}

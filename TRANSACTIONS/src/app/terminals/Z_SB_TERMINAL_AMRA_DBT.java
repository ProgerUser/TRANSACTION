package app.terminals;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Z_SB_TERMINAL_AMRA_DBT {
	/** Название терминала в кабинете дилера АМРА */
	private StringProperty SDNAME;
	/** Наша комиссия */
	private StringProperty INCOME;
	/** Чистая сумма */
	private StringProperty CLEAR_SUM;
	/** Комиссия общая */
	private StringProperty GENERAL_COMIS;
	/** Сдачи */
	private StringProperty DEAL_ACC;
	/** Авария */
	private StringProperty CRASH_ACC;
	/** Общий счет */
	private StringProperty GENERAL_ACC;
	/** Счет 20208% */
	private StringProperty ACCOUNT;
	/** Адрес */
	private StringProperty ADDRESS;
	/** Отделение */
	private IntegerProperty DEPARTMENT;
	/** Название терминала */
	private StringProperty NAME;

	public Z_SB_TERMINAL_AMRA_DBT() {
		this.SDNAME = new SimpleStringProperty();
		this.INCOME = new SimpleStringProperty();
		this.CLEAR_SUM = new SimpleStringProperty();
		this.GENERAL_COMIS = new SimpleStringProperty();
		this.DEAL_ACC = new SimpleStringProperty();
		this.CRASH_ACC = new SimpleStringProperty();
		this.GENERAL_ACC = new SimpleStringProperty();
		this.ACCOUNT = new SimpleStringProperty();
		this.ADDRESS = new SimpleStringProperty();
		this.DEPARTMENT = new SimpleIntegerProperty();
		this.NAME = new SimpleStringProperty();
	}

	public void setSDNAME(String SDNAME) {
		this.SDNAME.set(SDNAME);
	}

	public void setINCOME(String INCOME) {
		this.INCOME.set(INCOME);
	}

	public void setCLEAR_SUM(String CLEAR_SUM) {
		this.CLEAR_SUM.set(CLEAR_SUM);
	}

	public void setGENERAL_COMIS(String GENERAL_COMIS) {
		this.GENERAL_COMIS.set(GENERAL_COMIS);
	}

	public void setDEAL_ACC(String DEAL_ACC) {
		this.DEAL_ACC.set(DEAL_ACC);
	}

	public void setCRASH_ACC(String CRASH_ACC) {
		this.CRASH_ACC.set(CRASH_ACC);
	}

	public void setGENERAL_ACC(String GENERAL_ACC) {
		this.GENERAL_ACC.set(GENERAL_ACC);
	}

	public void setACCOUNT(String ACCOUNT) {
		this.ACCOUNT.set(ACCOUNT);
	}

	public void setADDRESS(String ADDRESS) {
		this.ADDRESS.set(ADDRESS);
	}

	public void setDEPARTMENT(Integer DEPARTMENT) {
		this.DEPARTMENT.set(DEPARTMENT);
	}

	public void setNAME(String NAME) {
		this.NAME.set(NAME);
	}

	public String getSDNAME() {
		return SDNAME.get();
	}

	public String getINCOME() {
		return INCOME.get();
	}

	public String getCLEAR_SUM() {
		return CLEAR_SUM.get();
	}

	public String getGENERAL_COMIS() {
		return GENERAL_COMIS.get();
	}

	public String getDEAL_ACC() {
		return DEAL_ACC.get();
	}

	public String getCRASH_ACC() {
		return CRASH_ACC.get();
	}

	public String getGENERAL_ACC() {
		return GENERAL_ACC.get();
	}

	public String getACCOUNT() {
		return ACCOUNT.get();
	}

	public String getADDRESS() {
		return ADDRESS.get();
	}

	public Integer getDEPARTMENT() {
		return DEPARTMENT.get();
	}

	public String getNAME() {
		return NAME.get();
	}

	public StringProperty SDNAMEProperty() {
		return SDNAME;
	}

	public StringProperty INCOMEProperty() {
		return INCOME;
	}

	public StringProperty CLEAR_SUMProperty() {
		return CLEAR_SUM;
	}

	public StringProperty GENERAL_COMISProperty() {
		return GENERAL_COMIS;
	}

	public StringProperty DEAL_ACCProperty() {
		return DEAL_ACC;
	}

	public StringProperty CRASH_ACCProperty() {
		return CRASH_ACC;
	}

	public StringProperty GENERAL_ACCProperty() {
		return GENERAL_ACC;
	}

	public StringProperty ACCOUNTProperty() {
		return ACCOUNT;
	}

	public StringProperty ADDRESSProperty() {
		return ADDRESS;
	}

	public IntegerProperty DEPARTMENTProperty() {
		return DEPARTMENT;
	}

	public StringProperty NAMEProperty() {
		return NAME;
	}
}

package sample.model;

import javafx.beans.property.*;

public class ServiceClass {
	private StringProperty name;
	private StringProperty idterm;
	private StringProperty account;
	private StringProperty account2;
	private StringProperty account3;
	private StringProperty account4;
	private StringProperty account5;
	private StringProperty inn;
	private StringProperty kpp;
	private StringProperty kor_bank_nbra;
	private StringProperty acc_rec;
	private StringProperty kbk;
	private StringProperty okato;
	private StringProperty stat;
	private StringProperty acc_name;
	private StringProperty bo1;
	private StringProperty bo2;
	private StringProperty comission;

	// Constructor
	public ServiceClass() {
		this.name = new SimpleStringProperty();
		this.idterm = new SimpleStringProperty();
		this.account = new SimpleStringProperty();
		this.account2 = new SimpleStringProperty();
		this.account3 = new SimpleStringProperty();
		this.account4 = new SimpleStringProperty();
		this.account5 = new SimpleStringProperty();
		this.inn = new SimpleStringProperty();
		this.kpp = new SimpleStringProperty();
		this.kor_bank_nbra = new SimpleStringProperty();
		this.acc_rec = new SimpleStringProperty();
		this.kbk = new SimpleStringProperty();
		this.okato = new SimpleStringProperty();
		this.stat = new SimpleStringProperty();
		this.acc_name = new SimpleStringProperty();
		this.bo1 = new SimpleStringProperty();
		this.bo2 = new SimpleStringProperty();
		this.comission = new SimpleStringProperty();
	}

	// comission
	public String getcomission() {
		return comission.get();
	}

	public void setcomission(String comission) {
		this.comission.set(comission);
	}

	public StringProperty comissionProperty() {
		return comission;
	}

	// acc_name
	public String getbo2() {
		return bo2.get();
	}

	public void setbo2(String bo2) {
		this.bo2.set(bo2);
	}

	public StringProperty bo2Property() {
		return bo2;
	}

	// acc_name
	public String getbo1() {
		return bo1.get();
	}

	public void setbo1(String bo1) {
		this.bo1.set(bo1);
	}

	public StringProperty bo1Property() {
		return bo1;
	}

	// acc_name
	public String getacc_name() {
		return acc_name.get();
	}

	public void setacc_name(String acc_name) {
		this.acc_name.set(acc_name);
	}

	public StringProperty acc_nameProperty() {
		return acc_name;
	}

	// stat
	public String getstat() {
		return stat.get();
	}

	public void setstat(String stat) {
		this.stat.set(stat);
	}

	public StringProperty statProperty() {
		return stat;
	}

	// okato
	public String getokato() {
		return okato.get();
	}

	public void setokato(String okato) {
		this.okato.set(okato);
	}

	public StringProperty okatoProperty() {
		return okato;
	}

	// kbk
	public String getkbk() {
		return kbk.get();
	}

	public void setkbk(String kbk) {
		this.kbk.set(kbk);
	}

	public StringProperty kbkProperty() {
		return kbk;
	}

	// acc_rec
	public String getacc_rec() {
		return acc_rec.get();
	}

	public void setacc_rec(String acc_rec) {
		this.acc_rec.set(acc_rec);
	}

	public StringProperty acc_recProperty() {
		return acc_rec;
	}

	// kor_bank_nbra
	public String getkor_bank_nbra() {
		return kor_bank_nbra.get();
	}

	public void setkor_bank_nbra(String kor_bank_nbra) {
		this.kor_bank_nbra.set(kor_bank_nbra);
	}

	public StringProperty kor_bank_nbraProperty() {
		return kor_bank_nbra;
	}

	// kpp
	public String getkpp() {
		return kpp.get();
	}

	public void setkpp(String kpp) {
		this.kpp.set(kpp);
	}

	public StringProperty kppProperty() {
		return kpp;
	}

	// inn
	public String getinn() {
		return inn.get();
	}

	public void setinn(String inn) {
		this.inn.set(inn);
	}

	public StringProperty innProperty() {
		return inn;
	}

	// account5
	public String getaccount5() {
		return account5.get();
	}

	public void setaccount5(String account5) {
		this.account5.set(account5);
	}

	public StringProperty account5Property() {
		return account5;
	}

	// account4
	public String getaccount4() {
		return account4.get();
	}

	public void setaccount4(String account4) {
		this.account4.set(account4);
	}

	public StringProperty account4Property() {
		return account4;
	}

	// account3
	public String getaccount3() {
		return account3.get();
	}

	public void setaccount3(String account3) {
		this.account3.set(account3);
	}

	public StringProperty account3Property() {
		return account3;
	}

	// account2
	public String getaccount2() {
		return account2.get();
	}

	public void setaccount2(String account2) {
		this.account2.set(account2);
	}

	public StringProperty account2Property() {
		return account2;
	}

	// account
	public String getaccount() {
		return account.get();
	}

	public void setaccount(String account) {
		this.account.set(account);
	}

	public StringProperty accountProperty() {
		return account;
	}

	// idterm
	public String getidterm() {
		return idterm.get();
	}

	public void setidterm(String idterm) {
		this.idterm.set(idterm);
	}

	public StringProperty idtermProperty() {
		return idterm;
	}

	// name
	public String getname() {
		return name.get();
	}

	public void setname(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}
}

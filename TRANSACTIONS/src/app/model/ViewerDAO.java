package app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import app.util.DBUtil;

@SuppressWarnings("unused")
public class ViewerDAO {

	// *******************************
	// SELECT an Employee
	// *******************************
	public static TransactClass searchTransact(String fio) {
		// Declare a SELECT statement
		String selectStmt = "SELECT * FROM Z_SB_TRANSACT_DBT WHERE lower(FIO) like '" + fio + "'";

		// select PAYMENTNUMBER, FIO, DATETIMEPAYMENT, ACCOUNT, PAYMENTDATA from
		// Z_SB_TRANSACT_DBT t"

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeFromResultSet method and get employee object
		TransactClass transact = getEmployeeFromResultSet(rsEmp);

		// Return employee object
		return transact;
	}

	// Use ResultSet from DB as parameter and set Employee Object's attributes and
	// return employee object.
	private static TransactClass getEmployeeFromResultSet(ResultSet rs) {
		TransactClass tr = null;
		try {
			while (rs.next()) {
				tr = new TransactClass();
				tr.setPAYMENTNUMBER(rs.getString("PAYMENTNUMBER"));
				tr.setFIO(rs.getString("FIO"));
				tr.setDATETIMEPAYMENT(rs.getString("DATETIMEPAYMENT"));
				tr.setACCOUNT(rs.getString("ACCOUNT"));
				tr.setPAYMENTDATA(rs.getString("PAYMENTDATA"));
				tr.setINSUM(rs.getString("INSUM"));
				tr.setFEESUM(rs.getString("FEESUM"));
				tr.setSESS_ID(rs.getString("SESS_ID"));
				tr.setIDTERM(rs.getString("IDTERM"));
				tr.setADDRESS(rs.getString("ADDRESS"));
				tr.setRECEIVERSUM(rs.getString("RECEIVERSUM"));
				tr.setkindergarten(rs.getString("kindergarten"));
				tr.setchgroup(rs.getString("chgroup"));
				tr.setperiod(rs.getString("period"));
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
		return tr;
	}

	// *******************************
	// SELECT Transact
	// *******************************
	public static ObservableList<TransactClass> searchEmployees(String FIO, String PAYMENTNUMBER, String DT1,
			String DT2) {
		// Declare a SELECT statement
		String dt_betw = "";
		String p_n = "";
		if (PAYMENTNUMBER.equals("")) {

		} else {
			p_n = "and PAYMENTNUMBER like '" + PAYMENTNUMBER + "'\n";
		}

		if (DT1.equals("") & DT2.equals("")) {

		} else {
			dt_betw = "and to_date(DATETIMEPAYMENT, 'dd-mm-rrrr HH24:MI:SS') between to_date('" + DT1
					+ "','dd-mm-rrrr') and to_date('" + DT2 + "','dd-mm-rrrr')\n";
		}

		String selectStmt = "select PAYMENTNUMBER,\r\n" + "       FIO,\r\n" + "       DATETIMEPAYMENT,\r\n"
				+ "       ACCOUNT,\r\n"
				+ "       decode(PAYMENTDATA, null, COMPOSITEDATA, PAYMENTDATA) PAYMENTDATA,\r\n" + "       INSUM,\r\n"
				+ "       FEESUM,\r\n" + "       SESS_ID,\r\n" + "       IDTERM,\r\n" + "       (select ADDRESS\r\n"
				+ "          from Z_SB_TERMINAL_DBT t\r\n" + "         where rownum = 1\r\n"
				+ "           and lower(replace(NAME, ' ', '')) =\r\n"
				+ "               lower(replace(IDTERM, ' ', ''))) ADDRESS,\r\n" + "       RECEIVERSUM,\r\n"
				+ "       decode(SERVICE,\r\n" + "              'Оплата детского сада СБ',\r\n"
				+ "              substr(PAYMENTDATA, 1, instr(PAYMENTDATA, ',') - 1)) kindergarten,\r\n"
				+ "       trim(regexp_substr(PAYMENTDATA, '(.*?,){1}(.*?),', 1, 1, '', 2)) chgroup,\r\n"
				+ "       trim(decode(SERVICE,\r\n" + "                   'Оплата детского сада СБ',\r\n"
				+ "                   substr(PAYMENTDATA, instr(PAYMENTDATA, 'за') + 2))) period\r\n"
				+ "from Z_SB_TRANSACT_DBT t\n" + "where lower(fio) like '" + FIO + "'\n" + p_n + dt_betw
				+ "order by to_date(DATETIMEPAYMENT, 'dd-mm-rrrr HH24:MI:SS') desc\n";

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<TransactClass> empList = getEmployeeList(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT Terminal
	// *******************************
	public static ObservableList<TerminalClass> searchTerminal() {
		String selectStmt = "select * from Z_SB_TERMINAL_AMRA_DBT order by name";
		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<TerminalClass> empList = getTerminallist(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT Kash
	// *******************************
	public static ObservableList<KashClass> searchKash() {
		String selectStmt = 
				"select dp.cnameoper, dp.ckbk, dp.cpsevdo, a.C_CASHNAME \n" + 
				"  from ov_plat dp, OV_VCPLAT a\n" + 
				" where dp.IDOV_PLAT = a.IDOV_PLAT\n" + 
				"   and dp.idov_plat in (SELECT idov_plat_ FROM z_sb_psevdo_aggregate)\n";
		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<KashClass> empList = getKASHllist(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT Service
	// *******************************
	public static ObservableList<ServiceClass> searchService(String idterm) {
		String selectStmt = "select * from Z_SB_TERMSERV_AMRA_DBT t\n\r" + "where idterm = '" + idterm + "'\n\r";
		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<ServiceClass> empList = getServiceList(rsEmps);
		// Return employee object
		return empList;
	}

	// Select * from transact operation
	private static ObservableList<TransactClass> getEmployeeList(ResultSet rs) {
		// Declare a observable List which comprises of Employee objects
		ObservableList<TransactClass> empList = FXCollections.observableArrayList();
		try {
			while (rs.next()) {
				TransactClass tr = new TransactClass();
				tr.setPAYMENTNUMBER(rs.getString("PAYMENTNUMBER"));
				tr.setFIO(rs.getString("FIO"));
				tr.setDATETIMEPAYMENT(rs.getString("DATETIMEPAYMENT"));
				tr.setACCOUNT(rs.getString("ACCOUNT"));
				tr.setPAYMENTDATA(rs.getString("PAYMENTDATA"));
				tr.setINSUM(rs.getString("INSUM"));
				tr.setFEESUM(rs.getString("FEESUM"));
				tr.setSESS_ID(rs.getString("SESS_ID"));
				tr.setIDTERM(rs.getString("IDTERM"));
				tr.setADDRESS(rs.getString("ADDRESS"));
				tr.setRECEIVERSUM(rs.getString("RECEIVERSUM"));
				tr.setkindergarten(rs.getString("kindergarten"));
				tr.setchgroup(rs.getString("chgroup"));
				tr.setperiod(rs.getString("period"));
				// Add employee to the ObservableList
				empList.add(tr);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
		// return empList (ObservableList of Employees)
		return empList;
	}

	// Select * from transact operation
	private static ObservableList<ServiceClass> getServiceList(ResultSet rs) {
		// Declare a observable List which comprises of Employee objects
		ObservableList<ServiceClass> empList = FXCollections.observableArrayList();
		try {
			while (rs.next()) {
				ServiceClass sr = new ServiceClass();
				sr.setname(rs.getString("name"));
				sr.setacc_name(rs.getString("acc_name"));
				sr.setacc_rec(rs.getString("acc_rec"));
				sr.setaccount(rs.getString("account"));
				sr.setidterm(rs.getString("idterm"));
				sr.setinn(rs.getString("inn"));
				sr.setkbk(rs.getString("kbk"));
				sr.setkpp(rs.getString("kpp"));
				sr.setokato(rs.getString("okato"));
				sr.setbo1(rs.getString("bo1"));
				sr.setbo2(rs.getString("bo2"));
				sr.setcomission(rs.getString("comission"));
				empList.add(sr);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
		// return empList (ObservableList of Employees)
		return empList;
	}

	// Select * from transact operation
	private static ObservableList<TerminalClass> getTerminallist(ResultSet rs) {
		// Declare a observable List which comprises of Employee objects
		ObservableList<TerminalClass> empList = FXCollections.observableArrayList();
		try {
			while (rs.next()) {
				TerminalClass tr = new TerminalClass();
				tr.setACCOUNT(rs.getString("ACCOUNT"));
				tr.setDEPARTMENT(rs.getString("DEPARTMENT"));
				tr.setADDRESS(rs.getString("ADDRESS"));
				tr.setNAME(rs.getString("NAME"));
				tr.setGENERAL_ACC(rs.getString("general_acc"));
				tr.setCRASH_ACC(rs.getString("crash_acc"));
				tr.setDEAL_ACC(rs.getString("deal_acc"));
				tr.setGENERAL_COMIS(rs.getString("general_comis"));
				tr.setCLEAR_SUM(rs.getString("clear_sum"));
				tr.setINCOME(rs.getString("income"));
				empList.add(tr);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();
		}
		// return empList (ObservableList of Employees)
		return empList;
	}

	// Select * from transact operation
	private static ObservableList<KashClass> getKASHllist(ResultSet rs) {
		// Declare a observable List which comprises of Employee objects
		ObservableList<KashClass> empList = FXCollections.observableArrayList();
		try {
			while (rs.next()) {
				KashClass tr = new KashClass();
				tr.setckbk(rs.getString("ckbk"));
				tr.setcnameoper(rs.getString("cnameoper"));
				tr.setcpsevdo(rs.getString("cpsevdo"));
				tr.setC_CASHNAME(rs.getString("C_CASHNAME"));
				empList.add(tr);
			}
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("terminal.png"));
			alert.setTitle("Внимание");
			alert.setHeaderText(null);
			alert.setContentText(ExceptionUtils.getStackTrace(e));
			alert.showAndWait();

		}
		// return empList (ObservableList of Employees)
		return empList;
	}

	// *************************************
	// UPDATE
	// *************************************
	public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty()) {
			return false;
		}
		return true;
	}

	public static void updateTerminal(
			String NAME,
			String ACCOUNT, 
			String DEPARTMENT,
			String ADDRESS,
			String general_acc,
			String crash_acc,
			String deal_acc,
			String general_comis,
			String clear_sum,
			String income,
			String BeforeName) {
		
		// Declare a UPDATE statement
		String NAME_ = "";
		String ACCOUNT_ = "";
		String DEPARTMENT_ = "";
		String ADDRESS_ = "";
		String general_acc_ = "";
		String crash_acc_ = "";
		String deal_acc_ = "";
		String general_comis_ = "";
		String clear_sum_ = "";
		String income_ = "";

		if (isNullOrEmpty(NAME)) {

		} else {
			NAME_ = NAME;
		}
		if (isNullOrEmpty(ACCOUNT)) {

		} else {
			ACCOUNT_ = ACCOUNT;
		}
		if (isNullOrEmpty(DEPARTMENT)) {

		} else {
			DEPARTMENT_ = DEPARTMENT;
		}
		if (isNullOrEmpty(ADDRESS)) {

		} else {
			ADDRESS_ = ADDRESS;
		}
		if (isNullOrEmpty(general_acc)) {

		} else {
			general_acc_ = general_acc;
		}
		if (isNullOrEmpty(crash_acc)) {

		} else {
			crash_acc_ = crash_acc;
		}
		if (isNullOrEmpty(deal_acc)) {

		} else {
			deal_acc_ = deal_acc;
		}
		if (isNullOrEmpty(general_comis)) {

		} else {
			general_comis_ = general_comis;
		}
		if (isNullOrEmpty(clear_sum)) {

		} else {
			clear_sum_ = clear_sum;
		}
		if (isNullOrEmpty(income)) {

		} else {
			income_ = income;
		}
		
		String updateStmt = "BEGIN\n" 
		        + "   "
				+ "UPDATE Z_SB_TERMINAL_DBT\n" 
		        + "      SET NAME = '" + NAME_ + "',\n"
				+ "ACCOUNT = '" + ACCOUNT_ + "',\n" 
		        + "DEPARTMENT = '" + DEPARTMENT_ + "',\n" 
				+ "ADDRESS = '" + ADDRESS_+ "',\n" 
		        + "general_acc = '" + general_acc_ + "',\n" 
				+ "crash_acc = '" + crash_acc_ + "',\n"
				+ "deal_acc = '" + deal_acc_ + "',\n" 
				+ "general_comis = '" + general_comis_ + "',\n"
				+ "clear_sum = '" + clear_sum_ + "',\n" 
				+ "income = '" + income_ + "'\n" + "',\n"
				+ " WHERE NAME = '" + BeforeName + "';\n"
				+ "   COMMIT;\n" + "END;";
		System.out.println(updateStmt);
		DBUtil.dbExecuteUpdate(updateStmt);
	}

	public static void updateService(
			String acc_name,
			String acc_rec,
			String account,
			String idterm,
			String inn, 
			String kbk, 
			String kpp,
			String name,
			String okato,
			String bo1,
			String bo2,
			String BeforeAcc,
			String Beforeidterm,
			String Beforename,
			String comission) {
		// Declare a UPDATE statement
		String acc_name_ = "";
		String acc_rec_ = "";
		String account_ = "";
		String idterm_ = "";
		String inn_ = "";
		String kbk_ = "";
		String kpp_ = "";
		String name_ = "";
		String okato_ = "";
		String bo1_ = "";
		String bo2_ = "";
		String comission_="";

		if (isNullOrEmpty(acc_name)) {

		} else {
			acc_name_ = acc_name;
		}
		if (isNullOrEmpty(acc_rec)) {

		} else {
			acc_rec_ = acc_rec;
		}
		if (isNullOrEmpty(account)) {

		} else {
			account_ = account;
		}
		if (isNullOrEmpty(idterm)) {

		} else {
			idterm_ = idterm;
		}
		if (isNullOrEmpty(inn)) {

		} else {
			inn_ = inn;
		}
		if (isNullOrEmpty(kbk)) {

		} else {
			kbk_ = kbk;
		}
		if (isNullOrEmpty(kpp)) {

		} else {
			kpp_ = kpp;
		}
		if (isNullOrEmpty(name)) {

		} else {
			name_ = name;
		}
		if (isNullOrEmpty(okato)) {

		} else {
			okato_ = okato;
		}
		if (isNullOrEmpty(bo1)) {

		} else {
			bo1_ = bo1;
		}
		if (isNullOrEmpty(bo2)) {

		} else {
			bo2_ = bo2;
		}
		if (isNullOrEmpty(comission)) {

		} else {
			comission_ = comission;
		}
		
		String acc_name_C = isNullOrEmpty(acc_name) ? "" : "acc_name = '" + acc_name + "',";
		String acc_rec_C = isNullOrEmpty(acc_rec) ? "" : "acc_rec = '" + acc_rec + "',";
		String account_C = isNullOrEmpty(account) ? "" : "account = '" + account + "',";
		String idterm_C = isNullOrEmpty(idterm) ? "" : "idterm = '" + idterm + "',";
		String inn_C = isNullOrEmpty(inn) ? "" : "inn = '" + inn + "',";
		String kbk_C = isNullOrEmpty(kbk) ? "" : "kbk = '" + kbk + "',";
		String kpp_C = isNullOrEmpty(kpp) ? "" : "kpp = '" + kpp + "',";
		String name_C = isNullOrEmpty(name) ? "" : "name = '" + name + "',";
		String okato_C = isNullOrEmpty(okato) ? "" : "okato = '" + okato + "',";
		String bo1_C = isNullOrEmpty(bo1) ? "" : "bo1 = '" + bo1 + "',";
		String bo2_C = isNullOrEmpty(bo2) ? "" : "bo2 = '" + bo2 + "',";
		String comission_C = isNullOrEmpty(comission) ? "" : "comission = '" + comission + "',";

		String param = acc_name_C + acc_rec_C + account_C  + idterm_C
				+ inn_C + kbk_C  + kpp_C + name_C + okato_C + bo1_C + bo2_C +comission_C;

		String updateStmt = 
		"BEGIN\n" 
		+ "   UPDATE Z_SB_TERMSERV_AMRA_DBT\n" 
						+ "SET " 
		+ "acc_name = '" + acc_name_ + "', "
				+ "acc_rec = '"+ acc_rec_ + "', " 
				+ "account = '"+ account_ + "', " 
				+ "idterm = '"+ idterm_ + "', " 
				+ "inn = '" + inn_ + "', " 
				+ "kbk = '" + kbk_+ "', " 
				+ "kpp = '" + kpp_ + "', " 
				+ "name = '" + name_+ "', " 
				+ "okato = '" + okato_ + "', " 
				+ "bo1 = '" + bo1_ + "', " 
				+ "bo2 = '" + bo2_  + "',"
				+ "comission = " + comission_ 
				+ " WHERE ACCOUNT = '" + BeforeAcc + "' and idterm = '" + Beforeidterm
				+ "' and name = '" + Beforename + "';\n" 
				+ "   COMMIT;\n" 
	   + "END;";
		DBUtil.dbExecuteUpdate(updateStmt);
	}

	public static void InsertService(
			String acc_name, 
			String acc_rec,
			String account,
			String idterm,
			String inn,
			String kbk,
			String kpp,
			String name,
			String okato,
			String bo1, 
			String bo2, 
			String comission) {
		// Declare a UPDATE statement
		String acc_name_ = "";
		String acc_rec_ = "";
		String account_ = "";
		String idterm_ = "";
		String inn_ = "";
		String kbk_ = "";
		String kpp_ = "";
		String name_ = "";
		String okato_ = "";
		String bo1_ = "";
		String bo2_ = "";
		String comission_="";

		if (isNullOrEmpty(acc_name)) {

		} else {
			acc_name_ = "'" + acc_name + "',";
		}
		if (isNullOrEmpty(acc_rec)) {

		} else {
			acc_rec_ = "'" + acc_rec + "',";
		}
		if (isNullOrEmpty(account)) {

		} else {
			account_ = "'" + account + "',";
		}
		if (isNullOrEmpty(idterm)) {

		} else {
			idterm_ = "'" + idterm + "',";
		}
		if (isNullOrEmpty(inn)) {

		} else {
			inn_ = "'" + inn + "',";
		}
		if (isNullOrEmpty(kbk)) {

		} else {
			kbk_ = "'" + kbk + "',";
		}
		
		if (isNullOrEmpty(kpp)) {

		} else {
			kpp_ = "'" + kpp + "',";
		}
		if (isNullOrEmpty(name)) {

		} else {
			name_ = "'" + name + "',";
		}
		if (isNullOrEmpty(okato)) {

		} else {
			okato_ = "'" + okato + "',";
		}
		if (isNullOrEmpty(bo1)) {

		} else {
			bo1_ = "'" + bo1 + "',";
		}
		if (isNullOrEmpty(bo2)) {

		} else {
			bo2_ = "'" + bo2 + "',";
		}
		if (isNullOrEmpty(comission_)) {

		} else {
			comission_ = "'" + comission+ "',";
		}
		
		String acc_name_C = isNullOrEmpty(acc_name) ? "" : "acc_name,";
		String acc_rec_C = isNullOrEmpty(acc_rec) ? "" : "acc_rec,";
		String account_C = isNullOrEmpty(account) ? "" : "account,";
		String idterm_C = isNullOrEmpty(idterm) ? "" : "idterm,";
		String inn_C = isNullOrEmpty(inn) ? "" : "inn,";
		String kbk_C = isNullOrEmpty(kbk) ? "" : "kbk,";
		String kpp_C = isNullOrEmpty(kpp) ? "" : "kpp,";
		String name_C = isNullOrEmpty(name) ? "" : "name,";
		String okato_C = isNullOrEmpty(okato) ? "" : "okato,";
		String bo1_C = isNullOrEmpty(bo1) ? "" : "bo1,";
		String bo2_C = isNullOrEmpty(bo2) ? "" : "bo2,";
		String comission_C = isNullOrEmpty(comission) ? "" : "comission,";

		String Values = "(" + acc_name_ + acc_rec_ + account_  + idterm_
				+ inn_ + kbk_  + kpp_ + name_ + okato_ + bo1_ + bo2_  + comission_+");\n";
		String Columns = "(" + acc_name_C + acc_rec_C + account_C 
				+ idterm_C + inn_C + kbk_C  + kpp_C + name_C + okato_C + bo1_C + bo2_C 
				+ comission_C+ ")\n ";
		String insertStmt = "BEGIN\n" + "   insert into  Z_SB_TERMSERV_AMRA_DBT \n" + Columns.replace(",)", ")")
				+ "      values \n" + Values.replace(",)", ")") + "   COMMIT;\n" + "END;";
		System.out.println(insertStmt);
		DBUtil.dbExecuteUpdate(insertStmt);
	}

	// *************************************
	// DELETE
	// *************************************
	public static void deleteTerminal(String NAME) {
		// Declare a DELETE statement
		String updateStmt = "BEGIN\n" + "   DELETE FROM Z_SB_TERMINAL_AMRA_DBT\n" + "         WHERE NAME ='" + NAME + "';\n"
				+ "   COMMIT;\n" + "END;";

		System.out.print(updateStmt);
		DBUtil.dbExecuteUpdate(updateStmt);
	}

	public static void kash_psevdo() {

		String update_psevdo = "BEGIN\n" + "   update_psevdonim_idovplat;\n" + "   COMMIT;\n" + "END;";

		DBUtil.dbExecuteUpdate(update_psevdo);
	}

	public static void delete_kash_psevdo() {

		String update_psevdo = "BEGIN\n" + "   delete from z_sb_psevdo_aggregate;\n" + "   COMMIT;\n" + "END;";

		DBUtil.dbExecuteUpdate(update_psevdo);
	}

	public static void deleteService(String idterm, String ACCOUNT, String name) {
		// Declare a DELETE statement
		String updateStmt = "BEGIN\n" + "   DELETE FROM Z_SB_TERMSERV_AMRA_DBT\n" + "         WHERE idterm ='" + idterm
				+ "' and account = '" + ACCOUNT + "' and name = '" + name + "';\n" + "   COMMIT;\n" + "END;";

		System.out.print(updateStmt);
		DBUtil.dbExecuteUpdate(updateStmt);
	}

	// *************************************
	// INSERT an employee
	// *************************************
	public static void InsertTerminal(
			String NAME,
			String ACCOUNT, 
			String DEPARTMENT,
			String ADDRESS,
			String general_acc,
			String crash_acc,
			String deal_acc,
			String general_comis,
			String clear_sum,
			String income
			) {
		// Declare a INSET statement
		String NAME_ = "";
		String ACCOUNT_ = "";
		String DEPARTMENT_ = "";
		String ADDRESS_ = "";
		String general_acc_ = "";
		String crash_acc_ = "";
		String deal_acc_ = "";
		String general_comis_ = "";
		String clear_sum_ = "";
		String income_ = "";

		if (isNullOrEmpty(NAME)) {

		} else {
			NAME_ = "'" + NAME + "',";
		}
		if (isNullOrEmpty(ACCOUNT)) {

		} else {
			ACCOUNT_ = "'" + ACCOUNT + "',";
		}
		if (isNullOrEmpty(DEPARTMENT)) {

		} else {
			DEPARTMENT_ = "'" + DEPARTMENT + "',";
		}
		if (isNullOrEmpty(ADDRESS)) {

		} else {
			ADDRESS_ = "'" + ADDRESS + "',";
		}
		if (isNullOrEmpty(general_acc)) {

		} else {
			general_acc_ = "'" + general_acc + "',";
		}
		if (isNullOrEmpty(crash_acc)) {

		} else {
			crash_acc_ = "'" + crash_acc + "',";
		}
		if (isNullOrEmpty(deal_acc)) {

		} else {
			deal_acc_ = "'" + deal_acc + "',";
		}
		if (isNullOrEmpty(general_comis)) {

		} else {
			general_comis_ = "'" + general_comis + "',";
		}
		if (isNullOrEmpty(clear_sum)) {

		} else {
			clear_sum_ = "'" + clear_sum + "',";
		}
		if (isNullOrEmpty(income)) {
		} 
		 else {
			income_ = "'" + income + "',";
		}
		

		String NAME_C = isNullOrEmpty(NAME) ? "" : "NAME,";
		String ACCOUNT_C = isNullOrEmpty(ACCOUNT) ? "" : "ACCOUNT,";
		String DEPARTMENT_C = isNullOrEmpty(DEPARTMENT) ? "" : "DEPARTMENT,";
		String ADDRESS_C = isNullOrEmpty(ADDRESS) ? "" : "ADDRESS,";
		String general_acc_C = isNullOrEmpty(general_acc) ? "" : "general_acc,";
		String crash_acc_C = isNullOrEmpty(crash_acc) ? "" : "crash_acc,";
		String deal_acc_C = isNullOrEmpty(deal_acc) ? "" : "deal_acc,";
		String general_comis_C = isNullOrEmpty(general_comis) ? "" : "general_comis,";
		String clear_sum_C = isNullOrEmpty(clear_sum) ? "" : "clear_sum,";
		String income_C = isNullOrEmpty(income) ? "" : "income,";

		
		String Values = "(" + NAME_ + ACCOUNT_ + DEPARTMENT_ + ADDRESS_ + general_acc_ + crash_acc_ + deal_acc_
				+ general_comis_ + clear_sum_ + income_ + ");\n ";
		String Columns = "(" + NAME_C + ACCOUNT_C + DEPARTMENT_C + ADDRESS_C + general_acc_C + crash_acc_C
				+ deal_acc_C + general_comis_C + clear_sum_C + income_C  + ")\n ";
		
		String insertStmt = "BEGIN\n"
				+ "insert into  Z_SB_TERMINAL_AMRA_DBT \n" 
				+ Columns.replace(",)", ")")
				+ "      values \n" + Values.replace(",)", ")") 
				+ "   COMMIT;\n" + "END;";
		System.out.println(insertStmt);
		DBUtil.dbExecuteUpdate(insertStmt);
	}
}

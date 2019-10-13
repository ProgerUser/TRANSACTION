package sample.model;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TerminalDAO {

	// *******************************
	// SELECT an Employee
	// *******************************
	public static Transact searchTransact(String fio) throws SQLException, ClassNotFoundException {
		// Declare a SELECT statement
		String selectStmt = "SELECT * FROM Z_SB_TRANSACT_DBT WHERE lower(FIO) like '" + fio + "'";

		// select PAYMENTNUMBER, FIO, DATETIMEPAYMENT, ACCOUNT, PAYMENTDATA from
		// Z_SB_TRANSACT_DBT t"

		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeFromResultSet method and get employee object
			Transact transact = getEmployeeFromResultSet(rsEmp);

			// Return employee object
			return transact;
		} catch (SQLException e) {
			System.out.println("При поиске Транзакции с " + fio + " FIO, произошла ошибка: " + e);
			// Return exception
			throw e;
		}
	}

	// Use ResultSet from DB as parameter and set Employee Object's attributes and
	// return employee object.
	private static Transact getEmployeeFromResultSet(ResultSet rs) throws SQLException {
		Transact tr = null;
		while (rs.next()) {
			tr = new Transact();
			tr.setPAYMENTNUMBER(rs.getString("PAYMENTNUMBER"));
			tr.setFIO(rs.getString("FIO"));
			tr.setDATETIMEPAYMENT(rs.getString("DATETIMEPAYMENT"));
			tr.setACCOUNT(rs.getString("ACCOUNT"));
			tr.setPAYMENTDATA(rs.getString("PAYMENTDATA"));
			tr.setINSUM(rs.getString("INSUM"));
			tr.setFEESUM(rs.getString("FEESUM"));
			tr.setSESS_ID(rs.getString("SESS_ID"));
		}
		return tr;
	}

	// *******************************
	// SELECT Transact
	// *******************************
	public static ObservableList<Transact> searchEmployees(String FIO, String PAYMENTNUMBER, String DT1, String DT2)
			throws SQLException, ClassNotFoundException {
		// Declare a SELECT statement
		String dt_betw = "\n";
		String p_n = "\n";
		if (PAYMENTNUMBER.equals("")) {

		} else {
			p_n = "and PAYMENTNUMBER like '" + PAYMENTNUMBER + "'\n";
		}

		if (DT1.equals("") & DT2.equals("")) {

		} else {
			dt_betw = "and to_date(DATETIMEPAYMENT, 'dd-mm-rrrr HH24:MI:SS') between to_date('" + DT1
					+ "','dd-mm-rrrr') and to_date('" + DT2 + "','dd-mm-rrrr')\n";
		}

		String selectStmt = "select PAYMENTNUMBER,\n" + "FIO,\n" + "DATETIMEPAYMENT,\n" + "ACCOUNT,\n"
				+ "decode(PAYMENTDATA, null, COMPOSITEDATA, PAYMENTDATA) PAYMENTDATA,\n" + "INSUM,\n" + "FEESUM,\n"
				+ "SESS_ID\n" + "from Z_SB_TRANSACT_DBT t\n" + "where lower(fio) like '" + FIO + "'\n" + p_n + dt_betw
				+ "order by to_date(DATETIMEPAYMENT, 'dd-mm-rrrr HH24:MI:SS') desc\n";

		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Transact> empList = getEmployeeList(rsEmps);

			// Return employee object
			return empList;
		} catch (SQLException e) {
			System.out.println("Операция выбора SQL не удалась: " + e);
			// Return exception
			throw e;
		}
	}

	// *******************************
	// SELECT FN_SESS
	// *******************************
	public static ObservableList<FN_SESS_AMRA> srch_fn_sess(String SESS_ID, String PAYMENTNUMBER, LocalDate dt1,
			LocalDate dt2) throws SQLException, ClassNotFoundException, ParseException {
		// Declare a SELECT statement

		String dt_betw = "\n";
		String p_n = "\n";
		String clob = "\n";

		if (PAYMENTNUMBER.equals("")) {

		} else {
			clob = "and FILECLOB like '%" + PAYMENTNUMBER + "%'\n";
		}

		if (SESS_ID.equals("")) {

		} else {
			p_n = "and SESS_ID = '" + SESS_ID + "'\n";
		}

		String ldt1 = null;
		String ldt2 = null;

		if (dt1 != null)
			ldt1 = dt1.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		if (dt2 != null)
			ldt2 = dt2.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		String ldt1_ = "\n";
		String ldt2_ = "\n";
		String bt = "\n";
		if (dt1 != null & dt2 != null) {
			bt = " and trunc(date_time) between to_date('" + ldt1 + "','dd.mm.yyyy') and to_date('" + ldt2
					+ "','dd.mm.yyyy') \n";
		} else if (dt1 != null & dt2 == null) {
			ldt1_ = " and trunc(date_time) = to_date('" + ldt1 + "','dd.mm.yyyy')\n";
		} else if (dt1 == null & dt2 != null) {
			ldt2_ = " and trunc(date_time) = to_date('" + ldt2 + "','dd.mm.yyyy')\n";
		}

		String selectStmt = "" + "select sess_id,\n " + "file_name, \n" + "date_time, \n" + "fileclob \n"
				+ "from Z_SB_FN_SESS_AMRA \n" + "where 1=1" + ldt1_ + ldt2_ + p_n + bt + clob
				+ "order by date_time desc";

		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<FN_SESS_AMRA> empList = get_fn_sess(rsEmps);

			// Return employee object
			return empList;
		} catch (SQLException e) {
			System.out.println("Операция выбора SQL не удалась: " + e);
			// Return exception
			throw e;
		}
	}

	// *******************************
	// SELECT FN_SESS
	// *******************************
	public static ObservableList<Amra_Trans> Amra_Trans_(String SESS_ID, LocalDate dt1, LocalDate dt2)
			throws SQLException, ClassNotFoundException, ParseException {
		String ldt1 = null;
		String ldt2 = null;

		if (dt1 != null)
			ldt1 = dt1.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		if (dt2 != null)
			ldt2 = dt2.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		String ldt1_ = "\n";
		String ldt2_ = "\n";
		String bt = "\n";
		if (dt1 != null & dt2 != null) {
			bt = " and trunc(paydate) between to_date('" + ldt1 + "','dd.mm.yyyy') and to_date('" + ldt2
					+ "','dd.mm.yyyy') \n";
		} else if (dt1 != null & dt2 == null) {
			ldt1_ = " and trunc(paydate) = to_date('" + ldt1 + "','dd.mm.yyyy')\n";
		} else if (dt1 == null & dt2 != null) {
			ldt2_ = " and trunc(paydate) = to_date('" + ldt2 + "','dd.mm.yyyy')\n";
		}

		String selectStmt = " select rownum,t.* from (select rownum,t.* from Z_SB_TRANSACT_AMRA_DBT t where sess_id = "
				+ SESS_ID + ldt1_ + ldt2_ + bt + " order by PAYDATE desc) t";

		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Amra_Trans> empList = get_amra_trans(rsEmps);

			// Return employee object
			return empList;
		} catch (SQLException e) {
			System.out.println("Операция выбора SQL не удалась: " + e);
			// Return exception
			throw e;
		}
	}

	// *******************************
	// SELECT Attributes
	// *******************************
	public static ObservableList<Attributes> Attributes_() throws SQLException, ClassNotFoundException, ParseException {

		String selectStmt = "SELECT Service, CheckNumber, AttributeName, AttributeValue\n"
				+ "  FROM (select ATTRIBUTES_\n" + "          from Z_SB_TRANSACT_AMRA_DBT\n"
				+ "         where CHECKNUMBER = '" + Connect.PNMB_ + "'),\n"
				+ "       XMLTABLE('/Атрибуты/Атр' PASSING xmltype(ATTRIBUTES_) COLUMNS\n"
				+ "                Service VARCHAR2(500) PATH '@Услуга',\n"
				+ "                CheckNumber VARCHAR2(500) PATH '@НомерЧека',\n"
				+ "                AttributeName VARCHAR2(500) PATH '@ИмяАтрибута',\n"
				+ "                AttributeValue VARCHAR2(500) PATH '@ЗначениеАтрибута')";

		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Attributes> empList = get_attr(rsEmps);

			// Return employee object
			return empList;
		} catch (SQLException e) {
			System.out.println("Операция выбора SQL не удалась: " + e);
			// Return exception
			throw e;
		}
	}

	// *******************************
	// SELECT Termdial_
	// *******************************
	public static ObservableList<Termdial> Termdial_(LocalDate dt1, LocalDate dt2, String pnmb, String sess_id)
			throws SQLException, ClassNotFoundException, ParseException {

		String pnmb_ = "\n";
		String sess_id_ = "\n";

		String ldt1 = null;
		String ldt2 = null;

		if (dt1 != null)
			ldt1 = dt1.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		if (dt2 != null)
			ldt2 = dt2.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		String ldt1_ = "\n";
		String ldt2_ = "\n";
		String bt = "\n";

		if (dt1 != null & dt2 != null) {
			bt = " and trunc(recdate) between to_date('" + ldt1 + "','dd.mm.yyyy') and to_date('" + ldt2
					+ "','dd.mm.yyyy') \n";
		} else if (dt1 != null & dt2 == null) {
			ldt1_ = " and trunc(recdate) = to_date('" + ldt1 + "','dd.mm.yyyy')\n";
		} else if (dt1 == null & dt2 != null) {
			ldt2_ = " and trunc(recdate) = to_date('" + ldt2 + "','dd.mm.yyyy')\n";
		}

		if (pnmb.equals("")) {

		} else {
			pnmb_ = "and PAYMENTNUMBER like '%" + pnmb + "%'\n";
		}
		if (sess_id.equals("")) {

		} else {
			sess_id_ = "and SESS_ID = '" + sess_id + "'\n";
		}

		String selectStmt = "select * from z_sb_termdeal_amra_dbt where 1=1" + ldt1_ + bt + ldt2_ + pnmb_ + sess_id_;

		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Termdial> empList = get_term(rsEmps);

			// Return employee object
			return empList;
		} catch (SQLException e) {
			System.out.println("Операция выбора SQL не удалась: " + e);
			// Return exception
			throw e;
		}
	}

	// Select * from fn_sess operation
	private static ObservableList<FN_SESS_AMRA> get_fn_sess(ResultSet rs)
			throws SQLException, ClassNotFoundException, ParseException {
		ObservableList<FN_SESS_AMRA> fn_list = FXCollections.observableArrayList();
		while (rs.next()) {
			FN_SESS_AMRA fn = new FN_SESS_AMRA();
			fn.setsess_id(rs.getString("sess_id"));
			fn.setfile_name(rs.getString("file_name"));
			fn.setdate_time(rs.getString("date_time"));
			fn_list.add(fn);
		}
		return fn_list;
	}

	// Select * from fn_sess operation
	private static ObservableList<Termdial> get_term(ResultSet rs)
			throws SQLException, ClassNotFoundException, ParseException {
		ObservableList<Termdial> fn_list = FXCollections.observableArrayList();
		while (rs.next()) {
			Termdial td = new Termdial();
			// String format = new SimpleDateFormat("MM.dd.yyyy
			// HH:mm:ss").format(rs.getTimestamp("recdate"));
			td.set_recdate(rs.getString("recdate"));
			td.set_department(rs.getString("department"));
			td.set_paymentnumber(rs.getString("paymentnumber"));
			td.set_dealstartdate(rs.getString("dealstartdate"));
			td.set_sum_(rs.getString("sum_"));
			td.set_dealenddate(rs.getString("dealenddate"));
			td.set_dealpaymentnumber(rs.getString("dealpaymentnumber"));
			td.set_status(rs.getString("status"));
			td.set_sess_id(rs.getString("sess_id"));
			fn_list.add(td);
		}
		return fn_list;
	}

	// Select * from fn_sess operation
	private static ObservableList<Attributes> get_attr(ResultSet rs)
			throws SQLException, ClassNotFoundException, ParseException {
		ObservableList<Attributes> fn_list = FXCollections.observableArrayList();
		while (rs.next()) {
			Attributes fn = new Attributes();
			fn.set_Service(rs.getString("Service"));
			fn.set_AttributeName(rs.getString("AttributeName"));
			fn.set_AttributeValue(rs.getString("AttributeValue"));
			fn.set_CheckNumber(rs.getString("CheckNumber"));
			fn_list.add(fn);
		}
		return fn_list;
	}

	// Select * from fn_sess operation
	private static ObservableList<Amra_Trans> get_amra_trans(ResultSet rs)
			throws SQLException, ClassNotFoundException, ParseException {
		ObservableList<Amra_Trans> fn_list = FXCollections.observableArrayList();
		while (rs.next()) {
			Amra_Trans fn = new Amra_Trans();

			// String recdate = new SimpleDateFormat("d.M.yyyy
			// hh24:mm:ss").format(rs.getTimestamp("recdate"));
			// String paydate = new SimpleDateFormat("dd.M.yyyy
			// hh24:mm:ss").format(rs.getTimestamp("paydate"));

			fn.set_rownum(rs.getString("rownum"));
			fn.set_recdate(rs.getString("recdate"));
			fn.set_paydate(rs.getString("paydate"));
			fn.set_currency(rs.getString("currency"));
			fn.set_paymenttype(rs.getString("paymenttype"));
			fn.set_vk(rs.getString("vk"));
			fn.set_dateofoperation(rs.getString("dateofoperation"));
			fn.set_dataps(rs.getString("dataps"));
			fn.set_dateclearing(rs.getString("dateclearing"));
			fn.set_dealer(rs.getString("dealer"));
			fn.set_accountpayer(rs.getString("accountpayer"));
			fn.set_cardnumber(rs.getString("cardnumber"));
			fn.set_operationnumber(rs.getString("operationnumber"));
			fn.set_operationnumberdelivery(rs.getString("operationnumberdelivery"));
			fn.set_checknumber(rs.getString("checknumber"));
			fn.set_checkparent(rs.getString("checkparent"));
			fn.set_orderofprovidence(rs.getString("orderofprovidence"));
			fn.set_provider(rs.getString("provider"));
			fn.set_owninown(rs.getString("owninown"));
			fn.set_corrected(rs.getString("corrected"));
			fn.set_commissionrate(rs.getString("commissionrate"));
			fn.set_status(rs.getString("status"));
			fn.set_stringfromfile(rs.getString("stringfromfile"));
			fn.set_rewardamount(rs.getString("rewardamount"));
			fn.set_ownerincomeamount(rs.getString("ownerincomeamount"));
			fn.set_commissionamount(rs.getString("commissionamount"));
			fn.set_nkamount(rs.getString("nkamount"));
			fn.set_maxcommissionamount(rs.getString("maxcommissionamount"));
			fn.set_mincommissionamount(rs.getString("mincommissionamount"));
			fn.set_cashamount(rs.getString("cashamount"));
			fn.set_sumnalprimal(rs.getString("sumnalprimal"));
			fn.set_amounttocheck(rs.getString("amounttocheck"));
			fn.set_amountofpayment(rs.getString("amountofpayment"));
			fn.set_sumofsplitting(rs.getString("sumofsplitting"));
			fn.set_amountintermediary(rs.getString("amountintermediary"));
			fn.set_amountofscs(rs.getString("amountofscs"));
			fn.set_amountwithchecks(rs.getString("amountwithchecks"));
			fn.set_counter(rs.getString("counter"));
			fn.set_terminal(rs.getString("terminal"));
			fn.set_terminalnetwork(rs.getString("terminalnetwork"));
			fn.set_transactiontype(rs.getString("transactiontype"));
			fn.set_service(rs.getString("service"));
			fn.set_filetransactions(rs.getString("filetransactions"));
			fn.set_fio(rs.getString("fio"));
			fn.set_checksincoming(rs.getString("checksincoming"));
			fn.set_barcode(rs.getString("barcode"));
			fn.set_isaresident(rs.getString("isaresident"));
			fn.set_valuenotfound(rs.getString("valuenotfound"));
			fn.set_providertariff(rs.getString("providertariff"));
			fn.set_counterchecks(rs.getString("counterchecks"));
			fn.set_countercheck(rs.getString("countercheck"));
			fn.set_id_(rs.getString("id_"));
			fn.set_detailing(rs.getString("detailing"));
			fn.set_walletpayer(rs.getString("walletpayer"));
			fn.set_walletreceiver(rs.getString("walletreceiver"));
			fn.set_purposeofpayment(rs.getString("purposeofpayment"));
			fn.set_dataprovider(rs.getString("dataprovider"));
			fn.set_statusabs(rs.getString("statusabs"));
			fn.set_sess_id(rs.getString("sess_id"));

			fn_list.add(fn);
		}
		return fn_list;
	}

	// Select * from employees operation
	private static ObservableList<Transact> getEmployeeList(ResultSet rs) throws SQLException, ClassNotFoundException {
		// Declare a observable List which comprises of Employee objects
		ObservableList<Transact> empList = FXCollections.observableArrayList();
		while (rs.next()) {
			Transact tr = new Transact();
			tr.setPAYMENTNUMBER(rs.getString("PAYMENTNUMBER"));
			tr.setFIO(rs.getString("FIO"));
			tr.setDATETIMEPAYMENT(rs.getString("DATETIMEPAYMENT"));
			tr.setACCOUNT(rs.getString("ACCOUNT"));
			tr.setPAYMENTDATA(rs.getString("PAYMENTDATA"));
			tr.setINSUM(rs.getString("INSUM"));
			tr.setFEESUM(rs.getString("FEESUM"));
			tr.setSESS_ID(rs.getString("SESS_ID"));
			// Add employee to the ObservableList
			empList.add(tr);
		}
		// return empList (ObservableList of Employees)
		return empList;
	}

	// *************************************
	// UPDATE an employee's email address
	// *************************************
	public static void updateEmpEmail(String empId, String empEmail) throws SQLException, ClassNotFoundException {
		// Declare a UPDATE statement
		String updateStmt = "BEGIN\n" + "   UPDATE employees\n" + "      SET EMAIL = '" + empEmail + "'\n"
				+ "    WHERE EMPLOYEE_ID = " + empId + ";\n" + "   COMMIT;\n" + "END;";

		// Execute UPDATE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while UPDATE Operation: " + e);
			throw e;
		}
	}

	// *************************************
	// DELETE an employee
	// *************************************
	public static void deleteEmpWithId(String empId) throws SQLException, ClassNotFoundException {
		// Declare a DELETE statement
		String updateStmt = "BEGIN\n" + "   DELETE FROM employees\n" + "         WHERE employee_id =" + empId + ";\n"
				+ "   COMMIT;\n" + "END;";

		// Execute UPDATE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while DELETE Operation: " + e);
			throw e;
		}
	}

	// *************************************
	// INSERT an employee
	// *************************************
	public static void insertEmp(String name, String lastname, String email)
			throws SQLException, ClassNotFoundException {
		// Declare a DELETE statement
		String updateStmt = "BEGIN\n" + "INSERT INTO employees\n"
				+ "(EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, HIRE_DATE, JOB_ID)\n" + "VALUES\n"
				+ "(sequence_employee.nextval, '" + name + "', '" + lastname + "','" + email
				+ "', SYSDATE, 'IT_PROG');\n" + "END;";

		// Execute DELETE operation
		try {
			DBUtil.dbExecuteUpdate(updateStmt);
		} catch (SQLException e) {
			System.out.print("Error occurred while DELETE Operation: " + e);
			throw e;
		}
	}
}

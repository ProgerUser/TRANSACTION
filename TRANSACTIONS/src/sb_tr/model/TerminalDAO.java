package sb_tr.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sb_tr.util.DBUtil;

public class TerminalDAO {

	// *******************************
	// SELECT an Employee
	// *******************************
	public static Transact searchTransact(String fio) {
		// Declare a SELECT statement
		String selectStmt = "SELECT * FROM Z_SB_TRANSACT_DBT WHERE lower(FIO) like '" + fio + "'";

		// select PAYMENTNUMBER, FIO, DATETIMEPAYMENT, ACCOUNT, PAYMENTDATA from
		// Z_SB_TRANSACT_DBT t"

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeFromResultSet method and get employee object
		Transact transact = getEmployeeFromResultSet(rsEmp);

		// Return employee object
		return transact;
	}

	// Use ResultSet from DB as parameter and set Employee Object's attributes and
	// return employee object.
	private static Transact getEmployeeFromResultSet(ResultSet rs) {
		try {
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
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;

	}

	// *******************************
	// SELECT Transact
	// *******************************
	public static ObservableList<Transact> searchEmployees(String FIO, String PAYMENTNUMBER, String DT1, String DT2) {
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

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Transact> empList = getEmployeeList(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT FN_SESS
	// *******************************
	public static ObservableList<FN_SESS_AMRA> srch_fn_sess(String SESS_ID, String PAYMENTNUMBER, LocalDate dt1,
			LocalDate dt2) {
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
			bt = " and trunc(DateOfOperation) between to_date('" + ldt1 + "','dd.mm.yyyy') and to_date('" + ldt2
					+ "','dd.mm.yyyy') \n";
		} else if (dt1 != null & dt2 == null) {
			ldt1_ = " and trunc(DateOfOperation) >= to_date('" + ldt1 + "','dd.mm.yyyy')\n";
		} else if (dt1 == null & dt2 != null) {
			ldt2_ = " and trunc(DateOfOperation) <= to_date('" + ldt2 + "','dd.mm.yyyy')\n";
		}

		String selectStmt = "select * from ( select sess_id,\n" + 
				"       file_name,\n" + 
				"       date_time,\n" + 
				"       fileclob,\n" + 
				"       case\n" + 
				"         when status = 0 then\n" + 
				"          'Загружен'\n" + 
				"         when status = 1 then\n" + 
				"          'Разобран'\n" + 
				"         when status = 2 then\n" + 
				"          'Рассчитан'\n" + 
				"       end status,\n" +
				"  case\n" + 
				"         when length(z_sb_fn_sess_getdate_clob(SESS_ID)) > 10 then\n" + 
				"          to_date(substr(z_sb_fn_sess_getdate_clob(SESS_ID),\n" + 
				"                 1,\n" + 
				"                 instr(z_sb_fn_sess_getdate_clob(SESS_ID), '-') - 1))\n" + 
				"                 else\n" + 
				"                  to_date(z_sb_fn_sess_getdate_clob(SESS_ID)) \n" + 
				"       end DateOfOperation,"+
				"       path,\n" + 
				"       user_\n" + 
				"  from Z_SB_FN_SESS_AMRA)\n" + 
				" where 1 = 1 "+ ldt1_ + ldt2_ + p_n + bt + clob+"\n" + 
				" order by DateOfOperation desc";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<FN_SESS_AMRA> empList = get_fn_sess(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT FN_SESS
	// *******************************
	public static ObservableList<Add_File> add_file(String SESS_ID, LocalDate dt) {

		String ldt = "\n";
		String ldt_ = "\n";
		String p_n = "\n";
		if (SESS_ID.equals("")) {

		} else {
			p_n = "and SESS_ID = '" + SESS_ID + "'\n";
		}
		if (dt != null)
			ldt_ = dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		if (dt != null) {
			ldt = " and trunc(date_time) = to_date('" + ldt_ + "','dd.mm.yyyy')\n";
		}
		String selectStmt = "select sess_id,\n" + "       file_name,\n" + "       date_time,\n" + "       fileclob,\n"
				+ "       case\n" + "         when status = 0 then\n" + "          'Загружен'\n"
				+ "         when status = 1 then\n" + "          'Разобран'\n" + "         when status = 2 then\n"
				+ "          'Рассчитан'\n" + "       end status,\n" + "       path,\n"
				+ "       user_ from Z_SB_FN_SESS_AMRA \n" + "where 1=1" + p_n + ldt + "order by date_time desc";

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Add_File> empList = get_file(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT FN_SESS
	// *******************************
	public static ObservableList<Add_File> add_file_d(LocalDate Date) {
		String p_n = "\n";
		String dd = "\n";
		if (Date != null)
			dd = Date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		if (Date != null) {
			p_n = "and trunc(DATE_TIME) = to_date('" + dd + "','dd.mm.yyyy')\n";
		}

		String selectStmt = "select sess_id,\n" + "       file_name,\n" + "       date_time,\n" + "       fileclob,\n"
				+ "       case\n" + "         when status = 0 then\n" + "          'Загружен'\n"
				+ "         when status = 1 then\n" + "          'Разобран'\n" + "         when status = 2 then\n"
				+ "          'Рассчитан'\n" + "       end status,\n" + "       path,\n"
				+ "       user_ from Z_SB_FN_SESS_AMRA \n" + "where 1=1" + p_n + "order by date_time desc";

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Add_File> empList = get_file(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT FN_SESS
	// *******************************
	public static ObservableList<Amra_Trans> Amra_Trans_(
			String SESS_ID,
			LocalDate dt1,
			LocalDate dt2,
			String FIO,
			boolean chk,
			boolean chk_pay,
			String terminal) {

		String ldt1 = null;
		String ldt2 = null;

		String table = null;
		
		if (chk == true) {
			table = "z_sb_transact_amra_inkas";
		} else if (chk_pay == true) {
			table = "z_sb_transact_amra_ret";
		} else if ((chk == false & chk_pay == false)) {
			table = "Z_SB_TRANSACT_AMRA_DBT";
		}
		
		if (dt1 != null)
			ldt1 = dt1.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		if (dt2 != null)
			ldt2 = dt2.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

		String sess = "\n";
		String ldt1_ = "\n";
		String ldt2_ = "\n";
		String bt = "\n";
		String FIO_ = "\n";
		if (dt1 != null & dt2 != null) {
			bt = " and trunc(paydate) between to_date('" + ldt1 + "','dd.mm.yyyy') and to_date('" + ldt2
					+ "','dd.mm.yyyy') \n";
		} else if (dt1 != null & dt2 == null) {
			ldt1_ = " and trunc(paydate) >= to_date('" + ldt1 + "','dd.mm.yyyy')\n";
		} else if (dt1 == null & dt2 != null) {
			ldt2_ = " and trunc(paydate) <= to_date('" + ldt2 + "','dd.mm.yyyy')\n";
		}

		if (SESS_ID != null) {
			if (SESS_ID.equals("")) {

			} else {
				sess = " and sess_id = " + SESS_ID + "\n";
			}
		} else {

		}

		if (FIO != null) {
			if (FIO.equals("")) {

			} else {
				FIO_ = " and lower(attributes_) like '" + FIO + "'\n";
			}
		} else {

		}
		String selectStmt = " select rownum,t.* from (select rownum,t.* from " + table + " t where 1=1 and (TERMINAL = '"+terminal+"' or '"+terminal+"' = 'Все' ) " + sess + ldt1_
				+ ldt2_ + bt + FIO_+" order by PAYDATE desc) t";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Amra_Trans> empList = get_amra_trans(rsEmps);

		
		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT Attributes
	// *******************************
	public static ObservableList<Attributes> Attributes_() {
		String selectStmt = "SELECT Service, CheckNumber, AttributeName, AttributeValue\n"
				+ "  FROM (select ATTRIBUTES_\n" + "          from Z_SB_TRANSACT_AMRA_DBT\n"
				+ "         where CHECKNUMBER = '" + Connect.PNMB_ + "'),\n"
				+ "       XMLTABLE('/Атрибуты/Атр' PASSING xmltype(ATTRIBUTES_) COLUMNS\n"
				+ "                Service VARCHAR2(500) PATH '@Услуга',\n"
				+ "                CheckNumber VARCHAR2(500) PATH '@НомерЧека',\n"
				+ "                AttributeName VARCHAR2(500) PATH '@ИмяАтрибута',\n"
				+ "                AttributeValue VARCHAR2(500) PATH '@ЗначениеАтрибута')";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Attributes> empList = get_attr(rsEmps);

		// Return employee object
		return empList;
	}

	// *******************************
	// SELECT Forms
	// *******************************
	public static ObservableList<Forms> User_Forms() {
		String selectStmt = "select id_form, form_name, formn_desc from Z_SB_ACCESS_AMRA order by id_form\n";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Forms> Forms_lst = get_forms(rsEmps);

		// Return employee object
		return Forms_lst;
	}

	// *******************************
	// SELECT Z_SB_PENS_4FILE
	// *******************************
	public static ObservableList<pensmodel> Z_SB_PENS_4FILE() {
		String selectStmt = "select * from Z_SB_PENS_4FILE t order by DATE_LOAD desc";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<pensmodel> Forms_lst = PENS_4FILE(rsEmps);

		// Return employee object
		return Forms_lst;
	}
	// *******************************
	// SELECT ibank2.CLIENTS
	// *******************************
	public static ObservableList<Ibank2> CLIENTS(String db,String login,String pass) {
		String selectStmt = 
				"select CLIENT_ID, NAME_CLN\n" + 
				"  from ibank2.CLIENTS t";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Ibank2> Cli_lst = get_cli(rsEmps);

		// Return Cli_lst object
		return Cli_lst;
	}
	
	// *******************************
	// SELECT User_in
	// *******************************
	public static ObservableList<User_in> User_in(Integer form_name) {
		String selectStmt = 
				"select CUSRLOGNAME, CUSRNAME, T_NAME\n" + 
				"  from z_sb_access_amra a,\n" + 
				"       z_sb_access_gr_amra b,\n" + 
				"       z_sb_access_gr_type_amra c,\n" + 
				"       (select t.cusrlogname, t.iusrid, t.CUSRNAME from usr t) d\n" + 
				" where a.id_form = b.form_id\n" + 
				"   and b.gr_id = c.id_type\n" + 
				"   and b.usr_id = d.iusrid\n" + 
				"   and ID_FORM = "+form_name+"\n";

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<User_in> Forms_lst = get_usr_in(rsEmps);

		// Return employee object
		return Forms_lst;
	}
	
	// *******************************
		// SELECT User_out
		// *******************************
		public static ObservableList<User_out> User_out(Integer form_id) {
			String selectStmt = 
					"select CUSRLOGNAME, CUSRNAME\n" + 
					"  from usr\n" + 
					" where usr.dusrfire is null\n" + 
					"   and CUSRLOGNAME not in\n" + 
					"       (select CUSRLOGNAME\n" + 
					"          from z_sb_access_amra a,\n" + 
					"               z_sb_access_gr_amra b,\n" + 
					"               z_sb_access_gr_type_amra c,\n" + 
					"               (select t.cusrlogname, t.iusrid, t.CUSRNAME from usr t) d\n" + 
					"         where a.id_form = b.form_id\n" + 
					"           and b.gr_id = c.id_type\n" + 
					"           and b.usr_id = d.iusrid\n" + 
					"           and ID_FORM = "+form_id+")\n" + 
					" order by CUSRLOGNAME";

			// Execute SELECT statement

			// Get ResultSet from dbExecuteQuery method
			ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<User_out> Forms_lst = get_usr_out(rsEmps);

			// Return employee object
			return Forms_lst;
		}
	
    //*************************************
    //UPDATE usr right
    //*************************************
    public static void update_usr_right(String usr_name, String form_name) {   
      String updateStmt ="";
      DBUtil.dbExecuteUpdate(updateStmt);
    }
    
    public static void alert(String mes) {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("terminal.png"));
		alert.setTitle("Внимание");
		alert.setHeaderText(null);
		alert.setContentText(mes);
		alert.showAndWait();
    }
	// *******************************
	// SELECT Unpiv
	// *******************************
	public static ObservableList<Unpiv> Unpiv_View() {
		
		
		try {
		SqlMap s = new SqlMap().load(System.getenv("TRANSACT_PATH") + "\\report\\SQL.xml");
		
		String selectStmt = s.getSql("getUnpivot").replace(":p1", "'"+Connect.PNMB_+"'");

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Unpiv> empList = get_unpiv(rsEmps);
		return empList;
		// Return employee object
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert(e.getMessage());
		}
		return null;
		
	}

	// *******************************
	// SELECT Termdial_
	// *******************************
	public static ObservableList<Termdial> Termdial_(LocalDate dt1, LocalDate dt2, String pnmb, String sess_id,boolean chk) {
		String pnmb_ = "\n";
		String sess_id_ = "\n";

		String ldt1 = null;
		String ldt2 = null;

		String table = null;
		if (chk == true) {
			table = "z_sb_termdeal_amra_dbt";
		} else {
			table = "Z_SB_TERMDEAL_AMRA_FEB";
		}
		
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

		String selectStmt = "select * from "+table+" t where 1=1" + ldt1_ + bt + ldt2_ + pnmb_ + sess_id_;

		// Execute SELECT statement

		// Get ResultSet from dbExecuteQuery method
		ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

		// Send ResultSet to the getEmployeeList method and get employee object
		ObservableList<Termdial> empList = null; 
		
		if (chk == true) {
			empList = get_term(rsEmps);
		} else {
			empList = get_term_2(rsEmps);
		}
		// Return employee object
		return empList;
	}

	// Select * from fn_sess operation
	private static ObservableList<FN_SESS_AMRA> get_fn_sess(ResultSet rs) {
		try {
			ObservableList<FN_SESS_AMRA> fn_list = FXCollections.observableArrayList();
			while (rs.next()) {
				FN_SESS_AMRA fn = new FN_SESS_AMRA();
				String date_time = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("date_time"));
				String DateOfOperation = new SimpleDateFormat("dd.MM.yy").format(rs.getDate("DateOfOperation"));
				fn.setsess_id(rs.getString("sess_id"));
				fn.setfile_name(rs.getString("file_name"));
				fn.setdate_(DateOfOperation);  
				fn.setdate_time(date_time);
				fn.setpath_(rs.getString("path"));
				fn.setuser(rs.getString("user_"));
				fn.setstatus(rs.getString("status"));
				fn_list.add(fn);
			}
			return fn_list;
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

	// Select * from fn_sess operation
	private static ObservableList<Add_File> get_file(ResultSet rs) {
		try {
			ObservableList<Add_File> fn_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Add_File adf = new Add_File();
				String date_ = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("date_time"));
				adf.set_FileName(rs.getString("file_name"));
				adf.set_Status(rs.getString("status"));
				adf.set_Date(date_);
				adf.set_User(rs.getString("user_"));
				adf.set_FileId(rs.getString("sess_id"));
				adf.set_Path(rs.getString("path"));
				fn_list.add(adf);
			}
			return fn_list;
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

	// Select * from fn_sess operation
	private static ObservableList<Termdial> get_term(ResultSet rs) {
		try {
			ObservableList<Termdial> fn_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Termdial td = new Termdial();
				// String format = new SimpleDateFormat("MM.dd.yyyy
				// HH:mm:ss").format(rs.getTimestamp("recdate"));
				String recdate = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("recdate"));
				String dealstartdate = new SimpleDateFormat("dd.MM.yy HH:mm:ss")
						.format(rs.getTimestamp("dealstartdate"));
				String dealenddate = null;
				if (rs.getString("dealenddate") == null) {
					dealenddate = rs.getString("dealenddate");
				} else {
					dealenddate = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("dealenddate"));
				}
				td.set_recdate(recdate);
				td.set_department(rs.getString("department"));
				td.set_paymentnumber(rs.getString("paymentnumber"));
				td.set_dealstartdate(dealstartdate);
				td.set_sum_(rs.getString("sum_"));
				td.set_dealenddate(dealenddate);
				td.set_dealpaymentnumber(rs.getString("dealpaymentnumber"));
				td.set_status(rs.getString("status"));
				td.set_sess_id(rs.getString("sess_id"));
				fn_list.add(td);
			}
			return fn_list;
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

	// Select * from fn_sess operation
		private static ObservableList<Termdial> get_term_2(ResultSet rs) {
			try {
				ObservableList<Termdial> fn_list = FXCollections.observableArrayList();
				while (rs.next()) {
					Termdial td = new Termdial();
					// String format = new SimpleDateFormat("MM.dd.yyyy
					// HH:mm:ss").format(rs.getTimestamp("recdate"));
					String recdate = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("recdate"));
					String dealstartdate = new SimpleDateFormat("dd.MM.yy HH:mm:ss")
							.format(rs.getTimestamp("dealstartdate"));
					String dealenddate = null;
					if (rs.getString("dealenddate") == null) {
						dealenddate = rs.getString("dealenddate");
					} else {
						dealenddate = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("dealenddate"));
					}
					td.set_recdate(recdate);
					td.set_department(rs.getString("department"));
					td.set_VECTOR(rs.getString("VECTOR"));
					td.set_paymentnumber(rs.getString("paymentnumber"));
					td.set_dealstartdate(dealstartdate);
					td.set_sum_(rs.getString("sum_"));
					td.set_dealenddate(dealenddate);
					td.set_dealpaymentnumber(rs.getString("dealpaymentnumber"));
					td.set_status(rs.getString("status"));
					td.set_sess_id(rs.getString("sess_id"));
					fn_list.add(td);
				}
				return fn_list;
			} catch (SQLException e) {
				alert(e.getMessage());
			}
			return null;
		}
	// Select * from fn_sess operation
	private static ObservableList<Attributes> get_attr(ResultSet rs) {
		try {
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
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

	// Select * from fn_sess Z_SB_ACCESS_AMRA
		private static ObservableList<Forms> get_forms(ResultSet rs) {
			try {
				ObservableList<Forms> forms_list = FXCollections.observableArrayList();
				while (rs.next()) {
					Forms frms = new Forms();
					frms.set_ID_FORM(rs.getInt("ID_FORM"));
					frms.set_FORM_NAME(rs.getString("FORM_NAME"));
					frms.set_FORMN_DESC(rs.getString("FORMN_DESC"));
					forms_list.add(frms);
				}
				return forms_list;
			} catch (SQLException e) {
				alert(e.getMessage());
			}
			return null;
		}

		// Select * from fn_sess Z_SB_PENS_4FILE
		private static ObservableList<pensmodel> PENS_4FILE(ResultSet rs) {
			try {
				ObservableList<pensmodel> forms_list = FXCollections.observableArrayList();
				while (rs.next()) {
					pensmodel frms = new pensmodel();
					frms.setid(rs.getInt("ID"));
					frms.setdateload(rs.getDate("DATE_LOAD"));
					frms.setfilename(rs.getString("FILENAME"));
					frms.setone_part("<CLOB>");
					frms.setTWO_PART("<CLOB>");
					frms.setTHREE_PART("<CLOB>");
					frms.setFOUR_PART("<CLOB>");
					forms_list.add(frms);
				}
				return forms_list;
			} catch (SQLException e) {
				alert(e.getMessage());
			}
			return null;
		}
	// Select * from usr
	private static ObservableList<User_in> get_usr_in(ResultSet rs) {
		try {
			ObservableList<User_in> user_in_list = FXCollections.observableArrayList();
			while (rs.next()) {
				User_in user_in = new User_in();
				user_in.set_FIO_I(rs.getString("CUSRNAME"));
				user_in.set_USR_ID_I(rs.getString("CUSRLOGNAME"));
				user_in.set_TYPE_ACCESS_I(rs.getString("T_NAME"));
				user_in_list.add(user_in);
			}
			return user_in_list;
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}
	
	
	// Select * from Clients
		private static ObservableList<Ibank2> get_cli(ResultSet rs) {
			try {
				ObservableList<Ibank2> user_in_list = FXCollections.observableArrayList();
				while (rs.next()) {
					Ibank2 user_in = new Ibank2();
					user_in.set_CLIENT_ID(rs.getInt("CLIENT_ID"));
					user_in.set_NAME_CLN(rs.getString("NAME_CLN"));
					user_in_list.add(user_in);
				}
				return user_in_list;
			} catch (SQLException e) {
				alert(e.getMessage());
			}
			return null;
		}

	// Select * from usr
	private static ObservableList<User_out> get_usr_out(ResultSet rs) {
		try {
			ObservableList<User_out> user_o_list = FXCollections.observableArrayList();
			while (rs.next()) {
				User_out user_o = new User_out();
				user_o.set_FIO_O(rs.getString("CUSRNAME"));
				user_o.set_USR_ID_O(rs.getString("CUSRLOGNAME"));
				user_o_list.add(user_o);
			}
			return user_o_list;
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}
	// Select * from fn_sess operation
	private static ObservableList<Unpiv> get_unpiv(ResultSet rs) {
		try {
			ObservableList<Unpiv> fn_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Unpiv fn = new Unpiv();
				fn.set_COL(rs.getString("COL"));
				fn.set_COLVALUE(rs.getString("COLVALUE"));
				fn_list.add(fn);
			}
			return fn_list;
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

	// Select * from fn_sess operation
	private static ObservableList<Amra_Trans> get_amra_trans(ResultSet rs) {
		try {
			ObservableList<Amra_Trans> fn_list = FXCollections.observableArrayList();
			while (rs.next()) {
				Amra_Trans fn = new Amra_Trans();

				String recdate = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("recdate"));
				String paydate = new SimpleDateFormat("dd.MM.yy HH:mm:ss").format(rs.getTimestamp("paydate"));

				fn.set_rownum(rs.getString("rownum"));
				fn.set_recdate(recdate);
				fn.set_paydate(paydate);
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
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

	// Select * from employees operation
	private static ObservableList<Transact> getEmployeeList(ResultSet rs) {
		try {
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
		} catch (SQLException e) {
			alert(e.getMessage());
		}
		return null;
	}

}

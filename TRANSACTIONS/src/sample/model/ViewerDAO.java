package sample.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class ViewerDAO {

    //*******************************
    //SELECT an Employee
    //*******************************
    public static TransactClass searchTransact(String fio) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM Z_SB_TRANSACT_DBT WHERE lower(FIO) like '" + fio+ "'";
        
        //select PAYMENTNUMBER, FIO, DATETIMEPAYMENT, ACCOUNT, PAYMENTDATA from Z_SB_TRANSACT_DBT t"

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            TransactClass transact = getEmployeeFromResultSet(rsEmp);

            //Return employee object
            return transact;
        } catch (SQLException e) {
            System.out.println("При поиске Транзакции с " + fio + " FIO, произошла ошибка: " + e);
            //Return exception
            throw e;
        }
    }

    //Use ResultSet from DB as parameter and set Employee Object's attributes and return employee object.
    private static TransactClass getEmployeeFromResultSet(ResultSet rs) throws SQLException
    {
    	TransactClass tr = null;
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
        return tr;
    }

    //*******************************
    //SELECT Transact
    //*******************************
    public static ObservableList<TransactClass> searchEmployees (String FIO,String PAYMENTNUMBER,String DT1, String DT2) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
    	String dt_betw = "";
    	String p_n = "";
    	if(PAYMENTNUMBER.equals("")) {
    		
    	}
    	else{
    		p_n = "and PAYMENTNUMBER like '"+PAYMENTNUMBER+"'\n";
    	}
    	
    	if(DT1.equals("") & DT2.equals("")) {
    		
    	}
    	else {
    		dt_betw = "and to_date(DATETIMEPAYMENT, 'dd-mm-rrrr HH24:MI:SS') between to_date('"+DT1+"','dd-mm-rrrr') and to_date('"+DT2+"','dd-mm-rrrr')\n";
    	}
    	
        String selectStmt = "select PAYMENTNUMBER,\r\n"
        		+"       FIO,\r\n"
        		+"       DATETIMEPAYMENT,\r\n"
        		+"       ACCOUNT,\r\n"
        		+"       decode(PAYMENTDATA, null, COMPOSITEDATA, PAYMENTDATA) PAYMENTDATA,\r\n"
        		+"       INSUM,\r\n"
        		+"       FEESUM,\r\n"
        		+"       SESS_ID,\r\n"
        		+"       IDTERM,\r\n"
        		+"       (select ADDRESS\r\n"
        		+"          from Z_SB_TERMINAL_DBT t\r\n"
        		+"         where rownum = 1\r\n"
        		+"           and lower(replace(NAME, ' ', '')) =\r\n"
        		+"               lower(replace(IDTERM, ' ', ''))) ADDRESS,\r\n"
        		+"       RECEIVERSUM,\r\n"
        		+"       decode(SERVICE,\r\n"
        		+"              'Оплата детского сада СБ',\r\n"
        		+"              substr(PAYMENTDATA, 1, instr(PAYMENTDATA, ',') - 1)) kindergarten,\r\n"
        		+"       trim(regexp_substr(PAYMENTDATA, '(.*?,){1}(.*?),', 1, 1, '', 2)) chgroup,\r\n"
        		+"       trim(decode(SERVICE,\r\n"
        		+"                   'Оплата детского сада СБ',\r\n"
        		+"                   substr(PAYMENTDATA, instr(PAYMENTDATA, 'за') + 2))) period\r\n"
        	    +"from Z_SB_TRANSACT_DBT t\n"
        	    +"where lower(fio) like '"+FIO+"'\n"
        	    +p_n
        	    +dt_betw
        	    +"order by to_date(DATETIMEPAYMENT, 'dd-mm-rrrr HH24:MI:SS') desc\n";

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<TransactClass> empList = getEmployeeList(rsEmps);

            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("Операция выбора SQL не удалась: " + e);
            //Return exception
            throw e;
        }
    }
    //*******************************
    //SELECT Terminal
    //*******************************
    public static ObservableList<TerminalClass> searchTerminal() throws SQLException, ClassNotFoundException {
        String selectStmt = "select name,\r\n" 
+"       department,\r\n"
+"       address,\r\n"
+"       account,\r\n"
+"       acc_30232_01,\r\n"
+"       acc_30232_02,\r\n"
+"       acc_30232_03,\r\n"
+"       acc_30232_04,\r\n"
+"       acc_30232_05,\r\n"
+"       acc_70107\r\n"
+"  from Z_SB_TERMINAL_DBT t\r\n"
+"order by name \r\n";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<TerminalClass> empList = getTerminallist(rsEmps);

            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("Операция выбора SQL не удалась: " + e);
            //Return exception
            throw e;
        }
    }
    
    
  //*******************************
    //SELECT Kash
    //*******************************
    public static ObservableList<KashClass> searchKash() throws SQLException, ClassNotFoundException {
        String selectStmt = "SELECT cnameoper, ckbk, cpsevdo\r\n" + "FROM ov_plat\r\n"
				+ "WHERE idov_plat IN (SELECT idov_plat_ FROM z_sb_psevdo_aggregate)\r\n";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<KashClass> empList = getKASHllist(rsEmps);

            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("Операция выбора SQL не удалась: " + e);
            //Return exception
            throw e;
        }
    }

  //*******************************
    //SELECT Service
    //*******************************
    public static ObservableList<ServiceClass> searchService(String idterm) throws SQLException, ClassNotFoundException {
        String selectStmt = "select name,\n\r"
        		+"       idterm,\n\r"
        		+"       account,\n\r"
        		+"       account2,\n\r"
        		+"       account3,\n\r"
        		+"       account4,\n\r"
        		+"       account5,\n\r"
        		+"       inn,\n\r"
        		+"       kpp,\n\r"
        		+"       kor_bank_nbra,\n\r"
        		+"       acc_rec,\n\r"
        		+"       kbk,\n\r"
        		+"       okato,\n\r"
        		+"       stat,\n\r"
        		+"       acc_name,\n\r"
        		+"       bo1,\n\r"
        		+"       bo2\n\r"
        		+"  from Z_SB_TERMSERV_DBT t\n\r"
        		+"where idterm = '"+idterm+"'\n\r";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<ServiceClass> empList = getServiceList(rsEmps);
            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("Операция выбора SQL не удалась: " + e);
            //Return exception
            throw e;
        }
    }
    //Select * from transact operation
    private static ObservableList<TransactClass> getEmployeeList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<TransactClass> empList = FXCollections.observableArrayList();
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
            //Add employee to the ObservableList
            empList.add(tr);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }
    
  //Select * from transact operation
    private static ObservableList<ServiceClass> getServiceList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<ServiceClass> empList = FXCollections.observableArrayList();
        while (rs.next()) {
        	ServiceClass sr = new ServiceClass();       	
        	sr.setname(rs.getString("name"));
        	sr.setacc_name(rs.getString("acc_name"));
        	sr.setacc_rec(rs.getString("acc_rec"));
        	sr.setaccount(rs.getString("account"));
        	sr.setaccount2(rs.getString("account2"));
        	sr.setaccount3(rs.getString("account3"));
        	sr.setaccount4(rs.getString("account4"));
        	sr.setaccount5(rs.getString("account5"));
        	sr.setidterm(rs.getString("idterm"));
        	sr.setinn(rs.getString("inn"));
        	sr.setkbk(rs.getString("kbk"));
        	sr.setkor_bank_nbra(rs.getString("kor_bank_nbra"));
        	sr.setkpp(rs.getString("kpp"));   	
        	sr.setokato(rs.getString("okato"));
        	sr.setbo1(rs.getString("bo1"));
        	sr.setbo2(rs.getString("bo2"));
        	sr.setstat(rs.getString("stat"));
            empList.add(sr);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }
    
  //Select * from transact operation
    private static ObservableList<TerminalClass> getTerminallist(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<TerminalClass> empList = FXCollections.observableArrayList();
        while (rs.next()) {
        	TerminalClass tr = new TerminalClass();
            tr.setACCOUNT(rs.getString("ACCOUNT"));
            tr.setDEPARTMENT(rs.getString("DEPARTMENT"));
            tr.setADDRESS(rs.getString("ADDRESS"));
            tr.setNAME(rs.getString("NAME"));
            tr.setacc_30232_01(rs.getString("acc_30232_01"));
            tr.setacc_30232_02(rs.getString("acc_30232_02"));
            tr.setacc_30232_03(rs.getString("acc_30232_03"));
            tr.setacc_30232_03(rs.getString("acc_30232_03"));
            tr.setacc_30232_04(rs.getString("acc_30232_04"));
            tr.setacc_30232_05(rs.getString("acc_30232_05"));
            tr.setacc_70107(rs.getString("acc_70107"));
            empList.add(tr);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }
    
  //Select * from transact operation
    private static ObservableList<KashClass> getKASHllist(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
        ObservableList<KashClass> empList = FXCollections.observableArrayList();
        while (rs.next()) {
        	KashClass tr = new KashClass();
            tr.setckbk(rs.getString("ckbk"));
            tr.setcnameoper(rs.getString("cnameoper"));
            tr.setcpsevdo(rs.getString("cpsevdo"));
            empList.add(tr);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }

    //*************************************
    //UPDATE 
    //*************************************
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
			String acc_30232_01,
			String acc_30232_02,
			String acc_30232_03,
			String acc_30232_04,
			String acc_30232_05,
			String acc_70107,
			String BeforeName) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
		String NAME_ = "";
		String ACCOUNT_ = "";
		String DEPARTMENT_ = "";
		String ADDRESS_ = "";
		String acc_30232_01_ = "";
		String acc_30232_02_ = "";
		String acc_30232_03_ = "";
		String acc_30232_04_ = "";
		String acc_30232_05_ = "";
		String acc_70107_ = "";
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
		if (isNullOrEmpty(acc_30232_01)) {

		} else {
			acc_30232_01_ = acc_30232_01;
		}
		if (isNullOrEmpty(acc_30232_02)) {

		} else {
			acc_30232_02_ =acc_30232_02;
		}
		if (isNullOrEmpty(acc_30232_03)) {

		} else {
			acc_30232_03_ = acc_30232_03;
		}
		if (isNullOrEmpty(acc_30232_04)) {

		} else {
			acc_30232_04_ = acc_30232_04;
		}
		if (isNullOrEmpty(acc_30232_05)) {

		} else {
			acc_30232_05_ = acc_30232_05;
		}
		if (isNullOrEmpty(acc_70107)) {

		} else {
			acc_70107_ = acc_70107;
		}
        String updateStmt =
                "BEGIN\n" +
                        "   UPDATE Z_SB_TERMINAL_DBT\n" +
                        "      SET NAME = '" + NAME_ + "',\n"+
                              "ACCOUNT = '" + ACCOUNT_ + "',\n"+
                              "DEPARTMENT = '" + DEPARTMENT_ + "',\n"+   
                              "ADDRESS = '" + ADDRESS_ + "',\n"+
                              "acc_30232_01 = '" + acc_30232_01_ + "',\n"+
                              "acc_30232_02 = '" + acc_30232_02_ + "',\n"+
                              "acc_30232_03 = '" + acc_30232_03_ + "',\n"+
                              "acc_30232_04 = '" + acc_30232_04_ + "',\n"+
                              "acc_30232_05 = '" + acc_30232_05_ + "',\n"+
                              "acc_70107 = '" + acc_70107_ + "'\n"
                        +"    WHERE NAME = '" + BeforeName + "';\n" 
                        +"   COMMIT;\n" +
                        "END;";
        System.out.println(updateStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }
	public static void updateService(
			String acc_name,
			String acc_rec,
			String account,
			String account2,
			String account3,
			String account4,
			String account5,
			String idterm,
			String inn,
			String kbk,
			String kor_bank_nbra,
			String kpp,
			String name,
			String okato,
			String bo1,
			String bo2,
			String stat,
			String BeforeAcc,
			String Beforeidterm,
			String Beforename) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
		String acc_name_ = "";
		String acc_rec_ = "";
		String account_ = "";
		String account2_ = "";
		String account3_ = "";
		String account4_ = "";
		String account5_ = "";
		String idterm_ = "";
		String inn_ = "";
		String kbk_ = "";
		String kor_bank_nbra_ = "";
		String kpp_ = "";
		String name_ = "";
		String okato_ = "";
		String bo1_ = "";
		String bo2_ = "";
		String stat_ = "";

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
		if (isNullOrEmpty(account2)) {

		} else {
			account2_ = account2;
		}
		if (isNullOrEmpty(account3)) {

		} else {
			account3_ = account3;
		}
		if (isNullOrEmpty(account4)) {

		} else {
			account4_ =account4;
		}
		if (isNullOrEmpty(account5)) {

		} else {
			account5_ = account5;
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
		if (isNullOrEmpty(kor_bank_nbra)) {

		} else {
			kor_bank_nbra_ = kor_bank_nbra;
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
		if (isNullOrEmpty(stat)) {

		} else {
			stat_ = stat;
		}
		String acc_name_C = isNullOrEmpty(acc_name) ? "":"acc_name = '"+acc_name+"',";
		String acc_rec_C = isNullOrEmpty(acc_rec) ? "":"acc_rec = '"+acc_rec+"',";
		String account_C = isNullOrEmpty(account) ? "":"account = '"+account+"',";
		String account2_C = isNullOrEmpty(account2) ? "":"account2 = '"+account2+"',";
		String account3_C = isNullOrEmpty(account3) ? "":"account3 = '"+account3+"',";
		String account4_C = isNullOrEmpty(account4) ? "":"account4 = '"+account4+"',";
		String account5_C = isNullOrEmpty(account5) ? "":"account5 = '"+account5+"',";
		String idterm_C = isNullOrEmpty(idterm) ? "":"idterm = '"+idterm+"',";
		String inn_C = isNullOrEmpty(inn) ? "":"inn = '"+inn+"',";
		String kbk_C = isNullOrEmpty(kbk) ? "":"kbk = '"+kbk+"',";
		String kor_bank_nbra_C = isNullOrEmpty(kor_bank_nbra) ? "":"kor_bank_nbra = '"+kor_bank_nbra+"',";
		String kpp_C = isNullOrEmpty(kpp) ? "":"kpp = '"+kpp+"',";
		String name_C = isNullOrEmpty(name) ? "":"name = '"+name+"',";
		String okato_C = isNullOrEmpty(okato) ? "":"okato = '"+okato+"',";
		String bo1_C = isNullOrEmpty(bo1) ? "":"bo1 = '"+bo1+"',";
		String bo2_C = isNullOrEmpty(bo2) ? "":"bo2 = '"+bo2+"',";
		String stat_C = isNullOrEmpty(stat) ? "":"stat = '"+stat+"',";

		
		String param = acc_name_C+ 
        		acc_rec_C+ 
        		account_C+ 
        		account2_C+
        		account3_C+
        		account4_C+
        		account5_C+
        		idterm_C+ 
        		inn_C+ 
        		kbk_C+ 
        		kor_bank_nbra_C+
        		kpp_C+ 
        		name_C+ 
        		okato_C+ 
        		bo1_C+ 
        		bo2_C+
        		stat_C;
		
        String updateStmt = 
                "BEGIN\n" +
                        "   UPDATE Z_SB_TERMSERV_DBT\n" 
                		+"SET "+
                		"acc_name = '"+acc_name_+"', "+
                		"acc_rec = '"+acc_rec_+"', "+
                		"account = '"+account_+"', "+
                		"account2 = '"+account2_+"', "+
                		"account3 = '"+account3_+"', "+
                		"account4 = '"+account4_+"', "+
                		"account5 = '"+account5_+"', "+
                		"idterm = '"+idterm_+"', "+
                		"inn = '"+inn_+"', "+
                		"kbk = '"+kbk_+"', "+
                		"kor_bank_nbra = '"+kor_bank_nbra_+"', "+
                		"kpp = '"+kpp_+"', "+
                		"name = '"+name_+"', "+
                		"okato = '"+okato_+"', "+
                		"bo1 = '"+bo1_+"', "+
                		"bo2 = '"+bo2_+"', "+
                		"stat = '"+stat_+"'"
                		+" WHERE ACCOUNT = '" + BeforeAcc + "' and idterm = '"+Beforeidterm+"' and name = '"+Beforename+"';\n"
                        +"   COMMIT;\n" +
                        "END;";
        System.out.println(updateStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

	public static void InsertService(
			String acc_name,
			String acc_rec,
			String account,
			String account2,
			String account3,
			String account4,
			String account5,
			String idterm,
			String inn,
			String kbk,
			String kor_bank_nbra,
			String kpp,
			String name,
			String okato,
			String bo1,
			String bo2,
			String stat) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
		String acc_name_ = "";
		String acc_rec_ = "";
		String account_ = "";
		String account2_ = "";
		String account3_ = "";
		String account4_ = "";
		String account5_ = "";
		String idterm_ = "";
		String inn_ = "";
		String kbk_ = "";
		String kor_bank_nbra_ = "";
		String kpp_ = "";
		String name_ = "";
		String okato_ = "";
		String bo1_ = "";
		String bo2_ = "";
		String stat_ = "";

		if (isNullOrEmpty(acc_name)) {

		} else {
			acc_name_ = "'"+acc_name+"',";
		}
		if (isNullOrEmpty(acc_rec)) {

		} else {
			acc_rec_ = "'"+acc_rec+"',";
		}
		if (isNullOrEmpty(account)) {

		} else {
			account_ = "'"+account+"',";
		}
		if (isNullOrEmpty(account2)) {

		} else {
			account2_ = "'"+account2+"',";
		}
		if (isNullOrEmpty(account3)) {

		} else {
			account3_ = "'"+account3+"',";
		}
		if (isNullOrEmpty(account4)) {

		} else {
			account4_ ="'"+account4+"',";
		}
		if (isNullOrEmpty(account5)) {

		} else {
			account5_ = "'"+account5+"',";
		}
		if (isNullOrEmpty(idterm)) {

		} else {
			idterm_ = "'"+idterm+"',";
		}
		if (isNullOrEmpty(inn)) {

		} else {
			inn_ = "'"+inn+"',";
		}
		if (isNullOrEmpty(kbk)) {

		} else {
			kbk_ = "'"+kbk+"',";
		}
		if (isNullOrEmpty(kor_bank_nbra)) {

		} else {
			kor_bank_nbra_ = "'"+kor_bank_nbra+"',";
		}
		if (isNullOrEmpty(kpp)) {

		} else {
			kpp_ = "'"+kpp+"',";
		}	
		if (isNullOrEmpty(name)) {

		} else {
			name_ = "'"+name+"',";
		}
		if (isNullOrEmpty(okato)) {

		} else {
			okato_ = "'"+okato+"',";
		}
		if (isNullOrEmpty(bo1)) {

		} else {
			bo1_ = "'"+bo1+"',";
		}
		if (isNullOrEmpty(bo2)) {

		} else {
			bo2_ = "'"+bo2+"',";
		}
		if (isNullOrEmpty(stat)) {

		} else {
			stat_ = "'"+stat+"',";
		}
		String acc_name_C = isNullOrEmpty(acc_name) ? "":"acc_name,";
		String acc_rec_C = isNullOrEmpty(acc_rec) ? "":"acc_rec,";
		String account_C = isNullOrEmpty(account) ? "":"account,";
		String account2_C = isNullOrEmpty(account2) ? "":"account2,";
		String account3_C = isNullOrEmpty(account3) ? "":"account3,";
		String account4_C = isNullOrEmpty(account4) ? "":"account4,";
		String account5_C = isNullOrEmpty(account5) ? "":"account5,";
		String idterm_C = isNullOrEmpty(idterm) ? "":"idterm,";
		String inn_C = isNullOrEmpty(inn) ? "":"inn,";
		String kbk_C = isNullOrEmpty(kbk) ? "":"kbk,";
		String kor_bank_nbra_C = isNullOrEmpty(kor_bank_nbra) ? "":"kor_bank_nbra,";
		String kpp_C = isNullOrEmpty(kpp) ? "":"kpp,";
		String name_C = isNullOrEmpty(name) ? "":"name,";
		String okato_C = isNullOrEmpty(okato) ? "":"okato,";
		String bo1_C = isNullOrEmpty(bo1) ? "":"bo1,";
		String bo2_C = isNullOrEmpty(bo2) ? "":"bo2,";
		String stat_C = isNullOrEmpty(stat) ? "":"stat,";

		String Values = "("+acc_name_+ acc_rec_+ account_+ account2_+ account3_+ account4_+ account5_+ idterm_+ inn_+ kbk_+ kor_bank_nbra_+ kpp_+ name_+ okato_+ bo1_+ bo2_+ stat_+");\n";
		String Columns = "("+acc_name_C+ acc_rec_C+ account_C+ account2_C+ account3_C+ account4_C+ account5_C+ idterm_C+ inn_C+ kbk_C+ kor_bank_nbra_C+ kpp_C+ name_C+ okato_C+ bo1_C+ bo2_C+ stat_C+")\n ";
        String insertStmt =
                "BEGIN\n" +
                        "   insert into  Z_SB_TERMSERV_DBT \n"
                        +Columns.replace(",)", ")")+
                        "      values \n"+
                        Values.replace(",)", ")")+
                        "   COMMIT;\n" +
                        "END;";
        System.out.println(insertStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(insertStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }
    //*************************************
    //DELETE 
    //*************************************
    public static void deleteTerminal(String NAME) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                "BEGIN\n" +
                        "   DELETE FROM Z_SB_TERMINAL_DBT\n" +
                        "         WHERE NAME ='"+ NAME +"';\n" +
                        "   COMMIT;\n" +
                        "END;";

        System.out.print(updateStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
    
    public static void kash_psevdo() throws SQLException, ClassNotFoundException {
    	
        String update_psevdo =
                "BEGIN\n" +
                        "   update_psevdonim_idovplat;\n" +
                        "   COMMIT;\n" +
                        "END;";

        //System.out.print(update_psevdo);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(update_psevdo);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
    
    public static void delete_kash_psevdo() throws SQLException, ClassNotFoundException {
    	
        String update_psevdo =
                "BEGIN\n" +
                        "   delete from z_sb_psevdo_aggregate;\n" +
                        "   COMMIT;\n" +
                        "END;";

        //System.out.print(update_psevdo);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(update_psevdo);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
    public static void deleteService(String idterm,String ACCOUNT,String name) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                "BEGIN\n" +
                        "   DELETE FROM Z_SB_TERMSERV_DBT\n" +
                        "         WHERE idterm ='"+ idterm +"' and account = '"+ACCOUNT+"' and name = '"+name+"';\n" +
                        "   COMMIT;\n" +
                        "END;";

        System.out.print(updateStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //INSERT an employee
    //*************************************
    public static void InsertTerminal(
			String NAME,
			String ACCOUNT,
			String DEPARTMENT,
			String ADDRESS,
			String acc_30232_01,
			String acc_30232_02,
			String acc_30232_03,
			String acc_30232_04,
			String acc_30232_05,
			String acc_70107) throws SQLException, ClassNotFoundException {
        //Declare a INSET statement
		String NAME_ = "";
		String ACCOUNT_ = "";
		String DEPARTMENT_ = "";
		String ADDRESS_ = "";
		String acc_30232_01_ = "";
		String acc_30232_02_ = "";
		String acc_30232_03_ = "";
		String acc_30232_04_ = "";
		String acc_30232_05_ = "";
		String acc_70107_ = "";
		if (isNullOrEmpty(NAME)) {

		} else {
			NAME_ = "'"+NAME+"',";
		}
		if (isNullOrEmpty(ACCOUNT)) {

		} else {
			ACCOUNT_ = "'"+ACCOUNT+"',";
		}
		if (isNullOrEmpty(DEPARTMENT)) {

		} else {
			DEPARTMENT_ = "'"+DEPARTMENT+"',";
		}
		if (isNullOrEmpty(ADDRESS)) {

		} else {
			ADDRESS_ = "'"+ADDRESS+"',";
		}
		if (isNullOrEmpty(acc_30232_01)) {

		} else {
			acc_30232_01_ = "'"+acc_30232_01+"',";
		}
		if (isNullOrEmpty(acc_30232_02)) {

		} else {
			acc_30232_02_ ="'"+acc_30232_02+"',";
		}
		if (isNullOrEmpty(acc_30232_03)) {

		} else {
			acc_30232_03_ = "'"+acc_30232_03+"',";
		}
		if (isNullOrEmpty(acc_30232_04)) {

		} else {
			acc_30232_04_ = "'"+acc_30232_04+"',";
		}
		if (isNullOrEmpty(acc_30232_05)) {

		} else {
			acc_30232_05_ = "'"+acc_30232_05+"',";
		}
		if (isNullOrEmpty(acc_70107)) {

		} else {
			acc_70107_ = "'"+acc_70107+"',";
		}
		
		String NAME_C = isNullOrEmpty(NAME)? "":"NAME,";
		String ACCOUNT_C = isNullOrEmpty(ACCOUNT)? "":"ACCOUNT,";
		String DEPARTMENT_C = isNullOrEmpty(DEPARTMENT)? "":"DEPARTMENT,";
		String ADDRESS_C = isNullOrEmpty(ADDRESS)? "":"ADDRESS,";
		String acc_30232_01_C = isNullOrEmpty(acc_30232_01)? "":"acc_30232_01,";
		String acc_30232_02_C = isNullOrEmpty(acc_30232_02)? "":"acc_30232_02,";
		String acc_30232_03_C = isNullOrEmpty(acc_30232_03)? "":"acc_30232_03,";
		String acc_30232_04_C = isNullOrEmpty(acc_30232_04)? "":"acc_30232_04,";
		String acc_30232_05_C = isNullOrEmpty(acc_30232_05)? "":"acc_30232_05,";
		String acc_70107_C = isNullOrEmpty(acc_70107)? "":"acc_70107,";
		
		String Values = "("+NAME_+ACCOUNT_+DEPARTMENT_+ADDRESS_+acc_30232_01_+acc_30232_02_+acc_30232_03_+acc_30232_04_+acc_30232_05_+acc_70107_+");\n ";
		String Columns = "("+NAME_C+ACCOUNT_C+DEPARTMENT_C+ADDRESS_C+acc_30232_01_C+acc_30232_02_C+acc_30232_03_C+acc_30232_04_C+acc_30232_05_C+acc_70107_C+")\n ";
        String insertStmt =
                "BEGIN\n" +
                        "   insert into  Z_SB_TERMINAL_DBT \n"
                        +Columns.replace(",)", ")")+
                        "      values \n"+
                        Values.replace(",)", ")")+
                        "   COMMIT;\n" +
                        "END;";
        System.out.println(insertStmt);
        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(insertStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while Insert Operation: " + e);
            throw e;
        }
    }
}

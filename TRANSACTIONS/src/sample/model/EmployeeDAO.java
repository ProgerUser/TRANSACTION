package sample.model;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class EmployeeDAO {

    //*******************************
    //SELECT an Employee
    //*******************************
    public static Transact searchTransact (String fio) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM Z_SB_TRANSACT_DBT WHERE lower(FIO) like '" + fio+ "'";
        
        //select PAYMENTNUMBER, FIO, DATETIMEPAYMENT, ACCOUNT, PAYMENTDATA from Z_SB_TRANSACT_DBT t"

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEmp = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            Transact transact = getEmployeeFromResultSet(rsEmp);

            //Return employee object
            return transact;
        } catch (SQLException e) {
            System.out.println("При поиске Транзакции с " + fio + " FIO, произошла ошибка: " + e);
            //Return exception
            throw e;
        }
    }

    //Use ResultSet from DB as parameter and set Employee Object's attributes and return employee object.
    private static Transact getEmployeeFromResultSet(ResultSet rs) throws SQLException
    {
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

    //*******************************
    //SELECT Employees
    //*******************************
    public static ObservableList<Transact> searchEmployees (String FIO,String PAYMENTNUMBER,String DT1, String DT2) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
    	String dt_betw = "\n";
    	String p_n = "\n";
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
    	
        String selectStmt = "select PAYMENTNUMBER,\n"
        	       +"FIO,\n"
        	       +"DATETIMEPAYMENT,\n"
        	       +"ACCOUNT,\n"
        	       +"decode(PAYMENTDATA, null, COMPOSITEDATA, PAYMENTDATA) PAYMENTDATA,\n"
        	       +"INSUM,\n"
        	       +"FEESUM,\n"
        	       +"SESS_ID\n"
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
            ObservableList<Transact> empList = getEmployeeList(rsEmps);

            //Return employee object
            return empList;
        } catch (SQLException e) {
            System.out.println("Операция выбора SQL не удалась: " + e);
            //Return exception
            throw e;
        }
    }

    //Select * from employees operation
    private static ObservableList<Transact> getEmployeeList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of Employee objects
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
            //Add employee to the ObservableList
            empList.add(tr);
        }
        //return empList (ObservableList of Employees)
        return empList;
    }

    //*************************************
    //UPDATE an employee's email address
    //*************************************
    public static void updateEmpEmail (String empId, String empEmail) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt =
                "BEGIN\n" +
                        "   UPDATE employees\n" +
                        "      SET EMAIL = '" + empEmail + "'\n" +
                        "    WHERE EMPLOYEE_ID = " + empId + ";\n" +
                        "   COMMIT;\n" +
                        "END;";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE an employee
    //*************************************
    public static void deleteEmpWithId (String empId) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                "BEGIN\n" +
                        "   DELETE FROM employees\n" +
                        "         WHERE employee_id ="+ empId +";\n" +
                        "   COMMIT;\n" +
                        "END;";

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
    public static void insertEmp (String name, String lastname, String email) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt =
                "BEGIN\n" +
                        "INSERT INTO employees\n" +
                        "(EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, HIRE_DATE, JOB_ID)\n" +
                        "VALUES\n" +
                        "(sequence_employee.nextval, '"+name+"', '"+lastname+"','"+email+"', SYSDATE, 'IT_PROG');\n" +
                        "END;";

        //Execute DELETE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
}

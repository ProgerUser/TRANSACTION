package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {

	public static void main(String[] args) {
		try{
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:XXI/ver8i@10.111.64.24:1521/odb");
		Statement sqlStatement = conn.createStatement();
		String readRecordSQL = "SELECT g.Currency,\r\n" + 
				"       g.PaymentType,\r\n" + 
				"       g.VK,\r\n" + 
				"       g.DateOfOperation,\r\n" + 
				"       g.DataPs,\r\n" + 
				"       g.DateClearing,\r\n" + 
				"       g.Dealer,\r\n" + 
				"       g.AccountPayer,\r\n" + 
				"       g.CardNumber,\r\n" + 
				"       g.OperationNumber,\r\n" + 
				"       g.OperationNumberDelivery,\r\n" + 
				"       g.CheckNumber,\r\n" + 
				"       g.CheckParent,\r\n" + 
				"       g.OrderOfProvidence,\r\n" + 
				"       g.Provider,\r\n" + 
				"       g.OwnInOwn,\r\n" + 
				"       g.Corrected,\r\n" + 
				"       g.CommissionRate,\r\n" + 
				"       g.Status,\r\n" + 
				"       g.StringFromFile,\r\n" + 
				"       g.RewardAmount,\r\n" + 
				"       g.OwnerIncomeAmount,\r\n" + 
				"       g.CommissionAmount,\r\n" + 
				"       g.NKAmount,\r\n" + 
				"       g.MaxCommissionAmount,\r\n" + 
				"       g.MinCommissionAmount,\r\n" + 
				"       g.CashAmount,\r\n" + 
				"       g.SumNalPrimal,\r\n" + 
				"       g.AmountToCheck,\r\n" + 
				"       g.AmountOfPayment,\r\n" + 
				"       g.SumOfSplitting,\r\n" + 
				"       g.AmountIntermediary,\r\n" + 
				"       g.AmountOfSCS,\r\n" + 
				"       g.AmountWithChecks,\r\n" + 
				"       g.Counter,\r\n" + 
				"       g.Terminal,\r\n" + 
				"       g.TerminalNetwork,\r\n" + 
				"       g.TransactionType,\r\n" + 
				"       g.Service,\r\n" + 
				"       g.FileTransactions,\r\n" + 
				"       g.FIO,\r\n" + 
				"       g.ChecksIncoming,\r\n" + 
				"       g.Barcode,\r\n" + 
				"       g.IsAResident,\r\n" + 
				"       g.ValueNotFound,\r\n" + 
				"       g.ProviderTariff,\r\n" + 
				"       g.CounterChecks,\r\n" + 
				"       g.CounterCheck,\r\n" + 
				"       g.Id_,\r\n" + 
				"       g.Detailing,\r\n" + 
				"       g.WalletPayer,\r\n" + 
				"       g.WalletReceiver,\r\n" + 
				"       g.PurposeOfPayment,\r\n" + 
				"       g.DataProvider,\r\n" + 
				"       g.Attributes_\r\n" + 
				"  FROM Z_SB_FN_SESS_AMRA t,\r\n" + 
				"       XMLTABLE('/����������/���' PASSING xmltype(FILECLOB) COLUMNS\r\n" + 
				"                Currency VARCHAR2(500) PATH '@������',\r\n" + 
				"                PaymentType VARCHAR2(500) PATH '@����������',\r\n" + 
				"                VK VARCHAR2(500) PATH '@��',\r\n" + 
				"                DateOfOperation VARCHAR2(500) PATH '@������������',\r\n" + 
				"                DataPs VARCHAR2(500) PATH '@������',\r\n" + 
				"                DateClearing VARCHAR2(500) PATH '@������������',\r\n" + 
				"                Dealer VARCHAR2(500) PATH '@�����',\r\n" + 
				"                AccountPayer VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                CardNumber VARCHAR2(500) PATH '@����������',\r\n" + 
				"                OperationNumber VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                OperationNumberDelivery VARCHAR2(500) PATH\r\n" + 
				"                '@������������������',\r\n" + 
				"                CheckNumber VARCHAR2(500) PATH '@���������',\r\n" + 
				"                CheckParent VARCHAR2(500) PATH '@�����������',\r\n" + 
				"                OrderOfProvidence VARCHAR2(500) PATH '@����������������',\r\n" + 
				"                Provider VARCHAR2(500) PATH '@���������',\r\n" + 
				"                OwnInOwn VARCHAR2(500) PATH '@����������',\r\n" + 
				"                Corrected VARCHAR2(500) PATH '@���������������',\r\n" + 
				"                CommissionRate VARCHAR2(500) PATH '@��������������',\r\n" + 
				"                Status VARCHAR2(500) PATH '@������',\r\n" + 
				"                StringFromFile VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                RewardAmount VARCHAR2(500) PATH '@�������������������',\r\n" + 
				"                OwnerIncomeAmount VARCHAR2(500) PATH '@�������������������',\r\n" + 
				"                CommissionAmount VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                NKAmount VARCHAR2(500) PATH '@�������',\r\n" + 
				"                MaxCommissionAmount VARCHAR2(500) PATH '@�����������������',\r\n" + 
				"                MinCommissionAmount VARCHAR2(500) PATH '@����������������',\r\n" + 
				"                CashAmount VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                SumNalPrimal VARCHAR2(500) PATH '@�������������������',\r\n" + 
				"                AmountToCheck VARCHAR2(500) PATH '@����������',\r\n" + 
				"                AmountOfPayment VARCHAR2(500) PATH '@������������',\r\n" + 
				"                SumOfSplitting VARCHAR2(500) PATH '@������������������',\r\n" + 
				"                AmountIntermediary VARCHAR2(500) PATH '@���������������',\r\n" + 
				"                AmountOfSCS VARCHAR2(500) PATH '@��������',\r\n" + 
				"                AmountWithChecks VARCHAR2(500) PATH '@�����������',\r\n" + 
				"                Counter VARCHAR2(500) PATH '@�������',\r\n" + 
				"                Terminal VARCHAR2(500) PATH '@��������',\r\n" + 
				"                TerminalNetwork VARCHAR2(500) PATH '@����������������',\r\n" + 
				"                TransactionType VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                Service VARCHAR2(500) PATH '@������',\r\n" + 
				"                FileTransactions VARCHAR2(500) PATH '@��������������',\r\n" + 
				"                FIO VARCHAR2(500) PATH '@���',\r\n" + 
				"                ChecksIncoming VARCHAR2(500) PATH '@������������',\r\n" + 
				"                Barcode VARCHAR2(500) PATH '@��������',\r\n" + 
				"                IsAResident VARCHAR2(500) PATH '@������������������',\r\n" + 
				"                ValueNotFound VARCHAR2(500) PATH '@�����������������',\r\n" + 
				"                ProviderTariff VARCHAR2(500) PATH '@���������������',\r\n" + 
				"                CounterChecks VARCHAR2(500) PATH '@�������������',\r\n" + 
				"                CounterCheck VARCHAR2(500) PATH '@������������',\r\n" + 
				"                Id_ VARCHAR2(500) PATH '@Id',\r\n" + 
				"                Detailing VARCHAR2(500) PATH '@�����������',\r\n" + 
				"                WalletPayer VARCHAR2(500) PATH '@�����������������',\r\n" + 
				"                WalletReceiver VARCHAR2(500) PATH '@�����������������',\r\n" + 
				"                PurposeOfPayment VARCHAR2(500) PATH '@�����������������',\r\n" + 
				"                DataProvider VARCHAR2(500) PATH '@��������������',\r\n" + 
				"                Attributes_ xmltype PATH '��������') g\r\n" + 
				" where sess_id = 14707\r\n" + 
				"";
		ResultSet myResultSet = sqlStatement.executeQuery(readRecordSQL);
		while (myResultSet.next()) {
			myResultSet.getString("PaymentType");
		}
	}
	catch (SQLException e) {
		System.out.println(e.getMessage());
	}
	
	}
	}

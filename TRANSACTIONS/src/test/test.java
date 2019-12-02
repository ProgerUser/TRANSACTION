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
				"       XMLTABLE('/Транзакции/Трн' PASSING xmltype(FILECLOB) COLUMNS\r\n" + 
				"                Currency VARCHAR2(500) PATH '@Валюта',\r\n" + 
				"                PaymentType VARCHAR2(500) PATH '@ВидПлатежа',\r\n" + 
				"                VK VARCHAR2(500) PATH '@ВК',\r\n" + 
				"                DateOfOperation VARCHAR2(500) PATH '@ДатаОперации',\r\n" + 
				"                DataPs VARCHAR2(500) PATH '@ДатаПС',\r\n" + 
				"                DateClearing VARCHAR2(500) PATH '@ДатаКлиринга',\r\n" + 
				"                Dealer VARCHAR2(500) PATH '@Дилер',\r\n" + 
				"                AccountPayer VARCHAR2(500) PATH '@ЛСПлательщика',\r\n" + 
				"                CardNumber VARCHAR2(500) PATH '@НомерКарты',\r\n" + 
				"                OperationNumber VARCHAR2(500) PATH '@НомерОперации',\r\n" + 
				"                OperationNumberDelivery VARCHAR2(500) PATH\r\n" + 
				"                '@НомерОперацииСдача',\r\n" + 
				"                CheckNumber VARCHAR2(500) PATH '@НомерЧека',\r\n" + 
				"                CheckParent VARCHAR2(500) PATH '@ЧекРодитель',\r\n" + 
				"                OrderOfProvidence VARCHAR2(500) PATH '@ПорядокПровдения',\r\n" + 
				"                Provider VARCHAR2(500) PATH '@Провайдер',\r\n" + 
				"                OwnInOwn VARCHAR2(500) PATH '@СвойВСвоем',\r\n" + 
				"                Corrected VARCHAR2(500) PATH '@Скорректирована',\r\n" + 
				"                CommissionRate VARCHAR2(500) PATH '@СтавкаКомиссии',\r\n" + 
				"                Status VARCHAR2(500) PATH '@Статус',\r\n" + 
				"                StringFromFile VARCHAR2(500) PATH '@СтрокаИзФайла',\r\n" + 
				"                RewardAmount VARCHAR2(500) PATH '@СуммаВознаграждения',\r\n" + 
				"                OwnerIncomeAmount VARCHAR2(500) PATH '@СуммаДоходВладельца',\r\n" + 
				"                CommissionAmount VARCHAR2(500) PATH '@СуммаКомиссии',\r\n" + 
				"                NKAmount VARCHAR2(500) PATH '@СуммаНК',\r\n" + 
				"                MaxCommissionAmount VARCHAR2(500) PATH '@СуммаКомиссииМакс',\r\n" + 
				"                MinCommissionAmount VARCHAR2(500) PATH '@СуммаКомиссииМин',\r\n" + 
				"                CashAmount VARCHAR2(500) PATH '@СуммаНаличных',\r\n" + 
				"                SumNalPrimal VARCHAR2(500) PATH '@СуммаНалИзначальная',\r\n" + 
				"                AmountToCheck VARCHAR2(500) PATH '@СуммаНаЧек',\r\n" + 
				"                AmountOfPayment VARCHAR2(500) PATH '@СуммаПлатежа',\r\n" + 
				"                SumOfSplitting VARCHAR2(500) PATH '@СуммаНаРасщепление',\r\n" + 
				"                AmountIntermediary VARCHAR2(500) PATH '@СуммаПосредника',\r\n" + 
				"                AmountOfSCS VARCHAR2(500) PATH '@СуммаСКС',\r\n" + 
				"                AmountWithChecks VARCHAR2(500) PATH '@СуммаСЧеков',\r\n" + 
				"                Counter VARCHAR2(500) PATH '@Счетчик',\r\n" + 
				"                Terminal VARCHAR2(500) PATH '@Терминал',\r\n" + 
				"                TerminalNetwork VARCHAR2(500) PATH '@ТерминальнаяСеть',\r\n" + 
				"                TransactionType VARCHAR2(500) PATH '@ТипТранзакции',\r\n" + 
				"                Service VARCHAR2(500) PATH '@Услуга',\r\n" + 
				"                FileTransactions VARCHAR2(500) PATH '@ФайлТранзакции',\r\n" + 
				"                FIO VARCHAR2(500) PATH '@ФИО',\r\n" + 
				"                ChecksIncoming VARCHAR2(500) PATH '@ЧекиВходящие',\r\n" + 
				"                Barcode VARCHAR2(500) PATH '@ШтрихКод',\r\n" + 
				"                IsAResident VARCHAR2(500) PATH '@ЯвляетсяРезидентом',\r\n" + 
				"                ValueNotFound VARCHAR2(500) PATH '@ЗначениеНеНайдено',\r\n" + 
				"                ProviderTariff VARCHAR2(500) PATH '@ТарифПровайдера',\r\n" + 
				"                CounterChecks VARCHAR2(500) PATH '@СчетчикСчеков',\r\n" + 
				"                CounterCheck VARCHAR2(500) PATH '@СчетчикНаЧек',\r\n" + 
				"                Id_ VARCHAR2(500) PATH '@Id',\r\n" + 
				"                Detailing VARCHAR2(500) PATH '@Деталировка',\r\n" + 
				"                WalletPayer VARCHAR2(500) PATH '@КошелекПлательщик',\r\n" + 
				"                WalletReceiver VARCHAR2(500) PATH '@КошелекПолучатель',\r\n" + 
				"                PurposeOfPayment VARCHAR2(500) PATH '@НазначениеПлатежа',\r\n" + 
				"                DataProvider VARCHAR2(500) PATH '@ДатаПровайдера',\r\n" + 
				"                Attributes_ xmltype PATH 'Атрибуты') g\r\n" + 
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

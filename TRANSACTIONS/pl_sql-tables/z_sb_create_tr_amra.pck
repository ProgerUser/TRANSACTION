create or replace package z_sb_create_tr_amra is

  -- Author  : SAIDP
  -- Created : 18.06.2019 14:11:48
  -- Purpose : 
  function load_pack(idfile number) return varchar2;
  FUNCTION makedprt(str VARCHAR2) RETURN VARCHAR;
  FUNCTION fn_sess_add(fileclob_ clob, path varchar2, file_name_ VARCHAR2)
    RETURN VARCHAR;
end z_sb_create_tr_amra;
/
create or replace package body z_sb_create_tr_amra is
  /*������� ���������� ��������� �� ������������ ��������� ������*/
  FUNCTION makedprt(str VARCHAR2) RETURN VARCHAR IS
    ret VARCHAR2(32767);
  BEGIN
    BEGIN
      SELECT LOWER(department)
        INTO ret
        FROM z_sb_terminal_amra_dbt
       WHERE LOWER(TRIM(name)) = LOWER(TRIM(str));
    
      RETURN ret;
    EXCEPTION
      WHEN OTHERS THEN
        ret := 'not_found';
        RETURN ret;
    END;
  END makedprt;
  /*������� ���������� ��������� �� ������������ ��������� ������*/

  /*������� ���������� �����*/
  FUNCTION fn_sess_add(fileclob_ clob, path varchar2, file_name_ VARCHAR2)
    RETURN VARCHAR IS
    ret     varchar(500);
    dubl    number;
    session number;
    c1      clob;
  BEGIN
    begin
      /*
      select count(*)
       into dubl
       from Z_SB_FN_SESS_AMRA t
      where t.FILECLOB like fileclob_;
      */
    
      /*select FILECLOB into c1 from Z_SB_FN_SESS_AMRA where sess_id = 17207;*/
      for r in (select * from Z_SB_FN_SESS_AMRA t) loop
        if dbms_lob.compare(r.fileclob, fileclob_) = 0 then
          /*dbms_output.put_line(r.sess_id);*/
          dubl := 1;
          /*        else
          dubl := 0;*/
        end if;
      end loop;
      dubl := 0;
      if dubl = 0 then
        session := z_sb_sq_sess_id.NEXTVAL;
        insert into Z_SB_FN_SESS_AMRA
          (sess_id, file_name, date_time, fileclob, status, path, USER_)
        values
          (session, file_name_, sysdate, fileclob_, 0, path, user);
        ret := 'Inserted;' || session;
        commit;
      elsif dubl = 1 then
        rollback;
        ret := 'Dublicate;0';
      end if;
    exception
      when others then
        rollback;
        ret := 'Exception;' || SQLERRM;
    END;
    return ret;
  end fn_sess_add;
  /*������� ���������� �����*/

  /*��������� ������ ����*/
  PROCEDURE writelog(datetimepayment_ VARCHAR2 default sysdate,
                     descripion       VARCHAR2,
                     sess_id1         NUMBER) is
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    INSERT INTO Z_SB_LOG_AMRA
      (recdate, paydate, desc_, sess_id)
    VALUES
      (SYSTIMESTAMP,
       TO_DATE(datetimepayment_, 'DD-MM-RRRR HH24:MI:SS'),
       descripion,
       sess_id1);
    COMMIT;
  END;
  /*��������� ������ ����*/

  /*�������� ������� ��������� ����������*/
  function load_pack(idfile number) return varchar2 is
  
    ret VARCHAR(50);
    res number;
  
    dubl_number      number;
    deal_dubl_number number;
    deal_dubl_num    number;
    recdate_         date;
  
    iferr number;
  
    findchk      number;
    findchk_dubl number;
    findchkf     number;
    bnk_deal     number;
    term_deal    varchar2(50);
  
    serch_service number;
  
    pnmb  varchar2(50);
    recdp varchar2(50);
    sump  varchar2(50);
  
    CURSOR xml_razbor IS
      SELECT g.Currency,
             g.PaymentType,
             g.VK,
             g.DateOfOperation,
             g.DataPs,
             g.DateClearing,
             g.Dealer,
             g.AccountPayer,
             g.CardNumber,
             g.OperationNumber,
             g.OperationNumberDelivery,
             g.CheckNumber,
             g.CheckParent,
             g.OrderOfProvidence,
             g.Provider,
             g.OwnInOwn,
             g.Corrected,
             g.CommissionRate,
             g.Status,
             g.StringFromFile,
             g.RewardAmount,
             g.OwnerIncomeAmount,
             g.CommissionAmount,
             g.NKAmount,
             g.MaxCommissionAmount,
             g.MinCommissionAmount,
             g.CashAmount,
             g.SumNalPrimal,
             g.AmountToCheck,
             g.AmountOfPayment,
             g.SumOfSplitting,
             g.AmountIntermediary,
             g.AmountOfSCS,
             g.AmountWithChecks,
             g.Counter,
             g.Terminal,
             g.TerminalNetwork,
             g.TransactionType,
             g.Service,
             g.FileTransactions,
             g.FIO,
             g.ChecksIncoming,
             g.Barcode,
             g.IsAResident,
             g.ValueNotFound,
             g.ProviderTariff,
             g.CounterChecks,
             g.CounterCheck,
             g.Id_,
             g.Detailing,
             g.WalletPayer,
             g.WalletReceiver,
             g.PurposeOfPayment,
             g.DataProvider,
             g.Attributes_
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/����������/���' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(500) PATH '@������',
                      PaymentType VARCHAR2(500) PATH '@����������',
                      VK VARCHAR2(500) PATH '@��',
                      DateOfOperation VARCHAR2(500) PATH '@������������',
                      DataPs VARCHAR2(500) PATH '@������',
                      DateClearing VARCHAR2(500) PATH '@������������',
                      Dealer VARCHAR2(500) PATH '@�����',
                      AccountPayer VARCHAR2(500) PATH '@�������������',
                      CardNumber VARCHAR2(500) PATH '@����������',
                      OperationNumber VARCHAR2(500) PATH '@�������������',
                      OperationNumberDelivery VARCHAR2(500) PATH
                      '@������������������',
                      CheckNumber VARCHAR2(500) PATH '@���������',
                      CheckParent VARCHAR2(500) PATH '@�����������',
                      OrderOfProvidence VARCHAR2(500) PATH
                      '@����������������',
                      Provider VARCHAR2(500) PATH '@���������',
                      OwnInOwn VARCHAR2(500) PATH '@����������',
                      Corrected VARCHAR2(500) PATH '@���������������',
                      CommissionRate VARCHAR2(500) PATH '@��������������',
                      Status VARCHAR2(500) PATH '@������',
                      StringFromFile VARCHAR2(500) PATH '@�������������',
                      RewardAmount VARCHAR2(500) PATH '@�������������������',
                      OwnerIncomeAmount VARCHAR2(500) PATH
                      '@�������������������',
                      CommissionAmount VARCHAR2(500) PATH '@�������������',
                      NKAmount VARCHAR2(500) PATH '@�������',
                      MaxCommissionAmount VARCHAR2(500) PATH
                      '@�����������������',
                      MinCommissionAmount VARCHAR2(500) PATH
                      '@����������������',
                      CashAmount VARCHAR2(500) PATH '@�������������',
                      SumNalPrimal VARCHAR2(500) PATH '@�������������������',
                      AmountToCheck VARCHAR2(500) PATH '@����������',
                      AmountOfPayment VARCHAR2(500) PATH '@������������',
                      SumOfSplitting VARCHAR2(500) PATH
                      '@������������������',
                      AmountIntermediary VARCHAR2(500) PATH
                      '@���������������',
                      AmountOfSCS VARCHAR2(500) PATH '@��������',
                      AmountWithChecks VARCHAR2(500) PATH '@�����������',
                      Counter VARCHAR2(500) PATH '@�������',
                      Terminal VARCHAR2(500) PATH '@��������',
                      TerminalNetwork VARCHAR2(500) PATH '@����������������',
                      TransactionType VARCHAR2(500) PATH '@�������������',
                      Service VARCHAR2(500) PATH '@������',
                      FileTransactions VARCHAR2(500) PATH '@��������������',
                      FIO VARCHAR2(500) PATH '@���',
                      ChecksIncoming VARCHAR2(500) PATH '@������������',
                      Barcode VARCHAR2(500) PATH '@��������',
                      IsAResident VARCHAR2(500) PATH '@������������������',
                      ValueNotFound VARCHAR2(500) PATH '@�����������������',
                      ProviderTariff VARCHAR2(500) PATH '@���������������',
                      CounterChecks VARCHAR2(500) PATH '@�������������',
                      CounterCheck VARCHAR2(500) PATH '@������������',
                      Id_ VARCHAR2(500) PATH '@Id',
                      Detailing VARCHAR2(500) PATH '@�����������',
                      WalletPayer VARCHAR2(500) PATH '@�����������������',
                      WalletReceiver VARCHAR2(500) PATH '@�����������������',
                      PurposeOfPayment VARCHAR2(500) PATH
                      '@�����������������',
                      DataProvider VARCHAR2(500) PATH '@��������������',
                      Attributes_ xmltype PATH '��������') g
       where t.SESS_ID = idfile;
  
    CURSOR file_find(chk_ varchar2) IS
      SELECT count(*) cnt
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/����������/���' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(500) PATH '@������',
                      PaymentType VARCHAR2(500) PATH '@����������',
                      VK VARCHAR2(500) PATH '@��',
                      DateOfOperation VARCHAR2(500) PATH '@������������',
                      DataPs VARCHAR2(500) PATH '@������',
                      DateClearing VARCHAR2(500) PATH '@������������',
                      Dealer VARCHAR2(500) PATH '@�����',
                      AccountPayer VARCHAR2(500) PATH '@�������������',
                      CardNumber VARCHAR2(500) PATH '@����������',
                      OperationNumber VARCHAR2(500) PATH '@�������������',
                      OperationNumberDelivery VARCHAR2(500) PATH
                      '@������������������',
                      CheckNumber VARCHAR2(500) PATH '@���������',
                      CheckParent VARCHAR2(500) PATH '@�����������',
                      OrderOfProvidence VARCHAR2(500) PATH
                      '@����������������',
                      Provider VARCHAR2(500) PATH '@���������',
                      OwnInOwn VARCHAR2(500) PATH '@����������',
                      Corrected VARCHAR2(500) PATH '@���������������',
                      CommissionRate VARCHAR2(500) PATH '@��������������',
                      Status VARCHAR2(500) PATH '@������',
                      StringFromFile VARCHAR2(500) PATH '@�������������',
                      RewardAmount VARCHAR2(500) PATH '@�������������������',
                      OwnerIncomeAmount VARCHAR2(500) PATH
                      '@�������������������',
                      CommissionAmount VARCHAR2(500) PATH '@�������������',
                      NKAmount VARCHAR2(500) PATH '@�������',
                      MaxCommissionAmount VARCHAR2(500) PATH
                      '@�����������������',
                      MinCommissionAmount VARCHAR2(500) PATH
                      '@����������������',
                      CashAmount VARCHAR2(500) PATH '@�������������',
                      SumNalPrimal VARCHAR2(500) PATH '@�������������������',
                      AmountToCheck VARCHAR2(500) PATH '@����������',
                      AmountOfPayment VARCHAR2(500) PATH '@������������',
                      SumOfSplitting VARCHAR2(500) PATH
                      '@������������������',
                      AmountIntermediary VARCHAR2(500) PATH
                      '@���������������',
                      AmountOfSCS VARCHAR2(500) PATH '@��������',
                      AmountWithChecks VARCHAR2(500) PATH '@�����������',
                      Counter VARCHAR2(500) PATH '@�������',
                      Terminal VARCHAR2(500) PATH '@��������',
                      TerminalNetwork VARCHAR2(500) PATH '@����������������',
                      TransactionType VARCHAR2(500) PATH '@�������������',
                      Service VARCHAR2(500) PATH '@������',
                      FileTransactions VARCHAR2(500) PATH '@��������������',
                      FIO VARCHAR2(500) PATH '@���',
                      ChecksIncoming VARCHAR2(500) PATH '@������������',
                      Barcode VARCHAR2(500) PATH '@��������',
                      IsAResident VARCHAR2(500) PATH '@������������������',
                      ValueNotFound VARCHAR2(500) PATH '@�����������������',
                      ProviderTariff VARCHAR2(500) PATH '@���������������',
                      CounterChecks VARCHAR2(500) PATH '@�������������',
                      CounterCheck VARCHAR2(500) PATH '@������������',
                      Id_ VARCHAR2(500) PATH '@Id',
                      Detailing VARCHAR2(500) PATH '@�����������',
                      WalletPayer VARCHAR2(500) PATH '@�����������������',
                      WalletReceiver VARCHAR2(500) PATH '@�����������������',
                      PurposeOfPayment VARCHAR2(500) PATH
                      '@�����������������',
                      DataProvider VARCHAR2(500) PATH '@��������������',
                      Attributes_ xmltype PATH '��������') g
       where CheckNumber = chk_
         and t.SESS_ID = idfile;
  begin
    /*\*������� ������ � ��������*\
    BEGIN
      INSERT INTO Z_SB_FN_SESS_AMRA
      VALUES
        (z_sb_sq_sess_id.NEXTVAL,
         replace(file_name, '::_', '\'),
         CURRENT_TIMESTAMP,
         file) RETURN sess_id INTO sess_id_;
    EXCEPTION
      WHEN OTHERS THEN
        rollback;
        writelog(descripion => SQLERRM || ' ' ||
                               DBMS_UTILITY.format_error_stack || ' ' ||
                               dbms_utility.format_error_backtrace,
                 sess_id1   => sess_id_);
    END;
    \*������� ������ � ��������*\*/
  
    begin
      for r in xml_razbor loop
        if r.provider = '��������' then
          select count(*)
            into serch_service
            from z_sb_termserv_amra_dbt t
           where t.idterm = r.terminal
             and t.name = r.service;
          if serch_service > 0 then
            null;
          elsif serch_service = 0 then
            writelog(r.DATEOFOPERATION,
                     '��������! ���������� CheckNumber = ' || r.CheckNumber ||
                     ' ���������� ������ ' || r.service ||
                     ' ������� �� ���������!',
                     idfile);
          end if;
        end if;
      
        if r.CHECKSINCOMING is not null then
          for r_ in (Select Regexp_Substr(r.CHECKSINCOMING,
                                          '[^|]+',
                                          1,
                                          Level) chk
                       From dual
                     Connect By Regexp_Substr(r.CHECKSINCOMING,
                                              '[^|]+',
                                              1,
                                              Level) Is Not Null) loop
            /*ID ��������� �� ������� �������� �����*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into term_deal
              From dual
             where level = 3
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            /*����� ���� �� ������*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into pnmb
              From dual
             where level = 1
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            /*���� ������������ ���������� ������� ������������ �����*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into recdp
              From dual
             where level = 4
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
          
            /*����� �����*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into sump
              From dual
             where level = 2
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
          
            /*��� ��� ��� ��������*/
            select count(*)
              into bnk_deal
              from z_sb_terminal_amra_dbt t
             where t.name = term_deal;
          
            /*���� ��� ��������*/
            if bnk_deal > 0 then
              /*���� ���������� �����*/
              select count(*)
                into findchk
                from z_sb_termdeal_amra_dbt t
               where t.paymentnumber = pnmb
                 and t.STATUS = 0;
              /*���� ���������� ���*/
              if findchk = 0 then
                /*���� � xml*/
                OPEN file_find(pnmb);
                LOOP
                  FETCH file_find
                    INTO findchkf;
                  EXIT WHEN file_find%notfound;
                end loop;
                CLOSE file_find;
                /*���� ����� ���*/
                if findchkf = 0 then
                  writelog(r.DATEOFOPERATION,
                           '��������! ���������� CheckNumber = ' ||
                           r.CheckNumber || ' ���������� ������ �� ���� ' || pnmb ||
                           ' �������� ��� �� � ����� �� � ��!',
                           idfile);
                end if;
              end if;
              /*���� ������ � ��� �������*/
              select count(*)
                into findchk_dubl
                from z_sb_termdeal_amra_dbt t
               where t.paymentnumber = pnmb
                 and t.STATUS = 1;
              /*���� ������ � ��� �������*/
              if findchk_dubl <> 0 then
                writelog(r.DATEOFOPERATION,
                         '��������! ���������� CheckNumber = ' ||
                         r.CheckNumber || ' ���������� ������ �� ���� ' || pnmb ||
                         ' �������� ��� �������!',
                         idfile);
              end if;
              /*���� �������� ����*/
            elsif bnk_deal = 0 and substr(term_deal, 1, 2) in ('��', 'IV') then
              /*���� ���������� �����*/
              select count(*)
                into deal_dubl_num
                from Z_SB_TERMDEAL_AMRA_FEB t
               where t.PAYMENTNUMBER = pnmb;
              /*���� ��� ����������*/
              if deal_dubl_num = 0 then
                /*output*/
                /*
                dbms_output.put_line(SYSDATE);
                dbms_output.put_line(TO_NUMBER(REPLACE(makedprt(r.TERMINAL),
                                                       '.',
                                                       ',')));
                dbms_output.put_line(pnmb);
                dbms_output.put_line(to_date(recdp,
                                             'DD-MM-RRRR HH24:MI:SS'));
                dbms_output.put_line(to_date(r.DateOfOperation,
                                             'DD-MM-RRRR HH24:MI:SS'));
                dbms_output.put_line(r.CheckNumber);
                dbms_output.put_line(to_number(replace(replace(sump,
                                                               '�',
                                                               ''),
                                                       '.',
                                                       ',')));
                dbms_output.put_line(1);
                dbms_output.put_line(idfile);
                dbms_output.put_line(term_deal || '->' || r.TERMINAL);
                dbms_output.put_line('<----------------------------->');
                */
                /*output*/
                INSERT INTO Z_SB_TERMDEAL_AMRA_FEB
                  (recdate,
                   department,
                   paymentnumber,
                   dealstartdate,
                   DEALENDDATE,
                   DEALPAYMENTNUMBER,
                   sum_,
                   status,
                   sess_id,
                   VECTOR)
                VALUES
                  (sysdate,
                   TO_NUMBER(REPLACE(makedprt(r.TERMINAL), '.', ',')),
                   pnmb,
                   to_date(recdp, 'DD-MM-RRRR HH24:MI:SS'),
                   to_date(r.DateOfOperation, 'DD-MM-RRRR HH24:MI:SS'),
                   r.CheckNumber,
                   to_number(replace(replace(sump, '�', ''), '.', ',')),
                   1,
                   idfile,
                   term_deal || '->' || r.TERMINAL);
              else
                /*���� ����*/
                writelog(r.DATEOFOPERATION,
                         '��������! ��� CheckNumber = ' || r.CheckNumber ||
                         ' ��� ��������� � ��� ' || recdp ||
                         ' � �������� ��� �������. ����������� ����� ' ||
                         r.TERMINAL || '->' || term_deal,
                         idfile);
              end if;
              /*���� �������� �� ����*/
            elsif substr(term_deal, 1, 2) not in ('��', 'IV') and
                  bnk_deal = 0 then
              writelog(r.DATEOFOPERATION,
                       '��������! ��� CheckNumber = ' || r.CheckNumber ||
                       ' ���� ' || recdp || ' �� �������� �� ��� SB! ' ||
                       r.TERMINAL || '->' || term_deal,
                       idfile);
            end if;
          end loop;
        end if;
      end loop;
    
      SELECT count(lg.sess_id)
        into iferr
        FROM z_sb_log_amra lg
       WHERE lg.sess_id = idfile;
    
      if iferr = 0 then
        /*��������, ���� ��� ������*/
        for r in xml_razbor loop
        
          /*������ �������������� ���������*/
          /*for row2 in (SELECT Service,
                              CheckNumber,
                              AttributeName,
                              AttributeValue
                         FROM XMLTABLE('/��������/���' PASSING r.Attributes_
                                       COLUMNS Service VARCHAR2(500) PATH
                                       '@������',
                                       CheckNumber VARCHAR2(500) PATH
                                       '@���������',
                                       AttributeName VARCHAR2(500) PATH
                                       '@�����������',
                                       AttributeValue VARCHAR2(500) PATH
                                       '@����������������')) loop
          end loop;*/
          /*������ �������������� ���������*/
        
          select count(*)
            into dubl_number
            from z_sb_transact_amra_dbt t
           where t.checknumber = r.CheckNumber;
        
          select count(*)
            into deal_dubl_number
            from Z_SB_TERMDEAL_AMRA_DBT t
           where t.PAYMENTNUMBER = r.CheckNumber;
        
          if dubl_number = 0 and r.TRANSACTIONTYPE <> '����������' then
            insert into z_sb_transact_amra_dbt
              (recdate,
               paydate,
               currency,
               paymenttype,
               vk,
               dateofoperation,
               dataps,
               dateclearing,
               dealer,
               accountpayer,
               cardnumber,
               operationnumber,
               operationnumberdelivery,
               checknumber,
               checkparent,
               orderofprovidence,
               provider,
               owninown,
               corrected,
               commissionrate,
               status,
               stringfromfile,
               rewardamount,
               ownerincomeamount,
               commissionamount,
               nkamount,
               maxcommissionamount,
               mincommissionamount,
               cashamount,
               sumnalprimal,
               amounttocheck,
               amountofpayment,
               sumofsplitting,
               amountintermediary,
               amountofscs,
               amountwithchecks,
               counter,
               terminal,
               terminalnetwork,
               transactiontype,
               service,
               filetransactions,
               fio,
               checksincoming,
               barcode,
               isaresident,
               valuenotfound,
               providertariff,
               counterchecks,
               countercheck,
               id_,
               detailing,
               walletpayer,
               walletreceiver,
               purposeofpayment,
               dataprovider,
               attributes_,
               statusabs,
               sess_id)
            values
              (sysdate,
               to_date(r.DATEOFOPERATION, 'DD-MM-RRRR HH24:MI:SS'),
               r.Currency,
               r.PaymentType,
               r.VK,
               to_date(r.DateOfOperation, 'DD-MM-RRRR HH24:MI:SS'),
               r.DataPs,
               r.DateClearing,
               r.Dealer,
               r.AccountPayer,
               r.CardNumber,
               r.OperationNumber,
               r.OperationNumberDelivery,
               r.CheckNumber,
               r.CheckParent,
               r.OrderOfProvidence,
               r.Provider,
               r.OwnInOwn,
               r.Corrected,
               r.CommissionRate,
               r.Status,
               r.StringFromFile,
               r.RewardAmount,
               r.OwnerIncomeAmount,
               to_number(replace(replace(r.CommissionAmount, '�', ''),
                                 '.',
                                 ',')),
               r.NKAmount,
               r.MaxCommissionAmount,
               r.MinCommissionAmount,
               to_number(replace(replace(r.CashAmount, '�', ''), '.', ',')),
               r.SumNalPrimal,
               to_number(replace(replace(r.AmountToCheck, '�', ''),
                                 '.',
                                 ',')),
               to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                 '.',
                                 ',')),
               r.SumOfSplitting,
               r.AmountIntermediary,
               r.AmountOfSCS,
               to_number(replace(replace(r.AmountWithChecks, '�', ''),
                                 '.',
                                 ',')),
               r.Counter,
               r.Terminal,
               r.TerminalNetwork,
               r.TransactionType,
               r.Service,
               r.FileTransactions,
               r.FIO,
               r.ChecksIncoming,
               r.Barcode,
               r.IsAResident,
               r.ValueNotFound,
               r.ProviderTariff,
               r.CounterChecks,
               r.CounterCheck,
               r.Id_,
               r.Detailing,
               r.WalletPayer,
               r.WalletReceiver,
               r.PurposeOfPayment,
               r.DataProvider,
               r.Attributes_.getClobVal(),
               0,
               idfile);
            dubl_number := null;
          elsif r.TRANSACTIONTYPE = '����������' then
            select count(*)
              into dubl_number
              from z_sb_transact_amra_inkas t
             where t.PAYDATE =
                   to_date(r.DATEOFOPERATION, 'DD-MM-RRRR HH24:MI:SS')
               and t.AMOUNTOFPAYMENT =
                   to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                     '.',
                                     ','))
               and t.FILETRANSACTIONS = r.FILETRANSACTIONS;
          
            if dubl_number = 0 then
              insert into z_sb_transact_amra_inkas
                (recdate,
                 paydate,
                 currency,
                 paymenttype,
                 vk,
                 dateofoperation,
                 dataps,
                 dateclearing,
                 dealer,
                 accountpayer,
                 cardnumber,
                 operationnumber,
                 operationnumberdelivery,
                 checknumber,
                 checkparent,
                 orderofprovidence,
                 provider,
                 owninown,
                 corrected,
                 commissionrate,
                 status,
                 stringfromfile,
                 rewardamount,
                 ownerincomeamount,
                 commissionamount,
                 nkamount,
                 maxcommissionamount,
                 mincommissionamount,
                 cashamount,
                 sumnalprimal,
                 amounttocheck,
                 amountofpayment,
                 sumofsplitting,
                 amountintermediary,
                 amountofscs,
                 amountwithchecks,
                 counter,
                 terminal,
                 terminalnetwork,
                 transactiontype,
                 service,
                 filetransactions,
                 fio,
                 checksincoming,
                 barcode,
                 isaresident,
                 valuenotfound,
                 providertariff,
                 counterchecks,
                 countercheck,
                 id_,
                 detailing,
                 walletpayer,
                 walletreceiver,
                 purposeofpayment,
                 dataprovider,
                 attributes_,
                 statusabs,
                 sess_id)
              values
                (sysdate,
                 to_date(r.DATEOFOPERATION, 'DD-MM-RRRR HH24:MI:SS'),
                 r.Currency,
                 r.PaymentType,
                 r.VK,
                 to_date(r.DateOfOperation, 'DD-MM-RRRR HH24:MI:SS'),
                 r.DataPs,
                 r.DateClearing,
                 r.Dealer,
                 r.AccountPayer,
                 r.CardNumber,
                 r.OperationNumber,
                 r.OperationNumberDelivery,
                 r.CheckNumber,
                 r.CheckParent,
                 r.OrderOfProvidence,
                 r.Provider,
                 r.OwnInOwn,
                 r.Corrected,
                 r.CommissionRate,
                 r.Status,
                 r.StringFromFile,
                 r.RewardAmount,
                 r.OwnerIncomeAmount,
                 to_number(replace(replace(r.CommissionAmount, '�', ''),
                                   '.',
                                   ',')),
                 r.NKAmount,
                 r.MaxCommissionAmount,
                 r.MinCommissionAmount,
                 to_number(replace(replace(r.CashAmount, '�', ''), '.', ',')),
                 r.SumNalPrimal,
                 to_number(replace(replace(r.AmountToCheck, '�', ''),
                                   '.',
                                   ',')),
                 to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                   '.',
                                   ',')),
                 r.SumOfSplitting,
                 r.AmountIntermediary,
                 r.AmountOfSCS,
                 to_number(replace(replace(r.AmountWithChecks, '�', ''),
                                   '.',
                                   ',')),
                 r.Counter,
                 r.Terminal,
                 r.TerminalNetwork,
                 r.TransactionType,
                 r.Service,
                 r.FileTransactions,
                 r.FIO,
                 r.ChecksIncoming,
                 r.Barcode,
                 r.IsAResident,
                 r.ValueNotFound,
                 r.ProviderTariff,
                 r.CounterChecks,
                 r.CounterCheck,
                 r.Id_,
                 r.Detailing,
                 r.WalletPayer,
                 r.WalletReceiver,
                 r.PurposeOfPayment,
                 r.DataProvider,
                 r.Attributes_.getClobVal(),
                 0,
                 idfile);
              dubl_number := null;
            else
              select t.RECDATE
                into recdate_
                from z_sb_transact_amra_inkas t
               where t.PAYDATE =
                     to_date(r.DATEOFOPERATION, 'DD-MM-RRRR HH24:MI:SS')
                 and t.AMOUNTOFPAYMENT =
                     to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                       '.',
                                       ','))
                 and t.FILETRANSACTIONS = r.FILETRANSACTIONS;
            
              writelog(r.DATEOFOPERATION,
                       '��������! ���������� ���������� ��� ��������� � ��� ' ||
                       recdate_ || ' � �������� ��� �������.',
                       idfile);
            end if;
          elsif dubl_number <> 0 and r.TRANSACTIONTYPE <> '����������' then
            select t.RECDATE
              into recdate_
              from z_sb_transact_amra_dbt t
             where t.checknumber = r.CheckNumber;
            writelog(r.DATEOFOPERATION,
                     '��������! ���������� CheckNumber = ' || r.CheckNumber ||
                     ' ��� ��������� � ��� ' || recdate_ ||
                     ' � �������� ��� �������.',
                     idfile);
          end if;
        
          if to_number(replace(replace(r.AMOUNTTOCHECK, '�', ''), '.', ',')) <> 0 then
            if deal_dubl_number = 0 then
            
              INSERT INTO Z_SB_TERMDEAL_AMRA_DBT
                (recdate,
                 department,
                 paymentnumber,
                 dealstartdate,
                 sum_,
                 status,
                 sess_id)
              VALUES
                (SYSDATE,
                 TO_NUMBER(REPLACE(makedprt(r.TERMINAL), '.', ',')),
                 r.CheckNumber,
                 to_date(r.DateOfOperation, 'DD-MM-RRRR HH24:MI:SS'),
                 to_number(replace(replace(r.AMOUNTTOCHECK, '�', ''),
                                   '.',
                                   ',')),
                 0,
                 idfile);
            else
              select t.RECDATE
                into recdate_
                from Z_SB_TERMDEAL_AMRA_DBT t
               where t.PAYMENTNUMBER = r.CheckNumber;
              writelog(r.DATEOFOPERATION,
                       '��������! ��� CheckNumber = ' || r.CheckNumber ||
                       ' ��� ��������� � ��� ' || recdate_ ||
                       ' � �������� ��� �������.',
                       idfile);
            end if;
          end if;
        end loop;
      end if;
    
    exception
      when others then
        rollback;
        writelog(descripion => SQLERRM || ' ' ||
                               DBMS_UTILITY.format_error_stack || ' ' ||
                               dbms_utility.format_error_backtrace,
                 sess_id1   => idfile);
      
    end;
    /*�������� ������� ��������� ����������*/
  
    /*������� ����������, ���� ������*/
    SELECT count(lg.sess_id)
      into res
      FROM Z_SB_LOG_AMRA lg
     WHERE lg.sess_id = idfile;
  
    IF res = 0 THEN
      ret := '0';
      update Z_SB_FN_SESS_AMRA t set STATUS = 1 where t.sess_id = idfile;
      commit;
    ELSE
      rollback;
      ret := '1';
    END IF;
    /*������� ����������, ���� ������*/
  
    RETURN ret || ':' || TO_CHAR(idfile);
  end;
begin
  null;
end z_sb_create_tr_amra;
/

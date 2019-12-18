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
  /*Функция нахождения отделения из наименования терминала оплаты*/
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
  /*Функция нахождения отделения из наименования терминала оплаты*/

  /*Функция добавления файла*/
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
  /*Функция добавления файла*/

  /*Процедура записи лога*/
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
  /*Процедура записи лога*/

  /*Основная Функция обработки транзакции*/
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
             XMLTABLE('/Транзакции/Трн' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(500) PATH '@Валюта',
                      PaymentType VARCHAR2(500) PATH '@ВидПлатежа',
                      VK VARCHAR2(500) PATH '@ВК',
                      DateOfOperation VARCHAR2(500) PATH '@ДатаОперации',
                      DataPs VARCHAR2(500) PATH '@ДатаПС',
                      DateClearing VARCHAR2(500) PATH '@ДатаКлиринга',
                      Dealer VARCHAR2(500) PATH '@Дилер',
                      AccountPayer VARCHAR2(500) PATH '@ЛСПлательщика',
                      CardNumber VARCHAR2(500) PATH '@НомерКарты',
                      OperationNumber VARCHAR2(500) PATH '@НомерОперации',
                      OperationNumberDelivery VARCHAR2(500) PATH
                      '@НомерОперацииСдача',
                      CheckNumber VARCHAR2(500) PATH '@НомерЧека',
                      CheckParent VARCHAR2(500) PATH '@ЧекРодитель',
                      OrderOfProvidence VARCHAR2(500) PATH
                      '@ПорядокПровдения',
                      Provider VARCHAR2(500) PATH '@Провайдер',
                      OwnInOwn VARCHAR2(500) PATH '@СвойВСвоем',
                      Corrected VARCHAR2(500) PATH '@Скорректирована',
                      CommissionRate VARCHAR2(500) PATH '@СтавкаКомиссии',
                      Status VARCHAR2(500) PATH '@Статус',
                      StringFromFile VARCHAR2(500) PATH '@СтрокаИзФайла',
                      RewardAmount VARCHAR2(500) PATH '@СуммаВознаграждения',
                      OwnerIncomeAmount VARCHAR2(500) PATH
                      '@СуммаДоходВладельца',
                      CommissionAmount VARCHAR2(500) PATH '@СуммаКомиссии',
                      NKAmount VARCHAR2(500) PATH '@СуммаНК',
                      MaxCommissionAmount VARCHAR2(500) PATH
                      '@СуммаКомиссииМакс',
                      MinCommissionAmount VARCHAR2(500) PATH
                      '@СуммаКомиссииМин',
                      CashAmount VARCHAR2(500) PATH '@СуммаНаличных',
                      SumNalPrimal VARCHAR2(500) PATH '@СуммаНалИзначальная',
                      AmountToCheck VARCHAR2(500) PATH '@СуммаНаЧек',
                      AmountOfPayment VARCHAR2(500) PATH '@СуммаПлатежа',
                      SumOfSplitting VARCHAR2(500) PATH
                      '@СуммаНаРасщепление',
                      AmountIntermediary VARCHAR2(500) PATH
                      '@СуммаПосредника',
                      AmountOfSCS VARCHAR2(500) PATH '@СуммаСКС',
                      AmountWithChecks VARCHAR2(500) PATH '@СуммаСЧеков',
                      Counter VARCHAR2(500) PATH '@Счетчик',
                      Terminal VARCHAR2(500) PATH '@Терминал',
                      TerminalNetwork VARCHAR2(500) PATH '@ТерминальнаяСеть',
                      TransactionType VARCHAR2(500) PATH '@ТипТранзакции',
                      Service VARCHAR2(500) PATH '@Услуга',
                      FileTransactions VARCHAR2(500) PATH '@ФайлТранзакции',
                      FIO VARCHAR2(500) PATH '@ФИО',
                      ChecksIncoming VARCHAR2(500) PATH '@ЧекиВходящие',
                      Barcode VARCHAR2(500) PATH '@ШтрихКод',
                      IsAResident VARCHAR2(500) PATH '@ЯвляетсяРезидентом',
                      ValueNotFound VARCHAR2(500) PATH '@ЗначениеНеНайдено',
                      ProviderTariff VARCHAR2(500) PATH '@ТарифПровайдера',
                      CounterChecks VARCHAR2(500) PATH '@СчетчикСчеков',
                      CounterCheck VARCHAR2(500) PATH '@СчетчикНаЧек',
                      Id_ VARCHAR2(500) PATH '@Id',
                      Detailing VARCHAR2(500) PATH '@Деталировка',
                      WalletPayer VARCHAR2(500) PATH '@КошелекПлательщик',
                      WalletReceiver VARCHAR2(500) PATH '@КошелекПолучатель',
                      PurposeOfPayment VARCHAR2(500) PATH
                      '@НазначениеПлатежа',
                      DataProvider VARCHAR2(500) PATH '@ДатаПровайдера',
                      Attributes_ xmltype PATH 'Атрибуты') g
       where t.SESS_ID = idfile;
  
    CURSOR file_find(chk_ varchar2) IS
      SELECT count(*) cnt
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/Транзакции/Трн' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(500) PATH '@Валюта',
                      PaymentType VARCHAR2(500) PATH '@ВидПлатежа',
                      VK VARCHAR2(500) PATH '@ВК',
                      DateOfOperation VARCHAR2(500) PATH '@ДатаОперации',
                      DataPs VARCHAR2(500) PATH '@ДатаПС',
                      DateClearing VARCHAR2(500) PATH '@ДатаКлиринга',
                      Dealer VARCHAR2(500) PATH '@Дилер',
                      AccountPayer VARCHAR2(500) PATH '@ЛСПлательщика',
                      CardNumber VARCHAR2(500) PATH '@НомерКарты',
                      OperationNumber VARCHAR2(500) PATH '@НомерОперации',
                      OperationNumberDelivery VARCHAR2(500) PATH
                      '@НомерОперацииСдача',
                      CheckNumber VARCHAR2(500) PATH '@НомерЧека',
                      CheckParent VARCHAR2(500) PATH '@ЧекРодитель',
                      OrderOfProvidence VARCHAR2(500) PATH
                      '@ПорядокПровдения',
                      Provider VARCHAR2(500) PATH '@Провайдер',
                      OwnInOwn VARCHAR2(500) PATH '@СвойВСвоем',
                      Corrected VARCHAR2(500) PATH '@Скорректирована',
                      CommissionRate VARCHAR2(500) PATH '@СтавкаКомиссии',
                      Status VARCHAR2(500) PATH '@Статус',
                      StringFromFile VARCHAR2(500) PATH '@СтрокаИзФайла',
                      RewardAmount VARCHAR2(500) PATH '@СуммаВознаграждения',
                      OwnerIncomeAmount VARCHAR2(500) PATH
                      '@СуммаДоходВладельца',
                      CommissionAmount VARCHAR2(500) PATH '@СуммаКомиссии',
                      NKAmount VARCHAR2(500) PATH '@СуммаНК',
                      MaxCommissionAmount VARCHAR2(500) PATH
                      '@СуммаКомиссииМакс',
                      MinCommissionAmount VARCHAR2(500) PATH
                      '@СуммаКомиссииМин',
                      CashAmount VARCHAR2(500) PATH '@СуммаНаличных',
                      SumNalPrimal VARCHAR2(500) PATH '@СуммаНалИзначальная',
                      AmountToCheck VARCHAR2(500) PATH '@СуммаНаЧек',
                      AmountOfPayment VARCHAR2(500) PATH '@СуммаПлатежа',
                      SumOfSplitting VARCHAR2(500) PATH
                      '@СуммаНаРасщепление',
                      AmountIntermediary VARCHAR2(500) PATH
                      '@СуммаПосредника',
                      AmountOfSCS VARCHAR2(500) PATH '@СуммаСКС',
                      AmountWithChecks VARCHAR2(500) PATH '@СуммаСЧеков',
                      Counter VARCHAR2(500) PATH '@Счетчик',
                      Terminal VARCHAR2(500) PATH '@Терминал',
                      TerminalNetwork VARCHAR2(500) PATH '@ТерминальнаяСеть',
                      TransactionType VARCHAR2(500) PATH '@ТипТранзакции',
                      Service VARCHAR2(500) PATH '@Услуга',
                      FileTransactions VARCHAR2(500) PATH '@ФайлТранзакции',
                      FIO VARCHAR2(500) PATH '@ФИО',
                      ChecksIncoming VARCHAR2(500) PATH '@ЧекиВходящие',
                      Barcode VARCHAR2(500) PATH '@ШтрихКод',
                      IsAResident VARCHAR2(500) PATH '@ЯвляетсяРезидентом',
                      ValueNotFound VARCHAR2(500) PATH '@ЗначениеНеНайдено',
                      ProviderTariff VARCHAR2(500) PATH '@ТарифПровайдера',
                      CounterChecks VARCHAR2(500) PATH '@СчетчикСчеков',
                      CounterCheck VARCHAR2(500) PATH '@СчетчикНаЧек',
                      Id_ VARCHAR2(500) PATH '@Id',
                      Detailing VARCHAR2(500) PATH '@Деталировка',
                      WalletPayer VARCHAR2(500) PATH '@КошелекПлательщик',
                      WalletReceiver VARCHAR2(500) PATH '@КошелекПолучатель',
                      PurposeOfPayment VARCHAR2(500) PATH
                      '@НазначениеПлатежа',
                      DataProvider VARCHAR2(500) PATH '@ДатаПровайдера',
                      Attributes_ xmltype PATH 'Атрибуты') g
       where CheckNumber = chk_
         and t.SESS_ID = idfile;
  begin
    /*\*Вставка данных о загрузке*\
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
    \*Вставка данных о загрузке*\*/
  
    begin
      for r in xml_razbor loop
        if r.provider = 'СберБанк' then
          select count(*)
            into serch_service
            from z_sb_termserv_amra_dbt t
           where t.idterm = r.terminal
             and t.name = r.service;
          if serch_service > 0 then
            null;
          elsif serch_service = 0 then
            writelog(r.DATEOFOPERATION,
                     'Внимание! Транзакция CheckNumber = ' || r.CheckNumber ||
                     ' Использует услугу ' || r.service ||
                     ' Которая не настроена!',
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
            /*ID терминала на котором возникла сдача*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into term_deal
              From dual
             where level = 3
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            /*номер чека со сдачей*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into pnmb
              From dual
             where level = 1
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            /*дата формирования транзакции которая сформировала сдачу*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into recdp
              From dual
             where level = 4
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
          
            /*сумма сдачи*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into sump
              From dual
             where level = 2
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
          
            /*наш или нет терминал*/
            select count(*)
              into bnk_deal
              from z_sb_terminal_amra_dbt t
             where t.name = term_deal;
          
            /*если наш терминал*/
            if bnk_deal > 0 then
              /*ищем совпадения сдачи*/
              select count(*)
                into findchk
                from z_sb_termdeal_amra_dbt t
               where t.paymentnumber = pnmb
                 and t.STATUS = 0;
              /*если совпадении нет*/
              if findchk = 0 then
                /*ищем в xml*/
                OPEN file_find(pnmb);
                LOOP
                  FETCH file_find
                    INTO findchkf;
                  EXIT WHEN file_find%notfound;
                end loop;
                CLOSE file_find;
                /*если нигде нет*/
                if findchkf = 0 then
                  writelog(r.DATEOFOPERATION,
                           'Внимание! Транзакция CheckNumber = ' ||
                           r.CheckNumber || ' Использует оплату по чеку ' || pnmb ||
                           ' Которого нет ни в файле ни в БД!',
                           idfile);
                end if;
              end if;
              /*если найден и уже погашен*/
              select count(*)
                into findchk_dubl
                from z_sb_termdeal_amra_dbt t
               where t.paymentnumber = pnmb
                 and t.STATUS = 1;
              /*если найден и уже погашен*/
              if findchk_dubl <> 0 then
                writelog(r.DATEOFOPERATION,
                         'Внимание! Транзакция CheckNumber = ' ||
                         r.CheckNumber || ' Использует оплату по чеку ' || pnmb ||
                         ' Котороый уже погашен!',
                         idfile);
              end if;
              /*если терминал Амра*/
            elsif bnk_deal = 0 and substr(term_deal, 1, 2) in ('АБ', 'IV') then
              /*ищем совпадения сдачи*/
              select count(*)
                into deal_dubl_num
                from Z_SB_TERMDEAL_AMRA_FEB t
               where t.PAYMENTNUMBER = pnmb;
              /*если нет совпадении*/
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
                                                               ' ',
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
                   to_number(replace(replace(sump, ' ', ''), '.', ',')),
                   1,
                   idfile,
                   term_deal || '->' || r.TERMINAL);
              else
                /*если есть*/
                writelog(r.DATEOFOPERATION,
                         'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                         ' уже загружена в АБС ' || recdp ||
                         ' и помечена как удачная. Направление сдачи ' ||
                         r.TERMINAL || '->' || term_deal,
                         idfile);
              end if;
              /*если терминал не амра*/
            elsif substr(term_deal, 1, 2) not in ('АБ', 'IV') and
                  bnk_deal = 0 then
              writelog(r.DATEOFOPERATION,
                       'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                       ' дата ' || recdp || ' не является АБ или SB! ' ||
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
        /*Загрузка, если нет ошибок*/
        for r in xml_razbor loop
        
          /*Разбор дополнительных атрибутов*/
          /*for row2 in (SELECT Service,
                              CheckNumber,
                              AttributeName,
                              AttributeValue
                         FROM XMLTABLE('/Атрибуты/Атр' PASSING r.Attributes_
                                       COLUMNS Service VARCHAR2(500) PATH
                                       '@Услуга',
                                       CheckNumber VARCHAR2(500) PATH
                                       '@НомерЧека',
                                       AttributeName VARCHAR2(500) PATH
                                       '@ИмяАтрибута',
                                       AttributeValue VARCHAR2(500) PATH
                                       '@ЗначениеАтрибута')) loop
          end loop;*/
          /*Разбор дополнительных атрибутов*/
        
          select count(*)
            into dubl_number
            from z_sb_transact_amra_dbt t
           where t.checknumber = r.CheckNumber;
        
          select count(*)
            into deal_dubl_number
            from Z_SB_TERMDEAL_AMRA_DBT t
           where t.PAYMENTNUMBER = r.CheckNumber;
        
          if dubl_number = 0 and r.TRANSACTIONTYPE <> 'Инкассация' then
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
               to_number(replace(replace(r.CommissionAmount, ' ', ''),
                                 '.',
                                 ',')),
               r.NKAmount,
               r.MaxCommissionAmount,
               r.MinCommissionAmount,
               to_number(replace(replace(r.CashAmount, ' ', ''), '.', ',')),
               r.SumNalPrimal,
               to_number(replace(replace(r.AmountToCheck, ' ', ''),
                                 '.',
                                 ',')),
               to_number(replace(replace(r.AmountOfPayment, ' ', ''),
                                 '.',
                                 ',')),
               r.SumOfSplitting,
               r.AmountIntermediary,
               r.AmountOfSCS,
               to_number(replace(replace(r.AmountWithChecks, ' ', ''),
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
          elsif r.TRANSACTIONTYPE = 'Инкассация' then
            select count(*)
              into dubl_number
              from z_sb_transact_amra_inkas t
             where t.PAYDATE =
                   to_date(r.DATEOFOPERATION, 'DD-MM-RRRR HH24:MI:SS')
               and t.AMOUNTOFPAYMENT =
                   to_number(replace(replace(r.AmountOfPayment, ' ', ''),
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
                 to_number(replace(replace(r.CommissionAmount, ' ', ''),
                                   '.',
                                   ',')),
                 r.NKAmount,
                 r.MaxCommissionAmount,
                 r.MinCommissionAmount,
                 to_number(replace(replace(r.CashAmount, ' ', ''), '.', ',')),
                 r.SumNalPrimal,
                 to_number(replace(replace(r.AmountToCheck, ' ', ''),
                                   '.',
                                   ',')),
                 to_number(replace(replace(r.AmountOfPayment, ' ', ''),
                                   '.',
                                   ',')),
                 r.SumOfSplitting,
                 r.AmountIntermediary,
                 r.AmountOfSCS,
                 to_number(replace(replace(r.AmountWithChecks, ' ', ''),
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
                     to_number(replace(replace(r.AmountOfPayment, ' ', ''),
                                       '.',
                                       ','))
                 and t.FILETRANSACTIONS = r.FILETRANSACTIONS;
            
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция Инкассаций уже загружена в АБС ' ||
                       recdate_ || ' и помечена как удачная.',
                       idfile);
            end if;
          elsif dubl_number <> 0 and r.TRANSACTIONTYPE <> 'Инкассация' then
            select t.RECDATE
              into recdate_
              from z_sb_transact_amra_dbt t
             where t.checknumber = r.CheckNumber;
            writelog(r.DATEOFOPERATION,
                     'Внимание! Транзакция CheckNumber = ' || r.CheckNumber ||
                     ' уже загружена в АБС ' || recdate_ ||
                     ' и помечена как удачная.',
                     idfile);
          end if;
        
          if to_number(replace(replace(r.AMOUNTTOCHECK, ' ', ''), '.', ',')) <> 0 then
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
                 to_number(replace(replace(r.AMOUNTTOCHECK, ' ', ''),
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
                       'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                       ' уже загружена в АБС ' || recdate_ ||
                       ' и помечена как удачная.',
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
    /*Основная Функция обработки транзакции*/
  
    /*Возврат результата, если ошибка*/
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
    /*Возврат результата, если ошибка*/
  
    RETURN ret || ':' || TO_CHAR(idfile);
  end;
begin
  null;
end z_sb_create_tr_amra;
/

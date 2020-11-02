create or replace package z_sb_create_tr_amra is

  -- Author  : SAIDP
  -- Created : 18.06.2019 14:11:48
  -- Purpose : 
  function load_pack(idfile number) return varchar2;
  FUNCTION makedprt(str VARCHAR2) RETURN VARCHAR;
  function get_attribure(sess_id_  number,
                         paramname varchar2,
                         pnmb      varchar2) return varchar2;
  function get_attribure2(sess_id_  number,
                          paramname varchar2,
                          pnmb      varchar2) return varchar2;
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

  /*Функция добавления файла*/
  FUNCTION fn_sess_add(fileclob_ clob, path varchar2, file_name_ VARCHAR2)
    RETURN VARCHAR IS
    ret       varchar(500);
    dubl      number;
    session   number;
    upd_clob  clob;
    file_date date;
  BEGIN
    begin
      upd_clob := fileclob_;
    
      /*Замена ID терминала на наш*/
      for r in (select NAME self_code, SDNAME en_code
                  from Z_SB_TERMINAL_AMRA_DBT) loop
        upd_clob := replace(upd_clob, r.en_code, r.self_code);
      end loop;
    
      /*Если был загружен ранее*/
      if upd_clob is not null then
        select count(*)
          into dubl
          from Z_SB_FN_SESS_AMRA t
         where dbms_lob.compare(t.fileclob, upd_clob) = 0;
      
        if dubl = 0 then
          session := z_sb_sq_sess_id.NEXTVAL;
          insert into Z_SB_FN_SESS_AMRA
            (sess_id, file_name, date_time, fileclob, status, path, USER_)
          values
            (session, file_name_, sysdate, upd_clob, 0, path, user);
          ret := 'Inserted;' || session;
          commit;
        elsif dubl = 1 then
          rollback;
          ret := 'Dublicate;0';
        end if;
      else
        ret := 'Exception;Error in replace';
      end if;
      --ret := 'Exception;Нет прав на загрузку!';
    exception
      when others then
        rollback;
        ret := 'Exception;' || SQLERRM;
    END;
  
    /*Проверка даты файла на текущий*/
    begin
      select distinct to_date(trunc(to_date(g.DateOfOperation,
                                            'DD-MM-RRRR HH24:MI:SS')),
                              'DD.MM.RRRR')
        into file_date
        from XMLTABLE('/Транзакции/Трн' PASSING xmltype(upd_clob) COLUMNS
                      DateOfOperation VARCHAR2(500) PATH '@ДатаОперации') g;
      if file_date = trunc(sysdate) then
        ret := 'Exception;Дата файла идентична текущей!';
      end if;
    exception
      when others then
        rollback;
        ret := 'Exception;' || SQLERRM;
    end;
    return ret;
  end fn_sess_add;

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

  function get_budinfo(sess_id_ number, paramname varchar2, pnmb varchar2)
    return varchar2 is
    ret varchar2(500) := null;
  begin
    select ATTRIBUTEVALUE
      into ret
      from z_sb_transact_amra_dbt t,
           XMLTABLE('/Атрибуты/Атр' PASSING xmltype(ATTRIBUTES_) COLUMNS
                    Service VARCHAR2(500) PATH '@Услуга',
                    CheckNumber VARCHAR2(500) PATH '@НомерЧека',
                    AttributeName VARCHAR2(500) PATH '@ИмяАтрибута',
                    AttributeValue VARCHAR2(500) PATH '@ЗначениеАтрибута') atr
     where SESS_ID = sess_id_
       and t.checknumber = pnmb
       and AttributeName = paramname;
    return ret;
  end;

  function get_attribure(sess_id_  number,
                         paramname varchar2,
                         pnmb      varchar2) return varchar2 is
    ret varchar2(500) := null;
  begin
    select ATTRIBUTEVALUE
      into ret
      from z_sb_transact_amra_dbt t,
           XMLTABLE('/Атрибуты/Атр' PASSING xmltype(ATTRIBUTES_) COLUMNS
                    Service VARCHAR2(500) PATH '@Услуга',
                    CheckNumber VARCHAR2(500) PATH '@НомерЧека',
                    AttributeName VARCHAR2(500) PATH '@ИмяАтрибута',
                    AttributeValue VARCHAR2(500) PATH '@ЗначениеАтрибута') atr
     where SESS_ID = sess_id_
       and t.checknumber = pnmb
       and AttributeName = paramname;
    return ret;
  end;

  function get_attribure2(sess_id_  number,
                          paramname varchar2,
                          pnmb      varchar2) return varchar2 is
    ret varchar2(500) := null;
  begin
    select ATTRIBUTEVALUE
      into ret
      from z_sb_transact_amra_dbt t,
           XMLTABLE('/Атрибуты/Атр' PASSING xmltype(ATTRIBUTES_) COLUMNS
                    Service VARCHAR2(500) PATH '@Услуга',
                    CheckNumber VARCHAR2(500) PATH '@НомерЧека',
                    AttributeName VARCHAR2(500) PATH '@ИмяАтрибута',
                    AttributeValue VARCHAR2(500) PATH '@ЗначениеАтрибута') atr
     where SESS_ID = sess_id_
       and AttributeName = paramname
       and t.checknumber = pnmb
       and rownum = 1;
    return ret;
  end;

  /*Основная Функция обработки транзакции*/
  function load_pack(idfile number) return varchar2 is
  
    ret              VARCHAR(50);
    res              number;
    custom_atr_cnt   number;
    sum_withattr     number;
    dubl_ret_cnt     number;
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
  
    tax_kbk       varchar2(100);
    serch_service number;
  
    pnmb  varchar2(50);
    recdp varchar2(50);
    sump  varchar2(50);
  
    cursor custom_looper(sid number, pnmb varchar2) is
      with dat as
       (select rownum rn, atr.*
          from (SELECT g.ATTRIBUTES_ ATTRIBUTES_, g.checknumber
                  FROM Z_SB_FN_SESS_AMRA t,
                       XMLTABLE('/Транзакции/Трн' PASSING xmltype(FILECLOB)
                                COLUMNS CheckNumber VARCHAR2(4000) PATH
                                '@НомерЧека',
                                Attributes_ xmltype PATH 'Атрибуты') g
                 where t.SESS_ID = sid) g,
               XMLTABLE('/Атрибуты/Атр' PASSING g.ATTRIBUTES_ COLUMNS
                        Service VARCHAR2(500) PATH '@Услуга',
                        CheckNumber VARCHAR2(500) PATH '@НомерЧека',
                        AttributeName VARCHAR2(500) PATH '@ИмяАтрибута',
                        AttributeValue VARCHAR2(500) PATH '@ЗначениеАтрибута') atr
         where g.checknumber = pnmb),
      ATTR_VIEW as
       (select *
          from dat
        PIVOT(listagg(AttributeValue, '@~@') within
         group(
         order by rn)
           FOR ATTRIBUTENAME IN('CountPay' "CountPay",
                               'DetailCheque' "DetailCheque",
                               'HeadCheque' "HeadCheque",
                               'Id_Платежа' "Id_Платежа",
                               'Price' "Price",
                               'account' "account",
                               'БанкПолучатель' "БанкПолучатель",
                               'Бик' "Бик",
                               'ВидПлатежа' "ВидПлатежа",
                               'ВидПлатежаНаименование'
                               "ВидПлатежаНаименование",
                               'ДатаОперации' "ДатаОперации",
                               'ИннПлательщик' "ИннПлательщик",
                               'ИннПолучатель' "ИннПолучатель",
                               'КБК' "КБК",
                               'КппБанкПолучатель' "КппБанкПолучатель",
                               'КппПлательщика' "КппПлательщика",
                               'ЛицевойСчетПолучателя'
                               "ЛицевойСчетПолучателя",
                               'НомерСтроки' "НомерСтроки",
                               'Номера чеков' "Номера чеков",
                               'Основание' "Основание",
                               'Плательщик' "Плательщик",
                               'Получатель' "Получатель",
                               'РасчетныйСчет' "РасчетныйСчет",
                               'Сумма' "Сумма"))),
      aggr_attr as
       (select "CountPay",
               "DetailCheque",
               "HeadCheque",
               trim((SELECT REGEXP_SUBSTR("Id_Платежа",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "Id_Платежа",
               "Price",
               "account",
               trim((SELECT REGEXP_SUBSTR("БанкПолучатель",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "БанкПолучатель",
               trim((SELECT REGEXP_SUBSTR("Бик", '[^@~@]+', 1, t1.l)
                      FROM DUAL)) "Бик",
               trim((SELECT REGEXP_SUBSTR("ВидПлатежа",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "ВидПлатежа",
               trim((SELECT REGEXP_SUBSTR("ВидПлатежаНаименование",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "ВидПлатежаНаименование",
               trim((SELECT REGEXP_SUBSTR("ДатаОперации",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "ДатаОперации",
               trim((SELECT REGEXP_SUBSTR("ИннПлательщик",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "ИннПлательщик",
               trim((SELECT REGEXP_SUBSTR("ИннПолучатель",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "ИннПолучатель",
               trim((SELECT REGEXP_SUBSTR("КБК", '[^@~@]+', 1, t1.l)
                      FROM DUAL)) "КБК",
               trim((SELECT REGEXP_SUBSTR("КппБанкПолучатель",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "КппБанкПолучатель",
               trim((SELECT REGEXP_SUBSTR("КппПлательщика",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "КппПлательщика",
               trim((SELECT REGEXP_SUBSTR("ЛицевойСчетПолучателя",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "ЛицевойСчетПолучателя",
               trim((SELECT REGEXP_SUBSTR("НомерСтроки",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "НомерСтроки",
               trim((SELECT REGEXP_SUBSTR("Номера чеков",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "Номера чеков",
               trim((SELECT REGEXP_SUBSTR("Основание",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "Основание",
               trim((SELECT REGEXP_SUBSTR("Плательщик",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "Плательщик",
               trim((SELECT REGEXP_SUBSTR("Получатель",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "Получатель",
               trim((SELECT REGEXP_SUBSTR("РасчетныйСчет",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "РасчетныйСчет"
          from ATTR_VIEW t,
               (SELECT LEVEL l FROM DUAL CONNECT BY LEVEL <= 20) t1
         WHERE ((t1.l <= t."CountPay") OR (t1.l = 1)))
      select "ВидПлатежаНаименование" names,
             "КБК"                                       kbks
        from aggr_attr;
  
    CURSOR xml_razbor IS
      SELECT g.*
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/Транзакции/Трн' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(4000) PATH '@Валюта',
                      PaymentType VARCHAR2(4000) PATH '@ВидПлатежа',
                      VK VARCHAR2(4000) PATH '@ВК',
                      DateOfOperation VARCHAR2(4000) PATH '@ДатаОперации',
                      DataPs VARCHAR2(4000) PATH '@ДатаПС',
                      DateClearing VARCHAR2(4000) PATH '@ДатаКлиринга',
                      Dealer VARCHAR2(4000) PATH '@Дилер',
                      AccountPayer VARCHAR2(4000) PATH '@ЛСПлательщика',
                      CardNumber VARCHAR2(4000) PATH '@НомерКарты',
                      OperationNumber VARCHAR2(4000) PATH '@НомерОперации',
                      OperationNumberDelivery VARCHAR2(4000) PATH
                      '@НомерОперацииСдача',
                      CheckNumber VARCHAR2(4000) PATH '@НомерЧека',
                      CheckParent VARCHAR2(4000) PATH '@ЧекРодитель',
                      OrderOfProvidence VARCHAR2(4000) PATH
                      '@ПорядокПровдения',
                      Provider VARCHAR2(4000) PATH '@Провайдер',
                      OwnInOwn VARCHAR2(4000) PATH '@СвойВСвоем',
                      Corrected VARCHAR2(4000) PATH '@Скорректирована',
                      CommissionRate VARCHAR2(4000) PATH '@СтавкаКомиссии',
                      Status VARCHAR2(4000) PATH '@Статус',
                      StringFromFile VARCHAR2(4000) PATH '@СтрокаИзФайла',
                      RewardAmount VARCHAR2(4000) PATH
                      '@СуммаВознаграждения',
                      OwnerIncomeAmount VARCHAR2(4000) PATH
                      '@СуммаДоходВладельца',
                      CommissionAmount VARCHAR2(4000) PATH '@СуммаКомиссии',
                      NKAmount VARCHAR2(4000) PATH '@СуммаНК',
                      MaxCommissionAmount VARCHAR2(4000) PATH
                      '@СуммаКомиссииМакс',
                      MinCommissionAmount VARCHAR2(4000) PATH
                      '@СуммаКомиссииМин',
                      CashAmount VARCHAR2(4000) PATH '@СуммаНаличных',
                      SumNalPrimal VARCHAR2(4000) PATH
                      '@СуммаНалИзначальная',
                      AmountToCheck VARCHAR2(4000) PATH '@СуммаНаЧек',
                      AmountOfPayment VARCHAR2(4000) PATH '@СуммаПлатежа',
                      SumOfSplitting VARCHAR2(4000) PATH
                      '@СуммаНаРасщепление',
                      AmountIntermediary VARCHAR2(4000) PATH
                      '@СуммаПосредника',
                      AmountOfSCS VARCHAR2(4000) PATH '@СуммаСКС',
                      AmountWithChecks VARCHAR2(4000) PATH '@СуммаСЧеков',
                      Counter VARCHAR2(4000) PATH '@Счетчик',
                      Terminal VARCHAR2(4000) PATH '@Терминал',
                      TerminalNetwork VARCHAR2(4000) PATH
                      '@ТерминальнаяСеть',
                      TransactionType VARCHAR2(4000) PATH '@ТипТранзакции',
                      Service VARCHAR2(4000) PATH '@Услуга',
                      FileTransactions VARCHAR2(4000) PATH '@ФайлТранзакции',
                      FIO VARCHAR2(4000) PATH '@ФИО',
                      ChecksIncoming VARCHAR2(4000) PATH '@ЧекиВходящие',
                      Barcode VARCHAR2(4000) PATH '@ШтрихКод',
                      IsAResident VARCHAR2(4000) PATH '@ЯвляетсяРезидентом',
                      ValueNotFound VARCHAR2(4000) PATH '@ЗначениеНеНайдено',
                      ProviderTariff VARCHAR2(4000) PATH '@ТарифПровайдера',
                      CounterChecks VARCHAR2(4000) PATH '@СчетчикСчеков',
                      CounterCheck VARCHAR2(4000) PATH '@СчетчикНаЧек',
                      Id_ VARCHAR2(4000) PATH '@Id',
                      Detailing VARCHAR2(4000) PATH '@Деталировка',
                      WalletPayer VARCHAR2(4000) PATH '@КошелекПлательщик',
                      WalletReceiver VARCHAR2(4000) PATH
                      '@КошелекПолучатель',
                      PurposeOfPayment VARCHAR2(4000) PATH
                      '@НазначениеПлатежа',
                      DataProvider VARCHAR2(4000) PATH '@ДатаПровайдера',
                      Attributes_ xmltype PATH 'Атрибуты',
                      ChecksIncoming_ xmltype PATH '@ЧекиВходящие') g
       where t.SESS_ID = idfile;
  
    CURSOR file_find(chk_ varchar2) IS
      SELECT count(*) cnt
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/Транзакции/Трн' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(4000) PATH '@Валюта',
                      PaymentType VARCHAR2(4000) PATH '@ВидПлатежа',
                      VK VARCHAR2(4000) PATH '@ВК',
                      DateOfOperation VARCHAR2(4000) PATH '@ДатаОперации',
                      DataPs VARCHAR2(4000) PATH '@ДатаПС',
                      DateClearing VARCHAR2(4000) PATH '@ДатаКлиринга',
                      Dealer VARCHAR2(4000) PATH '@Дилер',
                      AccountPayer VARCHAR2(4000) PATH '@ЛСПлательщика',
                      CardNumber VARCHAR2(4000) PATH '@НомерКарты',
                      OperationNumber VARCHAR2(4000) PATH '@НомерОперации',
                      OperationNumberDelivery VARCHAR2(4000) PATH
                      '@НомерОперацииСдача',
                      CheckNumber VARCHAR2(4000) PATH '@НомерЧека',
                      CheckParent VARCHAR2(4000) PATH '@ЧекРодитель',
                      OrderOfProvidence VARCHAR2(4000) PATH
                      '@ПорядокПровдения',
                      Provider VARCHAR2(4000) PATH '@Провайдер',
                      OwnInOwn VARCHAR2(4000) PATH '@СвойВСвоем',
                      Corrected VARCHAR2(4000) PATH '@Скорректирована',
                      CommissionRate VARCHAR2(4000) PATH '@СтавкаКомиссии',
                      Status VARCHAR2(4000) PATH '@Статус',
                      StringFromFile VARCHAR2(4000) PATH '@СтрокаИзФайла',
                      RewardAmount VARCHAR2(4000) PATH
                      '@СуммаВознаграждения',
                      OwnerIncomeAmount VARCHAR2(4000) PATH
                      '@СуммаДоходВладельца',
                      CommissionAmount VARCHAR2(4000) PATH '@СуммаКомиссии',
                      NKAmount VARCHAR2(4000) PATH '@СуммаНК',
                      MaxCommissionAmount VARCHAR2(4000) PATH
                      '@СуммаКомиссииМакс',
                      MinCommissionAmount VARCHAR2(4000) PATH
                      '@СуммаКомиссииМин',
                      CashAmount VARCHAR2(4000) PATH '@СуммаНаличных',
                      SumNalPrimal VARCHAR2(4000) PATH
                      '@СуммаНалИзначальная',
                      AmountToCheck VARCHAR2(4000) PATH '@СуммаНаЧек',
                      AmountOfPayment VARCHAR2(4000) PATH '@СуммаПлатежа',
                      SumOfSplitting VARCHAR2(4000) PATH
                      '@СуммаНаРасщепление',
                      AmountIntermediary VARCHAR2(4000) PATH
                      '@СуммаПосредника',
                      AmountOfSCS VARCHAR2(4000) PATH '@СуммаСКС',
                      AmountWithChecks VARCHAR2(4000) PATH '@СуммаСЧеков',
                      Counter VARCHAR2(4000) PATH '@Счетчик',
                      Terminal VARCHAR2(4000) PATH '@Терминал',
                      TerminalNetwork VARCHAR2(4000) PATH
                      '@ТерминальнаяСеть',
                      TransactionType VARCHAR2(4000) PATH '@ТипТранзакции',
                      Service VARCHAR2(4000) PATH '@Услуга',
                      FileTransactions VARCHAR2(4000) PATH '@ФайлТранзакции',
                      FIO VARCHAR2(4000) PATH '@ФИО',
                      ChecksIncoming VARCHAR2(4000) PATH '@ЧекиВходящие',
                      Barcode VARCHAR2(4000) PATH '@ШтрихКод',
                      IsAResident VARCHAR2(4000) PATH '@ЯвляетсяРезидентом',
                      ValueNotFound VARCHAR2(4000) PATH '@ЗначениеНеНайдено',
                      ProviderTariff VARCHAR2(4000) PATH '@ТарифПровайдера',
                      CounterChecks VARCHAR2(4000) PATH '@СчетчикСчеков',
                      CounterCheck VARCHAR2(4000) PATH '@СчетчикНаЧек',
                      Id_ VARCHAR2(4000) PATH '@Id',
                      Detailing VARCHAR2(4000) PATH '@Деталировка',
                      WalletPayer VARCHAR2(4000) PATH '@КошелекПлательщик',
                      WalletReceiver VARCHAR2(4000) PATH
                      '@КошелекПолучатель',
                      PurposeOfPayment VARCHAR2(4000) PATH
                      '@НазначениеПлатежа',
                      DataProvider VARCHAR2(4000) PATH '@ДатаПровайдера',
                      Attributes_ xmltype PATH 'Атрибуты') g
       where CheckNumber = chk_
         and t.SESS_ID = idfile;
  
    serch_terminal_1       varchar2(500);
    ChecksIncoming_varchar varchar2(32767);
    file_status            number := null;
    CHECKPARENT_           number;
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
    
      select t.STATUS
        into file_status
        from xxi.z_sb_fn_sess_amra t
       where t.SESS_ID = idfile;
    
      /*Проверка статуса файла*/
      if file_status = 0 then
        /*Проверка длины строки содержащую чеки оплыты. Проверка на статуса у переотправленного файла*/
        for r in xml_razbor loop
          ChecksIncoming_varchar := dbms_lob.substr(r.ChecksIncoming_.getClobVal(),
                                                    32767,
                                                    1);
          if length(ChecksIncoming_varchar) > 4000 then
            writelog(r.DATEOFOPERATION,
                     'Внимание! Транзакция CheckNumber = ' || r.CheckNumber ||
                     ' Использует сдачу varchar2 > 4000 символов. Настрой через CLOB!!!',
                     idfile);
          end if;
          /*<-->CHECK_STATUS_18<-->*/
          if r.Status = '18' and r.CHECKPARENT is not null then
            writelog(r.DATEOFOPERATION,
                     'Внимание! Транзакция CheckNumber = ' || r.CheckNumber ||
                     ' Использует платеж каторый переотправлен с недопустимым статусом!!!',
                     idfile);
          end if;
          /*Without money*/
          if r.CASHAMOUNT is null and r.CHECKSINCOMING is null then
            writelog(r.DATEOFOPERATION,
                     'Внимание! Транзакция CheckNumber = ' || r.CheckNumber ||
                     ' Возможно мошенничество, сумма наличных и оплат со сдачи 0!!!',
                     idfile);
          end if;
        
          begin
            /*Проверка комиссии*/
            if substr(r.NKAMOUNT, 1, 1) = '-' then
              if (to_number(replace(replace(substr(r.NKAMOUNT,
                                                   instr(r.NKAMOUNT, '-') + 1),
                                            ' ',
                                            ''),
                                    '.',
                                    ',')) >
                 replace(replace(r.COMMISSIONAMOUNT, ' ', ''), '.', ',')) and
                 r.status != '18' then
                writelog(r.DATEOFOPERATION,
                         'Внимание! Транзакция CheckNumber = ' ||
                         r.CheckNumber || ' СуммаНК > СуммаКомиссии! ' ||
                         r.NKAMOUNT,
                         idfile);
              end if;
            end if;
          exception
            when others then
              rollback;
              writelog(descripion => SQLERRM || '~ERROR~' || r.NKAMOUNT ||
                                     '~TR~' || r.CheckNumber,
                       sess_id1   => idfile);
            
          end;
        
          /*Проверка Докатки*/
          if r.transactiontype = 'ДокаткаПлатежа' then
          
            /*ищем в xml*/
            OPEN file_find(r.CheckParent);
            LOOP
              FETCH file_find
                INTO findchkf;
              EXIT WHEN file_find%notfound;
            end loop;
            CLOSE file_find;
          
            select count(*)
              into CHECKPARENT_
              from z_sb_transact_amra_dbt j
             where j.checknumber = r.CHECKPARENT;
            if CHECKPARENT_ = 0 and findchkf = 0 then
              writelog(descripion => 'Отсутствует родительская транзакция =' ||
                                     r.CHECKPARENT || ' в ' || '~TR~' ||
                                     r.CheckNumber,
                       sess_id1   => idfile);
            end if;
          end if;
        end loop;
      
        /*Проверка терминала*/
        for r in xml_razbor loop
          select count(*)
            into serch_terminal_1
            from Z_SB_TERMINAL_AMRA_DBT t
           where t.name = r.terminal;
          if serch_terminal_1 > 0 then
            null;
          elsif serch_terminal_1 = 0 and r.dealer = 'СберБанк' then
            writelog(r.DATEOFOPERATION,
                     'Внимание! Транзакция CheckNumber = ' || r.CheckNumber ||
                     ' Использует терминтал ' || r.terminal ||
                     ' Который не настроен!',
                     idfile);
          end if;
          /*Проверка услуги Направление СБЕР********************/
          if r.provider = 'СберБанк' and r.service = 'ГАИ.Штрафы(Сбербанк)' then
            select count(*)
              into serch_service
              from z_sb_termserv_amra_dbt t
             where t.idterm = r.terminal
               and t.name = r.service
               and t.kbk = '01211501116010000140';
          
            if serch_service > 0 then
              null;
            elsif serch_service = 0 then
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция CheckNumber = ' ||
                       r.CheckNumber || ' Использует услугу ' || r.service ||
                       ' Которая не настроена!',
                       idfile);
            end if;
          elsif r.provider = 'СберБанк' and r.service = 'Дет.сад СБЕРБАНК' then
            select count(*)
              into serch_service
              from z_sb_termserv_amra_dbt t
             where t.idterm = r.terminal
               and t.name = r.service
               and t.kbk = '20300000000000000130';
          
            if serch_service > 0 then
              null;
            elsif serch_service = 0 then
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция CheckNumber = ' ||
                       r.CheckNumber || ' Использует услугу ' || r.service ||
                       ' Которая не настроена!',
                       idfile);
            end if;
          elsif r.provider = 'СберБанк' and
                r.service not in
                ('ГАИ.Штрафы(Сбербанк)',
                 'Дет.сад СБЕРБАНК',
                 'Таможня РА(Сбер)',
                 'Налоги и Сборы (v3)(SB)') then
            select count(*)
              into serch_service
              from z_sb_termserv_amra_dbt t
             where t.idterm = r.terminal
               and t.name = r.service;
          
            if serch_service > 0 then
              null;
            elsif serch_service = 0 then
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция CheckNumber = ' ||
                       r.CheckNumber || ' Использует услугу ' || r.service ||
                       ' Которая не настроена!',
                       idfile);
            end if;
          end if;
        
          /*Проверка услуги Налоги и Сборы (v3)(SB)***********************************/
          if r.provider = 'СберБанк' and
             r.service = 'Налоги и Сборы (v3)(SB)' then
            for r2 in (select (SELECT AttributeValue
                                 FROM XMLTABLE('/Атрибуты/Атр' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@Услуга',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@НомерЧека',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@ИмяАтрибута',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@ЗначениеАтрибута')
                                where ATTRIBUTENAME = 'taxName') tax_name,
                              (SELECT AttributeValue
                                 FROM XMLTABLE('/Атрибуты/Атр' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@Услуга',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@НомерЧека',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@ИмяАтрибута',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@ЗначениеАтрибута')
                                where ATTRIBUTENAME = 'KBK') tax_kbk,
                              (SELECT AttributeValue
                                 FROM XMLTABLE('/Атрибуты/Атр' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@Услуга',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@НомерЧека',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@ИмяАтрибута',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@ЗначениеАтрибута')
                                where ATTRIBUTENAME = 'INNreceiver') tax_inn,
                              (SELECT AttributeValue
                                 FROM XMLTABLE('/Атрибуты/Атр' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@Услуга',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@НомерЧека',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@ИмяАтрибута',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@ЗначениеАтрибута')
                                where ATTRIBUTENAME = 'KPPreceiver') tax_kpp
                         from dual) loop
              select count(*)
                into serch_service
                from z_sb_termserv_amra_dbt t
               where t.idterm = r.terminal
                 and t.name = r2.tax_name
                 and t.KBK = r2.tax_kbk
                 and t.inn = r2.tax_inn
                 and t.kpp = r2.tax_kpp;
              if serch_service > 0 then
                null;
              elsif serch_service = 0 then
                writelog(r.DATEOFOPERATION,
                         'Внимание! Транзакция CheckNumber = ' ||
                         r.CheckNumber || ' Использует услугу "' ||
                         r2.tax_name || '" Которая не настроена!',
                         idfile);
              end if;
            end loop;
          end if;
        
          /*Проверка услуги Таможня РА(Сбер)************************************/
          if r.provider = 'СберБанк' and r.service = 'Таможня РА(Сбер)' then
          
            /*Проверка суммы, если больше суммы платежа*/
            SELECT sum(to_number(replace(replace(ATTRIBUTEVALUE, ' ', ''),
                                         '.',
                                         ',')))
              into sum_withattr
              FROM XMLTABLE('/Атрибуты/Атр' PASSING r.Attributes_ COLUMNS
                            Service VARCHAR2(4000) PATH '@Услуга',
                            CheckNumber VARCHAR2(4000) PATH '@НомерЧека',
                            AttributeName VARCHAR2(4000) PATH '@ИмяАтрибута',
                            AttributeValue VARCHAR2(4000) PATH
                            '@ЗначениеАтрибута')
             where lower(ATTRIBUTENAME) = lower('Сумма');
          
            if ((sum_withattr - mod(sum_withattr, 1)) <>
               to_number(replace(replace(r.AmountOfPayment, ' ', ''),
                                  '.',
                                  ',')) -
               mod(to_number(replace(replace(r.AmountOfPayment, ' ', ''),
                                      '.',
                                      ',')),
                    1)) then
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция CheckNumber = ' ||
                       r.CheckNumber ||
                       ' Сумма платежей из атрибутов превышает сумму платежа!!!! sum_withattr=' ||
                       (sum_withattr - mod(sum_withattr, 1)) ||
                       '<> AmountOfPayment=' ||
                       to_number(replace(replace(r.AmountOfPayment, ' ', ''),
                                         '.',
                                         ',')),
                       idfile);
            end if;
          
            /*Целостность атрибутов таможенного платежа***********/
            SELECT count(*)
              into custom_atr_cnt
              FROM XMLTABLE('/Атрибуты/Атр' PASSING r.Attributes_ COLUMNS
                            Service VARCHAR2(1000) PATH '@Услуга',
                            CheckNumber VARCHAR2(1000) PATH '@НомерЧека',
                            AttributeName VARCHAR2(1000) PATH '@ИмяАтрибута',
                            AttributeValue VARCHAR2(1000) PATH
                            '@ЗначениеАтрибута')
             where lower(ATTRIBUTENAME) in
                   ('сумма', 'кбк', 'иннплательщик');
          
            if custom_atr_cnt = 0 and r.status = '00' then
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция CheckNumber = ' ||
                       r.CheckNumber ||
                       ' Не содержит правильного списка атрибутов!',
                       idfile);
            end if;
            /*-------------ПРОВЕРКА НАЛИЧИЯ УСЛУГ*/
            for r2 in custom_looper(r.checknumber, idfile) loop
              select count(*)
                into serch_service
                from z_sb_termserv_amra_dbt t
               where t.idterm = r.terminal
                 and t.name = r2.names
                 and t.kbk = r2.kbks;
              if serch_service > 0 then
                null;
              elsif serch_service = 0 then
                writelog(r.DATEOFOPERATION,
                         'Внимание! Транзакция CheckNumber = ' ||
                         r.CheckNumber || ' Использует услугу "' ||
                         r2.names || '" Которая не настроена!',
                         idfile);
              end if;
            end loop;
          end if;
        
          /*Проверка чека*/
          if r.CHECKSINCOMING is not null and r.DEALER = 'СберБанк' then
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
              elsif bnk_deal = 0 and
                    substr(term_deal, 1, 2) in ('АБ', 'IV') then
                /*ищем совпадения сдачи*/
                select count(*)
                  into deal_dubl_num
                  from Z_SB_TERMDEAL_AMRA_FEB t
                 where t.PAYMENTNUMBER = pnmb;
                /*если нет совпадении*/
                if deal_dubl_num = 0 and r.Status = '00' then
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
                elsif deal_dubl_num <> 0 then
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
      
        /*<<if_err>>*/
        SELECT count(lg.sess_id)
          into iferr
          FROM z_sb_log_amra lg
         WHERE lg.sess_id = idfile;
      
        /*Если ошибок не было*/
        if iferr = 0 then
          /*Загрузка, если нет ошибок*/
          for r in xml_razbor loop
          
            /*Разбор дополнительных атрибутов*/
            /*for row2 in (SELECT Service,
                                CheckNumber,
                                AttributeName,
                                AttributeValue
                           FROM XMLTABLE('/Атрибуты/Атр' PASSING r.Attributes_
                                         COLUMNS Service VARCHAR2(1000) PATH
                                         '@Услуга',
                                         CheckNumber VARCHAR2(1000) PATH
                                         '@НомерЧека',
                                         AttributeName VARCHAR2(1000) PATH
                                         '@ИмяАтрибута',
                                         AttributeValue VARCHAR2(1000) PATH
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
          
            select count(*)
              into dubl_ret_cnt
              from Z_SB_TRANSACT_AMRA_RET t
             where t.checknumber = r.CheckNumber;
          
            /*Если возврат*/
            if dubl_ret_cnt = 0 and r.status = '25' and dubl_number > 0 then
              insert into z_sb_transact_amra_ret
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
                 sess_id,
                 CHECKSINCOMING_CLOB)
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
                 idfile,
                 r.ChecksIncoming);
            elsif dubl_ret_cnt > 0 and dubl_number = 0 then
              /*Если такая транзакция уже загружена*/
              select t.RECDATE
                into recdate_
                from z_sb_transact_amra_ret t
               where t.checknumber = r.CheckNumber;
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция ВОЗВРАТА!! CheckNumber = ' ||
                       r.CheckNumber || ' уже загружена в АБС ' || recdate_ ||
                       ' и помечена как удачная.',
                       idfile);
              /*GOTO if_err;*/
            end if;
          
            if dubl_number = 0 and r.TRANSACTIONTYPE <> 'Инкассация' and
               r.status <> '25' then
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
                 sess_id,
                 CHECKSINCOMING_CLOB)
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
                 idfile,
                 r.ChecksIncoming);
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
                   to_number(replace(replace(r.CashAmount, ' ', ''),
                                     '.',
                                     ',')),
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
                /*Если такая транзакция (инкассация) уже загружена*/
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
            elsif dubl_number <> 0 and r.TRANSACTIONTYPE <> 'Инкассация' and
                  r.status <> '25' then
              /*Если такая транзакция уже загружена*/
              select t.RECDATE
                into recdate_
                from z_sb_transact_amra_dbt t
               where t.checknumber = r.CheckNumber;
              writelog(r.DATEOFOPERATION,
                       'Внимание! Транзакция CheckNumber = ' ||
                       r.CheckNumber || ' уже загружена в АБС ' || recdate_ ||
                       ' и помечена как удачная.',
                       idfile);
            end if;
            /*Сдачи*/
            if to_number(replace(replace(r.AMOUNTTOCHECK, ' ', ''),
                                 '.',
                                 ',')) <> 0 then
              if deal_dubl_number = 0 /*and r.Status = '00'*/
                 and r.dealer = 'СберБанк' then
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
              elsif deal_dubl_number <> 0 /*and r.Status = '00'*/
               then
                begin
                  select t.RECDATE
                    into recdate_
                    from Z_SB_TERMDEAL_AMRA_DBT t
                   where t.PAYMENTNUMBER = r.CheckNumber;
                exception
                  when others then
                    writelog(descripion => SQLERRM || '|' || r.CheckNumber,
                             sess_id1   => idfile);
                  
                end;
                writelog(r.DATEOFOPERATION,
                         'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                         ' уже загружена в АБС ' || recdate_ ||
                         ' и помечена как удачная.',
                         idfile);
              end if;
            end if;
          end loop;
        end if;
      else
        writelog(trunc(sysdate),
                 'Внимание! Файл с ID = ' || idfile || ' имеет статус = ' ||
                 file_status,
                 idfile);
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

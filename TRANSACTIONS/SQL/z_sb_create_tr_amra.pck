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

  /*������� ���������� �����*/
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
    
      /*������ ID ��������� �� ���*/
      for r in (select NAME self_code, SDNAME en_code
                  from Z_SB_TERMINAL_AMRA_DBT) loop
        upd_clob := replace(upd_clob, r.en_code, r.self_code);
      end loop;
    
      /*���� ��� �������� �����*/
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
      --ret := 'Exception;��� ���� �� ��������!';
    exception
      when others then
        rollback;
        ret := 'Exception;' || SQLERRM;
    END;
  
    /*�������� ���� ����� �� �������*/
    begin
      select distinct to_date(trunc(to_date(g.DateOfOperation,
                                            'DD-MM-RRRR HH24:MI:SS')),
                              'DD.MM.RRRR')
        into file_date
        from XMLTABLE('/����������/���' PASSING xmltype(upd_clob) COLUMNS
                      DateOfOperation VARCHAR2(500) PATH '@������������') g;
      if file_date = trunc(sysdate) then
        ret := 'Exception;���� ����� ��������� �������!';
      end if;
    exception
      when others then
        rollback;
        ret := 'Exception;' || SQLERRM;
    end;
    return ret;
  end fn_sess_add;

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

  function get_budinfo(sess_id_ number, paramname varchar2, pnmb varchar2)
    return varchar2 is
    ret varchar2(500) := null;
  begin
    select ATTRIBUTEVALUE
      into ret
      from z_sb_transact_amra_dbt t,
           XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_) COLUMNS
                    Service VARCHAR2(500) PATH '@������',
                    CheckNumber VARCHAR2(500) PATH '@���������',
                    AttributeName VARCHAR2(500) PATH '@�����������',
                    AttributeValue VARCHAR2(500) PATH '@����������������') atr
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
           XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_) COLUMNS
                    Service VARCHAR2(500) PATH '@������',
                    CheckNumber VARCHAR2(500) PATH '@���������',
                    AttributeName VARCHAR2(500) PATH '@�����������',
                    AttributeValue VARCHAR2(500) PATH '@����������������') atr
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
           XMLTABLE('/��������/���' PASSING xmltype(ATTRIBUTES_) COLUMNS
                    Service VARCHAR2(500) PATH '@������',
                    CheckNumber VARCHAR2(500) PATH '@���������',
                    AttributeName VARCHAR2(500) PATH '@�����������',
                    AttributeValue VARCHAR2(500) PATH '@����������������') atr
     where SESS_ID = sess_id_
       and AttributeName = paramname
       and t.checknumber = pnmb
       and rownum = 1;
    return ret;
  end;

  /*�������� ������� ��������� ����������*/
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
                       XMLTABLE('/����������/���' PASSING xmltype(FILECLOB)
                                COLUMNS CheckNumber VARCHAR2(4000) PATH
                                '@���������',
                                Attributes_ xmltype PATH '��������') g
                 where t.SESS_ID = sid) g,
               XMLTABLE('/��������/���' PASSING g.ATTRIBUTES_ COLUMNS
                        Service VARCHAR2(500) PATH '@������',
                        CheckNumber VARCHAR2(500) PATH '@���������',
                        AttributeName VARCHAR2(500) PATH '@�����������',
                        AttributeValue VARCHAR2(500) PATH '@����������������') atr
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
                               'Id_�������' "Id_�������",
                               'Price' "Price",
                               'account' "account",
                               '��������������' "��������������",
                               '���' "���",
                               '����������' "����������",
                               '����������������������'
                               "����������������������",
                               '������������' "������������",
                               '�������������' "�������������",
                               '�������������' "�������������",
                               '���' "���",
                               '�����������������' "�����������������",
                               '��������������' "��������������",
                               '���������������������'
                               "���������������������",
                               '�����������' "�����������",
                               '������ �����' "������ �����",
                               '���������' "���������",
                               '����������' "����������",
                               '����������' "����������",
                               '�������������' "�������������",
                               '�����' "�����"))),
      aggr_attr as
       (select "CountPay",
               "DetailCheque",
               "HeadCheque",
               trim((SELECT REGEXP_SUBSTR("Id_�������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "Id_�������",
               "Price",
               "account",
               trim((SELECT REGEXP_SUBSTR("��������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "��������������",
               trim((SELECT REGEXP_SUBSTR("���", '[^@~@]+', 1, t1.l)
                      FROM DUAL)) "���",
               trim((SELECT REGEXP_SUBSTR("����������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "����������",
               trim((SELECT REGEXP_SUBSTR("����������������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "����������������������",
               trim((SELECT REGEXP_SUBSTR("������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "������������",
               trim((SELECT REGEXP_SUBSTR("�������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "�������������",
               trim((SELECT REGEXP_SUBSTR("�������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "�������������",
               trim((SELECT REGEXP_SUBSTR("���", '[^@~@]+', 1, t1.l)
                      FROM DUAL)) "���",
               trim((SELECT REGEXP_SUBSTR("�����������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "�����������������",
               trim((SELECT REGEXP_SUBSTR("��������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "��������������",
               trim((SELECT REGEXP_SUBSTR("���������������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "���������������������",
               trim((SELECT REGEXP_SUBSTR("�����������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "�����������",
               trim((SELECT REGEXP_SUBSTR("������ �����",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "������ �����",
               trim((SELECT REGEXP_SUBSTR("���������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "���������",
               trim((SELECT REGEXP_SUBSTR("����������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "����������",
               trim((SELECT REGEXP_SUBSTR("����������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "����������",
               trim((SELECT REGEXP_SUBSTR("�������������",
                                         '[^@~@]+',
                                         1,
                                         t1.l)
                      FROM DUAL)) "�������������"
          from ATTR_VIEW t,
               (SELECT LEVEL l FROM DUAL CONNECT BY LEVEL <= 20) t1
         WHERE ((t1.l <= t."CountPay") OR (t1.l = 1)))
      select "����������������������" names,
             "���"                                       kbks
        from aggr_attr;
  
    CURSOR xml_razbor IS
      SELECT g.*
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/����������/���' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(4000) PATH '@������',
                      PaymentType VARCHAR2(4000) PATH '@����������',
                      VK VARCHAR2(4000) PATH '@��',
                      DateOfOperation VARCHAR2(4000) PATH '@������������',
                      DataPs VARCHAR2(4000) PATH '@������',
                      DateClearing VARCHAR2(4000) PATH '@������������',
                      Dealer VARCHAR2(4000) PATH '@�����',
                      AccountPayer VARCHAR2(4000) PATH '@�������������',
                      CardNumber VARCHAR2(4000) PATH '@����������',
                      OperationNumber VARCHAR2(4000) PATH '@�������������',
                      OperationNumberDelivery VARCHAR2(4000) PATH
                      '@������������������',
                      CheckNumber VARCHAR2(4000) PATH '@���������',
                      CheckParent VARCHAR2(4000) PATH '@�����������',
                      OrderOfProvidence VARCHAR2(4000) PATH
                      '@����������������',
                      Provider VARCHAR2(4000) PATH '@���������',
                      OwnInOwn VARCHAR2(4000) PATH '@����������',
                      Corrected VARCHAR2(4000) PATH '@���������������',
                      CommissionRate VARCHAR2(4000) PATH '@��������������',
                      Status VARCHAR2(4000) PATH '@������',
                      StringFromFile VARCHAR2(4000) PATH '@�������������',
                      RewardAmount VARCHAR2(4000) PATH
                      '@�������������������',
                      OwnerIncomeAmount VARCHAR2(4000) PATH
                      '@�������������������',
                      CommissionAmount VARCHAR2(4000) PATH '@�������������',
                      NKAmount VARCHAR2(4000) PATH '@�������',
                      MaxCommissionAmount VARCHAR2(4000) PATH
                      '@�����������������',
                      MinCommissionAmount VARCHAR2(4000) PATH
                      '@����������������',
                      CashAmount VARCHAR2(4000) PATH '@�������������',
                      SumNalPrimal VARCHAR2(4000) PATH
                      '@�������������������',
                      AmountToCheck VARCHAR2(4000) PATH '@����������',
                      AmountOfPayment VARCHAR2(4000) PATH '@������������',
                      SumOfSplitting VARCHAR2(4000) PATH
                      '@������������������',
                      AmountIntermediary VARCHAR2(4000) PATH
                      '@���������������',
                      AmountOfSCS VARCHAR2(4000) PATH '@��������',
                      AmountWithChecks VARCHAR2(4000) PATH '@�����������',
                      Counter VARCHAR2(4000) PATH '@�������',
                      Terminal VARCHAR2(4000) PATH '@��������',
                      TerminalNetwork VARCHAR2(4000) PATH
                      '@����������������',
                      TransactionType VARCHAR2(4000) PATH '@�������������',
                      Service VARCHAR2(4000) PATH '@������',
                      FileTransactions VARCHAR2(4000) PATH '@��������������',
                      FIO VARCHAR2(4000) PATH '@���',
                      ChecksIncoming VARCHAR2(4000) PATH '@������������',
                      Barcode VARCHAR2(4000) PATH '@��������',
                      IsAResident VARCHAR2(4000) PATH '@������������������',
                      ValueNotFound VARCHAR2(4000) PATH '@�����������������',
                      ProviderTariff VARCHAR2(4000) PATH '@���������������',
                      CounterChecks VARCHAR2(4000) PATH '@�������������',
                      CounterCheck VARCHAR2(4000) PATH '@������������',
                      Id_ VARCHAR2(4000) PATH '@Id',
                      Detailing VARCHAR2(4000) PATH '@�����������',
                      WalletPayer VARCHAR2(4000) PATH '@�����������������',
                      WalletReceiver VARCHAR2(4000) PATH
                      '@�����������������',
                      PurposeOfPayment VARCHAR2(4000) PATH
                      '@�����������������',
                      DataProvider VARCHAR2(4000) PATH '@��������������',
                      Attributes_ xmltype PATH '��������',
                      ChecksIncoming_ xmltype PATH '@������������') g
       where t.SESS_ID = idfile;
  
    CURSOR file_find(chk_ varchar2) IS
      SELECT count(*) cnt
        FROM Z_SB_FN_SESS_AMRA t,
             XMLTABLE('/����������/���' PASSING xmltype(FILECLOB) COLUMNS
                      Currency VARCHAR2(4000) PATH '@������',
                      PaymentType VARCHAR2(4000) PATH '@����������',
                      VK VARCHAR2(4000) PATH '@��',
                      DateOfOperation VARCHAR2(4000) PATH '@������������',
                      DataPs VARCHAR2(4000) PATH '@������',
                      DateClearing VARCHAR2(4000) PATH '@������������',
                      Dealer VARCHAR2(4000) PATH '@�����',
                      AccountPayer VARCHAR2(4000) PATH '@�������������',
                      CardNumber VARCHAR2(4000) PATH '@����������',
                      OperationNumber VARCHAR2(4000) PATH '@�������������',
                      OperationNumberDelivery VARCHAR2(4000) PATH
                      '@������������������',
                      CheckNumber VARCHAR2(4000) PATH '@���������',
                      CheckParent VARCHAR2(4000) PATH '@�����������',
                      OrderOfProvidence VARCHAR2(4000) PATH
                      '@����������������',
                      Provider VARCHAR2(4000) PATH '@���������',
                      OwnInOwn VARCHAR2(4000) PATH '@����������',
                      Corrected VARCHAR2(4000) PATH '@���������������',
                      CommissionRate VARCHAR2(4000) PATH '@��������������',
                      Status VARCHAR2(4000) PATH '@������',
                      StringFromFile VARCHAR2(4000) PATH '@�������������',
                      RewardAmount VARCHAR2(4000) PATH
                      '@�������������������',
                      OwnerIncomeAmount VARCHAR2(4000) PATH
                      '@�������������������',
                      CommissionAmount VARCHAR2(4000) PATH '@�������������',
                      NKAmount VARCHAR2(4000) PATH '@�������',
                      MaxCommissionAmount VARCHAR2(4000) PATH
                      '@�����������������',
                      MinCommissionAmount VARCHAR2(4000) PATH
                      '@����������������',
                      CashAmount VARCHAR2(4000) PATH '@�������������',
                      SumNalPrimal VARCHAR2(4000) PATH
                      '@�������������������',
                      AmountToCheck VARCHAR2(4000) PATH '@����������',
                      AmountOfPayment VARCHAR2(4000) PATH '@������������',
                      SumOfSplitting VARCHAR2(4000) PATH
                      '@������������������',
                      AmountIntermediary VARCHAR2(4000) PATH
                      '@���������������',
                      AmountOfSCS VARCHAR2(4000) PATH '@��������',
                      AmountWithChecks VARCHAR2(4000) PATH '@�����������',
                      Counter VARCHAR2(4000) PATH '@�������',
                      Terminal VARCHAR2(4000) PATH '@��������',
                      TerminalNetwork VARCHAR2(4000) PATH
                      '@����������������',
                      TransactionType VARCHAR2(4000) PATH '@�������������',
                      Service VARCHAR2(4000) PATH '@������',
                      FileTransactions VARCHAR2(4000) PATH '@��������������',
                      FIO VARCHAR2(4000) PATH '@���',
                      ChecksIncoming VARCHAR2(4000) PATH '@������������',
                      Barcode VARCHAR2(4000) PATH '@��������',
                      IsAResident VARCHAR2(4000) PATH '@������������������',
                      ValueNotFound VARCHAR2(4000) PATH '@�����������������',
                      ProviderTariff VARCHAR2(4000) PATH '@���������������',
                      CounterChecks VARCHAR2(4000) PATH '@�������������',
                      CounterCheck VARCHAR2(4000) PATH '@������������',
                      Id_ VARCHAR2(4000) PATH '@Id',
                      Detailing VARCHAR2(4000) PATH '@�����������',
                      WalletPayer VARCHAR2(4000) PATH '@�����������������',
                      WalletReceiver VARCHAR2(4000) PATH
                      '@�����������������',
                      PurposeOfPayment VARCHAR2(4000) PATH
                      '@�����������������',
                      DataProvider VARCHAR2(4000) PATH '@��������������',
                      Attributes_ xmltype PATH '��������') g
       where CheckNumber = chk_
         and t.SESS_ID = idfile;
  
    serch_terminal_1       varchar2(500);
    ChecksIncoming_varchar varchar2(32767);
    file_status            number := null;
    CHECKPARENT_           number;
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
    
      select t.STATUS
        into file_status
        from xxi.z_sb_fn_sess_amra t
       where t.SESS_ID = idfile;
    
      /*�������� ������� �����*/
      if file_status = 0 then
        /*�������� ����� ������ ���������� ���� ������. �������� �� ������� � ����������������� �����*/
        for r in xml_razbor loop
          ChecksIncoming_varchar := dbms_lob.substr(r.ChecksIncoming_.getClobVal(),
                                                    32767,
                                                    1);
          if length(ChecksIncoming_varchar) > 4000 then
            writelog(r.DATEOFOPERATION,
                     '��������! ���������� CheckNumber = ' || r.CheckNumber ||
                     ' ���������� ����� varchar2 > 4000 ��������. ������� ����� CLOB!!!',
                     idfile);
          end if;
          /*<-->CHECK_STATUS_18<-->*/
          if r.Status = '18' and r.CHECKPARENT is not null then
            writelog(r.DATEOFOPERATION,
                     '��������! ���������� CheckNumber = ' || r.CheckNumber ||
                     ' ���������� ������ ������� ������������� � ������������ ��������!!!',
                     idfile);
          end if;
          /*Without money*/
          if r.CASHAMOUNT is null and r.CHECKSINCOMING is null then
            writelog(r.DATEOFOPERATION,
                     '��������! ���������� CheckNumber = ' || r.CheckNumber ||
                     ' �������� �������������, ����� �������� � ����� �� ����� 0!!!',
                     idfile);
          end if;
        
          begin
            /*�������� ��������*/
            if substr(r.NKAMOUNT, 1, 1) = '-' then
              if (to_number(replace(replace(substr(r.NKAMOUNT,
                                                   instr(r.NKAMOUNT, '-') + 1),
                                            '�',
                                            ''),
                                    '.',
                                    ',')) >
                 replace(replace(r.COMMISSIONAMOUNT, '�', ''), '.', ',')) and
                 r.status != '18' then
                writelog(r.DATEOFOPERATION,
                         '��������! ���������� CheckNumber = ' ||
                         r.CheckNumber || ' ������� > �������������! ' ||
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
        
          /*�������� �������*/
          if r.transactiontype = '��������������' then
          
            /*���� � xml*/
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
              writelog(descripion => '����������� ������������ ���������� =' ||
                                     r.CHECKPARENT || ' � ' || '~TR~' ||
                                     r.CheckNumber,
                       sess_id1   => idfile);
            end if;
          end if;
        end loop;
      
        /*�������� ���������*/
        for r in xml_razbor loop
          select count(*)
            into serch_terminal_1
            from Z_SB_TERMINAL_AMRA_DBT t
           where t.name = r.terminal;
          if serch_terminal_1 > 0 then
            null;
          elsif serch_terminal_1 = 0 and r.dealer = '��������' then
            writelog(r.DATEOFOPERATION,
                     '��������! ���������� CheckNumber = ' || r.CheckNumber ||
                     ' ���������� ��������� ' || r.terminal ||
                     ' ������� �� ��������!',
                     idfile);
          end if;
          /*�������� ������ ����������� ����********************/
          if r.provider = '��������' and r.service = '���.������(��������)' then
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
                       '��������! ���������� CheckNumber = ' ||
                       r.CheckNumber || ' ���������� ������ ' || r.service ||
                       ' ������� �� ���������!',
                       idfile);
            end if;
          elsif r.provider = '��������' and r.service = '���.��� ��������' then
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
                       '��������! ���������� CheckNumber = ' ||
                       r.CheckNumber || ' ���������� ������ ' || r.service ||
                       ' ������� �� ���������!',
                       idfile);
            end if;
          elsif r.provider = '��������' and
                r.service not in
                ('���.������(��������)',
                 '���.��� ��������',
                 '������� ��(����)',
                 '������ � ����� (v3)(SB)') then
            select count(*)
              into serch_service
              from z_sb_termserv_amra_dbt t
             where t.idterm = r.terminal
               and t.name = r.service;
          
            if serch_service > 0 then
              null;
            elsif serch_service = 0 then
              writelog(r.DATEOFOPERATION,
                       '��������! ���������� CheckNumber = ' ||
                       r.CheckNumber || ' ���������� ������ ' || r.service ||
                       ' ������� �� ���������!',
                       idfile);
            end if;
          end if;
        
          /*�������� ������ ������ � ����� (v3)(SB)***********************************/
          if r.provider = '��������' and
             r.service = '������ � ����� (v3)(SB)' then
            for r2 in (select (SELECT AttributeValue
                                 FROM XMLTABLE('/��������/���' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@������',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@���������',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@�����������',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@����������������')
                                where ATTRIBUTENAME = 'taxName') tax_name,
                              (SELECT AttributeValue
                                 FROM XMLTABLE('/��������/���' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@������',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@���������',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@�����������',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@����������������')
                                where ATTRIBUTENAME = 'KBK') tax_kbk,
                              (SELECT AttributeValue
                                 FROM XMLTABLE('/��������/���' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@������',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@���������',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@�����������',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@����������������')
                                where ATTRIBUTENAME = 'INNreceiver') tax_inn,
                              (SELECT AttributeValue
                                 FROM XMLTABLE('/��������/���' PASSING
                                               r.Attributes_ COLUMNS Service
                                               VARCHAR2(4000) PATH '@������',
                                               CheckNumber VARCHAR2(4000) PATH
                                               '@���������',
                                               AttributeName VARCHAR2(4000) PATH
                                               '@�����������',
                                               AttributeValue VARCHAR2(4000) PATH
                                               '@����������������')
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
                         '��������! ���������� CheckNumber = ' ||
                         r.CheckNumber || ' ���������� ������ "' ||
                         r2.tax_name || '" ������� �� ���������!',
                         idfile);
              end if;
            end loop;
          end if;
        
          /*�������� ������ ������� ��(����)************************************/
          if r.provider = '��������' and r.service = '������� ��(����)' then
          
            /*�������� �����, ���� ������ ����� �������*/
            SELECT sum(to_number(replace(replace(ATTRIBUTEVALUE, '�', ''),
                                         '.',
                                         ',')))
              into sum_withattr
              FROM XMLTABLE('/��������/���' PASSING r.Attributes_ COLUMNS
                            Service VARCHAR2(4000) PATH '@������',
                            CheckNumber VARCHAR2(4000) PATH '@���������',
                            AttributeName VARCHAR2(4000) PATH '@�����������',
                            AttributeValue VARCHAR2(4000) PATH
                            '@����������������')
             where lower(ATTRIBUTENAME) = lower('�����');
          
            if ((sum_withattr - mod(sum_withattr, 1)) <>
               to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                  '.',
                                  ',')) -
               mod(to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                      '.',
                                      ',')),
                    1)) then
              writelog(r.DATEOFOPERATION,
                       '��������! ���������� CheckNumber = ' ||
                       r.CheckNumber ||
                       ' ����� �������� �� ��������� ��������� ����� �������!!!! sum_withattr=' ||
                       (sum_withattr - mod(sum_withattr, 1)) ||
                       '<> AmountOfPayment=' ||
                       to_number(replace(replace(r.AmountOfPayment, '�', ''),
                                         '.',
                                         ',')),
                       idfile);
            end if;
          
            /*����������� ��������� ����������� �������***********/
            SELECT count(*)
              into custom_atr_cnt
              FROM XMLTABLE('/��������/���' PASSING r.Attributes_ COLUMNS
                            Service VARCHAR2(1000) PATH '@������',
                            CheckNumber VARCHAR2(1000) PATH '@���������',
                            AttributeName VARCHAR2(1000) PATH '@�����������',
                            AttributeValue VARCHAR2(1000) PATH
                            '@����������������')
             where lower(ATTRIBUTENAME) in
                   ('�����', '���', '�������������');
          
            if custom_atr_cnt = 0 and r.status = '00' then
              writelog(r.DATEOFOPERATION,
                       '��������! ���������� CheckNumber = ' ||
                       r.CheckNumber ||
                       ' �� �������� ����������� ������ ���������!',
                       idfile);
            end if;
            /*-------------�������� ������� �����*/
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
                         '��������! ���������� CheckNumber = ' ||
                         r.CheckNumber || ' ���������� ������ "' ||
                         r2.names || '" ������� �� ���������!',
                         idfile);
              end if;
            end loop;
          end if;
        
          /*�������� ����*/
          if r.CHECKSINCOMING is not null and r.DEALER = '��������' then
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
              elsif bnk_deal = 0 and
                    substr(term_deal, 1, 2) in ('��', 'IV') then
                /*���� ���������� �����*/
                select count(*)
                  into deal_dubl_num
                  from Z_SB_TERMDEAL_AMRA_FEB t
                 where t.PAYMENTNUMBER = pnmb;
                /*���� ��� ����������*/
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
                elsif deal_dubl_num <> 0 then
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
      
        /*<<if_err>>*/
        SELECT count(lg.sess_id)
          into iferr
          FROM z_sb_log_amra lg
         WHERE lg.sess_id = idfile;
      
        /*���� ������ �� ����*/
        if iferr = 0 then
          /*��������, ���� ��� ������*/
          for r in xml_razbor loop
          
            /*������ �������������� ���������*/
            /*for row2 in (SELECT Service,
                                CheckNumber,
                                AttributeName,
                                AttributeValue
                           FROM XMLTABLE('/��������/���' PASSING r.Attributes_
                                         COLUMNS Service VARCHAR2(1000) PATH
                                         '@������',
                                         CheckNumber VARCHAR2(1000) PATH
                                         '@���������',
                                         AttributeName VARCHAR2(1000) PATH
                                         '@�����������',
                                         AttributeValue VARCHAR2(1000) PATH
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
          
            select count(*)
              into dubl_ret_cnt
              from Z_SB_TRANSACT_AMRA_RET t
             where t.checknumber = r.CheckNumber;
          
            /*���� �������*/
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
                 idfile,
                 r.ChecksIncoming);
            elsif dubl_ret_cnt > 0 and dubl_number = 0 then
              /*���� ����� ���������� ��� ���������*/
              select t.RECDATE
                into recdate_
                from z_sb_transact_amra_ret t
               where t.checknumber = r.CheckNumber;
              writelog(r.DATEOFOPERATION,
                       '��������! ���������� ��������!! CheckNumber = ' ||
                       r.CheckNumber || ' ��� ��������� � ��� ' || recdate_ ||
                       ' � �������� ��� �������.',
                       idfile);
              /*GOTO if_err;*/
            end if;
          
            if dubl_number = 0 and r.TRANSACTIONTYPE <> '����������' and
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
                 idfile,
                 r.ChecksIncoming);
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
                   to_number(replace(replace(r.CashAmount, '�', ''),
                                     '.',
                                     ',')),
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
                /*���� ����� ���������� (����������) ��� ���������*/
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
            elsif dubl_number <> 0 and r.TRANSACTIONTYPE <> '����������' and
                  r.status <> '25' then
              /*���� ����� ���������� ��� ���������*/
              select t.RECDATE
                into recdate_
                from z_sb_transact_amra_dbt t
               where t.checknumber = r.CheckNumber;
              writelog(r.DATEOFOPERATION,
                       '��������! ���������� CheckNumber = ' ||
                       r.CheckNumber || ' ��� ��������� � ��� ' || recdate_ ||
                       ' � �������� ��� �������.',
                       idfile);
            end if;
            /*�����*/
            if to_number(replace(replace(r.AMOUNTTOCHECK, '�', ''),
                                 '.',
                                 ',')) <> 0 then
              if deal_dubl_number = 0 /*and r.Status = '00'*/
                 and r.dealer = '��������' then
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
                         '��������! ��� CheckNumber = ' || r.CheckNumber ||
                         ' ��� ��������� � ��� ' || recdate_ ||
                         ' � �������� ��� �������.',
                         idfile);
              end if;
            end if;
          end loop;
        end if;
      else
        writelog(trunc(sysdate),
                 '��������! ���� � ID = ' || idfile || ' ����� ������ = ' ||
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

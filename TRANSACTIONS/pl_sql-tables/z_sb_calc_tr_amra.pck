create or replace package z_sb_calc_tr_amra is

  -- Author  : Pachuliya_S_V
  -- Created : 02.07.2019 10:15:36
  -- Purpose : Расчет транзакции Амра Банка
  FUNCTION make(id_sess NUMBER /*, input_number varchar2, input_date date*/)
    RETURN CLOB;
  FUNCTION getclob3(sessid NUMBER) RETURN CLOB;
  FUNCTION getclob_(sessid NUMBER) RETURN CLOB;
  procedure insert_post(id_sess_        NUMBER,
                        department_     VARCHAR2 default 0,
                        real_payer_     VARCHAR2,
                        real_receiver_  VARCHAR2,
                        payer_          VARCHAR2,
                        receiver_       VARCHAR2,
                        okpo_receiver   VARCHAR2,
                        sum_            number,
                        ground_         VARCHAR2,
                        payment_number  VARCHAR2 DEFAULT NULL,
                        coracc_payer    VARCHAR2 DEFAULT NULL,
                        mfo_receiver    VARCHAR2 DEFAULT NULL,
                        bank_receiver   VARCHAR2 DEFAULT NULL,
                        kpp_receiver    VARCHAR2 DEFAULT NULL,
                        composerstatus  VARCHAR2 DEFAULT NULL,
                        budjclassifcode VARCHAR2 DEFAULT NULL,
                        okato           VARCHAR2 DEFAULT NULL,
                        taxground       VARCHAR2 DEFAULT NULL,
                        taxperiod       VARCHAR2 DEFAULT NULL,
                        taxnumber       VARCHAR2 DEFAULT NULL,
                        taxdate         VARCHAR2 DEFAULT NULL,
                        taxpaymenttype  VARCHAR2 DEFAULT NULL,
                        i1_status       VARCHAR2 DEFAULT NULL,
                        bo1_            VARCHAR2 DEFAULT '1',
                        bo2_            VARCHAR2 DEFAULT NULL,
                        okpo_payer      VARCHAR2 DEFAULT NULL,
                        numb            number,
                        PAYDATE_        date,
                        chk_            VARCHAR2 DEFAULT NULL,
                        for_nbra        boolean DEFAULT false);
end z_sb_calc_tr_amra;
/
create or replace package body z_sb_calc_tr_amra is

  /*Процедура записи лога*/
  PROCEDURE writelog(datetimepayment_ date default sysdate,
                     descripion       VARCHAR2,
                     sess_id1         NUMBER,
                     DC               VARCHAR2 default NULL) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    INSERT INTO Z_SB_LOG_AMRA
      (recdate, paydate, desc_, sess_id, DEB_CRED)
    VALUES
      (SYSTIMESTAMP, datetimepayment_, descripion, sess_id1, DC);
    COMMIT;
  END;
  /*Процедура записи лога*/

  PROCEDURE write_deal(sump_    number,
                       paydate_ date,
                       service_ VARCHAR2,
                       vector_  number,
                       chk      varchar2) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    insert into Z_SB_CALC_DEAL_
      (summ, date_, service_, vector, CHECKNUMBER)
    values
      (sump_, paydate_, service_, vector_, chk);
    COMMIT;
  END;

  FUNCTION getclob(sessid NUMBER) RETURN CLOB IS
    ret CLOB;
    xsl CLOB;
    q   DBMS_XMLGEN.ctxhandle;
  BEGIN
    DBMS_LOB.createtemporary(ret, TRUE);
    q   := DBMS_XMLGEN.newcontext(q'~
    select to_char(trunc(t.date_value),'dd.mm.yyyy') date_reg,
       t.real_payer,
       t.real_receiver,
       t.sum,
       to_char(trunc(t.date_value),'dd.mm.yyyy') date_value,
       to_char(trunc(t.date_document),'dd.mm.yyyy') date_document,
       t.payer,
       t.receiver,
       t.ground,
       t.okpo_receiver,
       to_char(trunc(t.pay_date),'dd.mm.yyyy') pay_date,
       t.userfield1 bo1,
       t.userfield2 bo2,
       CORACC_PAYER,
       MFO_RECEIVER,
       BANK_RECEIVER,
       KPP_RECEIVER,
       COMPOSERSTATUS,
       BUDJCLASSIFCODE,
       OKATO,
       TAXGROUND,
       TAXPERIOD,
       TAXNUMBER,
       TAXDATE,
       TAXPAYMENTTYPE,
       I1_STATUS,
       trid,
       sess_id from Z_SB_POSTDOC_AMRA_DBT t 
       where sess_id=~' || sessid ||
                                  'order by NUMB_PAYDOC asc');
    xsl := q'~<?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
    <xsl:for-each select="ROWSET/ROW">
    # Doc Begin<br/>
    Address 0<br/>
    BO1 <xsl:value-of select="BO1"/><br/>
    BO2 <xsl:value-of select="BO2"/><br/>
    Date_Reg  <xsl:value-of select="DATE_REG"/><br/>
    Date_Doc  <xsl:value-of select="DATE_DOCUMENT"/><br/>
    Date_Val  <xsl:value-of select="DATE_VALUE"/><br/>
    Date_Trn  <xsl:value-of select="PAY_DATE"/><br/>
    Doc_Num <xsl:value-of select="NUMB_PAYDOC"/><br/> 
    Batch_Num 1500<br/>
    Priority  6<br/>
    Purpose <xsl:value-of select="GROUND"/><br/>
    Summa <xsl:value-of select="SUM"/><br/>
    Currency  RUR<br/>
    DWay_Type E<br/>
    VO  01<br/>
    Cus_SB  17<br/>
    Payer_Acc <xsl:value-of select="REAL_PAYER"/><br/>
    Recipient_Acc <xsl:value-of select="REAL_RECEIVER"/><br/>
    Deb_Acc    <xsl:value-of select="REAL_PAYER"/><br/>
    Deb_Cur    RUR<br/>
    Cre_Acc    <xsl:value-of select="REAL_RECEIVER"/><br/>
    Cre_Cur    RUR<br/>
    Cre_Sum    <xsl:value-of select="SUM"/><br/>
    Deb_Sum    <xsl:value-of select="SUM"/><br/>
    Client_Name <xsl:value-of select="PAYER"/><br/>
    Client_INN  11000572<br/>
    KPP 111000171<br/>
    Client_CorAcc <xsl:value-of select="CORACC_PAYER"/><br/>
    Corr_RBIC <xsl:value-of select="MFO_RECEIVER"/><br/>
    Corr_Bank_Name  <xsl:value-of select="BANK_RECEIVER"/><br/>
    Corr_Name <xsl:value-of select="RECEIVER"/><br/>
    Corr_INN  <xsl:value-of select="OKPO_RECEIVER"/><br/>
    KPPpol  <xsl:value-of select="KPP_RECEIVER"/><br/>
    User  AMRA_IMPORT<br/>
    StatusSostavit  <xsl:value-of select="COMPOSERSTATUS"/><br/>
    KBK_F <xsl:value-of select="BUDJCLASSIFCODE"/><br/>
    OKATO <xsl:value-of select="OKATO"/><br/>
    PokDateDoc  <xsl:value-of select="TAXDATE"/><br/>
    PokNalPeriod  <xsl:value-of select="TAXPERIOD"/><br/>
    PokNumDoc <xsl:value-of select="TAXNUMBER"/><br/>
    PokOsnPlat  <xsl:value-of select="TAXGROUND"/><br/>
    PokTypePlat <xsl:value-of select="TAXPAYMENTTYPE"/><br/>
    Doc_Index <xsl:value-of select="I1_STATUS"/><br/>
    Cashier AMRA_IMPORT<br/>
    TRID_ <xsl:value-of select="TRID"/><br/>
    SESS_ID_ <xsl:value-of select="SESS_ID"/><br/>
    # Doc End<br/>
    </xsl:for-each>
    </xsl:template>
    </xsl:stylesheet>~';
  
    DBMS_XMLGEN.setxslt(q, xsl);
    DBMS_XMLGEN.getxml(q, ret);
    RETURN ret;
  END;

  FUNCTION getclob_(sessid NUMBER) RETURN CLOB IS
    ret CLOB;
    xsl CLOB;
    q   DBMS_XMLGEN.ctxhandle;
  BEGIN
    DBMS_LOB.createtemporary(ret, TRUE);
    q := DBMS_XMLGEN.newcontext(q'~ 
    select PAYDATE, DESC_, DEB_CRED
  from Z_SB_LOG_AMRA t
 where sess_id = ~' || sessid || q'~
union all
select DTRNCREATE PAYDATE,
       'DOC_NUM:' || ITRNDOCNUM || '|' || DTRNCREATE || '|' || CTRNACCD || '->' ||
       CTRNACCC || '|' || MTRNRSUM DESC_,
       ' ' DEB_CRED
  from trn
 where CTRNIDOPEN = (select upper(user) from dual)
   AND (DTRNCREATE, CTRNACCD, CTRNACCC, MTRNRSUM, CTRNPURP) in
       (select DATE_VALUE, ACCOUNT_PAYER, ACCOUNT_RECEIVER, SUM, GROUND
          from z_sb_postdoc_amra_dbt
         where sess_id =
         ~' || sessid || ')');
  
    xsl := q'~<?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
    <xsl:for-each select="ROWSET/ROW">
Дата_Платежа -> <xsl:value-of select="PAYDATE"/>
Дебет_Кредит -> <xsl:value-of select="DEB_CRED"/>
Описание_Ошибки -> <xsl:value-of select="DESC_"/>
    </xsl:for-each>
    </xsl:template>
    </xsl:stylesheet>~';
  
    DBMS_XMLGEN.setxslt(q, xsl);
    DBMS_XMLGEN.getxml(q, ret);
    RETURN ret;
  END;

  FUNCTION getclob3(sessid NUMBER) RETURN CLOB IS
    ret CLOB;
    xsl CLOB;
    q   DBMS_XMLGEN.ctxhandle;
  BEGIN
    DBMS_LOB.createtemporary(ret, TRUE);
    q := DBMS_XMLGEN.newcontext(q'~ 
    select ITRNDOCNUM || '|' || DTRNCREATE || '|' || CTRNACCD || '->' ||
       CTRNACCC || '|' || MTRNRSUM||chr(13) || chr(10)  ABC,
       DTRNCREATE,
       CTRNACCD,
       CTRNACCC,
       MTRNRSUM,
       CTRNPURP
  from trn
 where CTRNIDOPEN = (select upper(user) from dual)
   AND (DTRNCREATE, CTRNACCD, MTRNRSUM, CTRNPURP) in
       (select DATE_VALUE, ACCOUNT_PAYER, SUM, GROUND
          from z_sb_postdoc_amra_dbt
         where sess_id = ~' || sessid ||
                                ') order by ITRNDOCNUM');
  
    xsl := q'~<?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
    <xsl:for-each select="ROWSET/ROW">
DOC_NUM: <xsl:value-of select="ABC"/>
    </xsl:for-each>
    </xsl:template>
    </xsl:stylesheet>~';
  
    DBMS_XMLGEN.setxslt(q, xsl);
    DBMS_XMLGEN.getxml(q, ret);
    RETURN ret;
  END;

  procedure DOC_REG(real_payer1      varchar2,
                    real_receiver1   varchar2,
                    PAYDATE1         date,
                    numb1            number,
                    ground1          varchar2,
                    sum1             number,
                    id_sess1         number,
                    for_nbra_        boolean DEFAULT false,
                    mfo_receiver_    varchar2 DEFAULT null,
                    OKPO_PAYER_      varchar2 DEFAULT null,
                    CORACC_PAYER_    varchar2 DEFAULT null,
                    PAYER            varchar2 DEFAULT null,
                    BUDJCLASSIFCODE_ varchar2 DEFAULT null,
                    BANK_RECEIVER_   varchar2 DEFAULT null,
                    RECEIVER_        varchar2 DEFAULT null,
                    kpp_receiver_    varchar2 DEFAULT null,
                    OKPO_RECEIVER_   varchar2 DEFAULT null,
                    OKATO            varchar2 DEFAULT null) is
    PRAGMA AUTONOMOUS_TRANSACTION;
    err2 varchar2(2000);
    b    varchar2(60);
    tts  TS.T_DeptInfo;
  begin
    if for_nbra_ = true then
      idoc_reg.SetUpRegisterParams('2TRN');
      tts.cBudCode   := BUDJCLASSIFCODE_;
      tts.cOKATOCode := OKATO;
      b              := Idoc_Reg.Register(ErrorMsg      => err2, -- Сообщение об ошибке для возврата
                                          PayerAcc      => real_payer1, -- Счет дебета
                                          RecipientAcc  => real_receiver1, -- Счет получателя
                                          Summa         => sum1, -- Сумма, если документ нац.вал. то это сумма в рублях
                                          OpType        => 4, -- Тип операции
                                          RegDate       => PAYDATE1, -- Дата регистрации
                                          DocNum        => numb1, -- Номер документа
                                          BatNum        => 1500, -- Номер пачки
                                          bIgnoreRB     => true, -- Флаг: игнорировать возникновение красного сальдо
                                          Purpose       => ground1, -- Назначение платежа
                                          SubOpType     => 0,
                                          INNA          => OKPO_PAYER_,
                                          CorAccO       => CORACC_PAYER_, /*Client_RBIC   => MFO_RECEIVER_*/
                                          CorAccAName   => BANK_RECEIVER_,
                                          RecipientName => RECEIVER_,
                                          MFOa          => MFO_RECEIVER_,
                                          KPPA          => kpp_receiver_,
                                          Client_KPP    => '111000171',
                                          Client_INN    => OKPO_RECEIVER_,
                                          rDeptInfo     => tts /*,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                          
                                          
                                                                                                                                                                                                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
                                          
                                          
                                                                                                                                                                                                                                                           Client_Name => */
                                          
                                          /*CorAccA => CORACC_PAYER_*/); -- Тип операции 2-го порядка
      null;
    elsif for_nbra_ = false then
      idoc_reg.SetUpRegisterParams('2TRN');
    
      b := Idoc_Reg.Register(ErrorMsg     => err2, -- Сообщение об ошибке
                             PayerAcc     => real_payer1, -- Счет дебета
                             RecipientAcc => real_receiver1, -- Счет кредита
                             Summa        => sum1, -- Сумма дебета (в валюте дебета)
                             OpType       => 1, -- Тип операции 1-го порядка
                             RegDate      => PAYDATE1, -- Дата регистрации
                             DocNum       => numb1,
                             BatNum       => 1500, -- Пачка
                             bIgnoreRB    => true,
                             Purpose      => ground1);
      null;
    end if;
  
    if b <> 'Ok' then
      writelog(PAYDATE1,
               err2 || b,
               id_sess1,
               DC => 'Проводка ' || real_payer1 || '->' || real_receiver1);
      return;
    else
      commit;
      null;
    end if;
  end;

  procedure insert_post(id_sess_        NUMBER,
                        department_     VARCHAR2 default 0,
                        real_payer_     VARCHAR2,
                        real_receiver_  VARCHAR2,
                        payer_          VARCHAR2,
                        receiver_       VARCHAR2,
                        okpo_receiver   VARCHAR2,
                        sum_            number,
                        ground_         VARCHAR2,
                        payment_number  VARCHAR2 DEFAULT NULL,
                        coracc_payer    VARCHAR2 DEFAULT NULL,
                        mfo_receiver    VARCHAR2 DEFAULT NULL,
                        bank_receiver   VARCHAR2 DEFAULT NULL,
                        kpp_receiver    VARCHAR2 DEFAULT NULL,
                        composerstatus  VARCHAR2 DEFAULT NULL,
                        budjclassifcode VARCHAR2 DEFAULT NULL,
                        okato           VARCHAR2 DEFAULT NULL,
                        taxground       VARCHAR2 DEFAULT NULL,
                        taxperiod       VARCHAR2 DEFAULT NULL,
                        taxnumber       VARCHAR2 DEFAULT NULL,
                        taxdate         VARCHAR2 DEFAULT NULL,
                        taxpaymenttype  VARCHAR2 DEFAULT NULL,
                        i1_status       VARCHAR2 DEFAULT NULL,
                        bo1_            VARCHAR2 DEFAULT '1',
                        bo2_            VARCHAR2 DEFAULT NULL,
                        okpo_payer      VARCHAR2 DEFAULT NULL,
                        numb            number,
                        PAYDATE_        date,
                        chk_            VARCHAR2 DEFAULT NULL,
                        for_nbra        boolean DEFAULT false) is
    PRAGMA AUTONOMOUS_TRANSACTION;
    res number;
  begin
    SELECT count(lg.sess_id)
      into res
      FROM Z_SB_LOG_AMRA lg
     WHERE lg.sess_id = id_sess_;
  
    if res = 0 then
      INSERT INTO Z_SB_POSTDOC_AMRA_DBT
        (pay_date,
         date_document,
         date_value,
         department,
         account_payer,
         real_payer,
         payer,
         okpo_payer,
         account_receiver,
         real_receiver,
         receiver,
         okpo_receiver,
         iapplicationkind,
         number_pack,
         SUM,
         result_carry,
         shifr_oper,
         kind_oper,
         ground,
         symbol_cach,
         coracc_payer,
         mfo_receiver,
         bank_receiver,
         kpp_receiver,
         composerstatus,
         budjclassifcode,
         okato,
         taxground,
         taxperiod,
         taxnumber,
         taxdate,
         taxpaymenttype,
         i1_status,
         sess_id,
         userfield1,
         userfield2,
         NUMB_PAYDOC)
      VALUES
        (PAYDATE_,
         PAYDATE_,
         PAYDATE_,
         department_,
         real_payer_,
         real_payer_,
         payer_,
         okpo_payer,
         real_receiver_,
         real_receiver_,
         receiver_,
         okpo_receiver,
         2,
         1500,
         sum_,
         40,
         '09',
         1,
         ground_,
         '  0',
         coracc_payer,
         mfo_receiver,
         bank_receiver,
         kpp_receiver,
         composerstatus,
         budjclassifcode,
         okato,
         taxground,
         taxperiod,
         taxnumber,
         taxdate,
         taxpaymenttype,
         i1_status,
         id_sess_,
         bo1_,
         bo2_,
         numb);
      commit;
    end if;
  end;

  PROCEDURE POST(id_sess_        NUMBER,
                 department_     VARCHAR2 default 0,
                 real_payer_     VARCHAR2,
                 real_receiver_  VARCHAR2,
                 payer_          VARCHAR2,
                 receiver_       VARCHAR2,
                 okpo_receiver   VARCHAR2,
                 sum_            number,
                 ground_         VARCHAR2,
                 payment_number  VARCHAR2 DEFAULT NULL,
                 coracc_payer    VARCHAR2 DEFAULT NULL,
                 mfo_receiver    VARCHAR2 DEFAULT NULL,
                 bank_receiver   VARCHAR2 DEFAULT NULL,
                 kpp_receiver    VARCHAR2 DEFAULT NULL,
                 composerstatus  VARCHAR2 DEFAULT NULL,
                 budjclassifcode VARCHAR2 DEFAULT NULL,
                 okato           VARCHAR2 DEFAULT NULL,
                 taxground       VARCHAR2 DEFAULT NULL,
                 taxperiod       VARCHAR2 DEFAULT NULL,
                 taxnumber       VARCHAR2 DEFAULT NULL,
                 taxdate         VARCHAR2 DEFAULT NULL,
                 taxpaymenttype  VARCHAR2 DEFAULT NULL,
                 i1_status       VARCHAR2 DEFAULT NULL,
                 bo1_            VARCHAR2 DEFAULT '1',
                 bo2_            VARCHAR2 DEFAULT NULL,
                 okpo_payer      VARCHAR2 DEFAULT NULL,
                 numb            number,
                 PAYDATE_        date,
                 chk_            VARCHAR2 DEFAULT NULL,
                 for_nbra        boolean DEFAULT false) IS
    pnmb varchar2(50);
    res  number;
  BEGIN
    begin
      /*зарегать проводки*/
      SELECT count(lg.sess_id)
        into res
        FROM Z_SB_LOG_AMRA lg
       WHERE lg.sess_id = id_sess_;
      if res = 0 then
        DOC_REG(real_payer1      => real_payer_,
                real_receiver1   => real_receiver_,
                PAYDATE1         => PAYDATE_,
                numb1            => numb,
                ground1          => ground_,
                sum1             => sum_,
                id_sess1         => id_sess_,
                for_nbra_        => for_nbra,
                mfo_receiver_    => mfo_receiver,
                CORACC_PAYER_    => CORACC_PAYER,
                Payer            => Payer_,
                budjclassifcode_ => budjclassifcode,
                BANK_RECEIVER_   => BANK_RECEIVER,
                RECEIVER_        => receiver_,
                kpp_receiver_    => kpp_receiver,
                OKPO_RECEIVER_   => OKPO_RECEIVER,
                OKPO_PAYER_      => OKPO_PAYER,
                okato            => okato);
        --null;
        --commit;
      end if;
      /*зарегать проводки*/
    
      SELECT count(lg.sess_id)
        into res
        FROM Z_SB_LOG_AMRA lg
       WHERE lg.sess_id = id_sess_;
      if res = 0 then
      
        insert_post(id_sess_        => id_sess_,
                    department_     => department_,
                    real_payer_     => real_payer_,
                    real_receiver_  => real_receiver_,
                    payer_          => payer_,
                    receiver_       => receiver_,
                    okpo_receiver   => okpo_receiver,
                    sum_            => sum_,
                    ground_         => ground_,
                    payment_number  => payment_number,
                    coracc_payer    => coracc_payer,
                    mfo_receiver    => mfo_receiver,
                    bank_receiver   => bank_receiver,
                    kpp_receiver    => kpp_receiver,
                    composerstatus  => composerstatus,
                    budjclassifcode => budjclassifcode,
                    okato           => okato,
                    taxground       => taxground,
                    taxperiod       => taxperiod,
                    taxnumber       => taxnumber,
                    taxdate         => taxdate,
                    taxpaymenttype  => taxpaymenttype,
                    i1_status       => i1_status,
                    bo1_            => bo1_,
                    bo2_            => bo2_,
                    okpo_payer      => okpo_payer,
                    numb            => numb,
                    PAYDATE_        => PAYDATE_,
                    chk_            => chk_,
                    for_nbra        => for_nbra);
      
        if payment_number is not null then
          update Z_SB_TRANSACT_AMRA_DBT t
             set t.STATUSABS = 0
           where t.SESS_ID = id_sess_
             and t.CHECKNUMBER = payment_number;
        end if;
      
        if chk_ is not null then
          for r_ in (Select Regexp_Substr(chk_, '[^|]+', 1, Level) chk
                       From dual
                     Connect By Regexp_Substr(chk_, '[^|]+', 1, Level) Is Not Null) loop
          
            /*номер чека со сдачей*/
            Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
              into pnmb
              From dual
             where level = 1
            Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            /*номер чека со сдачей*/
          
            update z_sb_termdeal_amra_dbt t
               set t.DEALENDDATE       = PAYDATE_,
                   t.DEALPAYMENTNUMBER = payment_number,
                   t.STATUS            = 1
             where t.paymentnumber = pnmb;
          end loop;
        end if;
      end if;
    exception
      when others then
        rollback;
        writelog(descripion => SQLERRM || ' ' ||
                               DBMS_UTILITY.format_error_stack || ' ' ||
                               dbms_utility.format_error_backtrace,
                 sess_id1   => id_sess_);
    end;
  end POST;

  FUNCTION make(id_sess NUMBER /*, input_number varchar2, input_date date*/)
    RETURN CLOB IS
    ret               CLOB;
    num               number := 0;
    res               number;
    pnmb              varchar2(50);
    sump              varchar2(50);
    chknmb            varchar2(50);
    chkdate           varchar2(50);
    bnk_deal          number;
    search_termdeal   number;
    bnk_deal_for_chek number;
    comis_rate        number;
  
    cursor loop_474 is
      select sum(summ) summ, TRUNC(PAYDATE) date_ --, checknumber
        from (select SERVICE,
                     trunc(PAYDATE) PAYDATE,
                     NKAMOUNT,
                     t.SESS_ID,
                     CASHAMOUNT,
                     checknumber,
                     decode(PROVIDER,
                            'СберБанк',
                            decode(substr(NKAMOUNT, 1, 1),
                                   '-',
                                   
                                   to_number(replace(replace(replace(NKAMOUNT,
                                                                     '-',
                                                                     ''),
                                                             '.',
                                                             ','),
                                                     ' ',
                                                     '')),
                                   
                                   to_number(replace(replace(NKAMOUNT,
                                                             '.',
                                                             ','),
                                                     ' ',
                                                     ''))),
                            decode(substr(NKAMOUNT, 1, 1),
                                   '-',
                                   AMOUNTOFPAYMENT +
                                   to_number(replace(replace(replace(NKAMOUNT,
                                                                     '-',
                                                                     ''),
                                                             '.',
                                                             ','),
                                                     ' ',
                                                     '')),
                                   AMOUNTOFPAYMENT -
                                   to_number(replace(replace(NKAMOUNT,
                                                             '.',
                                                             ','),
                                                     ' ',
                                                     '')))) summ
                from Z_SB_TRANSACT_AMRA_DBT t
               where t.SESS_ID = id_sess
                 and t.TRANSACTIONTYPE not in
                     ('Инкассация', 'ДокаткаПлатежа')
                 and STATUS = '00'
                    /*and PROVIDER <> 'СберБанк'*/
                    --and t.checknumber = input_number
                    --and trunc(PAYDATE) = input_date
                 and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT))
       group by TRUNC(PAYDATE);
  
    CHEK_302326_3023211 BOOLEAN := FALSE;
    cursor sbra_pr is
      select (select ACCOUNT from Z_SB_TERMINAL_DBT where NAME = t.TERMINAL) acc_20208,
             (select ACC_30232_06
                from Z_SB_TERMINAL_DBT
               where NAME = t.TERMINAL) ACC_30232_06,
             (select ACCOUNT
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) acc40911,
             (select INN
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) inn,
             (select KBK
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) kbk_payer,
             (select kpp
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) kpp,
             (select ACC_NAME
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) ACC_NAME,
             (select ACC_REC
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) ACC_REC,
             (select OKATO
                from z_Sb_Termserv_Dbt h
               where idterm = t.TERMINAL
                 and h.NAME = t.service) OKATO,
             (select ISMRINN from smr) bank_inn,
             (select CSMRKORACC from smr) bank_cor_acc,
             (select CSMRRCSNAME from smr) bank_cor_bic,
             (select DEPARTMENT
                from Z_SB_TERMINAL_DBT
               where NAME = t.TERMINAL) DEPARTMENT,
             'Услуга "' || SERVICE || '" - ' ||
             upper((SELECT AttributeValue
                     FROM (select ATTRIBUTES_, sess_id, CHECKNUMBER
                             from Z_SB_TRANSACT_AMRA_DBT) g,
                          XMLTABLE('/Атрибуты/Атр' PASSING
                                   xmltype(ATTRIBUTES_) COLUMNS Service
                                   VARCHAR2(500) PATH '@Услуга',
                                   CheckNumber VARCHAR2(500) PATH
                                   '@НомерЧека',
                                   AttributeName VARCHAR2(500) PATH
                                   '@ИмяАтрибута',
                                   AttributeValue VARCHAR2(500) PATH
                                   '@ЗначениеАтрибута')
                    where ATTRIBUTENAME = 'fio_children'
                      and g.CHECKNUMBER = t.checknumber
                      and g.sess_id = t.sess_id)) || ' За ' ||
             upper((SELECT AttributeValue
                     FROM (select ATTRIBUTES_, sess_id, CHECKNUMBER
                             from Z_SB_TRANSACT_AMRA_DBT) g,
                          XMLTABLE('/Атрибуты/Атр' PASSING
                                   xmltype(ATTRIBUTES_) COLUMNS Service
                                   VARCHAR2(500) PATH '@Услуга',
                                   CheckNumber VARCHAR2(500) PATH
                                   '@НомерЧека',
                                   AttributeName VARCHAR2(500) PATH
                                   '@ИмяАтрибута',
                                   AttributeValue VARCHAR2(500) PATH
                                   '@ЗначениеАтрибута')
                    where ATTRIBUTENAME = 'period'
                      and g.CHECKNUMBER = t.checknumber
                      and g.sess_id = t.sess_id)) || ' TR:' || CHECKNUMBER ||
             ';SID:' || t.SESS_ID ground,
             t.CHECKNUMBER,
             t.AMOUNTOFPAYMENT,
             t.CASHAMOUNT,
             t.TERMINAL idterm,
             t.COMMISSIONAMOUNT,
             t.NKAMOUNT,
             t.AMOUNTTOCHECK,
             t.SESS_ID,
             t.STATUS,
             SUMNALPRIMAL,
             AMOUNTWITHCHECKS,
             CHECKSINCOMING,
             trunc(PAYDATE) PAYDATE,
             service
        from Z_SB_TRANSACT_AMRA_DBT t
       where t.SESS_ID = id_sess
            --and trunc(PAYDATE) = input_date
         and t.STATUSABS = 0
         and t.TRANSACTIONTYPE not in
             ('Инкассация', 'ДокаткаПлатежа')
         and PROVIDER = 'СберБанк'
            --and t.checknumber = input_number
         and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT);
  
    cursor amra_pr is
      select (select ACCOUNT from Z_SB_TERMINAL_DBT where NAME = t.TERMINAL) acc_20208,
             (select ACC_30232_06
                from Z_SB_TERMINAL_DBT
               where NAME = t.TERMINAL) ACC_30232_06,
             (select DEPARTMENT
                from Z_SB_TERMINAL_DBT
               where NAME = t.TERMINAL) DEPARTMENT,
             'Платеж по услуге ' || SERVICE || ' за ' ||
             trunc(DATEOFOPERATION) || ' TR:' || CHECKNUMBER || ';SID:' ||
             t.SESS_ID ground,
             t.CHECKNUMBER,
             t.AMOUNTOFPAYMENT,
             t.CASHAMOUNT,
             t.TERMINAL idterm,
             t.COMMISSIONAMOUNT,
             t.NKAMOUNT,
             t.AMOUNTTOCHECK,
             t.SESS_ID,
             t.STATUS,
             t.service,
             SUMNALPRIMAL,
             AMOUNTWITHCHECKS,
             CHECKSINCOMING,
             trunc(PAYDATE) PAYDATE
        from Z_SB_TRANSACT_AMRA_DBT t
       where t.SESS_ID = id_sess
         and t.STATUSABS = 0
         and t.TRANSACTIONTYPE not in
             ('Инкассация', 'ДокаткаПлатежа')
         and PROVIDER <> 'СберБанк'
            --and t.checknumber = input_number
            --and trunc(PAYDATE) = input_date
         and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT);
  BEGIN
    begin
      DBMS_LOB.createtemporary(ret, TRUE);
      /*Наши Услуги*/
      for r in sbra_pr loop
        if r.STATUS = '00' then
          /*
          1 
          Если СуммаПлатежа не равна 0
          20208/ACC_30232_06
          */
          if r.AMOUNTOFPAYMENT <> 0 then
            /*Выбираем сумму комиссии по услуге и терминалу (наше направление)*/
            select COMISSION
              into comis_rate
              from z_sb_termserv_dbt t
             where t.IDTERM = r.idterm
               and t.NAME = r.service;
            /*Выбираем сумму комиссии по услуге и терминалу (наше направление)*/
            if r.CASHAMOUNT <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.acc_20208,
                   real_receiver_ => r.ACC_30232_06,
                   payer_         => 'Счет терминала ' || r.idterm ||
                                     ' отделения ' || r.department,
                   receiver_      => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.CASHAMOUNT --r.AMOUNTOFPAYMENT - comis_rate
                  ,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            /*
            Убираем Сдачу
            ACC_30232_06/30232810700000010009
            */
            if r.AMOUNTTOCHECK <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => '30232810700000010009',
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Счет сдачи ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTTOCHECK,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            
            end if;
            /*Убираем Сдачу*/
          
            /*4 Если СуммаКомиссии не равна 0*/
          
            /*Выбираем сумму комиссии по услуге и терминалу (наше направление)*/
            select COMISSION
              into comis_rate
              from z_sb_termserv_dbt t
             where t.IDTERM = r.idterm
               and t.NAME = r.service;
            /*Выбираем сумму комиссии по услуге и терминалу (наше направление)*/
          
            /*Если нашли комиссию*/
            if comis_rate <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => '30232810100000010010',
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Счет общей комиссии ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => comis_rate,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            else
              writelog(r.PAYDATE,
                       'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                       ' Сдача № ' || chknmb || ' направление ' || pnmb ||
                       ' сумма ' || sump || ' дата ' || chkdate ||
                       'BLOCK:3',
                       id_sess);
            end if;
          
            /*
            5 
            Если СуммаПлатежа не равно 0
            ACC_30232_06/acc40911
            */
            if r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS = 0 then
              select COMISSION
                into comis_rate
                from z_sb_termserv_dbt t
               where t.IDTERM = r.idterm
                 and t.NAME = r.service;
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => r.acc40911,
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Транзитный счет  ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTOFPAYMENT - comis_rate -
                                     to_number(replace(replace(r.AMOUNTTOCHECK,
                                                               '.',
                                                               ','),
                                                       ' ',
                                                       '')),
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            elsif r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => r.acc40911,
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Транзитный счет  ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTOFPAYMENT - comis_rate -
                                     -to_number(replace(replace(r.AMOUNTTOCHECK,
                                                                '.',
                                                                ','),
                                                        ' ',
                                                        '')),
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            /*
            6 
            acc40911/ACC_REC
            */
            if r.AMOUNTOFPAYMENT <> 0 then
              select COMISSION
                into comis_rate
                from z_sb_termserv_dbt t
               where t.IDTERM = r.idterm
                 and t.NAME = r.service;
              num := num + 1;
              POST(id_sess_        => r.SESS_ID,
                   department_     => r.department,
                   real_payer_     => r.acc40911,
                   real_receiver_  => '30102810900000000017' /*r.ACC_REC*/,
                   payer_          => 'Транзитный счет ' || r.idterm ||
                                      ' отделения ' || r.department,
                   receiver_       => r.acc_name,
                   okpo_receiver   => r.inn,
                   sum_            => r.AMOUNTOFPAYMENT - comis_rate,
                   ground_         => r.ground,
                   payment_number  => r.CHECKNUMBER,
                   kpp_receiver    => r.kpp,
                   numb            => num,
                   PAYDATE_        => r.PAYDATE,
                   bo1_            => '4',
                   bo2_            => '0',
                   okpo_payer      => r.bank_inn,
                   coracc_payer    => r.bank_cor_acc,
                   mfo_receiver    => r.bank_cor_bic,
                   bank_receiver   => 'Банк Абхазии',
                   BUDJCLASSIFCODE => r.kbk_payer,
                   okato           => r.OKATO,
                   for_nbra        => true);
            end if;
          
            /*
            Если СуммаНаЧек не равно 0    
            30232810700000010009/ACC_30232_06
            30110810100000000003/30232810700000010012
            */
            /*Оплата со сдачи
            Если сумма с чека не равна 0*/
            if r.AMOUNTWITHCHECKS <> 0 then
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
                    into pnmb
                    From dual
                   where level = 3
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*ID терминала на котором возникла сдача*/
                
                  /*сумма сдачи*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into sump
                    From dual
                   where level = 2
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*сумма сдачи*/
                
                  /*Номер Чека сдачи*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chknmb
                    From dual
                   where level = 1
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*Номер Чека сдачи*/
                
                  /*Дата Чека сдачи*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chkdate
                    From dual
                   where level = 4
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*Дата Чека сдачи*/
                
                  /*наш или нет терминал*/
                  select count(*)
                    into bnk_deal
                    from z_sb_terminal_dbt t
                   where t.name = pnmb;
                  /*наш или нет терминал*/
                
                  if bnk_deal > 0 then
                    /*Поиск сдачи*/
                    select count(*)
                      into search_termdeal
                      from z_sb_termdeal_amra_dbt t
                     where t.paymentnumber = chknmb;
                    /*Поиск сдачи*/
                    if search_termdeal > 0 then
                      num := num + 1;
                      POST(id_sess_       => r.SESS_ID,
                           department_    => r.department,
                           real_payer_    => '30232810700000010009',
                           real_receiver_ => r.ACC_30232_06,
                           payer_         => 'Счет общей суммы транзакции ' ||
                                             r.idterm || ' отделения ' ||
                                             r.department,
                           receiver_      => 'Счет сдачи ' || r.idterm ||
                                             ' отделения ' || r.department,
                           okpo_receiver  => '11000572',
                           sum_           => to_number(replace(replace(sump,
                                                                       '.',
                                                                       ','),
                                                               ' ',
                                                               '')),
                           ground_        => r.ground,
                           payment_number => r.CHECKNUMBER,
                           kpp_receiver   => '111000171',
                           numb           => num,
                           PAYDATE_       => r.PAYDATE);
                    elsif search_termdeal = 0 then
                      writelog(r.PAYDATE,
                               'Внимание! Чек CheckNumber = ' ||
                               r.CheckNumber || ' Сдача № ' || chknmb ||
                               ' направление ' || pnmb || ' сумма ' || sump ||
                               ' дата ' || chkdate || ' BLOCK:1',
                               id_sess);
                    end if;
                  elsif bnk_deal = 0 then
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => '30110810100000000003',
                         real_receiver_ => '30232810700000010012',
                         payer_         => 'Счет общей суммы транзакции ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         receiver_      => 'Счет оплаты со сдачи ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => to_number(replace(replace(sump,
                                                                     '.',
                                                                     ','),
                                                             ' ',
                                                             '')),
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE,
                         chk_           => r.CHECKSINCOMING);
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => '30232810700000010012',
                         real_receiver_ => '30232810400000010011',
                         payer_         => 'Счет общей суммы транзакции ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         receiver_      => 'Счет оплаты со сдачи ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => to_number(replace(replace(sump,
                                                                     '.',
                                                                     ','),
                                                             ' ',
                                                             '')),
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE,
                         chk_           => r.CHECKSINCOMING);
                  end if;
                end loop;
              end if;
            end if;
          end if;
        else
          /*
          2 
          
          Если аварийная сумма
          
          acc_20208/ACC_30232_06
          
          ACC_30232_06/30232810400000010008
          
          ACC_30232_06/30232810700000010009
          
          */
        
          /*Выбираем сумму комиссии по услуге и терминалу (наше направление)*/
          select COMISSION
            into comis_rate
            from z_sb_termserv_dbt t
           where t.IDTERM = r.idterm
             and t.NAME = r.service;
          /*Выбираем сумму комиссии по услуге и терминалу (наше направление)*/
          if r.CASHAMOUNT <> 0 then
            num := num + 1;
            POST(id_sess_       => r.SESS_ID,
                 department_    => r.department,
                 real_payer_    => r.acc_20208,
                 real_receiver_ => r.ACC_30232_06,
                 payer_         => 'Счет терминала ' || r.idterm ||
                                   ' отделения ' || r.department,
                 receiver_      => 'Счет аварийного платежа ' || r.idterm ||
                                   ' отделения ' || r.department,
                 okpo_receiver  => '11000572',
                 sum_           => r.CASHAMOUNT --r.AMOUNTOFPAYMENT - comis_rate
                ,
                 ground_        => r.ground,
                 payment_number => r.CHECKNUMBER,
                 kpp_receiver   => '111000171',
                 numb           => num,
                 PAYDATE_       => r.PAYDATE);
          end if;
          num := num + 1;
          POST(id_sess_       => r.SESS_ID,
               department_    => r.department,
               real_payer_    => r.ACC_30232_06,
               real_receiver_ => '30232810400000010008',
               payer_         => 'Счет общей суммы транзакции ' || r.idterm ||
                                 ' отделения ' || r.department,
               receiver_      => 'Счет аварийного платежа ' || r.idterm ||
                                 ' отделения ' || r.department,
               okpo_receiver  => '11000572',
               sum_           => r.AMOUNTOFPAYMENT - comis_rate,
               ground_        => r.ground,
               payment_number => r.CHECKNUMBER,
               kpp_receiver   => '111000171',
               numb           => num,
               PAYDATE_       => r.PAYDATE);
        
          if r.AMOUNTTOCHECK <> 0 then
            num := num + 1;
            POST(id_sess_       => r.SESS_ID,
                 department_    => r.department,
                 real_payer_    => r.ACC_30232_06,
                 real_receiver_ => '30232810700000010009',
                 payer_         => 'Счет общей суммы транзакции ' ||
                                   r.idterm || ' отделения ' || r.department,
                 receiver_      => 'Счет оплаты со сдачи' || r.idterm ||
                                   ' отделения ' || r.department,
                 okpo_receiver  => '11000572',
                 sum_           => r.AMOUNTWITHCHECKS,
                 ground_        => r.ground,
                 payment_number => r.CHECKNUMBER,
                 kpp_receiver   => '111000171',
                 numb           => num,
                 PAYDATE_       => r.PAYDATE);
          end if;
        end if;
      end loop;
    
      /* 
      Группировка по направлениям 
      
      7
      30232810100000010010/30110810100000000003
      */
      for r in (select sum(to_number(replace(replace(replace(NKAMOUNT,
                                                             '-',
                                                             ''),
                                                     '.',
                                                     ','),
                                             ' ',
                                             ''))) summ,
                       trunc(PAYDATE) PAYDATE,
                       --t.checknumber,
                       SERVICE
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and substr(NKAMOUNT, 1, 1) = '-'
                   and STATUS = '00'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and PROVIDER = 'СберБанк'
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                 group by SERVICE, trunc(PAYDATE)) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810100000010010',
             real_receiver_ => '30110810100000000003',
             payer_         => 'Общая сумма комиссии',
             receiver_      => 'Корсчет Амра',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Комиссия за услугу ' || r.SERVICE || ' за ' ||
                               r.PAYDATE || /* 'TR:' || r.checknumber ||*/
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      end loop;
    
      /* 
      Группировка по направлениям
      8
      30232810100000010010/70107810000001720109
      */
      for r in (select COMMISSIONAMOUNT - decode(substr(NKAMOUNT, 1, 1),
                                                 '-',
                                                 to_number(replace(replace(replace(NKAMOUNT,
                                                                                   '-',
                                                                                   ''),
                                                                           '.',
                                                                           ','),
                                                                   ' ',
                                                                   '')),
                                                 0) summ,
                       trunc(PAYDATE) PAYDATE,
                       t.checknumber,
                       SERVICE
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and COMMISSIONAMOUNT <> 0
                   and STATUS = '00'
                   and PROVIDER = 'СберБанк'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                /*group by SERVICE, trunc(PAYDATE)*/
                ) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810100000010010',
             real_receiver_ => '70107810000001720109',
             payer_         => 'Общая сумма комиссии',
             receiver_      => 'Счет комиссии ',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Комиссия за услугу ' || r.SERVICE || ' за ' ||
                               r.PAYDATE || 'TR:' || r.checknumber ||
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      end loop;
    
      /* 
      Группировка по направлениям если нет верхней комиссии
      8
      30232810100000010010/70107810000001720109
      */
      for r in (select (select COMISSION
                          from z_sb_termserv_dbt t
                         where t.IDTERM = terminal
                           and t.NAME = service) -
                       to_number(replace(replace(replace(decode(substr(NKAMOUNT,
                                                                       1,
                                                                       1),
                                                                '-',
                                                                NKAMOUNT,
                                                                0),
                                                         '-',
                                                         ''),
                                                 '.',
                                                 ','),
                                         ' ',
                                         '')) summ,
                       trunc(PAYDATE) PAYDATE,
                       t.checknumber,
                       SERVICE
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and STATUS = '00'
                   and PROVIDER = 'СберБанк'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                /*group by SERVICE, trunc(PAYDATE)*/
                ) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810100000010010',
             real_receiver_ => '70107810000001720109',
             payer_         => 'Общая сумма комиссии',
             receiver_      => 'Счет комиссии ',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Комиссия за услугу ' || r.SERVICE || ' за ' ||
                               r.PAYDATE || 'TR:' || r.checknumber ||
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      end loop;
      /*Наши Услуги*/
    
      /*
      /----------------------------------------/
      /----------------------------------------/
      */
    
      for r in amra_pr loop
        if r.STATUS = '00' then
          -- 1 Если СуммаНаличных не равно 0
          if r.AMOUNTOFPAYMENT <> 0 then
          
            if r.CASHAMOUNT <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.acc_20208,
                   real_receiver_ => r.ACC_30232_06,
                   payer_         => 'Счет терминала ' || r.idterm ||
                                     ' отделения ' || r.department,
                   receiver_      => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.CASHAMOUNT --r.amountofpayment
                  ,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            -- 3 Если СуммаНаЧек не равно 0      
            if r.AMOUNTTOCHECK <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => '30232810700000010009',
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Счет сдачи ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTTOCHECK,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            
            end if;
            --  4 Если СуммаКомиссии не равно 0
            if r.COMMISSIONAMOUNT <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => '30232810100000010010',
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Счет общей комиссии ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.COMMISSIONAMOUNT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
          
            if r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS = 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => '30232810400000010011',
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Счет чистого платежа ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTOFPAYMENT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
              --CHEK_302326_3023211 := TRUE;
            elsif r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.ACC_30232_06,
                   real_receiver_ => '30232810400000010011',
                   payer_         => 'Счет общей суммы транзакции ' ||
                                     r.idterm || ' отделения ' ||
                                     r.department,
                   receiver_      => 'Счет чистого платежа ' || r.idterm ||
                                     ' отделения ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTOFPAYMENT - r.COMMISSIONAMOUNT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
              CHEK_302326_3023211 := TRUE;
            end if;
            /*if r.AMOUNTWITHCHECKS <> 0 then
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
                
                  \*ID терминала на котором возникла сдача*\
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into pnmb
                    From dual
                   where level = 3
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  \*ID терминала на котором возникла сдача*\
                
                  \*сумма сдачи*\
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into sump
                    From dual
                   where level = 2
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  \*сумма сдачи*\
                
                  \*Номер Чека сдачи*\
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chknmb
                    From dual
                   where level = 1
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  \*Номер Чека сдачи*\
                
                  \*Дата Чека сдачи*\
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chkdate
                    From dual
                   where level = 4
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  \*Дата Чека сдачи*\
                
                  \*наш или нет терминал*\
                  select count(*)
                    into bnk_deal
                    from z_sb_terminal_dbt t
                   where t.name = pnmb;
                  \*наш или нет терминал*\
                
                  if bnk_deal > 0 then
                    \*Поиск сдачи*\
                    select count(*)
                      into search_termdeal
                      from z_sb_termdeal_amra_dbt t
                     where t.paymentnumber = chknmb;
                    \*Поиск сдачи*\
                    if search_termdeal > 0 AND CHEK_302326_3023211 != FALSE then
                      -- 5 Если СуммаПлатежа не равно 0
                      if r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS = 0 then
                        num := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => r.ACC_30232_06,
                             real_receiver_ => '30232810400000010011',
                             payer_         => 'Счет общей суммы транзакции ' ||
                                               r.idterm || ' отделения ' ||
                                               r.department,
                             receiver_      => 'Счет чистого платежа ' ||
                                               r.idterm || ' отделения ' ||
                                               r.department,
                             okpo_receiver  => '11000572',
                             sum_           => r.AMOUNTOFPAYMENT,
                             ground_        => r.ground,
                             payment_number => r.CHECKNUMBER,
                             kpp_receiver   => '111000171',
                             numb           => num,
                             PAYDATE_       => r.PAYDATE);
                        CHEK_302326_3023211 := TRUE;
                      elsif r.AMOUNTOFPAYMENT <> 0 and
                            r.AMOUNTWITHCHECKS <> 0 then
                        num := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => r.ACC_30232_06,
                             real_receiver_ => '30232810400000010011',
                             payer_         => 'Счет общей суммы транзакции ' ||
                                               r.idterm || ' отделения ' ||
                                               r.department,
                             receiver_      => 'Счет чистого платежа ' ||
                                               r.idterm || ' отделения ' ||
                                               r.department,
                             okpo_receiver  => '11000572',
                             sum_           => r.AMOUNTOFPAYMENT -
                                               r.COMMISSIONAMOUNT,
                             ground_        => r.ground,
                             payment_number => r.CHECKNUMBER,
                             kpp_receiver   => '111000171',
                             numb           => num,
                             PAYDATE_       => r.PAYDATE);
                        CHEK_302326_3023211 := TRUE;
                      end if;
                    elsif search_termdeal = 0 then
                      writelog(r.PAYDATE,
                               'Внимание! Чек CheckNumber = ' ||
                               r.CheckNumber || ' Сдача № ' || chknmb ||
                               ' направление ' || pnmb || ' сумма ' || sump ||
                               ' дата ' || chkdate || ' BLOCK:1',
                               id_sess);
                    end if;
                  elsif bnk_deal = 0 then
                    NULL;
                  end if;
                end loop;
              end if;
            end if;*/
          
            --Оплата со сдачи
            if r.AMOUNTWITHCHECKS <> 0 then
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
                    into pnmb
                    From dual
                   where level = 3
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*ID терминала на котором возникла сдача*/
                
                  /*сумма сдачи*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into sump
                    From dual
                   where level = 2
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*сумма сдачи*/
                
                  /*Номер Чека сдачи*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chknmb
                    From dual
                   where level = 1
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*Номер Чека сдачи*/
                
                  /*Дата Чека сдачи*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chkdate
                    From dual
                   where level = 4
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*Дата Чека сдачи*/
                
                  /*наш или нет терминал*/
                  select count(*)
                    into bnk_deal
                    from z_sb_terminal_dbt t
                   where t.name = pnmb;
                  /*наш или нет терминал*/
                
                  if bnk_deal > 0 then
                    /*Поиск сдачи*/
                    select count(*)
                      into search_termdeal
                      from z_sb_termdeal_amra_dbt t
                     where t.paymentnumber = chknmb;
                    /*Поиск сдачи*/
                    if search_termdeal > 0 then
                      null;
                    elsif search_termdeal = 0 then
                      writelog(r.PAYDATE,
                               'Внимание! Чек CheckNumber = ' ||
                               r.CheckNumber || ' Сдача № ' || chknmb ||
                               ' направление ' || pnmb || ' сумма ' || sump ||
                               ' дата ' || chkdate || ' BLOCK:2',
                               id_sess);
                    end if;
                  elsif bnk_deal = 0 then
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => '30110810100000000003',
                         real_receiver_ => '30232810700000010012',
                         payer_         => 'Счет общей суммы транзакции ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         receiver_      => 'Счет оплаты со сдачи ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => to_number(replace(replace(sump,
                                                                     '.',
                                                                     ','),
                                                             ' ',
                                                             '')),
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE,
                         chk_           => r.CHECKSINCOMING);
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => '30232810700000010012',
                         real_receiver_ => '30232810400000010011',
                         payer_         => 'Счет общей суммы транзакции ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         receiver_      => 'Счет оплаты со сдачи ' ||
                                           r.idterm || ' отделения ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => to_number(replace(replace(sump,
                                                                     '.',
                                                                     ','),
                                                             ' ',
                                                             '')),
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE,
                         chk_           => r.CHECKSINCOMING);
                  end if;
                end loop;
              end if;
            
            end if;
          end if;
        else
          if r.CASHAMOUNT <> 0 then
            num := num + 1;
            POST(id_sess_       => r.SESS_ID,
                 department_    => r.department,
                 real_payer_    => r.acc_20208,
                 real_receiver_ => r.ACC_30232_06,
                 payer_         => 'Счет терминала ' || r.idterm ||
                                   ' отделения ' || r.department,
                 receiver_      => 'Счет аварийного платежа ' || r.idterm ||
                                   ' отделения ' || r.department,
                 okpo_receiver  => '11000572',
                 sum_           => r.CASHAMOUNT --r.AMOUNTOFPAYMENT - comis_rate
                ,
                 ground_        => r.ground,
                 payment_number => r.CHECKNUMBER,
                 kpp_receiver   => '111000171',
                 numb           => num,
                 PAYDATE_       => r.PAYDATE);
          end if;
          num := num + 1;
          POST(id_sess_       => r.SESS_ID,
               department_    => r.department,
               real_payer_    => r.ACC_30232_06,
               real_receiver_ => '30232810400000010008',
               payer_         => 'Счет общей суммы транзакции ' || r.idterm ||
                                 ' отделения ' || r.department,
               receiver_      => 'Счет аварийного платежа ' || r.idterm ||
                                 ' отделения ' || r.department,
               okpo_receiver  => '11000572',
               sum_           => r.AMOUNTOFPAYMENT,
               ground_        => r.ground,
               payment_number => r.CHECKNUMBER,
               kpp_receiver   => '111000171',
               numb           => num,
               PAYDATE_       => r.PAYDATE);
        
          if r.AMOUNTWITHCHECKS <> 0 then
            num := num + 1;
            POST(id_sess_       => r.SESS_ID,
                 department_    => r.department,
                 real_payer_    => r.ACC_30232_06,
                 real_receiver_ => '30232810700000010009',
                 payer_         => 'Счет общей суммы транзакции ' ||
                                   r.idterm || ' отделения ' || r.department,
                 receiver_      => 'Счет оплаты со сдачи' || r.idterm ||
                                   ' отделения ' || r.department,
                 okpo_receiver  => '11000572',
                 sum_           => r.AMOUNTWITHCHECKS,
                 ground_        => r.ground,
                 payment_number => r.CHECKNUMBER,
                 kpp_receiver   => '111000171',
                 numb           => num,
                 PAYDATE_       => r.PAYDATE);
          end if;
        end if;
      end loop;
    
      -- 6 Группировка по дате. Суммируем все 
      for r in loop_474 loop
        --13921
        /*writelog(r.date_,
        'Внимание! BLOCK:~1 = ' ||
        util_dm2.ACC_Ostt(1,
                          '30110810100000000003',
                          'RUR',
                          r.date_ + 1,
                          'R') || '_' || r.date_,
        id_sess);*/
        if util_dm2.ACC_Ostt(1,
                             '30110810100000000003',
                             'RUR',
                             r.date_ + 1,
                             'R') = 0 then
          num := num + 1;
          POST(id_sess_       => id_sess,
               real_payer_    => '30110810100000000003',
               real_receiver_ => '47422810800000000013',
               payer_         => 'Счет чистого платежа ',
               receiver_      => 'Корсчет Амра',
               okpo_receiver  => '11000572',
               sum_           => r.summ,
               ground_        => 'Урегулирование расчетов за ' || r.date_ || /*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               'TR:' || r.checknumber ||*/
                                 ';SID:' || id_sess,
               kpp_receiver   => '111000171',
               numb           => num,
               PAYDATE_       => r.date_);
        
          /*writelog(r.date_, 'Внимание! BLOCK:~1', id_sess);*/
        elsif util_dm2.ACC_Ostt(1,
                                '30110810100000000003',
                                'RUR',
                                r.date_ + 1,
                                'R') <> 0 and
              sign(util_dm2.ACC_Ostt(1,
                                     '30110810100000000003',
                                     'RUR',
                                     r.date_ + 1,
                                     'R')) <> -1 and
              util_dm2.ACC_Ostt(1,
                                '30110810100000000003',
                                'RUR',
                                r.date_ + 1,
                                'R') < r.summ then
          num := num + 1;
          POST(id_sess_       => id_sess,
               real_payer_    => '30110810100000000003',
               real_receiver_ => '47422810800000000013',
               payer_         => 'Счет чистого платежа ',
               receiver_      => 'Корсчет Амра',
               okpo_receiver  => '11000572',
               sum_           => r.summ - util_dm2.ACC_Ostt(1,
                                                            '30110810100000000003',
                                                            'RUR',
                                                            trunc(sysdate) + 1,
                                                            'R'),
               ground_        => 'Урегулирование расчетов за ' || r.date_ || /*
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             'TR:' || r.checknumber ||*/
                                 ';SID:' || id_sess,
               kpp_receiver   => '111000171',
               numb           => num,
               PAYDATE_       => r.date_);
          /*writelog(r.date_, 'Внимание! BLOCK:~2', id_sess);*/
        end if;
        /*Группировка по дате. Суммируем все */
      end loop;
    
      /*
      Оплата со сдачи: 
      1) наша сдача в нашем, 
      2) чужая сдача в нашем,
      3) наша сдача в чужом*
      */
      for r in (select (select DEPARTMENT
                          from Z_SB_TERMINAL_DBT
                         where NAME = t.TERMINAL) DEPARTMENT,
                       t.*
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and STATUS = '00'
                   and PROVIDER <> 'СберБанк'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and AMOUNTWITHCHECKS <> 0) loop
      
        /*наш или нет терминал*/
        select count(*)
          into bnk_deal_for_chek
          from z_sb_terminal_dbt t
         where t.name = r.terminal;
      
        /*наш или нет терминал*/
      
        /*Если наш терминал*/
        if bnk_deal_for_chek > 0 then
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
                into pnmb
                From dual
               where level = 3
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            
              /*ID терминала на котором возникла сдача*/
            
              /*сумма сдачи*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into sump
                From dual
               where level = 2
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*сумма сдачи*/
            
              /*Номер Чека сдачи*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into chknmb
                From dual
               where level = 1
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*Номер Чека сдачи*/
            
              /*Дата Чека сдачи*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into chkdate
                From dual
               where level = 4
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*Дата Чека сдачи*/
            
              /*наш или нет терминал*/
              select count(*)
                into bnk_deal
                from z_sb_terminal_dbt t
               where t.name = pnmb;
              /*наш или нет терминал*/
            
              --Если сдача нашего терминала
              if bnk_deal > 0 then
                /*Поиск сдачи*/
                select count(*)
                  into search_termdeal
                  from z_sb_termdeal_amra_dbt t
                 where t.paymentnumber = chknmb;
              
                /*Поиск сдачи*/
                --Если нашли сдачу
                if search_termdeal > 0 then
                  write_deal(sump,
                             trunc(r.paydate),
                             r.service,
                             1,
                             r.checknumber);
                  /*num := num + 1;
                  POST(id_sess_       => id_sess,
                       real_payer_    => '30232810700000010009',
                       real_receiver_ => '30232810400000010011',
                       payer_         => 'Счет оплаты со сдачи ',
                       receiver_      => 'Чистый платеж',
                       okpo_receiver  => '11000572',
                       sum_           => r.summ,
                       ground_        => 'Перенос сдачи ' || r.SERVICE ||
                                         ' за ' || r.PAYDATE || ' SID:' ||
                                         id_sess,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);*/
                elsif search_termdeal = 0 then
                  writelog(r.PAYDATE,
                           'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                           ' Сдача № ' || chknmb || ' направление ' || pnmb ||
                           ' сумма ' || sump || ' дата ' || chkdate ||
                           ' BLOCK:4',
                           id_sess);
                end if;
              elsif bnk_deal = 0 then
                null;
                /*num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => '30110810100000000003',
                     real_receiver_ => '30232810700000010012',
                     payer_         => 'Счет общей суммы транзакции ' ||
                                       r.idterm || ' отделения ' ||
                                       r.department,
                     receiver_      => 'Счет оплаты со сдачи ' || r.idterm ||
                                       ' отделения ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => to_number(replace(replace(sump,
                                                                 '.',
                                                                 ','),
                                                         ' ',
                                                         '')),
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE,
                     chk_           => r.CHECKSINCOMING);*/
              end if;
            end loop;
          end if;
        elsif bnk_deal_for_chek = 0 then
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
                into pnmb
                From dual
               where level = 3
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*ID терминала на котором возникла сдача*/
            
              /*сумма сдачи*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into sump
                From dual
               where level = 2
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*сумма сдачи*/
            
              /*Номер Чека сдачи*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into chknmb
                From dual
               where level = 1
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*Номер Чека сдачи*/
            
              /*Дата Чека сдачи*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into chkdate
                From dual
               where level = 4
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              /*Дата Чека сдачи*/
            
              /*наш или нет терминал*/
              select count(*)
                into bnk_deal
                from z_sb_terminal_dbt t
               where t.name = pnmb;
              /*наш или нет терминал*/
            
              --Если сдача нашего терминала
              if bnk_deal > 0 then
                /*Поиск сдачи*/
                select count(*)
                  into search_termdeal
                  from z_sb_termdeal_amra_dbt t
                 where t.paymentnumber = chknmb;
                /*Поиск сдачи*/
                --Если нашли сдачу
                if search_termdeal > 0 then
                  write_deal(sump,
                             trunc(r.paydate),
                             r.service,
                             2,
                             r.checknumber);
                  /*num := num + 1;
                  POST(id_sess_       => id_sess,
                       real_payer_    => '30232810700000010009',
                       real_receiver_ => '30110810100000000003',
                       payer_         => 'Счет оплаты со сдачи ',
                       receiver_      => 'Корсчет Амра',
                       okpo_receiver  => '11000572',
                       sum_           => r.summ,
                       ground_        => 'Перенос сдачи' || r.SERVICE ||
                                         ' за ' || r.PAYDATE || ' SID:' ||
                                         id_sess,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);*/
                end if;
              elsif search_termdeal = 0 then
                writelog(r.PAYDATE,
                         'Внимание! Чек CheckNumber = ' || r.CheckNumber ||
                         ' Сдача № ' || chknmb || ' направление ' || pnmb ||
                         ' сумма ' || sump || ' дата ' || chkdate ||
                         ' BLOCK:5',
                         id_sess);
              end if;
            end loop;
          end if;
        end if;
      end loop;
    
      --Агрегация
      for r in (select sum(summ) summ, t.date_, t.service_ --, t.checknumber
                  from Z_SB_CALC_DEAL_ t
                 where t.vector = 1
                 group by t.date_, t.service_) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810700000010009',
             real_receiver_ => '30232810800000010006',
             payer_         => 'Счет оплаты со сдачи ',
             receiver_      => 'Чистый платеж',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Перенос сдачи ' || r.service_ || ' За ' ||
                               r.date_ /*|| 'TR:' || r.checknumber */
                               || ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.date_);
      end loop;
      for r in (select sum(summ) summ, t.date_, t.service_ --, t.checknumber
                  from Z_SB_CALC_DEAL_ t
                 where t.vector = 2
                 group by t.date_, t.service_) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810700000010009',
             real_receiver_ => '30110810100000000003',
             payer_         => 'Счет оплаты со сдачи ',
             receiver_      => 'Корсчет Амра',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Перенос сдачи' || r.service_ || ' за ' ||
                               r.date_ || /* 'TR:' || r.checknumber ||*/
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.date_);
      end loop;
      /*
      Оплата со сдачи: 
      1) наша сдача в нашем, 
      2) чужая сдача в нашем,
      3) наша сдача в чужом*
      ------------------------------
      */
    
      --7
      for r in (select sum(AMOUNTOFPAYMENT) summ,
                       trunc(PAYDATE) PAYDATE,
                       SERVICE /*,
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       t.checknumber*/
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and STATUS = '00'
                   and PROVIDER <> 'СберБанк'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                 group by SERVICE, trunc(PAYDATE)) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810400000010011',
             real_receiver_ => '30110810100000000003',
             payer_         => 'Счет чистого платежа ',
             receiver_      => 'Корсчет Амра',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Расчеты с дилером потерминальной сети по усл. ' ||
                               r.SERVICE || ' за ' || r.PAYDATE || /*'TR:' ||
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   r.checknumber || */
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      end loop;
      --8
      for r in (select sum(to_number(replace(replace(replace(NKAMOUNT,
                                                             '-',
                                                             ''),
                                                     '.',
                                                     ','),
                                             ' ',
                                             ''))) summ,
                       trunc(PAYDATE) PAYDATE,
                       --t.checknumber,
                       SERVICE
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and substr(NKAMOUNT, 1, 1) = '-'
                   and STATUS = '00'
                   and PROVIDER <> 'СберБанк'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                 group by SERVICE, trunc(PAYDATE)) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810100000010010',
             real_receiver_ => '30110810100000000003',
             payer_         => 'Общая сумма комиссии',
             receiver_      => 'Корсчет Амра',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Комиссия за услугу ' || r.SERVICE || ' за ' ||
                               r.PAYDATE || /*'TR:' || r.checknumber ||*/
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      
      end loop;
      --9
      for r in (select COMMISSIONAMOUNT - decode(substr(NKAMOUNT, 1, 1),
                                                 '-',
                                                 to_number(replace(replace(replace(NKAMOUNT,
                                                                                   '-',
                                                                                   ''),
                                                                           '.',
                                                                           ','),
                                                                   ' ',
                                                                   '')),
                                                 0) summ,
                       trunc(PAYDATE) PAYDATE,
                       t.checknumber,
                       SERVICE
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and COMMISSIONAMOUNT <> 0
                   and STATUS = '00'
                   and PROVIDER <> 'СберБанк'
                      --and t.checknumber = input_number
                      --and trunc(PAYDATE) = input_date
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                /*group by SERVICE, trunc(PAYDATE)*/
                ) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30232810100000010010',
             real_receiver_ => '70107810000001720109',
             payer_         => 'Общая сумма комиссии',
             receiver_      => 'Счет комиссии ',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Комиссия за услугу ' || r.SERVICE || ' за ' ||
                               r.PAYDATE || 'TR:' || r.checknumber ||
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      
      end loop;
    
      --10
      for r in (select sum(NKAMOUNT) summ, trunc(PAYDATE) PAYDATE, SERVICE /*,
                                                                                                                                                                                                                                                                                                                                                                                                                                       t.checknumber*/
                  from Z_SB_TRANSACT_AMRA_DBT t
                 where t.SESS_ID = id_sess
                   and t.TRANSACTIONTYPE not in
                       ('Инкассация',
                        'ДокаткаПлатежа')
                   and substr(NKAMOUNT, 1, 1) <> '-'
                   and NKAMOUNT <> '0'
                   and STATUS = '00'
                      --and trunc(PAYDATE) = input_date
                      --and t.checknumber = input_number
                   and PROVIDER <> 'СберБанк'
                   and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)
                 group by SERVICE, trunc(PAYDATE)) loop
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => '30110810100000000003',
             real_receiver_ => '70107810000001720109',
             payer_         => 'Общая сумма комиссии',
             receiver_      => 'Счет комиссии ',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => 'Комиссия за услугу ' || r.SERVICE || ' за ' ||
                               r.PAYDATE || /*|| 'TR:' || r.checknumber ||*/
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE);
      
      end loop;
    exception
      when others then
        rollback;
        writelog(descripion => SQLERRM || ' ' ||
                               DBMS_UTILITY.format_error_stack || ' ' ||
                               dbms_utility.format_error_backtrace,
                 sess_id1   => id_sess);
      
    end;
  
    /*Возврат результата, если ошибка*/
    SELECT count(lg.sess_id)
      into res
      FROM Z_SB_LOG_AMRA lg
     WHERE lg.sess_id = id_sess;
  
    IF res = 0 THEN
      ret := getclob3(id_sess);
      update Z_SB_FN_SESS_AMRA t set STATUS = 2 where t.sess_id = id_sess;
      commit;
    ELSE
      rollback;
      ret := getclob_(id_sess);
    END IF;
    /*Возврат результата, если ошибка*/
  
    RETURN ret;
  end make;

begin
  null;
end z_sb_calc_tr_amra;
/

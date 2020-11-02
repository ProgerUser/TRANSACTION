create or replace package z_sb_calc_tr_amra is

  -- Author  : Pachuliya_S_V
  -- Created : 02.07.2019 10:15:36
  -- Purpose : ������ ���������� ���� �����
  FUNCTION make(id_sess NUMBER /*, input_number varchar2, input_date date*/)
    RETURN CLOB;
  FUNCTION getclob3(sessid NUMBER) RETURN CLOB;

  FUNCTION getclob_(sessid NUMBER) RETURN CLOB;
  v_ret Z_SB_POST_TABLE;
  function DOC_REG(real_payer1      varchar2,
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
                   CORACC_PAYER_2   varchar2 DEFAULT null,
                   BUDJCLASSIFCODE_ varchar2 DEFAULT null,
                   BANK_RECEIVER_   varchar2 DEFAULT null,
                   RECEIVER_        varchar2 DEFAULT null,
                   kpp_receiver_    varchar2 DEFAULT null,
                   OKPO_RECEIVER_   varchar2 DEFAULT null,
                   OKATO            varchar2 DEFAULT null,
                   pnmb             varchar2 DEFAULT null,
                   PLATELSHIK2      varchar2 DEFAULT NULL,
                   KPP_payer_       varchar2 DEFAULT NULL) return number;

  procedure insert_post(id_sess_         NUMBER,
                        department_      VARCHAR2 default 0,
                        real_payer_      VARCHAR2,
                        real_receiver_   VARCHAR2,
                        payer_           VARCHAR2,
                        receiver_        VARCHAR2,
                        okpo_receiver    VARCHAR2,
                        sum_             number,
                        ground_          VARCHAR2,
                        payment_number   varchar2 DEFAULT NULL,
                        coracc_payer     VARCHAR2 DEFAULT NULL,
                        mfo_receiver     VARCHAR2 DEFAULT NULL,
                        bank_receiver    VARCHAR2 DEFAULT NULL,
                        kpp_receiver     VARCHAR2 DEFAULT NULL,
                        budjclassifcode  VARCHAR2 DEFAULT NULL,
                        okato            VARCHAR2 DEFAULT NULL,
                        bo1_             VARCHAR2 DEFAULT '1',
                        bo2_             VARCHAR2 DEFAULT NULL,
                        okpo_payer       VARCHAR2 DEFAULT NULL,
                        numb             number,
                        PAYDATE_         date,
                        trnnum_          number,
                        payment_numbers_ clob DEFAULT NULL);

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
                 CORACC_PAYER_2  VARCHAR2 DEFAULT NULL,
                 mfo_receiver    VARCHAR2 DEFAULT NULL,
                 bank_receiver   VARCHAR2 DEFAULT NULL,
                 kpp_receiver    VARCHAR2 DEFAULT NULL,
                 budjclassifcode VARCHAR2 DEFAULT NULL,
                 okato           VARCHAR2 DEFAULT NULL,
                 bo1_            VARCHAR2 DEFAULT '1',
                 bo2_            VARCHAR2 DEFAULT NULL,
                 okpo_payer      VARCHAR2 DEFAULT NULL,
                 numb            number,
                 PAYDATE_        date,
                 chk_            VARCHAR2 DEFAULT NULL,
                 for_nbra        boolean DEFAULT false,
                 payment_numbers clob DEFAULT NULL,
                 PLATELSHIK1     varchar2 DEFAULT NULL,
                 KPP_payer       varchar2 DEFAULT NULL);

end z_sb_calc_tr_amra;
/
create or replace package body z_sb_calc_tr_amra is

  /*��������� ������ ����*/
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

  /*��������� ������ ������ �� ����*/
  PROCEDURE write_deal(sump_        number,
                       paydate_     date,
                       service_     VARCHAR2,
                       vector_      number,
                       chk          varchar2,
                       DEAL_ACC_    varchar2,
                       GENERAL_ACC_ varchar2) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
    insert into Z_SB_CALC_DEAL_
      (summ, date_, service_, vector, CHECKNUMBER, DEAL_ACC, GENERAL_ACC)
    values
      (sump_, paydate_, service_, vector_, chk, DEAL_ACC_, GENERAL_ACC_);
    COMMIT;
  END;

  /*������� �������� �������*/
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
 where ITRNNUM in
       (select KINDPAYMENT
          from z_sb_postdoc_amra_dbt
         where sess_id =
         ~' || sessid || ')');
  
    xsl := q'~<?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
    <xsl:for-each select="ROWSET/ROW">
����_������� -> <xsl:value-of select="PAYDATE"/><br/>
�����_������ -> <xsl:value-of select="DEB_CRED"/><br/>
��������_������ -> <xsl:value-of select="DESC_"/><br/>
    </xsl:for-each>
    </xsl:template>
    </xsl:stylesheet>~';
  
    DBMS_XMLGEN.setxslt(q, xsl);
    DBMS_XMLGEN.getxml(q, ret);
    RETURN ret;
  END;

  /*������� �������� �������_2*/
  FUNCTION getclob3(sessid NUMBER) RETURN CLOB IS
    ret CLOB;
    xsl CLOB;
    q   DBMS_XMLGEN.ctxhandle;
  BEGIN
    DBMS_LOB.createtemporary(ret, TRUE);
    q := DBMS_XMLGEN.newcontext(q'~ 
    select ITRNDOCNUM || '|' || DTRNCREATE || '|' || CTRNACCD || '->' ||
       CTRNACCC || '|' || MTRNRSUM  ABC,
       DTRNCREATE,
       CTRNACCD,
       CTRNACCC,
       MTRNRSUM,
       CTRNPURP
  from trn
 where ITRNNUM in
       (select KINDPAYMENT
          from z_sb_postdoc_amra_dbt
         where sess_id = ~' || sessid ||
                                ') order by ITRNDOCNUM');
  
    xsl := q'~<?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
    <xsl:for-each select="ROWSET/ROW">
DOC_NUM: <xsl:value-of select="ABC"/><br/>
    </xsl:for-each>
    </xsl:template>
    </xsl:stylesheet>~';
  
    DBMS_XMLGEN.setxslt(q, xsl);
    DBMS_XMLGEN.getxml(q, ret);
    RETURN ret;
  END;

  /*������� ����������� ����������*/
  function DOC_REG(real_payer1      varchar2,
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
                   CORACC_PAYER_2   varchar2 DEFAULT null,
                   BUDJCLASSIFCODE_ varchar2 DEFAULT null,
                   BANK_RECEIVER_   varchar2 DEFAULT null,
                   RECEIVER_        varchar2 DEFAULT null,
                   kpp_receiver_    varchar2 DEFAULT null,
                   OKPO_RECEIVER_   varchar2 DEFAULT null,
                   OKATO            varchar2 DEFAULT null,
                   pnmb             varchar2 DEFAULT null,
                   PLATELSHIK2      varchar2 DEFAULT NULL,
                   KPP_payer_       varchar2 DEFAULT NULL) return number is
    /*PRAGMA AUTONOMOUS_TRANSACTION;*/
    err2    varchar2(2000);
    b       varchar2(60);
    tts     TS.T_DeptInfo;
    itrnnum number;
    strwithparam varchar2(4000);
  begin
    if (NVL(ground1, 'NULL') = 'NULL' or length(ground1) = 0) then
      writelog(PAYDATE1,
               '����������� ���������� ������� ��� = '' ~ -> �����:' || sum1 ||
               '-> TR:' || pnmb,
               id_sess1,
               DC => '��������: ' || real_payer1 || '->' || real_receiver1);
    else
      if for_nbra_ = true then
      
        idoc_reg.SetUpRegisterParams('2TRN');
        tts.cBudCode     := BUDJCLASSIFCODE_;
        tts.cOKATOCode   := OKATO;
        tts.cCreatStatus := '15';
        tts.cNalPurp     := '0';
        tts.cNalPeriod   := '0';
        tts.cNalDocNum   := '0';
        tts.cNalDocDate  := '0';
        tts.cNalType     := '0';
        tts.cDocIndex    := '0';
      
        b := Idoc_Reg.Register(ErrorMsg      => err2, -- ��������� �� ������ ��� ��������
                               PayerAcc      => real_payer1, -- ���� ������
                               RecipientAcc  => real_receiver1, -- ���� ����������
                               Summa         => sum1, -- �����, ���� �������� ���.���. �� ��� ����� � ������
                               OpType        => 4, -- ��� ��������
                               RegDate       => /*trunc(sysdate)*/ trunc(PAYDATE1), -- ���� �����������
                               DocDate       => /*trunc(sysdate)*/ trunc(PAYDATE1), --���� ���������
                               DocNum        => numb1, -- ����� ���������
                               BatNum        => 1500, -- ����� �����
                               bIgnoreRB     => true, -- ����: ������������ ������������� �������� ������
                               Purpose       => ground1, -- ���������� �������
                               SubOpType     => 0,
                               INNA          => OKPO_PAYER_,
                               CorAccO       => CORACC_PAYER_, /*Client_RBIC   => MFO_RECEIVER_*/
                               CorAccAName   => BANK_RECEIVER_,
                               RecipientName => RECEIVER_,
                               MFOa          => MFO_RECEIVER_,
                               KPPA          => kpp_receiver_,
                               Client_KPP    => KPP_payer_,
                               Client_INN    => OKPO_RECEIVER_,
                               rDeptInfo     => tts,
                               CorAccA       => CORACC_PAYER_2,
                               Client_Name   => PLATELSHIK2); -- ��� �������� 2-�� �������
        null;
      elsif for_nbra_ = false then
        idoc_reg.SetUpRegisterParams('2TRN');
      
        b := Idoc_Reg.Register(ErrorMsg     => err2, -- ��������� �� ������
                               PayerAcc     => real_payer1, -- ���� ������
                               RecipientAcc => real_receiver1, -- ���� �������
                               Summa        => sum1, -- ����� ������ (� ������ ������)
                               OpType       => 1, -- ��� �������� 1-�� �������
                               RegDate      => /*trunc(sysdate)*/ trunc(PAYDATE1), -- ���� �����������
                               DocNum       => numb1,
                               BatNum       => 1500, -- �����
                               bIgnoreRB    => true,
                               Purpose      => ground1);
        null;
      end if;
  
      if b <> 'Ok' then
        strwithparam := chr(13)||chr(10)||
               'for_nbra_:'|| CASE for_nbra_
     WHEN TRUE THEN 'TRUE'
     WHEN FALSE THEN 'FALSE'
     ELSE 'NULL'
   END||chr(13)||chr(10)||
               'real_payer1:'||real_payer1||chr(13)||chr(10)||
               'real_receiver1:'||real_receiver1||chr(13)||chr(10)||
               'PAYDATE1:'||PAYDATE1||chr(13)||chr(10)||
               'numb1:'||numb1||chr(13)||chr(10)||
               'sum1:'||sum1||chr(13)||chr(10)||
               'id_sess1:'||id_sess1||chr(13)||chr(10)||
               'mfo_receiver_:'||mfo_receiver_||chr(13)||chr(10)||
               'OKPO_PAYER_:'||OKPO_PAYER_||chr(13)||chr(10)||
               'CORACC_PAYER_:'||CORACC_PAYER_||chr(13)||chr(10)||
               'CORACC_PAYER_2:'||CORACC_PAYER_2||chr(13)||chr(10)||  
               'BUDJCLASSIFCODE_:'||BUDJCLASSIFCODE_||chr(13)||chr(10)||
               'BANK_RECEIVER_:'||BANK_RECEIVER_||chr(13)||chr(10)||
               'RECEIVER_:'||RECEIVER_||chr(13)||chr(10)||
               'kpp_receiver_:'||kpp_receiver_||chr(13)||chr(10)||                                          
               'OKPO_RECEIVER_:'||OKPO_RECEIVER_||chr(13)||chr(10)||
               'OKATO:'||OKATO||chr(13)||chr(10)||
               'pnmb:'||pnmb||chr(13)||chr(10)||
               'PLATELSHIK2:'||PLATELSHIK2||chr(13)||chr(10)||
               'KPP_payer_:'||KPP_payer_||chr(13)||chr(10);
        writelog(PAYDATE1,
                 
                 DBMS_UTILITY.FORMAT_ERROR_BACKTRACE||chr(13)||chr(10)||
                 DBMS_UTILITY.FORMAT_CALL_STACK||chr(13)||chr(10)
                 ||err2 || b || '-> �����:' || sum1 || '-> TR:' || pnmb,
                 id_sess1,
                 DC =>strwithparam );
      else
        /*commit;*/
        itrnnum := IDOC_REG.GetLastDocID;
        null;
      end if;
    end if;
    return itrnnum;
  end;

  /*�������� � POSTDOC*/
  procedure insert_post(id_sess_         NUMBER,
                        department_      VARCHAR2 default 0,
                        real_payer_      VARCHAR2,
                        real_receiver_   VARCHAR2,
                        payer_           VARCHAR2,
                        receiver_        VARCHAR2,
                        okpo_receiver    VARCHAR2,
                        sum_             number,
                        ground_          VARCHAR2,
                        payment_number   varchar2 DEFAULT NULL,
                        coracc_payer     VARCHAR2 DEFAULT NULL,
                        mfo_receiver     VARCHAR2 DEFAULT NULL,
                        bank_receiver    VARCHAR2 DEFAULT NULL,
                        kpp_receiver     VARCHAR2 DEFAULT NULL,
                        budjclassifcode  VARCHAR2 DEFAULT NULL,
                        okato            VARCHAR2 DEFAULT NULL,
                        bo1_             VARCHAR2 DEFAULT '1',
                        bo2_             VARCHAR2 DEFAULT NULL,
                        okpo_payer       VARCHAR2 DEFAULT NULL,
                        numb             number,
                        PAYDATE_         date,
                        trnnum_          number,
                        payment_numbers_ clob DEFAULT NULL) is
    /*PRAGMA AUTONOMOUS_TRANSACTION;*/
    res number;
    /*dubl number;*/
  begin
    SELECT count(lg.sess_id)
      into res
      FROM Z_SB_LOG_AMRA lg
     WHERE lg.sess_id = id_sess_;
  
    if res = 0 then
      if payment_number is null then
        INSERT INTO Z_SB_POSTDOC_AMRA_DBT
          (recdate,
           pay_date,
           date_document,
           date_value,
           department,
           account_payer,
           payer,
           okpo_payer,
           account_receiver,
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
           budjclassifcode,
           okato,
           sess_id,
           userfield1,
           userfield2,
           NUMB_PAYDOC,
           KINDPAYMENT,
           PAYMENTNUMBERS)
        VALUES
          (sysdate,
           PAYDATE_,
           PAYDATE_,
           PAYDATE_,
           department_,
           real_payer_,
           payer_,
           okpo_payer,
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
           budjclassifcode,
           okato,
           id_sess_,
           bo1_,
           bo2_,
           numb,
           trnnum_,
           payment_numbers_);
        /*commit;*/
      else
        INSERT INTO Z_SB_POSTDOC_AMRA_DBT
          (recdate,
           pay_date,
           date_document,
           date_value,
           department,
           account_payer,
           payer,
           okpo_payer,
           account_receiver,
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
           budjclassifcode,
           okato,
           sess_id,
           userfield1,
           userfield2,
           NUMB_PAYDOC,
           KINDPAYMENT,
           PAYMENTNUMBERS)
        VALUES
          (sysdate,
           PAYDATE_,
           PAYDATE_,
           PAYDATE_,
           department_,
           real_payer_,
           payer_,
           okpo_payer,
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
           budjclassifcode,
           okato,
           id_sess_,
           bo1_,
           bo2_,
           numb,
           trnnum_,
           payment_number);
      end if;
    end if;
  
    /*        select count(*)
      into dubl
      from (select count(*) over(partition by account_payer, account_receiver, sum, sess_id, ground, pay_date) cnt,
                   ROW_NUMBER() over(partition by account_payer, account_receiver, sum, sess_id, ground, pay_date order by account_payer, account_receiver, sum, ground) rn,
                   rowid rd,
                   t.*
              from Z_SB_POSTDOC_AMRA_DBT t
             where t.sess_id = id_sess_)
     where cnt > 1;
    if dubl > 0 then
      writelog(PAYDATE_, '������� ' || ground_, id_sess_);
    end if;*/
  end;

  /*������ � ������� � � ���*/
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
                 CORACC_PAYER_2  VARCHAR2 DEFAULT NULL,
                 mfo_receiver    VARCHAR2 DEFAULT NULL,
                 bank_receiver   VARCHAR2 DEFAULT NULL,
                 kpp_receiver    VARCHAR2 DEFAULT NULL,
                 budjclassifcode VARCHAR2 DEFAULT NULL,
                 okato           VARCHAR2 DEFAULT NULL,
                 bo1_            VARCHAR2 DEFAULT '1',
                 bo2_            VARCHAR2 DEFAULT NULL,
                 okpo_payer      VARCHAR2 DEFAULT NULL,
                 numb            number,
                 PAYDATE_        date,
                 chk_            VARCHAR2 DEFAULT NULL,
                 for_nbra        boolean DEFAULT false,
                 payment_numbers clob DEFAULT NULL,
                 PLATELSHIK1     varchar2 DEFAULT NULL,
                 KPP_payer       varchar2 DEFAULT NULL) IS
    pnmb      varchar2(50);
    res       number;
    trnnum    number;
    dubl_post number;
  BEGIN
    if sum_ is not null and sum_ > 0 /*and
                                                                                   (ground_ is not null and ground_ <> '')*/
     then
      begin
        /*�������� ��������*/
        SELECT count(lg.sess_id)
          into res
          FROM Z_SB_LOG_AMRA lg
         WHERE lg.sess_id = id_sess_;
        if res = 0 then
          trnnum := DOC_REG(real_payer1      => real_payer_,
                            real_receiver1   => real_receiver_,
                            PAYDATE1         => PAYDATE_,
                            numb1            => numb,
                            ground1          => ground_,
                            sum1             => sum_,
                            id_sess1         => id_sess_,
                            for_nbra_        => for_nbra,
                            mfo_receiver_    => mfo_receiver,
                            CORACC_PAYER_    => CORACC_PAYER,
                            budjclassifcode_ => budjclassifcode,
                            BANK_RECEIVER_   => BANK_RECEIVER,
                            RECEIVER_        => receiver_,
                            CORACC_PAYER_2   => CORACC_PAYER_2,
                            kpp_receiver_    => kpp_receiver,
                            OKPO_RECEIVER_   => OKPO_RECEIVER,
                            OKPO_PAYER_      => OKPO_PAYER,
                            okato            => okato,
                            pnmb             => payment_number,
                            PLATELSHIK2      => PLATELSHIK1,
                            KPP_payer_       => KPP_payer);
          /*commit;*/
        end if;
        /*�������� ��������*/
      
        SELECT count(lg.sess_id)
          into res
          FROM Z_SB_LOG_AMRA lg
         WHERE lg.sess_id = id_sess_;
        if res = 0 then
          insert_post(id_sess_         => id_sess_,
                      department_      => department_,
                      real_payer_      => real_payer_,
                      real_receiver_   => real_receiver_,
                      payer_           => payer_,
                      receiver_        => receiver_,
                      okpo_receiver    => okpo_receiver,
                      sum_             => sum_,
                      ground_          => ground_,
                      payment_number   => payment_number,
                      coracc_payer     => coracc_payer,
                      mfo_receiver     => mfo_receiver,
                      bank_receiver    => bank_receiver,
                      kpp_receiver     => kpp_receiver,
                      budjclassifcode  => budjclassifcode,
                      okato            => okato,
                      bo1_             => bo1_,
                      bo2_             => bo2_,
                      okpo_payer       => okpo_payer,
                      numb             => numb,
                      PAYDATE_         => PAYDATE_,
                      trnnum_          => trnnum,
                      payment_numbers_ => payment_numbers);
        
          if payment_number is not null then
            update Z_SB_TRANSACT_AMRA_DBT t
               set t.STATUSABS = 1
             where t.SESS_ID = id_sess_
               and t.CHECKNUMBER = payment_number;
            /*�������� �� ��������� ���������� ��� � ���������*/
            select count(*)
              into dubl_post
              from z_sb_postdoc_amra_dbt p
             where p.paymentnumbers like '%' || payment_number || '&';
            if dubl_post > 0 then
              writelog(descripion => '��������� ����� ��. ' ||
                                     payment_number || ' � POSTDOCS',
                       sess_id1   => id_sess_);
            end if;
            /*------------------------------------------------*/
          end if;
        
          if payment_numbers is not null then
            for r in (select CHECKSINCOMING, PAYDATE, checknumber
                        from z_sb_transact_amra_dbt t
                       where t.checknumber in
                             (select COLUMN1
                                from table(lob2table.separatedcolumns(payment_numbers, /* the data LOB */
                                                                      chr(13) ||
                                                                      chr(10), /* row separator */
                                                                      '|', /* column separator */
                                                                      '' /* delimiter (optional) */)))) loop
              for r_ in (Select Regexp_Substr(r.CHECKSINCOMING,
                                              '[^|]+',
                                              1,
                                              Level) chk
                           From dual
                         Connect By Regexp_Substr(r.CHECKSINCOMING,
                                                  '[^|]+',
                                                  1,
                                                  Level) Is Not Null) loop
              
                /*����� ���� �� ������*/
                Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                  into pnmb
                  From dual
                 where level = 1
                Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
              
                update z_sb_termdeal_amra_dbt t
                   set t.DEALENDDATE       = r.PAYDATE,
                       t.DEALPAYMENTNUMBER = r.checknumber,
                       t.STATUS            = 1
                 where t.paymentnumber = pnmb;
                /*�������� �� ��������� ���������� ��� � ���������*/
                select count(*)
                  into dubl_post
                  from z_sb_postdoc_amra_dbt p
                 where p.paymentnumbers like '%' || pnmb || '&';
                if dubl_post > 0 then
                  writelog(descripion => '��������� ����� ��. ' || pnmb ||
                                         ' � POSTDOCS',
                           sess_id1   => id_sess_);
                end if;
                /*------------------------------------------------*/
              end loop;
            end loop;
          end if;
        
          if chk_ is not null then
            for r_ in (Select Regexp_Substr(chk_, '[^|]+', 1, Level) chk
                         From dual
                       Connect By Regexp_Substr(chk_, '[^|]+', 1, Level) Is Not Null) loop
            
              /*����� ���� �� ������*/
              Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                into pnmb
                From dual
               where level = 1
              Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
            
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
          writelog(descripion => SQLERRM || ' ' ||
                                 DBMS_UTILITY.format_error_stack || ' ' ||
                                 dbms_utility.format_error_backtrace,
                   sess_id1   => id_sess_);
      end;
    else
      /*if sum_ is null and sum_ < 0*/
      writelog(descripion => '��������! � ���������� ' || payment_numbers || ' ' ||
                             payment_number || ' ' || real_payer_ || '->' ||
                             real_receiver_ || ' ����� = NULL' || sum_,
               sess_id1   => id_sess_); /*
                                                                                elsif (ground_ is null and ground_ <> '') then
                                                                                  writelog(descripion => '��������! � ���������� ' || payment_numbers || ' ' ||
                                                                                                         payment_number || ' ' || real_payer_ || '->' ||
                                                                                                         real_receiver_ || ' ��������� ������ = ~' ||
                                                                                                         ground_ || '~',
                                                                                           sess_id1   => id_sess_);*/
    end if;
  end POST;

  /*�� ����� �������� �� ��������*/
  procedure comistocor(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     GENERAL_COMIS,
                     SERVICE,
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select to_number(replace(replace(replace(NKAMOUNT,
                                                               '-',
                                                               ''),
                                                       '.',
                                                       ','),
                                               '�',
                                               '')) summ,
                             trunc(PAYDATE) PAYDATE,
                             (select GENERAL_COMIS
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) GENERAL_COMIS,
                             SERVICE,
                             t.checknumber
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and t.TRANSACTIONTYPE not in
                             ('����������',
                              '��������������')
                         and substr(NKAMOUNT, 1, 1) = '-'
                         and STATUS = '00'
                            --and t.checknumber = input_number
                            --and trunc(PAYDATE) = input_date
                         and PROVIDER = '��������'
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE, GENERAL_COMIS) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => r.GENERAL_COMIS,
           real_receiver_  => '30232810300000010014',
           payer_          => '����� ����� ��������',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                              r.PAYDATE || /* 'TR:' || r.checknumber ||*/
                              ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*�� ����� �������� �� �������*/
  procedure comistocor_spec(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     --GENERAL_COMIS,
                     SERVICE,
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select to_number(replace(replace(replace(NKAMOUNT,
                                                               '-',
                                                               ''),
                                                       '.',
                                                       ','),
                                               '�',
                                               '')) summ,
                             trunc(PAYDATE) PAYDATE,
                             (select GENERAL_COMIS
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) GENERAL_COMIS,
                             SERVICE,
                             t.checknumber
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and t.TRANSACTIONTYPE not in
                             ('����������',
                              '��������������')
                         and substr(NKAMOUNT, 1, 1) = '-'
                         and STATUS = '00'
                            --and t.checknumber = input_number
                            --and trunc(PAYDATE) = input_date
                         and PROVIDER = '��������'
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE /*, GENERAL_COMIS*/
              ) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => '30232810300000010014',
           real_receiver_  => '30110810100000000003',
           payer_          => '����� ����� ��������',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                              r.PAYDATE || /* 'TR:' || r.checknumber ||*/
                              ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*���� ��������*/
  procedure selfcomiss(id_sess number, num in out number) is
  begin
    for r in (select COMMISSIONAMOUNT -
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
                                       '�',
                                       '')) summ,
                     trunc(PAYDATE) PAYDATE,
                     (select GENERAL_COMIS
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) GENERAL_COMIS,
                     (select INCOME
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) INCOME,
                     t.checknumber,
                     
                     SERVICE
                from Z_SB_TRANSACT_AMRA_DBT t
               where t.SESS_ID = id_sess
                 and t.TRANSACTIONTYPE not in
                     ('����������', '��������������')
                 and STATUS = '00'
                 and PROVIDER = '��������'
                    --and t.checknumber = input_number
                    --and trunc(PAYDATE) = input_date
                 and t.TERMINAL in (select NAME from Z_SB_TERMINAL_AMRA_DBT)
              /*group by SERVICE, trunc(PAYDATE)*/
              union all
              select COMMISSIONAMOUNT -
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
                                       '�',
                                       '')) summ,
                     trunc(PAYDATE) PAYDATE,
                     (select GENERAL_COMIS
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) GENERAL_COMIS,
                     (select INCOME
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) INCOME,
                     t.checknumber,
                     
                     SERVICE
                from Z_SB_TRANSACT_AMRA_DBT t
               where t.SESS_ID = id_sess
                 and t.TRANSACTIONTYPE in ('��������������')
                 and STATUS = '00'
                 and PROVIDER = '��������'
                    --and t.checknumber = input_number
                    --and trunc(PAYDATE) = input_date
                 and t.TERMINAL in (select NAME from Z_SB_TERMINAL_AMRA_DBT)
              /*group by SERVICE, trunc(PAYDATE)*/
              ) loop
      if r.summ > 0 then
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => r.GENERAL_COMIS,
             real_receiver_ => r.INCOME,
             payer_         => '����� ����� ��������',
             receiver_      => '���� �������� ',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                               r.PAYDATE || 'TR:' || r.checknumber ||
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE,
             payment_number => r.checknumber);
      end if;
    end loop;
  end;

  /*���� �� "�������" �� ��������*/
  procedure loop474(id_sess number, num in out number) is
    cursor loop_474 is
      select SUM(SUM) - nvl((select SUM(SUM) summa
                              from Z_SB_POSTDOC_AMRA_DBT t
                             where ACCOUNT_RECEIVER in
                                   ('30232810300000010014',
                                    '30232810000000010013',
                                    '30232810700000010012')
                               and sess_id = id_sess
                               and ACCOUNT_PAYER = '30110810100000000003'
                             group by trunc(DATE_VALUE)),
                            0) summa,
             listagg_clob(t.paymentnumbers) checknumbers,
             trunc(DATE_VALUE) date_
        from Z_SB_POSTDOC_AMRA_DBT t
       where ACCOUNT_RECEIVER = '30110810100000000003'
         and sess_id = id_sess
       group by trunc(DATE_VALUE);
  begin
    for r in loop_474 loop
      if util_dm2.ACC_Ostt(0,
                           '30110810100000000003',
                           'RUR',
                           r.date_,
                           'V',
                           2) < r.summa then
        num := num + 1;
        POST(id_sess_        => id_sess,
             real_payer_     => '30110810100000000003',
             real_receiver_  => '47422810800000000013',
             payer_          => '���� ������� ������� ',
             receiver_       => '������� ����',
             okpo_receiver   => '11000572',
             sum_            => r.summa,
             ground_         => '�������������� �������� �� ' || r.date_ ||
                                ';SID:' || id_sess,
             kpp_receiver    => '111000171',
             numb            => num,
             PAYDATE_        => r.date_,
             payment_numbers => r.checknumbers);
      end if;
    end loop;
  end;

  /*���� �����*/
  procedure selfdeal(id_sess number, num in out number) is
  begin
    for r in (select case
                       when (select l.CASHAMOUNT
                               from z_sb_transact_amra_dbt l
                              where l.checknumber = t.checknumber) = 0 then
                        sum(summ) -
                        (select l.COMMISSIONAMOUNT
                           from z_sb_transact_amra_dbt l
                          where l.checknumber = t.checknumber)
                       else
                        sum(summ)
                     end summ,
                     t.date_,
                     t.service_,
                     t.deal_acc,
                     t.general_acc,
                     t.checknumber
              /*listagg_clob(t.checknumber) checknumbers*/
                from Z_SB_CALC_DEAL_ t
               where t.vector = 1
               group by t.date_,
                        t.service_,
                        t.deal_acc,
                        t.general_acc,
                        t.checknumber) loop
      num := num + 1;
      POST(id_sess_       => id_sess,
           real_payer_    => r.DEAL_ACC,
           real_receiver_ => r.general_acc,
           payer_         => '���� ������ �� ����� ',
           receiver_      => '������ ������',
           okpo_receiver  => '11000572',
           sum_           => r.summ,
           ground_        => '������� ����� ' || r.service_ || ' �� ' ||
                             r.date_ || ';SID:' || id_sess || 'TR:' ||
                             r.checknumber,
           kpp_receiver   => '111000171',
           numb           => num,
           PAYDATE_       => r.date_,
           payment_number => r.checknumber);
    end loop;
  end;

  /*����� �����*/
  procedure notselfdeal(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     t.date_,
                     t.service_,
                     t.deal_acc,
                     listagg_clob(t.checknumber || ';' || summ) checknumbers
                from Z_SB_CALC_DEAL_ t
               where t.vector = 2
               group by t.date_, t.service_, t.deal_acc) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => r.DEAL_ACC,
           real_receiver_  => '30110810100000000003',
           payer_          => '���� ������ �� ����� ',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '������� �����' || r.service_ || ' �� ' ||
                              r.date_ || ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.date_,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*7*/
  procedure for7(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     SERVICE,
                     CLEAR_SUM,
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select AMOUNTOFPAYMENT summ,
                             trunc(PAYDATE) PAYDATE,
                             SERVICE,
                             (select CLEAR_SUM
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) CLEAR_SUM,
                             t.checknumber
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and STATUS = '00'
                         and PROVIDER <> '��������'
                            /*and t.checknumber = input_number
                            and trunc(PAYDATE) = input_date*/
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE, CLEAR_SUM) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => r.CLEAR_SUM,
           real_receiver_  => '30232810000000010013',
           payer_          => '���� ������� ������� ',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '������� � ������� �������������� ���� �� ���. ' ||
                              r.SERVICE || ' �� ' || r.PAYDATE || ';SID:' ||
                              id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*7_tocor*/
  procedure for7_tocor(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     SERVICE,
                     /*CLEAR_SUM,*/
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select AMOUNTOFPAYMENT summ,
                             trunc(PAYDATE) PAYDATE,
                             SERVICE,
                             (select CLEAR_SUM
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) CLEAR_SUM,
                             t.checknumber
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and STATUS = '00'
                         and PROVIDER <> '��������'
                            /*and t.checknumber = input_number
                            and trunc(PAYDATE) = input_date*/
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE /*, CLEAR_SUM*/
              ) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => '30232810000000010013',
           real_receiver_  => '30110810100000000003',
           payer_          => '���� ������� ������� ',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '������� � ������� �������������� ���� �� ���. ' ||
                              r.SERVICE || ' �� ' || r.PAYDATE || ';SID:' ||
                              id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*8*/
  procedure for8(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     GENERAL_COMIS,
                     SERVICE,
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select to_number(replace(replace(replace(NKAMOUNT,
                                                               '-',
                                                               ''),
                                                       '.',
                                                       ','),
                                               '�',
                                               '')) summ,
                             trunc(PAYDATE) PAYDATE,
                             (select GENERAL_COMIS
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) GENERAL_COMIS,
                             t.checknumber,
                             SERVICE
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and substr(NKAMOUNT, 1, 1) = '-'
                         and STATUS = '00'
                         and PROVIDER <> '��������'
                            /*and t.checknumber = input_number
                            and trunc(PAYDATE) = input_date*/
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE, GENERAL_COMIS) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => r.GENERAL_COMIS,
           real_receiver_  => '30232810300000010014',
           payer_          => '����� ����� ��������',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                              r.PAYDATE || ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*8_tocor*/
  procedure for8_tocor(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     --GENERAL_COMIS,
                     SERVICE,
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select to_number(replace(replace(replace(NKAMOUNT,
                                                               '-',
                                                               ''),
                                                       '.',
                                                       ','),
                                               '�',
                                               '')) summ,
                             trunc(PAYDATE) PAYDATE,
                             (select GENERAL_COMIS
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) GENERAL_COMIS,
                             t.checknumber,
                             SERVICE
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and substr(NKAMOUNT, 1, 1) = '-'
                         and STATUS = '00'
                         and PROVIDER <> '��������'
                            /*and t.checknumber = input_number
                            and trunc(PAYDATE) = input_date*/
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE /*, GENERAL_COMIS*/
              ) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => '30232810300000010014',
           real_receiver_  => '30110810100000000003',
           payer_          => '����� ����� ��������',
           receiver_       => '������� ����',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                              r.PAYDATE || ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*9*/
  procedure for9(id_sess number, num in out number) is
  begin
    for r in (select COMMISSIONAMOUNT - decode(substr(NKAMOUNT, 1, 1),
                                               '-',
                                               to_number(replace(replace(replace(NKAMOUNT,
                                                                                 '-',
                                                                                 ''),
                                                                         '.',
                                                                         ','),
                                                                 '�',
                                                                 '')),
                                               0) summ,
                     trunc(PAYDATE) PAYDATE,
                     (select GENERAL_COMIS
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) GENERAL_COMIS,
                     (select INCOME
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) INCOME,
                     t.checknumber,
                     SERVICE
                from Z_SB_TRANSACT_AMRA_DBT t
               where t.SESS_ID = id_sess
                 and COMMISSIONAMOUNT <> 0
                 and STATUS = '00'
                 and PROVIDER <> '��������'
                    /*and t.checknumber = input_number
                    and trunc(PAYDATE) = input_date*/
                 and t.TERMINAL in (select NAME from Z_SB_TERMINAL_AMRA_DBT)
              /*group by SERVICE, trunc(PAYDATE)*/
              ) loop
      if r.SUMM > 0 then
        num := num + 1;
        POST(id_sess_       => id_sess,
             real_payer_    => r.GENERAL_COMIS,
             real_receiver_ => r.INCOME,
             payer_         => '����� ����� ��������',
             receiver_      => '���� �������� ',
             okpo_receiver  => '11000572',
             sum_           => r.summ,
             ground_        => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                               r.PAYDATE || 'TR:' || r.checknumber ||
                               ';SID:' || id_sess,
             kpp_receiver   => '111000171',
             numb           => num,
             PAYDATE_       => r.PAYDATE,
             payment_number => r.checknumber);
      end if;
    end loop;
  end;

  /*10*/
  procedure for10(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     SERVICE,
                     INCOME,
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select NKAMOUNT summ,
                             trunc(PAYDATE) PAYDATE,
                             SERVICE,
                             (select INCOME
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) INCOME,
                             t.checknumber
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and substr(NKAMOUNT, 1, 1) <> '-'
                         and NKAMOUNT <> '0'
                         and STATUS = '00'
                            /*and trunc(PAYDATE) = input_date
                            and t.checknumber = input_number*/
                         and PROVIDER <> '��������'
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE, INCOME) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => '30232810300000010014',
           real_receiver_  => r.INCOME,
           payer_          => '����� ����� ��������',
           receiver_       => '���� �������� ',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                              r.PAYDATE || ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*10_spec*/
  procedure for10_spec(id_sess number, num in out number) is
  begin
    for r in (select sum(summ) summ,
                     PAYDATE,
                     SERVICE,
                     /*INCOME,*/
                     listagg_clob(checknumber || ';' || summ) checknumbers
                from (select NKAMOUNT summ,
                             trunc(PAYDATE) PAYDATE,
                             SERVICE,
                             (select INCOME
                                from Z_SB_TERMINAL_AMRA_DBT
                               where NAME = t.TERMINAL) INCOME,
                             t.checknumber
                        from Z_SB_TRANSACT_AMRA_DBT t
                       where t.SESS_ID = id_sess
                         and substr(NKAMOUNT, 1, 1) <> '-'
                         and NKAMOUNT <> '0'
                         and STATUS = '00'
                            /*and trunc(PAYDATE) = input_date
                            and t.checknumber = input_number*/
                         and PROVIDER <> '��������'
                         and t.TERMINAL in
                             (select NAME from Z_SB_TERMINAL_AMRA_DBT))
               group by SERVICE, PAYDATE /*, INCOME*/
              ) loop
      num := num + 1;
      POST(id_sess_        => id_sess,
           real_payer_     => '30110810100000000003',
           real_receiver_  => '30232810300000010014' /*r.INCOME*/,
           payer_          => '����� ����� ��������',
           receiver_       => '���� �������� ',
           okpo_receiver   => '11000572',
           sum_            => r.summ,
           ground_         => '�������� �� ������ ' || r.SERVICE || ' �� ' ||
                              r.PAYDATE || ';SID:' || id_sess,
           kpp_receiver    => '111000171',
           numb            => num,
           PAYDATE_        => r.PAYDATE,
           payment_numbers => r.checknumbers);
    end loop;
  end;

  /*���� ����������� ����� �� ����������,
  ��������� ��������� � ������� ��� �������*/
  procedure save_doc_if_ost_not_null(v_ret Z_SB_POST_TABLE) is
    Pragma Autonomous_Transaction;
  begin
    delete from Z_SB_POSTDOC_IFSUMNOTNULL;
    commit;
    insert into Z_SB_POSTDOC_IFSUMNOTNULL
      select * from table(v_ret);
    commit;
  end;

  /*������� ������� �������*/
  FUNCTION make(id_sess NUMBER) RETURN CLOB IS
    ret                      CLOB;
    num                      number := 0;
    res                      number;
    pnmb                     varchar2(50);
    sump                     varchar2(50);
    chknmb                   varchar2(50);
    chkdate                  varchar2(50);
    bnk_deal                 number;
    search_termdeal          number;
    search_termdeal_tek_reic number;
    --sum_from_atribute        number;
    bnk_deal_for_chek       number;
    comis_rate              number;
    deal_acc_for_comis      varchar2(50) := null;
    deal_acc_fromterm       varchar2(50) := null;
    d_acc_f_our_tr_in_en_tr varchar2(50);
    sumforclearsum          number := 0;
    sumforclearsum_self     number := 0;
  
    cursor sbra_with_attr(sid varchar2, numb varchar2) is
      with dat as
       (SELECT rownum rn,
               g.AttributeName,
               g.AttributeValue,
               t.recdate,
               t.paydate,
               t.currency,
               t.paymenttype,
               t.vk,
               t.dateofoperation,
               t.dataps,
               t.dateclearing,
               t.dealer,
               t.accountpayer,
               t.cardnumber,
               t.operationnumber,
               t.operationnumberdelivery,
               t.checknumber,
               t.checkparent,
               t.orderofprovidence,
               t.provider,
               t.owninown,
               t.corrected,
               t.commissionrate,
               t.status,
               t.stringfromfile,
               t.rewardamount,
               t.ownerincomeamount,
               t.commissionamount,
               t.nkamount,
               t.maxcommissionamount,
               t.mincommissionamount,
               t.cashamount,
               t.sumnalprimal,
               t.amounttocheck,
               t.amountofpayment,
               t.sumofsplitting,
               t.amountintermediary,
               t.amountofscs,
               t.amountwithchecks,
               t.counter,
               t.terminal,
               t.terminalnetwork,
               t.transactiontype,
               t.service,
               t.filetransactions,
               t.fio,
               t.checksincoming,
               t.barcode,
               t.isaresident,
               t.valuenotfound,
               t.providertariff,
               t.counterchecks,
               t.countercheck,
               t.id_,
               t.detailing,
               t.walletpayer,
               t.walletreceiver,
               t.purposeofpayment,
               t.dataprovider,
               t.statusabs,
               t.sess_id
          FROM z_sb_transact_amra_dbt t,
               XMLTABLE('/��������/���' PASSING xmltype(t.attributes_)
                        COLUMNS Service VARCHAR2(500) PATH '@������',
                        CheckNumber VARCHAR2(500) PATH '@���������',
                        AttributeName VARCHAR2(500) PATH '@�����������',
                        AttributeValue VARCHAR2(500) PATH '@����������������') g
         where t.SESS_ID = sid
           and t.checknumber = numb),
      ATTR_VIEW as
       (select recdate,
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
               statusabs,
               sess_id,
               CHECKNUMBER,
               "CountPay",
               "DetailCheque",
               "HeadCheque",
               "Id_�������",
               "Price",
               "account",
               "��������������",
               "���",
               "����������",
               "����������������������",
               "������������",
               "�������������",
               "�������������",
               "���",
               "�����������������",
               "��������������",
               "���������������������",
               "�����������",
               "������ �����",
               "���������",
               "����������",
               "����������",
               "�������������",
               "�����",
               fio_children,
               period
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
                               '�����' "�����",
                               'fio_children' fio_children,
                               'period' period))),
      aggr_attr as
       (select t1.l,
               CHECKNUMBER,
               "CountPay",
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
               trim((SELECT REGEXP_SUBSTR(fio_children, '[^@~@]+', 1, t1.l)
                      FROM DUAL)) fio_children,
               trim((SELECT REGEXP_SUBSTR(period, '[^@~@]+', 1, t1.l)
                      FROM DUAL)) period,
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
                      FROM DUAL)) "�������������",
               to_number(replace(replace(trim((SELECT REGEXP_SUBSTR("�����",
                                                                   '[^@~@]+',
                                                                   1,
                                                                   t1.l)
                                                FROM DUAL)),
                                         '�',
                                         ''),
                                 '.',
                                 ',')) "�����",
               recdate,
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
               statusabs,
               sess_id
          from ATTR_VIEW t,
               (SELECT LEVEL l FROM DUAL CONNECT BY LEVEL <= 20) t1
         WHERE ((t1.l <= t."CountPay") OR (t1.l = 1)))
      select L,
             /* CHECKNUMBER,*/
             "CountPay" CountPay,
             "DetailCheque" DetailCheque,
             "HeadCheque" HeadCheque,
             "Id_�������" id_payment,
             "Price" Price,
             "account" account_,
             "��������������" bank_poluchatel,
             "���" BIK_,
             "����������" VId_platezha,
             "����������������������" VId_platezha_naimenovanie,
             "������������" data_operacii,
             nvl("�������������", '00000000') INN_platelshik,
             nvl("�������������", '000000000') INN_poluchatel,
             "���" KBK_,
             "�����������������" kpp_bank_poluchat,
             case
               when "��������������" like '%,%' or "��������������" like '%.%' then
                '000000000'
               else
                "��������������"
             end kpp_platelshika,
             "���������������������" ls_poluchatelia,
             "�����������" nomer_stroki,
             "������ �����" nomera_chekov,
             "���������" osnovanie,
             "����������" platelshik,
             "����������" poluchatel,
             "�������������" rasche_chet,
             "�����" summa,
             fio_children,
             period,
             (select ACCOUNT
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) acc_20208,
             (select DEAL_ACC
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) DEAL_ACC,
             (select GENERAL_ACC
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) GENERAL_ACC,
             (select GENERAL_COMIS
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) GENERAL_COMIS,
             (select CLEAR_SUM
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) CLEAR_SUM,
             (select CRASH_ACC
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) CRASH_ACC,
             (select ACCOUNT
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") acc40911,
             (select INN
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") inn,
             (select KBK
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") kbk_payer,
             (select kpp
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") kpp,
             (select ACC_NAME
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") ACC_NAME,
             (select ACC_REC
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") ACC_REC,
             (select OKATO
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") OKATO,
             (select ISMRINN from smr) bank_inn,
             (select CSMRKORACC from smr) bank_cor_acc,
             (select CSMRRCSNAME from smr) bank_cor_bic,
             (select DEPARTMENT
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) DEPARTMENT,
             '"' || "����������������������" || '",' || upper("����������") || ', ' ||
             "���������" ground,
             (select COMISSION
                from z_sb_termserv_amra_dbt h
               where idterm = t.TERMINAL
                 and h.NAME = "����������������������"
                 and h.kbk = "���") comissia,
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
             PAYDATE,
             service
        from aggr_attr t
       order by SUMMA desc;
  
    r_sbra_with_attr sbra_with_attr%rowtype;
  
    cursor sbra_pr is
      select t.*,
             decode(t.transactiontype,
                    '��������������',
                    t.CRASH_ACC,
                    t.GENERAL_ACC) if_dokatka
        from (select (select ACCOUNT
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) acc_20208,
                     (select DEAL_ACC
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) DEAL_ACC,
                     (select GENERAL_ACC
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) GENERAL_ACC,
                     (select GENERAL_COMIS
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) GENERAL_COMIS,
                     (select CLEAR_SUM
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) CLEAR_SUM,
                     (select CRASH_ACC
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) CRASH_ACC,
                     case
                       when service = '������ � ����� (v3)(SB)' then
                        (select ACCOUNT
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'taxName',
                                                                  checknumber)
                            and h.kbk =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KBK',
                                                                  checknumber)
                            and h.inn =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'INNreceiver',
                                                                  checknumber)
                            and h.kpp =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KPPreceiver',
                                                                  checknumber))
                       else
                        (select ACCOUNT
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME = t.service)
                     end acc40911,
                     case
                       when service = '������ � ����� (v3)(SB)' then
                        (select INN
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'taxName',
                                                                  checknumber)
                            and h.kbk =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KBK',
                                                                  checknumber)
                            and h.inn =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'INNreceiver',
                                                                  checknumber)
                            and h.kpp =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KPPreceiver',
                                                                  checknumber)
                         
                         )
                       else
                        (select INN
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME = t.service)
                     end inn,
                     case
                       when t.service = '���.������(��������)' then
                        case
                          when length(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                        'account',
                                                                        checknumber)) < 20 then
                           '01211501071010000140'
                          else
                           (select KBK
                              from z_sb_termserv_amra_dbt h
                             where idterm = t.TERMINAL
                               and h.NAME = t.service)
                        end
                       else
                        case
                          when service = '������ � ����� (v3)(SB)' then
                           (select KBK
                              from z_sb_termserv_amra_dbt h
                             where idterm = t.TERMINAL
                               and h.NAME =
                                   z_sb_create_tr_amra.get_attribure(sess_id,
                                                                     'taxName',
                                                                     checknumber)
                               and h.kbk =
                                   z_sb_create_tr_amra.get_attribure(sess_id,
                                                                     'KBK',
                                                                     checknumber)
                               and h.inn =
                                   z_sb_create_tr_amra.get_attribure(sess_id,
                                                                     'INNreceiver',
                                                                     checknumber)
                               and h.kpp =
                                   z_sb_create_tr_amra.get_attribure(sess_id,
                                                                     'KPPreceiver',
                                                                     checknumber)
                            
                            )
                          else
                           (select KBK
                              from z_sb_termserv_amra_dbt h
                             where idterm = t.TERMINAL
                               and h.NAME = t.service)
                        end
                     end kbk_payer,
                     case
                       when service = '������ � ����� (v3)(SB)' then
                        (select kpp
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'taxName',
                                                                  checknumber)
                            and h.kbk =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KBK',
                                                                  checknumber)
                            and h.inn =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'INNreceiver',
                                                                  checknumber)
                            and h.kpp =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KPPreceiver',
                                                                  checknumber)
                         
                         )
                       else
                        (select kpp
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME = t.service)
                     end kpp,
                     case
                       when service = '������ � ����� (v3)(SB)' then
                        (select ACC_NAME
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'taxName',
                                                                  checknumber)
                            and h.kbk =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KBK',
                                                                  checknumber)
                            and h.inn =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'INNreceiver',
                                                                  checknumber)
                            and h.kpp =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KPPreceiver',
                                                                  checknumber)
                         
                         )
                       else
                        (select ACC_NAME
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME = t.service)
                     end ACC_NAME,
                     case
                       when service = '������ � ����� (v3)(SB)' then
                        (select ACC_REC
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'taxName',
                                                                  checknumber)
                            and h.kbk =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KBK',
                                                                  checknumber)
                            and h.inn =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'INNreceiver',
                                                                  checknumber)
                            and h.kpp =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KPPreceiver',
                                                                  checknumber)
                         
                         )
                       else
                        (select ACC_REC
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME = t.service)
                     end ACC_REC,
                     case
                       when service = '������ � ����� (v3)(SB)' then
                        (select OKATO
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'taxName',
                                                                  checknumber)
                            and h.kbk =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KBK',
                                                                  checknumber)
                            and h.inn =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'INNreceiver',
                                                                  checknumber)
                            and h.kpp =
                                z_sb_create_tr_amra.get_attribure(sess_id,
                                                                  'KPPreceiver',
                                                                  checknumber)
                         
                         )
                       else
                        (select OKATO
                           from z_sb_termserv_amra_dbt h
                          where idterm = t.TERMINAL
                            and h.NAME = t.service)
                     end OKATO,
                     (select ISMRINN from smr) bank_inn,
                     (select CSMRKORACC from smr) bank_cor_acc,
                     (select CSMRRCSNAME from smr) bank_cor_bic,
                     (select DEPARTMENT
                        from Z_SB_TERMINAL_AMRA_DBT
                       where NAME = t.TERMINAL) DEPARTMENT,
                     /*���������*/
                     case
                       when SERVICE = '���.��� ��������' then
                        '���.��� - ' ||
                        upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                'ds_name',
                                                                checknumber)) || ' ' ||
                        upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                'fio_children',
                                                                checknumber)) ||
                        ' �� ' ||
                        upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                'period',
                                                                checknumber)) ||
                        ' TR:' || t.checknumber
                       when SERVICE = '���.������(��������)' then
                        upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                'FIO',
                                                                checknumber)) ||
                        ' ������������� ' ||
                        upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                'account',
                                                                checknumber)) ||
                        ' TR:' || t.checknumber
                       when SERVICE = '������� ��(����)' then
                        UPPER(z_sb_create_tr_amra.get_attribure2(sess_id,
                                                                 '����������',
                                                                 checknumber)) ||
                        ' TR:' || t.checknumber
                       when SERVICE = '������ � ����� (v3)(SB)' then
                         SERVICE || '" - ' ||
                        UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                'taxPayer',
                                                                checknumber)) ||
                        ' TR:' || t.checknumber
                     end ground,
                     UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                             'account',
                                                             checknumber)) inn_nalog,
                     UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                             'KPP',
                                                             checknumber)) kpp_nalog,
                     /*-------------------------------------------------*/
                     decode(UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                    'taxName',
                                                                    checknumber)),
                            '����������� ����� �� ��������� ���� ������������',
                            '����. �����',
                            UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                    'taxName',
                                                                    checknumber)
                                  
                                  )) || ' ' ||
                     UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                             'taxPayer',
                                                             checknumber)) ||
                     ' ��' || UPPER(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                      'period',
                                                                      checknumber)
                                    
                                    ) ground_40911,
                     /*-------------------------------------------------*/
                     nvl(decode(SERVICE,
                                '���.��� ��������',
                                upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                        'fio_children',
                                                                        checknumber)
                                      
                                      ),
                                '���.������(��������)',
                                upper(z_sb_create_tr_amra.get_attribure(sess_id,
                                                                        'FIO',
                                                                        checknumber)
                                      
                                      ),
                                '������� ��(����)',
                                UPPER(z_sb_create_tr_amra.get_attribure2(sess_id,
                                                                         '����������',
                                                                         checknumber)),
                                '������ � ����� (v3)(SB)',
                                UPPER(
                                      
                                      z_sb_create_tr_amra.get_attribure(sess_id,
                                                                        'taxPayer',
                                                                        checknumber))),
                         '�������� ������� (���)') FIO,
                     t.CHECKNUMBER,
                     t.AMOUNTOFPAYMENT,
                     t.CASHAMOUNT,
                     t.TERMINAL idterm,
                     t.COMMISSIONAMOUNT,
                     t.NKAMOUNT,
                     t.AMOUNTTOCHECK,
                     t.transactiontype,
                     t.terminalnetwork,
                     t.SESS_ID,
                     t.STATUS,
                     SUMNALPRIMAL,
                     AMOUNTWITHCHECKS,
                     CHECKSINCOMING,
                     /*trunc(PAYDATE)*/
                     PAYDATE,
                     service,
                     Attributes_
                from Z_SB_TRANSACT_AMRA_DBT t
               where t.SESS_ID = id_sess
                    /*and trunc(PAYDATE) = input_date*/
                 and t.STATUSABS = 0
                 and PROVIDER = '��������') t
      
      /*and t.checknumber = input_number*/
      /*and t.TERMINAL in (select NAME from Z_SB_TERMINAL_AMRA_DBT)*/
      ;
  
    cursor amra_pr is
      select (select ACCOUNT
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) acc_20208,
             (select DEAL_ACC
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) DEAL_ACC,
             (select CLEAR_SUM
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) CLEAR_SUM,
             (select GENERAL_ACC
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) GENERAL_ACC,
             (select GENERAL_COMIS
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) GENERAL_COMIS,
             (select CRASH_ACC
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) CRASH_ACC,
             (select DEPARTMENT
                from Z_SB_TERMINAL_AMRA_DBT
               where NAME = t.TERMINAL) DEPARTMENT,
             '������ �� ������ ' || SERVICE || ' �� ' ||
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
             t.terminal,
             SUMNALPRIMAL,
             AMOUNTWITHCHECKS,
             t.terminalnetwork,
             t.transactiontype,
             CHECKSINCOMING,
             /*trunc(PAYDATE)*/
             PAYDATE
        from Z_SB_TRANSACT_AMRA_DBT t
       where t.SESS_ID = id_sess
         and t.STATUSABS = 0
         and PROVIDER <> '��������'
      --and t.checknumber = input_number
      --and trunc(PAYDATE) = input_date
      /*and t.TERMINAL in (select NAME from Z_SB_TERMINAL_AMRA_DBT)*/
      ;
  
    cursor acc_ost is
      with dat as
       (select distinct ACC, replace(col, '_DEPARTMENT', '') acc_name, otd
          from (select general_acc, general_comis, clear_sum, DEPARTMENT
                  from Z_SB_TERMINAL_AMRA_DBT) UNPIVOT((acc, otd) FOR col IN((general_acc,
                                                                              DEPARTMENT),
                                                                             (general_comis,
                                                                              DEPARTMENT),
                                                                             (clear_sum,
                                                                              DEPARTMENT))))
      select util_dm2.ACC_Ostt(0,
                               ACC,
                               decode(substr(ACC, 6, 3),
                                      '810',
                                      'RUR',
                                      '840',
                                      'USD',
                                      '978',
                                      'EUR'),
                               trunc(sysdate) + 1,
                               'V',
                               2) ostt,
             t.*
        from dat t
      union all
      select util_dm2.ACC_Ostt(0,
                               '30232810300000010014',
                               'RUR',
                               trunc(sysdate) + 1,
                               'V',
                               2) ostt,
             '30232810300000010014',
             'AGGR_COMISS',
             0 otd
        from dual
      union all
      select util_dm2.ACC_Ostt(0,
                               '30232810000000010013',
                               'RUR',
                               trunc(sysdate) + 1,
                               'V',
                               2) ostt,
             '30232810000000010013',
             'AGGR_COMISS',
             0 otd
        from dual
      union all
      select util_dm2.ACC_Ostt(0,
                               '30232810700000010012',
                               'RUR',
                               trunc(sysdate) + 1,
                               'V',
                               2) ostt,
             '30232810700000010012',
             'AGGR_COMISS',
             0 otd
        from dual;
  
    file_status number := null;
  BEGIN
    begin
    
      select t.STATUS
        into file_status
        from xxi.z_sb_fn_sess_amra t
       where t.SESS_ID = id_sess;
    
      if file_status = 1 then
        DBMS_LOB.createtemporary(ret, TRUE);
        /*
        |--------------------------------------------------------------------------------|
        |                                ������ SBRA                                     |
        |--------------------------------------------------------------------------------|
        */
        for r in sbra_pr loop
          if r.STATUS = '00' then
            /*
            1 
            ���� ������������ �� ����� 0
            20208/GENERAL_ACC
            */
            if r.CASHAMOUNT <> 0 and r.transactiontype <> '��������������' and
               r.terminalnetwork = '��������' then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.acc_20208,
                   real_receiver_ => r.GENERAL_ACC,
                   payer_         => '���� ��������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   receiver_      => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.CASHAMOUNT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            /*
            ������� �����
            GENERAL_ACC/30232810700000010009
            */
            if r.AMOUNTTOCHECK <> 0 and r.terminalnetwork = '��������' then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.GENERAL_ACC,
                   real_receiver_ => r.DEAL_ACC,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ����� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTTOCHECK,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            
            end if;
            /*������� �����*/
          
            /*4 ���� ������������� �� ����� 0*/
          
            /*�������� ����� �������� �� ������ � ��������� (���� �����������)*/
            /*            if r.service = '������� ��(����)' then
              OPEN sbra_with_attr(r.SESS_ID, r.CHECKNUMBER);
              LOOP
                FETCH sbra_with_attr
                  INTO r_sbra_with_attr;
                EXIT WHEN sbra_with_attr%notfound;
                \*dbms_output.put_line(r_sbra_with_attr.VID_PLATEZHA_NAIMENOVANIE);*\
                comis_rate := r_sbra_with_attr.comissia;
              end loop;
              CLOSE sbra_with_attr;
            elsif r.service <> '������ � ����� (v3)(SB)' then
              select COMISSION
                into comis_rate
                from z_sb_termserv_amra_dbt t
               where t.IDTERM = r.idterm
                 and t.NAME = r.service;
            elsif r.service = '������ � ����� (v3)(SB)' then
              comis_rate := r.COMMISSIONAMOUNT;
            end if;*/
            comis_rate := r.COMMISSIONAMOUNT;
            /*�������� ����� �������� �� ������ � ��������� (���� �����������)*/
          
            /*SELECT case
                       when r.service = '������� ��(����)' then
                        nvl(mod(sum(to_number(replace(replace(ATTRIBUTEVALUE,
                                                              '�',
                                                              ''),
                                                      '.',
                                                      ','))),
                                1),
                            0)
                       else
                        0
                     end
                into sum_from_atribute
                FROM XMLTABLE('/��������/���' PASSING xmltype(r.Attributes_)
                              COLUMNS Service VARCHAR2(1000) PATH '@������',
                              CheckNumber VARCHAR2(1000) PATH '@���������',
                              AttributeName VARCHAR2(1000) PATH '@�����������',
                              AttributeValue VARCHAR2(1000) PATH
                              '@����������������')
               where lower(ATTRIBUTENAME) = lower('�����');
            */
            /*���� ����� ��������*/
            if comis_rate <> 0 and r.terminalnetwork = '��������' and
               r.transactiontype = '��������������' then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.CRASH_ACC,
                   real_receiver_ => r.GENERAL_COMIS,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ����� �������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => comis_rate /*- sum_from_atribute*/,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            elsif comis_rate = 0 then
              writelog(r.PAYDATE,
                       '��������! ��� CheckNumber = ' || r.CheckNumber ||
                       ' ����� � ' || chknmb || ' ����������� ' || pnmb ||
                       ' ����� ' || sump || ' ���� ' || chkdate ||
                       'BLOCK:333',
                       id_sess);
            end if;
          
            if comis_rate <> 0 and r.terminalnetwork = '��������' and
               r.transactiontype <> '��������������' then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.GENERAL_ACC,
                   real_receiver_ => r.GENERAL_COMIS,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ����� �������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => comis_rate /*- sum_from_atribute*/,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            elsif comis_rate = 0 then
              writelog(r.PAYDATE,
                       '��������! ��� CheckNumber = ' || r.CheckNumber ||
                       ' ����� � ' || chknmb || ' ����������� ' || pnmb ||
                       ' ����� ' || sump || ' ���� ' || chkdate ||
                       'BLOCK:3',
                       id_sess);
            end if;
          
            /*
            5 
            ���� ������������ �� ����� 0
            GENERAL_ACC/acc40911
            */
          
            /*if r.service = '������� ��(����)' then
              OPEN sbra_with_attr(r.SESS_ID, r.CHECKNUMBER);
              LOOP
                FETCH sbra_with_attr
                  INTO r_sbra_with_attr;
                EXIT WHEN sbra_with_attr%notfound;
                \*dbms_output.put_line(r_sbra_with_attr.VID_PLATEZHA_NAIMENOVANIE);*\
                comis_rate := r_sbra_with_attr.comissia;
              end loop;
              CLOSE sbra_with_attr;
            elsif r.service <> '������ � ����� (v3)(SB)' then
              select COMISSION
                into comis_rate
                from z_sb_termserv_amra_dbt t
               where t.IDTERM = r.idterm
                 and t.NAME = r.service;
            elsif r.service = '������ � ����� (v3)(SB)' then
              comis_rate := r.COMMISSIONAMOUNT;
            end if;*/
          
            comis_rate := r.COMMISSIONAMOUNT;
          
            if r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS = 0 and
               r.terminalnetwork = '��������' then
              --���������� ����� ������ � ����� 
              if r.service = '������� ��(����)' then
                OPEN sbra_with_attr(r.SESS_ID, r.CHECKNUMBER);
                LOOP
                  FETCH sbra_with_attr
                    INTO r_sbra_with_attr;
                  EXIT WHEN sbra_with_attr%notfound or r_sbra_with_attr.SUMMA = 0;
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => r.if_dokatka,
                       real_receiver_ => r_sbra_with_attr.acc40911,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���������� ����  ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r_sbra_with_attr.SUMMA,
                       ground_        => r_sbra_with_attr.ground || ' TR:' ||
                                         r.CHECKNUMBER,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                end loop;
                CLOSE sbra_with_attr;
              else
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.if_dokatka,
                     real_receiver_ => r.acc40911,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���������� ����  ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => (r.Cashamount + r.AMOUNTWITHCHECKS) -
                                       comis_rate -
                                       to_number(replace(replace(r.AMOUNTTOCHECK,
                                                                 '.',
                                                                 ','),
                                                         '�',
                                                         '')),
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              end if;
            elsif r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS <> 0 and
                  r.terminalnetwork = '��������' then
              if r.service = '������� ��(����)' then
                OPEN sbra_with_attr(r.SESS_ID, r.CHECKNUMBER);
                LOOP
                  FETCH sbra_with_attr
                    INTO r_sbra_with_attr;
                  EXIT WHEN sbra_with_attr%notfound or r_sbra_with_attr.SUMMA = 0;
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => r.if_dokatka,
                       real_receiver_ => r_sbra_with_attr.acc40911,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���������� ����  ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r_sbra_with_attr.SUMMA,
                       ground_        => r_sbra_with_attr.ground || ' TR:' ||
                                         r.CHECKNUMBER,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                end loop;
                CLOSE sbra_with_attr;
              else
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.if_dokatka,
                     real_receiver_ => r.acc40911,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���������� ����  ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => (r.Cashamount + r.AMOUNTWITHCHECKS) -
                                       comis_rate -
                                       to_number(replace(replace(r.AMOUNTTOCHECK,
                                                                 '.',
                                                                 ','),
                                                         '�',
                                                         '')),
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              end if;
            end if;
            /*
            6 
            acc40911/ACC_REC
            */
          
            if r.AMOUNTOFPAYMENT <> 0 and r.kbk_payer is not null and
               r.terminalnetwork = '��������' and
               r.service not in ('������ � ����� (v3)(SB)',
                                 '������� ��(����)') then
            
              select COMISSION
                into comis_rate
                from z_sb_termserv_amra_dbt t
               where t.IDTERM = r.idterm
                 and t.NAME = r.service;
            
              num := num + 1;
              POST(id_sess_        => r.SESS_ID,
                   department_     => r.department,
                   real_payer_     => r.acc40911,
                   real_receiver_  => r.ACC_REC,
                   payer_          => '���������� ���� ' || r.idterm ||
                                      ' ��������� ' || r.department,
                   receiver_       => r.acc_name,
                   okpo_receiver   => /*r.bank_inn*/ '00000000',
                   sum_            => (r.Cashamount + r.AMOUNTWITHCHECKS) -
                                      comis_rate -
                                      to_number(replace(replace(r.AMOUNTTOCHECK,
                                                                '.',
                                                                ','),
                                                        '�',
                                                        '')),
                   ground_         => r.ground,
                   payment_number  => r.CHECKNUMBER,
                   kpp_receiver    => r.kpp,
                   numb            => num,
                   PAYDATE_        => r.PAYDATE,
                   bo1_            => '4',
                   bo2_            => '0',
                   okpo_payer      => r.inn,
                   coracc_payer    => '30102810900000000017',
                   mfo_receiver    => r.bank_cor_bic,
                   bank_receiver   => '���� �������',
                   BUDJCLASSIFCODE => r.kbk_payer,
                   okato           => r.OKATO,
                   for_nbra        => true,
                   PLATELSHIK1     => r.FIO,
                   KPP_payer       => '000000000');
            elsif r.AMOUNTOFPAYMENT <> 0 and r.kbk_payer is not null and
                  r.terminalnetwork = '��������' and
                  r.service = '������ � ����� (v3)(SB)' then
            
              comis_rate := r.COMMISSIONAMOUNT;
            
              num := num + 1;
              POST(id_sess_        => r.SESS_ID,
                   department_     => r.department,
                   real_payer_     => r.acc40911,
                   real_receiver_  => r.ACC_REC,
                   payer_          => '���������� ���� ' || r.idterm ||
                                      ' ��������� ' || r.department,
                   receiver_       => r.acc_name,
                   okpo_receiver   => nvl(r.INN_NALOG, '00000000'),
                   sum_            => (r.Cashamount + r.AMOUNTWITHCHECKS) -
                                      comis_rate -
                                      to_number(replace(replace(r.AMOUNTTOCHECK,
                                                                '.',
                                                                ','),
                                                        '�',
                                                        '')),
                   ground_         => r.ground_40911,
                   payment_number  => r.CHECKNUMBER,
                   kpp_receiver    => r.kpp,
                   numb            => num,
                   PAYDATE_        => r.PAYDATE,
                   bo1_            => '4',
                   bo2_            => '0',
                   okpo_payer      => r.inn,
                   coracc_payer    => '30102810900000000017',
                   mfo_receiver    => r.bank_cor_bic,
                   bank_receiver   => '���� �������',
                   BUDJCLASSIFCODE => r.kbk_payer,
                   okato           => r.OKATO,
                   for_nbra        => true,
                   PLATELSHIK1     => r.FIO,
                   KPP_payer       => nvl(r.KPP_NALOG, '000000000'));
            elsif r.AMOUNTOFPAYMENT <> 0 and r.service = '������� ��(����)' and
                  r.terminalnetwork = '��������' then
              OPEN sbra_with_attr(r.SESS_ID, r.CHECKNUMBER);
              LOOP
                FETCH sbra_with_attr
                  INTO r_sbra_with_attr;
                EXIT WHEN sbra_with_attr%notfound or r_sbra_with_attr.SUMMA = 0;
              
                if r_sbra_with_attr.KBK_PAYER is not null then
                  num := num + 1;
                  POST(id_sess_        => r.SESS_ID,
                       department_     => r.department,
                       real_payer_     => r_sbra_with_attr.acc40911,
                       real_receiver_  => r_sbra_with_attr.ACC_REC,
                       payer_          => '���������� ���� ' || r.idterm ||
                                          ' ��������� ' || r.department,
                       receiver_       => r_sbra_with_attr.acc_name,
                       okpo_receiver   => r_sbra_with_attr.INN_platelshik /*bank_inn*/,
                       sum_            => r_sbra_with_attr.SUMMA,
                       ground_         => r_sbra_with_attr.ground,
                       payment_number  => r.CHECKNUMBER,
                       kpp_receiver    => r_sbra_with_attr.kpp,
                       numb            => num,
                       PAYDATE_        => r.PAYDATE,
                       bo1_            => '4',
                       bo2_            => '0',
                       okpo_payer      => r_sbra_with_attr.inn,
                       coracc_payer    => '30102810900000000017',
                       mfo_receiver    => r_sbra_with_attr.bank_cor_bic,
                       bank_receiver   => '���� �������',
                       BUDJCLASSIFCODE => r_sbra_with_attr.kbk_payer,
                       okato           => r_sbra_with_attr.OKATO,
                       for_nbra        => true,
                       PLATELSHIK1     => r_sbra_with_attr.PLATELSHIK,
                       KPP_payer       => r_sbra_with_attr.KPP_PLATELSHIKA);
                end if;
              end loop;
              CLOSE sbra_with_attr;
            end if;
            /*r.bank_cor_acc,CORACC_PAYER_2  => '30102810900000000017'*/
            /*
            ���� ���������� �� ����� 0    
            30232810700000010009/GENERAL_ACC
            30110810100000000003/30232810700000010012
            */
            /*������ �� �����
            ���� ����� � ���� �� ����� 0*/
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
                
                  /*ID ��������� �� ������� �������� �����*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into pnmb
                    From dual
                   where level = 3
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*ID ��������� �� ������� �������� �����*/
                
                  /*����� �����*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into sump
                    From dual
                   where level = 2
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*����� �����*/
                
                  /*����� ���� �����*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chknmb
                    From dual
                   where level = 1
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*����� ���� �����*/
                
                  /*���� ���� �����*/
                  Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                    into chkdate
                    From dual
                   where level = 4
                  Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                  /*���� ���� �����*/
                
                  /*��� ��� ��� ��������*/
                  select count(*)
                    into bnk_deal
                    from Z_SB_TERMINAL_AMRA_DBT t
                   where t.name = pnmb;
                  /*��� ��� ��� ��������*/
                
                  if bnk_deal > 0 then
                    /*����� �����*/
                    begin
                      select DEAL_ACC
                        into deal_acc_fromterm
                        from z_sb_termdeal_amra_dbt deal,
                             z_sb_transact_amra_dbt tr,
                             z_sb_postdoc_amra_dbt  doc,
                             z_sb_terminal_amra_dbt term,
                             trn
                       where deal.paymentnumber = tr.checknumber
                         and doc.paymentnumbers like tr.checknumber
                         and trn.ITRNNUM = doc.kindpayment
                         and term.name = tr.terminal
                         and trn.CTRNACCC = term.deal_acc
                         and tr.checknumber = chknmb;
                    exception
                      when others then
                        begin
                          select DEAL_ACC
                            into deal_acc_fromterm
                            from z_sb_termdeal_amra_dbt deal,
                                 z_sb_transact_amra_dbt tr,
                                 z_sb_terminal_amra_dbt term
                           where deal.paymentnumber = tr.checknumber
                             and term.name = tr.terminal
                             and tr.checknumber = chknmb
                             and tr.sess_id = r.SESS_ID;
                        exception
                          when others then
                            writelog(r.PAYDATE,
                                     '��������! ��� CheckNumber = ' ||
                                     r.CheckNumber || ' ����� � ' || chknmb ||
                                     ' ����������� ' || pnmb || ' ����� ' || sump ||
                                     ' ���� ' || chkdate ||
                                     ' BLOCK:4568_6 self ->',
                                     id_sess);
                        end;
                    end;
                  
                    select count(*)
                      into search_termdeal
                      from z_sb_termdeal_amra_dbt deal,
                           z_sb_transact_amra_dbt tr,
                           z_sb_postdoc_amra_dbt  doc,
                           z_sb_terminal_amra_dbt term,
                           trn
                     where deal.paymentnumber = tr.checknumber
                       and doc.paymentnumbers like tr.checknumber
                       and trn.ITRNNUM = doc.kindpayment
                       and term.name = tr.terminal
                       and trn.CTRNACCC = term.deal_acc
                       and tr.checknumber = chknmb;
                  
                    /*���� � ��� �����*/
                    select count(*)
                      into search_termdeal_tek_reic
                      from z_sb_termdeal_amra_dbt deal,
                           z_sb_transact_amra_dbt tr,
                           z_sb_terminal_amra_dbt term
                     where deal.paymentnumber = tr.checknumber
                       and term.name = tr.terminal
                       and tr.checknumber = chknmb
                       and tr.sess_id = r.SESS_ID;
                  
                    /*����� �����*/
                    if search_termdeal > 0 then
                      num := num + 1;
                      POST(id_sess_       => r.SESS_ID,
                           department_    => r.department,
                           real_payer_    => deal_acc_fromterm /*r.DEAL_ACC*/,
                           real_receiver_ => r.GENERAL_ACC,
                           payer_         => '���� ����� ����� ���������� ' ||
                                             r.idterm || ' ��������� ' ||
                                             r.department,
                           receiver_      => '���� ����� ' || r.idterm ||
                                             ' ��������� ' || r.department,
                           okpo_receiver  => '11000572',
                           sum_           => to_number(replace(replace(sump,
                                                                       '.',
                                                                       ','),
                                                               '�',
                                                               '')),
                           ground_        => r.ground || ' �����:' || sump ||
                                             '_�:' || num,
                           payment_number => r.CHECKNUMBER,
                           kpp_receiver   => '111000171',
                           numb           => num,
                           PAYDATE_       => r.PAYDATE,
                           chk_           => r.CHECKSINCOMING);
                    elsif search_termdeal = 0 then
                      if search_termdeal_tek_reic > 0 then
                        num := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => deal_acc_fromterm /*r.DEAL_ACC*/,
                             real_receiver_ => r.GENERAL_ACC,
                             payer_         => '���� ����� ����� ���������� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             receiver_      => '���� ����� ' || r.idterm ||
                                               ' ��������� ' || r.department,
                             okpo_receiver  => '11000572',
                             sum_           => to_number(replace(replace(sump,
                                                                         '.',
                                                                         ','),
                                                                 '�',
                                                                 '')),
                             ground_        => r.ground || ' �����:' || sump ||
                                               '_�:' || num,
                             payment_number => r.CHECKNUMBER,
                             kpp_receiver   => '111000171',
                             numb           => num,
                             PAYDATE_       => r.PAYDATE,
                             chk_           => r.CHECKSINCOMING);
                      else
                        writelog(r.PAYDATE,
                                 '��������! ��� CheckNumber = ' ||
                                 r.CheckNumber || ' ����� � ' || chknmb ||
                                 ' ����������� ' || pnmb || ' ����� ' || sump ||
                                 ' ���� ' || chkdate || ' BLOCK:1',
                                 id_sess);
                      end if;
                    
                    end if;
                  elsif bnk_deal = 0 then
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => '30110810100000000003',
                         real_receiver_ => '30232810700000010012',
                         payer_         => '���� ����� ����� ���������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         receiver_      => '���� ������ �� ����� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => to_number(replace(replace(sump,
                                                                     '.',
                                                                     ','),
                                                             '�',
                                                             '')),
                         ground_        => r.ground || ' �����:' || sump ||
                                           '_�:' || num,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE,
                         chk_           => r.CHECKSINCOMING);
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => '30232810700000010012',
                         real_receiver_ => r.GENERAL_ACC,
                         payer_         => '���� ����� ����� ���������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         receiver_      => '���� ������ �� ����� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => to_number(replace(replace(sump,
                                                                     '.',
                                                                     ','),
                                                             '�',
                                                             '')),
                         ground_        => r.ground || ' �����:' || sump ||
                                           '_�:' || num,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE,
                         chk_           => r.CHECKSINCOMING);
                  end if;
                end loop;
              end if;
            end if;
          elsif r.STATUS != '00' and r.terminalnetwork = '��������' and
                r.transactiontype <> '��������������' then
            /*
            2 
            
            ���� ��������� �����
            
            acc_20208/GENERAL_ACC
            
            GENERAL_ACC/30232810400000010008
            
            GENERAL_ACC/30232810700000010009
            
            */
            if r.CASHAMOUNT <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.acc_20208,
                   real_receiver_ => r.GENERAL_ACC,
                   payer_         => '���� ��������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   receiver_      => '���� ���������� ������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.CASHAMOUNT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.GENERAL_ACC,
                   real_receiver_ => r.CRASH_ACC,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ���������� ������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.cashamount,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            /*���� �� ��������� ������� ���� �����*/
            if r.AMOUNTTOCHECK <> 0 /*and r.CASHAMOUNT >= r.AMOUNTTOCHECK*/
             then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.CRASH_ACC,
                   real_receiver_ => r.DEAL_ACC,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ������ �� �����' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTTOCHECK,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            elsif r.AMOUNTTOCHECK <> 0 and r.CASHAMOUNT < r.AMOUNTTOCHECK then
              writelog(r.PAYDATE,
                       '��������! �� ������� �� ����� � �������. ��� CheckNumber = ' ||
                       r.CheckNumber,
                       id_sess);
            end if;
          
            if r.AMOUNTWITHCHECKS <> 0 then
              if r.CHECKSINCOMING is not null then
                /*��� ��� ��� ��������*/
                select count(*)
                  into bnk_deal_for_chek
                  from Z_SB_TERMINAL_AMRA_DBT t
                 where t.name = r.idterm;
                /*���� ��� ��������*/
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
                      /*ID ��������� �� ������� �������� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into pnmb
                        From dual
                       where level = 3
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into sump
                        From dual
                       where level = 2
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chknmb
                        From dual
                       where level = 1
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*���� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chkdate
                        From dual
                       where level = 4
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*��� ��� ��� ��������*/
                      select count(*)
                        into bnk_deal
                        from Z_SB_TERMINAL_AMRA_DBT t
                       where t.name = pnmb;
                      /*���� ����� ������ ���������*/
                      if bnk_deal > 0 then
                        /*����� �����*/
                        begin
                          select DEAL_ACC
                            into deal_acc_fromterm
                            from z_sb_termdeal_amra_dbt deal,
                                 z_sb_transact_amra_dbt tr,
                                 z_sb_postdoc_amra_dbt  doc,
                                 z_sb_terminal_amra_dbt term,
                                 trn
                           where deal.paymentnumber = tr.checknumber
                             and doc.paymentnumbers like tr.checknumber
                             and trn.ITRNNUM = doc.kindpayment
                             and term.name = tr.terminal
                             and trn.CTRNACCC = term.deal_acc
                             and tr.checknumber = chknmb;
                        exception
                          when others then
                            begin
                              select DEAL_ACC
                                into deal_acc_fromterm
                                from z_sb_termdeal_amra_dbt deal,
                                     z_sb_transact_amra_dbt tr,
                                     z_sb_terminal_amra_dbt term
                               where deal.paymentnumber = tr.checknumber
                                 and term.name = tr.terminal
                                 and tr.checknumber = chknmb
                                 and tr.sess_id = r.SESS_ID;
                            exception
                              when others then
                                writelog(r.PAYDATE,
                                         '��������! ��� CheckNumber = ' ||
                                         r.CheckNumber || ' ����� � ' ||
                                         chknmb || ' ����������� ' || pnmb ||
                                         ' ����� ' || sump || ' ���� ' ||
                                         chkdate || ' BLOCK:4568_5 self ->',
                                         id_sess);
                            end;
                        end;
                      
                        select count(*)
                          into search_termdeal
                          from z_sb_termdeal_amra_dbt deal,
                               z_sb_transact_amra_dbt tr,
                               z_sb_postdoc_amra_dbt  doc,
                               z_sb_terminal_amra_dbt term,
                               trn
                         where deal.paymentnumber = tr.checknumber
                           and doc.paymentnumbers like tr.checknumber
                           and trn.ITRNNUM = doc.kindpayment
                           and term.name = tr.terminal
                           and trn.CTRNACCC = term.deal_acc
                           and tr.checknumber = chknmb;
                      
                        /*��� ����*/
                        select DEAL_ACC
                          into search_termdeal_tek_reic
                          from z_sb_termdeal_amra_dbt deal,
                               z_sb_transact_amra_dbt tr,
                               z_sb_terminal_amra_dbt term
                         where deal.paymentnumber = tr.checknumber
                           and term.name = tr.terminal
                           and tr.checknumber = chknmb
                           and tr.sess_id = r.SESS_ID;
                      
                        /*���� ����� �����*/
                        if search_termdeal > 0 then
                          num := num + 1;
                          POST(id_sess_       => r.SESS_ID,
                               department_    => r.department,
                               real_payer_    => deal_acc_fromterm /*r.DEAL_ACC*/,
                               real_receiver_ => r.CRASH_ACC,
                               payer_         => '���� ����� ����� ���������� ' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               receiver_      => '���� ������ �� �����' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               okpo_receiver  => '11000572',
                               sum_           => to_number(replace(replace(sump,
                                                                           '.',
                                                                           ','),
                                                                   '�',
                                                                   '')),
                               ground_        => r.ground,
                               payment_number => r.CHECKNUMBER,
                               kpp_receiver   => '111000171',
                               numb           => num,
                               PAYDATE_       => r.PAYDATE);
                        elsif search_termdeal = 0 then
                          if search_termdeal_tek_reic > 0 then
                            num := num + 1;
                            POST(id_sess_       => r.SESS_ID,
                                 department_    => r.department,
                                 real_payer_    => deal_acc_fromterm /*r.DEAL_ACC*/,
                                 real_receiver_ => r.CRASH_ACC,
                                 payer_         => '���� ����� ����� ���������� ' ||
                                                   r.idterm || ' ��������� ' ||
                                                   r.department,
                                 receiver_      => '���� ������ �� �����' ||
                                                   r.idterm || ' ��������� ' ||
                                                   r.department,
                                 okpo_receiver  => '11000572',
                                 sum_           => to_number(replace(replace(sump,
                                                                             '.',
                                                                             ','),
                                                                     '�',
                                                                     '')),
                                 ground_        => r.ground,
                                 payment_number => r.CHECKNUMBER,
                                 kpp_receiver   => '111000171',
                                 numb           => num,
                                 PAYDATE_       => r.PAYDATE);
                          else
                            writelog(r.PAYDATE,
                                     '��������! ��� CheckNumber = ' ||
                                     r.CheckNumber || ' ����� � ' || chknmb ||
                                     ' ����������� ' || pnmb || ' ����� ' || sump ||
                                     ' ���� ' || chkdate || ' BLOCK:4_2',
                                     id_sess);
                          end if;
                        end if;
                      elsif bnk_deal = 0 then
                        num := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => '30110810100000000003',
                             real_receiver_ => r.CRASH_ACC,
                             payer_         => '���� ����� ����� ���������� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             receiver_      => '���� ������ �� ����� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             okpo_receiver  => '11000572',
                             sum_           => to_number(replace(replace(sump,
                                                                         '.',
                                                                         ','),
                                                                 '�',
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
                elsif bnk_deal_for_chek = 0 then
                  /*���� �� ��� ��������*/
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
                        into pnmb
                        From dual
                       where level = 3
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into sump
                        From dual
                       where level = 2
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chknmb
                        From dual
                       where level = 1
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*���� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chkdate
                        From dual
                       where level = 4
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*��� ��� ��� ��������*/
                      select count(*)
                        into bnk_deal
                        from Z_SB_TERMINAL_AMRA_DBT t
                       where t.name = pnmb;
                      /*���� ����� ������ ���������*/
                      if bnk_deal > 0 then
                        /*����� �����*/
                        select count(*)
                          into search_termdeal
                          from z_sb_termdeal_amra_dbt t
                         where t.paymentnumber = chknmb;
                        /*���� ����� �����*/
                        if search_termdeal > 0 then
                          begin
                            select DEAL_ACC
                              into d_acc_f_our_tr_in_en_tr
                              from z_sb_termdeal_amra_dbt deal,
                                   z_sb_transact_amra_dbt tr,
                                   z_sb_postdoc_amra_dbt  doc,
                                   z_sb_terminal_amra_dbt term,
                                   trn
                             where deal.paymentnumber = tr.checknumber
                               and doc.paymentnumbers like tr.checknumber
                               and trn.ITRNNUM = doc.kindpayment
                               and term.name = tr.terminal
                               and trn.CTRNACCC = term.deal_acc
                               and tr.checknumber = chknmb;
                          exception
                            when others then
                              begin
                                select DEAL_ACC
                                  into d_acc_f_our_tr_in_en_tr
                                  from z_sb_termdeal_amra_dbt deal,
                                       z_sb_transact_amra_dbt tr,
                                       z_sb_terminal_amra_dbt term
                                 where deal.paymentnumber = tr.checknumber
                                   and term.name = tr.terminal
                                   and tr.checknumber = chknmb
                                   and tr.sess_id = r.SESS_ID;
                              exception
                                when others then
                                  writelog(r.PAYDATE,
                                           '��������! ��� CheckNumber = ' ||
                                           r.CheckNumber || ' ����� � ' ||
                                           chknmb || ' ����������� ' || pnmb ||
                                           ' ����� ' || sump || ' ���� ' ||
                                           chkdate || ' BLOCK:777',
                                           id_sess);
                              end;
                            
                          end;
                          num := num + 1;
                          POST(id_sess_       => r.SESS_ID,
                               department_    => r.department,
                               real_payer_    => d_acc_f_our_tr_in_en_tr,
                               real_receiver_ => '30110810100000000003',
                               payer_         => '���� ����� ����� ���������� ' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               receiver_      => '���� ������ �� �����' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               okpo_receiver  => '11000572',
                               sum_           => r.AMOUNTWITHCHECKS,
                               ground_        => r.ground,
                               payment_number => r.CHECKNUMBER,
                               kpp_receiver   => '111000171',
                               numb           => num,
                               PAYDATE_       => r.PAYDATE);
                        end if;
                      elsif search_termdeal = 0 then
                        writelog(r.PAYDATE,
                                 '��������! ��� CheckNumber = ' ||
                                 r.CheckNumber || ' ����� � ' || chknmb ||
                                 ' ����������� ' || pnmb || ' ����� ' || sump ||
                                 ' ���� ' || chkdate || ' BLOCK:5',
                                 id_sess);
                      end if;
                    end loop;
                  end if;
                end if;
              end if;
            end if;
          
          end if;
        end loop;
      
        /* 
        ����������� �� ������������ 
        7
        30232810100000010010/30110810100000000003
        */
        comistocor(id_sess, num);
        comistocor_spec(id_sess, num);
        /* 
        ����������� �� ������������ ���� ��� ������� ��������
        8
        30232810100000010010/70107810000001720109
        */
        selfcomiss(id_sess, num);
        /*
        |--------------------------------------------------------------------------------|
        |                                ������ AMRA                                     |
        |--------------------------------------------------------------------------------|
        */
        for r in amra_pr loop
          if r.STATUS = '00' then
            /*1 ���� ������������� �� ����� 0*/
            if r.CASHAMOUNT <> 0 and r.transactiontype <> '��������������' and
               r.terminalnetwork = '��������' then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.acc_20208,
                   real_receiver_ => r.GENERAL_ACC,
                   payer_         => '���� ��������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   receiver_      => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.CASHAMOUNT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            /*3 ���� ���������� �� ����� 0*/
            if r.AMOUNTTOCHECK <> 0 and
               r.transactiontype <> '��������������' and
               r.terminalnetwork = '��������' then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.GENERAL_ACC,
                   real_receiver_ => r.DEAL_ACC,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ����� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTTOCHECK,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            
            end if;
          
            /*������ �� �����*/
            if r.AMOUNTWITHCHECKS <> 0 and
               r.transactiontype != '��������������' then
              if r.CHECKSINCOMING is not null then
                /*��� ��� ��� ��������*/
                select count(*)
                  into bnk_deal_for_chek
                  from Z_SB_TERMINAL_AMRA_DBT t
                 where t.name = r.terminal;
                /*���� ��� ��������*/
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
                      /*ID ��������� �� ������� �������� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into pnmb
                        From dual
                       where level = 3
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into sump
                        From dual
                       where level = 2
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chknmb
                        From dual
                       where level = 1
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*���� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chkdate
                        From dual
                       where level = 4
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*��� ��� ��� ��������*/
                      select count(*)
                        into bnk_deal
                        from Z_SB_TERMINAL_AMRA_DBT t
                       where t.name = pnmb;
                      /*���� ����� ������ ���������*/
                      if bnk_deal > 0 then
                      
                        /*����� ����� ��� ��������*/
                        begin
                          if deal_acc_for_comis is null and
                             to_number(replace(replace(sump, '.', ','),
                                               '�',
                                               '')) >= r.COMMISSIONAMOUNT and
                             r.COMMISSIONAMOUNT <> 0 then
                            select DEAL_ACC
                              into deal_acc_for_comis
                              from z_sb_termdeal_amra_dbt deal,
                                   z_sb_transact_amra_dbt tr,
                                   z_sb_postdoc_amra_dbt  doc,
                                   z_sb_terminal_amra_dbt term,
                                   trn
                             where deal.paymentnumber = tr.checknumber
                               and doc.paymentnumbers like tr.checknumber
                               and trn.ITRNNUM = doc.kindpayment
                               and term.name = tr.terminal
                               and trn.CTRNACCC = term.deal_acc
                               and tr.checknumber = chknmb;
                          end if;
                        exception
                          when others then
                            begin
                              select DEAL_ACC
                                into deal_acc_for_comis
                                from z_sb_termdeal_amra_dbt deal,
                                     z_sb_transact_amra_dbt tr,
                                     z_sb_terminal_amra_dbt term
                               where deal.paymentnumber = tr.checknumber
                                 and term.name = tr.terminal
                                 and tr.checknumber = chknmb
                                 and tr.sess_id = r.SESS_ID;
                            exception
                              when others then
                                writelog(r.PAYDATE,
                                         '��������! ��� CheckNumber = ' ||
                                         r.CheckNumber || ' ����� � ' ||
                                         chknmb || ' ����������� ' || pnmb ||
                                         ' ����� ' || sump || ' ���� ' ||
                                         chkdate || ' BLOCK:4568_4',
                                         id_sess);
                            end;
                          
                        end;
                      
                        begin
                          select DEAL_ACC
                            into deal_acc_fromterm
                            from z_sb_termdeal_amra_dbt deal,
                                 z_sb_transact_amra_dbt tr,
                                 z_sb_postdoc_amra_dbt  doc,
                                 z_sb_terminal_amra_dbt term,
                                 trn
                           where deal.paymentnumber = tr.checknumber
                             and doc.paymentnumbers like tr.checknumber
                             and trn.ITRNNUM = doc.kindpayment
                             and term.name = tr.terminal
                             and trn.CTRNACCC = term.deal_acc
                             and tr.checknumber = chknmb;
                        exception
                          when others then
                            begin
                              select DEAL_ACC
                                into deal_acc_fromterm
                                from z_sb_termdeal_amra_dbt deal,
                                     z_sb_transact_amra_dbt tr,
                                     z_sb_terminal_amra_dbt term
                               where deal.paymentnumber = tr.checknumber
                                 and term.name = tr.terminal
                                 and tr.checknumber = chknmb
                                 and tr.sess_id = r.SESS_ID;
                            exception
                              when others then
                                writelog(r.PAYDATE,
                                         '��������! ��� CheckNumber = ' ||
                                         r.CheckNumber || ' ����� � ' ||
                                         chknmb || ' ����������� ' || pnmb ||
                                         ' ����� ' || sump || ' ���� ' ||
                                         chkdate || ' BLOCK:4568_3',
                                         id_sess);
                            end;
                          
                        end;
                        /*����� �����*/
                        select count(*)
                          into search_termdeal
                          from z_sb_termdeal_amra_dbt deal,
                               z_sb_transact_amra_dbt tr,
                               z_sb_postdoc_amra_dbt  doc,
                               z_sb_terminal_amra_dbt term,
                               trn
                         where deal.paymentnumber = tr.checknumber
                           and doc.paymentnumbers like tr.checknumber
                           and trn.ITRNNUM = doc.kindpayment
                           and term.name = tr.terminal
                           and trn.CTRNACCC = term.deal_acc
                           and tr.checknumber = chknmb;
                        /*���� � ��� �����*/
                        select count(*)
                          into search_termdeal_tek_reic
                          from z_sb_termdeal_amra_dbt deal,
                               z_sb_transact_amra_dbt tr,
                               z_sb_terminal_amra_dbt term
                         where deal.paymentnumber = tr.checknumber
                           and term.name = tr.terminal
                           and tr.checknumber = chknmb
                           and tr.sess_id = r.SESS_ID;
                        /*���� ����� �����*/
                        if search_termdeal > 0 then
                          sumforclearsum_self := sumforclearsum_self +
                                                 r.amountofpayment
                          /*to_number(replace(replace(sump,'.',','),'�',''))*/
                           ;
                          write_deal(to_number(replace(replace(sump,
                                                               '.',
                                                               ','),
                                                       '�',
                                                       '')) /*r.amountofpayment*/,
                                     trunc(r.paydate),
                                     r.service,
                                     1,
                                     r.checknumber,
                                     deal_acc_fromterm /*r.DEAL_ACC*/,
                                     r.GENERAL_ACC);
                        elsif search_termdeal = 0 then
                          if search_termdeal_tek_reic > 0 then
                            sumforclearsum_self := sumforclearsum_self +
                                                   r.amountofpayment
                            /*to_number(replace(replace(sump,'.',','),'�',''))*/
                             ;
                            write_deal(to_number(replace(replace(sump,
                                                                 '.',
                                                                 ','),
                                                         '�',
                                                         '')) /*r.amountofpayment*/,
                                       trunc(r.paydate),
                                       r.service,
                                       1,
                                       r.checknumber,
                                       deal_acc_fromterm /*r.DEAL_ACC*/,
                                       r.GENERAL_ACC);
                          else
                            writelog(r.PAYDATE,
                                     '��������! ��� CheckNumber = ' ||
                                     r.CheckNumber || ' ����� � ' || chknmb ||
                                     ' ����������� ' || pnmb || ' ����� ' || sump ||
                                     ' ���� ' || chkdate || ' BLOCK:4568_2',
                                     id_sess);
                          end if;
                        
                        end if;
                      elsif bnk_deal = 0 then
                        sumforclearsum := sumforclearsum +
                                          to_number(replace(replace(sump,
                                                                    '.',
                                                                    ','),
                                                            '�',
                                                            ''));
                        num            := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => '30110810100000000003',
                             real_receiver_ => '30232810700000010012',
                             payer_         => '���� ����� ����� ���������� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             receiver_      => '���� ������ �� ����� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             okpo_receiver  => '11000572',
                             sum_           => to_number(replace(replace(sump,
                                                                         '.',
                                                                         ','),
                                                                 '�',
                                                                 '')),
                             ground_        => r.ground || ' �����:' || sump ||
                                               '_�:' || num,
                             payment_number => r.CHECKNUMBER,
                             kpp_receiver   => '111000171',
                             numb           => num,
                             PAYDATE_       => r.PAYDATE,
                             chk_           => r.CHECKSINCOMING);
                        num := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => '30232810700000010012',
                             real_receiver_ => r.CLEAR_SUM,
                             payer_         => '���� ����� ����� ���������� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             receiver_      => '���� ������ �� ����� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             okpo_receiver  => '11000572',
                             sum_           => to_number(replace(replace(sump,
                                                                         '.',
                                                                         ','),
                                                                 '�',
                                                                 '')),
                             ground_        => r.ground || ' �����:' || sump ||
                                               '_�:' || num,
                             payment_number => r.CHECKNUMBER,
                             kpp_receiver   => '111000171',
                             numb           => num,
                             PAYDATE_       => r.PAYDATE,
                             chk_           => r.CHECKSINCOMING);
                      end if;
                    end loop;
                  end if;
                elsif bnk_deal_for_chek = 0 then
                  /*���� �� ��� ��������*/
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
                        into pnmb
                        From dual
                       where level = 3
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into sump
                        From dual
                       where level = 2
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chknmb
                        From dual
                       where level = 1
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*���� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chkdate
                        From dual
                       where level = 4
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*��� ��� ��� ��������*/
                      select count(*)
                        into bnk_deal
                        from Z_SB_TERMINAL_AMRA_DBT t
                       where t.name = pnmb;
                      /*���� ����� ������ ���������*/
                      if bnk_deal > 0 then
                        /*����� �����*/
                        select count(*)
                          into search_termdeal
                          from z_sb_termdeal_amra_dbt t
                         where t.paymentnumber = chknmb;
                        /*���� ����� �����*/
                        if search_termdeal > 0 then
                          /*
                          select DEAL_ACC
                           into d_acc_f_our_tr_in_en_tr
                           from z_sb_termdeal_amra_dbt t1,
                                Z_SB_TRANSACT_AMRA_DBT t2,
                                z_sb_terminal_amra_dbt t3
                          where t1.paymentnumber = chknmb
                            and t1.paymentnumber = t2.checknumber
                            and t3.name = t2.terminal;*/
                          begin
                            select DEAL_ACC
                              into d_acc_f_our_tr_in_en_tr
                              from z_sb_termdeal_amra_dbt deal,
                                   z_sb_transact_amra_dbt tr,
                                   z_sb_postdoc_amra_dbt  doc,
                                   z_sb_terminal_amra_dbt term,
                                   trn
                             where deal.paymentnumber = tr.checknumber
                               and doc.paymentnumbers like tr.checknumber
                               and trn.ITRNNUM = doc.kindpayment
                               and term.name = tr.terminal
                               and trn.CTRNACCC = term.deal_acc
                               and tr.checknumber = chknmb;
                          exception
                            when others then
                              begin
                                select DEAL_ACC
                                  into d_acc_f_our_tr_in_en_tr
                                  from z_sb_termdeal_amra_dbt deal,
                                       z_sb_transact_amra_dbt tr,
                                       z_sb_terminal_amra_dbt term
                                 where deal.paymentnumber = tr.checknumber
                                   and term.name = tr.terminal
                                   and tr.checknumber = chknmb
                                   and tr.sess_id = r.SESS_ID;
                              exception
                                when others then
                                  writelog(r.PAYDATE,
                                           '��������! ��� CheckNumber = ' ||
                                           r.CheckNumber || ' ����� � ' ||
                                           chknmb || ' ����������� ' || pnmb ||
                                           ' ����� ' || sump || ' ���� ' ||
                                           chkdate || ' BLOCK:16',
                                           id_sess);
                              end;
                          end;
                          write_deal(to_number(replace(replace(sump,
                                                               '.',
                                                               ','),
                                                       '�',
                                                       '')),
                                     r.paydate,
                                     r.service,
                                     2,
                                     r.checknumber,
                                     d_acc_f_our_tr_in_en_tr,
                                     '30110810100000000003' /*r.GENERAL_ACC*/);
                        end if;
                      elsif search_termdeal = 0 then
                        writelog(r.PAYDATE,
                                 '��������! ��� CheckNumber = ' ||
                                 r.CheckNumber || ' ����� � ' || chknmb ||
                                 ' ����������� ' || pnmb || ' ����� ' || sump ||
                                 ' ���� ' || chkdate || ' BLOCK:5',
                                 id_sess);
                      end if;
                    end loop;
                  end if;
                end if;
              end if;
            end if;
            /*4 ���� ������������� �� ����� 0 
            ����������� ������, ���� ��������������, ���� ����� ���� �� ������� ���
            ��������, ����������� ������� ������ � �����, ���� ���� ����, �� ����� ������� �� �����
            ����. ���� ����� ������ ������, �� ����������� � �������� ���������.
            �������� ������ ������ ������ ������, ������ ������ � ��� ��������.
            ��� ������� ������� �������, �� ���� ���������� 2-� ����������, ��...*/
          
            /*���� �������������� � �� ������*/
            if r.transactiontype = '��������������' and
               r.terminalnetwork = '��������' then
              if r.COMMISSIONAMOUNT <> 0 then
                /*� ����� �������� �� ����*/
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.CRASH_ACC,
                     real_receiver_ => r.GENERAL_COMIS,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���� ����� �������� ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => r.COMMISSIONAMOUNT,
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              end if;
            else
              /*���� ��� ��������:
              1) ���� ����� ������� �� ���� � ��� ������ � �����.
               ����� �������, � ���� ������ ��� ������������� ���������� ����� �����.*/
              if r.COMMISSIONAMOUNT <> 0 and r.CASHAMOUNT <> 0 and
                 r.terminalnetwork = '��������' and r.AMOUNTWITHCHECKS = 0 then
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.GENERAL_ACC,
                     real_receiver_ => r.GENERAL_COMIS,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���� ����� �������� ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => r.COMMISSIONAMOUNT,
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
                /*
                2) ���� ����� ������� ���� � ������ � �����.
                 ��� ����� ������ � �����. ���� �����������*/
              elsif r.COMMISSIONAMOUNT <> 0 and r.CASHAMOUNT = 0 and
                    r.AMOUNTWITHCHECKS <> 0 and
                    r.terminalnetwork = '��������' then
                /*���� ������ �� ����� ������ ������ ������*/
                if (sumforclearsum_self = 0 and sumforclearsum > 0) then
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => r.CLEAR_SUM,
                       real_receiver_ => r.GENERAL_COMIS,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���� ����� �������� ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r.COMMISSIONAMOUNT,
                       ground_        => r.ground,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                  /*���� ������ �� ����� ������ � ������*/
                elsif (sumforclearsum_self > 0 and sumforclearsum > 0) then
                  if sumforclearsum_self >= r.COMMISSIONAMOUNT then
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => deal_acc_for_comis /*r.GENERAL_ACC*/,
                         real_receiver_ => r.GENERAL_COMIS,
                         payer_         => '���� ����� ����� ���������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         receiver_      => '���� ����� �������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => r.COMMISSIONAMOUNT,
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE);
                  elsif sumforclearsum >= r.COMMISSIONAMOUNT then
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => deal_acc_for_comis /*r.GENERAL_COMIS*/,
                         real_receiver_ => r.GENERAL_COMIS,
                         payer_         => '���� ����� ����� ���������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         receiver_      => '���� ����� �������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => r.COMMISSIONAMOUNT,
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE);
                  end if;
                  /*���� ������ �� ����� ������ ������ ������*/
                elsif (sumforclearsum_self > 0 and sumforclearsum = 0) then
                  if deal_acc_for_comis is null then
                    writelog(r.PAYDATE,
                             '��������! ��� CheckNumber = ' ||
                             r.CheckNumber || ' ����� � ' || chknmb ||
                             ' ����������� ' || pnmb || ' ����� ' || sump ||
                             ' ���� ' || chkdate || ' BLOCK:16578',
                             id_sess);
                  end if;
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => deal_acc_for_comis /*r.GENERAL_ACC*/,
                       real_receiver_ => r.GENERAL_COMIS,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���� ����� �������� ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r.COMMISSIONAMOUNT,
                       ground_        => r.ground,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                end if;
                /*���� ����� ������� �� ���� � ������ � �����*/
              elsif r.COMMISSIONAMOUNT <> 0 and r.CASHAMOUNT <> 0 and
                    r.AMOUNTWITHCHECKS <> 0 and
                    r.terminalnetwork = '��������' then
                if r.CASHAMOUNT >= r.COMMISSIONAMOUNT then
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => r.GENERAL_ACC,
                       real_receiver_ => r.GENERAL_COMIS,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���� ����� �������� ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r.COMMISSIONAMOUNT,
                       ground_        => r.ground,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                elsif r.CASHAMOUNT < r.COMMISSIONAMOUNT then
                  if (sumforclearsum_self = 0 and sumforclearsum > 0) then
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => r.CLEAR_SUM,
                         real_receiver_ => r.GENERAL_COMIS,
                         payer_         => '���� ����� ����� ���������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         receiver_      => '���� ����� �������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => r.COMMISSIONAMOUNT,
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE);
                    /*���� ������ �� ����� ������ � ������*/
                  elsif (sumforclearsum_self > 0 and sumforclearsum > 0) then
                    if sumforclearsum_self >= r.COMMISSIONAMOUNT then
                      num := num + 1;
                      POST(id_sess_       => r.SESS_ID,
                           department_    => r.department,
                           real_payer_    => deal_acc_for_comis /*r.GENERAL_ACC*/,
                           real_receiver_ => r.GENERAL_COMIS,
                           payer_         => '���� ����� ����� ���������� ' ||
                                             r.idterm || ' ��������� ' ||
                                             r.department,
                           receiver_      => '���� ����� �������� ' ||
                                             r.idterm || ' ��������� ' ||
                                             r.department,
                           okpo_receiver  => '11000572',
                           sum_           => r.COMMISSIONAMOUNT,
                           ground_        => r.ground,
                           payment_number => r.CHECKNUMBER,
                           kpp_receiver   => '111000171',
                           numb           => num,
                           PAYDATE_       => r.PAYDATE);
                    elsif sumforclearsum >= r.COMMISSIONAMOUNT then
                      num := num + 1;
                      POST(id_sess_       => r.SESS_ID,
                           department_    => r.department,
                           real_payer_    => deal_acc_for_comis /*r.GENERAL_COMIS*/,
                           real_receiver_ => r.GENERAL_COMIS,
                           payer_         => '���� ����� ����� ���������� ' ||
                                             r.idterm || ' ��������� ' ||
                                             r.department,
                           receiver_      => '���� ����� �������� ' ||
                                             r.idterm || ' ��������� ' ||
                                             r.department,
                           okpo_receiver  => '11000572',
                           sum_           => r.COMMISSIONAMOUNT,
                           ground_        => r.ground,
                           payment_number => r.CHECKNUMBER,
                           kpp_receiver   => '111000171',
                           numb           => num,
                           PAYDATE_       => r.PAYDATE);
                    end if;
                    /*���� ������ �� ����� ������ ������ ������*/
                  elsif (sumforclearsum_self > 0 and sumforclearsum = 0) then
                    if deal_acc_for_comis is null then
                      writelog(r.PAYDATE,
                               '��������! ��� CheckNumber = ' ||
                               r.CheckNumber || ' ����� � ' || chknmb ||
                               ' ����������� ' || pnmb || ' ����� ' || sump ||
                               ' ���� ' || chkdate || ' BLOCK:16578',
                               id_sess);
                    end if;
                    num := num + 1;
                    POST(id_sess_       => r.SESS_ID,
                         department_    => r.department,
                         real_payer_    => deal_acc_for_comis /*r.GENERAL_ACC*/,
                         real_receiver_ => r.GENERAL_COMIS,
                         payer_         => '���� ����� ����� ���������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         receiver_      => '���� ����� �������� ' ||
                                           r.idterm || ' ��������� ' ||
                                           r.department,
                         okpo_receiver  => '11000572',
                         sum_           => r.COMMISSIONAMOUNT,
                         ground_        => r.ground,
                         payment_number => r.CHECKNUMBER,
                         kpp_receiver   => '111000171',
                         numb           => num,
                         PAYDATE_       => r.PAYDATE);
                  end if;
                end if;
              
              end if;
            end if;
            /*�������� ���� �����!!!*/
            deal_acc_for_comis := null;
          
            /*������ ������*/
            if r.transactiontype = '��������������' then
              if r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS = 0 and
                 r.terminalnetwork = '��������' then
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.CRASH_ACC,
                     real_receiver_ => r.CLEAR_SUM,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���� ������� ������� ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => r.AMOUNTOFPAYMENT,
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              elsif r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS <> 0 and
                    r.terminalnetwork = '��������' then
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.CRASH_ACC,
                     real_receiver_ => r.CLEAR_SUM,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���� ������� ������� ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => r.AMOUNTOFPAYMENT,
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              end if;
            else
              if r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS = 0 and
                 r.terminalnetwork = '��������' then
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.GENERAL_ACC,
                     real_receiver_ => r.CLEAR_SUM,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���� ������� ������� ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => r.AMOUNTOFPAYMENT,
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              elsif r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS <> 0 and
                    r.terminalnetwork = '��������' and r.CASHAMOUNT <> 0 then
                num := num + 1;
                POST(id_sess_       => r.SESS_ID,
                     department_    => r.department,
                     real_payer_    => r.GENERAL_ACC,
                     real_receiver_ => r.CLEAR_SUM,
                     payer_         => '���� ����� ����� ���������� ' ||
                                       r.idterm || ' ��������� ' ||
                                       r.department,
                     receiver_      => '���� ������� ������� ' || r.idterm ||
                                       ' ��������� ' || r.department,
                     okpo_receiver  => '11000572',
                     sum_           => /*r.CASHAMOUNT */ r.AMOUNTOFPAYMENT
                     /*-r.COMMISSIONAMOUNT*/,
                     ground_        => r.ground,
                     payment_number => r.CHECKNUMBER,
                     kpp_receiver   => '111000171',
                     numb           => num,
                     PAYDATE_       => r.PAYDATE);
              elsif r.AMOUNTOFPAYMENT <> 0 and r.AMOUNTWITHCHECKS <> 0 and
                    r.terminalnetwork = '��������' and r.CASHAMOUNT = 0 then
                /*���� ������ �� ����� ������ ������ ������*/
                if (sumforclearsum_self = 0 and sumforclearsum > 0) then
                  null;
                  /*���� ������ �� ����� ������ � ������*/
                elsif (sumforclearsum_self > 0 and sumforclearsum > 0) then
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => r.GENERAL_ACC,
                       real_receiver_ => r.CLEAR_SUM,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���� ������� ������� ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r.AMOUNTOFPAYMENT - sumforclearsum,
                       ground_        => r.ground,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                  /*���� ������ �� ����� ������ ������ ������*/
                elsif (sumforclearsum_self > 0 and sumforclearsum = 0) then
                  num := num + 1;
                  POST(id_sess_       => r.SESS_ID,
                       department_    => r.department,
                       real_payer_    => r.GENERAL_ACC,
                       real_receiver_ => r.CLEAR_SUM,
                       payer_         => '���� ����� ����� ���������� ' ||
                                         r.idterm || ' ��������� ' ||
                                         r.department,
                       receiver_      => '���� ������� ������� ' || r.idterm ||
                                         ' ��������� ' || r.department,
                       okpo_receiver  => '11000572',
                       sum_           => r.AMOUNTOFPAYMENT,
                       ground_        => r.ground,
                       payment_number => r.CHECKNUMBER,
                       kpp_receiver   => '111000171',
                       numb           => num,
                       PAYDATE_       => r.PAYDATE);
                end if;
              end if;
            end if;
            /*�������� ����������, ��� ��������� �����*/
            sumforclearsum      := 0;
            sumforclearsum_self := 0;
          elsif r.STATUS != '00' and r.terminalnetwork = '��������' and
                r.transactiontype <> '��������������' then
            /*���� ������
            20208/crash*/
            if r.CASHAMOUNT <> 0 then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.acc_20208,
                   real_receiver_ => r.GENERAL_ACC,
                   payer_         => '���� ��������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   receiver_      => '���� ���������� ������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.CASHAMOUNT,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.GENERAL_ACC,
                   real_receiver_ => r.CRASH_ACC,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ���������� ������� ' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.cashamount,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            end if;
            /*���� �� ��������� ������� ���� �����*/
            if r.AMOUNTTOCHECK <> 0 /*and r.CASHAMOUNT >= r.AMOUNTTOCHECK*/
             then
              num := num + 1;
              POST(id_sess_       => r.SESS_ID,
                   department_    => r.department,
                   real_payer_    => r.CRASH_ACC,
                   real_receiver_ => r.DEAL_ACC,
                   payer_         => '���� ����� ����� ���������� ' ||
                                     r.idterm || ' ��������� ' ||
                                     r.department,
                   receiver_      => '���� ������ �� �����' || r.idterm ||
                                     ' ��������� ' || r.department,
                   okpo_receiver  => '11000572',
                   sum_           => r.AMOUNTTOCHECK,
                   ground_        => r.ground,
                   payment_number => r.CHECKNUMBER,
                   kpp_receiver   => '111000171',
                   numb           => num,
                   PAYDATE_       => r.PAYDATE);
            elsif r.AMOUNTTOCHECK <> 0 and r.CASHAMOUNT < r.AMOUNTTOCHECK then
              writelog(r.PAYDATE,
                       '��������! �� ������� �� ����� � �������. ��� CheckNumber = ' ||
                       r.CheckNumber,
                       id_sess);
            end if;
            /*���� ������ ������ �� �����*/
            if r.AMOUNTWITHCHECKS <> 0 then
              if r.CHECKSINCOMING is not null then
                /*��� ��� ��� ��������*/
                select count(*)
                  into bnk_deal_for_chek
                  from Z_SB_TERMINAL_AMRA_DBT t
                 where t.name = r.idterm;
                /*���� ��� ��������*/
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
                      /*ID ��������� �� ������� �������� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into pnmb
                        From dual
                       where level = 3
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into sump
                        From dual
                       where level = 2
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chknmb
                        From dual
                       where level = 1
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*���� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chkdate
                        From dual
                       where level = 4
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*��� ��� ��� ��������*/
                      select count(*)
                        into bnk_deal
                        from Z_SB_TERMINAL_AMRA_DBT t
                       where t.name = pnmb;
                      /*���� ����� ������ ���������*/
                      if bnk_deal > 0 then
                        begin
                          select DEAL_ACC
                            into deal_acc_fromterm
                            from z_sb_termdeal_amra_dbt deal,
                                 z_sb_transact_amra_dbt tr,
                                 z_sb_postdoc_amra_dbt  doc,
                                 z_sb_terminal_amra_dbt term,
                                 trn
                           where deal.paymentnumber = tr.checknumber
                             and doc.paymentnumbers like tr.checknumber
                             and trn.ITRNNUM = doc.kindpayment
                             and term.name = tr.terminal
                             and trn.CTRNACCC = term.deal_acc
                             and tr.checknumber = chknmb;
                        exception
                          when others then
                            begin
                              select DEAL_ACC
                                into deal_acc_fromterm
                                from z_sb_termdeal_amra_dbt deal,
                                     z_sb_transact_amra_dbt tr,
                                     z_sb_terminal_amra_dbt term
                               where deal.paymentnumber = tr.checknumber
                                 and term.name = tr.terminal
                                 and tr.checknumber = chknmb
                                 and tr.sess_id = r.SESS_ID;
                            exception
                              when others then
                                writelog(r.PAYDATE,
                                         '��������! ��� CheckNumber = ' ||
                                         r.CheckNumber || ' ����� � ' ||
                                         chknmb || ' ����������� ' || pnmb ||
                                         ' ����� ' || sump || ' ���� ' ||
                                         chkdate || ' BLOCK:4568_1 st 18',
                                         id_sess);
                            end;
                          
                        end;
                        /*����� �����*/
                        select count(*)
                          into search_termdeal
                          from z_sb_termdeal_amra_dbt deal,
                               z_sb_transact_amra_dbt tr,
                               z_sb_postdoc_amra_dbt  doc,
                               z_sb_terminal_amra_dbt term,
                               trn
                         where deal.paymentnumber = tr.checknumber
                           and doc.paymentnumbers like tr.checknumber
                           and trn.ITRNNUM = doc.kindpayment
                           and term.name = tr.terminal
                           and trn.CTRNACCC = term.deal_acc
                           and tr.checknumber = chknmb;
                      
                        /*���� � ��� �����*/
                        select count(*)
                          into search_termdeal_tek_reic
                          from z_sb_termdeal_amra_dbt deal,
                               z_sb_transact_amra_dbt tr,
                               z_sb_terminal_amra_dbt term
                         where deal.paymentnumber = tr.checknumber
                           and term.name = tr.terminal
                           and tr.checknumber = chknmb
                           and tr.sess_id = r.SESS_ID;
                      
                        /*���� ����� �����*/
                        if search_termdeal > 0 then
                          num := num + 1;
                          POST(id_sess_       => r.SESS_ID,
                               department_    => r.department,
                               real_payer_    => deal_acc_fromterm /*r.DEAL_ACC*/,
                               real_receiver_ => r.CRASH_ACC,
                               payer_         => '���� ����� ����� ���������� ' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               receiver_      => '���� ������ �� �����' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               okpo_receiver  => '11000572',
                               sum_           => to_number(replace(replace(sump,
                                                                           '.',
                                                                           ','),
                                                                   '�',
                                                                   '')),
                               ground_        => r.ground,
                               payment_number => r.CHECKNUMBER,
                               kpp_receiver   => '111000171',
                               numb           => num,
                               PAYDATE_       => r.PAYDATE);
                        elsif search_termdeal = 0 then
                          if search_termdeal_tek_reic > 0 then
                            num := num + 1;
                            POST(id_sess_       => r.SESS_ID,
                                 department_    => r.department,
                                 real_payer_    => deal_acc_fromterm /*r.DEAL_ACC*/,
                                 real_receiver_ => r.CRASH_ACC,
                                 payer_         => '���� ����� ����� ���������� ' ||
                                                   r.idterm || ' ��������� ' ||
                                                   r.department,
                                 receiver_      => '���� ������ �� �����' ||
                                                   r.idterm || ' ��������� ' ||
                                                   r.department,
                                 okpo_receiver  => '11000572',
                                 sum_           => to_number(replace(replace(sump,
                                                                             '.',
                                                                             ','),
                                                                     '�',
                                                                     '')),
                                 ground_        => r.ground,
                                 payment_number => r.CHECKNUMBER,
                                 kpp_receiver   => '111000171',
                                 numb           => num,
                                 PAYDATE_       => r.PAYDATE);
                          else
                            writelog(r.PAYDATE,
                                     '��������! ��� CheckNumber = ' ||
                                     r.CheckNumber || ' ����� � ' || chknmb ||
                                     ' ����������� ' || pnmb || ' ����� ' || sump ||
                                     ' ���� ' || chkdate || ' BLOCK:4_1',
                                     id_sess);
                          
                          end if;
                        
                        end if;
                      elsif bnk_deal = 0 then
                        num := num + 1;
                        POST(id_sess_       => r.SESS_ID,
                             department_    => r.department,
                             real_payer_    => '30110810100000000003',
                             real_receiver_ => r.CRASH_ACC,
                             payer_         => '���� ����� ����� ���������� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             receiver_      => '���� ������ �� ����� ' ||
                                               r.idterm || ' ��������� ' ||
                                               r.department,
                             okpo_receiver  => '11000572',
                             sum_           => to_number(replace(replace(sump,
                                                                         '.',
                                                                         ','),
                                                                 '�',
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
                elsif bnk_deal_for_chek = 0 then
                  /*���� �� ��� ��������*/
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
                        into pnmb
                        From dual
                       where level = 3
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into sump
                        From dual
                       where level = 2
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*����� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chknmb
                        From dual
                       where level = 1
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*���� ���� �����*/
                      Select Regexp_Substr(r_.chk, '[^/]+', 1, Level) chk
                        into chkdate
                        From dual
                       where level = 4
                      Connect By Regexp_Substr(r_.chk, '[^/]+', 1, Level) Is Not Null;
                      /*��� ��� ��� ��������*/
                      select count(*)
                        into bnk_deal
                        from Z_SB_TERMINAL_AMRA_DBT t
                       where t.name = pnmb;
                      /*���� ����� ������ ���������*/
                      if bnk_deal > 0 then
                        /*����� �����*/
                        select count(*)
                          into search_termdeal
                          from z_sb_termdeal_amra_dbt t
                         where t.paymentnumber = chknmb;
                        /*���� ����� �����*/
                        if search_termdeal > 0 then
                          begin
                            select DEAL_ACC
                              into d_acc_f_our_tr_in_en_tr
                              from z_sb_termdeal_amra_dbt deal,
                                   z_sb_transact_amra_dbt tr,
                                   z_sb_postdoc_amra_dbt  doc,
                                   z_sb_terminal_amra_dbt term,
                                   trn
                             where deal.paymentnumber = tr.checknumber
                               and doc.paymentnumbers like tr.checknumber
                               and trn.ITRNNUM = doc.kindpayment
                               and term.name = tr.terminal
                               and trn.CTRNACCC = term.deal_acc
                               and tr.checknumber = chknmb;
                          exception
                            when others then
                              begin
                                select DEAL_ACC
                                  into d_acc_f_our_tr_in_en_tr
                                  from z_sb_termdeal_amra_dbt deal,
                                       z_sb_transact_amra_dbt tr,
                                       z_sb_terminal_amra_dbt term
                                 where deal.paymentnumber = tr.checknumber
                                   and term.name = tr.terminal
                                   and tr.checknumber = chknmb
                                   and tr.sess_id = r.SESS_ID;
                              exception
                                when others then
                                  writelog(r.PAYDATE,
                                           '��������! ��� CheckNumber = ' ||
                                           r.CheckNumber || ' ����� � ' ||
                                           chknmb || ' ����������� ' || pnmb ||
                                           ' ����� ' || sump || ' ���� ' ||
                                           chkdate || ' BLOCK:777',
                                           id_sess);
                              end;
                            
                          end;
                          num := num + 1;
                          POST(id_sess_       => r.SESS_ID,
                               department_    => r.department,
                               real_payer_    => d_acc_f_our_tr_in_en_tr,
                               real_receiver_ => '30110810100000000003',
                               payer_         => '���� ����� ����� ���������� ' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               receiver_      => '���� ������ �� �����' ||
                                                 r.idterm || ' ��������� ' ||
                                                 r.department,
                               okpo_receiver  => '11000572',
                               sum_           => r.AMOUNTWITHCHECKS,
                               ground_        => r.ground,
                               payment_number => r.CHECKNUMBER,
                               kpp_receiver   => '111000171',
                               numb           => num,
                               PAYDATE_       => r.PAYDATE);
                        end if;
                      elsif search_termdeal = 0 then
                        writelog(r.PAYDATE,
                                 '��������! ��� CheckNumber = ' ||
                                 r.CheckNumber || ' ����� � ' || chknmb ||
                                 ' ����������� ' || pnmb || ' ����� ' || sump ||
                                 ' ���� ' || chkdate || ' BLOCK:5',
                                 id_sess);
                      end if;
                    end loop;
                  end if;
                end if;
              end if;
            end if;
          
          end if;
        end loop;
      
        /*���������
        ������ �� �����: 
        1) ���� ����� � �����,
        2) ����� ����� � �����
        3) ���� ����� � �����
        */
        /*
        |-----------------|
        |���� ��� ��������|
        |-----------------|
        */
        selfdeal(id_sess, num);
        /*
        |--------------------|
        |���� �� ��� ��������|
        |--------------------|
        */
        notselfdeal(id_sess, num);
        /*7*/
        for7(id_sess, num);
        for7_tocor(id_sess, num);
        /*8*/
        for8(id_sess, num);
        for8_tocor(id_sess, num);
        /*9*/
        for9(id_sess, num);
        /*10*/
        for10(id_sess, num);
        for10_spec(id_sess, num);
        /*--------------------*/
      
        /*�������*/
        loop474(id_sess, num);
      else
        writelog(trunc(sysdate),
                 '��������! ���� � ID = ' || id_sess || ' ����� ������ = ' ||
                 file_status,
                 id_sess);
      end if;
    
      /*�������� �� ������� � Z_SB_POSTDOC_AMRA_DBT______________________*/
    
      for r in (select count(*) cnt
                  from z_sb_postdoc_amra_dbt post
                 where post.sess_id = id_sess) loop
        if r.cnt = 0 then
          writelog(trunc(sysdate),
                   '��������! ���� � ID = ' || id_sess ||
                   ' ����������� ������ �  Z_SB_POSTDOC_AMRA_DBT',
                   id_sess);
        end if;
      end loop;
    
      /*�������� �� ������� � TRN______________________*/
      for r in (select count(*) cnt
                  from trn
                 where trn.ITRNNUM in
                       (select t.kindpayment
                          from z_sb_postdoc_amra_dbt t
                         where t.sess_id = id_sess)) loop
        if r.cnt = 0 then
          writelog(trunc(sysdate),
                   '��������! ���� � ID = ' || id_sess ||
                   ' ����������� ������ �  TRN',
                   id_sess);
        end if;
      end loop;
    
    exception
      when others then
        writelog(descripion => SQLERRM || ' ' ||
                               DBMS_UTILITY.format_error_stack || ' ' ||
                               dbms_utility.format_error_backtrace,
                 sess_id1   => id_sess);
    end;
  
    /*�������� �� ��������� � ������� �� �������.*/
    begin
      SELECT count(*)
        into res
        FROM Z_SB_LOG_AMRA lg
       WHERE lg.sess_id = id_sess;
      IF res = 0 THEN
        for r in acc_ost loop
          if r.ostt <> 0 then
            /*v_ret.delete;*/
            select Z_SB_POST_RECORD(ACCOUNT_PAYER,
                                    CORACC_PAYER,
                                    MFO_RECEIVER,
                                    ACCOUNT_RECEIVER,
                                    SUM,
                                    IAPPLICATIONKIND,
                                    SYMBOL_CACH,
                                    RESULT_CARRY,
                                    NUMBER_PACK,
                                    DATE_VALUE,
                                    DATE_DOCUMENT,
                                    KIND_OPER,
                                    PAYER,
                                    RECEIVER,
                                    BANK_RECEIVER,
                                    GROUND,
                                    OKPO_PAYER,
                                    OKPO_RECEIVER,
                                    PAY_DATE,
                                    USERFIELD1,
                                    USERFIELD2,
                                    DEPARTMENT,
                                    SHIFR_OPER,
                                    KINDPAYMENT,
                                    KPP_RECEIVER,
                                    BUDJCLASSIFCODE,
                                    OKATO,
                                    NUMB_PAYDOC,
                                    SESS_ID,
                                    RECDATE,
                                    PAYMENTNUMBERS)
              bulk collect
              into v_ret
              from Z_SB_POSTDOC_AMRA_DBT h
             where h.sess_id = id_sess;
          
            if res >= 0 then
              save_doc_if_ost_not_null(v_ret);
            end if;
          
            writelog(descripion => '��������� �� ������� ������� �� ����� ' ||
                                   r.acc || ' ��������: ' || r.acc_name ||
                                   ' ���. ' || r.otd || ' �����:' || r.ostt,
                     sess_id1   => id_sess);
            dbms_output.put_line('��������� �� ������� ������� �� ����� ' ||
                                 r.acc || ' ��������: ' || r.acc_name ||
                                 ' ���. ' || r.otd || ' �����:' || r.ostt);
            /*exit;*/
            res := -1;
          end if;
        end loop;
      END IF;
    exception
      when others then
        writelog(descripion =>                 SQLERRM||chr(13)||chr(10)|| DBMS_UTILITY.FORMAT_ERROR_BACKTRACE||chr(13)||chr(10)||
                 DBMS_UTILITY.FORMAT_CALL_STACK||chr(13)||chr(10),
                 sess_id1   => id_sess);
    end;
    /*-----------------------------------------------------*/
    /*������� ����������, ���� ������, �������� �� ������!*/
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
  
    RETURN ret;
  end make;

begin
  null;
end z_sb_calc_tr_amra;
/

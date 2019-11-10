PL/SQL Developer Test script 3.0
136
-- Created on 06.11.2019 by SAIDP 
declare
  comis_rate number;
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
           (select DEPARTMENT from Z_SB_TERMINAL_DBT where NAME = t.TERMINAL) DEPARTMENT,
           '������ "' || SERVICE || '" - ' ||
           upper((SELECT AttributeValue
                   FROM (select ATTRIBUTES_, sess_id, CHECKNUMBER
                           from Z_SB_TRANSACT_AMRA_DBT) g,
                        XMLTABLE('/��������/���' PASSING
                                 xmltype(ATTRIBUTES_) COLUMNS Service
                                 VARCHAR2(500) PATH '@������',
                                 CheckNumber VARCHAR2(500) PATH '@���������',
                                 AttributeName VARCHAR2(500) PATH
                                 '@�����������',
                                 AttributeValue VARCHAR2(500) PATH
                                 '@����������������')
                  where ATTRIBUTENAME = 'fio_children'
                    and g.CHECKNUMBER = t.checknumber
                    and g.sess_id = t.sess_id)) || ' �� ' ||
           upper((SELECT AttributeValue
                   FROM (select ATTRIBUTES_, sess_id, CHECKNUMBER
                           from Z_SB_TRANSACT_AMRA_DBT) g,
                        XMLTABLE('/��������/���' PASSING
                                 xmltype(ATTRIBUTES_) COLUMNS Service
                                 VARCHAR2(500) PATH '@������',
                                 CheckNumber VARCHAR2(500) PATH '@���������',
                                 AttributeName VARCHAR2(500) PATH
                                 '@�����������',
                                 AttributeValue VARCHAR2(500) PATH
                                 '@����������������')
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
     where t.SESS_ID = 15205
       and t.TRANSACTIONTYPE not in
           ('����������', '��������������')
       and PROVIDER = '��������'
       and t.checknumber = '02013580261452743045'
       and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT);
begin
  for r in sbra_pr loop
    if r.STATUS = '00' then
      if r.AMOUNTOFPAYMENT <> 0 then
        select COMISSION
          into comis_rate
          from z_sb_termserv_dbt t
         where t.IDTERM = r.idterm
           and t.NAME = r.service;
        z_sb_calc_tr_amra.POST(id_sess_        => r.SESS_ID,
                               department_     => r.department,
                               real_payer_     => r.acc40911,
                               real_receiver_  => r.ACC_REC,
                               payer_          => '���������� ���� ' ||
                                                  r.idterm || ' ��������� ' ||
                                                  r.department,
                               receiver_       => r.acc_name,
                               okpo_receiver   => r.bank_inn,
                               sum_            => (r.Cashamount +
                                                  r.AMOUNTWITHCHECKS) -
                                                  comis_rate -
                                                  to_number(replace(replace(r.AMOUNTTOCHECK,
                                                                            '.',
                                                                            ','),
                                                                    '�',
                                                                    '')),
                               ground_         => r.ground,
                               payment_number  => r.CHECKNUMBER,
                               kpp_receiver    => r.kpp,
                               numb            => 1,
                               PAYDATE_        => r.PAYDATE,
                               bo1_            => '4',
                               bo2_            => '0',
                               okpo_payer      => r.inn,
                               coracc_payer    => '30102810900000000017'/*r.bank_cor_acc,
                               CORACC_PAYER_2  => '30102810900000000017'*/,
                               mfo_receiver    => r.bank_cor_bic,
                               bank_receiver   => '���� �������',
                               BUDJCLASSIFCODE => r.kbk_payer,
                               okato           => r.OKATO,
                               for_nbra        => true);
      end if;
    end if;
  end loop;
end;
0
0

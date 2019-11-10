delete from Z_SB_TRANSACT_AMRA_DBT t where sess_id  in (&d);
delete from z_sb_termdeal_amra_dbt where sess_id in (&d);
delete from z_sb_postdoc_amra_dbt where sess_id in (&d);
delete from Z_SB_TERMDEAL_AMRA_FEB where sess_id in (&d);
delete from Z_SB_TRANSACT_AMRA_INKAS where sess_id in (&d);  
delete from z_sb_fn_sess_amra where sess_id in (&d);


update z_sb_termdeal_amra_dbt
   set DEALENDDATE = null, DEALPAYMENTNUMBER = null, STATUS = 0
 where PAYMENTNUMBER = 02013580261601321546;
select * from z_sb_termdeal_amra_dbt;

------------------------------------------
delete from z_sb_fn_sess_amra
 where sess_id in
       (select SESS_ID
          from z_sb_fn_sess_amra
        minus
        select distinct SESS_ID
          from Z_SB_TRANSACT_AMRA_DBT
         where SESS_ID in (select SESS_ID from z_sb_fn_sess_amra))

z_sb_calc_deal_

select * from z_sb_fn_sess_amra;
select * from Z_SB_LOG_AMRA;
select * from Z_SB_TRANSACT_AMRA_DBT t where sess_id in (&d);
select * from z_sb_termdeal_amra_dbt where sess_id in (&d);
select * from z_sb_postdoc_amra_dbt where sess_id in (&d);
select * from Z_SB_TERMDEAL_AMRA_FEB where sess_id in (&d);

CREATE PUBLIC SYNONYM z_sb_fn_sess_amra FOR xxi.z_sb_fn_sess_amra;


grant execute on Z_SB_LOG_AMRA to odb;

Select Regexp_Substr(r.CHECKSINCOMING, '[^|]+', 1, Level) chk
  From dual
Connect By Regexp_Substr(r.CHECKSINCOMING, '[^|]+', 1, Level) Is Not Null

  select util_dm2.ACC_Ost(0,
                                    '30110810100000000003',
                                    'RUR',
                                    trunc(sysdate) + 1,
                                    'R')
              from dual;


select 'DOC_NUM:' || rownum || '_' || DTRNCREATE || '_' || CTRNACCD || '->' ||
       CTRNACCC || '_' || MTRNRSUM
  from trn
 where CTRNIDOPEN = 'AMRA_IMPORT'
   AND (DTRNCREATE, CTRNACCD, CTRNACCC, MTRNRSUM, CTRNPURP) in
       (select DATE_VALUE, ACCOUNT_PAYER, ACCOUNT_RECEIVER, SUM, GROUND
          from z_sb_postdoc_amra_dbt
         where sess_id in (&d))

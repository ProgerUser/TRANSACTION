select AMOUNTWITHCHECKS,
       CASHAMOUNT -
       to_number(replace(replace(COMMISSIONAMOUNT, '.', ','), ' ', '')),
       t.* --regexp_substr(CHECKSINCOMING, '[0-9]{20}'), t.*
  from Z_SB_TRANSACT_AMRA_DBT t
 where trunc(paydate) between to_date('27-09-2019', 'dd-mm-yyyy') and
       to_date('27-09-2019', 'dd-mm-yyyy')
   and AMOUNTOFPAYMENT = '139,5'
/*and to_number(replace(replace(AMOUNTWITHCHECKS, '.', ','), ' ', '')) <> 0
and t.TERMINAL in (select NAME from Z_SB_TERMINAL_DBT)*/

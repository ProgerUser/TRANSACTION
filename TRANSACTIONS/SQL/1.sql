select /*CASHAMOUNT  AMOUNTOFPAYMENT*/
 DECODE(CASHAMOUNT,
        AMOUNTOFPAYMENT -
        (80 - to_number(replace(replace(AMOUNTTOCHECK, ' ', ''), '.', ','))),
        50,),
 t.*
  from Z_SB_TRANSACT_AMRA_DBT t
 where t.checknumber = '02013580262037170710'

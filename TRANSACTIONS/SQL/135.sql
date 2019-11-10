select * from NOR20 t where cnor0id = '104';
select * from NOR21 t where INOR1SORT = 60;

SELECT *
  FROM NORR1
 where (trunc(DNOR1CLC) = to_date('011019', 'ddmmrr') and (inorr1sort = 60))
 order by INORR1SORT;

select *
  from NORR0 t
 where cnorr0id = '104'
   and trunc(DNORR0CLC) = to_date('011019', 'ddmmrr');

select CCDA2ISOTHERACCPAY, CCDA2ISOTHERACCPAY_SUP, NCDA2AGRID
  from cda2
 where NCDA2AGRID = 474;

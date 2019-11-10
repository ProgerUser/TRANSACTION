/*15.10.19 17 :29 = время регистрации*/
select t.OBJ$NAME,
       to_timestamp_tz(t.NTIMESTAMP#, 'DD.MM.YYYY HH24:MI:SS TZR'),
       t.SPARE1,
       t.*
  from sys.aud$ t
 where trunc(t.ntimestamp#) = '05.11.2019'
   and t.action# in (97, 98, 99) /*94  CREATE PACKAGE*/
 order by trunc(t.ntimestamp#) desc
/*15.10.19 17 :29 = время регистрации*/

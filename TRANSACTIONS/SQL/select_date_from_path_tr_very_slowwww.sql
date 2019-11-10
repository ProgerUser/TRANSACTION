select distinct to_date(substr(substr(g.date_f, instr(g.date_f, '-') + 1),
                               1,
                               8),
                        'yyyy.mm.dd') date_,
                t.file_name,
                t.date_time,
                t.sess_id
  from Z_SB_FN_SESS_AMRA t,
       (Select Regexp_Substr(FILE_NAME, '[^\]+', 1, Level) date_f,
               level lvl,
               sess_id,
               date_time
          From Z_SB_FN_SESS_AMRA
        Connect By Regexp_Substr(FILE_NAME, '[^\]+', 1, Level) Is Not Null) g,
       (select max(lvl) maxx, sess_id, date_time
          from (Select level lvl, sess_id, date_time
                  From Z_SB_FN_SESS_AMRA
                Connect By Regexp_Substr(FILE_NAME, '[^\]+', 1, Level) Is Not Null)
         group by sess_id, date_time) h
 where t.sess_id = g.sess_id
   and g.sess_id = h.sess_id
   and g.lvl = h.maxx
   and t.date_time = g.date_time
   and g.date_time = h.date_time
 order by date_ desc

/*
select 1
  from dual
 where regexp_like('D:\Users\saidp\Desktop\AMRA_XML\10.19\632-20191001,632-20191004.xml',
                   '632{2}')*/

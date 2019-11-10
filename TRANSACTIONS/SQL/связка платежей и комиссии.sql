
update (select c.*, a.CNAMEOPER /*,
                                       (SELECT t.CACCOUNT
                                          FROM ov_acccash t
                                         where t.IIDSUBSYSTEM in
                                               (SELECT k.IDCOM
                                                  FROM ov_com k
                                                 WHERE k.ID_BOPER in (select l.ID_BOPER
                                                                        from ov_boper l
                                                                       where l.CCBID in ('TM_IN', 'TM_OUT')
                                                                         and l.CCBID2 = 'C001'))
                                           and t.ISUBSYSTEM = 1
                                           and t.IDCNUM = c.idcnum
                                           and t.CVAL = c.CVAL
                                           and t.IIDSUBSYSTEM = 1) acc*/
          from ov_boper a, ov_com b, ov_acccash c
         where a.ID_BOPER = b.id_boper
           and a.CCBID in ('TM_IN', 'TM_OUT')
           and c.IIDSUBSYSTEM = b.idcom
              /*   and a.ccbid2 = 'C005'*/
           and isubsystem = 1
           and c.caccount is null) c
   set CACCOUNT =
       (SELECT t.CACCOUNT
          FROM ov_acccash t
         where t.IIDSUBSYSTEM in
               (SELECT k.IDCOM
                  FROM ov_com k
                 WHERE k.ID_BOPER in (select l.ID_BOPER
                                        from ov_boper l
                                       where l.CCBID in ('TM_IN', 'TM_OUT')
                                         and l.CCBID2 = 'C001'))
           and t.ISUBSYSTEM = 1
           and t.IDCNUM = c.idcnum
           and t.CVAL = c.CVAL
           and t.IIDSUBSYSTEM = 1)

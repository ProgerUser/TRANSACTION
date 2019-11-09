CREATE OR REPLACE VIEW V_PLC
(iplcid, cplcnum, iplcprimary, cplcprimary, dplcopen, dplcend, cplpword, icusnum, ccusname, iplaagrid, dplcfirst, iplpid, cplpid, cplctypename, cusrlogname, cplcstatus, iplcstatus, cplpcur, plcrowid, iplprodid, cmdpname, cplpname, id_az, id_name, iotdnum, usr_dep,gr_bl)
AS
SELECT plc.iplcid,
       cplcnum,
       iplcprimary,
       DECODE(iplcprimary,
              0,
              'Осн',
              1,
              'Доп',
              2,
              'Прв',
              'Неопр') cplcprimary,
       dplcopen,
       dplcend,
       cplpword,
       plc.icusnum,
       ccusname,
       iplaagrid,
       dplcfirst,
       plc.iplpid,
       v_pl_p.cplctypename,
       cplctypename,
       cusrlogname,
       pl_card.get_cardstatus(plc.iplcid) cplcstatus,
       TO_NUMBER(pl_card.get_cardparam(1, plc.iplcid), '9999') iplcstatus,
       cplpcur,
       plc.ROWID AS plcrowid,
       iplprodid,
       cmdpname,
       cplpname,
       pl_util.get_agrigaz_id(plc.iplaagrid) id_az,
       pl_util.get_agrigaz_name(plc.iplaagrid) id_name,
       iotdnum,
       case
         when plc.CUSRLOGNAME = 'XXI' then
          (select nvl((select t.department
                        from Z_SB_COMFORT_CARD t
                       where t.cardnum = plc.CPLCNUM
                         and rownum = 1),
                      'Нет данных!')
             from dual)
         else
          (select nvl((select C_CASHNAME
                        from OV_CASH a, ov_user_cash b
                       where a.IDCNUM = b.IDCNUM
                         and b.CUSRLOGNAME = plc.CUSRLOGNAME
                         and rownum = 1),
                      'Нет в кассе! ' || plc.CUSRLOGNAME)
             from dual)
       end usr_dep,
       (select g.ground
          from Z_SB_CARDBLOCK g
         where g.dateoper = (select max(h.DATEOPER)
                               from Z_SB_CARDBLOCK h
                              where h.cardid = plc.IPLCID)
           and g.cardid = plc.IPLCID) gr_bl
  FROM plc, xcus, v_pl_p
 WHERE xcus.icusnum = plc.icusnum
   AND v_pl_p.iplpid = plc.iplpid
   AND v_pl_p.imdproctype = pl_adm.get_proc;

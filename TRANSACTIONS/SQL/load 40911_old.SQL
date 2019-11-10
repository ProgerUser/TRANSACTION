FUNCTION Get_XlsAFile( --pErr      OUT VARCHAR2,
                      pXlsName IN VARCHAR2
                      --    pEmpty    IN  VARCHAR2 DEFAULT 'NO'
                      ) RETURN BOOLEAN IS

  ITYPE    number;
  icom     number;
  proc     number;
  mmin     number;
  mmax     number;
  idp      number;
  idcC     number;
  isv      number;
  NNEWCOM  number;
  icnum    number;
  bb       NUMBER;
  bixc     varchar2(30);
  ispp     number;
  nameplat varchar2(1024);
  cSym     varchar2(1024);
  MCONST   NUMBER;

  cpo varchar2(10);
  cursor get_idcnum(acc in varchar2) is
    select idcnum
      from ov_account
     where idacc = 1
       and caccount = acc
       and ISUBSYSTEM = 0
       and IIDSUBSYSTEM = 0
       and idkassir = 0;
  /*cursor dplat(dbik in varchar2,dSCHET in varchar2,dICNUM in number,kbk in varchar2,innn in varchar2,cnmoper in varchar2) is
    select dp.idov_plat,dp.id_usedonecom
      from ov_plat dp,OV_CPLAT cp,ov_pdept_info pd
      where  
          cbic = dbik and cacc = dSCHET and upper(innn) = upper(cinn) and
          upper(cnameoper) = upper(cnmoper) and 
          cp.IDOV_PLAT = dp.IDOV_PLAT and IDCNUM =dICNUM and 
          pd.IDOV_PLAT(+) = dp.IDOV_PLAT and upper(NVL(kbk,'X')) = upper(NVL(CBUDCODE,'X'));
  --       insert into ov_pdept_info(IDOV_PLAT,CBUDCODE,COKATOCODE)
  --       values (idp,kbk,okato);*/

  cursor dplat(dbik      in varchar2,
               dSCHET    in varchar2,
               dICNUM    in number,
               kbk       in varchar2,
               innn      in varchar2,
               cnmoper   in varchar2,
               okto      in varchar2,
               TRAN_     in varchar2,
               CACCOUNT_ in varchar2) is
    select dp.idov_plat, dp.id_usedonecom
      from ov_plat dp, ov_account a, OV_VCPLAT b, ov_vaccount c
     where cbic = dbik
       and cacc = dSCHET
       and upper(innn) = upper(cinn)
       and upper(cnameoper) = upper(cnmoper)
       and upper(nvl(ckbk, 'X')) = upper(nvl(kbk, 'X'))
       and cokato = okto
          --
       and a.idacc = 1
       and a.caccount = CACCOUNT_
       and a.ISUBSYSTEM = 0
       and a.IIDSUBSYSTEM = 0
       and a.idkassir = 0
       and a.IDCNUM = b.IDCNUM
          --and b.IDOV_PLAT = 17718
       and c.IDCNUM = b.IDOV_PLAT
       and c.cval = 'RUR'
          --and c.idcnum = 17718
       and c.idkassir = 0
       and c.ISUBSYSTEM = 0
       AND c.IIDSUBSYSTEM = 0
       and c.CACCOUNT = TRAN_
       and c.IDACC in (113) /*
        select dp.idov_plat, dp.id_usedonecom
          from ov_plat dp
         where cbic = dbik
           and cacc = dSCHET
           and upper(innn) = upper(cinn)
           and upper(cnameoper) = upper(cnmoper)
           and upper(nvl(ckbk, 'X')) = upper(nvl(kbk, 'X'))
           and cokato = okto
           and exists (select 1
                  from ov_account a, OV_VCPLAT b, ov_vaccount c
                 where a.idacc = 1
                   and a.caccount = CACCOUNT_
                   and a.ISUBSYSTEM = 0
                   and a.IIDSUBSYSTEM = 0
                   and a.idkassir = 0
                   and a.IDCNUM = b.IDCNUM
                      --and b.IDOV_PLAT = 17718
                   and c.IDCNUM = b.IDOV_PLAT
                   and c.cval = 'RUR'
                      --and c.idcnum = 17718
                   and c.idkassir = 0
                   and c.ISUBSYSTEM = 0
                   AND c.IIDSUBSYSTEM = 0
                   and c.CACCOUNT = TRAN_
                   and c.IDACC in (113))*/
    ;

  CACCOUNT VARCHAR2(1024);
  prev_com NUMBER;
  doch     VARCHAR2(1024);
  OPERNAME VARCHAR2(1024);
  KBK      VARCHAR2(1024);
  OKATO    VARCHAR2(1024);
  INN      VARCHAR2(1024);
  KPP      VARCHAR2(1024);
  BIK      VARCHAR2(1024);
  SCHET    VARCHAR2(1024);
  NSHET    VARCHAR2(1024);
  TRAN     VARCHAR2(1024);
  kdest    VARCHAR2(1024);
  cdest    VARCHAR2(1024);
  pdest    VARCHAR2(1024);
  psevd    VARCHAR2(1024);
  grname   VARCHAR2(1024);

  objAP          OLE2.Obj_Type;
  objWorkBooks   OLE2.Obj_Type;
  objActiveSheet OLE2.Obj_Type;
  objSheet       OLE2.Obj_Type;
  objUsedRange   OLE2.Obj_Type;
  objRows        OLE2.Obj_Type;
  objCols        OLE2.Obj_Type;
  objCells       OLE2.Obj_Type;

  c_Value  VARCHAR2(2000);
  vRow     INTEGER;
  vSheet   INTEGER;
  vCol     INTEGER;
  i_Row    INTEGER;
  i_Col    INTEGER;
  i_Sheets INTEGER;
  ctemp    varchar2(300);

  /*  DDATEPOD    date;
    CCONCODE    varchar(4);
    cname       varchar(255);
    icomfizlic  NUMBER;
    icomfl_bank NUMBER;
    icomjurlic  NUMBER;
    creqdescr   varchar(255);
  */

  IRET NUMBER := 0;
  IALL NUMBER := 0;
  IINS NUMBER := 0;
  INS  NUMBER := 0;

  inumstr NUMBER := 0;

  inum1 number;
  inum2 number;

  FUNCTION ExcelCells(objAP IN OLE2.Obj_Type,
                      iRow  IN INTEGER,
                      iCol  IN INTEGER) RETURN OLE2.Obj_Type IS
    objExcelCells OLE2.Obj_Type;
    my_ArgList    OLE2.List_Type;
  BEGIN
    my_ArgList := OLE2.Create_ArgList;
    OLE2.Add_Arg(my_ArgList, iRow);
    OLE2.Add_Arg(my_ArgList, iCol);
    objExcelCells := OLE2.Get_Obj_Property(objAP, 'Cells', my_ArgList);
    OLE2.Destroy_Arglist(my_ArgList);
    RETURN objExcelCells;
  END ExcelCells;
  FUNCTION ExcelSheets(objAP IN OLE2.Obj_Type, iRow IN INTEGER)
    RETURN OLE2.Obj_Type IS
    objExcelSheets OLE2.Obj_Type;
    my_ArgList     OLE2.List_Type;
  BEGIN
    my_ArgList := OLE2.Create_ArgList;
    OLE2.Add_Arg(my_ArgList, iRow);
    objExcelSheets := OLE2.Get_Obj_Property(objAP, 'Sheet', my_ArgList);
    OLE2.Destroy_Arglist(my_ArgList);
    RETURN objExcelSheets;
  END ExcelSheets;

BEGIN
  select CSMRMFO8 into bixc from smr;
  objAp := OLE2.Create_Obj('Excel.Application');
  OLE2.Set_Property(objAp, 'Visible', 0);
  objWorkBooks := OLE2.Get_Obj_Property(objAp, 'WorkBooks');
  OLE_I.Invoke(objWorkBooks, 'open', pXlsName);

  objSheet       := OLE2.Get_Obj_Property(objAp, 'Sheets');
  i_Sheets       := OLE2.Get_Num_Property(objSheet, 'Count');
  objActiveSheet := OLE2.Get_Obj_Property(objAp, 'ActiveSheet');

  objUsedRange := OLE2.Get_Obj_Property(objActiveSheet, 'UsedRange');

  objRows := OLE2.Get_Obj_Property(objUsedRange, 'Rows');
  i_Row   := OLE2.Get_Num_Property(objRows, 'Count');
  OLE2.Release_Obj(objRows);

  objCols := OLE2.Get_Obj_Property(objUsedRange, 'Columns');
  i_Col   := OLE2.Get_Num_Property(objCols, 'Count');
  OLE2.Release_Obj(objCols);

  --  i_Row:=1;

  FOR vRow IN 6 .. i_Row LOOP
  
    inumstr := inumstr + 1;
  
    FOR vCol IN 1 .. i_Col LOOP
      objCells := ExcelCells(objUsedRange, vRow, vCol);
      c_Value  := OLE2.Get_Char_Property(objCells, 'Text');
      OLE2.Release_Obj(objCells);
      --      c_Value := Mstr.CharOne(Mstr.Alltrim(replace(replace(replace(c_Value,chr(10),' '),chr(13),' '),chr(7),'')), ' ');
      c_Value := replace(c_value, '%', '');
      cpo     := '0';
      IF vCol = 1 THEN
        ITYPE := 0;
        IF c_Value IN ('K', 'k', 'К', 'к') THEN
          ITYPE := 1;
        END IF;
      END IF;
    
      IF vCol = 2 and c_Value is null THEN
        IRET := 2;
        EXIT;
      END IF;
      --касса
      IF vCol = 2 THEN
        CACCOUNT := replace(c_Value, ' ', '');
      END IF;
      -- название операции
      IF vCol = 3 THEN
        OPERNAME := c_Value;
      END IF;
      -- получатель платежа
      IF vCol = 4 THEN
        nameplat := c_Value;
      END IF;
      -- кбк
      IF vCol = 5 THEN
        KBK := replace(c_Value, ' ', '');
      END IF;
      -- окато
      IF vCol = 6 THEN
        OKATO := replace(c_Value, ' ', '');
      END IF;
      -- INN
      IF vCol = 7 THEN
        INN := replace(c_Value, ' ', '');
      END IF;
      -- KPP
      IF vCol = 8 THEN
        KPP := replace(c_Value, ' ', '');
      END IF;
      -- БИК
      IF vCol = 9 THEN
        BIK := replace(c_Value, ' ', '');
      END IF;
      -- расчетный счет
      IF vCol = 10 THEN
        SCHET := replace(c_Value, ' ', '');
      END IF;
      -- наименование р.счета
      IF vCol = 11 THEN
        NSHET := c_Value;
      END IF;
      -- транзитник
      IF vCol = 12 THEN
        TRAN := replace(c_Value, ' ', '');
      END IF;
      -- тип комиссии
      IF vCol = 13 THEN
        icom := c_Value;
        if ITYPE = 0 and icom is not Null then
          icom := 1;
        END IF;
      END IF;
      -- процент
      IF vCol = 14 THEN
        proc := replace(replace(nvl(c_Value, '0'), ',', '.'), '%', null);
      END IF;
      -- минимум
      IF vCol = 15 THEN
        mmin := replace(nvl(c_Value, '0'), ',', '.');
      END IF;
      -- максимум
      IF vCol = 16 THEN
        mmax := replace(nvl(c_Value, '0'), ',', '.');
      END IF;
      -- счет доходов
      IF vCol = 17 THEN
        doch := replace(c_Value, ' ', '');
      end if;
      -- сводная комиссия
      IF vCol = 18 THEN
        null; /*doch:=c_Value;*/
      end if;
      --назначение платежа для комиссии
      IF vCol = 19 THEN
        kdest := c_Value;
      end if;
      -- сводная кассовый
      IF vCol = 20 THEN
        null; /*doch:=c_Value;*/
      end if;
      --
      IF vCol = 21 THEN
        cSym := c_Value;
      end if;
      -- назначение платежа для комиссии
      IF vCol = 22 THEN
        cdest := c_Value;
      end if;
      -- сводная платежка
      IF vCol = 23 THEN
        null;
        /*doch:=c_Value;*/
        ispp := 1;
        if bik = bixc and upper(c_Value) in ('ДА') then
          ispp := 0;
        end if;
      end if;
      -- назначение платежа для 
      IF vCol = 24 THEN
        pdest := c_Value;
      end if;
      -- псевдоним 
      IF vCol = 25 THEN
        psevd := c_Value;
      end if;
      -- группа
      IF vCol = 26 THEN
        grname := c_Value;
      end if;
    END LOOP;
  
    icnum := null;
    open get_idcnum(CACCOUNT);
    fetch get_idcnum
      into icnum;
    close get_idcnum;
  
    if icnum is not null or Upper(CACCOUNT) in ('ALL', 'ВСЕ') then
      idp  := null;
      idcC := null;
      open dplat(bik,
                 SCHET,
                 ICNUM,
                 kbk,
                 INN,
                 OPERNAME,
                 OKATO,
                 TRAN,
                 CACCOUNT);
      fetch dplat
        into idp, idcC;
      close dplat;
    
      isv := 0;
      if ITYPE = 0 then
        isv := 1;
      end if;
      ctemp := 'ov_plat';
    
      if idcC is null then
        idcC := ov_upacsh.GetNumOper;
      end if;
    
      cpo := '1';
      bik := ltrim(rtrim(bik));
      /* if length(ltrim(rtrim(bik)))!=9 then
         msg('БИК. Не верное количество символов!'||bik);
         raise form_trigger_failure;
      end if;*/
      inn := ltrim(rtrim(inn));
      /*  if inn is not null and length(ltrim(rtrim(inn))) not in (10,12) then
         msg('ИНН. Не верное количество символов ('||inn||')!');
         raise form_trigger_failure;
      end if;*/
      kpp := ltrim(rtrim(kpp));
      /*   if kpp is not null and kpp!='0' and length(rtrim(kpp))!=9 then
         msg('КПП. Не верное количество символов ('||kpp||')!');
         raise form_trigger_failure;
      end if;*/
    
      if idp is null then
        idp := ov_upacsh.GetNumOper;
        --idcC:=ov_upacsh.GetNumOper;
        ctemp := 'ov_plat_insert ';
      
        insert into ov_plat
          (ITYPECLI,
           idov_plat,
           cbic,
           cinn,
           ckpp,
           cacc,
           cnameacc,
           cval,
           cnameoper,
           iusedonecom,
           id_usedonecom,
           cdestcash,
           USED_DESTCASH,
           CDESTPP,
           ISVOD,
           ISVODPP,
           SIMVOL,
           cokato,
           ISUSEDPPCOM,
           ISVODPPCOM,
           ISUSEDTMCOM,
           CPSEVDO,
           CGROUPS,
           CWHO,
           ckbk,
           ISTMCOM)
        values
          (ITYPE,
           idp,
           rtrim(bik),
           rtrim(inn),
           rtrim(kpp),
           SCHET,
           nvl(NSHET, nameplat),
           'RUR',
           NVL(OPERNAME, NSHET),
           decode(icom, 1, 1, 0),
           idcC,
           cdest,
           1,
           pdest,
           isv,
           ispp,
           cSym,
           okato,
           decode(icom, 2, 1, 0),
           decode(icom, 2, idcC, NULL),
           decode(icom, 4, 1, NULL),
           psevd,
           grname,
           '@CASHIER@',
           KBK,
           decode(icom, 4, idcC, NULL));
      else
        update ov_plat
           set ITYPECLI      = ITYPE,
               idov_plat     = idp,
               cbic          = bik,
               cinn          = inn,
               ckpp          = kpp,
               cacc          = SCHET,
               cnameacc      = nvl(NSHET, nameplat),
               cval          = 'RUR',
               cnameoper     = NVL(OPERNAME, NSHET),
               iusedonecom   = decode(icom, 1, 1, 0),
               id_usedonecom = idcC,
               cdestcash     = cdest,
               USED_DESTCASH = 1,
               CDESTPP       = pdest,
               ISVOD         = isv,
               ISVODPP       = ispp,
               cokato        = okato,
               ISUSEDPPCOM   = decode(icom, 2, 1, 0),
               ISVODPPCOM    = decode(icom, 2, idcC, NULL),
               ISUSEDTMCOM   = decode(icom, 4, 1, NULL),
               CPSEVDO       = psevd,
               CGROUPS       = grname,
               cwho          = '@CASHIER@',
               ckbk          = KBK,
               ISTMCOM       = decode(icom, 4, idcC, NULL)
         where idov_plat = idp;
      
      end if;
    
      cpo := '2';
    
      post;
      -- комиссия
      if idp is Null and (sign(proc) > 0 OR (mmax > 0 AND mmIN > 0)) then
      
        NNEWCOM := ov_upacsh.GetNumCom;
        ctemp   := 'ov_com';
      
        begin
        
          INSERT INTO OV_COM
            (idcom,
             ithewho,
             dbegin,
             dend,
             id_boper,
             ires,
             cnamecom,
             idc,
             itypecom,
             icontrolcash,
             iruborval,
             icontrolcurs,
             itypedoc,
             cbo2,
             ccashsimvol,
             cval,
             cvaloff,
             cvalto,
             caccount,
             ccur,
             cnameaccount,
             cdest,
             ccodecom,
             iprovcom,
             iokrug,
             ifaction,
             icodeop,
             cwho,
             idoc,
             iprint,
             cdestcli,
             ievery,
             ireduce,
             ICONTACT,
             COUTLAY)
          VALUES
            (NNEWCOM,
             NULL,
             null,
             null,
             idcC,
             0,
             'Комиссия за платеж',
             NULL,
             0,
             NULL,
             0,
             0,
             decode(icom, 1, 0, 2, 9, 4, 11),
             NULL,
             decode(icom, 1, 32, NULL),
             'RUR',
             NULL,
             NULL,
             doch,
             'RUR',
             NULL,
             Kdest,
             NULL,
             'NO',
             0,
             0,
             NULL,
             decode(icom, 1, '@CASHIER@'),
             NULL,
             decode(icom, 1, 1, 0),
             NULL,
             0,
             0,
             0,
             'SHA');
          post;
          ctemp  := 'ov_dcom';
          MCONST := 0;
          if sign(mmax) < 1 then
            mmax := 999999;
          end if;
          IF SIGN(proc) < 1 THEN
            MCONST := mmin;
            mmin   := 0;
            mmax   := 0;
          END IF;
        
          INSERT INTO OV_DCOM
            (idcom,
             mmore,
             mconstant,
             mpercent,
             cvaleqv,
             mporog1,
             mporog2,
             mstep,
             mstepconst)
          VALUES
            (NNEWCOM, 0, MCONST, proc, NULL, mmin, mmax, 0, 0);
          -- Запись счета доходов в конкретную кассу                   
          if Upper(CACCOUNT) not in ('ALL', 'ВСЕ') then
            INSERT INTO OV_account
              (IDAcc,
               cVal,
               idcnum,
               idkassir,
               ISubSystem,
               IIDSubSystem,
               Caccount,
               ipp)
            VALUES
              (0, 'RUR', icnum, 0, 1, NNEWCOM, doch, 0);
          end if;
        
        exception
          when others then
            null;
        end;
      
      elsif idp is not null and (sign(proc) > 0 OR (mmax > 0 AND mmIN > 0)) then
      
        ctemp := 'ov_com';
        begin
        
          Select idcom
            into prev_com
            from ov_com
           where idcom =
                 (select max(idcom) from ov_com c where c.id_boper = idcC);
        
          if Upper(CACCOUNT) not in ('ALL', 'ВСЕ') then
            -- Запись счета доходов в конкретную кассу
            begin
              INSERT INTO OV_account
                (IDAcc,
                 cVal,
                 idcnum,
                 idkassir,
                 ISubSystem,
                 IIDSubSystem,
                 Caccount,
                 ipp)
              VALUES
                (0, 'RUR', icnum, 0, 1, prev_com, doch, 0);
            exception
              when dup_val_on_index then
                UPDATE OV_account
                   set Caccount = doch
                 where IDAcc = 0
                   and cVal = 'RUR'
                   and idcnum = icnum
                   and idkassir = 0
                   and ISubSystem = 1
                   and IIDSubSystem = prev_com;
            end;
          else
            update ov_com set caccount = doch where idcom = prev_com;
          end if;
        exception
          when no_data_found then
            NNEWCOM := ov_upacsh.GetNumCom;
            begin
              INSERT INTO OV_COM
                (idcom,
                 ithewho,
                 dbegin,
                 dend,
                 id_boper,
                 ires,
                 cnamecom,
                 idc,
                 itypecom,
                 icontrolcash,
                 iruborval,
                 icontrolcurs,
                 itypedoc,
                 cbo2,
                 ccashsimvol,
                 cval,
                 cvaloff,
                 cvalto,
                 caccount,
                 ccur,
                 cnameaccount,
                 cdest,
                 ccodecom,
                 iprovcom,
                 iokrug,
                 ifaction,
                 icodeop,
                 cwho,
                 idoc,
                 iprint,
                 cdestcli,
                 ievery,
                 ireduce,
                 ICONTACT,
                 COUTLAY)
              VALUES
                (NNEWCOM,
                 NULL,
                 null,
                 null,
                 idcC,
                 0,
                 'Комиссия за платеж',
                 NULL,
                 0,
                 NULL,
                 0,
                 0,
                 decode(icom, 1, 0, 2, 9, 4, 11),
                 NULL,
                 decode(icom, 1, 32, NULL),
                 'RUR',
                 NULL,
                 NULL,
                 doch,
                 'RUR',
                 NULL,
                 Kdest,
                 NULL,
                 'NO',
                 0,
                 0,
                 NULL,
                 decode(icom, 1, '@CASHIER@'),
                 NULL,
                 decode(icom, 1, 1, 0),
                 NULL,
                 0,
                 0,
                 0,
                 'SHA');
              post;
              ctemp  := 'ov_dcom';
              MCONST := 0;
              if sign(mmax) < 1 then
                mmax := 999999;
              end if;
              IF SIGN(proc) < 1 THEN
                MCONST := mmin;
                mmin   := 0;
                mmax   := 0;
              END IF;
            
              INSERT INTO OV_DCOM
                (idcom,
                 mmore,
                 mconstant,
                 mpercent,
                 cvaleqv,
                 mporog1,
                 mporog2,
                 mstep,
                 mstepconst)
              VALUES
                (NNEWCOM, 0, MCONST, proc, NULL, mmin, mmax, 0, 0);
              -- Запись счета доходов в конкретную кассу                   
              if Upper(CACCOUNT) not in ('ALL', 'ВСЕ') then
                INSERT INTO OV_account
                  (IDAcc,
                   cVal,
                   idcnum,
                   idkassir,
                   ISubSystem,
                   IIDSubSystem,
                   Caccount,
                   ipp)
                VALUES
                  (0, 'RUR', icnum, 0, 1, NNEWCOM, doch, 0);
              end if;
            exception
              when others then
                null;
            end;
          
          when others then
            null;
        end;
      
      end if;
    
      --  вычислить счет кассы 
    
      if Upper(CACCOUNT) in ('ALL', 'ВСЕ') then
        for x in (SELECT idcnum FROM ov_cash WHERE i_status = 0) loop
          begin
            DELETE FROM OV_CPLAT
             WHERE IDOV_PLAT = idp
               AND IDCNUM = x.idcnum;
          
            INSERT INTO OV_CPLAT
              (IDOV_PLAT, IDCNUM)
            VALUES
              (idp, x.idcnum);
          exception
            when others then
              null;
          end;
        end loop;
      
        begin
          DELETE FROM OV_ACCOUNT
           WHERE IDCNUM = IDP
             AND cval = 'RUR'
             AND ISUBSYSTEM = 0
             AND IIDSUBSYSTEM = ICNUM
             AND IDKASSIR = 0;
          INSERT INTO OV_account
            (IDCNUM,
             idacc,
             cval,
             ISUBSYSTEM,
             IIDSUBSYSTEM,
             IDKASSIR,
             CACCOUNT,
             IPP)
          VALUES
            (idp, 1, 'RUR', 0, 0, 0, null, 1);
          INSERT INTO OV_account
            (IDCNUM,
             idacc,
             cval,
             ISUBSYSTEM,
             IIDSUBSYSTEM,
             IDKASSIR,
             CACCOUNT,
             IPP)
          VALUES
            (idp, 113, 'RUR', 0, 0, 0, TRAN, 113);
          -- VALUES(idp,113,'RUR',0,ICNUM,0,TRAN,113);
        exception
          when others then
            null;
        end;
      end if;
    
      if icnum is not null then
        begin
          DELETE FROM OV_CPLAT
           WHERE IDOV_PLAT = idp
             AND IDCNUM = ICNUM;
        
          INSERT INTO OV_CPLAT (IDOV_PLAT, IDCNUM) VALUES (idp, icnum);
        exception
          when others then
            null;
        end;
      
        begin
          DELETE FROM OV_ACCOUNT
           WHERE IDCNUM = IDP
             AND cval = 'RUR'
             AND ISUBSYSTEM = 0
             AND IIDSUBSYSTEM = ICNUM
             AND IDKASSIR = 0;
          INSERT INTO OV_account
            (IDCNUM,
             idacc,
             cval,
             ISUBSYSTEM,
             IIDSUBSYSTEM,
             IDKASSIR,
             CACCOUNT,
             IPP)
          VALUES
            (idp, 113, 'RUR', 0, ICNUM, 0, TRAN, 113);
          INSERT INTO OV_account
            (IDCNUM,
             idacc,
             cval,
             ISUBSYSTEM,
             IIDSUBSYSTEM,
             IDKASSIR,
             CACCOUNT,
             IPP)
          VALUES
            (idp, 1, 'RUR', 0, ICNUM, 0, null, 1);
          INSERT INTO OV_account
            (IDCNUM,
             idacc,
             cval,
             ISUBSYSTEM,
             IIDSUBSYSTEM,
             IDKASSIR,
             CACCOUNT,
             IPP)
          VALUES
            (idp, 1, 'RUR', 0, 0, 0, null, 1);
          INSERT INTO OV_account
            (IDCNUM,
             idacc,
             cval,
             ISUBSYSTEM,
             IIDSUBSYSTEM,
             IDKASSIR,
             CACCOUNT,
             IPP)
          VALUES
            (idp, 113, 'RUR', 0, 0, 0, TRAN, 113);
        exception
          when others then
            null;
        end;
      end if;
    
      if kbk is not null or okato is not null then
        begin
          delete from ov_pdept_info where IDOV_PLAT = idp;
          insert into ov_pdept_info
            (IDOV_PLAT, CBUDCODE, COKATOCODE)
          values
            (idp, decode(length(kbk), 20, kbk), okato);
        exception
          when others then
            null;
        end;
      
      end if;
    else
      msg(' строка ' || to_char(inumstr) || ' счет ' || CACCOUNT ||
          ' в кассах не найден!');
    end if;
  
    IF IRET = 2 THEN
      EXIT;
    END IF;
  
    IALL := IALL + 1;
    --COMMIT;
  END LOOP;
  ov_upacsh.UpdateNewOper(bb);
  DBMS_SQL_ADD.COMMIT;
  OLE2.Release_Obj(objUsedRange);
  OLE2.Release_Obj(objActiveSheet);
  OLE2.Release_Obj(objWorkBooks);
  OLE2.Release_Obj(objSheet);
  OLE2.Invoke(objAp, 'Quit');
  OLE2.Release_Obj(objAp);
  --IF IRET = 1 THEN RETURN FALSE;END IF;
  msg('Загрузка завершена');
  RETURN TRUE;
exception
  when form_trigger_failure then
    ROLLBACK;
  when others then
    ROLLBACK;
    msg('тип ' || ITYPE || CHR(10) || ' idp ' || idp || CHR(10) || ' bik ' ||
        rtrim(bik) || CHR(10) || ' inn ' || rtrim(inn) || CHR(10) ||
        ' кпп ' || rtrim(kpp) || CHR(10) || ' SCHET ' || SCHET || CHR(10) ||
        ' NSHET ' || nvl(NSHET, nameplat) || CHR(10) || ' OPERNAME ' ||
        NVL(OPERNAME, NSHET) || CHR(10) || ' idcC ' || idcC || CHR(10) ||
        ' cdest ' || cdest || CHR(10) || ' isv ' || isv || CHR(10) ||
        ' pdest ' || pdest || CHR(10) || ' isv ' || ispp || CHR(10) ||
        ' cSym ' || cSym || CHR(10) || ' okato ' || okato);
  
    msg(cpo || ' строка ' || to_char(inumstr) || ' ' || sqlcode || ' ' ||
        sqlerrm);
    OLE2.Release_Obj(objUsedRange);
    OLE2.Release_Obj(objActiveSheet);
    OLE2.Release_Obj(objWorkBooks);
    OLE2.Release_Obj(objSheet);
    OLE2.Invoke(objAp, 'Quit');
    OLE2.Release_Obj(objAp);
  
    RETURN TRUE;
END Get_XlsAFile;

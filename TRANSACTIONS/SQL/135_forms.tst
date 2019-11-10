PL/SQL Developer Test script 3.0
132
DECLARE
  D      Date := TO_DATE(:P3);
  vFil   Number := TO_NUMBER(:P4);
  iAu    Number := TO_NUMBER(NVL(:P5, 0));
  vIDstr Number := TO_NUMBER(:P6);
  cdrez  NUMBER := 0;
  mOst   NUMBER := 0;
  mClc   NUMBER := 0;
  mReP   NUMBER := 0;
  mRest  NUMBER := 0;
  mRePp  NUMBER := 0;
  /*====================*/
 /* Cursor cAcc IS
    Select B.ncdvagrid cArg,
           cdterms.get_DogACC(B.nCDVagrid, 6) cAcc,
           B.ICDVCLIENT iCus,
           F.idSMR idSMR,
           F.iSMRfil iFil,
           substr(cdterms.get_DogACC(B.nCDVagrid, 6), 1, 5) BS2,
           RATES.Cur_Rate_New(B.CCDVCURISO, (D - 1)) *
           nvl(CDBALANCE.get_CurSaldo(B.NCDVAGRID, 6, null, null, (D - 1)),
               0) mRePA, --просроченные проценты
           0 mRePpA
      from Fil_On2 F, cdv B, cda A
     Where A.idSmr = F.idSMR
       and F.iSMRfil = Decode(vFil, -1, F.iSMRfil, vFil)
       and B.ncdvagrid = A.ncdaagrid
       and (D - 1) - B.DCDVENDDATE <= 1095
       and substr(cdterms.get_DogACC(B.nCDVagrid, 6), 1, 5) != 45918 --исключаем приват.фонд
    
    union all
    
    Select B.ncdvagrid cArg,
           cdterms.get_DogACC(B.nCDVagrid, 101) cAcc,
           B.ICDVCLIENT iCus,
           F.idSMR idSMR,
           F.iSMRfil iFil,
           substr(cdterms.get_DogACC(B.nCDVagrid, 101), 1, 5) BS2,
           0 mRePA,
           RATES.Cur_Rate_New(B.CCDVCURISO, (D - 1)) *
           nvl(CDBALANCE.get_CurSaldo(B.NCDVAGRID, 101, null, null, (D - 1)),
               0) mRePpA --проценты на просроченные средства
      from Fil_On2 F, cdv B, cda A
     Where A.idSmr = F.idSMR
       and F.iSMRfil = Decode(vFil, -1, F.iSMRfil, vFil)
       and B.ncdvagrid = A.ncdaagrid
       and (D - 1) - B.DCDVENDDATE <= 1095
       and substr(cdterms.get_DogACC(B.nCDVagrid, 101), 1, 5) != 45918; --исключаем приват.фонд*/
       Cursor cAcc IS
    Select B.ncdvagrid cArg,
           cdterms.get_DogACC(B.nCDVagrid, 6) cAcc,
           B.ICDVCLIENT iCus,
           F.idSMR idSMR,
           F.iSMRfil iFil,
           substr(cdterms.get_DogACC(B.nCDVagrid, 6), 1, 5) BS2,
           RATES.Cur_Rate_New(B.CCDVCURISO, (D - 1)) *
           nvl(CDBALANCE.get_CurSaldo(B.NCDVAGRID, 6, null, null, (D - 1)),
               0) mRePA, --просроченные проценты
           0 mRePpA
      from Fil_On2 F, cdv B, cda A
     Where A.idSmr = F.idSMR
       and F.iSMRfil = Decode(vFil, -1, F.iSMRfil, vFil)
       and B.ncdvagrid = A.ncdaagrid
       and (D - 1) -
           greatest(B.DCDVENDDATE,
                    (select nvl(max(DCDEDATE),
                                to_date('01.01.1900', 'dd.mm.yyyy'))
                       from cde e
                      where e.NCDEAGRID = a.NCDAAGRID
                        and e.icdetype in (2, 3, 12, 13, 72, 113, 172))) > 1095
       and substr(cdterms.get_DogACC(B.nCDVagrid, 6), 1, 5) != 45918 --исключаем приват.фонд
    
    union all
    
    Select B.ncdvagrid cArg,
           cdterms.get_DogACC(B.nCDVagrid, 101) cAcc,
           B.ICDVCLIENT iCus,
           F.idSMR idSMR,
           F.iSMRfil iFil,
           substr(cdterms.get_DogACC(B.nCDVagrid, 101), 1, 5) BS2,
           0 mRePA,
           RATES.Cur_Rate_New(B.CCDVCURISO, (D - 1)) *
           nvl(CDBALANCE.get_CurSaldo(B.NCDVAGRID, 101, null, null, (D - 1)),
               0) mRePpA --проценты на просроченные средства
      from Fil_On2 F, cdv B, cda A
     Where A.idSmr = F.idSMR
       and F.iSMRfil = Decode(vFil, -1, F.iSMRfil, vFil)
       and B.ncdvagrid = A.ncdaagrid
       and (D - 1) -
           greatest(B.DCDVENDDATE,
                    (select nvl(max(DCDEDATE),
                                to_date('01.01.1900', 'dd.mm.yyyy'))
                       from cde e
                      where e.NCDEAGRID = a.NCDAAGRID
                        and e.icdetype in (2, 3, 12, 13, 72, 113, 172))) > 1095
       and substr(cdterms.get_DogACC(B.nCDVagrid, 101), 1, 5) != 45918; --исключаем приват.фонд
  /*====================*/
begin
  For rAcc in cAcc loop
    mRest := rAcc.mRePA + rAcc.mRePpA;
  
    if mRest != 0 then
    
      IF iAu = 1 THEN
        INSERT INTO NAUS_FOR_CHECK
          (inausfil,
           inauscus,
           CNAUSDOG,
           cnausacc, /*inausbs2,*/
           mnaussumn,
           mnaussumm,
           inausstrid)
        VALUES
          (rAcc.iFil,
           rAcc.iCus,
           rAcc.cArg,
           rAcc.cAcc /*, rAcc.BS2*/,
           mRest,
           ROUND(mRest / 1000),
           vIDstr);
      END IF;
      mOst := mOst + mRest;
    END IF;
  End loop;
  :o1 := mOST;
Exception
  when Others then
    dbms_output.put_line(Substr('Условие !!! -> ошибка : ' || SqlErrM,
                                1,
                                255));
    :o1 := null;
END;
5
P3
1
01.10.2019
12
P4
1
-1
3
P5
1
1
3
P6
1
33004
3
o1
1
267648446,5969
5
0

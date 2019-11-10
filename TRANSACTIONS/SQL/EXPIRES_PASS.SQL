
select username, account_status, EXPIRY_DATE from dba_users where username IN ('IBANK2','MONITORING');




alter profile DEFAULT limit PASSWORD_LIFE_TIME   UNLIMITED;

alter profile DEFAULT limit PASSWORD_REUSE_TIME    UNLIMITED;
prompt PL/SQL Developer Export Tables for user XXI@ODB
prompt Created by saidp on 30 ќкт€брь 2019 г.
set feedback off
set define off

prompt Creating Z_SB_LOG_AMRA...
create table Z_SB_LOG_AMRA
(
  recdate  TIMESTAMP(6),
  paydate  DATE,
  desc_    VARCHAR2(1000),
  sess_id  NUMBER,
  deb_cred VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 80K
    next 1M
    minextents 1
    maxextents unlimited
  );
grant select, insert, update, delete on Z_SB_LOG_AMRA to ODB;

prompt Disabling triggers for Z_SB_LOG_AMRA...
alter table Z_SB_LOG_AMRA disable all triggers;
prompt Deleting Z_SB_LOG_AMRA...
delete from Z_SB_LOG_AMRA;
commit;
prompt Loading Z_SB_LOG_AMRA...
prompt Table is empty
prompt Enabling triggers for Z_SB_LOG_AMRA...
alter table Z_SB_LOG_AMRA enable all triggers;

set feedback on
set define on
prompt Done

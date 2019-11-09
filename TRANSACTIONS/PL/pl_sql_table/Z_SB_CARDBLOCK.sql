prompt PL/SQL Developer Export Tables for user XXI@ODB
prompt Created by saidp on 9 Ноябрь 2019 г.
set feedback off
set define off

prompt Creating Z_SB_CARDBLOCK...
create table Z_SB_CARDBLOCK
(
  cardid   NUMBER not null,
  dateoper DATE not null,
  domainac VARCHAR2(100),
  ground   VARCHAR2(500)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
create index CARDID_ on Z_SB_CARDBLOCK (CARDID)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;
grant select, insert, update, delete on Z_SB_CARDBLOCK to ODB;

prompt Disabling triggers for Z_SB_CARDBLOCK...
alter table Z_SB_CARDBLOCK disable all triggers;
prompt Deleting Z_SB_CARDBLOCK...
delete from Z_SB_CARDBLOCK;
commit;
prompt Loading Z_SB_CARDBLOCK...
prompt Table is empty
prompt Enabling triggers for Z_SB_CARDBLOCK...
alter table Z_SB_CARDBLOCK enable all triggers;

set feedback on
set define on
prompt Done

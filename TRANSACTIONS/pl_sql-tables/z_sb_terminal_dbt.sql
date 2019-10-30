prompt PL/SQL Developer Export Tables for user XXI@ODB
prompt Created by saidp on 30 ������� 2019 �.
set feedback off
set define off

prompt Creating Z_SB_TERMINAL_DBT...
create table Z_SB_TERMINAL_DBT
(
  name         VARCHAR2(99) not null,
  department   INTEGER not null,
  address      VARCHAR2(199) not null,
  account      VARCHAR2(20) not null,
  acc_30232_01 VARCHAR2(20) not null,
  acc_30232_02 VARCHAR2(20) not null,
  acc_30232_03 VARCHAR2(20) not null,
  acc_30232_04 VARCHAR2(20),
  acc_30232_05 VARCHAR2(20) not null,
  acc_70107    VARCHAR2(20) not null,
  acc_30232_06 VARCHAR2(20)
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
create index KEY01_ on Z_SB_TERMINAL_DBT (NAME)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index KEY11_ on Z_SB_TERMINAL_DBT (DEPARTMENT, NAME)
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table Z_SB_TERMINAL_DBT
  add constraint NAME_TERM primary key (NAME);

prompt Disabling triggers for Z_SB_TERMINAL_DBT...
alter table Z_SB_TERMINAL_DBT disable all triggers;
prompt Deleting Z_SB_TERMINAL_DBT...
delete from Z_SB_TERMINAL_DBT;
commit;
prompt Loading Z_SB_TERMINAL_DBT...
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0001', 2, '���������� �����, ���� "��������"', '20208810900020010001', '30232810500020010001', '30232810800020010002', '30232810100020010003', null, '30232810700020010005', '70107810400021720103', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0002', 0, '�. �����, ��. ���������, 10/12', '20208810700000010001', '30232810300000010001', '30232810600000010002', '30232810900000010003', null, '30232810500000010005', '70107810200001720103', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0004', 0, '�. �����, ��. �.�. ������, 2', '20208810000000010002', '30232810300000010001', '30232810600000010002', '30232810900000010003', null, '30232810500000010005', '70107810200001720103', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0003', 5, '�������� �����, ��� "����"', '20208810200050010001', '30232810800050010001', '30232810100050010002', '30232810400050010003', null, '30232810000050010005', '70107810500051720112', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0005', 5, '�������� �����, ��� "�����"', '20208810500050010002', '30232810800050010001', '30232810100050010002', '30232810400050010003', null, '30232810000050010005', '70107810500051720112', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0009', 5, '�������� �����, ��� "����"', '20208810800050010003', '30232810800050010001', '30232810100050010002', '30232810400050010003', null, '30232810000050010005', '70107810500051720112', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0007', 0, '�. �����, ��. �.�. ������, 2 (���������)', '20208810300000010003', '30232810300000010001', '30232810600000010002', '30232810900000010003', null, '30232810500000010005', '70107810200001720103', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('�� 0008', 0, '�.����� ��.������ �������������,���� ���, 2 ����, ���. 10', '20208810600000010004', '30232810300000010001', '30232810600000010002', '30232810900000010003', null, '30232810500000010005', '70107810200001720103', null);
insert into Z_SB_TERMINAL_DBT (name, department, address, account, acc_30232_01, acc_30232_02, acc_30232_03, acc_30232_04, acc_30232_05, acc_70107, acc_30232_06)
values ('SB 0001', 0, '�. �����, ��. ���������, 10/12', '20208810900000010005', '30232810300000010001', '30232810600000010002', '30232810900000010003', ' ', '30232810500000010005', '70107810200001720103', '30232810800000010006');
commit;
prompt 9 records loaded
prompt Enabling triggers for Z_SB_TERMINAL_DBT...
alter table Z_SB_TERMINAL_DBT enable all triggers;

set feedback on
set define on
prompt Done

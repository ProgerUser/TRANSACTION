prompt PL/SQL Developer Export Tables for user XXI@ODB
prompt Created by saidp on 30 Октябрь 2019 г.
set feedback off
set define off

prompt Creating Z_SB_TERMSERV_DBT...
create table Z_SB_TERMSERV_DBT
(
  name          VARCHAR2(99),
  idterm        VARCHAR2(99),
  account       VARCHAR2(20),
  account2      VARCHAR2(20),
  account3      VARCHAR2(20),
  account4      VARCHAR2(20),
  account5      VARCHAR2(20),
  inn           VARCHAR2(20),
  kpp           VARCHAR2(20),
  kor_bank_nbra VARCHAR2(20),
  acc_rec       VARCHAR2(20),
  kbk           VARCHAR2(30),
  okato         VARCHAR2(20),
  stat          VARCHAR2(20),
  acc_name      VARCHAR2(100),
  bo1           NUMBER,
  bo2           NUMBER,
  comission     NUMBER
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
create index TERMSERVKEY0 on Z_SB_TERMSERV_DBT (NAME, IDTERM)
  tablespace INDEXES
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
create index TERMSERVKEY1 on Z_SB_TERMSERV_DBT (ACCOUNT)
  tablespace INDEXES
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

prompt Disabling triggers for Z_SB_TERMSERV_DBT...
alter table Z_SB_TERMSERV_DBT disable all triggers;
prompt Deleting Z_SB_TERMSERV_DBT...
delete from Z_SB_TERMSERV_DBT;
commit;
prompt Loading Z_SB_TERMSERV_DBT...
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Дет.сад СБЕРБАНК', 'SB 0001', '40911810700000000168', null, null, null, null, '12002971', '111000603', null, '40603810200000000004', '20300000000000000130', '401000', '01', 'Минфин (Управление Образования Администрации г.Сухум)', 4, 0, 50);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Налог Специальный СБ', 'СБ 0002', '40911810800000010003', null, null, null, null, '12000001', '111000003', null, '40101810300000000082', '03010502010030000110', '401000', '01', 'Минфин (ИМНС РА по г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата детского сада СБ', 'СБ 0002', '40911810500000010002', null, null, null, null, '12002971', '111000603', null, '40603810200000000004', '20300000000000000130', '401000', '01', 'Минфин (Управление Образования Администрации г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0001', '40911810400020010001', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0002', '40911810200000010001', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('НДС', 'СБ 0003', '40911810700050010001', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810301000010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0002', '40911810100000010004', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сбор и прочие платежи', 'СБ 0004', '40911810400000010005', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811202000010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на пиво', 'СБ 0004', '40911810600000010009', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302050010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0004', '40911810100000010004', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0002', '30233810600000010001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сбор и прочие платежи', 'СБ 0003', '40911810700050010014', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811202000010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина ввозная', 'СБ 0003', '40911810000050010002', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201010010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина ввозная', 'СБ 0004', '40911810700000010006', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201010010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('НДС', 'СБ 0004', '40911810000000010007', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810301000010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата за заказ-наряд', 'СБ 0004', '40911810300000010008', null, null, null, null, '12004881', '111001035', null, '40503810000000000016', '02800000000000000130', '401000', '20', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата за заказ-наряд', 'СБ 0003', '40911810000050010015', null, null, null, null, '12004881', '111001035', null, '40503810000000000016', '02800000000000000130', '401000', '20', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на пиво', 'СБ 0003', '40911810100050010012', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302050010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0003', '40911810300050010016', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0003', '30233810710550010001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0001', '30232810800020010002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0004', '30233810600000010001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Совокупный т/п за личные товары', 'СБ 0003', '40911810600050010017', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811203010010000180', '401000', '01', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Аквафон', 'СБ 0001', '40911810700020010002', null, null, null, null, '11000572', '111000171', null, null, null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0001', '40911810000020010003', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('НДС', 'СБ 0002', '40911810000000010007', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810301000010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сбор и прочие платежи', 'СБ 0002', '40911810400000010005', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811202000010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина ввозная', 'СБ 0002', '40911810700000010006', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201010010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата за заказ-наряд', 'СБ 0002', '40911810300000010008', null, null, null, null, '12004881', '111001035', null, '40503810000000000016', '02800000000000000130', '401000', '20', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на пиво', 'СБ 0002', '40911810600000010009', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302050010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Таможенные пошлины, налоги по единым ставкам в отношении товаров для личного пользования', 'СБ 0003', '40911810900050010018', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Таможенные пошлины, налоги по единым ставкам в отношении товаров для личного пользования', 'СБ 0002', '40911810000000010010', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Таможенные пошлины, налоги по единым ставкам в отношении товаров для личного пользования', 'СБ 0004', '40911810000000010010', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0004', '40911810200000010001', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Совокупный т/п за личные товары', 'СБ 0002', '40911810300000010011', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811203010010000180', '401000', '01', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Совокупный т/п за личные товары', 'СБ 0004', '40911810300000010011', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811203010010000180', '401000', '01', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина вывозная', 'СБ 0003', '40911810300050010003', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201020010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0005', '40911810200050010019', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0005', '40911810300050010016', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на табак', 'СБ 0003', '40911810600050010020', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302060010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на табак', 'СБ 0004', '40911810600000010012', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302060010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата детского сада СБ', 'СБ 0004', '40911810500000010002', null, null, null, null, '12002971', '111000603', null, '40603810200000000004', '20300000000000000130', '401000', '01', 'Минфин (Управление Образования Администрации г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Аквафон', 'СБ 0005', '40911810900050010021', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на ювелирные', 'СБ 0004', '40911810900000010013', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302070010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата детского сада СБ', 'СБ 0003', '40911810200050010022', null, null, null, null, '12002971', '111000603', null, '40603810200000000004', '20300000000000000130', '401000', '01', 'Минфин (Управление Образования Администрации г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0009', '40911810300050010016', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Аквафон', 'СБ 0005', '40911810900050010021', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на пиво', 'СБ 0009', '40911810100050010012', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302050010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на табак', 'СБ 0009', '40911810600050010020', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302060010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('НДС', 'СБ 0009', '40911810700050010001', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810301000010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата детского сада СБ', 'СБ 0009', '40911810200050010022', null, null, null, null, '12002971', '111000603', null, '40603810200000000004', '20300000000000000130', '401000', '01', 'Минфин (Управление Образования Администрации г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата за заказ-наряд', 'СБ 0009', '40911810000050010015', null, null, null, null, '12004881', '111001035', null, '40503810000000000016', '02800000000000000130', '401000', '20', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина ввозная', 'СБ 0009', '40911810000050010002', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201010010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сбор и прочие платежи', 'СБ 0009', '40911810700050010014', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811202000010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0009', '30233810710550010001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Совокупный т/п за личные товары', 'СБ 0009', '40911810600050010017', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811203010010000180', '401000', '01', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Таможенные пошлины, налоги по единым ставкам в отношении товаров для личного пользования', 'СБ 0009', '40911810900050010018', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0009', '40911810200050010019', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина вывозная', 'СБ 0009', '40911810300050010003', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201020010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Экран.Супер+Сухум', 'СБ 0002', '40911810200000010014', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Экран.Супер', 'СБ 0002', '40911810200000010014', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0007', '40911810100000010004', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на пиво', 'СБ 0007', '40911810600000010009', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302050010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на табак', 'СБ 0007', '40911810600000010012', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302060010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на ювелирные', 'СБ 0007', '40911810900000010013', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302070010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('НДС', 'СБ 0007', '40911810000000010007', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810301000010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата детского сада СБ', 'СБ 0007', '40911810500000010002', null, null, null, null, '12002971', '111000603', null, '40603810200000000004', '20300000000000000130', '401000', '01', 'Минфин (Управление Образования Администрации г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата за заказ-наряд', 'СБ 0007', '40911810300000010008', null, null, null, null, '12004881', '111001035', null, '40503810000000000016', '02800000000000000130', '401000', '20', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Пошлина ввозная', 'СБ 0007', '40911810700000010006', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811201010010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сбор и прочие платежи', 'СБ 0007', '40911810400000010005', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811202000010000180', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0007', '30233810600000010001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Совокупный т/п за личные товары', 'СБ 0007', '40911810300000010011', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02811203010010000180', '401000', '01', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Таможенные пошлины, налоги по единым ставкам в отношении товаров для личного пользования', 'СБ 0004', '40911810000000010010', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0007', '40911810200000010001', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Налог Подоходный СБ', 'СБ 0003', '40911810500050010023', null, null, null, null, '12000003', '311000172', null, '40101810300000000082', '03010102010020000110', '401000', '01', 'Минфин (ИМНС РА по г.Сухум)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акцизные марки (табачная продукция)', 'СБ 0003', '40911810600050010020', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акцизные марки (табачная продукция)', 'СБ 0004', '40911810600000010012', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акцизные марки (табачная продукция)', 'СБ 0007', '40911810600000010012', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акцизные марки (табачная продукция)', 'СБ 0009', '40911810600050010020', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('А-Мобайл СБ', 'СБ 0008', '40911810100000010004', null, null, null, null, '11001729', '111000382', null, '40702810400000000622', null, null, null, null, 1, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0008', '40911810200000010001', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Сдача СБ', 'СБ 0008', '30233810600000010001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Экран.Супер', 'СБ 0008', '40911810200000010014', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Экран.Супер+Сухум', 'СБ 0008', '40911810200000010014', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Оплата за заказ-наряд', 'СБ 0008', '40911810300000010008', null, null, null, null, '12004881', '111001035', null, '40503810000000000016', '02800000000000000130', '401000', '20', 'Минфин (ГТК РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Штрафы ГАИ МВД РА СБ', 'СБ 0003', '40911810200050010019', null, null, null, null, '12002647', '111000548', null, '40101810300000000082', '01211501116010000140', '401000', '15', 'Минфин (МВД РА)', 4, 0, null);
insert into Z_SB_TERMSERV_DBT (name, idterm, account, account2, account3, account4, account5, inn, kpp, kor_bank_nbra, acc_rec, kbk, okato, stat, acc_name, bo1, bo2, comission)
values ('Акциз на ювелирные', 'СБ 0009', '40911810800050000306', null, null, null, null, '12004881', '111001035', null, '40101810300000000082', '02810302060010000110', '401000', '15', 'Минфин (ГТК РА)', 4, 0, null);
commit;
prompt 87 records loaded
prompt Enabling triggers for Z_SB_TERMSERV_DBT...
alter table Z_SB_TERMSERV_DBT enable all triggers;

set feedback on
set define on
prompt Done

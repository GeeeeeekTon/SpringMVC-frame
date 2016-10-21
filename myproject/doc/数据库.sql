drop table if exists da_retrain_sample_test;

drop table if exists da_retrain_sample_user;

/*==============================================================*/
/* Table: da_retrain_sample_test                                */
/*==============================================================*/
create table da_retrain_sample_test
(
   id                   bigint not null auto_increment comment '���',
   login_name           varchar(64) comment '��¼��',
   password             varchar(64) comment '����',
   login_date           datetime comment '��¼ʱ��',
   type                 int comment '����',
   is_valid             int comment '�Ƿ���Ч',
   create_date          datetime comment '��������',
   create_user          bigint comment '������',
   update_date          datetime comment '��������',
   update_user          bigint comment '������',
   primary key (id)
);

alter table da_retrain_sample_test comment '���Ա�������';

/*==============================================================*/
/* Table: da_retrain_sample_user                                */
/*==============================================================*/
create table da_retrain_sample_user
(
   id                   bigint not null auto_increment comment '���',
   login_name           varchar(64) comment '��¼��',
   password             varchar(64) comment '����',
   login_date           datetime comment '��¼ʱ��',
   type                 int comment '����',
   is_valid             int comment '�Ƿ���Ч',
   create_date          datetime comment '��������',
   create_user          bigint comment '������',
   update_date          datetime comment '��������',
   update_user          bigint comment '������',
   primary key (id)
);

alter table da_retrain_sample_user comment '�û���������';
drop table if exists da_retrain_sample_test;

drop table if exists da_retrain_sample_user;

/*==============================================================*/
/* Table: da_retrain_sample_test                                */
/*==============================================================*/
create table da_retrain_sample_test
(
   id                   bigint not null auto_increment comment '编号',
   login_name           varchar(64) comment '登录名',
   password             varchar(64) comment '密码',
   login_date           datetime comment '登录时间',
   type                 int comment '类型',
   is_valid             int comment '是否有效',
   create_date          datetime comment '创建日期',
   create_user          bigint comment '创建人',
   update_date          datetime comment '更新日期',
   update_user          bigint comment '更新人',
   primary key (id)
);

alter table da_retrain_sample_test comment '测试表（样例）';

/*==============================================================*/
/* Table: da_retrain_sample_user                                */
/*==============================================================*/
create table da_retrain_sample_user
(
   id                   bigint not null auto_increment comment '编号',
   login_name           varchar(64) comment '登录名',
   password             varchar(64) comment '密码',
   login_date           datetime comment '登录时间',
   type                 int comment '类型',
   is_valid             int comment '是否有效',
   create_date          datetime comment '创建日期',
   create_user          bigint comment '创建人',
   update_date          datetime comment '更新日期',
   update_user          bigint comment '更新人',
   primary key (id)
);

alter table da_retrain_sample_user comment '用户表（样例）';
drop table if exists user;

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   CREATE_DATE datetime DEFAULT NULL COMMENT '创建时间',
   ID                   bigint not null auto_increment comment '主键',
   USER_NAME            varchar(32) comment '用户名',
   EMAIL                varchar(128) comment '邮箱',
   TELEPHONE            varchar(32) comment '电话号码',
   PASSWORD             varchar(256) comment '密码',
   HEADER_URL           varchar(256) comment '头像',
   LOGIN_STATUS         tinyint default 1 comment '登录状态  1:允许，0：不允许',
   primary key (ID),
   unique key AK_uq_telephone (TELEPHONE),
   unique key AK_uq_email (EMAIL)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table user comment '用户表';


drop table if exists role;

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   CREATE_DATE datetime DEFAULT NULL COMMENT '创建时间',
   ID                   bigint not null auto_increment comment '主键',
   ROLE_NAME            varchar(32) comment '角色名称',
   ROLE_CODE            varchar(16) comment '角色编码',
   COMMENT              varchar(256) comment '备注说明',
   STATUS               char(10) comment '状态  1：正常 0 封存',
   primary key (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table role comment '角色表';

drop table if exists user_role_rel;

/*==============================================================*/
/* Table: user_role_rel                                         */
/*==============================================================*/
create table user_role_rel
(
   CREATE_DATE datetime DEFAULT NULL COMMENT '创建时间',
   ID                   bigint not null auto_increment comment '主键',
   ROLE_ID              bigint comment '关联角色',
   USER_ID              bigint comment '关联用户表',
   primary key (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table user_role_rel comment '用户角色关联';


drop table if exists permission;

/*==============================================================*/
/* Table: permission                                            */
/*==============================================================*/
create table permission
(
   CREATE_DATE datetime DEFAULT NULL COMMENT '创建时间',
   ID                   bigint not null auto_increment comment '主键',
   AUTH_NAME            varchar(256) comment '权限名称',
   AUTH_CODE            varchar(16) comment '权限编码',
   AUTH_TYPE            varchar(16) comment '权限类型  ',
   STATUS               char(10) comment '状态  1：正常 0 封存',
   REQUEST_URL          varchar(256) comment '请求地址',
   PARENT_PERMISSION    bigint comment '父权限',
   primary key (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table permission comment '权限表';

drop table if exists role_permission_rel;

/*==============================================================*/
/* Table: role_permission_rel                                   */
/*==============================================================*/
create table role_permission_rel
(
   CREATE_DATE datetime DEFAULT NULL COMMENT '创建时间',
   ID                   bigint not null auto_increment comment '主键',
   ROLE_ID              bigint comment '关联角色',
   PERMISSION_ID        bigint comment '关联权限',
   primary key (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table role_permission_rel comment '角色权限表';

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(48) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;




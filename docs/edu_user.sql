/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : edu_user

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 09/03/2020 16:34:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL,
  `refresh_token_validity` int(11) NULL DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('edu_pc_teach', NULL, '$2a$10$hdmA.JdfloV27KRzQ3Xjx.rR9ti.UF/shzooCfv2jVT.0ySNmo3Iy', NULL, 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `AUTH_NAME` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `AUTH_CODE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限编码',
  `AUTH_TYPE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限类型  ',
  `STATUS` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态  1：正常 0 封存',
  `REQUEST_URL` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `PARENT_PERMISSION` bigint(16) NULL DEFAULT NULL COMMENT '父权限',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('2020-02-26 14:09:12', 1, '中台系统', 'CENTER', 'SYSTEM', '1', NULL, -1);
INSERT INTO `permission` VALUES ('2020-02-26 14:10:24', 2, '中台-首页', 'CENTEER_INDEX', 'MENU', '1', NULL, 1);
INSERT INTO `permission` VALUES ('2020-02-26 14:11:08', 3, '中台-用户管理', 'CENTEER_USER', 'MENU', '1', NULL, 1);
INSERT INTO `permission` VALUES ('2020-02-26 14:11:50', 4, '中台-用户列表', 'CENTER_USER_LIST', 'BUTTON', '1', '/user/user/list', 3);
INSERT INTO `permission` VALUES ('2020-02-26 14:13:02', 5, '中台-用户添加', 'CENTER_USER_ADD', 'BUTTON', '1', '/user/user/save', 3);
INSERT INTO `permission` VALUES ('2020-02-26 14:16:04', 6, '中台-用户查看', 'CENTER_USER_VIEW', 'BUTTON', '1', '/user/user/*', 3);
INSERT INTO `permission` VALUES ('2020-03-04 15:33:59', 7, 'IM-发送消息', 'IM-SEND-MESSAGE', 'BUTTON', '1', '/im/send', 3);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ROLE_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `ROLE_CODE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `COMMENT` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `STATUS` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态  1：正常 0 封存',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('2020-02-21 15:20:20', 1, '超级管理员', 'SUPERADMIN', '备注', '1');
INSERT INTO `role` VALUES ('2020-02-26 14:41:20', 2, '中台管理员', 'CENTER_ADMIN', '备注', '1');

-- ----------------------------
-- Table structure for role_permission_rel
-- ----------------------------
DROP TABLE IF EXISTS `role_permission_rel`;
CREATE TABLE `role_permission_rel`  (
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ROLE_ID` bigint(20) NULL DEFAULT NULL COMMENT '关联角色',
  `PERMISSION_ID` bigint(20) NULL DEFAULT NULL COMMENT '关联权限',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission_rel
-- ----------------------------
INSERT INTO `role_permission_rel` VALUES ('2020-02-21 17:43:33', 1, 1, 1);
INSERT INTO `role_permission_rel` VALUES ('2020-02-25 10:56:24', 2, 1, 2);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:17:47', 3, 1, 3);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:17:57', 4, 1, 4);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:18:09', 5, 1, 5);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:18:18', 6, 1, 6);
INSERT INTO `role_permission_rel` VALUES ('2020-02-21 17:43:33', 7, 2, 1);
INSERT INTO `role_permission_rel` VALUES ('2020-02-25 10:56:24', 8, 2, 2);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:17:47', 9, 2, 3);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:17:57', 10, 2, 4);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:18:09', 11, 2, 5);
INSERT INTO `role_permission_rel` VALUES ('2020-02-26 14:18:18', 12, 2, 6);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_NAME` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `EMAIL` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `TELEPHONE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `PASSWORD` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `HEADER_URL` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `LOGIN_STATUS` tinyint(4) NULL DEFAULT 1 COMMENT '登录状态  1:允许，0：不允许',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `AK_uq_telephone`(`TELEPHONE`) USING BTREE,
  UNIQUE INDEX `AK_uq_email`(`EMAIL`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('2020-02-20 11:21:43', 1, '大仙', 'z', NULL, '$2a$10$XS3tc9SAaavYHcbuNLwMqOqRbNccaCuR8sxru2T2o2o/qy.8Ra9sm', 'http://www.baidu.com', 1);
INSERT INTO `user` VALUES ('2020-02-26 16:12:40', 10, '仙哥', NULL, '13252808069', '$2a$10$EiOjQwXe9A0SZqpIOvfdSuFWPNz2x.UL27wcQYGdSpgPkmOGyuc8u', 'https://www.baidu.com', 1);
INSERT INTO `user` VALUES ('2020-03-09 11:29:09', 11, '11', 'zz', NULL, '$2a$10$oLtrq2gG6ZUgvMCtkaIY6e1Jp.n/Dus0UhgiiBV9gpElXYvdO0tC2', NULL, NULL);

-- ----------------------------
-- Table structure for user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `user_role_rel`;
CREATE TABLE `user_role_rel`  (
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ROLE_ID` bigint(20) NULL DEFAULT NULL COMMENT '关联角色',
  `USER_ID` bigint(20) NULL DEFAULT NULL COMMENT '关联用户表',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_rel
-- ----------------------------
INSERT INTO `user_role_rel` VALUES ('2020-02-21 17:42:56', 1, 1, 1);
INSERT INTO `user_role_rel` VALUES ('2020-02-26 14:41:46', 2, 2, 1);

SET FOREIGN_KEY_CHECKS = 1;

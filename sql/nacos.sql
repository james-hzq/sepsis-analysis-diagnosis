/*
 Navicat Premium Data Transfer

 Source Server         : local-mysql-8.0.42-winx64
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3307
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 03/05/2025 16:11:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'group_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '配置的模式',
  `encrypted_data_key` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'redis-config.yaml', 'sepsis-mimic-common', 'spring:\n  data:\n    redis:\n      host: 127.0.0.1\n      port: 6379\n      password: xxxxxx\n      database: 1\n      timeout: 10s\n      lettuce:\n        pool:\n          min-idle: 0\n          max-idle: 8\n          max-active: 8\n          max-wait: -1ms', '43b1d3d6b2c783e9c7e00e09c73b39f8', '2025-05-03 15:11:22', '2025-05-03 16:11:27', NULL, '192.168.0.107', '', 'sepsis-mimic-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'druid-config.yaml', 'sepsis-mimic-common', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: xxxxxx\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      initialSize: 5\n      minIdle: 10\n      maxActive: 20 \n      maxWait: 60000\n      connectTimeout: 30000\n      socketTimeout: 60000\n      timeBetweenEvictionRunsMillis: 60000\n      minEvictableIdleTimeMillis: 300000\n      maxEvictableIdleTimeMillis: 900000\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', '5d601f3b388b98b19f5ccf101ad3a27e', '2025-05-03 15:12:06', '2025-05-03 16:11:36', NULL, '192.168.0.107', '', 'sepsis-mimic-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'sepsis-gateway-dev.yaml', 'sepsis-mimic-service', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:12:33', '2025-05-03 15:12:33', NULL, '0:0:0:0:0:0:0:1', '', 'sepsis-mimic-dev', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (4, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', 'spring:\n  jpa:\n    show-sql: true \n    open-in-view: false\n    database-platform: org.hibernate.dialect.MySQLDialect\n    hibernate:\n      ddl-auto: update', '2289daf69bcf183a9bbdcb312de42023', '2025-05-03 15:12:54', '2025-05-03 15:19:37', NULL, '0:0:0:0:0:0:0:1', '', 'sepsis-mimic-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'sepsis-auth-dev.yaml', 'sepsis-mimic-service', 'spring:\n  jpa:\n    show-sql: true \n    open-in-view: false\n    database-platform: org.hibernate.dialect.MySQLDialect\n    hibernate:\n      ddl-auto: update', '2289daf69bcf183a9bbdcb312de42023', '2025-05-03 15:13:19', '2025-05-03 16:00:37', NULL, '192.168.0.107', '', 'sepsis-mimic-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (6, 'sepsis-analysis-dev.yaml', 'sepsis-mimic-service', 'spring:\n  jpa:\n    show-sql: true \n    open-in-view: false\n    database-platform: org.hibernate.dialect.MySQLDialect\n    hibernate:\n      ddl-auto: update', '2289daf69bcf183a9bbdcb312de42023', '2025-05-03 15:13:41', '2025-05-03 16:00:49', NULL, '192.168.0.107', '', 'sepsis-mimic-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'sepsis-diagnosis-dev.yaml', 'sepsis-mimic-service', 'spring:\n  jpa:\n    show-sql: true \n    open-in-view: false\n    database-platform: org.hibernate.dialect.MySQLDialect\n    hibernate:\n      ddl-auto: update', '2289daf69bcf183a9bbdcb312de42023', '2025-05-03 15:14:05', '2025-05-03 16:00:55', NULL, '192.168.0.107', '', 'sepsis-mimic-dev', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_gray
-- ----------------------------
DROP TABLE IF EXISTS `config_info_gray`;
CREATE TABLE `config_info_gray`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'group_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'md5',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT 'src_user',
  `src_ip` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'src_ip',
  `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
  `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT 'tenant_id',
  `gray_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'gray_name',
  `gray_rule` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'gray_rule',
  `encrypted_data_key` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfogray_datagrouptenantgray`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `gray_name` ASC) USING BTREE,
  INDEX `idx_dataid_gmt_modified`(`data_id` ASC, `gmt_modified` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'config_info_gray' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_gray
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------
INSERT INTO `group_capacity` VALUES (1, '', 0, 7, 0, 0, 0, 0, '2025-05-03 15:08:37', '2025-05-03 16:04:12');

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL COMMENT 'id',
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT '密钥',
  `publish_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT 'formal' COMMENT 'publish type gray or formal',
  `gray_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'gray name',
  `ext_info` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'ext info',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: jameshzq\r\n      database: 1\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', '06c4d3c319ce95c73c0ac6ea3e9c2ebe', '2025-04-13 10:28:15', '2025-04-13 10:28:15', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 2, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3306/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: jameshzq\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      # 初始连接数\r\n      initialSize: 5\r\n      # 最小连接池数量\r\n      minIdle: 10\r\n      # 最大连接池数量\r\n      maxActive: 20 \r\n      # 配置获取连接等待超时的时间\r\n      maxWait: 60000\r\n      # 配置连接超时时间\r\n      connectTimeout: 30000\r\n      # 配置网络超时时间\r\n      socketTimeout: 60000\r\n      # 配置间隔多久才进行一次检测，单位是毫秒\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n      minEvictableIdleTimeMillis: 300000\r\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\r\n      maxEvictableIdleTimeMillis: 900000\r\n      # 配置检测连接是否有效\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', '504779da6bb4e21ff50e7e93d4769dc3', '2025-04-13 10:29:18', '2025-04-13 10:29:19', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 3, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-04-13 10:30:21', '2025-04-13 10:30:22', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 4, 'sepsis-gateway-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-04-13 10:31:33', '2025-04-13 10:31:34', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (4, 5, 'sepsis-gateway-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-04-13 10:32:42', '2025-04-13 10:32:42', 'nacos', '192.168.142.1', 'U', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 6, 'sepsis-auth-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-04-13 10:34:20', '2025-04-13 10:34:20', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 7, 'sepsis-analysis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-04-13 10:36:05', '2025-04-13 10:36:05', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 8, 'sepsis-diagnosis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-04-13 10:36:57', '2025-04-13 10:36:58', NULL, '192.168.142.1', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (1, 9, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: jameshzq\r\n      database: 1\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', '06c4d3c319ce95c73c0ac6ea3e9c2ebe', '2025-05-02 17:54:23', '2025-05-02 17:54:23', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (2, 10, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3306/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: jameshzq\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      # 初始连接数\r\n      initialSize: 5\r\n      # 最小连接池数量\r\n      minIdle: 10\r\n      # 最大连接池数量\r\n      maxActive: 20 \r\n      # 配置获取连接等待超时的时间\r\n      maxWait: 60000\r\n      # 配置连接超时时间\r\n      connectTimeout: 30000\r\n      # 配置网络超时时间\r\n      socketTimeout: 60000\r\n      # 配置间隔多久才进行一次检测，单位是毫秒\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n      minEvictableIdleTimeMillis: 300000\r\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\r\n      maxEvictableIdleTimeMillis: 900000\r\n      # 配置检测连接是否有效\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', '504779da6bb4e21ff50e7e93d4769dc3', '2025-05-02 17:54:25', '2025-05-02 17:54:25', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (3, 11, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 17:54:27', '2025-05-02 17:54:28', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (4, 12, 'sepsis-gateway-dev.yaml', 'sepsis-mimic-service', '', 'spring:\n  jpa:\n    show-sql: true \n    open-in-view: false\n    hibernate:\n      ddl-auto: update', '71b3bd7520cfe0bd63f029ad13356b97', '2025-05-02 17:54:30', '2025-05-02 17:54:31', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\",\"src_user\":\"nacos\"}');
INSERT INTO `his_config_info` VALUES (5, 13, 'sepsis-auth-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 17:54:32', '2025-05-02 17:54:32', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (6, 14, 'sepsis-analysis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 17:54:34', '2025-05-02 17:54:34', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (7, 15, 'sepsis-diagnosis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 17:54:37', '2025-05-02 17:54:37', NULL, '192.168.0.107', 'D', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 16, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: 060510\r\n      database: 1\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', '94056c457b22ada482948c5eb02099f0', '2025-05-02 18:03:44', '2025-05-02 18:03:44', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 17, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: 060510\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      # 初始连接数\r\n      initialSize: 5\r\n      # 最小连接池数量\r\n      minIdle: 10\r\n      # 最大连接池数量\r\n      maxActive: 20 \r\n      # 配置获取连接等待超时的时间\r\n      maxWait: 60000\r\n      # 配置连接超时时间\r\n      connectTimeout: 30000\r\n      # 配置网络超时时间\r\n      socketTimeout: 60000\r\n      # 配置间隔多久才进行一次检测，单位是毫秒\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n      minEvictableIdleTimeMillis: 300000\r\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\r\n      maxEvictableIdleTimeMillis: 900000\r\n      # 配置检测连接是否有效\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', 'f05f28c87e566eb767bd61fe1f2b44e9', '2025-05-02 18:04:44', '2025-05-02 18:04:44', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 18, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 18:05:09', '2025-05-02 18:05:10', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 19, 'sepsis-auth-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 18:05:35', '2025-05-02 18:05:36', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 20, 'sepsis-gateway-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 18:05:53', '2025-05-02 18:05:53', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 21, 'sepsis-analysis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 18:06:16', '2025-05-02 18:06:16', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 22, 'sepsis-diagnosis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-02 18:06:39', '2025-05-02 18:06:39', NULL, '192.168.0.107', 'I', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (2, 23, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: 060510\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      # 初始连接数\r\n      initialSize: 5\r\n      # 最小连接池数量\r\n      minIdle: 10\r\n      # 最大连接池数量\r\n      maxActive: 20 \r\n      # 配置获取连接等待超时的时间\r\n      maxWait: 60000\r\n      # 配置连接超时时间\r\n      connectTimeout: 30000\r\n      # 配置网络超时时间\r\n      socketTimeout: 60000\r\n      # 配置间隔多久才进行一次检测，单位是毫秒\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n      minEvictableIdleTimeMillis: 300000\r\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\r\n      maxEvictableIdleTimeMillis: 900000\r\n      # 配置检测连接是否有效\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', 'f05f28c87e566eb767bd61fe1f2b44e9', '2025-05-02 21:42:18', '2025-05-02 21:42:19', NULL, '192.168.0.107', 'U', 'dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 24, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: 060510\r\n      database: 0\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', 'cd12a1a8ebd574e55ed3e8d3af060400', '2025-05-03 15:08:37', '2025-05-03 15:08:37', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (8, 25, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: 060510\r\n      database: 0\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', 'cd12a1a8ebd574e55ed3e8d3af060400', '2025-05-03 15:09:13', '2025-05-03 15:09:14', NULL, '0:0:0:0:0:0:0:1', 'D', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 26, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: 060510\r\n      database: 1\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', '94056c457b22ada482948c5eb02099f0', '2025-05-03 15:11:22', '2025-05-03 15:11:22', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 27, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: 060510\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      initialSize: 5\r\n      minIdle: 10\r\n      maxActive: 20 \r\n      maxWait: 60000\r\n      connectTimeout: 30000\r\n      socketTimeout: 60000\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      minEvictableIdleTimeMillis: 300000\r\n      maxEvictableIdleTimeMillis: 900000\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', '6bb34e1f8283f2aec8d4e1078af1d072', '2025-05-03 15:12:06', '2025-05-03 15:12:06', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 28, 'sepsis-gateway-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:12:32', '2025-05-03 15:12:33', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 29, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:12:54', '2025-05-03 15:12:54', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 30, 'sepsis-auth-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:13:19', '2025-05-03 15:13:19', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 31, 'sepsis-analysis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:13:41', '2025-05-03 15:13:41', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (0, 32, 'sepsis-diagnosis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:14:05', '2025-05-03 15:14:05', NULL, '0:0:0:0:0:0:0:1', 'I', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (4, 33, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 15:18:17', '2025-05-03 15:18:17', NULL, '0:0:0:0:0:0:0:1', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (4, 34, 'sepsis-system-dev.yaml', 'sepsis-mimic-service', '', 'spring:\n  jpa:\n    show-sql: true \n    open-in-view: false\n    database-platform: org.hibernate.dialect.MySQLInnoDBDialect\n    hibernate:\n      ddl-auto: update', 'f384c0452c9434e6ec73f871058aca1b', '2025-05-03 15:19:36', '2025-05-03 15:19:37', NULL, '0:0:0:0:0:0:0:1', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (2, 35, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: 060510\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      initialSize: 5\r\n      minIdle: 10\r\n      maxActive: 20 \r\n      maxWait: 60000\r\n      connectTimeout: 30000\r\n      socketTimeout: 60000\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      minEvictableIdleTimeMillis: 300000\r\n      maxEvictableIdleTimeMillis: 900000\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', '6bb34e1f8283f2aec8d4e1078af1d072', '2025-05-03 15:26:51', '2025-05-03 15:26:51', NULL, '0:0:0:0:0:0:0:1', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (2, 36, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: 060510\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      initialSize: 5\n      minIdle: 10\n      maxActive: 20 \n      maxWait: 60000\n      connectTimeout: 30000\n      socketTimeout: 60000\n      timeBetweenEvictionRunsMillis: 60000\n      minEvictableIdleTimeMillis: 300000\n      maxEvictableIdleTimeMillis: 900000\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', '2fb9222748ee0367d04f61d258b79804', '2025-05-03 15:28:17', '2025-05-03 15:28:18', NULL, '0:0:0:0:0:0:0:1', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (2, 37, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: \"060510\"\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      initialSize: 5\n      minIdle: 10\n      maxActive: 20 \n      maxWait: 60000\n      connectTimeout: 30000\n      socketTimeout: 60000\n      timeBetweenEvictionRunsMillis: 60000\n      minEvictableIdleTimeMillis: 300000\n      maxEvictableIdleTimeMillis: 900000\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', 'fdae059179f8551090f8ed9630900ab0', '2025-05-03 15:59:58', '2025-05-03 15:59:59', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (5, 38, 'sepsis-auth-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 16:00:36', '2025-05-03 16:00:37', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (6, 39, 'sepsis-analysis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 16:00:48', '2025-05-03 16:00:49', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (7, 40, 'sepsis-diagnosis-dev.yaml', 'sepsis-mimic-service', '', 'spring:\r\n  jpa:\r\n    show-sql: true \r\n    open-in-view: false\r\n    hibernate:\r\n      ddl-auto: update', '3df8b32be150d4a2d5a4f61bda72dd38', '2025-05-03 16:00:55', '2025-05-03 16:00:55', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (1, 41, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      password: 060510\r\n      database: 1\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          min-idle: 0\r\n          max-idle: 8\r\n          max-active: 8\r\n          max-wait: -1ms', '94056c457b22ada482948c5eb02099f0', '2025-05-03 16:10:12', '2025-05-03 16:10:13', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (1, 42, 'redis-config.yaml', 'sepsis-mimic-common', '', 'spring:\n  data:\n    redis:\n      host: 127.0.0.1\n      port: 6379\n      password: 670510\n      database: 1\n      timeout: 10s\n      lettuce:\n        pool:\n          min-idle: 0\n          max-idle: 8\n          max-active: 8\n          max-wait: -1ms', '98a03ac53f07062ba736b2521d881b4f', '2025-05-03 16:11:26', '2025-05-03 16:11:27', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');
INSERT INTO `his_config_info` VALUES (2, 43, 'druid-config.yaml', 'sepsis-mimic-common', '', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3307/sepsis_mimic?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: 670510\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      initialSize: 5\n      minIdle: 10\n      maxActive: 20 \n      maxWait: 60000\n      connectTimeout: 30000\n      socketTimeout: 60000\n      timeBetweenEvictionRunsMillis: 60000\n      minEvictableIdleTimeMillis: 300000\n      maxEvictableIdleTimeMillis: 900000\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', '22a9331b1eac3d88bd64cf2ef1eb9e11', '2025-05-03 16:11:36', '2025-05-03 16:11:36', NULL, '192.168.0.107', 'U', 'sepsis-mimic-dev', '', 'formal', '', '{\"type\":\"yaml\"}');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'role',
  `resource` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'resource',
  `action` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'action',
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'username',
  `role` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'role',
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------
INSERT INTO `tenant_capacity` VALUES (1, 'sepsis-mimic-dev', 0, 7, 0, 0, 0, 0, '2025-05-03 15:08:37', '2025-05-03 16:04:12');

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'sepsis-mimic-dev', 'sepsis-mimic-dev', 'sepsis-mimic-dev', 'nacos', 1746256029399, 1746256029399);
INSERT INTO `tenant_info` VALUES (2, '1', 'sepsis-mimic-pro', 'sepsis-mimic-pro', 'sepsis-mimic-pro', 'nacos', 1746256051072, 1746256051072);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'username',
  `password` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'password',
  `enabled` tinyint(1) NOT NULL COMMENT 'enabled',
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

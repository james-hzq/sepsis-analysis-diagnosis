/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80028 (8.0.28)
 Source Host           : localhost:3308
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 27/09/2024 00:02:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (10, 'redis-config.yaml', 'common-group', 'spring:\r\n  data:\r\n    redis:\r\n      host: 127.0.0.1\r\n      port: 6379\r\n      database: 1\r\n      timeout: 10s\r\n      lettuce:\r\n        pool:\r\n          # 连接池中的最小空闲连接\r\n          min-idle: 0\r\n          # 连接池中的最大空闲连接 \r\n          max-idle: 8\r\n          # 连接池的最大数据库连接数\r\n          max-active: 8\r\n          # 连接池最大阻塞等待时间（使用负值表示没有限制）\r\n          max-wait: -1ms', 'e7b2ef32586f336f522d61fccc97a29c', '2024-08-26 13:13:04', '2024-08-26 13:13:04', NULL, '0:0:0:0:0:0:0:1', '', 'dev', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (11, 'druid-config.yaml', 'common-group', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3308/mysql_sepsis?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: 318777541\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      # 初始连接数\n      initialSize: 5\n      # 最小连接池数量\n      minIdle: 10\n      # 最大连接池数量\n      maxActive: 20 \n      # 配置获取连接等待超时的时间\n      maxWait: 60000\n      # 配置连接超时时间\n      connectTimeout: 30000\n      # 配置网络超时时间\n      socketTimeout: 60000\n      # 配置间隔多久才进行一次检测，单位是毫秒\n      timeBetweenEvictionRunsMillis: 60000\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\n      minEvictableIdleTimeMillis: 300000\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\n      maxEvictableIdleTimeMillis: 900000\n      # 配置检测连接是否有效\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', 'c4748643e02a1c334d2c7cd35887d9b8', '2024-08-26 13:16:16', '2024-09-25 14:48:46', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (12, 'sepsis-system-dev.yaml', 'sepsis-service', 'spring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update ', 'bb12fccd23dbae0f64dbc2c310e49a6d', '2024-08-26 13:20:01', '2024-09-26 15:47:14', 'nacos', '0:0:0:0:0:0:0:1', '', 'dev', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (12, 18, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\r\n  port: 9201\r\n  tomcat:\r\n    uri-encoding: UTF-8\r\n\r\nlogging:\r\n  config: classpath:log4j2-spring.xml', '40f11634eb35628a0692bc6ab91bfaae', '2024-08-28 21:49:02', '2024-08-28 13:49:03', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (11, 19, 'druid-config.yaml', 'common-group', '', 'spring:\r\n  datasource:\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    driverClassName: com.mysql.cj.jdbc.Driver\r\n    druid:\r\n      master:\r\n        url: jdbc:mysql://localhost:3308/mimic_sepsis?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n        username: root\r\n        password: 318777541\r\n      slave:\r\n        enabled: false\r\n        url:\r\n        username:\r\n        password:\r\n      # 初始连接数\r\n      initialSize: 5\r\n      # 最小连接池数量\r\n      minIdle: 10\r\n      # 最大连接池数量\r\n      maxActive: 20 \r\n      # 配置获取连接等待超时的时间\r\n      maxWait: 60000\r\n      # 配置连接超时时间\r\n      connectTimeout: 30000\r\n      # 配置网络超时时间\r\n      socketTimeout: 60000\r\n      # 配置间隔多久才进行一次检测，单位是毫秒\r\n      timeBetweenEvictionRunsMillis: 60000\r\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\r\n      minEvictableIdleTimeMillis: 300000\r\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\r\n      maxEvictableIdleTimeMillis: 900000\r\n      # 配置检测连接是否有效\r\n      validationQuery: SELECT 1 FROM DUAL \r\n      testWhileIdle: true\r\n      testOnBorrow: false\r\n      testOnReturn: false', 'dc6197dda4867c038355743c66a4940c', '2024-08-28 22:32:07', '2024-08-28 14:32:07', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 20, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 数据库方言\n    database-platform: org.hibernate.dialect.MySQL57Dialect\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml', 'd5bdb265dd085fbe7fcd835d29edf096', '2024-08-28 22:51:32', '2024-08-28 14:51:32', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 21, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 数据库方言\n    database-platform: org.hibernate.dialect.MySQL8Dialect\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml', '0607aa745883bb325f84a688d999886e', '2024-08-31 08:39:23', '2024-08-31 00:39:23', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 22, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 数据库方言\n    database-platform: org.hibernate.dialect.MySQLDialect\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml', 'b8bf95e7423a84f11863e2a897173d9b', '2024-08-31 08:41:46', '2024-08-31 00:41:46', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (0, 23, 'sepsis-auth-dev.yaml', 'sepsis-service', '', 'server:\r\n  port:', '8df96a3533eea79c2eacd83883c6dc76', '2024-09-01 22:06:10', '2024-09-01 14:06:11', NULL, '0:0:0:0:0:0:0:1', 'I', 'dev', '');
INSERT INTO `his_config_info` VALUES (18, 24, 'sepsis-auth-dev.yaml', 'sepsis-service', '', 'server:\r\n  port:', '8df96a3533eea79c2eacd83883c6dc76', '2024-09-01 22:06:59', '2024-09-01 14:06:59', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (11, 25, 'druid-config.yaml', 'common-group', '', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3308/mysql_sepsis?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: root\n        password: 318777541\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      # 初始连接数\n      initialSize: 5\n      # 最小连接池数量\n      minIdle: 10\n      # 最大连接池数量\n      maxActive: 20 \n      # 配置获取连接等待超时的时间\n      maxWait: 60000\n      # 配置连接超时时间\n      connectTimeout: 30000\n      # 配置网络超时时间\n      socketTimeout: 60000\n      # 配置间隔多久才进行一次检测，单位是毫秒\n      timeBetweenEvictionRunsMillis: 60000\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\n      minEvictableIdleTimeMillis: 300000\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\n      maxEvictableIdleTimeMillis: 900000\n      # 配置检测连接是否有效\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', 'c4748643e02a1c334d2c7cd35887d9b8', '2024-09-16 22:24:40', '2024-09-16 14:24:40', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (11, 26, 'druid-config.yaml', 'common-group', '', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    driverClassName: com.mysql.cj.jdbc.Driver\n    druid:\n      master:\n        url: jdbc:mysql://localhost:3308/mysql_sepsis?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n        username: hua\n        password: 318777541\n      slave:\n        enabled: false\n        url:\n        username:\n        password:\n      # 初始连接数\n      initialSize: 5\n      # 最小连接池数量\n      minIdle: 10\n      # 最大连接池数量\n      maxActive: 20 \n      # 配置获取连接等待超时的时间\n      maxWait: 60000\n      # 配置连接超时时间\n      connectTimeout: 30000\n      # 配置网络超时时间\n      socketTimeout: 60000\n      # 配置间隔多久才进行一次检测，单位是毫秒\n      timeBetweenEvictionRunsMillis: 60000\n      # 配置一个连接在池中最小生存的时间，单位是毫秒\n      minEvictableIdleTimeMillis: 300000\n      # 配置一个连接在池中最大生存的时间，单位是毫秒\n      maxEvictableIdleTimeMillis: 900000\n      # 配置检测连接是否有效\n      validationQuery: SELECT 1 FROM DUAL \n      testWhileIdle: true\n      testOnBorrow: false\n      testOnReturn: false', 'dacb5832ee9523e37ba0f10f602fbc08', '2024-09-25 22:48:46', '2024-09-25 14:48:46', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (18, 27, 'sepsis-auth-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9200\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml', '3afe56da5a3cf21f2f9e60600a842127', '2024-09-26 12:20:51', '2024-09-26 04:20:51', NULL, '0:0:0:0:0:0:0:1', 'D', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 28, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml', '1494a410df718bbd1e496c2f8657713b', '2024-09-26 15:38:17', '2024-09-26 07:38:17', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 29, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml\n  path: ${user.home}/logs/${spring.application.name}\n  name: ${spring.application.name}', '6413b6e955d43540635f37f2a5ac41a2', '2024-09-26 15:41:16', '2024-09-26 07:41:16', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 30, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\n', 'b29f35f0426bca87e2e5a6be13a42741', '2024-09-26 16:01:01', '2024-09-26 08:01:02', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 31, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml\n  file:\n    path: ${user.dir}/logs/\n    name: ${spring.application.name}', '5be6f23f631ed3713c222929cee6b398', '2024-09-26 16:01:45', '2024-09-26 08:01:45', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 32, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update \n\nlogging:\n  config: classpath:log4j2-spring.xml\n  file:\n    path: ${user.dir}/logs/\n    name: ${spring.application.name}', '5be6f23f631ed3713c222929cee6b398', '2024-09-26 16:02:19', '2024-09-26 08:02:19', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (0, 33, 'log-config.yaml', 'common-group', '', 'logging:\r\n  config: classpath:log4j2-spring.xml\r\n  file:\r\n    path: ${user.dir}/logs/${spring.application.name}/', '161778f4454ca2d2f586c5b45f735cf6', '2024-09-26 16:12:54', '2024-09-26 08:12:55', NULL, '0:0:0:0:0:0:0:1', 'I', 'dev', '');
INSERT INTO `his_config_info` VALUES (0, 34, 'log4j2-spring.xml', 'common-group', '', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!-- 日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->\r\n<!--status：设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出-->\r\n<!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数-->\r\n<configuration>\r\n    <!-- 全局属性配置, 使用时通过:${name} -->\r\n    <Properties>\r\n        <!-- 文件日志格式 -->\r\n        <property name=\"LOG_PATTERN\" value=\"%date{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n\"/>\r\n        <!-- 控制台彩色日志格式 -->\r\n        <property name=\"CONSOLE_LOG_PATTERN\" value=\"%style{%d{yyyy-MM-dd HH:mm:ss.SSS}}{green} %highlight{%-5level} %style{[%t]}{magenta} %style{%-40.40logger{39}}{cyan}: %style{%msg}{yellow}%n\"/>\r\n        <!-- 定义日志存储的路径 -->\r\n        <property name=\"FILE_PATH\" value=\"${sys:LOG_PATH}\"/>\r\n    </Properties>\r\n\r\n    <!-- 日志处理 -->\r\n    <appenders>\r\n        <!-- 控制台输出 appender，SYSTEM_OUT输出黑色，SYSTEM_ERR输出红色 -->\r\n        <Console name=\"Console\" target=\"SYSTEM_OUT\">\r\n            <!-- 控制台输出日志的格式-->\r\n            <PatternLayout pattern=\"${CONSOLE_LOG_PATTERN}\"/>\r\n            <!-- 控制台只输出level及其以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"info\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n        </Console>\r\n\r\n        <!-- 打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->\r\n        <RollingFile name=\"RollingFileInfo\" fileName=\"${FILE_PATH}/info.log\" filePattern=\"${FILE_PATH}/INFO-%d{yyyy-MM-dd}_%i.log.gz\">\r\n            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"info\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\r\n            <Policies>\r\n                <!--interval属性用来指定多久滚动一次，默认是1 hour-->\r\n                <TimeBasedTriggeringPolicy interval=\"1\"/>\r\n                <SizeBasedTriggeringPolicy size=\"10MB\"/>\r\n            </Policies>\r\n            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->\r\n            <DefaultRolloverStrategy max=\"15\"/>\r\n        </RollingFile>\r\n\r\n        <!-- 打印出所有的warn及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->\r\n        <RollingFile name=\"RollingFileWarn\" fileName=\"${FILE_PATH}/warn.log\" filePattern=\"${FILE_PATH}/WARN-%d{yyyy-MM-dd}_%i.log.gz\">\r\n            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"warn\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\r\n            <Policies>\r\n                <!--interval属性用来指定多久滚动一次，默认是1 hour-->\r\n                <TimeBasedTriggeringPolicy interval=\"1\"/>\r\n                <SizeBasedTriggeringPolicy size=\"10MB\"/>\r\n            </Policies>\r\n            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->\r\n            <DefaultRolloverStrategy max=\"15\"/>\r\n        </RollingFile>\r\n\r\n        <!-- 打印出所有的error及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->\r\n        <RollingFile name=\"RollingFileError\" fileName=\"${FILE_PATH}/error.log\" filePattern=\"${FILE_PATH}/ERROR-%d{yyyy-MM-dd}_%i.log.gz\">\r\n            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"error\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\r\n            <Policies>\r\n                <!--interval属性用来指定多久滚动一次，默认是1 hour-->\r\n                <TimeBasedTriggeringPolicy interval=\"1\"/>\r\n                <SizeBasedTriggeringPolicy size=\"10MB\"/>\r\n            </Policies>\r\n            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->\r\n            <DefaultRolloverStrategy max=\"15\"/>\r\n        </RollingFile>\r\n    </appenders>\r\n\r\n    <!--Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。-->\r\n    <!--然后定义loggers，只有定义了logger并引入的appender，appender才会生效-->\r\n    <loggers>\r\n        <!--过滤掉spring和mybatis的一些无用的DEBUG信息-->\r\n        <Logger name=\"org.mybatis\" level=\"info\" additivity=\"false\">\r\n            <AppenderRef ref=\"Console\"/>\r\n        </Logger>\r\n\r\n        <!-- 若是additivity设为false，则 子Logger 只会在自己的appender里输出，而不会在 父Logger 的appender里输出。-->\r\n        <Logger name=\"org.springframework\" level=\"info\" additivity=\"false\">\r\n            <AppenderRef ref=\"Console\"/>\r\n        </Logger>\r\n\r\n        <!-- root logger 配置, 全局配置，默认所有的Logger都继承此配置 -->\r\n        <root level=\"info\">\r\n            <appender-ref ref=\"Console\"/>\r\n            <appender-ref ref=\"RollingFileInfo\"/>\r\n            <appender-ref ref=\"RollingFileWarn\"/>\r\n            <appender-ref ref=\"RollingFileError\"/>\r\n        </root>\r\n    </loggers>\r\n</configuration>\r\n', '7220b00b2b2e85bdaed17094fc9bc4d3', '2024-09-26 16:14:25', '2024-09-26 08:14:25', NULL, '0:0:0:0:0:0:0:1', 'I', 'dev', '');
INSERT INTO `his_config_info` VALUES (27, 35, 'log-config.yaml', 'common-group', '', 'logging:\r\n  config: classpath:log4j2-spring.xml\r\n  file:\r\n    path: ${user.dir}/logs/${spring.application.name}/', '161778f4454ca2d2f586c5b45f735cf6', '2024-09-26 16:15:41', '2024-09-26 08:15:41', NULL, '0:0:0:0:0:0:0:1', 'D', 'dev', '');
INSERT INTO `his_config_info` VALUES (28, 36, 'log4j2-spring.xml', 'common-group', '', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!-- 日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->\r\n<!--status：设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出-->\r\n<!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数-->\r\n<configuration>\r\n    <!-- 全局属性配置, 使用时通过:${name} -->\r\n    <Properties>\r\n        <!-- 文件日志格式 -->\r\n        <property name=\"LOG_PATTERN\" value=\"%date{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n\"/>\r\n        <!-- 控制台彩色日志格式 -->\r\n        <property name=\"CONSOLE_LOG_PATTERN\" value=\"%style{%d{yyyy-MM-dd HH:mm:ss.SSS}}{green} %highlight{%-5level} %style{[%t]}{magenta} %style{%-40.40logger{39}}{cyan}: %style{%msg}{yellow}%n\"/>\r\n        <!-- 定义日志存储的路径 -->\r\n        <property name=\"FILE_PATH\" value=\"${sys:LOG_PATH}\"/>\r\n    </Properties>\r\n\r\n    <!-- 日志处理 -->\r\n    <appenders>\r\n        <!-- 控制台输出 appender，SYSTEM_OUT输出黑色，SYSTEM_ERR输出红色 -->\r\n        <Console name=\"Console\" target=\"SYSTEM_OUT\">\r\n            <!-- 控制台输出日志的格式-->\r\n            <PatternLayout pattern=\"${CONSOLE_LOG_PATTERN}\"/>\r\n            <!-- 控制台只输出level及其以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"info\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n        </Console>\r\n\r\n        <!-- 打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->\r\n        <RollingFile name=\"RollingFileInfo\" fileName=\"${FILE_PATH}/info.log\" filePattern=\"${FILE_PATH}/INFO-%d{yyyy-MM-dd}_%i.log.gz\">\r\n            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"info\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\r\n            <Policies>\r\n                <!--interval属性用来指定多久滚动一次，默认是1 hour-->\r\n                <TimeBasedTriggeringPolicy interval=\"1\"/>\r\n                <SizeBasedTriggeringPolicy size=\"10MB\"/>\r\n            </Policies>\r\n            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->\r\n            <DefaultRolloverStrategy max=\"15\"/>\r\n        </RollingFile>\r\n\r\n        <!-- 打印出所有的warn及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->\r\n        <RollingFile name=\"RollingFileWarn\" fileName=\"${FILE_PATH}/warn.log\" filePattern=\"${FILE_PATH}/WARN-%d{yyyy-MM-dd}_%i.log.gz\">\r\n            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"warn\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\r\n            <Policies>\r\n                <!--interval属性用来指定多久滚动一次，默认是1 hour-->\r\n                <TimeBasedTriggeringPolicy interval=\"1\"/>\r\n                <SizeBasedTriggeringPolicy size=\"10MB\"/>\r\n            </Policies>\r\n            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->\r\n            <DefaultRolloverStrategy max=\"15\"/>\r\n        </RollingFile>\r\n\r\n        <!-- 打印出所有的error及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->\r\n        <RollingFile name=\"RollingFileError\" fileName=\"${FILE_PATH}/error.log\" filePattern=\"${FILE_PATH}/ERROR-%d{yyyy-MM-dd}_%i.log.gz\">\r\n            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->\r\n            <ThresholdFilter level=\"error\" onMatch=\"ACCEPT\" onMismatch=\"DENY\"/>\r\n            <PatternLayout pattern=\"${LOG_PATTERN}\"/>\r\n            <Policies>\r\n                <!--interval属性用来指定多久滚动一次，默认是1 hour-->\r\n                <TimeBasedTriggeringPolicy interval=\"1\"/>\r\n                <SizeBasedTriggeringPolicy size=\"10MB\"/>\r\n            </Policies>\r\n            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->\r\n            <DefaultRolloverStrategy max=\"15\"/>\r\n        </RollingFile>\r\n    </appenders>\r\n\r\n    <!--Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。-->\r\n    <!--然后定义loggers，只有定义了logger并引入的appender，appender才会生效-->\r\n    <loggers>\r\n        <!--过滤掉spring和mybatis的一些无用的DEBUG信息-->\r\n        <Logger name=\"org.mybatis\" level=\"info\" additivity=\"false\">\r\n            <AppenderRef ref=\"Console\"/>\r\n        </Logger>\r\n\r\n        <!-- 若是additivity设为false，则 子Logger 只会在自己的appender里输出，而不会在 父Logger 的appender里输出。-->\r\n        <Logger name=\"org.springframework\" level=\"info\" additivity=\"false\">\r\n            <AppenderRef ref=\"Console\"/>\r\n        </Logger>\r\n\r\n        <!-- root logger 配置, 全局配置，默认所有的Logger都继承此配置 -->\r\n        <root level=\"info\">\r\n            <appender-ref ref=\"Console\"/>\r\n            <appender-ref ref=\"RollingFileInfo\"/>\r\n            <appender-ref ref=\"RollingFileWarn\"/>\r\n            <appender-ref ref=\"RollingFileError\"/>\r\n        </root>\r\n    </loggers>\r\n</configuration>\r\n', '7220b00b2b2e85bdaed17094fc9bc4d3', '2024-09-26 16:15:43', '2024-09-26 08:15:44', NULL, '0:0:0:0:0:0:0:1', 'D', 'dev', '');
INSERT INTO `his_config_info` VALUES (0, 37, 'sepsis-gateway-dev.yaml', 'sepsis-service', '', 'server:\r\n    port: 9100', 'ed5104b9facc6920da83fd6b92a00498', '2024-09-26 16:53:11', '2024-09-26 08:53:12', NULL, '0:0:0:0:0:0:0:1', 'I', 'dev', '');
INSERT INTO `his_config_info` VALUES (29, 38, 'sepsis-gateway-dev.yaml', 'sepsis-service', '', 'server:\r\n    port: 9100', 'ed5104b9facc6920da83fd6b92a00498', '2024-09-26 16:53:48', '2024-09-26 08:53:49', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (29, 39, 'sepsis-gateway-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9100\n  tomcat:\n    uri-encoding: UTF-8', '25268b6a6b5f652042d9a03a18a56351', '2024-09-26 16:54:06', '2024-09-26 08:54:06', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (12, 40, 'sepsis-system-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9201\n  tomcat:\n    uri-encoding: UTF-8\n\nspring:\n  jpa:\n    # 数据库类型\n    database: mysql\n    # 控制台打印SQL\n    show-sql: true \n    hibernate:\n      # 根据实体类自动更新表\n      ddl-auto: update ', '03f30f76cd60260db60cf9d2b4dfd203', '2024-09-26 23:47:14', '2024-09-26 15:47:14', 'nacos', '0:0:0:0:0:0:0:1', 'U', 'dev', '');
INSERT INTO `his_config_info` VALUES (29, 41, 'sepsis-gateway-dev.yaml', 'sepsis-service', '', 'server:\n  port: 9100\n  tomcat:\n    uri-encoding: UTF-8', '25268b6a6b5f652042d9a03a18a56351', '2024-09-26 23:47:39', '2024-09-26 15:47:40', NULL, '0:0:0:0:0:0:0:1', 'D', 'dev', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (2, '1', 'dev', 'dev', '开发环境', 'nacos', 1724677310832, 1727324428226);
INSERT INTO `tenant_info` VALUES (3, '1', 'pro', 'pro', '生产环境', 'nacos', 1724677331504, 1724677331504);
INSERT INTO `tenant_info` VALUES (4, '1', 'test', 'test', '测试环境', 'nacos', 1724677352311, 1724677352311);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;

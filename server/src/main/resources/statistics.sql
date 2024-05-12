/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : localhost:3306
 Source Schema         : statistics

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 12/05/2024 22:46:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for concept
-- ----------------------------
DROP TABLE IF EXISTS `concept`;
CREATE TABLE `concept`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `concept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `num` int NULL DEFAULT NULL,
  `sort_avg_12` double(16, 2) NULL DEFAULT NULL,
  `sort_avg_13` double(16, 2) NULL DEFAULT NULL,
  `sort_avg_14` double(16, 2) NULL DEFAULT NULL,
  `sort_avg_15` double(16, 2) NULL DEFAULT NULL,
  `sort_avg_16` double(16, 2) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unin`(`num`, `concept`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cost
-- ----------------------------
DROP TABLE IF EXISTS `cost`;
CREATE TABLE `cost`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dong_cai_industry` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `days1_hands_rate` double(10, 2) NULL DEFAULT NULL,
  `days3_hands_rate` double(10, 2) NULL DEFAULT NULL,
  `days5_hands_rate` double(10, 2) NULL DEFAULT NULL,
  `days1_price_rate` double(10, 2) NULL DEFAULT NULL,
  `days3_price_rate` double(10, 2) NULL DEFAULT NULL,
  `days5_price_rate` double(10, 2) NULL DEFAULT NULL,
  `days10_price_rate` double(10, 2) NULL DEFAULT NULL,
  `days30_price_rate` double(10, 2) NULL DEFAULT NULL,
  `days1_zf_rate` double(10, 2) NULL DEFAULT NULL,
  `days3_zf_rate` double(10, 2) NULL DEFAULT NULL,
  `days5_zf_rate` double(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46361 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for departure
-- ----------------------------
DROP TABLE IF EXISTS `departure`;
CREATE TABLE `departure`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `policy_appearance` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政策出现',
  `block_rose_sharp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块全面大涨',
  `high_turnover_rate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头一天换手率大',
  `price_increase` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '从最底部已大幅上涨',
  `continuous_high_walk` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '股价连续的高走',
  `dragons_tigers_appears` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出现龙虎榜',
  `bulk_consignment_order` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '买盘出现大量托单',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for detail
-- ----------------------------
DROP TABLE IF EXISTS `detail`;
CREATE TABLE `detail`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report_date` date NULL DEFAULT NULL,
  `capital` double(32, 2) NULL DEFAULT NULL COMMENT '资金净流入',
  `capital_in` double(32, 2) NULL DEFAULT NULL COMMENT '买入资金',
  `capital_out` double(32, 2) NULL DEFAULT NULL COMMENT '卖出资金',
  `capital_eq` double(32, 2) NULL DEFAULT NULL COMMENT '等价买入卖出资金',
  `change_times` bigint NULL DEFAULT NULL COMMENT '交易次数',
  `hands` bigint NULL DEFAULT NULL COMMENT '交易手数',
  `hands_rate` double(32, 2) NULL DEFAULT NULL COMMENT '真实流动换手率，排查了前十大流动股东',
  `irregular_up` int NULL DEFAULT NULL COMMENT '股票价格向上异动次数',
  `irregular_down` int NULL DEFAULT NULL COMMENT '股票价格向下异动次数',
  `irregular` int NULL DEFAULT NULL COMMENT '股票价格上午下跌，下午上涨',
  `shang_die` double(16, 2) NULL DEFAULT NULL COMMENT '股票价格上午下跌，',
  `xia_zhang` double(16, 2) NULL DEFAULT NULL COMMENT '下午上涨',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for detail_count
-- ----------------------------
DROP TABLE IF EXISTS `detail_count`;
CREATE TABLE `detail_count`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '0表示涨跌幅超过1%次数，1表示价格涨跌1%的换手数量，2表价值涨跌1%需要的钱',
  `rank_num` double(16, 2) NULL DEFAULT NULL COMMENT '排名',
  `energy_rat3` double(16, 2) NULL DEFAULT NULL COMMENT '最近3天平均涨跌幅超过1%的次数',
  `energy_rat5` double(16, 2) NULL DEFAULT NULL COMMENT '最近5天平均涨跌幅超过1%的次数',
  `energy_rat10` double(16, 2) NULL DEFAULT NULL COMMENT '最近10天平均涨跌幅超过1%的次数',
  `energy_rat15` double(16, 2) NULL DEFAULT NULL COMMENT '最近15天平均涨跌幅超过1%的次数',
  `energy_rat20` double(16, 2) NULL DEFAULT NULL COMMENT '最近20天平均涨跌幅超过1%的次数',
  `energy_rat30` double(16, 2) NULL DEFAULT NULL COMMENT '最近30天平均涨跌幅超过1%的次数',
  `energy_rat40` double(16, 2) NULL DEFAULT NULL COMMENT '最近40天平均涨跌幅超过1%的次数',
  `energy_rat60` double(16, 2) NULL DEFAULT NULL COMMENT '最近60天平均涨跌幅超过1%的次数',
  `energy_rat100` double(16, 2) NULL DEFAULT NULL COMMENT '最近100天平均涨跌幅超过1%的次数',
  `energy_avg30` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近30天平均涨跌幅超过1%的次数（股价横盘期）',
  `energy_avg60` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近60天平均涨跌幅超过1%的次数（股价横盘期）',
  `energy_avg100` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近100天平均涨跌幅超过1%的次数（股价横盘期）',
  `energy_avg150` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近150天平均涨跌幅超过1%的次数（股价横盘期）',
  `energy_avg200` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近200天平均涨跌幅超过1%的次数（股价横盘期）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for dragon_tiger_department_rat
-- ----------------------------
DROP TABLE IF EXISTS `dragon_tiger_department_rat`;
CREATE TABLE `dragon_tiger_department_rat`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `operatedept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_org_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `con_num` int NULL DEFAULT NULL COMMENT '包含的股票数量',
  `con_price` double(10, 2) NULL DEFAULT NULL COMMENT '共同买卖的价格',
  `con_rat` double(10, 2) NULL DEFAULT NULL COMMENT '相关性',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `in`(`operatedept_code`, `operatedept_org_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for energy
-- ----------------------------
DROP TABLE IF EXISTS `energy`;
CREATE TABLE `energy`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rank_num` double(16, 2) NULL DEFAULT NULL COMMENT '排名',
  `energy_rat3` double(16, 2) NULL DEFAULT NULL COMMENT '最近3天平均能动',
  `energy_rat5` double(16, 2) NULL DEFAULT NULL COMMENT '最近5天平均能动',
  `energy_rat10` double(16, 2) NULL DEFAULT NULL COMMENT '最近10天平均能动',
  `energy_rat15` double(16, 2) NULL DEFAULT NULL COMMENT '最近15天平均能动',
  `energy_rat20` double(16, 2) NULL DEFAULT NULL COMMENT '最近20天平均能动',
  `energy_rat30` double(16, 2) NULL DEFAULT NULL COMMENT '最近30天平均能动',
  `energy_rat40` double(16, 2) NULL DEFAULT NULL COMMENT '最近40天平均能动',
  `energy_rat60` double(16, 2) NULL DEFAULT NULL COMMENT '最近60天平均能动',
  `energy_rat100` double(16, 2) NULL DEFAULT NULL COMMENT '最近100天平均能动',
  `energy_avg30` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近30天平均能动（股价横盘期）',
  `energy_avg60` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近60天平均能动（股价横盘期）',
  `energy_avg100` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近100天平均能动（股价横盘期）',
  `energy_avg150` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近150天平均能动（股价横盘期）',
  `energy_avg200` double(16, 2) NULL DEFAULT NULL COMMENT '上一次最近200天平均能动（股价横盘期）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1649349 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for holder
-- ----------------------------
DROP TABLE IF EXISTS `holder`;
CREATE TABLE `holder`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票名称',
  `listing_date` date NULL DEFAULT NULL COMMENT '上市时间',
  `report_date` date NULL DEFAULT NULL COMMENT '报告时间',
  `dong_cai_industry` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '东财行业',
  `price_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '庄家利润率',
  `oder_num3` int NULL DEFAULT NULL COMMENT '当前人数在最近3年的排名',
  `oder_num5` int NULL DEFAULT NULL COMMENT '当前人数在最近5年的排名',
  `sort_1_2` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数最新排名与倒数第二排名的人数变动比',
  `sort_1_3` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数最新排名与倒数第三排名的人数变动比',
  `sort_1_4` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数最新排名与倒数第四排名的人数变动比',
  `sort_1_5` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数最新排名与倒数第五排名的人数变动比',
  `sort_1_6` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数最新排名与倒数第六排名的人数变动比',
  `sort_1_7` double(16, 2) NULL DEFAULT NULL,
  `sort_1_8` double(16, 2) NULL DEFAULT NULL,
  `sort_1_9` double(16, 2) NULL DEFAULT NULL,
  `flow_market` double(32, 2) NULL DEFAULT NULL COMMENT '实际交易市值',
  `holder_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '私募名称',
  `makers_cost` double(32, 2) NULL DEFAULT NULL COMMENT '庄家建仓成本价',
  `days` int NULL DEFAULT NULL,
  `count` int NULL DEFAULT NULL,
  `market` double(32, 2) NULL DEFAULT NULL COMMENT '总市值',
  `price` double(16, 2) NULL DEFAULT NULL COMMENT '当前价格',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `avg_assets` double(32, 2) NULL DEFAULT NULL COMMENT '人均持股金额（万）',
  `ten_flow_holder_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '前十大股东占总流通股本持股比例',
  `makers_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '庄家持股比例',
  `san_hu_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '散户持股比例',
  `maker_vs_san_hu_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '庄家和散户比例',
  `holder_num` int NULL DEFAULT NULL COMMENT '股东人数',
  `notice_price` double(32, 2) NULL DEFAULT NULL COMMENT '通知价格',
  `industry_big` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '大行业',
  `industry_middle` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '中行业',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_code`(`code`) USING BTREE,
  INDEX `in1`(`industry_big`) USING BTREE,
  INDEX `in2`(`industry_middle`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 637860 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '股东人数统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for holder_org
-- ----------------------------
DROP TABLE IF EXISTS `holder_org`;
CREATE TABLE `holder_org`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票名称',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `start_time` date NULL DEFAULT NULL COMMENT '建仓开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '建仓结束时间',
  `date_num` bigint NULL DEFAULT NULL COMMENT '建仓耗时天数',
  `holder_change_rate` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数变化率',
  `price_rate` double(16, 2) NULL DEFAULT NULL COMMENT '股价变动率',
  `style` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数变化率*股价变动率/建仓天数（建仓风格）',
  `ten_day_max_price` double(16, 2) NULL DEFAULT NULL COMMENT '最近10天最大值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `in1`(`start_time`) USING BTREE,
  INDEX `in2`(`date_num`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 497920 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '股东人数统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for jian_cang
-- ----------------------------
DROP TABLE IF EXISTS `jian_cang`;
CREATE TABLE `jian_cang`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `day3_inMemory` double(10, 2) NULL DEFAULT NULL,
  `day5_inMemory` double(10, 2) NULL DEFAULT NULL,
  `day10_inMemory` double(10, 2) NULL DEFAULT NULL,
  `day30_inMemory` double(10, 2) NULL DEFAULT NULL,
  `day60_inMemory` double(10, 2) NULL DEFAULT NULL,
  `day3_inTimes` double(10, 2) NULL DEFAULT NULL,
  `day5_inTimes` double(10, 2) NULL DEFAULT NULL,
  `day10_inTimes` double(10, 2) NULL DEFAULT NULL,
  `day30_inTimes` double(10, 2) NULL DEFAULT NULL,
  `day60_inTimes` double(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for one_hand_count
-- ----------------------------
DROP TABLE IF EXISTS `one_hand_count`;
CREATE TABLE `one_hand_count`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report_date` date NULL DEFAULT NULL,
  `one_hand_times` double(32, 0) NULL DEFAULT NULL COMMENT '一手交易次数',
  `tow_hand_times` double(32, 0) NULL DEFAULT NULL COMMENT '二手交易次数',
  `all_times` double NULL DEFAULT NULL COMMENT '总交易次数',
  `one_hand_rate` double(32, 2) NULL DEFAULT NULL COMMENT '一手交易次数占比',
  `one_hand_sum_time` double(32, 2) NULL DEFAULT NULL COMMENT '一手交易距前一笔交易的总时间（秒）',
  `one_hand_avg_time` double NULL DEFAULT NULL COMMENT '一手交易距前一笔交易的平均时间（秒）',
  `price` double(32, 2) NULL DEFAULT NULL COMMENT '价格',
  `concurrence_hands` double(32, 0) NULL DEFAULT NULL COMMENT '集合竞价交易手数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_org_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构组织名称',
  `relevant_org_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '相关机名称',
  `hold_market_cap` double(255, 2) NULL DEFAULT NULL COMMENT '机构持股总市值(亿)',
  `relevant_rate` double(20, 6) NULL DEFAULT NULL COMMENT '机构相关系数',
  `denominator` int NULL DEFAULT NULL COMMENT '两个相关机构持股数量之和',
  `numerator` int NULL DEFAULT NULL COMMENT '两个相关机构持股交集的数量',
  `org_num` int NULL DEFAULT NULL COMMENT '机构下属基金数量数量',
  `security_num` int NULL DEFAULT NULL COMMENT '股票数量',
  `report_date` date NULL DEFAULT NULL COMMENT '报告时间',
  `security_codes` varchar(20000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票代码集合(逗号隔开)',
  `parent_org_code_old` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构组织代码',
  `relevant_org` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '相关机构代码(逗号隔开)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`parent_org_name`, `report_date`, `relevant_org_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '股票代码和名称' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for organization_detail_rate
-- ----------------------------
DROP TABLE IF EXISTS `organization_detail_rate`;
CREATE TABLE `organization_detail_rate`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_holder_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '上级机构组织名称',
  `holder_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构组织名称',
  `rat_barn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '老鼠仓',
  `holder_code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构代码',
  `code` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '股票名称',
  `holder_cap` double(255, 2) NULL DEFAULT NULL COMMENT '机构持股总市值(万）',
  `holder_profit` double(20, 2) NULL DEFAULT NULL COMMENT '机构利润',
  `holder_profit_rate` double(20, 2) NULL DEFAULT NULL COMMENT '机构利润',
  `day_num` int NULL DEFAULT NULL COMMENT '持股天数',
  `start_date` date NULL DEFAULT NULL,
  `end_date` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`holder_code`, `name`, `start_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '股票代码和名称' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qian
-- ----------------------------
DROP TABLE IF EXISTS `qian`;
CREATE TABLE `qian`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `from_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源',
  `why` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推荐原因',
  `shichang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时长，短，中，长',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `un`(`code`, `from_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for similarity
-- ----------------------------
DROP TABLE IF EXISTS `similarity`;
CREATE TABLE `similarity`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `days` int NULL DEFAULT NULL COMMENT '过去天数',
  `future_days` int NULL DEFAULT NULL COMMENT '预测未来天数',
  `sqrt_scope_start` int NULL DEFAULT NULL COMMENT 'sqrt区间排名【100,200】',
  `sqrt_scope_end` int NULL DEFAULT NULL COMMENT 'sqrt区间排名【100,200】',
  `sqrt` double(16, 2) NULL DEFAULT NULL COMMENT 'sqrt',
  `future_sqrt` double(16, 2) NULL DEFAULT NULL COMMENT '未来sqrt',
  `future_price1` double(16, 2) NULL DEFAULT NULL COMMENT '未来1天的价格',
  `future_price3` double(16, 2) NULL DEFAULT NULL COMMENT '未来3天的价格',
  `future_price5` double(16, 2) NULL DEFAULT NULL COMMENT '未来5天的价格',
  `future_price10` double(16, 2) NULL DEFAULT NULL COMMENT '未来10天的价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for slide
-- ----------------------------
DROP TABLE IF EXISTS `slide`;
CREATE TABLE `slide`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `start_date` datetime(0) NULL DEFAULT NULL,
  `end_date` datetime(0) NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `range` double(32, 2) NULL DEFAULT NULL,
  `days` int NULL DEFAULT NULL,
  `step` int NULL DEFAULT NULL,
  `poll` int NULL DEFAULT NULL,
  `inflow_funds` bigint NULL DEFAULT NULL,
  `flow_market_change` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2404511 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stockselection
-- ----------------------------
DROP TABLE IF EXISTS `stockselection`;
CREATE TABLE `stockselection`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `main_force` double(32, 2) NULL DEFAULT NULL COMMENT '主力',
  `price_increase` double(32, 2) NULL DEFAULT NULL COMMENT '股价大幅上涨',
  `flow_market` double(32, 2) NULL DEFAULT NULL COMMENT '流动市值',
  `industry_advanced` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行业先进',
  `private_fund` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '私募基金',
  `rat_barn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '老鼠仓',
  `industry_leader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '细分行业龙头',
  `policy_block` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政策板块',
  `low_turnover_rate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '换手率低',
  `three_red` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '底部三红兵',
  `heat_lead_stock` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块龙头股票热度',
  `event` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大事件',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for total_operate_income
-- ----------------------------
DROP TABLE IF EXISTS `total_operate_income`;
CREATE TABLE `total_operate_income`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `variance2` double(16, 2) NULL DEFAULT NULL COMMENT '最近2年均方差',
  `variance3` double(16, 2) NULL DEFAULT NULL COMMENT '最近3年均方差',
  `variance4` double(16, 2) NULL DEFAULT NULL COMMENT '最近4年均方差',
  `variance5` double(16, 2) NULL DEFAULT NULL COMMENT '最近5年均方差',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收类型：增涨（up），减少（down）',
  `growth2` double(16, 2) NULL DEFAULT NULL COMMENT '最近2年复合增长率（百分比）',
  `growth3` double(16, 2) NULL DEFAULT NULL COMMENT '最近3年复合增长率（百分比）',
  `growth4` double(16, 2) NULL DEFAULT NULL COMMENT '最近4年复合增长率（百分比）',
  `growth5` double(16, 2) NULL DEFAULT NULL COMMENT '最近5年复合增长率（百分比）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rr`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54732 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for total_score
-- ----------------------------
DROP TABLE IF EXISTS `total_score`;
CREATE TABLE `total_score`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '股票代码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '股票名称',
  `one_vote_veto` tinyint NULL DEFAULT NULL COMMENT '一票否决',
  `one_vote_veto_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '一票否决说明',
  `holder_score` double(16, 2) NULL DEFAULT NULL COMMENT '股东人数得分 50',
  `price_score` double(16, 2) NULL DEFAULT NULL COMMENT '价格变动得分 20',
  `concept_score` double(16, 2) NULL DEFAULT NULL COMMENT '概念板块得分 10',
  `industry_score` double(16, 2) NULL DEFAULT NULL COMMENT '行业板块得分 10',
  `finance_score` double(16, 2) NULL DEFAULT NULL COMMENT '财务状况得分 0',
  `market_score` double(16, 2) NULL DEFAULT NULL COMMENT '市场规模得分 10',
  `total_score` double(16, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tran_count
-- ----------------------------
DROP TABLE IF EXISTS `tran_count`;
CREATE TABLE `tran_count`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `start_date` timestamp(0) NULL DEFAULT NULL,
  `end_date` timestamp(0) NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `range` double(32, 2) NULL DEFAULT NULL,
  `days` int NULL DEFAULT NULL,
  `report_date` datetime(0) NULL DEFAULT NULL,
  `pchg` double(16, 2) NULL DEFAULT NULL,
  `pchg3` double(16, 2) NULL DEFAULT NULL,
  `pchg5` double(16, 2) NULL DEFAULT NULL,
  `pchg10` double(16, 2) NULL DEFAULT NULL,
  `pchg30` double(16, 2) NULL DEFAULT NULL,
  `chang_hands` double(16, 2) NULL DEFAULT NULL,
  `chang_hands3` double(16, 2) NULL DEFAULT NULL,
  `chang_hands5` double(16, 2) NULL DEFAULT NULL,
  `chang_hands10` double(16, 2) NULL DEFAULT NULL,
  `chang_hands30` double(16, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tran_detail_count
-- ----------------------------
DROP TABLE IF EXISTS `tran_detail_count`;
CREATE TABLE `tran_detail_count`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report_date` datetime(0) NULL DEFAULT NULL,
  `up_num` double(16, 2) NULL DEFAULT NULL,
  `down_num` double(16, 2) NULL DEFAULT NULL,
  `hand_up_sum` double(16, 0) NULL DEFAULT NULL,
  `hand_down_sum` double(16, 0) NULL DEFAULT NULL,
  `price_up_sum` double(16, 2) NULL DEFAULT NULL,
  `price_down_sum` double(16, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- View structure for holder_view_individual_stocks
-- ----------------------------
DROP VIEW IF EXISTS `holder_view_individual_stocks`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `holder_view_individual_stocks` AS select `holder`.`id` AS `id`,`holder`.`name` AS `name`,`holder`.`listing_date` AS `listing_date`,`holder`.`report_date` AS `report_date`,`holder`.`dong_cai_industry` AS `dong_cai_industry`,`holder`.`price_ratio` AS `price_ratio`,`holder`.`oder_num3` AS `oder_num3`,`holder`.`oder_num5` AS `oder_num5`,`holder`.`sort_1_2` AS `sort_1_2`,`holder`.`sort_1_3` AS `sort_1_3`,`holder`.`sort_1_4` AS `sort_1_4`,`holder`.`sort_1_5` AS `sort_1_5`,`holder`.`sort_1_6` AS `sort_1_6`,`holder`.`sort_1_7` AS `sort_1_7`,`holder`.`sort_1_8` AS `sort_1_8`,`holder`.`sort_1_9` AS `sort_1_9`,`holder`.`flow_market` AS `flow_market`,`holder`.`holder_name` AS `holder_name`,`holder`.`makers_cost` AS `makers_cost`,`holder`.`days` AS `days`,`holder`.`count` AS `count`,`holder`.`market` AS `market`,`holder`.`price` AS `price`,`holder`.`code` AS `code`,`holder`.`avg_assets` AS `avg_assets`,`holder`.`ten_flow_holder_ratio` AS `ten_flow_holder_ratio`,`holder`.`makers_ratio` AS `makers_ratio`,`holder`.`san_hu_ratio` AS `san_hu_ratio`,`holder`.`maker_vs_san_hu_ratio` AS `maker_vs_san_hu_ratio`,`holder`.`holder_num` AS `holder_num`,`holder`.`notice_price` AS `notice_price`,`holder`.`industry_big` AS `industry_big`,`holder`.`industry_middle` AS `industry_middle`,`holder`.`update_time` AS `update_time`,`holder`.`create_time` AS `create_time` from `holder` where ((`holder`.`listing_date` < '2021-01-01') and (`holder`.`report_date` > '2024-01-01') and (`holder`.`oder_num5` < 4) and (not((`holder`.`name` like '%st%'))) and (greatest(`holder`.`sort_1_2`,`holder`.`sort_1_3`,`holder`.`sort_1_4`,`holder`.`sort_1_5`,`holder`.`sort_1_6`,`holder`.`sort_1_7`,`holder`.`sort_1_8`,`holder`.`sort_1_9`) > 1.5) and (greatest(`holder`.`sort_1_2`,`holder`.`sort_1_3`,`holder`.`sort_1_4`) > 1.5) and (`holder`.`flow_market` < 50)) order by greatest(`holder`.`sort_1_2`,`holder`.`sort_1_3`,`holder`.`sort_1_4`,`holder`.`sort_1_5`,`holder`.`sort_1_6`,`holder`.`sort_1_7`,`holder`.`sort_1_8`,`holder`.`sort_1_9`) desc;

SET FOREIGN_KEY_CHECKS = 1;

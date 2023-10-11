/*
 Navicat Premium Data Transfer

 Source Server         : 172.16.63.143MYSQL
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 172.16.63.143:3306
 Source Schema         : security

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 28/08/2023 16:42:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_composition_analysis
-- ----------------------------
DROP TABLE IF EXISTS `business_composition_analysis`;
CREATE TABLE `business_composition_analysis`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `main_business_rpofit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营利润(元)',
  `mbr_ratio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '利润比例',
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营构成',
  `mbc_ratio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '成本比例',
  `gross_rpofit_ratio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毛利率(%)',
  `main_business_cost` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营成本(元)',
  `security_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `mainop_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业类型',
  `mbi_ratio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营收入比例',
  `rank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排名',
  `secucode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '报告日期',
  `main_business_income` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营收入(元)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`report_date`, `secucode`, `item_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '主营构成分析' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commission_detail
-- ----------------------------
DROP TABLE IF EXISTS `commission_detail`;
CREATE TABLE `commission_detail`  (
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report_date` date NULL DEFAULT NULL,
  `tran_id` bigint(0) NOT NULL COMMENT '交易流水号',
  `time` time(0) NULL DEFAULT NULL COMMENT '成交时间',
  `price` decimal(16, 2) NULL DEFAULT NULL COMMENT '成交价格',
  `volume` bigint(0) NULL DEFAULT NULL COMMENT '成交量',
  `sale_order_volume` bigint(0) NULL DEFAULT NULL COMMENT '委卖量,此表中的委托量全为成交的委托量',
  `buy_order_volume` bigint(0) NULL DEFAULT NULL COMMENT '委买量',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '成交类型。即代表主动买（B）还是主动卖（S）',
  `sale_order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '委卖ID。此ID不是交易席位，相同ID代表是同一人委托，而同一个人不同时段委托，则ID号不同。',
  `sale_order_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '委卖价格。',
  `buy_order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '委买ID',
  `buy_order_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '委买价格',
  UNIQUE INDEX `code`(`code`, `report_date`, `tran_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for company_info
-- ----------------------------
DROP TABLE IF EXISTS `company_info`;
CREATE TABLE `company_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `listing_date` datetime(0) NULL DEFAULT NULL COMMENT '上市日期',
  `establishment_date` datetime(0) NULL DEFAULT NULL COMMENT '成立日期',
  `issuance` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发行量',
  `issue_price` double(255, 2) NULL DEFAULT NULL COMMENT '每股发行价',
  `dongcai_industry` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属东财行业',
  `securities_industry` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属证监会行业',
  `legal_representative` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人代表',
  `chairman` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '董事长',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域',
  `registered_capital` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册资本',
  `employees` int(0) NULL DEFAULT NULL COMMENT '雇员人数',
  `company_profile` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公司简介',
  `business_scope` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '经营范围',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33473 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '股票基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for concept_stock
-- ----------------------------
DROP TABLE IF EXISTS `concept_stock`;
CREATE TABLE `concept_stock`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `concept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unin`(`code`, `concept`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 364262 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dragon_tiger_date
-- ----------------------------
DROP TABLE IF EXISTS `dragon_tiger_date`;
CREATE TABLE `dragon_tiger_date`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `trade_date` date NULL DEFAULT NULL COMMENT '龙虎榜时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `node`(`code`, `trade_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dragon_tiger_department
-- ----------------------------
DROP TABLE IF EXISTS `dragon_tiger_department`;
CREATE TABLE `dragon_tiger_department`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `trade_date` date NULL DEFAULT NULL,
  `d1_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `d2_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `d3_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `d5_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `d10_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `d20_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `d30_close_adjchrate` double(255, 2) NULL DEFAULT NULL,
  `act_buy` bigint(0) NULL DEFAULT NULL,
  `act_sell` bigint(0) NULL DEFAULT NULL,
  `net_amt` bigint(0) NULL DEFAULT NULL,
  `explanation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_code_old` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `org_name_abbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `change_rate` double(255, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dragon_tiger_detail
-- ----------------------------
DROP TABLE IF EXISTS `dragon_tiger_detail`;
CREATE TABLE `dragon_tiger_detail`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `trade_date` date NULL DEFAULT NULL,
  `operatedept_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `explanation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `change_rate` double(16, 2) NULL DEFAULT NULL,
  `close_price` double(16, 2) NULL DEFAULT NULL,
  `accum_amount` bigint(0) NULL DEFAULT NULL,
  `accum_volume` double(255, 2) NULL DEFAULT NULL,
  `buy` double(255, 2) NULL DEFAULT NULL,
  `sell` double(255, 2) NULL DEFAULT NULL,
  `net` double(255, 2) NULL DEFAULT NULL,
  `rise_probability_3day` double(255, 2) NULL DEFAULT NULL,
  `total_buyer_salestimes_3day` double(255, 2) NULL DEFAULT NULL,
  `change_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `operatedept_code_old` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `total_buyrio` double(16, 2) NULL DEFAULT NULL,
  `total_sellrio` double(16, 2) NULL DEFAULT NULL,
  `trade_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for event
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notice_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un`(`code`, `event_type`, `content`, `notice_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2233136 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for growth_ability
-- ----------------------------
DROP TABLE IF EXISTS `growth_ability`;
CREATE TABLE `growth_ability`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '报告日期',
  `main_business_income_growth_rate` double(32, 2) NULL DEFAULT NULL COMMENT '主营业务收入增长率(%)	',
  `net_profit_growth_rate` double(32, 2) NULL DEFAULT NULL COMMENT '净利润增长率(%)	',
  `net_assets_growth_rate` double(32, 2) NULL DEFAULT NULL COMMENT '净资产增长率(%)',
  `growth_rate_of_total_assets` double(32, 2) NULL DEFAULT NULL COMMENT '总资产增长率(%)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`, `report_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '成长能力' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for guba
-- ----------------------------
DROP TABLE IF EXISTS `guba`;
CREATE TABLE `guba`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `num` int(0) NULL DEFAULT NULL COMMENT '人气排名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for history_capital_flows
-- ----------------------------
DROP TABLE IF EXISTS `history_capital_flows`;
CREATE TABLE `history_capital_flows`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `tran_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日期',
  `main` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主力净流入',
  `small_single` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小单净流入',
  `medium_singles` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中单净流入',
  `large_orders` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大单净流入',
  `single_large_orders` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超大单净流入',
  `main_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主力净流入占比',
  `small_single_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小单净流入占比',
  `medium_singles_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中单净流入占比',
  `large_orders_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大单净流入占比',
  `single_large_orders_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超大单净流入占比',
  `closing_price` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收盘价',
  `quote_change` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '涨跌幅',
  `other` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他',
  `other_one` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '其他1',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`, `tran_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '历史资金流向' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for holder_num
-- ----------------------------
DROP TABLE IF EXISTS `holder_num`;
CREATE TABLE `holder_num`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `total_market_cap` decimal(32, 2) NULL DEFAULT NULL,
  `change_shares` bigint(0) NULL DEFAULT NULL,
  `holder_num` bigint(0) NULL DEFAULT NULL,
  `holder_num_change` bigint(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `report_date` date NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pre_holder_num` bigint(0) NULL DEFAULT NULL,
  `hold_notice_date` date NULL DEFAULT NULL,
  `holder_num_ratio` decimal(16, 2) NULL DEFAULT NULL,
  `pre_end_date` date NULL DEFAULT NULL,
  `change_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_a_shares` bigint(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un`(`code`, `report_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13732922 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '股东人数' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for industry
-- ----------------------------
DROP TABLE IF EXISTS `industry`;
CREATE TABLE `industry`  (
  `id` int(0) NOT NULL,
  `plate_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块名称',
  `plate_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块代码',
  `report_date` date NULL DEFAULT NULL COMMENT '日期',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `chg` double(10, 2) NULL DEFAULT NULL COMMENT '涨跌额',
  `pchg` double(10, 2) NULL DEFAULT NULL COMMENT '涨跌幅',
  `tcap` double(32, 2) NULL DEFAULT NULL COMMENT '总市值',
  `up_num` int(0) NULL DEFAULT NULL COMMENT '上涨家数',
  `down_num` int(0) NULL DEFAULT NULL COMMENT '下跌家数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for main_finance
-- ----------------------------
DROP TABLE IF EXISTS `main_finance`;
CREATE TABLE `main_finance`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票名称',
  `report_date` date NULL DEFAULT NULL COMMENT '报告日期',
  `basic_earnings` double(32, 2) NULL DEFAULT NULL COMMENT '基本每股收益(元)\r\n',
  `net_assets` double(32, 2) NULL DEFAULT NULL COMMENT '每股净资产(元)',
  `net_cash_flow` double(32, 2) NULL DEFAULT NULL COMMENT '每股经营活动产生的现金流量净额(元)',
  `main_business_income` double(32, 2) NULL DEFAULT NULL COMMENT '主营业务收入(万元)',
  `main_business_profit` double(32, 2) NULL DEFAULT NULL COMMENT '主营业务利润(万元)',
  `operating_profit` double(32, 2) NULL DEFAULT NULL COMMENT '营业利润(万元)',
  `investment_income` double(32, 2) NULL DEFAULT NULL COMMENT '投资收益(万元)',
  `net_non_operating_income` double(32, 2) NULL DEFAULT NULL COMMENT '营业外收支净额(万元)',
  `total_profit` double(32, 2) NULL DEFAULT NULL COMMENT '利润总额(万元)',
  `net_profit` double(32, 2) NULL DEFAULT NULL COMMENT '净利润(万元)',
  `net_profit_after_deducting` double(32, 2) NULL DEFAULT NULL COMMENT '净利润(扣除非经常性损益后)(万元)',
  `net_cash_flow_operating` double(32, 2) NULL DEFAULT NULL COMMENT '经营活动产生的现金流量净额(万元)',
  `net_increase_in_cash` double(32, 2) NULL DEFAULT NULL COMMENT '现金及现金等价物净增加额(万元)',
  `total_assets` double(32, 2) NULL DEFAULT NULL COMMENT '总资产(万元)',
  `current_assets` double(32, 2) NULL DEFAULT NULL COMMENT '流动资产(万元)',
  `total_liabilities` double(32, 2) NULL DEFAULT NULL COMMENT '总负债(万元)',
  `current_liabilities` double(32, 2) NULL DEFAULT NULL COMMENT '流动负债(万元)',
  `shareholders` double(32, 2) NULL DEFAULT NULL COMMENT '股东权益不含少数股东权益(万元)',
  `weighted_return_on_equity` double(32, 2) NULL DEFAULT NULL COMMENT '净资产收益率加权(%)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_date`(`code`, `report_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '主要财务指标' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operating_capacity
-- ----------------------------
DROP TABLE IF EXISTS `operating_capacity`;
CREATE TABLE `operating_capacity`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '报告日期',
  `turnover_rate_of_accounts_receivable` double(32, 2) NULL DEFAULT NULL COMMENT '应收账款周转率(次)',
  `accounts_receivable_turnover_days` double(32, 2) NULL DEFAULT NULL COMMENT '应收账款周转天数(天)	',
  `inventory_turnover_rate` double(32, 2) NULL DEFAULT NULL COMMENT '存货周转率(次)',
  `turnover_rate_of_fixed_assets` double(32, 2) NULL DEFAULT NULL COMMENT '固定资产周转率(次)',
  `turnover_rate_of_total_assets` double(32, 2) NULL DEFAULT NULL COMMENT '总资产周转率(次)',
  `inventory_turnover_days` double(32, 2) NULL DEFAULT NULL COMMENT '存货周转天数(天)',
  `total_asset_turnover_days` double(32, 2) NULL DEFAULT NULL COMMENT '总资产周转天数(天)',
  `liquid_assets_turnover_rate` double(32, 2) NULL DEFAULT NULL COMMENT '流动资产周转率(次)',
  `turnover_days_of_current_assets` double(32, 2) NULL DEFAULT NULL COMMENT '流动资产周转天数(天)',
  `ratio_of_net_operating_cash_flow_to_sales_revenue` double(32, 2) NULL DEFAULT NULL COMMENT '经营现金净流量对销售收入比率(%)	',
  `return_on_operating_cash_flow_of_assets` double(32, 2) NULL DEFAULT NULL COMMENT '资产的经营现金流量回报率(%)',
  `the_ratio_of_operating_net_cash_flow_to_net_profit` double(32, 2) NULL DEFAULT NULL COMMENT '经营现金净流量与净利润的比率(%)',
  `operating_net_cash_flow_to_debt_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '经营现金净流量对负债比率(%)',
  `cash_flow_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '现金流量比率(%)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`, `report_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '营运能力' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for organization_details_holder
-- ----------------------------
DROP TABLE IF EXISTS `organization_details_holder`;
CREATE TABLE `organization_details_holder`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票名称',
  `parent_org_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构组织代码',
  `org_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构类型',
  `org_type_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构类型编码',
  `security_inner_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码内部',
  `free_shares_ratio` double(16, 2) NULL DEFAULT NULL COMMENT '	占流通股本比例(%)',
  `parent_org_code_old` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构组织代码',
  `parent_org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构组织名称',
  `hold_market_cap` double(20, 2) NULL DEFAULT NULL COMMENT '持股市值',
  `holder_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构代码',
  `total_shares` bigint(0) NULL DEFAULT NULL COMMENT '持股总数',
  `total_shares_ratio` double(16, 2) NULL DEFAULT NULL COMMENT '	占总股本比例(%',
  `report_date` date NULL DEFAULT NULL COMMENT '报告时间',
  `holder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  `net_asset_ratio` double(16, 2) NULL DEFAULT NULL COMMENT '净资产率',
  `org_name_abbr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `buy_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `name`(`name`, `report_date`) USING BTREE,
  INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18823049 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for para_config
-- ----------------------------
DROP TABLE IF EXISTS `para_config`;
CREATE TABLE `para_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置类型：功能之母大',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性值：json格式保存',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_ability
-- ----------------------------
DROP TABLE IF EXISTS `pay_ability`;
CREATE TABLE `pay_ability`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '报告日期',
  `current_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '流动比率(%)',
  `quick_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '速动比率(%)',
  `cash_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '现金比率(%)',
  `interest_payment_multiple` double(32, 2) NULL DEFAULT NULL COMMENT '利息支付倍数(%)',
  `ssets_and_liabilities` double(32, 2) NULL DEFAULT NULL COMMENT '资产负债率(%)',
  `working_capital_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '长期债务与营运资金比率(%)',
  `shareholders_equity_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '股东权益比率(%)',
  `long_term_debt_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '长期负债比率(%)',
  `ratio_of_shareholders` double(32, 2) NULL DEFAULT NULL COMMENT '股东权益与固定资产比率(%)',
  `debt_to_owner_equity_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '负债与所有者权益比率(%)',
  `long_term_assets_to_long_term_funds` double(32, 2) NULL DEFAULT NULL COMMENT '长期资产与长期资金比率(%)',
  `capitalization_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '资本化比率(%)',
  `net_fixed_assets_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '固定资产净值率(%)',
  `capital_fixed_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '资本固定化比率(%)',
  `equity_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '产权比率(%)',
  `liquidation_value_ratio` double(32, 2) NULL DEFAULT NULL COMMENT '清算价值比率(%)',
  `fixed_assets` double(32, 2) NULL DEFAULT NULL COMMENT '固定资产比重(%)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`, `report_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '偿还能力' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plate_block
-- ----------------------------
DROP TABLE IF EXISTS `plate_block`;
CREATE TABLE `plate_block`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `plate_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块名称',
  `plate_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `report_date` date NULL DEFAULT NULL COMMENT '日期',
  `net_cap` double(32, 2) NULL DEFAULT NULL,
  `pchg` double(32, 2) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `socket_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `plate_num` int(0) NULL DEFAULT NULL,
  `rocket_launch` int(0) NULL DEFAULT NULL,
  `big_buy` int(0) NULL DEFAULT NULL,
  `big_sales` int(0) NULL DEFAULT NULL,
  `rapid_rebound` int(0) NULL DEFAULT NULL,
  `buy_bid` int(0) NULL DEFAULT NULL,
  `sales_bid` int(0) NULL DEFAULT NULL,
  `high_diving` int(0) NULL DEFAULT NULL,
  `day_low_60` int(0) NULL DEFAULT NULL,
  `sharp_rise_60` int(0) NULL DEFAULT NULL,
  `day_high_60` int(0) NULL DEFAULT NULL,
  `sharp_drop_60` int(0) NULL DEFAULT NULL,
  `down_limit` int(0) NULL DEFAULT NULL,
  `high_limit` int(0) NULL DEFAULT NULL,
  `open_down_limit` int(0) NULL DEFAULT NULL,
  `open_high_limit` int(0) NULL DEFAULT NULL,
  `upward_notch` int(0) NULL DEFAULT NULL,
  `downward_notch` int(0) NULL DEFAULT NULL,
  `accelerate_decline` int(0) NULL DEFAULT NULL,
  `palling_up` int(0) NULL DEFAULT NULL,
  `palling_down` int(0) NULL DEFAULT NULL,
  `low_opened_5` int(0) NULL DEFAULT NULL,
  `high_opened_5` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un`(`plate_name`, `report_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 770716 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for plate_stock
-- ----------------------------
DROP TABLE IF EXISTS `plate_stock`;
CREATE TABLE `plate_stock`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '板块代码',
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '日期',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `vo` double(10, 2) NULL DEFAULT NULL COMMENT '成交量',
  `pchg` double(10, 2) NULL DEFAULT NULL COMMENT '涨跌幅',
  `plate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异常',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un`(`code`, `report_date`, `plate`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18717366 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for profit_ability
-- ----------------------------
DROP TABLE IF EXISTS `profit_ability`;
CREATE TABLE `profit_ability`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `report_date` datetime(0) NULL DEFAULT NULL COMMENT '报告日期',
  `total_assets` double(32, 2) NULL DEFAULT NULL COMMENT '总资产利润率(%)',
  `profit_margin` double(32, 2) NULL DEFAULT NULL COMMENT '主营业务利润率(%)',
  `net_profit_margin_total_assets` double(32, 2) NULL DEFAULT NULL COMMENT '总资产净利润率(%)',
  `cost_profit_margin` double(32, 2) NULL DEFAULT NULL COMMENT '成本费用利润率(%)',
  `operating_profit_margin` double(32, 2) NULL DEFAULT NULL COMMENT '营业利润率(%)',
  `cost_rate_of_main_business` double(32, 2) NULL DEFAULT NULL COMMENT '主营业务成本率(%)',
  `net_profit_margin` double(32, 2) NULL DEFAULT NULL COMMENT '销售净利率(%)',
  `return_on_net_assets` double(32, 2) NULL DEFAULT NULL COMMENT '净资产收益率(%)',
  `return_on_equity` double(32, 2) NULL DEFAULT NULL COMMENT '股本报酬率(%)',
  `return_net_assets` double(32, 2) NULL DEFAULT NULL COMMENT '净资产报酬率(%)',
  `return_assets` double(32, 2) NULL DEFAULT NULL COMMENT '资产报酬率(%)',
  `gross_profit_margin` double(32, 2) NULL DEFAULT NULL COMMENT '销售毛利率(%)',
  `three_expenses` double(32, 2) NULL DEFAULT NULL COMMENT '三项费用比重(%)',
  `non_main_business` double(32, 2) NULL DEFAULT NULL COMMENT '非主营比重(%)',
  `main_profits` double(32, 2) NULL DEFAULT NULL COMMENT '主营利润比重(%)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据入库时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`code`, `report_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '盈利能力' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for security_code
-- ----------------------------
DROP TABLE IF EXISTS `security_code`;
CREATE TABLE `security_code`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '数据入库时间',
  `listing_date` datetime(0) NULL DEFAULT NULL COMMENT '上市时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26009 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '股票代码和名称' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for share
-- ----------------------------
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `security_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `security_name_abbr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `report_date` datetime(0) NULL DEFAULT NULL,
  `equity_record_date` datetime(0) NULL DEFAULT NULL COMMENT '股权登记日',
  `ex_dividend_date` datetime(0) NULL DEFAULT NULL COMMENT '除权出息日',
  `impl_plan_profile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unon`(`security_code`, `equity_record_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '股票分红' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ten_fixed_holder
-- ----------------------------
DROP TABLE IF EXISTS `ten_fixed_holder`;
CREATE TABLE `ten_fixed_holder`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `secucode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `hold_num_change` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '增减(股)',
  `shares_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股份类型',
  `holder_rank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名次',
  `holder_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股东性质',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '报告时间',
  `holder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股东名称',
  `security_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `change_ratio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变动比例',
  `hold_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '持股数(股)',
  `hold_num_ratio` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '占总流通股本持股比例',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `code`(`secucode`, `end_date`, `holder_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ten_flow_holder
-- ----------------------------
DROP TABLE IF EXISTS `ten_flow_holder`;
CREATE TABLE `ten_flow_holder`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `hold_num_change` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '增减(股)',
  `shares_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股份类型',
  `holder_rank` int(0) NULL DEFAULT NULL COMMENT '名次',
  `holder_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股东性质',
  `report_date` date NULL DEFAULT NULL COMMENT '报告时间',
  `holder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股东名称',
  `change_ratio` decimal(16, 2) NULL DEFAULT NULL COMMENT '变动比例',
  `hold_num` bigint(0) NULL DEFAULT NULL COMMENT '持股数(股)',
  `free_holdnum_ratio` decimal(16, 2) NULL DEFAULT NULL COMMENT '占总流通股本持股比例',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`, `report_date`, `holder_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14651565 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '十大流动股东' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tran_detail
-- ----------------------------
DROP TABLE IF EXISTS `tran_detail`;
CREATE TABLE `tran_detail`  (
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `report_date` date NULL DEFAULT NULL COMMENT '报告时间（天）',
  `tran_time` time(0) NULL DEFAULT NULL COMMENT '交易时间（时分秒）',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '成交价',
  `change_price` double(10, 2) NULL DEFAULT NULL COMMENT '价格变动',
  `changed_hands` bigint(0) NULL DEFAULT NULL COMMENT '成交量（手）',
  `changed_price` double(10, 0) NULL DEFAULT NULL COMMENT '成交额（元）',
  `nature` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性质',
  UNIQUE INDEX `bb`(`code`, `report_date`, `tran_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transaction
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `report_date` datetime(0) NOT NULL COMMENT '日期',
  `tclose` double(32, 2) NULL DEFAULT NULL COMMENT '收盘价',
  `nor_tclose` double(32, 2) NULL DEFAULT NULL COMMENT '归一化价格',
  `high` double(32, 2) NULL DEFAULT NULL COMMENT '最高价',
  `low` double(32, 2) NULL DEFAULT NULL COMMENT '最低价',
  `topen` double(32, 2) NULL DEFAULT NULL COMMENT '开盘价',
  `energy` double(32, 2) NULL DEFAULT NULL COMMENT '原始能动比',
  `avg_energy` double(32, 2) NULL DEFAULT NULL,
  `energy_do` double(32, 2) NULL DEFAULT NULL COMMENT '能动最终',
  `amplitude` double(32, 2) NULL DEFAULT NULL COMMENT '振幅',
  `chg` double(32, 2) NULL DEFAULT NULL COMMENT '涨跌额',
  `pchg` double(32, 2) NULL DEFAULT NULL COMMENT '涨跌幅',
  `turnover` double(32, 2) NULL DEFAULT NULL COMMENT '换手率',
  `voturnover` double(32, 2) NULL DEFAULT NULL COMMENT '成交量',
  `vaturnover` double(32, 2) NULL DEFAULT NULL COMMENT '成交金额',
  `section` double(32, 2) NULL DEFAULT NULL,
  `price_section` double(32, 2) NULL DEFAULT NULL COMMENT '价格上涨和下跌区间',
  `data_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据类型，101 天，5分钟，15分钟，30分钟，60分钟',
  `reinstatement` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0复权   1表示前复权  2 后复权',
  `plate_count` int(0) NULL DEFAULT NULL COMMENT '全部异动次数',
  `plate_up_count` int(0) NULL DEFAULT NULL COMMENT '上涨异动次数',
  `t3` double(32, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique`(`report_date`, `reinstatement`, `data_type`, `code`) USING BTREE,
  INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 197042006 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '历史交易数据' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

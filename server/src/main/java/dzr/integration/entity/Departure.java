package dzr.integration.entity;

import lombok.Data;

@Data
public class Departure {
    private Integer id;

    private String code;

    private String name;
    /**
     * 涨跌幅度
     */
    private Double pchg;
    private Double pchg3;
    private Double pchg5;
    private Double pchg10;
    private Double pchg30;

    /**
     * 有效换手率
     */
    private Double changHands;
    private Double changHands3;
    private Double changHands5;
    private Double changHands10;
    private Double changHands30;


    /**
     * 连续上涨天数
     */
    private Integer upDownDays;


    /**
     * 政策出现
     */
    private String policyAppearance;

    /**
     * 板块全面大涨
     */
    private String blockRoseSharp;

    /**
     * 头一天换手率大
     */
    private String highTurnoverRate;

    /**
     * 从最底部已大幅上涨
     */
    private String priceIncrease;

    /**
     * 股价连续的高走
     */
    private String continuousHighWalk;

    /**
     * 出现龙虎榜
     */
    private String dragonsTigersAppears;

    /**
     * 买盘出现大量托单
     */
    private String bulkConsignmentOrder;

}

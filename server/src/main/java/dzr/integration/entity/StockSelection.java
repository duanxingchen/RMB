package dzr.integration.entity;

import lombok.Data;

@Data
public class StockSelection {
    private Integer id;

    private String code;

    private String name;

    /**
     * 主力
     */
    private Double mainForce;

    /**
     * 股价大幅上涨
     */
    private Double priceIncrease;

    /**
     * 流动市值
     */
    private Double flowMarket;

    /**
     * 行业先进
     */
    private String industryAdvanced;

    /**
     * 私募基金
     */
    private String privateFund;

    /**
     * 细分行业龙头
     */
    private String industryLeader;

    /**
     * 政策板块
     */
    private String policyBlock;

    /**
     * 换手率低
     */
    private String lowTurnoverRate;

    /**
     * 底部三红兵
     */
    private String threeRed;

    /**
     * 板块龙头股票热度
     */
    private String heatLeadStock;

    /**
     * 最近大事件
     */
    private String event;


}

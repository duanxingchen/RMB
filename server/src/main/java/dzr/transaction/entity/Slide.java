package dzr.transaction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Slide {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 0--表示上涨，1--表示下跌
     */
    private String type;

    /**
     * 幅度
     */
    private Double range;

    /**
     * 持续天数
     */
    private Integer days;

    /**
     * 幅度
     */
    private Integer poll;

    /**
     * 持续天数
     */
    private Integer step;

    /**
     * 流入资金（万）
     */
    private Long inflowFunds;

    /**
     * 流动市值变化（万）
     */
    private Long flowMarketChange;

}

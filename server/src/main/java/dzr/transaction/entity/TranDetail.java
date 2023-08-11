package dzr.transaction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TranDetail {

    /**
     * 股票代码
     */
    private String code;

    /**
     * 报告时间（天）
     */
    private Date reportDate;

    /**
     * 交易时间（时分秒）
     */
    private Date tranTime;

    /**
     * 成交价
     */
    private Double price;

    /**
     * 价格变动
     */
    private Double changePrice;

    /**
     * 成交量（手）
     */
    private Long changedHands;

    /**
     * 成交额（元）
     */
    private Double changedPrice;

    /**
     * 性质
     */
    private String nature;

}

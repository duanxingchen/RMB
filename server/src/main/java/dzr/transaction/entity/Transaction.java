package dzr.transaction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Transaction {
    private Integer id;

    /**
     * 日期
     */
    private Date reportDate;

    /***
     * 日期排名数字化
     */
    private Integer oderNum;

    /**
     * 能动最小倍数比
     */
    private Double bei;

    /**
     * 股票代码
     */
    private String code;

    private String name;

    /**
     * 收盘价
     */
    private Double tclose;
    private Double norTclose;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 最低价
     */
    private Double low;

    /**
     * 开盘价
     */
    private Double topen;

    /**
     * 能动率
     */
    private Double energy;

    private Double avgEnergy;


    /**
     * 涨跌额
     */
    private Double chg;

    /**
     * 涨跌幅
     */
    private Double pchg;

    /**
     * 换手率
     */
    private Double turnover;

    /**
     * 成交量
     */
    private Double voturnover;

    /**
     * 成交金额
     */
    private Double vaturnover;

    /**
     * 价格变动区间
     */
    private Double priceSection;

    /**
     * 振幅
     */
    private Double amplitude;

    /**
     * 能动处理
     */
    private Double energyDo;

    /**
     * 数据类型，101 天，5分钟，15分钟，30分钟，60分钟
     */
    private String dataType;

    /**
     * 0复权   1表示前复权  2 后复权
     */

    private String reinstatement;

    /**
     * 时间段
     */
    private Double section;

    /**
     * 测试字段
     */
    private Integer plateCount;
    private Integer plateUpCount;
    private Double t3;


}

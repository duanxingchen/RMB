package dzr.plate.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PlateBlock {
    private Integer id;

    /**
     * 板块名称
     */
    private String plateName;

    /**
     * 板块代码
     */
    private String plateCode;

    /**
     * 日期
     */
    private Date reportDate;

    /**
     * 净流入（亿）
     */
    private Double netCap;

    /**
     * 涨跌幅
     */
    private Double pchg;

    /**
     * 异动最频繁的个股
     */
    private String code;

    /**
     * 异动最频繁的个股
     */
    private String name;

    private String socketType;

    /**
     * 异动总次数
     */
    private Integer plateNum;

    /**
     * 火箭发射
     */
    private Integer rocketLaunch;

    /**
     * 大笔买入
     */
    private Integer bigBuy;

    /**
     * 大笔卖出
     */
    private Integer bigSales;

    /**
     * 快速反弹
     */
    private Integer rapidRebound;

    /**
     * 有大买盘
     */
    private Integer buyBid;

    /**
     * 有大卖盘
     */
    private Integer salesBid;

    /**
     * 高台跳水
     */
    private Integer highDiving;

    /**
     * 60日新低
     */
    private Integer dayLow60;

    /**
     * 60日大幅上涨
     */
    private Integer sharpRise60;

    /**
     * 60日新高
     */
    private Integer dayHigh60;

    /**
     * 60日大幅下跌
     */
    private Integer sharpDrop60;

    /**
     * 封跌停板
     */
    private Integer downLimit;

    /**
     * 封涨停板
     */
    private Integer highLimit;

    /**
     * 打开跌停板
     */
    private Integer openDownLimit;

    /**
     * 打开涨停板
     */
    private Integer openHighLimit;

    /**
     * 向上缺口
     */
    private Integer upwardNotch;

    /**
     * 向下缺口
     */
    private Integer downwardNotch;

    /**
     * 加速下跌
     */
    private Integer accelerateDecline;

    /**
     * 竞价上涨
     */
    private Integer pallingUp;

    /**
     * 竞价下跌
     */
    private Integer pallingDown;

    /**
     * 低开5日线
     */
    private Integer lowOpened5;

    /**
     * 高开5日线
     */
    private Integer highOpened5;

}

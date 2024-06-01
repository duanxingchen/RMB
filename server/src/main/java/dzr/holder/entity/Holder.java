package dzr.holder.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Holder implements Serializable {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    private String holderName;


    /**
     * 报告时间
     */
    private Date reportDate;


    /**
     * 股东人数
     */
    private Integer holderNum;

    /**
     * 股票价格最高与最低比例
     */
    private Double price;

    private Double noticePrice;
    /**
     * 股票价格最高与最低比例
     */
    private Double priceRatio;
    /***
     * 当前人数在最近3年的排名
     */
    private Integer oderNum3;
    /***
     * 当前人数在最近5年的排名
     */
    private Integer oderNum5;

    private Integer days;

    private Integer count;

    /**
     * 股东人数最新排名与倒数第二排名的人数变动比
     */
    private Double sort12;

    /**
     * 股东人数最新排名与倒数第三排名的人数变动比
     */
    private Double sort13;

    /**
     * 股东人数最新排名与倒数第四排名的人数变动比
     */
    private Double sort14;

    /**
     * 股东人数最新排名与倒数第五排名的人数变动比
     */
    private Double sort15;

    /**
     * 股东人数最新排名与倒数第六排名的人数变动比
     */
    private Double sort16;
    private Double sort17;
    private Double sort18;
    private Double sort19;

    /**
     * 人均持股金额（万）
     */
    private Double avgAssets;

    /**
     * 总市值
     */
    private Double market;

    /**
     * 实际交易市值
     */
    private Double flowMarket;

    /**
     * 前十大股东占总流通股本持股比例
     */
    private Double tenFlowHolderRatio;

    /**
     * 黑心庄家持股比例
     */
    private Double makersRatio;

    /**
     *
     */
    private Double makerVsSanHuRatio;

    /**
     * 庄家建仓成本
     */
    private Double makersCost;


    /**
     * 散户持股比例
     */
    private Double sanHuRatio;

    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     * 东财行业
     */
    private String dongCaiIndustry;

    /**
     * 大行业
     */
    private String industryBig;

    /**
     * 中行业
     */
    private String industryMiddle;

    /**
     * 上市时间
     */
    private Date listingDate;

    /**
     * 股东人数减少时，价格的变化
     */
    private Double changePriceWithHolderDown;

}

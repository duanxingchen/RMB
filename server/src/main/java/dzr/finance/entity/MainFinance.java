package dzr.finance.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MainFinance {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票代码
     */
    private String name;

    /**
     * 报告日期
     */
    private Date reportDate;

    /**
     * 基本每股收益(元)

     */
    private Double basicEarnings;

    /**
     * 每股净资产(元)
     */
    private Double netAssets;

    /**
     * 每股经营活动产生的现金流量净额(元)
     */
    private Double netCashFlow;

    /**
     * 主营业务收入(万元)
     */
    private Double mainBusinessIncome;

    /**
     * 主营业务利润(万元)
     */
    private Double mainBusinessProfit;

    /**
     * 营业利润(万元)
     */
    private Double operatingProfit;

    /**
     * 投资收益(万元)
     */
    private Double investmentIncome;

    /**
     * 营业外收支净额(万元)
     */
    private Double netNonOperatingIncome;

    /**
     * 利润总额(万元)
     */
    private Double totalProfit;

    /**
     * 净利润(万元)
     */
    private Double netProfit;

    /**
     * 净利润(扣除非经常性损益后)(万元)
     */
    private Double netProfitAfterDeducting;

    /**
     * 经营活动产生的现金流量净额(万元)
     */
    private Double netCashFlowOperating;

    /**
     * 现金及现金等价物净增加额(万元)
     */
    private Double netIncreaseInCash;

    /**
     * 总资产(万元)
     */
    private Double totalAssets;

    /**
     * 流动资产(万元)
     */
    private Double currentAssets;

    /**
     * 总负债(万元)
     */
    private Double totalLiabilities;

    /**
     * 流动负债(万元)
     */
    private Double currentLiabilities;

    /**
     * 股东权益不含少数股东权益(万元)
     */
    private Double shareholders;

    /**
     * 净资产收益率加权(%)
     */
    private Double weightedReturnOnEquity;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;


}

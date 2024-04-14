package dzr.finance.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TotalOperateIncome {
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
     * 最近2年均方差
     */
    private Double variance2;

    /**
     * 最近3年均方差
     */
    private Double variance3;

    /**
     * 最近4年均方差
     */
    private Double variance4;

    /**
     * 最近5年均方差
     */
    private Double variance5;

    /**
     * 最近2年复合增长率（百分比）
     */
    private Double growth2;


    /**
     * 最近3年复合增长率（百分比）
     */
    private Double growth3;


    /**
     * 最近4年复合增长率（百分比）
     */
    private Double growth4;


    /**
     * 最近5年复合增长率（百分比）
     */
    private Double growth5;


    /**
     * 收类型：增涨（up），减少（down）
     */
    private String state;
}

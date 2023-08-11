package dzr.organization.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TenFixedHolder {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票代码
     */
    private String secucode;

    /**
     * 增减(股)
     */
    private String holdNumChange;

    /**
     * 股份类型
     */
    private String sharesType;

    /**
     * 名次
     */
    private String holderRank;

    /**
     * 股东性质
     */
    private String holderType;

    /**
     * 报告时间
     */
    private Date endDate;

    /**
     * 股东名称
     */
    private String holderName;

    /**
     * 股票代码
     */
    private String securityCode;

    /**
     * 变动比例
     */
    private String changeRatio;

    /**
     * 持股数(股)
     */
    private String holdNum;

    /**
     * 占总流通股本持股比例
     */
    private String holdNumRatio;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

}

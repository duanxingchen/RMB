package dzr.organization.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Organization implements Serializable {
    private Integer id;

    /**
     * 机构组织名称
     */
    private String parentOrgName;

    /**
     * 相关机名称
     */
    private String relevantOrgName;

    /**
     * 机构持股总市值
     */
    private Double holdMarketCap;

    private Double increaseRate;


    /**
     * 机构相关系数
     */
    private Double relevantRate;

    /**
     * 两个相关机构持股数量之和
     */
    private Integer denominator;

    /**
     * 两个相关机构持股交集的数量
     */
    private Integer numerator;

    /**
     * 机构下属基金数量数量
     */
    private Integer orgNum;

    /**
     * 股票数量
     */
    private Integer securityNum;

    /**
     * 报告时间
     */
    private String reportDate;

    /**
     * 股票代码集合(逗号隔开)
     */
    private String securityCodes;

    /**
     * 机构组织代码
     */
    private String parentOrgCodeOld;

    /**
     * 相关机构代码(逗号隔开)
     */
    private String relevantOrg;

    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

}

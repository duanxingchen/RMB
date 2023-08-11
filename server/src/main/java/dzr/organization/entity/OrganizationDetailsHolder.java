package dzr.organization.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrganizationDetailsHolder {
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
     * 机构组织代码
     */
    private String parentOrgCode;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 机构类型编码
     */
    private String orgTypeCode;

    /**
     * 股票代码内部
     */
    private String securityInnerCode;

    /**
     * 	占流通股本比例(%)
     */
    private Double freeSharesRatio;

    /**
     * 机构组织代码
     */
    private String parentOrgCodeOld;

    /**
     * 机构组织名称
     */
    private String parentOrgName;

    /**
     * 持股市值
     */
    private Double holdMarketCap;

    /**
     * 机构代码
     */
    private String holderCode;

    /**
     * 持股总数
     */
    private Long totalShares;

    /**
     * 	占总股本比例(%
     */
    private Double totalSharesRatio;

    /**
     * 报告时间
     */
    private Date reportDate;

    /**
     * 机构名称
     */
    private String holderName;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     * 净资产率
     */
    private Double netAssetRatio;

    /**
     * 机构名称
     */
    private String orgNameAbbr;

    private String buyState;

}

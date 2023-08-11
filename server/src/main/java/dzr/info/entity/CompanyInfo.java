package dzr.info.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyInfo {
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
     * 上市日期
     */
    private Date listingDate;

    /**
     * 成立日期
     */
    private Date establishmentDate;

    /**
     * 发行量
     */
    private String issuance;

    /**
     * 每股发行价
     */
    private Double issuePrice;

    /**
     * 所属东财行业
     */
    private String dongcaiIndustry;

    /**
     * 所属证监会行业
     */
    private String securitiesIndustry;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 董事长
     */
    private String chairman;

    /**
     * 区域
     */
    private String area;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 雇员人数
     */
    private Integer employees;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;


}

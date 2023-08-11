package dzr.organization.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrganizationDetailRate {
    private Integer id;

    /**
     * 上级机构组织名称
     */
    private String parentHolderName;

    /**
     * 机构组织名称
     */
    private String holderName;

    /**
     * 机构代码
     */
    private String holderCode;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 机构持股总市值(万）
     */
    private Double holderCap;

    /**
     * 机构利润
     */
    private Double holderProfit;

    /**
     * 机构利润
     */
    private Double holderProfitRate;

    /**
     * 持股天数
     */
    private Integer dayNum;

    private Date startDate;

    private Date endDate;

}

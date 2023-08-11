package dzr.organization.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TenFlowHolder {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

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
    private Integer holderRank;

    /**
     * 股东性质
     */
    private String holderType;

    /**
     * 报告时间
     */
    private Date reportDate;

    /**
     * 股东名称
     */
    private String holderName;

    /**
     * 变动比例
     */
    private BigDecimal changeRatio;

    /**
     * 持股数(股)
     */
    private Long holdNum;

    /**
     * 占总流通股本持股比例
     */
    private BigDecimal freeHoldnumRatio;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

}

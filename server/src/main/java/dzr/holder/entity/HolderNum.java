package dzr.holder.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HolderNum {
    private Integer id;

    private BigDecimal totalMarketCap;

    private Long changeShares;

    private Long holderNum;

    private Long holderNumChange;

    private String name;

    private Date reportDate;

    private String code;

    private Long preHolderNum;

    private Date holdNoticeDate;

    private BigDecimal holderNumRatio;

    private Date preEndDate;

    private String changeReason;

    private Long totalAShares;

    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 数据更新时间
     */
    private Date updateTime;

}

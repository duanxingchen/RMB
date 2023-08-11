package dzr.transaction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TranDetailCount {
    private Integer id;

    private String code;

    private String name;

    /**
     * 报告时间
     */
    private Date reportDate;

    /**
     * 幅度
     */
    private Double priceUpSum;

    private Double priceDownSum;

    private Double handUpSum;

    private Double handDownSum;

    /**
     *
     */
    private Double upNum;

    private Double downNum;
}

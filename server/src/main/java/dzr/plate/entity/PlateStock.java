package dzr.plate.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PlateStock {
    private Integer id;

    /**
     * 异动最频繁的个股
     */
    private String code;

    /**
     * 异动最频繁的个股
     */
    private String name;

    /**
     * 日期
     */
    private Date reportDate;

    /**
     * 涨跌幅
     */
    private Double pchg;

    private Double price;

    /**
     * 成交手数
     */
    private Double vo;

    /**
     * 详细信息
     */
    private String detail;

    /**
     * 异动总次数
     */
    private String plate;

    /**
     * 所属东财行业
     */
    private String dongcaiIndustry;

}

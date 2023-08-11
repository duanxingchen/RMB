package dzr.web.service.todo;

import lombok.Data;

import java.util.Date;

/**
 * @Author dzr
 * @Date 2023/3/3 15:46
 * @Version 1.0
 * @Description:
 */
@Data
public class PlateStockTop {
    private String name;

    private String code;

    private Date reportDate;

    private String dongcaiIndustry;

    private Double flowMarket;

    /**
     * 火箭发射次数
     */
    private Integer fireTimes;

    /**
     * 距上次
     */
    private Integer fireDays;

    /**
     * 距上上
     */
    private Integer fireDays1;

    /**
     * 涨跌幅度
     */
    private Double pchg;
    private Double pchg3;
    private Double pchg5;

    /**
     * 有效换手率
     */
    private Double changHands;
    private Double changHands3;
    private Double changHands5;




}

package dzr.transaction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OneHandCount {
    private Integer id;

    private String name;

    private String code;

    private Date reportDate;
    private Double price;

    /**
     * 一手交易次数
     */
    private Double oneHandTimes;

    private Double towHandTimes;

    /**
     * 集合竞价成交手动
     */
    private Double concurrenceHands;


    /**
     * 总交易次数
     */
    private Double allTimes;

    /**
     * 一手交易次数占比
     */
    private Double oneHandRate;

    /**
     * 一手交易距前一笔交易的总时间（秒）
     */
    private Double oneHandSumTime;

    /**
     * 一手交易距前一笔交易的平均时间（秒）
     */
    private Double oneHandAvgTime;

}

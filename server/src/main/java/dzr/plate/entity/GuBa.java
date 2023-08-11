package dzr.plate.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GuBa {
    private Integer id;

    private String code;

    private String name;

    private String date;

    /**
     * 人气排名
     */
    private Integer rankNum;

    /**
     * 人气排名时间
     */
    private Date rankNumDate;

    /**
     * 实时人气变动
     */
    private Integer lineRankNum;

    /**
     * 实时变动人气排名时间
     */
    private Date lineRankNumDate;

    /**
     * 昨天人气变动
     */
    private Integer yesterdayRankNum;

    /**
     * 昨天人气变动时间
     */
    private Date yesterdayRankNumDate;

}

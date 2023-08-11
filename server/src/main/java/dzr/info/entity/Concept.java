package dzr.info.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Concept {
    private Integer id;

    private Double sortAvg12;

    private Double sortAvg13;

    private Double sortAvg14;

    private Double sortAvg15;

    private Double sortAvg16;

    private String num;

    private String concept;

    private Date createTime;

    private Date updateTime;

}

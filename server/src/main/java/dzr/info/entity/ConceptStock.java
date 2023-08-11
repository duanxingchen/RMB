package dzr.info.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ConceptStock {
    private Integer id;

    private String code;

    private String name;

    private String concept;

    private Date createTime;

    private Date updateTime;

}

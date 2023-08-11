package dzr.info.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Event {
    private Integer id;

    private String code;

    private String name;

    private String eventType;

    private String content;

    private Date noticeDate;

}

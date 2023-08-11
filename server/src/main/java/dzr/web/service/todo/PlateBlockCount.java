package dzr.web.service.todo;

import lombok.Data;

import java.util.Date;

/**
 * @Author dzr
 * @Date 2023/2/23 14:00
 * @Version 1.0
 * @Description:
 */
@Data
public class PlateBlockCount {
    private String concept;
    private Date reportDate;
    private Integer plateNum;
    private Long upPlate;

}

package dzr.web.service.todo;

import lombok.Data;

/**
 * @Author dzr
 * @Date 2023/2/23 14:00
 * @Version 1.0
 * @Description:
 */
@Data
public class PlateCount {
    private String concept;
    private Integer plateNum;
    private Integer upPlate;
    private Long stockNum;
    /**
     * 涨异常次数/股票数据量
     */
    private Double rate;

}

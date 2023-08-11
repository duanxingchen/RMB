package dzr.integration.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author dzr
 * @Date 2023/4/7 11:41
 * @Version 1.0
 * @Description:
 */
@Data
public class SquareRoot {
    private String code;
    private String name;

    private String vsCode;
    private String vsName;

    private Date vsStartDate;
    private Date vsEndDate;

    private Double sqrt;
    private Double sqrtFuture;
    private Double futurePrice1;
    private Double futurePrice3;
    private Double futurePrice5;
    private Double futurePrice10;

}

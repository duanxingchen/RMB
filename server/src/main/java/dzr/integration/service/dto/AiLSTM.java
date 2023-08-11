package dzr.integration.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author dzr
 * @Date 2023/4/12 11:18
 * @Version 1.0
 * @Description:
 */
@Data
public class AiLSTM {
    private String code;
    private String name;
    private Date reportDate;
    private Double price;
    private Double tornOver;
}

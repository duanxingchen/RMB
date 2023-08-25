package dzr.transaction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class JianCangDto {
    private String code;
    private String name;
    private Date reportDate;
    private String nature;
    private Double sumMemory;
    private Integer times;
}

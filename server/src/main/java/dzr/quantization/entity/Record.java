package dzr.quantization.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Record {
    private Integer id;

    private String code;

    private String name;

    private String type;

    private Double price;

    private Timestamp TradingTime;
}

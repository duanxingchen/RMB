package dzr.quantization.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class PlateHeat {
    private Integer id;

    private String code;

    private String name;

    private Double plateScore;

    private Double gubaScore;

    private Timestamp time;
}

package dzr.quantization.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StockPool {
    private Integer id;

    private String code;

    private String name;

    private Double price;

    private Double priceRange;

    public String getCodeWithNumber() {
        return code.startsWith("6")? "1."+ code :(code.startsWith("8")? code +".bj":"0."+ code);
    }

}

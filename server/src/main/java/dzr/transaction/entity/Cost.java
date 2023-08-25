package dzr.transaction.entity;

import lombok.Data;

@Data
public class Cost {
    private Integer id;

    private String code;

    private String name;
    private String dongCaiIndustry;

    private Double days1ZfRate;
    private Double days3ZfRate;
    private Double days5ZfRate;

    private Double days1HandsRate;
    private Double days3HandsRate;

    private Double days5HandsRate;

    private Double days10HandsRate;

    private Double days20HandsRate;

    private Double days30HandsRate;

    private Double days60HandsRate;
    private Double days1PriceRate;
    private Double days3PriceRate;

    private Double days5PriceRate;

    private Double days10PriceRate;

    private Double days20PriceRate;

    private Double days30PriceRate;

    private Double days60PriceRate;

}

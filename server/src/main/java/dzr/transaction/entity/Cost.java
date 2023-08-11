package dzr.transaction.entity;

import lombok.Data;

@Data
public class Cost {
    private Integer id;

    private String code;

    private String name;
    private Double days1Cost;

    private Double days3Cost;

    private Double days5Cost;

    private Double days10Cost;

    private Double days20Cost;

    private Double days30Cost;

    private Double days60Cost;
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

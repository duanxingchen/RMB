package dzr.transaction.entity;

import lombok.Data;

@Data
public class Energy {
    private Integer id;

    private String code;

    private String name;

    /**
     * 最近3天平均能动
     */
    private Double energyRat3;

    /**
     * 最近5天平均能动
     */
    private Double energyRat5;

    /**
     * 最近10天平均能动
     */
    private Double energyRat10;

    /**
     * 最近15天平均能动
     */
    private Double energyRat15;

    /**
     * 最近20天平均能动
     */
    private Double energyRat20;

    /**
     * 最近30天平均能动
     */
    private Double energyRat30;

    /**
     * 最近40天平均能动
     */
    private Double energyRat40;

    private Double energyRat60;
    private Double energyRat100;

    /**
     * 上一次最近30天平均能动（股价横盘期）
     */
    private Double energyAvg30;



    /**
     * 上一次最近60天平均能动（股价横盘期）
     */
    private Double energyAvg60;

    /**
     * 上一次最近100天平均能动（股价横盘期）
     */
    private Double energyAvg100;

    /**
     * 上一次最近150天平均能动（股价横盘期）
     */
    private Double energyAvg150;

    private Double energyAvg200;

    private Double rankNum;

    }

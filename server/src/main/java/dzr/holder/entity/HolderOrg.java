package dzr.holder.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HolderOrg implements Serializable {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;


    /**
     * 建仓开始时间
     */
    private Date startTime;

    /**
     * 建仓结束时间
     */
    private Date endTime;
    /**
     * 建仓耗时天数
     */
    private Integer dateNum;

    /**
     * 股东人数变化率
     */
    private Double holderChangeRate;


    /**
     * 股价变动率
     */
    private Double priceRate;

    /**
     * 股东人数变化率/建仓天数（建仓风格）
     */
    private Double style;

    private Double tenDayMaxPrice;

}

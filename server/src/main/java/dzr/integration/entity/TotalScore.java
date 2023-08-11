package dzr.integration.entity;

import lombok.Data;

/**
 * @Author dzr
 * @Date 2021/11/29 20:59
 * @Version 1.0
 * @Description:各项得分分别为100分，总得分为各项得分*权重的总和
 */
@Data
public class TotalScore {
    private Integer id;

    private String code;

    private String name;

    /**
     * 一票否决
     */
    private Byte  oneVoteVeto = 0;

    /**
     * 一票否决说明
     */
    private String oneVoteVetoRemarks = "";


    /**
     * 股东人数得分 50
     */
    private Double holderScore;

    /**
     * 价格变动得分 20
     */
    private Double priceScore;

    /**
     * 概念板块得分 10
     */
    private Double conceptScore;

    /**
     * 行业板块得分 10
     */
    private Double industryScore;

    /**
     * 财务状况得分 0
     */
    private Double financeScore;

    /**
     * 市场规模得分 10
     */
    private Double marketScore;

    /**
     * 总分
     */
    private Double totalScore;
}

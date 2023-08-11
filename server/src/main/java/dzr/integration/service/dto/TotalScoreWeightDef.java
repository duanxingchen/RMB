package dzr.integration.service.dto;

import lombok.Data;

/**
 * @Author dzr
 * @Date 2021/11/29 20:59
 * @Version 1.0
 * @Description:各项得分分别为100分，总得分为各项得分*权重的总和
 */
@Data
public class TotalScoreWeightDef {

    /**
     * 股东人数得分 50
     */
    public final static double HOLDER_SCORE_WEIGHT = 0.5;

    /**
     * 价格变动得分 20
     */
    public final static double PRICE_SCORE_WEIGHT = 0.2;

    /**
     * 概念板块得分 10
     */
    public final static double CONCEPT_SCORE_WEIGHT = 0.1;

    /**
     * 行业板块得分 10
     */
    public final static double INDUSTRY_SCORE_WEIGHT = 0.1;

    /**
     * 财务状况得分 0
     */
    public final static double FINANCE_SCORE_WEIGHT = 0.0;

    /**
     * 市场规模得分 10
     */
    public final static double MARKET_SCORE_WEIGHT = 0.1;

}

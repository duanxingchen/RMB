package dzr.finance.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Gincomeqc {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票代码
     */
    private String name;

    /**
     * 报告日期
     */
    private Date reportDate;

    /**
     *营业总收入
     */
    private Double totalOperateIncome;

    /**
     *营业总成本
     */
    private Double totalOperateCost;

    /**
     *营业成本
     */
    private Double operateCost;

    /**
     *研发费用
     */
    private Double researchExpense;

    /**
     *营业税金及附加
     */
    private Double operateTaxAdd;

    /**
     *销售费用
     */
    private Double saleExpense;

    /**
     *管理费用
     */
    private Double manageExpense;

    /**
     *财务费用
     */
    private Double financeExpense;

    /**
     *其中:利息费用
     */
    private Double feInterestExpense;

    /**
     *其中:利息收入
     */
    private Double feInterestIncome;

    /**
     *其他收益
     */
    private Double otherIncome;

    /**
     *投资收益
     */
    private Double investIncome;

    /**
     *加:公允价值变动收益
     */
    private Double fairvalueChangeIncome;

    /**
     *信用减值损失(新)
     */
    private Double creditImpairmentIncome;

    /**
     *营业利润
     */
    private Double operateProfit;

    /**
     *加:营业外收入
     */
    private Double nonbusinessIncome;

    /**
     *减:营业外支出
     */
    private Double nonbusinessExpense;

    /**
     *利润总额
     */
    private Double totalProfit;

    /**
     *减:所得税
     */
    private Double incomeTax;

    /**
     *净利润
     */
    private Double netprofit;

    /**
     *持续经营净利润
     */
    private Double continuedNetprofit;

    /**
     *归属于母公司股东的净利润
     */
    private Double parentNetprofit;

    /**
     *扣除非经常性损益后的净利润
     */
    private Double deductParentNetprofit;

}

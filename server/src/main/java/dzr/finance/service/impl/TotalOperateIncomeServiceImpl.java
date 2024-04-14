package dzr.finance.service.impl;


import dzr.common.utils.MathUtils;
import dzr.finance.entity.Gincomeqc;
import dzr.finance.entity.TotalOperateIncome;
import dzr.finance.mapper.GincomeqcMapper;
import dzr.finance.mapper.TotalOperateIncomeMapper;
import dzr.finance.service.TotalOperateIncomeService;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TotalOperateIncomeServiceImpl implements TotalOperateIncomeService {
    private final SecurityCodeMapper securityCodeMapper;
    private final GincomeqcMapper gincomeqcMapper;
    private final TotalOperateIncomeMapper totalOperateIncomeMapper;

    @Override
    public void calculate() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            //securityCode.setCode("300818");
            log.info(securityCode.toString());
            List<Gincomeqc> gincomeqcs = gincomeqcMapper.selectByCode(securityCode.getCode());
            gincomeqcs = gincomeqcs.stream().sorted(Comparator.comparing(Gincomeqc::getReportDate).reversed()).collect(Collectors.toList());

            if (gincomeqcs.size() >8){

                try {


                    /**
                     * 减去其中:利息收入、其他收益、投资收益、加:公允价值变动收益、信用减值损失(新)
                     */
                    gincomeqcs.stream().forEach(gincomeqc -> {
                        double otherIncome = 0.0;
                        if (gincomeqc.getInvestIncome() != null && gincomeqc.getInvestIncome() > 0){
                            otherIncome = otherIncome + gincomeqc.getInvestIncome();
                        }
                        if (gincomeqc.getCreditImpairmentIncome() != null && gincomeqc.getCreditImpairmentIncome() > 0){
                            otherIncome = otherIncome + gincomeqc.getCreditImpairmentIncome();
                        }


                        if (gincomeqc.getFairvalueChangeIncome() != null && gincomeqc.getFairvalueChangeIncome() > 0){
                            otherIncome = otherIncome + gincomeqc.getFairvalueChangeIncome();
                        }


                        if (gincomeqc.getOtherIncome() != null && gincomeqc.getOtherIncome() > 0){
                            otherIncome = otherIncome + gincomeqc.getOtherIncome();
                        }

                        if (gincomeqc.getFeInterestIncome() != null && gincomeqc.getFeInterestIncome() > 0){
                            otherIncome = otherIncome + gincomeqc.getFeInterestIncome();
                        }
                        gincomeqc.setTotalOperateIncome(gincomeqc.getTotalOperateIncome() - otherIncome);
                    });





                    /**
                     * 1、以连续4个季度组成最近一年的收入，得到最近5年收入
                     */
                    ArrayList<Double> sumOneYear = new ArrayList<>();
                    for (int i = 0; i <= gincomeqcs.size()-4; i=i+4) {
                        sumOneYear.add(gincomeqcs.get(i).getTotalOperateIncome()+gincomeqcs.get(i+1).getTotalOperateIncome()+
                                gincomeqcs.get(i+2).getTotalOperateIncome()+gincomeqcs.get(i+3).getTotalOperateIncome());

                    }

                    /**
                     * 2、缩放收入比例。每期值除以最小值。
                     */
                    double min = sumOneYear.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
                    /**
                     * 最低年收入1一个亿
                     */
                    if (min > 200000000){
                        ArrayList<Double> sumOneYearNew = new ArrayList<>();
                        sumOneYear.stream().forEach(one -> {
                            double v = one / min;
                            sumOneYearNew.add(v);
                        });

                        /**
                         * 3、分别计算2年，3年，4年，5年收入的均方差
                         */
                        TotalOperateIncome totalOperateIncome = new TotalOperateIncome();
                        totalOperateIncome.setCode(securityCode.getCode());
                        totalOperateIncome.setName(securityCode.getName());
                        totalOperateIncome.setVariance2(getMeanSquareDeviation(sumOneYearNew,2));
                        totalOperateIncome.setVariance3(getMeanSquareDeviation(sumOneYearNew,3));
                        totalOperateIncome.setVariance4(getMeanSquareDeviation(sumOneYearNew,4));
                        totalOperateIncome.setVariance5(getMeanSquareDeviation(sumOneYearNew,5));


                        /**
                         * 4、收类型：增涨（up），减少（down）
                         */
                        if (sumOneYear.get(0) > sumOneYear.get(sumOneYear.size()-1)){
                            totalOperateIncome.setState("up");
                        }else {
                            totalOperateIncome.setState("down");
                        }


                        /**
                         * 5、复合增长率（百分比）
                         */
                        totalOperateIncome.setGrowth2(getCompoundGrowthRate(sumOneYearNew,2));
                        totalOperateIncome.setGrowth3(getCompoundGrowthRate(sumOneYearNew,3));
                        totalOperateIncome.setGrowth4(getCompoundGrowthRate(sumOneYearNew,4));
                        totalOperateIncome.setGrowth5(getCompoundGrowthRate(sumOneYearNew,5));


                        totalOperateIncomeMapper.delete(totalOperateIncome);
                        totalOperateIncomeMapper.insert(totalOperateIncome);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private Double getCompoundGrowthRate(ArrayList<Double> sumOneYear, int num) {


        if (sumOneYear.size() >= num){
            ArrayList<Double> list = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                list.add(sumOneYear.get(i));
            }
            return growth(list);
        }
        return null;
    }

    private Double growth(ArrayList<Double> list) {
        Double oldV = list.get(list.size() - 1);
        Double newV = list.get(0);
        return MathUtils.doubleRetain2Bit(100*(newV-oldV)/oldV);
    }


    private Double getMeanSquareDeviation(ArrayList<Double> sumOneYear, int num) {


        if (sumOneYear.size() >= num){
            ArrayList<Double> list = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                list.add(sumOneYear.get(i));
            }
            return variance(list);
        }
        return null;
    }

    public static double variance(ArrayList<Double> x) {
        int m=x.size();
        if(m == 0){
            return 0.0;
        }
        double sum=0;

        for(int i=0;i<m;i++){//求和
            sum += x.get(i);
        }

        double dAve=sum/m;//求平均值
        double dVar=0;

        for(int i=0;i<m;i++){//求方差
            dVar+=(x.get(i)-dAve)*(x.get(i)-dAve);
        }

        return MathUtils.doubleRetain2Bit(Math.sqrt(dVar/m));
    }
}
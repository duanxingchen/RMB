package dzr.holder.service.comprehensive;

import dzr.common.utils.ReportDateUtils;
import dzr.holder.entity.HolderNum;
import dzr.holder.mapper.HolderNumMapper;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 连续的股东人数发布(最新一次股东人数报告时期间隔小于32天)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class Shareholders implements Filter{
    private final HolderNumMapper holderNumMapper;
    private final static int DAYS = 15;
    private final static double  HOLDER_RATE = 1.2;

    /**
     * 过滤掉不符合的code，返回股票代码
     * @return
     */
    @Override
    public List<SecurityCode> filter(List<SecurityCode> securityCodeList){
        ArrayList<SecurityCode> includeSecurityCodes = new ArrayList<>();
        securityCodeList.forEach(securityCode -> {
            log.info("Shareholders : {}",securityCode);
            List<HolderNum> holderNums =  holderNumMapper.selectByCode(securityCode.getCode());
            holderNums = holderNums.stream().sorted(Comparator.comparing(HolderNum::getReportDate).reversed()).collect(Collectors.toList());
            /**
             * 初始化股东人数，取3个平均值，最近一期的人数不变
             */
            initiateHolderNums(holderNums);
            if (holderNums.size() >2 ){
                log.info("" + (holderNums.get(0).getReportDate().getTime() - holderNums.get(1).getReportDate().getTime() )/1000/60/60/24);
                if ((holderNums.get(0).getReportDate().getTime() - holderNums.get(1).getReportDate().getTime())/1000/60/60/24 < DAYS){


                    List<Date> newestReportDates = ReportDateUtils.getNewestReportDates(4);
                    long nowHolderNum = holderNums.get(0).getHolderNum();
                    double firstHolderNumRate = (double)getHolderByReportDate(holderNums, newestReportDates.get(0))/nowHolderNum;
                    double secondHolderNumRate = (double)getHolderByReportDate(holderNums, newestReportDates.get(1))/nowHolderNum;
                    double threeHolderNumRate = (double)getHolderByReportDate(holderNums, newestReportDates.get(2))/nowHolderNum;
                    double fourHolderNumRate = (double)getHolderByReportDate(holderNums, newestReportDates.get(3))/nowHolderNum;

                    if (firstHolderNumRate > HOLDER_RATE
                    || secondHolderNumRate > HOLDER_RATE
                    || threeHolderNumRate > HOLDER_RATE
                    || fourHolderNumRate > HOLDER_RATE){
                        securityCode.setName(securityCode.getName() + "   股东变化:" + Math.max(Math.max(firstHolderNumRate,secondHolderNumRate),Math.max(threeHolderNumRate,fourHolderNumRate)));
                        includeSecurityCodes.add(securityCode);

                    }
                }
            }
        });
        return includeSecurityCodes;
       
   }
    private void initiateHolderNums(List<HolderNum> holderNums) {
        ArrayList<Long> newHolderNums = new ArrayList<>();
        if (holderNums.size() < 3){
            return ;
        }

        /**
         * 最新一期股东人数不变
         */
        newHolderNums.add(holderNums.get(0).getHolderNum());
        for (int i = 1; i < (holderNums.size() - 2); i++) {
            long l = (holderNums.get(i - 1).getHolderNum() + holderNums.get(i).getHolderNum() + holderNums.get(i + 1).getHolderNum()) / 3;
            newHolderNums.add(l);
        }
        for (int i = 0; i < newHolderNums.size(); i++) {
            holderNums.get(i).setHolderNum(newHolderNums.get(i));
        }

    }
    private long getHolderByReportDate(List<HolderNum> holderNums, Date date) {
        return holderNums.stream().filter(holderNum -> holderNum.getReportDate().getTime() <= date.getTime()).collect(Collectors.toList()).get(0).getHolderNum();
    }
}

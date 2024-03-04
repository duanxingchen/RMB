package dzr.holder.service.impl;

import com.google.common.util.concurrent.AtomicDouble;
import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.common.utils.ReportDateUtils;
import dzr.holder.entity.Holder;
import dzr.holder.entity.HolderNum;
import dzr.holder.entity.HolderOrg;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.mapper.HolderNumMapper;
import dzr.holder.mapper.HolderOrgMapper;
import dzr.holder.service.HolderService;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.entity.TenFlowHolder;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.organization.mapper.TenFlowHolderMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {

    private final SecurityCodeMapper securityCodeMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final TenFlowHolderMapper tenFlowHolderMapper;
    private final HolderNumMapper holderNumMapper;
    private final TransactionMapper transactionMapper;
    private final HolderMapper holderMapper;
    private final HolderOrgMapper holderOrgMapper;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;

    public void calculate() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            //securityCode.setCode("300862");
            CompanyInfo companyInfo = companyInfoMapper.selectOneByCode(securityCode.getCode());
            List<TenFlowHolder> tenFlowHolders = tenFlowHolderMapper.selectByCode(securityCode.getCode());
            List<HolderNum> holderNums =  holderNumMapper.selectByCode(securityCode.getCode());
            List<Transaction> transactions = transactionMapper.selectReinstatementByCode(securityCode.getCode());
            List<OrganizationDetailsHolder> organizationDetailsHolders = organizationDetailsHolderMapper.selectByCode(securityCode.getCode());

            if (holderNums.size() > 0 && transactions.size() >0){
                log.info(securityCode.toString());
                HolderDto holderDto = new HolderDto(securityCode, new Holder(), holderNums, transactions, companyInfo, tenFlowHolders,organizationDetailsHolders);
                holderDto.doIt();
            }else {
                log.error("股东人数错误: {}",securityCode.toString());
            }

        });
    }

    private class HolderDto{
        private final List<OrganizationDetailsHolder> organizationDetailsHolders;
        private Holder holder;
        private List<HolderNum> holderNums;
        private List<HolderNum> holderNumsOld;
        private List<Transaction> transactions;
        private SecurityCode securityCode;
        private CompanyInfo companyInfo;
        private List<TenFlowHolder> tenFlowHolders;

        /**
         * 机构建仓风格阈值
         */
        private double newThreshold = 0.2;
        private double oldThreshold = 0.2;

        private HolderDto(SecurityCode securityCode, Holder holder, List<HolderNum> holderNums, List<Transaction> transactions, CompanyInfo companyInfo, List<TenFlowHolder> tenFlowHolders, List<OrganizationDetailsHolder> organizationDetailsHolders) {
            this.holder = holder;
            this.holderNums = holderNums;
            this.transactions = transactions;
            this.securityCode = securityCode;
            this.companyInfo = companyInfo;
            this.tenFlowHolders = tenFlowHolders;
            this.organizationDetailsHolders =organizationDetailsHolders;
        }
        public void doIt(){
            try {
                transactions = transactions.stream().sorted(Comparator.comparing(Transaction::getReportDate).reversed()).collect(Collectors.toList());
                holderNums = holderNums.stream().sorted(Comparator.comparing(HolderNum::getReportDate).reversed()).collect(Collectors.toList());

                /**
                 * 初始化股东人数，取3个平均值，最近一期的人数不变
                 */
                initiateHolderNums(holderNums);
                /**
                 * 用于主力风格计算
                 */
                holderNumsOld = holderNums.stream().filter(holderNum -> (System.currentTimeMillis() - holderNum.getReportDate().getTime())/365/24/3600000 < 2)
                        .sorted(Comparator.comparing(HolderNum::getReportDate).reversed()).collect(Collectors.toList());

                /**
                 * 人数减少期数和天数
                 */
                countAndDays();

                /**
                 * 人数对比
                 */
                calculateRate();
                /**
                 * 行业和上市时间
                 */
                setIndustryAndListingDate();
                /**
                 * 十大流通股东
                 */
                calculateTenFlowHolderRatio();
                /**
                 * 主力与散户对比
                 */
                calculateMakersRateWithSanHuRate();
                /**
                 * 流通市值和人均持股金额（万）
                 */
                calculateFlowMarket();
                /**
                 * 庄家成本
                 */
                calculateMakersCost();
                /**
                 * 价格
                 */
                calculatePrice();

                /**
                 * 计算是否包括机构私募
                 */
                calculateOrganization();


                /**
                 * 计算股东建仓风格
                 */
                calculateHolderOrg();

                holderMapper.delete(holder);
                holderMapper.insert(holder);
            }catch (Exception e){
                e.printStackTrace();

            }

        }

        private void calculateHolderOrg() {
            HolderOrg holderOrg = new HolderOrg();

            HolderNum startHolder = null;
            HolderNum endHolder = null;
            int startNum =0;


            /**
             * 上市时间满2年
             */
            if ((System.currentTimeMillis() - holder.getListingDate().getTime())/365/24/3600000 < 2){
                return;
            }

            /**
             * 股东人数减少
             */
            if(holder.getSort12() < 0.9 || holder.getSort13() < 0.9 || holder.getSort14() < 0.9){
                return;
            }

            /**
             * 1 找开始时间，从当前位置倒退，若大于阈值，则为开始时间，
             */
            for (int i = 0; i < holderNumsOld.size() -1; i++) {
                if ((holderNumsOld.get(i+1).getHolderNum() - holderNumsOld.get(i).getHolderNum())/(holderNumsOld.get(i).getHolderNum()*1.0) > newThreshold*(DateUtils.differentDays(holderNumsOld.get(i+1).getReportDate(),holderNumsOld.get(i).getReportDate()))/90.0){
                    endHolder =  holderNumsOld.get(i);
                    startNum = i;
                    break;
                }
            }
            /**
             * 没有任何一期超过阈值，退出
             */
            if (endHolder == null){
                return;
            }


            /**
             * 2 找结束时间，从开始时间倒推，若小于阈值，则为结束时间。
             */
            for (int i = startNum+1; i < holderNumsOld.size() -1; i++) {
                if ((holderNumsOld.get(i+1).getHolderNum() - holderNumsOld.get(i).getHolderNum())/(holderNumsOld.get(i).getHolderNum()*1.0) < oldThreshold){
                    startHolder = holderNumsOld.get(i);
                    break;
                }
            }
            /**
             * 没有任何一期小于阈值，退出
             */
            if (startHolder == null){
                return;
            }

            Date startDate = startHolder.getReportDate();
            Date endDate = endHolder.getReportDate();

            Long startHolderNum = startHolder.getHolderNum();
            Long endHolderNum = endHolder.getHolderNum();


            double startPrice = this.transactions.stream().filter(transaction -> transaction.getReportDate().getTime() <= startDate.getTime()).findFirst().get().getTclose();
            double endPrice = this.transactions.stream().filter(transaction -> transaction.getReportDate().getTime() <= endDate.getTime()).findFirst().get().getTclose();

            holderOrg.setCode(securityCode.getCode());
            holderOrg.setName(securityCode.getName());
            holderOrg.setStartTime(startDate);
            holderOrg.setEndTime(endDate);
            holderOrg.setDateNum(DateUtils.differentDays(endDate,startDate));
            holderOrg.setPriceRate(MathUtils.doubleRetain2Bit((endPrice-startPrice)/startPrice*100));
            holderOrg.setHolderChangeRate(MathUtils.doubleRetain2Bit((endHolderNum-startHolderNum)/(startHolderNum*1.0)*100));
            holderOrg.setStyle(MathUtils.doubleRetain2Bit(holderOrg.getHolderChangeRate()*holderOrg.getPriceRate()/holderOrg.getDateNum()));
            holderOrgMapper.delete(holderOrg);
            holderOrgMapper.insert(holderOrg);
        }

        /**
         *
         * @param holderNums 已经按最新日期排序的股人数
         */
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

        /**
         * 包括私募基金
         */
        private void calculateOrganization() {
            if (organizationDetailsHolders.size() > 0){
                List<Date> newestReportDates = ReportDateUtils.getNewestReportDates(1);
                long newWestTimes = organizationDetailsHolders.stream().map(OrganizationDetailsHolder::getReportDate).mapToLong(Date::getTime).max().getAsLong();
                List<String> collect = organizationDetailsHolders.stream().filter(organizationDetailsHolder ->
                        organizationDetailsHolder.getReportDate().getTime() == newWestTimes &&
                                (organizationDetailsHolder.getHolderName().contains("私募") || organizationDetailsHolder.getHolderName().contains("社保")))
                        .map(OrganizationDetailsHolder::getHolderName).collect(Collectors.toList());
                if (collect.size() >0){
                    holder.setHolderName(collect.toString());
                }
            }

        }

        private void countAndDays() {
            int count = 0;
            for (int i = 0; i < holderNums.size() - 1; i++) {
                if (holderNums.get(i).getHolderNum() < holderNums.get(i+1).getHolderNum()){
                    count ++ ;
                }else {
                    break;
                }
            }

            holder.setCount(count);
            if (count>0){
               holder.setDays(DateUtils.differentDays(holderNums.get(0).getReportDate(),holderNums.get(count).getReportDate()));
            }
        }

        private void calculatePrice() {
            Date when = holderNums.get(0).getHoldNoticeDate();
            holder.setPrice(transactions.get(0).getTclose());

            for (Transaction transaction : transactions) {
                if (transaction.getReportDate().before(when)) {
                    holder.setNoticePrice(transaction.getTclose());
                    break;
                }
            }

            if (holder.getPrice() != null && holder.getMakersCost() != null){
                holder.setPriceRatio(MathUtils.doubleRetain2Bit(100*(holder.getPrice()-holder.getMakersCost())/holder.getMakersCost()));
            }

        }


        private void calculateMakersCost() {
            HolderNum moreHolderNum = holderNums.get(0);
            int moreLocation = 0;

            for (int i = 0; i < holderNums.size() && i < 6; i++) {
                if (holderNums.get(i).getHolderNum() > moreHolderNum.getHolderNum()){
                    moreHolderNum = holderNums.get(i);
                    moreLocation = i;
                }
            }

            HashMap<Double, Long> map = new HashMap<>();
            long divSumHolderNum = 0;
            for (int j = 0; j < moreLocation; j++) {
                Date startDate = holderNums.get(j+1).getReportDate();
                Date endDate = holderNums.get(j).getReportDate();
                double vagPrice = getVagPrice(startDate,endDate);
                long divHolderNum = Math.abs(holderNums.get(j).getHolderNum() - holderNums.get(j+1).getHolderNum());
                divSumHolderNum = divSumHolderNum + divHolderNum;
                Long value = map.get(vagPrice);
                if (value != null){
                    divHolderNum = divHolderNum + value;
                }
                map.put(vagPrice,divHolderNum);
            }

            if (divSumHolderNum > 0){
                AtomicDouble cost = new  AtomicDouble(0.0);
                long finalDivSumHolderNum = divSumHolderNum;
                map.forEach(new BiConsumer<Double, Long>() {
                    @Override
                    public void accept(Double price, Long sumHolderNum) {
                        cost.addAndGet(price*sumHolderNum.doubleValue()/finalDivSumHolderNum);
                    }
                });
                holder.setMakersCost(cost.get());
            }

        }

        private double getVagPrice(Date startDate, Date endDate) {
            HashMap<Double, Double> map = new HashMap<>();
            double sumVoTurnOver = 0.0;
            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                if (transaction.getReportDate().getTime() > startDate.getTime()
                        && transaction.getReportDate().getTime() < endDate.getTime()){
                    Double tClose = transaction.getTclose();
                    Double voTurnOver = transaction.getVoturnover();
                    sumVoTurnOver = sumVoTurnOver + voTurnOver;
                    Double value = map.get(tClose);
                    if (value != null){
                        voTurnOver = voTurnOver + value;
                    }
                    map.put(tClose,voTurnOver);
                }
            }

            AtomicDouble price = new AtomicDouble(0.0);

            double finalSumVoTurnOver = sumVoTurnOver;
            map.forEach(new BiConsumer<Double, Double>() {
                @Override
                public void accept(Double tClose, Double voTurnOver) {
                    price.getAndAdd(tClose * (voTurnOver / finalSumVoTurnOver));
                }
            });
            return price.get();
        }

        /**
         *更新人数排名 ,人数最新排名变动比
         * @param
         * @return
         */
        private void calculateRate() {
            /**
             * 剔除连续更新股东人数的时间，股东人数去最新的和之前每一个季度末的数据。需要剔除非最新并且不是季度末的数据
             */
            HashMap<Date, HolderNum> holderNumHashMap = new HashMap<>();
            holderNums = holderNums.stream()
                    .map(holderNum ->{
                        holderNumHashMap.put(holderNum.getReportDate(),holderNum);
                        return holderNum;
                    })
                    .sorted(Comparator.comparing(HolderNum::getReportDate).reversed()).collect(Collectors.toList());

            List<Date> reportDates = ReportDateUtils.getNewestReportDates(6*4);

            reportDates.sort(Comparator.comparing(Date::getTime).reversed());
            if(reportDates.get(0).getTime() != this.holderNums.get(0).getReportDate().getTime()){
                reportDates.add(this.holderNums.get(0).getReportDate());
            }

            ArrayList<HolderNum> newHolderNums = new ArrayList<>();

            reportDates.forEach(date->{
                HolderNum holderNum = holderNumHashMap.get(date);
                if (holderNum != null){
                    newHolderNums.add(holderNum);
                }
            });


            this.holderNums = newHolderNums;
            holderNums.sort(Comparator.comparing(HolderNum::getReportDate).reversed());

            HolderNum newestHolderNum = this.holderNums.get(0);
            holder.setOderNum3(calculateOderNum(this.holderNums,newestHolderNum,3));
            holder.setOderNum5(calculateOderNum(this.holderNums,newestHolderNum,5));

            holder.setSort12(calculateSortRate(this.holderNums,1,2));
            holder.setSort13(calculateSortRate(this.holderNums,1,3));
            holder.setSort14(calculateSortRate(this.holderNums,1,4));
            holder.setSort15(calculateSortRate(this.holderNums,1,5));
            holder.setSort16(calculateSortRate(this.holderNums,1,6));
            holder.setSort17(calculateSortRate(this.holderNums,1,7));
            holder.setSort18(calculateSortRate(this.holderNums,1,8));
            holder.setSort19(calculateSortRate(this.holderNums,1,9));

            holder.setCode(securityCode.getCode());
            holder.setName(securityCode.getName());
            holder.setHolderNum(newestHolderNum.getHolderNum().intValue());
            holder.setReportDate(newestHolderNum.getReportDate());
            holder.setMarket(MathUtils.doubleRetain2Bit(transactions.get(0).getVaturnover()/transactions.get(0).getTurnover()/1000000));

        }

        private Integer calculateOderNum(List<HolderNum> holderNums, HolderNum newestHolderNum, int year) {
            int oderNum = 0;
            Long holderNum = newestHolderNum.getHolderNum();
            ArrayList<Long> num = new ArrayList<>();
            int min = Math.min(year * 4, holderNums.size());
            for (int i = 0; i <  min; i++) {
                num.add(holderNums.get(i).getHolderNum());
            }
            num.sort(Comparator.comparing(Long::longValue));

            if (num.get(0) >=  holderNum){
                oderNum = 1;
            }else {
                for (int i = 0; i < num.size() - 1; i++) {
                    if (num.get(i) <= holderNum && num.get(i + 1) > holderNum) {
                        oderNum = i + 1;
                    }
                }
            }
            if(oderNum ==  0 && num.get(num.size()-1) <= holderNum){
                oderNum = min;
            }
            return oderNum;
        }



        private Double calculateSortRate(List<HolderNum> holderNums, int sortNum, int sortNum1){
            if(holderNums.size() < sortNum || holderNums.size() < sortNum1 ){
                return 0.0;
            }
            BigDecimal decimal = new BigDecimal(holderNums.get(sortNum1 - 1).getHolderNum());
            BigDecimal divide = decimal.divide(new BigDecimal(holderNums.get(sortNum - 1).getHolderNum()),2, RoundingMode.HALF_UP);
            return divide.doubleValue();
        }



        private void calculateMakersRateWithSanHuRate(){
            double maxSortRate = getMaxValue(1.0,holder.getSort12(),holder.getSort13(),holder.getSort14()
                    ,holder.getSort15(),holder.getSort16());
            double makersRate = (100 - holder.getTenFlowHolderRatio()) * (1-1/maxSortRate);
            holder.setMakersRatio(MathUtils.doubleRetain2Bit(makersRate));
            holder.setSanHuRatio(MathUtils.doubleRetain2Bit(100 - holder.getTenFlowHolderRatio() - makersRate));
            holder.setMakerVsSanHuRatio(MathUtils.doubleRetain2Bit(100*holder.getMakersRatio()/holder.getSanHuRatio()));
        }

        private void setIndustryAndListingDate(){
            if (companyInfo == null) {
                return;
            }
            String industry = String.valueOf(companyInfo.getDongcaiIndustry());
            if (industry != null){
                holder.setDongCaiIndustry(industry);
                holder.setIndustryBig(industry.split("-")[1]);
                holder.setIndustryMiddle(industry.split("-")[2]);
            }
            holder.setListingDate(companyInfo.getListingDate());
        }

        private double getMaxValue(Double ... sort) {
            return Arrays.stream(sort).mapToDouble(Double::doubleValue).max().getAsDouble();
        }


        private void  calculateTenFlowHolderRatio(){
            if (this.tenFlowHolders.size() == 0){
                holder.setTenFlowHolderRatio(0.0);
                return;
            }

            this.tenFlowHolders.sort(Comparator.comparing(TenFlowHolder::getReportDate).reversed());
            TenFlowHolder newFlowHolders = this.tenFlowHolders.get(0);

            double sum = this.tenFlowHolders.stream()
                    .filter(tenFlowHolder -> tenFlowHolder.getReportDate().getTime() == newFlowHolders.getReportDate().getTime())
                    .map(tenFlowHolder -> tenFlowHolder.getFreeHoldnumRatio())
                    .mapToDouble(BigDecimal::doubleValue).sum();
            holder.setTenFlowHolderRatio(sum);
        }

        private void calculateFlowMarket(){
            holder.setFlowMarket(MathUtils.doubleRetain2Bit(holder.getMarket()*(100-holder.getTenFlowHolderRatio())/100));
            holder.setAvgAssets(MathUtils.doubleRetain2Bit(holder.getFlowMarket()*10000/holder.getHolderNum()));
        }
    }

}

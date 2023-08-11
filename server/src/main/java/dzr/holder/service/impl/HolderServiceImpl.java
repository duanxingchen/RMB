package dzr.holder.service.impl;

import com.google.common.util.concurrent.AtomicDouble;
import dzr.common.utils.DateUtils;
import dzr.common.utils.MathUtils;
import dzr.common.utils.ReportDateUtils;
import dzr.holder.entity.Holder;
import dzr.holder.entity.HolderNum;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.mapper.HolderNumMapper;
import dzr.holder.service.HolderService;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.OrganizationDetailRate;
import dzr.organization.entity.TenFlowHolder;
import dzr.organization.mapper.OrganizationDetailRateMapper;
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
    private final OrganizationDetailRateMapper organizationDetailRateMapper;

    public void calculate() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            CompanyInfo companyInfo = companyInfoMapper.selectOneByCode(securityCode.getCode());
            List<TenFlowHolder> tenFlowHolders = tenFlowHolderMapper.selectByCode(securityCode.getCode());
            List<HolderNum> holderNums =  holderNumMapper.selectByCode(securityCode.getCode());
            List<Transaction> transactions = transactionMapper.selectReinstatementByCode(securityCode.getCode());
            List<OrganizationDetailRate> organizationDetailRates = organizationDetailRateMapper.selectByCode(securityCode.getCode());

            HolderDto holderDto = new HolderDto(securityCode, new Holder(), holderNums, transactions, companyInfo, tenFlowHolders, organizationDetailRates);
            holderDto.doit();
        });
    }

    private class HolderDto{
        private Holder holder;
        private List<HolderNum> holderNums;
        private List<Transaction> transactions;
        private SecurityCode securityCode;
        private CompanyInfo companyInfo;
        private List<TenFlowHolder> tenFlowHolders;
        private List<OrganizationDetailRate> organizationDetailRates;

        private HolderDto(SecurityCode securityCode, Holder holder, List<HolderNum> holderNums, List<Transaction> transactions, CompanyInfo companyInfo, List<TenFlowHolder> tenFlowHolders, List<OrganizationDetailRate> organizationDetailRates) {
            this.holder = holder;
            this.holderNums = holderNums;
            this.transactions = transactions;
            this.securityCode = securityCode;
            this.companyInfo = companyInfo;
            this.tenFlowHolders = tenFlowHolders;
            this.organizationDetailRates = organizationDetailRates;
        }
        public void doit(){
            try {
                calculateRate();
                setIndustryAndListingDate();
                calculateTenFlowHolderRatio();
                calculateMakersRateWithSanHuRate();
                calculateFlowMarket();
                calculateMakersCost();
                calculateHolderName();
                calculatePrice();
            }catch (Exception e){
                e.printStackTrace();
            }
            holderMapper.delete(holder);
            holderMapper.insert(holder);
        }


        private void calculateHolderName() {
            List<OrganizationDetailRate> holderName = organizationDetailRates.stream()
                    .filter(organizationDetailRate -> organizationDetailRate.getEndDate().getTime() == DateUtils.strToDate("2022-09-30 00:00:00").getTime())
                    .collect(Collectors.toList());
            if (holderName.size() >0 ){
                holder.setHolderName(holderName.get(0).getHolderName());
            }
        }

        private void calculatePrice() {
            Date when = holderNums.get(0).getHoldNoticeDate();
            List<Transaction> collect = transactions.stream().filter(transaction -> transaction.getReportDate().before(when))
                    .collect(Collectors.toList());
            collect.sort(Comparator.comparing(Transaction::getReportDate).reversed());
            holder.setPrice(transactions.get(0).getTclose());

            holder.setNoticePrice(collect.get(0).getTclose());
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
        }
    }

}

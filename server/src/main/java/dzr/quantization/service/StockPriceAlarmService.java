package dzr.quantization.service;

import dzr.holder.entity.HolderOrg;
import dzr.holder.mapper.HolderOrgMapper;
import dzr.info.entity.SecurityCode;
import dzr.quantization.init.RealtimeAcquisitionStockPrices;
import dzr.quantization.mapper.StockPoolMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *股票价格实时告警
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StockPriceAlarmService {
    private final StockPoolMapper stockPoolMapper;
    private final TransactionMapper transactionMapper;
    private final HolderOrgMapper holderOrgMapper;

    /**
     *股价跌幅
     */
    private final static double PRICE_DOWN_RATE = 0.1;

    public SecurityCode filter(SecurityCode securityCode){
        List<Transaction> transactions = transactionMapper.selectReinstatementByCode(securityCode.getCode());
        transactions = transactions.stream().sorted(Comparator.comparing(Transaction::getReportDate).reversed()).collect(Collectors.toList());


        /**
         * 最近10天的最大值
         */
        double tenDayMaxPrice = transactions.stream().limit(10).mapToDouble(Transaction::getTclose).max().getAsDouble();

        List<HolderOrg> holderOrgs = holderOrgMapper.selectByCode(securityCode.getCode());
        /**
         * 计算从股东人数减少开始起，平均跌幅（回撤）
         * 跌幅超过阈值（计算的平均跌幅和自定义的最小跌幅），发通知
         */

        if ((tenDayMaxPrice - transactions.get(0).getTclose())/transactions.get(0).getTclose() > 1 + PRICE_DOWN_RATE){
            return securityCode;
        }
        return null;
    }
}

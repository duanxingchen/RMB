package dzr.integration.service.impl;

import dzr.info.entity.SecurityCode;
import dzr.integration.service.AiDataService;
import dzr.integration.service.dto.AiLSTM;
import dzr.transaction.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2023/4/12 11:04
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
public class AiDataServiceImpl implements AiDataService {

    /**
     * 去掉上市后的前N天
     */
    private static int LISTING_AFTER_DAYS = 5*20;


    @Override
    public void transform(SecurityCode securityCode, List<Transaction> transactions) {
        String code = securityCode.getCode();
        transactions.sort(Comparator.comparing(Transaction::getReportDate).reversed());
        ArrayList<AiLSTM> aiLSTMs = new ArrayList<>();

        for (int i = 0; i < transactions.size() - LISTING_AFTER_DAYS; i++) {
            Transaction transaction = transactions.get(i);
            Double tClose = transaction.getTclose();
            if (tClose < 0.5){
                break;
            }
            if (transaction.getTurnover() < 0.01){
                continue;
            }
            AiLSTM aiLSTM = new AiLSTM();
            aiLSTM.setCode(code);
            aiLSTM.setName(securityCode.getName());
            aiLSTM.setPrice(tClose);
            aiLSTM.setReportDate(transaction.getReportDate());
            aiLSTM.setTornOver(transaction.getTurnover());
            aiLSTMs.add(aiLSTM);
        }

        aiLSTMs.sort(Comparator.comparing(AiLSTM::getReportDate));

        List<Double> priceNormal = aiLSTMs.stream().map(AiLSTM::getPrice).collect(Collectors.toList());
        List<Double> turnOverNormal = aiLSTMs.stream().map(AiLSTM::getTornOver).collect(Collectors.toList());


        /*redisTemplate.delete(AiDefineArg.PRICE_REDIS_PREFIX + code);
        redisTemplate.delete(AiDefineArg.TURN_OVER_REDIS_PREFIX + code);
        redisTemplate.opsForList().rightPushAll(AiDefineArg.PRICE_REDIS_PREFIX + code,priceNormal);
        redisTemplate.opsForList().rightPushAll(AiDefineArg.TURN_OVER_REDIS_PREFIX + code,turnOverNormal);*/

    }

    @Override
    public void loadSecurityCodeToRedis(List<SecurityCode> securityCodes) {
        List<String> codes = securityCodes.stream().map(SecurityCode::getCode).collect(Collectors.toList());
        /*redisTemplate.delete(AiDefineArg.ALL_CODE_REDIS_KEY);
        redisTemplate.opsForList().rightPushAll(AiDefineArg.ALL_CODE_REDIS_KEY,codes);*/

    }
}

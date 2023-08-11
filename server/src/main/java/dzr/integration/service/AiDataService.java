package dzr.integration.service;


import dzr.info.entity.SecurityCode;
import dzr.transaction.entity.Transaction;

import java.util.List;

/**
 * @Author dzr
 * @Date 2023/4/12 11:03
 * @Version 1.0
 * @Description:
 */
public interface AiDataService {
    void transform(SecurityCode securityCode, List<Transaction> transactions);

    void loadSecurityCodeToRedis(List<SecurityCode> securityCodes);
}

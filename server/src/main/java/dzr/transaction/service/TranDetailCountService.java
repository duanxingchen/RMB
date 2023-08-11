package dzr.transaction.service;


import dzr.holder.entity.Holder;
import dzr.info.entity.SecurityCode;
import dzr.transaction.entity.TranDetail;
import dzr.transaction.entity.TranDetailCount;
import dzr.transaction.entity.Transaction;

import java.util.List;

/**
 * @Author dzr
 * @Date 2023/3/20 22:04
 * @Version 1.0
 * @Description:
 */
public interface TranDetailCountService {
    List<TranDetailCount> calculate(SecurityCode securityCode, List<TranDetail> tranDetails, List<Transaction> transactions, Holder holder);
}

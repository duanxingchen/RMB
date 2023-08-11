package dzr.transaction.service;


import dzr.info.entity.SecurityCode;
import dzr.plate.entity.PlateStock;
import dzr.transaction.entity.Slide;
import dzr.transaction.entity.Transaction;

import java.util.List;

/**
 * @Author dzr
 * @Date 2022/10/26 14:31
 * @Version 1.0
 * @Description:
 */
public interface EnergyPlateCountService{
    List<Transaction> calculate(SecurityCode securityCode, List<Transaction> transactions, List<Slide> slides, List<PlateStock> plates);
}

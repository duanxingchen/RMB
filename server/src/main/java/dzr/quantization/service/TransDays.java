package dzr.quantization.service;

import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransDays {
    private final TransactionMapper transactionMapper;

    public Date getBeforeDate(int num){
        List<Transaction> transactions = transactionMapper.selectReinstatementByCode("000001");
        transactions = transactions.stream().sorted(Comparator.comparing(Transaction::getReportDate).reversed()).collect(Collectors.toList());
        return transactions.get(num).getReportDate();
    }
}

package dzr.transaction.mapper;

import dzr.holder.mapper.BaseMapper;
import dzr.transaction.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionMapper extends BaseMapper<Transaction> {
    List<Transaction> selectReinstatementByCode(String code);

    List<Transaction> selectBeforeAllByReportDate(Date beforeDate);

    List<Transaction> selectNotReinstatementByCode(String code);

    List<Transaction> selectReinstatementDataTypeByReportDates(Date startDate, Date endDate);

    List<Transaction> selectReinstatementByCodeAndReportDates(String code, Date startDate, Date endDate);

    int countByCodeAndReportDate(String code, Date reportDate);

    Transaction selectNewestByCode(String code);
}


package dzr.transaction.mapper;


import dzr.holder.mapper.BaseMapper;
import dzr.transaction.entity.TranDetail;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TranDetailMapper extends BaseMapper<TranDetail> {
    int countByCodeAndReportDate(String code, Date reportDate);

    List<Date> groupByCodeAndReportDate(String code);

    List<TranDetail> selectByCodeAndReportDate(String code, Date beforeDate);
}


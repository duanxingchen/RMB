package dzr.plate.mapper;

import dzr.holder.mapper.BaseMapper;
import dzr.plate.entity.PlateStock;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlateStockMapper extends BaseMapper<PlateStock> {
    List<PlateStock> selectBeforeAllByReportDate(Date beforeDate);

    List<PlateStock> selectAllByReportDates(Date startDate, Date endDate);

    PlateStock selectNewestByCode(String code);
}

package dzr.plate.mapper;

import dzr.holder.mapper.BaseMapper;
import dzr.plate.entity.PlateBlock;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlateBlockMapper extends BaseMapper<PlateBlock> {
    List<PlateBlock> selectBeforeAllByReportDate(Date beforeDate);
}

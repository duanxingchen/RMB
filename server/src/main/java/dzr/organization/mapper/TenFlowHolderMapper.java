package dzr.organization.mapper;

import dzr.holder.mapper.BaseMapper;
import dzr.organization.entity.TenFlowHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TenFlowHolderMapper extends BaseMapper<TenFlowHolder> {
    List<Date> groupByCodeAndReportDate(String code);
}

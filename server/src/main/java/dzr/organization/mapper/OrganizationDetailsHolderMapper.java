package dzr.organization.mapper;

import dzr.holder.mapper.BaseMapper;
import dzr.organization.entity.OrganizationDetailsHolder;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrganizationDetailsHolderMapper extends BaseMapper<OrganizationDetailsHolder> {
    List<OrganizationDetailsHolder> selectByObject(OrganizationDetailsHolder detailsHolder);
}

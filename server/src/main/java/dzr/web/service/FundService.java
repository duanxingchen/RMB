package dzr.web.service;


import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.web.service.todo.OrganizationCoordinate;

import java.util.List;

/**
 * @Author dzr
 * @Date 2022/12/23 17:05
 * @Version 1.0
 * @Description:
 */
public interface FundService {

    List<OrganizationCoordinate> coordinate(List<OrganizationDetailsHolder> organizationDetailsHolders);
}

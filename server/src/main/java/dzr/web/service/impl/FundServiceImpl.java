package dzr.web.service.impl;


import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.web.service.FundService;
import dzr.web.service.todo.OrganizationCoordinate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author dzr
 * @Date 2022/12/27 11:45
 * @Version 1.0
 * @Description:
 */
@Service
@Primary
public class FundServiceImpl implements FundService {


    @Override
    public List<OrganizationCoordinate> coordinate(List<OrganizationDetailsHolder> organizationDetailsHolders) {
        ArrayList<OrganizationCoordinate> coordinates = new ArrayList<>();
        Map<String, List<OrganizationDetailsHolder>> map = organizationDetailsHolders.stream().collect(Collectors.groupingBy(OrganizationDetailsHolder::getHolderName));
        map.forEach((holderName, organizationDetailsHolders1) -> {
            OrganizationCoordinate organizationCoordinate = new OrganizationCoordinate();
            organizationCoordinate.setParentOrgName(organizationDetailsHolders1.get(0).getParentOrgName());
            organizationCoordinate.setHolderName(holderName);
            organizationCoordinate.setCoordinateCount(organizationDetailsHolders1.size());
            coordinates.add(organizationCoordinate);
        });
        return coordinates;
    }
}

package dzr.web.controller;



import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.web.service.FundService;
import dzr.web.service.todo.OrganizationCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author dzr
 * @Date 2022/10/9 17:31
 * @Version 1.0
 * @Description:
 */
@RestController
@RequestMapping("/fund")
public class fundController {
    @Autowired
    OrganizationDetailsHolderMapper organizationDetailsHolderMapper;

    @Autowired
    FundService fundService;

    @RequestMapping(method = RequestMethod.GET,value = "/fundDetail/{holderName}")
    public List<OrganizationDetailsHolder> selectOrganizationDetailsHolder(@PathVariable("holderName") String holderName) {
        OrganizationDetailsHolder detailsHolder = new OrganizationDetailsHolder();
        detailsHolder.setHolderName(holderName);
        return organizationDetailsHolderMapper.selectByObject(detailsHolder);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/coordinate/{holderName}")
    public List<OrganizationCoordinate> selectCoordinate(@PathVariable("holderName") String holderName) {
        List<OrganizationDetailsHolder> hs = new ArrayList<>();
        OrganizationDetailsHolder detailsHolder = new OrganizationDetailsHolder();
        detailsHolder.setHolderName(holderName);
        List<OrganizationDetailsHolder> organizationDetailsHolders = organizationDetailsHolderMapper.selectByObject(detailsHolder);
        for (int i = 0; i < organizationDetailsHolders.size(); i++) {
            OrganizationDetailsHolder organizationDetailsHolder = new OrganizationDetailsHolder();
            organizationDetailsHolder.setCode(organizationDetailsHolders.get(i).getCode());
            organizationDetailsHolder.setReportDate(organizationDetailsHolders.get(i).getReportDate());
            hs.addAll(organizationDetailsHolderMapper.selectByObject(organizationDetailsHolder));
        }

        return fundService.coordinate(hs);
    }

}

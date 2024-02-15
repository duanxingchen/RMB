package dzr.holder.service.comprehensive;


import dzr.info.entity.SecurityCode;
import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 私募参与（最近一次机构持股中筛选）
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEquity implements Filter{
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;

    @Override
    public List<SecurityCode> filter(List<SecurityCode> securityCodeList) {
        ArrayList<SecurityCode> includeSecurityCodes = new ArrayList<>();
        securityCodeList.stream().forEach(securityCode -> {
            log.info("PrivateEquity : {}",securityCode);
            List<OrganizationDetailsHolder> organizationDetailsHolders = organizationDetailsHolderMapper.selectByCode(securityCode.getCode());
            organizationDetailsHolders = organizationDetailsHolders.stream().sorted(Comparator.comparing(OrganizationDetailsHolder::getReportDate).reversed()).collect(Collectors.toList());

            if (organizationDetailsHolders.size() > 0){
                Date newestReportDate = organizationDetailsHolders.get(0).getReportDate();
                List<String> collect = organizationDetailsHolders.stream().filter(organizationDetailsHolder ->
                        organizationDetailsHolder.getReportDate().getTime() == newestReportDate.getTime() &&
                                (organizationDetailsHolder.getHolderName().contains("私募") || organizationDetailsHolder.getHolderName().contains("社保")))
                        .map(OrganizationDetailsHolder::getHolderName).collect(Collectors.toList());
                if (collect.size() > 0){
                    AtomicReference<String> privateEquityNames = new AtomicReference<>("");
                    collect.forEach(name-> privateEquityNames.set(privateEquityNames + "  " + name));
                    securityCode.setName(securityCode.getName() + "   私募名称:" + privateEquityNames);
                    includeSecurityCodes.add(securityCode);
                }
            }
        });
        return includeSecurityCodes;
    }
}

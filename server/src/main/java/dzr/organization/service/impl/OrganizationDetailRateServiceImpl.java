package dzr.organization.service.impl;


import dzr.common.utils.DateUtils;
import dzr.common.utils.ReportDateUtils;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.OrganizationDetailRate;
import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.mapper.OrganizationDetailRateMapper;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.organization.service.OrganizationDetailRateService;
import dzr.transaction.entity.Slide;
import dzr.transaction.mapper.SlideMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationDetailRateServiceImpl implements OrganizationDetailRateService {
    private final SecurityCodeMapper securityCodeMapper;
    private final OrganizationDetailRateMapper organizationDetailRateMapper;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;
    private final SlideMapper slideMapper;

    @Override
    public void calculate() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            calculateRelationship(securityCode);
        });

    }


    private void calculateRelationship(SecurityCode securityCode) {
        Map<String, List<OrganizationDetailsHolder>> map = organizationDetailsHolderMapper.selectByCode(securityCode.getCode()).stream()
                .filter(organizationDetailsHolder -> organizationDetailsHolder.getHolderName().contains("私募"))
                .collect(Collectors.groupingBy(OrganizationDetailsHolder::getHolderCode));

        map.forEach((holderCode, organizationDetailsHolders) -> {
            OrganizationDetailRate organizationDetailRate = new OrganizationDetailRate();

            organizationDetailRate.setHolderCode(holderCode);
            OrganizationDetailsHolder organizationDetailsHolder = organizationDetailsHolders.get(0);
            String holderName = organizationDetailsHolder.getHolderName();
            organizationDetailRate.setHolderName(holderName);
            organizationDetailRate.setParentHolderName(holderName.split("-")[0]);
            organizationDetailRate.setCode(securityCode.getCode());
            organizationDetailRate.setName(securityCode.getName());
            organizationDetailRate.setHolderCap(organizationDetailsHolder.getHoldMarketCap()/10000);

            organizationDetailsHolders.sort((o1, o2) -> o1.getReportDate().before(o2.getReportDate())?-1:1);
            int size = organizationDetailsHolders.size();
            int startPointer = 0;
            int endPointer = 0;
            while (true) {
                for (int i = startPointer; i < size; i++) {
                    Date first = organizationDetailsHolders.get(i).getReportDate();
                    Date second = first;
                    if (i + 1 < size) {
                        second = organizationDetailsHolders.get(i + 1).getReportDate();
                    }
                    String nextDate = ReportDateUtils.getAfterStringReportDate(DateUtils.dateToStr(first));
                    if (!nextDate.equals(DateUtils.dateToStr(second)) || first.getTime() == second.getTime()) {
                        endPointer = i;
                        Date startDate = organizationDetailsHolders.get(startPointer).getReportDate();
                        Date endDate = organizationDetailsHolders.get(endPointer).getReportDate();
                        calculateIncreaseRate(securityCode,organizationDetailRate,startDate, endDate);
                        organizationDetailRate.setStartDate(startDate);
                        organizationDetailRate.setEndDate(endDate);
                        startPointer = i + 1;
                        break;
                    }
                }

                if (startPointer >= size) {
                    break;
                }
            }
            organizationDetailRateMapper.insert(organizationDetailRate);
        });
    }

    private void calculateIncreaseRate(SecurityCode securityCode, OrganizationDetailRate organizationDetailRate, Date startDate, Date endDate) {
        List<Slide> slides = slideMapper.selectByCode(securityCode.getCode());
        for (int i = 0; i < slides.size() ; i++) {
            Slide slide = slides.get(i);
            long maxStartDate = Math.max(slide.getStartDate().getTime(), startDate.getTime());
            long minEndDate = Math.min(slide.getEndDate().getTime(), endDate.getTime());
            long did = minEndDate - maxStartDate;
            if (did >= 0){
                long slideDate = slide.getEndDate().getTime() - slide.getStartDate().getTime();
                long orgDate = endDate.getTime() - startDate.getTime();
                long minDate = Math.min(slideDate, orgDate);
                if (did >= minDate/2){
                    int type = slide.getType().equals("上涨") ? 1 : -1;
                    organizationDetailRate.setHolderProfitRate(type*slide.getRange());
                }

            }
        }

    }

}

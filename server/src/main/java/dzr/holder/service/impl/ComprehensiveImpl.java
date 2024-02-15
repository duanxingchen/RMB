package dzr.holder.service.impl;

import dzr.holder.service.Comprehensive;
import dzr.holder.service.comprehensive.PrivateEquity;
import dzr.holder.service.comprehensive.RealFlowMarket;
import dzr.holder.service.comprehensive.Shareholders;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ComprehensiveImpl implements Comprehensive
{
    private final Shareholders shareholders;
    private final RealFlowMarket realFlowMarket;
    private final SecurityCodeMapper securityCodeMapper;
    private final PrivateEquity privateEquity;

    @Override
    public void analyse() {
        /**
         * 排除ST和上市时间小于2年的
         */
        List<SecurityCode> securityCodes = securityCodeMapper.selectAll().stream().filter(
                securityCode -> !securityCode.getName().contains("st") &&
                        ((System.currentTimeMillis() - securityCode.getListingDate().getTime())/365/24/3600/1000 > 2))
                .collect(Collectors.toList());
        securityCodes = shareholders.filter(securityCodes);
        securityCodes = realFlowMarket.filter(securityCodes);
        securityCodes = privateEquity.filter(securityCodes);

        securityCodes.forEach(securityCode -> {
            log.info("ComprehensiveImpl : {}",securityCode);});
    }

}

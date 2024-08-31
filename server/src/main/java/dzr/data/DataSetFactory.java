package dzr.data;

import dzr.holder.mapper.HolderMapper;
import dzr.holder.mapper.HolderNumMapper;
import dzr.holder.mapper.HolderOrgMapper;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.organization.mapper.TenFlowHolderMapper;
import dzr.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSetFactory {
    private final SecurityCodeMapper securityCodeMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final TenFlowHolderMapper tenFlowHolderMapper;
    private final HolderNumMapper holderNumMapper;
    private final TransactionMapper transactionMapper;
    private final HolderMapper holderMapper;
    private final HolderOrgMapper holderOrgMapper;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;


    public  DataSet getInstance(){
        return new DataSet(securityCodeMapper
                ,companyInfoMapper
                ,tenFlowHolderMapper
                ,holderNumMapper
                ,transactionMapper
                ,holderMapper
                ,holderOrgMapper
                ,organizationDetailsHolderMapper);
    }
}

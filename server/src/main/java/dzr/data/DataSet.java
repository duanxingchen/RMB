package dzr.data;

import dzr.exception.DataNullException;
import dzr.exception.NoCodeException;
import dzr.holder.entity.HolderNum;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.mapper.HolderNumMapper;
import dzr.holder.mapper.HolderOrgMapper;
import dzr.info.entity.CompanyInfo;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.entity.TenFlowHolder;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import dzr.organization.mapper.TenFlowHolderMapper;
import dzr.transaction.entity.Transaction;
import dzr.transaction.mapper.TransactionMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DataSet {

    private final SecurityCodeMapper securityCodeMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final TenFlowHolderMapper tenFlowHolderMapper;
    private final HolderNumMapper holderNumMapper;
    private final TransactionMapper transactionMapper;
    private final HolderMapper holderMapper;
    private final HolderOrgMapper holderOrgMapper;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;

    private List<SecurityCode> securityCodes;
    private CompanyInfo companyInfo;
    private List<CompanyInfo> companyInfos;
    private List<TenFlowHolder> tenFlowHolders;
    private List<HolderNum> holderNums;
    private List<Transaction> transactions;
    private List<OrganizationDetailsHolder> organizationDetailsHolders;
    private String code;

    private boolean isLoadedSecurityCodes = false;
    private boolean isLoadedCompanyInfos = false;
    private boolean isLoadedTenFlowHolders = false;
    private boolean isLoadedHolderNums = false;
    private boolean isLoadedTransactions = false;
    private boolean isLoadedOrganizationDetailsHolders = false;

    public DataSet(SecurityCodeMapper securityCodeMapper
            , CompanyInfoMapper companyInfoMapper
            , TenFlowHolderMapper tenFlowHolderMapper
            , HolderNumMapper holderNumMapper
            , TransactionMapper transactionMapper
            , HolderMapper holderMapper
            , HolderOrgMapper holderOrgMapper
            , OrganizationDetailsHolderMapper organizationDetailsHolderMapper) {
        this.securityCodeMapper = securityCodeMapper;
        this.companyInfoMapper = companyInfoMapper;
        this.tenFlowHolderMapper = tenFlowHolderMapper;
        this.holderNumMapper = holderNumMapper;
        this.transactionMapper = transactionMapper;
        this.holderMapper = holderMapper;
        this.holderOrgMapper = holderOrgMapper;
        this.organizationDetailsHolderMapper = organizationDetailsHolderMapper;
    }



    public DataSet setCode(String code){
        this.code = code;
        return this;
    }

    public DataSet isLoadSecurityCodes(){
        securityCodes = securityCodeMapper.selectAll();
        isLoadedSecurityCodes = true;
        return this;
    }


    public DataSet isLoadByCodeCompanyInfo(){
        checkCode();
        companyInfo = companyInfoMapper.selectOneByCode(code);
        isLoadedCompanyInfos = true;
        return this;
    }


    public DataSet isLoadAllCompanyInfo(){
        companyInfos = companyInfoMapper.selectAll();
        isLoadedCompanyInfos = true;
        return this;
    }


    public DataSet isLoadByCodeTenFlowHolder(){
        checkCode();
        tenFlowHolders = tenFlowHolderMapper.selectByCode(code);
        isLoadedTenFlowHolders = true;
        return this;
    }

    public DataSet isLoadAllTenFlowHolder(){
        tenFlowHolders = tenFlowHolderMapper.selectAll();
        isLoadedTenFlowHolders = true;
        return this;
    }

    public DataSet isLoadByCodeHolderNum(){
        checkCode();
        holderNums = holderNumMapper.selectByCode(code);
        isLoadedHolderNums = true;
        return this;
    }

    public DataSet isLoadAllHolderNum(){
        holderNums = holderNumMapper.selectAll();
        isLoadedHolderNums = true;
        return this;
    }

    public DataSet isLoadByCodeTransaction(){
        checkCode();
        transactions = transactionMapper.selectReinstatementByCode(code);
        isLoadedTransactions = true;
        return this;
    }

    public DataSet isLoadAllTransaction(){
        transactions = transactionMapper.selectAll();
        isLoadedTransactions = true;
        return this;
    }

    public DataSet isLoadByCodeOrganizationDetailsHolder(){
        checkCode();
        organizationDetailsHolders = organizationDetailsHolderMapper.selectByCode(code);
        isLoadedOrganizationDetailsHolders = true;
        return this;
    }

    public DataSet isLoadAllOrganizationDetailsHolder(){
        organizationDetailsHolders = organizationDetailsHolderMapper.selectAll();
        isLoadedOrganizationDetailsHolders = true;
        return this;
    }

    private void checkCode(){
        if (code == null){
            throw new NoCodeException("Code is null, Code must be set");
        }
    }

    public void checkDataNotNull(){
        if (isLoadedSecurityCodes && securityCodes.isEmpty()){
            throw new DataNullException("securityCodes is null");
        }
        if (isLoadedCompanyInfos && companyInfos.isEmpty() && companyInfo == null){
            throw new DataNullException("companyInfos is null");
        }
        if (isLoadedTenFlowHolders && tenFlowHolders.isEmpty()){
            throw new DataNullException("tenFlowHolders is null");
        }
        if (isLoadedHolderNums && holderNums.isEmpty()){
            throw new DataNullException("holderNums is null");
        }
        if (isLoadedTransactions && transactions.isEmpty()){
            throw new DataNullException("transactions is null");
        }
        if (isLoadedOrganizationDetailsHolders && organizationDetailsHolders.isEmpty()){
            throw new DataNullException("organizationDetailsHolders is null");
        }
    }



}

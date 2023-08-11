package dzr.info.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.info.entity.CompanyInfo;
import dzr.info.mapper.CompanyInfoMapper;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyInfoInitData {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final CompanyInfoMapper companyInfoMapper;

    private final static String url = "https://emweb.securities.eastmoney.com/PC_HSF10/CompanySurvey/PageAjax?code=$code";

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            log.info(securityCode.toString());
            String urlPre = url.replace("$code", securityCode.getCodeWithExchange());
            String ret = httpClientService.doGet(urlPre).toLowerCase();
            JSONObject fxxg = JSON.parseObject(ret).getJSONArray("fxxg").getJSONObject(0);
            JSONObject jbzl = JSON.parseObject(ret).getJSONArray("jbzl").getJSONObject(0);
            CompanyInfo companyInfo = new CompanyInfo();
            companyInfo.setCode(securityCode.getCode());
            companyInfo.setName(securityCode.getName());
            companyInfo.setArea(jbzl.getString("province"));
            companyInfo.setChairman(jbzl.getString("chairman"));
            companyInfo.setDongcaiIndustry(jbzl.getString("em2016"));
            companyInfo.setSecuritiesIndustry(jbzl.getString("industrycsrc1"));
            companyInfo.setEmployees(jbzl.getInteger("emp_num"));
            companyInfo.setRegisteredCapital(jbzl.getString("reg_capital"));
            companyInfo.setLegalRepresentative(jbzl.getString("legal_person"));
            companyInfo.setEstablishmentDate(fxxg.getDate("found_date"));
            companyInfo.setListingDate(fxxg.getDate("listing_date"));
            companyInfo.setIssuance(fxxg.getString("total_issue_num"));
            companyInfo.setIssuePrice(fxxg.getDouble("issue_price"));
            companyInfoMapper.insert(companyInfo);
        });
    }

}

package dzr.organization.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.common.utils.ReportDateUtils;
import dzr.info.entity.SecurityCode;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.OrganizationDetailsHolder;
import dzr.organization.mapper.OrganizationDetailsHolderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dzr
 * @Date 2022/10/31 18:08
 * @Version 1.0
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationDetailsHolderInitData {
    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final OrganizationDetailsHolderMapper organizationDetailsHolderMapper;
    private final static String url = "https://data.eastmoney.com/dataapi/zlsj/detail?SHType=&SHCode=&SCode=$code&Pages=$pages&ReportDate=$date&sortField=HOLDER_CODE&sortDirec=1";



    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            ArrayList<OrganizationDetailsHolder> organizationDetailsHolders = new ArrayList<>();

            List<String> reportDates =  ReportDateUtils.getReportDateWithString(securityCode.getListingDate());

            for (int j = 0; j < reportDates.size(); j++) {
                int pages = getDataFromWebReturnPages(securityCode, url, organizationDetailsHolders,reportDates.get(j),1);
                for (int i = 1; i < pages; i++) {
                    getDataFromWebReturnPages(securityCode, url, organizationDetailsHolders,reportDates.get(j),i);
                }
            }
        });

    }

    private int getDataFromWebReturnPages(SecurityCode securityCode, String url, ArrayList<OrganizationDetailsHolder> organizationDetailsHolders, String reportDate, int pages) {
        String urlPre = url.replace("$code", securityCode.getCode()).replace("$date", reportDate).replace("$pages", String.valueOf(pages));
        String result = httpClientService.doGet(urlPre).toLowerCase();

        if(result.contains("data")){
            JSONObject parseObject = JSON.parseObject(result);
            JSONArray array = parseObject.getJSONArray("data");
            int page = parseObject.getInteger("pages");
            for (int i = 0; i < array.size(); i++) {
                JSONObject one = array.getJSONObject(i);
                OrganizationDetailsHolder detailsHolder = new OrganizationDetailsHolder();
                detailsHolder.setCode(one.getString("security_code"));
                detailsHolder.setName(one.getString("security_name_abbr"));
                detailsHolder.setSecurityInnerCode(one.getString("security_inner_code"));
                detailsHolder.setReportDate(DateUtils.strToDate(one.getString("report_date")));
                detailsHolder.setHolderCode(one.getString("holder_code"));
                detailsHolder.setHolderName(one.getString("holder_name"));
                detailsHolder.setParentOrgCode(one.getString("parent_org_code"));
                detailsHolder.setParentOrgCodeOld(one.getString("parent_orgcode_old"));
                detailsHolder.setParentOrgName(one.getString("parent_org_name"));
                detailsHolder.setOrgTypeCode(one.getString("org_type_code"));
                detailsHolder.setOrgType(one.getString("org_type"));
                detailsHolder.setTotalShares(one.getLong("total_shares"));
                detailsHolder.setHoldMarketCap(one.getDouble("hold_market_cap"));
                detailsHolder.setTotalSharesRatio(one.getDouble("total_shares_ratio"));
                detailsHolder.setFreeSharesRatio(one.getDouble("free_shares_ratio"));
                detailsHolder.setNetAssetRatio(one.getDouble("netasset_ratio"));
                detailsHolder.setOrgNameAbbr(one.getString("org_name_abbr"));
                organizationDetailsHolders.add(detailsHolder);
            }
            return page;
        }
        return 0;
    }

}

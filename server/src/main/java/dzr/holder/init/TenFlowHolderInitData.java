package dzr.holder.init;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.common.utils.ReportDateUtils;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.TenFlowHolder;
import dzr.organization.mapper.TenFlowHolderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenFlowHolderInitData {

    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final TenFlowHolderMapper tenFlowHolderMapper;
    private final static String url = "https://emweb.securities.eastmoney.com/PC_HSF10/ShareholderResearch/PageSDLTGD?code=$code&date=$date";

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            log.info(securityCode.toString());
            ArrayList<TenFlowHolder> tenFlowHolders = new ArrayList<>();
            ArrayList<String> reportDate = ReportDateUtils.getReportDateWithString(securityCode.getListingDate());
            for (int j = 0; j < reportDate.size(); j++) {
                String urlPre = url.replace("$code", securityCode.getCodeWithExchange()).replace("$date", reportDate.get(j));
                String ret = httpClientService.doGet(urlPre);
                JSONArray data = JSON.parseObject(ret).getJSONArray("sdltgd");
                for (int i = 0; i < data.size(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    TenFlowHolder tenFlowHolder = new TenFlowHolder();
                    tenFlowHolder.setCode(info.getString("SECURITY_CODE"));
                    tenFlowHolder.setReportDate(info.getDate("END_DATE"));
                    tenFlowHolder.setHolderRank(info.getInteger("HOLDER_RANK"));
                    tenFlowHolder.setHolderName(info.getString("HOLDER_NAME"));
                    tenFlowHolder.setHolderType(info.getString("HOLDER_TYPE"));
                    tenFlowHolder.setSharesType(info.getString("SHARES_TYPE"));
                    tenFlowHolder.setHoldNum(info.getLong("HOLD_NUM"));
                    tenFlowHolder.setFreeHoldnumRatio(info.getBigDecimal("FREE_HOLDNUM_RATIO"));
                    tenFlowHolder.setHoldNumChange(info.getString("HOLD_NUM_CHANGE"));
                    tenFlowHolder.setChangeRatio(info.getBigDecimal("CHANGE_RATIO"));
                    tenFlowHolders.add(tenFlowHolder);
                }
            }
            if (tenFlowHolders.size()>0){
                tenFlowHolderMapper.batchInsert(tenFlowHolders);
            }
        });
    }

}

package dzr.holder.init;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.DateUtils;
import dzr.common.utils.HttpClientService;
import dzr.common.utils.ReportDateUtils;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.entity.TenFlowHolder;
import dzr.organization.mapper.TenFlowHolderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            try{
                log.info(securityCode.toString());
                ArrayList<TenFlowHolder> tenFlowHolders = new ArrayList<>();
                Date limitDate = securityCode.getListingDate();
                if (securityCode.getListingDate().before(DateUtils.strToDate("2013-12-31", "yyyy-MM-dd"))) {
                    limitDate= DateUtils.strToDate("2013-12-31", "yyyy-MM-dd");
                }
                ArrayList<String> reportDate = ReportDateUtils.getReportDateWithString(limitDate);
                List<Date> includeReportDate= tenFlowHolderMapper.groupByCodeAndReportDate(securityCode.getCode());
                HashMap<Date, Date> dateDateHashMap = new HashMap<>();
                for (int j = 0; j < includeReportDate.size(); j++) {
                    dateDateHashMap.put(includeReportDate.get(j),includeReportDate.get(j));
                }

                List<String> lossReportDate = new ArrayList<>();
                for (int i = 0; i < reportDate.size(); i++) {
                    Date date = DateUtils.strToDate(reportDate.get(i), "yyyy-MM-dd");
                    if (dateDateHashMap.get(date) == null && date.getTime() < System.currentTimeMillis()){
                        lossReportDate.add(reportDate.get(i));
                    }
                }

                for (int j = 0; j < lossReportDate.size(); j++) {
                    String urlPre = url.replace("$code", securityCode.getCodeWithExchange()).replace("$date", lossReportDate.get(j));
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
            }catch (Exception exception){
                exception.printStackTrace();
            }
        });
    }

}

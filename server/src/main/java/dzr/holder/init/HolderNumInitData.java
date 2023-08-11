package dzr.holder.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.holder.entity.HolderNum;
import dzr.holder.mapper.HolderNumMapper;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@Slf4j
@RequiredArgsConstructor
public class HolderNumInitData  {

    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final HolderNumMapper holderNumMapper;
    private final static String url = "http://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns=END_DATE&sortTypes=-1&reportName=RPT_HOLDERNUM_DET&columns=SECURITY_CODE,SECURITY_NAME_ABBR,CHANGE_SHARES,CHANGE_REASON,END_DATE,TOTAL_MARKET_CAP,TOTAL_A_SHARES,HOLD_NOTICE_DATE,HOLDER_NUM,PRE_HOLDER_NUM,HOLDER_NUM_CHANGE,HOLDER_NUM_RATIO,PRE_END_DATE&filter=(SECURITY_CODE=$code)";

    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            log.info(securityCode.toString());
            String urlPre = url.replace("$code", securityCode.getCode());
            String ret = httpClientService.doGet(urlPre).toLowerCase();
            ArrayList<HolderNum> holderNums = new ArrayList<>();
            if (ret.contains("data")) {
                JSONArray data = JSON.parseObject(ret).getJSONObject("result").getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    HolderNum holderNum = new HolderNum();
                    holderNum.setCode(info.getString("security_code"));
                    holderNum.setName(info.getString("security_name_abbr"));
                    holderNum.setChangeShares(info.getLong("change_shares"));
                    holderNum.setChangeReason(info.getString("change_reason"));
                    holderNum.setReportDate(info.getDate("end_date"));
                    holderNum.setTotalMarketCap(info.getBigDecimal("total_market_cap"));
                    holderNum.setTotalAShares(info.getLong("total_a_shares"));
                    holderNum.setHoldNoticeDate(info.getDate("hold_notice_date"));
                    holderNum.setHolderNum(info.getLong("holder_num"));
                    holderNum.setPreHolderNum(info.getLong("pre_holder_num"));
                    holderNum.setHolderNumChange(info.getLong("holder_num_change"));
                    holderNum.setHolderNumRatio(info.getBigDecimal("holder_num_ratio"));
                    holderNum.setPreEndDate(info.getDate("pre_end_date"));
                    holderNums.add(holderNum);
                }
                holderNumMapper.batchInsert(holderNums);
            }
        });

    }
}

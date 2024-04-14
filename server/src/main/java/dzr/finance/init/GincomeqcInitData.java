package dzr.finance.init;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dzr.common.utils.HttpClientService;
import dzr.finance.entity.Gincomeqc;
import dzr.finance.entity.MainFinance;
import dzr.finance.mapper.GincomeqcMapper;
import dzr.info.mapper.SecurityCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * @Author dzr
 * @Date 2022/10/27 11:27
 * @Version 1.0  业务收入和利润
 * @Description:
 */
@Service
@Primary
@Log
@RequiredArgsConstructor
public class GincomeqcInitData {

    private final SecurityCodeMapper securityCodeMapper;
    private final HttpClientService httpClientService;
    private final GincomeqcMapper gincomeqcMapper;

    private final static String url = "https://datacenter.eastmoney.com/securities/api/data/get?type=RPT_F10_FINANCE_GINCOMEQC&sty=APP_F10_GINCOMEQC&filter=(SECUCODE%3D%22$code%22)&st=REPORT_DATE&source=HSF10&client=PC&v=01204936604528537";

    public  void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            log.info(securityCode.toString());
            String urlPre = url.replace("$code", securityCode.getCodeForFinance());
            String ret = httpClientService.doGet(urlPre).toLowerCase();
            ArrayList<Gincomeqc> gincomeqcs = new ArrayList<>();
            if (ret.contains("data")) {
                JSONArray data = JSON.parseObject(ret).getJSONObject("result").getJSONArray("data");
                for (int i = 0; i < data.size(); i++) {
                    JSONObject info = data.getJSONObject(i);
                    Gincomeqc gincomeqc = new Gincomeqc();
                    gincomeqc.setCode(info.getString("security_code"));
                    gincomeqc.setName(info.getString("security_name_abbr"));
                    gincomeqc.setReportDate(info.getDate("report_date"));
                    gincomeqc.setTotalOperateIncome(info.getDouble("total_operate_income"));
                    gincomeqc.setTotalOperateCost(info.getDouble("total_operate_cost"));
                    gincomeqc.setOperateCost(info.getDouble("operate_cost"));
                    gincomeqc.setResearchExpense(info.getDouble("research_expense"));
                    gincomeqc.setOperateTaxAdd(info.getDouble("operate_tax_add"));
                    gincomeqc.setSaleExpense(info.getDouble("sale_expense"));
                    gincomeqc.setManageExpense(info.getDouble("manage_expense"));
                    gincomeqc.setFinanceExpense(info.getDouble("finance_expense"));
                    gincomeqc.setFeInterestIncome(info.getDouble("fe_interest_expense"));
                    gincomeqc.setFeInterestIncome(info.getDouble("fe_interest_income"));
                    gincomeqc.setOtherIncome(info.getDouble("other_income"));
                    gincomeqc.setInvestIncome(info.getDouble("invest_income"));
                    gincomeqc.setFairvalueChangeIncome(info.getDouble("fairvalue_change_income"));
                    gincomeqc.setCreditImpairmentIncome(info.getDouble("credit_impairment_income"));
                    gincomeqc.setOperateProfit(info.getDouble("operate_profit"));
                    gincomeqc.setNonbusinessIncome(info.getDouble("nonbusiness_income"));
                    gincomeqc.setNonbusinessExpense(info.getDouble("nonbusiness_expense"));
                    gincomeqc.setTotalProfit(info.getDouble("total_profit"));
                    gincomeqc.setIncomeTax(info.getDouble("income_tax"));
                    gincomeqc.setNetprofit(info.getDouble("netprofit"));
                    gincomeqc.setContinuedNetprofit(info.getDouble("continued_netprofit"));
                    gincomeqc.setParentNetprofit(info.getDouble("parent_netprofit"));
                    gincomeqc.setDeductParentNetprofit(info.getDouble("deduct_parent_netprofit"));

                    gincomeqcs.add(gincomeqc);
                }
            }
            if (gincomeqcs.size() > 0){
                gincomeqcMapper.batchInsert(gincomeqcs);

            }

        });
    }
}

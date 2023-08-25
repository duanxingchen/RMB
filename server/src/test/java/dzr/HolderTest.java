package dzr;

import dzr.holder.init.HolderNumInitData;
import dzr.holder.init.TenFlowHolderInitData;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.service.impl.HolderServiceImpl;
import dzr.info.entity.SecurityCode;
import dzr.info.init.CompanyInfoInitData;
import dzr.info.init.SecurityCodeInitData;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.transaction.init.SinaTranDetailInitData;
import dzr.transaction.init.TranDetailInitData;
import dzr.transaction.init.TransactionInitData;
import dzr.transaction.service.impl.CostServiceImpl;
import dzr.transaction.service.impl.JianCangServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author dzr
 * @Date 2023/8/11 15:21
 * @Version 1.0
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HolderTest {
    @Autowired
    SecurityCodeInitData securityCodeInitData;

    @Autowired
    CompanyInfoInitData companyInfoInitData;

    @Autowired
    TenFlowHolderInitData tenFlowHolderInitData;

    @Autowired
    TransactionInitData transactionInitData;

    @Autowired
    HolderNumInitData holderNumInitData;

    @Autowired
    HolderServiceImpl holderService;

    @Autowired
    CostServiceImpl costService;

    @Autowired
    SinaTranDetailInitData sinaTranDetailInitData;

    @Autowired
    JianCangServiceImpl jianCangService;

    @Autowired
    HolderMapper holderMapper;

    @Autowired
    SecurityCodeMapper securityCodeMapper;

    @Autowired
    TranDetailInitData tranDetailInitData;

    @Test
    public void securityCodeInitData(){
        securityCodeInitData.remountPullDataFromWeb();
    }

    @Test
    public void companyInfoInitData(){
        companyInfoInitData.remountPullDataFromWeb();
    }

    @Test
    public void tenFlowHolderInitData(){
        tenFlowHolderInitData.remountPullDataFromWeb();
    }

    @Test
    public void costService(){
        costService.calculate();
    }

    @Test
    public void transactionInitData(){
        transactionInitData.remountPullDataFromWeb();
    }

    @Test
    public void holderNumInitData(){
        holderNumInitData.remountPullDataFromWeb();
    }

    @Test
    public void holderService(){
        //holderNumInitData();
        transactionInitData();
        holderService.calculate();
        costService.calculate();
    }

    /**
     * 新浪历史明细
     */
    @Test
    public void sinaTranDetailInitData(){
        holderMapper.selectAll().stream().filter(holder -> holder.getIndustryMiddle()!= null && holder.getIndustryMiddle().equals("证券"))
                .forEach(holder -> {
            SecurityCode securityCode = new SecurityCode();
            securityCode.setCode(holder.getCode());
            sinaTranDetailInitData.remountByCode(securityCode);
        });

    }

    /**
     * 腾讯当日明细
     */
    @Test
    public void tranDetailInitData(){
        tranDetailInitData.remountPullDataFromWeb();
    }

    /**
     * 建仓统计
     */
    @Test
    public void jianCangService(){
       /* SecurityCode securityCode = new SecurityCode();
        securityCode.setCode("000712");
        securityCode.setName("锦龙");*/
        //jianCangService.calculate(securityCode);
        securityCodeMapper.selectAll().forEach(securityCode -> {
            jianCangService.calculate(securityCode);
        });

    }
}


package dzr;

import dzr.holder.init.HolderNumInitData;
import dzr.holder.init.TenFlowHolderInitData;
import dzr.holder.service.impl.HolderServiceImpl;
import dzr.info.init.CompanyInfoInitData;
import dzr.info.init.SecurityCodeInitData;
import dzr.transaction.init.TransactionInitData;
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
    public void transactionInitData(){
        transactionInitData.remountPullDataFromWeb();
    }

    @Test
    public void holderNumInitData(){
        holderNumInitData.remountPullDataFromWeb();
    }

    @Test
    public void holderService(){
        holderService.calculate();
    }
}

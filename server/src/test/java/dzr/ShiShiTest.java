package dzr;

import dzr.holder.init.HolderNumInitData;
import dzr.holder.init.TenFlowHolderInitData;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.service.impl.HolderServiceImpl;
import dzr.info.entity.SecurityCode;
import dzr.info.init.CompanyInfoInitData;
import dzr.info.init.SecurityCodeInitData;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.plate.init.GuBaInitData;
import dzr.transaction.init.SinaTranDetailInitData;
import dzr.transaction.init.TranDetailInitData;
import dzr.transaction.init.TransactionInitData;
import dzr.transaction.service.impl.CostServiceImpl;
import dzr.transaction.service.impl.JianCangServiceImpl;
import dzr.transaction.service.impl.JianCangTXServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author dzr
 * @Date 2023/8/11 15:21
 * @Version 1.0
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiShiTest {
    @Autowired
    GuBaInitData guBaInitData;


    @Test
    public void securityCodeInitData(){
        guBaInitData.remountPullDataFromWeb();
    }


}


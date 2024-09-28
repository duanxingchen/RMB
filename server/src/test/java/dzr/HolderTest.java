package dzr;

import dzr.common.config.ThreadPools;
import dzr.holder.init.HolderNumInitData;
import dzr.holder.init.TenFlowHolderInitData;
import dzr.holder.mapper.HolderMapper;
import dzr.holder.service.impl.HolderServiceImpl;
import dzr.info.entity.SecurityCode;
import dzr.info.init.CompanyInfoInitData;
import dzr.info.init.ConceptStockInitData;
import dzr.info.init.SecurityCodeInitData;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.organization.init.OrganizationDetailsHolderInitData;
import dzr.plate.init.PlateStockInitData;
import dzr.quantization.service.TradingStockService;
import dzr.transaction.init.SinaTranDetailInitData;
import dzr.transaction.init.TranDetailInitData;
import dzr.transaction.init.TransactionInitData;
import dzr.transaction.service.SlideService;
import dzr.transaction.service.impl.CostServiceImpl;
import dzr.transaction.service.impl.JianCangServiceImpl;
import dzr.transaction.service.impl.JianCangTXServiceImpl;
import dzr.transaction.service.impl.SlideServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

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
    JianCangTXServiceImpl jianCangTXService;

    @Autowired
    HolderMapper holderMapper;

    @Autowired
    SecurityCodeMapper securityCodeMapper;

    @Autowired
    TranDetailInitData tranDetailInitData;


    @Autowired
    SlideServiceImpl slideService;

    @Autowired
    OrganizationDetailsHolderInitData organizationDetailsHolderInitData;


    @Autowired
    PlateStockInitData plateStockInitData;

    @Autowired
    TradingStockService tradingStockService;

    @Autowired
    ConceptStockInitData conceptStockInitData;

    @Test
    public void conceptStockInitData(){
        conceptStockInitData.remountPullDataFromWeb();
    }

    @Test
    public void plateStockInitData(){
        plateStockInitData.remountPullDataFromWeb();
    }

    @Test
    public void organizationDetailsHolderInitData(){
        organizationDetailsHolderInitData.remountPullDataFromWeb();
    }


    @Test
    public void slideService(){
        securityCodeMapper.selectAll().forEach(securityCode -> {
            slideService.calculate(securityCode);
        });

    }

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


    /**
     * 顺序不能乱
     */
    @Test
    public void holderService(){
        /**
         * 初始化数据
         */
        holderNumInitData();
        transactionInitData();
        //plateStockInitData();
        /**
         * 计算股东人数
         */
        holderService.calculate();
        /**
         * 跟踪股票，发钉钉
         */
        //tradingStockService.noticePrice();
        //costService.calculate();
    }


    @Test
    public void noticePrice(){

        /**
         * 跟踪股票，发钉钉
         */
        tradingStockService.noticePrice();
    }

    /**
     * 新浪历史明细
     */
    @Test
    public void sinaTranDetailInitData(){
        securityCodeMapper.selectAll().forEach(securityCode -> {
            securityCode.setCode("300818");
            sinaTranDetailInitData.remountByCode(securityCode);
        });

    }

    /**
     * 腾讯当日明细
     */
    @Test
    public void tranDetailTXInitData(){
        tranDetailInitData.remountPullDataFromWeb();
        //jianCangTXService();
    }

    /**
     * 建仓统计
     */
    @Test
    public void jianCangService(){
        securityCodeMapper.selectAll().forEach(securityCode -> {
            jianCangService.calculate(securityCode);
        });

    }


    @Test
    public void jianCangTXService(){
        List<SecurityCode> securityCodes = securityCodeMapper.selectAll();
        AtomicInteger num = new AtomicInteger();
        securityCodes.forEach(securityCode -> {
            ThreadPoolExecutor executor = ThreadPools.getExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    jianCangTXService.calculate(securityCode);
                    num.getAndIncrement();
                }
            });
        });

        while (true){
            if (Math.abs(num.get() - securityCodes.size()) < 1){
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }
    }
}


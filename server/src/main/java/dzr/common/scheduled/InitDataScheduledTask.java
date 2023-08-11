package dzr.common.scheduled;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import dzr.holder.init.HolderNumInitData;
import dzr.holder.init.TenFlowHolderInitData;
import dzr.info.init.CompanyInfoInitData;
import dzr.info.init.ConceptStockInitData;
import dzr.info.init.EventInitData;
import dzr.info.init.SecurityCodeInitData;
import dzr.organization.init.OrganizationDetailsHolderInitData;
import dzr.plate.init.GuBaInitData;
import dzr.plate.init.PlateBlockInitData;
import dzr.plate.init.PlateStockInitData;
import dzr.transaction.init.SinaTranDetailInitData;
import dzr.transaction.init.TranDetailInitData;
import dzr.transaction.init.TransactionInitData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @Author dzr
 * @Date 2021/12/8 14:27
 * @Version 1.0
 * @Description: 定时任务，每天1点，获取最新数据
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InitDataScheduledTask {

    private final HolderNumInitData holderNumInitData;

    private final TenFlowHolderInitData tenFlowHolderInitData;

    private final TranDetailInitData tranDetailInitData;

    private final TransactionInitData transactionInitData;

    private final OrganizationDetailsHolderInitData organizationDetailsHolderInitData;

    private final GuBaInitData guBaInitData;

    private final PlateStockInitData plateStockInitData;

    private final PlateBlockInitData plateBlockInitData;

    private final SinaTranDetailInitData sinaTranDetailInitData;

    private final EventInitData eventInitData;

    private final SecurityCodeInitData securityCodeInitData;

    private final ConceptStockInitData conceptStockInitData;

    private final CompanyInfoInitData companyInfoInitData;

    @XxlJob("companyInfoInitData")
    public ReturnT<String> companyInfoInitData(String date){
        companyInfoInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }

    @XxlJob("conceptStockInitData")
    public ReturnT<String> conceptStockInitData(String date){
        conceptStockInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }



    @XxlJob("securityCodeInitData")
    public ReturnT<String> securityCodeInitData(String date){
        securityCodeInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }



    @XxlJob("tenFlowHolderInitData")
    public ReturnT<String> tenFlowHolderInitData(String date){
        tenFlowHolderInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }



    /**
     *每月最后一天23点
     */
    //@Async
    //@Scheduled(cron = "0 0 23 L * ?")
    @XxlJob("organizationDetailsHolderInitData")
    public ReturnT<String> organizationDetailsHolderInitData(String date){
        organizationDetailsHolderInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }


    /**
     * 每天凌晨1点
     */
    //@Async
    //@Scheduled(cron = "0 0 1 * * ?")
    @XxlJob("eventInitData")
    public ReturnT<String> eventInitData(String date){
        eventInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }

    /**
     * 每天凌晨1点
     */
    //@Async
    //@Scheduled(cron = "0 0 1 * * ?")
    @XxlJob("guBaInitData")
    public ReturnT<String> guBaInitData(String date){
        guBaInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }


    /**
     * 每天凌晨1点
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    //@Async
    @XxlJob("holderNumInitData")
    public ReturnT<String> holderNumInitData(String date) {
        log.info("---------股东人数任务--------");
        holderNumInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }

    /**
     *周一到周五，上午9点至16点，每隔10分钟
     */
    //@Async
    //@Scheduled(cron = "0 0/10 9-16 ? * MON-FRI")
    @XxlJob("plateBlockInitData")
    public ReturnT<String> plateBlockInitData(String date){
        plateBlockInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }

    /**
     *周一到周五，上午11点1分,15点1分
     */
    //@Async
    //@Scheduled(cron = "0 1 12,15 ? * MON-FRI")
    @XxlJob("plateStockInitData")
    public ReturnT<String> plateStockInitData(String date){
        plateStockInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }

    /**
     *周一到周五，上午11点1分,15点1分
     */
    //@Async
    //@Scheduled(cron = "0 1 12,15 ? * MON-FRI")
    @XxlJob("tranDetailInitData")
    public ReturnT<String> tranDetailInitData(String date){
        tranDetailInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }
    /**
     *周一到周五，每天15点1分
     */
    //@Async
    //@Scheduled(cron = "0 1 15 ? * MON-FRI")
    @XxlJob("transactionInitData")
    public ReturnT<String> transactionInitData(String date){
        transactionInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }

    @XxlJob("sinaTranDetailInitData")
    public ReturnT<String> sinaTranDetailInitData(String date){
        sinaTranDetailInitData.remountPullDataFromWeb();
        return ReturnT.SUCCESS;
    }
}

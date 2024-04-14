package dzr;

import dzr.finance.init.GincomeqcInitData;
import dzr.finance.service.TotalOperateIncomeService;
import dzr.finance.service.impl.TotalOperateIncomeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FinanceTest {
    @Autowired
    GincomeqcInitData financeGincomeqcInitData;

    @Autowired
    TotalOperateIncomeServiceImpl totalOperateIncomeService;

    @Test
    public void financeGincomeqcInitData(){
        financeGincomeqcInitData.remountPullDataFromWeb();
    }

    @Test
    public void totalOperateIncomeService(){
        totalOperateIncomeService.calculate();
    }

}

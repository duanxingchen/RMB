package dzr;


import dzr.quantization.service.TradingStockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingStockServiceTest {

    @Autowired
    TradingStockService tradingStockService;

    @Test
    public void tradingStockService(){
        tradingStockService.noticePrice();
    }
}

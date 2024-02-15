package dzr;


import dzr.holder.service.Comprehensive;
import dzr.holder.service.impl.ComprehensiveImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComprehensiveTest {
    @Autowired
    ComprehensiveImpl comprehensive;

    @Test
    public void comprehensiveTest(){
        comprehensive.analyse();
    }

}

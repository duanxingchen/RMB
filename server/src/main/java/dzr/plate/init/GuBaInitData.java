package dzr.plate.init;


import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import dzr.common.config.ThreadPools;
import dzr.common.utils.DateUtils;
import dzr.info.mapper.SecurityCodeMapper;
import dzr.plate.entity.GuBa;
import dzr.plate.mapper.GuBaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author dzr
 * @Date 2022/10/8 15:24
 * @Version 1.0
 * @Description: 股东人数
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GuBaInitData  {
    private final SecurityCodeMapper securityCodeMapper;
    private final GuBaMapper guBaMapper;

    private final static String url = "http://guba.eastmoney.com/rank/stock?code=$code";
    private final static Playwright playwright = Playwright.create();
    private final static Browser browser = playwright.chromium().launch();
    private final static BrowserContext context = browser.newContext();
    private final static Page page = context.newPage();


    public void remountPullDataFromWeb() {
        securityCodeMapper.selectAll().forEach(securityCode -> {
            try{
                log.info(securityCode.toString());
                String urlPre = url.replace("$code",securityCode.getCode());
                page.navigate(urlPre);
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                Document document = Jsoup.parse(page.content());
                GuBa guBa = new GuBa();
                guBa.setCode(securityCode.getCode());
                guBa.setName(securityCode.getName());

                Elements ranknum = document.getElementsByClass("ranknum");
                Elements datanum = document.getElementsByClass("datanum");
                Elements reportDate = document.getElementsByClass("timenum");
                Elements iconRankdown = document.getElementsByClass("headdata");

                int upOrDown = 1;
                if (iconRankdown.get(1).child(0).className().contains("icon_rankdown")){
                    upOrDown = -1;
                }

                guBa.setRankNum(Integer.valueOf(ranknum.text()));
                guBa.setLineRankNum(Integer.valueOf(datanum.get(0).text()));
                if (datanum.size() >1){
                    guBa.setYesterdayRankNum(upOrDown*Integer.valueOf(datanum.get(1).text()));
                }
                guBa.setRankNumDate(DateUtils.strToDate(reportDate.get(0).text(),"yyyy-MM-dd HH:mm"));
                guBa.setLineRankNumDate(DateUtils.strToDate(reportDate.get(1).text(),"yyyy-MM-dd HH:mm"));
                guBa.setYesterdayRankNumDate(DateUtils.strToDate(reportDate.get(2).text(),"yyyy-MM-dd HH:mm"));
                guBaMapper.delete(guBa);
                guBaMapper.insert(guBa);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}

package dzr.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author dzr
 * @Date 2021/12/14 11:33
 * @Version 1.0
 * @Description:
 */
public class ReportDateUtils {
    private final static Logger log = LoggerFactory.getLogger(ReportDateUtils.class);

    public static  List<Date> getNewestReportDates(int num){
        List<String> fixedReportDatesString = getNewestFixedReportDatesString(num);
        ArrayList<Date> dates = new ArrayList<>();
        fixedReportDatesString.forEach(fixed->{
            dates.add(DateUtils.strToDate(fixed,"yyyy-MM-dd"));
        });
        return dates;
    }

    /**
     * 获取当期最近的num个固定报告期
     * @param num
     * @return
     */
    public static  List<String> getNewestFixedReportDatesString(int num){
        List<String> reportDates = new ArrayList<>();
        Date now = DateUtils.getNowDate();
        int year = now.getYear() + 1900;
        int month = now.getMonth() + 1;

        for (int i = 0; i < num; i++) {
            if (month >= 1 && month <= 3){
                year--;
                month = 12;
            }else if (month >= 4 && month <= 6){
                month = 3;
            }else if (month >= 7 && month <= 9){
                month = 6;
            }else {
                month = 9;
            }
            String reportDate = year + "-" +  getMonthDate(month);
            reportDates.add(reportDate);
        }

        return reportDates;
    }

    private static String getMonthDate(int month){
        String monthStr = null;
        String dayStr = null;
        if (month == 3){
            monthStr = "03";
            dayStr = "31";
        }else if (month == 6){
            monthStr = "06";
            dayStr = "30";
        }else if (month == 9){

            monthStr = "09";
            dayStr = "30";
        }else {
            monthStr = "12";
            dayStr = "31";
        }
        return monthStr + "-" + dayStr;
    }

    public static Date getBeforeReportDate(Date reportDate) {
        return DateUtils.strToDate(getBeforeStringReportDate(DateUtils.dateToStr(reportDate)));

    }

        public static String getBeforeStringReportDate(String reportDate){
        String[] split = reportDate.split(" ")[0].split("-");
        int year = Integer.parseInt(split[0]);
        String monthStr = split[1];
        String dayStr = null;
        switch (monthStr){
            case "03":
                year--;
                monthStr = "12";
                dayStr = "31";
                break;
            case "06":
                monthStr = "03";
                dayStr = "31";
                break;
            case "09":
                monthStr = "06";
                dayStr = "30";
                break;
            case "12":
                monthStr = "09";
                dayStr = "30";
                break;
            default:
        }
     return year + "-" + monthStr + "-" + dayStr + " 00:00:00";
    }

    public static Date getAfterReportDate(Date reportDate){
        return DateUtils.strToDate(getAfterStringReportDate(DateUtils.dateToStr(reportDate)));
    }

    public static String getAfterStringReportDate(String reportDate){
        String[] split = reportDate.split(" ")[0].split("-");
        int year = Integer.parseInt(split[0]);
        String monthStr = split[1];
        String dayStr = null;
        switch (monthStr){
            case "03":
                monthStr = "06";
                dayStr = "30";
                break;
            case "06":
                monthStr = "09";
                dayStr = "30";
                break;
            case "09":
                monthStr = "12";
                dayStr = "31";
                break;
            case "12":
                year++;
                monthStr = "03";
                dayStr = "31";
                break;
            default:
        }
        return year + "-" + monthStr + "-" + dayStr + " 00:00:00";
    }


    /**
     * 开始时间默认从最早的股票上市时间1990年
     * @return
     * @param listingDate
     */
    public static ArrayList<String> getReportDateWithString(Date listingDate)  {
        ArrayList<String> reportDate = new ArrayList<>();
        if(listingDate == null ){
            log.warn("上市时间为空");
            return reportDate;
        }
        int nowYear = DateUtils.getNowDate().getYear() + 1900;
        for (int year = listingDate.getYear() + 1900 ; year < nowYear +1; year++) {
            reportDate.add(year + "-03-31");
            reportDate.add(year + "-06-30");
            reportDate.add(year + "-09-30");
            reportDate.add(year + "-12-31");
        }

        return reportDate;
    }


}

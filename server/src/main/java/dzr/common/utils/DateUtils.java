package dzr.common.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author dzr
 * @Date 2021/12/8 17:13
 * @Version 1.0
 * @Description:
 */
public class DateUtils {

    public static int differentDays(Date date1,Date date2){
        return (int)Math.abs((date2.getTime() - date1.getTime())/(1000*3600*24));
    }

    /**
     * 获取现在时间
     *
     * @return返回长时间格式 yyyy-MM-dd
     */
    public static Date getNowShortDate() {
        return dateToDate(getNowDate(),"yyyy-MM-dd");
    }

    /**
     * 获取现在时间
     *
     * @return返回长时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        return dateToDate(currentTime,"yyyy-MM-dd HH:mm:ss");
    }

    public static int getHours(){
        return getNowDate().getHours();
    }

    public static Date getNowDate(long timeStamp) {
        Date currentTime = new Date(timeStamp);
        return dateToDate(currentTime,"yyyy-MM-dd HH:mm:ss");
    }

    public static Date dateToDate(Date date, String pattern){
        if (pattern == null){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }


    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate,String pattern) {
        if (pattern == null)
        {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        ParsePosition pos = new ParsePosition(0);
        Date strToDate = formatter.parse(strDate, pos);
        return strToDate;
    }

    public static Date strToDate(String strDate) {
        return strToDate(strDate,"yyyy-MM-dd HH:mm:ss");
    }


    public static Date strNotToDate(String strDate) {
        if (strDate.length() == 15){
            return strToDate(strDate,"yyyyMMdd HHmmss");
        }else {
            return strToDate(strDate,"yyyyMMdd Hmmss");
        }
    }


    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     *
     * @param date
     * @param
     * @return
     */
    public static String dateToStr(Date date, String format) {
        SimpleDateFormat formatter;
        if (format == null){
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else {
            formatter = new SimpleDateFormat(format);
        }
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String dateToStr(Date date) {
        return dateToStr(date,null);
    }

    public static int getMinutes(Date date){
        return date.getMinutes();
    }


    /**
     * 时间前推或后推月份,其中JJ表示月份.
     */
    public static Date getPreTime(Date inDate, int month) {
        inDate.setMonth(inDate.getMonth() -month);
        return inDate;
    }

    /**
     * 9:25-15:10交易时间
     * @return
     */
    public static boolean isWorkDayAndIn0925To1600() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(calendar.DAY_OF_WEEK) -1;
        int hours = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        if (week > 0 && week <6){
            if (hours > 8 && hours < 16){
                if(hours == 9 && minute < 25) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 每天1:00
     * @return
     */
    public static boolean isEveryDay0100() {
        return isEveryDay(1);
    }


    public static boolean isEveryDay0300() {
        return isEveryDay(3);
    }

    private static boolean isEveryDay(int t) {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        if(hours == t && minute < 20) {
            return true;
        }
        return false;

    }

    public static Date getBeforeDate(int num) {
        Date nowDate = getNowDate();
        nowDate.setDate(nowDate.getDate() - num);
        return nowDate;
    }


    //获取某个时间段的所有日期
    public static List<Date> findBetweenDates(Date dBegin, Date dEnd)  {
        //日期工具类准备
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dateToDate(dBegin,"yyyy-MM-dd"));

        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dateToDate(dEnd,"yyyy-MM-dd"));

        //装返回的日期集合容器
        List<Date> Datelist = new ArrayList<Date>();
        //将第一个月添加里面去
        Datelist.add(dBegin);
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (dEnd.after(calBegin.getTime()))  {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(calBegin.getTime());
        }

        return Datelist;
    }
}

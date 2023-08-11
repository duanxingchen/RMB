package dzr.common.utils;

/**
 * @Author dzr
 * @Date 2022/10/18 15:56
 * @Version 1.0
 * @Description:
 */
public class FormatUtils {

    public static String parameterAlignment(String msg, Object ... values){
        String ret = "";
        String[] msgs = msg.split("\\|");
        for (int i = 0; i < msgs.length; i++) {
            String msg1 = msgs[i];
            String value = String.valueOf(values[i]);
            value = stringLengthSupplement(value,msg1.length());
            ret = ret + value;
        }
        System.out.println(msg);
        System.out.println(ret);
        return ret;
    }

    private  static String stringLengthSupplement(String str, int sup){
        if (sup <= str.length())
            return str;
        int len = sup - str.length();
        String supStr = "";
        for (int i = 0; i < len; i++) {
            supStr = supStr + "_";
        }
        return supStr.concat(str);

    }



}

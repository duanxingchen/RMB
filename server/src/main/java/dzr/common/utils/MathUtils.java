package dzr.common.utils;

/**
 * @Author dzr
 * @Date 2022/10/25 10:00
 * @Version 1.0
 * @Description:
 */
public class MathUtils {

    public static double doubleRetain2Bit(double in){
        return Math.round(in*100)/100.0;
    }
}

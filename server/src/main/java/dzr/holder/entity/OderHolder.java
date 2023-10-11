package dzr.holder.entity;

import dzr.info.entity.SecurityCode;
import lombok.Data;

/**
 * @Author dzr
 * @Date 2023/9/14 18:22
 * @Version 1.0
 * @Description:
 */
@Data
public class OderHolder {
    SecurityCode securityCode;
    Integer count;
    Integer days;
}

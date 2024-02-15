package dzr.holder.service.comprehensive;

import dzr.info.entity.SecurityCode;

import java.util.List;

public interface Filter {
     List<SecurityCode> filter(List<SecurityCode> securityCodeList);
}

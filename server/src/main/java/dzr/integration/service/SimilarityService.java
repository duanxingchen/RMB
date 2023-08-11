package dzr.integration.service;


import dzr.info.entity.SecurityCode;
import dzr.integration.service.dto.SquareRoot;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * @Author dzr
 * @Date 2023/3/22 15:17
 * @Version 1.0
 * @Description:
 */
public interface SimilarityService {
    CompletableFuture<List<SquareRoot>> calculate(SecurityCode smallSecurityCode,
                                                  SecurityCode bigSecurityCode,
                                                  List<Double> smallFutureData,
                                                  List<Double> smallData,
                                                  int days,
                                                  int futureDays);


}


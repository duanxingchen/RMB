package dzr.web.service.todo;

import lombok.Data;

@Data
public class OrganizationCoordinate {

    private String holderName;


    private String parentOrgName;

    /**
     * 协调次数
     */
    private Integer coordinateCount;

}

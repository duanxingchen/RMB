package dzr.info.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SecurityCode {
    private Integer id;

    /**
     * 股票代码
     */
    private String code;

    /**
     * 股票名称
     */
    private String name;

    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 上市时间
     */
    private Date listingDate;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     *
     * @return sh000001
     */
    public String   getCodeWithExchange() {
        return code.startsWith("6")? "sh"+ code :(code.startsWith("8")? code +".bj":"sz"+ code);
    }

    public String getCodeWithNumber() {
        return code.startsWith("6")? "1."+ code :(code.startsWith("8")? code +".bj":"0."+ code);
    }

    public String getCodeWithCn() {
        return "cn_" + code;
    }
}

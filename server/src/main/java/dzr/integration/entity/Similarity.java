package dzr.integration.entity;

public class Similarity {
    private Integer id;

    private String code;

    private String name;

    /**
     * 过去天数
     */
    private Integer days;

    /**
     * 预测未来天数
     */
    private Integer futureDays;

    /**
     * sqrt区间排名【100,200】
     */
    private Integer sqrtScopeStart;

    /**
     * sqrt区间排名【100,200】
     */
    private Integer sqrtScopeEnd;

    /**
     * sqrt
     */
    private Double sqrt;

    /**
     * 未来sqrt
     */
    private Double futureSqrt;

    /**
     * 未来1天的价格
     */
    private Double futurePrice1;

    /**
     * 未来3天的价格
     */
    private Double futurePrice3;

    /**
     * 未来5天的价格
     */
    private Double futurePrice5;

    /**
     * 未来10天的价格
     */
    private Double futurePrice10;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getFutureDays() {
        return futureDays;
    }

    public void setFutureDays(Integer futureDays) {
        this.futureDays = futureDays;
    }

    public Integer getSqrtScopeStart() {
        return sqrtScopeStart;
    }

    public void setSqrtScopeStart(Integer sqrtScopeStart) {
        this.sqrtScopeStart = sqrtScopeStart;
    }

    public Integer getSqrtScopeEnd() {
        return sqrtScopeEnd;
    }

    public void setSqrtScopeEnd(Integer sqrtScopeEnd) {
        this.sqrtScopeEnd = sqrtScopeEnd;
    }

    public Double getSqrt() {
        return sqrt;
    }

    public void setSqrt(Double sqrt) {
        this.sqrt = sqrt;
    }

    public Double getFutureSqrt() {
        return futureSqrt;
    }

    public void setFutureSqrt(Double futureSqrt) {
        this.futureSqrt = futureSqrt;
    }

    public Double getFuturePrice1() {
        return futurePrice1;
    }

    public void setFuturePrice1(Double futurePrice1) {
        this.futurePrice1 = futurePrice1;
    }

    public Double getFuturePrice3() {
        return futurePrice3;
    }

    public void setFuturePrice3(Double futurePrice3) {
        this.futurePrice3 = futurePrice3;
    }

    public Double getFuturePrice5() {
        return futurePrice5;
    }

    public void setFuturePrice5(Double futurePrice5) {
        this.futurePrice5 = futurePrice5;
    }

    public Double getFuturePrice10() {
        return futurePrice10;
    }

    public void setFuturePrice10(Double futurePrice10) {
        this.futurePrice10 = futurePrice10;
    }
}

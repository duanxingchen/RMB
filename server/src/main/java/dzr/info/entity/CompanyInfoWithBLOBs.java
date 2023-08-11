package dzr.info.entity;

public class CompanyInfoWithBLOBs extends CompanyInfo {
    /**
     * 公司简介
     */
    private String companyProfile;

    /**
     * 经营范围
     */
    private String businessScope;

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile == null ? null : companyProfile.trim();
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope == null ? null : businessScope.trim();
    }
}

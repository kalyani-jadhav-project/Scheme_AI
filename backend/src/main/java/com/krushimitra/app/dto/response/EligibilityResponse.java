package com.krushimitra.app.dto.response;

import java.util.List;
import java.util.Map;

public class EligibilityResponse {
    private String farmerName;
    private int totalSchemesChecked;
    private int eligibleCount;
    private int notEligibleCount;
    private List<EligibleSchemeResult> eligibleSchemes;
    private List<EligibleSchemeResult> notEligibleSchemes;

    public EligibilityResponse() {}
    public EligibilityResponse(String farmerName, int totalSchemesChecked, int eligibleCount,
                                int notEligibleCount, List<EligibleSchemeResult> eligibleSchemes,
                                List<EligibleSchemeResult> notEligibleSchemes) {
        this.farmerName = farmerName; this.totalSchemesChecked = totalSchemesChecked;
        this.eligibleCount = eligibleCount; this.notEligibleCount = notEligibleCount;
        this.eligibleSchemes = eligibleSchemes; this.notEligibleSchemes = notEligibleSchemes;
    }

    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }
    public int getTotalSchemesChecked() { return totalSchemesChecked; }
    public void setTotalSchemesChecked(int totalSchemesChecked) { this.totalSchemesChecked = totalSchemesChecked; }
    public int getEligibleCount() { return eligibleCount; }
    public void setEligibleCount(int eligibleCount) { this.eligibleCount = eligibleCount; }
    public int getNotEligibleCount() { return notEligibleCount; }
    public void setNotEligibleCount(int notEligibleCount) { this.notEligibleCount = notEligibleCount; }
    public List<EligibleSchemeResult> getEligibleSchemes() { return eligibleSchemes; }
    public void setEligibleSchemes(List<EligibleSchemeResult> eligibleSchemes) { this.eligibleSchemes = eligibleSchemes; }
    public List<EligibleSchemeResult> getNotEligibleSchemes() { return notEligibleSchemes; }
    public void setNotEligibleSchemes(List<EligibleSchemeResult> notEligibleSchemes) { this.notEligibleSchemes = notEligibleSchemes; }

    public static class EligibleSchemeResult {
        private Long schemeId;
        private String schemeName;
        private String schemeCode;
        private boolean eligible;
        private String eligibilityReason;
        private String benefits;
        private String requiredDocuments;
        private List<String> nextSteps;
        private String officialWebsite;

        public EligibleSchemeResult() {}
        public EligibleSchemeResult(Long schemeId, String schemeName, String schemeCode, boolean eligible,
                                     String eligibilityReason, String benefits, String requiredDocuments,
                                     List<String> nextSteps, String officialWebsite) {
            this.schemeId = schemeId; this.schemeName = schemeName; this.schemeCode = schemeCode;
            this.eligible = eligible; this.eligibilityReason = eligibilityReason; this.benefits = benefits;
            this.requiredDocuments = requiredDocuments; this.nextSteps = nextSteps; this.officialWebsite = officialWebsite;
        }

        public Long getSchemeId() { return schemeId; }
        public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }
        public String getSchemeName() { return schemeName; }
        public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
        public String getSchemeCode() { return schemeCode; }
        public void setSchemeCode(String schemeCode) { this.schemeCode = schemeCode; }
        public boolean isEligible() { return eligible; }
        public void setEligible(boolean eligible) { this.eligible = eligible; }
        public String getEligibilityReason() { return eligibilityReason; }
        public void setEligibilityReason(String eligibilityReason) { this.eligibilityReason = eligibilityReason; }
        public String getBenefits() { return benefits; }
        public void setBenefits(String benefits) { this.benefits = benefits; }
        public String getRequiredDocuments() { return requiredDocuments; }
        public void setRequiredDocuments(String requiredDocuments) { this.requiredDocuments = requiredDocuments; }
        public List<String> getNextSteps() { return nextSteps; }
        public void setNextSteps(List<String> nextSteps) { this.nextSteps = nextSteps; }
        public String getOfficialWebsite() { return officialWebsite; }
        public void setOfficialWebsite(String officialWebsite) { this.officialWebsite = officialWebsite; }
    }
}

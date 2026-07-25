package com.krushimitra.app.dto.response;

import com.krushimitra.app.entity.GovernmentScheme;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SchemeResponse {
    private Long id;
    private String schemeCode;
    private String name;
    private String description;
    private String benefits;
    private String eligibilityCriteria;
    private String requiredDocuments;
    private String howToApply;
    private String ministry;
    private LocalDate launchDate;
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;
    private String officialWebsite;
    private String helplineNumber;
    private String beneficiaryType;
    private GovernmentScheme.SchemeCategory category;
    private String schemeType;
    private String financialAssistance;
    private String imageUrl;
    private boolean active;
    private boolean centralScheme;
    private String applicableStates;
    private LocalDateTime createdAt;
    private int totalApplications;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSchemeCode() { return schemeCode; }
    public void setSchemeCode(String schemeCode) { this.schemeCode = schemeCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    public String getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }
    public String getRequiredDocuments() { return requiredDocuments; }
    public void setRequiredDocuments(String requiredDocuments) { this.requiredDocuments = requiredDocuments; }
    public String getHowToApply() { return howToApply; }
    public void setHowToApply(String howToApply) { this.howToApply = howToApply; }
    public String getMinistry() { return ministry; }
    public void setMinistry(String ministry) { this.ministry = ministry; }
    public LocalDate getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDate launchDate) { this.launchDate = launchDate; }
    public LocalDate getApplicationStartDate() { return applicationStartDate; }
    public void setApplicationStartDate(LocalDate applicationStartDate) { this.applicationStartDate = applicationStartDate; }
    public LocalDate getApplicationEndDate() { return applicationEndDate; }
    public void setApplicationEndDate(LocalDate applicationEndDate) { this.applicationEndDate = applicationEndDate; }
    public String getOfficialWebsite() { return officialWebsite; }
    public void setOfficialWebsite(String officialWebsite) { this.officialWebsite = officialWebsite; }
    public String getHelplineNumber() { return helplineNumber; }
    public void setHelplineNumber(String helplineNumber) { this.helplineNumber = helplineNumber; }
    public String getBeneficiaryType() { return beneficiaryType; }
    public void setBeneficiaryType(String beneficiaryType) { this.beneficiaryType = beneficiaryType; }
    public GovernmentScheme.SchemeCategory getCategory() { return category; }
    public void setCategory(GovernmentScheme.SchemeCategory category) { this.category = category; }
    public String getSchemeType() { return schemeType; }
    public void setSchemeType(String schemeType) { this.schemeType = schemeType; }
    public String getFinancialAssistance() { return financialAssistance; }
    public void setFinancialAssistance(String financialAssistance) { this.financialAssistance = financialAssistance; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isCentralScheme() { return centralScheme; }
    public void setCentralScheme(boolean centralScheme) { this.centralScheme = centralScheme; }
    public String getApplicableStates() { return applicableStates; }
    public void setApplicableStates(String applicableStates) { this.applicableStates = applicableStates; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getTotalApplications() { return totalApplications; }
    public void setTotalApplications(int totalApplications) { this.totalApplications = totalApplications; }
}

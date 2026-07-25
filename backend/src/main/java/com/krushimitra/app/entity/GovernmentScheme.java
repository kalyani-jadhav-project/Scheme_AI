package com.krushimitra.app.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "government_schemes")
@EntityListeners(AuditingEntityListener.class)
public class GovernmentScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheme_code", unique = true, length = 50)
    private String schemeCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "eligibility_criteria", columnDefinition = "TEXT")
    private String eligibilityCriteria;

    @Column(name = "required_documents", columnDefinition = "TEXT")
    private String requiredDocuments;

    @Column(name = "how_to_apply", columnDefinition = "TEXT")
    private String howToApply;

    @Column(name = "ministry", length = 200)
    private String ministry;

    @Column(name = "launch_date")
    private LocalDate launchDate;

    @Column(name = "application_start_date")
    private LocalDate applicationStartDate;

    @Column(name = "application_end_date")
    private LocalDate applicationEndDate;

    @Column(name = "official_website", length = 300)
    private String officialWebsite;

    @Column(name = "helpline_number", length = 100)
    private String helplineNumber;

    @Column(name = "beneficiary_type", length = 100)
    private String beneficiaryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "scheme_category")
    private SchemeCategory category;

    @Column(name = "scheme_type", length = 100)
    private String schemeType;

    @Column(name = "financial_assistance", length = 200)
    private String financialAssistance;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "is_central_scheme")
    private boolean centralScheme = true;

    @Column(name = "applicable_states", columnDefinition = "TEXT")
    private String applicableStates;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EligibilityRule> eligibilityRules = new ArrayList<>();

    @OneToMany(mappedBy = "scheme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SchemeApplication> applications = new ArrayList<>();

    public GovernmentScheme() {}

    public enum SchemeCategory {
        INCOME_SUPPORT, CROP_INSURANCE, CREDIT, IRRIGATION, MARKET_ACCESS,
        TECHNOLOGY, ORGANIC_FARMING, SOIL_HEALTH, INFRASTRUCTURE, TRAINING, OTHER
    }

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
    public SchemeCategory getCategory() { return category; }
    public void setCategory(SchemeCategory category) { this.category = category; }
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
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<EligibilityRule> getEligibilityRules() { return eligibilityRules; }
    public void setEligibilityRules(List<EligibilityRule> eligibilityRules) { this.eligibilityRules = eligibilityRules; }
    public List<SchemeApplication> getApplications() { return applications; }
    public void setApplications(List<SchemeApplication> applications) { this.applications = applications; }
}

package com.krushimitra.app.dto.response;

import com.krushimitra.app.entity.SchemeApplication;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationResponse {
    private Long id;
    private String applicationNumber;
    private Long farmerId;
    private String farmerName;
    private Long schemeId;
    private String schemeName;
    private String schemeCode;
    private SchemeApplication.ApplicationStatus status;
    private String remarks;
    private String adminRemarks;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private Double disbursementAmount;
    private LocalDateTime disbursementDate;
    private LocalDateTime createdAt;
    private List<DocumentResponse> documents;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }
    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }
    public String getFarmerName() { return farmerName; }
    public void setFarmerName(String farmerName) { this.farmerName = farmerName; }
    public Long getSchemeId() { return schemeId; }
    public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }
    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    public String getSchemeCode() { return schemeCode; }
    public void setSchemeCode(String schemeCode) { this.schemeCode = schemeCode; }
    public SchemeApplication.ApplicationStatus getStatus() { return status; }
    public void setStatus(SchemeApplication.ApplicationStatus status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }
    public Double getDisbursementAmount() { return disbursementAmount; }
    public void setDisbursementAmount(Double disbursementAmount) { this.disbursementAmount = disbursementAmount; }
    public LocalDateTime getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDateTime disbursementDate) { this.disbursementDate = disbursementDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<DocumentResponse> getDocuments() { return documents; }
    public void setDocuments(List<DocumentResponse> documents) { this.documents = documents; }
}

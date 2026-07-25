package com.krushimitra.app.dto.response;

import java.util.Map;

public class AdminDashboardResponse {
    private long totalFarmers;
    private long totalSchemes;
    private long activeSchemes;
    private long totalApplications;
    private long pendingApplications;
    private long approvedApplications;
    private long rejectedApplications;
    private long totalDisbursed;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> applicationsByScheme;
    private Map<String, Long> farmersByState;
    private long newFarmersThisMonth;
    private long newApplicationsThisMonth;

    public AdminDashboardResponse() {}

    public long getTotalFarmers() { return totalFarmers; }
    public void setTotalFarmers(long totalFarmers) { this.totalFarmers = totalFarmers; }
    public long getTotalSchemes() { return totalSchemes; }
    public void setTotalSchemes(long totalSchemes) { this.totalSchemes = totalSchemes; }
    public long getActiveSchemes() { return activeSchemes; }
    public void setActiveSchemes(long activeSchemes) { this.activeSchemes = activeSchemes; }
    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
    public long getPendingApplications() { return pendingApplications; }
    public void setPendingApplications(long pendingApplications) { this.pendingApplications = pendingApplications; }
    public long getApprovedApplications() { return approvedApplications; }
    public void setApprovedApplications(long approvedApplications) { this.approvedApplications = approvedApplications; }
    public long getRejectedApplications() { return rejectedApplications; }
    public void setRejectedApplications(long rejectedApplications) { this.rejectedApplications = rejectedApplications; }
    public long getTotalDisbursed() { return totalDisbursed; }
    public void setTotalDisbursed(long totalDisbursed) { this.totalDisbursed = totalDisbursed; }
    public Map<String, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) { this.applicationsByStatus = applicationsByStatus; }
    public Map<String, Long> getApplicationsByScheme() { return applicationsByScheme; }
    public void setApplicationsByScheme(Map<String, Long> applicationsByScheme) { this.applicationsByScheme = applicationsByScheme; }
    public Map<String, Long> getFarmersByState() { return farmersByState; }
    public void setFarmersByState(Map<String, Long> farmersByState) { this.farmersByState = farmersByState; }
    public long getNewFarmersThisMonth() { return newFarmersThisMonth; }
    public void setNewFarmersThisMonth(long newFarmersThisMonth) { this.newFarmersThisMonth = newFarmersThisMonth; }
    public long getNewApplicationsThisMonth() { return newApplicationsThisMonth; }
    public void setNewApplicationsThisMonth(long newApplicationsThisMonth) { this.newApplicationsThisMonth = newApplicationsThisMonth; }
}

package com.krushimitra.app.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private FarmerProfileResponse farmerProfile;
    private long totalApplications;
    private long pendingApplications;
    private long approvedApplications;
    private long rejectedApplications;
    private List<SchemeResponse> recommendedSchemes;
    private List<ApplicationResponse> recentApplications;
    private List<NotificationResponse> recentNotifications;
    private long totalSchemes;
    private long newSchemesThisMonth;
    private Map<String, Object> weatherInfo;

    public DashboardResponse() {}

    public FarmerProfileResponse getFarmerProfile() { return farmerProfile; }
    public void setFarmerProfile(FarmerProfileResponse farmerProfile) { this.farmerProfile = farmerProfile; }
    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
    public long getPendingApplications() { return pendingApplications; }
    public void setPendingApplications(long pendingApplications) { this.pendingApplications = pendingApplications; }
    public long getApprovedApplications() { return approvedApplications; }
    public void setApprovedApplications(long approvedApplications) { this.approvedApplications = approvedApplications; }
    public long getRejectedApplications() { return rejectedApplications; }
    public void setRejectedApplications(long rejectedApplications) { this.rejectedApplications = rejectedApplications; }
    public List<SchemeResponse> getRecommendedSchemes() { return recommendedSchemes; }
    public void setRecommendedSchemes(List<SchemeResponse> recommendedSchemes) { this.recommendedSchemes = recommendedSchemes; }
    public List<ApplicationResponse> getRecentApplications() { return recentApplications; }
    public void setRecentApplications(List<ApplicationResponse> recentApplications) { this.recentApplications = recentApplications; }
    public List<NotificationResponse> getRecentNotifications() { return recentNotifications; }
    public void setRecentNotifications(List<NotificationResponse> recentNotifications) { this.recentNotifications = recentNotifications; }
    public long getTotalSchemes() { return totalSchemes; }
    public void setTotalSchemes(long totalSchemes) { this.totalSchemes = totalSchemes; }
    public long getNewSchemesThisMonth() { return newSchemesThisMonth; }
    public void setNewSchemesThisMonth(long newSchemesThisMonth) { this.newSchemesThisMonth = newSchemesThisMonth; }
    public Map<String, Object> getWeatherInfo() { return weatherInfo; }
    public void setWeatherInfo(Map<String, Object> weatherInfo) { this.weatherInfo = weatherInfo; }
}

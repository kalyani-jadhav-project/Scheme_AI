package com.krushimitra.app.service;

import com.krushimitra.app.dto.response.*;
import com.krushimitra.app.entity.Farmer;
import com.krushimitra.app.entity.Notification;
import com.krushimitra.app.entity.SchemeApplication;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.repository.*;
import com.krushimitra.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for dashboard data aggregation
 */
@Service
public class DashboardService {

    @Autowired private FarmerRepository farmerRepository;
    @Autowired private GovernmentSchemeRepository schemeRepository;
    @Autowired private SchemeApplicationRepository applicationRepository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private FarmerService farmerService;
    @Autowired private SchemeService schemeService;
    @Autowired private NotificationService notificationService;

    /**
     * Get farmer dashboard data
     */
    public DashboardResponse getFarmerDashboard() {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Farmer farmer = farmerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        FarmerProfileResponse profile = farmerService.getCurrentFarmerProfile();

        // Application stats
        long total = applicationRepository.countByFarmerId(farmer.getId());
        long pending = applicationRepository.countByFarmerIdAndStatus(farmer.getId(), SchemeApplication.ApplicationStatus.SUBMITTED) +
                       applicationRepository.countByFarmerIdAndStatus(farmer.getId(), SchemeApplication.ApplicationStatus.UNDER_REVIEW);
        long approved = applicationRepository.countByFarmerIdAndStatus(farmer.getId(), SchemeApplication.ApplicationStatus.APPROVED);
        long rejected = applicationRepository.countByFarmerIdAndStatus(farmer.getId(), SchemeApplication.ApplicationStatus.REJECTED);

        // Recent applications
        List<ApplicationResponse> recentApps = applicationRepository
                .findByFarmerIdOrderByCreatedAtDesc(farmer.getId())
                .stream()
                .limit(5)
                .map(this::mapToApplicationResponse)
                .collect(Collectors.toList());

        // Recommended schemes (all active schemes for now)
        List<SchemeResponse> recommended = schemeRepository
                .findActiveSchemesByState(farmer.getState() != null ? farmer.getState() : "ALL")
                .stream()
                .limit(6)
                .map(schemeService::mapToSchemeResponse)
                .collect(Collectors.toList());

        // Recent notifications
        List<NotificationResponse> notifications = notificationService.getRecentNotifications(farmer.getId());

        // Scheme counts
        long totalSchemes = schemeRepository.countByActiveTrue();
        long newSchemesThisMonth = schemeRepository.countNewSchemesThisMonth();

        // Mock weather info
        Map<String, Object> weather = new HashMap<>();
        weather.put("location", farmer.getDistrict() + ", " + farmer.getState());
        weather.put("temperature", "28°C");
        weather.put("condition", "Partly Cloudy");
        weather.put("humidity", "72%");
        weather.put("rainfall", "Light rain expected tomorrow");

        DashboardResponse dash = new DashboardResponse();
        dash.setFarmerProfile(profile);
        dash.setTotalApplications(total);
        dash.setPendingApplications(pending);
        dash.setApprovedApplications(approved);
        dash.setRejectedApplications(rejected);
        dash.setRecommendedSchemes(recommended);
        dash.setRecentApplications(recentApps);
        dash.setRecentNotifications(notifications);
        dash.setTotalSchemes(totalSchemes);
        dash.setNewSchemesThisMonth(newSchemesThisMonth);
        dash.setWeatherInfo(weather);
        return dash;
    }

    /**
     * Get admin dashboard analytics
     */
    public AdminDashboardResponse getAdminDashboard() {
        long totalFarmers = farmerRepository.count();
        long totalSchemes = schemeRepository.count();
        long activeSchemes = schemeRepository.countByActiveTrue();
        long totalApps = applicationRepository.count();
        long pending = applicationRepository.countByStatus(SchemeApplication.ApplicationStatus.SUBMITTED) +
                       applicationRepository.countByStatus(SchemeApplication.ApplicationStatus.UNDER_REVIEW);
        long approved = applicationRepository.countByStatus(SchemeApplication.ApplicationStatus.APPROVED);
        long rejected = applicationRepository.countByStatus(SchemeApplication.ApplicationStatus.REJECTED);

        Map<String, Long> appsByStatus = new LinkedHashMap<>();
        for (SchemeApplication.ApplicationStatus status : SchemeApplication.ApplicationStatus.values()) {
            appsByStatus.put(status.name(), applicationRepository.countByStatus(status));
        }

        Map<String, Long> appsByScheme = new LinkedHashMap<>();
        applicationRepository.countApplicationsByScheme()
                .forEach(row -> appsByScheme.put((String) row[0], (Long) row[1]));

        Map<String, Long> farmersByState = new LinkedHashMap<>();
        // Simplified - would need a proper query in production
        farmersByState.put("Maharashtra", farmerRepository.countByState("Maharashtra"));
        farmersByState.put("Punjab", farmerRepository.countByState("Punjab"));
        farmersByState.put("Uttar Pradesh", farmerRepository.countByState("Uttar Pradesh"));

        AdminDashboardResponse adminDash = new AdminDashboardResponse();
        adminDash.setTotalFarmers(totalFarmers);
        adminDash.setTotalSchemes(totalSchemes);
        adminDash.setActiveSchemes(activeSchemes);
        adminDash.setTotalApplications(totalApps);
        adminDash.setPendingApplications(pending);
        adminDash.setApprovedApplications(approved);
        adminDash.setRejectedApplications(rejected);
        adminDash.setApplicationsByStatus(appsByStatus);
        adminDash.setApplicationsByScheme(appsByScheme);
        adminDash.setFarmersByState(farmersByState);
        adminDash.setNewFarmersThisMonth(farmerRepository.countNewFarmersThisMonth());
        adminDash.setNewApplicationsThisMonth(applicationRepository.countNewApplicationsThisMonth());
        return adminDash;
    }

    private ApplicationResponse mapToApplicationResponse(SchemeApplication app) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setApplicationNumber(app.getApplicationNumber());
        response.setFarmerId(app.getFarmer().getId());
        response.setFarmerName(app.getFarmer().getUser().getFullName());
        response.setSchemeId(app.getScheme().getId());
        response.setSchemeName(app.getScheme().getName());
        response.setSchemeCode(app.getScheme().getSchemeCode());
        response.setStatus(app.getStatus());
        response.setSubmittedAt(app.getSubmittedAt());
        response.setCreatedAt(app.getCreatedAt());
        return response;
    }
}

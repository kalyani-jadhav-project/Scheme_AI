package com.krushimitra.app.controller;

import com.krushimitra.app.dto.request.SchemeRequest;
import com.krushimitra.app.dto.response.*;
import com.krushimitra.app.entity.Notification;
import com.krushimitra.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Admin operations
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminController {

    @Autowired private DashboardService dashboardService;
    @Autowired private FarmerService farmerService;
    @Autowired private SchemeService schemeService;
    @Autowired private ApplicationService applicationService;
    @Autowired private NotificationService notificationService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard fetched", dashboardService.getAdminDashboard()));
    }

    @GetMapping("/farmers")
    public ResponseEntity<ApiResponse<Page<FarmerProfileResponse>>> getAllFarmers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Farmers fetched",
                farmerService.getAllFarmers(PageRequest.of(page, size))));
    }

    @GetMapping("/farmers/{id}")
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getFarmer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Farmer fetched", farmerService.getFarmerById(id)));
    }

    @GetMapping("/schemes")
    public ResponseEntity<ApiResponse<Page<SchemeResponse>>> getAllSchemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Schemes fetched",
                schemeService.getAllActiveSchemes(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @PostMapping("/schemes")
    public ResponseEntity<ApiResponse<SchemeResponse>> createScheme(@RequestBody SchemeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Scheme created", schemeService.createScheme(request)));
    }

    @PutMapping("/schemes/{id}")
    public ResponseEntity<ApiResponse<SchemeResponse>> updateScheme(
            @PathVariable Long id, @RequestBody SchemeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Scheme updated", schemeService.updateScheme(id, request)));
    }

    @DeleteMapping("/schemes/{id}")
    public ResponseEntity<ApiResponse<String>> deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Scheme deleted", null));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Applications fetched",
                applicationService.getAllApplications(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @PostMapping("/notifications/broadcast")
    public ResponseEntity<ApiResponse<String>> broadcast(
            @RequestParam String title,
            @RequestParam String message) {
        notificationService.sendGlobalNotification(title, message, Notification.NotificationType.GENERAL);
        return ResponseEntity.ok(ApiResponse.success("Notification sent", null));
    }
}

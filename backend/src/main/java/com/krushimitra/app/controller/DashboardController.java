package com.krushimitra.app.controller;

import com.krushimitra.app.dto.response.AdminDashboardResponse;
import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.DashboardResponse;
import com.krushimitra.app.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for dashboard data
 */
@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    @Autowired private DashboardService dashboardService;

    @GetMapping("/farmer")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<DashboardResponse>> getFarmerDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard data fetched", dashboardService.getFarmerDashboard()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getAdminDashboard() {
        return ResponseEntity.ok(ApiResponse.success("Admin dashboard fetched", dashboardService.getAdminDashboard()));
    }
}

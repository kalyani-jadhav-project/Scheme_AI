package com.krushimitra.app.controller;

import com.krushimitra.app.dto.request.FarmerProfileRequest;
import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.FarmerProfileResponse;
import com.krushimitra.app.service.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for farmer profile operations
 */
@RestController
@RequestMapping("/farmer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FarmerController {

    @Autowired private FarmerService farmerService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getMyProfile() {
        return ResponseEntity.ok(ApiResponse.success("Profile fetched", farmerService.getCurrentFarmerProfile()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> updateProfile(
            @RequestBody FarmerProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated", farmerService.updateFarmerProfile(request)));
    }

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<FarmerProfileResponse>> getFarmerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Farmer fetched", farmerService.getFarmerProfileByUserId(userId)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<FarmerProfileResponse>>> getAllFarmers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Farmers fetched",
                farmerService.getAllFarmers(PageRequest.of(page, size))));
    }
}

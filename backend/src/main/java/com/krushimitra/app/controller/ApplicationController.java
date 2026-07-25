package com.krushimitra.app.controller;

import com.krushimitra.app.dto.request.ApplicationRequest;
import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.ApplicationResponse;
import com.krushimitra.app.dto.response.DocumentResponse;
import com.krushimitra.app.entity.SchemeApplication;
import com.krushimitra.app.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST Controller for scheme applications
 */
@RestController
@RequestMapping("/applications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApplicationController {

    @Autowired private ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> apply(@RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Application submitted", applicationService.applyForScheme(request)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Applications fetched",
                applicationService.getMyApplications(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Application fetched", applicationService.getApplicationById(id)));
    }

    @PostMapping("/{id}/documents")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @PathVariable Long id,
            @RequestParam String documentType,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(ApiResponse.success("Document uploaded",
                applicationService.uploadDocument(id, documentType, file)));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam SchemeApplication.ApplicationStatus status,
            @RequestParam(required = false) String adminRemarks) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                applicationService.updateApplicationStatus(id, status, adminRemarks)));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("All applications fetched",
                applicationService.getAllApplications(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }
}

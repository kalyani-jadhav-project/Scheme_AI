package com.krushimitra.app.controller;

import com.krushimitra.app.dto.request.SchemeRequest;
import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.SchemeResponse;
import com.krushimitra.app.entity.GovernmentScheme;
import com.krushimitra.app.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Government Scheme operations
 */
@RestController
@RequestMapping("/schemes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SchemeController {

    @Autowired private SchemeService schemeService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<SchemeResponse>>> getAllSchemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Schemes fetched",
                schemeService.getAllActiveSchemes(PageRequest.of(page, size, Sort.by("createdAt").descending()))));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SchemeResponse>>> searchSchemes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success("Search results",
                schemeService.searchSchemes(keyword, PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchemeResponse>> getSchemeById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Scheme fetched", schemeService.getSchemeById(id)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SchemeResponse>> getSchemeByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success("Scheme fetched", schemeService.getSchemeByCode(code)));
    }

    @GetMapping("/by-state/{state}")
    public ResponseEntity<ApiResponse<List<SchemeResponse>>> getSchemesByState(@PathVariable String state) {
        return ResponseEntity.ok(ApiResponse.success("Schemes by state fetched",
                schemeService.getSchemesByState(state)));
    }

    @GetMapping("/by-category/{category}")
    public ResponseEntity<ApiResponse<List<SchemeResponse>>> getSchemesByCategory(
            @PathVariable GovernmentScheme.SchemeCategory category) {
        return ResponseEntity.ok(ApiResponse.success("Schemes by category fetched",
                schemeService.getSchemesByCategory(category)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SchemeResponse>> createScheme(@RequestBody SchemeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Scheme created", schemeService.createScheme(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<SchemeResponse>> updateScheme(
            @PathVariable Long id, @RequestBody SchemeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Scheme updated", schemeService.updateScheme(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Scheme deactivated", null));
    }

    // Public endpoints
    @GetMapping("/public/list")
    public ResponseEntity<ApiResponse<Page<SchemeResponse>>> getPublicSchemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(ApiResponse.success("Public schemes fetched",
                schemeService.getAllActiveSchemes(PageRequest.of(page, size))));
    }
}

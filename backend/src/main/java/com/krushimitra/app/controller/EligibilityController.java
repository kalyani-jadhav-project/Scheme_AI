package com.krushimitra.app.controller;

import com.krushimitra.app.dto.request.EligibilityCheckRequest;
import com.krushimitra.app.dto.response.ApiResponse;
import com.krushimitra.app.dto.response.EligibilityResponse;
import com.krushimitra.app.service.EligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for eligibility checking
 */
@RestController
@RequestMapping("/eligibility")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EligibilityController {

    @Autowired private EligibilityService eligibilityService;

    @PostMapping("/check")
    public ResponseEntity<ApiResponse<EligibilityResponse>> checkEligibility(
            @RequestBody EligibilityCheckRequest request) {
        EligibilityResponse response = eligibilityService.checkEligibility(request);
        return ResponseEntity.ok(ApiResponse.success("Eligibility check completed", response));
    }
}

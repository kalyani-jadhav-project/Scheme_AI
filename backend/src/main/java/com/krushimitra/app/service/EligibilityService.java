package com.krushimitra.app.service;

import com.krushimitra.app.dto.request.EligibilityCheckRequest;
import com.krushimitra.app.dto.response.EligibilityResponse;
import com.krushimitra.app.entity.Farmer;
import com.krushimitra.app.entity.GovernmentScheme;
import com.krushimitra.app.repository.GovernmentSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service for eligibility checking logic
 */
@Service
public class EligibilityService {

    @Autowired private GovernmentSchemeRepository schemeRepository;

    /**
     * Check eligibility for all active schemes based on farmer data
     */
    public EligibilityResponse checkEligibility(EligibilityCheckRequest request) {
        List<GovernmentScheme> allSchemes = schemeRepository.findByActiveTrue(
                org.springframework.data.domain.Pageable.unpaged()).getContent();

        List<EligibilityResponse.EligibleSchemeResult> eligible = new ArrayList<>();
        List<EligibilityResponse.EligibleSchemeResult> notEligible = new ArrayList<>();

        for (GovernmentScheme scheme : allSchemes) {
            EligibilityResponse.EligibleSchemeResult result = checkSchemeEligibility(request, scheme);
            if (result.isEligible()) {
                eligible.add(result);
            } else {
                notEligible.add(result);
            }
        }

        EligibilityResponse response = new EligibilityResponse();
        response.setFarmerName(request.getName());
        response.setTotalSchemesChecked(allSchemes.size());
        response.setEligibleCount(eligible.size());
        response.setNotEligibleCount(notEligible.size());
        response.setEligibleSchemes(eligible);
        response.setNotEligibleSchemes(notEligible);
        return response;
    }

    private EligibilityResponse.EligibleSchemeResult checkSchemeEligibility(
            EligibilityCheckRequest req, GovernmentScheme scheme) {

        boolean isEligible = true;
        StringBuilder reason = new StringBuilder();
        List<String> nextSteps = new ArrayList<>();
        List<String> missingItems = new ArrayList<>();

        switch (scheme.getSchemeCode()) {
            case "PM-KISAN" -> {
                // Requires: cultivable land, no govt job, no income tax payer
                if (req.getFarmerCategory() == Farmer.FarmerCategory.LANDLESS) {
                    isEligible = false;
                    reason.append("PM-KISAN requires ownership/cultivable land. ");
                }
                if (!req.isAadhaarAvailable()) {
                    isEligible = false;
                    missingItems.add("Aadhaar Card");
                }
                if (!req.isBankAccountAvailable()) {
                    isEligible = false;
                    missingItems.add("Bank Account");
                }
                if (isEligible) {
                    reason.append("You are eligible for ₹6,000/year income support. ");
                    nextSteps.addAll(Arrays.asList(
                        "Visit pmkisan.gov.in or nearest CSC",
                        "Carry Aadhaar card and land records",
                        "Complete e-KYC on the portal",
                        "Link bank account for direct transfer"
                    ));
                } else {
                    if (!missingItems.isEmpty()) {
                        reason.append("Missing: ").append(String.join(", ", missingItems));
                    }
                }
            }
            case "PMFBY" -> {
                if (req.getCropType() == null || req.getCropType().isEmpty()) {
                    isEligible = false;
                    reason.append("Crop type information is required for PMFBY enrollment. ");
                }
                if (!req.isAadhaarAvailable()) {
                    isEligible = false;
                    missingItems.add("Aadhaar Card");
                }
                if (!req.isBankAccountAvailable()) {
                    isEligible = false;
                    missingItems.add("Bank Account");
                }
                if (isEligible) {
                    reason.append("You are eligible for crop insurance coverage. ");
                    nextSteps.addAll(Arrays.asList(
                        "Enroll before sowing season deadline",
                        "Contact nearest bank or cooperative society",
                        "Carry land records and Aadhaar",
                        "Pay premium (2% for Kharif, 1.5% for Rabi)"
                    ));
                } else {
                    if (!missingItems.isEmpty()) {
                        reason.append("Missing: ").append(String.join(", ", missingItems));
                    }
                }
            }
            case "KCC" -> {
                if (!req.isAadhaarAvailable()) {
                    isEligible = false;
                    missingItems.add("Aadhaar Card");
                }
                if (req.getLandHolding() == null || req.getLandHolding().compareTo(BigDecimal.ZERO) <= 0) {
                    if (req.getFarmerCategory() != Farmer.FarmerCategory.TENANT) {
                        isEligible = false;
                        reason.append("KCC requires land ownership or tenant agreement. ");
                    }
                }
                if (isEligible) {
                    reason.append("You are eligible for Kisan Credit Card with interest subvention. ");
                    nextSteps.addAll(Arrays.asList(
                        "Visit nearest bank branch",
                        "Carry Aadhaar, PAN, and land records",
                        "Fill KCC application form",
                        "Credit limit based on crop requirements"
                    ));
                } else {
                    if (!missingItems.isEmpty()) {
                        reason.append("Missing: ").append(String.join(", ", missingItems));
                    }
                }
            }
            case "SHC" -> {
                // All farmers are eligible
                reason.append("All farmers are eligible for free Soil Health Card. ");
                nextSteps.addAll(Arrays.asList(
                    "Contact local agriculture department",
                    "Request soil sample collection",
                    "Carry Aadhaar and land records",
                    "Receive card with crop-wise fertilizer recommendations"
                ));
            }
            case "PMKSY" -> {
                if (req.getLandHolding() == null || req.getLandHolding().compareTo(BigDecimal.ZERO) <= 0) {
                    isEligible = false;
                    reason.append("PMKSY requires cultivable land. ");
                }
                if (isEligible) {
                    BigDecimal subsidy = (req.getFarmerCategory() == Farmer.FarmerCategory.SMALL ||
                                          req.getFarmerCategory() == Farmer.FarmerCategory.MARGINAL)
                            ? BigDecimal.valueOf(55) : BigDecimal.valueOf(45);
                    reason.append("Eligible for ").append(subsidy).append("% subsidy on micro-irrigation. ");
                    nextSteps.addAll(Arrays.asList(
                        "Apply at district agriculture office",
                        "Carry land records and Aadhaar",
                        "Select approved micro-irrigation equipment",
                        "Subsidy directly credited to account"
                    ));
                }
            }
            case "ENAM" -> {
                if (!req.isAadhaarAvailable() || !req.isBankAccountAvailable()) {
                    isEligible = false;
                    reason.append("eNAM requires Aadhaar and bank account. ");
                } else {
                    reason.append("Eligible to sell produce at better prices through digital mandi. ");
                    nextSteps.addAll(Arrays.asList(
                        "Register at enam.gov.in",
                        "Carry Aadhaar and bank account details",
                        "Locate nearest eNAM-integrated mandi",
                        "Start trading your produce online"
                    ));
                }
            }
            case "FPO" -> {
                reason.append("You can join or form an FPO with other farmers for collective benefits. ");
                nextSteps.addAll(Arrays.asList(
                    "Contact NABARD or SFAC office",
                    "Find 300+ farmers (100 for hilly areas)",
                    "Register as Farmer Producer Organization",
                    "Access equity grant of ₹18 lakh"
                ));
            }
            default -> {
                reason.append("Please check official scheme portal for detailed eligibility. ");
                nextSteps.add("Visit " + scheme.getOfficialWebsite());
            }
        }

        EligibilityResponse.EligibleSchemeResult result = new EligibilityResponse.EligibleSchemeResult();
        result.setSchemeId(scheme.getId());
        result.setSchemeName(scheme.getName());
        result.setSchemeCode(scheme.getSchemeCode());
        result.setEligible(isEligible);
        result.setEligibilityReason(reason.toString().trim());
        result.setBenefits(scheme.getBenefits());
        result.setRequiredDocuments(scheme.getRequiredDocuments());
        result.setNextSteps(nextSteps);
        result.setOfficialWebsite(scheme.getOfficialWebsite());
        return result;
    }
}

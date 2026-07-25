package com.krushimitra.app.service;

import com.krushimitra.app.dto.request.SchemeRequest;
import com.krushimitra.app.dto.response.SchemeResponse;
import com.krushimitra.app.entity.GovernmentScheme;
import com.krushimitra.app.exception.DuplicateResourceException;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.repository.GovernmentSchemeRepository;
import com.krushimitra.app.repository.SchemeApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for government scheme management
 */
@Service
@Transactional
public class SchemeService {

    @Autowired private GovernmentSchemeRepository schemeRepository;
    @Autowired private SchemeApplicationRepository applicationRepository;

    public Page<SchemeResponse> getAllActiveSchemes(Pageable pageable) {
        return schemeRepository.findByActiveTrue(pageable)
                .map(this::mapToSchemeResponse);
    }

    public Page<SchemeResponse> searchSchemes(String keyword, Pageable pageable) {
        return schemeRepository.searchSchemes(keyword, pageable)
                .map(this::mapToSchemeResponse);
    }

    public SchemeResponse getSchemeById(Long id) {
        GovernmentScheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        return mapToSchemeResponse(scheme);
    }

    public SchemeResponse getSchemeByCode(String code) {
        GovernmentScheme scheme = schemeRepository.findBySchemeCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "code", code));
        return mapToSchemeResponse(scheme);
    }

    public List<SchemeResponse> getSchemesByState(String state) {
        return schemeRepository.findActiveSchemesByState(state)
                .stream()
                .map(this::mapToSchemeResponse)
                .collect(Collectors.toList());
    }

    public List<SchemeResponse> getSchemesByCategory(GovernmentScheme.SchemeCategory category) {
        return schemeRepository.findByActiveTrueAndCategory(category)
                .stream()
                .map(this::mapToSchemeResponse)
                .collect(Collectors.toList());
    }

    public SchemeResponse createScheme(SchemeRequest request) {
        if (request.getSchemeCode() != null &&
            schemeRepository.findBySchemeCode(request.getSchemeCode()).isPresent()) {
            throw new DuplicateResourceException("Scheme with code already exists: " + request.getSchemeCode());
        }
        GovernmentScheme scheme = mapRequestToScheme(request, new GovernmentScheme());
        return mapToSchemeResponse(schemeRepository.save(scheme));
    }

    public SchemeResponse updateScheme(Long id, SchemeRequest request) {
        GovernmentScheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        mapRequestToScheme(request, scheme);
        return mapToSchemeResponse(schemeRepository.save(scheme));
    }

    public void deleteScheme(Long id) {
        GovernmentScheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", id));
        scheme.setActive(false); // Soft delete
        schemeRepository.save(scheme);
    }

    private GovernmentScheme mapRequestToScheme(SchemeRequest request, GovernmentScheme scheme) {
        if (request.getSchemeCode() != null) scheme.setSchemeCode(request.getSchemeCode());
        if (request.getName() != null) scheme.setName(request.getName());
        if (request.getDescription() != null) scheme.setDescription(request.getDescription());
        if (request.getBenefits() != null) scheme.setBenefits(request.getBenefits());
        if (request.getEligibilityCriteria() != null) scheme.setEligibilityCriteria(request.getEligibilityCriteria());
        if (request.getRequiredDocuments() != null) scheme.setRequiredDocuments(request.getRequiredDocuments());
        if (request.getHowToApply() != null) scheme.setHowToApply(request.getHowToApply());
        if (request.getMinistry() != null) scheme.setMinistry(request.getMinistry());
        if (request.getLaunchDate() != null) scheme.setLaunchDate(request.getLaunchDate());
        if (request.getApplicationStartDate() != null) scheme.setApplicationStartDate(request.getApplicationStartDate());
        if (request.getApplicationEndDate() != null) scheme.setApplicationEndDate(request.getApplicationEndDate());
        if (request.getOfficialWebsite() != null) scheme.setOfficialWebsite(request.getOfficialWebsite());
        if (request.getHelplineNumber() != null) scheme.setHelplineNumber(request.getHelplineNumber());
        if (request.getBeneficiaryType() != null) scheme.setBeneficiaryType(request.getBeneficiaryType());
        if (request.getCategory() != null) scheme.setCategory(request.getCategory());
        if (request.getSchemeType() != null) scheme.setSchemeType(request.getSchemeType());
        if (request.getFinancialAssistance() != null) scheme.setFinancialAssistance(request.getFinancialAssistance());
        if (request.getImageUrl() != null) scheme.setImageUrl(request.getImageUrl());
        if (request.getApplicableStates() != null) scheme.setApplicableStates(request.getApplicableStates());
        scheme.setActive(request.isActive());
        scheme.setCentralScheme(request.isCentralScheme());
        return scheme;
    }

    public SchemeResponse mapToSchemeResponse(GovernmentScheme scheme) {
        SchemeResponse response = new SchemeResponse();
        response.setId(scheme.getId());
        response.setSchemeCode(scheme.getSchemeCode());
        response.setName(scheme.getName());
        response.setDescription(scheme.getDescription());
        response.setBenefits(scheme.getBenefits());
        response.setEligibilityCriteria(scheme.getEligibilityCriteria());
        response.setRequiredDocuments(scheme.getRequiredDocuments());
        response.setHowToApply(scheme.getHowToApply());
        response.setMinistry(scheme.getMinistry());
        response.setLaunchDate(scheme.getLaunchDate());
        response.setApplicationStartDate(scheme.getApplicationStartDate());
        response.setApplicationEndDate(scheme.getApplicationEndDate());
        response.setOfficialWebsite(scheme.getOfficialWebsite());
        response.setHelplineNumber(scheme.getHelplineNumber());
        response.setBeneficiaryType(scheme.getBeneficiaryType());
        response.setCategory(scheme.getCategory());
        response.setSchemeType(scheme.getSchemeType());
        response.setFinancialAssistance(scheme.getFinancialAssistance());
        response.setImageUrl(scheme.getImageUrl());
        response.setActive(scheme.isActive());
        response.setCentralScheme(scheme.isCentralScheme());
        response.setApplicableStates(scheme.getApplicableStates());
        response.setCreatedAt(scheme.getCreatedAt());
        response.setTotalApplications(scheme.getApplications().size());
        return response;
    }
}

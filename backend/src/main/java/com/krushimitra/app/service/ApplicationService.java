package com.krushimitra.app.service;

import com.krushimitra.app.dto.request.ApplicationRequest;
import com.krushimitra.app.dto.response.ApplicationResponse;
import com.krushimitra.app.dto.response.DocumentResponse;
import com.krushimitra.app.entity.*;
import com.krushimitra.app.exception.BadRequestException;
import com.krushimitra.app.exception.ResourceNotFoundException;
import com.krushimitra.app.exception.UnauthorizedException;
import com.krushimitra.app.repository.*;
import com.krushimitra.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for scheme application management
 */
@Service
@Transactional
public class ApplicationService {

    @Autowired private SchemeApplicationRepository applicationRepository;
    @Autowired private FarmerRepository farmerRepository;
    @Autowired private GovernmentSchemeRepository schemeRepository;
    @Autowired private UploadedDocumentRepository documentRepository;
    @Autowired private NotificationRepository notificationRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Apply for a scheme
     */
    public ApplicationResponse applyForScheme(ApplicationRequest request) {
        UserPrincipal currentUser = getCurrentUser();
        Farmer farmer = farmerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        GovernmentScheme scheme = schemeRepository.findById(request.getSchemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Scheme", "id", request.getSchemeId()));

        if (applicationRepository.existsByFarmerIdAndSchemeId(farmer.getId(), scheme.getId())) {
            throw new BadRequestException("You have already applied for this scheme");
        }

        String appNumber = "KM" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        SchemeApplication application = new SchemeApplication();
        application.setApplicationNumber(appNumber);
        application.setFarmer(farmer);
        application.setScheme(scheme);
        application.setStatus(SchemeApplication.ApplicationStatus.SUBMITTED);
        application.setRemarks(request.getRemarks());
        application.setSubmittedAt(LocalDateTime.now());

        SchemeApplication saved = applicationRepository.save(application);

        // Create notification
        Notification notification = new Notification();
        notification.setFarmer(farmer);
        notification.setTitle("Application Submitted Successfully");
        notification.setMessage("Your application for " + scheme.getName() + " (Ref: " + appNumber + ") has been submitted.");
        notification.setType(Notification.NotificationType.APPLICATION_STATUS);
        notification.setRelatedSchemeId(scheme.getId());
        notification.setRelatedApplicationId(saved.getId());
        notificationRepository.save(notification);

        return mapToApplicationResponse(saved);
    }

    /**
     * Get current farmer's applications
     */
    public Page<ApplicationResponse> getMyApplications(Pageable pageable) {
        UserPrincipal currentUser = getCurrentUser();
        Farmer farmer = farmerRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));
        return applicationRepository.findByFarmerId(farmer.getId(), pageable)
                .map(this::mapToApplicationResponse);
    }

    /**
     * Get application by ID
     */
    public ApplicationResponse getApplicationById(Long id) {
        UserPrincipal currentUser = getCurrentUser();
        SchemeApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));

        // Verify ownership unless admin
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().contains("ADMIN"));
        if (!isAdmin && !application.getFarmer().getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to view this application");
        }
        return mapToApplicationResponse(application);
    }

    /**
     * Upload document for application
     */
    public DocumentResponse uploadDocument(Long applicationId, String documentType, MultipartFile file) throws IOException {
        SchemeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));

        // Ensure upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        UploadedDocument document = new UploadedDocument();
        document.setApplication(application);
        document.setDocumentType(documentType);
        document.setDocumentName(file.getOriginalFilename());
        document.setFilePath(filePath.toString());
        document.setFileSize(file.getSize());
        document.setFileType(file.getContentType());
        document.setVerificationStatus(UploadedDocument.VerificationStatus.PENDING);

        UploadedDocument saved = documentRepository.save(document);
        return mapToDocumentResponse(saved);
    }

    /**
     * Update application status (Admin)
     */
    public ApplicationResponse updateApplicationStatus(Long id, SchemeApplication.ApplicationStatus status, String adminRemarks) {
        SchemeApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));

        application.setStatus(status);
        application.setAdminRemarks(adminRemarks);

        if (status == SchemeApplication.ApplicationStatus.APPROVED) {
            application.setApprovedAt(LocalDateTime.now());
        } else if (status == SchemeApplication.ApplicationStatus.REJECTED) {
            application.setRejectedAt(LocalDateTime.now());
        }

        SchemeApplication saved = applicationRepository.save(application);

        // Notify farmer
        Notification notification = new Notification();
        notification.setFarmer(application.getFarmer());
        notification.setTitle("Application Status Updated");
        notification.setMessage("Your application " + application.getApplicationNumber() + " status is now: " + status);
        notification.setType(Notification.NotificationType.APPLICATION_STATUS);
        notification.setRelatedApplicationId(saved.getId());
        notificationRepository.save(notification);

        return mapToApplicationResponse(saved);
    }

    /**
     * Get all applications (Admin)
     */
    public Page<ApplicationResponse> getAllApplications(Pageable pageable) {
        return applicationRepository.findAll(pageable)
                .map(this::mapToApplicationResponse);
    }

    private ApplicationResponse mapToApplicationResponse(SchemeApplication application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setApplicationNumber(application.getApplicationNumber());
        response.setFarmerId(application.getFarmer().getId());
        response.setFarmerName(application.getFarmer().getUser().getFullName());
        response.setSchemeId(application.getScheme().getId());
        response.setSchemeName(application.getScheme().getName());
        response.setSchemeCode(application.getScheme().getSchemeCode());
        response.setStatus(application.getStatus());
        response.setRemarks(application.getRemarks());
        response.setAdminRemarks(application.getAdminRemarks());
        response.setSubmittedAt(application.getSubmittedAt());
        response.setApprovedAt(application.getApprovedAt());
        response.setRejectedAt(application.getRejectedAt());
        response.setDisbursementAmount(application.getDisbursementAmount());
        response.setDisbursementDate(application.getDisbursementDate());
        response.setCreatedAt(application.getCreatedAt());

        List<DocumentResponse> docs = application.getDocuments().stream()
                .map(this::mapToDocumentResponse).collect(Collectors.toList());
        response.setDocuments(docs);
        return response;
    }

    private DocumentResponse mapToDocumentResponse(UploadedDocument doc) {
        DocumentResponse response = new DocumentResponse();
        response.setId(doc.getId());
        response.setDocumentType(doc.getDocumentType());
        response.setDocumentName(doc.getDocumentName());
        response.setFilePath(doc.getFilePath());
        response.setFileSize(doc.getFileSize());
        response.setFileType(doc.getFileType());
        response.setVerificationStatus(doc.getVerificationStatus());
        response.setVerificationRemarks(doc.getVerificationRemarks());
        response.setUploadedAt(doc.getUploadedAt());
        return response;
    }

    private UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

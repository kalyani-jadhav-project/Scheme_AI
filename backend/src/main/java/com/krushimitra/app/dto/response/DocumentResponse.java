package com.krushimitra.app.dto.response;

import com.krushimitra.app.entity.UploadedDocument;
import java.time.LocalDateTime;

public class DocumentResponse {
    private Long id;
    private String documentType;
    private String documentName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private UploadedDocument.VerificationStatus verificationStatus;
    private String verificationRemarks;
    private LocalDateTime uploadedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public UploadedDocument.VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(UploadedDocument.VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getVerificationRemarks() { return verificationRemarks; }
    public void setVerificationRemarks(String verificationRemarks) { this.verificationRemarks = verificationRemarks; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}

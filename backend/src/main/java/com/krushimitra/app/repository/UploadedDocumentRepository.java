package com.krushimitra.app.repository;

import com.krushimitra.app.entity.UploadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, Long> {
    List<UploadedDocument> findByApplicationId(Long applicationId);
    void deleteByApplicationId(Long applicationId);
}

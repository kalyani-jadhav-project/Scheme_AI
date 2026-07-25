package com.krushimitra.app.repository;

import com.krushimitra.app.entity.SchemeApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchemeApplicationRepository extends JpaRepository<SchemeApplication, Long> {
    Optional<SchemeApplication> findByApplicationNumber(String applicationNumber);

    Page<SchemeApplication> findByFarmerId(Long farmerId, Pageable pageable);

    List<SchemeApplication> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);

    boolean existsByFarmerIdAndSchemeId(Long farmerId, Long schemeId);

    long countByFarmerId(Long farmerId);

    long countByFarmerIdAndStatus(Long farmerId, SchemeApplication.ApplicationStatus status);

    long countByStatus(SchemeApplication.ApplicationStatus status);

    @Query("SELECT COUNT(a) FROM SchemeApplication a WHERE MONTH(a.createdAt) = MONTH(CURRENT_DATE) AND YEAR(a.createdAt) = YEAR(CURRENT_DATE)")
    long countNewApplicationsThisMonth();

    @Query("SELECT a.scheme.name, COUNT(a) FROM SchemeApplication a GROUP BY a.scheme.name ORDER BY COUNT(a) DESC")
    List<Object[]> countApplicationsByScheme();

    @Query("SELECT a FROM SchemeApplication a WHERE a.scheme.id = :schemeId")
    Page<SchemeApplication> findBySchemeId(@Param("schemeId") Long schemeId, Pageable pageable);
}

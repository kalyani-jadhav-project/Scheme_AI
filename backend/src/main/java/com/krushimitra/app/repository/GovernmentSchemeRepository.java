package com.krushimitra.app.repository;

import com.krushimitra.app.entity.GovernmentScheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, Long> {
    Optional<GovernmentScheme> findBySchemeCode(String schemeCode);

    Page<GovernmentScheme> findByActiveTrue(Pageable pageable);

    @Query("SELECT s FROM GovernmentScheme s WHERE s.active = true AND " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.ministry) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<GovernmentScheme> searchSchemes(@Param("keyword") String keyword, Pageable pageable);

    List<GovernmentScheme> findByActiveTrueAndCategory(GovernmentScheme.SchemeCategory category);

    @Query("SELECT s FROM GovernmentScheme s WHERE s.active = true AND " +
           "(s.applicableStates = 'ALL' OR s.applicableStates LIKE CONCAT('%', :state, '%'))")
    List<GovernmentScheme> findActiveSchemesByState(@Param("state") String state);

    long countByActiveTrue();

    @Query("SELECT COUNT(s) FROM GovernmentScheme s WHERE MONTH(s.createdAt) = MONTH(CURRENT_DATE) AND YEAR(s.createdAt) = YEAR(CURRENT_DATE)")
    long countNewSchemesThisMonth();
}

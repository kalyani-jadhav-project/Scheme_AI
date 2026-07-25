package com.krushimitra.app.repository;

import com.krushimitra.app.entity.Farmer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByUserId(Long userId);

    @Query("SELECT f FROM Farmer f WHERE f.state = :state")
    Page<Farmer> findByState(@Param("state") String state, Pageable pageable);

    @Query("SELECT f FROM Farmer f WHERE f.state = :state AND f.district = :district")
    Page<Farmer> findByStateAndDistrict(@Param("state") String state, @Param("district") String district, Pageable pageable);

    long countByState(String state);

    @Query("SELECT COUNT(f) FROM Farmer f WHERE MONTH(f.createdAt) = MONTH(CURRENT_DATE) AND YEAR(f.createdAt) = YEAR(CURRENT_DATE)")
    long countNewFarmersThisMonth();
}

package com.krushimitra.app.repository;

import com.krushimitra.app.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByFarmerIdOrderByCreatedAtDesc(Long farmerId);

    Page<Notification> findByFarmerIdOrGlobalTrue(Long farmerId, Pageable pageable);

    long countByFarmerIdAndReadFalse(Long farmerId);

    List<Notification> findTop5ByFarmerIdOrGlobalTrueOrderByCreatedAtDesc(Long farmerId);
}

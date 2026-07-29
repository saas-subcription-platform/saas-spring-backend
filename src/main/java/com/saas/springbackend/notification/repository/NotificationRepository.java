package com.saas.springbackend.notification.repository;

import com.saas.springbackend.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
    Optional<Notification> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
    @Transactional
    void deleteByUserId(Long userId);


    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = 'READ' WHERE n.company.id = :companyId")
    void markAllAsReadByCompanyId(@Param("companyId") Long companyId);
}

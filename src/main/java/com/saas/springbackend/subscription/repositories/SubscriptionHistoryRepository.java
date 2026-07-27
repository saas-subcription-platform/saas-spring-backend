package com.saas.springbackend.subscription.repositories;

import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory,Long> {
//    List<SubscriptionHistory> findBySubscriptionOrderByCreatedAtDesc(
//            Subscription subscription);
}

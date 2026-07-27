package com.saas.springbackend.subscription.repositories;

import com.saas.springbackend.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    /**
     * Checks whether a subscription already exists for the company.
     * @return true if subscription exists.
     */
    boolean existsByCompanyId(Long companyId);
}

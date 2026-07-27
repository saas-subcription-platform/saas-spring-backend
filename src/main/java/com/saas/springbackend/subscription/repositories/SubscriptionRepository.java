package com.saas.springbackend.subscription.repositories;

import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    /**
     * Checks whether a subscription already exists for the company.
     *
     * @param companyId Company ID.
     * @return true if subscription exists.
     */
    boolean existsByCompanyId(Long companyId);

    /**
     * Retrieves subscription by company.
     *
     * @param companyId Company ID.
     * @return Subscription.
     */
    Optional<Subscription> findByCompanyId(Long companyId);
}

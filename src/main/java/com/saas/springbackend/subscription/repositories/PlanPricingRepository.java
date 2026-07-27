package com.saas.springbackend.subscription.repositories;

import com.saas.springbackend.subscription.entity.PlanPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanPricingRepository extends JpaRepository<PlanPricing,Long> {
    /**
     * Retrieves all active pricing options for a subscription plan.
     */
    List<PlanPricing> findBySubscriptionPlanIdAndActiveTrue(Long subscriptionPlanId);

    /**
     * Retrieves all active plans options for a subscription plan.
     */
    Optional<PlanPricing> findByIdAndSubscriptionPlanIdAndActiveTrue(
            Long pricingId,
            Long subscriptionPlanId
    );
}

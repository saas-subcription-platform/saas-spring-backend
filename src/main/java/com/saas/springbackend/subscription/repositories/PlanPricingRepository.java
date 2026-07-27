package com.saas.springbackend.subscription.repositories;

import com.saas.springbackend.subscription.entity.BillingCycle;
import com.saas.springbackend.subscription.entity.PlanPricing;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanPricingRepository extends JpaRepository<PlanPricing,Long> {
    /**
     * Retrieves all active pricing options for a subscription plan.
     *
     * @param subscriptionPlanId Subscription Plan ID.
     * @return List of pricing options.
     */
    List<PlanPricing> findBySubscriptionPlanIdAndActiveTrue(Long subscriptionPlanId);
    Optional<PlanPricing> findByIdAndSubscriptionPlanIdAndActiveTrue(
            Long pricingId,
            Long subscriptionPlanId
    );
}

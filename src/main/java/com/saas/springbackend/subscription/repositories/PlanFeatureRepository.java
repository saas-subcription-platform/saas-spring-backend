package com.saas.springbackend.subscription.repositories;

import com.saas.springbackend.subscription.entity.PlanFeature;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanFeatureRepository extends JpaRepository<PlanFeature,Long> {
    /**
     * Retrieves all features belonging to a subscription plan.
     *
     * @param subscriptionPlanId Subscription Plan ID.
     * @return List of plan features.
     */
    List<PlanFeature> findBySubscriptionPlanId(Long subscriptionPlanId);
}

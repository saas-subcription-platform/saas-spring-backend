package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.PlanFeatureResponseDTO;

import java.util.List;

public interface PlanFeatureService {
    /**
     * Retrieves all features for a subscription plan.
     *
     * @param planId Subscription Plan ID.
     * @return List of features.
     */
    List<PlanFeatureResponseDTO> getFeaturesByPlan(Long planId);
}

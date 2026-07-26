package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.PlanPricingResponseDTO;

import java.util.List;

public interface PlanPricingService {
    /**
     * Retrieves all active pricing options for a subscription plan.
     *
     * @param planId Subscription Plan ID.
     * @return List of pricing options.
     */
    List<PlanPricingResponseDTO> getPricingByPlan(Long planId);
}

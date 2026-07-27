package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.PlanPricingResponseDTO;

import java.util.List;

public interface PlanPricingService {
    /**
     * Retrieves all active pricing options for a subscription plan.
     * @return List of pricing options.
     */
    List<PlanPricingResponseDTO> getPricingByPlan(Long planId);
}

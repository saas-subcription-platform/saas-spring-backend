package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.PlanPricingResponseDTO;
import com.saas.springbackend.subscription.entity.PlanPricing;
import com.saas.springbackend.subscription.repositories.PlanPricingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanPricingServiceImpl implements PlanPricingService{
    private final PlanPricingRepository planPricingRepository;

    /**
     * Retrieves all active pricing options for a subscription plan.
     *
     * @param planId Subscription Plan ID.
     * @return List of pricing options.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlanPricingResponseDTO> getPricingByPlan(Long planId) {

        return planPricingRepository
                .findBySubscriptionPlanIdAndActiveTrue(planId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Converts PlanPricing entity into PlanPricingResponseDTO.
     *
     * @param pricing PlanPricing entity.
     * @return PlanPricingResponseDTO.
     */
    private PlanPricingResponseDTO mapToResponse(PlanPricing pricing) {

        PlanPricingResponseDTO response = new PlanPricingResponseDTO();

        response.setId(pricing.getId());
        response.setBillingCycle(pricing.getBillingCycle());
        response.setPrice(pricing.getPrice());
        response.setActive(pricing.isActive());

        return response;
    }
}

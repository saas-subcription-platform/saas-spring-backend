package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.PlanPricingResponseDTO;
import com.saas.springbackend.subscription.mapper.SubscriptionMapper;
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
    private final SubscriptionMapper subscriptionMapper;

    /**
     * Retrieves all active pricing options for a subscription plan.
     * @return List of pricing options.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlanPricingResponseDTO> getPricingByPlan(Long planId) {

        return planPricingRepository
                .findBySubscriptionPlanIdAndActiveTrue(planId)
                .stream()
                .map(subscriptionMapper::toPlanPricingResponse)
                .toList();
    }
}

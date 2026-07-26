package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.PlanFeatureResponseDTO;
import com.saas.springbackend.subscription.entity.PlanFeature;
import com.saas.springbackend.subscription.repositories.PlanFeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanFeatureServiceImpl implements PlanFeatureService{

    private final PlanFeatureRepository planFeatureRepository;

    /**
     * Retrieves all enabled features associated with a subscription plan.
     *
     * @param planId Subscription Plan ID.
     * @return List of plan features.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlanFeatureResponseDTO> getFeaturesByPlan(Long planId) {

        return planFeatureRepository.findBySubscriptionPlanId(planId)
                .stream()
                .filter(PlanFeature::isEnabled)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Converts PlanFeature entity into PlanFeatureResponseDTO.
     *
     * @param feature PlanFeature entity.
     * @return PlanFeatureResponseDTO.
     */
    private PlanFeatureResponseDTO mapToResponse(PlanFeature feature) {

        PlanFeatureResponseDTO response = new PlanFeatureResponseDTO();

        response.setId(feature.getId());
        response.setFeatureName(feature.getFeatureName());
        response.setFeatureDescription(feature.getFeatureDescription());

        return response;
    }
}

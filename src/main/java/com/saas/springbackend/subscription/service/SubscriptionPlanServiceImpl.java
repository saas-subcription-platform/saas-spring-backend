package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.ResourceNotFoundException;
import com.saas.springbackend.subscription.dtos.response.SubscriptionPlanResponseDTO;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import com.saas.springbackend.subscription.repositories.SubscriptionPlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionPlanServiceImpl implements  SubscriptionPlanService{
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * Retrieves a subscription plan using its ID.
     *
     * @param id Subscription Plan ID.
     * @return SubscriptionPlanResponse containing plan details.
     * @throws ResourceNotFoundException if the plan does not exist.
     */
    @Override
    public SubscriptionPlanResponseDTO getPlanById(Long id) {

        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription Plan not found with id : " + id));

        return mapToResponse(plan);
    }

    /**
     * Retrieves all subscription plans from the database.
     *
     * @return List of all subscription plans.
     */
    @Override
    public List<SubscriptionPlanResponseDTO> getAllPlans() {

        return subscriptionPlanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    /**
     * Converts SubscriptionPlan entity into SubscriptionPlanResponse DTO.
     *
     * This method prevents exposing JPA entities directly
     * to the client.
     *
     * @param plan SubscriptionPlan entity.
     * @return SubscriptionPlanResponse DTO.
     */
    private SubscriptionPlanResponseDTO mapToResponse(
            SubscriptionPlan plan) {

        SubscriptionPlanResponseDTO response =
                new SubscriptionPlanResponseDTO();

        response.setId(plan.getId());
        response.setPlanName(plan.getPlanName());
        response.setPlanDescription(plan.getPlanDescription());
        response.setMaximumUsers(plan.getMaximumUsers());
        response.setActive(plan.isActive());

        return response;
    }
}

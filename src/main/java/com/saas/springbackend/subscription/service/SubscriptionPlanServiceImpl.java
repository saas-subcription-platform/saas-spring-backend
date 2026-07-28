package com.saas.springbackend.subscription.service;

import com.saas.springbackend.common.exception.ResourceNotFoundException;
import com.saas.springbackend.subscription.dtos.response.SubscriptionPlanResponseDTO;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import com.saas.springbackend.subscription.mapper.SubscriptionMapper;
import com.saas.springbackend.subscription.repositories.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionPlanServiceImpl implements  SubscriptionPlanService{
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionMapper subscriptionMapper;

    /**
     * Retrieves a subscription plan using its ID.
     * @return SubscriptionPlanResponse containing plan details.
     * @throws ResourceNotFoundException if the plan does not exist.
     */
    @Override
    public SubscriptionPlanResponseDTO getPlanById(Long id) {

        SubscriptionPlan plan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription Plan not found with id : " + id));

        return subscriptionMapper.toSubscriptionPlanResponse(plan);
    }

    /**
     * Retrieves all subscription plans from the database.
     * @return List of all subscription plans.
     */
    @Override
    public List<SubscriptionPlanResponseDTO> getAllPlans() {

        return subscriptionPlanRepository.findAll()
                .stream()
                .map(subscriptionMapper::toSubscriptionPlanResponse)
                .toList();
    }


}

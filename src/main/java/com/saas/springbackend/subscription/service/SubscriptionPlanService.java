package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.response.SubscriptionPlanResponseDTO;

import java.util.List;

public interface SubscriptionPlanService {

    SubscriptionPlanResponseDTO getPlanById(Long planId);

    List<SubscriptionPlanResponseDTO> getAllPlans();

}

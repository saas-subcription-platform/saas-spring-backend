package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.request.SubscribeRequestDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;

public interface SubscriptionService {
    /**
     * Creates a new subscription for a company.
     *
     * @param request Checkout request.
     * @return Subscription details.
     */
    SubscriptionResponseDTO subscribe(SubscribeRequestDTO request);

    /**
     * Retrieves subscription details.
     *
     * @param subscriptionId Subscription ID.
     * @return Subscription details.
     */
    SubscriptionResponseDTO getSubscription(Long subscriptionId);

    SubscriptionResponseDTO renewSubscription(Long subscriptionId);

    SubscriptionResponseDTO cancelSubscription(Long subscriptionId);
}

package com.saas.springbackend.subscription.service;

import com.saas.springbackend.subscription.dtos.request.ChangePlanRequestDTO;
import com.saas.springbackend.subscription.dtos.request.RenewSubscriptionRequestDTO;
import com.saas.springbackend.subscription.dtos.request.SubscribeRequestDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionDetailsResponseDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;

public interface SubscriptionService {
    /**
     * Creates a new subscription for a company.
     * @return Subscription details.
     */
    SubscriptionResponseDTO subscribe(SubscribeRequestDTO request);

    /**
     * Retrieves subscription details.
     * @return Subscription details.
     */
    SubscriptionResponseDTO getSubscription(Long subscriptionId);

    /**
     * Retrieves subscription details.
     * renews subscription and save in entity.
     * @return Subscription details.
     */
    SubscriptionResponseDTO renewSubscription(
            Long subscriptionId,
            RenewSubscriptionRequestDTO request
    );

    /**
     * Cancel subscription details.
     * Marked as Canceled.
     * @return Subscription details.
     */
    SubscriptionResponseDTO cancelSubscription(Long subscriptionId);

    /**
     * Retrieves subscription details.
     * Upgrades or Degrades the subscription plan.
     * @return Subscription details.
     */
    SubscriptionResponseDTO changePlan(
            Long subscriptionId,
            ChangePlanRequestDTO request
    );

    SubscriptionDetailsResponseDTO getMySubscription();
}

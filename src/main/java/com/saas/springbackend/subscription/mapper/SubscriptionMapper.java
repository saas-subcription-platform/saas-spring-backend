package com.saas.springbackend.subscription.mapper;

import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;
import com.saas.springbackend.subscription.entity.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    /**
     * Converts Subscription entity into SubscriptionResponseDTO.
     *
     * @param subscription Subscription entity.
     * @return SubscriptionResponseDTO.
     */
    public SubscriptionResponseDTO toResponse(
            Subscription subscription) {

        return SubscriptionResponseDTO.builder()
                .subscriptionId(subscription.getId())
                .companyName(subscription.getCompany().getCompanyName())
                .planName(subscription.getSubscriptionPlan().getPlanName())
                .billingCycle(
                        subscription.getPlanPricing()
                                .getBillingCycle()
                                .name()
                )
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .build();
    }
}

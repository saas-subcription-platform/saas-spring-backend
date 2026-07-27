package com.saas.springbackend.subscription.mapper;

import com.saas.springbackend.subscription.dtos.response.PlanFeatureResponseDTO;
import com.saas.springbackend.subscription.dtos.response.PlanPricingResponseDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionPlanResponseDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;
import com.saas.springbackend.subscription.entity.PlanFeature;
import com.saas.springbackend.subscription.entity.PlanPricing;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    /**
     * Converts Subscription entity into SubscriptionResponseDTO.
     * @return SubscriptionResponseDTO.
     */
    public SubscriptionResponseDTO toResponse(Subscription subscription) {

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

    /**
     * Converts PlanFeature entity into PlanFeatureResponseDTO.
     * @return PlanFeatureResponseDTO.
     */
    public PlanFeatureResponseDTO toPlanFeatureResponse(PlanFeature feature) {

        if (feature == null) {
            return null;
        }

        PlanFeatureResponseDTO response = new PlanFeatureResponseDTO();

        response.setId(feature.getId());
        response.setFeatureName(feature.getFeatureName());
        response.setFeatureDescription(feature.getFeatureDescription());

        return response;
    }

    /**
     * Converts PlanPricing entity into PlanPricingResponseDTO.
     * @return PlanPricingResponseDTO.
     */
    public PlanPricingResponseDTO toPlanPricingResponse(PlanPricing pricing) {

        if (pricing == null) {
            return null;
        }

        PlanPricingResponseDTO response = new PlanPricingResponseDTO();

        response.setId(pricing.getId());
        response.setBillingCycle(pricing.getBillingCycle());
        response.setPrice(pricing.getPrice());
        response.setActive(pricing.isActive());

        return response;
    }

    /**
     * Converts SubscriptionPlan entity into SubscriptionPlanResponse DTO.
     * This method prevents exposing JPA entities directly
     * to the client.
     * @return SubscriptionPlanResponse DTO.
     */
    public SubscriptionPlanResponseDTO toSubscriptionPlanResponse(
            SubscriptionPlan plan) {

        if (plan == null) {
            return null;
        }

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

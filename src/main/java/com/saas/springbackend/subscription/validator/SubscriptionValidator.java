package com.saas.springbackend.subscription.validator;

import com.saas.springbackend.common.exception.InvalidOperationException;
import com.saas.springbackend.common.exception.InvalidRequestException;
import com.saas.springbackend.common.exception.ResourceNotFoundException;
import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.subscription.entity.PlanPricing;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import com.saas.springbackend.subscription.entity.SubscriptionStatus;
import com.saas.springbackend.subscription.repositories.PlanPricingRepository;
import com.saas.springbackend.subscription.repositories.SubscriptionPlanRepository;
import com.saas.springbackend.subscription.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionValidator {
    private final CompanyRepository companyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PlanPricingRepository planPricingRepository;

    /**
     * Validates whether the company exists.
     */
    public Company validateCompany(Long companyId) {

        return companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID : " + companyId
                        ));
    }

    /**
     * Validates whether company already has a subscription.
     */
    public void validateSubscription(Long companyId) {

        if (subscriptionRepository.existsByCompanyId(companyId)) {

            throw new InvalidOperationException(
                    "Company already has an active subscription."
            );
        }
    }

    /**
     * Validates subscription plan.
     */
    public SubscriptionPlan validatePlan(Long planId) {

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription plan not found with ID : "
                                        + planId
                        ));

        if (!plan.isActive()) {

            throw new InvalidOperationException(
                    "Selected subscription plan is inactive."
            );
        }

        return plan;
    }

    /**
     * Validates pricing.
     */
    public PlanPricing validatePricing(
            Long pricingId,
            Long planId) {

        return planPricingRepository
                .findByIdAndSubscriptionPlanIdAndActiveTrue(
                        pricingId,
                        planId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pricing option not found for the selected subscription plan."
                        ));
    }

    public Subscription validateSubscriptionById(Long subscriptionId) {

        return subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription not found with ID : " + subscriptionId
                        )
                );
    }

    public void validateCancellation(Subscription subscription) {

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new InvalidOperationException(
                    "Subscription is already cancelled."
            );
        }
    }

    public void validatePlanChange(
            Subscription currentSubscription,
            SubscriptionPlan newPlan) {

        if (currentSubscription.getSubscriptionPlan()
                .getId()
                .equals(newPlan.getId())) {

            throw new InvalidRequestException(
                    "Subscription is already using the selected plan."
            );
        }
    }

}

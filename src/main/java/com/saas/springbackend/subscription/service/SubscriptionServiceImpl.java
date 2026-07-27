package com.saas.springbackend.subscription.service;

import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.subscription.ResourceNotFoundException;
import com.saas.springbackend.subscription.dtos.request.SubscribeRequestDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;
import com.saas.springbackend.subscription.entity.*;
import com.saas.springbackend.subscription.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;
    private final CompanyRepository companyRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PlanPricingRepository planPricingRepository;

    @Override
    public SubscriptionResponseDTO subscribe(SubscribeRequestDTO request) {

        System.out.println("Step 1");

        Company company = validateCompany(request.getCompanyId());

        System.out.println("Step 2");

        validateSubscription(company.getId());

        System.out.println("Step 3");

        SubscriptionPlan plan = validatePlan(request.getPlanId());

        System.out.println("Step 4");

        PlanPricing pricing = validatePricing(
                request.getPricingId(),
                request.getPlanId()
        );

        System.out.println("Step 5");

        Subscription subscription = createSubscription(
                company,
                plan,
                pricing
        );

        System.out.println("Step 6");

        return mapToResponse(subscription);
    }
    /**
     * Validates whether the company exists.
     *
     * @param companyId Company ID.
     * @return Company entity.
     * @throws ResourceNotFoundException if company is not found.
     */
    private Company validateCompany(Long companyId) {

        return companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID : " + companyId
                        )
                );
    }

    /**
     * Validates whether the company already has a subscription.
     *
     * @param companyId Company ID.
     * @throws IllegalStateException if subscription already exists.
     */
    private void validateSubscription(Long companyId) {

        if (subscriptionRepository.existsByCompanyId(companyId)) {
            throw new IllegalStateException(
                    "Company already has an active subscription."
            );
        }
    }

    /**
     * Validates whether the subscription plan exists and is active.
     *
     * @param planId Subscription Plan ID.
     * @return SubscriptionPlan entity.
     * @throws ResourceNotFoundException if plan is not found.
     * @throws IllegalStateException if plan is inactive.
     */
    private SubscriptionPlan validatePlan(Long planId) {

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription plan not found with ID : " + planId
                        )
                );

        if (!plan.isActive()) {
            throw new IllegalStateException(
                    "Selected subscription plan is inactive."
            );
        }

        return plan;
    }

    /**
     * Validates whether the selected pricing belongs to the selected plan
     * and is currently active.
     *
     * @param pricingId Pricing ID.
     * @param planId Subscription Plan ID.
     * @return PlanPricing entity.
     * @throws ResourceNotFoundException if pricing is invalid.
     */
    private PlanPricing validatePricing(Long pricingId, Long planId) {

        return planPricingRepository
                .findByIdAndSubscriptionPlanIdAndActiveTrue(
                        pricingId,
                        planId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pricing option not found for the selected subscription plan."
                        )
                );
    }

    /**
     * Creates a new subscription.
     *
     * @param company Company entity.
     * @param plan Subscription plan.
     * @param pricing Selected pricing.
     * @return Saved Subscription.
     */
    private Subscription createSubscription(
            Company company,
            SubscriptionPlan plan,
            PlanPricing pricing) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(
                startDate,
                pricing.getBillingCycle()
        );

        Subscription subscription = Subscription.builder()
                .company(company)
                .subscriptionPlan(plan)
                .planPricing(pricing)
                .amount(pricing.getPrice())
                .startDate(startDate)
                .endDate(endDate)
                .renewalDate(endDate)
                .status(SubscriptionStatus.ACTIVE)
                .autoRenew(true)
                .build();

        return subscriptionRepository.save(subscription);
    }

    /**
     * Calculates subscription end date based on billing cycle.
     *
     * @param startDate Subscription start date.
     * @param billingCycle Billing cycle.
     * @return Subscription end date.
     */
    private LocalDate calculateEndDate(
            LocalDate startDate,
            BillingCycle billingCycle) {

        return switch (billingCycle) {

            case MONTHLY -> startDate.plusMonths(1);

            case YEARLY -> startDate.plusYears(1);
        };
    }

    /**
     * Converts Subscription entity to SubscriptionResponseDTO.
     *
     * @param subscription Subscription entity.
     * @return SubscriptionResponseDTO.
     */
    private SubscriptionResponseDTO mapToResponse(
            Subscription subscription) {

        return SubscriptionResponseDTO.builder()
                .subscriptionId(subscription.getId())
                .companyName(subscription.getCompany().getCompanyName())
                .planName(subscription.getSubscriptionPlan().getPlanName())
                .billingCycle(subscription.getPlanPricing()
                        .getBillingCycle()
                        .name())
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .build();
    }

    /**
     * Retrieves subscription details.
     *
     * @param subscriptionId Subscription ID.
     * @return SubscriptionResponseDTO.
     */
    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponseDTO getSubscription(Long subscriptionId) {

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription not found with ID : " + subscriptionId
                        )
                );

        return mapToResponse(subscription);
    }
}

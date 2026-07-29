package com.saas.springbackend.subscription.service;

import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.common.exception.ResourceNotFoundException;
import com.saas.springbackend.subscription.dtos.request.ChangePlanRequestDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionDetailsResponseDTO;
import com.saas.springbackend.subscription.factory.SubscriptionFactory;
import com.saas.springbackend.subscription.validator.SubscriptionValidator;
import com.saas.springbackend.subscription.dtos.request.SubscribeRequestDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;
import com.saas.springbackend.subscription.entity.*;
import com.saas.springbackend.subscription.mapper.SubscriptionMapper;
import com.saas.springbackend.subscription.repositories.*;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionHistoryService subscriptionHistoryService;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionValidator subscriptionValidator;
    private final SubscriptionFactory subscriptionFactory;
    private final UserRepository userRepository;

    @Override
    public SubscriptionResponseDTO subscribe(SubscribeRequestDTO request) {

        Company company =
                subscriptionValidator.validateCompany(
                        request.getCompanyId()
                );

        subscriptionValidator.validateSubscription(
                company.getId()
        );

        SubscriptionPlan plan =
                subscriptionValidator.validatePlan(
                        request.getPlanId()
                );

        PlanPricing pricing =
                subscriptionValidator.validatePricing(
                        request.getPricingId(),
                        request.getPlanId()
                );

        Subscription subscription =
                subscriptionFactory.createSubscription(
                        company,
                        plan,
                        pricing
                );

        subscription = subscriptionRepository.save(subscription);

        subscriptionHistoryService
                .recordSubscriptionCreated(subscription);

        return subscriptionMapper.toResponse(subscription);
    }

    /**
     * Retrieves subscription details.
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

        return subscriptionMapper.toResponse(subscription);
    }

    /**
     * Renews Subscription
     * updates the endDate of subscription plan.
     * */
    @Override
    public SubscriptionResponseDTO renewSubscription(Long subscriptionId) {

        Subscription subscription =
                subscriptionValidator.validateSubscriptionById(subscriptionId);

        LocalDate newEndDate =
                subscriptionFactory.calculateEndDate(
                        subscription.getEndDate(),
                        subscription.getPlanPricing().getBillingCycle()
                );

        subscription.setEndDate(newEndDate);
        subscription.setRenewalDate(newEndDate);

        System.out.println("Old End Date : " + subscription.getEndDate());
        System.out.println("New End Date : " + newEndDate);
        System.out.println("Billing Cycle : " + subscription.getPlanPricing().getBillingCycle());

        Subscription saved = subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordSubscriptionRenewed(saved);

        return subscriptionMapper.toResponse(saved);
    }

    /**
     * Cancel Subscription
     * update the action canceled and save in history.
     * */
    @Override
    public SubscriptionResponseDTO cancelSubscription(Long subscriptionId) {

        Subscription subscription =
                subscriptionValidator.validateSubscriptionById(subscriptionId);

        subscriptionValidator.validateCancellation(subscription);

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);

        Subscription saved =
                subscriptionRepository.save(subscription);

        subscriptionHistoryService
                .recordSubscriptionCancelled(saved);

        return subscriptionMapper.toResponse(saved);
    }

    /**
     * Upgrade/Degrade Subscription
     * update the action Upgraded/degraded and save in history.
     * */
    @Override
    public SubscriptionResponseDTO changePlan(
            Long subscriptionId,
            ChangePlanRequestDTO request) {

        Subscription subscription =
                subscriptionValidator.validateSubscriptionById(subscriptionId);

        // Store current plan before updating
        SubscriptionPlan oldPlan = subscription.getSubscriptionPlan();

        // Validate new plan
        SubscriptionPlan newPlan =
                subscriptionValidator.validatePlan(request.getPlanId());

        // Validate pricing
        PlanPricing newPricing =
                subscriptionValidator.validatePricing(
                        request.getPricingId(),
                        request.getPlanId()
                );

        // Validate plan change
        subscriptionValidator.validatePlanChange(subscription, newPlan);

        // Update subscription
        subscription.setSubscriptionPlan(newPlan);
        subscription.setPlanPricing(newPricing);
        subscription.setAmount(newPricing.getPrice());

        // Save updated subscription
        Subscription saved = subscriptionRepository.save(subscription);

        // Record history
        if (newPlan.getMaximumUsers() > oldPlan.getMaximumUsers()) {

            subscriptionHistoryService.recordSubscriptionUpgraded(
                    saved,
                    oldPlan.getPlanName(),
                    newPlan.getPlanName()
            );

        } else {

            subscriptionHistoryService.recordSubscriptionUpgraded(
                    saved,
                    oldPlan.getPlanName(),
                    newPlan.getPlanName()
            );

        }

        return subscriptionMapper.toResponse(saved);
    }

    @Override
    public SubscriptionDetailsResponseDTO getMySubscription() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        System.out.println("Logged In Email = " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Company company = user.getCompany();

        Subscription subscription = subscriptionRepository
                .findByCompanyId(company.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subscription not found"));

        return SubscriptionDetailsResponseDTO.builder()
                .subscriptionId(subscription.getId())
                .companyName(company.getCompanyName())
                .adminName(user.getFirstName() + " " + user.getLastName())
                .planName(subscription.getSubscriptionPlan().getPlanName())
                .maximumUsers(subscription.getSubscriptionPlan().getMaximumUsers())
                .billingCycle(subscription.getPlanPricing().getBillingCycle())
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .renewalDate(subscription.getRenewalDate())
                .status(subscription.getStatus().name())
                .autoRenew(subscription.isAutoRenew())
                .features(
                        subscription.getSubscriptionPlan()
                                .getPlanFeatures()
                                .stream()
                                .filter(PlanFeature::isEnabled)
                                .map(PlanFeature::getFeatureName)
                                .toList()
                )
                .build();
    }
}

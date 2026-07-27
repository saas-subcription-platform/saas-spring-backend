package com.saas.springbackend.subscription.service;

import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.subscription.ResourceNotFoundException;
import com.saas.springbackend.subscription.factory.SubscriptionFactory;
import com.saas.springbackend.subscription.validator.SubscriptionValidator;
import com.saas.springbackend.subscription.dtos.request.SubscribeRequestDTO;
import com.saas.springbackend.subscription.dtos.response.SubscriptionResponseDTO;
import com.saas.springbackend.subscription.entity.*;
import com.saas.springbackend.subscription.mapper.SubscriptionMapper;
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
    private final SubscriptionHistoryService subscriptionHistoryService;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionValidator subscriptionValidator;
    private final SubscriptionFactory subscriptionFactory;

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

        return subscriptionMapper.toResponse(subscription);
    }

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

        Subscription saved = subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordSubscriptionRenewed(saved);

        return subscriptionMapper.toResponse(saved);
    }

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
}

package com.saas.springbackend.subscription.factory;

import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.subscription.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriptionFactory {
    //subscription creation
    public Subscription createSubscription(
            Company company,
            SubscriptionPlan plan,
            PlanPricing pricing) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(
                startDate,
                pricing.getBillingCycle()
        );

        return Subscription.builder()
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
    }
//Calculating EndDate for renew subscription
    public LocalDate calculateEndDate(
            LocalDate startDate,
            BillingCycle billingCycle) {

        return switch (billingCycle) {
            case MONTHLY -> startDate.plusMonths(1);
            case YEARLY -> startDate.plusYears(1);
        };
    }
}

package com.saas.springbackend.security.config;

import com.saas.springbackend.subscription.entity.BillingCycle;
import com.saas.springbackend.subscription.entity.PlanFeature;
import com.saas.springbackend.subscription.entity.PlanPricing;
import com.saas.springbackend.subscription.entity.SubscriptionPlan;
import com.saas.springbackend.subscription.repositories.PlanFeatureRepository;
import com.saas.springbackend.subscription.repositories.PlanPricingRepository;
import com.saas.springbackend.subscription.repositories.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Transactional
public class SubscriptionDataSeeder implements CommandLineRunner {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PlanPricingRepository planPricingRepository;
    private final PlanFeatureRepository planFeatureRepository;

    @Override
    public void run(String... args) throws Exception {

        if (subscriptionPlanRepository.count() > 0) {
            return;
        }

        createStarterPlan();
        createProfessionalPlan();
        createEnterprisePlan();

        System.out.println("=====================================");
        System.out.println("Subscription Master Data Seeded");
        System.out.println("=====================================");
    }

    private void createStarterPlan() {

        SubscriptionPlan starter = SubscriptionPlan.builder()
                .planName("Starter")
                .planDescription("Perfect for startups and small teams.")
                .maximumUsers(10)
                .active(true)
                .build();

        starter = subscriptionPlanRepository.save(starter);

        savePricing(starter, BillingCycle.MONTHLY, new BigDecimal("999"));
        savePricing(starter, BillingCycle.YEARLY, new BigDecimal("9999"));

        saveFeature(starter, "Timesheet", "Timesheet Management");
        saveFeature(starter, "Leave Management", "Employee Leave Management");
    }

    private void createProfessionalPlan() {

        SubscriptionPlan professional = SubscriptionPlan.builder()
                .planName("Professional")
                .planDescription("Ideal for growing organizations with collaboration features.")
                .maximumUsers(50)
                .active(true)
                .build();

        professional = subscriptionPlanRepository.save(professional);

        savePricing(professional, BillingCycle.MONTHLY, new BigDecimal("2999"));
        savePricing(professional, BillingCycle.YEARLY, new BigDecimal("29999"));

        saveFeature(professional, "Timesheet", "Timesheet Management");
        saveFeature(professional, "Leave Management", "Employee Leave Management");
        saveFeature(professional, "Team Collaboration", "Internal Team Collaboration");
    }

    private void createEnterprisePlan() {

        SubscriptionPlan enterprise = SubscriptionPlan.builder()
                .planName("Enterprise")
                .planDescription("Best suited for large organizations with complete access.")
                .maximumUsers(500)
                .active(true)
                .build();

        enterprise = subscriptionPlanRepository.save(enterprise);

        savePricing(enterprise, BillingCycle.MONTHLY, new BigDecimal("9999"));
        savePricing(enterprise, BillingCycle.YEARLY, new BigDecimal("99999"));

        saveFeature(enterprise, "Timesheet", "Timesheet Management");
        saveFeature(enterprise, "Leave Management", "Employee Leave Management");
        saveFeature(enterprise, "Team Collaboration", "Internal Team Collaboration");
        saveFeature(enterprise, "Goals & OKRs", "Goals and OKR Management");
    }

    private void savePricing(
            SubscriptionPlan plan,
            BillingCycle billingCycle,
            BigDecimal price
    ) {

        PlanPricing pricing = PlanPricing.builder()
                .subscriptionPlan(plan)
                .billingCycle(billingCycle)
                .price(price)
                .active(true)
                .build();

        planPricingRepository.save(pricing);
    }

    private void saveFeature(
            SubscriptionPlan plan,
            String featureName,
            String description
    ) {

        PlanFeature feature = PlanFeature.builder()
                .subscriptionPlan(plan)
                .featureName(featureName)
                .featureDescription(description)
                .enabled(true)
                .build();

        planFeatureRepository.save(feature);
    }
}
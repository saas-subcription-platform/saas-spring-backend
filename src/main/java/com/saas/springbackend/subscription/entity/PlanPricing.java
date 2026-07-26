package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "plan_pricing")
@SuperBuilder
@AttributeOverride(name = "id",column = @Column(name = "plan_pricing_id"))
public class PlanPricing extends BaseClass {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Builder.Default
    private boolean active = true;
}

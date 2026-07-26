package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "id",column = @Column(name = "sub_plan_id"))
public class SubscriptionPlan extends BaseClass {
    private String planName;
    private BigDecimal price;
    private BillingCycle billingCycle;
    private boolean active;
    private Set<PlanFeature> features = new HashSet<>();

    public void addFeature(PlanFeature feature){
        features.add(feature);
        feature.setPlan(this);
    }
}

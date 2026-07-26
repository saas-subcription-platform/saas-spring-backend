package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Plan name is required")
    @Column(name = "plan_name", nullable = false, unique = true, length = 100)
    private String planName;

    @Column(name = "plan_description", length = 500)
    private String planDescription;

    @NotNull(message = "Maximum users is required")
    @Min(value = 1, message = "Maximum users must be at least 1")
    @Column(name = "maximum_users", nullable = false)
    private Integer maximumUsers;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(
            mappedBy = "subscriptionPlan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<PlanPricing> pricingOptions = new HashSet<>();

    @OneToMany(
            mappedBy = "subscriptionPlan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<PlanFeature> planFeatures = new HashSet<>();

    @OneToMany(
            mappedBy = "subscriptionPlan",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<Subscription> subscriptions = new HashSet<>();

    // Convenience Methods

    public void addFeature(PlanFeature feature) {
        planFeatures.add(feature);
        feature.setSubscriptionPlan(this);
    }

    public void removeFeature(PlanFeature feature) {
        planFeatures.remove(feature);
        feature.setSubscriptionPlan(null);
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setSubscriptionPlan(this);
    }

    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
        subscription.setSubscriptionPlan(null);
    }
}

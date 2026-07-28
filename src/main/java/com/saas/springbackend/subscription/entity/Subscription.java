package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;
import com.saas.springbackend.company.entity.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@AttributeOverride(name = "id",column = @Column(name = "subscription_id"))
public class Subscription extends BaseClass {
    @NotNull(message = "Company is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;

    @NotNull(message = "Subscription Plan is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricing_id")
    private PlanPricing planPricing;

    @DecimalMin(value = "0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;

    private LocalDate renewalDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Builder.Default
    @Column(nullable = false)
    private boolean autoRenew = true;

    @OneToMany(
            mappedBy = "subscription",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<SubscriptionHistory> subscriptionHistory = new ArrayList<>();

    public void addHistory(SubscriptionHistory history) {
        subscriptionHistory.add(history);
        history.setSubscription(this);
    }
    public void removeHistory(SubscriptionHistory history) {
        subscriptionHistory.remove(history);
        history.setSubscription(null);
    }

}

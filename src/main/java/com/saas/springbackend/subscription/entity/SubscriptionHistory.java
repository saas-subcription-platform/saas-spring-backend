package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscription_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@AttributeOverride(name = "id",column = @Column(name = "history_id"))
public class SubscriptionHistory extends BaseClass {
    @NotNull(message = "Subscription is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @NotNull(message = "Action is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionAction action;

    @Column(name = "old_plan", length = 100)
    private String oldPlan;

    @Column(name = "new_plan", length = 100)
    private String newPlan;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(length = 500)
    private String remarks;
}

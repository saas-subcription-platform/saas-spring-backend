package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@AttributeOverride(name = "id",column = @Column(name = "plan_feature_id"))
public class PlanFeature extends BaseClass {

    @NotBlank(message = "Feature name is required")
    @Column(name = "feature_name", nullable = false, length = 100)
    private String featureName;

    @Column(name = "feature_description", length = 500)
    private String featureDescription;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;
}

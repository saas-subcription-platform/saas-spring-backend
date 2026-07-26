package com.saas.springbackend.subscription.entity;

import com.saas.springbackend.common.entity.BaseClass;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String featureKey;
    @Column(nullable = false)
    private String featureValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id" , nullable = false)
    private SubscriptionPlan plan;
}

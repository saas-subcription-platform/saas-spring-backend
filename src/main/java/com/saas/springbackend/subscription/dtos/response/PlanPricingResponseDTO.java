package com.saas.springbackend.subscription.dtos.response;

import com.saas.springbackend.subscription.entity.BillingCycle;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanPricingResponseDTO {
    private Long id;
    private BillingCycle billingCycle;
    private BigDecimal price;
    private boolean active;
}

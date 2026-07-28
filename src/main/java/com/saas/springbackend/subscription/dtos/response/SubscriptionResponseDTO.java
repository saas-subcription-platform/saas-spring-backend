package com.saas.springbackend.subscription.dtos.response;

import com.saas.springbackend.subscription.entity.SubscriptionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDTO {

    private Long subscriptionId;

    private String companyName;

    private String planName;

    private String billingCycle;

    private BigDecimal amount;

    private LocalDate startDate;

    private LocalDate endDate;

    private SubscriptionStatus status;
}

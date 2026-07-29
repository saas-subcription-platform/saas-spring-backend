package com.saas.springbackend.subscription.dtos.response;

import com.saas.springbackend.subscription.entity.BillingCycle;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDetailsResponseDTO {

    private Long subscriptionId;

    private String companyName;

    private String adminName;

    private String planName;

    private Integer maximumUsers;

    private BigDecimal amount;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate renewalDate;

    private String status;

    private Boolean autoRenew;

    private BillingCycle billingCycle;

    private List<String> features;
}
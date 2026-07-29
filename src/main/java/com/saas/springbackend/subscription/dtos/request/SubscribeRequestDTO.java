package com.saas.springbackend.subscription.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeRequestDTO {
    @NotNull(message = "Company ID is required.")
    private Long companyId;

    @NotNull(message = "Subscription Plan ID is required.")
    private Long planId;

    @NotNull(message = "Pricing ID is required.")
    private Long pricingId;

    @NotNull(message = "Payment ID is required.")
    private Long paymentId;
}

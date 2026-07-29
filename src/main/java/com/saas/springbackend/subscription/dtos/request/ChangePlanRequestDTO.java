package com.saas.springbackend.subscription.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePlanRequestDTO {
    @NotNull(message = "Plan ID is required")
    private Long planId;

    @NotNull(message = "Pricing ID is required")
    private Long pricingId;

    @NotNull(message = "Payment ID is required")
    private Long paymentId;
}

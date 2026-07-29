package com.saas.springbackend.subscription.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RenewSubscriptionRequestDTO {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;
}
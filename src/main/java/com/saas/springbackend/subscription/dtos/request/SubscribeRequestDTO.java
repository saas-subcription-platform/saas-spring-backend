package com.saas.springbackend.subscription.dtos.request;

import com.saas.springbackend.company.entity.CompanySize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
}

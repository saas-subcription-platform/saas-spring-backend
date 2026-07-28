package com.saas.springbackend.invoice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateInvoiceRequestDto {

    @NotNull(message = "Payment Id is required")
    @Positive(message = "Payment Id must be positive")
    private Long paymentId;
}

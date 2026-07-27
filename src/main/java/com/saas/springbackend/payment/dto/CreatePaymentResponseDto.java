package com.saas.springbackend.payment.dto;

import com.saas.springbackend.payment.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentResponseDto {

    private Long paymentId;

    private Long subscriptionId;

    private BigDecimal amount;

    private String currency;

    private String gatewayOrderId;

    private PaymentStatus status;
}
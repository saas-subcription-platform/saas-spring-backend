package com.saas.springbackend.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailureRequestDto {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String failureReason;
}
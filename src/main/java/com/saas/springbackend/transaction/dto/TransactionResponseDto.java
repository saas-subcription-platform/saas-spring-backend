package com.saas.springbackend.transaction.dto;

import com.saas.springbackend.transaction.entity.PaymentMethod;
import com.saas.springbackend.transaction.entity.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {

    private Long transactionId;

    private Long paymentId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private TransactionStatus status;

    private String gatewayPaymentId;
}

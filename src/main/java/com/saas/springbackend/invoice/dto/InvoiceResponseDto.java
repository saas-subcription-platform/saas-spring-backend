package com.saas.springbackend.invoice.dto;

import com.saas.springbackend.invoice.entity.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponseDto {

    private Long invoiceId;

    private String invoiceNumber;

    private Long paymentId;

    private LocalDate billingStartDate;

    private LocalDate billingEndDate;

    private BigDecimal subtotal;

    private BigDecimal cgst;

    private BigDecimal sgst;

    private BigDecimal totalAmount;

    private InvoiceStatus status;
}
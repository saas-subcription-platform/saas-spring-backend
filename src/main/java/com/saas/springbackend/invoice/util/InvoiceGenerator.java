package com.saas.springbackend.invoice.util;

import com.saas.springbackend.invoice.entity.Invoice;
import com.saas.springbackend.invoice.entity.InvoiceStatus;
import com.saas.springbackend.payment.entity.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class InvoiceGenerator {

    public Invoice generate(Payment payment) {

        BigDecimal totalAmount = payment.getAmount();

        BigDecimal subtotal = totalAmount.divide(
                new BigDecimal("1.18"),
                2,
                RoundingMode.HALF_UP
        );

        BigDecimal totalGst = totalAmount.subtract(subtotal);

        BigDecimal cgst = totalGst.divide(
                new BigDecimal("2"),
                2,
                RoundingMode.HALF_UP
        );

        BigDecimal sgst = totalGst.subtract(cgst);

        String invoiceNumber =
                "INV-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        return Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .billingStartDate(LocalDate.now())
                .billingEndDate(LocalDate.now().plusMonths(1))
                .subtotal(subtotal)
                .cgst(cgst)
                .sgst(sgst)
                .totalAmount(totalAmount)
                .status(InvoiceStatus.PAID)
                .payment(payment)
                .build();
    }
}
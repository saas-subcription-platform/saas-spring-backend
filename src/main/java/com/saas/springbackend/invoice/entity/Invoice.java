package com.saas.springbackend.invoice.entity;

import com.saas.springbackend.common.entity.BaseClass;
import com.saas.springbackend.payment.entity.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "invoices")
@AttributeOverride(
        name = "id",
        column = @Column(name = "invoice_id")
)
public class Invoice extends BaseClass {

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "billing_start_date", nullable = false)
    private LocalDate billingStartDate;

    @Column(name = "billing_end_date", nullable = false)
    private LocalDate billingEndDate;

    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cgst;

    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sgst;

    @Positive
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    //Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;
}
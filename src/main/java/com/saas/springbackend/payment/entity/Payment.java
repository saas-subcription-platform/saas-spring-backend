package com.saas.springbackend.payment.entity;

import com.saas.springbackend.common.entity.BaseClass;
import com.saas.springbackend.invoice.entity.Invoice;
import com.saas.springbackend.transaction.entity.PaymentMethod;
import com.saas.springbackend.transaction.entity.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "payments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(
        name = "id",
        column = @Column(name = "payment_id")
)
public class Payment extends BaseClass {

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    //Enums
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    //Razorpay id
    @Column(name = "gateway_order_id", unique = true, nullable = false)
    private String gatewayOrderId;

    //Relationships
    @OneToMany(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Transaction> transactions;

    @OneToOne(
            mappedBy = "payment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Invoice invoice;
}


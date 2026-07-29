package com.saas.springbackend.invoice.repository;

import com.saas.springbackend.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByPaymentId(Long paymentId);

    List<Invoice> findByPaymentSubscriptionId(Long subscriptionId);

    Optional<Invoice> findByIdAndPaymentSubscriptionId(
            Long id,
            Long subscriptionId
    );
}
package com.saas.springbackend.invoice.repository;

import com.saas.springbackend.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}

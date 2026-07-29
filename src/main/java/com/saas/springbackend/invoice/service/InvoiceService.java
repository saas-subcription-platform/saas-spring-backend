package com.saas.springbackend.invoice.service;

import com.saas.springbackend.common.exception.DuplicateInvoiceException;
import com.saas.springbackend.common.exception.InvoiceNotFoundException;
import com.saas.springbackend.invoice.dto.InvoiceResponseDto;
import com.saas.springbackend.invoice.entity.Invoice;
import com.saas.springbackend.invoice.repository.InvoiceRepository;
import com.saas.springbackend.invoice.util.InvoiceGenerator;
import com.saas.springbackend.invoice.util.InvoicePdfGenerator;
import com.saas.springbackend.payment.entity.Payment;
import com.saas.springbackend.subscription.entity.Subscription;
import com.saas.springbackend.subscription.repositories.SubscriptionRepository;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final InvoicePdfGenerator invoicePdfGenerator;
    private final InvoiceGenerator invoiceGenerator;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public List<InvoiceResponseDto> getAllInvoices() {

        Long subscriptionId = getCurrentCompanySubscriptionId();

        List<Invoice> invoices =
                invoiceRepository.findByPaymentSubscriptionId(subscriptionId);

        return invoices.stream()
                .map(invoice -> {

                    InvoiceResponseDto dto =
                            modelMapper.map(invoice, InvoiceResponseDto.class);

                    dto.setInvoiceId(invoice.getId());
                    dto.setPaymentId(invoice.getPayment().getId());

                    return dto;
                })
                .toList();
    }

    public InvoiceResponseDto getInvoiceById(Long id) {

        Long subscriptionId = getCurrentCompanySubscriptionId();

        Invoice invoice = invoiceRepository
                .findByIdAndPaymentSubscriptionId(id, subscriptionId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException(
                                "Invoice not found with id: " + id
                        )
                );

        InvoiceResponseDto dto =
                modelMapper.map(invoice, InvoiceResponseDto.class);

        dto.setInvoiceId(invoice.getId());
        dto.setPaymentId(invoice.getPayment().getId());

        return dto;
    }

    public InvoiceResponseDto createInvoice(Payment payment) {

        if (invoiceRepository.findByPaymentId(payment.getId()).isPresent()) {
            throw new DuplicateInvoiceException(
                    "Invoice already exists for payment: " + payment.getId()
            );
        }

        Invoice invoice = invoiceGenerator.generate(payment);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        InvoiceResponseDto dto =
                modelMapper.map(savedInvoice, InvoiceResponseDto.class);

        dto.setInvoiceId(savedInvoice.getId());
        dto.setPaymentId(savedInvoice.getPayment().getId());

        return dto;
    }

    public byte[] downloadInvoice(Long id) {

        Long subscriptionId = getCurrentCompanySubscriptionId();

        Invoice invoice = invoiceRepository
                .findByIdAndPaymentSubscriptionId(id, subscriptionId)
                .orElseThrow(() ->
                        new InvoiceNotFoundException(
                                "Invoice not found with id: " + id
                        )
                );

        return invoicePdfGenerator.generate(invoice);
    }

    private Long getCurrentCompanySubscriptionId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Long companyId = user.getCompany().getId();

        Subscription subscription =
                subscriptionRepository.findByCompanyId(companyId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subscription not found for company"
                                )
                        );

        return subscription.getId();
    }
}
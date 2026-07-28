package com.saas.springbackend.invoice.service;

import com.saas.springbackend.invoice.dto.InvoiceResponseDto;
import com.saas.springbackend.invoice.entity.Invoice;
import com.saas.springbackend.invoice.entity.InvoiceStatus;
import com.saas.springbackend.invoice.repository.InvoiceRepository;
import com.saas.springbackend.invoice.util.InvoiceGenerator;
import com.saas.springbackend.invoice.util.InvoicePdfGenerator;
import com.saas.springbackend.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final InvoicePdfGenerator invoicePdfGenerator;
    private final InvoiceGenerator invoiceGenerator;

    public List<InvoiceResponseDto> getAllInvoices() {

        List<Invoice> invoices = invoiceRepository.findAll();

        return invoices.stream()
                .map(invoice -> {

                    InvoiceResponseDto dto = modelMapper.map(invoice, InvoiceResponseDto.class);

                    dto.setInvoiceId(invoice.getId());
                    dto.setPaymentId(invoice.getPayment().getId());

                    return dto;
                })
                .toList();
    }


    public InvoiceResponseDto getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        InvoiceResponseDto dto = modelMapper.map(invoice, InvoiceResponseDto.class);

        dto.setInvoiceId(invoice.getId());
        dto.setPaymentId(invoice.getPayment().getId());

        return dto;
    }

    public InvoiceResponseDto createInvoice(Payment payment) {

        if (invoiceRepository.findByPaymentId(payment.getId()).isPresent()) {
            throw new RuntimeException("Invoice already exists for this payment");
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

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return invoicePdfGenerator.generate(invoice);
    }
}

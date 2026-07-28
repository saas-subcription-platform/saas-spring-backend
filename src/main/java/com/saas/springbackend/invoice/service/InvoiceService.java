package com.saas.springbackend.invoice.service;

import com.saas.springbackend.common.exception.DuplicateInvoiceException;
import com.saas.springbackend.common.exception.InvoiceNotFoundException;
import com.saas.springbackend.invoice.dto.InvoiceResponseDto;
import com.saas.springbackend.invoice.entity.Invoice;
import com.saas.springbackend.invoice.repository.InvoiceRepository;
import com.saas.springbackend.invoice.util.InvoiceGenerator;
import com.saas.springbackend.invoice.util.InvoicePdfGenerator;
import com.saas.springbackend.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found with id: " + id));

        InvoiceResponseDto dto = modelMapper.map(invoice, InvoiceResponseDto.class);

        dto.setInvoiceId(invoice.getId());
        dto.setPaymentId(invoice.getPayment().getId());

        return dto;
    }

    public InvoiceResponseDto createInvoice(Payment payment) {

        if (invoiceRepository.findByPaymentId(payment.getId()).isPresent()) {
            throw new DuplicateInvoiceException(
                    "Invoice already exists for payment: " + payment.getId());
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
                .orElseThrow(() ->
                        new InvoiceNotFoundException("Invoice not found with id: " + id));

        return invoicePdfGenerator.generate(invoice);
    }
}

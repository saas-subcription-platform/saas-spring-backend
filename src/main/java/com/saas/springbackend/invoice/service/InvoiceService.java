package com.saas.springbackend.invoice.service;

import com.saas.springbackend.invoice.dto.InvoiceResponseDto;
import com.saas.springbackend.invoice.entity.Invoice;
import com.saas.springbackend.invoice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;

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
}

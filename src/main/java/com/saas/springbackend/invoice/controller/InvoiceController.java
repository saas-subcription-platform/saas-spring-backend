package com.saas.springbackend.invoice.controller;

import com.saas.springbackend.invoice.dto.GenerateInvoiceRequestDto;
import com.saas.springbackend.invoice.dto.InvoiceResponseDto;
import com.saas.springbackend.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    //Get all invoices
    @GetMapping
    public ResponseEntity<?> getAllInvoices() {

        List<InvoiceResponseDto> invoices = invoiceService.getAllInvoices();

        return ResponseEntity.ok(invoices);
    }

    //Get invoice by Id
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(
            @PathVariable Long id) {

        InvoiceResponseDto invoice = invoiceService.getInvoiceById(id);

        return ResponseEntity.ok(invoice);
    }

}

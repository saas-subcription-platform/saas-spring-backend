package com.saas.springbackend.invoice.controller;

import com.saas.springbackend.invoice.dto.InvoiceResponseDto;
import com.saas.springbackend.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    //Download invoice
    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadInvoice(@PathVariable Long id){

        byte[] pdf = invoiceService.downloadInvoice(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice-" + id + ".pdf")
                .body(pdf);
    }
}

package com.saas.springbackend.invoice.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.saas.springbackend.invoice.entity.Invoice;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class InvoicePdfGenerator {

    public byte[] generate(Invoice invoice) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();

            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.add(new Paragraph("INVOICE"));
            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Invoice Number: " + invoice.getInvoiceNumber()
            ));

            document.add(new Paragraph(
                    "Payment ID: " + invoice.getPayment().getId()
            ));

            document.add(new Paragraph(
                    "Billing Start Date: " + invoice.getBillingStartDate()
            ));

            document.add(new Paragraph(
                    "Billing End Date: " + invoice.getBillingEndDate()
            ));

            document.add(new Paragraph(
                    "Status: " + invoice.getStatus()
            ));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2);

            table.addCell("Description");
            table.addCell("Amount");

            table.addCell("Subtotal");
            table.addCell("Rs. " + invoice.getSubtotal());

            table.addCell("CGST");
            table.addCell("Rs. " + invoice.getCgst());

            table.addCell("SGST");
            table.addCell("Rs. " + invoice.getSgst());

            table.addCell("Total Amount");
            table.addCell("Rs. " + invoice.getTotalAmount());

            document.add(table);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }

        return outputStream.toByteArray();
    }
}
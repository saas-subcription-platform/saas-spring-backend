package com.saas.springbackend.invoice.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.saas.springbackend.invoice.entity.Invoice;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Component
public class InvoicePdfGenerator {

    public byte[] generate(Invoice invoice) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 11);
            Font boldFont = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new float[]{1, 1});

            addDetail(
                    detailsTable,
                    "Invoice Number",
                    invoice.getInvoiceNumber(),
                    boldFont,
                    normalFont
            );

            addDetail(
                    detailsTable,
                    "Payment ID",
                    String.valueOf(invoice.getPayment().getId()),
                    boldFont,
                    normalFont
            );

            addDetail(
                    detailsTable,
                    "Billing Start Date",
                    String.valueOf(invoice.getBillingStartDate()),
                    boldFont,
                    normalFont
            );

            addDetail(
                    detailsTable,
                    "Billing End Date",
                    String.valueOf(invoice.getBillingEndDate()),
                    boldFont,
                    normalFont
            );

            addDetail(
                    detailsTable,
                    "Status",
                    String.valueOf(invoice.getStatus()),
                    boldFont,
                    normalFont
            );

            document.add(detailsTable);

            Paragraph spacing = new Paragraph(" ");
            spacing.setSpacingAfter(10);
            document.add(spacing);

            PdfPTable amountTable = new PdfPTable(2);
            amountTable.setWidthPercentage(100);
            amountTable.setWidths(new float[]{3, 1});

            PdfPCell descriptionHeader =
                    new PdfPCell(new Phrase("Description", boldFont));

            descriptionHeader.setPadding(10);
            descriptionHeader.setBackgroundColor(new Color(240, 240, 240));

            PdfPCell amountHeader =
                    new PdfPCell(new Phrase("Amount", boldFont));

            amountHeader.setPadding(10);
            amountHeader.setBackgroundColor(new Color(240, 240, 240));
            amountHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);

            amountTable.addCell(descriptionHeader);
            amountTable.addCell(amountHeader);

            addAmountRow(
                    amountTable,
                    "Subtotal",
                    "Rs. " + invoice.getSubtotal(),
                    normalFont
            );

            addAmountRow(
                    amountTable,
                    "CGST",
                    "Rs. " + invoice.getCgst(),
                    normalFont
            );

            addAmountRow(
                    amountTable,
                    "SGST",
                    "Rs. " + invoice.getSgst(),
                    normalFont
            );

            addAmountRow(
                    amountTable,
                    "Total Amount",
                    "Rs. " + invoice.getTotalAmount(),
                    totalFont
            );

            document.add(amountTable);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }

        return outputStream.toByteArray();
    }

    private void addDetail(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font valueFont) {

        PdfPCell labelCell =
                new PdfPCell(new Phrase(label, labelFont));

        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(8);

        PdfPCell valueCell =
                new PdfPCell(new Phrase(value, valueFont));

        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(8);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addAmountRow(
            PdfPTable table,
            String description,
            String amount,
            Font font) {

        PdfPCell descriptionCell =
                new PdfPCell(new Phrase(description, font));

        descriptionCell.setPadding(10);

        PdfPCell amountCell =
                new PdfPCell(new Phrase(amount, font));

        amountCell.setPadding(10);
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        table.addCell(descriptionCell);
        table.addCell(amountCell);
    }
}
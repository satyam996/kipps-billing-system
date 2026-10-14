
package com.kipps.billingsystem.service;

import com.kipps.billingsystem.entity.bill.Bill;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class BillPdfService {

    public byte[] generatePdf(Bill bill) {

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, output);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

            Paragraph shopName =
                    new Paragraph("H G SWEETS (KIPPS)", titleFont);
            shopName.setAlignment(Element.ALIGN_CENTER);
            document.add(shopName);

            Paragraph heading = new Paragraph("INVOICE", boldFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            document.add(new Paragraph("Bill No: " + bill.getId(), normalFont));
            document.add(new Paragraph(
                    "Date: " + bill.getBillDate(), normalFont));
            document.add(new Paragraph(" ", normalFont));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1.5f, 2, 1.5f, 2.5f});

            addCell(table, "Item", boldFont);
            addCell(table, "Qty", boldFont);
            addCell(table, "Rate", boldFont);
            addCell(table, "GST %", boldFont);
            addCell(table, "Amount", boldFont);

            for (var item : bill.getItems()) {
                addCell(table, item.getProduct().getName(), normalFont);
                addCell(table, item.getQuantity().toPlainString(), normalFont);
                addCell(table, item.getPrice().toPlainString(), normalFont);
                addCell(table, item.getGstPercentage().toPlainString(), normalFont);
                addCell(table, item.getTotal().toPlainString(), normalFont);
            }

            document.add(table);
            document.add(new Paragraph(" ", normalFont));

            addTotal(document, "Subtotal", bill.getSubtotal(), normalFont);
            addTotal(document, "GST", bill.getGstAmount(), normalFont);
            addTotal(document, "GRAND TOTAL", bill.getGrandTotal(), boldFont);

            Paragraph footer =
                    new Paragraph("Thanks! Visit Again!", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(" ", normalFont));
            document.add(footer);

            document.close();

            return output.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Could not generate bill PDF", e);
        }
    }

    private void addCell(PdfPTable table, String text, Font font) {
        table.addCell(new Phrase(text, font));
    }

    private void addTotal(
            Document document,
            String label,
            BigDecimal amount,
            Font font) throws com.lowagie.text.DocumentException {

        Paragraph line = new Paragraph(
                label + ": Rs. " + amount.toPlainString(), font);
        line.setAlignment(Element.ALIGN_RIGHT);
        document.add(line);
    }
}

package com.sk.system.util;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;

public class PDFGenerator {

    public static void generateAnnouncementPDF(String title, String content, String date, String filePath) {
        try {
            // Create a writer
            PdfWriter writer = new PdfWriter(new File(filePath));
            // Create a PDF document
            PdfDocument pdf = new PdfDocument(writer);
            // Create a document
            Document document = new Document(pdf);

            // Add title
            Paragraph titlePara = new Paragraph("Announcement")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setMarginBottom(20);
            document.add(titlePara);

            // Add actual announcement title
            Paragraph header = new Paragraph(title)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10);
            document.add(header);

            // Add date
            Paragraph datePara = new Paragraph("Date: " + date)
                    .setFontSize(12)
                    .setMarginBottom(10);
            document.add(datePara);

            // Add content
            Paragraph contentPara = new Paragraph(content)
                    .setFontSize(12)
                    .setMarginBottom(20);
            document.add(contentPara);

            // Close the document
            document.close();

            System.out.println("PDF created at: " + filePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

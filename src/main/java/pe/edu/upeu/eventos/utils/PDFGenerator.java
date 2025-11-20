package pe.edu.upeu.gestioneventos.utils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PDFGenerator {

    public static byte[] generatePDF(String title, String[] headers, List<String[]> data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            document.add(new Paragraph(title).setBold().setFontSize(18));
            document.add(new Paragraph("\n"));

            // Tabla
            Table table = new Table(headers.length);
            
            // Headers
            for (String header : headers) {
                table.addHeaderCell(header);
            }

            // Data
            for (String[] row : data) {
                for (String cell : row) {
                    table.addCell(cell != null ? cell : "");
                }
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }
    }
}

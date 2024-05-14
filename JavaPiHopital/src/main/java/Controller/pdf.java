package Controller;

import Model.dossiermedical;
import Model.ordonnance;
import Service.dossiermedicalService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class pdf {
    public void GeneratePdf(String filename, dossiermedical dossier, int id) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filename + ".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        dossiermedicalService dossierService = new dossiermedicalService();
        document.add(new Paragraph("Dossier Medical Details").setFontSize(20).setBold());
        document.add(new Paragraph("Date: " + LocalDateTime.now()));
        document.add(new Paragraph("Dossier medical: " + dossier.getId()));
        document.add(new Paragraph("Antécédents personnelles: " + dossier.getAntecedentspersonelles()));
        ;
        document.add(new Paragraph("Résultas Examens: " + dossier.getResultatexamen()));

        document.close();

    }
    public void GeneratePdfO(String filename, ordonnance ordonnance, int id) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filename + ".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        dossiermedicalService dossierService = new dossiermedicalService();
        document.add(new Paragraph("Ordonnance Details").setFontSize(20).setBold());
        document.add(new Paragraph("Date: " + LocalDateTime.now()));
        document.add(new Paragraph("Ordonnance: " + ordonnance.getId()));
        document.add(new Paragraph("Médicaments préscrits: " + ordonnance.getMedecamentprescrit()));
        ;
        document.add(new Paragraph("Date renouvellment: " + ordonnance.getRenouvellement()));

        document.close();

    }
}


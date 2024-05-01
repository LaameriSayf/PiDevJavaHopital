package Controllers;
import Models.dossiermedical;
import Models.ordonnance;
import Services.dossiermedicalService;
import Services.ordonnanceService;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
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
        document.add(new Paragraph("Antécédents personnelles: " + dossier.getAntecedentspersonelles()));;
        document.add(new Paragraph("Résultas Examens: " + dossier.getResultatexamen()));

        document.close();

    }
    public void GeneratePdf(String filename, ordonnance ordonnance, int id) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filename + ".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

         ordonnance  ordo = new ordonnance();
        document.add(new Paragraph("ordonnance Details").setFontSize(20).setBold());
        document.add(new Paragraph("Date: " + LocalDateTime.now()));
        document.add(new Paragraph("Date de renouvellement : " + ordo.getRenouvellement() ));
        document.add(new Paragraph("Dossier medical: " + ordo.getId()));
        document.add(new Paragraph("Medicaments prescrits: " + ordo.getMedecamentprescrit()));
        document.add(new Paragraph("Résultas Examens: " + ordo.getAdresse()));


        document.close();

    }


}
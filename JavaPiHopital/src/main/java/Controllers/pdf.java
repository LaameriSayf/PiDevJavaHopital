package Controllers;
import Models.dossiermedical;
import Models.ordonnance;
import Controllers.DigitalSignature;
import Services.dossiermedicalService;
import Services.ordonnanceService;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
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
}



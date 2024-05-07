package Controllers;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class sign_pdf {
    public void signPdf(String pdfFile, String signedPdfFile, String privateKeyFile, String privateKeyPassword) {
//        PdfDocument pdfDoc = null;
//        try {
//            // Charger la clé privée
//            DigitalSignature signatureHelper = new DigitalSignature();
//            PrivateKey privateKey = signatureHelper.privateKeyFromJKS(privateKeyFile, privateKeyPassword, "noura");
//            PublicKey publicKey = signatureHelper.publicKeyFromCertificate("certificate.cert");
//
//            // Ouvrir le document PDF
//            PdfReader reader = new PdfReader(pdfFile);
//            FileOutputStream outputStream = new FileOutputStream(signedPdfFile);
//            PdfWriter writer = new PdfWriter(outputStream);
//            pdfDoc = new PdfDocument(reader, writer);
//
//            // Initialiser PdfSigner
//            PdfSigner signer = new PdfSigner(reader, outputStream, false);  // Remove the isAppendMode flag
//
//
//            // Créer l'apparence de la signature
//            PdfSignatureAppearance appearance = signer.getSignatureAppearance();
//            appearance.setReason("Test SignPDF");
//            appearance.setLocation("Location");
//            appearance.setPageNumber(1);
//
//            // Configurer la signature
//            IExternalSignature pks = new PrivateKeySignature(privateKey, "SHA-256", BouncyCastleProvider.PROVIDER_NAME);
//            IExternalDigest digest = new BouncyCastleDigest();
//            signer.signDetached(digest, pks, new Certificate[]{ /* Ajoutez votre certificat si nécessaire */}, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (pdfDoc != null) {
//                pdfDoc.close();
//            }
//        }
    }}



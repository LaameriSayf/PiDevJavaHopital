package Controller;

import Model.ordonnance;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelWriter {
    public void writeOrdonnancesToExcel(List<ordonnance> ordonnanceList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ordonnances");

            // En-têtes de colonnes
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Date Prescription");
            headerRow.createCell(2).setCellValue("Renouvellement");
            headerRow.createCell(3).setCellValue("Médicament Prescrit");
            headerRow.createCell(4).setCellValue("Adresse");

            int rowNum = 1;
            for (ordonnance ord : ordonnanceList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(ord.getId());
                row.createCell(1).setCellValue(ord.getDateprescription().toString());
                row.createCell(2).setCellValue(ord.getRenouvellement().toString());
                row.createCell(3).setCellValue(ord.getMedecamentprescrit());
                row.createCell(4).setCellValue(ord.getAdresse());
                // Ajoutez d'autres colonnes si nécessaire
            }

            // Enregistrement du fichier Excel
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("Fichier Excel créé avec succès : " + filePath);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'enregistrement du fichier Excel : " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier Excel : " + e.getMessage());
        }
    }

}

package Controllers;

import Models.dossiermedical;
import Services.dossiermedicalService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

public class OCRController {
    @FXML
    private TextArea ocrTextArea;

    @FXML
    private ImageView imageView;
    @FXML
    private TextField resultatExamenField;
    @FXML
    private TextField dateCreationField;
    @FXML
    private TextField antecedentsPersonellesField;
    @FXML
    private TextField imageField;
    @FXML
    private TextField patientIdField;
    @FXML
    private TextField numDossierField;

    private dossiermedicalService dossierService = new dossiermedicalService();

    public void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) resultatExamenField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            // Perform OCR on the selected image file
            ITesseract tess = new Tesseract();
            tess.setLanguage("eng"); // Specify the language
            tess.setDatapath("C:\\Users\\noura\\Desktop\\tessdata-main");

            try {
                String extractedText = tess.doOCR(selectedFile);
                ocrTextArea.setText(extractedText);
                // Extract relevant information from the extracted text and populate the fields
                dossiermedical newDossier = parseOCRText(extractedText);
                populateFields(newDossier);

            } catch (TesseractException e) {
                e.printStackTrace();
            }
        }
    }

    private dossiermedical parseOCRText(String text) {
        // Perform parsing of the OCR text and create a dossiermedical instance
        // Here's a sample implementation assuming the text format is consistent:
        String[] lines = text.split("\n");

        // Assuming the lines contain the information in a specific order
        String resultatExamen = lines[0];
        String dateCreationStr = lines[1]; // Assuming the date format is in a recognizable format
        Date dateCreation = parseDate(dateCreationStr);
        String antecedentsPersonelles = lines[2];
        String image = ""; // You might need additional logic to extract image path
        int patientId = 1; // Assuming a default patient ID
        int numdossier = 1; // Assuming a default dossier number

        return new dossiermedical(0, resultatExamen, dateCreation, antecedentsPersonelles, image, patientId, numdossier);
    }

    private Date parseDate(String dateStr) {
        // Implement logic to parse date string into a Date object
        // This is just a placeholder implementation, you should replace it with your own logic
        return new Date();
    }

    private void populateFields(dossiermedical dossier) {
        // Populate the text fields with the data from the dossiermedical object
        resultatExamenField.setText(dossier.getResultatexamen());
        dateCreationField.setText(dossier.getDate_creation().toString());
        antecedentsPersonellesField.setText(dossier.getAntecedentspersonelles());
        imageField.setText(dossier.getImage());
        patientIdField.setText(String.valueOf(dossier.getPatient_id()));
        numDossierField.setText(String.valueOf(dossier.getNumdossier()));
    }

    public void saveData(ActionEvent event) {
        // Retrieve data from text fields and save it into the database
        String resultatExamen = resultatExamenField.getText();
        // Assuming dateCreationField contains a valid date string
        Date dateCreation = parseDate(dateCreationField.getText());
        String antecedentsPersonelles = antecedentsPersonellesField.getText();
        String image = imageField.getText();
        int patientId = Integer.parseInt(patientIdField.getText());
        int numdossier = Integer.parseInt(numDossierField.getText());

        dossiermedical newDossier = new dossiermedical(10, resultatExamen, dateCreation, antecedentsPersonelles, image, patientId, numdossier);

        try {
            dossierService.ajouter(newDossier);
            showNotification("Dossier ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database insertion error
        }
    }
    private void showNotification(String message) {
        Notifications.create()
                .title("Succès")
                .text(message)
                .darkStyle()  // Style sombre
                .hideAfter(Duration.seconds(20))  // Durée d'affichage de 20 secondes
                .position(Pos.CENTER)  // Position au milieu de la page
                .showInformation();
    }


}



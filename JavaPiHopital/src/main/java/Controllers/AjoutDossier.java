package Controllers;

import Models.Patient;
import Models.dossiermedical;
import Services.dossiermedicalService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjoutDossier implements Initializable {
    private dossiermedicalService dossierService = new dossiermedicalService();
    private String imageFilePath;

    @FXML
    private TextArea resultatExamenField;

    @FXML
    private TextArea antecedentsField;

    @FXML
    private DatePicker dateCreationPicker;

    @FXML
    private Button chooseImageButton;

    @FXML
    private Button ajouterButton;

    @FXML
    private Text imagePathText;
    @FXML
    private Patient patient;

    @FXML
    private ImageView imageView;
    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageFilePath = selectedFile.getAbsolutePath();
            imagePathText.setText(imageFilePath);

            try {
                // Charger l'image à partir du chemin sélectionné
                Image selectedImage = new Image(selectedFile.toURI().toString());
                imageView.setImage(selectedImage);
            } catch (Exception e) {
                afficherAlerteErreur("Erreur lors du chargement de l'image : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private void ajouterDossier() {
        LocalDate dateCreation = dateCreationPicker.getValue();
        String resultatExamen = resultatExamenField.getText();
        String antecedents = antecedentsField.getText();

        if (dateCreation == null || resultatExamen.isEmpty() || antecedents.isEmpty() || imageFilePath == null) {
            afficherAlerteErreur("Veuillez remplir tous les champs.");
            return;
        }

        if (!isValidImageType(imageFilePath)) {
            afficherAlerteErreur("Veuillez sélectionner une image de type JPG, JPEG, PNG ou GIF.");
            return;
        }

        Timestamp timestamp = Timestamp.valueOf(dateCreation.atStartOfDay());

        dossiermedical dossier = new dossiermedical();
        dossier.setDate_creation(timestamp);
        dossier.setResultatexamen(resultatExamen);
        dossier.setAntecedentspersonelles(antecedents);
        dossier.setImage(imageFilePath);

        try {
            dossierService.ajouter(dossier);
            afficherAlerteInformation("Dossier ajouté avec succès !");
        } catch (SQLException e) {
            afficherAlerteErreur("Erreur lors de l'ajout du dossier médical : " + e.getMessage());
        }
    }





    private boolean isValidImageType(String imagePath) {
        String extension = imagePath.substring(imagePath.lastIndexOf(".")).toLowerCase();
        return extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".gif");
    }

    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherAlerteInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dateCreationPicker.setValue(LocalDate.now());
        chooseImageButton.setOnAction(event -> chooseImage());
        ajouterButton.setOnAction(event -> ajouterDossier());


    }

}

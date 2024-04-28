package Controllers;

import Models.dossiermedical;
import Services.dossiermedicalService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateDossierMedical implements Initializable {
    private dossiermedicalService dossierService = new dossiermedicalService();
    private String imageFilePath;

    @FXML
    private TextField resultatExamenField;

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
    private ImageView imageView;

    private dossiermedical dossierModifier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ajoutez vos initialisations ici
        dateCreationPicker.setValue(LocalDate.now()); // Par exemple, initialisez la date à aujourd'hui
    }

    public void setDossierModifier(dossiermedical dossier) {
        this.dossierModifier = dossier;
        if (dossierModifier != null) {
            resultatExamenField.setText(dossierModifier.getResultatexamen());
            antecedentsField.setText(dossierModifier.getAntecedentspersonelles());

            // Vérifiez si le chemin de l'image existe dans le dossier
            String imagePath = dossierModifier.getImage(); // Assurez-vous que votre modèle possède un champ pour le chemin de l'image

            if (imagePath != null && !imagePath.isEmpty()) {
                // Mettez à jour l'ImageView avec l'image correspondante
                imageView.setImage(new Image(new File(imagePath).toURI().toString()));
            } else {
                // Effacez le texte du chemin de l'image s'il n'y a pas d'image
                imagePathText.setText("");
            }
        }
    }



    @FXML
    private void modifierDossier() {
        if (dossierModifier != null) {
            dossierModifier.setResultatexamen(resultatExamenField.getText());
            dossierModifier.setAntecedentspersonelles(antecedentsField.getText());
            dossierModifier.setImage(imageFilePath); // Mettre à jour le chemin de l'image

            try {
                dossierService.modifier(dossierModifier);
                // Afficher une alerte pour indiquer que le dossier a été modifié avec succès
                afficherAlerteInformation("Le dossier a été modifié avec succès.");
            } catch (SQLException e) {
                // Afficher une alerte en cas d'erreur lors de la modification du dossier
                afficherAlerteErreur("Une erreur est survenue lors de la modification du dossier : " + e.getMessage());
            }
        }
    }






    @FXML
    private void clearFields() {
        resultatExamenField.clear();
        antecedentsField.clear();
        dateCreationPicker.setValue(LocalDate.now());
        imagePathText.setText("");
        imageView.setImage(null);
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imageFilePath = selectedFile.getAbsolutePath();
            imagePathText.setText(imageFilePath);
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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





}

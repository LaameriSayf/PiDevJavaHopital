package Controllers;

import Models.dossiermedical;
import Services.dossiermedicalService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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

    @FXML
    private Button modifierDossier;

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
                // Modification réussie, afficher la notification
                showNotification("Dossier Modifié avec succès");
            } catch (SQLException e) {
                // Afficher une alerte en cas d'erreur lors de la modification du dossier
                afficherErreur("Une erreur est survenue lors de la modification du dossier : " + e.getMessage());
            }
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

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






}

package Controller;

import Model.Patient;
import Model.dossiermedical;
import Service.dossiermedicalService;
import Test.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
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
    private TextArea antecedentspersonnellesField;

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
    private TextField numdossierField ;
    @FXML
    private Button dosBtn;
    @FXML
    private Button logout_btn;
    @FXML
    private Button ordBnt;

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
        String antecedents = antecedentspersonnellesField.getText();
        String numdossierText = numdossierField.getText().trim();

        // Vérifier si le champ numdossier est vide
        if (numdossierText.isEmpty()) {
            afficherAlerteErreur("Veuillez saisir un numéro de dossier.");
            return;
        }

        Integer numdossier;
        try {
            numdossier = Integer.parseInt(numdossierText);
        } catch (NumberFormatException e) {
            afficherAlerteErreur("Le numéro de dossier doit être un nombre entier.");
            return;
        }

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
        dossier.setNumdossier(numdossier); // Assurez-vous que setNumdossier accepte un Integer
        dossier.setAntecedentspersonelles(antecedents);
        dossier.setImage(imageFilePath);


        try {
            dossierService.ajouter(dossier);
            showNotification("Dossier ajouté avec succès !");
        } catch (SQLException e) {
            afficherAlerteErreur("Erreur lors de l'ajout du dossier médical : " + e.getMessage());
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
    @FXML
    void dos(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AllDossier.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  dosBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/login.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ord(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/AllOrdonnace.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage)  ordBnt.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
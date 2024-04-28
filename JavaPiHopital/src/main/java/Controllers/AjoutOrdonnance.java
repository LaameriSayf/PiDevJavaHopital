package Controllers;

import Models.dossiermedical;
import Models.ordonnance;
import Services.ordonnanceService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjoutOrdonnance implements Initializable {
    private ordonnanceService service = new ordonnanceService();
    private dossiermedical selectedDossier;

    @FXML
    private DatePicker datePrescriptionPicker;

    @FXML
    private DatePicker renouvellementPicker;

    @FXML
    private TextArea medecamentField;

    @FXML
    private TextField adresseField;

    // Méthode pour définir le dossier médical sélectionné
    public void setSelectedDossier(dossiermedical dossier) {
        this.selectedDossier = dossier;
    }

    @FXML
    private void ajouterOrdonnance() {
        // Récupérer les valeurs saisies dans les champs
        LocalDate datePrescription = datePrescriptionPicker.getValue();
        LocalDate renouvellement = renouvellementPicker.getValue();
        String medecament = medecamentField.getText();
        String adresse = adresseField.getText();

        // Vérifier si les champs sont remplis
        if (datePrescription == null || renouvellement == null || medecament.isEmpty() || adresse.isEmpty()) {
            // Afficher une alerte si tous les champs ne sont pas remplis
            afficherAlerteErreur("Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si la date de renouvellement est supérieure à la date de prescription
        if (renouvellement.isBefore(datePrescription)) {
            // Afficher une alerte si la date de renouvellement est antérieure à la date de prescription
            afficherAlerteErreur("La date de renouvellement doit être ultérieure à la date de prescription.");
            return;
        }

        // Convertir LocalDate en java.sql.Date
        java.sql.Date renouvellementSQL = java.sql.Date.valueOf(renouvellement);

        // Créer un objet ordonnance avec les données saisies
        ordonnance ordonnance = new ordonnance();
        ordonnance.setDateprescription(java.sql.Date.valueOf(datePrescription));
        ordonnance.setRenouvellement(renouvellementSQL);
        ordonnance.setMedecamentprescrit(medecament);
        ordonnance.setAdresse(adresse);

        // Définir l'ID du dossier médical à partir du dossier sélectionné
        ordonnance.setDossiermedical_id(selectedDossier.getId());

        try {
            // Appeler la méthode pour ajouter l'ordonnance avec l'ID du dossier médical correspondant
            service.ajouter(ordonnance); // Passer l'ID du dossier médical

            // Afficher une alerte pour indiquer que l'ordonnance a été ajoutée avec succès
            afficherAlerteInformation("L'ordonnance a été ajoutée avec succès.");
        } catch (SQLException e) {
            // Afficher une alerte en cas d'erreur lors de l'ajout de l'ordonnance
            afficherAlerteErreur("Une erreur est survenue lors de l'ajout de l'ordonnance : " + e.getMessage());
        }
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Définir la date de prescription sur la date actuelle
        datePrescriptionPicker.setValue(LocalDate.now());
    }

}

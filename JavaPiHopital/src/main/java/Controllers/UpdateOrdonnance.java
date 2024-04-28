package Controllers;

import Models.ordonnance;
import Services.ordonnanceService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateOrdonnance implements Initializable {
    private ordonnanceService ordonnanceService = new ordonnanceService();
    private ordonnance ordonnanceModifier;

    @FXML
    private TextField medecamentPrescritField;

    @FXML
    private TextArea adresseField;

    @FXML
    private DatePicker renouvellementField;

    @FXML
    private Button modifierButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setOrdonnanceModifier(ordonnance ordonnance) {
        this.ordonnanceModifier = ordonnance;
        if (ordonnanceModifier != null) {
            medecamentPrescritField.setText(ordonnanceModifier.getMedecamentprescrit());
            adresseField.setText(ordonnanceModifier.getAdresse());

            // Convertir la date de renouvellement de java.sql.Date en LocalDate
            java.sql.Date dateRenouvellement = (java.sql.Date) ordonnanceModifier.getRenouvellement();
            if (dateRenouvellement != null) {
                // Convertir la date SQL en LocalDate
                LocalDate localDateRenouvellement = dateRenouvellement.toLocalDate();
                renouvellementField.setValue(localDateRenouvellement);
            }
        }
    }


    @FXML
    private void modifierOrdonnance() {
        if (ordonnanceModifier != null) {
            ordonnanceModifier.setMedecamentprescrit(medecamentPrescritField.getText());
            ordonnanceModifier.setAdresse(adresseField.getText());

            // Convertir LocalDate en java.sql.Date
            LocalDate localDateRenouvellement = renouvellementField.getValue();
            if (localDateRenouvellement != null) {
                java.sql.Date sqlDateRenouvellement = java.sql.Date.valueOf(localDateRenouvellement);
                ordonnanceModifier.setRenouvellement(sqlDateRenouvellement);
            }

            // Appel à la fonction de modification dans le service
            try {
                ordonnanceService.modifier(ordonnanceModifier);
                // Afficher une alerte pour indiquer que l'ordonnance a été modifiée avec succès
                // afficherAlerteInformation("L'ordonnance a été modifiée avec succès.");
            } catch (SQLException e) {
                // Afficher une alerte en cas d'erreur lors de la modification de l'ordonnance
                // afficherAlerteErreur("Une erreur est survenue lors de la modification de l'ordonnance : " + e.getMessage());
            }
        }
    }


    @FXML
    private void clearFields() {
        medecamentPrescritField.clear();
        adresseField.clear();
        renouvellementField.setValue(null);  // Effacement de la date de renouvellement sélectionnée
        // Effacer d'autres champs si nécessaire
    }



}
